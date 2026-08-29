## 🧵 Runnable, Callable и ExecutorService

Это **три кита** асинхронного программирования в Java. Они работают в связке: `ExecutorService` — это **менеджер потоков**, а `Runnable`/`Callable` — это **задачи**, которые он выполняет.

---
## 📦 1. Runnable vs Callable (в чем разница)

|Характеристика|**Runnable**|**Callable<V>**|
|---|---|---|
|**Возвращаемое значение**|`void` (ничего не возвращает)|**Возвращает результат** (тип `V`)|
|**Исключения**|**Не может** бросать checked исключения (только unchecked)|**Может** бросать checked исключения (`Exception`)|
|**Метод**|`void run()`|`V call() throws Exception`|
|**Когда использовать**|Простые задачи без результата (логирование, запись в файл)|Задачи, где нужен результат (запрос к БД, вычисление, вызов API)|
|**Получение результата**|❌ Нет (только через `Future<?>`)|✅ Да (`Future<V>.get()`)|

---
### 📝 Примеры

**Runnable (без результата):**
```java
Runnable task = () -> System.out.println("Hello from Runnable");
executor.execute(task); // Ничего не возвращает
```

**Callable (с результатом):**

```java
Callable<Integer> task = () -> {
    Thread.sleep(1000);
    return 42; // Результат вычисления
};
Future<Integer> future = executor.submit(task);
Integer result = future.get(); // 42
```

---

## ⚙️ 2. ExecutorService — Пул потоков

`ExecutorService` — это **сервис**, который управляет пулом потоков. Вместо того чтобы создавать `Thread` вручную (`new Thread().start()`), ты передаешь задачи в пул, а он сам распределяет их по потокам.

### 📌 Как создать ExecutorService:
```java
// Фиксированный пул из 5 потоков
ExecutorService executor = Executors.newFixedThreadPool(5);

// Кэширующий пул (создает потоки по мере необходимости)
ExecutorService executor = Executors.newCachedThreadPool();

// Одиночный поток (все задачи выполняются последовательно)
ExecutorService executor = Executors.newSingleThreadExecutor();

// Пул для отложенных задач
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
```

---

### 📋 Основные методы ExecutorService

|Метод|Что делает|Принимает|Возвращает|
|---|---|---|---|
|**`execute(Runnable)`**|Запускает задачу **без результата**|`Runnable`|`void`|
|**`submit(Runnable)`**|Запускает задачу, возвращает `Future` (но `.get()` вернет `null`)|`Runnable`|`Future<?>`|
|**`submit(Callable)`**|Запускает задачу, возвращает `Future` с результатом|`Callable<V>`|`Future<V>`|
|**`invokeAll(Collection<Callable>)`**|Запускает **все** задачи и ждет завершения всех|`Collection<Callable>`|`List<Future<V>>`|
|**`invokeAny(Collection<Callable>)`**|Запускает все, но возвращает **первый успешный** результат|`Collection<Callable>`|`V` (результат)|
|**`shutdown()`**|Больше не принимает новые задачи, но завершает текущие|—|`void`|
|**`shutdownNow()`**|Принудительно останавливает все задачи|—|`List<Runnable>` (незапущенные)|
|**`awaitTermination(...)`**|Блокируется до завершения всех задач или таймаута|—|`boolean`|

---

## 🔄 Как это работает вместе (жизненный цикл)
```text
1. Создаем ExecutorService
         ↓
2. Отправляем задачу (submit / execute)
         ↓
3. ExecutorService помещает задачу в очередь
         ↓
4. Свободный поток берет задачу из очереди
         ↓
5. Если это Runnable → выполняет run()
   Если это Callable → выполняет call() и сохраняет результат в Future
         ↓
6. Мы получаем Future и вызываем .get() (блокируется, пока результат не готов)
         ↓
7. После всех задач вызываем shutdown() и awaitTermination()
```

---
## 💡 Полный пример (Runnable + Callable + ExecutorService)
```java
public class ExecutorServiceExample {
    public static void main(String[] args) throws Exception {
    
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // 1. Runnable (без результата)
        Runnable logTask = () -> {
            System.out.println("Logging from " 
	            + Thread.currentThread().getName());
        };
        
        // 2. Callable (с результатом)
        Callable<Integer> calculateTask = () -> {
            Thread.sleep(1000);
            return 42;
        };
        
        // Запускаем Runnable
        executor.execute(logTask);
        
        // Запускаем Callable и получаем Future
        Future<Integer> future = executor.submit(calculateTask);
        
        // Блокируемся до получения результата
        Integer result = future.get();
        System.out.println("Результат вычисления: " + result);
        
        // Завершаем пул
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}
```

---
## ⚠️ Важные нюансы (собеседование)

### 1️⃣ `Future.get()` — блокирующий вызов

- Если результат еще не готов, `get()` **блокирует** текущий поток.
    
- Используй `get(timeout)` чтобы не ждать бесконечно:
```java
Integer result = future.get(2, TimeUnit.SECONDS);
```

### 2️⃣ Исключения в Callable

- Если `call()` выбросил исключение, `future.get()` завершится с `ExecutionException`.
    
- Реальное исключение можно получить через `e.getCause()`.

### 3️⃣ Разница между `execute()` и `submit(Runnable)`

- `execute()` — ничего не возвращает (просто выполняет).
    
- `submit(Runnable)` — возвращает `Future<?>`, по которому можно отследить статус выполнения (через `isDone()`, `isCancelled()`).

### 4️⃣ shutdown vs shutdownNow

- `shutdown()` — не принимает новые задачи, но дает завершиться текущим.
    
- `shutdownNow()` — пытается прервать все запущенные задачи (через `interrupt()`) и возвращает список невыполненных.

---
## 🎤 Короткий ответ на собеседовании (одной фразой)

> _«Runnable — это задача без результата, Callable — задача с результатом (и возможностью бросать checked исключения). ExecutorService — это пул потоков, который управляет выполнением таких задач. Мы отправляем задачи через `execute()` (для Runnable) или `submit()` (для Callable), получаем `Future`, через который потом забираем результат или проверяем статус. Пул нужно корректно завершать через `shutdown()` и `awaitTermination()`.»_

---
## 🧠 Шпаргалка

|Сценарий|Использовать|
|---|---|
|Напечатать логи|`Runnable` + `execute()`|
|Сделать запрос к БД и получить результат|`Callable` + `submit()`|
|Выполнить 10 задач и дождаться всех результатов|`invokeAll()`|
|Выполнить 10 задач, но взять результат первой успешной|`invokeAny()`|
|Запустить задачу с задержкой|`ScheduledExecutorService.schedule()`|

---
