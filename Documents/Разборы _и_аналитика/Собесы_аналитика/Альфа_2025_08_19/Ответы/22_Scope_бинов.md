# Кратко (*простым языком*)

**Scope — это «жизненный цикл» и область видимости бина в Spring.** Самые распространённые: `singleton` (по умолчанию), `prototype`, `request`, `session`, `application`, `websocket`. Как правило: _сервисы/репозитории — singleton_, _объекты с состоянием на запрос — request/prototype_, _корзина пользователя — session_.

---
# Подробно — каждый scope, когда применять, примеры и подводные камни

### 1) `singleton` (*по умолчанию*)
- **Что:** один экземпляр на `ApplicationContext`.    
- **Когда:** подавляющее большинство сервисов, репозиториев, компонентов: stateless-логика.    
- **Пример:** `@Service class OrderService { ... }`    
- **Почему:** экономно по памяти, потокобезопасность решается внутри.    

---
### 2) `prototype`
- **Что:** при каждом `getBean()` создаётся новый экземпляр. Контейнер **не управляет destroy**.    
- **Когда:** когда нужен stateful объект кратковременной жизни (factory-like), например DTO-класс с большим состоянием, временный builder, или объект, который нельзя шарить.    
- **Пример:**   
```java
@Component
@Scope("prototype")
public class ReportBuilder { ... }
```

- **Подводный камень:** если prototype внедрён в singleton напрямую, то singleton получит один экземпляр при создании. Для получения новых экземпляров внутри singleton используйте `ObjectProvider<ReportBuilder>` / `Provider` / `@Lookup` / `ApplicationContext.getBean(...)`.    

---
### 3) `request` (web)
- **Что:** один бин на HTTP-запрос. Живёт только в контексте запроса.    
- **Когда:** хранить данные конкретного запроса (например, `RequestContext`, parsed user info), которые нужны в разных слоях.    
- **Пример:**
```java
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestContext { ... }
```

- **Важно:** чаще всего требуется `proxyMode` (scoped proxy), потому что контроллеры/сервисы — singleton, и им нужно «вставлять» request-скоуп-бин.    

---
### 4) `session` (web)
- **Что:** один бин на HTTP-сессию пользователя.    
- **Когда:** корзина покупок, пользовательские настройки в рамках сессии.    
- **Пример:**
 ```java
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCart { ... }
```

---
### 5) `application` (ServletContext)
- **Что:** бин на весь веб-приложение (по сути: один на `ServletContext`).    
- **Когда:** shared data на весь веб-приложение, редко используется.    

---
### 6) `websocket`
- **Что:** бин на WebSocket-сессию (при использовании spring-websocket).    
- **Когда:** state per websocket connection.    

---
### 7) Кастомные scope
- **Что:** можно зарегистрировать собственный scope (`ConfigurableBeanFactory.registerScope("my", scopeImpl)`).    
- **Когда:** специфичные lifecycle (например, per-tenant, per-correlationId).    

---
# Внедрение с разными scope-ами — важные техники

### Scoped proxy
- `@Scope(..., proxyMode = ScopedProxyMode.TARGET_CLASS)` создаёт прокси, который внутри делегирует к реальному бину соответствующего scope. Нужен, когда singleton ожидает ссылку на request/session bean.    

### Получать prototype динамически
- `ObjectProvider<T>` / `javax.inject.Provider<T>` / `ApplicationContext.getBean(...)` / `@Lookup` — способы получить свежий prototype внутри singleton.
    

Пример с `ObjectProvider`:
```java
@Autowired
private ObjectProvider<ReportBuilder> reportBuilderProvider;

public void doJob() {
   ReportBuilder rb = reportBuilderProvider.getObject();
   ...
}
```

### `@Lookup`
- Аннотация для динамического получения бина (генерирует override-метод, возвращающий новый prototype).    

---
# Жизненный цикл и destroy для prototype
- **Prototype**: Spring **не вызывает** `@PreDestroy`/`DisposableBean` при завершении — ответственность потребителя. Для request/session контейнер вызывает destroy при завершении scope.    

---
# Практические правила и рекомендации (как отвечать на собесе)

1. **Всегда по умолчанию — `singleton`** для сервисов и репозиториев.
    
2. **Используй `prototype` экономно** — только когда нужен новый экземпляр часто.
    
3. **Для per-request state — `request` + scoped proxy** (или передавать данные явно как аргументы).
    
4. **Для per-user state — `session`**, но избегай больших объёмов данных в сессии.
    
5. **Не внедряй stateful prototype напрямую в singleton** — используй `ObjectProvider`/`@Lookup`.
    
6. **Если нужна сложная политика жизни — делай кастомный scope или управляй вручную**.    

---
# Примеры из практики (коротко, кейсы)

- **Singleton**: `UserService`, `PaymentService`, `Repository` — stateless, многопользовательские вызовы.
    
- **Prototype**: `ReportBuilder` — создавалась новая инстанция для каждого запроса формирования отчёта; получал через `ObjectProvider` в singleton сервисе.
    
- **Request scope**: `RequestContext` — парсил correlationId из заголовка и делал доступным в разных слоях через `@Autowired` (использовался proxyMode).
    
- **Session scope**: `ShoppingCart` в e-commerce PoC (хранение id товаров, но без тяжёлых объектов — только ids).
    
- **Custom scope**: делал per-tenant scope в мульти-тенантном приложении (регистрация scope на старте, бин держал tenant-specific cache).    

---
# Короткие фразы для интервью <br>(*1–2 предложения*)

- **«`singleton` — дефолт, prototype — новый экземпляр при каждом getBean, request/session — веб-скоупы; для внедрения request/session в singleton используют scoped proxy или ObjectProvider; prototype-дестройте сами»**.    

---