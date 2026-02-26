# Хранение бинов в `ApplicationContext`

**ApplicationContext** — это центральный интерфейс в Spring, который предоставляет <u>конфигурацию</u> и управление <u>жизненным циклом бинов</u>. По сути, это "умная" фабрика бинов.

## **Как он хранит бины:**
1. **Контейнер Singleton-бинов (DefaultSingletonBeanRegistry):**    
    - Большинство бинов (по умолчанию) имеют скоуп `singleton`.        
    - Spring хранит их в `ConcurrentHashMap` (конкретно — `singletonObjects`).        
    - Ключ — это имя бина (String), значение — готовый объект (сам экземпляр вашего класса).
```java
// Пример того, как это выглядит внутри Spring (упрощенно)
public class DefaultSingletonBeanRegistry {
    // Самая главная мапа, где хранятся готовые бины
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
    
    // Дополнительные мапы для решения циклических зависимостей:
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16); // ранние ссылки
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16); // фабрики
}
```
      
2. **Контейнер Prototype-бинов:**    
    - Для скоупа `prototype` Spring **не хранит** бин после создания. Он создает новый экземпляр при каждом запросе и отдает его вам, не сохраняя в контексте для последующего использования.
    
3. **Дополнительные структуры:**    
    - Мапы для хранения информации о бинах (`BeanDefinition`).        
    - Кэши для ускорения доступа.

**Процесс получения бина из контекста:**  
Когда вы пишете `context.getBean("myService")`, Spring просто делает `map.get("myService")` (если это синглтон) и возвращает вам готовый объект.

---
## Что еще хранится в **ApplicationContext**

Кроме самих объектов бинов, Spring хранит много метаинформации:
1. **BeanDefinitionRegistry:** Хранит `BeanDefinition` — это рецепт создания бина (класс, скоуп, имена методов init/destroy, аргументы конструктора).    
2. **Aliases Map:** Хранит псевдонимы для бинов (если вы дали бину несколько имен).    
3. **ResolvableDependencies:** Для внедрения зависимостей по типам.    

### Наглядный пример (как это выглядит в памяти)
Допустим, у вас есть класс:
```java
@Component
public class UserService {
    @Autowired
    private OrderService orderService;
}
```
В момент старта приложения в `ApplicationContext` (в `DefaultListableBeanFactory`) создаются/хранятся:
1. **Ключ:** `"userService"` : **Значение:** `com.example.UserService@1234` (сам объект)    
2. **Ключ:** `"orderService"` : **Значение:** `com.example.OrderService@5678` (сам объект)    
3. **Отдельно в другом реестре:** `BeanDefinition` для UserService: `scope=singleton`, `beanClass=UserService`, `autowired=byType` и т.д.    
4. **В поле объекта UserService:** лежит ссылка на `orderService` (которую Spring туда положил через рефлексию).    

---
### Резюме для собеседования

Если спросят "Как хранятся бины?", можно ответить так:

> "Синглтон-бины хранятся в `ConcurrentHashMap` внутри `DefaultSingletonBeanRegistry`. Ключом является имя бина, а значением — готовый объект (либо `ObjectFactory` для решения циклических зависимостей). `ApplicationContext` хранит не только сами бины, но и их метаданные (`BeanDefinition`) в отдельных реестрах. Это позволяет Spring управлять их жизненным циклом, применять постпроцессоры и внедрять зависимости. В отличие от `BeanFactory`, `ApplicationContext` преинициализирует синглтоны при старте."




