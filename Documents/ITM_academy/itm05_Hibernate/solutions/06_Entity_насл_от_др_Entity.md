# Может ли Entity класс наследоваться от других Entity классов?

---
### ✅ Да, может.

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B4%D0%B0-%D0%BC%D0%BE%D0%B6%D0%B5%D1%82)

Но с этим связано использование **наследования в JPA/Hibernate**, и тут важно выбрать правильную стратегию.

---

### 🧬 Основные стратегии JPA-наследования (`@Inheritance`)

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BE%D1%81%D0%BD%D0%BE%D0%B2%D0%BD%D1%8B%D0%B5-%D1%81%D1%82%D1%80%D0%B0%D1%82%D0%B5%D0%B3%D0%B8%D0%B8-jpa-%D0%BD%D0%B0%D1%81%D0%BB%D0%B5%D0%B4%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F-inheritance)

|**Стратегия**|`JOIN`'ы|**Производительность**|**Описание** / Когда применять?|
|---|---|---|---|
|🟨 `SINGLE_TABLE` _(по умолчанию)_|❌|🔥 Высокая|Все сущности — в **одной таблице**, используется `@DiscriminatorColumn`  <br>/Когда **простая иерархия**, немного полей|
|🟦 `JOINED`|✅|🐢 Средняя|Каждая сущность — в своей таблице, соединяются по внешнему ключу  <br>/Когда нужна **нормализованная БД** и читаемость|
|🟪 `TABLE_PER_CLASS`|❌|🐌 Низкая|Каждая сущность — своя таблица, **без объединения**  <br>/Когда каждая сущность **отдельна** и **независима**|
|**Стратегия**|**Описание**|||
|---------------------------------|-------------------------------------------------------------------------|||
|`SINGLE_TABLE` _(по умолчанию)_|Все сущности — в **одной таблице**, используется `@DiscriminatorColumn`|||
|`JOINED`|Каждая сущность — в своей таблице, соединяются по внешнему ключу|||
|`TABLE_PER_CLASS`|Каждая сущность — своя таблица, **без объединения**|||

### 💡 Как выбрать стратегию?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BA%D0%B0%D0%BA-%D0%B2%D1%8B%D0%B1%D1%80%D0%B0%D1%82%D1%8C-%D1%81%D1%82%D1%80%D0%B0%D1%82%D0%B5%D0%B3%D0%B8%D1%8E)

🔹 Ты — **практичный ремесленник** → бери `SINGLE_TABLE`. Всё в одной куче, быстро, легко.  
🔹 Ты — **чистоплотный архитектор** → `JOINED`. Всё по полочкам, без `null`'ов, красиво.  
🔹 Ты — **максимальный индивидуалист** → `TABLE_PER_CLASS`. Полная свобода, но будь готов к трудностям с запросами.

### 🛠️ Аннотации

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%EF%B8%8F-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D0%B8)

- `@Inheritance(strategy = ...)` — на родительском `@Entity` классе
- `@DiscriminatorColumn` — (для `SINGLE_TABLE`) для различения подклассов
- `@DiscriminatorValue` — для указания значения конкретной сущности
- `@PrimaryKeyJoinColumn` — (для `JOINED`) связывает родителя и потомка

---

### 📌 Вывод

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B2%D1%8B%D0%B2%D0%BE%D0%B4-4)

|**Можно ли?**|**Условие**|**Комментарий**|
|---|---|---|
|✅ Да|Оба класса `@Entity`|Обязательно указывай `@Inheritance` в родителе|
|⚠ Важно|Настрой стратегию наследования|Иначе могут быть ошибки при маппинге и создании таблиц|

---

💡 **Совет**:  
Используй `JOINED` для нормализованной структуры, `SINGLE_TABLE` — для простоты и быстродействия.  
А вот `TABLE_PER_CLASS` — редко, только если тебе **очень** нужно.

> У Entity-классов может быть родословная, но у каждой стратегии — свой характер 😉

---

---

## ПРИМЕРЫ:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80%D1%8B)

---

### ☝ Все классы — одна таблица

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B2%D1%81%D0%B5-%D0%BA%D0%BB%D0%B0%D1%81%D1%81%D1%8B--%D0%BE%D0%B4%D0%BD%D0%B0-%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D0%B0)

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Animal {
    @Id
    private Long id;
    private String name;
}

@Entity
@DiscriminatorValue("Dog")
public class Dog extends Animal {
    private String breed;
}

@Entity
@DiscriminatorValue("Cat")
public class Cat extends Animal {
    private String color;
}
```

🗃️ **Таблица `Animal` в БД:**

|id|name|type|breed|color|
|---|---|---|---|---|
|1|Бобик|Dog|Хаски|null|
|2|Мурка|Cat|null|Рыжий|

✅ Проста в реализации  
❌ Много `null` в колонках, если много подклассов

---

### 💡 `@Inheritance(strategy = InheritanceType.JOINED)`  
☝ Каждая сущность — своя таблица, связанные ключами

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-inheritancestrategy--inheritancetypejoined---%D0%BA%D0%B0%D0%B6%D0%B4%D0%B0%D1%8F-%D1%81%D1%83%D1%89%D0%BD%D0%BE%D1%81%D1%82%D1%8C--%D1%81%D0%B2%D0%BE%D1%8F-%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D0%B0-%D1%81%D0%B2%D1%8F%D0%B7%D0%B0%D0%BD%D0%BD%D1%8B%D0%B5-%D0%BA%D0%BB%D1%8E%D1%87%D0%B0%D0%BC%D0%B8)

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Animal {
    @Id
    private Long id;
    private String name;
}

@Entity
public class Dog extends Animal {
    private String breed;
}

@Entity
public class Cat extends Animal {
    private String color;
}
```

🗃️ **Таблицы в БД:** **Animal** markdown Copy code 🗃️ **Таблицы в БД:**

**Animal:**

|id|name|
|---|---|
|1|Бобик|
|2|Мурка|

**Dog:**

|id|breed|
|---|---|
|1|Хаски|

**Cat:**

|id|color|
|---|---|
|2|Рыжий|

✅ Нормализовано  
❌ Более сложные `JOIN`-запросы

---

### 💡 @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)  
☝ Каждая сущность — своя таблица, полностью независимая

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-inheritancestrategy--inheritancetypetable_per_class--%D0%BA%D0%B0%D0%B6%D0%B4%D0%B0%D1%8F-%D1%81%D1%83%D1%89%D0%BD%D0%BE%D1%81%D1%82%D1%8C--%D1%81%D0%B2%D0%BE%D1%8F-%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D0%B0-%D0%BF%D0%BE%D0%BB%D0%BD%D0%BE%D1%81%D1%82%D1%8C%D1%8E-%D0%BD%D0%B5%D0%B7%D0%B0%D0%B2%D0%B8%D1%81%D0%B8%D0%BC%D0%B0%D1%8F)

```java
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Animal {
    @Id
    private Long id;
    private String name;
}

@Entity
public class Dog extends Animal {
    private String breed;
}

@Entity
public class Cat extends Animal {
    private String color;
}
```

🗃️ **Таблица `Dog`:**

|id|name|breed|
|---|---|---|
|1|Бобик|Хаски|

🗃️ **Таблица `Cat`:**

|id|name|color|
|---|---|---|
|2|Мурка|Рыжий|

✅ Нет `null`, каждая таблица компактна  
❌ Невозможно использовать JPQL по родительскому классу `Animal`, нужен `UNION ALL` при запросе всех животных

---

🔥 **Резюме по стратегиям**

|**Стратегия**|**Таблиц**|**Производительность**|**Простота**|**Плюсы**|**Минусы**|
|---|---|---|---|---|---|
|**SINGLE_TABLE**|1|🔥 Высокая|✅ Простая|Легко управлять, меньше `JOIN`'ов|Много `null`, нечитабельность|
|**JOINED**|N|🐢 Средняя|⚠ Сложнее|Структурно правильно, чистая модель|`JOIN`-ы при запросах|
|**TABLE_PER_CLASS**|N|🐢🐢 Низкая|⚠⚠ Сложная|Независимые таблицы|Неудобна для полиморфных запросов|

---

---

```
***** из методички *****
Может
```
