# Группа 7: `Spring Data` и Работа с БД 
*(Очень высокая частота задавания этих вопросов)*

[⬅️**Previous**](Группа_06-Spring_Boot_Core)   //   [⬆️](SPRING-121_вопрос_на_middle)   //   [**Next**➡️](Группа_08-Spring_Security)

**Spring Data JPA** — это то, с чем Middle-разработчик работает постоянно. 
Отвечаю максимально точно и с аргументацией.

---

### 74. Что такое Spring Data JPA? Чем отличается от Hibernate?

**Ответ:** Spring Data JPA — это **надстройка** над JPA (и, соответственно, над Hibernate), которая **упрощает реализацию слоя доступа к данным** за счет генерации реализации репозиториев на основе интерфейсов.

**Разница между Spring Data JPA и Hibernate:**

|Характеристика|Hibernate|Spring Data JPA|
|---|---|---|
|**Роль**|ORM-фреймворк (реализация JPA)|Абстракция над JPA для упрощения работы|
|**Уровень**|Работает с сущностями, Session/EntityManager, SQL|Работает с репозиториями, автоматически генерирует запросы|
|**Что делает**|Маппинг Java-объектов на таблицы БД, управление кэшем, ленивая загрузка|Предоставляет готовые CRUD-методы, генерацию запросов по имени метода, пагинацию|
|**Зависимость**|Без Spring Data JPA можно использовать Hibernate напрямую|Зависит от JPA-провайдера (обычно Hibernate)|

**Простыми словами:**

- **Hibernate** — отвечает за то, _как_ данные сохраняются в БД и загружаются из нее.
    
- **Spring Data JPA** — отвечает за то, _как удобно_ писать код для работы с данными (не писать DAO вручную).
    

**Пример:** Без Spring Data JPA нужно писать реализацию DAO с `EntityManager`. С Spring Data JPA достаточно объявить интерфейс, расширяющий `JpaRepository`, и все CRUD-методы уже готовы.

---

### 75. Что такое `JpaRepository`? Чем отличается от `CrudRepository` и `PagingAndSortingRepository`?

**Ответ:** Это интерфейсы Spring Data, которые предоставляют готовые методы для работы с сущностями. Они образуют иерархию:
```text
Repository (маркерный интерфейс)
    ↓
CrudRepository (базовые CRUD-операции)
    ↓
PagingAndSortingRepository (пагинация и сортировка)
    ↓
JpaRepository (JPA-специфичные методы + batch-операции)
```

**Отличия:**

|Интерфейс|Методы|Когда использовать|
|---|---|---|
|**`CrudRepository<T, ID>`**|`save()`, `findById()`, `findAll()`, `delete()`, `count()`, `existsById()`|Базовая функциональность, не нужна пагинация|
|**`PagingAndSortingRepository<T, ID>`**|Добавляет `findAll(Pageable)` и `findAll(Sort)`|Нужна пагинация или сортировка|
|**`JpaRepository<T, ID>`**|Добавляет `flush()`, `saveAndFlush()`, `deleteInBatch()`, `getOne()` (deprecated), `findAll(Example)`|**Рекомендуемый** — полная функциональность JPA|

**Почему обычно используют `JpaRepository`:**

- `saveAndFlush()` — немедленная синхронизация с БД.
    
- `deleteInBatch()` — массовое удаление одним запросом (эффективнее, чем逐个 удаление).
    
- `getReferenceById()` (замена deprecated `getOne()`) — ленивая загрузка прокси.
    
- `findAll(Example)` — query by example.
    

**Практический совет:** Начинайте с `JpaRepository` — он дает все возможности, а лишние методы можно игнорировать.

---

### 76. Как работают derived queries (методы `findByName`)? Парсинг имен методов.

**Ответ:** Derived queries — это механизм Spring Data, при котором **SQL/JPQL запрос генерируется автоматически** на основе имени метода, без явного написания `@Query`.

**Как работает парсинг имени метода:**

1. Spring Data анализирует имя метода и разбивает его на составные части.
    
2. Отбрасываются префиксы: `find…By`, `read…By`, `query…By`, `count…By`, `exists…By`, `delete…By`, `remove…By`.
    
3. После `By` начинаются условия, разделенные ключевыми словами `And`, `Or`.
    
4. Каждое условие содержит имя поля и опционально ключевое слово (Like, GreaterThan, IsNull и т.д.).
    

**Примеры:**
```java
public interface UserRepository extends JpaRepository<User, Long> {
    
    // SELECT u FROM User u WHERE u.name = ?1
    User findByName(String name);
    
    // SELECT u FROM User u WHERE u.name = ?1 AND u.age > ?2
    List<User> findByNameAndAgeGreaterThan(String name, int age);
    
    // SELECT u FROM User u WHERE u.email LIKE ?1
    List<User> findByEmailLike(String pattern);
    
    // SELECT COUNT(u) FROM User u WHERE u.active = ?1
    long countByActive(boolean active);
    
    // SELECT u FROM User u WHERE u.name IN ?1
    List<User> findByNameIn(List<String> names);
    
    // DELETE FROM User u WHERE u.inactive = true
    @Modifying
    void deleteByInactiveTrue();
}
```

**Поддерживаемые ключевые слова:**

|Ключевое слово|JPQL фрагмент|
|---|---|
|`IsNotNull`, `NotNull`|`WHERE field IS NOT NULL`|
|`IsNull`, `Null`|`WHERE field IS NULL`|
|`Like`|`WHERE field LIKE ?1`|
|`Containing`|`WHERE field LIKE %?1%`|
|`StartingWith`|`WHERE field LIKE ?1%`|
|`EndingWith`|`WHERE field LIKE %?1`|
|`LessThan`|`WHERE field < ?1`|
|`GreaterThan`|`WHERE field > ?1`|
|`Between`|`WHERE field BETWEEN ?1 AND ?2`|
|`In`|`WHERE field IN ?1`|
|`OrderBy`|`ORDER BY field ASC/DESC`|

**Ограничения:** При сложных условиях имя метода становится длинным и нечитаемым. Тогда лучше использовать `@Query`.

---

### 77. Что такое `@Query`? Нативная или JPQL?

**Ответ:** `@Query` — аннотация для **явного написания запросов** в методах репозитория. Она используется, когда derived query не подходит (сложная логика, нестандартные JOIN, агрегации).

**Два типа запросов в `@Query`:**

|Тип|Параметр|Описание|Пример|
|---|---|---|---|
|**JPQL**|`nativeQuery = false` (по умолчанию)|Запрос к **сущностям и их полям**, не к таблицам. Переносим между БД.|`FROM User u WHERE u.name = :name`|
|**Native SQL**|`nativeQuery = true`|Прямой SQL для конкретной БД. Использует таблицы и колонки.|`SELECT * FROM users WHERE name = :name`|

**Примеры:**
```java
public interface UserRepository extends JpaRepository<User, Long> {
    
    // JPQL (по умолчанию)
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);
    
    // JPQL с JOIN FETCH для решения N+1
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id = :id")
    Optional<User> findByIdWithOrders(@Param("id") Long id);
    
    // Native SQL
    @Query(value = "SELECT * FROM users u WHERE u.email = :email", nativeQuery = true)
    User findByEmailNative(@Param("email") String email);
    
    // JPQL с проекцией (только некоторые поля)
    @Query("SELECT u.name, u.email FROM User u WHERE u.active = true")
    List<Object[]> findActiveUsersNamesAndEmails();
    
    // Update через JPQL (требуется @Modifying)
    @Modifying
    @Query("UPDATE User u SET u.active = false WHERE u.lastLogin < :date")
    int deactivateInactiveUsers(@Param("date") LocalDateTime date);
}
```

**Когда что использовать:**

|Сценарий|Рекомендация|
|---|---|
|Простые условия (WHERE field = value)|Derived query (findByName)|
|JOIN между сущностями|JPQL с JOIN FETCH|
|Запрос с агрегацией (SUM, AVG)|JPQL|
|Сложные отчеты, оконные функции|Native SQL|
|Запрос с вложенными SELECT|JPQL (если возможно) или Native|
|Специфичные для БД фичи (PostgreSQL JSON,全文搜索)|Native SQL|

**Важно:** При `nativeQuery = true` маппинг результата в сущности происходит по именам колонок, а не по полям сущности. Нужно либо использовать алиасы, либо возвращать `Object[]` и маппить вручную.

---

### 78. Что такое `@Modifying`? Зачем нужен (`clearAutomatically`, `flushAutomatically`)?

**Ответ:** `@Modifying` — аннотация, которая указывает Spring Data, что JPQL-запрос **изменяет данные** (UPDATE/DELETE), а не просто читает. Она обязательна для запросов с `@Query`, которые не являются SELECT.

**Почему нужна:** По умолчанию `@Query` подразумевает SELECT. Без `@Modifying` запрос UPDATE/DELETE выбросит исключение `InvalidDataAccessApiUsageException`.

**Пример:**
```java
@Modifying
@Query("UPDATE User u SET u.status = 'INACTIVE' WHERE u.lastLogin < :date")
int deactivateOldUsers(@Param("date") LocalDateTime date);
```

**Параметры `@Modifying`:**

|Параметр|По умолчанию|Описание|
|---|---|---|
|**`clearAutomatically`**|`false`|Очищает Persistence Context после выполнения запроса, чтобы stale-сущности не оставались в кэше|
|**`flushAutomatically`**|`false`|Выполняет flush Persistence Context **перед** запросом, чтобы все ожидающие изменения были применены|

**Проблема без `clearAutomatically = true`:**
```java
@Modifying
@Query("UPDATE User u SET u.name = :newName WHERE u.id = :id")
void updateName(@Param("id") Long id, @Param("newName") String newName);
// В вызывающем коде:
User user = repository.findById(1L).get(); // загружена сущность в контекст
repository.updateName(1L, "New Name");     // UPDATE выполнен, но user в контексте НЕ обновлен!
System.out.println(user.getName());        // старое имя (stale)
```

**Решение с `clearAutomatically`:**
```java
@Modifying(clearAutomatically = true)
@Query("UPDATE User u SET u.name = :newName WHERE u.id = :id")
void updateName(...);
```

После выполнения кэш очищается, и при следующем `findById()` данные будут свежими.

**`flushAutomatically` — когда нужно:**

```java
@Modifying(flushAutomatically = true)
@Query("UPDATE User u SET u.name = :newName WHERE u.id = :id")
void updateName(...);
```

Заставляет Hibernate отправить все ожидающие INSERT/UPDATE/DELETE в БД **до** выполнения этого запроса. Полезно, если предыдущие операции в той же транзакции должны быть видны в UPDATE.

**Важный нюанс:** `@Modifying` запросы **не возвращают обновленные сущности** — только количество измененных строк (int). Если нужно вернуть обновленные данные, лучше использовать `@Query` с SELECT после UPDATE или загрузить сущность через `findById()`.

---

### 79. Проблема `LazyInitializationException`. Как решить?

**Ответ:** `LazyInitializationException` возникает, когда **лениво загруженная ассоциация** (FetchType.LAZY) пытается быть доступной **за пределами открытой Hibernate-сессии** (EntityManager).

**Типичный сценарий:**
```java
@RestController
public class UserController {
    @GetMapping("/user/{id}")
    public UserDto getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        // Пытаемся обратиться к user.getOrders() уже после закрытия сессии
        return new UserDto(user.getId(), user.getOrders().size()); // LazyInitializationException
    }
}
```

**Почему возникает:**

1. В сервисном методе `@Transactional` открыта сессия.
    
2. Возвращается сущность с ленивой коллекцией `orders`.
    
3. Транзакция (и сессия) закрывается.
    
4. В контроллере пытаемся обратиться к `orders` — сессии уже нет, Hibernate не может подгрузить данные.
    

**Способы решения (от лучшего к худшему):**

|Способ|Описание|Когда использовать|
|---|---|---|
|**1. `JOIN FETCH` в JPQL**|Загружает ассоциацию в том же запросе|**Рекомендуемый** — один запрос, полный контроль|
|**2. `@EntityGraph`**|Декларативный способ загрузки графа сущностей|Когда нужно переиспользовать или гибко настраивать|
|**3. `@Transactional` на контроллере**|Открывает транзакцию на весь HTTP-запрос|**Антипаттерн** (OSIV), только для простых случаев|
|**4. DTO проекция**|Возвращать не сущность, а DTO с нужными полями|Самый производительный, но требует дополнительного кода|
|**5. `Hibernate.initialize()`**|Принудительная инициализация внутри транзакции|Когда нет другого выхода|

**Пример JOIN FETCH:**
```java
@Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id = :id")
Optional<User> findByIdWithOrders(@Param("id") Long id);
```

**Пример @EntityGraph:**
```java
@EntityGraph(attributePaths = {"orders"})
@Query("SELECT u FROM User u WHERE u.id = :id")
Optional<User> findByIdWithOrders(@Param("id") Long id);
```

**Что такое OSIV (Open Session In View):** Механизм Spring, который держит сессию открытой на протяжении всего HTTP-запроса. Включается через `spring.jpa.open-in-view=true` (по умолчанию true). Это **антипаттерн** для высоконагруженных систем, так как может приводить к утечкам соединений и проблемам с производительностью.

---

### 80. Что такое `@EntityGraph`? Когда использовать?

**Ответ:** `@EntityGraph` — аннотация Spring Data JPA, которая позволяет **явно указать**, какие ассоциации сущности должны быть загружены (жадно), без написания `JOIN FETCH` в JPQL.

**Преимущества перед `JOIN FETCH`:**

- **Переиспользование** — можно определить именованный граф на сущности и использовать в разных репозиториях.
    
- **Гибкость** — можно загружать разные наборы полей в разных методах.
    
- **Чистота кода** — JPQL остается простым, без `JOIN FETCH`.
    

**Определение графа на сущности:**
```java
@Entity
@NamedEntityGraph(name = "User.withOrders", attributeNodes = @NamedAttributeNode("orders"))
@NamedEntityGraph(name = "User.withOrdersAndAddress", attributeNodes = {
    @NamedAttributeNode("orders"),
    @NamedAttributeNode("address")
})
public class User {
    @Id
    private Long id;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;
    
    @OneToOne(fetch = FetchType.LAZY)
    private Address address;
}
```

**Использование в репозитории:**
```java
public interface UserRepository extends JpaRepository<User, Long> {
    
    @EntityGraph("User.withOrders")
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdWithOrders(@Param("id") Long id);
    
    // Альтернативный синтаксис без именованного графа
    @EntityGraph(attributePaths = {"orders", "address"})
    Optional<User> findById(Long id);
}
```

**Типы `@EntityGraph`:**

|Тип|Описание|
|---|---|
|`FETCH` (по умолчанию)|Загружает только указанные атрибуты, остальные остаются ленивыми|
|`LOAD`|Загружает указанные атрибуты **дополнительно** к уже настроенным EAGER|

**Когда использовать `@EntityGraph`:**

- Нужно загружать разные комбинации ассоциаций в разных местах приложения.
    
- Хочется отделить логику загрузки от JPQL-запроса.
    
- В проекте принят декларативный стиль конфигурации.
    

**Когда использовать `JOIN FETCH`:**

- Единичный случай, не требующий переиспользования.
    
- Нужен `DISTINCT` для устранения дубликатов (хотя `@EntityGraph` тоже можно с DISTINCT).
    
- Более сложные условия на присоединяемых таблицах.
    

---

### 81. Что такое `@Transactional` на методах репозитория?

**Ответ:** `@Transactional` на репозиториях Spring Data JPA управляет **границами транзакций** для методов доступа к данным.

**Практика по умолчанию в Spring Data JPA:**
```java
@Repository
@Transactional(readOnly = true)  // на уровне класса — readOnly для всех методов
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Transactional(readOnly = false)  // переопределяем для изменяющих методов
    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") String status);
    
    // findById наследует readOnly = true
    Optional<User> findById(Long id);
    
    // save переопределен в JpaRepository и имеет @Transactional(readOnly = false)
}
```

**Почему `readOnly = true` на классе:**

|Аспект|`readOnly = true`|`readOnly = false` (по умолчанию)|
|---|---|---|
|**Flush mode**|`FlushMode.NEVER` (Hibernate не проверяет изменения)|`FlushMode.AUTO` (проверяет изменения перед запросом)|
|**Dirty checking**|Отключено (не нужно)|Включено|
|**Производительность**|Выше (меньше накладных расходов)|Ниже|
|**Случайные изменения**|Не будут сохранены|Будут сохранены|

**Важный нюанс:** Методы, которые изменяют данные (`save()`, `delete()`, `@Modifying`), должны иметь `readOnly = false`. В `JpaRepository` эти методы уже аннотированы с `@Transactional(readOnly = false)`, поэтому их не нужно переопределять.

**Когда НЕ нужно ставить `@Transactional` на репозиторий:**

- Если транзакции управляются на уровне сервисов (самый распространенный подход). Тогда репозиторий должен быть **без** `@Transactional`, а сервисный метод — с `@Transactional`.
    

**Рекомендация:** Обычно `@Transactional` ставят **на уровне сервисов**, а репозитории оставляют без транзакций. Это дает контроль над границами транзакций (несколько репозиториев в одной транзакции). Но подход с `@Transactional(readOnly = true)` на репозитории полезен как дополнительная защита от случайных изменений.

---

### 82. Что такое `Pessimistic` и `Optimistic` locking в Spring Data? (`@Version`)

**Ответ:** Locking (блокировки) — механизмы предотвращения **потери обновлений** (lost update) при параллельном доступе к одним и тем же данным.

**Сравнение подходов:**

|Характеристика|Optimistic Locking|Pessimistic Locking|
|---|---|---|
|**Механизм**|Проверка версии при обновлении|Блокировка строки в БД|
|**Когда срабатывает**|При COMMIT, если версия изменилась|При SELECT, блокирует строку до COMMIT|
|**Использование в JPA**|`@Version`|`LockModeType.PESSIMISTIC_*`|
|**Производительность**|Высокая (нет блокировок)|Низкая (блокировки)|
|**Риск deadlock'а**|Нет|Есть|
|**Когда использовать**|Чаще чтение, реже запись|Частые конфликтные обновления|

**Optimistic Locking с `@Version`:**
```java
@Entity
public class Product {
    @Id
    private Long id;
    
    private String name;
    private int quantity;
    
    @Version
    private Long version;  // автоматически увеличивается при каждом UPDATE
}
```

При обновлении Hibernate выполняет:
```sql
UPDATE product SET name=?, quantity=?, version=? WHERE id=? AND version=?
```

Если `version` изменился другой транзакцией, обновление не затронет строки, Hibernate выбросит `OptimisticLockException`.

**Pessimistic Locking в репозитории:**
```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithPessimisticLock(@Param("id") Long id);
}
```

Генерирует `SELECT ... FOR UPDATE` (в PostgreSQL, Oracle, MySQL с InnoDB).

**Типы pessimistic блокировок:**

|LockModeType|SQL|Описание|
|---|---|---|
|`PESSIMISTIC_READ`|`SELECT ... FOR SHARE`|Другие транзакции могут читать, но не обновлять|
|`PESSIMISTIC_WRITE`|`SELECT ... FOR UPDATE`|Полная блокировка на запись|
|`PESSIMISTIC_FORCE_INCREMENT`|`SELECT ... FOR UPDATE` + увеличивает version|Комбинация pessimistic + optimistic|

**Timeout для pessimistic lock:**
```java
@Query("SELECT p FROM Product p WHERE p.id = :id")
@Lock(LockModeType.PESSIMISTIC_WRITE)
@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "5000")})
Optional<Product> findByIdWithLock(@Param("id") Long id);
```

---

### 83. Паттерн Specification (Spring Data JPA Criteria API)

**Ответ:** Specification — это паттерн, реализованный в Spring Data JPA, который позволяет **строить динамические запросы** программно, комбинируя условия, без написания JPQL с кучей необязательных параметров.

**Проблема, которую решает Specification:**
```java
// Без Specification — ужасный метод с кучей параметров и null-проверок
@Query("SELECT u FROM User u WHERE " +
       "(:name IS NULL OR u.name = :name) AND " +
       "(:age IS NULL OR u.age >= :age)")
List<User> search(@Param("name") String name, @Param("age") Integer age);
```

**С Specification:**
```java
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}
// Создание спецификаций
public class UserSpecifications {
    public static Specification<User> nameEquals(String name) {
        return (root, query, cb) -> cb.equal(root.get("name"), name);
    }
    
    public static Specification<User> ageGreaterThanOrEqual(Integer age) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("age"), age);
    }
}
// Использование
Specification<User> spec = Specification
    .where(UserSpecifications.nameEquals("John"))
    .and(UserSpecifications.ageGreaterThanOrEqual(18));
    
List<User> users = userRepository.findAll(spec);
```

**Ключевые методы `Specification`:**

|Метод|Описание|
|---|---|
|`where(Specification<T>)`|Стартовая точка|
|`and(Specification<T>)`|Логическое И|
|`or(Specification<T>)`|Логическое ИЛИ|
|`not(Specification<T>)`|Логическое НЕ|

**Пример сложного динамического поиска:**
```java
@Service
public class UserSearchService {
    
    public List<User> searchUsers(String name, Integer minAge, String email, Boolean active) {
        Specification<User> spec = Specification.where(null);
        
        if (name != null) {
            spec = spec.and((root, query, cb) -> 
                cb.like(root.get("name"), "%" + name + "%"));
        }
        if (minAge != null) {
            spec = spec.and((root, query, cb) -> 
                cb.greaterThanOrEqualTo(root.get("age"), minAge));
        }
        if (email != null) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(root.get("email"), email));
        }
        if (active != null) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(root.get("active"), active));
        }
        
        return userRepository.findAll(spec);
    }
}
```

**Преимущества:**

- Типобезопасность (в отличие от JPQL строк).
    
- Переиспользование условий.
    
- Легко комбинировать.
    
- Интеграция с пагинацией и сортировкой: `findAll(spec, pageable)`.
    

**Недостатки:**

- Громоздкость для простых запросов.
    
- Сложнее читать, чем JPQL.
    
- Кривой API с корнями, запросами и билдерами.
    

---

### 84. Что такое `Pageable` и `Page<T>`? Как работает пагинация в БД?

**Ответ:** `Pageable` — интерфейс Spring Data для передачи параметров пагинации (номер страницы, размер страницы, сортировка). `Page<T>` — объект-ответ, содержащий данные текущей страницы и метаинформацию.

**Использование в репозитории:**
```java
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);
    Page<User> findByActiveTrue(Pageable pageable);
    Page<User> findByNameContaining(String name, Pageable pageable);
}
```

**Пример в контроллере:**
```java
@GetMapping("/users")
public Page<UserDto> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "id,asc") String[] sort) {
    
    Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
    Page<User> userPage = userRepository.findAll(pageable);
    
    return userPage.map(UserDto::fromEntity);
}
```

**Структура `Page<T>`:**
```java
public interface Page<T> {
    List<T> getContent();        // данные текущей страницы
    int getNumber();              // номер текущей страницы (0-based)
    int getSize();                // размер страницы
    int getTotalPages();          // общее количество страниц
    long getTotalElements();      // общее количество записей
    boolean isFirst();            // первая ли страница
    boolean isLast();             // последняя ли страница
    boolean hasNext();            // есть ли следующая
    boolean hasPrevious();        // есть ли предыдущая
    Sort getSort();               // параметры сортировки
    Pageable getPageable();       // следующий Pageable
}
```

**Как работает пагинация в БД (под капотом):**
```sql
-- PostgreSQL / MySQL (LIMIT/OFFSET)
SELECT * FROM users ORDER BY id LIMIT 20 OFFSET 40;  -- страница 2, размер 20
-- Oracle (ROWNUM или OFFSET/FETCH)
SELECT * FROM users OFFSET 40 ROWS FETCH NEXT 20 ROWS ONLY;
```

**Важные нюансы:**

1. **Проблема больших OFFSET:** При больших номерах страниц (`OFFSET 100000`) БД все равно сканирует все предыдущие строки, что очень медленно. Решение — **keyset pagination** (использование `WHERE id > lastId`).
    
2. **Подсчет общего количества (count query):** `Page<T>` выполняет **два запроса**:
    
    - Основной запрос с LIMIT/OFFSET.
        
    - `COUNT(*)` запрос для определения `totalElements`. На больших таблицах это дорого.
        
3. **Оптимизация с `Slice<T>`:** Если не нужно общее количество, используйте `Slice<T>` — выполняется только один запрос с LIMIT + 1.
    

```java
Slice<User> findByActiveTrue(Pageable pageable);  // только данные, без count
```

**Keyset pagination (для больших таблиц):**
```java
@Query("SELECT u FROM User u WHERE u.id > :lastId ORDER BY u.id ASC")
List<User> findAfterId(@Param("lastId") Long lastId, Pageable pageable);
```

---

### 85. Как логировать SQL запросы со значениями параметров?

**Ответ:** В Spring Boot есть несколько способов логирования SQL-запросов с параметрами.

**Способ 1: Через `application.properties` (простой, но параметры отдельно):**
```properties
# Показывать SQL
spring.jpa.show-sql=true
# Форматировать SQL для читаемости
spring.jpa.properties.hibernate.format_sql=true
# Показывать параметры (?? вместо значений)
logging.level.org.hibernate.SQL=DEBUG
# Показывать значения параметров
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

**Способ 2: Через `logback-spring.xml` (более гибкий):**
```xml
<logger name="org.hibernate.SQL" level="DEBUG"/>
<logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="TRACE"/>
<logger name="org.hibernate.engine.jdbc.batch.internal.BatchingBatch" level="DEBUG"/>
```
**Способ 3: Использование `p6spy` (самый мощный — видит реальные значения, подставленные в запрос):**
```xml
<dependency>
    <groupId>p6spy</groupId>
    <artifactId>p6spy</artifactId>
    <version>3.9.1</version>
</dependency>
```

```properties
# application.properties
spring.datasource.url=jdbc:p6spy:postgresql://localhost:5432/mydb
spring.datasource.driver-class-name=com.p6spy.engine.spy.P6SpyDriver
```

Настройка `spy.properties`:
```properties
appender=com.p6spy.engine.spy.appender.StdoutLogger
logMessageFormat=com.p6spy.engine.spy.appender.CustomLineFormat
customLogMessageFormat=%(currentTime) | %(category) | connection %(connectionId) | %(sql)
```

**Что покажет каждый уровень логирования:**

|Настройка|Пример вывода|
|---|---|
|`spring.jpa.show-sql=true`|`select user0_.id as id1_0_, user0_.name as name2_0_ from users user0_`|
|`logging.level.org.hibernate.SQL=DEBUG`|То же, но с меткой времени и потоком|
|`logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE`|`binding parameter [1] as [VARCHAR] - [John]`|

**Пример полного вывода с TRACE:**
```text
DEBUG [main] org.hibernate.SQL - select * from users where name=?
TRACE [main] org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [1] as [VARCHAR] - [John]
```

**Рекомендация для разработки:** Используйте `show-sql=true` + `format_sql=true` + `BasicBinder=TRACE`.

**Для production:** Отключите подробное логирование SQL (оставляет только WARN/ERROR) из-за производительности и безопасности (пароли в логах). Используйте p6spy только для отладки.

---

[⬅️**Previous**](Группа_06-Spring_Boot_Core)          [⬆️](SPRING-121_вопрос_на_middle)          [**Next**➡️](Группа_08-Spring_Security)
