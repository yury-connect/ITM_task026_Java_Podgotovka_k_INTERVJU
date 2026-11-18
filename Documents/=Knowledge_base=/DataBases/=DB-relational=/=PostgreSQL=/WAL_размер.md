В PostgreSQL понятие «размер WAL по умолчанию» относится к нескольким параметрам. ==**Размер одного файла (сегмента) WAL по умолчанию составляет 16 МБ**==, а **максимальный общий объём журнала между контрольными точками (`max_wal_size`) — 1 ГБ**. [1](https://postgrespro.ru/docs/postgrespro/current/runtime-config-wal), [2](https://habr.com/ru/companies/postgrespro/articles/459250/)

Основные параметры размера WAL

- **Размер сегмента (`wal_segment_size`).** По умолчанию составляет **16 МБ** и хранится в каталоге `$PGDATA/pg_wal`. Изменить это значение можно только при инициализации нового кластера с помощью флага `--wal-segsize` у утилиты `initdb`. [1](https://habr.com/ru/companies/postgrespro/articles/459250/)
- **Максимальный размер (`max_wal_size`).** По умолчанию равен **1 ГБ** и задаёт мягкий предел объёма WAL между автоматическими контрольными точками (checkpoint). При высокой нагрузке, сбоях архивации или задержке репликации фактический размер каталога может временно превышать этот лимит. [1](https://postgrespro.ru/docs/postgresql/9.6/runtime-config-wal?lang=ru-en), [2](https://postgresqlco.nf/doc/ru/param/max_wal_size/), [3](https://postgrespro.ru/docs/postgrespro/current/runtime-config-wal)
- **Минимальный размер (`min_wal_size`).** По умолчанию составляет **80 МБ**. Пока общий объём файлов WAL не опустится ниже этой отметки, старые сегменты не удаляются, а переиспользуются для новых записей. [1](https://postgrespro.ru/docs/postgrespro/current/runtime-config-wal)
- **Буфер WAL в памяти (`wal_buffers`).** По умолчанию установлено значение `-1`, что означает автоматический расчёт как **1/32 (около 3%) от `shared_buffers`**, но не менее 64 КБ и не более размера одного сегмента (16 МБ). [1](https://postgrespro.ru/docs/postgresql/9.6/runtime-config-wal?lang=ru-en)

Подробное описание настроек журнала предзаписи доступно в [документации Postgres Pro](https://postgrespro.ru/docs/postgrespro/current/runtime-config-wal).

---
