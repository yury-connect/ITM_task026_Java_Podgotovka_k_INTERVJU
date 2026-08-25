### **3. RestTemplate (Legacy)**

**Популярность:** ⚠️ **Устарел, но ещё используется**  
**Почему:**
- Прост в освоении.    
- До сих пор поддерживается (хотя deprecated в новых версиях Spring).    

**Где использовать:**
- Легационные Spring MVC-приложения.

---
### **RestTemplate**

🔹 **Для чего?**
- Блокирующий (*синхронный*) HTTP-клиент.    
- Используется в традиционных Spring MVC-приложениях.    
- Подходит для простых сценариев, где не требуется реактивность.    

🔹 **Особенности:**
- Работает в рамках одного потока (блокирует его до получения ответа).    
- Интегрируется с Jackson для преобразования JSON ↔ DTO.    
- Упрощает работу с **REST API** (`GET`, `POST`, `PUT`, `DELETE` и т. д.).    

🔹 **Пример:**
```java
RestTemplate restTemplate = new RestTemplate();
String url = "https://api.example.com/users/1";
User user = restTemplate.getForObject(url, User.class);
```

---
## **Примеры с `RestTemplate` (синхронный, блокирующий клиент)**

`RestTemplate` — это классический клиент для работы с REST API в Spring. Он блокирует текущий поток до получения ответа.

### 🔹 **Настройка `RestTemplate`**
Добавим его в Spring-приложение:
```java
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

### 🔹 **GET-запрос (получение данных)**
**Получение объекта по ID:**
```java
@RestController
public class UserController {
    @Autowired
    private RestTemplate restTemplate;
		
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        String url = "https://jsonplaceholder.typicode.com/users/" + id;
        return restTemplate.getForObject(url, User.class); 
	        // GET + автоматическая десериализация в объект
    }
}
```

**Получение списка объектов:**
```java
@GetMapping("/users")
public List<User> getUsers() {
    String url = "https://jsonplaceholder.typicode.com/users";
    User[] users = restTemplate.getForObject(url, User[].class);
    return Arrays.asList(users);
}
```

### 🔹 **POST-запрос (отправка данных)**
```java
@PostMapping("/create")
public User createUser(@RequestBody User user) {
    String url = "https://jsonplaceholder.typicode.com/users";
    return restTemplate.postForObject(url, user, User.class); 
	    // POST + автоматическая сериализация
}
```

### 🔹 **PUT-запрос (обновление данных)**
```java
@PutMapping("/update/{id}")
public void updateUser(@PathVariable Long id, @RequestBody User user) {
    String url = "https://jsonplaceholder.typicode.com/users/" + id;
    restTemplate.put(url, user); // PUT (возвращает void)
}
```

### 🔹 **DELETE-запрос (удаление данных)**
```java
@DeleteMapping("/delete/{id}")
public void deleteUser(@PathVariable Long id) {
    String url = "https://jsonplaceholder.typicode.com/users/" + id;
    restTemplate.delete(url); // DELETE (возвращает void)
}
```

---

