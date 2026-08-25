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

## 📊 **Сценарий работы (на примере)**

**Настройки:**
```java
corePoolSize = 0
maximumPoolSize = 10
keepAliveTime = 60s
queue = SynchronousQueue (не хранит задачи)
```

|Шаг|Действие|Потоков создано|Что происходит|
|---|---|---|---|
|1|Запустили пул|**0**|—|
|2|Пришла задача №1|**1**|Создан поток для задачи|
|3|Пришла задача №2 (пока №1 выполняется)|**2**|Создан новый поток|
|4|Пришла задача №11 (первые 10 заняты)|**10**|Достигнут `maximumPoolSize`|
|5|Пришла задача №12|**10**|❌ **RejectedExecutionException**|

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

> **`cachedThreadPool` — безлимитный «ковбой» (♾️ потоков).**
> 
> **`ThreadPoolExecutor` — ваш «диспетчер» с полным контролем.**

---
