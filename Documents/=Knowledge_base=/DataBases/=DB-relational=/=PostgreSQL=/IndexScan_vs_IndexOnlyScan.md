Разница между **Index Scan** и **Index Only Scan** в PostgreSQL заключается в том, куда базе данных приходится обращаться за самими данными: в основную таблицу (heap) или только в индекс.

> **Index Scan** — идёт в таблицу за данными.  
> **Index Only Scan** — берёт всё из индекса, таблицу **не трогает**.

## 🔍 `Index Scan` (*Обычное сканирование по индексу*)
📌 Это стандартный способ доступа к данным ч-з индекс. Он сост. из 2 шагов:
	
1. **Поиск в индексе:** База данных находит нужные записи (например, `WHERE id = 5`) и получает из индекса физический адрес строки в основной таблице.
    
2. **Обращение к таблице:** По этому адресу она извлекает саму строку данных (все её столбцы) из основной таблицы (heap) [](https://www.rockdata.net/tutorial/plan-index-scan-types/#bitmap-index-scan)[](https://www.percona.com/blog/one-index-three-different-postgresql-scan-types-bitmap-index-and-index-only/?trk=public_post-text)[](https://www.percona.com/blog/one-index-three-different-postgresql-scan-types-bitmap-index-and-index-only/).

Этот метод эффективен, когда нужно получить небольшое количество строк, но каждое такое обращение — это две операции ввода-вывода (сначала индекс, потом таблица).

## ⚡ `Index Only Scan` (*Сканирование **только** по индексу*)
📌 Это более быстрый метод, который используется, когда **все данные, необходимые для выполнения запроса, уже содержатся в самом индексе**. База данных не обращается к основной таблице вообще [](https://www.postgresql.org/message-id/16103e6fe0e-1719-e6dc%40webjas-vad073.srv.aolmail.net)[](https://www.postgresql.org/docs/14/indexes-index-only-scans.html)[](https://www.postgresql.org/docs/10/indexes-index-only-scans.html).
	
#### Чтобы это сработало, должны выполняться **два условия**:
	
- **Тип индекса:** Индекс должен поддерживать такую возможность. `B-tree` индексы (*самые распространенные*) поддерживают всегда. `GIN` индексы, например, **не поддерживают**, так как хранят только часть данных [](https://www.postgresql.org/docs/14/indexes-index-only-scans.html)[](https://www.postgresql.org/docs/10/indexes-index-only-scans.html).
    
- **Состав запроса:** Запрос должен использовать только те столбцы, которые уже включены в индекс. Например, если у вас есть индекс по `(x, y)`, то запрос `SELECT x, y FROM tab WHERE x = 'key'` может использовать Index Only Scan, а `SELECT x, z FROM tab WHERE x = 'key'` — нет, потому что столбца `z` в индексе нет [](https://www.postgresql.org/docs/14/indexes-index-only-scans.html)[](https://docs.postgresql.tw/16/the-sql-language/index/index-only-scans-and-covering-indexes.md).

### 🗺️ Скрытый механизм: `Visibility Map`
Даже если все данные есть в индексе, есть нюанс. PostgreSQL хранит информацию о видимости строк (для MVCC) только в основной таблице, а не в индексе. Поэтому формально базе всё равно нужно проверить, видна ли строка текущей транзакции.
	
Чтобы избежать этого, используется **Visibility Map** — специальная карта, которая отмечает страницы таблицы, где все строки видны всем текущим и будущим транзакциям. Index Only Scan сначала проверяет эту карту. Если страница помечена как «все видимы», данные возвращаются напрямую из индекса без обращения к таблице. Если нет — базе всё равно придется заглянуть в таблицу для проверки видимости, и преимущество в скорости исчезнет [](https://www.postgresql.org/docs/14/indexes-index-only-scans.html)[](https://www.postgresql.org/docs/10/indexes-index-only-scans.html)[](https://www.rockdata.net/tutorial/plan-index-only-scan/#cost-estimation).

### 💡 Итог
**Index Scan** всегда идет в таблицу за данными. **Index Only Scan** пытается получить данные только из индекса, что делает его значительно быстрее, но он возможен только при определенных условиях (подходящий индекс и запрос).

---
