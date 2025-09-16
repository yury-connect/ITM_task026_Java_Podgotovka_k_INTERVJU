# SQL-задача с подвохом: NULL ловушка


🛢️ SQL-задача с подвохом: NULL ловушка

Условие:
Есть таблица employees:

| id  | name     | department |
|-----|----------|------------|
| 1   | Alice    | Sales      |
| 2   | Bob      | NULL       |
| 3   | Charlie  | HR         |
| 4   | Diana    | NULL       |
| 5   | Eve      | Sales      |

Ты хочешь выбрать всех сотрудников, которые не работают в отделе Sales. Пишешь простой запрос:
```sql
SELECT * FROM employees
WHERE department <> 'Sales';
```

❓ Вопрос:  
Какие строки вернёт этот запрос? Почему результат может удивить даже опытных специалистов?

---

🔍 Разбор:

На первый взгляд логика понятна: мы хотим исключить сотрудников из отдела Sales. Кажется, что должны вернуться:

- Bob (NULL)
- Charlie (HR)
- Diana (NULL)

Но вот главный подвох: NULL — это "неизвестное значение", и в SQL любые сравнения с NULL дают UNKNOWN.

Запрос:
```sql
WHERE department <> 'Sales'
```

- Charlie (HR): ✅ вернётся, потому что HR <> Sales.  
- Bob (NULL): ❌ НЕ вернётся, потому что NULL <> 'Sales' даёт UNKNOWN (не TRUE).  
- Diana (NULL): ❌ по той же причине.  
- Alice и Eve: ❌ потому что у них Sales.

---

✅ Фактический результат:

| id  | name    | department |
|-----|---------|------------|
| 3   | Charlie | HR         |

---

💥 Подвох:

Многие думают, что NULL автоматически участвует в сравнении как будто это "значение", но SQL строго следует трёхзначной логике:

- TRUE
- FALSE
- UNKNOWN

В WHERE фильтре остаются только строки, где условие = TRUE. Строки с NULL дают UNKNOWN и отбрасываются.

---

🛠 Как исправить запрос, чтобы включить сотрудников без отдела (NULL):
```sql
SELECT * FROM employees
WHERE department <> 'Sales' OR department IS NULL;
```

Теперь вернётся:

| id  | name    | department |
|-----|---------|------------|
| 2   | Bob     | NULL       |
| 3   | Charlie | HR         |
| 4   | Diana   | NULL       |

---

✅ Вывод:

• В SQL сравнения с NULL всегда возвращают UNKNOWN.  
• Обычные условия (`<>`, =, >, <`) **не учитывают NULL правильно**, если явно не проверить `IS NULL или IS NOT NULL.  
• Даже простой фильтр может дать неожиданный результат, если в данных есть пропуски.

💡 Бонус-вопрос:  
Что будет, если использовать NOT department = 'Sales' вместо department <> 'Sales'? 😉

➡ SQL Community (https://t.me/+2zyWagcp1DxjNGFh) |



