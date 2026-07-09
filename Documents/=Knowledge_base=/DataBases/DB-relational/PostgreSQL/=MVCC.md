# **`MVCC` *(Multiversion Concurrency Control)***

MVCC (Multi-Version Concurrency Control) — это архитектурная основа PostgreSQL, которая позволяет читающим и пишущим операциям выполняться одновременно, не блокируя друг друга, за счет хранения нескольких версий одной строки данных [](https://learn.microsoft.com/ja-jp/training/modules/understand-concurrency-postgresql/2-understand-multi-version-concurrency-control-snapshots)[](https://severalnines.com/blog/comparing-data-stores-postgresql-mvcc-vs-innodb?page=1).

Вот как это работает: кратко и затем структурированно и подробно.

---
## Кратко
Вместо того чтобы изменять или удалять данные "на месте", PostgreSQL создает новую версию строки (tuple) при каждом изменении. Старая версия остается в таблице, но помечается как неактуальная с помощью системных атрибутов `xmin` и `xmax`. Каждая транзакция видит свой "*снимок*" данных (*snapshot*) на определенный момент времени, что гарантирует ей согласованное представление данных без блокировок [](https://learn.microsoft.com/ja-jp/training/modules/understand-concurrency-postgresql/2-understand-multi-version-concurrency-control-snapshots)[](https://www.postgresql.org/files/developer/internalpics.pdf#4#4). Устаревшие версии строк впоследствии очищаются процессом `VACUUM` [](https://www.postgresql.eu/events/pgconfde2025/sessions/session/6582/slides/671/PGConf.de%20-%20MVCC.pdf#1#1)[](https://beta.pgcon.org/2015/schedule/attachments/367_Vacuum.pdf#1#1).

---
## Детально

#### 1. Версии строк (Tuples) и системные атрибуты
Каждая строка (tuple) в PostgreSQL хранит в своем заголовке несколько служебных полей, определяющих ее "историю" и видимость [](https://severalnines.com/blog/comparing-data-stores-postgresql-mvcc-vs-innodb?page=1)[](https://www.highgo.ca/2024/04/19/a-deeper-look-inside-postgresql-visibility-check-mechanism/):
	
- **`xmin`** (`Transaction ID MIN`): Идентификатор транзакции, которая **создала** эту версию строки (через `INSERT` или `UPDATE`).
    
- **`xmax`** (`Transaction ID MAX`): Идентификатор транзакции, которая **удалила** эту версию строки (через `DELETE` или `UPDATE`). Если строка активна, `xmax = 0`.
    
- **`cmin`/`cmax`**: Идентификаторы команд внутри транзакции, используемые для того, чтобы транзакция видела свои собственные изменения последовательно [](https://postgrespro.com/blog/pgsql/5967899).

**Пример жизненного цикла строки:**
1. **`INSERT`**: Транзакция с `txid = 12` вставляет строку 'A'.
    - Новая версия: `xmin = 12`, `xmax = 0` [](https://www.postgresql.eu/events/pgconfde2025/sessions/session/6582/slides/671/PGConf.de%20-%20MVCC.pdf#1#1).
    
2. **`UPDATE`**: Транзакция с `txid = 13` обновляет строку 'A' на 'B'.
    - Старая версия ('A') помечается как удаленная: `xmax = 13`.
    - Создается новая версия ('B'): `xmin = 13`, `xmax = 0` [](https://www.postgresql.eu/events/pgconfde2025/sessions/session/6582/slides/671/PGConf.de%20-%20MVCC.pdf#1#1)[](https://www.highgo.ca/2024/09/27/table-access-method-how-data-update-is-handled-in-postgresql/).
    
3. **`DELETE`**: Транзакция с `txid = 14` удаляет строку 'B'.
    - Текущая версия ('B') помечается как удаленная: `xmax = 14` [](https://www.postgresql.eu/events/pgconfde2025/sessions/session/6582/slides/671/PGConf.de%20-%20MVCC.pdf#1#1).

Таким образом, `UPDATE` — это, по сути, `DELETE` + `INSERT` [](https://beta.pgcon.org/2015/schedule/attachments/367_Vacuum.pdf#1#1)[](https://severalnines.com/blog/comparing-data-stores-postgresql-mvcc-vs-innodb?page=1).

#### 2. Снимки данных (Snapshots) и правила видимости
**Снимок** — это **"мгновенный снимок" состояния базы данных на момент начала запроса или транзакции**, который определяет, какие версии строк **видны**. 
Он состоит из трех ключевых чисел [](https://postgrespro.com/blog/pgsql/5967899)[](https://www.highgo.ca/2024/04/19/a-deeper-look-inside-postgresql-visibility-check-mechanism/):
	
- **`xmin`**: Наименьший `txid` среди всех еще активных транзакций.
    
- **`xmax`**: Первый "будущий" `txid`, который еще не был присвоен (`= старший активный txid + 1`).
    
- **`xip`**: Список `txid` всех транзакций, которые были активны (in-progress) на момент создания снимка.

**Упрощенное правило видимости**: Версия строки видна для снимка, если [](https://www.postgresql.org/files/developer/internalpics.pdf#4#4)[](https://postgrespro.com/blog/pgsql/5967899):
	
1. Ее `xmin` уже завершился (`COMMITTED`).
    
2. Ее `xmin` **не находится** в списке активных транзакций (`xip`) на момент создания снимка.
    
3. Ее `xmax` либо равен `0`, либо еще не завершился (`IN_PROGRESS`), либо находится в списке активных транзакций (`xip`).

Для проверки статуса транзакций (`COMMITTED` или `ABORTED`) используется специальный лог, называемый **Commit Log (CLOG)** [](https://www.postgresql.eu/events/pgconfde2025/sessions/session/6582/slides/671/PGConf.de%20-%20MVCC.pdf#1#1)[](https://www.highgo.ca/2024/04/19/a-deeper-look-inside-postgresql-visibility-check-mechanism/). Для ускорения проверок используются битовые флаги (`hint bits`), которые кешируют результат проверки по CLOG прямо в заголовке строки [](https://www.highgo.ca/2024/04/19/a-deeper-look-inside-postgresql-visibility-check-mechanism/).

#### 3. Уровни изоляции и Snapshot
Время жизни снимка зависит от уровня изоляции транзакции [](https://postgrespro.com/blog/pgsql/5967899):
	
- **`READ COMMITTED`**: Новый снимок создается для **каждого отдельного оператора (запроса)** в транзакции. Это позволяет видеть изменения, закоммиченные другими транзакциями между запросами.
    
- **`REPEATABLE READ` и `SERIALIZABLE`**: Один снимок создается в начале транзакции и используется для **всех** ее операторов. Это гарантирует, что транзакция видит одно и то же состояние данных на протяжении всего своего выполнения.

#### 4. Оптимизация: HOT-обновления (Heap-Only Tuples)
Чтобы снизить нагрузку на индексы при обновлении, PostgreSQL использует HOT-обновления [](https://www.highgo.ca/2024/09/27/table-access-method-how-data-update-is-handled-in-postgresql/)[](https://beta.pgcon.org/2015/schedule/attachments/367_Vacuum.pdf#1#1). Если выполняются два условия:
	
- Обновление не затрагивает ни одного столбца, входящего в какой-либо индекс.
    
- Новая версия строки может поместиться на ту же страницу (page) в памяти, что и старая.

То PostgreSQL не создает новую запись в индексе. Вместо этого он связывает старую и новую версии строки внутри одной страницы (цепочечная ссылка), и старая индексная запись продолжает указывать на начало этой цепочки. Это значительно экономит место и ускоряет операции.

#### 5. Очистка мусора: **VACUUM**
Поскольку старые версии строк не удаляются физически, таблицы и индексы со временем "раздуваются" (bloat) [](https://edu.postgrespro.com/2dintro-15-en/intro_04_vacuum_overview.html). Для решения этой проблемы служит процесс `VACUUM` (обычно автоматический `autovacuum`) [](https://beta.pgcon.org/2015/schedule/attachments/367_Vacuum.pdf#1#1). Его основные задачи:

- **Удаление "мертвых" версий**: Удаляет версии строк, которые больше не видны ни одной транзакции (чтобы это определить, он использует понятие **горизонта транзакций** — `global xmin`, старейшую из всех активных транзакций) [](https://www.postgresql.eu/events/pgconfde2025/sessions/session/6582/slides/671/PGConf.de%20-%20MVCC.pdf#1#1)[](https://postgrespro.com/blog/pgsql/5967918).
    
- **Очистка индексов**: Удаляет из индексов ссылки на удаленные версии строк [](https://postgrespro.com/blog/pgsql/5967918).
    
- **Обновление карт**: Обновляет `Free Space Map` (FSM) для указания свободного места и `Visibility Map` (VM) для указания страниц, содержащих только видимые для всех строки. Это ускоряет последующие сканирования [](https://www.postgresql.eu/events/pgconfde2025/sessions/session/6582/slides/671/PGConf.de%20-%20MVCC.pdf#1#1)[](https://postgrespro.com/blog/pgsql/5967918)[](https://beta.pgcon.org/2015/schedule/attachments/367_Vacuum.pdf#1#1).
    

`VACUUM` работает конкурентно с обычными операциями, не блокируя их на длительное время [](https://postgrespro.com/blog/pgsql/5967918). Однако долго живущие транзакции могут сдвигать "горизонт транзакций" и мешать `VACUUM` очищать "мертвые" строки, что ведет к разрастанию таблиц [](https://www.postgresql.eu/events/pgconfde2025/sessions/session/6582/slides/671/PGConf.de%20-%20MVCC.pdf#1#1)[](https://postgrespro.com/blog/pgsql/5967918).

--
