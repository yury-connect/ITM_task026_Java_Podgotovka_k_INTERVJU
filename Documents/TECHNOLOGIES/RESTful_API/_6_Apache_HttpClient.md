### **6. Apache HttpClient**

**Популярность:** ⬇️ **Снижается из-за сложности**  
**Почему:**
- Мощный, но громоздкий.    
- Используется в библиотеках (например, Elasticsearch Client).    

**Где использовать:**
- Low-level HTTP-запросы.    
- Интеграция со старыми системами.
 ---
## **4. Apache HttpClient**

**Пакет:** `org.apache.httpcomponents:httpclient`  
**Тип:** Синхронный (есть асинхронная версия `HttpAsyncClient`).  
**Плюсы:**  
✔ Мощный и гибкий.  
✔ Поддержка сложных сценариев (кеширование, пулы соединений).  
**Минусы:**  
❌ Громоздкий API.  
❌ Нет автоматической работы с JSON.

### Пример (синхронный GET):
```java
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ApacheHttpClientExample {
    public static void main(String[] args) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("https://jsonplaceholder.typicode.com/users/1");
            String response = client.execute(request, httpResponse -> 
                EntityUtils.toString(httpResponse.getEntity()));
            System.out.println(response);
        }
    }
}
```

---
