# 🌉 Spring Proxy: _Полное руководство_

![олицетворение дизайн-патера PROXY](/ITM/ITM06_Spring/otc/Spring_proxy/2025-04-25_15-14-43.png)

[_Видео-объяснение._](https://youtu.be/DKNDU7OjyJs?t=541)

### PROXY что это, для чего и как применяется в Spring

**Паттерн Заместитель** (_Proxy_) предоставляет объект-заместитель, который управляет 
доступом к другому объекту. То есть создается объект-суррогат, который может 
выступать в роли другого объекта и замещать его (_перехватывать все вызовы_).  

![справка](/ITM/ITM06_Spring/otc/Spring_proxy/2025-04-25_15-26-31.png)

<details>
        <summary>пример →</summary>

```java
// Базовый пример
public interface Image {
    void display();
}

    public class RealImage implements Image {
        public void display() {
    System.out.println("Displaying real image");
    }
}

public class ProxyImage implements Image {
    private RealImage realImage;

    public void display() {
        if (realImage == null) {
            realImage = new RealImage(); // Ленивая инициализация
        }
        realImage.display();
    }
}
```
</details>


В _Spring_ прокси просто **оборачивает** `bean`, 
он может добавить логику **до** и **после** выполнения методов.
- 🛡️ Контролирует **доступ** к целевому объекту
- ✨ Добавляет **дополнительную логику** (_кэширование, логирование, безопасность_)
- 🎭 Полностью **прозрачен** для клиента

---
## 🧩 Реализации Proxy в Spring
### 1. JDK Dynamic Proxy
#### Когда используется:
* Когда бин реализует хотя бы один интерфейс
* По умолчанию в Spring AOP

#### Особенности:
* ⚡ Быстрое создание
* 🔌 Работает только через интерфейсы
* 🏎️ Оптимален для кратковременных объектов

### 2. CGLIB Proxy
#### Когда используется:
* Когда бин не реализует интерфейсы
* При явном указании proxy-target-class=true

#### Особенности:
* 🧬 Использует наследование
* 🚫 Не работает с final классами/методами
* 🏗️ Требует больше ресурсов при создании

---

<details>
        <summary>⚙️ Настройка прокси в Spring</summary>

```java
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true) // Принудительное использование CGLIB
public class AppConfig {
    // Конфигурация бинов
}
```

```xml
<!-- XML конфигурация -->
<aop:config proxy-target-class="true">
    <!-- Pointcut определения -->
</aop:config>
```
</details>

### 🛠️ Практическое применение

<details>
        <summary>Пример с транзакциями</summary>

```java
@Service
public class UserService {
    @Transactional // Spring создаст прокси для управления транзакцией
    public User createUser(String name) {
        // Логика создания пользователя
    }
}
```
</details>

<details>
        <summary>АОП с кастомной аннотацией</summary>

```java
@Aspect
@Component
public class LoggingAspect {
    @Around("@annotation(Loggable)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        System.out.println(joinPoint.getSignature() + " executed in " + duration + "ms");
        return result;
    }
}
```
</details>

---
### 📊 Сравнение JDK vs CGLIB

| Критерий      | 	`JDK Dynamic Proxy`  | 	`CGLIB Proxy`       |
|:--------------|:----------------------|:---------------------|
| Основа        | 	Интерфейсы           | 	Наследование        |
| Скорость      | 	Быстрее создание     | 	Быстрее выполнение  |
| Ограничения   | 	Требует интерфейс    | 	Не работает с final |
| Инициализация | 	Легковесная          | 	Тяжеловесная        |

---
### 💡 Лучшие практики
* **Для сервисов** - используйте интерфейсы + JDK Proxy
* **Для компонентов** - CGLIB при необходимости
* **Избегайте** self-invocation внутри проксируемых бинов
* **Помните** о final методах при использовании CGLIB

---
### 🚀 Производительность
* Для **short-lived** объектов - JDK Proxy
* Для **long-lived** объектов - CGLIB
* Для **критичных к perfomance** участков - рассмотрите AspectJ compile-time weaving


---
**Динамические прокси-серверы JDK предпочтительнее**, когда у вас есть выбор.

Если целевой объект для проксирования реализует **хотя бы один** интерфейс, то будет
использоваться **динамический прокси JDK**. Все интерфейсы, реализованные целевым
типом, будут проксированы.                             

![как работает dynamic proxy](/ITM/ITM06_Spring/otc/Spring_proxy/2025-04-25_15-32-37.png)

Если целевой объект **не реализует** интерфейсов, будет создан прокси-сервер **CGLIB**.

Если вы хотите принудительно использовать проксирование CGLIB 
(например, проксировать каждый метод, определенный для целевого объекта, 
а не только те, которые реализованы его интерфейсами), вы можете это сделать.

Чтобы принудительно использовать прокси CGLIB, установите для атрибута 
**proxy-target-class** элемента **\<aop:config>** значение true.

![Spring AOP - процессор](/ITM/ITM06_Spring/otc/Spring_proxy/2025-04-25_15-38-27.png)





---

заготовки: 

<details>
        <summary>пример →</summary>

```java

```
</details>


