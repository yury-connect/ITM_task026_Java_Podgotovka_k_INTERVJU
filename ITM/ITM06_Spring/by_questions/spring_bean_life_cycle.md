# Жизненный цикл бина в Spring 

###### [_home_](https://habr.com/ru/articles/893614/)

---



<details>
        <summary>🟢 Этап 1. Создание бина (Instantiation) 🔽</summary>

---
Spring создает объект бина с помощью конструктора, но пока не внедряет в него зависимости.

📌 **Пример из жизни**: Вы купили кофеварку, но еще не подключили ее к сети и не залили воду.

---
</details>



<details>
        <summary>🟢 Этап 2. Внедрение зависимостей (Dependency Injection) 🔽</summary>

---
Spring автоматически добавляет в бин все его зависимости.

📌 **Аналогия**: Вы подключили кофеварку к сети и залили воду.

**Способы внедрения зависимостей:**

* **Через конструктор** – рекомендуется для обязательных зависимостей, 
в комбинации с модификатором final обеспечивает неизменяемость объекта.  


* **Через поле - `@Autowired`** можно ставить на поле, и Spring внедрит зависимость. 
Однако этот способ не рекомендуется, так как он усложняет тестирование и делает зависимость менее явной.  


* **Через сеттеры** – используется, если зависимость может изменяться после создания бина.  


* **Через аннотации `@Value`, `@Qualifier` и др.** – применяется для внедрения простых значений 
и указания конкретного бина при наличии нескольких кандидатов.  

---
</details>



<details>
        <summary>🟢 Этап 3. Aware-интерфейсы 🔽</summary>

---
Если бин реализует BeanNameAware, BeanFactoryAware, ApplicationContextAware, Spring передает ему информацию о контексте.

📌 **Пример из жизни**: Кофеварка проверяет напряжение в сети перед включением.

* BeanNameAware — получает **имя бина**


* BeanFactoryAware — доступ к **BeanFactory**


* ApplicationContextAware — доступ ко **всему контексту Spring**

> ⚠️ Используйте их с осторожностью, так как это привязывает код к _Spring API_ и усложняет тестирование.

---
</details>



<details>
        <summary>🟢 Этап 4. BeanPostProcessor до инициализации 🔽</summary>

---
Spring вызывает `postProcessBeforeInitialization` у всех `BeanPostProcessor`.

**Примеры использования**:

* **Валидация бинов** – проверка корректности данных перед их использованием.


* **Изменение свойств** – настройка конфигурации бина в зависимости от профиля приложения (@Profile).


* **Логирование** – запись информации о бине перед его инициализацией.

---
</details>



<details>
        <summary>🟢 Этап 5. Инициализация (init) 🔽</summary>

---
Spring выполняет методы:

* `@PostConstruct`


* `InitializingBean#afterPropertiesSet()`


* Метод, указанный в `@Bean(initMethod = "init")`

📌 **Пример**: Кофеварка прогревает воду перед приготовлением кофе.

> Здесь можно проверять настройки бина и подключаться к ресурсам _(например, БД)_.

---
</details>



<details>
        <summary>🟢 Этап 6. BeanPostProcessor после инициализации 🔽</summary>

---
Spring вызывает postProcessAfterInitialization у BeanPostProcessor.

**Где применяется:**

* **Проксирование бинов** – создание AOP-прокси для аннотаций @Transactional, @Async и подобных.


* **Кэширование** – автоматическое добавление механизма кэширования для методов.


* **Изменение бинов** – динамическое добавление новых методов или обертывание логикой безопасности.

⚠️ При использовании проксирования бин может подменяться Spring-оберткой (**важно при `instanceof`**).

---
</details>



<details>
        <summary>🟢 Этап 7. Уничтожение бина (destroy) 🔽</summary>

---
Spring вызывает:

* `@PreDestroy`  


* `DisposableBean#destroy()`  


* Метод, указанный в `@Bean(destroyMethod = "cleanup")`  

📌 **Пример**: Вы выключаете кофеварку, сливаете воду, чистите фильтр и убираете ее.  

⚠️ Для prototype-бинов этот этап не вызывается — их уничтожение нужно обрабатывать вручную.  

---
</details>



<details>
        <summary>📌 Пример кода 🔽</summary>

---
#### 📌 Код

```java
// Главный бин с логами всех этапов
@Component
class MyBean implements BeanNameAware, ApplicationContextAware, InitializingBean, DisposableBean {
    
    private String beanName;
    private ApplicationContext context;

    public MyBean() {
        System.out.println("1. Конструктор MyBean вызван (Instantiation)");
    }

    @Autowired
    public void setDependency(MyDependency dependency) {
        System.out.println("2. Зависимость MyDependency внедрена (DI)");
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
        System.out.println("3. BeanNameAware: Имя бина - " + name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        System.out.println("3. ApplicationContextAware: Контекст передан");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("5. @PostConstruct: Бин проинициализирован");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("5. InitializingBean: Бин завершил инициализацию");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("7. @PreDestroy: Перед уничтожением бина");
    }

    @Override
    public void destroy() {
        System.out.println("7. DisposableBean: Бин уничтожен");
    }
}

// Дополнительный бин для DI
@Component
class MyDependency {
    public MyDependency() {
        System.out.println("1. Конструктор MyDependency вызван (Instantiation)");
    }
}

// BeanPostProcessor для логирования этапов postProcessBeforeInitialization и postProcessAfterInitialization
@Component
class MyBeanPostProcessor implements BeanPostProcessor {
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof MyBean) {
            System.out.println("4. BeanPostProcessor: Before Init - " + beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof MyBean) {
            System.out.println("6. BeanPostProcessor: After Init - " + beanName);
        }
        return bean;
    }
}

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
``` 

### 📌 Вывод в консоль

```textmate
1. Конструктор MyDependency вызван (Instantiation)
1. Конструктор MyBean вызван (Instantiation)
2. Зависимость MyDependency внедрена (DI)
3. BeanNameAware: Имя бина - myBean
3. ApplicationContextAware: Контекст передан
4. BeanPostProcessor: Before Init - myBean
5. @PostConstruct: Бин проинициализирован
5. InitializingBean: Бин завершил инициализацию
6. BeanPostProcessor: After Init - myBean
>>> Контекст запущен

>>> Закрытие контекста
7. @PreDestroy: Перед уничтожением бина
7. DisposableBean: Бин уничтожен
```


---
</details>







<details>
        <summary>📝 Материал из методички 🔽</summary>


</details>

---
###### __

---

[🔙 _к списку вопросов по теме_ **Spring** 🔙](/ITM/ITM06_Spring/Spring.md)
