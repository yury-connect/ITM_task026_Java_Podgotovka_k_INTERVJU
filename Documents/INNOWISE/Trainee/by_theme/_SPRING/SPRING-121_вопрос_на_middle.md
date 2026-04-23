Spring / Spring Boot — это, пожалуй, самая важная тема для Middle-разработчика. Здесь я также разбил вопросы по частоте встречаемости: от «священных коров», которые спрашивают всегда, до глубоких внутренностей, которые выделяют сеньоров.

**Общий объем: ~120 вопросов.**

---
### Группа 1: [Основы IoC и DI](Группа_01-Основы_IoC_и_DI.md)
*(Задают в 99% случаев)*
_Без этих вопросов собеседование не начнется._

1. Что такое IoC (Inversion of Control)?    
2. Что такое DI (Dependency Injection)? В чем разница между IoC и DI?    
3. Какие типы внедрения зависимостей поддерживает Spring? (Field, Setter, Constructor).    
4. Какой тип внедрения предпочтительнее и почему? (Constructor injection — почему immutable и тестируемость).    
5. Что такое ApplicationContext? Чем отличается от BeanFactory?    
6. Что такое BeanDefinition?    
7. Что такое Spring Bean? Жизненный цикл бина (по шагам: создание, заполнение свойств, BeanPostProcessor, init, destroy).    
8. Какие скоупы (scope) бинов бывают? (singleton, prototype, request, session, application, websocket).    
9. В чем разница между `@Component`, `@Service`, `@Repository`, `@Controller`?    
10. Что такое `@Autowired`? Как работает разрешение зависимостей (byType -> byName -> `@Primary` -> `@Qualifier`)?    
11. Что такое циклическая зависимость? Как Spring её решает (三级缓存 — трехуровневый кеш)?    
12. Почему не получается заинжектить `Prototype` бин в `Singleton` бин? Как решить проблему (прокси, `@Lookup`, `ObjectFactory`)?    
13. Что такое `@Configuration`? Чем отличается от `@Component`? (Proxy CGLIB).    
14. Что такое `@Bean` (методный уровень)? Чем отличается от `@Component`?    
15. Что такое `@PostConstruct` и `@PreDestroy`?    
16. Как инициализировать бин после того, как все свойства установлены? (InitializingBean, @PostConstruct, init-method).    

### Группа 2: [Конфигурация и свойства](Группа 2: Конфигурация и свойства )
*(Очень высокая частота)*
17. Что такое `application.properties` / `application.yml`?    
18. Как связать свойства из файла с полем в классе? (`@Value`, `@ConfigurationProperties`).    
19. В чем преимущество `@ConfigurationProperties` перед `@Value`? (Type safety, валидация, вложенные объекты).    
20. Как задать профили (profiles)? (`@Profile`, `spring.profiles.active`).    
21. Как сделать конфигурацию, зависящую от профиля? (`application-dev.yml`, `application-prod.yml`).    
22. Что такое `@PropertySource`?    
23. Что такое `SpringEnvironment`? Как получить доступ к пропертям из кода?    
24. Как задать случайный порт для тестов (`server.port=0`)?    
25. Поддержка мульти-документ файлов YAML в Spring Boot?    

### Группа 3: [AOP (Aspect Oriented Programming)](Группа_03-AOP.md)
 *— Средне-высокая частота*
26. Что такое AOP? Зачем нужен? (Логирование, транзакции, security).    
27. Основные термины AOP: Aspect, JoinPoint, Advice, Pointcut, Weaving.    
28. Какие типы Advice бывают? (`@Before`, `@After`, `@AfterReturning`, `@AfterThrowing`, `@Around`).    
29. Чем отличается `@Around` от остальных?    
30. Как работают прокси в Spring AOP? (JDK Dynamic Proxy vs CGLIB).    
31. Почему метод `@Transactional`, вызванный из того же класса, не работает? (Проблема self-invocation).    
32. Как заставить работать AOP при self-invocation? (AopContext.currentProxy()).    
33. Что такое `@EnableAspectJAutoProxy(proxyTargetClass=true)`?    
34. Можно ли зааспектить `private` метод? (Нет, только public/ protected из-за прокси).    

### Группа 4: [Управление транзакциями (Очень высокая частота)](Группа_04-Управление_транзакциями.md)
35. Что такое `@Transactional`? Как он работает под капотом? (Прокси + AOP).    
36. Какие propagation (распространение транзакций) бывают? (`REQUIRED`, `REQUIRES_NEW`, `SUPPORTS`, `MANDATORY`, `NOT_SUPPORTED`, `NEVER`, `NESTED`).    
37. Что делает `REQUIRES_NEW`? Будет ли транзакция приостановлена?    
38. Какие isolation levels (уровни изоляции) поддерживает Spring? (`DEFAULT`, `READ_UNCOMMITTED`, `READ_COMMITTED`, `REPEATABLE_READ`, `SERIALIZABLE`).    
39. Как работает `@Transactional(readOnly = true)`? Оптимизации (Hibernate, flush mode).    
40. Что такое `rollbackFor`? По умолчанию откат только по `RuntimeException` и `Error`.    
41. Почему проверяемые исключения (`Exception`) не откатывают транзакцию по умолчанию?    
42. Как задать таймаут транзакции? (`timeout`).    
43. Проблема: `@Transactional` в private методе — работает?    
44. Как мониторить транзакции и видеть, открыта ли транзакция на самом деле?    
45. Что такое `TransactionSynchronizationManager`?    
46. Как работает менеджер транзакций (`PlatformTransactionManager`)? Реализации (`DataSourceTransactionManager`, `JtaTransactionManager`).    

### Группа 5: [Spring MVC и REST (Очень высокая частота)](Группа_05-Spring_MVC_и_REST.md)
47. Что такое DispatcherServlet? Как работает? (Front Controller).    
48. Жизненный цикл HTTP запроса в Spring MVC (от сервлета до контроллера).    
49. Что такое `@RestController`? Чем отличается от `@Controller` + `@ResponseBody`?    
50. Аннотации маппинга: `@RequestMapping`, `@GetMapping`, `@PostMapping` и т.д.    
51. Что такое `@PathVariable`, `@RequestParam`, `@RequestHeader`, `@CookieValue`?    
52. Как принять JSON в методе контроллера? (`@RequestBody`).    
53. Как вернуть JSON? (Автоматически через `HttpMessageConverter` и Jackson).    
54. Что такое `ResponseEntity`? Когда использовать?    
55. Что такое `@ModelAttribute`? Где применяется (формы, Thymeleaf, фильтры).    
56. Как обрабатывать исключения в REST контроллерах? (`@ExceptionHandler`, `@ControllerAdvice`).    
57. В чем разница между `@ControllerAdvice` и `@RestControllerAdvice`?    
58. Что такое `HandlerInterceptor`? Чем отличается от Filter (сервлетный)?    
59. Что такое `WebMvcConfigurer`? Зачем нужен (добавление интерсепторов, CORS, форматтеры).    
60. Как настроить CORS в Spring Boot? (`@CrossOrigin`).    

### Группа 6: [Spring Boot Core (Средне-высокая частота)](Группа_06-Spring_Boot_Core.md)
61. Что такое Spring Boot? В чем отличие от Spring Framework    
62. Что такое auto-configuration? Как она работает? (`@EnableAutoConfiguration`, `spring.factories`).    
63. Что делает `@SpringBootApplication` (составная аннотация)?    
64. Что такое `SpringBootApplication` и какие три аннотации в нее входят?    
65. Как отключить конкретную auto-configuration? (`@EnableAutoConfiguration(exclude=...)`).    
66. Что такое `SpringApplicationBuilder`?    
67. Как работает встроенный сервер (Tomcat / Jetty / Undertow)?    
68. Как заменить Tomcat на Jetty или Undertow?    
69. Что такое `Spring Boot Starter`? Какие стартеры вы знаете? (`starter-web`, `starter-data-jpa`, `starter-test`).    
70. Что такое `@SpringBootTest`? Как он поднимает контекст?    
71. Как запустить Spring Boot приложение без веб-сервера? (`WebApplicationType.NONE`).    
72. Что такое Actuator? Какие эндпоинты есть? (`/health`, `/info`, `/metrics`, `/env`).    
73. Как добавить свой эндпоинт в Actuator? (`@Endpoint`).    

### Группа 7: [Spring Data и Работа с БД (Очень высокая частота)](Группа_07-Spring_Data_&_БД.md)
74. Что такое Spring Data JPA? Чем отличается от Hibernate?    
75. Что такое `JpaRepository`? Чем отличается от `CrudRepository` и `PagingAndSortingRepository`?    
76. Как работают derived queries (методы `findByName`)? Парсинг имен методов.    
77. Что такое `@Query`? Нативная или JPQL?    
78. Что такое `@Modifying`? Зачем нужен (`clearAutomatically`, `flushAutomatically`)?    
79. Проблема `LazyInitializationException`. Как решить? (`@Transactional`, JOIN FETCH, `EntityGraph`).    
80. Что такое `@EntityGraph`? Когда использовать?    
81. Что такое `@Transactional` на методах репозитория? (Обычно `@Transactional(readOnly=true)` на классе).    
82. Что такое `Pessimistic` и `Optimistic` locking в Spring Data? (`@Version` для оптимистичной блокировки).    
83. Паттерн Specification (Spring Data JPA Criteria API).    
84. Что такое `Pageable` и `Page<T>`? Как работает пагинация в БД?    
85. Как логировать SQL запросы со значениями параметров?    

### Группа 8: [Безопасность (Spring Security) — Для Middle часто базово](Группа_08-Spring_Security.md)
86. Что такое Spring Security? Основные компоненты (SecurityContext, Authentication, Principal).    
87. Что такое `SecurityFilterChain`? Как работают фильтры?    
88. Что такое `@PreAuthorize`, `@PostAuthorize`, `@Secured`?    
89. Что такое `UserDetailsService`?    
90. Как работает Basic Auth и JWT в Spring Security?    
91. Что такое `PasswordEncoder`? (`BCryptPasswordEncoder`).    
92. Что такое CSRF? Когда его надо отключать (REST API)?    

### Группа 9: [Тестирование (Важно для Middle)](Группа_09-Тестирование.md)
93. Какие типы тестов поддерживает Spring Boot? (`@SpringBootTest`, `@WebMvcTest`, `@DataJpaTest`).    
94. Чем отличается `@MockBean` от `@Mock`? (Spring context vs plain Mockito).    
95. Как протестировать слой Controller (`@WebMvcTest` + `MockMvc`)?    
96. Как протестировать слой Repository (`@DataJpaTest`, `TestEntityManager`, транзакционный откат)?    
97. Что такое `@TestConfiguration`?    
98. Как протестировать асинхронные методы или таймеры?    

### Группа 10: [Асинхронность, События, Задачи (Средняя частота)](Группа_10-Асинхронность_События_Задачи.md)
99. Что такое `@Async`? Как включить (`@EnableAsync`).    
100. Как работает `TaskExecutor` в Spring? Пулы потоков по умолчанию.    
101. Что такое `@Scheduled`? Как включить (`@EnableScheduling`).    
102. Поддерживаемые cron-выражения в Spring? А также `fixedDelay` vs `fixedRate`.
103. Что такое `ApplicationEventPublisher` и `ApplicationListener`? (Синхронные события по умолчанию).    
104. Как сделать асинхронные события? (`@Async` + `ApplicationEventPublisher`).    

### Группа 11: [Внутренности и продвинутые темы (Ниже средней, но для сильного Middle)](Группа_11-продвинутые_темы.md)
105. Как работает внедрение зависимостей в `@Bean` методах одной конфигурации? (CGLIB прокси перехватывает вызовы).    
106. Что такое `BeanPostProcessor` и `BeanFactoryPostProcessor`? Разница и порядок выполнения.    
107. Что такое `ImportSelector` и `ImportBeanDefinitionRegistrar`? (Для написания стартеров).    
108. Что такое `Conditional` аннотации? (`@ConditionalOnMissingBean`, `@ConditionalOnProperty`, `@ConditionalOnClass`).    
109. Как написать свой собственный Spring Boot Starter?    
110. Что такое `ApplicationRunner` и `CommandLineRunner`? Когда выполняются?    
111. Как Spring Boot обрабатывает `@Value` с выражениями (`${...}`) и SpEL (`#{...}`)? 
112. Что такое `DeferredResult` или `Callable` в Spring MVC? (Асинхронные запросы, освобождение tomcat-потока).    
113. Как работает управление кешами (`@Cacheable`, `@CacheEvict`, `@EnableCaching`)?

### Группа 12: [«Странные вопросы» на знание нюансов (Редко, но метко)](Группа_12-Странные_вопросы.md)
114. Почему `@Autowired` нельзя использовать со статическими полями?    
115. Что произойдет, если заинжектить бин с `prototype` скоупом в `singleton` без прокси?    
116. Можно ли заменить реализацию бина во время выполнения приложения? (Нет, контекст immutable после refresh).    
117. Как получить все бины определенного интерфейса? (`List<MyInterface>` или `Map<String, MyInterface>`).    
118. Что такое `@Lookup`? (Для инжекта прототипов в синглтоны).    
119. Как работает `@RefreshScope` в Spring Cloud? (Уничтожение и пересоздание бина).

### Бонус: [Spring Boot 3.x и Spring Framework 6 (Новые темы)](Группа_13-Spring_Boot_3x_и_Spring_Framework_6.md)
120. Что изменилось в Spring Boot 3? (Java 17 baseline, Jakarta EE 9+ вместо javax).    
121. Что такое AOT (Ahead-Of-Time) компиляция и GraalVM Native Image поддержка?

---
### Главные советы для собеседования по Spring:

1. **Жизненный цикл бина** — учите как "Отче наш". Это база.    
2. **Self-invocation и AOP/Транзакции** — самый частый подвох в коде. Будьте готовы объяснить, почему `this.method()` не работает.    
3. **Конструкторная инъекция** — защищайте её как лучшее решение (тестируемость, immutability).    
4. **Разница `@Component` и `@Bean`** — первое для своих классов, второе для чужих или сложной инициализации.    
5. **Не учите всё подряд.** Сфокусируйтесь на IoC, AOP, Транзакциях, MVC и Boot AutoConfiguration. Остальное (Security, Data глубоко) — по потребностям вакансии.

---
