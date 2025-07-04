# Аннотации для передачи параметров в контроллер. 
### На стороне клиента - `Thumeleaf`. На стороне сервера - `Spring`.


* **[@RequestParam](#requestparam)** используется для query-параметров (например, ?param=value).
* **[@PathVariable](#pathvariable)** — для параметров пути (например, /user/{id}).
* **[@RequestBody](#requestbody)** — для получения тела запроса в виде объекта.
* **[@ModelAttribute](#modelattribute)** — для связывания данных формы с объектами.
* **[@RequestHeader](#requestheader)** и **[@CookieValue](#cookievalue)** — для работы с заголовками и куками.
* **[@RequestPart](#requestpart)** —
* **[@SessionAttribute](#sessionattribute)** —
* **[@MatrixVariable](#matrixvariable)** —
* ПРИМЕР 1: [**Ссылка** для перехода](#ссылка-для-перехода)
* ПРИМЕР 2: [**Кнопка** для перехода](#кнопка-для-перехода)



---
## @RequestParam
Используется для извлечения **параметров из запроса**. Чаще всего применяется для обработки данных из строки запроса (например, query parameters).

##### Клиентская часть (_Thymeleaf_):
```html
<form th:action="@{/search}" method="get">
    <label for="query">Search Query:</label>
    <input type="text" id="query" name="query">
    <button type="submit">Search</button>
</form>
```

##### Контроллер:
```java
@GetMapping("/search")
public String search(@RequestParam("query") String query, Model model) {
    model.addAttribute("result", "Result for query: " + query);
    return "searchResult";
}
```
##### Пример запроса: `/search?query=spring`

<p align="right" style="color: gray;">
    <a href="#аннотации-для-передачи-параметров-в-контроллер">Перейти в начало</a>
</p>





---
## @PathVariable
Используется для извлечения переменных из `URI` _(маршрута)_ // через **URL-путь**.

##### Клиентская часть (Thymeleaf):
```html
<a th:href="@{/user/1}">View User</a>
```

##### Контроллер:
```java
@GetMapping("/user/{id}")
public String getUserById(@PathVariable("id") int userId, Model model) {
    model.addAttribute("user", "User ID: " + userId);
    return "userDetails";
}
```
##### Пример запроса: `/user/1`

<p align="right" style="color: gray;">
    <a href="#аннотации-для-передачи-параметров-в-контроллер">Перейти в начало</a>
</p>





---
## @RequestBody
Используется для передачи `JSON` или других данных в теле запроса. Для отправки `JSON` с помощью `HTML`-форм потребуется `JavaScript`.

##### Клиентская часть (JavaScript для отправки JSON):
```html
<form id="userForm">
    <input type="text" id="name" name="name" placeholder="Name">
    <input type="email" id="email" name="email" placeholder="Email">
    <button type="button" onclick="submitForm()">Submit</button>
</form>

<script>
function submitForm() {
    const user = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value
    };

    fetch('/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    }).then(response => response.json()).then(data => console.log(data));
}
</script>
```

##### Контроллер:
```java
@PostMapping("/users")
public ResponseEntity<String> createUser(@RequestBody User user) {
    // Сохранить пользователя
    return ResponseEntity.ok("User created: " + user.getName());
}
```

<p align="right" style="color: gray;">
    <a href="#аннотации-для-передачи-параметров-в-контроллер">Перейти в начало</a>
</p>





---
## @ModelAttribute
Используется для привязки полей `HTML`-форм к объектам. Обычно применяется для работы с формами.

##### Клиентская часть (Thymeleaf):
```html
<form th:action="@{/user/save}" th:object="${user}" method="post">
    <label for="name">Name:</label>
    <input type="text" id="name" th:field="*{name}">
    
    <label for="email">Email:</label>
    <input type="email" id="email" th:field="*{email}">
    
    <button type="submit">Save</button>
</form>
```

##### Контроллер:
```java
@PostMapping("/user/save")
public String saveUser(@ModelAttribute User user, Model model) {
    model.addAttribute("message", "User saved: " + user.getName());
    return "userSaved";
}
```

<p align="right" style="color: gray;">
    <a href="#аннотации-для-передачи-параметров-в-контроллер">Перейти в начало</a>
</p>





---
## @RequestHeader
Используется для передачи данных через заголовки HTTP-запроса.

##### Клиентская часть (JavaScript для отправки заголовка):
```html
<button onclick="sendRequest()">Send Request</button>

<script>
function sendRequest() {
    fetch('/header', {
        method: 'GET',
        headers: {
            'User-Agent': 'MyCustomUserAgent'
        }
    }).then(response => response.text()).then(data => console.log(data));
}
</script>
```

##### Контроллер:
```java
@GetMapping("/header")
public String getHeader(@RequestHeader("User-Agent") String userAgent, Model model) {
    model.addAttribute("header", "User-Agent: " + userAgent);
    return "headerView";
}
```

<p align="right" style="color: gray;">
    <a href="#аннотации-для-передачи-параметров-в-контроллер">Перейти в начало</a>
</p>





---
## @CookieValue
Используется для получения данных **из куки**.

##### Клиентская часть (JavaScript для установки куки):
```html
<button onclick="setCookie()">Set Cookie</button>

<script>
function setCookie() {
    document.cookie = "sessionId=abc123; path=/";
    window.location.href = "/cookie";
}
</script>
```

##### Контроллер:
```java
@GetMapping("/cookie")
public String getCookie(@CookieValue(value = "sessionId", defaultValue = "none") String sessionId, Model model) {
    model.addAttribute("cookie", "Session ID: " + sessionId);
    return "cookieView";
}
```

<p align="right" style="color: gray;">
    <a href="#аннотации-для-передачи-параметров-в-контроллер">Перейти в начало</a>
</p>





---
## @RequestPart
Используется для обработки части `multipart`-запроса, например, для загрузки файлов.

##### Клиентская часть (Thymeleaf):
```html
<form th:action="@{/upload}" method="post" enctype="multipart/form-data">
    <input type="file" name="file">
    <button type="submit">Upload</button>
</form>
```

##### Контроллер:
```java
@PostMapping("/upload")
public String handleFileUpload(@RequestPart("file") MultipartFile file, Model model) {
    model.addAttribute("message", "File uploaded: " + file.getOriginalFilename());
    return "uploadResult";
}
```

<p align="right" style="color: gray;">
    <a href="#аннотации-для-передачи-параметров-в-контроллер">Перейти в начало</a>
</p>





---
## @SessionAttribute
Используется для извлечения данных **из сессии** пользователя.

##### Клиентская часть (Thymeleaf):
```html
<a th:href="@{/profile}">View Profile</a>
```

##### Контроллер:
```java
@GetMapping("/profile")
public String getProfile(@SessionAttribute("user") User user, Model model) {
    model.addAttribute("user", user);
    return "profile";
}
```
* Для корректной работы сессии, в контроллере должен быть ранее сохранён объект сессии **user**.

<p align="right" style="color: gray;">
    <a href="#аннотации-для-передачи-параметров-в-контроллер">Перейти в начало</a>
</p>





---
## @MatrixVariable
Используется для передачи данных через матричные параметры в URL.

##### Клиентская часть (Thymeleaf):
```html
<a th:href="@{/cars/1;color=red;brand=bmw}">View Car</a>
```

##### Контроллер:
```java
@GetMapping("/cars/{id}")
public String getCarDetails(@PathVariable String id, @MatrixVariable Map<String, String> filters, Model model) {
    model.addAttribute("filters", filters);
    return "carDetails";
}
```
##### Пример URL запроса: `/cars/1;color=red;brand=bmw`

<p align="right" style="color: gray;">
    <a href="#аннотации-для-передачи-параметров-в-контроллер">Перейти в начало</a>
</p>





---

---

---

# ПРИМЕРЫ

## ССЫЛКА ДЛЯ ПЕРЕХОДА
### Клиентская часть (HTML с Thymeleaf:):
На стороне клиента можно создать ссылку с динамическим параметром id, используя Thymeleaf следующим образом:
```html
<a th:href="@{'/user/' + ${user.id}}">Просмотреть пользователя</a>
```
> Здесь 
>  * `user.id` — это идентификатор, который мы передаем в ссылку, и _Thymeleaf_ динамически подставит его в _URL_. 
Например, если `user.id = 123`, ссылка станет: `/user/123`.

### Контроллер на Spring MVC:
В контроллере можно использовать аннотацию `@PathVariable` для захвата параметра `id` из URL:
```java
@Controller
public class UserController {

    @GetMapping("/user/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        // Логика для получения пользователя по id
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user-details"; // Имя шаблона Thymeleaf для отображения деталей пользователя
    }
}
```
> Здесь:
> * `@GetMapping("/user/{id}")` — контроллер обрабатывает GET-запросы по пути `/user/{id}`, 
где `{id}` — это переменная, которую можно извлечь с помощью `@PathVariable`.
> * `@PathVariable Long id` — извлекаем переменную `id` из URL и передаем ее в метод контроллера.

### Thymeleaf-шаблон для отображения информации о пользователе:
После того как контроллер получит id, он может извлечь данные о пользователе и отобразить их в шаблоне:
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
    <head>
        <title>Информация о пользователе</title>
    </head>
    <body>
        <h1>Пользователь: <span th:text="${user.name}"></span></h1>
        <p>Возраст: <span th:text="${user.age}"></span></p>
        <p>Email: <span th:text="${user.email}"></span></p>
    </body>
</html>
```

<p align="right" style="color: gray;">
    <a href="#аннотации-для-передачи-параметров-в-контроллер">Перейти в начало</a>
</p>





---
## КНОПКА ДЛЯ ПЕРЕХОДА
### HTML с Thymeleaf _(кнопка для перехода)_:
На стороне клиента можно создать кнопку, которая при нажатии перенаправит пользователя на нужный метод контроллера:
```html
<form th:action="@{/someAction}" method="get">
    <button type="submit">Перейти</button>
</form>
```
> Здесь:
> * `th:action="@{/someAction}"` — указывает URL для перенаправления (в данном случае, `/someAction`).
> * Метод отправки формы — `GET`, что соответствует обычному переходу по ссылке.

### Контроллер на Spring MVC:
В контроллере можно использовать аннотацию `@GetMapping` для обработки этого запроса:
```java
@Controller
public class SomeController {

    @GetMapping("/someAction")
    public String handleAction() {
        // Логика метода контроллера
        return "result-page"; // Имя Thymeleaf-шаблона, куда произойдет переход
    }
}
```
> Здесь:
> * `@GetMapping("/someAction")` — обрабатывает GET-запрос по пути `/someAction`.
> * Внутри метода можно реализовать любую необходимую логику, а затем вернуть имя представления, например, `"result-page"`.

### Thymeleaf-шаблон для отображения результата:
После того как контроллер выполнит свою логику, он может вернуть результат для отображения на соответствующей странице:
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
    <head>
        <title>Результат</title>
    </head>
    <body>
        <h1>Вы успешно перешли по кнопке!</h1>
    </body>
</html>
```
> * Thymeleaf генерирует кнопку, которая вызывает GET-запрос на указанный URL.
> * Контроллер обрабатывает запрос с помощью аннотации `@GetMapping` и выполняет необходимую логику.
> * Результат отображается в соответствующем Thymeleaf-шаблоне.

<p align="right" style="color: gray;">
    <a href="#аннотации-для-передачи-параметров-в-контроллер">Перейти в начало</a>
</p>
