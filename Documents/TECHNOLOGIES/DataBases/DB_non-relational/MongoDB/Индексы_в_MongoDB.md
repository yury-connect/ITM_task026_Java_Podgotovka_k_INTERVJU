В MongoDB индексы — это специальные структуры данных, которые хранят небольшой набор данных в удобном для быстрого поиска виде. Без них базе данных пришлось бы сканировать каждый документ в коллекции, что крайне неэффективно для больших объёмов данных. Представьте поиск информации в книге без алфавитного указателя: вам пришлось бы пролистать её целиком.

---
### 📌 1. `@Indexed(unique = true)` и для чего это нужно
В контексте Spring Data MongoDB аннотация `@Indexed(unique = true)` служит для создания **уникального индекса**. Это означает, что база данных будет гарантировать уникальность значений в этом поле для всех документов в коллекции [](https://www.mongodb.com/es/docs/upcoming/core/indexes/index-properties/)[](https://spring.pleiades.io/spring-data/mongodb/docs/2.2.9.RELEASE/api/org/springframework/data/mongodb/core/index/Indexed.html).

#### **Как это работает и для чего используется:**
- **Защита от дубликатов**: Если попытаться вставить новый документ с email, который уже существует в коллекции, MongoDB отклонит операцию и выбросит исключение `DuplicateKeyException` [](https://www.baeldung-cn.com/spring-data-mongodb-unique)[](https://www.baeldung.com/spring-data-mongodb-unique).
    
- **Это не "Not Null"**: Важно понимать, что уникальный индекс не запрещает отсутствие поля. Если поле отсутствует, MongoDB рассматривает это как `null`. При этом можно вставить только один документ с отсутствующим полем (или со значением `null`), так как второе значение `null` будет считаться дубликатом [](https://www.baeldung-cn.com/spring-data-mongodb-unique)[](https://www.baeldung.com/spring-data-mongodb-unique).
    
- **Где применять**: Идеальное решение для полей, которые по логике приложения должны быть уникальными: email, логин пользователя, серийный номер товара, номер заказа и т.п. [](https://www.baeldung.com/spring-data-mongodb-unique).

---
### 🗂️ 2. Какие бывают индексы в MongoDB
MongoDB предлагает множество типов индексов. Вот ключевые:

- **Одиночный (Single Field)**: Самый простой индекс на одном поле. Ускоряет запросы, которые фильтруют или сортируют по этому полю. `_id` — классический пример уникального индекса по умолчанию [](https://www.mongodb.com/es/docs/upcoming/core/indexes/index-types/)[](https://www.mongodb.com/resources/basics/databases/database-index?utm_campaign=devrel&utm_source=third-party-content&utm_medium=cta&utm_content=mongodb_collections_datacamp&utm_term=luce.carter)[](https://www.mongodb.com/community/forums/t/100daysofcodechallenge/309876/41).
    
- **Составной (Compound)**: Индекс на нескольких полях. Очень важен порядок полей, так как индекс можно использовать для запросов по префиксу полей. Например, индекс `{ "фамилия": 1, "имя": 1 }` ускорит поиск как по фамилии, так и по паре "фамилия + имя" [](https://www.mongodb.com/es/docs/upcoming/core/indexes/index-types/)[](https://www.mongodb.com/community/forums/t/100daysofcodechallenge/309876/41)[](https://www.mongodb.com/ko-kr/docs/v2.6/core/indexes-introduction/).
    
- **Мультиключевой (Multikey)**: Используется для полей, которые содержат массивы. MongoDB создаёт отдельную запись в индексе для каждого элемента массива, что позволяет эффективно искать документы по значениям внутри массива [](https://www.mongodb.com/es/docs/upcoming/core/indexes/index-types/)[](https://www.mongodb.com/resources/basics/databases/database-index?utm_campaign=devrel&utm_source=third-party-content&utm_medium=cta&utm_content=mongodb_collections_datacamp&utm_term=luce.carter)[](https://www.mongodb.com/community/forums/t/100daysofcodechallenge/309876/41).
    
- **Текстовый (Text)**: Предназначен для полнотекстового поиска по строковым полям. Поддерживает поиск по словам, игнорируя стоп-слова (например, "и", "в") и приводя слова к корневой форме [](https://www.mongodb.com/community/forums/t/100daysofcodechallenge/309876/41)[](https://www.mongodb.com/ko-kr/docs/v2.6/core/indexes-introduction/)[](https://www.mongodb.com/community/forums/t/100daysofcodechallenge/309876/36).
    
- **Геопространственный (Geospatial)**: Используется для эффективных запросов по геоданным, например, для поиска ближайших точек или объектов в заданном радиусе [](https://www.mongodb.com/es/docs/upcoming/core/indexes/index-types/)[](https://www.mongodb.com/resources/basics/databases/database-index?utm_campaign=devrel&utm_source=third-party-content&utm_medium=cta&utm_content=mongodb_collections_datacamp&utm_term=luce.carter)[](https://www.mongodb.com/community/forums/t/100daysofcodechallenge/309876/41).
    
- **Хешированный (Hashed)**: Индексирует хеш значения поля. В основном используется для шардинга (горизонтального масштабирования), чтобы обеспечить более равномерное распределение данных [](https://www.mongodb.com/es/docs/upcoming/core/indexes/index-types/)[](https://www.mongodb.com/community/forums/t/100daysofcodechallenge/309876/41)[](https://www.mongodb.com/ko-kr/docs/v2.6/core/indexes-introduction/).

---
### ⚙️ 3. Как работают индексы

По умолчанию MongoDB использует **B-дерево (B-tree)** как структуру данных для индексов. Эта структура позволяет выполнять поиск очень быстро, за логарифмическое время.

**Принцип работы можно описать так:**

1. Когда вы создаёте индекс на поле, MongoDB строит упорядоченный список значений этого поля, где каждое значение связано с указателем (`pointer`) на соответствующий документ [](https://www.mongodb.com/resources/basics/databases/database-index?utm_campaign=devrel&utm_source=third-party-content&utm_medium=cta&utm_content=mongodb_collections_datacamp&utm_term=luce.carter).
    
2. При выполнении запроса с условием на это поле, механизм запросов может использовать B-дерево, чтобы быстро найти нужное значение, вместо того чтобы сканировать все документы [](https://www.mongodb.com/resources/basics/databases/database-index?utm_campaign=devrel&utm_source=third-party-content&utm_medium=cta&utm_content=mongodb_collections_datacamp&utm_term=luce.carter)[](https://www.mongodb.com/ko-kr/docs/v2.6/core/indexes-introduction/).
    
3. Индексы отлично ускоряют чтение, но замедляют операции записи (вставка, обновление, удаление), так как индекс тоже нужно обновлять. Поэтому важно индексировать только те поля, которые часто используются в запросах.

---
