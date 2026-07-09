# Загрузка properties в Java и Spring

## Java (стандарт)
- **Properties + FileInputStream** – ручная загрузка из файловой системы
- **Properties + ClassLoader** – загрузка из classpath (ресурсов JAR)
## Spring
- **@PropertySource + @Value** – декларативная инъекция отдельных свойств
- **Environment** – программное чтение свойств из всех источников
- **@ConfigurationProperties** – привязка группы свойств к POJO (type-safe)
## Spring Boot
- **Автоматическая загрузка** – `application.properties/yml` загружается по умолчанию из предопределенных папок
- **@ConfigurationProperties** – основной способ для структурированной конфигурации
- **Профили** – загрузка файлов `application-{profile}.properties` для разных сред
- **External конфигурация** – переопределение через аргументы командной строки, переменные окружения, внешние файлы (`--spring.config.location`)
- **Spring Cloud Config** – централизованное хранилище конфигураций для микросервисов

> **Самая важная иерархия приоритетов в Spring Boot** (от высшего к низшему):
> 1. Аргументы командной строки
> 2. Переменные окружения
> 3. `application-{profile}.properties`
> 4. `application.properties



---
## 1. **Standard Java**

### 1.1 `Properties` + `FileInputStream`
```java
Properties props = new Properties();
try (FileInputStream fis = new FileInputStream("config.properties")) {
    props.load(fis);
}
String value = props.getProperty("key");
```
**Характеристика**: Базовый способ, работает везде  
**Когда использовать**: В простых Java приложениях без фреймворков  
**Плюсы**: Простота, нет зависимостей  
**Минусы**: Жестко закодированный путь к файлу, нет инъекции зависимостей

### 1.2 `Properties` + `ClassLoader`
```java
Properties props = new Properties();
try (InputStream is = getClass().getClassLoader()
        .getResourceAsStream("application.properties")) {
    props.load(is);
}
```
**Характеристика**: Загрузка из classpath  
**Когда использовать**: Когда файл в ресурсах проекта  
**Плюсы**: Независимость от абсолютных путей, работает в JAR  
**Минусы**: Только для файлов в classpath

---
## 2. Spring Framework

### 2.1 @PropertySource + @Value
```java
@Configuration
@PropertySource("classpath:app.properties")
public class AppConfig {
    
    @Value("${database.url}")
    private String dbUrl;
}
```
**Характеристика**: Декларативная загрузка свойств  
**Когда использовать**: Для конфигурации бинов, когда нужно инжектить отдельные свойства  
**Плюсы**: Простота, интеграция со Spring контекстом  
**Минусы**: Только для файлов в classpath, нет профилей

### 2.2 PropertySourcesPlaceholderConfigurer
```java
@Bean
public static PropertySourcesPlaceholderConfigurer properties() {
    PropertySourcesPlaceholderConfigurer configurer = 
        new PropertySourcesPlaceholderConfigurer();
    configurer.setLocation(new ClassPathResource("app.properties"));
    return configurer;
}
```
**Характеристика**: Programmatic конфигурация properties  
**Когда использовать**: Когда нужен полный контроль над загрузкой properties  
**Плюсы**: Гибкость, можно задавать несколько источников  
**Минусы**: Более многословный подход

### 2.3 Environment API
```java
@Autowired
private Environment env;
public void someMethod() {
    String value = env.getProperty("some.key");
}
```
**Характеристика**: Программный доступ к свойствам  
**Когда использовать**: Когда нужно динамически читать свойства в runtime  
**Плюсы**: Доступ ко всем источникам свойств, поддержка профилей  
**Минусы**: Нет type-safe доступа

### 2.4 @ConfigurationProperties
```java
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private int version;
    // getters/setters
}
```
**Характеристика**: Type-safe binding свойств к POJO  
**Когда использовать**: Для группировки связанных свойств  
**Плюсы**: Type-safe, валидация, IDE поддержка  
**Минусы**: Требует создания классов-конфигураций

---
## 3. Spring Boot

### 3.1 Автоматическая загрузка

Spring Boot автоматически загружает:
- `application.properties` / `application.yml` из:
    - classpath root
    - classpath:/config/
    - current directory
    - current directory/config/
    - и другие...

**Характеристика**: Convention over configuration  
**Когда использовать**: Всегда в Spring Boot приложениях  
**Плюсы**: Автоматика, multiple sources, профили  
**Минусы**: Магия - нужно понимать порядок загрузки

### 3.2 @ConfigurationProperties с валидацией
```java
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {
    @NotBlank
    private String name;
    
    @Min(1)
    private int version;
}
```
**Характеристика**: Type-safe конфигурация с валидацией  
**Когда использовать**: Для сложных конфигураций с валидацией  
**Плюсы**: Валидация, type-safe, хорошая документация  
**Минусы**: Overkill для простых случаев

### 3.3 Профили
```properties
# application-dev.properties
server.port=8080
# application-prod.properties  
server.port=80
```

```bash
java -jar app.jar --spring.profiles.active=prod
```

**Характеристика**: Environment-specific конфигурация  
**Когда использовать**: Для разных сред (dev, test, prod)  
**Плюсы**: Изоляция конфигов по средам  
**Минусы**: Усложняет управление конфигурациями

### 3.4 External Configuration
```bash
java -jar app.jar --spring.config.location=file:/external/config/
java -jar app.jar --app.name="MyApp"
```
**Характеристика**: Внешняя конфигурация  
**Когда использовать**: В production, Docker, cloud environments  
**Плюсы**: Без пересборки, security, разные среды  
**Минусы**: Нужно управлять внешними файлами

### 3.5 Cloud Config (Spring Cloud)
```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```
**Характеристика**: Centralized configuration management  
**Когда использовать**: В микросервисных архитектурах  
**Плюсы**: Централизованное управление, версионирование  
**Минусы**: Сложность настройки, дополнительная точка отказа

---
## Рекомендации по выбору

### Для простых Java приложений:
- **Properties + ClassLoader** - достаточно для большинства случаев

### Для Spring приложений:
- **@PropertySource + @Value** - для простой конфигурации
- **@ConfigurationProperties** - для сложных, type-safe конфигураций

### Для Spring Boot приложений:
- **Автоматическая загрузка** + **@ConfigurationProperties** - стандартный подход
- **Профили** - для environment-specific конфигурации
- **External config** - для production deployment

### Для enterprise/microservices:
- **Spring Cloud Config** - для распределенных систем

---
