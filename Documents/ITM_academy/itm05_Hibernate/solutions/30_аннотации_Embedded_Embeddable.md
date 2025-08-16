# Для чего нужны аннотации @Embedded и @Embeddable?

---
## Для чего нужны аннотации @Embedded и @Embeddable?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%B4%D0%BB%D1%8F-%D1%87%D0%B5%D0%B3%D0%BE-%D0%BD%D1%83%D0%B6%D0%BD%D1%8B-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D0%B8-embedded-%D0%B8-embeddable)

## 🧱 Аннотации `@Embeddable` и `@Embedded` в JPA

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D0%B8-embeddable-%D0%B8-embedded-%D0%B2-jpa)

**_JPA_** предоставляет аннотации `@Embeddable` и `@Embedded` для реализации **встраиваемых объектов (_композиция_)**, хранимых в **одной таблице** с родительской сущностью.

---

### 📌 Назначение аннотаций

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BD%D0%B0%D0%B7%D0%BD%D0%B0%D1%87%D0%B5%D0%BD%D0%B8%D0%B5-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D0%B9)

|**Аннотация**|**Где используется**|**Назначение**|
|---|---|---|
|`@Embeddable`|Над классом|Обозначает, что класс может быть встроен в сущность.|
|`@Embedded`|Над полем в сущности|Обозначает, что это поле встраивает другой (_встроенный_) объект.|

---

### 📦 Когда использовать

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BA%D0%BE%D0%B3%D0%B4%D0%B0-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D1%82%D1%8C-1)

Эти аннотации применяются при моделировании **композиции**, когда один объект **не существует отдельно** от другого.

**Например**:

- Адрес (`Address`) как часть Пользователя (`User`)
- Финансовые реквизиты (`BankDetails`) как часть Контрагента (`Customer`)

---

### 📐 Поведение при маппинге

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D0%BE%D0%B2%D0%B5%D0%B4%D0%B5%D0%BD%D0%B8%D0%B5-%D0%BF%D1%80%D0%B8-%D0%BC%D0%B0%D0%BF%D0%BF%D0%B8%D0%BD%D0%B3%D0%B5)

- Встроенные поля объекта `@Embeddable` **инлайн-отображаются** в таблице сущности, как будто они её собственные.
- Не создаётся отдельная таблица, всё содержимое — в **одной таблице** БД.

---

### ✅ Пример использования

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F-1)

```java
// Встраиваемый класс
@Embeddable
public class Address {
  private String street;
  private String city;
  private String zip;
}

// Сущность, в которую встраивается Address
@Entity
public class User {
  @Id
  private Long id;

  @Embedded
  private Address address;
}
```

---

```
***** из методички *****
@Embeddable - аннотация JPA, размещается над классом для указания того, что класс является встраиваемым в другие классы.
@Embedded - аннотация JPA, используется для размещения над полем в классе-сущности для указания того, что мы внедряем встраиваемый класс.

Для отображения композиции в общую таблицу существуют аннотации @Embedable и @Embeded. Первая ставится над полем, а вторая над классом. Они взаимозаменяемые. (Композиция, когда не может существовать отдельно)
```
