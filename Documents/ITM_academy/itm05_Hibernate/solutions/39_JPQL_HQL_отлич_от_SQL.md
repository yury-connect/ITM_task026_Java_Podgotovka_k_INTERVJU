# Что такое JPQL/HQL и чем он отличается от SQL?

---
## Что такое `JPQL`/`HQL` и чем он отличается от `SQL`?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D1%87%D1%82%D0%BE-%D1%82%D0%B0%D0%BA%D0%BE%D0%B5-jpqlhql-%D0%B8-%D1%87%D0%B5%D0%BC-%D0%BE%D0%BD-%D0%BE%D1%82%D0%BB%D0%B8%D1%87%D0%B0%D0%B5%D1%82%D1%81%D1%8F-%D0%BE%D1%82-sql)

### 📘 Что такое `JPQL` ~~_(стандарт)_~~ и `HQL` ~~_(его расширение)_~~ ?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D1%87%D1%82%D0%BE-%D1%82%D0%B0%D0%BA%D0%BE%D0%B5-jpql-%D1%81%D1%82%D0%B0%D0%BD%D0%B4%D0%B0%D1%80%D1%82-%D0%B8-hql-%D0%B5%D0%B3%D0%BE-%D1%80%D0%B0%D1%81%D1%88%D0%B8%D1%80%D0%B5%D0%BD%D0%B8%D0%B5-)

|**Язык**|**Описание**|
|---|---|
|`JPQL`|(_Java Persistence Query Language_) — объектно-ориентированный язык запросов, стандартизированный в **JPA**.  <br>// САМ СТАНДАРТ|
|`HQL`|(_Hibernate Query Language_) — **надмножество JPQL**, реализованное в `Hibernate` с дополнительными возможностями.  <br>// РАСШИРЯЕТ `JPQL`|

Они позволяют писать запросы **не к таблицам**, а **к сущностям Java** (`Entity`).

---

### ⚙️ JPQL/HQL vs SQL

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%EF%B8%8F-jpqlhql-vs-sql)

|`SQL`|`JPQL` / `HQL`|
|---|---|
|Работает с **таблицами**|Работает с **Java сущностями** (`@Entity`)|
|Оперирует **колонками**|Оперирует **полями класса**|
|Нет полиморфизма|Есть **автоматический полиморфизм**|
|`JOIN` по **ключам**|`JOIN` через **объектные связи**|
|**Прямой доступ** к БД|**Абстракция** над БД через `JPA` / `Hibernate`|

> 💡 **В JPQL/HQL можно использовать связи между сущностями через поля, а не через внешние ключи.**

---

### 🔁 Отличие `JPQL` от `HQL`

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BE%D1%82%D0%BB%D0%B8%D1%87%D0%B8%D0%B5-jpql-%D0%BE%D1%82-hql)

|**Характеристика**|`JPQL`|`HQL`|
|---|---|---|
|Стандарт|Да (_часть спецификации JPA_)|Нет (_часть `Hibernate`_)|
|Поддержка в Hibernate|✅ Полностью поддерживается|✅ Полностью поддерживается|
|Расширения|❌ Только согласно спецификации JPA|✅ Поддерживает дополнительные Hibernate-специфичные возможности|
|Получение запроса|`EntityManager.createQuery()`|`session.createQuery()`|
|Именованные запросы|`@NamedQuery`,  <br>`entityManager.createNamedQuery()`|`@NamedQuery`, `session.getNamedQuery()`, `session.createNamedQuery()`|

---

### 🧪 Пример: JPQL vs SQL

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80-jpql-vs-sql)

#### 📌 SQL:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-sql)

```sql
SELECT name FROM users WHERE age > 30;
```

#### ✅ JPQL / HQL:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-jpql--hql)

```java
SELECT u.name FROM User u WHERE u.age > 30
```

> **Здесь `User` — Java-класс с аннотацией `@Entity`, а `name`, `age` — его поля.**

### 🔧 Выполнение JPQL-запроса

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B2%D1%8B%D0%BF%D0%BE%D0%BB%D0%BD%D0%B5%D0%BD%D0%B8%D0%B5-jpql-%D0%B7%D0%B0%D0%BF%D1%80%D0%BE%D1%81%D0%B0)

```java
TypedQuery<User> query = entityManager.createQuery(
    "SELECT u FROM User u WHERE u.age > :age", User.class);
query.setParameter("age", 30);
List<User> results = query.getResultList();
```

---

### 🧬 Полиморфизм в JPQL

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D0%BE%D0%BB%D0%B8%D0%BC%D0%BE%D1%80%D1%84%D0%B8%D0%B7%D0%BC-%D0%B2-jpql)

`JPQL` автоматически возвращает не только объекты указанной сущности, но и **всех её наследников**:

```java
SELECT a FROM Animal a
```

> Вернёт и `Cat`, и `Dog`, если они наследуют `Animal`.

---

---

### ПРИМЕР

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80)

### 🔹 SQL

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-sql-1)

```sql
SELECT * FROM users WHERE age > 21;
```

### 🔸 JPQL

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-jpql)

```java
entityManager.createQuery("SELECT u FROM User u WHERE u.age > :age", User.class)
             .setParameter("age", 21)
             .getResultList();
```

### 🔸 HQL

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-hql)

```java
session.createQuery("FROM User u WHERE u.age > :age", User.class)
       .setParameter("age", 21)
       .getResultList();
```

### 📌 Что важно заметить:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D1%87%D1%82%D0%BE-%D0%B2%D0%B0%D0%B6%D0%BD%D0%BE-%D0%B7%D0%B0%D0%BC%D0%B5%D1%82%D0%B8%D1%82%D1%8C)

- `SQL` обращается **к таблицам** и столбцам напрямую.
- `JPQL` и `HQL` используют **сущности** и поля класса (объектный подход).
- `JPQL` требует **явного** указания `SELECT u`, а в `HQL` можно упростить запрос до `FROM User`.

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