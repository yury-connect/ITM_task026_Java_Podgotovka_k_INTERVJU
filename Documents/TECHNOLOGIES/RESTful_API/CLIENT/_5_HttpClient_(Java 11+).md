### **5. HttpClient (Java 11+)**

**Популярность:** ⬆️ **Растёт с переходом на Java 17+**  
**Почему:**
- Встроен в JDK.    
- Поддержка HTTP/2 и WebSockets.    

**Где использовать:**
- Приложения без Spring.    
- Когда нельзя добавить внешние зависимости.

---
## **1. HttpClient (Java 11+)**

**Пакет:** `java.net.http` (встроен в Java 11+)
**Тип:** Синхронный и асинхронный (на основе `CompletableFuture`)  
**Плюсы:**  
✔ Встроен в JDK, не требует зависимостей.  
✔ Поддержка HTTP/2.  
✔ Асинхронные запросы через `CompletableFuture`.  
**Минусы:**  
❌ Менее удобен, чем Spring-клиенты (нет автоматической сериализации JSON).

### Пример (синхронный GET):
```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JavaHttpClientExample {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/1"))
                .build();
			
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}
```

### Пример (асинхронный GET):
```java
client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
      .thenApply(HttpResponse::body)
      .thenAccept(System.out::println)
      .join(); // или без join() для настоящей асинхронности
```

---

