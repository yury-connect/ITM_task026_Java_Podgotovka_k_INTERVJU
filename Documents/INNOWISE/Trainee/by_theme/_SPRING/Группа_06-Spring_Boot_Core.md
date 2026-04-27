# Группа 6: Spring Boot Core 
*(Средне-высокая частота задавания этих вопросов)*

[⬅️**Previous**](Группа_05-Spring_MVC_и_REST)   //   [⬆️](SPRING-121_вопрос_на_middle)   //   [**Next**➡️](Группа_07-Spring_Data_&_БД)

Spring Boot Core — это то, что отличает Middle-разработчика от Junior. Отвечаю максимально точно и с аргументацией.

---

### 61. Что такое Spring Boot? В чем отличие от Spring Framework?

**Ответ:** Spring Boot — это **надстройка** над Spring Framework, предназначенная для быстрого создания production-ready приложений с минимальной конфигурацией.

**Ключевые отличия от Spring Framework:**

|Характеристика|Spring Framework|Spring Boot|
|---|---|---|
|**Конфигурация**|Требует ручной настройки (XML или Java Config)|Автоконфигурация по умолчанию|
|**Зависимости**|Нужно указывать версии всех библиотек|Стартеры управляют версиями (BOM)|
|**Веб-сервер**|Нужно отдельно устанавливать Tomcat/Jetty|Встроенный сервер (Tomcat по умолчанию)|
|**Проект**|Фреймворк для DI, AOP, MVC и т.д.|Инструмент для быстрого старта и production-фич|
|**Миграция**|Можно, но долго|Готовые миграционные скрипты|

**Что дает Spring Boot:**

- **Auto-configuration** — автоматически настраивает бины по наличию классов в classpath.
    
- **Starter-ы** — удобные зависимости, включающие все необходимое для определенной функциональности.
    
- **Embedded сервер** — приложение запускается как `java -jar`.
    
- **Actuator** — production-метрики, health checks, мониторинг.
    
- **Externalized configuration** — удобная работа с пропертями из разных источников.
    

**Простой ответ на собеседовании:** Spring Framework — это основа (IoC, DI, AOP, MVC). Spring Boot — это инструмент, который **упрощает** использование Spring Framework, добавляя автоконфигурацию, встроенный сервер и production-фичи.

---

### 62. Что такое auto-configuration? Как она работает? (`@EnableAutoConfiguration`, `spring.factories`)
[Подробности по данному вопросу...](@Configuration_vs_@AutoConfiguration)

**Ответ:** Auto-configuration — это механизм Spring Boot, который **автоматически создает и настраивает бины** на основе зависимостей в classpath, без необходимости писать конфигурацию вручную.

**Как работает (по шагам):**

1. **Наличие `@EnableAutoConfiguration`** (входит в `@SpringBootApplication`).
    
2. **Spring Boot сканирует файл `META-INF/spring.factories`** во всех JAR-файлах classpath. В этом файле перечислены классы автоконфигурации:
```text
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,\
...
```
    
3. **Каждый класс автоконфигурации** (например, `DataSourceAutoConfiguration`) содержит условия (`@Conditional`), определяющие, нужно ли создавать бины:
```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DataSource.class)
@ConditionalOnMissingBean(DataSource.class)
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }
}
```
    
4. **Условия (`@Conditional*`)** проверяются:
    
    - `@ConditionalOnClass` — есть ли нужный класс в classpath.
        
    - `@ConditionalOnMissingBean` — нет ли уже такого бина в контексте (чтобы пользователь мог переопределить).
        
    - `@ConditionalOnProperty` — задано ли определенное свойство.
        
    - `@ConditionalOnWebApplication` — является ли приложение веб-приложением.
        
5. **Если все условия выполнены** — Spring создает бины с настройками по умолчанию (часто из `application.properties` через `@ConfigurationProperties`).
    

**Важный нюанс:** Auto-configuration работает **по принципу "умолчаний, которые можно переопределить"**. Если вы создаете свой бин того же типа (например, свой `DataSource`), автоконфигурация отключается (`@ConditionalOnMissingBean`).

[Подробности по данному вопросу...](@Configuration_vs_@AutoConfiguration)

---

### 63. Что делает `@SpringBootApplication` (составная аннотация)?

**Ответ:** `@SpringBootApplication` — это **главная аннотация** Spring Boot, которая помечает класс как точку входа в приложение. Она объединяет в себе три аннотации.

**Что делает:**

- Указывает Spring Boot, что это конфигурационный класс.
    
- Включает автоконфигурацию.
    
- Включает компонент-сканирование в текущем пакете и подпакетах.
    

**Ключевая особенность:** Замена трех аннотаций одной, что упрощает код и уменьшает количество шаблонов.

---

### 64. Что такое `SpringBootApplication` и какие три аннотации в нее входят?

**Ответ:** `@SpringBootApplication` — это **составная (composite) аннотация**, которая включает три аннотации:

|Аннотация|Назначение|
|---|---|
|**`@SpringBootConfiguration`**|Указывает, что класс является конфигурационным (специализация `@Configuration` для Spring Boot).|
|**`@EnableAutoConfiguration`**|Включает механизм автоконфигурации Spring Boot.|
|**`@ComponentScan`**|Включает сканирование компонентов в пакете, где находится аннотированный класс, и во всех подпакетах.|

**Пример (как выглядит `@SpringBootApplication` внутри):**
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
public @interface SpringBootApplication {
    // ...
}
```

**Почему это удобно:** Не нужно писать три аннотации отдельно. Достаточно одной `@SpringBootApplication` на классе `main`.

**Важный нюанс:** Можно переопределить поведение через параметры `@SpringBootApplication`:

- `exclude` — отключить конкретные автоконфигурации.
    
- `scanBasePackages` — указать другие пакеты для сканирования.
    

---

### 65. Как отключить конкретную auto-configuration? (`@EnableAutoConfiguration(exclude=...)`)

**Ответ:** Есть несколько способов отключить конкретную автоконфигурацию:

**Способ 1: Через `@SpringBootApplication` (рекомендуемый):**
```java
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

**Способ 2: Через `@EnableAutoConfiguration`:**
```java
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@SpringBootConfiguration
@ComponentScan
public class MyApplication { ... }
```

**Способ 3: Через `application.properties` (для production-настроек):**
```properties
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
```

**Способ 4: Через `application.yml`:**
```yaml
spring:
  autoconfigure:
    exclude:
      - org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
      - org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration

```

**Когда нужно отключать:**

- Вы хотите полностью контролировать создание бина (например, своего `DataSource` с нестандартной логикой).
    
- Автоконфигурация создает бины, которые конфликтуют с вашими.
    
- Вы используете альтернативную библиотеку (например, вместо JPA — JDBC template вручную).
    
- Для ускорения старта приложения (меньше проверок условий).
    

**Важный нюанс:** Исключение автоконфигурации отключает **все** бины, которые она создает. Если нужно переопределить только один бин, лучше создать свой бин того же типа — `@ConditionalOnMissingBean` отключит автоматический.

---

### 66. Что такое `SpringApplicationBuilder`?

**Ответ:** `SpringApplicationBuilder` — это **fluent API-билдер** для программной настройки `SpringApplication` перед его запуском. Позволяет конфигурировать приложение без использования `application.properties`.

**Пример использования:**
```java
public static void main(String[] args) {
    new SpringApplicationBuilder(MyApplication.class)
        .properties("spring.config.name=myapp")
        .profiles("prod", "eu-west")
        .listeners(new MyApplicationListener())
        .headless(false)
        .bannerMode(Banner.Mode.OFF)
        .logStartupInfo(true)
        .run(args);
}
```

**Что можно настроить через `SpringApplicationBuilder`:**

|Метод|Назначение|
|---|---|
|`profiles(String... profiles)`|Установка активных профилей|
|`properties(String... properties)`|Добавление свойств (`key=value`)|
|`listeners(ApplicationListener... listeners)`|Добавление слушателей событий|
|`bannerMode(Banner.Mode mode)`|Управление баннером (OFF, CONSOLE, LOG)|
|`headless(boolean headless)`|Режим headless (для UI-приложений)|
|`logStartupInfo(boolean logStartupInfo)`|Логирование информации о старте|
|`registerShutdownHook(boolean register)`|Регистрация shutdown hook|
|`web(WebApplicationType webApplicationType)`|Установка типа веб-приложения (SERVLET, REACTIVE, NONE)|
|`parent(ApplicationContext parent)`|Установка родительского контекста|

**Когда использовать:**

- Программная конфигурация без `application.properties` (например, для тестов).
    
- Настройка нескольких приложений в одном JVM (иерархические контексты).
    
- Динамическое определение профилей или свойств на основе внешних условий.
    

**Разница от `SpringApplication.run()`:** `SpringApplicationBuilder` дает больше контроля и поддерживает цепочки вызовов, но для простых случаев достаточно статического `SpringApplication.run(MyApp.class, args)`.

---

### 67. Как работает встроенный сервер (Tomcat / Jetty / Undertow)?

**Ответ:** Spring Boot включает встроенный сервер, который запускается **внутри JVM** приложения, без необходимости устанавливать и настраивать отдельный контейнер сервлетов.

**Как работает под капотом:**

1. **Зависимость** — в `spring-boot-starter-web` входит `spring-boot-starter-tomcat`, который содержит `tomcat-embed-core`.
    
2. **Автоконфигурация** — `ServletWebServerFactoryAutoConfiguration` создает `TomcatServletWebServerFactory` (или Jetty/Undertow, если соответствующий стартер в classpath).   [Подробности по **Автоконфигурация**...](@Configuration_vs_@AutoConfiguration)
    
3. **При запуске** (`SpringApplication.run()`):
    
    - Spring Boot создает экземпляр `TomcatServletWebServerFactory`.
        
    - Фабрика создает и конфигурирует встроенный Tomcat:
        
        - Устанавливает порт (из `server.port`, по умолчанию 8080).
            
        - Настраивает контекст (`/`).
            
        - Регистрирует `DispatcherServlet`.
            
    - Tomcat стартует в **отдельном потоке** (неблокирующем основной поток приложения).
        
4. **Приложение остается запущенным**, потому что Tomcat создает не-daemon потоки.
    

**Ключевые моменты:**

- Встроенный сервер — это **полноценный** сервер, такой же, как standalone Tomcat.
    
- Можно настраивать через `application.properties`:
```properties
server.port=8081
server.address=127.0.0.1
server.servlet.context-path=/api
server.tomcat.max-threads=200
server.tomcat.max-connections=10000
```
    
- Можно получить доступ к серверу через `WebServer` или `TomcatServletWebServerFactory` для кастомной настройки (SSL, дополнительные коннекторы).
    

**Преимущества:** Простота разработки (не нужно устанавливать Tomcat), единый артефакт (один JAR), одинаковое окружение на всех этапах (dev → test → prod).

---

### 68. Как заменить Tomcat на Jetty или Undertow?

**Ответ:** Замена встроенного сервера делается через **исключение Tomcat и добавление другого стартера** в `pom.xml` (Maven) или `build.gradle` (Gradle).

**Maven — замена на Jetty:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

**Maven — замена на Undertow:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-undertow</artifactId>
</dependency>
```

**Сравнение серверов:**

|Сервер|Преимущества|Недостатки|
|---|---|---|
|**Tomcat**|Стандарт, большое сообщество, хорошая документация|Тяжелее Jetty|
|**Jetty**|Легкий, быстрый старт, подходит для embedded|Меньше фич, чем у Tomcat|
|**Undertow**|Очень высокая производительность, неблокирующий I/O|Меньше документации, сложнее настройка|

**Важно:** После замены сервер работает **прозрачно** — код контроллеров не меняется. Все настройки через `server.*` продолжают работать (но могут быть сервер-специфичные параметры).

---

### 69. Что такое `Spring Boot Starter`? Какие стартеры вы знаете?

**Ответ:** Spring Boot Starter — это **описатель зависимости** (bill of materials), который объединяет набор библиотек, необходимых для определенной функциональности, с **совместимыми версиями**.

**Что входит в стартер:**

- Основные библиотеки для функциональности.
    
- Транзитивные зависимости (другие стартеры или библиотеки).
    
- Автоконфигурации (через `spring.factories`).
    

**Пример стартера `spring-boot-starter-web`:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Тянет за собой: `spring-web`, `spring-webmvc`, `spring-boot-starter-tomcat`, `jackson-databind` и т.д.

**Популярные стартеры:**

|Стартер|Назначение|
|---|---|
|`spring-boot-starter-web`|Веб-приложения (REST, MVC, встроенный Tomcat)|
|`spring-boot-starter-data-jpa`|JPA + Hibernate + Spring Data|
|`spring-boot-starter-test`|Тестирование (JUnit, Mockito, AssertJ, Spring Test)|
|`spring-boot-starter-security`|Spring Security|
|`spring-boot-starter-actuator`|Production-метрики, health checks|
|`spring-boot-starter-amqp`|RabbitMQ|
|`spring-boot-starter-data-redis`|Redis + Lettuce или Jedis|
|`spring-boot-starter-thymeleaf`|Thymeleaf шаблонизатор|
|`spring-boot-starter-websocket`|WebSocket поддержка|
|`spring-boot-starter-mail`|Email отправка (JavaMail)|
|`spring-boot-starter-batch`|Spring Batch|
|`spring-boot-starter-cache`|Кэширование (Caffeine, Ehcache, Redis)|

**Преимущества стартеров:**

- **Упрощение зависимостей** — одна зависимость вместо 5-10.
    
- **Управление версиями** — все версии совместимы (через Spring Boot BOM).
    
- **Автоконфигурация** — стартеры часто включают автоконфигурацию.

**Можно ли создать свой стартер?** Да — через модуль с `spring.factories`, `@Configuration` и `@Conditional*`.

---

### 70. Что такое `@SpringBootTest`? Как он поднимает контекст?

**Ответ:** `@SpringBootTest` — это аннотация для **интеграционного тестирования** Spring Boot приложений, которая поднимает полный ApplicationContext.

**Как работает:**

1. **Поиск главного класса** — `@SpringBootTest` ищет `@SpringBootApplication` в текущем пакете и выше.
    
2. **Загрузка контекста** — создает `ApplicationContext` так же, как при реальном запуске:
    
    - Сканируются все компоненты.
        
    - Выполняется автоконфигурация.
        
    - Создаются все бины.
        
    - Поднимается встроенный сервер (если указан `webEnvironment`).
        
3. **Кэширование контекста** — Spring Test кэширует контекст между тестами (если конфигурация одинаковая), что ускоряет выполнение.
    

**Параметры `webEnvironment`:**

|Значение|Описание|
|---|---|
|`MOCK` (по умолчанию)|Мок-сервлетное окружение (без реального сервера), используется `MockMvc`|
|`RANDOM_PORT`|Реальный сервер на случайном порту|
|`DEFINED_PORT`|Реальный сервер на указанном порту (из `server.port`)|
|`NONE`|Без веб-окружения (не поднимает сервер)|

**Примеры:**
```java
@SpringBootTest // MOCK по умолчанию
class MyServiceTest { ... }
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MyRestControllerTest {
    @LocalServerPort
    private int port; // получим случайный порт
}
@SpringBootTest(classes = {MyConfig.class}, properties = {"spring.main.web-application-type=none"})
class MyUnitTest { ... }
```

**Важные нюансы:**

- `@SpringBootTest` **не** включает транзакции для тестов (в отличие от `@DataJpaTest`).
    
- Можно сузить контекст через `@SpringBootTest(classes = {...})` для ускорения.
    
- `@MockBean` позволяет подменить реальные бины на моки.
    

---

### 71. Как запустить Spring Boot приложение без веб-сервера? (`WebApplicationType.NONE`)

**Ответ:** Spring Boot позволяет запускать приложение **без веб-сервера** для консольных приложений, шедулеров, бATCH-процессов или просто утилит.

**Способы:**

**Способ 1: Через `SpringApplicationBuilder` (программно):**
```java
@SpringBootApplication
public class MyConsoleApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(MyConsoleApp.class)
            .web(WebApplicationType.NONE)
            .run(args);
    }
}
```

**Способ 2: Через `SpringApplication.setWebApplicationType()`:**
```java
SpringApplication app = new SpringApplication(MyConsoleApp.class);
app.setWebApplicationType(WebApplicationType.NONE);
app.run(args);
```

**Способ 3: Через `application.properties`:**
```properties
spring.main.web-application-type=none
```

**Способ 4: Исключить веб-стартер (Maven):**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <!-- вместо spring-boot-starter-web -->
</dependency>
```

**Что происходит:**

- Не поднимается встроенный Tomcat/Jetty/Undertow.
    
- `DispatcherServlet` не создается.
    
- `@RestController` и `@Controller` **не работают** (но их можно использовать, если не нужны HTTP-запросы).
    
- Все остальные функции Spring (DI, AOP, транзакции, шедулеры) работают нормально.

**Типичные сценарии:**

- Консольные утилиты (например, миграция данных).
    
- Приложения с `@Scheduled` (планировщики).
    
- Spring Batch приложения.
    
- Приложения, которые стартуют, делают работу и завершаются (через `CommandLineRunner` или `ApplicationRunner`).

---

### 72. Что такое Actuator? Какие эндпоинты есть?

**Ответ:** Spring Boot Actuator — это модуль, предоставляющий **production-ready** мониторинг и управление приложением через HTTP-эндпоинты или JMX.

**Как включить:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**Основные эндпоинты Actuator (через `/actuator`):**

|Эндпоинт|Назначение|Пример|
|---|---|---|
|**`/health`**|Статус здоровья приложения (UP/DOWN) + детали компонентов (БД, диск, Redis)|`{"status":"UP","components":{"db":{"status":"UP"}}}`|
|**`/info`**|Произвольная информация о приложении (версия, автор, git commit)|Настраивается через `info.*` в properties|
|**`/metrics`**|Метрики (память, CPU, количество запросов, тайминги)|`/actuator/metrics/jvm.memory.used`|
|**`/env`**|Все свойства окружения (переменные, properties, JVM аргументы)|**Внимание:** может содержать секреты|
|**`/beans`**|Все Spring бины в контексте|Диагностика конфигурации|
|**`/mappings`**|Все URL-маппинги (контроллеры)|Просмотр всех REST-эндпоинтов|
|**`/configprops`**|Значения `@ConfigurationProperties`|Просмотр настроек приложения|
|**`/loggers`**|Уровни логирования (можно менять на лету)|POST для изменения уровня|
|**`/threaddump`**|Дамп всех потоков|Анализ deadlock'ов|
|**`/heapdump`**|Дамп кучи (heap) для анализа памяти|Используется в VisualVM, MAT|
|**`/shutdown`**|**Остановка приложения** (выключен по умолчанию)|Требует явного включения|

**Настройка доступности эндпоинтов:**
```properties
# Включить все эндпоинты, кроме shutdown
management.endpoints.web.exposure.include=*
management.endpoints.web.exposure.exclude=shutdown
# Включить только конкретные
management.endpoints.web.exposure.include=health,info,metrics,prometheus
# Включить shutdown (опасно!)
management.endpoint.shutdown.enabled=true
```

**Безопасность:** В production эндпоинты Actuator должны быть защищены (например, через Spring Security) и часто вынесены на отдельный порт.

---

### 73. Как добавить свой эндпоинт в Actuator? (`@Endpoint`)

**Ответ:** Для добавления кастомного эндпоинта используется аннотация **`@Endpoint`** с методом, помеченным `@ReadOperation`, `@WriteOperation` или `@DeleteOperation`.

**Пример простого эндпоинта:**
```java
@Component
@Endpoint(id = "custom-info")
public class CustomEndpoint {
    
    @ReadOperation
    public Map<String, String> getInfo() {
        return Map.of("status", "OK", "version", "1.0");
    }
    
    @ReadOperation
    public String getById(@Selector String id) {
        return "Value for " + id;
    }
    
    @WriteOperation
    public void updateStatus(@Selector String status) {
        // Обновление статуса
    }
}
```

**Доступ к эндпоинту:** `/actuator/custom-info`

**Варианты аннотаций операций:**

|Аннотация|HTTP метод|Назначение|
|---|---|---|
|`@ReadOperation`|GET|Чтение данных|
|`@WriteOperation`|POST|Запись/обновление данных|
|`@DeleteOperation`|DELETE|Удаление данных|

**С `@Selector` для динамических путей:**
```java
@ReadOperation
public String getUser(@Selector String username) {
    return "User: " + username;
}
```

Доступ: `/actuator/custom-info/john` → вернет `User: john`

**С `@JmxEndpoint` для JMX:** Аналогично, но для JMX-доступа.

**Настройка:**
```properties
# Включить кастомный эндпоинт
management.endpoint.custom-info.enabled=true
management.endpoints.web.exposure.include=custom-info
```

**Важный нюанс:** Класс должен быть **бином Spring** (`@Component`, `@Service` или создан через `@Bean`). Эндпоинт автоматически регистрируется Actuator-ом.

**Когда использовать:** Для добавления production-метрик, диагностики, управления через HTTP без написания отдельного REST-контроллера. Actuator эндпоинты автоматически получают настройки безопасности и мониторинга, в отличие от обычных контроллеров.

---

[⬅️**Previous**](Группа_05-Spring_MVC_и_REST)          [⬆️](SPRING-121_вопрос_на_middle)          [**Next**➡️](Группа_07-Spring_Data_&_БД)
