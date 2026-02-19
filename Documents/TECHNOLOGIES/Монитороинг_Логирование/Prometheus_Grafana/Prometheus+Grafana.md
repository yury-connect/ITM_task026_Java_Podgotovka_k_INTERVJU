# Prometheus + Grafana

Это уже другой стек — **мониторинг метрик** (не логов, а чисел: CPU, память, запросы в секунду, ошибки и т.д.).

**Prometheus**
- Open-source система мониторинга и алертинга (создана в SoundCloud, сейчас CNCF).
- Основная роль: собирает метрики по HTTP (pull-модель) с экспортёров (node_exporter, mysql_exporter и т.д.).
- Хранит данные в time-series базе (свой формат).
- Очень мощный язык запросов — PromQL.
- Идеально подходит для Kubernetes (service discovery, kube-state-metrics и т.д.).
- Простыми словами: «счётчик всего, что можно посчитать» + алерты.

**Grafana**
- Самый популярный инструмент для **визуализации метрик, логов и трейсов**.
- Подключается к десяткам источников: Prometheus, Elasticsearch, Loki, InfluxDB, Graphite, SQL-базы и т.д.
- Позволяет строить красивые дашборды, алерты, панели с графиками.
- Есть бесплатная open-source версия и платная Grafana Cloud / Enterprise.
- Простыми словами: «универсальный конструктор дашбордов» — рисует всё красиво, независимо от того, откуда данные.

---
### Краткое сравнение

| Инструмент    | Основная задача           | Тип данных            | Самый частый стек                    | Альтернативы              |
| ------------- | ------------------------- | --------------------- | ------------------------------------ | ------------------------- |
| Kibana        | Визуализация и поиск      | Логи + поиск          | Elastic Stack (ELK)                  | Grafana + Loki            |
| Elasticsearch | Хранение и быстрый поиск  | JSON, логи, документы | Elastic Stack                        | OpenSearch, Vespa         |
| Logstash      | Сбор и обработка данных   | Логи, события         | Elastic Stack                        | Fluentd, Vector, Filebeat |
| Prometheus    | Сбор и хранение метрик    | Time-series метрики   | Prometheus + Grafana (PromQL)        | VictoriaMetrics, Mimir    |
| Grafana       | Визуализация любых данных | Метрики, логи, трейсы | Prometheus + Grafana, Loki + Grafana | Kibana, Chronograf        |
### Если коротко:
- Хочешь **логи + поиск** → Elastic Stack (Elasticsearch + Logstash + Kibana).
- Хочешь **метрики + красивые графики** → Prometheus + Grafana (самый популярный стек в 2025–2026 для Kubernetes/микросервисов).

---
