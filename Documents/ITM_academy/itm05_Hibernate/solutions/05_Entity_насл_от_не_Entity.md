# Может ли Entity класс наследоваться от не Entity классов (non-entity classes)?

### ✅ **Да, может**, но с учетом следующих условий:

---
### 🔹 1. Родительский класс — обычный (_без аннотаций_)

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-1-%D1%80%D0%BE%D0%B4%D0%B8%D1%82%D0%B5%D0%BB%D1%8C%D1%81%D0%BA%D0%B8%D0%B9-%D0%BA%D0%BB%D0%B0%D1%81%D1%81--%D0%BE%D0%B1%D1%8B%D1%87%D0%BD%D1%8B%D0%B9-%D0%B1%D0%B5%D0%B7-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D0%B9)

- **Не аннотирован** `@Entity` или `@MappedSuperclass`
- ❌ **Поля родительского класса не будут сохранены в БД**

📌 Используется только для логики, утилит, вспомогательных методов.

---

### 🔹 2. Родительский класс аннотирован `@MappedSuperclass`

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-2-%D1%80%D0%BE%D0%B4%D0%B8%D1%82%D0%B5%D0%BB%D1%8C%D1%81%D0%BA%D0%B8%D0%B9-%D0%BA%D0%BB%D0%B0%D1%81%D1%81-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD-mappedsuperclass)

- ✅ **Поля наследуются** и включаются в таблицу БД дочернего `@Entity`
- 👌 Подходит для переиспользования общих полей (_например, `id`, `createdAt` и др._)

---

### 📌 Вывод

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B2%D1%8B%D0%B2%D0%BE%D0%B4-3)

|**Условие**|**Поля сохраняются в БД?**|**Комментарий**|
|---|---|---|
|Родитель без аннотаций|❌ Нет|Используется только для логики|
|Родитель с `@MappedSuperclass`|✅ Да|Поля включаются в таблицу потомка|
|Родитель с `@Entity`|⚠️ Не рекомендуется|Может вызвать проблемы с маппингом|

---

💡 **Совет**:  
Если тебе нужно, чтобы родительские поля попали в БД — используй `@MappedSuperclass`.  
Если нет — просто пиши обычный _Java_-класс без аннотаций.

> ORM любит порядок, а порядок начинается с правильного наследования 😎

---

```
***** из методички *****
Может
```
