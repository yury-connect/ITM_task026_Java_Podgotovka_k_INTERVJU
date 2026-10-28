## 9/20: Where will the selection results be after executing of the following command?

```sql
SELECT R3.NZK, R3.DTS, R3.BALL, R3.SEM2 AS SEM1
FROM R3 INTO TABLE SEMESTR
```

- To the table SEMESTR    
- To the table SEM1    
- Results will be displayed    
- To the request SEMESTR    

---
## 9/20: Где окажутся результаты выборки после выполнения следующей команды?

- В таблицу SEMESTR    
- В таблицу SEM1    
- Результаты будут отображены (на экране)    
- В запрос SEMESTR

---
Правильный ответ: **To the table SEMESTR** (первый вариант).

**Объяснение:**

Этот вопрос относится к синтаксису SQL (в частности, к диалекту T-SQL, используемому в Microsoft SQL Server).

Конструкция `INTO` в операторе `SELECT` используется для создания новой таблицы и вставки в нее результатов запроса.

В данном коде:
```sql
SELECT R3.NZK, R3.DIS, R3.BALL, R3.SEM2 AS SEM1
FROM R3 INTO TABLE SEMESTR
```

- Команда `SELECT ... INTO TABLE SEMESTR` берет выбранные столбцы из таблицы `R3` и **сохраняет эти результаты в новую таблицу с именем `SEMESTR`**.
    
- `AS SEM1` — это просто переименование столбца (алиас) в результирующем наборе данных (в новой таблице столбец будет называться `SEM1`), но это не меняет места сохранения данных.    

#### Остальные варианты неверны:

- _To the table SEM1_ — `SEM1` это имя столбца, а не таблицы.
    
- _Results will be displayed_ — при использовании `INTO` результаты не выводятся на экран, а сохраняются в таблицу.
    
- _To the request SEMESTR_ — такого понятия в контексте SQL-запросов нет.

---
