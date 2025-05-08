# Как `ApplicationContext` расширяет `BeanFactory` в _Spring_

---
ApplicationContext — это расширенная версия BeanFactory, которая добавляет множество enterprise-ориентированных функций. Рассмотрим ключевые дополнения:

## Упрощенная иерархия интерфейсов `ApplicationContext`
```mermaid
graph TD
    A[📦 BeanFactory] --> B[🔍 Listable]
    B --> C[🌳 Hierarchical]
    C --> D[🚀 ApplicationContext]
    D --> E[⚙️ Configurable]
    E --> F[📝 Abstract]
    F --> G[🖋️ Annotation]
    F --> H[📜 XML]
    F --> I[🌐 Web]
    
    classDef interface fill:#FFD6E0,stroke:#FF85A2;
    classDef abstract fill:#E0F9FF,stroke:#85D4FF;
    classDef impl fill:#D0F0C0,stroke:#7ABA7A;
    
    class A,B,C,D interface;
    class E,F abstract;
    class G,H,I impl;
```

т.е. в кратце выглядит так:
```mermaid
graph LR
    Интерфейсы --> Абстрактные_классы --> Реализации
    style Интерфейсы fill:#FFD6E0
    style Абстрактные_классы fill:#E0F9FF
    style Реализации fill:#D0F0C0
```


## Полная иерархия интерфейсов `ApplicationContext`
```mermaid
graph TD
    A[BeanFactory] --> B[ListableBeanFactory]
    B --> C[HierarchicalBeanFactory]
    C --> D[ApplicationContext]
    D --> E[ConfigurableApplicationContext]
    E --> F[AbstractApplicationContext]
    F --> G[AnnotationConfigApplicationContext]
    F --> H[ClassPathXmlApplicationContext]
    F --> I[GenericWebApplicationContext<br>🌐 - веб]
    
    classDef interface fill:#FFD6E0,stroke:#FF85A2;
    classDef abstract fill:#E0F9FF,stroke:#85D4FF;
    classDef impl fill:#D0F0C0,stroke:#7ABA7A;
    
    class A,B,C,D,E interface;
    class F abstract;
    class G,H,I impl;
```


<details>
        <summary>📝 пример: 🔽</summary>

---
#### описание

```java

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
