# 🚀 Реализация в Spring Boot (Java)

Разберём **два варианта**:  
1️⃣ **`WebSocket`** (двусторонний чат)  
2️⃣ **`SSE`** (поток уведомлений)

---
# 🔌 1. WebSocket в Spring Boot
Используем **STOMP** поверх **WebSocket** *(production-ready вариант)*.
### 📦 Зависимость
```xml
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-websocket</artifactId>  
</dependency>
```

---
### ⚙️ Конфигурация
```java
@Configuration  
@EnableWebSocketMessageBroker  
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {  
  
    @Override  
    public void configureMessageBroker(MessageBrokerRegistry config) {  
        config.enableSimpleBroker("/topic");   // куда публикуем  
        config.setApplicationDestinationPrefixes("/app"); // куда шлёт клиент  
    }  
  
    @Override  
    public void registerStompEndpoints(StompEndpointRegistry registry) {  
        registry.addEndpoint("/ws")  
                .setAllowedOriginPatterns("*")  
                .withSockJS(); // fallback  
    }  
}
```

---
### 🎯 Контроллер
```java
@Controller  
public class ChatController {  
  
    @MessageMapping("/chat")       // клиент шлёт на /app/chat  
    @SendTo("/topic/messages")     // подписчики получают  
    public String send(String message) {  
        return message;  
    }  
}
```

---
### 🔁 Поток

**Client** → `/app/chat`  
**Server** → `/topic/messages`  
**Subscribers** получают сообщение

---
### 🧠 Важно (production)
- Горизонтальное масштабирование → `Redis`/`RabbitMQ` broker    
- Без брокера — только один инстанс    
- Security: `Spring Security` + `CSRF` disable для ws    

---
# 📡 2. SSE в Spring Boot

Проще. Используем `SseEmitter`.

---
### 🎯 Контроллер
```java
    @GetMapping("/stream")  
    public SseEmitter stream() {  
		  
        SseEmitter emitter = new SseEmitter();  
		  
        Executors.newSingleThreadExecutor().execute(() -> {  
            try {  
                for (int i = 0; i < 5; i++) {  
                    emitter.send("Event #" + i);  
                    Thread.sleep(2000);  
                }  
                emitter.complete();  
            } catch (Exception e) {  
                emitter.completeWithError(e);  
            }  
        });  
		  
        return emitter;  
    }  
@RestController  
public class NotificationController {  
  
}
```

---
### 🔁 Поток

Client делает GET /stream  
Server держит соединение  
Периодически отправляет данные

---
# ⚖️ Архитектурное сравнение в backend

|                     | WebSocket                | SSE                        |
| ------------------- | ------------------------ | -------------------------- |
| Thread model        | Event-driven             | Обычно blocking            |
| Масштабирование     | Через broker             | Stateless                  |
| Нагрузка            | Лучше при high-frequency | Подходит для low-frequency |
| Поддержка браузером | Через STOMP/SockJS       | EventSource                |

---
# 🧠 Как выбрать (практика middle/senior)

Если:
- Нужно real-time + bidirectional → WebSocket    
- Только уведомления → SSE    
- Высокая нагрузка → WebSocket + внешний broker    
- Простой сервис статусов → SSE    

---
# 🏗 Production-архитектура (рекомендовано)

#### **WebSocket:**
`Client` → `WS` → `App`  
              ↓  
           `Redis Pub/Sub`  
              ↓  
           Другие инстансы

#### **SSE:**
`Client` → `App` → `DB`/`Queue`

---
