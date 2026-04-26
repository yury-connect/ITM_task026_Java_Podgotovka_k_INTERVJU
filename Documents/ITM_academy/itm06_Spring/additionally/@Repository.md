# Обработка исключений аннотацией `@Repository` 🚀

---
Аннотация `@Repository` не только помечает класс как компонент для работы с данными, но и автоматически **перехватывает и преобразует** исключения, связанные с доступом к данным, в унифицированные исключения Spring из пакета `org.springframework.dao`. Это упрощает обработку ошибок в приложении.

## Какие исключения обрабатывает `@Repository`? 🔍
`@Repository` работает в связке с **PersistenceExceptionTranslationPostProcessor**, который автоматически переводит специфические для технологий исключения (например, JDBC, Hibernate, JPA) в иерархию исключений Spring `DataAccessException`.

### Основные категории обрабатываемых исключений:
1. **JDBC**:
    - `SQLException` → Преобразуется в подклассы `DataAccessException` (например, `DataIntegrityViolationException`, `DuplicateKeyException`).
2. **Hibernate**:
    - `HibernateException` → Преобразуется в соответствующие исключения, такие как `ConstraintViolationException` или `NonUniqueResultException`.
3. **JPA**:
    - `PersistenceException`, `EntityNotFoundException` → Преобразуются в `DataAccessException` (например, `EmptyResultDataAccessException`).
4. **Другие ORM**:
    - Исключения других ORM-фреймворков (например, MyBatis) также преобразуются, если используется соответствующий адаптер.

### Примеры преобразований:

| Технология | Исходное исключение                         | Преобразуется в (Spring)                         |
| :--------- | :------------------------------------------ | :----------------------------------------------- |
| JDBC       | `SQLException` <br>(нарушение уникальности) | `DuplicateKeyException`                          |
| Hibernate  | `ConstraintViolationException`              | `DataIntegrityViolationException`                |
| JPA        | `NoResultException`                         | `EmptyResultDataAccessException`                 |
| JPA        | `EntityNotFoundException`                   | `EmptyResultDataAccessException`                 |
| Общее      | `OptimisticLockingFailureException`         | `OptimisticLockingFailureException` <br>(Spring) |

---
## Как это работает? 🛠️

1. **Аннотация `@Repository`**:
    - Помечает класс как бин, предназначенный для работы с данными.
    - Регистрирует его для обработки исключений через `PersistenceExceptionTranslator`.
    
2. **PersistenceExceptionTranslationPostProcessor**:
    - Автоматически применяется к бинам с `@Repository`.
    - Перехватывает исключения, выбрасываемые в методах класса, <br>и преобразует их в `DataAccessException`.
    
3. **Иерархия `DataAccessException`**:
    - Это непроверяемые исключения (`RuntimeException`), <br>что упрощает их обработку.
    - Включает множество подклассов для точной классификации <br>ошибок (например, `DataAccessResourceFailureException`, `InvalidDataAccessApiUsageException`).

---
## Пример работы 🎯
Предположим, у нас есть репозиторий, который пытается выполнить запрос, но запись не найдена:
```java
@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public User findById(Long id) {
        try {
            return entityManager
	            .createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            // NoResultException преобразуется в EmptyResultDataAccessException
            throw new EmptyResultDataAccessException("User not found", 1);
        }
    }
}
```

**Что происходит**:
- Если запрос выбрасывает `NoResultException` (JPA), то `@Repository` автоматически преобразует его в `EmptyResultDataAccessException`.
- Разработчику не нужно вручную обрабатывать `NoResultException` — он работает с унифицированным исключением Spring.

---
## Почему это полезно? 🌟
- **Унификация**: Разные технологии (JDBC, Hibernate, JPA) имеют свои исключения, но `@Repository` приводит их к единой иерархии `DataAccessException`.
- **Читаемость**: Код становится чище, так как не нужно обрабатывать специфические исключения каждой технологии.
- **Гибкость**: Легко переключаться между ORM или JDBC, не меняя логику обработки ошибок.
- **Совместимость с транзакциями**: `DataAccessException` — это `RuntimeException`, что идеально для отката транзакций с `@Transactional`.

---
## Как включить обработку исключений? ⚙️

1. **Аннотация `@Repository`**:
    - Достаточно пометить класс, и Spring автоматически применит обработку исключений.
    
2. **Включение обработки исключений**:
    - В Spring Boot это работает автоматически благодаря автоконфигурации.
    - В обычном Spring нужно добавить:
        
```java
@Configuration
@EnableJpaRepositories
public class AppConfig {
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
}
```

---
## Подводные камни ⚠️
1. **Только для `@Repository`**:
    - Обработка исключений применяется только к классам, помеченным `@Repository`. Если использовать `@Component`, исключения не преобразуются.
2. **Ограниченная область действия**:
    - Преобразование работает только для исключений, связанных с доступом к данным. Например, `NullPointerException` не затрагивается.
3. **Зависимость от технологии**:
    - Для корректной работы нужен правильный `PersistenceExceptionTranslator` (например, для Hibernate или JPA).

---
## Итоги 🎯
Аннотация `@Repository` автоматически преобразует специфические исключения (JDBC `SQLException`, Hibernate `HibernateException`, JPA `PersistenceException`) в унифицированные исключения Spring (`DataAccessException`). Это упрощает обработку ошибок, делает код чище и поддерживает переключение между технологиями без изменения логики.

---
