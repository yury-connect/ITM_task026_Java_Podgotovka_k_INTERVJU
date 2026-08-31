**ObjectProvider** — это не генератор прокси, а **поставщик (*провайдер*)**, который при вызове `getObject()` обращается непосредственно к `BeanFactory` и каждый раз запрашивает у контейнера новый экземпляр бина (или возвращает уже существующий, если это синглтон) [](https://github.com/marcnuri-demo/blog-tutorials/blob/v0.0.14/spring-bean-scopes/ObjectProviderSolution.java)[](https://github.com/sumitgupta28/interview-questions-answers/wiki/Scope-of-the-bean).

---
### В чем принципиальная разница?

| Характери-стика               | `@Lookup`                                                                                                                                                                                                                                                                                                                   | **ObjectProvider**                                                                                                                                                                                                                                                                                                                                                                |
| ----------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Механизм**                  | *CGLIB*-прокси. Spring динамически создает подкласс вашего бина и переопределяет метод, помеченный **`@Lookup`** [](https://www.nowcoder.com/questionTerminal/3dde7376cac04503bc49a104c50a287a?orderByHotValue=0&mutiTagIds=3935&page=6&onlyReference=false)[](https://blog.csdn.net/u010362741/article/details/144032444). | Простой **внедренный объект-поставщик**, который  <br>внутри себя хранит ссылку на `BeanFactory` [](https://docs.spring.io/spring-framework/docs/4.3.4.RELEASE/javadoc-api/org/springframework/beans/factory/ObjectFactory.html)[](https://docs.spring.io/spring-framework/docs/4.3.1.RELEASE/javadoc-api/org/springframework/beans/factory/ObjectFactory.html?is-external=true). |
| **Способ получения**          | Вызов помеченного метода бина.                                                                                                                                                                                                                                                                                              | Вызов метода `getObject()` у внедренного `ObjectProvider`.                                                                                                                                                                                                                                                                                                                        |
| **Что происходит при вызове** | Выполняется <br>переопределенный код,  <br>который делает `beanFactory.getBean(...)` [](https://www.nowcoder.com/questionTerminal/3dde7376cac04503bc49a104c50a287a?orderByHotValue=0&mutiTagIds=3935&page=6&onlyReference=false)[](https://stackoverflow.com/revisions/a9e6c3f8-1f78-4d08-9fb2-169d1e286a59/view-source).   | Выполняется **прямой вызов** `beanFactory.getBean(...)`  <br>из метода `getObject()` [](https://blog.csdn.net/u010362741/article/details/144032444)[](https://stackoverflow.com/posts/55098628/edit).                                                                                                                                                                             |
| **Создание прокси**           | Создается **прокси <br>для всего класса-владельца** (того бина, в котором объявлен `@Lookup`-метод).                                                                                                                                                                                                                        | **Никакого прокси для бина-владельца не создается.** Сам `ObjectProvider`  <br>не является прокси бина,  <br>а лишь его "каналом" для запросов [](https://prod.velog.io/@_koiil/Bean%ec%9d%98-%eb%b2%94%ec%9c%84-%ea%b7%b8%eb%a6%ac%ea%b3%a0-ObjectProvider%ec%99%80-ProxyMode).                                                                                                  |

---
### Как это работает для **прототипного** бина?

#### **Пример**:
```java
@Component
public class SingletonBean {

    @Autowired
    private ObjectProvider<PrototypeBean> prototypeProvider; // Внедр-ся провайдер
    
    public void usePrototype() {
	    // 1. Прямой запрос в BeanFactory
        PrototypeBean bean = prototypeProvider.getObject(); 
        // ... использование bean
    }
}
```

#### **Последовательность действий**:
1. **На этапе создания `SingletonBean`**: Spring видит поле `ObjectProvider<PrototypeBean>` и **внедряет не сам `PrototypeBean`**, а специальный объект-провайдер (`ObjectProvider`). Этот объект знает, как обратиться к `BeanFactory` и какие параметры передать [](https://www.nowcoder.com/questionTerminal/3dde7376cac04503bc49a104c50a287a?orderByHotValue=0&mutiTagIds=3935&page=6&onlyReference=false)[](https://codegym.cc/es/quests/lectures/es.questspring.level01.lecture19).
    
2. **В момент вызова `prototypeProvider.getObject()`**: Метод `getObject()` этого провайдера просто вызывает `beanFactory.getBean(PrototypeBean.class)` [](https://stackoverflow.com/posts/55098628/edit).
    
3. **`BeanFactory`** создает **новый экземпляр** `PrototypeBean` (если его скоуп `prototype`) и возвращает его.

Таким образом, **ничего не проксируется** в том смысле, как это делает `@Lookup`. ObjectProvider — это легковесная обертка вокруг `BeanFactory`, которая позволяет **отложить запрос** на создание экземпляра до момента вызова `getObject()` [](https://blog.csdn.net/u010362741/article/details/144032444)[](https://prod.velog.io/@_koiil/Bean%ec%9d%98-%eb%b2%94%ec%9c%84-%ea%b7%b8%eb%a6%ac%ea%b3%a0-ObjectProvider%ec%99%80-ProxyMode).

---
