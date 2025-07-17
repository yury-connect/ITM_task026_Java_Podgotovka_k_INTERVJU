### **1. WebClient (Spring WebFlux)**

**Популярность:** 🔥 **#1 в Spring-экосистеме**  
**Почему:**
- Стандарт для реактивных приложений (Spring Boot 2.0+).    
- Поддержка асинхронности, HTTP/2, SSE.    
- Интеграция с Spring Security, Circuit Breaker.    

**Где использовать:**
- Реактивные микросервисы.    
- Высоконагруженные системы.

---
### **WebClient**

🔹 **Для чего?**
- Неблокирующий (асинхронный) реактивный HTTP-клиент.    
- Часть Spring WebFlux (реактивный стек Spring).    
- Подходит для микросервисов, высоконагруженных систем и реактивных приложений.    

🔹 **Особенности:**
- Работает на основе **Project Reactor** (Mono/Flux).    
- Поддерживает асинхронные и потоковые запросы.    
- Лучше масштабируется (меньше потребляет ресурсов при большом числе запросов).    

🔹 **Пример:**
```java
WebClient webClient = WebClient.create();
Mono<User> userMono = webClient.get()
        .uri("https://api.example.com/users/1")
        .retrieve()
        .bodyToMono(User.class);

userMono.subscribe(user -> System.out.println(user));
```

---
## **Примеры с `WebClient` (асинхронный, реактивный клиент)**

`WebClient` — это современный неблокирующий клиент из Spring WebFlux. Работает с `Mono` и `Flux`.

### 🔹 **Настройка `WebClient`**

Добавим его в Spring Boot:
```java
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
    }
}
```

### 🔹 **GET-запрос (получение данных)**

**Получение объекта (`Mono`):**
```java
@RestController
public class UserController {
    @Autowired
    private WebClient webClient;

    @GetMapping("/user/{id}")
    public Mono<User> getUser(@PathVariable Long id) {
        return webClient.get()
                .uri("/users/" + id)
                .retrieve()
                .bodyToMono(User.class); // Асинхронный запрос (возвращает Mono)
    }
}
```

**Получение списка (`Flux`):**
```java
@GetMapping("/users")
public Flux<User> getUsers() {
    return webClient.get()
            .uri("/users")
            .retrieve()
            .bodyToFlux(User.class); // Асинхронный поток данных
}
```

### 🔹 **POST-запрос (отправка данных)**
```java
@PostMapping("/create")
public Mono<User> createUser(@RequestBody User user) {
    return webClient.post()
            .uri("/users")
            .bodyValue(user) // Устанавливаем тело запроса
            .retrieve()
            .bodyToMono(User.class);
}
```

### 🔹 **PUT-запрос (обновление данных)**
```java
@PutMapping("/update/{id}")
public Mono<User> updateUser(@PathVariable Long id, @RequestBody User user) {
    return webClient.put()
            .uri("/users/" + id)
            .bodyValue(user)
            .retrieve()
            .bodyToMono(User.class);
}
```

### 🔹 **DELETE-запрос (удаление данных)**
```java
@DeleteMapping("/delete/{id}")
public Mono<Void> deleteUser(@PathVariable Long id) {
    return webClient.delete()
            .uri("/users/" + id)
            .retrieve()
            .bodyToMono(Void.class); // Возвращает пустой Mono
}
```


---
