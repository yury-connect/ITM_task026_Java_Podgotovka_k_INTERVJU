# ⚖️ **cachedThreadPool** vs **ThreadPoolExecutor**

|Параметр|**newCachedThreadPool()**|**ThreadPoolExecutor (ручной)**|
|---|---|---|
|**Гибкость**|❌ Жестко зашито|✅ Полный контроль|
|**Максимум потоков**|`Integer.MAX_VALUE` (♾️)|**Вы задаёте**|
|**Очередь**|`SynchronousQueue` (0 ёмкости)|**Вы выбираете**|
|**Время жизни**|60 секунд|**Вы задаёте**|

---
## 🏗️ **Как это выглядит в коде**

### 1️⃣ **cachedThreadPool** (*стандартный*)
```java
ExecutorService pool = Executors.newCachedThreadPool();
```

**Внутри:**
```java
new ThreadPoolExecutor(
    0,                     // corePoolSize
    Integer.MAX_VALUE,     // maximumPoolSize — 🔴 БЕЗ ЛИМИТА!
    60L, TimeUnit.SECONDS,
    new SynchronousQueue<>()
);
```

---
### 2️⃣ **ThreadPoolExecutor** (*ручной с лимитом*)
```java
ExecutorService pool = new ThreadPoolExecutor(
    0,                     // corePoolSize
    10,                    // maximumPoolSize — ✅ ВАШ ЛИМИТ
    60L, TimeUnit.SECONDS,
    new SynchronousQueue<>()
);
```

---
## 📊 **Сценарий работы** (*на примере*)

**Настройки:**
```java
corePoolSize = 0
maximumPoolSize = 10
keepAliveTime = 60s
queue = SynchronousQueue // (не хранит задачи)
```

| Шаг | Действие                               | Потоков создано | Что происходит                   |
|:---:|:-------------------------------------- |:---------------:|:-------------------------------- |
| 1   | Запустили пул                          | **0**           | —                                |
| 2   | Пришла задача №1                       | **1**           | Создан поток для задачи          |
| 3   | Пришла задача №2 (пока №1 выполняется) | **2**           | Создан новый поток               |
| 4   | Пришла задача №11 (первые 10 заняты)   | **10**          | Достигнут `maximumPoolSize`      |
| 5   | Пришла задача №12                      | **10**          | ❌ **RejectedExecutionException** |

---
## 📋 **Когда что использовать**

| Сценарий                                           | Решение                        |
| -------------------------------------------------- | ------------------------------ |
| **Короткие задачи, не боитесь бесконечного роста** | `newCachedThreadPool()`        |
| **Нужен контроль над числом потоков**              | `ThreadPoolExecutor` вручную   |
| **Пиковые нагрузки могут быть высокими**           | `ThreadPoolExecutor` с лимитом |
| **Хотите защиту от `OutOfMemoryError`**            | `ThreadPoolExecutor` с лимитом |

---
## 🧠 **Вывод одной фразой**

> **`cachedThreadPool` — безлимитный «ковбой»** *(♾️ потоков)*.
> 
> **`ThreadPoolExecutor` — ваш «диспетчер» с полным контролем.**

---
---
---
#### *Обзор от ИИ*

`CachedThreadPool` под капотом использует класс `ThreadPoolExecutor`, создавая его с конкретными параметрами.

`Executors.newCachedThreadPool()` — это готовый метод-фабрика, а `ThreadPoolExecutor` — гибкий класс для ручной настройки любых пулов потоков. [](https://medium.com/@praveengaddam319/cachedthreadpool-vs-fixedthreadpool-choosing-the-right-thread-pool-for-your-workload-in-java-956bd0e80384)

## Как устроен `CachedThreadPool` через `ThreadPoolExecutor`
При вызове `Executors.newCachedThreadPool()` создается `ThreadPoolExecutor` со следующей конфигурацией:
- **`corePoolSize`** = `0` (нет постоянных потоков).
- **`maximumPoolSize`** = `Integer.MAX_VALUE` (почти бесконечное число потоков).
- **`keepAliveTime`** = `60 секунд` (время жизни простаивающего потока перед удалением).
- **`unit`** = `TimeUnit.SECONDS`.
- **`workQueue`** = `SynchronousQueue` (очередь без буфера, где каждая задача сразу передается потоку). [](https://stackoverflow.com/questions/949355/executors-newcachedthreadpool-versus-executors-newfixedthreadpool)

## Главные отличия и особенности
- **Создание потоков:**
    - `CachedThreadPool` создает новый поток для каждой новой задачи, если все существующие заняты. Если есть свободный спящий поток (прошло менее 60 секунд), он переиспользуется.
    - `ThreadPoolExecutor` позволяет строго задать минимальное (`corePoolSize`) и максимальное (`maximumPoolSize`) число потоков, а также выбрать тип очереди (`LinkedBlockingQueue`, `ArrayBlockingQueue` и др.). [](https://stackoverflow.com/questions/27987724/threadpoolexecutor-similar-to-executors-cachedthreadpool-but-with-max-threads-an)
	
- **Очередь задач:**
    - В `CachedThreadPool` используется `SynchronousQueue`, у которой емкость равна нулю. Задача не встает в очередь, она либо падает на свободный поток, либо создает новый.
    - В `ThreadPoolExecutor` очередь может быть как с размером, так и бесконечной. Задачи могут ожидать исполнения, если все потоки заняты. [](https://stackoverflow.com/questions/1800317/impossible-to-make-a-cached-thread-pool-with-a-size-limit)
	
- **Риски:**
    - `CachedThreadPool` опасен при резком росте числа долгих или входящих задач: количество потоков может вырасти до `Integer.MAX_VALUE`, что приведет к переполнению памяти (`OutOfMemoryError`) или исчерпанию ресурсов ОС (`OOM: native thread`).
    - `ThreadPoolExecutor` с правильной очередью и лимитом (`maximumPoolSize`) защищает приложение от падения при пиковых нагрузках. [](https://stackoverflow.com/questions/949355/executors-newcachedthreadpool-versus-executors-newfixedthreadpool)

## Что выбрать?
- Используйте `CachedThreadPool` только для коротких, быстрых и асинхронных фоновых задач, когда нагрузка предсказуема и кратковременна.
	
- Используйте `ThreadPoolExecutor` для продакшн-систем со средней или высокой нагрузкой, чтобы контролировать максимальное число потоков и избежать падения сервера. [](https://stackoverflow.com/questions/949355/executors-newcachedthreadpool-versus-executors-newfixedthreadpool)

---

