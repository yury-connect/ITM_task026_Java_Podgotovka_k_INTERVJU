# `@Configuration` и `@Component`

---
`@Configuration` → Spring создаёт **CGLIB-прокси** вашего класса, 
который перехватывает вызовы `@Bean`-методов 
и заменяет их обращением к контейнеру Spring.

`@Component` → Spring создаёт **обычный объект** вашего класса без всякого перехвата.

---
## Подробное объяснение

### Ситуация 1: `@Configuration` – с проксированием
```java
@Configuration
public class AppConfig {
    
    @Bean
    public BeanB beanB() {
        return new BeanB();
    }
    
    @Bean
    public BeanA beanA() {
        System.out.println("Creating BeanA...");
        BeanB b = beanB();  // Вызов метода
        return new BeanA(b);
    }
}
```

Что делает Spring:
1. Видит аннотацию `@Configuration`    
2. **Создаёт CGLIB-прокси** класса `AppConfig`    
3. Этот прокси наследуется от `AppConfig` и переопределяет все `@Bean`-методы    
4. В переопределённых методах вместо прямого создания объекта идёт обращение к контейнеру    

**Примерно так выглядит сгенерированный прокси (упрощённо):**
```java
public class AppConfig$$Proxy extends AppConfig {
    private BeanB cachedBeanB;
    
    @Override
    public BeanB beanB() {
        if (cachedBeanB == null) {
            // Первый вызов - создаём и кешируем
            cachedBeanB = super.beanB();  // Вызов оригинального метода
        }
        return cachedBeanB;  // Дальше всегда возвращаем кешированный
    }
    
    @Override
    public BeanA beanA() {
        // Когда вызывают beanA(), прокси перехватывает вызов beanB()
        // и возвращает синглтон из контейнера
        BeanB b = beanB();  // Вызывается переопределённый метод!
        return new BeanA(b);
    }
}
```

### Ситуация 2: `@Component` – без проксирования
```java
@Component  // или @Service, @Repository и т.д.
public class AppConfig {
    
    @Bean
    public BeanB beanB() {
        return new BeanB();
    }
    
    @Bean
    public BeanA beanA() {
        System.out.println("Creating BeanA...");
        BeanB b = beanB();  // Вызов метода
        return new BeanA(b);
    }
}
```

Что делает Spring:
1. Видит `@Component` – создаёт **обычный объект** без прокси    
2. Класс создаётся через `new AppConfig()`    
3. Все вызовы `beanB()` идут напрямую в оригинальный метод    

**Результат:**
- При создании бина `beanA` вызывается оригинальный `beanB()`    
- Оригинальный метод всегда делает `new BeanB()`    
- Даже если Spring отдельно создаст бин `beanB`, метод `beanA()` об этом не знает    

---
## Эксперимент, чтобы убедиться
```java
@Configuration
public class ConfigVsComponent {
    
    @Bean
    public String value() {
        System.out.println("Creating value");
        return "singleton";
    }
    
    @Bean
    public String consumer() {
        System.out.println("consumer() called");
        String v = value();  // Вызов метода
        return "consumer uses: " + v;
    }
}

@Component
public class ComponentConfig {
    
    @Bean
    public String value() {
        System.out.println("Creating value in Component");
        return "component-singleton";
    }
    
    @Bean
    public String consumer() {
        System.out.println("consumer() called in Component");
        String v = value();  // Вызов метода
        return "consumer uses: " + v;
    }
}
```

**Вывод в логе:**
```text
// Для @Configuration:
consumer() called
Creating value          ← value() создаётся только 1 раз
// value-бин уже в контейнере, но proxy его создал при вызове

// Для @Component:
consumer() called in Component
Creating value in Component  ← создаётся НОВЫЙ объект
Creating value in Component  ← И ЕЩЁ РАЗ для самого бина value
                               (для контейнера Spring тоже)
```

---
## Почему Spring сделал так?

1. **Синглтон-контракт** – в `@Configuration` гарантируется, что `@Bean`-методы возвращают управляемые Spring-синглтоны
    
2. **Внедрение зависимостей** – если бин A зависит от бина B, Spring должен подставить тот же B, что и везде
    
3. **Производительность** – проксирование нужно только для `@Configuration`. Обычные `@Component` не должны иметь накладных расходов    

---
## Итоговая таблица

|Характеристика|`@Configuration`|`@Component`|
|---|---|---|
|Проксирование|✅ CGLIB-прокси|❌ Нет|
|Вызов `@Bean`-метода внутри|Возвращает бин из контейнера|Создаёт новый объект|
|Целевое использование|Классы с конфигурацией, `@Bean`-методами|Обычные компоненты (сервисы, репозитории)|
|Можно ли использовать `@Bean` внутри|✅ Да, стандартный кейс|⚠️ Технически да, но с ошибками|

**Правило большого пальца:**  
Для конфигурационных классов используйте `@Configuration`. 
Для всего остального – `@Component`, `@Service`, `@Repository`.

---
