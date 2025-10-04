# Простейшим языком — что такое Spring Context 🟢

Spring Context (или _Spring container_) — это «умный шкаф» для ваших Java-объектов (бинов).  
Когда говорят _«поднимается Spring context»_ — имеют в виду: Spring прочитал конфигурацию (`@Configuration`, `@ComponentScan`, XML и т.д.), зарегистрировал описания бинов и создал (инициализировал) нужные объекты, подготовил их зависимости и сделал их доступными через `ApplicationContext.getBean(...)`.

Физически все бины хранятся в структурах внутри контейнера (карты/реестры) — да, чаще всего это `HashMap`-подобные мапы, но контейнер — это не только хранилище: это механизмы создания, связывания, проксирования, lifecycle и hook-ов.

---
# Максимально детально <br>(*технически, по шагам*) 🔧

## 1) Что именно представляет собой «контейнер»
- **ApplicationContext** — высокоуровневый интерфейс контейнера (наследует `BeanFactory`) и даёт доп. возможности: `MessageSource`, `ResourceLoader`, `ApplicationEventPublisher`, `Environment`.
    
- **DefaultListableBeanFactory** — одна из ключевых реализаций `BeanFactory` внутри `ApplicationContext`. Она _хранит_ определения бинов и управляет их созданием.
    
- Контейнер = набор классов (BeanFactory, BeanDefinitionRegistry, post-processors, lifecycle managers) + внутренние мапы/кэш-структуры, которые держат метаданные и инстансы бинов.    

## 2) Что хранится и где (*физически*)
- **BeanDefinition** — метаданные о бине (класс, scope, ctor args, property values, init/destroy methods, autowire mode). Хранятся в `beanDefinitionMap` (в `DefaultListableBeanFactory`).
    
- **singletonObjects (Map<String,Object>)** — кэш готовых singleton-инстансов. Это основная HashMap, «реальный» контейнер инстансов.
    
- **earlySingletonObjects (Map)** — ранние ссылки для решения circular dependencies (частично созданные объекты).
    
- **singletonFactories (`Map<String,ObjectFactory<?>>`)** — фабрики для создания ранних прокси/фабричных ссылок (трёхуровневая кэш-схема).
    
- Кроме того: `beanDefinitionNames` (список имён), `dependentBeanMap` (зависимости для shutdown), `mergedBeanDefinitions` и т.д.    

> Итого: метаданные → `beanDefinitionMap`; реальные объекты → `singletonObjects` (и вспомогательные кэши).

## 3) «**Три уровня кэша**» *(важно для circular deps*)
Spring использует 3-уровня для singletons, чтобы поддержать разрешение циклических зависимостей:
1. **singletonObjects** — готовые инстансы.    
2. **earlySingletonObjects** — ранние, ещё не полностью инициализированные объекты (без применения postProcessors).    
3. **singletonFactories** — фабрики, которые могут вернуть proxy/раннюю фабричную ссылку (используется для создания прокси при необходимости).  
    Это позволяет одному бину получить ссылку на другой, пока тот ещё создаётся.

## 4) Жизненный цикл создания бина (*схематично*)
1. Spring читает конфигурацию → регистрирует `BeanDefinition`.
    
2. `refresh()` (ApplicationContext) → `prepareBeanFactory()` → `postProcessBeanFactory()` → регистрация `BeanPostProcessor`, `BeanFactoryPostProcessor` и т.д.
    
3. `preInstantiateSingletons()` (если eager) → для каждого singleton: `createBean(beanName, beanDefinition)`:    
    - `resolveBeforeInstantiation` (возможные `InstantiationAwareBeanPostProcessor`/proxy)        
    - **instantiate** (выбор конструктора, `ConstructorResolver`)        
    - **populate** (впрыскивание свойств, автowired, `AutowiredAnnotationBeanPostProcessor`)        
    - **applyBeanPostProcessorsBeforeInitialization**        
    - **invoke init methods** (`@PostConstruct`, `InitializingBean.afterPropertiesSet`, custom init-method)        
    - **applyBeanPostProcessorsAfterInitialization** (здесь часто создаются AOP-прокси) 
    - регистрация в `singletonObjects`
    
4. При shutdown: `DisposableBean`/destroy-methods вызываются, зависимости дёргаются в обратном порядке.    

## 5) BeanPostProcessor и BeanFactoryPostProcessor
- **BeanFactoryPostProcessor** — влияет на `BeanDefinition` перед созданием инстансов (например, `ConfigurationClassPostProcessor`, `PropertySourcesPlaceholderConfigurer`).
    
- **BeanPostProcessor** — влияет на инстансы до/после init (например, `AutowiredAnnotationBeanPostProcessor`, `AopAutoProxyCreator`). Они реализуют ключевые расширения контейнера.    

## 6) **Proxies** / **FactoryBean** / **AOP**
- **FactoryBean** — специальный бин, который при `getBean("name")` возвращает не сам FactoryBean, а `getObject()`; сам FactoryBean хранится под &name.
    
- **AOP-прокси** создаются через `BeanPostProcessor` (обычно `AopAutoProxyCreator`) и могут быть JDK-proxy (интерфейсы) или CGLIB (подкласс). В `singletonObjects` обычно хранится уже прокси-инстанс (если проксирование применилось).    

## 7) **Scopes**
- **singleton** — один инстанс на контейнер (по умолчанию).
    
- **prototype** — каждый `getBean()` — новый объект (контейнер не управляет destroy).
    
- **request/session/websocket** — для веб-контекста (в рамках HTTP запроса/сессии).
    
- Можно регистрировать кастомные scope-ы.    

## 8) Где/когда происходит связывание зависимостей
- Связывание (injection) происходит на этапе **populate**: через конструктор, сеттеры или поля (`@Autowired`).
    
- `DependencyDescriptor` и резолверы (qualifiers, primary, byType/byName) отвечают за поиск нужного бина в `BeanFactory`.    

## 9) Конфигурация через аннотации/классы
- `@Configuration` классы обрабатываются `ConfigurationClassPostProcessor` → создаёт `BeanDefinition` для `@Bean` методов; `@ComponentScan` регистрирует компоненты.
    
- Для `@Configuration(proxyBeanMethods=true)` Spring создаёт CGLIB-прокси конфиг-класса, чтобы `@Bean` методы при вызове из другого `@Bean` возвращали тот же singleton, а не новый объект.    

## 10) `ApplicationContext` ≠ просто `HashMap` — **почему**
- Да, реальные объекты лежат в `Map<String,Object>` (singletonObjects), но контейнер — это гораздо больше:    
    - **менеджмент lifecycle**, **dependency injection**, **post processors**, **eventing**, **resource & properties resolution**, **scope management**, **AOP**, **autowiring rules**, **classloader handling**, **bean definition registry**, **shutdown hooks**.
        
    - Контейнер инкапсулирует логику создания объектов, а HashMap — только хранилище инстансов.

## 11) Практические нюансы и ошибки
- **ClassLoader / Multiple contexts**: в web-приложениях есть rootContext + child WebApplicationContext; singleton в child — не виден в root и наоборот (иерархия контекстов).
    
- **Bean overriding**: последние зарегистрированные определения могут переопределять предыдущие (можно отключить).
    
- **Lazy init**: `@Lazy` откладывает создание бина до first use.
    
- **Circular dependencies**: Spring умеет решать циклы для singleton (через ранние объекты), но не для prototype по умолчанию.
    
- **Memory leak**: незакрытые bean-resources или плохо реализованные `destroy` могут помнить ссылки и мешать GC.    

---
# Что сказать коротко на собеседовании <br>(*1–2 предложения*)

> «**Spring Context** — это runtime-контейнер, который хранит метаданные бинов (`BeanDefinition`) и инстансы (singletonObjects map) и управляет их созданием, связыванием, проксированием и lifecycle через набор фабрик и post-processors (DefaultListableBeanFactory + ApplicationContext). Фактически бины лежат в HashMap внутри `DefaultSingletonBeanRegistry`, но «контейнер» — это ещё и логика DI, lifecycle, AOP и eventing.»

---
