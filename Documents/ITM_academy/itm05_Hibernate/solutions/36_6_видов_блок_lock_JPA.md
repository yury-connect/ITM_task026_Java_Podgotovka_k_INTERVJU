# Какие шесть видов блокировок (lock) описаны в спецификации JPA (или какие есть значения у enum LockModeType в JPA)?

---
## Какие шесть видов блокировок (_lock_) описаны в спецификации JPA  
(_или какие есть значения у enum LockModeType в JPA_)?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%BA%D0%B0%D0%BA%D0%B8%D0%B5-%D1%88%D0%B5%D1%81%D1%82%D1%8C-%D0%B2%D0%B8%D0%B4%D0%BE%D0%B2-%D0%B1%D0%BB%D0%BE%D0%BA%D0%B8%D1%80%D0%BE%D0%B2%D0%BE%D0%BA-lock-%D0%BE%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D1%8B-%D0%B2-%D1%81%D0%BF%D0%B5%D1%86%D0%B8%D1%84%D0%B8%D0%BA%D0%B0%D1%86%D0%B8%D0%B8-jpa-%D0%B8%D0%BB%D0%B8-%D0%BA%D0%B0%D0%BA%D0%B8%D0%B5-%D0%B5%D1%81%D1%82%D1%8C-%D0%B7%D0%BD%D0%B0%D1%87%D0%B5%D0%BD%D0%B8%D1%8F-%D1%83-enum-lockmodetype-%D0%B2-jpa)

## 🔐 LockModeType в JPA — Режимы блокировки

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-lockmodetype-%D0%B2-jpa--%D1%80%D0%B5%D0%B6%D0%B8%D0%BC%D1%8B-%D0%B1%D0%BB%D0%BE%D0%BA%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B8)

JPA предоставляет **6** уровней блокировок, определённых в `enum javax.persistence.LockModeType`.  
Они различаются по надёжности, производительности и сценарию использования. отимистичные - предполагают, что конфликтов между транзакциями будет мало пессимистичные - что конфликтов возможно будет много и нужно обеспечить гарантию консистентности

---

### 📋 Таблица: режимы блокировки в JPA

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D0%B0-%D1%80%D0%B5%D0%B6%D0%B8%D0%BC%D1%8B-%D0%B1%D0%BB%D0%BE%D0%BA%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B8-%D0%B2-jpa)

|**🔢**|**LockModeType**|**Тип блокировки**|**Описание**|
|---|---|---|---|
|1️⃣|`NONE`|🔓 Без блокировки|Данные читаются без наложения каких-либо блокировок.|
|2️⃣|`OPTIMISTIC`|✅ Оптимистичная|Проверка поля `@Version` _(версии сущности)_ при `commit`.  <br>Если значение изменилось — `OptimisticLockException`.|
|3️⃣|`OPTIMISTIC_FORCE_INCREMENT`|🔁 Оптимистичная  <br>+ инкремент|Аналог `OPTIMISTIC`, но поле `@Version` увеличивается **всегда**, даже без изменений.|
|4️⃣|`PESSIMISTIC_READ`|🔒 Пессимистичная  <br>(_чтение_)|Данные блокируются на чтение.  <br>Другие могут **читать**, но **не изменять**.|
|5️⃣|`PESSIMISTIC_WRITE`|🔐 Пессимистичная  <br>(_запись_)|Данные блокируются полностью.  <br>**Ни чтение**, **ни запись** другими транзакциями **невозможны**.|
|6️⃣|`PESSIMISTIC_FORCE_INCREMENT`|🔐➕ Пессимистичная  <br>+ инкремент|Как `PESSIMISTIC_WRITE`, но дополнительно увеличивает `@Version`.|
|7️⃣|`READ` (_устаревший_)|📖 Синоним `OPTIMISTIC`.|_устаревший_  <br>Рекомендуется использовать  <br>`OPTIMISTIC` в новых приложениях.|
|8️⃣|`WRITE` (_устаревший_)|✏️ Синоним `OPTIMISTIC_FORCE_INCREMENT`.|_устаревший_  <br>Рекомендуется использовать `<br>OPTIMISTIC_FORCE_INCREMENT` в новых приложениях.|

---

### ⚙️ Использование блокировки

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%EF%B8%8F-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5-%D0%B1%D0%BB%D0%BE%D0%BA%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B8)

```java
EntityManager em = entityManagerFactory.createEntityManager();
em.getTransaction().begin();

Company company = em.find(Company.class, 1L);
em.lock(company, LockModeType.OPTIMISTIC);

em.getTransaction().commit();
```

---

## 🧠 Оптимистичная блокировка (_Optimistic Lock_)

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BE%D0%BF%D1%82%D0%B8%D0%BC%D0%B8%D1%81%D1%82%D0%B8%D1%87%D0%BD%D0%B0%D1%8F-%D0%B1%D0%BB%D0%BE%D0%BA%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0-optimistic-lock)

- Не блокирует ресурсы БД.
- Проверяет поле `@Version` при коммите транзакции.
- Идеальна для систем с **низким уровнем конфликта доступа**.

```java
@Version
private long version;
```

🧪 При несовпадении версий — `OptimisticLockException`.

---

## 🧱 Пессимистичная блокировка (_Pessimistic Lock_)

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D0%B5%D1%81%D1%81%D0%B8%D0%BC%D0%B8%D1%81%D1%82%D0%B8%D1%87%D0%BD%D0%B0%D1%8F-%D0%B1%D0%BB%D0%BE%D0%BA%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0-pessimistic-lock)

- Активно блокирует данные в момент чтения или записи.
- Гарантирует защиту от параллельных изменений.
- Используется при **высокой конкуренции** и необходимости изоляции.

📌 Требует поддержки БД.

---

## 🆚 Сравнение: `Optimistic` vs `Pessimistic`

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D1%81%D1%80%D0%B0%D0%B2%D0%BD%D0%B5%D0%BD%D0%B8%D0%B5-optimistic-vs-pessimistic)

|**Критерий**|`Optimistic`|`Pessimistic`|
|---|---|---|
|Блокировка в БД|❌ Нет|✅ Да|
|Производительность|🔼 Выше|🔽 Ниже|
|Конфликты|Обнаруживаются при коммите|Предотвращаются заранее|
|Риск исключений|`OptimisticLockException`|`PessimisticLockException`, deadlock|
|Подходит для|Чтение-ориентированных систем|Высококонкурентных систем|

> **💡 Рекомендация: по умолчанию используй `OPTIMISTIC`,  
> а `PESSIMISTIC` — только при необходимости _строгой изоляции транзакций_.**

---

```
***** из методички *****
В порядке от самого ненадежного и быстрого, до самого надежного и медленного:
1.        NONE — без блокировки.
2.        OPTIMISTIC (синоним READ в JPA 1) — оптимистическая  блокировка, которая работает, так: 
            если при завершении транзакции кто-то извне изменит поле @Version, 
            то будет сделан RollBack транзакции и будет выброшено OptimisticLockException.
3.        OPTIMISTIC_FORCE_INCREMENT (синоним WRITE в JPA 1) — работает по тому же алгоритму, 
            что и LockModeType.OPTIMISTIC за тем исключением, что после commit значение поле Version 
            принудительно увеличивается на 1. В итоге окончательно после каждого коммита поле увеличится 
            на 2(увеличение, которое можно увидеть в Post-Update + принудительное увеличение).
4.        PESSIMISTIC_READ — данные блокируются в момент чтения и это гарантирует, что никто 
            в ходе выполнения транзакции не сможет их изменить. Остальные транзакции, тем не менее, 
            смогут параллельно читать эти данные. Использование этой блокировки может вызывать 
            долгое ожидание блокировки или даже выкидывание PessimisticLockException.
5.        PESSIMISTIC_WRITE — данные блокируются в момент записи и никто с момента захвата блокировки 
            не может в них писать и не может их читать до окончания транзакции, владеющей блокировкой. 
            Использование этой блокировки может вызывать долгое ожидание блокировки.
6.        PESSIMISTIC_FORCE_INCREMENT — ведёт себя как PESSIMISTIC_WRITE, но в конце транзакции 
            увеличивает значение поля @Version, даже если фактически сущность не изменилась.

Оптимистичное блокирование - подход предполагает, что параллельно выполняющиеся транзакции 
редко обращаются к одним и тем же данным и позволяет им свободно выполнять любые чтения и обновления данных. 
Но при окончании транзакции производится проверка, изменились ли данные в ходе выполнения данной транзакции и, 
если да, транзакция обрывается и выбрасывается OptimisticLockException. 
Оптимистичное блокирование в JPA реализовано путём внедрения в сущность специального поля версии:
    @Version
    private long version;
Поле, аннотирование @Version, может быть целочисленным или временнЫм. 
При завершении транзакции, если сущность была заблокирована оптимистично, будет проверено, 
не изменилось ли значение @Version кем-либо ещё, после того как данные были прочитаны, и, если изменилось, 
будет выкинуто OptimisticLockException. Использование этого поля позволяет отказаться от блокировок 
на уровне базы данных и сделать всё на уровне JPA, улучшая уровень конкурентности.
Позволяет отказатьсь от блокировок на уровне БД и делать всё с JPA.

Пессимистичное блокирование - подход напротив, ориентирован на транзакции, которые часто конкурируют 
за одни и те же данные и поэтому блокирует доступ к данным в тот момент когда читает их. 
Другие транзакции останавливаются, когда пытаются обратиться к заблокированным данным 
и ждут снятия блокировки (или кидают исключение). 
Пессимистичное блокирование выполняется на уровне базы и поэтому не требует вмешательств в код сущности.

Блокировки ставятся путём вызова метода lock() у EntityManager, 
в который передаётся сущность, требующая блокировки и уровень блокировки:
EntityManager em = entityManagerFactory.createEntityManager();
em.lock(company1, LockModeType.OPTIMISTIC);
```
