# ForkJoinPool

**Spliterator** — это "итератор, который умеет делиться" (*Splitable Iterator*).  
**ForkJoinPool** — пул потоков для вып-я задач в стиле "разделяй и властвуй" (fork/join).  
 (`Java 7+`, пакет `java.util.concurrent`).

**Связь:** `Spliterator` создаёт задачи для `ForkJoinPool`, разбивая данные на части для параллельной обработки.
## Схема
```text
Данные (Collection/Stream)
    ↓
Spliterator.trySplit() → части данных
    ↓                  ↓
Задача 1             Задача 2
    ↓                  ↓
    Подзадача        Подзадача
    ↓                  ↓
   Join ←──────────────┘
ForkJoinPool (параллельное выполнение)
    ↓
Результат объединяется
```

---
### Как работает
```java
ForkJoinPool pool = new ForkJoinPool(4); // 4 потока
pool.invoke(new MyRecursiveTask()); // выполняем задачу
```
**Принцип:**
1. **Fork** — большая задача **делится** на мелкие подзадачи
2. **Join** — результаты подзадач **склеиваются** в общий результат

---
## !!! **Главная фишка** !!!

**Work Stealing** (кража работы):
- Каждый поток имеет свою очередь задач    
- Свободный поток может "украсть" задачу из очереди другого потока    
- → эффективная загрузка всех ядер CPU    
```text
Поток 1: [задача] [задача]       ← занят
Поток 2: [пусто]                 ← свободен
Поток 2 крадёт задачу у потока 1 → работа идёт параллельно
```

---
### Основные классы

|Класс|Назначение|
|---|---|
|**`ForkJoinPool`**|Сам пул потоков|
|**`RecursiveTask<V>`**|Задача, возвращающая результат|
|**`RecursiveAction`**|Задача без результата (void)|
```java
class SumTask extends RecursiveTask<Integer> {

    @Override
    protected Integer compute() {
        if (task маленький) return directCalculate();
        else {
            SumTask subtask1 = new SumTask(...);
            SumTask subtask2 = new SumTask(...);
            subtask1.fork();  // асинхронно
            return subtask2.compute() + subtask1.join(); // ждём результат
        }
    }
}
```

---
### Где используется

|Место|Что делает|
|---|---|
|**`parallelStream()`**|Автоматически использует `ForkJoinPool.commonPool()`|
|**`Arrays.parallelSort()`**|Сортирует большие массивы параллельно|
|**`CompletableFuture`**|Может использовать `ForkJoinPool`|
|**Свои рекурсивные задачи**|Вычисление чисел Фибоначчи, обход дерева и т.д.|
```java
// Пример: parallelStream использует ForkJoinPool
list.parallelStream()          // ForkJoinPool.commonPool()
    .map(x -> heavyOperation()) // выполняется параллельно
    .collect(toList());
```

---
### Виды пулов

|Пул|Описание|
|---|---|
|**`commonPool()`**|Общий для всех, создаётся автоматически (по умолчанию)|
|**`new ForkJoinPool(N)`**|Свой пул с N потоками|
```java
// Общий пул (используется Stream API)
ForkJoinPool common = ForkJoinPool.commonPool();

// Свой пул
ForkJoinPool custom = new ForkJoinPool(8); // 8 потоков
```

---
### Когда использовать

|Ситуация|Решение|
|---|---|
|Рекурсивные вычисления (Fibonacci, MergeSort)|✅ `RecursiveTask` + `ForkJoinPool`|
|Параллельная обработка больших данных|✅ `parallelStream()`|
|Простые задачи|❌ обычный `ExecutorService` проще|
|I/O-операции|❌ виртуальные потоки (Java 21+)|

---
### Итог
```text
ForkJoinPool = пул потоков для параллельных рекурсивных задач
     ↓
+ Work Stealing (эффективная загрузка CPU)
+ Fork/Join (разделяй и властвуй)
+ commonPool() для Stream API
```

---
