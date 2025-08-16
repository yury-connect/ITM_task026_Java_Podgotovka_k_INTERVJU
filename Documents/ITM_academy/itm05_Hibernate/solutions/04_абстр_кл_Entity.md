# Может ли абстрактный класс быть Entity?

---
✅ **Да, может.**

Абстрактный класс может быть аннотирован `@Entity` и при этом сохраняет **все свойства обычного Entity-класса**.  
Единственное отличие — **нельзя создать экземпляр абстрактного класса** напрямую.

---
### 📌 Основные особенности:


- 🔒 **Абстрактность**: нельзя создать объект напрямую.
- 📦 **Наследование**: используется как родительский класс для других `@Entity`, чтобы переиспользовать общие поля, аннотации и логику.
- 🔄 **Общие поля**: можно вынести `id`, `createdAt`, `updatedAt`, и другие общие свойства.

---

### 🛠 Пример: использование `@MappedSuperclass`


```java
@MappedSuperclass
public abstract class BaseEntity {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Геттеры и сеттеры
}

@Entity
public class User extends BaseEntity {
private String username;
private String email;
}
```

#### 💡 Когда использовать?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BA%D0%BE%D0%B3%D0%B4%D0%B0-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D1%82%D1%8C)

- Когда **несколько Entity-классов** используют одинаковые поля (_например,_ `id`, `createdAt`, `updatedAt`).
- Чтобы **избежать дублирования кода** и централизовать общую бизнес-логику.
- Для **удобного расширения** архитектуры, особенно при большом количестве сущностей.

---

### ✅ Вывод

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B2%D1%8B%D0%B2%D0%BE%D0%B4-2)

Абстрактный `@Entity` или `@MappedSuperclass` — это мощный инструмент повторного использования кода, который:

✔️ Сохраняет поведение `Entity` ✔️ Не создаёт экземпляры напрямую ✔️ Передаёт поля и аннотации потомкам

---

```
***** из методички *****
Может, при этом он сохраняет все свойства Entity, 
отличается от обычных Entity классов только тем, 
что нельзя создать объект этого класса. 

Абстрактные Entity классы используются в наследовании, 
когда их потомки наследуют поля абстрактного класса
```