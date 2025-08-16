# Какие четыре статуса жизненного цикла Entity объекта (Entity Instance’s Life Cycle) вы можете перечислить?

---
## 🔄 Жизненный цикл `Entity` в *JPA*

| **Состояние**                                 | **Описание**                                                                                                                                                                                                                                                                                                                 |
| --------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 🆕  <br>**`Transient`**  <br>_(Переходное)_   | — Сущность **только что создана** (`new`)  <br>— Не связана с БД/ `persistence context`  <br>— Не имеет `ID`  <br>**Возможен** переход в `Managed`:  <br>`persist()` -> **`Managed`** , если объект ранее **не имел** `ID`  <br>`merge()` -> **`Managed`**, но создаётся **новый объект**, а **не текущий** экземпляр!       |
| ✅  <br>**`Managed`**  <br>_(Управляемое)_     | — Сущность **связана** с `Persistence Context`  <br>— Управляется `EntityManager`  <br>— Будет автоматически сохранена при коммите.  <br>**Возможно**:  <br>* **удалить**: `remove()` → **`Removed`**  <br>* **отсоединить**: `close()` или `clear()` → **`Detached`**                                                       |
| 🔌  <br>**`Detached`**  <br>_(Отсоединённое)_ | — Сущность **имеет ID**, но **вне** `Persistence Context`  <br>— Изменения **не сохраняются автоматически**  <br>**Возможно**:  <br>* **подключить обратно** — `merge()` → создаётся управляемый экземпляр (_а **не текущий**!_)  <br>* **найти заново** — `find()` вернёт **новый** `Managed`, текущий останется `Detached` |
| 🗑  <br>**`Removed`**  <br>_(Удалённое)_      | — Сущность **помечена на удаление** (`em.remove(entity)`)  <br>— Всё ещё `Managed`**но** будет удалён из БД при коммите.                                                                                                                                                                                                     |
### 📝 Переходы между состояниями:
**`new`** → `persist()` → **`Managed`**
**`Managed`** → `detach()` → **`Detached`**
**`Managed`** → `remove()` → **`Removed`** → **`Detached`** (_после коммита_)

[![Переходы между состояниями](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/raw/by_questions/ITM/ITM05_Hibernate/imgs/2025-04-08_18-02-16.png)](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/imgs/2025-04-08_18-02-16.png)

> **Переходы между этими состояниями осуществляются с помощью методов `EntityManager`:**
> 
> - **`persist(entity)`**: Переводит сущность из состояния **Transient** в **Managed**.  
>     Если сущность уже **Managed**, метод игнорируется.  
>     Если сущность **Removed**, она снова становится **Managed**.
> - **`remove(entity)`**: Переводит сущность из состояния **Managed** в **Removed**.  
>     Если сущность **Detached** или **Transient**, выбрасывается исключение `IllegalArgumentException`.
> - **`merge(entity)`**: Объединяет состояние переданной сущности  
>     с существующей управляемой сущностью или создаёт новую управляемую копию.  
>     Возвращает управляемую сущность.  
>     Если исходная сущность была **Detached**, она остаётся в этом состоянии.
> - **`detach(entity)`**: Переводит сущность из состояния **Managed** в **Detached**, прекращая её отслеживание контекстом персистенции.
> - **`find(entityClass, primaryKey)`**: Если сущность с указанным ключом существует в контексте персистенции, возвращает её.  
>     В противном случае загружает сущность из базы данных и делает её **Managed**.
> - **`refresh(entity)`**: Обновляет состояние **Managed** сущности, перезагружая данные из базы данных.

---

| Операция    | New (новый)                                              | Managed (управляемый)                                       | Detached (отсоединённый)                                                  | Removed (удалённый)                                |
| ----------- | -------------------------------------------------------- | ----------------------------------------------------------- | ------------------------------------------------------------------------- | -------------------------------------------------- |
| **persist** | `new → managed` (сохранится в БД при commit/flush)       | Игнорируется (но каскадные entity могут стать `managed`)    | ❌ **Exception** (у detached уже есть ID)                                  | `removed → managed` (возврат в управляемый статус) |
| **remove**  | Игнорируется (но каскадные entity могут стать `removed`) | `managed → removed` (удалится из БД при commit)             | ❌ **Exception**                                                           | Игнорируется                                       |
| **merge**   | Создаётся новая `managed`-копия                          | Игнорируется (но каскадные entity обновляются)              | Данные копируются в существующую `managed`-сущность (или создаётся новая) | ❌ **Exception**                                    |
| **refresh** | ❌ **Exception**                                          | Восстанавливает данные из БД (отменяет локальные изменения) | ❌ **Exception**                                                           | ❌ **Exception**                                    |
| **detach**  | ❌ **Exception** (не имеет смысла)                        | `managed → detached` (больше не отслеживается)              | Игнорируется (уже `detached`)                                             | `removed → detached` (прекращает отслеживание)     |



---

## Примеры кода:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80%D1%8B-%D0%BA%D0%BE%D0%B4%D0%B0)

```java
// Пример перехода Transient -> Managed
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();
Person person = new Person();
person.setName("Иван");
em.persist(person); // Теперь person в состоянии Managed
em.getTransaction().commit();
em.close();
```

```java
// Пример перехода Managed -> Detached
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();
Person person = em.find(Person.class, 1L); // person в состоянии Managed
em.detach(person); // Теперь person в состоянии Detached
em.getTransaction().commit();
em.close();
```

```java
// Пример перехода Detached -> Managed
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();
Person detachedPerson = new Person();
detachedPerson.setId(1L);
detachedPerson.setName("Пётр");
Person managedPerson = em.merge(detachedPerson); // managedPerson в состоянии Managed
em.getTransaction().commit();
em.close();
```

---

```
***** из методички *****
Transient (New) — свежесозданная оператором new() сущность не имеет связи с базой данных, 
не имеет данных в базе данных и не имеет сгенерированных первичных ключей.

managed - объект создан, сохранён в бд, имеет primary key, управляется JPA
detached -объект создан, имеет primary key, не является (или больше не является) 
    частью контекста персистентности (не управляется JPA);
removed - объект создан, управляется JPA, будет удален при commit-е и статус станет опять detached
```
