# `@Configuration` vs `@AutoConfiguration` <br>в Spring Boot

---
## 🎯 Краткая суть

| Аннотация                | Назначение                                                                                                                              |
| ------------------------ | --------------------------------------------------------------------------------------------------------------------------------------- |
| **`@Configuration`**     | Ручное объявление бинов <br>(*вы явно пишете, что и как создавать*)                                                                     |
| **`@AutoConfiguration`** | Автоматическое объявление бинов <br>(*Spring Boot сам решает, создавать бин или нет, <br>в зависимости от наличия классов в classpath*) |

---
## 📦 `@Configuration` — ручное управление

### Что это?
Обычный класс конфигурации, кот. **вы пишете сами** в своём прилож. или стартере.
### Пример
```java
@Configuration
public class MyConfig {
    
    @Bean
    public MyService myService() {
        return new MyService();
    }
}
```
### Особенности
- ✅ Вы **полностью** контролируете, какие бины создаются    
- ✅ Работает **всегда** (*если класс попал в context scan*)    
- ❌ **Не зависит** от наличия **других классов** в classpath    
- ❌ **Не может быть "отключена" автоматически** 
	  (*только если вы уберёте её из сканирования*)
### Где использовать
- Ваше **собственное приложение**    
- Ваш **собственный стартер** (*если бин нужен **всегда***)    

---
## 🔄 `@AutoConfiguration` — автоматическое управление
*Доступна только в **Spring boot***

### Что это?
Специальная аннотация для **автоконфигураций в Spring Boot Starter'ах**. 
Это расширение `@Configuration` с <u>дополнительными возможностями</u>.
### Пример
```java
@AutoConfiguration
@ConditionalOnClass(OpenWeatherClient.class)  // Ключевое отличие !!!
@ConditionalOnProperty(name = "weather.enabled", havingValue = "true")
public class WeatherAutoConfiguration {
    
    @Bean
    public WeatherService weatherService() {
        return new WeatherService();
    }
}
```
### Особенности
- ✅ Срабатывает **только при определённых условиях** (`@Conditional...`)    
- ✅ Можно **отключить** целиком через `spring.autoconfigure.exclude`    
- ✅ Spring Boot загружает их **в специальном порядке** 
	  *(можно управлять очередностью)*    
- ❌ **Не сработает**, если не добавить файл в <br>`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

### Где использовать
- **ТОЛЬКО** в `Spring Boot Starter`'ах    
- Для библиотек, которые могут присутствовать или отсутствовать в проекте    

---
## 📋 Сравнительная таблица

| Характеристика                 | `@Configuration`                         | `@AutoConfiguration`                                                                           |
| ------------------------------ | ---------------------------------------- | ---------------------------------------------------------------------------------------------- |
| **Для чего**                   | Для вашего кода                          | Для сторонних стартеров                                                                        |
| **Условия создания бинов**     | Нет (или сам пишете `@Conditional`)      | Есть встроенные механизмы                                                                      |
| **Порядок загрузки**           | Как попали <br>в сканирование            | Можно управлять (`@AutoConfigureAfter`, <br>`@AutoConfigureBefore`, <br>`@AutoConfigureOrder`) |
| **Отключение**                 | Только удалить класс <br>из сканирования | Через <br>`spring.autoconfigure.exclude`                                                       |
| **Файл регистрации**           | Не нужен                                 | Нужен <br>(`AutoConfiguration.imports`)                                                        |
| **Использование в стартерах**  | Да <br>(*если бин всегда нужен*)         | Да <br>(рекомендованный способ)                                                                |
| **Использование в приложении** | Да <br>(*основной способ*)               | Нет (бессмысленно)                                                                             |

---
### Что такое `before`, `beforeName`, `after`, `afterName`?
Это **атрибуты**, которые можно указать прямо в `@AutoConfiguration`:
```java
@AutoConfiguration(
    before = DataSourceAutoConfiguration.class,  // до конкретной конфигурации
    beforeName = "org.some.AutoConfig",          // до конфигурации по имени
    after = JdbcTemplateAutoConfiguration.class, // после конкретной конфигурации
    afterName = "com.example.MyAutoConfig"       // после конфигурации по имени
)
public class MyWeatherAutoConfiguration {
    // ...
}
```
### Альтернатива (***рекомендуемый** способ*)
Вместо атрибутов в аннотации, можно использовать отдельные аннотации:
```java
@AutoConfiguration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class MyWeatherAutoConfiguration {
    // ...
}
```
### Важный момент
> Эти аннотации могут быть применены как к классам 
> с аннотацией `@Configuration`, так и к классам с `@AutoConfiguration`

Это означает, что даже в **обычном приложении** (*не стартере*) вы **можете** управлять порядком загрузки ваших конфигураций:
```java
@Configuration
@AutoConfigureAfter(SomeOtherConfig.class)  // ✅ Работает!
public class MyAppConfig {
    // Ваши бины
}
```

---
## 🏗️ Как работает **автоконфигурация** (*подробно*)

### Шаг 1. Пишем автоконфигурацию в стартере
```java
// В вашем стартере: WeatherAutoConfiguration.java
@AutoConfiguration
@ConditionalOnClass(
	name = "com.github.prominence.openweathermap.api.OpenWeatherClient")
@ConditionalOnProperty(
	prefix = "weather", 
	name = "enabled", 
	havingValue = "true", 
	matchIfMissing = true)
public class WeatherAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean  // Позволяет пользователю переопределить
    public WeatherService weatherService() {
        return new WeatherService();
    }
}
```

### Шаг 2. Регистрируем автоконфигурацию
	Создаём файл:
```text
src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```
	Содержимое файла:
```text
com.innowise.weather.WeatherAutoConfiguration
```

### Шаг 3. Spring Boot при запуске:
1. Сканирует все `AutoConfiguration.imports` в *classpath*    
2. Проверяет условия у каждой автоконфигурации    
3. Создаёт бины ТОЛЬКО из тех, чьи условия выполнены    

---
## 🔧 Популярные `@Conditional` аннотации

|Аннотация|Что делает|
|---|---|
|`@ConditionalOnClass`|Бин создаётся, если указанный класс есть в classpath|
|`@ConditionalOnMissingClass`|Бин создаётся, если указанного класса НЕТ в classpath|
|`@ConditionalOnBean`|Бин создаётся, если указанный бин уже существует|
|`@ConditionalOnMissingBean`|Бин создаётся, если указанного бина ещё нет (позволяет переопределять)|
|`@ConditionalOnProperty`|Бин создаётся в зависимости от значения в application.properties|
|`@ConditionalOnWebApplication`|Бин создаётся только для веб-приложений|
|`@ConditionalOnExpression`|Сложные условия на SpEL|

---
## 🎮 Порядок загрузки автоконфигураций
```java
@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)  // Самая первая
public class FirstAutoConfig { }

@AutoConfiguration
@AutoConfigureAfter(FirstAutoConfig.class)  // После FirstAutoConfig
@AutoConfigureBefore(ThirdAutoConfig.class) // До ThirdAutoConfig
public class SecondAutoConfig { }

@AutoConfiguration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)  // Самая последняя
public class ThirdAutoConfig { }
```

---
## 🚫 Как отключить автоконфигурацию

### В `application.properties`:
```properties
# Отключить конкретную автоконфигурацию
spring.autoconfigure.exclude=com.innowise.weather.WeatherAutoConfiguration

# Отключить несколько (через запятую)
spring.autoconfigure.exclude=com.innowise.weather.WeatherAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```

### В `application.yml`:
```yaml
spring:
  autoconfigure:
    exclude:
      - com.innowise.weather.WeatherAutoConfiguration
      - org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```

### Через аннотацию в `@SpringBootApplication`:
```java
@SpringBootApplication(exclude = WeatherAutoConfiguration.class)
public class MyApp {
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);
    }
}
```

---
## 💡 Итоговое правило

|Сценарий|Используйте|
|---|---|
|Вы пишете **своё приложение** и создаёте бины|`@Configuration`|
|Вы пишете **стартер** и хотите, чтобы бины создавались только при наличии библиотек|`@AutoConfiguration`|
|Вы пишете **стартер** с бинами, которые всегда нужны|`@Configuration` (проще) или `@AutoConfiguration` (более гибкий)|

---
## 📝 Резюме одной фразой

> **`@Configuration`** — вы говорите Spring "создай ЭТО всегда",  
> **`@AutoConfiguration`** — вы говорите Spring "создай ЭТО, ЕСЛИ нужны условия выполнены".

---
