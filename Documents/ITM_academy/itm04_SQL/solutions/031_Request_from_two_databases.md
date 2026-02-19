## Как сделать запрос из двух баз данных?

---
### ✅ Если базы на одном сервере

Просто указываем **имя базы** перед таблицей:
```sql
SELECT t1.*, t2.*
FROM database1.table1 t1
JOIN database2.table2 t2 ON t1.field1 = t2.field1;
```

### ✅ Если базы на разных серверах

#### 🔹 MySQL (_FEDERATED_)
1. Включите поддержку в `my.cnf`: `federated`
2. Создайте таблицу с удалённым подключением:
```sql
CREATE TABLE database1.table1 (...)
ENGINE=FEDERATED CONNECTION='mysql://user:pass@host/database2/table2';
```

#### 🔹 PostgreSQL (_dblink_)
```sql
SELECT * FROM dblink('dbname=db2', 'SELECT field1 FROM table2') AS t2(field1 INT)
JOIN database1.table1 t1 ON t1.field1 = t2.field1;
```

#### 🔹 Oracle (_DB LINK_)
```sql
SELECT t1.*, t2.* FROM database1.table1 t1 JOIN database2.table2@remote_db t2 ON t1.field1 = t2.field1;
```
---
### 🔥 Вывод:
- 🟢 **Один сервер** — используем `database.table`.
- 🟢 **Разные серверы** — `FEDERATED`, `dblink` или `DB LINK`.

**🚀 Выбирайте подходящий метод в зависимости от СУБД!**

---

```
***** из методички *****
Если в запросе таблица указывается с именем базы данных database1.table1, то таблица выбирается из database1, если просто table1, то - из активной базы данных.

Надо, чтобы базы были на одном сервере.
    SELECT t1.*, t2.*
    FROM database1.table1 AS t1
    INNER JOIN database2.table2 AS t2 ON t1.field1 = t2.field1
```

---
