## 🔍 Сравнение со стилями

| Стиль                | Пример               | Где используется                                              |
| -------------------- | -------------------- | ------------------------------------------------------------- |
| **kebab-case**       | `my-awesome-service` | URL, CSS-классы, HTML-атрибуты, имена веток Git, имена файлов |
| **snake_case**       | `my_awesome_service` | Python, Ruby, имена переменных в БД                           |
| **camelCase**        | `myAwesomeService`   | Java, JavaScript (переменные, методы)                         |
| **PascalCase**       | `MyAwesomeService`   | Java (классы), C#, TypeScript (классы, интерфейсы)            |
| **UPPER_SNAKE_CASE** | `MY_AWSOME_SERVICE`  | Константы, env-переменные                                     |

---
---
---
## 🎯 Кратко и с примерами

---
### 🥒 **kebab-case** `(все-слова-маленькие-через-дефис)`

|Где|Пример|
|---|---|
|Git-ветки|`feature/add-weather-api`|
|URL-адреса|`https://site.com/user-profile`|
|Имена файлов|`application-dev.yml`|
|CSS-классы|`<div class="weather-widget">`|
Дефис похож на **шампур**, а слова — на **кусочки мяса/овощей** на шампуре. 
Отсюда и название "кебаб". 🍢

---
### 🐍 **snake_case** `(все_слова_через_подчёркивание)`

|Где|Пример|
|---|---|
|Python (переменные)|`user_name = "Yury"`|
|Ruby on Rails|`user_profile_path`|
|Переменные в БД|`created_at`, `user_id`|
|env-переменные|`OPENWEATHER_API_KEY` (но это UPPER_SNAKE)|

---
### 🐫 **camelCase** `(первоеСлово маленькое)`

|Где|Пример|
|---|---|
|Java (переменные, методы)|`userName`, `getUserById()`|
|JavaScript (переменные)|`userName`, `fetchData()`|
|JSON-поля|`{"userName": "Yury"}`|

---
### 🐪 **PascalCase** `(КаждоеСлово СБольшой)`

|Где|Пример|
|---|---|
|Java (классы)|`class WeatherService`|
|C# (всё)|`public class UserController`|
|TypeScript (интерфейсы)|`interface IUserProfile`|

---
### 🔥 **UPPER_SNAKE_CASE** `(ВСЁ_БОЛЬШИМИ_С_ПОДЧЁРКИВАНИЕМ)`

|Где|Пример|
|---|---|
|Константы (Java)|`public static final int MAX_RETRIES = 3;`|
|env-переменные|`DATABASE_URL`, `API_KEY`|

---
## 📋 Шпаргалка одной строкой
```text
URL/css      → kebab-case  (my-file.css)
Java/JS/JSON → camelCase   (myVariable)
Java-классы  → PascalCase  (MyClass)
Python/БД    → snake_case  (my_variable)
Константы    → UPPER_SNAKE (MY_CONSTANT)
Git-ветки    → kebab-case  (feature/my-work)  ✅
```

---
---
---
## 📋 Полный справочник: что как называется в Java-мире

---

### 🗂️ **Пакеты (packages)** → `lowercase.only.dots`
```java
package com.innowise.weatherstarter.config;
```
**Правило:** только маленькие буквы, никаких подчёркиваний/дефисов

|✅ Пример|❌ Неправильно|
|---|---|
|`com.innowise.weather`|`com.Innowise.Weather`|
|`org.springframework.boot`|`org.spring-boot`|
|`io.github.yury`|`io.github.yury_123`|

---
### 📦 **Артефакты Maven/Gradle** → `kebab-case`
```xml
<artifactId>openweathermap-starter</artifactId>
<artifactId>spring-boot-starter-web</artifactId>
```

|✅ Пример|❌ Неправильно|
|---|---|
|`weather-service`|`weather_service`|
|`api-client`|`ApiClient`|

---
### 🏷️ **Git-ветки** → `kebab-case` или `/`



feature/add-weather-api          
refactor/weather-cleanup         
bugfix/null-pointer-fix           

|✅ Пример|❌ Неправильно|
|---|---|
|`feature/user-auth`|`feature/userAuth`|
|`refactor/date-2026-04-24`|`refactor/date_2026_04_24`|

---

### 🏷️ **Git-теги (версии)** → `vX.Y.Z` или стандарт семантического версионирования

bash

v1.0.0
v2.1.3-SNAPSHOT
v0.0.1

---

### 🧬 **Имена классов** → `PascalCase`

java

public class WeatherService {}
public class OpenWeatherClient {}
public class UserProfileController {}

|✅ Пример|❌ Неправильно|
|---|---|
|`HttpClient`|`httpClient` / `HTTPClient`|
|`UserServiceImpl`|`userService`|

---

### 🔧 **Имена методов** → `camelCase`

java

public String getWeatherByCity() {}
public void saveUserProfile() {}
private boolean isValidApiKey() {}

---

### 📝 **Имена переменных** → `camelCase`

java

String userName = "Yury";
int maxRetryCount = 3;
List<WeatherData> weatherHistory;

---

### 🔒 **Константы** → `UPPER_SNAKE_CASE`

java

public static final int MAX_RETRIES = 3;
public static final String DEFAULT_API_URL = "https://api.openweathermap.org";
private static final Logger LOGGER = LoggerFactory.getLogger(MyClass.class);

---

### 🎨 **Имена перечислений (enum)** → `PascalCase` для класса, `UPPER_SNAKE` для значений

java

public enum WeatherUnit {
    METRIC,
    IMPERIAL,
    STANDARD
}

---

### 🏗️ **Имена интерфейсов** → `PascalCase`

java

public interface WeatherClient {}
public interface UserRepository {}
// Опционально: префикс I (C# стиль) не принят в Java

---

### 📁 **Имена файлов (Java)** → `PascalCase.java`

text

WeatherService.java
OpenWeatherClientImpl.java
ApplicationConfig.java

---

### ⚙️ **Имена файлов конфигурации** → `kebab-case`

text

application.yml
application-dev.properties
logback-spring.xml
bootstrap.yml

---

### 🐳 **Docker-образы** → `lowercase:kebab-case`

text

openweathermap-starter:1.0.0
myapp/weather-service:latest

---

### 🔗 **URL-маппинги в Spring** → `kebab-case` (рекомендуется) или `snake_case`

java

@GetMapping("/user-profile")           // ✅ kebab-case
@GetMapping("/weather/by-city")        // ✅ kebab-case
@GetMapping("/user_profile")           // ⚠️ snake_case (работает, но редко)

---

### 📦 **Имена модулей (Java 9+)** → `kebab-case`

text

module openweathermap.starter {
    exports com.innowise.weather;
}

---

### 🗃️ **Имена БД и таблиц** → `snake_case` (традиция SQL)

sql

CREATE TABLE user_profile (
    user_id BIGINT,
    created_at TIMESTAMP
);

---

### 🌿 **Имена веток с типом изменений** → `тип/описание`

text

feature/    - новая функциональность
bugfix/     - исправление бага
refactor/   - рефакторинг
hotfix/     - срочный баг в проде
chore/      - обслуживание (зависимости, конфиги)
docs/       - документация
test/       - тесты
perf/       - производительность

---

## 📊 Сводная таблица (шпаргалка)

|Что именуем|Стиль|Пример|
|---|---|---|
|Пакеты|`lowercase.dotted`|`com.innowise.weather`|
|Артефакты (Maven)|`kebab-case`|`openweathermap-starter`|
|Git ветки|`kebab-case` или `feature/name`|`feature/add-weather-api`|
|Git теги|`vX.Y.Z`|`v1.0.0`|
|Классы|`PascalCase`|`WeatherService`|
|Интерфейсы|`PascalCase`|`WeatherClient`|
|Методы|`camelCase`|`getWeather()`|
|Переменные|`camelCase`|`userName`|
|Константы|`UPPER_SNAKE_CASE`|`MAX_RETRIES`|
|Enum значения|`UPPER_SNAKE_CASE`|`METRIC`|
|Java файлы|`PascalCase.java`|`WeatherService.java`|
|Файлы конфигов|`kebab-case.yml`|`application-dev.yml`|
|URL-маппинги (рекомендуется)|`kebab-case`|`/user-profile`|
|Docker-образы|`kebab-case`|`weather-service:latest`|
|Модули (Java 9+)|`kebab-case`|`openweathermap.starter`|
|Таблицы в БД|`snake_case`|`user_profile`|

---

## 💡 Золотое правило

> **Python/БД** → `snake_case` 🐍  
> **Java/JS** → `camelCase` / `PascalCase` 🐫  
> **URL/CSS/Git/конфиги** → `kebab-case` 🥒  
> **Константы** → `UPPER_SNAKE_CASE` 🔥