# Для чего нужна аннотация Transient?

---
## Для чего нужна аннотация `Transient`?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%B4%D0%BB%D1%8F-%D1%87%D0%B5%D0%B3%D0%BE-%D0%BD%D1%83%D0%B6%D0%BD%D0%B0-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D1%8F-transient)

Аннотация `@Transient` используется для **исключения поля из маппинга с базой данных**.  
Такие поля **не сохраняются** и **не извлекаются** из БД.

---

### 🧩 Где можно использовать?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B3%D0%B4%D0%B5-%D0%BC%D0%BE%D0%B6%D0%BD%D0%BE-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D1%82%D1%8C)

- В **Entity**-классах
- Во **встраиваемых классах** (`@Embeddable`)
- В **MappedSuperclass**

---

### 🔍 Отличие `Persistent` и `Transient` полей

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BE%D1%82%D0%BB%D0%B8%D1%87%D0%B8%D0%B5-persistent-%D0%B8-transient-%D0%BF%D0%BE%D0%BB%D0%B5%D0%B9)

|**Тип поля**|**Описание**|
|---|---|
|`Persistent`|По умолчанию — все `non-static`, `non-transient`, `non-final` поля сохраняются в БД.|
|`Transient`|Поля, исключаемые из маппинга — не участвуют в сохранении.|

---

### 🧠 Как сделать поле `transient`?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BA%D0%B0%D0%BA-%D1%81%D0%B4%D0%B5%D0%BB%D0%B0%D1%82%D1%8C-%D0%BF%D0%BE%D0%BB%D0%B5-transient)

|**Способ**|**Описание**|
|---|---|
|`@Transient`|Явно указано JPA, что поле не сохраняется.|
|`transient` (_ключевое слово Java_)|Исключается сериализация, **не влияет на JPA**.|
|`static` или `final`|JPA по умолчанию их игнорирует.|

> ☝️ Лучше **всегда использовать `@Transient`**, а не только модификатор `transient`, для полного соответствия JPA-спецификации.

---

### 🧪 Пример

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80-2)

```java
@Entity
public class User {
  private String login;
  private String password;

  @Transient
  private String sessionToken;

  // sessionToken не попадет в БД
}
```

---

### 📦 Пример из жизни (_разграничение модели_)

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80-%D0%B8%D0%B7-%D0%B6%D0%B8%D0%B7%D0%BD%D0%B8-%D1%80%D0%B0%D0%B7%D0%B3%D1%80%D0%B0%D0%BD%D0%B8%D1%87%D0%B5%D0%BD%D0%B8%D0%B5-%D0%BC%D0%BE%D0%B4%D0%B5%D0%BB%D0%B8)

```java
User{firstName='Stefan', ..., password='gemma_arterton_4ever_in_my_heart91'}
// После загрузки из файла или БД
User{firstName='Stefan', ..., password='null'} // password transient
```

---

### ✅ Когда использовать?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BA%D0%BE%D0%B3%D0%B4%D0%B0-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D1%82%D1%8C-2)

- Для хранения **временных**, **вычисляемых**, **технических данных**.
- Если поле **не должно попадать в БД** (_например, токены, флаги, временные значения_).
- При **разделении модели данных и бизнес-логики**.

---

### 🛠️ Сравнение `@Transient` и `transient` (_Java_)

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%EF%B8%8F-%D1%81%D1%80%D0%B0%D0%B2%D0%BD%D0%B5%D0%BD%D0%B8%D0%B5-transient-%D0%B8-transient-java)

|**Особенность**|`@Transient` (_JPA_)|`transient` (_Java_)|
|---|---|---|
|Учитывается JPA|✅|❌|
|Учитывается сериализацией|❌|✅|
|Поведение при сохранении в БД|Не сохраняется|Может быть сохранено, если нет `@Transient`|

---

> **💡 Используй `@Transient`, если поле не должно участвовать в персистентности, и  
> `transient`, если оно не должно сериализоваться в Java.**

---

```
***** из методички *****
 @Transient используется для объявления того, какие поля у сущности, 
 встраиваемого класса или Mapped SuperClass НЕ БУДУТ сохранены в базе данных.
 
Persistent fields (постоянные поля) - это поля, значения которых 
будут по умолчанию сохранены в БД. 
Ими являются любые не static и не final поля.

Transient fields (временные поля):
❖        static и final поля сущностей;
❖        иные поля, объявленные явно с использованием 
            Java-модификатора transient, либо JPA-аннотации @Transient.

Initial user: User{firstName='Stefan', lastName='Smith', email='ssmith@email.com', 
        birthDate=1991-07-16, login='ssmith', password='gemma_arterton_4ever_in_my_heart91'} 
Loaded user from file: User{firstName='Stefan', lastName='Smith', email='ssmith@email.com', 
        birthDate=1991-07-16, login='ssmith', password='null'}
```
