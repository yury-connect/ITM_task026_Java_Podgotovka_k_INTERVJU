### **Prometheus — что это?**

**Prometheus** — это **open-source система мониторинга и алертинга**, созданная для надежного сбора метрик и анализа работы приложений в реальном времени. Изначально разработанная в **SoundCloud**, сейчас она входит в **CNCF** (*Cloud Native Computing Foundation*) и является одним из ключевых инструментов в экосистеме **Kubernetes** и микросервисов.

---
## 🔹 **Основные возможности Prometheus**

### 1. **Сбор метрик (Pull-модель)**
Prometheus **периодически опрашивает** (*HTTP-запросы*) целевые сервисы, которые предоставляют метрики в специальном формате.  
📌 Пример: Spring Boot приложение с `/actuator/prometheus`.

### 2. **Хранение данных**
- Встроенная **база данных (TSDB — Time Series Database)** оптимизирована для хранения временных рядов.    
- Поддерживает эффективное сжатие данных.    

### 3. **Гибкий язык запросов — PromQL**
Позволяет анализировать метрики с помощью мощных запросов, например:
```promql
http_requests_total{status="500"}  # Сколько было 500-х ошибок?
node_memory_usage_bytes / 1024 / 1024  # Память в MB
```

### 4. **Визуализация (Grafana + Prometheus)**
Prometheus сам имеет простой UI, но чаще его используют вместе с **Grafana** для красивых дашбордов.

### 5. **Алертинг (Alertmanager)**
- Можно настраивать правила (например, "если CPU > 90% больше 5 минут — отправить уведомление").    
- Поддерживает **Slack, Email, PagerDuty** и другие системы оповещения.    

---
## 🔹 **Архитектура Prometheus**

[https://prometheus.io/assets/architecture.png](https://prometheus.io/assets/architecture.png)

1. **Prometheus Server** — ядро (сбор, хранение, запросы).    
2. **Targets** — приложения/сервисы, которые отдают метрики (например, Spring Boot + Micrometer).    
3. **Service Discovery** — автоматическое обнаружение сервисов (Kubernetes, Consul, AWS EC2).    
4. **Alertmanager** — обработка и отправка алертов.    
5. **Grafana** — визуализация.    

---
## 🔹 **Как Prometheus собирает метрики?**

### 1. **Pull vs Push**
- **Prometheus использует Pull-модель** (сам ходит за метриками по расписанию).    
- Альтернатива: **Push-модель** (как в Graphite), где приложения сами отправляют данные.    

### 2. **Формат метрик**
Данные должны быть в формате:
```plaintext
http_requests_total{method="GET", status="200"} 42
http_requests_total{method="POST", status="500"} 3
```
📌 Spring Boot с Micrometer автоматически предоставляет такой endpoint (`/actuator/prometheus`).

### 3. **Пример конфигурации** (`prometheus.yml`)
```yaml
global:
  scrape_interval: 15s  # Как часто собирать метрики

scrape_configs:
  - job_name: "spring-boot-app"
    metrics_path: "/actuator/prometheus"
    static_configs:
      - targets: ["localhost:8080"]  # Адрес приложения
```

## 🔹 **Prometheus vs Grafana**

|**Prometheus**|**Grafana**|
|---|---|
|Сбор и хранение метрик|Визуализация метрик|
|Свой язык запросов (PromQL)|Поддержка разных источников (Prometheus, InfluxDB, Elasticsearch)|
|Есть простой UI|Красивые дашборды с графиками|
👉 **Они отлично работают вместе!**

---
## 🔹 **Примеры использования**

### 1. **Мониторинг Spring Boot приложения**

1. Приложение отдает метрики через `/actuator/prometheus`.    
2. Prometheus раз в 15 сек забирает их.    
3. Grafana подключается к Prometheus и рисует графики.    

### 2. **Алертинг при высокой загрузке CPU**
```yaml
# alert.rules.yml
groups:
- name: example
  rules:
  - alert: HighCpuUsage
    expr: 100 - (avg by(instance)(irate(node_cpu_seconds_total{mode="idle"}[5m])) * 100) > 80
    for: 5m
    labels:
      severity: warning
    annotations:
      summary: "High CPU usage on {{ $labels.instance }}"
```
👉 Alertmanager получит этот *алерт* и отправит уведомление в Slack.

---
## 🔹 **Плюсы и минусы Prometheus**

### ✅ **Преимущества**
- **Простота** — легко развернуть и настроить.    
- **Надежность** — работает даже при отказе отдельных сервисов (Pull-модель).    
- **Интеграция с Kubernetes** — автоматически обнаруживает поды.    
- **Мощный PromQL** — сложная аналитика "из коробки".    

### ❌ **Недостатки**
- **Нет долгосрочного хранения** (*обычно данные хранят **15-30 дней***).    
- **Нет полноценного distributed-режима** (*но есть Thanos/Cortex для масштабирования*).    

---
## 🔹 **Как попробовать?**

1. **Установка Prometheus** (Docker):
```bash
docker run -p 9090:9090 prom/prometheus
```
→ Открыть: `http://localhost:9090`

2. **Добавить Spring Boot в конфиг** (`prometheus.yml`):
```yaml
scrape_configs:
  - job_name: "spring-app"
    metrics_path: "/actuator/prometheus"
    static_configs:
      - targets: ["host.docker.internal:8080"]  # Для Docker на Mac/Windows
```

3. **Подключить Grafana** и импортировать дашборды (например, [JVM Micrometer](https://grafana.com/grafana/dashboards/4701)).

---
## **Вывод**

**Prometheus** — это:  
🚀 **Мощная система мониторинга** для сбора метрик.  
📊 **Гибкий анализ** через PromQL.  
🔔 **Интеграция с алертингом** (Alertmanager + Slack/Email).  
🛠 **Лучший выбор для Kubernetes и микросервисов**.

Если нужно **мониторить Spring Boot, серверы или облачную инфраструктуру** — Prometheus + Grafana идеально подходят!







