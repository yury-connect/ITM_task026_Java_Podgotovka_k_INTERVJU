#   🏗️ Основные игроки и их роли

*Вся транзакционная "магия" Spring строится на неск-х ключ-х интерфейсах и классах.*

- **`PlatformTransactionManager` (*Стратегия управления*)** [](http://rwinch.github.io/spring-framework/data-access/transaction/strategies.html)[](https://docs.spring.io/spring-framework/docs/2.0.8/javadoc-api/org/springframework/transaction/PlatformTransactionManager.html): Это самый главный интерфейс. Он определяет, _как_ именно управлять транзакциями (*начать, зафиксировать, откатить*) [](https://docs.spring.io/spring-framework/docs/2.0.8/javadoc-api/org/springframework/transaction/PlatformTransactionManager.html). Это как универсальный пульт управления. Основные методы:
    - `getTransaction(TransactionDefinition)` — получить транзакцию.
    - `commit(TransactionStatus)` — зафиксировать.
    - `rollback(TransactionStatus)` — откатить [](http://rwinch.github.io/spring-framework/data-access/transaction/strategies.html).

- **`TransactionDefinition` (*Настройки*)** [](http://rwinch.github.io/spring-framework/data-access/transaction/strategies.html)[](https://spring.pleiades.io/spring-framework/docs/1.2.7/javadoc-api/org/springframework/transaction/package-summary.html): Это интерфейс, который содержит _описание_ транзакции. Он не управляет ею, а только говорит, какой она должна быть. Его ключевые атрибуты:
	    
    - **Propagation (*Распространение*)**: Как транзакция относится к уже существующей (например, `REQUIRED` или `REQUIRES_NEW`).
        
    - **Isolation (*Изоляция*)**: Уровень изоляции транзакции.
        
    - **Timeout (*Таймаут*)**: Максимальное время выполнения.
        
    - **Read-only (*Только чтение*)**: Флаг, что транзакция только читает данные [](http://rwinch.github.io/spring-framework/data-access/transaction/strategies.html).

- **`TransactionStatus` (*Состояние*)** [](http://rwinch.github.io/spring-framework/data-access/transaction/strategies.html)[](https://spring.pleiades.io/spring-framework/docs/1.2.7/javadoc-api/org/springframework/transaction/package-summary.html): Этот интерфейс отражает текущее _состояние_ конкретной транзакции. Через него можно:	    
    - Проверить, является ли транзакция новой.        
    - Установить флаг `rollback-only` (*принудительный откат*).        
    - Проверить, отмечена ли транзакция на откат [](http://rwinch.github.io/spring-framework/data-access/transaction/strategies.html).

---
### 🧬 Иерархия и реализации: От абстракции к конкретике

Теперь посмотрим, как эти интерфейсы связаны между собой.

- **`AbstractPlatformTransactionManager`** [](https://docs.spring.io/spring-framework/docs/5.2.24.RELEASE/javadoc-api/org/springframework/transaction/support/package-tree.html)[](https://docs.spring.io/spring-framework/docs/2.0.7/javadoc-api/org/springframework/transaction/support/AbstractPlatformTransactionManager.html)[](https://docs.spring.io/spring-framework/docs/2.0.8/javadoc-api/org/springframework/transaction/PlatformTransactionManager.html): Это абстрактный класс, который является "скелетом" для всех менеджеров транзакций. Он реализует общую логику, такую как обработка распространения (`Propagation`) и синхронизацию, но оставляет конкретные детали работы с ресурсами (например, как начать транзакцию в JDBC) наследникам [](https://docs.spring.io/spring-framework/docs/2.0.7/javadoc-api/org/springframework/transaction/support/AbstractPlatformTransactionManager.html)[](https://docs.spring.io/spring-framework/docs/2.0.8/javadoc-api/org/springframework/transaction/PlatformTransactionManager.html). Это позволяет не писать один и тот же код для каждого нового типа транзакций.

- **Конкретные реализации** [](https://docs.spring.io/spring-framework/docs/2.0.8/javadoc-api/org/springframework/transaction/PlatformTransactionManager.html): Это классы, которые наследуются от `AbstractPlatformTransactionManager` и знают, как работать с конкретными технологиями. Например:    
    - `DataSourceTransactionManager`: Для транзакций на основе JDBC.        
    - `JpaTransactionManager`: Для JPA (например, с Hibernate).        
    - `JtaTransactionManager`: Для глобальных, распределен-х транзакций (JTA) [](https://docs.spring.io/spring-framework/docs/2.0.8/javadoc-api/org/springframework/transaction/PlatformTransactionManager.html)[](https://spring.pleiades.io/spring-framework/docs/2.0.8/javadoc-api/org/springframework/transaction/jta/JtaTransactionManager.html#setTransactionManager\(javax.transaction.TransactionManager\))

- **`CallbackPreferringPlatformTransactionManager`** [](https://docs.spring.io/spring-framework/docs/5.2.24.RELEASE/javadoc-api/org/springframework/transaction/support/package-tree.html)[](https://cloud.tencent.cn/developer/article/1937169?from=15425#3)[](https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/kdoc-api/spring-framework/org.springframework.transaction.support/): Это интерфейс-маркер, расширяющий `PlatformTransactionManager`. Он показывает, что менеджер "предпочитает" работу через колбэки (как в `TransactionTemplate`), а не через прямой вызов `getTransaction`/`commit`/`rollback`. `TransactionTemplate` автоматически использует этот интерфейс, если он реализован [](https://cloud.tencent.cn/developer/article/1937169?from=15425#3)[](https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/kdoc-api/spring-framework/org.springframework.transaction.support/).    

---
### 🛠️ Как это используется на практике?

Разработчики редко работают с `PlatformTransactionManager` напрямую, потому что это низкоуровнево. Вместо этого Spring предлагает два подхода [](https://docs.spring.io/spring-framework/reference/6.2/data-access/transaction/programmatic.html)[](https://spring.pleiades.io/spring/reference/6.2/data-access/transaction/programmatic.html)[](https://docs.spring.io/spring-framework/docs/6.0.0-RC3/reference/pdf/data-access.pdf#19#1):

1. **Декларативный (`@Transactional`)**: Самый популярный способ. Вы просто вешаете аннотацию `@Transactional` на метод, а Spring AOP автоматически подхватывает правильный `PlatformTransactionManager` и оборачивает метод в транзакцию. Это чистый и ненавязчивый подход [](https://docs.spring.io/spring-framework/docs/6.0.0-RC3/reference/pdf/data-access.pdf#19#1).

2. **Программный (`TransactionTemplate`)**: Этот подход используется, когда нужен более тонкий контроль. **`TransactionTemplate`** — это **обертка над `PlatformTransactionManager`**, которая сильно упрощает работу с ним [](https://docs.spring.io/spring-framework/docs/1.0.2/javadoc-api/org/springframework/transaction/support/TransactionTemplate.html#getTransactionManager\(\))[](https://spring.pleiades.io/spring/reference/6.2/data-access/transaction/programmatic.html).
	    
    - Вы создаете `TransactionTemplate`, передавая ему нужный `PlatformTransactionManager`.
        
    - Затем вызываете его метод `execute()`, передавая блок кода в виде колбэка (`TransactionCallback`).
        
    - Шаблон сам берет на себя всю "грязную" работу: открытие, коммит, откат и обработку исключений. Он по сути, реализует паттерн "Шаблонный метод" для транзакций [](https://docs.spring.io/spring-framework/reference/6.2/data-access/transaction/programmatic.html)[](https://cloud.tencent.cn/developer/article/1937169?from=15425#3)[](https://spring.pleiades.io/spring/reference/6.2/data-access/transaction/programmatic.html).

---
### 💎 Итоговая схема связей

Представьте это так:
	
1. **`PlatformTransactionManager`** — это **интерфейс**, который задает контракт для управления транзакциями.
    
2. **`AbstractPlatformTransactionManager`** — это абстрактный класс, который реализует общую логику и служит фундаментом для конкретных реализаций.
    
3. **Конкретные реализации** (`DataSourceTransactionManager` и др.) знают, как управлять транзакциями для конкретных технологий.
    
4. **`TransactionTemplate`** — это удобный **инструмент**, который использует `PlatformTransactionManager`, чтобы предоставить простой программный API.
    
5. **`@Transactional`** — это **декларативный способ**, который использует всё ту же инфраструктуру `PlatformTransactionManager` "под капотом".

Таким образом, независимо от того, используете ли вы `@Transactional` или `TransactionTemplate`, в конечном итоге все упирается в `PlatformTransactionManager`. Это и есть суть элегантной абстракции Spring.

---
