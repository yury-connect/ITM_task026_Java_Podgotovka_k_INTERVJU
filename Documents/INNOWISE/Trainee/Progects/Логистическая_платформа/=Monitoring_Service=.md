Получаем **единую точку входа** (Grafana), где можно:
- смотреть графики **метрик** (из Prometheus)    
- искать и анализировать **логи** (из Loki)    
- и связывать их между собой (например, кликнуть на ошибку в метрике и сразу увидеть логи этого сервиса)    
Это называется **Full-Stack Observability**

---
### Компоненты и их задачи

|Компонент|Задача|Как работает|
|---|---|---|
|**Prometheus**|Сбор и хранение **метрик** (CPU, память, кол-во запросов, длительность ответов)|Ваши микросервисы отдают метрики на эндпоинте `/actuator/prometheus` (Spring Boot Actuator), а Prometheus их периодически "выкачивает" [](https://github.com/takeshi-su57/luckyplans/issues/51).|
|**Loki**|Хранение **логов** (текстовых сообщений)|Логи поступают в Loki от специального агента — Promtail [](https://grafana.com/docs/enterprise-logs/latest/send-data/).|
|**Promtail**|Сбор и доставка логов в Loki|Promtail запускается рядом с вашими сервисами (или как sidecar-контейнер), читает файлы логов (или stdout) и отправляет их в Loki [](https://github.com/Raj-Manghani/prom-graf-loki-monitoring-stack)[](https://grafana.com/docs/enterprise-logs/latest/send-data/).|
|**Grafana**|Визуализация **и метрик, и логов**|Вы подключаете в Grafana два источника данных: **Prometheus** (для метрик) и **Loki** (для логов) [](https://documentation.ubuntu.com/lxd/default/howto/grafana/#grafana).|

---
### Архитектура метрик и логирования
```text
Каждый микросервис
├── (1) отдает метрики на /actuator/prometheus
└── (2) пишет логи в консоль (stdout) или в файл

Prometheus ──(выкачивает метрики)──>  Каждый микросервис
Promtail   ──(читает логи)────────>  Каждый микросервис (логи из файлов/контейнера)

Prometheus ──(отдает метрики)──> Grafana (DataSource: Prometheus)
Promtail   ──(отправляет логи)─> Loki ──(отдает логи)──> Grafana (DataSource: Loki)
```

---
## 🔗 Как связать метрики и логи в Grafana 
(важная фишка)

Чтобы из графика с ошибками можно было **кликнуть и перейти к логам**, настройте в Loki **derived fields**:
1. Зайдите в **Configuration → Data Sources → Loki**    
2. Добавьте производное поле (Derived Field) с именем `trace_id`    
3. Укажите regex для извлечения: `"trace_id":"(\w+)"`    
4. Настройте внутреннюю ссылку на источник данных Loki с запросом: `{container="$container"} |= "$__value"`    

**Что это даст:** в интерфейсе Explore вы сможете кликнуть на любую ошибку в метрике и мгновенно увидеть все логи, связанные с этим запросом.

---
## ⚠️ Важное примечание про Grafana Alloy 
(на будущее)

Согласно документации Grafana, **Promtail объявлен устаревшим (deprecated)** [](https://grafana.com/docs/enterprise-logs/latest/send-data/). Его поддержка полностью прекратится в **марте 2026 года**. На смену ему приходит **Grafana Alloy** — единый агент для метрик, логов и трейсов.

**Для вашего учебного проекта:** сейчас можно смело использовать Promtail — он работает стабильно, и в ближайшие месяцы ничего не сломается. Но в production-проектах или если захотите продвинутой конфигурации — лучше сразу смотреть в сторону Grafana Alloy.

---
## 📋 Что нужно сделать в Spring Boot микросервисах

Чтобы ваши сервисы отдавали метрики для Prometheus:
1. Добавьте в `build.gradle` или `pom.xml`:    
```gradle
implementation 'org.springframework.boot:spring-boot-starter-actuator'
implementation 'io.micrometer:micrometer-registry-prometheus'
```

2. В `application.yml`:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

3. Для структурированных логов (чтобы Loki лучше парсил):
```yaml
logging:
  pattern:
    console: '{"timestamp":"%d{ISO8601}","level":"%p","service":"${spring.application.name}","trace_id":"%X{trace_id}","message":"%m"}\n'
```

---
## 🚀 Запуск
```bash
# 1. Создайте структуру папок
mkdir -p prometheus loki promtail grafana/provisioning/datasources
# 2. Поместите все файлы конфигурации
# ... (создайте файлы по шаблонам выше)
# 3. Запустите
docker-compose up -d
```

После запуска:
- **Grafana**: [http://localhost:3000](http://localhost:3000/) (admin/admin)    
- **Prometheus**: [http://localhost:9090](http://localhost:9090/)    
- **Loki**: [http://localhost:3100](http://localhost:3100/) (API для проверки)    

---
## Итог: что вы получаете

|Задача|Решение|
|---|---|
|Сбор и хранение метрик|Prometheus [](https://github.com/Raj-Manghani/prom-graf-loki-monitoring-stack)[](https://github.com/takeshi-su57/luckyplans/issues/51)|
|Сбор и хранение логов|Loki + Promtail [](https://grafana.com/docs/enterprise-logs/latest/send-data/)|
|Визуализация (дашборды)|Grafana (один интерфейс для всего) [](https://documentation.ubuntu.com/lxd/default/howto/grafana/#grafana)|
|Связь метрик и логов|derived fields в Loki + единая Grafana [](https://github.com/takeshi-su57/luckyplans/issues/51)|
|Удобство запуска|один docker-compose.yml для всей связки|

Это **самый популярный и надёжный** open-source стек для observability. Для учебного проекта он идеален — легко настраивается, наглядно работает, и его используют в реальных компаниях. Удачи с внедрением!

---
---
---
## 📁 Готовая конфигурация (Docker Compose)

Вот минимальный, но полностью рабочий `docker-compose.yml`, который поднимает всю связку:
```yaml
version: '3.8'

services:
  # ==================== МЕТРИКИ ====================
  prometheus:
    image: prom/prometheus:latest
    container_name: prometheus
    volumes:
      - ./prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
      - prometheus_data:/prometheus
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--storage.tsdb.path=/prometheus'
      - '--storage.tsdb.retention.time=15d'
    ports:
      - "9090:9090"
    networks:
      - observability

  # ==================== ЛОГИ ====================
  loki:
    image: grafana/loki:latest
    container_name: loki
    ports:
      - "3100:3100"
    volumes:
      - ./loki/loki-config.yaml:/etc/loki/loki-config.yaml
      - loki_data:/loki
    command: -config.file=/etc/loki/loki-config.yaml
    networks:
      - observability

  promtail:
    image: grafana/promtail:latest
    container_name: promtail
    volumes:
      - ./promtail/promtail-config.yaml:/etc/promtail/promtail-config.yaml
      - /var/lib/docker/containers:/var/lib/docker/containers:ro  # читает логи контейнеров
      - /var/log:/var/log:ro                                      # системные логи
      - ./logs:/logs:ro                                           # кастомные файлы логов
    command: -config.file=/etc/promtail/promtail-config.yaml
    networks:
      - observability

  # ==================== ВИЗУАЛИЗАЦИЯ ====================
  grafana:
    image: grafana/grafana:latest
    container_name: grafana
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_USER=admin
      - GF_SECURITY_ADMIN_PASSWORD=admin
      - GF_INSTALL_PLUGINS=grafana-piechart-panel
    volumes:
      - grafana_data:/var/lib/grafana
      - ./grafana/provisioning:/etc/grafana/provisioning
    networks:
      - observability

networks:
  observability:
    driver: bridge

volumes:
  prometheus_data:
  loki_data:
  grafana_data:
```
### Файлы конфигурации
**`prometheus/prometheus.yml`** — говорит Prometheus, где забирать метрики:
```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'microservices'
    metrics_path: '/actuator/prometheus'   # Spring Boot Actuator endpoint
    static_configs:
      - targets:
        - 'host.docker.internal:8081'      # ваш API Gateway
        - 'host.docker.internal:8082'      # ваш User Service
        - 'host.docker.internal:8083'      # ваш Delivery Service
        # ... добавьте все свои сервисы
```

**`loki/loki-config.yaml`** — настройки хранения логов:
```yaml
auth_enabled: false

server:
  http_listen_port: 3100

common:
  path_prefix: /loki
  storage:
    filesystem:
      chunks_directory: /loki/chunks
      rules_directory: /loki/rules
  replication_factor: 1
  ring:
    kvstore:
      store: inmemory

schema_config:
  configs:
    - from: 2020-10-24
      store: tsdb
      object_store: filesystem
      schema: v13
      index:
        prefix: index_
        period: 24h

limits_config:
  retention_period: 168h   # 7 дней хранения
```

**`promtail/promtail-config.yaml`** — настройка сбора логов:
```yaml
server:
  http_listen_port: 9080
  grpc_listen_port: 0

positions:
  filename: /tmp/positions.yaml

clients:
  - url: http://loki:3100/loki/api/v1/push

scrape_configs:
  - job_name: 'docker_containers'
    docker_sd_configs:
      - host: unix:///var/run/docker.sock
        refresh_interval: 5s
    relabel_configs:
      - source_labels: ['__meta_docker_container_name']
        regex: '/(.*)'
        target_label: 'container'
      - source_labels: ['__meta_docker_container_label_com_docker_compose_service']
        target_label: 'service'          # имя сервиса из docker-compose
```

**`grafana/provisioning/datasources/datasource.yml`** — автоматически подключает Prometheus и Loki в Grafana:
```yaml
apiVersion: 1

datasources:
  - name: Prometheus
    type: prometheus
    access: proxy
    url: http://prometheus:9090
    isDefault: true

  - name: Loki
    type: loki
    access: proxy
    url: http://loki:3100
    isDefault: false
```

---


