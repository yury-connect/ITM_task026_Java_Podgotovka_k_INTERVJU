# Задачка: 
## Дана таблица `employees(id, name, department, salary)`. Нужно вывести `name` и `salary` сотрудников из <u>отдела 'IT'</u>, отсортировать по зарплате по убыванию и взять <u>первых 3</u> из <u>второго десятка</u>.

---
Для решения задачи нужно выбрать сотрудников отдела **IT**, отсортировать их по зарплате **по убыванию**, пропустить **первые 10 записей** (первый десяток) и взять **следующие 3** (второй десяток).

В разных СУБД синтаксис немного отличается. Вот варианты:

### 1. **PostgreSQL, MySQL 8+, H2** (используем `OFFSET` и `FETCH`)
```sql
SELECT name, salary
FROM employees
WHERE department = 'IT'
ORDER BY salary DESC
OFFSET 10 ROWS
FETCH NEXT 3 ROWS ONLY;
```

### 2. **MySQL (старые версии)**
```sql
SELECT name, salary
FROM employees
WHERE department = 'IT'
ORDER BY salary DESC
LIMIT 3 OFFSET 10;
```
или
```sql
SELECT name, salary
FROM employees
WHERE department = 'IT'
ORDER BY salary DESC
LIMIT 10, 3;  -- сначала OFFSET, потом LIMIT
```

### 3. **Oracle**
```sql
SELECT name, salary
FROM (
    SELECT name, salary, ROWNUM AS rn
    FROM (
        SELECT name, salary
        FROM employees
        WHERE department = 'IT'
        ORDER BY salary DESC
    )
    WHERE ROWNUM <= 13  -- ограничиваем 13-ю (10 + 3)
)
WHERE rn > 10;          -- отсекаем первые 10
```

### 4. **SQL Server**
```sql
SELECT name, salary
FROM employees
WHERE department = 'IT'
ORDER BY salary DESC
OFFSET 10 ROWS
FETCH NEXT 3 ROWS ONLY;  -- аналогично PostgreSQL
```

### Результат:
Будут выведены **3 сотрудника** из отдела IT с **11-го по 13-е место** в рейтинге зарплат (по убыванию).

---
[Перейти к списку заданий](ЗАДАНИЯ.md)
