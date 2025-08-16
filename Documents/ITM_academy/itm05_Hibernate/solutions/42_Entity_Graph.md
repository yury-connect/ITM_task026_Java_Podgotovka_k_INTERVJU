# Что такое Entity Graph

---
### 🧭 Что такое `Entity Graph` в JPA/Hibernate?
📌 **Entity Graph** — это механизм, позволяющий **гибко управлять загрузкой связанных сущностей** (_fetching strategy_) **на уровне запроса, без изменения аннотаций в моделях**.

---
### 🎯 Основное назначение:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BE%D1%81%D0%BD%D0%BE%D0%B2%D0%BD%D0%BE%D0%B5-%D0%BD%D0%B0%D0%B7%D0%BD%D0%B0%D1%87%D0%B5%D0%BD%D0%B8%D0%B5)

> **⚡ Повысить производительность путём указания, какие связанные объекты загружать в одном SELECT-запросе.**

---

### 🛠️ Ключевые особенности:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%EF%B8%8F-%D0%BA%D0%BB%D1%8E%D1%87%D0%B5%D0%B2%D1%8B%D0%B5-%D0%BE%D1%81%D0%BE%D0%B1%D0%B5%D0%BD%D0%BD%D0%BE%D1%81%D1%82%D0%B8)

|**Характеристика**|**Описание**|
|---|---|
|🔄 **Динамичность**|Позволяет переопределить `LAZY` → `EAGER` на лету, в конкретном запросе|
|🔗 **Гибкость**|Можно указать, **какие поля или связи подгружать**, а какие оставить ленивыми|
|📦 **Один SQL-запрос**|Всё загружается сразу в рамках **одного основного запроса**, без N+1!|
|🎛 **Типы графов**|`NamedEntityGraph` (_статически_) и `EntityGraph` (_динамически, в коде_)|

---

### 💡 Пример пользы:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80-%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D1%8B)

Если сущность User имеет List и Profile, ты можешь загрузить только нужные связи — например, только Profile, игнорируя Order, — без изменений в самой сущности. Эффективно и быстро!

---

### ✅ Когда использовать:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BA%D0%BE%D0%B3%D0%B4%D0%B0-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D1%82%D1%8C-4)

- Когда нужно управлять глубиной загрузки (вложенные связи).
- При работе с разными use-case'ами, где нужны разные связи.
- Альтернатива JOIN FETCH, когда не хочется писать вручную JPQL-запросы.

---

### ⚖️ Сравнение с `JOIN FETCH`:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%EF%B8%8F-%D1%81%D1%80%D0%B0%D0%B2%D0%BD%D0%B5%D0%BD%D0%B8%D0%B5-%D1%81-join-fetch)

||**JOIN FETCH**|**Entity Graph**|
|---|---|---|
|📄 Основан на JPQL|Да|Нет|
|🧠 Интеллектуальнее выбор|❌ Всегда все связи|✅ Только указанные|
|🛠️ Гибкость|Низкая|Высокая|
|📚 Удобство для DTO|Меньше|Лучше совместим с фреймворками типа Spring Data|

---

### 🧩 Заключение:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B7%D0%B0%D0%BA%D0%BB%D1%8E%D1%87%D0%B5%D0%BD%D0%B8%D0%B5)

**Entity Graph** — это как выбрать, что положить в корзинку 🧺 при походе в БД-супермаркет: не нужно тащить всё подряд, бери только нужное — быстро, эффективно и с удовольствием 😎

---

---

### Пример использования EntityGraph в JPA (_Hibernate_)

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F-entitygraph-%D0%B2-jpa-hibernate)

В данном примере мы создаём EntityGraph и с его помощью настраиваем загрузку сущности Post таким образом, чтобы при её загрузке автоматически загружались поля subject, user, comments. Кроме того, мы хотим, чтобы для каждой сущности comments также загружалось поле user. Для этого используются subgraphs.

#### Создание EntityGraph с помощью JPA API

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D1%81%D0%BE%D0%B7%D0%B4%D0%B0%D0%BD%D0%B8%D0%B5-entitygraph-%D1%81-%D0%BF%D0%BE%D0%BC%D0%BE%D1%89%D1%8C%D1%8E-jpa-api)

EntityGraph можно определить программно, без аннотаций, используя EntityManager:

```java
EntityGraph<Post> entityGraph = entityManager.createEntityGraph(Post.class);
entityGraph.addAttributeNodes("subject");
entityGraph.addAttributeNodes("user");
entityGraph.addSubgraph("comments").addAttributeNodes("user");
```

#### Способы загрузки графа сущности в JPA

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D1%81%D0%BF%D0%BE%D1%81%D0%BE%D0%B1%D1%8B-%D0%B7%D0%B0%D0%B3%D1%80%D1%83%D0%B7%D0%BA%D0%B8-%D0%B3%D1%80%D0%B0%D1%84%D0%B0-%D1%81%D1%83%D1%89%D0%BD%D0%BE%D1%81%D1%82%D0%B8-%D0%B2-jpa)

JPA предоставляет два способа указания, как именно должен быть извлечён граф сущности во время выполнения:

- `fetchgraph` — из базы данных извлекаются только указанные в графе атрибуты.
    
    > _Поскольку мы используем Hibernate, стоит отметить, что в отличие от спецификаций JPA, атрибуты, статически настроенные как `EAGER`, также загружаются._
    
- `loadgraph` — помимо указанных в графе атрибутов, также извлекаются атрибуты, настроенные как `EAGER` на уровне сущности.
    

> **В любом случае, первичный ключ и версия (_если таковые имеются_) загружаются всегда.**

---

```
***** из методички *****
Он позволяет определить шаблон путем группировки связанных полей, которые мы хотим получить, и позволяет нам выбирать тип графа во время выполнения.
Основная цель JPA Entity Graph - улучшить производительность в рантайме при загрузке базовых полей сущности и связанных сущностей и коллекций. 
Вкратце, Hibernate загружает весь граф в одном SELECT-запросе, то есть все указанные связи от нужной нам сущности:

Entity Graphs — механизм динамического изменения fetchType для каждого запроса
```