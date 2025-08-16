# Как влияет операция persist на Entity объекты каждого из четырех статусов?

---
## Как влияет операция `persist` на `Entity` объекты каждого из четырех статусов?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%BA%D0%B0%D0%BA-%D0%B2%D0%BB%D0%B8%D1%8F%D0%B5%D1%82-%D0%BE%D0%BF%D0%B5%D1%80%D0%B0%D1%86%D0%B8%D1%8F-persist-%D0%BD%D0%B0-entity-%D0%BE%D0%B1%D1%8A%D0%B5%D0%BA%D1%82%D1%8B-%D0%BA%D0%B0%D0%B6%D0%B4%D0%BE%D0%B3%D0%BE-%D0%B8%D0%B7-%D1%87%D0%B5%D1%82%D1%8B%D1%80%D0%B5%D1%85-%D1%81%D1%82%D0%B0%D1%82%D1%83%D1%81%D0%BE%D0%B2)

### 🧩 Влияние `persist()` на `Entity` в разных состояниях

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B2%D0%BB%D0%B8%D1%8F%D0%BD%D0%B8%D0%B5-persist-%D0%BD%D0%B0-entity-%D0%B2-%D1%80%D0%B0%D0%B7%D0%BD%D1%8B%D1%85-%D1%81%D0%BE%D1%81%D1%82%D0%BE%D1%8F%D0%BD%D0%B8%D1%8F%D1%85)

|**Статус** `Entity`|**Поведение** `persist(entity)`|
|---|---|
|`Transient` 🆕|✅ Становится `Managed`  <br>🔄 Вставка в БД при `flush()` или `commit()`|
|`Managed` ✅|🔁 Игнорируется  <br>⚠ Однако, каскадные зависимости (`CascadeType.PERSIST`) могут быть обработаны|
|`Detached` 🔌|❌ Бросается `EntityExistsException`  <br>(_либо при вызове, либо при коммите_)|
|`Removed` 🗑|⚠ Может вернуть сущность в `Managed`  <br>Только в рамках текущей транзакции|

---

### Состояния сущности в JPA

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D1%81%D0%BE%D1%81%D1%82%D0%BE%D1%8F%D0%BD%D0%B8%D1%8F-%D1%81%D1%83%D1%89%D0%BD%D0%BE%D1%81%D1%82%D0%B8-%D0%B2-jpa)

В JPA сущность может находиться в одном из следующих состояний:

1. **Transient** (_Новая_): Сущность создана, но не связана с контекстом персистенции и не имеет соответствующей записи в базе данных.
2. **Managed** (_Управляемая_): Сущность связана с активным контекстом персистенции; изменения автоматически отслеживаются и синхронизируются с базой данных.
3. **Detached** (_Отсоединенная_): Сущность ранее была управляемой, но теперь не связана с текущим контекстом персистенции; изменения не отслеживаются.
4. **Removed** (_Удаленная_): Сущность помечена для удаления из базы данных; удаление произойдет при синхронизации контекста персистенции.

## 🧠 Состояния сущности и их поведение при JPA-операциях

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D1%81%D0%BE%D1%81%D1%82%D0%BE%D1%8F%D0%BD%D0%B8%D1%8F-%D1%81%D1%83%D1%89%D0%BD%D0%BE%D1%81%D1%82%D0%B8-%D0%B8-%D0%B8%D1%85-%D0%BF%D0%BE%D0%B2%D0%B5%D0%B4%D0%B5%D0%BD%D0%B8%D0%B5-%D0%BF%D1%80%D0%B8-jpa-%D0%BE%D0%BF%D0%B5%D1%80%D0%B0%D1%86%D0%B8%D1%8F%D1%85)

|🛠️ **Операция \ 📦 Состояние сущности**|🌱 **Transient** (_Новая_)|🟢 **Managed** (_Управляемая_)|🔌 **Detached** (_Отсоединённая_)|🗑️ **Removed** (_Удалённая_)|
|---|---|---|---|---|
|`persist()` 🏗️|☑️ Становится Managed; SQL `INSERT` при синхронизации|🔄 Без эффекта|❌ `IllegalArgumentException`|♻️ Становится Managed; SQL `INSERT` при синхронизации|
|`remove()` 🧹|❌ `IllegalArgumentException`|🗑️ Становится Removed; SQL `DELETE` при синхронизации|❌ `IllegalArgumentException`|🙈 Без эффекта|
|`merge()` 🧬|☑️ Становится Managed; SQL `INSERT` при синхронизации|🔄 Без эффекта|🔄 Становится Managed; копирование данных; SQL `INSERT` или `UPDATE`|❌ `IllegalArgumentException`|
|`refresh()` ♻️|❌ `EntityNotFoundException`|🔄 Обновление полей из БД; возможны ошибки при отсутствии записи|❌ `IllegalArgumentException`|❌ `EntityNotFoundException`|
|`detach()` ✂️|🙈 Без эффекта|🔌 Становится Detached; изменения не отслеживаются|🙈 Без эффекта|🔌 Становится Detached; изменения не отслеживаются|

**Пояснения к операциям**

- `persist`: Регистрирует новую сущность в контексте персистенции. Используется для сохранения новых объектов.
- `remove`: Помечает управляемую сущность для удаления из базы данных. Применяется только к управляемым сущностям.
- `merge`: Копирует состояние переданной сущности в управляемую сущность. Используется для синхронизации изменений отсоединенных сущностей с базой данных.
- `refresh`: Обновляет состояние управляемой сущности данными из базы данных. Полезно для отмены несохраненных изменений.
- `detach`: Отсоединяет сущность от контекста персистенции, прекращая отслеживание изменений.

```
***** из методички *****
new → managed, и объект будет сохранен 
в базу при commit-е транзакции или в результате flush операций
managed → операция игнорируется, однако зависимые Entity 
могут поменять статус на managed, если у них есть аннотации каскадных изменений
detached → exception сразу или на этапе commit-а транзакции
removed → managed, но только в рамках одной транзакции. 
```