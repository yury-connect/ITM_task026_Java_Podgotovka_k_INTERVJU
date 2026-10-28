## 20/20: Which SQL statement is used to return only different values?

- SELECT DISTINCT    
- SELECT UNIQUE    
- SELECT DIFFERENT    

---
## 20/20: Какой оператор SQL используется для возврата только различных (уникальных) значений?

- SELECT DISTINCT    
- SELECT UNIQUE    
- SELECT DIFFERENT

---
Правильный ответ: **SELECT DISTINCT** (первый вариант).

**Объяснение:**

В SQL для того, чтобы получить только уникальные (различные) значения из столбца и отфильтровать дубликаты, используется ключевое слово **`DISTINCT`** после `SELECT`.

- **`SELECT DISTINCT`** — правильный синтаксис. Например: `SELECT DISTINCT city FROM customers;` вернет список городов без повторений.
    
- **`SELECT UNIQUE`** — не является стандартом SQL (хотя в некоторых устаревших версиях Oracle это работало как синоним `DISTINCT`, в стандартном SQL это неверно).
    
- **`SELECT DIFFERENT`** — такой команды в SQL не существует.

---
