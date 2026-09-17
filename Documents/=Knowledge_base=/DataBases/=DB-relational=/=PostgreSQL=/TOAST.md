# 📦 **12. TOAST <br>(*The Oversized-Attribute Storage Technique*)**

Механизм *PostgreSQL* для обхода ограничения: *`строка должна помещаться в страницу (8 КБ)`*, но значения `TEXT`, `JSONB`, `BYTEA`, `VARCHAR`, `XML` и массивов могут быть намного больше.

Когда строка не влезает в страницу, TOAST **автоматически и прозрачно**:
1. **Сжимает** крупные значения (*по умолчанию алгоритм `pglz`, с PG14 — можно `lz4`*).
2. Если после сжатия строка всё ещё слишком велика — **выносит** значение в **отдельную скрытую TOAST-таблицу**, разбивая его на чанки по ~2000 байт [](https://www.postgresql.org/docs/14/storage-toast.html)[](https://www.cybrosys.com/research-and-development/postgres/how-does-postgresql-deal-with-oversized-data-silently).
3. В основной строке остаётся **компактный указатель** (*18 байт + заголовок*) на эти данные [](https://www.postgresql.org/docs/14/storage-toast.html)[](https://www.cybrosys.com/research-and-development/postgres/how-does-postgresql-deal-with-oversized-data-silently).

Всё это **невидимо** для SQL-запросов: PostgreSQL сам «достаёт» данные из TOAST-таблицы, собирает и распаковывает.

### 📌 **Четыре стратегии хранения** для *TOAST*-колонок <br>(*можно задать через `ALTER TABLE ... SET STORAGE`*):
	
- **PLAIN** — без сжатия и без выноса. Только для типов фиксированной длины [](https://www.postgresql.org/message-id/3D821650.8090208%40joeconway.com)[](https://github.com/damusix/skills/blob/main/postgres/references/31-toast.md#1).
    
- **EXTENDED** (*по умолчанию*) — сначала сжатие, потом вынос. Лучший баланс [](https://github.com/damusix/skills/blob/main/postgres/references/31-toast.md#1).
    
- **EXTERNAL** — без сжатия, сразу вынос. Полезно для **уже сжатых** данных (*архивы, изображения*) [](https://github.com/damusix/skills/blob/main/postgres/references/31-toast.md#1).
    
- **MAIN** — сжатие, вынос только в крайнем случае [](https://github.com/damusix/skills/blob/main/postgres/references/31-toast.md#1).    

**Скрытая цена:** `SELECT *` по таблице с TOAST-колонками читает **и основную таблицу, и TOAST-таблицу** — это медленнее. Если TOAST-колонка не нужна — не запрашивай её [](https://www.cybrosys.com/research-and-development/postgres/how-does-postgresql-deal-with-oversized-data-silently). Также `UPDATE` любой строки с TOAST-полем создаёт мусор в TOAST-таблице, который потом чистит `VACUUM` [](https://www.cybrosys.com/research-and-development/postgres/how-does-postgresql-deal-with-oversized-data-silently).

> - Порог срабатывания — не «значение > 2 КБ», а «строка не влезает в страницу, и TOAST ужимает её до ~2 КБ».  
> - Указатель остаётся в строке **только** при выносе. При сжатии — данные лежат in-line.

---
