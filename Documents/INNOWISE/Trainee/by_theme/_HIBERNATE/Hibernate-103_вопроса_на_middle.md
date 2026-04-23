Hibernate (и JPA) — это тема, где Middle-разработчика проверяют очень тщательно, особенно на позициях с активной работой с БД.

Ниже приведен **расширенный список из ~100 вопросов**, ранжированный по частоте встречаемости — от «священных коров», которые задают всегда, до глубоких нюансов кэширования и производительности.

---

### Группа 1: Основы JPA/Hibernate, отличие от JDBC (Задают в 99% случаев)

1. **Что такое JPA? Чем JPA отличается от Hibernate?** (JPA — спецификация, Hibernate — реализация).
    
2. **Какие преимущества дает Hibernate перед JDBC?** (Уровень абстракции, объектно-реляционное отображение (ORM), кэширование, lazy loading, кроссплатформенность запросов).
    
3. **Какие минусы/проблемы может создать Hibernate?** (N+1 проблема, сложность с настройкой производительности, неочевидные SQL-запросы).
    
4. **Что такое Entity? Какие требования к классу-сущности?** (Конструктор по умолчанию, идентификатор, не final, поля private).
    
5. **Что такое Session (в Hibernate) / EntityManager (в JPA)?** Чем отличается от SessionFactory / EntityManagerFactory?
    
6. **Жизненный цикл сущности (Entity Lifecycle):** Transient (New), Managed (Persistent), Detached, Removed.
    
7. **Что происходит с сущностью в состоянии Managed?** (Отслеживание изменений, автоматический flush при коммите транзакции).
    
8. **Что такое Persistence Context?** (Первый уровень кэша).
    
9. **Какая связь между Session (EntityManager) и транзакцией?**
    
10. **Что делает метод `merge()`? Чем отличается от `persist()`?** (persist — для новых, merge — для detached).
    
11. **Чем отличается `get()` от `load()` в Hibernate (или `find()` от `getReference()` в JPA)?** (get/find — сразу запрос, load/getReference — ленивый прокси).
    
12. **Что такое `flush()`? Чем отличается от `commit()`?** (flush — синхронизация с БД, commit — завершение транзакции + flush).
    
13. **Что такое `clear()`? Зачем нужен?** (Очистка Persistence Context, все сущности становятся detached).
    

### Группа 2: Маппинг связей (Mapping Associations) — Очень высокая частота

14. **Какие виды связей между сущностями существуют?** (One-to-One, One-to-Many, Many-to-One, Many-to-Many).
    
15. **Как маппится `@OneToMany`? Какие атрибуты важны?** (mappedBy, cascade, fetch, orphanRemoval).
    
16. **Что такое mappedBy? Кто является владельцем связи (owning side)?** (Сторона без mappedBy — владелец, содержит внешний ключ).
    
17. **Чем отличается `@OneToMany` от `@ManyToMany`?**
    
18. **Как реализуется `@ManyToMany`? Как выглядит join table?** (`@JoinTable`, `joinColumns`, `inverseJoinColumns`).
    
19. **Что такое `orphanRemoval = true`? Чем отличается от `CascadeType.REMOVE`?** (orphanRemoval удаляет дочернюю сущность при удалении из коллекции, REMOVE — только при удалении родителя).
    
20. **Какие есть CascadeType?** (ALL, PERSIST, MERGE, REMOVE, REFRESH, DETACH).
    
21. **В чем разница между `FetchType.LAZY` и `FetchType.EAGER`?**
    
22. **Почему `FetchType.EAGER` — это антипаттерн?** (Риск N+1, загрузка лишних данных, Cartesian product).
    
23. **Что такое `@ManyToOne(fetch = FetchType.LAZY)`. Работает ли?** (Да, но Hibernate может подменить прокси).
    
24. **Как работает ленивая загрузка (Lazy Loading) под капотом?** (Прокси-объект, который при обращении к полю инициализируется при открытой сессии).
    
25. **Что такое `LazyInitializationException`? Почему возникает и как лечить?** (Закрытие сессии/EntityManager до обращения к ленивому полю).
    

### Группа 3: Проблема N+1 и способы решения (Критически важно)

26. **Что такое проблема N+1 запроса? Приведите пример.**
    
27. **Как решить проблему N+1 с помощью `JOIN FETCH`?** (`@Query("SELECT DISTINCT p FROM Post p JOIN FETCH p.comments")`).
    
28. **Что такое `@EntityGraph`? Чем отличается от `JOIN FETCH`?** (Более гибкий, можно применять к методам репозитория без написания JPQL).
    
29. **Как работает `@NamedEntityGraph`?**
    
30. **Что такое `HINT` для загрузки графа?** (`@EntityGraph(attributePaths = {"comments"}, type = FETCH)`).
    
31. **Чем отличается `JOIN FETCH` от обычного `JOIN` в JPQL?** (JOIN FETCH загружает ассоциацию в тот же запрос, обычный JOIN — только для фильтрации).
    
32. **В чем разница между `JOIN FETCH` и `@BatchSize`?** (JOIN FETCH — один запрос, @BatchSize — N+1, но пачками).
    
33. **Как работает `@BatchSize`?** (Hibernate загружает ленивые коллекции несколькими запросами по batchsize элементов).
    

### Группа 4: Кэширование (Caching) — Средне-высокая частота

34. **Какие уровни кэша есть в Hibernate?** (First Level Cache, Second Level Cache, Query Cache).
    
35. **Что такое First Level Cache (сессия/EntityManager)?** (Включен всегда, живет в пределах одной сессии/транзакции).
    
36. **Как работает Second Level Cache? Как включить?** (`@Cacheable`, `@Cache`, настройка в `persistence.xml` или `application.properties`).
    
37. **Какие стратегии конкуренции (concurrency strategies) для 2-го уровня?** (READ_ONLY, NONSTRICT_READ_WRITE, READ_WRITE, TRANSACTIONAL).
    
38. **Что такое Query Cache? Зачем нужен?** (Кэширует результаты JPQL/HQL запросов, зависит от 2-го уровня).
    
39. **В чем проблема Query Cache?** (Инвалидация при любом изменении таблиц, участвующих в запросе).
    
40. **Что такое кэш коллекций?** (`@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)` для `@OneToMany`/`@ManyToMany`).
    

### Группа 5: JPQL, HQL, Criteria API (Очень высокая частота)

41. **Что такое JPQL? Чем отличается от SQL?** (Работает с сущностями и их полями, не с таблицами).
    
42. **Что такое HQL? Чем отличается от JPQL?** (Расширенная версия JPQL с Hibernate-специфичными фичами).
    
43. **Что такое Criteria API? Когда использовать?** (Динамические запросы, типобезопасность, но громоздко).
    
44. **Что лучше: JPQL или Criteria API?** (JPQL — для простых, Criteria — для динамических запросов).
    
45. **Что такое `SELECT NEW` в JPQL?** (DTO проекция, создает объект прямо в запросе).
    
46. **Как написать запрос с `JOIN` в JPQL?** (`JOIN`, `LEFT JOIN`, `INNER JOIN`).
    
47. **Как написать update или delete через JPQL?** (`@Modifying` + `@Query`).
    
48. **Что делает `@Modifying(clearAutomatically = true)`?** (Очищает Persistence Context после выполнения update/delete, чтобы не было stale state).
    
49. **Как пагинировать запросы?** (`setFirstResult()`, `setMaxResults()` в Hibernate, или `Pageable` в Spring Data JPA).
    

### Группа 6: Идентификаторы (ID) и генерация значений (Средне-высокая частота)

50. **Что такое `@GeneratedValue`? Какие стратегии генерации ID бывают?** (`AUTO`, `IDENTITY`, `SEQUENCE`, `TABLE`).
    
51. **В чем разница между `IDENTITY` и `SEQUENCE`?** (IDENTITY — insert сразу, SEQUENCE — можно получить ID до insert, лучше для батчинга).
    
52. **Как работает `SEQUENCE` стратегия? Какие есть оптимизаторы?** (`pooled`, `pooled-lo` для уменьшения обращений к БД).
    
53. **Что такое `@SequenceGenerator`? Какие параметры?** (name, sequenceName, allocationSize).
    
54. **Почему `TABLE` стратегия — это плохо?** (Пессимистичная блокировка, накладные расходы).
    
55. **Как использовать UUID в качестве ID?** (`@GeneratedValue(strategy = IDENTITY)` не подходит, обычно генерируется в коде или через `UUIDGenerator`).
    

### Группа 7: Транзакции и изоляция в контексте Hibernate (Средняя частота)

56. **Как Hibernate взаимодействует с транзакциями БД?** (Через `session.beginTransaction()` или `@Transactional`).
    
57. **Что такое `@Transactional` (из Spring) в связке с Hibernate?** (Открывает EntityManager, начинает транзакцию, коммитит/откатывает, закрывает EntityManager).
    
58. **Как уровень изоляции транзакций влияет на работу Hibernate?** (Через `@Transactional(isolation = ...)` или `spring.datasource.hikari.isolation`).
    
59. **Что такое optimistic locking? Как реализовать в Hibernate?** (`@Version` с полем `int` или `timestamp`).
    
60. **Как работает optimistic locking?** (Проверка версии при update, если версия изменилась — `OptimisticLockException`).
    
61. **Что такое pessimistic locking? Как реализовать?** (`LockModeType.PESSIMISTIC_READ`, `PESSIMISTIC_WRITE` через `entityManager.lock()` или `@Lock` в репозитории).
    
62. **Когда использовать optimistic vs pessimistic locking?** (Optimistic — мало конфликтов, Pessimistic — частая конкуренция).
    

### Группа 8: Типы данных и конвертеры (Средняя частота)

63. **Какие базовые типы поддерживаются "из коробки"?** (Примитивы, обертки, String, Date, Enum, BigDecimal).
    
64. **Как хранить `enum` в БД?** (`@Enumerated(EnumType.ORDINAL)` — плохо, `EnumType.STRING` — лучше, или свой `AttributeConverter`).
    
65. **Что такое `AttributeConverter`? Зачем нужен?** (Конвертация любого типа данных в поддерживаемый БД и обратно).
    
66. **Как хранить JSON в БД через Hibernate?** (Свой конвертер или расширения типа `io.hypersistence`).
    
67. **Что такое `@Embeddable` и `@Embedded`?** (Value-объекты, компонуемые типы).
    
68. **Чем отличается `@Embeddable` от `@OneToOne`?** (Embedded — часть той же таблицы, OneToOne — отдельная таблица).
    

### Группа 9: Наследование (Inheritance Mapping) — Реже, но для Middle норм

69. **Какие стратегии маппинга наследования есть в JPA?** (`SINGLE_TABLE`, `TABLE_PER_CLASS`, `JOINED`).
    
70. **Как работает `SINGLE_TABLE`? Плюсы и минусы.** (Одна таблица, дискриминатор, много nullable колонок).
    
71. **Как работает `JOINED`?** (Таблица на каждый класс, связь по FK, много JOIN-ов).
    
72. **Как работает `TABLE_PER_CLASS`?** (У каждой конкретной сущности своя таблица, проблемы с полиморфными запросами).
    
73. **Какую стратегию выбрать?** (SINGLE_TABLE — производительность, JOINED — нормализация).
    

### Группа 10: Производительность, мониторинг и настройка (Важно для Middle+)

74. **Как включить логирование SQL-запросов?** (`spring.jpa.show-sql=true`, `spring.jpa.properties.hibernate.format_sql=true`).
    
75. **Как логировать значения параметров в запросах?** (`spring.jpa.properties.hibernate.use_sql_comments=true` и `log4jdbc` или `p6spy`).
    
76. **Что такое `hibernate.jdbc.batch_size`? Как включить батчинг?** (Группировка нескольких insert/update в один).
    
77. **Почему `IDENTITY` стратегия ломает батчинг?** (Hibernate не может знать ID до insert).
    
78. **Что такое `hibernate.order_inserts` и `hibernate.order_updates`?** (Сортировка запросов для лучшего батчинга).
    
79. **Что такое `hibernate.jdbc.fetch_size`?** (Количество строк, забираемых за один round-trip с БД).
    
80. **Что такое `hibernate.jdbc.lob.non_contextual_creation`?** (Для правильной работы с CLOB/BLOB в разных драйверах).
    
81. **Что такое N+1 в контексте коллекций?** (При итерации по родительским сущностям каждый вызов дочерней коллекции — отдельный запрос).
    
82. **Как найти проблемные запросы?** (Включить show-sql, использовать статистику Hibernate, DataSource Proxy, `hibernate.generate_statistics`).
    
83. **Что такое `hibernate.hbm2ddl.auto`? Варианты (`validate`, `update`, `create`, `create-drop`, `none`).**
    
84. **Чем опасен `update` на проде?** (Риск потери данных, неконтролируемые изменения схемы).
    

### Группа 11: Продвинутые темы и нюансы (Редко, но для сильного Middle)

85. **Что такое `@DynamicInsert` и `@DynamicUpdate`? Когда нужны?** (Генерирует insert/update только с измененными полями, для триггеров или больших таблиц).
    
86. **Что такое `@Where` и `@SQLDelete`? (Soft delete).**
    
87. **Что такое `@Filter` и `@FilterDef`?** (Глобальные фильтры для сущностей, например, "только неудаленные").
    
88. **Что такое `@SQLInsert`, `@SQLUpdate`, `@SQLDelete`, `@Loader`?** (Кастомные SQL операции, обход ORM).
    
89. **Что такое `@NaturalId`? Зачем нужен?** (Бизнес-ключ, отличный от первичного, поддержка кэша).
    
90. **Что такое `@Immutable`?** (Сущность только для чтения, Hibernate не отслеживает изменения).
    
91. **Что такое `@Subselect`?** (Мапинг сущности на результат SQL-запроса, как view).
    
92. **Что такое `@Tuplizer`?** (Кастомная стратегия создания экземпляров сущностей).
    
93. **Как работает dirty checking (автоматическое отслеживание изменений)?** (Hibernate сравнивает текущее состояние сущности со snapshot-ом из момента загрузки).
    
94. **Что такое `Session.evict()`? Чем отличается от `clear()`?** (evict — выселить конкретную сущность, clear — все).
    
95. **Как загрузить только часть полей сущности? (Проекция).** (DTO через `SELECT NEW`, `Tuple`, `Object[]`).
    
96. **Что такое `@ManyToMany` с дополнительными колонками в join table?** (Нужно разбить на две `@OneToMany` с промежуточной сущностью).
    
97. **Что такое `@PostLoad`, `@PrePersist` и другие callback-методы?**
    
98. **Как работает `hibernate.id.new_generator_mappings`?** (Флаг для включения более новых, правильных генераторов ID).
    
99. **Как дебажить Hibernate?** (Уровни логов `org.hibernate.SQL`, `org.hibernate.type`, `org.hibernate.stat`).
    
100. **Что такое OSIV (Open Session In View)? Почему антипаттерн?** (Держит сессию открытой на всю жизнь view, приводит к LazyInitializationException или проблемам с производительностью).
    

### Группа 12: Бонус — различия Hibernate 5 vs 6 (Если вакансия на новых версиях)

101. **Какие изменения в Hibernate 6?** (Новый парсер JPQL, улучшенная поддержка Java 8 Date/Time, новый Query API).
    
102. **Что изменилось в `hibernate-types`?** (Многие типы стали встроенными, например, JSON, ARRAY).
    
103. **Как Hibernate 6 обрабатывает `byte[]` и `UUID`?** (Лучшая поддержка нативных типов БД).
    

---

### Главные советы по Hibernate для собеседования:

1. **Жизненный цикл сущности и Persistence Context** — это база, без которой не понять ничего остального.
    
2. **Проблема N+1 и способы ее решения (`JOIN FETCH`, `@EntityGraph`, `@BatchSize`)** — самый частый практический вопрос. Будьте готовы написать код.
    
3. **Разница между `get()`/`load()` (или `find()`/`getReference()`)** — проверяет понимание ленивости и прокси.
    
4. **Кэширование (1й уровень vs 2й уровень)** — часто спрашивают, особенно на проектах с высокими нагрузками.
    
5. **Не путайте JPA и Hibernate** — умение разделять спецификацию и реализацию ценится.
    
6. **Знайте, как включить логирование SQL и параметров** — это покажет, что вы умеете дебажить проблемы.
    
7. **`@Transactional`** — если позиция со Spring, то вопросы по транзакциям будут пересекаться со Spring-списком.