Полный список параметров, которые можно передавать в `@Transactional`:

### 1. Уровни изоляции (Isolation)
Определяют, насколько изменения одной транзакции видны другой[](https://github.com/vivek075/Documentation-Spring-Transaction)[](http://www.51testing.com/html/92/n-228592-2.html).
- `DEFAULT` (стандарт БД — для PostgreSQL это `READ_COMMITTED`)    
- `READ_UNCOMMITTED`    
- `READ_COMMITTED`    
- `REPEATABLE_READ`    
- `SERIALIZABLE`    

### 2. Режимы доступа (Read-only)
- `readOnly = true` — оптимизирует работу 
  (Hibernate не делает `flush` изменений, например)[](http://www.51testing.com/html/92/n-228592-2.html).    

### 3. Распространение (Propagation) — _это важно!_
Определяет, как метод ведет себя, если транзакция уже запущена[](https://github.com/vivek075/Documentation-Spring-Transaction).
- `REQUIRED` (по умолчанию) — входит в текущую или создает новую.    
- `REQUIRES_NEW` — приостанавливает текущую и создает новую.    
- `MANDATORY` — требует наличия текущей.    
- `NESTED` — создает вложенную (savepoint).    
- `SUPPORTS` — если есть — входит, если нет — работает без транзакции.    
- `NOT_SUPPORTED` и `NEVER`.    

### 4. Таймаут (Timeout)
- `timeout = 5` — максимальное время выполнения транзакции в секундах[](http://www.51testing.com/html/92/n-228592-2.html).    

### 5. Правила отката (Rollback)
- `rollbackFor` — список исключений, при которых откатывать (по умолчанию откат только при `RuntimeException`)[](https://vaadin.com/docs/latest/building-apps/forms-data/consistency/transactions/declarative)[](http://www.51testing.com/html/92/n-228592-2.html).    
- `noRollbackFor` — исключения, при которых не откатывать.    

#### Пример полной аннотации
```java
@Transactional(
    isolation = Isolation.REPEATABLE_READ,
    propagation = Propagation.REQUIRES_NEW,
    readOnly = false,
    timeout = 30,
    rollbackFor = SQLException.class
)
```

---
---
---
## **4 уровня изоляции** *(от слабого к сильному)*

|Уровень|Dirty Read|Non-Repeatable Read|Phantom Read|
|---|---|---|---|
|**READ UNCOMMITTED**|✅ есть|✅ есть|✅ есть|
|**READ COMMITTED**|❌ нет|✅ есть|✅ есть|
|**REPEATABLE READ**|❌ нет|❌ нет|✅ есть (в PostgreSQL — нет)|
|**SERIALIZABLE**|❌ нет|❌ нет|❌ нет|

### Что означают **аномалии**

|Аномалия|Что происходит|
|---|---|
|**Dirty Read**|Читаешь незакоммиченные данные другого потока (могут откатиться)|
|**Non-Repeatable Read**|Два одинаковых `SELECT` внутри транзакции дают разные результаты (данные изменились)|
|**Phantom Read**|Появились или исчезли строки, удовлетворяющие условию `WHERE`|

---
## Как работают **уровни** *(коротко)*

#### READ UNCOMMITTED (грязное чтение)
- Можно читать чужие незакоммиченные данные    
- Почти не используется в реальных БД    
- Самая высокая производительность    
#### READ COMMITTED (по умолчанию в PostgreSQL, SQL Server, Oracle)
- Читаешь только закоммиченные данные    
- Но в рамках транзакции повторный `SELECT` может увидеть новые данные    
- **Баланс производительности и консистентности**    
#### REPEATABLE READ (по умолчанию в MySQL)
- Повторный `SELECT` в транзакции видит те же данные (снимок на момент начала)    
- Но могут появиться новые строки (phantom)    
- **Тяжелее для БД**   
#### SERIALIZABLE (самый строгий)
- Полная изоляция — как будто транзакции выполняются последовательно    
- Может быть много конфликтов и блокировок    
- Самая низкая производительность    

---
## Другие параметры транзакций

#### READ ONLY / READ WRITE
```sql
BEGIN TRANSACTION READ ONLY;
```
- Запрещает изменения данных    
- Оптимизация для БД    

#### DEFERRABLE (только в PostgreSQL, только с SERIALIZABLE)
```sql
BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE, DEFERRABLE;
```
- Транзакция может ждать, чтобы гарантировать сериализуемость без ошибок

---
## Примеры использования
```sql
-- 1. READ COMMITTED (по умолчанию в PostgreSQL)
BEGIN;
SELECT * FROM accounts WHERE id = 1;
COMMIT;
-- 2. REPEATABLE READ
BEGIN ISOLATION LEVEL REPEATABLE READ;
SELECT balance FROM accounts WHERE id = 1;
-- ... какой-то код
SELECT balance FROM accounts WHERE id = 1; -- то же значение
COMMIT;
-- 3. SERIALIZABLE + только чтение
BEGIN ISOLATION LEVEL SERIALIZABLE, READ ONLY;
SELECT SUM(amount) FROM payments;
COMMIT;
```

---
## Какой уровень выбрать (таблица)

|Сценарий|Рекомендуемый уровень|
|---|---|
|Отчёты, аналитика|`READ COMMITTED` или `REPEATABLE READ`|
|Финансовые операции|`SERIALIZABLE`|
|Высоконагруженные транзакции|`READ COMMITTED`|
|Данные, которые не должны меняться за время чтения|`REPEATABLE READ`|

---
## Важное отличие PostgreSQL
В PostgreSQL `REPEATABLE READ` **не допускает Phantom Read** 
	(в отличие от стандарта SQL). То есть в PG:
- `REPEATABLE READ` = нет Dirty Read, нет Non-Repeatable Read, нет Phantom Read    
- `SERIALIZABLE` — ещё строже (ловит другие конфликты, например, read-write)    

---
## Итог (самое главное)

> **По умолчанию почти везде — `READ COMMITTED`** (хороший баланс).  
> **Если нужны точные повторяемые чтения — `REPEATABLE READ`**.  
> **Если критично отсутствие любых аномалий — `SERIALIZABLE`**, но готовься к блокировкам.

---
