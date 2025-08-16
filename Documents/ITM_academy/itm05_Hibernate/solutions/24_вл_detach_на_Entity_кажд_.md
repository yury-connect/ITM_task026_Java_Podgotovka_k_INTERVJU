# Как влияет операция detach на Entity объекты каждого из четырех статусов?

---
## Как влияет операция `detach` на `Entity` объекты каждого из четырех статусов?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%BA%D0%B0%D0%BA-%D0%B2%D0%BB%D0%B8%D1%8F%D0%B5%D1%82-%D0%BE%D0%BF%D0%B5%D1%80%D0%B0%D1%86%D0%B8%D1%8F-detach-%D0%BD%D0%B0-entity-%D0%BE%D0%B1%D1%8A%D0%B5%D0%BA%D1%82%D1%8B-%D0%BA%D0%B0%D0%B6%D0%B4%D0%BE%D0%B3%D0%BE-%D0%B8%D0%B7-%D1%87%D0%B5%D1%82%D1%8B%D1%80%D0%B5%D1%85-%D1%81%D1%82%D0%B0%D1%82%D1%83%D1%81%D0%BE%D0%B2)

## 🔌 Влияние `detach()` на Entity в разных состояниях

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B2%D0%BB%D0%B8%D1%8F%D0%BD%D0%B8%D0%B5-detach-%D0%BD%D0%B0-entity-%D0%B2-%D1%80%D0%B0%D0%B7%D0%BD%D1%8B%D1%85-%D1%81%D0%BE%D1%81%D1%82%D0%BE%D1%8F%D0%BD%D0%B8%D1%8F%D1%85)

|**Статус** `Entity`|**Поведение** `detach(entity)`|
|---|---|
|`Managed` ✅|✅ Становится `Detached`  <br>🔄 Объект больше не отслеживается `EntityManager`|
|`Removed` 🗑|✅ Переходит в `Detached`  <br>🔁 Удаление отменяется, объект отсоединяется|
|`Transient` 🆕|🔁 Игнорируется — не является частью персистентного контекста|
|`Detached` 🔌|🔁 Игнорируется — уже отсоединён|

---

```
***** из методички *****
managed, removed → detached.
new, detached → операция игнорируется
```