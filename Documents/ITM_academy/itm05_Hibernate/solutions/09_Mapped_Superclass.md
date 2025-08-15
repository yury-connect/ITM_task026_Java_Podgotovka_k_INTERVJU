# Что такое Mapped Superclass?

---





---
### **`@MappedSuperclass` — кратко и на примере**

#### **➤ Что это?**
Аннотация, которая позволяет **вынести общие поля сущностей в родительский класс**, но **без создания для него отдельной таблицы** в БД.

#### **➤ Пример**
**1. Базовый класс (не является Entity)**
```java
@MappedSuperclass  // Поля этого класса будут в таблицах наследников
public abstract class BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
```

**2. Сущности-наследники**
```java
@Entity
public class User extends BaseEntity {
    private String name;  // + поля id и createdAt из BaseEntity
}

@Entity
public class Product extends BaseEntity {
    private BigDecimal price;  // + поля id и createdAt из BaseEntity
}
```

**3. Таблицы в БД**
- `users (id, created_at, name)`    
- `products (id, created_at, price)`

> **Вопрос:** Будет ли переопределён родительский `@Id` при объявлении своего в классе-наследнике?
> **Ответ:** Да, переопределит.  
> Если в классе-наследнике (`@Entity`) объявить свой `@Id`, он **заменит** первичный ключ, унаследованный от `@MappedSuperclass`.

---
### **➤ Зачем использовать?**
- **Убрать дублирование** общих полей 
	  (`id`, `createdAt`, `version` и т.д.).    
- **Не создавать лишнюю таблицу** для родительского класса 
	  (*в отличие от `@Inheritance`*).    

### **➤ Отличие от `@Embeddable`**

| `@MappedSuperclass`                                 | `@Embeddable`                                                         |
| --------------------------------------------------- | --------------------------------------------------------------------- |
| Для **наследования** полей сущностями.              | Для **встраивания** объекта в сущность.                               |
| Родительский класс — абстрактный (*обычно*).        | Класс может быть самостоятельным.                                     |
| Поля добавляются **напрямую** в таблицу наследника. | Поля добавляются с префиксом (*если не указан `@AttributeOverride`*). |

---
### **➤ Когда применять?**
- Если у нескольких `@Entity` есть **одинаковые поля** (например, аудит-поля).    
- Если **не нужна** отдельная таблица для родительского класса.    

**Важно:** Класс с `@MappedSuperclass` **не может быть `@Entity`** 
	(*нельзя сделать `em.find(BaseEntity.class, id)`*).

Пример из реального мира:
```java
@MappedSuperclass
public abstract class Auditable {
    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;
}
```

Все сущности, наследующие `Auditable`, автоматически получат поля `createdDate` и `updatedDate` в своих таблицах.

**Итог:** Это способ DRY (Don’t Repeat Yourself) в JPA!

---
