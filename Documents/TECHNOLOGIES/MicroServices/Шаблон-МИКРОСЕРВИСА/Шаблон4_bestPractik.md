---
tags:
  - TECHNOLOGIES/Microsevices
  - TECHNOLOGIES/Microsevices/Шаблон
---
# Архитектура и слои микросервиса

Предлагаемая структура проекта следует классической слоистой архитектуре Spring Boot, где каждый пакет отвечает за свою ответственность. 
Например:
```text
src/
├─ main/
│  ├─ java/com/example/adviceuserservice/
│  │   ├─ controller/       # REST-контроллеры (обработка HTTP-запросов):contentReference[oaicite:0]{index=0}  
│  │   ├─ service/          # Сервисный слой – бизнес-логика, транзакции:contentReference[oaicite:1]{index=1}  
│  │   ├─ repository/       # Слой доступа к данным – Spring Data JPA репозитории:contentReference[oaicite:2]{index=2}  
│  │   ├─ model/            # Сущности JPA (Entity) и DTO (модели данных):contentReference[oaicite:3]{index=3}:contentReference[oaicite:4]{index=4}  
│  │   ├─ config/           # Конфигурации приложения (безопасность, CORS и др.)  
│  │   └─ exception/        # Глобальный обработчик ошибок (@ControllerAdvice):contentReference[oaicite:5]{index=5}  
│  └─ resources/  
│       ├─ application.yml  # Настройки (БД, Spring Security, Flyway, Actuator и т.д.)  
│       └─ db/migration/    # Миграции Flyway (SQL-скрипты для инициализации БД)  
└─ test/                   # Unit- и интеграционные тесты (JUnit 5, Mockito, Testcontainers/H2)  
Dockerfile                 # Сборка Docker-образа приложения:contentReference[oaicite:6]{index=6}  
docker-compose.yml         # Поднятие сервиса + БД (PostgreSQL) в контейнерах:contentReference[oaicite:7]{index=7}:contentReference[oaicite:8]{index=8}
```

```text
src/
├─ main/
│  ├─ java/com/example/adviceuserservice/
│  │   ├─ controller/
│  │   ├─ service/
│  │   ├─ repository/
│  │   ├─ model/
│  │   ├─ config/
│  │   └─ exception/
│  └─ resources/  
│       ├─ application.yml
│       └─ db/migration/
└─ test/
Dockerfile
docker-compose.yml
```

Каждый слой обеспечивает **разделение ответственности**: контроллеры принимают и валидируют HTTP-запросы, сервисы реализуют бизнес-логику, репозитории выполняют запросы к БД, а модели (Entity/DTO) инкапсулируют данные. Такая архитектура улучшает читаемость, расширяемость и сопровождение кода[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=2%EF%B8%8F%E2%83%A3%20Controller%20Layer%20,Requests%20%26%20Responses)[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=Summary%20of%20Spring%20Boot%20Architecture). Модели (Entity) не следует напрямую возвращать клиенту: вместо этого используются DTO, например, Java `record`, обеспечивающие иммутабельность и удобную сериализацию[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=Entities%20are%20mapped%20to%20database,help%20transfer%20only%20required%20data)[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=,record). Глобальная обработка ошибок с помощью `@RestControllerAdvice` (@ControllerAdvice) позволяет централизованно формировать HTTP-ответы на исключения[baeldung.com](https://www.baeldung.com/exception-handling-for-rest-with-spring#:~:text=A%20%40ControllerAdvice%20contains%20code%20that,response%20body%2C%20there%E2%80%99s%20a%20%40RestControllerAdvice).

## Контроллеры
Контроллеры помечаются `@RestController` и обычно содержат аннотации маршрутизации (`@GetMapping`, `@PostMapping` и т.д.). Они не выполняют бизнес-логику, а лишь делегируют её в сервисный слой[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=%E2%9C%94%20Receives%20client%20requests%20%28,Returns%20the%20appropriate%20HTTP%20response)[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=public%20ProductController%28ProductService%20productService%29%20,productService%3B). 
Пример контроллера:
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return userService.getUserById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody UserDTO userDto) {
        return userService.createUser(userDto);
    }
    // другие endpoints...
}
```

Этот контроллер принимает HTTP-запросы, вызывает методы сервисов и возвращает готовые DTO. Логика преобразования `User <-> UserDTO` и проверки находятся в сервисе или маппере. Контроллер сам по себе не должен содержать «тяжёлой» логики – он лишь «направляет» запрос дальше[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=2%EF%B8%8F%E2%83%A3%20Controller%20Layer%20,Requests%20%26%20Responses)[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=3%EF%B8%8F%E2%83%A3%20Service%20Layer%20,Processing).
## Сервисный слой
Сервисные классы помечены `@Service` и реализуют бизнес-логику приложения. Они взаимодействуют с репозиториями, выполняют необходимые вычисления и формируют модели/DTO. В сервисах удобно использовать `@Transactional` для группировки нескольких операций БД в одну транзакцию (атомарно)[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=3%EF%B8%8F%E2%83%A3%20Service%20Layer%20,Processing). Пример сервиса:
```java
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper; // конвертер Entity <-> DTO
	
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
	
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
            .map(userMapper::toDto);
    }
	
    @Transactional
    public UserDTO createUser(UserDTO dto) {
        User user = userMapper.toEntity(dto);
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }
    // другие методы...
}
```

Сервисный слой **отделяет контроллеры от репозиториев** и содержит бизнес-правила (например, проверку уникальности email, хеширование пароля и т.д.). Все операции с БД через `userRepository` обёрнуты в транзакцию Spring (`@Transactional`) для обеспечения консистентности (при ошибке транзакция откатится). Сам интерфейс репозитория прост: он наследуется от `JpaRepository` и автоматически получает набор CRUD-методов[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=Spring%20Data%20JPA%20reduces%20the,etc).
## Репозитории и модели
Репозитории — это интерфейсы, аннотированные `@Repository` (неявно через `JpaRepository`). Они отвечают за доступ к данным с помощью Spring Data JPA. 
Например:
```java
public interface UserRepository extends JpaRepository<User, Long> {
    // при необходимости можно добавить методы вида findByEmail
}
```

Spring Data JPA сам реализует большинство CRUD-методов (`findAll()`, `save()`, `deleteById()` и т.д.), избавляя от шаблонного кода[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=Spring%20Data%20JPA%20reduces%20the,etc). Репозитории взаимодействуют с сущностями (Entity) – классами, помеченными `@Entity`. 
Например:
```java
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    // геттеры/сеттеры...
}
```

Для передачи данных клиенту и между слоями лучше использовать DTO. 
Например, с помощью Java `record`:
```java
public record UserDTO(Long id, String username, String email) { }
```

**Важно:** сами `Entity` не возвращаются клиенту напрямую – для API используются DTO, чтобы скрыть внутренние детали БД[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=Entities%20are%20mapped%20to%20database,help%20transfer%20only%20required%20data)[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=,record).

## Обработка исключений
Глобальный обработчик ошибок реализуется с помощью `@RestControllerAdvice` (наследует или совместим с `ResponseEntityExceptionHandler`). Такой класс перехватывает исключения из контроллеров и формирует единообразный ответ (JSON-объект с описанием ошибки). Пример:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
	
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(EntityNotFoundException ex) {
        return new ErrorResponse("NOT_FOUND", ex.getMessage());
    }
	
    // другие обработчики исключений...
}
```

Использование `@ControllerAdvice` позволяет охватить все контроллеры приложения общим кодом обработки ошибок[baeldung.com](https://www.baeldung.com/exception-handling-for-rest-with-spring#:~:text=A%20%40ControllerAdvice%20contains%20code%20that,response%20body%2C%20there%E2%80%99s%20a%20%40RestControllerAdvice). Это повышает согласованность API и упрощает отладку.

## Безопасность: JWT и OAuth2
Для аутентификации и авторизации используется Spring Security. **JWT** (JSON Web Token) позволяет сделать приложение без состояния (stateless): при логине пользователь получает токен, который отправляет в заголовке `Authorization` при последующих запросах. В `SecurityConfig` регистрируется фильтр для проверки JWT на каждый запрос. Пример конфигурации:
```java
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Реализация фильтра JWT (jwtAuthFilter) и провайдеров...
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and()       // включаем CORS (см. ниже)
            .csrf().disable()   // обычно CSRF отключается для JWT (stateless)
            .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .oauth2Login();     // включаем OAuth2/Social Login (Google, GitHub и т.д.)
        
        http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
    }
	
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        // настройка разрешённых origin, методов, заголовков
    }
}
```

- **JWT:** Для генерации и верификации JWT обычно используется библиотека (например, `io.jsonwebtoken`). Настройка Spring Security включает проверку подписи и сроков годности токена. При каждой защищённой операции проверяется валидность токена и роль пользователя (настройка `@PreAuthorize` или `hasRole` в конфиге).
    
- **OAuth2:** Spring Boot имеет встроенную поддержку OAuth2/Social login. Через `spring.security.oauth2.client` в `application.yml` настраиваются клиенты (Google, GitHub и др.). После редиректа в провайдера пользователь возвращается в приложение и становится аутентифицированным. Пример: включение `oauth2Login()` в конфиге позволяет войти через GitHub/Google без дополнительного кода[spring.io](https://spring.io/guides/tutorials/spring-boot-oauth2/#:~:text=This%20guide%20shows%20you%20how,0%20and%20Spring%20Boot). Spring-руководство показывает применение OAuth2-клиентов с GitHub/Google в одном приложении[spring.io](https://spring.io/guides/tutorials/spring-boot-oauth2/#:~:text=This%20guide%20shows%20you%20how,0%20and%20Spring%20Boot).
    
- **Keycloak/OpenID:** Для корпоративной аутентификации можно использовать Keycloak. Это отдельный сервер аутентификации (OpenID Connect), поддерживающий SSO, социальную аутентификацию, UI администрирования и т.п[baeldung.com](https://www.baeldung.com/spring-boot-keycloak#:~:text=We%E2%80%99ll%20use%20Keycloak%20as%20an,implementations%20with%20features%20like). Spring Boot интегрируется с Keycloak через `spring-security-oauth2` или специальный адаптер.
    
Также важно настроить **CORS** и **CSRF**. CORS (Cross-Origin) разрешает запросы с фронтенда на другом домене: в Spring это делается аннотацией `@CrossOrigin` на контроллере или глобально (см. пример из официального гида[spring.io](https://spring.io/guides/gs/rest-service-cors/#:~:text=Enabling%20CORS)). CSRF по умолчанию включён, но для JWT (stateless API) его часто отключают (`.csrf().disable()`), т.к. сам токен защищает от CSRF-атак. При использовании сессий или form-login CSRF следует оставить включённым.

## Работа с базой данных и миграции
Для работы с БД используется Spring Data JPA и реляционная БД (например, PostgreSQL). Настройки подключения хранятся в `application.yml` (`spring.datasource.*`)[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=spring.datasource.url%3Djdbc%3Ah2%3Amem%3Atestdb%20spring.datasource.driver,enabled%3Dtrue). Работа с транзакциями обеспечивается аннотацией `@Transactional` в сервисах. Репозитории (интерфейсы `JpaRepository`) автоматически выполняют транзакции для простых CRUD-методов[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=Spring%20Data%20JPA%20reduces%20the,etc).

Для версионирования схемы БД применяем **Flyway**. В папке `resources/db/migration/` размещаются SQL-скрипты вида `V1__init.sql`, `V2__add_fields.sql` и т.д. При старте приложения Flyway автоматически применит все неподтверждённые миграции. Flyway гарантирует, что все скрипты запускаются в одной транзакции («versioned migrations are applied in one go»)[baeldung.com](https://www.baeldung.com/database-migrations-with-flyway#:~:text=Migrations%20can%20either%20be%20versioned,every%20time%20their%20checksum%20changes), что исключает частичное применение изменений. Например, `spring.flyway.locations=classpath:db/migration` позволяет задать путь к миграциям, а `spring.flyway.baselineOnMigrate=true` – применить существующую БД как базовую версию.

Таким образом, при доработках и деплое новой версии приложения изменение схемы БД выполняется через контролируемые миграции Flyway, что упрощает поддержку и автоматизацию обновлений.

## Тестирование
Код покрывается тестами на нескольких уровнях:

- **Unit-тесты для сервисов и контроллеров:** с помощью JUnit 5 и Mockito создаются «моковые» зависимости и проверяются бизнес-методы. Spring Boot Starter Test включает необходимые библиотеки (JUnit, Mockito, AssertJ)[medium.com](https://medium.com/@Lakshitha_Fernando/spring-boot-unit-testing-for-repositories-controllers-and-services-using-junit-5-and-mockito-def3ff5891be#:~:text=In%20this%20tutorial%2C%20we%20will,using%20Junit%205%20and%20Mockito). Например, для `UserService` делается тестовый класс с аннотацией `@ExtendWith(MockitoExtension.class)`, где `UserRepository` — мок, а в `@BeforeEach` настраиваются ожидаемые ответы. Контроллеры можно тестировать с помощью `MockMvc` и `@WebMvcTest`, что позволяет эмулировать HTTP-запросы к методам контроллера[spring.io](https://spring.io/guides/gs/testing-web/#:~:text=What%20You%20Will%20Build).
    
- **Интеграционные тесты:** проверяют взаимодействие слоёв и базы. Здесь полезны **Testcontainers** — библиотека, запускающая настоящую БД (например, PostgreSQL) в Docker-контейнере во время теста. Например, можно аннотировать класс теста `@SpringBootTest` и `@ContextConfiguration`, чтобы перед запуском поднять контейнер с Postgres и подключить к нему Spring (как показано в Baeldung[baeldung.com](https://www.baeldung.com/spring-boot-testcontainers-integration-test#:~:text=In%20this%20tutorial%2C%20we%E2%80%99ll%20demonstrate,JPA%20and%20the%20PostgreSQL%20database)). Это позволяет убедиться, что запросы JPA работают с реальной БД, а не только в памяти. Альтернатива — встроенная H2 БД: можно пометить тесты `@DataJpaTest`, и Spring автоматически подключит в память H2 для проверки репозиториев.

Таким образом, покрытие тестами обеспечивает, что сервис и контроллеры работают как ожидалось, а интеграции с базой данных корректны[spring.io](https://spring.io/guides/gs/testing-web/#:~:text=What%20You%20Will%20Build)[baeldung.com](https://www.baeldung.com/spring-boot-testcontainers-integration-test#:~:text=In%20this%20tutorial%2C%20we%E2%80%99ll%20demonstrate,JPA%20and%20the%20PostgreSQL%20database).

## Docker и Docker Compose
Для развёртывания микросервиса и БД используются контейнеры. Пример простого **Dockerfile** для Spring Boot (на основе официального гайда[spring.io](https://spring.io/guides/gs/spring-boot-docker/#:~:text=FROM%20openjdk%3A8,jar%22%2C%22%2Fapp.jar)):
```dockerfile
FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Этот `Dockerfile` копирует собранный fat-JAR в контейнер и запускает его. Важный момент — запуск приложения не от имени root: можно добавить пользователя (аналогично примеру из гайда, см. `RUN adduser`[spring.io](https://spring.io/guides/gs/spring-boot-docker/#:~:text=FROM%20openjdk%3A8,jar%20COPY%20%24%7BJAR_FILE%7D%20app.jar)). Также хорошей практикой является multi-stage сборка (в Gradle/Maven) или использование Buildpacks (`bootBuildImage`), но базовый Dockerfile уже достаточен.

**docker-compose.yml** позволяет запустить сервис вместе с БД. Пример конфигурации для сервиса и PostgreSQL (по образцу Baeldung[baeldung.com](https://www.baeldung.com/spring-boot-postgresql-docker#:~:text=Now%20let%E2%80%99s%20write%20our%20Docker,and%20save%20it%20in%20src%2Fmain%2Fdocker)[baeldung.com](https://www.baeldung.com/spring-boot-postgresql-docker#:~:text=services%3A%20app%3A%20image%3A%20%27docker,postgres)):
```yaml
version: '3.8'
services:
  app:
    build: .
    image: myapp:latest
    container_name: app
    depends_on:
      - db
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/mydb
      SPRING_DATASOURCE_USERNAME: appuser
      SPRING_DATASOURCE_PASSWORD: secret
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
    ports:
      - "8080:8080"

  db:
    image: postgres:13-alpine
    container_name: db
    environment:
      POSTGRES_DB: mydb
      POSTGRES_USER: appuser
      POSTGRES_PASSWORD: secret
    volumes:
      - db-data:/var/lib/postgresql/data
volumes:
  db-data:
```

Тут сервис `app` (наше приложение) зависит от `db` (PostgreSQL). Параметры подключения (`SPRING_DATASOURCE_URL`, `USERNAME`, `PASSWORD`) передаются через переменные окружения, чтобы приложение могло сразу подключиться к контейнерной БД после старта[baeldung.com](https://www.baeldung.com/spring-boot-postgresql-docker#:~:text=Now%20let%E2%80%99s%20write%20our%20Docker,and%20save%20it%20in%20src%2Fmain%2Fdocker)[baeldung.com](https://www.baeldung.com/spring-boot-postgresql-docker#:~:text=services%3A%20app%3A%20image%3A%20%27docker,postgres). При `docker-compose up` сначала соберётся образ приложения, затем запустится контейнер с БД, а потом контейнер приложения подключится к нему. Такая схема упрощает локальную разработку и CI/CD.

## Логирование и мониторинг
По умолчанию Spring Boot использует **SLF4J + Logback** для логирования. Например, можно аннотировать классы Lombok-ом `@Slf4j`, или вручную создавать `Logger logger = LoggerFactory.getLogger(...)`. Spring Boot уже включает `spring-boot-starter-logging`, который настраивает Logback с разумными шаблонами и цветами вывода[baeldung.com](https://www.baeldung.com/spring-boot-logging#:~:text=When%20using%20starters%2C%20Logback%20is,used%20for%20logging%20by%20default). Логи выводятся в консоль и/или файл по настройкам `logback-spring.xml` в `resources`. Рекомендуется логировать значимые события (запросы, ошибки, важные шаги) с соответствующими уровнями (INFO, WARN, ERROR).

Для мониторинга добавляется **Spring Boot Actuator** (зависимость `spring-boot-starter-actuator`). Actuator предоставляет готовые HTTP-эндпоинты для мониторинга и управления приложением, например `/actuator/health`, `/actuator/metrics`, `/actuator/info` и др. Он «выводит» информацию о состоянии приложения, использовании памяти, метриках, наличии БД и т.д. Как пишет Baeldung, Actuator «вносит в приложение production-ready возможности, собирает метрики и упрощает мониторинг»[baeldung.com](https://www.baeldung.com/spring-boot-actuators#:~:text=In%20essence%2C%20Actuator%20brings%20production,features%20to%20our%20application). После добавления этой зависимости автоматически становятся доступны базовые метрики, а список и детали эндпоинтов конфигурируются через `management.endpoints.web.exposure`.

Таким образом, готовая структура проекта содержит чёткое разделение на слои, JWT/OAuth2-безопасность, миграции Flyway, тесты и средства развёртывания. Все ключевые компоненты снабжены комментариями и сконфигурированы по лучшим практикам Spring Boot.

**Источники:** Руководства по слоистой архитектуре и Spring Boot[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=2%EF%B8%8F%E2%83%A3%20Controller%20Layer%20,Requests%20%26%20Responses)[javaguides.net](https://www.javaguides.net/2025/03/spring-boot-architecture.html#:~:text=Spring%20Data%20JPA%20reduces%20the,etc), официальные гайды по безопасности и OAuth2[spring.io](https://spring.io/guides/tutorials/spring-boot-oauth2/#:~:text=This%20guide%20shows%20you%20how,0%20and%20Spring%20Boot)[baeldung.com](https://www.baeldung.com/spring-boot-keycloak#:~:text=We%E2%80%99ll%20use%20Keycloak%20as%20an,implementations%20with%20features%20like), документация Flyway[baeldung.com](https://www.baeldung.com/database-migrations-with-flyway#:~:text=Migrations%20can%20either%20be%20versioned,every%20time%20their%20checksum%20changes), тестирование с Testcontainers[baeldung.com](https://www.baeldung.com/spring-boot-testcontainers-integration-test#:~:text=In%20this%20tutorial%2C%20we%E2%80%99ll%20demonstrate,JPA%20and%20the%20PostgreSQL%20database), Docker-руководства Spring[spring.io](https://spring.io/guides/gs/spring-boot-docker/#:~:text=FROM%20openjdk%3A8,jar%22%2C%22%2Fapp.jar)[baeldung.com](https://www.baeldung.com/spring-boot-postgresql-docker#:~:text=Now%20let%E2%80%99s%20write%20our%20Docker,and%20save%20it%20in%20src%2Fmain%2Fdocker), а также Baeldung по логированию и Actuator[baeldung.com](https://www.baeldung.com/spring-boot-logging#:~:text=When%20using%20starters%2C%20Logback%20is,used%20for%20logging%20by%20default)[baeldung.com](https://www.baeldung.com/spring-boot-actuators#:~:text=In%20essence%2C%20Actuator%20brings%20production,features%20to%20our%20application).


