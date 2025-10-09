# Ответ — поэтапно и по делу ✅

## Что означает ELK
- **E — Elasticsearch** — распределённая поисковая БД + аналитический движок (индексирование, полнотекстовый поиск, агрегации).    
- **L — Logstash** — pipeline: принимает логи, парсит/фильтрует/трансформирует, буферизует и шлёт в Elasticsearch (или куда нужно).    
- **K — Kibana** — UI: визуализации, dashboards, Discover, лог-просмотр, алерты и дашборды.   
> Часто говорят _Elastic Stack_ — это ELK + Beats (Filebeat, Metricbeat и т.д.).

---
## Полный поток: «из кода → в Kibana» (шаг за шагом)

1. **В коде приложение пишет лог**    
    - `logger.info("...");` с полезной структурой: уровень, message, stacktrace, MDC (correlationId, userId, traceId).
    
2. **Форматирование логера**    
    - Logback/Log4j2 + JSON-encoder (рекомендуется структурированный JSON). Или обычный текстовый layout.
    
3. **Отправка логов на хост**    
    - Вариант A: лог пишется в файл/stdout (в контейнере) — затем «shipper» (Filebeat/Fluent Bit) считывает.        
    - Вариант B: логер напрямую шлёт в Logstash (TCP/UDP/HTTP) через appender.
    
4. **Shipper (Filebeat/Fluentd/Fluent Bit)**    
    - Тянет новые строки из файлов или получает stdout, добавляет метаданные (k8s labels, pod, namespace), и шлёт в Logstash или прямо в Elasticsearch.
    
5. **Logstash (опционально)**    
    - Вход: beats/tcp/http.        
    - Фильтры: `grok`, `json`, `mutate`, `geoip`, `date` — нормализация полей, извлечение полей из message, удаление лишнего.        
    - Output: Elasticsearch (или Kafka, если нужен buffering).
    
6. **Elasticsearch**    
    - Индексирование документа → применяются mapping/index template и ingest pipeline (доп. parsing).        
    - ILM (index lifecycle) управляет хранением/ролловером/удалением.
    
7. **Kibana**    
    - Пользователь видит логи в Discover/Logs, строит дашборды, создаёт визуализации и алерты (Watcher / Kibana Alerting).
    
8. **Результат:** через секунды (зависит от конфигурации) лог становится доступен в Kibana.

---
## Пример «быстрой» цепочки (реально часто в проде)

App (Logback → JSON → файл) → Filebeat → Logstash (grok/json) → Elasticsearch (index myapp-YYYY.MM.DD) → Kibana (Discover/Dashboard).

---
## Мини-конфиги (чтобы показать на собеседовании)

**Logback (JSON через logstash-logback-encoder)**
```xml
<appender name="LOGSTASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
  <destination>logstash:5000</destination>
  <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
</appender>

```

**Filebeat (отправка в Logstash)**
```yaml
filebeat.inputs:
- type: log
  paths: ["/var/log/myapp/*.log"]
output.logstash:
  hosts: ["logstash:5044"]

```

**Logstash pipeline**
```conf
input { beats { port => 5044 } }
filter {
  if [message] =~ /^\{/ { json { source => "message" } }
  else { grok { match => { "message" => "%{TIMESTAMP_ISO8601:ts} %{LOGLEVEL:level} \[%{DATA:thread}\] %{JAVACLASS:logger} - %{GREEDYDATA:msg}" } } }
  date { match => ["ts","ISO8601"] }
}
output {
  elasticsearch { hosts => ["es:9200"] index => "myapp-%{+YYYY.MM.dd}" }
}

```

---
## Важные детали и best-practices (коротко)

- **Структурированные логи (JSON)** — намного удобнее для парсинга и агрегаций.    
- **MDC / correlationId** — проставлять requestId из фронта/nginx, чтобы коррелировать логи/трейсы.    
- **Политика индексов (ILM)** — ротация, горячие/тёплые/холодные слои.    
- **Mappings** — заранее задать шаблоны индексов (чтобы не плодить dynamic fields).    
- **Безопасность**: TLS, аутентификация (user/pass, API key), RBAC в Kibana.    
- **Буферизация/надёжность**: Filebeat + Logstash persistent queue / Kafka в качестве durable buffer.    
- **Ошибки парсинга**: хранить raw message + поля парсинга, чтобы не терять данные. 
- **Alerting**: настроить алерты по ошибкам/пиковым задержкам через Kibana Alerts/Watcher/External (Prometheus Alertmanager).    
- **Observability**: связывать логи с метриками (Prometheus) и трассировками (APM/Jaeger) для полного контекста.    

---
## Частые вопросы интервьюеров — короткие ответы

- **Почему JSON?** → удобство парсинга, точные типы, быстрые агрегации.
    
- **Logstash или Filebeat → ES?** → Filebeat прямой путь дешевле по ресурсам; Logstash даёт мощную трансформацию. Часто: Filebeat → Logstash → ES.
    
- **Что при больших объёмах?** → добавляем Kafka между shipper и processing, масштабируем ES (шарды, ноды), используем ILM.
    
- **Как снизить стоимость хранения?** → агрегации/резюме, хранить raw короткий период, long-term cold storage.

---
---
---
## Ниже — **конфигурации, команды и объяснения**: Logback → Filebeat → Logstash → Elasticsearch (с ILM, шаблонами) → Kibana, + K8s-примеры и рекомендации по безопасности.

# 1) Краткая схема (одно предложение)

App (Logback json) → stdout/file → Filebeat (DaemonSet) → Logstash (парсинг, фильтры, buffering) → Elasticsearch (индексы + ILM) → Kibana (dashboards, alerting). 🔁

---
# 2) Logback (приложение) — JSON + MDC + Logstash appender
```xml
<configuration>
  <appender name="ASYNC_JSON" class="ch.qos.logback.classic.AsyncAppender">
    <appender-ref ref="LOGSTASH"/>
    <queueSize>512</queueSize>
    <discardingThreshold>0</discardingThreshold>
  </appender>

  <appender name="LOGSTASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
    <destination>logstash:5044</destination>
    <encoder class="net.logstash.logback.encoder.LogstashEncoder">
      <includeMdcKeyName>traceId</includeMdcKeyName>
      <includeMdcKeyName>userId</includeMdcKeyName>
    </encoder>
  </appender>

  <root level="INFO">
    <appender-ref ref="ASYNC_JSON"/>
  </root>
</configuration>

```

В коде используй MDC:
```java
MDC.put("traceId", traceId);
logger.info("Order processed, id={}, durationMs={}", orderId, durationMs);
MDC.clear();
```

---
# 3) Filebeat (shipper) — production-ready (TLS → Logstash)

```yaml
filebeat.inputs:
- type: container
  paths:
    - /var/log/containers/*.log
  processors:
    - add_kubernetes_metadata:
        in_cluster: true

output.logstash:
  hosts: ["logstash:5044"]
  ssl.certificate_authorities: ["/etc/pki/tls/certs/ca.crt"]
  ssl.certificate: "/etc/pki/tls/certs/filebeat.crt"
  ssl.key: "/etc/pki/tls/private/filebeat.key"
logging.level: info

```

Если хотите отправлять в ES напрямую:
```yaml
output.elasticsearch:
  hosts: ["https://es:9200"]
  protocol: "https"
  username: "filebeat"
  password: "${FILEBEAT_PASSWORD}"
  ssl.certificate_authorities: ["/etc/pki/tls/certs/ca.crt"]

```
K8s: Filebeat развертывается как `DaemonSet` и монтирует `/var/log/containers`, kubelet metadata.

---
# 4) Logstash — pipeline (ingest, grok/json, output → ES/Kafka)

`logstash.yml` (включить persistent queue для надежности)
```yaml
path.data: /var/lib/logstash
queue.type: persisted
queue.max_bytes: 1gb
```

`pipeline/logs.conf`
```conf
input {
  beats {
    port => 5044
    ssl => true
    ssl_certificate => "/etc/logstash/certs/logstash.crt"
    ssl_key => "/etc/logstash/certs/logstash.key"
  }
}

filter {
  # Если приложение шлёт JSON
  if [message] =~ /^\{/ {
    json {
      source => "message"
      target => "json"
      remove_field => ["message"]
    }
    mutate {
      rename => { "[json][@timestamp]" => "@timestamp" }
      rename => { "[json][level]" => "log.level" }
      rename => { "[json][message]" => "log.message" }
    }
  } else {
    # grok fallback
    grok { match => { "message" => "%{TIMESTAMP_ISO8601:ts} %{LOGLEVEL:log.level} \[%{DATA:thread}\] %{JAVACLASS:logger} - %{GREEDYDATA:log.message}" } }
    date { match => ["ts", "ISO8601"] }
  }

  # Парсинг поля duration, userId и т.д.
  mutate { convert => { "durationMs" => "integer" } }
  # Собираем полезные поля в standardized structure
  mutate {
    add_field => { "service.name" => "my-service" }
  }
}

output {
  elasticsearch {
    hosts => ["https://es:9200"]
    index => "my-service-%{+YYYY.MM.dd}"
    ssl => true
    ssl_certificate_verification => true
    user => "logstash_internal"
    password => "${LOGSTASH_ES_PASS}"
    ilm_enabled => false
    # или использовать index шаблон + ILM
  }
  # опционально: для долговременного буфера -> kafka
  # kafka { ... }
}
```

**Примечания:**
- В production Logstash лучше запускать в кластере с минимум 3 реплик, persistent queue включён.    
- Для высокой нагрузки можно ставить Kafka между Filebeat и Logstash.    

---
# 5) Elasticsearch — индекс шаблон + ILM (index lifecycle)

**ILM policy** `logs-hot-warm-cold`:
```json
PUT _ilm/policy/logs-hot-warm-cold
{
  "policy": {
    "phases": {
      "hot": {
        "min_age": "0ms",
        "actions": { "rollover": { "max_age": "1d", "max_size": "50gb" } }
      },
      "warm": {
        "min_age": "1d",
        "actions": { "forcemerge": { "max_num_segments": 1 }, "allocate": { "number_of_replicas": 1 } }
      },
      "cold": {
        "min_age": "7d",
        "actions": { "freeze": {} }
      },
      "delete": {
        "min_age": "30d",
        "actions": { "delete": {} }
      }
    }
  }
}

```

**Index template + ILM policy attach**
```json
PUT _index_template/my-service-logs
{
  "index_patterns": ["my-service-*"],
  "template": {
    "settings": {
      "number_of_shards": 1,
      "index.lifecycle.name": "logs-hot-warm-cold",
      "index.lifecycle.rollover_alias": "my-service-write"
    },
    "mappings": {
      "properties": {
        "@timestamp": { "type": "date" },
        "log.level": { "type": "keyword" },
        "service.name": { "type": "keyword" },
        "message": { "type": "text" },
        "durationMs": { "type": "integer" },
        "userId": { "type": "keyword" }
      }
    }
  },
  "priority": 500
}
```

Создать initial write index:
```json
PUT my-service-000001
{
  "aliases": {
    "my-service-write": { "is_write_index": true }
  }
}
```

---
# 6) Безопасность и выдача прав (Logstash → ES)

Рекомендуется использовать API Key или internal user:
```bash
# Создать user logstash_internal (пример, через ES security)
# или создать API key
curl -u elastic:ELASTIC_PWD -X POST "https://es:9200/_security/api_key" -H 'Content-Type: application/json' -d'
{
  "name": "logstash-apikey",
  "role_descriptors": {
    "logstash_writer": {
      "cluster": ["monitor","manage_index_templates"],
      "index": [
        {
          "names": ["my-service-*"],
          "privileges": ["create_index","write","create","manage","view_index_metadata"]
        }
      ]
    }
  }
}'

```
API key → использовать в Logstash output.
TLS: всегда включать между Filebeat → Logstash → ES.

---
# 7) K8s: Filebeat DaemonSet (*минимальный пример*)

`filebeat-config` ConfigMap (содержит `filebeat.yml`), Secret с TLS-кейтом, затем DaemonSet монтирует `/var/log/containers` и `/var/lib/docker/containers`.

Не печатаю весь большой манифест — скажу кратко:
- RBAC (ClusterRole/ClusterRoleBinding) для чтения Pod metadata.    
- DaemonSet со `hostPath` `/var/log/containers` и `volumeMount` `/var/lib/docker/containers`.    
- ConfigMap с `filebeat.yml`.    
- Secret с TLS certs.   
Если нужно — могу прислать полный DaemonSet-манифест.

---
# 8) Log rotation, raw-logs preservation

- Filebeat читает files, поэтому лог-роутинг должен быть совместим: use `copytruncate`/proper rotation; лучше логировать в stdout и let container runtime handle logs + Filebeat reads `/var/log/containers/...`.    

---
# 9) Monitoring, resilience, buffering

- Filebeat + Logstash + ES → при высоких нагрузках: поставить Kafka между Filebeat и Logstash или Filebeat to Kafka.
    
- Logstash persistent queues + monitoring (X-Pack Monitoring) → отслеживать lag.
    
- ES: мониторинг heap, GC, disk watermark.    

---
# 10) Kibana: как создать полезные dash / quick queries (ошибки + latency)

## A) Индекс-паттерн
- Создать `my-service-*` индекс pattern, указать `@timestamp`.    

## B) Saved Search (Errors)
KQL:
```pgsql
log.level : "ERROR" or log.level : "ERROR"
```
Сохранить как `MyService - Errors`.

## C) Visualization (Errors over time)
- Открой Kibana → Visualize → Create visualization → Line chart (or Lens)    
- X-axis: Date histogram `@timestamp` (interval: auto/1m)    
- Y-axis: Count    
- Filter: `log.level: "ERROR"`    

## D) Visualization (Avg latency)
Если в логах есть поле `durationMs`:
- Visualize → Lens → Y: Average of `durationMs`    
- X: Date histogram `@timestamp`    
- Filter: service.name: "my-service"    

## E) Dashboard
- Создать Dashboard, добавить визуализации: Errors over time, Avg latency, Top 10 endpoints by errors, Recent error events (Saved search), Heatmap of response times per endpoint.    

---
# 11) Примеры запросов Elasticsearch (DSL) для панелей

**1) Count errors per minute**
```json
POST my-service-*/_search
{
  "size": 0,
  "query": { "term": { "log.level": "ERROR" } },
  "aggs": {
    "per_minute": {
      "date_histogram": { "field": "@timestamp", "fixed_interval": "1m" }
    }
  }
}

```

**2) Avg latency in last 15m**
```json
POST my-service-*/_search
{
  "size": 0,
  "query": { "range": { "@timestamp": { "gte": "now-15m" } } },
  "aggs": {
    "avg_duration": { "avg": { "field": "durationMs" } }
  }
}

```

---
# 12) Пример простого Kibana Dashboard JSON (скелет)

Ниже — **минимальный skeleton**, который можно экспортировать/импортировать в Kibana (зависит от версии Kibana — может потребовать прав). Это лишь пример структуры — обычно экспорт делает Kibana UI.
```json
{
  "objects": [
    {
      "type": "visualization",
      "id": "errors-over-time",
      "attributes": {
        "title": "Errors over time",
        "visState": "{\"type\":\"line\",\"aggs\": ... }",
        "uiStateJSON": "{}",
        "kibanaSavedObjectMeta": { "searchSourceJSON": "{\"index\":\"my-service-*\",\"query\":{},\"filter\":[]}" }
      }
    },
    {
      "type": "visualization",
      "id": "avg-latency",
      "attributes": {
        "title": "Average latency",
        "visState": "{\"type\":\"line\",\"aggs\": ... }",
        "kibanaSavedObjectMeta": { "searchSourceJSON": "{\"index\":\"my-service-*\"}" }
      }
    },
    {
      "type": "dashboard",
      "id": "my-service-dashboard",
      "attributes": {
        "title": "My Service Observability",
        "panelsJSON": "[{\"panelIndex\":\"1\",\"type\":\"visualization\",\"id\":\"errors-over-time\"},{\"panelIndex\":\"2\",\"type\":\"visualization\",\"id\":\"avg-latency\"}]"
      }
    }
  ]
}

```
> Совет: создайте визуализации через UI, экспортируйте dashboard — получите корректный JSON под вашу Kibana-версию.

---
# 13) Best practices / checklist для продакшна ✅

- JSON-struct logs + MDC (traceId).    
- TLS everywhere (Filebeat→Logstash→ES + Kibana auth).    
- RBAC & API keys, least privilege.    
- ILM: hot/warm/cold/delete.    
- Persistent queue в Logstash или Kafka.    
- Index templates + explicit mappings.    
- Store raw message (raw.message) при парсинг-ошибках.    
- Alerts: настроить по росту ERROR rate и по latency SLA.    
- Detect & strip PII before отправки (GDPR).    
- Pre-commit git-secrets, CI checks.    

---
# 14) Полезные команды и quick snippets

- Проверить индексы:   
`curl -u elastic:pwd "https://es:9200/_cat/indices/my-service-*?v&s=index"`

- Просмотреть ILM:    
`curl -u elastic:pwd "https://es:9200/_ilm/policy/logs-hot-warm-cold?pretty"`

---
