# Отличия `@Component` vs `@Bean`

---
## Полная таблица отличий `@Component` vs `@Bean`

|Характеристика|`@Component`|`@Bean`|
|---|---|---|
|Где ставится|На классе|На методе|
|Кто пишет код класса|Вы сами|Вы или сторонняя библиотека|
|Контроль над созданием|Spring через рефлексию|Вы сами (new, фабрика, builder)|
|Внешние классы (из JAR)|❌ Нельзя|✅ Можно обернуть|
|**Имена бинов**|По умолчанию: ClassName -> camelCase|По умолчанию: имя метода|
|**Несколько имён (алиасы)**|❌ Не поддерживается|✅ `@Bean(name = {"bean1", "bean2"})`|
|**Параметры/зависимости**|Через поля конструктора (внедрение)|Как аргументы метода|
|**Условное создание**|`@ConditionalOnClass` и т.д.|`@ConditionalOnClass`, `@ConditionalOnMissingBean` и т.д.|
|**Инициализация/уничтожение**|`@PostConstruct`, `@PreDestroy`|`initMethod`, `destroyMethod`|
|**Тип возвращаемого значения**|Сам класс|Любой тип (интерфейс, абстрактный класс)|
|**Проксирование (scope)**|Да, через `@Scope`|Да, через `@Scope`|

---
## Алиасы (несколько имён)

### `@Component` – НЕТ алиасов
```java
@Component
public class MyService {
    // Можно задать ТОЛЬКО одно имя через @Component("customName")
    // Несколько имён указать НЕЛЬЗЯ
}
```
### `@Bean` – ЕСТЬ алиасы
```java
@Configuration
public class AppConfig {
    
    @Bean(name = {"weatherService", "weatherApi", "openweather"})
    public WeatherService weatherService() {
        return new OpenWeatherService();
    }
}

// Использование по любому из трёх имён:
@Autowired
@Qualifier("weatherApi")
private WeatherService service;  // Работает!
```

---
## Параметры бинов

Это **КЛЮЧЕВОЕ отличие**!

### `@Component` – зависимости через поля/конструктор
```java
@Component
public class UserService {
    private final UserRepository repository;
    
    // Зависимости внедряются Spring'ом автоматически
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

### `@Bean` – зависимости как параметры метода
```java
@Configuration
public class AppConfig {
    
    @Bean
    public UserService userService(UserRepository repository,  // Spring подставит бин
                                   MailSender mailSender,     // Spring подставит бин
                                   String appName) {         // Или значение из properties
        return new UserService(repository, mailSender, appName);
    }
}
```

**Важное преимущество – динамическое создание с разными параметрами:**
```java

```


@Configuration
public class DataSourceConfig {
    
    @Bean
    public DataSource dataSource1() {
        return createDataSource("db1", 5432);
    }
    
    @Bean
    public DataSource dataSource2() {
        return createDataSource("db2", 5433);
    }
    
    private DataSource createDataSource(String host, int port) {
        // Каждый вызов может вернуть НАСТРОЕННЫЙ разный бин
        return new HikariDataSource(/* ... */);
    }
}

---

## Возврат интерфейса

### `@Component` – возвращает конкретный класс

java

@Component
public class JdbcUserRepository implements UserRepository {
    // В контейнере будет бин типа JdbcUserRepository
}
// Можно внедрить по интерфейсу:
@Autowired
private UserRepository repository;  // Работает, но бин-то конкретный

### `@Bean` – может вернуть интерфейс

java

@Configuration
public class AppConfig {
    
    @Bean
    public UserRepository userRepository() {
        // Решаем во время выполнения, какую реализацию создать
        if (useMongo) {
            return new MongoUserRepository();
        } else {
            return new JdbcUserRepository();
        }
    }
}

---

## Инициализация и уничтожение

### `@Component`

java

@Component
public class MyService {
    
    @PostConstruct
    public void init() {
        System.out.println("Инициализация");
    }
    
    @PreDestroy
    public void cleanup() {
        System.out.println("Уничтожение");
    }
}

### `@Bean`

java

@Configuration
public class AppConfig {
    
    @Bean(initMethod = "start", destroyMethod = "stop")
    public SomeLibraryClass someLibrary() {
        return new SomeLibraryClass();  // Чужой класс, нет аннотаций
    }
}

---

## Условное создание (пример)

java

@Configuration
public class DatabaseConfig {
    
    @Bean
    @ConditionalOnProperty(name = "db.type", havingValue = "postgres")
    public DataSource postgresDataSource() {
        return new PostgresDataSource();
    }
    
    @Bean
    @ConditionalOnProperty(name = "db.type", havingValue = "mysql")
    public DataSource mysqlDataSource() {
        return new MySqlDataSource();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public DataSource defaultDataSource() {
        return new H2DataSource();  // Если ничего не подошло
    }
}

Для `@Component` тоже есть `@Conditional`, но для `@Bean` это более гибко.

---

## Итог: когда что использовать?

|Сценарий|Решение|
|---|---|
|**Ваш собственный класс**|`@Component` (проще и понятнее)|
|**Нужно несколько экземпляров с разной конфигурацией**|`@Bean`|
|**Нужно дать бину несколько имён (алиасы)**|`@Bean`|
|**Класс из сторонней библиотеки**|`@Bean`|
|**Нужна сложная логика создания (if/switch, try/catch)**|`@Bean`|
|**Бин может отсутствовать или быть условным**|`@Bean`|
|**Простота и стандартный DI**|`@Component`|








