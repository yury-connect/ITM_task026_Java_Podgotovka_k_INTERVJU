## 1/20: What is/are typically case(s) where you usually need to manually instantiated an ApplicationContext?

- In a web application.    
- None of these.    
- In an integration test running with the SpringJUnit4ClassRunner.    
- In a standalone application started with a main method.    

---
## 1/20: В каких типичных случаях вам обычно требуется вручную создавать экземпляр (инстанцировать) ApplicationContext?

- В веб-приложении.    
- Ни один из этих вариантов.    
- В интеграционном тесте, запускаемом с помощью SpringJUnit4ClassRunner.    
- В автономном (standalone) приложении, запускаемом с помощью метода main.

---
Правильный ответ: **In a standalone application started with a main method** (четвертый вариант).

**Объяснение:**

`ApplicationContext` — это контейнер Spring IoC (Inversion of Control). В разных сценариях использования Spring этот контейнер создается разными способами:

1. **Веб-приложение (Spring MVC, Spring Boot):** Вам **не нужно** создавать контекст вручную. За это отвечает сервлет-контейнер (например, Tomcat) или механизмы автоконфигурации Spring Boot. Контекст инициализируется автоматически при старте приложения.
    
2. **Интеграционные тесты (`@SpringBootTest` или `SpringJUnit4ClassRunner`):** Вам **не нужно** создавать контекст вручную. Специальные раннеры (TestContext Framework) автоматически поднимают контекст, внедряют зависимости в тестовый класс и управляют его жизненным циклом.
    
3. **Standalone-приложение (обычная программа с методом `main`):** Если вы пишете простое консольное приложение или библиотеку, которая использует Spring для внедрения зависимостей (DI), но не запускается на сервере приложений и не использует тестовые фреймворки, **вам необходимо вручную** инициализировать `ApplicationContext` в методе `main`.
    
    - _Пример:_
```java
public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    MyService service = context.getBean(MyService.class);
    service.doSomething();
}
```

---
   