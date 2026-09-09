# 🧪 **`JUnit 4`** vs **`JUnit 5
`**
#### **Вариант ответа № 1:**
### 🧪 Главные отличия JUnit 4 и JUnit 5
- **Модульность:** JUnit 4 — один монолитный фреймворк. JUnit 5 разделен на три части: 1️⃣**Platform** (запуск), 2️⃣**Jupiter** (новый API) и 3️⃣**Vintage** (поддержка старых тестов JUnit 3/4).
    
- **Понятные аннотации:**    
    - `@Before` / `@After` $\rightarrow$ `@BeforeEach` / `@AfterEach` (каждый тест)        
    - `@BeforeClass` / `@AfterClass` $\rightarrow$ `@BeforeAll` / `@AfterAll` (один раз на класс) 
    - `@Ignore` $\rightarrow$ `@Disabled` (пропустить)
    
- **Единый механизм расширений:** Мешанину из `@RunWith` и `@Rule` заменили на один гибкий аннотатор `@ExtendWith`.
    
- **Новые фичи из коробки:**    
    - Удобные параметризованные тесты (`@ParameterizedTest`).        
    - Группировка тестов во вложенные классы (`@Nested`).        
    - Человекочитаемые названия (`@DisplayName`).  !!!!!       
    - `assertAll()` — проверка сразу нескольких условий (*тест не падают на первой же ошибке*).        

> **Ответ для собеседования:**> 
> JUnit 5 стал модульным, ушел от монструозных раннеров/рулов к единому `@ExtendWith`, обновил названия аннотаций (`@BeforeEach`) и получил полноценную встроенную поддержку параметризованных и вложенных тестов.

#### **Вариант ответа № 2:**
- 🏛️ **Архитектура:** JUnit 4 — монолитная библиотека (`junit.jar`). JUnit 5 состоит из 3 независимых модулей: `JUnit Platform` (запуск тестов), `JUnit Jupiter` (новый API) и `JUnit Vintage` (обратная совместимость с JUnit 3/4).
    
- 🏷️ **Аннотации:**    
    - `@Test` — в JUnit 5 находится в пакете `org.junit.jupiter.api`.        
    - `@Before` / `@After` $\rightarrow$ заменены на `@BeforeEach` / `@AfterEach`.        
    - `@BeforeClass` / `@AfterClass` $\rightarrow$ заменены на `@BeforeAll` / `@AfterAll`.        
    - `@Ignore` $\rightarrow$ заменена на `@Disabled`.
    
- 🛠️ **Расширяемость:** Вместо громоздких `@RunWith` и `Rule` в JUnit 5 используется единая модель расширений **`@ExtendWith`** (например, `@ExtendWith(MockitoExtension.class)`).
    
- ⚡ **Новые фичи:** Встроенная поддержка параметризованных тестов (`@ParameterizedTest`), иерархических вложенных тестов (`@Nested`), кастомных имен (`@DisplayName`) и группировки утверждений (`assertAll`).

---
