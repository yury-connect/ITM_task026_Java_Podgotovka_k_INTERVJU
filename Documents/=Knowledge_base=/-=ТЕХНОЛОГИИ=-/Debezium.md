**Debezium** — это ==распределенная платформа с открытым исходным кодом для реализации паттерна _Change Data Capture_ (CDC)==. Она непрерывно отслеживает изменения в базах данных (события создания, обновления и удаления строк) и передает их в режиме реального времени в другие системы, чаще всего в брокеры сообщений. [1](https://gitverse.ru/blog/articles/data/253-chto-takoe-debezium-i-dlya-chego-ispolzuetsya), [2](https://datafinder.ru/products/kursy/uchebnyy-kurs-sovremennaya-arhitektura-hranilishcha-dannyh/chto-takoe-debezium-i-kak-ego-primenyat), [3](https://docs.arenadata.io/ru/ADStreaming/current/concept/architecture/kafka-connect/debezium_overview.html)

### Ключевые особенности и архитектура

- **Как это работает:** Вместо того чтобы нагружать базу периодическими запросами, Debezium подключается к ее внутренним журналам (например, _Write-Ahead Log_ в PostgreSQL или _oplog_ в MongoDB). Это обеспечивает минимальную задержку и гарантирует, что ни одно изменение не будет пропущено. [1](https://www.reddit.com/r/apachekafka/comments/1jr1she/understanding_how_debezium_captures_changes_from/?tl=ru), [2](https://ru.linkedin.com/pulse/debezium-blueprint-how-unlocks-real-time-database-part-muse-semu-u7bfe?tl=ru), [3](https://debezium.io/)
  
- **Где используется:** Для синхронизации данных между микросервисами, мгновенной инвалидации кэшей, обновления поисковых индексов (например, Elasticsearch) и создания витрин данных. [1](https://debezium.io/)
  
- **Экосистема:** Обычно *Debezium* развертывается в виде набора плагинов в среде _Apache Kafka Connect_. Существует также _Debezium Server_ — готовое приложение, которое может пересылать события в облачные хранилища или другие очереди (Amazon Kinesis, Google Pub/Sub, Redis и др.) без обязательного использования Kafka. [1](https://habr.com/ru/companies/flant/articles/523510/)

Поддерживаются многие популярные СУБД, среди которых MySQL, PostgreSQL, Oracle, SQL Server и MongoDB. [1](https://habr.com/ru/companies/flant/articles/523510/)
