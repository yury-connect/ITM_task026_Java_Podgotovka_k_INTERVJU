**Основные виды:**

**1. FixedThreadPool** - фиксированное число потоков
```java
Executors.newFixedThreadPool(10);
```

**2. CachedThreadPool** - динамическое расширение, потоки живут 60 сек
```java
Executors.newCachedThreadPool();
```

**3. SingleThreadExecutor** - один поток
```java
Executors.newSingleThreadExecutor();
```

**4. ScheduledThreadPool** - для планирования задач
```java
Executors.newScheduledThreadPool(5);
```

**5. ForkJoinPool** - для рекурсивных задач (разделяй и властвуй)
- Использует **work-stealing** (воровство задач)    
- Создается под количество ядер: `ForkJoinPool.commonPool()`    
- Для задач: `RecursiveTask` (с возвратом) и `RecursiveAction` (без)    
```java
ForkJoinPool pool = new ForkJoinPool();
pool.invoke(new MyRecursiveTask());
```

**Основной принцип:** переиспользование потоков вместо создания новых, управление очередью задач.
