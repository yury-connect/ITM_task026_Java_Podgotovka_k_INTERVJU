# Что такое `JPQL`/`HQL` и чем он отличается от `SQL`?

---
### 📘 Что такое `JPQL` ~~_(стандарт)_~~ и `HQL` ~~_(его расширение)_~~ ?

|**Язык**|**Описание**|
|---|---|
|`JPQL`|(_Java Persistence Query Language_) — объектно-ориентированный язык запросов, стандартизированный в **JPA**.  <br>// САМ СТАНДАРТ|
|`HQL`|(_Hibernate Query Language_) — **надмножество JPQL**, реализованное в `Hibernate` с дополнительными возможностями.  <br>// РАСШИРЯЕТ `JPQL`|
Они позволяют писать запросы **не к таблицам**, а **к сущностям Java** (`Entity`).

---
### ⚙️ JPQL/HQL vs SQL

|`SQL`|`JPQL` / `HQL`|
|---|---|
|Работает с **таблицами**|Работает с **Java сущностями** (`@Entity`)|
|Оперирует **колонками**|Оперирует **полями класса**|
|Нет полиморфизма|Есть **автоматический полиморфизм**|
|`JOIN` по **ключам**|`JOIN` через **объектные связи**|
|**Прямой доступ** к БД|**Абстракция** над БД через `JPA` / `Hibernate`|
> 💡 **В JPQL/HQL можно использовать связи между сущностями через поля, а не через внешние ключи.**

---
### 🔁 Отличие `JPQL` от `HQL`

|**Характеристика**|`JPQL`|`HQL`|
|---|---|---|
|Стандарт|Да (_часть спецификации JPA_)|Нет (_часть `Hibernate`_)|
|Поддержка в Hibernate|✅ Полностью поддерживается|✅ Полностью поддерживается|
|Расширения|❌ Только согласно спецификации JPA|✅ Поддерживает дополнительные Hibernate-специфичные возможности|
|Получение запроса|`EntityManager.createQuery()`|`session.createQuery()`|
|Именованные запросы|`@NamedQuery`,  <br>`entityManager.createNamedQuery()`|`@NamedQuery`, `session.getNamedQuery()`, `session.createNamedQuery()`|

---
### 🧪 Пример: JPQL vs SQL

#### 📌 SQL:
```sql
SELECT name FROM users WHERE age > 30;
```

#### ✅ JPQL / HQL:
```java
SELECT u.name FROM User u WHERE u.age > 30
```
> **Здесь `User` — Java-класс с аннотацией `@Entity`, а `name`, `age` — его поля.**

### 🔧 Выполнение JPQL-запроса
```java
TypedQuery<User> query = entityManager.createQuery(
    "SELECT u FROM User u WHERE u.age > :age", User.class);
query.setParameter("age", 30);
List<User> results = query.getResultList();
```

---
### 🧬 Полиморфизм в JPQL
`JPQL` автоматически возвращает не только объекты указанной сущности, но и **всех её наследников**:

```java
SELECT a FROM Animal a
```
> Вернёт и `Cat`, и `Dog`, если они наследуют `Animal`.

---
### ПРИМЕР

### 🔹 SQL
```sql
SELECT * FROM users WHERE age > 21;
```

### 🔸 JPQL
```java
entityManager.createQuery("SELECT u FROM User u WHERE u.age > :age", User.class)
             .setParameter("age", 21)
             .getResultList();
```

### 🔸 HQL
```java
session.createQuery("FROM User u WHERE u.age > :age", User.class)
       .setParameter("age", 21)
       .getResultList();
```

### 📌 Что важно заметить:
- `SQL` обращается **к таблицам** и столбцам напрямую.
- `JPQL` и `HQL` используют **сущности** и поля класса (объектный подход).
- `JPQL` требует **явного** указания `SELECT u`, а в `HQL` можно упростить запрос до `FROM User`.

---

```
***** из методички *****
Hibernate Query Language (HQL) и Java Persistence Query Language (JPQL) - оба являются 
объектно-ориентированными языками запросов, схожими по природе с SQL. 
JPQL - это подмножество HQL.

JPQL - это язык запросов, практически такой же как SQL, однако, вместо имен 
и колонок таблиц базы данных, он использует имена классов Entity и их атрибуты. 
В качестве параметров запросов также используются типы данных атрибутов Entity, 
а не полей баз данных. 

В отличии от SQL в JPQL есть автоматический полиморфизм, 
то есть каждый запрос к Entity возвращает не только объекты этого Entity, 
но также объекты всех его классов-потомков, независимо от стратегии наследования. 

В JPA запрос представлен в виде 
javax.persistence.Query или javax.persistence.TypedQuery, полученных из EntityManager. 

В Hibernate HQL-запрос представлен org.hibernate.query.Query, полученный из Session. 
Если HQL является именованным запросом, то будет использоваться Session#getNamedQuery, 
в противном случае требуется Session#createQuery.
```

---
