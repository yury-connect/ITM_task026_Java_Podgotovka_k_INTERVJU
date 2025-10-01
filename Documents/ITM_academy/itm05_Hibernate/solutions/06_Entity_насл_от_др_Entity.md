# Может ли `Entity` класс наследоваться от других `Entity` классов?

---
### ✅ Да, может.
Но с этим связано использование **наследования в JPA/Hibernate**, и тут важно выбрать правильную стратегию.

### 🧬 Основные стратегии JPA-наследования (`@Inheritance`)

| **Стратегия**                      | `JOIN`'ы                                                                  | **Производительность** | **Описание** / Когда применять?                                                                                         |
| ---------------------------------- | ------------------------------------------------------------------------- | ---------------------- | ----------------------------------------------------------------------------------------------------------------------- |
| 🟨 `SINGLE_TABLE` _(по умолчанию)_ | ❌                                                                         | 🔥 Высокая             | Все сущности — в **одной таблице**, используется `@DiscriminatorColumn`  <br>/Когда **простая иерархия**, немного полей |
| 🟦 `JOINED`                        | ✅                                                                         | 🐢 Средняя             | Каждая сущность — в своей таблице, соединяются по внешнему ключу  <br>/Когда нужна **нормализованная БД** и читаемость  |
| 🟪 `TABLE_PER_CLASS`               | ❌                                                                         | 🐌 Низкая              | Каждая сущность — своя таблица, **без объединения**  <br>/Когда каждая сущность **отдельна** и **независима**           |
| **Стратегия**                      | **Описание**                                                              |                        |                                                                                                                         |
### 💡 Как выбрать стратегию?
🔹 Ты — **практичный ремесленник** → 
	бери `SINGLE_TABLE`. Всё в одной куче, быстро, легко.  
🔹 Ты — **чистоплотный архитектор** → 
	`JOINED`. Всё по полочкам, без `null`'ов, красиво.  
🔹 Ты — **максимальный индивидуалист** → 
	`TABLE_PER_CLASS`. Полная свобода, но будь готов к трудностям с запросами.
### 🛠️ Аннотации
- `@Inheritance(strategy = ...)` — на родительском `@Entity` классе
- `@DiscriminatorColumn` — (для `SINGLE_TABLE`) для различения подклассов
- `@DiscriminatorValue` — для указания значения конкретной сущности
- `@PrimaryKeyJoinColumn` — (для `JOINED`) связывает родителя и потомка

|**Можно ли?**|**Условие**|**Комментарий**|
|---|---|---|
|✅ Да|Оба класса `@Entity`|Обязательно указывай `@Inheritance` в родителе|
|⚠ Важно|Настрой стратегию наследования|Иначе могут быть ошибки при маппинге и создании таблиц|
#### 💡 Совет:  
Используй `JOINED` для нормализованной структуры,   
`SINGLE_TABLE` — для простоты и быстродействия.   
А вот `TABLE_PER_CLASS` — редко, только если тебе **очень** нужно.

> У Entity-классов мож_быть родословная, но у каждой стратегии — свой характер😉

---
## ПРИМЕРЫ:

### ☝ Все классы — одна таблица
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
#### 🗃️ **Таблица `Animal` в БД:**

|id|name|type|breed|color|
|---|---|---|---|---|
|1|Бобик|Dog|Хаски|null|
|2|Мурка|Cat|null|Рыжий|
✅ Проста в реализации  
❌ Много `null` в колонках, если много подклассов

### 💡 `@Inheritance(strategy = InheritanceType.JOINED)`  
☝ Каждая сущность — своя таблица, связанные ключами
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
#### 🗃️ **Таблицы в БД:**
###### **Animal:**

|id|name|
|---|---|
|1|Бобик|
|2|Мурка|
###### **Dog:**

|id|breed|
|---|---|
|1|Хаски|

###### **Cat:**

|id|color|
|---|---|
|2|Рыжий|
✅ Нормализовано  
❌ Более сложные `JOIN`-запросы

### 💡 @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)  
☝ Каждая сущность — своя таблица, полностью независимая
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

###### 🗃️ **Таблица `Dog`:**

|id|name|breed|
|---|---|---|
|1|Бобик|Хаски|

###### 🗃️ **Таблица `Cat`:**

|id|name|color|
|---|---|---|
|2|Мурка|Рыжий|
✅ Нет `null`, каждая таблица компактна  
❌ Невозможно использовать JPQL по родительскому классу `Animal`, нужен `UNION ALL` при запросе всех животных

---
### 🔥 **Резюме по стратегиям**

|**Стратегия**|**Таблиц**|**Производительность**|**Простота**|**Плюсы**|**Минусы**|
|---|---|---|---|---|---|
|**SINGLE_TABLE**|1|🔥 Высокая|✅ Простая|Легко управлять, меньше `JOIN`'ов|Много `null`, нечитабельность|
|**JOINED**|N|🐢 Средняя|⚠ Сложнее|Структурно правильно, чистая модель|`JOIN`-ы при запросах|
|**TABLE_PER_CLASS**|N|🐢🐢 Низкая|⚠⚠ Сложная|Независимые таблицы|Неудобна для полиморфных запросов|

---

---

```
***** из методички *****
Может
```

---
