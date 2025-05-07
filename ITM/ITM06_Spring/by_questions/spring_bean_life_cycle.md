# Жизненный цикл бина в Spring 

###### [_home_](https://habr.com/ru/articles/893614/)

---

![Жизненный цикл бина](/ITM/ITM06_Spring/imgs/2025-04-17_23-25-36.png)

<details>
        <summary>🟢 Этап 1. Парсинг конфигурации и создание BeanDefinition 🔽</summary>

---
Перед созданием бинов Spring загружает их метаданные в виде `BeanDefinition`.  

### Способы конфигурации:
* **XML**: `ClassPathXmlApplicationContext("context.xml")`


* **Аннотации**:
  * **Сканирование пакетов**: `AnnotationConfigApplicationContext("package.name")`
  
  * '**JavaConfig**': Указание `@Configuration`-**классов** (_или массива классов_): 
`@Configuration`, `@Bean`, `@Component`, `AnnotationConfigApplicationContext(JavaConfig.class)`
  
  * **Groovy конфигурация**: `GenericGroovyApplicationContext("context.groovy")`


### Как это работает:
* Для аннотаций Spring использует два `private final` поля 
(_мы их увидим, если заглянем внутрь `AnnotationConfigApplicationContext`_):
  * `ClassPathBeanDefinitionScanner` — сканирует пакеты на наличие `@Component` (_или её алиаса_).
    > Найденные классы _парсируются_ и для них создаются `BeanDefinition`.  
    Чтобы было запущено сканирование, в конфигурации должен быть указан пакет 
    для сканирования `@ComponentScan({"package.name"})`
  * `AnnotatedBeanDefinitionReader` — обрабатывает `@Configuration`-классы и `@Bean`-методы.
    > `AnnotatedBeanDefinitionReader` работает в несколько этапов: 
    > 1. **Первый этап** — это регистрация всех `@Configuration` для дальнейшего парсирования.  
     Если в конфигурации используются `Conditional`, то будут зарегистрированы 
     только те конфигурации, для которых `Condition` вернет `true`.  
    > `BeanDefinition` — это специальный интерфейс, через который можно получить доступ к метаданным будущего бина. 
     В зависимости от того, какая у вас конфигурация, будет использоваться  
     тот или иной механизм парсирования конфигурации.
    > >
    > 2. **Второй этап** — это регистрация `BeanDefinitionRegistryPostProcessor`, который при помощи 
     класса `ConfigurationClassPostProcessor` парсирует `JavaConfig` и создает `BeanDefinition`.
    > >

**`BeanDefinition`** — это набор метаданных будущего бина, макет, 
по которому нужно будет создавать бин в случае необходимости.  
То есть для каждого бина создается свой объект `BeanDefinition`, 
в котором хранится описание того, как создавать и управлять этим конкретным бином.  
Проще говоря, **сколько бинов** в программе - **столько и объектов** `BeanDefinition`, их описывающих. 
> Сюда входит: 
> * из какого класса бин надо создать, scope, 
> * установлена ли ленивая инициализация, 
> * нужно ли перед данным бином инициализировать другой, init и destroy методы, 
> * зависимости.  
> Все полученные `BeanDefinition`’ы складываются в `ConcurrentHashMap`, в которой **ключём** является имя бина, 
> а **объект** - сам `BeanDefinition`.  
> При старте приложения, в `IoC` контейнер попадут бины, которые имеют `scope Singleton` 
> (_устанавливается по-умолчанию_), остальные же создаются, тогда когда они нужны.

* **Результат**: `ConcurrentHashMap<String, BeanDefinition>`, где **ключ** — имя бина, **значение** — его метаданные.

> Контейнер анализирует классы, находит бины и создает `BeanDefinition`.

### Что хранится в `BeanDefinition`:
* Класс бина, 
* scope (`singleton`, `prototype` _и др._), 
* ленивая инициализация (`lazy-init`), 
* зависимости, 
* init/destroy-методы.

---
</details>


<details>
        <summary>🟢 Этап 2. Настройка BeanDefinition (BeanFactoryPostProcessor) 🔽</summary>

---
Есть возможность повлиять на бины до их создания, иначе говоря мы имеем доступ к метаданным класса.  

⚠️ Для этого существует специальный интерфейс **BeanFactoryPostProcessor**, реализовав который, 
мы получаем доступ к созданным `BeanDefinition` и можем их **изменять**.

В нем единственный метод -  
`postProcessBeanFactory` принимает параметром `ConfigurableListableBeanFactory`.  
Данная фабрика содержит много полезных методов, в том числе `getBeanDefinitionNames`, 
через который мы можем получить все `BeanDefinitionNames`, а уже потом по конкретному имени 
получить `BeanDefinition` для дальнейшей обработки метаданных.

Разберем одну из родных реализаций интерфейса `BeanFactoryPostProcessor`.  
Обычно, настройки подключения к базе данных выносятся в отдельный `property` файл, потом при помощи 
`PropertySourcesPlaceholderConfigurer` они загружаются и делается `inject` этих значений в нужное поле. 
Так как `inject` делается **по ключу**, то до создания экземпляра бина нужно заменить этот ключ 
на само значение из `property` файла. 
Эта замена происходит в классе, который реализует интерфейс `BeanFactoryPostProcessor`. 
Название этого класса — `PropertySourcesPlaceholderConfigurer`. 
Он должен быть объявлен как `static` 




### Пример:

```java
@Bean
public static PropertySourcesPlaceholderConfigurer configurer() {
    return new PropertySourcesPlaceholderConfigurer();
}
```

```java
@Component
public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        BeanDefinition bd = beanFactory.getBeanDefinition("myBean");
        bd.setScope("prototype"); // Меняем scope на лету
    }
}
```

### Стандартные реализации:
* `PropertySourcesPlaceholderConfigurer` — подставляет значения из `.properties`-файлов в `@Value`.

---
</details>



<details>
        <summary>Создание кастомных FactoryBean (только для XML-конфигурации) 🔽</summary>

---
`FactoryBean` — это `generic` интерфейс, которому можно делегировать процесс создания бинов типа. 
В те времена, когда конфигурация была исключительно в xml, разработчикам был необходим механизм. 
с помощью которого они бы могли управлять процессом создания бинов. 
Именно для этого и был сделан этот интерфейс.

Создадим фабрику которая будет отвечать за создание всех бинов типа — _Color_.

```java
public class ColorFactory implements FactoryBean<Color> {
    @Override
    public Color getObject() throws Exception {
        Random random = new Random();
        Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        return color;
    }

    @Override 
    public Class<?> getObjectType() {
        return Color.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
``` 

Теперь создание бина типа `Color.class` будет делегироваться `ColorFactory`, 
у которого при каждом создании нового бина будет вызываться метод `getObject`.
Для тех кто пользуется `JavaConfig`, этот интерфейс будет **абсолютно бесполезен**.

---
</details>


<details>
        <summary>🟢 Этап 3. Создание экземпляров бинов 🔽</summary>

---
Spring создаёт бины на основе `BeanDefinition` в несколько этапов:

### 3.1. Создание инфраструктурных бинов
Сначала создаются все `BeanPostProcessor` (_например, для обработки_ _@Autowired_).
> т.е. Сначала `BeanFactory` из коллекции `Map` с объектами `BeanDefinition` достаёт те из них, 
> из которых создаёт все `BeanPostProcessor`-ы (_Инфраструктурные бины_), необходимые для настройки обычных бинов.  
> Создаются экземпляры бинов через `BeanFactory` на основе ранее созданных `BeanDefinition`.


### 3.2. Создание обычных бинов
> Созданием **экземпляров бинов** занимается `BeanFactory` на основе ранее созданных `BeanDefinition`.  
> Из `Map<BeanName, BeanDefinition>` получаем `Map<BeanName, Bean>`.  
> Создание бинов может делегироваться кастомным FactoryBean. О их создании читай выше.

* Для `singleton`: Бины создаются при **старте** контекста.


* Для `prototype`: При **каждом** вызове `getBean()`.


* Через `FactoryBean` (для _XML_):

```java
public class ColorFactory implements FactoryBean<Color> {
    @Override
    public Color getObject() {
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }
}
```

---
</details>



<details>
        <summary>🟢 Этап 4. Настройка созданных бинов (BeanPostProcessor) 🔽</summary>

---
После создания бина Spring применяет пост-обработку:

### 4.1. `Aware`-интерфейсы
Если бин реализует `Aware`-интерфейсы, Spring передаёт ему контекстные объекты:

* `BeanNameAware` — имя бина.


* `ApplicationContextAware` — доступ к контексту.

### 4.2. `BeanPostProcessor`
* `postProcessBeforeInitialization()`: Вызывается **до** инициализации.

  * Обрабатывает `@PostConstruct`, `@Autowired`.

* `postProcessAfterInitialization()`: Вызывается **после** инициализации.

  * Создаёт **AOP**-прокси (_для_ `@Transactional`, `@Cacheable`).

---
</details>



<details>
        <summary>🟢 Этап 5. Инициализация бина 🔽</summary>

---
### Порядок вызовов:

1. `@PostConstruct` (_JSR-250, рекомендуется_).


2. `InitializingBean.afterPropertiesSet()` (_устаревший способ_).


3. **Пользовательский** `init-method` (_указанный в `@Bean(initMethod = "...")` или XML_).

### Пример:
```java
@Component
public class MyBean {
    @PostConstruct
    public void init() {
        System.out.println("5.1. @PostConstruct");
    }
}
```

---
</details>



<details>
        <summary>🟢 Этап 6. Бин готов к использованию 🔽</summary>

---
После всех этапов бин попадает в контекст и **доступен** через:

```java
ApplicationContext.getBean("myBean");
```

---
</details>



<details>
        <summary>🟢 Этап 7. Уничтожение бина 🔽</summary>

---
При **закрытии контекста** (`context.close()`) вызываются:

1. `@PreDestroy` (_JSR-250_).


2. `DisposableBean.destroy()` (_устаревший способ_).


3. Пользовательский `destroy-method` (_аналогично `init-method`_).

### Пример:

```java
@Component
public class MyBean {
    @PreDestroy
    public void cleanup() {
        System.out.println("7.1. @PreDestroy");
    }
}
```

---
</details>



<details>
        <summary>⏱ 'Scope' бинов 🔽</summary>

---
| Scope            | 	Описание                                                          |
|:-----------------|:-------------------------------------------------------------------|
| `singleton`      | 	Один бин на весь контекст (_по умолчанию_).                       |
| `prototype`      | 	Новый экземпляр при каждом `getBean()`.                           |
| `request`        | 	Новый бин для каждого HTTP-запроса (_только для веб-приложений_). |
| `session`        | 	Бин живёт пока активна HTTP-сессия.                               |
| `global-session` | 	Аналог `session` для портлетов.                                   |

---
</details>

## Итоговая последовательность

1. **Парсинг конфигурации** → `BeanDefinition` (_XML/аннотации/Java Config_).
2. **Настройка** `BeanDefinition`:
   * Через `BeanFactoryPostProcessor` (_например,_ `PropertySourcesPlaceholderConfigurer`).
   * Динамическое добавление бинов через `BeanDefinitionRegistryPostProcessor`.
3. **Создание бинов**:
   * Сначала инфраструктурные (`BeanPostProcessor`), потом обычные.
   * Для `prototype` — при каждом `getBean()`.
4. **Внедрение зависимостей** (`@Autowired`).
5. `Aware`-интерфейсы (_BeanNameAware_, `ApplicationContextAware`).
6. `BeanPostProcessor.postProcessBeforeInitialization()`.
   * Обработка `@PostConstruct`, `@Autowired`.
7. **Инициализация**:
   * `@PostConstruct` → `InitializingBean.afterPropertiesSet()` → `init-method`.
8. `BeanPostProcessor.postProcessAfterInitialization()`
   * Создание AOP-прокси (_если требуется_).
9. **Бин готов**  (_доступен через_ `ApplicationContext.getBean()`).
10. Уничтожение (_только для_ `singleton`):
    * `@PreDestroy` → `DisposableBean.destroy()` → `destroy-method`.

---

<details>
        <summary>📌 Пример кода 🔽</summary>

---
### 📌 Полный пример жизненного цикла бина в Spring с комментариями и выводом

```java
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Демонстрация полного жизненного цикла бина в Spring.
 * Включает все ключевые этапы: от создания до уничтожения.
 */
@Configuration
public class FullLifecycleDemo {

    public static void main(String[] args) {
        // Создаем контекст (этап парсинга конфигурации)
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(FullLifecycleDemo.class);

        // Регистрируем кастомный BeanPostProcessor
        context.registerBean("customProcessor", CustomBeanPostProcessor.class);

        // Обновляем контекст (запускает создание бинов)
        context.refresh();

        // Получаем бин (этап использования)
        DemoBean bean = context.getBean(DemoBean.class);
        System.out.println("Бин готов к использованию: " + bean);

        // Закрываем контекст (этап уничтожения)
        context.close();
    }

    @Bean
    public DemoBean demoBean() {
        return new DemoBean();
    }

    @Bean
    public DependencyBean dependency() {
        return new DependencyBean();
    }
}

/**
 * Основной бин, реализующий ключевые интерфейсы жизненного цикла.
 */
@Component
class DemoBean implements BeanNameAware, BeanFactoryAware,
        ApplicationContextAware, InitializingBean, DisposableBean {

    private String name;

    public DemoBean() {
        System.out.println("1. Вызов конструктора (Instantiation)");
    }

    @Autowired
    public void setDependency(DependencyBean dependency) {
        System.out.println("2. Внедрение зависимости (Dependency Injection)");
    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
        System.out.println("3. BeanNameAware: имя бина - " + name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("3. BeanFactoryAware: фабрика бинов установлена");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("3. ApplicationContextAware: контекст установлен");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("5. @PostConstruct метод вызван");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("5. InitializingBean.afterPropertiesSet() вызван");
    }

    public void customInit() {
        System.out.println("5. Пользовательский init-method");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("7. @PreDestroy метод вызван");
    }

    @Override
    public void destroy() {
        System.out.println("7. DisposableBean.destroy() вызван");
    }

    public void customDestroy() {
        System.out.println("7. Пользовательский destroy-method");
    }
}

/**
 * Зависимый бин для демонстрации DI
 */
@Component
class DependencyBean {
    public DependencyBean() {
        System.out.println("1. Создание DependencyBean");
    }
}

/**
 * Кастомный BeanPostProcessor с логированием
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class CustomBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof DemoBean) {
            System.out.println("4. BeanPostProcessor.postProcessBeforeInitialization() для " + beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof DemoBean) {
            System.out.println("6. BeanPostProcessor.postProcessAfterInitialization() для " + beanName);
        }
        return bean;
    }
}
``` 

### 📌 Вывод в консоль

```textmate
1. Создание DependencyBean
1. Вызов конструктора (Instantiation)
2. Внедрение зависимости (Dependency Injection)
3. BeanNameAware: имя бина - demoBean
3. BeanFactoryAware: фабрика бинов установлена
3. ApplicationContextAware: контекст установлен
4. BeanPostProcessor.postProcessBeforeInitialization() для demoBean
5. @PostConstruct метод вызван
5. InitializingBean.afterPropertiesSet() вызван
5. Пользовательский init-method
6. BeanPostProcessor.postProcessAfterInitialization() для demoBean
Бин готов к использованию: DemoBean@12345
7. @PreDestroy метод вызван
7. DisposableBean.destroy() вызван
7. Пользовательский destroy-method
```


---
</details>







<details>
        <summary>📝 Материал из методички 🔽</summary>


</details>

---
###### __

---

[ссылка1](https://habr.com/ru/articles/893614/)

[🔙 _к списку вопросов по теме_ **Spring** 🔙](/ITM/ITM06_Spring/Spring.md)
