**Change Data Capture (CDC)** — это ==технология захвата изменений данных, которая непрерывно отслеживает операции вставки, обновления и удаления в базе данных (БД) и передает эти изменения в целевые системы (например, хранилища данных или аналитические платформы) в режиме реального времени или почти в реальном времени==. [1](https://yandex.cloud/ru/docs/data-transfer/concepts/cdc), [2](https://datafinder.ru/products/change-data-capture-cdc-zahvat-izmeneniy-dannyh-kak-rabotaet-gde-primenyaetsya-kakie-est), [3](https://wiki.loginom.ru/articles/cdc.html)

### Как это работает

В отличие от классических пакетных загрузок (ETL), когда данные считываются из источника полностью, CDC фиксирует и передает _только_ те строки, которые действительно были изменены. Это реализуется двумя основными способами: [1](https://datafinder.ru/products/change-data-capture-cdc-zahvat-izmeneniy-dannyh-kak-rabotaet-gde-primenyaetsya-kakie-est)

1. **Чтение журнала транзакций (log-based CDC):** Система считывает внутренний бинарный лог транзакций базы данных (например, WAL в PostgreSQL или Binlog в MySQL). Это самый эффективный способ, так как он не создает нагрузки на саму БД и передает события мгновенно. [1](https://datafinder.ru/products/chto-takoe-cdc-change-data-capture-i-kak-eto-rabotaet)
   
2. **Использование триггеров и меток времени (query-based CDC):** База данных настраивается так, чтобы при любой модификации записывать информацию об изменении (время, тип операции, измененные данные) в служебную таблицу, откуда ее забирает сервис-потребитель. [1](https://datafinder.ru/products/chto-takoe-cdc-change-data-capture-i-kak-eto-rabotaet)

### Зачем нужен CDC

- **Синхронизация хранилищ и витрин данных:** Позволяет быстро наполнять аналитические системы актуальными данными.
  
- **Микросервисная архитектура:** Обеспечивает мгновенную реакцию других сервисов на изменения в основной базе данных без лишних запросов к ней (паттерн _Transactional outbox_).
  
- **Репликация и бэкапы:** Помогает создавать резервные копии и поддерживать горячие реплики для обеспечения высокой доступности. [1](https://wiki.loginom.ru/articles/cdc.html), [2](https://habr.com/ru/companies/yandex_cloud_and_infra/articles/754802/), [3](https://platformv.sbertech.ru/docs/public/IGN/17.6.0/common/documents/administration-guide/change-data-capture.html)

### Популярные инструменты для реализации CDC

Для внедрения технологии используются специализированные решения (часто работающие по принципу event-driven архитектуры): [1](https://habr.com/ru/companies/yandex_cloud_and_infra/articles/754802/)

- **Debezium:** Open-source платформа, часто работающая в связке с Apache Kafka. Позволяет захватывать изменения из множества популярных баз данных.
  
- **Yandex Data Transfer:** Облачный сервис, поддерживающий CDC для непрерывной передачи данных между различными типами СУБД. [1](https://yandex.cloud/ru/docs/data-transfer/concepts/cdc), [2](https://habr.com/ru/companies/yandex_cloud_and_infra/articles/754802/)
  
- **Встроенные возможности:** Многие современные СУБД имеют собственные встроенные механизмы CDC, например, _SQL Server Change Data Capture_ или _Oracle GoldenGate_. [1](https://learn.microsoft.com/ru-ru/sql/relational-databases/track-changes/about-change-data-capture-sql-server?view=sql-server-ver17)

---
