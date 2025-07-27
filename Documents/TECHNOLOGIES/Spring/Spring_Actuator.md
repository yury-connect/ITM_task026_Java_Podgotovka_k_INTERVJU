**Spring Actuator** — это подпроект **Spring Boot**, который предоставляет **готовые endpoint'ы** (HTTP и JMX) для мониторинга и управления приложением в `production`-среде.

### 🔹 **Основные возможности Spring Actuator**:
1. **Health Check** (`/actuator/health`) — проверка состояния приложения (UP/DOWN).    
2. **Info Endpoint** (`/actuator/info`) — вывод произвольной информации о приложении (версия, описание).    
3. **Metrics** (`/actuator/metrics`) — метрики (CPU, память, HTTP-запросы, базы данных и др.).   
4. **Env** (`/actuator/env`) — информация о переменных окружения и конфигурации.    
5. **Loggers** (`/actuator/loggers`) — динамическое управление уровнем логирования.    
6. **Thread Dump** (`/actuator/threaddump`) — дамп потоков приложения.    
7. **HTTP Tracing** (`/actuator/httptrace`) — история HTTP-запросов.    

### 🔹 **Как подключить?**
1. Добавить зависимость в `pom.xml` (Maven) или `build.gradle` (Gradle):
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
2. Настроить доступные endpoint'ы в `application.properties`/`application.yml`:
```properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
```

### 🔹 **Безопасность**
- По умолчанию доступны только `/actuator/health` и `/actuator/info`.    
- Для защиты `sensitive endpoint'ов` (`env`, `heapdump`) можно подключить **Spring Security**.    

### 🔹 **Пример использования**
```java
@SpringBootApplication
public class MyApp {
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);
    }
}
```

После запуска можно обратиться к:
- `http://localhost:8080/actuator/health`    
- `http://localhost:8080/actuator/metrics`    

### **Вывод**
Spring Actuator — мощный инструмент для **мониторинга, диагностики и управления** Spring Boot-приложением без написания собственного кода. 🚀

