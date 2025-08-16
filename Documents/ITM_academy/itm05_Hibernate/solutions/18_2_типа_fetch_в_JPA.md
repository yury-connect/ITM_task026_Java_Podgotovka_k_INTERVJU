# Какие два типа fetch стратегии в JPA вы знаете?

---
## Какие два типа `fetch` стратегии в `JPA` вы знаете?

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#%D0%BA%D0%B0%D0%BA%D0%B8%D0%B5-%D0%B4%D0%B2%D0%B0-%D1%82%D0%B8%D0%BF%D0%B0-fetch-%D1%81%D1%82%D1%80%D0%B0%D1%82%D0%B5%D0%B3%D0%B8%D0%B8-%D0%B2-jpa-%D0%B2%D1%8B-%D0%B7%D0%BD%D0%B0%D0%B5%D1%82%D0%B5)

### ⚡ Стратегии загрузки (_Fetch Types_) в _JPA_

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D1%81%D1%82%D1%80%D0%B0%D1%82%D0%B5%D0%B3%D0%B8%D0%B8-%D0%B7%D0%B0%D0%B3%D1%80%D1%83%D0%B7%D0%BA%D0%B8-fetch-types-%D0%B2-jpa)

JPA определяет **два типа стратегий выборки (_fetching_)** для ассоциированных сущностей:

|**Тип стратегии**|**Описание**|**Применяется по умолчанию к:**|
|---|---|---|
|`LAZY` 💤|Загрузка данных **откладывается** до первого обращения к ним.  <br>Использует **прокси-объекты**.|`@OneToMany`, `@ManyToMany,` `@ElementCollection`|
|`EAGER` ⚡|Данные загружаются **немедленно**,  <br>вместе с основной сущностью.|`@OneToOne`, `@ManyToOne`, `@Basic`|

### 📝 Важно:

[](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/by_questions/ITM/ITM05_Hibernate/Hibernate.md#-%D0%B2%D0%B0%D0%B6%D0%BD%D0%BE)

> Хотя `LAZY` является рекомендованной стратегией для многих ассоциаций, провайдер _JPA_ может переопределить её, особенно при отсутствии поддержки прокси или в специфических конфигурациях.

---

```
***** из методички *****
1) LAZY — Hibernate может загружать данные не сразу, 
    а при первом обращении к ним, но так как это необязательное требование, 
    то Hibernate имеет право изменить это поведение и загружать их сразу. 
    Это поведение по умолчанию для полей, аннотированных @OneToMany, @ManyToMany и @ElementCollection. 
    В объект загружается прокси lazy-поля.
2) EAGER — данные поля будут загруженны немедленно. 
    Это поведение по умолчанию для полей, аннотированных @Basic, @ManyToOne и @OneToOne.
```
