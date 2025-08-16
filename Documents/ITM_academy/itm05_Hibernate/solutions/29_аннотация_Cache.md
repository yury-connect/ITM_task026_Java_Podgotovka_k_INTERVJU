# Для чего нужна аннотация @Cache?

---
## Для чего нужна аннотация `@Cache`?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%B4%D0%BB%D1%8F-%D1%87%D0%B5%D0%B3%D0%BE-%D0%BD%D1%83%D0%B6%D0%BD%D0%B0-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D1%8F-cache)

### 🧠 Hibernate: Аннотация `@Cache`

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-hibernate-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D1%8F-cache)

Аннотация `@Cache` используется в `Hibernate` для **гибкой настройки кэширования второго уровня (_L2 Cache_)**.

> Работает только в связке с `@Cacheable`, которая включает сам механизм кэширования для сущности.

---

### ⚙️ Основные параметры

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%EF%B8%8F-%D0%BE%D1%81%D0%BD%D0%BE%D0%B2%D0%BD%D1%8B%D0%B5-%D0%BF%D0%B0%D1%80%D0%B0%D0%BC%D0%B5%D1%82%D1%80%D1%8B)

|**Параметр**|**Назначение**|
|---|---|
|`usage`|Определяет стратегию кэширования и управления конкурентным доступом.|
|`region`|Имя области (_региона_) кэша, позволяет разделять сущности на разные группы хранения.|
|`include`|Указывает, кэшировать ли ленивые поля (`lazy`) или только eagerly-загружаемые.|

---

### 🧩 Значения `usage` (_стратегия кэширования_)

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B7%D0%BD%D0%B0%D1%87%D0%B5%D0%BD%D0%B8%D1%8F-usage-%D1%81%D1%82%D1%80%D0%B0%D1%82%D0%B5%D0%B3%D0%B8%D1%8F-%D0%BA%D1%8D%D1%88%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F)

|**Значение**|**Описание**|
|---|---|
|`READ_ONLY`|Только для чтения. Идеально для неизменяемых данных. Ошибка при попытке записи.|
|`NONSTRICT_READ_WRITE`|Чтение/запись без строгой консистентности. Возможны "**грязные**" чтения.|
|`READ_WRITE`|Безопасный режим. Использует блокировки, обеспечивает консистентность.|
|`TRANSACTIONAL`|Используется с JTA. Полная согласованность, подходит для продвинутых сценариев.|

---

### 🗂️ Использование `region`

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%EF%B8%8F-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5-region)

- Позволяет логически **разделять кэш по регионам**, задавая разные настройки (_время жизни, объем, политику вытеснения и т.п._).
- По умолчанию `Hibernate` создает регион с именем полного имени класса.
- **Пример**: сущность `Foo` будет храниться в регионе `com.example.model.Foo`.

---

### 🚫 Параметр `include`

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D0%B0%D1%80%D0%B0%D0%BC%D0%B5%D1%82%D1%80-include)

|**Значение**|**Поведение**|
|---|---|
|`all`|Кэшируются все поля сущности, включая ленивые (`lazy`). _(по умолчанию)_|
|`non-lazy`|Исключаются ленивые поля из кэширования. Полезно для избежания затрат на тяжелые ассоциации.|

---

### ✅ Пример использования

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F)

```java
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "users", include = "all")
public class User {
  @Id
  private Long id;

  private String name;

  // ...
}
```

---

```
***** из методички *****
Это аннотация Hibernate, настраивающая тонкости кэширования объекта в кэше второго уровня Hibernate. @Cache принимает три параметра:
❖        include - имеет по умолчанию значение all и означающий кэширование всего объекта. Второе возможное значение - non-lazy, запрещает кэширование лениво загружаемых объектов. Кэш первого уровня не обращает внимания на эту директиву и всегда кэширует лениво загружаемые объекты.
❖        region - позволяет задать имя региона кэша для хранения сущности. Регион можно представить как разные области кэша, имеющие разные настройки на уровне реализации кэша. Например, можно было бы создать в конфигурации ehcache два региона, один с краткосрочным хранением объектов, другой с долгосрочным и отправлять часто изменяющиеся объекты в первый регион, а все остальные - во второй. Ehcache по умолчанию создает регион для каждой сущности с именем класса этой сущности, соответственно в этом регионе хранятся только эти сущности. К примеру, экземпляры Foo хранятся в Ehcache в кэше с именем “com.baeldung.hibernate.cache.model.Foo”.
❖        usage - задаёт стратегию одновременного доступа к объектам.
transactional
read-write
nonstrict-read-write
read-only
```
