Ниже приведен расширенный список (**100+ вопросов**), ранжированный по условной частоте задавания от самых частых («Святая троица») до редких, но проверяющих глубину.

---
## Группа 1: База и «Святая троица» (99% собеседований)
_Эти вопросы задают почти всегда._

1. Что такое процесс и поток? Чем они отличаются в Java?    
2. Как создать поток? Назови все способы (Thread, Runnable, ExecutorService).    
3. В чем разница между `Thread.start()` и `Thread.run()`?    
4. Что такое `join()`? Зачем он нужен?    
5. Что такое `sleep()`? Чем отличается от `wait()`?    
6. Что такое `yield()`? Есть ли в этом практический смысл?    
7. Что такое `interrupt()`? Как правильно обрабатывать прерывания?    
8. В чем разница между `wait()` и `sleep()`? (Глобальный топ вопрос).    
9. Почему `wait()`, `notify()`, `notifyAll()` определены в `Object`, а не в `Thread`?    
10. Что такое монитор (monitor) в Java?    
11. Что делает `synchronized`? На что можно повесить синхронизацию (метод, блок, статический метод)?    
12. В чем разница между синхронизацией на `this` и на отдельном `private final Object lock`?    
13. Что такое изменчивость (visibility) переменных? Как `volatile` решает эту проблему?    
14. Чем отличается `volatile` от `synchronized`?    
15. Что такое атомарность? Приведет ли `volatile` к атомарности `i++`?    
16. Что такое `AtomicInteger`, `AtomicBoolean` и их принцип работы (CAS)?    
17. Что такое deadlock (взаимная блокировка)? Условия возникновения.    
18. Как обнаружить deadlock в работающем приложении (jstack, VisualVM)?    
19. Что такое livelock и starvation (голодание)?    
20. Классы `CountDownLatch`, `CyclicBarrier`, `Semaphore`: зачем нужны и в чем разница?    
21. Что такое `Exchanger`?    
22. Интерфейс `Lock` (`ReentrantLock`). Чем отличается от `synchronized`?    
23. Что такое `tryLock()`?    
24. Что такое `ReadWriteLock`? В чем его преимущество?    
25. Что такое `ThreadLocal`? Где используется (например, `SimpleDateFormat`)? Проблема утечки памяти.    

## Группа 2: Коллекции и Concurrency Utilities (Задают очень часто, Middle обязан знать)

26. Что не так с `HashMap` в многопоточности? (Бесконечный цикл в Java 7, потеря данных).    
27. Чем отличается `Hashtable` от `HashMap`? Почему `Hashtable` устарел?    
28. Что такое `ConcurrentHashMap`? Как устроен внутри (Java 8+: CAS + синхронизация на бакетах)?    
29. В чем отличие `ConcurrentHashMap` от `Collections.synchronizedMap()`?    
30. Что такое `CopyOnWriteArrayList`? Когда использовать? (Мало пишем, много читаем).    
31. Какие есть реализации блокирующих очередей? (`ArrayBlockingQueue`, `LinkedBlockingQueue`, `PriorityBlockingQueue`, `SynchronousQueue`).    
32. Чем отличается `BlockingQueue` от обычной очереди?    
33. Что такое `ConcurrentLinkedQueue`? В чем суть неблокирующего алгоритма?    
34. Коллекция `ConcurrentSkipListMap` (структура и применение).    
35. Что такое `ForkJoinPool`? В чем отличие от обычного `ThreadPoolExecutor`?    
36. Принцип работы `ForkJoinPool` (воровство задач — work-stealing).    

## Группа 3: Executors и Thread Pools (Очень высокая частота)
37. Зачем нужны пулы потоков?    
38. Интерфейсы `Executor`, `ExecutorService`, `ScheduledExecutorService`.    
39. Какие стандартные пулы предлагает `Executors`?    
    - `newCachedThreadPool`        
    - `newFixedThreadPool`        
    - `newSingleThreadExecutor`        
    - `newScheduledThreadPool`        
    - `newWorkStealingPool`        
40. В чем проблема пула `Executors.newFixedThreadPool` с очередью? (Очередь без границ).    
41. В чем проблема `Executors.newCachedThreadPool`? (Риск создания бесконечного числа потоков).    
42. Как правильно создавать пулы напрямую через `ThreadPoolExecutor`? Параметры конструктора.    
43. Что такое `corePoolSize`, `maxPoolSize`, `keepAliveTime`, `workQueue`?    
44. Как пул решает: создать новый поток или положить задачу в очередь? (Правило: core -> queue -> max).    
45. Какие бывают стратегии отказа (`RejectedExecutionHandler`)?    
    - `AbortPolicy`, `CallerRunsPolicy`, `DiscardPolicy`, `DiscardOldestPolicy`.        
46. Что такое `Future`? Как получить результат?    
47. Что такое `Callable`? Чем отличается от `Runnable`?    
48. Методы `submit()` и `execute()`: разница.    
49. Что такое `CompletableFuture`? (Сейчас спрашивают очень часто).    
50. Цепочки в `CompletableFuture`: `thenApply`, `thenAccept`, `thenRun`.    
51. Как объединить несколько `CompletableFuture` (`allOf`, `anyOf`).    

## Группа 4: Память, JMM и Продвинутые механизмы (Средняя частота, хард)
52. Что такое Java Memory Model (JMM)?    
53. Что такое happens-before? Приведи примеры правил (volatile, synchronized, start, join).    
54. Что такое reordering (переупорядочивание инструкций)? Как JMM его ограничивает?    
55. Как работает `LongAdder`? Чем лучше `AtomicLong` при высокой конкуренции?    
56. Что такое псевдо-шаринг (False Sharing)? Как бороться (`@Contended`)?    
57. Класс `Phaser` (более гибкая версия `CyclicBarrier`/`CountDownLatch`).    
58. Что такое `StampedLock`? Чем лучше `ReadWriteLock`? (Оптимистичное чтение).    
59. Что такое `VarHandle` (Java 9+)? Зачем он нужен, если есть `Unsafe` и Atomic-ы?    
60. Что такое `ThreadFactory`? Зачем кастомизировать (имена, демоны, группы)?    

## Группа 5: Тонкости синхронизации и паттерны (Средняя частота)
61. Что такое _Double Checked Locking_ (DCL) для Singleton? Работает ли он в Java?    
62. Почему DCL сломан без `volatile`? (Из-за reordering).    
63. Как правильно писать Singleton в многопоточном окружении (enum, holder class, DCL с volatile)?    
64. Что такое _Busy Spin_ (пустой цикл в ожидании)? Когда это оправдано?    
65. Что такое _Thread Pool_ для IO-операций vs CPU-bound задач? Как выбрать количество потоков?    
66. Формула оптимального размера пула: `N_threads = N_cpu * U_cpu * (1 + W/C)`.    
67. Что такое `ThreadGroup`? Стоит ли использовать?    
68. Как остановить поток извне? (Проблема `Thread.stop()` deprecated).    
69. Что такое _Context Switching_? Чем он дорог?    

## Группа 6: Атомарные и нетривиальные классы (Ниже средней, но для Middle норм)
70. Принцип работы CAS (Compare-And-Swap). Что такое ABA-проблема?    
71. Как решается ABA-проблема? (`AtomicStampedReference`,`AtomicMarkableReference`). 
72. Класс `AtomicIntegerArray`.    
73. `AtomicReference` и `AtomicReferenceFieldUpdater`.    
74. `LongAccumulator` и `DoubleAccumulator`.    

## Группа 7: GUI, устаревшее и редкое (Проверка эрудиции)
75. Что такое _SwingWorker_? (Мертвая тема, но иногда спрашивают).    
76. Что такое _Event Dispatch Thread_ (EDT) в Swing/JavaFX?    
77. Что такое `Thread.dumpStack()`?    
78. Приоритеты потоков (`setPriority`). Работают ли они на уровне ОС? (Спойлер: не особо).    
79. Что такое _Daemon_ поток? Чем отличается от user потока? Когда завершается JVM?
80. Метод `runFinalizersOnExit` (почему это зло?).    
81. Что такое _Shutdown Hook_ (`Runtime.addShutdownHook`)? Где применяется?    
82. Интерфейс `Thread.UncaughtExceptionHandler`. Зачем нужен?    

## Группа 8: Проблемы реального мира и отладка (Хороший тон для Middle+)
83. Как отладить многопоточное приложение? Сложности воспроизведения.    
84. Как найти deadlock через код (ThreadMXBean)?    
85. Что такое _Thread Dump_? Как его снять и как читать?    
86. Какие есть тулзы для анализа многопоточности (jconsole, jstack, VisualVM, Java Flight Recorder)?    
87. Почему `System.out.println` может менять видимость переменных? (Из-за `synchronized` внутри PrintStream).    
88. Может ли `HashMap` встать в бесконечный цикл при `get()`? (В Java 7 — да).    
89. Что значит _thread-safe_, _not thread-safe_, _conditionally thread-safe_ (как `Vector`?).    
90. Что такое _race condition_? Приведите простой пример кода.    
91. Может ли `synchronized` метод быть статическим и нестатическим одновременно? Блокировки разные или общие?    

## Группа 9: Вопросы «Со звездочкой» (Очень редко, но убивают наповал)
92. Почему в Java 9-11 `Thread.stop()`, `suspend()`, `resume()` deprecated с точки зрения JMM?    
93. Как работает `sun.misc.Unsafe`? Почему его не рекомендуют использовать?
94. Что такое _SEV (Structured Event Variable)_ или _Phaser_ в деталях?    
95. Реализация `AbstractQueuedSynchronizer` (AQS) — основа `ReentrantLock`, `Semaphore`, `CountDownLatch`. Можешь объяснить принцип (CLH очередь)?
96. В чем разница между _barging_ и _fairness_ в `ReentrantLock`?    
97. Что такое _LockSupport.park()_ / _unpark()_?    
98. Проблема _Priority Inversion_ (инверсия приоритетов). Как решается в Java (или не решается)?    
99. Что такое _Memory Barrier_ (LoadLoad, StoreStore и т.д.)? Как `volatile` компилируется в байткод и машинный код?    
100. **Каверзный финал:** Объясни на пальцах, почему `x = 0;` в одном потоке и `if (x == 0) print();` в другом, даже с `volatile`, не дают 100% гарантии порядка без `happens-before`?
## Группа 10: Java 21+ (Virtual Threads) — Сейчас важно для Middle
101. Что такое Virtual Threads (Project Loom)?    
102. В чем разница между платформенным (kernel) потоком и виртуальным?    
103. Как создать виртуальный поток? (`Thread.startVirtualThread`, `Executors.newVirtualThreadPerTaskExecutor()`).    
104. Как работают виртуальные потоки под капотом (continuations / mount / unmount)?    
105. При каких операциях виртуальный поток отмонтируется (паркуется) от носителя? (Блокирующие IO, `synchronized`).    
106. Почему пулы виртуальных потоков не нуждаются в «пулинге»?    
107. Проблема: использование `synchronized` с виртуальными потоками (пиннинг). Как бороться (ReentrantLock)?    

---
### Итоговый совет для подготовки:

1. **Топ-5 для заучивания:** разница `wait`/`sleep`, `volatile` vs `synchronized`, устройство `ConcurrentHashMap`, параметры `ThreadPoolExecutor`, happens-before.
    
2. **Практика:** Обязательно напишите код с deadlock, поиграйте с `CountDownLatch`, посмотрите дамп потоков (jstack). Теория без практики на Middle собеседовании проваливается на ура.
    
3. **Virtual Threads:** Если позиция требует Java 21+, готовьте вопросы 101-107 как «очень частые».
