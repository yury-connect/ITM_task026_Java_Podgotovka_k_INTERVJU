# Как устроен ApplicationContext?

---
###### Близкая тема: [`ApplicationContext` -_xранение бинов_](/Documents/ITM_academy/itm06_Spring/additionally/ApplicationContext-xранение_бинов.md)
###### Близкая тема: [Расскажите про ApplicationContext - xранение бинов и BeanFactory, чем отличаются? В каких случаях что стоит использовать?](/Documents/ITM_academy/itm06_Spring/solutions/03_ApplicationContext&BeanFactory.md)
## 🏛️ Простая метафора

> **ApplicationContext** — это **директор ресторана**. Он не готовит сам, но:> 
> - Знает все рецепты (`BeanDefinition`)
> - Управляет поварами (`BeanFactory`)
> - Решает проблемы (циклические зависимости)
> - Публикует объявления (события)     

---
## 📊 Главное запомнить (90% ответа на собеседовании)
```text
ApplicationContext = Фасад (обертка) над DefaultListableBeanFactory
```

|Компонент|Что делает|
|---|---|
|**ApplicationContext** (интерфейс)|Объявляет возможности: события, интернационализация, ресурсы|
|**DefaultListableBeanFactory** (внутри)|Реально **хранит и создает бины**|
```java
// Все getBean() делегируются сюда:
context.getBean("user")  
    ↓
defaultListableBeanFactory.getBean("user")
```

---
## 🗂️ Трехуровневый кеш (решение циклических зависимостей)

**Где хранится:** в `DefaultSingletonBeanRegistry` (родитель `DefaultListableBeanFactory`)

|Уровень|Название|Что лежит|Аналогия|
|---|---|---|---|
|**1**|`singletonObjects`|Готовые бины|Готовое блюдо на выдачу|
|**2**|`earlySingletonObjects`|Ранние ссылки (объект есть, поля пустые)|Полуфабрикат|
|**3**|`singletonFactories`|Фабрики для создания early-ссылок|Рецепт + повар|

### Как работает (3 шага):
```text
1. Бин создан (new) → кладем фабрику в 3-й кеш
2. Обнаружен цикл → берем early reference из 2-го или 3-го кеша
3. Бин готов → перемещаем в 1-й кеш, чистим 2-й и 3-й
```

---
## 🎯 Минимальный набор для ответа

> **"ApplicationContext** — это интерфейс-фасад. Внутри любого современного контекста (например, `AnnotationConfigApplicationContext`) лежит `DefaultListableBeanFactory`, которая реально хранит бины.
> 
> Бинарники `singleton` лежат в **трехуровневом кеше**:> 
> 1. `singletonObjects` — готовые
> 2. `earlySingletonObjects` — ранние ссылки
> 3. `singletonFactories` — фабрики
> 
> Это решает проблему циклических зависимостей. 
> А еще контекст умеет события, интернационализацию и
>  сам преинициализирует все `singleton`-бины при старте."

---
## 🧠 Шпаргалка для запоминания

|Хотите запомнить|Используйте|
|---|---|
|Суть|**Фасад над фабрикой бинов**|
|Главный класс внутри|`DefaultListableBeanFactory`|
|3 уровня кеша|1-готовые, 2-ранние, 3-фабрики|
|Популярная реализация|`AnnotationConfigApplicationContext`|
|Ключевой метод|`refresh()` — запускает весь механизм|

---
## ⚠️ Один важный нюанс (исправление)

В исходном ответе сказано: _"ApplicationContext не хранит бины сам"_. Это **не совсем точно**: контекст их **не создает** (создает фабрика), но он **владеет** фабрикой → по сути хранит через нее. Лучше говорить:

> **ApplicationContext делегирует хранение и создание бинов внутренней DefaultListableBeanFactory.**

---
## 📋 Итог: схема запоминания
```text
ApplicationContext (фасад)
       │
       ├── DefaultListableBeanFactory (реальный контейнер)
       │      │
       │      ├── BeanDefinitionRegistry (рецепты)
       │      └── DefaultSingletonBeanRegistry (трехуровневый кеш)
       │
       └── Доп. возможности (события, i18n, ресурсы)
```

**Резюме одной фразой:**  
_ApplicationContext — это умная обертка вокруг фабрики, которая хранит бины в трех кешах и добавляет enterprise-фичи._

---
###### Близкая тема: [`ApplicationContext` -_xранение бинов_](/Documents/ITM_academy/itm06_Spring/additionally/ApplicationContext-xранение_бинов.md)
###### Близкая тема: [Расскажите про ApplicationContext - xранение бинов и BeanFactory, чем отличаются? В каких случаях что стоит использовать?](/Documents/ITM_academy/itm06_Spring/solutions/03_ApplicationContext&BeanFactory.md)
