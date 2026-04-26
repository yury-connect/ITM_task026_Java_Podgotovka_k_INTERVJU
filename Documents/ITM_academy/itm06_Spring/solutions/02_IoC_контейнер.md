# Что такое `IoC` контейнер?

---
# 🔄 Что такое `IoC`-контейнер в `Spring`?

**IoC-контейнер** — это ядро _Spring_, которое управляет созданием, конфигурацией и жизненным циклом объектов (📦 _beans_) на основе метаданных (_**аннотаций** или `XML`_).

## ⚙️ Основные интерфейсы:

| Интерфейс            | Назначение                                                                                                                                                                                                                                                                                                                            |
| :------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `BeanFactory`        | Базовый интерфейс **IoC**-контейнера. <br>Управляет созданием и получением бинов.                                                                                                                                                                                                                                                     |
| `ApplicationContext` | `ApplicationContext` — это "продвинутая" <br>версия `BeanFactory`, которая добавляет:  <br>1. **Удобные способы конфигурации** (аннотации, Java Config). <br>2. **Enterprise-функции** (AOP, события, i18n).  <br>3. **Интеграции с веб-средой**.  <br>4. **Автоматизацию** (регистрация BeanPostProcessor, <br>обработка аннотаций). |

## 📋 Сравнение `BeanFactory` и `ApplicationContext`

| Функция                        | BeanFactory | ApplicationContext |
| :----------------------------- | :---------: | :----------------: |
| Создание бинов                 |    ✅ Да     |        ✅ Да        |
| Внедрение зависимостей         |    ✅ Да     |        ✅ Да        |
| Аннотации (`@Autowired`)       |    ❌ Нет    |        ✅ Да        |
| `@PostConstruct`/`@PreDestroy` |    ❌ Нет    |        ✅ Да        |
| AOP                            |    ❌ Нет    |        ✅ Да        |
| Сканирование компонентов       |    ❌ Нет    |        ✅ Да        |
| Интернационализация            |    ❌ Нет    |        ✅ Да        |
| Публикация событий             |    ❌ Нет    |        ✅ Да        |
| Доступ к ресурсам              |    ❌ Нет    |        ✅ Да        |
| Веб-интеграция                 |    ❌ Нет    |        ✅ Да        |

---
Пояснения с примерами по каждому пункту 🔽
### 1. Создание бинов ✅
**BeanFactory**: Создаёт бины по запросу (`lazy initialization`) или при старте <br>(_если явно указано_). Поддерживает базовую конфигурацию через XML или Java.

**ApplicationContext**: Унаследовал эту функцию, но автоматически создаёт все singleton-бины при старте контейнера (`eager initialization`), что ускоряет доступ к ним.
```java
// XML-конфигурация для BeanFactory
<bean id="myBean" class="com.example.MyBean" />

// Использование BeanFactory
BeanFactory factory = new XmlBeanFactory(new ClassPathResource("beans.xml"));
MyBean bean = factory.getBean("myBean", MyBean.class);

// ApplicationContext (автоматическая инициализация singleton)
ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
MyBean bean = context.getBean("myBean", MyBean.class);
```

**Улучшение**: `ApplicationContext` упрощает работу, автоматически инициализируя бины, что удобно для большинства приложений.

---
### 2. Внедрение зависимостей ✅
**BeanFactory**: Поддерживает базовое внедрение зависимостей <br>(через setter или конструктор) в XML-конфигурации.

**ApplicationContext**: Унаследовал эту функцию, <br>но добавляет поддержку аннотаций и автосвязывания.
```java
// BeanFactory: XML-конфигурация
<bean id="service" class="com.example.MyService">
    <property name="dao" ref="dao" />
</bean>
<bean id="dao" class="com.example.MyDao" />

// ApplicationContext: То же самое, но поддерживает аннотации (см. ниже)
```

**Улучшение**: `ApplicationContext` делает внедрение более гибким за счёт аннотаций, но базовая функциональность идентична.

---
### 3. Аннотации (`@Autowired`) 🆕
**BeanFactory**: Не поддерживает аннотации, такие как `@Autowired`, `@Qualifier`, `@Value`. Требуется явная конфигурация в XML.

**ApplicationContext**: Полностью поддерживает аннотации для автоматического внедрения зависимостей, что упрощает конфигурацию.
```java
@Service
public class MyService {
    @Autowired
    private MyDao dao; // Автоматическое внедрение
}

// Конфигурация для ApplicationContext
@Configuration
@ComponentScan("com.example")
public class AppConfig {}

// Использование
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
MyService service = context.getBean(MyService.class);
```
**Улучшение**: Аннотации в `ApplicationContext` делают код чище, устраняя необходимость в громоздких XML-файлах.

---
### 4. `@PostConstruct`/`@PreDestroy` 🆕
**BeanFactory**: Не поддерживает методы жизненного цикла, помеченные `@PostConstruct` (_инициализация_) или `@PreDestroy` (_уничтожение_).

**ApplicationContext**: Вызывает методы, аннотированные `@PostConstruct` после создания бина и `@PreDestroy` перед его уничтожением.
```java
@Service
public class MyService {
    @PostConstruct
    public void init() {
        System.out.println("Инициализация бина");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Уничтожение бина");
    }
}

// Конфигурация
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
// init() вызывается после создания, destroy() — при закрытии контекста
context.close();
```
**Улучшение**: Поддержка жизненного цикла упрощает настройку и очистку ресурсов, таких как соединения с БД.

---
### 5. AOP 🆕
**BeanFactory**: Не поддерживает аспектно-ориентированное программирование (AOP) из коробки.

**ApplicationContext**: Интегрирует Spring AOP, позволяя добавлять сквозную функциональность (логирование, транзакции, безопасность) через **аспекты**.
```java
@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.example.MyService.*(..))")
    public void logBefore() {
        System.out.println("Вызов метода");
    }
}

// Конфигурация
@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.example")
public class AppConfig {}

// Использование
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
MyService service = context.getBean(MyService.class);
service.doSomething(); // Выводит "Вызов метода" перед выполнением
```
**Улучшение**: AOP в ApplicationContext позволяет разделять сквозную логику, делая код модульным и поддерживаемым.

---
### 6. Сканирование компонентов 🆕
**BeanFactory**: Требует явного определения всех бинов в XML или Java-конфигурации.

**ApplicationContext**: Поддерживает автоматическое сканирование компонентов 
с аннотациями @Component, @Service, @Repository, @Controller.
```java
@Service
public class MyService {
    // Логика сервиса
}

// Конфигурация
@Configuration
@ComponentScan("com.example")
public class AppConfig {}

// Использование
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
MyService service = context.getBean(MyService.class);
```
**Улучшение**: Автосканирование уменьшает объём конфигурации, автоматически регистрируя бины.

---
### 7. Интернационализация 🆕
**BeanFactory**: Не поддерживает интернационализацию (i18n).

**ApplicationContext**: Предоставляет интерфейс MessageSource для работы с локализованными сообщениями.
```java
// Файл messages_en.properties
greeting=Hello, {0}!

// Файл messages_ru.properties
greeting=Привет, {0}!

// Конфигурация
@Configuration
public class AppConfig {
    @Bean
    public Message/Source messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages");
        return source;
    }
}

// Использование
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
String message = context.getMessage("greeting", new Object[]{"User"}, Locale.forLanguageTag("ru"));
System.out.println(message); // Привет, User!
```
**Улучшение**: Интернационализация позволяет легко поддерживать многоязычные приложения.

---
### 8. Публикация событий 🆕
**BeanFactory**: Не поддерживает механизм событий.

**ApplicationContext**: Реализует паттерн "Издатель-Подписчик" через интерфейсы `ApplicationEvent` и `ApplicationListener`.  
`ApplicationContext` выступает как диспетчер событий, публикуя их и уведомляя подписчиков.

Как это работает:
- **Публикация**: Бины могут публиковать события через `ApplicationEventPublisher` (встроен в `ApplicationContext`).
- **Обработка**: Слушатели (`ApplicationListener`) или методы с `@EventListener` обрабатывают события.
- **Асинхронность**: Поддерживается асинхронная обработка через `@Async`.

```java
// Событие
public class CustomEvent extends ApplicationEvent {
    private final String message;
	
    public CustomEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
	
    public String getMessage() {
        return message;
    }
}

// Слушатель
@Component
public class CustomEventListener {
    @EventListener
    public void handleEvent(CustomEvent event) {
        System.out.println("Событие получено: " + event.getMessage());
    }
}

// Публикация события
@Service
public class MyService implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher publisher;
	
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
	
    public void doSomething() {
        publisher.publishEvent(new CustomEvent(this, "Действие выполнено!"));
    }
}

// Конфигурация
@Configuration
@ComponentScan("com.example")
public class AppConfig {}

// Использование
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
MyService service = context.getBean(MyService.class);
service.doSomething(); // Вывод: Событие получено: Действие выполнено!
```
**Улучшение**: Механизм событий позволяет реализовать слабосвязанную архитектуру, где компоненты обмениваются сообщениями без прямых зависимостей.

---
### 9. Доступ к ресурсам 🆕
**BeanFactory**: Не предоставляет удобного доступа к ресурсам (_файлы, URL, classpath_).

**ApplicationContext**: Реализует интерфейс `ResourceLoader`, позволяя загружать ресурсы через `Resource`.
```java
ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
Resource resource = context.getResource("classpath:config.properties");
try (InputStream is = resource.getInputStream()) {
    Properties props = new Properties();
    props.load(is);
    System.out.println(props.getProperty("key"));
}
```
**Улучшение**: Упрощённый доступ к файлам, конфигурациям и другим ресурсам.

---
### 10. Веб-интеграция 🆕
**BeanFactory**: Не поддерживает интеграцию с веб-приложениями.

**ApplicationContext**: Предоставляет WebApplicationContext для работы с веб-приложениями, поддерживая MVC, REST и контекст сервлета.
```java
// Конфигурация веб-приложения
public class WebConfig implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);
        servletContext.addListener(new ContextLoaderListener(context));
    }
}

// REST-контроллер
@RestController
public class MyController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Web!";
    }
}
```
**Улучшение**: WebApplicationContext упрощает создание веб-приложений, интегрируя Spring с сервлетами и MVC.

---
### Итоги 🎯
**ApplicationContext** значительно расширяет возможности `BeanFactory`, добавляя современные функции, которые упрощают разработку и делают код более декларативным.  

#### Основные улучшения:
- **Автоматизация**: Аннотации, автосканирование и eager-инициализация.
- **Гибкость**: Поддержка AOP, событий, интернационализации и веб-интеграции.
- **Удобство**: Доступ к ресурсам и управление жизненным циклом бинов.
- **Слабая связанность**: Публикация событий для асинхронного взаимодействия.

#### Когда использовать:
- **BeanFactory**: Для минималистичных приложений с ручной конфигурацией.
- **ApplicationContext**: Для большинства современных приложений, особенно веб и enterprise.

---
## 📌 Что делает `IoC`-контейнер:
- 🛠️ Создаёт объекты (_бины_)
- 🔗 Связывает зависимости (_внедрение_)
- ⚙️ Конфигурирует компоненты
- 🔄 Управляет жизненным циклом (_инициализация → работа → уничтожение_)

## 🗂️ Конфигурация контейнера:
- 💡 Аннотации: `@Component`, `@Service`, `@Autowired`, и т.п.
- 📄 `XML`-файлы (_олдскул, но всё ещё поддерживаются_)
- 🧬 Java-конфигурации (`@Configuration`, `@Bean`)

## 📦 Объекты, управляемые контейнером:
Называются `beans` — это любые классы, объявленные и зарегистрированные в контейнере.

---
###### _"Контейнер — это как личный дворецкий для твоих объектов. <br>Всё под контролем, всё по расписанию."_

---
#### 📝 Материал из методички 🔽
```
***** из методички *****
В среде Spring IoC-контейнер представлен интерфейсом ApplicationContext, который является оберткой над BeanFactory, предоставляющей дополнительные возможности, например AOP и транзакции. 

Интерфейс BeanFactory предоставляет фабрику для бинов, которая в то же время и является IoC-контейнером приложения. 
Управление бинами основано на конфигурации (аннотации или xml). 
Контейнер создает бъекты на основе конфигураций и управляет их жизненным циклом от создания объекта до уничтожения.

Контейнер отвечает за управление жизненным циклом объекта: создание объектов, вызов методов инициализации и конфигурирование объектов путём связывания их между собой.
Объекты, создаваемые контейнером, называются beans. 
Конфигурирование контейнера осуществляется путём внедрения аннотаций, но также, есть возможность, по старинке, загрузить XML-файлы, содержащие определение bean’ов и предоставляющие информацию, необходимую для создания bean’ов.
```

---