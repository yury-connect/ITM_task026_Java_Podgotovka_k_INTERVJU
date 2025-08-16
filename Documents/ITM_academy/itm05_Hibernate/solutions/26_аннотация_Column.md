# Для чего нужна аннотация Column?

---
## Для чего нужна аннотация Column?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%B4%D0%BB%D1%8F-%D1%87%D0%B5%D0%B3%D0%BE-%D0%BD%D1%83%D0%B6%D0%BD%D0%B0-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D1%8F-column)

### 🧱 Для чего нужна аннотация `@Column` в JPA?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B4%D0%BB%D1%8F-%D1%87%D0%B5%D0%B3%D0%BE-%D0%BD%D1%83%D0%B6%D0%BD%D0%B0-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D1%8F-column-%D0%B2-jpa)

Аннотация `@Column` используется для **сопоставления поля класса со столбцом таблицы БД** и настройки поведения этого столбца при генерации схемы базы данных.

> 📌 Если `@Column` не указана, используются значения по умолчанию.

---

### ⚙️ Основные параметры `@Column`

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%EF%B8%8F-%D0%BE%D1%81%D0%BD%D0%BE%D0%B2%D0%BD%D1%8B%D0%B5-%D0%BF%D0%B0%D1%80%D0%B0%D0%BC%D0%B5%D1%82%D1%80%D1%8B-column)

|**Атрибут**|**Тип / Значение по умолчанию**|**Назначение**|
|---|---|---|
|`name`|Имя поля|Имя столбца в БД|
|`nullable`|`true`|Может ли столбец содержать `NULL`|
|`unique`|`false`|Уникальность значений в столбце|
|`length`|`255`|Длина для строковых типов|
|`insertable`|`true`|Участвует ли столбец при `INSERT`|
|`updatable`|`true`|Участвует ли столбец при `UPDATE`|

---

### 🔍 Сравнение `@Basic` vs `@Column`

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D1%81%D1%80%D0%B0%D0%B2%D0%BD%D0%B5%D0%BD%D0%B8%D0%B5-basic-vs-column)

|**Аспект**|`@Basic`|`@Column`|
|---|---|---|
|Применение|Поля сущности|Столбцы таблицы БД|
|Управление `null`|`optional` (_Java уровень_)|`nullable` (_БД уровень_)|
|Загрузка|`fetch` (_EAGER / LAZY_)|Не управляется|
|Настройки схемы|❌|✅ (_имя, длина, уникальность и др._)|

---

### 🧠 Вывод:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B2%D1%8B%D0%B2%D0%BE%D0%B4-5)

- `@Basic` — управляет **поведением данных в памяти** (fetch, optional).
- `@Column` — управляет **структурой и ограничениями в БД** (name, nullable, length и пр.).

---

### 💡 Пример:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80)

```java
@Column(name = "STUDENT_NAME", length = 50, nullable = false, unique = false)
private String name;
```

---

```
***** из методички *****
 @Column сопоставляет поле класса столбцу таблицы, а её атрибуты определяют поведение в этом столбце, используется для генерации схемы базы данных
 
 @Basic vs @Column:
1. Атрибуты @Basic применяются к сущностям JPA, тогда как атрибуты @Column применяются к столбцам базы данных.
2. @Basic имеет атрибут optional, который говорит о том, может ли поле объекта быть null или нет; с другой стороны атрибут nullable аннотации @Column указывает, может ли соответствующий столбец в таблице быть null.
3. Мы можем использовать @Basic, чтобы указать, что поле должно быть загружено лениво.
4. Аннотация @Column позволяет нам указать имя столбца в таблице и ряд других свойств:
 a. insertable/updatable - можно ли добавлять/изменять данные в колонке, по умолчанию true;
 b. length - длина, для строковых типов данных, по умолчанию 255.
Коротко, в Column (колум) мы задаем constraints (констрейнтс), а в Basic (бейсик) - ФЕТЧ ТАЙП

    @Column(name="STUDENT_NAME", length=50, nullable=false, unique=false)
    private String name;
```
