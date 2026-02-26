# Хранение в **BeanFactory**

**BeanFactory** — это корневой интерфейс, родитель [`ApplicationContext`](Documents/ITM_academy/itm06_Spring/additionally/ApplicationContext). 
Он предоставляет более простую функциональность/ базовый контракт (контейнер должен уметь отдавать бины по имени/типу и управлять ими: ленивая инициализация, базовое DI).

## Как работает его внутренняя кухня?

Когда ты вызываешь `beanFactory.getBean("someBean")`, происходит следующий конвейер (упрощенно для понимания):
	
1. **Поиск рецепта:** Сначала контейнер идет в `BeanDefinitionRegistry` (место, где хранятся рецепты) и ищет `BeanDefinition` с именем "someBean".
    
2. **Проверка скоупа:**    
    - Если это **Singleton** и бин уже создан — он просто достается из кэша (`singletonObjects`).        
    - Если это **Singleton**, но создается впервые — запускается процесс создания.        
    - Если это **Prototype** — старый экземпляр не ищется, сразу запускается создание нового.
    
3. **Создание (если нужно):** Запускается цепочка: создание экземпляра через конструктор → внедрение зависимостей → вызов методов `init`.
    
4. **Возврат объекта:** Вам отдается готовый объект.

## Какие бины хранятся в BeanFactory?

- **Синглтоны хранятся всегда**, иначе это был бы не контейнер.  
- Прототипы **не хранятся**.
> Это логично: зачем хранить то, что каждый раз должно быть новым?

## Главное отличие от `ApplicationContext` (для галочки)

Если `ApplicationContext` создает синглтоны при старте *(как рачительный хозяин заранее забивает холодильник продуктами)*, то `BeanFactory` создает бин только тогда, когда к нему обращаются *(ленивый холостяк, который ходит в магазин, только когда захотел есть)*.

---
## Почему про него нужно знать?

1. **Понимание основ:** Все `ApplicationContext` (ClassPathXmlApplicationContext, AnnotationConfigApplicationContext) внутри имеют `DefaultListableBeanFactory`, который имплементирует `BeanFactory`. Зная `BeanFactory`, ты понимаешь корень любой конфигурации Spring.
    
2. **Встраиваемые системы (крайне редко):** Теоретически, если ты пишешь мобильное приложение или что-то с очень жесткими ограничениями по памяти, использование легковесного `BeanFactory` вместо тяжелого `ApplicationContext` может быть оправдано. На практике сейчас это встречается крайне редко.
    
3. **Тестирование:** Иногда в модульных тестах (не интеграционных) удобно использовать `BeanFactory` для быстрого поднятия минимального контекста без лишних накладных расходов.
    
4. **Ответ на собеседовании:** Если ты скажешь, что `BeanFactory` — это просто интерфейс с методом `getBean`, а `DefaultListableBeanFactory` — это основная рабочая лошадка, которая хранит бины в `ConcurrentHashMap` и управляет `BeanDefinition`, это покажет глубокое понимание устройства Spring.

---
### Код-пример для закрепления

Как это выглядит в коде (если бы мы писали на чистом Spring без спринг-бута):
```java
// Создаем фабрику (она же реестр бинов)
DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

// Создаем рецепт (BeanDefinition) для нашего сервиса
BeanDefinitionBuilder builder = BeanDefinitionBuilder
        .rootBeanDefinition(MyService.class)
        .setScope(ConfigurableBeanFactory.SCOPE_SINGLETON)
        .addPropertyValue("propertyName", "someValue");

// Регистрируем рецепт в фабрике под именем "mySuperService"
factory.registerBeanDefinition("mySuperService", builder.getBeanDefinition());

// Теперь фабрика знает про бин, но сам объект еще НЕ создан (ленивость)

// В этот момент происходит магия: проверка кэша, создание, DI
MyService service = factory.getBean("mySuperService", MyService.class);

// Второй раз бин просто достанется из той самой мапы
MyService serviceAgain = factory.getBean("mySuperService", MyService.class);
// service == serviceAgain (true)
```

---
