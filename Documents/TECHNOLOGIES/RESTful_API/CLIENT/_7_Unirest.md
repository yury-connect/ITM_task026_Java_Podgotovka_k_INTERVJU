### **7. Unirest**

**Популярность:** 🟢 **Для быстрого прототипирования**  
**Почему:**
- Проще, чем Apache HttpClient.    
- Поддержка JSON "из коробки".    

**Где использовать:**
- Тесты, скрипты, пет-проекты.

---
## **5. Unirest (обёртка над Apache HttpClient)**

**Пакет:** `com.konghq:unirest-java`  
**Тип:** Синхронный + асинхронный.  
**Плюсы:**  
✔ Простой API.  
✔ Поддержка JSON "из коробки".  
**Минусы:**  
❌ Менее гибкий, чем Apache HttpClient напрямую.

### Пример (синхронный GET):
```java
import kong.unirest.Unirest;

public class UnirestExample {
    public static void main(String[] args) {
        String response = Unirest.get("https://jsonplaceholder.typicode.com/users/1")
                .asString()
                .getBody();
        System.out.println(response);
    }
}
```

---
