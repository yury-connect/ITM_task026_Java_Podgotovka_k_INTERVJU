# 🧪 JUnit 4 vs JUnit 5

- 🏛️ **Архитектура:** JUnit 4 — практически единый фреймворк. JUnit 5 разделён на **Platform** (запуск), **Jupiter** (новый API) и **Vintage** (запуск старых тестов JUnit 3/4).
- 🏷️ **Аннотации:** в основном изменились названия:
    - `@Before` → `@BeforeEach`
    - `@After` → `@AfterEach`
    - `@BeforeClass` → `@BeforeAll`
    - `@AfterClass` → `@AfterAll`
    - `@Ignore` → `@Disabled`
    - `@Test` в JUnit 5 — `org.junit.jupiter.api.Test`
- 🛠️ **Расширения:** в JUnit 4 использовались `@RunWith` и `Rule`, в JUnit 5 — единый механизм **Extensions** через `@ExtendWith`.
- ⚡ **Возможности:** JUnit 5 добавил более удобные **параметризованные тесты** (`@ParameterizedTest`), вложенные тесты (`@Nested`), понятные имена (`@DisplayName`) и `assertAll`.

### 🎯 Совсем кратко для собеса:
> **JUnit 5 — это более современная и расширяемая версия JUnit 4.  
> Основные изменения — новые аннотации `@BeforeEach/@AfterEach`, единый механизм Extensions вместо `Runner/Rule`, плюс удобные параметризованные и вложенные тесты.**

---
