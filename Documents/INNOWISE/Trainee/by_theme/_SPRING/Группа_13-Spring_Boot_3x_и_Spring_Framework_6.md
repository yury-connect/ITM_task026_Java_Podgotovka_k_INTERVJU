# Бонус: `Spring Boot 3.x` и `Spring Framework 6`
(Новые темы)

**Spring Boot 3.x** и **AOT** — это темы, которые показывают, что разработчик следит за развитием экосистемы. Отвечаю максимально точно и с аргументацией.

---
### 120. Что изменилось в Spring Boot 3? (Java 17 baseline, Jakarta EE 9+ вместо javax)

**Ответ:** Spring Boot 3.x — это **мажорный релиз** с фундаментальными изменениями, прежде всего связанными с переходом на Java 17 и миграцией с `javax` на `jakarta`.

**Основные изменения:**

|Область|Spring Boot 2.x|Spring Boot 3.x|
|---|---|---|
|**Java версия**|Java 8+|**Java 17 минимум** (Java 19/20 поддерживаются)|
|**Jakarta EE**|`javax.*`|**`jakarta.*`** (все пакеты переименованы)|
|**Hibernate**|Hibernate 5|**Hibernate 6**|
|**Spring Framework**|5.x|**6.x**|
|**Tomcat**|Tomcat 9|**Tomcat 10**|
|**Jetty**|Jetty 9|**Jetty 11**|
|**Undertow**|Undertow 2|**Undertow 2 (совместим)**|

**Ключевые изменения подробно:**

**1. `Jakarta EE 9+` вместо `Java EE` (`javax` → `jakarta`):**
```java
// Spring Boot 2.x
import javax.servlet.http.HttpServletRequest;
import javax.persistence.Entity;
import javax.validation.Valid;

// Spring Boot 3.x
import jakarta.servlet.http.HttpServletRequest;
import jakarta.persistence.Entity;
import jakarta.validation.Valid;
```

Это **самое болезненное изменение** при миграции — нужно обновить все импорты во всем проекте.

**2. Новая версия Hibernate 6:**
- Изменения в синтаксисе JPQL (например, `java.time` типы работают лучше)    
- Новая стратегия именования таблиц    
- Удалены deprecated API    

**3. Миграция Spring Security:**
```java
// Spring Boot 2.x
.authorizeRequests().antMatchers("/public/**").permitAll()
// Spring Boot 3.x (новая lambda-стиль)
.authorizeHttpRequests(auth -> auth.requestMatchers("/public/**").permitAll())
```

**4. Другие важные изменения:**

|Изменение|Описание|
|---|---|
|**`spring.factories` устарел**|Теперь используется `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`|
|**`@ConstructorBinding`**|Больше не нужна для `@ConfigurationProperties` (автоопределяется)|
|**`RestTemplate`**|Все еще доступен, но `WebClient` рекомендуется для новых проектов|
|**Actuator endpoints**|Пути изменились, требуется явное включение через `management.endpoints.web.exposure.include`|
|**Log4j 2**|Обновлен до версии 2.19+|
|**Micrometer**|Обновлен до версии 1.10+|

**Что остается (*для обратной совместимости*):**
- `spring-boot-starter-parent` все еще работает    
- `application.properties` / `application.yml` — без изменений    
- `@SpringBootApplication` — без изменений    
- Большинство стартеров остались с теми же именами    

**Миграция с 2.x на 3.x — ключевые шаги:**
```bash
1. Обновить Java до 17
2. Заменить все javax.* на jakarta.*
3. Обновить Spring Boot версию в pom.xml/build.gradle
4. Обновить Spring Security конфигурацию
5. Проверить Hibernate запросы (JPQL)
6. Обновить тесты
```

---
### 121. Что такое AOT (Ahead-Of-Time) компиляция и GraalVM Native Image поддержка?

**Ответ:** **AOT (Ahead-Of-Time)** — это компиляция Java-приложения в **нативный исполняемый файл** до его запуска (в отличие от традиционной JIT-компиляции во время выполнения). **GraalVM Native Image** — это технология, позволяющая создавать такие нативные образы для Spring Boot приложений.

**Традиционный Java (JIT) vs AOT (Native Image):**

|Характеристика|Традиционная JVM (JIT)|GraalVM Native Image (AOT)|
|---|---|---|
|**Время запуска**|Медленное (секунды)|**Мгновенное** (миллисекунды)|
|**Потребление памяти**|Высокое|**Низкое** (в 5-10 раз меньше)|
|**Размер приложения**|JAR + JVM (сотни МБ)|**Один исполняемый файл** (десятки МБ)|
|**Пиковая производительность**|Высокая (после прогрева)|Средняя (нет JIT-оптимизаций)|
|**Рефлексия**|Поддерживается полностью|**Ограничена** (требует конфигурации)|
|**Динамическая загрузка классов**|Да|Нет|

**Как это работает в Spring Boot 3.x:**
```bash
# 1. Обычная сборка JAR
mvn clean package
java -jar target/myapp.jar          # запуск ~2-5 секунд
# 2. AOT сборка нативного образа
mvn -Pnative native compile
./target/myapp                       # запуск ~0.05-0.1 секунды
```

**Что делает Spring Boot AOT при сборке:**
1. **Анализирует** все бины и их зависимости на этапе компиляции    
2. **Генерирует** дополнительные классы-хинты для рефлексии, ресурсов, прокси    
3. **Предварительно инициализирует** статические поля    
4. **Создает** native image через GraalVM    

**Spring Boot AOT специфичные хуки:**
```java
@Configuration
public class MyConfig {
    
    @Bean
    @Reflective  // указывает, что класс нужен для рефлексии в native image
    public MyService myService() {
        return new MyService();
    }
}
// RuntimeHintsRegistrar для тонкой настройки
@Configuration
public class MyRuntimeHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection()
            .registerType(MyDynamicClass.class, MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.resources()
            .registerPattern("data/*.json");
    }
}
```

**Когда использовать Native Image:**

|Сценарий|Подходит|Не подходит|
|---|---|---|
|**Serverless/FaaS** (AWS Lambda)|✅ Идеально (холодный старт)|❌|
|**Микросервисы с быстрым стартом**|✅ Да|❌|
|**CLI утилиты**|✅ Да|❌|
|**Крупные монолиты**|❌ (мало выгоды)|✅|
|**Приложения с интенсивной рефлексией**|❌ (сложно конфигурировать)|✅|
|**Динамическая загрузка классов**|❌ (невозможно)|✅|

**Ограничения и сложности:**
- **Нет динамической загрузки классов** (Class.forName ограничен)    
- **Рефлексия требует явной регистрации** (Spring AOT помогает, но не всегда)    
- **Сериализация/десериализация JSON** (Jackson требует hints)    
- **Прокси CGLIB** (должны быть известны на этапе компиляции)    
- **Время сборки** (дольше, чем обычная)    

**Gradle конфигурация:**
```gradle
plugins {
    id 'org.springframework.boot' version '3.2.0'
    id 'io.spring.dependency-management' version '1.1.4'
    id 'org.graalvm.buildtools.native' version '0.9.28'
}
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    // ...
}
graalvmNative {
    binaries {
        main {
            imageName = 'myapp'
            buildArgs.add('--enable-url-protocols=http')
        }
    }
}
```

**Вывод:** AOT и Native Image — это будущее для **cloud-native** приложений (Serverless, Kubernetes), где важен быстрый старт и малое потребление памяти. Но для обычных серверных приложений традиционная JVM остается предпочтительнее из-за лучшей производительности после прогрева и отсутствия ограничений. Spring Boot 3.x делает эту технологию доступной, но не заменяет традиционный подход полностью.

---

[⬅️**Previous**](Группа_12-Странные_вопросы) ⬆️ 
