# Группа 5: Spring MVC и REST 
*(Эти вопросы задают с очень высокой частотой)*

[⬅️**Previous**](Группа_04-Управление_транзакциями.md)   //   [⬆️](SPRING-121_вопрос_на_middle.md)   //   [**Next**➡️](Группа_06-Spring_Boot_Core.md)

 Spring MVC — это то, с чем Middle-разработчик работает ежедневно. Отвечаю четко, с акцентами на важных деталях.

---
### 47. Что такое DispatcherServlet? <br>Как работает? (Front Controller)

**Ответ:** `DispatcherServlet` — это **центральный сервлет-диспетчер** в Spring MVC, реализующий паттерн **Front Controller**. Он принимает все входящие HTTP-запросы 
(в зависимости от маппинга, часто `/`, но может быть и другой *pattern*, например `/api/*`) и направляет их в соответствующие обработчики (*контроллеры*).

#### **Как работает (основные шаги):**
1. Запрос поступает в `DispatcherServlet` от контейнера сервлетов (`Tomcat`/`Jetty`).
    
2. `DispatcherServlet` запрашивает у `HandlerMapping` подходящий обработчик (контроллер + метод).
    
3. Получает `HandlerExecutionChain` (`обработчик` + `список перехватчиков`).
    
4. Вызывает `HandlerAdapter`, который выполняет метод контроллера.
    
5. Получает результат (модель + имя view или прямой ответ).
    
6. Если это view — передает запрос `ViewResolver` для рендеринга.
    
7. Отправляет HTTP-ответ клиенту.    

**Ключевая роль:** Единая точка входа, централизующая обработку запросов, 
что упрощает конфигурацию и добавление сквозной логики 
(интерсепторы, обработка исключений).

---
### 48. Жизненный цикл HTTP запроса в Spring MVC <br>(*от сервлета до контроллера*)

**Ответ:** Жизненный цикл включает следующие этапы:

| Этап | Компонент                                    | Действие                                                                                                     |
| ---- | -------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| 1    | Контейнер сервлетов (*Tomcat*)               | Принимает TCP-соединение, <br>парсит HTTP-запрос, <br>создает `HttpServletRequest`/<br>`HttpServletResponse` |
| 2    | `DispatcherServlet`                          | Получает запрос от контейнера (метод `service()` → `doGet()`/`doPost()`)                                     |
| 3    | `HandlerMapping`                             | Ищет обработчик (*контроллер и метод*) <br>по URL, HTTP-методу, параметрам, заголовкам                       |
| 4    | `HandlerInterceptor` <br>(*preHandle*)       | Выполняются все перехватчики <br>**до** вызова контроллера                                                   |
| 5    | `HandlerAdapter`                             | Адаптирует вызов контроллера: преобразует параметры, валидацию, вызывает метод                               |
| 6    | **Метод контроллера**                        | Выполняется бизнес-логика, <br>возвращает `ModelAndView`, `ResponseEntity` <br>или другое значение           |
| 7    | `HandlerInterceptor` <br>(*postHandle*)      | Выполняются **после** контроллера, <br>но **до** рендеринга view                                             |
| 8    | `ViewResolver`                               | Определяет, какой view рендерить <br>(если возвращено имя view)                                              |
| 9    | Рендеринг                                    | *JSP*/ *Thymeleaf* и т.д. формируют *HTML*                                                                   |
| 10   | `HandlerInterceptor` <br>(*afterCompletion*) | Выполняются **после** рендеринга <br>(всегда, даже при исключениях)                                          |
| 11   | `DispatcherServlet`                          | Отправляет HTTP-ответ клиенту                                                                                |

**Важно:** Для REST-контроллеров (`@RestController` / `@ResponseBody`) этапы рендеринга view (8–9) пропускаются — `HandlerAdapter` через `HttpMessageConverter` сразу записывает результат (JSON/XML и т.д.) в `HttpServletResponse`.

---
### 49. Что такое `@RestController`? <br>Чем отличается от `@Controller` + `@ResponseBody`?

**Ответ:** `@RestController` — это **специализированная аннотация** Spring MVC, предназначенная для RESTful веб-сервисов.

**Состав аннотации:**
```java
@Controller
@ResponseBody
public @interface RestController {
    String value() default "";
}
```

**Разница:**

| Характеристика                       | `@Controller`                              | `@RestController`                                   |
| ------------------------------------ | ------------------------------------------ | --------------------------------------------------- |
| Назначение                           | Web-приложения <br>с HTML-страницами       | REST API (JSON/XML)                                 |
| Возвращаемое значение                | Имя view <br>(JSP, Thymeleaf)              | Данные <br>(POJO, коллекции, строки)                |
| `@ResponseBody`                      | Нужно ставить <br>на каждый метод отдельно | Не нужно — все методы подразумевают `@ResponseBody` |
| `@ResponseBody` <br>на уровне класса | Можно добавить, <br>но многословно         | Уже включена                                        |

**Пример:**
```java
// Эквивалентны:
@RestController
public class UserController {

    @GetMapping("/user")
    public User getUser() { return new User(); }
}

// И:

@Controller
@ResponseBody
public class UserController {

    @GetMapping("/user")
    public User getUser() { return new User(); }
}
```

**Вывод:** `@RestController` — это удобный синтаксический сахар, 
избавляющий от необходимости повторять `@ResponseBody` на каждом методе.

---
### 50. Аннотации маппинга: <br>`@RequestMapping`, `@GetMapping`, `@PostMapping` и т.д.

**Ответ:** Это аннотации, связывающие HTTP-запросы с методами контроллеров.

**Иерархия:**
- `@RequestMapping` — базовая, может использоваться на уровне класса и метода.
    
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@PatchMapping` — специализированные версии для конкретных HTTP-методов (доступны с Spring 4.3). 

**Параметры `@RequestMapping` (важные):**

| Параметр         | Описание                          | Пример                                        |
| ---------------- | --------------------------------- | --------------------------------------------- |
| `value` / `path` | URL-шаблон                        | `@RequestMapping("/users")`                   |
| `method`         | HTTP-метод                        | `method = RequestMethod.POST`                 |
| `params`         | Условия <br>на параметрах запроса | `params = "action=create"`                    |
| `headers`        | Условия на заголовках             | `headers = "Content-Type=application/json"`   |
| `consumes`       | Тип <br>содержимого запроса       | `consumes = MediaType.APPLICATION_JSON_VALUE` |
| `produces`       | Тип <br>содержимого ответа        | `produces = MediaType.APPLICATION_JSON_VALUE` |

**Примеры:**
```java
@GetMapping("/users/{id}")                    // GET
@PostMapping("/users")                        // POST
@RequestMapping(value = "/user", method = RequestMethod.DELETE) // старый стиль
@PostMapping(value = "/user", consumes = "application/json", produces = "application/json")
```

**Важный нюанс:** `@RequestMapping` на уровне класса 
	задает **базовый путь** для всех методов:
```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/{id}")  // реальный путь: /api/v1/users/{id}
    public User get(@PathVariable Long id) { ... }
}
```

С *Spring 4.3+* рекомендуется использовать специализированные аннотации 
(`@GetMapping`, `@PostMapping` и т.д.) вместо `@RequestMapping(method = ...)`.

---
### 51. Что такое `@PathVariable`, `@RequestParam`, `@RequestHeader`, `@CookieValue`?

**Ответ:** Это аннотации для извлечения данных из HTTP-запроса 
	в параметры метода контроллера.

|Аннотация|Источник данных|Пример URL|Пример кода|
|---|---|---|---|
|**`@PathVariable`**|Переменная в URL-шаблоне|`/users/123`|`@PathVariable Long id`|
|**`@RequestParam`**|Параметр query string или form data|`/users?id=123`|`@RequestParam Long id`|
|**`@RequestHeader`**|HTTP-заголовок|(внутри запроса)|`@RequestHeader("User-Agent") String ua`|
|**`@CookieValue`**|Cookie|(внутри запроса)|`@CookieValue("JSESSIONID") String sessionId`|

**Детали и важные параметры:**

#### **`@PathVariable`:**
```java
@GetMapping("/users/{userId}/orders/{orderId}")
public String get(@PathVariable Long userId, @PathVariable Long orderId)
```
- Можно указать имя, если параметр метода отличается: 
	  `@PathVariable("user_id") Long id`.    
- С версии 4.3+ можно не указывать имя, если совпадает с именем переменной.

#### **`@RequestParam`:**
```java
@GetMapping("/users")
public String get(@RequestParam(defaultValue = "1") int page,
                  @RequestParam(required = false) String name)
```
- `required = true` (по умолчанию) — исключение, если параметр отсутствует.    
- `defaultValue` — значение по умолчанию (неявно делает `required=false`).    
- Можно получить `Map<String, String>` всех параметров: 
	  `@RequestParam Map<String, String> allParams`.    

#### **`@RequestHeader`:**
```java
public String get(@RequestHeader("Accept-Language") String lang)
```
- Можно получить все заголовки: 
	  `@RequestHeader MultiValueMap<String, String> headers`.

#### **`@CookieValue`:**
```java
public String get(@CookieValue
	(value = "JSESSIONID", required = false) String sessionId)
```

**Важный нюанс:** Все эти аннотации могут быть использованы вместе в одном методе.

---
### 52. Как принять JSON в методе контроллера? (`@RequestBody`)

**Ответ:** Для приема JSON в теле HTTP-запроса используется аннотация **`@RequestBody`**.

#### **Пример:**
```java
@PostMapping("/users")
public User createUser(@RequestBody User user) {

    // user автоматически десериализован из JSON
    return userService.save(user);
}
```

#### **Как работает под капотом:**
1. `DispatcherServlet` определяет, что параметр метода помечен `@RequestBody`.
    
2. Вызывает `HandlerMethodArgumentResolver` 
	   для `@RequestBody` (реализация `RequestResponseBodyMethodProcessor`).
    
3. Этот резолвер определяет тип содержимого (`Content-Type: application/json`).
    
4. Выбирает подходящий `HttpMessageConverter` из зарегистрированных:    
    - `MappingJackson2HttpMessageConverter` (Jackson 2) — стандартный для JSON.
        
    - `GsonHttpMessageConverter`, `JsonbHttpMessageConverter` и другие.
    
5. Конвертер читает `HttpServletRequest.getInputStream()` 
	   и десериализует JSON в Java-объект.
    
6. Выполняется валидация, если есть `@Valid` или `@Validated`.    

#### **Важные нюансы:**
- Можно принять `List<T>`: `@RequestBody List<User> users`.
    
- Можно принять `Map<String, Object>`.
    
- Требуется `Content-Type: application/json` в запросе 
	  (иначе 415 Unsupported Media Type).
    
- С Jackson можно кастомизировать десериализацию 
	  через аннотации (`@JsonIgnore`, `@JsonProperty`, `@JsonFormat`).    

---
### 53. Как вернуть JSON? <br>(Автоматически через `HttpMessageConverter` и `Jackson`)

**Ответ:** Spring MVC автоматически конвертирует возвращаемое значение метода 
в JSON, когда:
- Метод помечен `@ResponseBody` или класс — `@RestController`.    
- Клиент указал `Accept: application/json` (или `*/*`).    

#### **Как работает:**
1. `DispatcherServlet` определяет, что метод 
	   должен вернуть тело ответа (`@ResponseBody`).
    
2. Вызывает `HandlerMethodReturnValueHandler` для `@ResponseBody` (`RequestResponseBodyMethodProcessor`).
    
3. Определяет тип содержимого ответа:    
    - Из `produces` аннотации (`@GetMapping(produces = "application/json")`).        
    - Из заголовка `Accept` клиента (Content negotiation).
    
4. Выбирает подходящий `HttpMessageConverter`:    
    - `MappingJackson2HttpMessageConverter` для JSON.        
    - `Jaxb2RootElementHttpMessageConverter` для XML.
    
5. Конвертер записывает Java-объект 
   в `HttpServletResponse.getOutputStream()`  как JSON.    

#### **Пример:**
```java
@RestController
public class UserController {
    @GetMapping("/user")
    public User getUser() {
        return new User(1L, "John"); // → {"id":1,"name":"John"}
    }
}
```

#### **Jackson автоматически:**
- Сериализует публичные поля или через геттеры.    
- По умолчанию сериализует null 
	  (можно отключить через @JsonInclude(Include.NON_NULL)).    
- Работает с `LocalDate`, `Instant` через модуль `JavaTimeModule`.    

**Ручное управление:** Можно вернуть `ResponseEntity<T>` 
		для полного контроля над статусом и заголовками.

---
### 54. Что такое `ResponseEntity`? Когда использовать?

**Ответ:** `ResponseEntity` — это обертка над HTTP-ответом, позволяющая контролировать **статус-код**, **заголовки** и **тело** ответа.

**Пример:**
```java
@GetMapping("/user/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    if (user == null) {
        return ResponseEntity.notFound().build(); // 404
    }
    return ResponseEntity.ok(user); // 200 + тело
}

@PostMapping("/users")
public ResponseEntity<User> create(@RequestBody User user) {
    User saved = userService.save(user);
    URI location = URI.create("/users/" + saved.getId());
    return ResponseEntity.created(location).body(saved); // 201 + Location header
}
```

**Когда использовать `ResponseEntity` вместо `@ResponseBody`:**

| Сценарий                                                  | `@ResponseBody`            | `ResponseEntity` |
| --------------------------------------------------------- | -------------------------- | ---------------- |
| Успешный ответ с данными, <br>статус 200                  | ✅ (по умолчанию)           | ✅                |
| Изменение статус-кода <br>(201, 204, 404, 409)            | ❌ (нужен `ResponseEntity`) | ✅                |
| Добавление заголовков <br>(Location, ETag, Cache-Control) | ❌                          | ✅                |
| Возврат разных статусов <br>в одном методе                | ❌                          | ✅                |
| Ответ без тела (204 No Content)                           | ❌                          | ✅ (`build()`)    |

**Статические фабричные методы `ResponseEntity`:**
```java
ResponseEntity.ok(body)                 // 200
ResponseEntity.status(HttpStatus.CREATED).body(body)  // 201
ResponseEntity.noContent().build()      // 204
ResponseEntity.badRequest().build()     // 400
ResponseEntity.notFound().build()       // 404
ResponseEntity.internalServerError().body(error) // 500
```

**Вывод:** Если ответ всегда одинаковый (например, всегда 200 с JSON) — достаточно `@ResponseBody`. Если статус или заголовки зависят от результата — используйте `ResponseEntity`.

---
### 55. Что такое `@ModelAttribute`? Где применяется?

**Ответ:** `@ModelAttribute` — аннотация Spring MVC для связывания параметров запроса с **полями объекта** (data binding). Она работает на уровне методов класса и на параметрах методов.

#### **Два основных применения:**

**1. На параметре метода — привязка параметров запроса к полям объекта:**
```java
@PostMapping("/register")
public String register(@ModelAttribute User user) {
    // Поля user заполняются из: /register?name=John&age=25
    return "success";
}

```
Аналог: Spring берет все `@RequestParam` и устанавливает их в объект.

**2. На методе класса — добавление атрибута во все модели контроллера:**
```java
@Controller
public class MyController {

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        return securityContext.getUser(); // добавляется во все модели
    }
    
    @GetMapping("/profile")
    public String profile(Model model) {
        // model уже содержит "currentUser"
        return "profile";
    }
}
```

#### **Где применяется:**
- **HTML-формы (Thymeleaf, JSP)** — для отображения и отправки данных формы.
    
- **Фильтры/интерсепторы** — для предзаполнения общих атрибутов 
	  (текущий пользователь, настройки).
    
- **REST API** — реже, но можно использовать вместо `@RequestBody` для form-urlencoded.    

#### **Важные нюансы:**
- Поддерживает `BindingResult` для валидации: `@ModelAttribute User user, BindingResult result`.
    
- `@ModelAttribute` на методе выполняется **перед каждым** запросом в контроллере.
    
- Можно использовать `@SessionAttributes` для сохранения `@ModelAttribute` между запросами.    

---
### 56. Как обрабатывать исключения в REST контроллерах? <br>(`@ExceptionHandler`, `@ControllerAdvice`)

**Ответ:** В Spring MVC есть два основных способа централизованной 
обработки исключений в REST:

**Способ 1: `@ExceptionHandler` внутри контроллера** (локальный):
```java
@RestController
public class UserController {

    @GetMapping("/user/{id}")
    public User get(@PathVariable Long id) { ... }
    
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }
}
```

**Способ 2: `@ControllerAdvice`** (глобальный, **рекомендуемый для REST API**):
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(
	    MethodArgumentNotValidException e) {
        return e
	        .getBindingResult()
	        .getFieldErrors()
	        .stream()
            .collect(Collectors
	            .toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneric(Exception e) {
        log.error("Unexpected error", e);
        return new ErrorResponse("Internal server error");
    }
}
```

**Приоритет обработки:**
1. `@ExceptionHandler` в самом контроллере (самый высокий приоритет).    
2. `@ControllerAdvice` с указанием `assignableTypes` или аннотаций.    
3. `@ControllerAdvice` без ограничений.    

**Важные моменты:**
- Методы `@ExceptionHandler` могут принимать 
	  `HttpServletRequest`, `HttpServletResponse`, `WebRequest`.    
- Можно возвращать `ResponseEntity` для полного контроля.    
- `@ControllerAdvice` поддерживает ограничения: 
	  `@ControllerAdvice(assignableTypes = UserController.class)`.    

---
### 57. В чем разница между <br>`@ControllerAdvice` и `@RestControllerAdvice`?

**Ответ:** `@RestControllerAdvice` — это специализированная версия 
`@ControllerAdvice` для REST API.

**Состав `@RestControllerAdvice`:**
```java
@ControllerAdvice
@ResponseBody
public @interface RestControllerAdvice {
    // ... атрибуты
}
```

**Разница:**

|Характеристика|`@ControllerAdvice`|`@RestControllerAdvice`|
|---|---|---|
|Назначение|Web-приложения (HTML + JSON)|Чистые REST API|
|`@ResponseBody` по умолчанию|Нет (нужно ставить на каждый метод)|Да (все методы подразумевают `@ResponseBody`)|
|Возвращаемое значение|`ModelAndView` или `@ResponseBody`|Данные (автоматически JSON/XML)|
|Типичное использование|Приложение с JSP/Thymeleaf|Микросервис, SPA-бэкенд|

**Эквивалентны:**
```java
@RestControllerAdvice
public class RestExceptionHandler { ... }

// И:

@ControllerAdvice
@ResponseBody
public class RestExceptionHandler { ... }
```

**Вывод:** Для современных REST-приложений используйте `@RestControllerAdvice` — это сокращает код и делает намерения явными.

---
### 58. Что такое `HandlerInterceptor`? <br>Чем отличается от Filter (сервлетный)?

**Ответ:** `HandlerInterceptor` — это интерфейс Spring MVC для перехвата HTTP-запросов **на уровне диспетчера Spring**, до и после вызова контроллера.

#### **Методы `HandlerInterceptor`:**
```java
public interface HandlerInterceptor {

    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler);
    
    void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav);
    
    void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex);
    
}
```

#### **Отличия от сервлетного `Filter`:**

|Характеристика|`Filter` (Servlet API)|`HandlerInterceptor` (Spring MVC)|
|---|---|---|
|Уровень|Контейнер сервлетов (Tomcat)|Spring MVC (после `DispatcherServlet`)|
|Доступ к контроллеру|Нет|Есть (параметр `handler`)|
|Доступ к `ModelAndView`|Нет|Есть (в `postHandle`)|
|Знает о `@Controller` и маппинге|Нет|Да|
|Может использовать Spring-бины|Да (через внедрение)|Да (легко)|
|Порядок выполнения|**Внешний** (раньше)|**Внутренний** (после `DispatcherServlet`)|

#### **Когда что использовать:**
- **`Filter`** — для низкоуровневых вещей: логирование IP, CORS, сжатие, аутентификация на уровне контейнера, кодировка (`CharacterEncodingFilter`).
    
- **`HandlerInterceptor`** — для Spring-специфичных задач: проверка прав на основе аннотаций контроллера, добавление общих атрибутов в модель, измерение времени выполнения контроллера.    

#### **Пример интерсептора:**
```java
@Component
public class PerformanceInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(
		    HttpServletRequest request, 
		    HttpServletResponse response, 
		    Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }
    
    @Override
    public void afterCompletion(
		    HttpServletRequest request, 
		    HttpServletResponse response, 
		    Object handler, Exception ex) {
        long duration = System.currentTimeMillis() - 
		        (long) request.getAttribute("startTime");
        log.info("Request {} took {} ms", request.getRequestURI(), duration);
    }
}
```

---
### 59. Что такое `WebMvcConfigurer`? Зачем нужен?

**Ответ:** `WebMvcConfigurer` — это интерфейс для **программной настройки** Spring MVC поверх конфигурации по умолчанию (Spring Boot auto-configuration). Он пришел на замену XML-конфигурации и старому `WebMvcConfigurerAdapter`.

**Основные возможности (что можно настроить):**

|Метод|Что настраивает|
|---|---|
|`addInterceptors()`|Добавление `HandlerInterceptor`|
|`addCorsMappings()`|Глобальная настройка CORS|
|`addViewControllers()`|Простые маршруты без контроллера|
|`configureViewResolvers()`|Настройка резолверов представлений|
|`configureMessageConverters()`|Добавление/замена `HttpMessageConverter`|
|`addResourceHandlers()`|Настройка статических ресурсов (CSS, JS)|
|`configurePathMatch()`|Настройка маппинга путей (trailing slash, case-sensitive)|
|`addArgumentResolvers()`|Добавление кастомных резолверов параметров|
|`addReturnValueHandlers()`|Добавление обработчиков возвращаемых значений|

**Пример:**
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
        .addInterceptor(new PerformanceInterceptor())
        .addPathPatterns("/api/**");
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowedOrigins("https://example.com");
    }
    
    @Override
    public void configureMessageConverters(
	    List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }
}
```

**Важно:** В Spring Boot, если вы реализуете `WebMvcConfigurer`, ваши настройки **добавляются** к авто-конфигурации, а не заменяют её. Если нужно полностью заменить конфигурацию, используйте `@EnableWebMvc`.

---
### 60. Как настроить **CORS** в Spring Boot? (`@CrossOrigin`)

**CORS (Cross-Origin Resource Sharing)** — механизм безопасности браузера, который разрешает или запрещает веб-страницам обращаться к ресурсам на другом домене, порту или протоколе. (*"**Пускать** или **не пускать** запросы с других сайтов?"*)

**Ответ:** CORS (Cross-Origin Resource Sharing) настраивается 
	в Spring Boot на трех уровнях:

1️⃣ **Уровень 1: `@CrossOrigin` на уровне контроллера или метода (*самый простой*):**
```java
@RestController
@CrossOrigin(origins = "https://example.com") // для всего контроллера
public class UserController {
    
    @GetMapping("/user")
    @CrossOrigin(origins = "http://localhost:3000") // только для этого метода
    public User getUser() { ... }
}
```

2️⃣ **Уровень 2: Глобальная конфигурация через `WebMvcConfigurer` (*рекомендуемый способ*):**
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("https://example.com", "https://app.example.com")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600); // время кэширования preflight запроса (в секундах)
    }
}
```

3️⃣ **Уровень 3: Через `application.properties` (*Spring Boot 2+*):**
```properties
spring.mvc.cors.allowed-origins=https://example.com
spring.mvc.cors.allowed-methods=GET,POST,PUT,DELETE
spring.mvc.cors.allowed-headers=*
spring.mvc.cors.allow-credentials=true
spring.mvc.cors.max-age=3600
```

#### **Как работает CORS в Spring:**
1. При preflight-запросе (OPTIONS) Spring автоматически отвечает заголовками CORS.
2. При основном запросе проверяет заголовок `Origin` 
	   и добавляет `Access-Control-Allow-Origin` в ответ.    
```text
// ### 🧪 Как это работает под капотом (для сложных запросов)
1. Браузер отправляет preflight-запрос OPTIONS
   ↓
2. Сервер отвечает заголовками CORS:
   Access-Control-Allow-Origin: http://localhost:3000
   Access-Control-Allow-Methods: GET, POST
   ↓
3. Браузер проверяет — разрешено?
   ↓
4. Если да → отправляет реальный запрос
```

#### **Важные нюансы:**
- Если используются `@CrossOrigin` + глобальная конфигурация, 
	  они объединяются (*более специфичная имеет приоритет*).
    
- `allowCredentials = true` и `allowedOrigins = "*"` несовместимы 
	  (*нужно указывать конкретные origins*).
    
- Для WebFlux (*реактивный стек*) используется `CorsConfiguration` 
	  и `WebFluxConfigurer`.

#### 📊 Все параметры `@CrossOrigin`

|Параметр|Что делает|Пример|
|---|---|---|
|`origins` / `value`|Разрешенные домены|`"http://localhost:3000"`|
|`allowedHeaders`|Какие заголовки можно отправлять|`"Authorization", "Content-Type"`|
|`exposedHeaders`|Какие заголовки видит клиент|`"X-Total-Count"`|
|`methods`|Разрешенные HTTP-методы|`"GET", "POST", "DELETE"`|
|`allowCredentials`|Можно ли передавать куки/токены|`"true"`|
|`maxAge`|Кэширование preflight-запроса (сек)|`3600`|

**Частая ошибка:** Не настроен OPTIONS-метод для preflight-запросов. 
Spring обрабатывает его автоматически, если CORS настроен правильно.

---

[⬅️**Previous**](Группа_04-Управление_транзакциями.md)   //   [⬆️](SPRING-121_вопрос_на_middle.md)   //   [**Next**➡️](Группа_06-Spring_Boot_Core.md)
