# Как влияет операция refresh на Entity объекты каждого из четырех статусов?

---
## Как влияет операция `refresh` на `Entity` объекты каждого из четырех статусов?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%BA%D0%B0%D0%BA-%D0%B2%D0%BB%D0%B8%D1%8F%D0%B5%D1%82-%D0%BE%D0%BF%D0%B5%D1%80%D0%B0%D1%86%D0%B8%D1%8F-refresh-%D0%BD%D0%B0-entity-%D0%BE%D0%B1%D1%8A%D0%B5%D0%BA%D1%82%D1%8B-%D0%BA%D0%B0%D0%B6%D0%B4%D0%BE%D0%B3%D0%BE-%D0%B8%D0%B7-%D1%87%D0%B5%D1%82%D1%8B%D1%80%D0%B5%D1%85-%D1%81%D1%82%D0%B0%D1%82%D1%83%D1%81%D0%BE%D0%B2)

## 🔄 Влияние `refresh()` на Entity в разных состояниях

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B2%D0%BB%D0%B8%D1%8F%D0%BD%D0%B8%D0%B5-refresh-%D0%BD%D0%B0-entity-%D0%B2-%D1%80%D0%B0%D0%B7%D0%BD%D1%8B%D1%85-%D1%81%D0%BE%D1%81%D1%82%D0%BE%D1%8F%D0%BD%D0%B8%D1%8F%D1%85)

|**Статус** `Entity`|**Поведение** `refresh(entity)`|
|---|---|
|`Managed` ✅|✅ Состояние сущности обновляется из БД  <br>🔄 Также обновляются каскадные зависимости (`CascadeType.REFRESH`)|
|`Transient` 🆕|❌ `IllegalArgumentException` — объект не привязан к БД|
|`Detached` 🔌|❌ `IllegalArgumentException` — объект вне контекста|
|`Removed` 🗑|❌ `IllegalArgumentException` — объект помечен на удаление|

---

```
***** из методички *****
managed → будут восстановлены все изменения из базы данных данного Entity, 
также произойдет refresh всех каскадно зависимых объектов
new, removed, detached → exception

```
