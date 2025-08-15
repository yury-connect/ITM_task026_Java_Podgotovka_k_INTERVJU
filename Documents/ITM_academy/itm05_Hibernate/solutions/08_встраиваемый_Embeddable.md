# Что такое встраиваемый (**Embeddable**) класс? _требования JPA_?

---






---
на примере:
### **`@Embeddable` в JPA — кратко на примере**

**Что делает?**  
Позволяет встроить один класс (`Embeddable`) в другой (`Entity`) **как часть его таблицы в БД** (без отдельной таблицы для `Embeddable`).

### **Пример**
#### **1. Класс `Address` (встраиваемый)**
```java
@Embeddable  // Говорим JPA, что этот класс можно встраивать в Entity
public class Address {
    private String city;
    private String street;
    private String zipCode;
}
```

#### **2. Класс `User` (сущность)**
```java
@Entity
public class User {
    @Id
    private Long id;

    private String name;

    @Embedded  // Встраиваем Address в таблицу User
    private Address homeAddress;
}
```

#### **3. Таблица в БД**
Будет выглядеть так:
```text
users (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    city VARCHAR(255),      // Поля из Address
    street VARCHAR(255),
    zipCode VARCHAR(255)
)
```

### **Когда использовать?**

- Если нужно **группировать поля** (например, адрес, координаты, имя/фамилию).
    
- Если объект **не имеет смысла отдельно от родительской сущности**.
    

### **Плюсы**

- Нет JOIN-запросов (всё в одной таблице).
    
- Упрощает код (вместо `@OneToOne`).
    

### **Ограничения**

- Не может иметь собственный `@Id`.
    
- Не поддерживает наследование.
    

**Аналог в SQL:** Как если бы поля `Address` были напрямую в таблице `users`.


