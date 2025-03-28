


<details>
        <summary>1. Что такое DDL? Какие операции в него входят? Рассказать про них.</summary>

## Что такое DDL? Какие операции в него входят? Рассказать про них.

```text
***** из методички *****
```
---
</details>



<details>
        <summary>2. Что такое DML? Какие операции в него входят? Рассказать про них.</summary>

## Что такое DML? Какие операции в него входят? Рассказать про них.

```text
***** из методички *****
```
---
</details>



<details>
        <summary>3. Что такое TCL? Какие операции в него входят? Рассказать про них.</summary>

## Что такое TCL? Какие операции в него входят? Рассказать про них.

```text
***** из методички *****
```
---
</details>



<details>
        <summary>4. Что такое DCL? Какие операции в него входят? Рассказать про них.</summary>

## Что такое DCL? Какие операции в него входят? Рассказать про них.

```text
***** из методички *****
```
---
</details>



<details>
        <summary>5. Нюансы работы с NULL в SQL. Как проверить поле на NULL?</summary>

## Нюансы работы с NULL в SQL. Как проверить поле на NULL?

```text
***** из методички *****
```
---
</details>



<details>
        <summary>6. Виды Join’ов?</summary>

## Виды Join’ов?

![Виды Join’ов](/ITM/ITM04_SQL/imgs/2025-03-28_17-08-34.png)

```text
***** из методички *****
```
---
</details>



<details>
        <summary>7. Что лучше использовать join или подзапросы? Почему?</summary>

## Что лучше использовать join или подзапросы? Почему?

```text
***** из методички *****
```
---
</details>



<details>
        <summary>8. Что делает UNION?</summary>

## Что делает UNION?

![UNION img](/ITM/ITM04_SQL/imgs/2025-03-28_17-23-08.png)

```text
***** из методички *****
```
---
</details>



<details>
        <summary>9. Чем WHERE отличается от HAVING_ ?</summary>

## Чем WHERE отличается от HAVING _(ответа про то что используются в разных частях запроса - недостаточно)_?

```text
***** из методички *****
```
---
</details>



<details>
        <summary>10. Что такое ORDER BY?</summary>

## Что такое ORDER BY?

```text
***** из методички *****
```
---
</details>



<details>
        <summary>11. Что такое DISTINCT?</summary>

## Что такое DISTINCT?

```text
***** из методички *****
Позволяет ограничить количество выводимых записей. После FROM
```
---
</details>



<details>
        <summary>12. Что такое GROUP BY?</summary>

## Что такое GROUP BY?

```text
***** из методички *****
```
---
</details>



<details>
        <summary>+ 13. Что такое LIMIT?</summary>

## Что такое LIMIT?

`LIMIT` – для ограничения количества выводимых записей в результате запроса.   
`OFFSET` - пропустить заданное кол-во записей

```sql
SELECT * FROM table LIMIT 10 OFFSET 5; -- Пропустить 5 записей, вывести 10
```

```text
***** из методички *****
Позволяет ограничить количество выводимых записей. После FROM
```
---
</details>



<details>
        <summary>+ 14. Что такое EXISTS?</summary>

## Что такое EXISTS?

`EXISTS` – логический оператор в SQL, который проверяет наличие записей в подзапросе.  

🔹 Возвращает **TRUE**, если подзапрос содержит **хотя бы одну** строку.  
🔹 Возвращает **FALSE**, если подзапрос **не возвращает данных**.  
🔹 Используется для оптимизации запросов, особенно с **WHERE** или **NOT EXISTS**.  

Пример:

```sql
SELECT * FROM users u WHERE EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.id);
```

✅ Выведет пользователей, у которых есть заказы.  

```text
***** из методички *****
EXISTS берет подзапрос, как аргумент, и оценивает его как TRUE, 
если подзапрос возвращает какие-либо записи и FALSE, если нет. 

Возвращает значение TRUE, 
если вложенный запрос содержит хотя бы одну строку
```
---
</details>



<details>
        <summary>+ 15.Расскажите про операторы IN, BETWEEN, LIKE.</summary>

## Расскажите про операторы `IN`, `BETWEEN`, `LIKE`.

```text
***** из методички *****
•        IN - определяет наличие данных в масиве.
SELECT * FROM Persons WHERE name IN ('Ivan','Petr','Pavel');

•        BETWEEN определяет диапазон значений. В отличие от IN, 
BETWEEN чувствителен к порядку, и первое значение в предложении 
должно быть первым по алфавитному или числовому порядку.
SELECT * FROM Persons WHERE age BETWEEN 20 AND 25;

•        LIKE применим только к полям типа CHAR или VARCHAR, 
с которыми он используется чтобы находить подстроки. 
В качестве условия используются символы шаблонизации (wildkards) 
- специальные символы, которые могут соответствовать чему-нибудь: 
    % Любая строка, содержащая ноль или более символов 
    _ (подчеркивание) Любой одиночный символ. 
        Например, 'b_t' будет соответствовать словам 'bat' 
        или 'bit', но не будет соответствовать 'brat'.
    % замещает последовательность любого числа символов. 
        Например '%p%t' будет соответствовать словам 
        'put', 'posit', или 'opt', но не 'spite'.
SELECT * FROM UNIVERSITY WHERE NAME LIKE '%o';
```
---
</details>



<details>
        <summary>+ 16. Что делает оператор MERGE? Какие у него есть ограничения?</summary>

## Что делает оператор `MERGE`? Какие у него есть ограничения?

### ✅ `MERGE` - объединить (_слияние_) данные из одной таблицы с другой на основе условия (`ON`). 
В зависимости от соответствия выполняется:   
✔ `UPDATE`, если запись найдена в обеих таблицах.   
✔ `INSERT`, если запись отсутствует в целевой таблице.   
✔ _(Дополнительно)_ `DELETE`, если запись есть в целевой, но нет в источнике.   

### 📌 Ограничения `MERGE`
* ❌ **Нельзя **изменять поля из `ON`** — вызывает ошибки.  
* 🔄 Дубликаты** в источнике → ошибка.  
* 🐢 **Производительность** — требует индексы для ускорения.  
* 🔍 **Race Condition** — при параллельных изменениях возможны ошибки.  
* 🚫 **Ограниченная поддержка** — нет в **MySQL**, **SQLite**.  

### 📌 Использовать `MERGE`, когда:
* Требуется **объединить** данные из разных таблиц.   
* Нужно выполнять **обновление + вставку** в одном запросе.   

---
### 📊 Синтаксис (SQL Server, Oracle, PostgreSQL 15+):
```sql
MERGE INTO TargetTable AS t
USING SourceTable AS s
ON (t.id = s.id)  
WHEN MATCHED THEN  
    UPDATE SET t.value = s.value  
WHEN NOT MATCHED THEN  
    INSERT (id, value) VALUES (s.id, s.value)  
WHEN NOT MATCHED BY SOURCE THEN  
    DELETE;  -- (опционально) удаление записей, которых нет в источнике
```
---
```text
***** из методички *****
MERGE позволяет осущ-ть слияние данных 1й таблицы с данными 2й таблицы. 
При слиянии таблиц проверяется условие, и если оно истинно, 
то выполняется UPDATE, а если нет - INSERT. 

При этом изменять поля таблицы в секции UPDATE, 
по которым идет связывание двух таблиц, нельзя.


MERGE Ships AS t  -- таблица, которая будет меняться
USING (SELECT запрос ) AS s ON (t.name = s.ship)  -- условие слияния
    THEN UPDATE SET t.launched = s.year -- обновление
WHEN NOT MATCHED -- если условие не выполняется
    THEN INSERT VALUES(s.ship, s.year) -- вставка
        
        
MERGE dbo.TestTable AS T_Base --Целевая таблица 
USING dbo.TestTableDop AS T_Source --Таблица источник 
ON (T_Base.ProductId = T_Source.ProductId) --Условие объединения 
WHEN MATCHED THEN --Если истина (UPDATE) 
    UPDATE SET 
    ProductName = T_Source.ProductName, 
    Summa = T_Source.Summa 
WHEN NOT MATCHED THEN --Если НЕ истина (INSERT) 
    INSERT (ProductId, ProductName, Summa) 
    VALUES (T_Source.ProductId, T_Source.ProductName, T_Source.Summa) 
```
---
</details>



<details>
        <summary>17. Какие агрегатные функции вы знаете?</summary>

## Какие агрегатные функции вы знаете?

```text
***** из методички *****
```
---
</details>



<details>
        <summary>18. Что такое ограничения (constraints)? Какие вы знаете?</summary>

## Что такое ограничения (constraints)? Какие вы знаете?

```text
***** из методички *****
```
---
</details>



<details>
        <summary>+ 19. Что такое суррогатные ключи?</summary>

## Что такое суррогатные ключи?
**Определение**: Искусственно созданное, автоинкрементное поле для уникальной идентификации записи.

**Назначение**: Заменяет естественный ключ, не связанный с бизнес-данными.

**Преимущества**:
* Простота и стабильность _(не меняется при обновлении содержательных данных)_.
* Удобство при создании связей между таблицами.

\\* В таблице может быть **только один** первичный ключ. 
Если используется **суррогатный ключ**, то его следует назначить как **основной**, 
а прежний _естественный_ ключ можно определить как _уникальное ограничение (уникальный ключ)_,

---
Пример:
```sql
CREATE TABLE Employees (
    EmployeeID INT AUTO_INCREMENT PRIMARY KEY, -- Суррогатный ключ
    FirstName VARCHAR(50),
    LastName VARCHAR(50)
);
```

В этом примере поле `EmployeeID` автоматически генерируется 
и служит уникальным идентификатором каждой записи.

---

```text
***** из методички *****
    Суррога́тный ключ — это дополнительное служебное поле, 
автоматически добавленное к уже имеющимся информационным 
полям таблицы, предназначение которого — служить первичным ключом. 
 
В качестве первичного ключа может использоваться:
* Естественный Ключ (ЕК) – набор атрибутов описываемой записью сущности, 
    уникально её идентифицирующий 
    (например, номер паспорта для человека) или
* Суррогатный Ключ (СК) – автоматически сгенерированное поле, 
    никак не связанное с информационным содержанием записи. 
    
Обычно в роли СК выступает автоинкрементное поле типа INTEGER.
```
---
</details>



<details>
        <summary>20. Что такое индексы? Какие они бывают?</summary>

## Что такое индексы? Какие они бывают?

```text
***** из методички *****
Индексы относятся к методу настройки производительности, позволяющему быстрее извлекать записи из таблицы. Индекс создает структуру для индексируемого поля. Необходимо просто добавить указатель индекса в таблицу.                                    Содержит структуры бинарных деревьев

Есть три типа индексов, а именно:

Уникальный индекс (Unique Index): этот индекс не позволяет полю иметь повторяющиеся значения. Если первичный ключ определен, уникальный индекс применен автоматически.
Кластеризованный индекс (Clustered Index): Кластеризованный индекс хранит реальные строки данных в листьях индекса. Возвращаясь к предыдущему примеру, это означает что строка данных, связанная со значение ключа, равного 123 будет храниться в самом индексе. Важной характеристикой кластеризованного индекса является то, что все значения отсортированы в определенном порядке либо возрастания, либо убывания. Таким образом, таблица или представление может иметь только один кластеризованный индекс. В дополнение следует отметить, что данные в таблице хранятся в отсортированном виде только в случае если создан кластеризованный индекс у этой таблицы.
Таблица не имеющая кластеризованного индекса называется кучей.
Некластеризованный индекс (Non-Clustered Index): Создаются только после клатерного индекса, создаются автоматически при объявлении столбца UNIQUE.В отличие от кластеризованного индекса, листья некластеризованного индекса содержат только те столбцы (ключевые), по которым определен данный индекс, а также содержит указатель на строки с реальными данными в таблице. Это означает, что системе подзапросов необходима дополнительная операция для обнаружения и получения требуемых данных. Содержание указателя на данные зависит от способа хранения данных: кластеризованная таблица или куча. Если указатель ссылается на кластеризованную таблицу, то он ведет к кластеризованному индексу, используя который можно найти реальные данные.



Как создать индекс? b3
Индекс можно создать либо с помощью выражения CREATE INDEX:
CREATE INDEX index_name ON table_name (column_name)
либо указав ограничение целостности в виде уникального UNIQUE или первичного PRIMARY ключа в операторе создания таблицы CREATE TABLE.



Имеет ли смысл индексировать данные, имеющие небольшое количество возможных значений?
Примерное правило, которым можно руководствоваться при создании индекса - если объем информации (в байтах) НЕ удовлетворяющей условию выборки меньше, чем размер индекса (в байтах) по данному условию выборки, то в общем случае оптимизация приведет к замедлению выборки.Часто выполняем запросы тогда имеет смысл индексировать. Но потеряем скорость в UPDATE в INSERT и DELETE



Когда полное сканирование набора данных выгоднее доступа по индексу?
Полное сканирование производится многоблочным чтением. Сканирование по индексу - одноблочным. Также, при доступе по индексу сначала идет сканирование самого индекса, а затем чтение блоков из набора данных. Число блоков, которые надо при этом прочитать из набора зависит от фактора кластеризации. Если суммарная стоимость всех необходимых одноблочных чтений больше стоимости полного сканирования многоблочным чтением, то полное сканирование выгоднее и оно выбирается оптимизатором.
Таким образом, полное сканирование выбирается при слабой селективности предикатов зароса и/или слабой кластеризации данных, либо в случае очень маленьких наборов данных.



```
---
</details>



<details>
        <summary>21. Чем TRUNCATE отличается от DELETE?</summary>

## Чем TRUNCATE отличается от DELETE?

```text
***** из методички *****
```
---
</details>



<details>
        <summary>22. Что такое хранимые процедуры? Для чего они нужны?</summary>

## Что такое хранимые процедуры? Для чего они нужны?

```text
***** из методички *****
```
---
</details>



<details>
        <summary>23. Что такое представления (VIEW)? Для чего они нужны?</summary>

## Что такое представления (VIEW)? Для чего они нужны?

```text
***** из методички *****
```
---
</details>



<details>
        <summary>24. Что такое временные таблицы? Для чего они нужны?</summary>

## Что такое временные таблицы? Для чего они нужны?

```text
***** из методички *****
```
---
</details>



<details>
        <summary>25. Что такое транзакции? Расскажите про принципы ACID.</summary>

## Что такое транзакции? Расскажите про принципы ACID.

```text
***** из методички *****
```
---
</details>



<details>
        <summary>26. Расскажите про уровни изолированности транзакций.</summary>

## Расскажите про уровни изолированности транзакций.

```text
***** из методички *****
```
---
</details>



<details>
        <summary>27. Что такое нормализация и денормализация? Расскажите про 3 нормальные формы</summary>

## Что такое нормализация и денормализация? Расскажите про 3 нормальные формы??

```text
***** из методички *****
```
---
</details>



<details>
        <summary>28. Что такое TIMESTAMP?</summary>

## Что такое TIMESTAMP?

```text
***** из методички *****
```
---
</details>



<details>
        <summary>29. Шардирование БД</summary>

## Шардирование БД

```text
***** из методички *****
```
---
</details>



<details>
        <summary>30. EXPLAIN</summary>

## EXPLAIN

```text
***** из методички *****
```
---
</details>



<details>
        <summary>31. Как сделать запрос из двух баз?</summary>

## Как сделать запрос из двух баз?

```text
***** из методички *****
```
---
</details>



<details>
        <summary>32. Что быстрее убирает дубликаты distinct или group by?</summary>

## Что быстрее убирает дубликаты distinct или group by?

```text
***** из методички *****
```
---
</details>



<details>
        <summary>33. Механизмы оптимизации запросов в БД</summary>

## Механизмы оптимизации запросов в БД

```text
***** из методички *****
```
---
</details>



<details>
        <summary>34. Что такое «триггер»?</summary>

## Что такое «триггер»?

```text
***** из методички *****
```
---
</details>


---



<details>
        <summary>SQL: Порядок выполнения ...</summary>

![Порядок выполнения оконных функций в SELECT](/ITM/ITM04_SQL/imgs/2025-03-28_21-24-09.png)

---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



