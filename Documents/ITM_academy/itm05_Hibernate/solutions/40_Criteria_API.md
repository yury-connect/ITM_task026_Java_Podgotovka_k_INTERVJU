# Что такое Criteria API и для чего он используется?

---
[**Criteria API**](https://javarush.com/groups/posts/2259-jpa--znakomstvo-s-tekhnologiey#Criteria-API) предоставляет возможность создавать запросы к базе данных **в объектно-ориентированном стиле**, обеспечивая **типобезопасность** и возможность **динамического** формирования запросов.
![](_Attachments_40_Criteria_API/CriteriaAPI_logo.png)

---
### 🕰 Исторический контекст

🔸 Начиная с версии `Hibernate 5.2`, старый `Hibernate Criteria API` (_org.hibernate.Criteria_) помечен как устаревший (_deprecated_).  
🔸 🆕 `JPA Criteria API` — современная альтернатива, встроенная в спецификацию **JPA**  
👉 `JPA Criteria API` Рекомендуется к использованию вместо устаревшего `API Hibernate`.

---

### 🌟 Преимущества _JPA_ `Criteria API`:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D1%80%D0%B5%D0%B8%D0%BC%D1%83%D1%89%D0%B5%D1%81%D1%82%D0%B2%D0%B0-jpa-criteria-api)

|💎 **Возможность**|📋 **Описание**|
|---|---|
|🛡 **Типобезопасность**|Ошибки можно отловить уже при компиляции 🧠|
|🔄 **Динамические запросы**|Формируются «на лету» во время выполнения 🔧|

---

### ⚠️ Недостатки _JPA_ `Criteria API`:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%EF%B8%8F-%D0%BD%D0%B5%D0%B4%D0%BE%D1%81%D1%82%D0%B0%D1%82%D0%BA%D0%B8-jpa-criteria-api)

|❌ **Недостаток**|📋 **Описание**|
|---|---|
|📜 **Многословность**|Код может быть громоздким и трудным для восприятия 👀|
|🕹 **Ограниченный контроль над SQL**|Труднее добиться полной оптимизации запросов для конкретной СУБД ⚙️|

### Области применения JPA `Criteria API`:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%BE%D0%B1%D0%BB%D0%B0%D1%81%D1%82%D0%B8-%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D0%BD%D0%B5%D0%BD%D0%B8%D1%8F-jpa-criteria-api)

|🧩 **Возможность**|📋 **Что делает**|
|---|---|
|📊 **Агрегация**|`sum()`, `min()`, `max()` и другие функции|
|🎯 **Выбор колонок**|Извлечение только нужных полей с `ProjectionList`|
|🔗 **JOIN'ы**|Работа с несколькими таблицами: `createAlias()`, `setFetchMode()`|
|🚦 **Фильтрация**|Ограничения через `add()` + `Restrictions`|
|📈 **Сортировка**|`addOrder()` — задаёт порядок вывода|

---

### Рекомендации:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D1%80%D0%B5%D0%BA%D0%BE%D0%BC%D0%B5%D0%BD%D0%B4%D0%B0%D1%86%D0%B8%D0%B8)

✅ Используйте **JPA** `Criteria API`, если:

> - 🔹 структура запроса **меняется динамически**
> - 🔹 нужен строгий **контроль типов**

🤏 Отдайте предпочтение **JPQL/HQL**, если:

> - 🔹 запросы **простые** и заранее **известны**
> - 🔹 важна **читаемость** и **компактность** кода

---

> ✨ **Типичный выбор разработчика**:
> 
> - **Простые** статичные запросы — `JPQL`
> - **Сложные**, динамические, типобезопасные — `Criteria API`

---

## ПРИМЕР  
_одного и того же запроса на `HQL` и на `JPA Criteria API`_:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80-%D0%BE%D0%B4%D0%BD%D0%BE%D0%B3%D0%BE-%D0%B8-%D1%82%D0%BE%D0%B3%D0%BE-%D0%B6%D0%B5-%D0%B7%D0%B0%D0%BF%D1%80%D0%BE%D1%81%D0%B0-%D0%BD%D0%B0-hql-%D0%B8-%D0%BD%D0%B0-jpa-criteria-api)

### 🟢 HQL:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-hql-1)

```java
// HQL
List<User> users = session.createQuery(
    "FROM User WHERE age > :age", User.class)
    .setParameter("age", 18)
    .getResultList();
```

### 🟣 Criteria API (JPA):

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-criteria-api-jpa)

```java
// Criteria API
CriteriaBuilder cb = entityManager.getCriteriaBuilder();
CriteriaQuery<User> cq = cb.createQuery(User.class);
Root<User> root = cq.from(User.class);
cq.select(root).where(cb.gt(root.get("age"), 18));
List<User> users = entityManager.createQuery(cq).getResultList();
```

### 📌 Коротко о разнице:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BA%D0%BE%D1%80%D0%BE%D1%82%D0%BA%D0%BE-%D0%BE-%D1%80%D0%B0%D0%B7%D0%BD%D0%B8%D1%86%D0%B5)

- `HQL`: проще и компактнее, удобен для статичных запросов.
- `Criteria API`: объектно-ориентирован, типобезопасен, удобен для динамического построения запросов.

---

[javarush: **Criteria API**](https://javarush.com/quests/lectures/questhibernate.level16.lecture00)

---

```
***** из методички *****
Начиная с версии 5.2 Hibernate Criteria API объявлен deprecated. 
Вместо него рекомендуется использовать JPA Criteria API.
JPA Criteria API - это актуальный API, используемый только для выборки(select) 
сущностей из БД в более объектно-ориентированном стиле.

Основные преимущества JPA Criteria API:
❖        ошибки могут быть обнаружены во время компиляции;
❖        позволяет динамически формировать запросы на этапе выполнения приложения.

Основные недостатки:
❖        нет контроля над запросом, сложно отловить ошибку
❖        влияет на производительность, множество классов

Для динамических запросов - фрагменты кода создаются 
во время выполнения - JPA Criteria API является предпочтительней. 

Вот некоторые области применения Criteria API:
Criteria API поддерживает проекцию, которую мы можем использовать 
    для агрегатных функций вроде sum(), min(), max() и т.д.
Criteria API может использовать ProjectionList для извлечения 
    данных только из выбранных колонок.
Criteria API может быть использована для join запросов с помощью 
    соединения нескольких таблиц, используя методы 
    createAlias(), setFetchMode() и setProjection().
Criteria API поддерживает выборку результатов согласно условиям 
    (ограничениям). Для этого используется метод add() 
    с помощью которого добавляются ограничения (Restrictions).
Criteria API позволяет добавлять порядок (сортировку) 
    к результату с помощью метода addOrder().        
```
---
