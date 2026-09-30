**Change Data Capture (CDC)** — это ==технология захвата изменений данных, которая непрерывно отслеживает операции вставки, обновления и удаления в базе данных (БД) и передает эти изменения в целевые системы (*например, хранилища данных или аналитические платформы*) в режиме реального времени или почти в реальном времени==. [1](https://yandex.cloud/ru/docs/data-transfer/concepts/cdc), [2](https://datafinder.ru/products/change-data-capture-cdc-zahvat-izmeneniy-dannyh-kak-rabotaet-gde-primenyaetsya-kakie-est), [3](https://wiki.loginom.ru/articles/cdc.html)

## Как это работает

В отличие от классических пакетных загрузок (ETL), когда данные считываются из источника полностью, CDC фиксирует и передает _только_ те строки, которые действительно были изменены. Это реализуется двумя основными способами: [1](https://datafinder.ru/products/change-data-capture-cdc-zahvat-izmeneniy-dannyh-kak-rabotaet-gde-primenyaetsya-kakie-est)

1. **Чтение журнала транзакций (log-based CDC):** Система считывает внутренний бинарный лог транзакций базы данных (например, WAL в PostgreSQL или Binlog в MySQL). Это самый эффективный способ, так как он не создает нагрузки на саму БД и передает события мгновенно. [1](https://datafinder.ru/products/chto-takoe-cdc-change-data-capture-i-kak-eto-rabotaet)
   
2. **Использование триггеров и меток времени (query-based CDC):** База данных настраивается так, чтобы при любой модификации записывать информацию об изменении (*время, тип операции, измененные данные*) в служебную таблицу, откуда ее забирает сервис-потребитель. [1](https://datafinder.ru/products/chto-takoe-cdc-change-data-capture-i-kak-eto-rabotaet)

## Зачем нужен CDC

- **Синхронизация хранилищ и витрин данных:** Позволяет быстро наполнять аналитические системы актуальными данными.
  
- **Микросервисная архитектура:** Обеспечивает мгновенную реакцию других сервисов на изменения в основной базе данных без лишних запросов к ней (паттерн _Transactional outbox_).
  
- **Репликация и бэкапы:** Помогает создавать резервные копии и поддерживать горячие реплики для обеспечения высокой доступности. [1](https://wiki.loginom.ru/articles/cdc.html), [2](https://habr.com/ru/companies/yandex_cloud_and_infra/articles/754802/), [3](https://platformv.sbertech.ru/docs/public/IGN/17.6.0/common/documents/administration-guide/change-data-capture.html)

## Популярные инструменты для реализации CDC

Для внедрения технологии используются специализированные решения (часто работающие по принципу event-driven архитектуры): [1](https://habr.com/ru/companies/yandex_cloud_and_infra/articles/754802/)

- **Debezium:** Open-source платформа, часто работающая в связке с Apache Kafka. Позволяет захватывать изменения из множества популярных баз данных.
  
- **Yandex Data Transfer:** Облачный сервис, поддерживающий CDC для непрерывной передачи данных между различными типами СУБД. [1](https://yandex.cloud/ru/docs/data-transfer/concepts/cdc), [2](https://habr.com/ru/companies/yandex_cloud_and_infra/articles/754802/)
  
- **Встроенные возможности:** Многие современные СУБД имеют собственные встроенные механизмы CDC, например, _SQL Server Change Data Capture_ или _Oracle GoldenGate_. [1](https://learn.microsoft.com/ru-ru/sql/relational-databases/track-changes/about-change-data-capture-sql-server?view=sql-server-ver17)

---
---
---
CDC работает не только в реляционных базах данных, но и в нереляционных (NoSQL) — принцип один и тот же, но механизмы реализации разные.

---

### Работает ли CDC в нереляционных БД?

**Да, работает.** CDC изначально появился для реляционных баз (Oracle, PostgreSQL, MySQL), где изменения читаются из журнала транзакций (redo log, WAL, binlog)[](https://developer.confluent.io/courses/data-pipelines/kafka-data-ingestion-with-cdc/?utm_source=ytcommunity&utm_medium=organicsocial&utm_campaign=tm.devx_ch.cd-building-data-pipelines-with-apache-kafka-and-confluent_content.pipelines). Но современные NoSQL базы тоже поддерживают аналогичные механизмы.

В MongoDB есть **Change Streams** — это встроенный CDC-механизм, который позволяет подписаться на изменения в коллекции[](https://stackoverflow.com/feeds/question/56179867). В Cassandra тоже есть свой CDC, который записывает изменения в отдельный лог[](https://stackoverflow.com/feeds/question/56179867). DynamoDB использует **DynamoDB Streams** для тех же целей[](https://stackoverflow.com/feeds/question/56179867). Azure Cosmos DB имеет **Change Feed** — непрерывный журнал изменений, работающий с NoSQL, MongoDB, Cassandra и другими API[](https://learn.microsoft.com/id-id/Azure/cosmos-db/change-feed#1).

Инструменты вроде Redpanda Connect и Debezium тоже поддерживают NoSQL: MongoDB CDC через change streams, DynamoDB через streams, Cassandra через собственный механизм[](https://docs.redpanda.com/connect/guides/cdc/#nosql-databases)[](https://www.infoq.com/podcasts/change-data-capture-debezium/?topicPageSponsorship=edbfb4e9-40d6-457b-9c91-409a16c170c3#1).

**Разница в том, как CDC получает данные.** В реляционных БД чаще всего читают журнал транзакций на уровне диска (log-based CDC). В NoSQL базы предоставляют API для подписки на изменения (change streams, change feed), и инструмент просто подключается к этому API[](https://stackoverflow.com/feeds/question/56179867)[](https://learn.microsoft.com/id-id/Azure/cosmos-db/change-feed#1).

---

### Работает ли CDC в Kafka?

**Не просто работает — Kafka часто является центральным звеном CDC-пайплайнов.**

Стандартная архитектура такая: CDC-инструмент (например, Debezium) читает изменения из базы данных и **пишет их в топики Kafka**. Дальше другие системы (поисковые индексы, кэши, аналитические хранилища, другие микросервисы) подписываются на эти топики и получают изменения в реальном времени[](https://archive.qconsf.com/system/files/presentation-slides/gunnar_morling_-_practical-change-data-streaming-use-cases-with-apache-kafka-and-debezium-qconsf-2019.pdf#1#1)[](https://debezium.cn/documentation/faq/).

Debezium — это набор коннекторов для **Kafka Connect**, который как раз и предназначен для захвата изменений из баз и отправки их в Kafka[](https://debezium.cn/documentation/faq/)[](https://www.infoq.com/podcasts/change-data-capture-debezium/?topicPageSponsorship=edbfb4e9-40d6-457b-9c91-409a16c170c3#1). Так что Kafka не просто «поддерживает» CDC — она является транспортом, через который изменения доставляются потребителям[](https://aerospike.com/blog/change-data-capture-cdc-guide).

---

### Работает ли CDC с Redis?

**Да, но в основном как получатель данных, а не как источник.**

Redis Data Integration (RDI) использует CDC для синхронизации данных **из реляционных баз в Redis**. Изменения в Oracle, MySQL, PostgreSQL захватываются через CDC и применяются к Redis почти в реальном времени[](https://redis.io/docs/latest/develop/ai/context-engine/data-integration/). Есть и отдельные проекты вроде pg-redis-flux, которые делают то же самое для PostgreSQL[](https://pkg.go.dev/github.com/ksysoev/pg-redis-flux).

То есть Redis обычно выступает **целевой системой** (куда данные приходят), а не источником изменений. Но технически ничто не мешает сделать CDC и для самого Redis, если он поддерживает подписку на изменения (например, через keyspace notifications).

---

### Итог

CDC — это не технология, привязанная к типу базы. Это **паттерн захвата изменений**. В реляционных базах изменения читаются из журналов транзакций, в NoSQL — через change streams или change feed. Kafka — это стандартный транспорт для доставки этих изменений потребителям. Redis чаще всего выступает получателем данных через CDC, а не источником.
