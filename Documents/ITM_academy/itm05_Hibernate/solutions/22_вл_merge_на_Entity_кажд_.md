# Как влияет операция merge на Entity объекты каждого из четырех статусов?

---
## Как влияет операция `merge` на `Entity` объекты каждого из четырех статусов?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%BA%D0%B0%D0%BA-%D0%B2%D0%BB%D0%B8%D1%8F%D0%B5%D1%82-%D0%BE%D0%BF%D0%B5%D1%80%D0%B0%D1%86%D0%B8%D1%8F-merge-%D0%BD%D0%B0-entity-%D0%BE%D0%B1%D1%8A%D0%B5%D0%BA%D1%82%D1%8B-%D0%BA%D0%B0%D0%B6%D0%B4%D0%BE%D0%B3%D0%BE-%D0%B8%D0%B7-%D1%87%D0%B5%D1%82%D1%8B%D1%80%D0%B5%D1%85-%D1%81%D1%82%D0%B0%D1%82%D1%83%D1%81%D0%BE%D0%B2)

## 🔄 Влияние `merge()` на `Entity` в разных состояниях

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B2%D0%BB%D0%B8%D1%8F%D0%BD%D0%B8%D0%B5-merge-%D0%BD%D0%B0-entity-%D0%B2-%D1%80%D0%B0%D0%B7%D0%BD%D1%8B%D1%85-%D1%81%D0%BE%D1%81%D1%82%D0%BE%D1%8F%D0%BD%D0%B8%D1%8F%D1%85)

|**Статус** `Entity`|**Поведение** `merge(entity)`|
|---|---|
|`Transient` 🆕|✅ Создаётся **новый `Managed` объект**  <br>🔄 Данные копируются из `Transient`-объекта в новый, привязанный к контексту|
|`Managed` ✅|🔁 Операция игнорируется  <br>⚙ Но применяется `merge()` к зависимым Entity, если у них нет статуса `Managed`|
|`Detached` 🔌|✅ Ищется или создаётся `Managed` Entity с тем же ID  <br>🔄 Данные из `Detached` копируются в `Managed` объект|
|`Removed` 🗑|❌ Бросается `IllegalArgumentException`  <br>(либо немедленно, либо при `commit()`)|

---

```
***** из методички *****
new → будет создан новый managed entity, 
в который будут скопированы данные прошлого объекта
managed → операция игнорируется, однако операция merge 
сработает на каскадно зависимые Entity, если их статус не managed
detached → либо данные будут скопированы в существующий managed entity 
с тем же первичным ключом, либо создан новый managed в который скопируются данные
removed → exception сразу или на этапе commit-а транзакции

```
