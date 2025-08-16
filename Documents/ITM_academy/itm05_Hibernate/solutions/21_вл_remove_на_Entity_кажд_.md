# Как влияет операция remove на Entity объекты каждого из четырех статусов?

---
## Как влияет операция remove на Entity объекты каждого из четырех статусов?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%BA%D0%B0%D0%BA-%D0%B2%D0%BB%D0%B8%D1%8F%D0%B5%D1%82-%D0%BE%D0%BF%D0%B5%D1%80%D0%B0%D1%86%D0%B8%D1%8F-remove-%D0%BD%D0%B0-entity-%D0%BE%D0%B1%D1%8A%D0%B5%D0%BA%D1%82%D1%8B-%D0%BA%D0%B0%D0%B6%D0%B4%D0%BE%D0%B3%D0%BE-%D0%B8%D0%B7-%D1%87%D0%B5%D1%82%D1%8B%D1%80%D0%B5%D1%85-%D1%81%D1%82%D0%B0%D1%82%D1%83%D1%81%D0%BE%D0%B2)

## 🗑️ Влияние `remove()` на Entity в разных состояниях

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%EF%B8%8F-%D0%B2%D0%BB%D0%B8%D1%8F%D0%BD%D0%B8%D0%B5-remove-%D0%BD%D0%B0-entity-%D0%B2-%D1%80%D0%B0%D0%B7%D0%BD%D1%8B%D1%85-%D1%81%D0%BE%D1%81%D1%82%D0%BE%D1%8F%D0%BD%D0%B8%D1%8F%D1%85)

|**Статус** `Entity`|**Поведение** `remove(entity)`|
|---|---|
|`Transient` 🆕|🔁 Игнорируется  <br>⚠ Зависимые `Managed`-сущности с `CascadeType.REMOVE` могут перейти в `Removed`|
|`Managed` ✅|✅ Становится `Removed`  <br>🔄 Удаление из БД при `commit()`  <br>🧩 Каскадное удаление для зависимых сущностей|
|`Detached` 🔌|❌ Бросается `IllegalArgumentException`  <br>(_при вызове или на этапе_ `commit`)|
|`Removed` 🗑|🔁 Повторный вызов `remove()` игнорируется|

---

```
***** из методички *****
new → операция игнорируется, однако зависимые Entity могут поменять статус на removed, 
если у них есть аннотации каскадных изменений и они имели статус managed
managed → removed и запись объект в базе данных будет удалена 
при commit-е транзакции (также произойдут операции remove для всех каскадно зависимых объектов)
detached → exception сразу или на этапе commit-а транзакции
removed → операция игнорируется
```