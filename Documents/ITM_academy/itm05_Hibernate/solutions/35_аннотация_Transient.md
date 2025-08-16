# Для чего нужна аннотация Transient?

---
Аннотация `@Transient` используется для **исключения поля из маппинга с базой данных**.  
Такие поля **не сохраняются** и **не извлекаются** из БД.

### 🧩 Где можно использовать?
- В **Entity**-классах
- Во **встраиваемых классах** (`@Embeddable`)
- В **MappedSuperclass**

---
### 🔍 Отличие `Persistent` и `Transient` полей

|**Тип поля**|**Описание**|
|---|---|
|`Persistent`|По умолчанию — все `non-static`, `non-transient`, `non-final` поля сохраняются в БД.|
|`Transient`|Поля, исключаемые из маппинга — не участвуют в сохранении.|

---
### 🧠 Как сделать поле `transient`?

|**Способ**|**Описание**|
|---|---|
|`@Transient`|Явно указано JPA, что поле не сохраняется.|
|`transient` (_ключевое слово Java_)|Исключается сериализация, **не влияет на JPA**.|
|`static` или `final`|JPA по умолчанию их игнорирует.|
> ☝️ Лучше **всегда использовать `@Transient`**, а не только модификатор `transient`, для полного соответствия JPA-спецификации.

---
### 🧪 Пример
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

```java
User{firstName='Stefan', ..., password='gemma_arterton_4ever_in_my_heart91'}
// После загрузки из файла или БД
User{firstName='Stefan', ..., password='null'} // password transient
```

---
### ✅ Когда использовать?
- Для хранения **временных**, **вычисляемых**, **технических данных**.
- Если поле **не должно попадать в БД** (_например, токены, флаги, временные значения_).
- При **разделении модели данных и бизнес-логики**.

---
### 🛠️ Сравнение `@Transient` и `transient` (_Java_)

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

---
