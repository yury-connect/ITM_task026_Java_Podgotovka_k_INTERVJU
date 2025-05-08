# Как `ApplicationContext` расширяет `BeanFactory` в _Spring_

---
ApplicationContext — это расширенная версия BeanFactory, которая добавляет множество enterprise-ориентированных функций. Рассмотрим ключевые дополнения:


```mermaid
graph TD
    A[BeanFactory] --> B[ListableBeanFactory]
    B --> C[HierarchicalBeanFactory]
    C --> D[ApplicationContext]
    D --> E[ConfigurableApplicationContext]
    E --> F[AbstractApplicationContext]
    F --> G[AnnotationConfigApplicationContext]
    F --> H[ClassPathXmlApplicationContext]
    F --> I[GenericWebApplicationContext]
    
    classDef interface fill:#f9f,stroke:#333;
    classDef abstract fill:#ccc,stroke:#333;
    classDef impl fill:#9f9,stroke:#333;
    
    class A,B,C,D,E interface;
    class F abstract;
    class G,H,I impl;
```

```mermaid
graph TD
    A[BeanFactory\n<interface>] --> B[ListableBeanFactory\n<interface>]
    B --> C[HierarchicalBeanFactory\n<interface>]
    C --> D[ApplicationContext\n<interface>]
    D --> E[ConfigurableApplicationContext\n<interface>]
    E --> F[AbstractApplicationContext\n<abstract>]
    F --> G[AnnotationConfig\n<implementation>]
    F --> H[XML Config\n<implementation>]
    F --> I[Web Config\n<implementation>]
```

```mermaid
graph TD
    A[BeanFactory] --> B[ListableBeanFactory]
    B --> C[HierarchicalBeanFactory]
    C --> D[ApplicationContext]
    D --> E[ConfigurableApplicationContext]
    E --> F[AbstractApplicationContext]
    F --> G[AnnotationConfigApplicationContext]
    F --> H[ClassPathXmlApplicationContext]
    F --> I[GenericWebApplicationContext]
    
    classDef interface fill:#f9f,stroke:#333;
    classDef abstract fill:#ccc,stroke:#333;
    classDef impl fill:#9f9,stroke:#333;
    
    class A,B,C,D,E interface;
    class F abstract;
    class G,H,I impl;
```

```mermaid
graph TD
    A[BeanFactory] --> B[ListableBeanFactory]
    B --> C[HierarchicalBeanFactory]
    C --> D[ApplicationContext]
    D --> E[ConfigurableApplicationContext]
    E --> F[AbstractApplicationContext]
    F --> G[AnnotationConfigApplicationContext]
    F --> H[ClassPathXmlApplicationContext]
    F --> I[GenericWebApplicationContext]
```

```mermaid
graph TD
    A[BeanFactory] --> B[ListableBeanFactory]
    B --> C[HierarchicalBeanFactory]
    C --> D[ApplicationContext]
    D --> E[ConfigurableApplicationContext]
    E --> F[AbstractApplicationContext]
    F --> G[AnnotationConfigApplicationContext]
    F --> H[ClassPathXmlApplicationContext]
    F --> I[GenericWebApplicationContext]
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
