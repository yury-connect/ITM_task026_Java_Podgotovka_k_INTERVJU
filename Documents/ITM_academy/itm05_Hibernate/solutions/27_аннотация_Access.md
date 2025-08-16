# Для чего нужна аннотация Access?

---
## Для чего нужна аннотация `Access`?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%B4%D0%BB%D1%8F-%D1%87%D0%B5%D0%B3%D0%BE-%D0%BD%D1%83%D0%B6%D0%BD%D0%B0-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D1%8F-access)

## 🎛️ Для чего нужна аннотация `@Access` в JPA?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%EF%B8%8F-%D0%B4%D0%BB%D1%8F-%D1%87%D0%B5%D0%B3%D0%BE-%D0%BD%D1%83%D0%B6%D0%BD%D0%B0-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D1%8F-access-%D0%B2-jpa)

Аннотация `@Access` определяет **тип доступа (access type)**, который JPA использует для чтения и записи данных в сущности:

- `FIELD` — доступ напрямую к **полям**.
- `PROPERTY` — доступ через **геттеры/сеттеры**.

---

### 🛠️ Способы доступа:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%EF%B8%8F-%D1%81%D0%BF%D0%BE%D1%81%D0%BE%D0%B1%D1%8B-%D0%B4%D0%BE%D1%81%D1%82%D1%83%D0%BF%D0%B0)

|**Тип доступа**|**Особенности**|
|---|---|
|`FIELD`|Аннотации (`@Id`, `@Column`_, и др._) размещаются над **полями**. JPA работает напрямую с ними, игнорируя геттеры/сеттеры.|
|`PROPERTY`|Аннотации размещаются над **геттерами**. JPA использует **геттеры и сеттеры** для взаимодействия с данными. Требуется соблюдение стандартов JavaBeans (_например,_ `getFirstName()` / `setFirstName(String)`).|

---

### ⚙️ Где можно использовать `@Access`:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%EF%B8%8F-%D0%B3%D0%B4%D0%B5-%D0%BC%D0%BE%D0%B6%D0%BD%D0%BE-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D1%82%D1%8C-access)

- На уровне **класса** (`Entity`, `MappedSuperclass`, `Embeddable`) — задаёт общий способ доступа.
- На уровне **отдельного поля или метода** — позволяет смешивать `FIELD` и `PROPERTY`.

---

### 🧠 Поведение по умолчанию:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D0%BE%D0%B2%D0%B5%D0%B4%D0%B5%D0%BD%D0%B8%D0%B5-%D0%BF%D0%BE-%D1%83%D0%BC%D0%BE%D0%BB%D1%87%D0%B0%D0%BD%D0%B8%D1%8E-1)

> JPA определяет тип доступа автоматически — по расположению аннотации `@Id`:
> 
> - Если `@Id` над полем → `AccessType.FIELD`
> - Если `@Id` над геттером → `AccessType.PROPERTY`

---

### ⚠️ Важно:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%EF%B8%8F-%D0%B2%D0%B0%D0%B6%D0%BD%D0%BE)

- Если комбинируются разные типы доступа, необходимо использовать `@Transient` для исключения конфликта маппинга (во избежание дублирования одного и того же свойства).
- Унаследованные поля получают тип доступа суперкласса.

---

### 💡 Пример:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80-1)

```java
@Entity
@Access(AccessType.PROPERTY)
public class Customer {

  private String firstName;

  @Id
  public Long getId() { ... }

  public String getFirstName() { ... }
  public void setFirstName(String name) { ... }
}
```

---

```
***** из методички *****
Она определяет тип доступа (access type) для класса entity, Mapped Superclass, embeddable или отдельных атрибутов, то есть как JPA будет обращаться к атрибутам entity, как к полям класса (FIELD) или как к свойствам класса (PROPERTY), имеющие гетеры (getter) и сетеры (setter).

Определяет тип доступа к полям сущности. Для чтения и записи этих полей есть два подхода:
1. Field access (доступ по полям). При таком способе аннотации маппинга (Id, Column,...) размещаются над полями, и Hibernate напрямую работает с полями сущности, читая и записывая их.
2. Property access (доступ по свойствам). При таком способе аннотации размещаются над методами-геттерами, но никак не над сеттерами. Hibernate использует их и сеттеры для чтения и записи полей сущности. 

По умолчанию тип доступа определяется местом, в котором находится аннотация @Id. Если она будет над полем - это будет AccessType.FIELD, если над геттером - это AccessType.PROPERTY.
Чтобы явно определить тип доступа у сущности, нужно использовать аннотацию @Access, которая может быть указана у сущности, Mapped Superclass и Embeddable class, а также над полями или методами.
Поля, унаследованные от суперкласса, имеют тип доступа этого суперкласса.
Когда у одной сущности определены разные типы доступа, то нужно использовать аннотацию @Transient для избежания дублирования маппинга.

Но есть требование - у сущности с property access названия методов должны соответствовать требованиям JavaBeans. Например, если у сущности Customer есть поле с именем firstName, то у этой сущности должны быть определены методы getFirstName и setFirstName для чтения и записи поля firstName.
```
