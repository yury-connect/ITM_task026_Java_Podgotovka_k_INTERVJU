`@RestController` — это специализированная версия `@Controller` из Spring MVC, предназначенная для создания **RESTful веб-сервисов** (API).

### 1. Это `@Controller` + `@ResponseBody`
Главная фишка в том, что это **удобная составная аннотация**.
- `@Controller` помечает класс как Spring-бин, способный обрабатывать HTTP-запросы.    
- `@ResponseBody` говорит Spring'у: **"Не возвращай имя HTML-страницы (как в классическом MVC), а верни то, что возвращает метод, прямо в теле HTTP-ответа (обычно в JSON или XML)"**.

### 2. Как это работает (без `@RestController`)
Если бы вы писали старый контроллер без этой аннотации, вам пришлось бы ставить `@ResponseBody` перед каждым методом:
```java
@Controller
public class OldApiController {
    @ResponseBody // Нужно писать у каждого метода!
    @GetMapping("/user")
    public User getUser() {
        return new User("John");
    }
}
```

### 3. Как это работает (с `@RestController`)
Аннотация применяется на уровне класса, и все методы автоматически наследуют ее поведение:
```java
@RestController // Достаточно написать один раз
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return new User(id, "John"); // Автоматически конвертируется в JSON
    }
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.save(user); // Ответ сразу уйдет в JSON
    }
}
```

### 4. Что происходит под капотом?
Когда клиент (например, фронтенд на React или мобильное приложение) стучится в ваш API:
1. Метод контроллера возвращает Java-объект *(например, `User`)*.    
2. Spring с помощью **HttpMessageConverter** *(автоматически подключенного, если есть зависимость Jackson)* конвертирует этот объект в JSON (или XML).    
3. Этот JSON записывается в тело HTTP-ответа и отправляется клиенту.    

### Итог
**`@RestController`** = `@Controller` + `@ResponseBody` над каждым методом. Используется исключительно для API, где ответ — это данные (JSON/XML), а не HTML-страница.

---
