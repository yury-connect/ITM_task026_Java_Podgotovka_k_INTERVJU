# Расскажите про аннотации @JoinColumn и @JoinTable? Где и для чего они используются?

---
## Расскажите про аннотации `@JoinColumn` и `@JoinTable`? Где и для чего они используются?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D1%80%D0%B0%D1%81%D1%81%D0%BA%D0%B0%D0%B6%D0%B8%D1%82%D0%B5-%D0%BF%D1%80%D0%BE-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D0%B8-joincolumn-%D0%B8-jointable-%D0%B3%D0%B4%D0%B5-%D0%B8-%D0%B4%D0%BB%D1%8F-%D1%87%D0%B5%D0%B3%D0%BE-%D0%BE%D0%BD%D0%B8-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D1%83%D1%8E%D1%82%D1%81%D1%8F)

## 🔗 Аннотации JPA: `@JoinColumn`, `@JoinColumns`, `@JoinTable`

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D0%B8-jpa-joincolumn-joincolumns-jointable)

### 📌 Общая идея

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BE%D0%B1%D1%89%D0%B0%D1%8F-%D0%B8%D0%B4%D0%B5%D1%8F)

Аннотации `@JoinColumn`, `@JoinColumns` и `@JoinTable` применяются в JPA/Hibernate для настройки **связей между сущностями** и управления созданием **внешних ключей** и **соединительных таблиц** в базе данных.

---

## 🔸 `@JoinColumn`

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-joincolumn)

> Используется для указания внешнего ключа (**_FK_**), связывающего текущую сущность с другой.

### 💡 Применение:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D0%BD%D0%B5%D0%BD%D0%B8%D0%B5)

```java
@JoinColumn(name = "author_id")
private Author author;
```

### 📋 Поведение:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D0%BE%D0%B2%D0%B5%D0%B4%D0%B5%D0%BD%D0%B8%D0%B5)

- Применяется на **владеющей стороне связи**.
- Создаёт **внешний ключ** в таблице этой сущности.
- Используется с аннотациями: `@OneToOne`, `@ManyToOne`, `@OneToMany` (_иногда_).

### 🧩 Где появляется столбец?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B3%D0%B4%D0%B5-%D0%BF%D0%BE%D1%8F%D0%B2%D0%BB%D1%8F%D0%B5%D1%82%D1%81%D1%8F-%D1%81%D1%82%D0%BE%D0%BB%D0%B1%D0%B5%D1%86)

- **В таблице владельца**, даже если `@JoinColumn` указана на обеих сторонах.

---

## 🔸 `@JoinColumns`

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-joincolumns)

> **Используется для составных внешних ключей, когда внешний ключ состоит из нескольких колонок.**

### 💡 Применение:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D0%BD%D0%B5%D0%BD%D0%B8%D0%B5-1)

```java
@JoinColumns({
  @JoinColumn(name = "dept_id", referencedColumnName = "id"),
  @JoinColumn(name = "dept_code", referencedColumnName = "code")
})
private Department department;
```

### 🧠 Особенности:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BE%D1%81%D0%BE%D0%B1%D0%B5%D0%BD%D0%BD%D0%BE%D1%81%D1%82%D0%B8-2)

- Используется, когда **PK** или **FK** составной (_несколько столбцов_).
- Каждая `@JoinColumn` указывает `name` (_столбец в текущей таблице_) и `referencedColumnName` (_столбец во внешней таблице_).

---

## 🔸 `@JoinTable`

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-jointable)

> **Используется для создания промежуточной (_сводной_) таблицы, особенно в `@ManyToMany`.**

### 💡 Применение:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D0%BD%D0%B5%D0%BD%D0%B8%D0%B5-2)

```java
@JoinTable(
name = "catalog",
joinColumns = @JoinColumn(name = "id_book"),
inverseJoinColumns = @JoinColumn(name = "id_student")
)
private Student student;
```

### 📋 Поведение:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D0%BE%D0%B2%D0%B5%D0%B4%D0%B5%D0%BD%D0%B8%D0%B5-1)

- Определяет имя сводной таблицы и пары `joinColumns` / `inverseJoinColumns`.
- Используется в связях:
    - `@ManyToMany` (_обязательно_)
    - Иногда в `@OneToMany`, если нужна отдельная таблица

---

## 📘 Сводная таблица по аннотациям

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D1%81%D0%B2%D0%BE%D0%B4%D0%BD%D0%B0%D1%8F-%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D0%B0-%D0%BF%D0%BE-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D1%8F%D0%BC)

|**Аннотация**|**Назначение**|**Когда использовать**|**Где создается FK/таблица**|
|---|---|---|---|
|`@JoinColumn`|Один внешний ключ|`@OneToOne`, `@ManyToOne`|В таблице владельца|
|`@JoinColumns`|Несколько внешних ключей  <br>(_составной **FK**_)|Составной ключ|В таблице владельца|
|`@JoinTable`|Таблица-связка|`@ManyToMany`, иногда `@OneToMany`|Отдельная промежуточная таблица|

---

## ✅ Резюме

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D1%80%D0%B5%D0%B7%D1%8E%D0%BC%D0%B5)

🔹 `@JoinColumn` — один внешний ключ.

🔹 `@JoinColumns` — составной внешний ключ.

🔹 `@JoinTable` — создаёт таблицу-связку.

> 🧠 Только владеющая сторона определяет внешний ключ или таблицу-связку. Важно грамотно указывать `mappedBy`, чтобы избежать дублирования.

---

```
***** из методички *****
 @JoinColumn используется для указания столбца FOREIGN KEY, используемого при установлении связей между сущностями или коллекциями. Мы помним, что только сущность-владелец связи может иметь внешние ключи от другой сущности (владеемой). Однако, мы можем указать @JoinColumn как во владеющей таблице, так и во владеемой, но столбец с внешними ключами всё равно появится во владеющей таблице. 
Особенности использования:

❖        @OneToOne: означает, что появится столбец в таблице сущности-владельца связи, который будет содержать внешний ключ, ссылающийся на первичный ключ владеемой сущности.

❖        @OneToMany/@ManyToOne: если не указать на владеемой стороне связи атрибут mappedBy, создается joinTable с ключами обеих таблиц. Но при этом же у владельца создается столбец с внешними ключами.

 @JoinColumns используется для группировки нескольких аннотаций @JoinColumn, которые используются при установлении связей между сущностями или коллекциями, у которых составной первичный ключ и требуется несколько колонок для указания внешнего ключа.
В каждой аннотации @JoinColumn должны быть указаны элементы name и referencedColumnName.

 @JoinTable используется для указания связывающей (сводной, третьей) таблицы между двумя другими таблицами.                                @JoinTable(name = "CATALOG", joinColumns = @JoinColumn(name = "ID_BOOK"), inverseJoinColumns = @JoinColumn(name = "ID_STUDENT"))
 private Student student;
```
