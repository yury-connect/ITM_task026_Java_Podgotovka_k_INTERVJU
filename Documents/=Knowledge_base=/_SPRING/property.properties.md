## Способы чтения **`property.properties`** <br>в Spring (*по частоте применения*)

---
### 🥇 **1. `@Value` + `@PropertySource` — самый частый** / когда свойств мало (2-5 штук)
```java
@Configuration
@PropertySource("classpath:application.properties")
public class Config {
    
    @Value("${my.property}")
    private String myProperty;
}
```
**Особенности:**
- Для `@Value` в статических полях нужен `PropertySourcesPlaceholderConfigurer`
- Просто, быстро, точечно
- Подходит для небольшого количества свойств
**Пример:** `@Value("${host}")`  
**Когда:** Одиночные разрозненные свойства, которые не связаны друг с другом.

---
### 🥈 **2. `@ConfigurationProperties` — для групп свойств** / <br>когда свойств много и они связаны
```java
@ConfigurationProperties(prefix = "mail")
@Component
public class MailProperties {
    private String host;
    private int port;
    private String from;
    // геттеры/сеттеры
}

// application.properties:
// mail.host=smtp.gmail.com
// mail.port=587
```
**Особенности:**
- Автоматическое маппинг на объект
- Поддержка валидации (`@Validated`, `@NotNull`)
- Лучший способ для групп связанных свойств
**Пример:** все настройки для работы с почтой (host, port, username, password, timeout...)  
**Когда:**
- Свойства логически относятся к одному модулю/компоненту
- Нужна **валидация** (`@NotNull`, `@Min`, `@Max`)
- Хочется удобной структуры (вложенные объекты, списки, мапы)

---
### 🥉 **3. `Environment` — программный доступ** / <br>когда нужно программно решить, какое свойство взять
```java
@Autowired
private Environment env;

public void doSomething() {
    String host = env.getProperty("mail.host");
    int port = env.getProperty("mail.port", Integer.class);
}
```
**Особенности:**
- Гибкость — можно проверять наличие и типы
- Программный доступ, а не декларативный
- Не требует аннотаций в полях
**Пример:** логика типа "если host не задан, то берём значение по умолчанию и пробуем другое"  
**Когда:**
- Нужно проверить наличие свойства перед использованием
- Значение зависит от условий (if-else)
- Нужно получить свойство неизвестного заранее имени (динамически)

### 📝 Одной строкой

|Способ|Кратко|
|---|---|
|**`@Value`**|Просто и точечно — для пары свойств|
|**`@ConfigurationProperties`**|Структурно и с валидацией — для группы свойств|
|**`Environment`**|Гибко и программно — когда нужна логика вокруг свойств|

---
### 4. **`@PropertySources` — несколько файлов**
```java
@Configuration
@PropertySources({
    @PropertySource("classpath:db.properties"),
    @PropertySource("classpath:mail.properties")
})
public class AppConfig {
    // ...
}
```
**Особенности:**
- Объединяет несколько файлов
- Аналогично `@PropertySource`, но для множества файлов

---
### 5. **`YAML` (`@PropertySource` + `@Value`)**
```java
@Configuration
@PropertySource(value = "classpath:application.yml", 
                factory = YamlPropertySourceFactory.class)
```
**Особенности:**
- Работа с YAML (более читаемый формат)
- Требует кастомного `PropertySourceFactory`

---
### 6. **`@TestPropertySource` — для тестов**
```java
@RunWith(SpringRunner.class)
@TestPropertySource(properties = "my.property=testValue")
public class MyTest {
    // ...
}
```
**Особенности:**
- Только для тестов
- Можно задавать прямо в аннотации `properties = {...}

---
### 7. **`@PropertySource` с `ignoreResourceNotFound`**
```java
@PropertySource(value = "classpath:optional.properties", 
                ignoreResourceNotFound = true)
```
**Особенности:**
- Не падает, если файла нет
- Полезно для опциональных конфигураций

---
## 📊 **Резюме для собеседования**

|Способ|Когда использовать|
|---|---|
|`@Value`|2-3 свойства, простота|
|`@ConfigurationProperties`|Много связанных свойств, валидация|
|`Environment`|Программная логика, динамический доступ|
|`@TestPropertySource`|Только тесты|

---
## 💡 **Важно знать!**
```java
// Для @Value в статических полях ТОЛЬКО так:
@Configuration
public class Config {
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
```
**Почему `static`?** Чтобы бин создался до того, как начнут внедряться зависимости в другие бины. Spring обрабатывает такие бины в особом порядке (BeanFactoryPostProcessor).

---
