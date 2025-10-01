# Как влияет операция `refresh` на `Entity` объекты каждого из четырех статусов?

---
## 🔄 Влияние `refresh()` на `Entity` в разных состояниях

|**Статус** `Entity`|**Поведение** `refresh(entity)`|
|---|---|
|`Managed` ✅|✅ Состояние сущности обновляется из БД  <br>🔄 Также обновляются каскадные зависимости (`CascadeType.REFRESH`)|
|`Transient` 🆕|❌ `IllegalArgumentException` — объект не привязан к БД|
|`Detached` 🔌|❌ `IllegalArgumentException` — объект вне контекста|
|`Removed` 🗑|❌ `IllegalArgumentException` — объект помечен на удаление|

---

```
***** из методички *****
managed → будут восстановлены все изменения из базы данных данного Entity, 
также произойдет refresh всех каскадно зависимых объектов
new, removed, detached → exception

```

---
