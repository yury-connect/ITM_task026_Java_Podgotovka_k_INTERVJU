# **Circuit Breaker** (**`Resilience4j`**) <br>для обработки сбоев в микросервисной архитектуре

---
#### **1. Что такое Circuit Breaker?**
**Circuit Breaker (CB)** — это паттерн проектирования, который предотвращает каскадные сбои в распределённых системах. Он временно "разрывает цепь" при частых ошибках, перенаправляя запросы в fallback-методы или возвращая ошибку без попытки вызова неработающего сервиса.

Основные состояния CB:
- **Closed** — запросы проходят нормально.    
- **Open** — сервис не отвечает, запросы не отправляются 
	  (*возвращается `fallback` или `ошибка`*).    
- **Half-Open** — пробует пропустить часть запросов 
	  для проверки восстановления сервиса.    

#### **2. Resilience4j — реализация Circuit Breaker для Java**

**Resilience4j** — это легковесная библиотека для обеспечения отказоустойчивости 
	(*в отличие от Netflix Hystrix, который устарел*).

Поддерживает:
- **Circuit Breaker** (основной механизм)    
- **Rate Limiter** (ограничение числа запросов)    
- **Retry** (повторные попытки)    
- **Bulkhead** (изоляция ресурсов)    
- **TimeLimiter** (таймауты)    

#### **3. Как добавить Resilience4j в Spring Boot микросервис?**

##### **Шаг 1: Добавьте зависимости**
В `pom.xml` (для `Maven`):
```xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot2</artifactId>
    <version>2.1.0</version> <!-- Актуальную версию смотрим на resilience4j.github.io -->
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId> <!-- Нужен для работы аннотаций -->
</dependency>
```

##### **Шаг 2: Настройка Circuit Breaker в `application.yml`**
```yaml
resilience4j:
  circuitbreaker:
    instances:
      myService: # Имя вашего Circuit Breaker
        register-health-indicator: true # Интеграция с /actuator/health
        sliding-window-type: COUNT_BASED # Тип окна (COUNT_BASED или TIME_BASED)
        sliding-window-size: 10 # Размер окна (кол-во вызовов)
        minimum-number-of-calls: 5 # Минимальное число вызовов перед расчетом ошибок
        failure-rate-threshold: 50 # % ошибок для перехода в OPEN
        wait-duration-in-open-state: 5s # Время в OPEN перед переходом в HALF_OPEN
        permitted-number-of-calls-in-half-open-state: 3 # Кол-во вызовов в HALF_OPEN
        automatic-transition-from-open-to-half-open-enabled: true
```

##### **Шаг 3: Использование в коде**
Вариант 1: **Аннотации (*проще*)**
```java
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class MyService {

    @CircuitBreaker(name = "myService", fallbackMethod = "fallbackMethod")
    public String callExternalService() {
        // Вызов другого сервиса (например, через RestTemplate/WebClient)
        return restTemplate.getForObject("http://external-service/api", String.class);
    }

    // Fallback-метод (должен иметь тот же возвращаемый тип + параметры + исключение)
    public String fallbackMethod(Exception ex) {
        return "Fallback response (Service is down)";
    }
}
```

Вариант 2: **Программный подход (*гибче*)**
```java
CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("myService");
Supplier<String> decoratedSupplier = CircuitBreaker
    .decorateSupplier(circuitBreaker, () -> callExternalService());

Try.ofSupplier(decoratedSupplier)
    .recover(throwable -> "Fallback response")
    .get();
```

#### **4. Интеграция с Prometheus/Grafana**
`Resilience4j` предоставляет метрики для `Prometheus`:

1. Добавьте зависимость:
```xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-micrometer</artifactId>
    <version>2.1.0</version>
</dependency>
```

2. Настройте метрики:
```java
@Bean
public CircuitBreakerRegistry circuitBreakerRegistry() {
    return CircuitBreakerRegistry.ofDefaults();
}

@Bean
public MeterBinder resilience4jMetrics(CircuitBreakerRegistry registry) {
    return new Resilience4jBinder(registry);
}
```

3. В Grafana можно добавить дашборд для мониторинга состояния Circuit Breaker (например, `resilience4j_circuitbreaker_state`).    

#### **5. Дополнительные возможности**
- **Retry + Circuit Breaker**:
```java
@Retry(name = "myService", fallbackMethod = "fallback")
@CircuitBreaker(name = "myService", fallbackMethod = "fallback")
public String callService() { ... }
```
     
- **Bulkhead (ограничение параллельных вызовов)**
```java
@Bulkhead(name = "myService", type = Bulkhead.Type.SEMAPHORE)
```

#### **6. Где применять?**
- Вызовы других микросервисов (REST/gRPC)    
- Работа с внешними API (платежи, геокодинг и т. д.)    
- Доступ к БД (если есть сетевые вызовы)    

**Вывод:** Resilience4j — это мощный инструмент для повышения отказоустойчивости ваших микросервисов. Его легко интегрировать в Spring Boot, а в сочетании с mTLS и мониторингом (Prometheus/Grafana) вы получите надёжную систему.

---
