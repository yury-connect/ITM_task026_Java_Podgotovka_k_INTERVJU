Java Core — это фундамент, и здесь можно (и нужно) перебирать действительно много. Я постарался охватить все ключевые аспекты: от базового синтаксиса до многопоточности, JMM и новых фич. В итоге получилось **~190 вопросов**, разбитых по группам и ранжированных по частоте встречаемости.

---

### Группа 1: Основы синтаксиса и принципы ООП (99% собеседований)

_Без этого вообще никуда — спрашивают всегда._

1. **Назови основные особенности языка Java (платформонезависимость, объектно-ориентированность, сборщик мусора, многопоточность, JIT-компиляция).** [](https://medium.com/@pandyahimanshu09041995/50-most-common-java-interview-questions-and-answers-for-2025-28ed477ae23f)    
2. **Что такое JVM, JRE, JDK? Чем отличаются?** [](https://medium.com/@pandyahimanshu09041995/50-most-common-java-interview-questions-and-answers-for-2025-28ed477ae23f)    
3. **Чем отличается `float` от `double`?**    
4. **Что такое приведение типов? Какие бывают типы преобразований (явное/неявное, расширение/сужение)?**    
5. **Что такое стек (Stack) и куча (Heap)? Что где хранится?** [](https://medium.com/@pandyahimanshu09041995/50-most-common-java-interview-questions-and-answers-for-2025-28ed477ae23f)    
6. **Расскажи о четырех столпах ООП (Инкапсуляция, Наследование, Полиморфизм, Абстракция). Приведи примеры.** [](https://medium.com/@pandyahimanshu09041995/50-most-common-java-interview-questions-and-answers-for-2025-28ed477ae23f)
7. **Чем отличается абстракция от инкапсуляции?** [](https://medium.com/@pandyahimanshu09041995/50-most-common-java-interview-questions-and-answers-for-2025-28ed477ae23f)    
8. **Что такое перегрузка (overloading) методов? Это compile-time или runtime полиморфизм?** [](https://medium.com/@pandyahimanshu09041995/50-most-common-java-interview-questions-and-answers-for-2025-28ed477ae23f)    
9. **Что такое переопределение (overriding) методов? Какие правила (модификатор доступа, возвращаемый тип, исключения)?** [](https://medium.com/@pandyahimanshu09041995/50-most-common-java-interview-questions-and-answers-for-2025-28ed477ae23f)    
10. **Чем отличается перегрузка от переопределения?** [](https://medium.com/@pandyahimanshu09041995/50-most-common-java-interview-questions-and-answers-for-2025-28ed477ae23f)    
11. **Что такое интерфейс? Какие модификаторы доступа у методов интерфейса по умолчанию (до Java 8)?** [](https://medium.com/@pandyahimanshu09041995/50-most-common-java-interview-questions-and-answers-for-2025-28ed477ae23f)    
12. **Что такое абстрактный класс? Чем отличается от интерфейса?** [](https://medium.com/@pandyahimanshu09041995/50-most-common-java-interview-questions-and-answers-for-2025-28ed477ae23f)    
13. **Что такое конструктор? Чем отличается конструктор по умолчанию от конструктора копирования?** [](https://medium.com/@pandyahimanshu09041995/50-most-common-java-interview-questions-and-answers-for-2025-28ed477ae23f)    
14. **Что такое `final`? (final переменная, final метод, final класс).** [](https://medium.com/@pandyahimanshu09041995/50-most-common-java-interview-questions-and-answers-for-2025-28ed477ae23f)    
15. **Что такое `static`? Статические переменные, методы, блоки и вложенные классы.** [](https://medium.com/@pandyahimanshu09041995/50-most-common-java-interview-questions-and-answers-for-2025-28ed477ae23f)    
16. **Можно ли переопределить `static` метод? (Нет, это сокрытие — hiding).**    
17. **Что такое класс-одиночка (Singleton)? Как его реализовать потокобезопасно?** [](https://medium.com/@pandyahimanshu09041995/50-most-common-java-interview-questions-and-answers-for-2025-28ed477ae23f)
18. **Что такое неизменяемый (immutable) класс? Как создать? Приведи пример (`String`, `LocalDate`).**    
19. **Что такое блоки инициализации (instance и static)? В каком порядке они выполняются?**    
20. **Что такое рекурсия? Какие проблемы могут возникнуть (`StackOverflowError`)?**

### Группа 2: Работа со строками (Очень высокая частота)
21. **Что такое `String` в Java? Как устроен внутри (char[] / byte[])?**    
22. **Почему `String` immutable (неизменяемый)? Назови причины (безопасность, пул строк, кэширование хэш-кода, потокобезопасность).** [](https://medium.com/@pandyahimanshu09041995/50-most-common-java-interview-questions-and-answers-for-2025-28ed477ae23f)[](https://90zuneth99.medium.com/56-essential-java-interview-questions-and-answers-2026-expanded-version-with-examples-598b0ff1e5fd) 
23. **Что такое пул строк (String Pool)? Где он находится (до Java 7 и после)?**    
24. **Что делает оператор `+` при конкатенации строк? Во что компилируется?**    
25. **Что такое `StringBuilder`? В чем отличие от `StringBuffer`?** [](https://90zuneth99.medium.com/56-essential-java-interview-questions-and-answers-2026-expanded-version-with-examples-598b0ff1e5fd)
26. **Чем отличается `StringBuilder` от `String`? Когда что использовать?**
27. **Что такое `intern()`? Зачем нужен?**
28. **Что выведет код: `String s1 = "hello"; String s2 = new String("hello"); System.out.println(s1 == s2);`?** [](https://90zuneth99.medium.com/56-essential-java-interview-questions-and-answers-2026-expanded-version-with-examples-598b0ff1e5fd)
29. **Методы `String`: `substring()`, `split()`, `join()`, `format()`, `matches()`.

### Группа 3: Класс Object и основные методы (Очень высокая частота)

30. **Корневой класс в Java? (`Object`).**
    
31. **Зачем нужен метод `equals()`? Контракт `equals()` (рефлексивность, симметричность, транзитивность, согласованность, сравнение с null).**
    
32. **Зачем нужен метод `hashCode()`? Контракт между `equals()` и `hashCode()`.** [](https://90zuneth99.medium.com/56-essential-java-interview-questions-and-answers-2026-expanded-version-with-examples-598b0ff1e5fd)
    
33. **Почему при переопределении `equals()` всегда нужно переопределять `hashCode()`?**
    
34. **Что будет, если у двух равных (`equals()`) объектов разные `hashCode()`? (Проблемы с `HashMap`/`HashSet`).**
    
35. **Как работает метод `toString()`? Зачем его переопределять?**
    
36. **Что делает метод `clone()`? В чем проблема поверхностного (shallow) копирования?**
    
37. **Что такое `getClass()`?**
    
38. **Методы `wait()`, `notify()`, `notifyAll()`. Почему они в `Object`, а не в `Thread`? (Это из многопоточности, но база).**
    

### Группа 4: Обработка исключений (Очень высокая частота)

39. **Что такое исключение? Иерархия исключений (`Throwable` -> `Exception` & `Error`).**
    
40. **Чем отличается `Checked Exception` от `Unchecked (Runtime) Exception`? Приведи примеры.** [](https://medium.com/@gouravverma2907/top-20-java-spring-boot-interview-questions-you-must-know-in-2025-63dbac43169d)
    
41. **Чем отличается `Error` от `Exception`? (`OutOfMemoryError`, `StackOverflowError`).**
    
42. **Что такое `try-catch-finally`? Можно ли использовать `finally` без `catch`?**
    
43. **Может ли `finally` блок не выполниться? (Если `System.exit()`, бесконечный цикл, сбой JVM).**
    
44. **Что такое `try-with-resources`? Какие классы можно использовать (`AutoCloseable`)?**
    
45. **Что произойдет, если исключение вылетит в `catch` и в `finally` блоке? (Исключение из `finally` подавляет предыдущее).**
    
46. **Что такое `throws`? Чем отличается от `throw`?**
    
47. **Можно ли переопределить метод, который бросает `IOException`, и не бросать исключение? (Да, можно сузить).**
    
48. **Какие есть best practices по работе с исключениями? (Не глотать, логировать, не использовать для управления потоком).**
    

### Группа 5: Generics (Дженерики) — Очень важно для Middle

49. **Что такое Generics? Зачем нужны? (Type safety, устранение кастов).**
    
50. **Что такое type erasure (стирание типов)? Как это работает в байткоде?**
    
51. **Что такое `T`, `E`, `K`, `V`, `?`?**
    
52. **Что такое wildcard (`?`)? Чем отличается `? extends T` от `? super T`? (Producer Extends, Consumer Super — PECS).**
    
53. **Что такое raw type (сырой тип)? Почему его использование опасно?**
    
54. **Можно ли создать массив с generic компонентом? (Нет, `new T[10]` — нельзя).**
    
55. **Что такое `Type` и `ParameterizedType`? (Редко, но для харда).**
    

### Группа 6: Коллекции (Collections Framework) — Святая святых

#### List & Set

56. **Что такое `Collection`? Иерархия (`Iterable` -> `Collection` -> `List`/`Set`/`Queue`).**
    
57. **Чем отличается `List` от `Set` от `Map`?** [](https://90zuneth99.medium.com/56-essential-java-interview-questions-and-answers-2026-expanded-version-with-examples-598b0ff1e5fd)
    
58. **`ArrayList` vs `LinkedList`: когда что использовать, сложность операций (O(1) vs O(n)).** [](https://90zuneth99.medium.com/56-essential-java-interview-questions-and-answers-2026-expanded-version-with-examples-598b0ff1e5fd)
    
59. **Как работает `ArrayList` внутри (массив, расширение)? Что такое capacity и load factor?**
    
60. **Что такое `Vector`? Чем отличается от `ArrayList`? (Синхронизация, устаревший).**
    
61. **Что такое `HashSet`? Как он работает внутри (основан на `HashMap`)?**
    
62. **Что такое `TreeSet`? Как поддерживает порядок (`Comparable` / `Comparator`).**
    
63. **Что такое `LinkedHashSet`? (Сохраняет порядок вставки).**
    

#### Map — Это блок, где валятся чаще всего

64. **Что такое `Map`? Отличия от `Collection`.**
    
65. **Как работает `HashMap`? Расскажи про структуру (массив корзин -> связный список -> дерево, Java 8+).**
    
66. **Что такое `hashCode()` и `equals()` в контексте `HashMap`? Как они используются при `put()` и `get()`?**
    
67. **Как происходит разрешение коллизий в `HashMap`? (Метод цепочек).**
    
68. **Когда в `HashMap` происходит преобразование связного списка в красно-черное дерево? (При 8 элементах).**
    
69. **Что такое initial capacity и load factor у `HashMap`? Зачем они?**
    
70. **Чем отличается `HashMap` от `Hashtable`? (null ключи, синхронизация).** [](https://90zuneth99.medium.com/56-essential-java-interview-questions-and-answers-2026-expanded-version-with-examples-598b0ff1e5fd)
    
71. **Что такое `LinkedHashMap`? Чем отличается от `HashMap`? (Двусвязный список для порядка итерации).**
    
72. **Что такое `TreeMap`? (`NavigableMap` на основе красно-черного дерева, сортировка).**
    
73. **Что такое `ConcurrentHashMap`? (Уже было в многопоточке, но повторяют всегда).**
    

#### Прочее по коллекциям

74. **Что такое `Queue` и `Deque`? Какие реализации (`ArrayDeque`, `PriorityQueue`, `LinkedList`).**
    
75. **Что такое `Iterator`? Чем отличается от `ListIterator`?**
    
76. **Что такое `fail-fast` и `fail-safe` итераторы? (ConcurrentModificationException).**
    
77. **Что такое `Collections.sort()`? Как работает (TimSort)?**
    
78. **Методы `Collections.unmodifiableList()`, `Collections.synchronizedList()`.**
    
79. **Что такое `Comparable` vs `Comparator`? Когда что использовать?** [](https://90zuneth99.medium.com/56-essential-java-interview-questions-and-answers-2026-expanded-version-with-examples-598b0ff1e5fd)
    

### Группа 7: Потоки ввода-вывода (I/O Streams) — Средняя частота

80. **Что такое `InputStream` / `OutputStream` (байтовые)? `Reader` / `Writer` (символьные)?**
    
81. **В чем разница между `FileInputStream` и `FileReader`?**
    
82. **Что такое `BufferedInputStream`? Зачем нужно буферизированное чтение?**
    
83. **Что такое `ObjectInputStream` / `ObjectOutputStream`? Что такое сериализация?**
    
84. **Что такое `Serializable`? Какие поля не сериализуются? (`transient`, `static`).**
    
85. **Что такое `serialVersionUID`? Зачем нужен? Что будет, если его не объявить?**
    
86. **Что такое `try-with-resources` для работы с файлами?**
    
87. **NIO (New I/O): что такое `Channel`, `Buffer`, `Selector`? (Для Middle+).**
    
88. **Что такое `Path`, `Paths`, `Files` (NIO.2)? Чем лучше старых `File`?**
    

### Группа 8: Дата и Время (Java 8 Time API) — Часто спрашивают

89. **Чем плохи старые `Date` и `Calendar`? (Mutable, плохой дизайн, потоки).**
    
90. **Какие основные классы новой Date/Time API? (`LocalDate`, `LocalTime`, `LocalDateTime`, `ZonedDateTime`, `Instant`).**
    
91. **Чем отличается `Instant` от `LocalDateTime`? (Instant — момент на временной линии, UTC).**
    
92. **Что такое `Period` и `Duration`? В чем разница?**
    
93. **Как парсить и форматировать даты? (`DateTimeFormatter`, потокобезопасен в отличие от `SimpleDateFormat`).**
    
94. **Как работать с временными зонами (`ZoneId`, `ZonedDateTime`)?**
    

### Группа 9: Многопоточность (Частично пересекается с предыдущим списком, но тут акцент на Core)
95. **Способы создания потока (`Thread`, `Runnable`, `ExecutorService`).**    
96. **Жизненный цикл потока (New, Runnable, Running, Waiting/Blocked/Timed_Waiting, Terminated).**    
97. **Методы `sleep()`, `yield()`, `join()`.**    
98. **Приоритеты потоков. Зависят ли от ОС?**    
99. **Что такое Daemon-потоки?**    
100. **Проблемы многопоточности: race condition, deadlock, livelock, starvation.** [](https://90zuneth99.medium.com/56-essential-java-interview-questions-and-answers-2026-expanded-version-with-examples-598b0ff1e5fd)    
101. **`synchronized` блок и метод. На что вешается блокировка?** [](https://90zuneth99.medium.com/56-essential-java-interview-questions-and-answers-2026-expanded-version-with-examples-598b0ff1e5fd)    
102. **Монитор (внутренний lock). Как работает `wait()` и `notify()`?**    
103. **`volatile` — гарантии видимости и запрет переупорядочивания.** [](https://medium.com/@gouravverma2907/top-20-java-spring-boot-interview-questions-you-must-know-in-2025-63dbac43169d)    
104. **`Atomic` классы (`AtomicInteger`). Принцип CAS (Compare-And-Swap).** [](https://medium.com/@gouravverma2907/top-20-java-spring-boot-interview-questions-you-must-know-in-2025-63dbac43169d)    
105. **Что такое happens-before?** [](https://medium.com/@gouravverma2907/top-20-java-spring-boot-interview-questions-you-must-know-in-2025-63dbac43169d)
106. **Что такое `ThreadLocal`? Проблема утечки памяти.** [](https://medium.com/@gouravverma2907/top-20-java-spring-boot-interview-questions-you-must-know-in-2025-63dbac43169d)
107. **`ReentrantLock`: чем отличается от `synchronized`? `tryLock()`, `lockInterruptibly()`.**
108. **`ReadWriteLock`.**
109. **`CountDownLatch`, `CyclicBarrier`, `Semaphore`, `Exchanger`, `Phaser`.**
110. **`Future`, `Callable`.
111. **`CompletableFuture`. (Очень модно сейчас).**
112. **Что такое `ForkJoinPool`? Work-stealing.**
113. **Пул потоков (`ThreadPoolExecutor`): параметры, очередь.** [](https://90zuneth99.medium.com/56-essential-java-interview-questions-and-answers-2026-expanded-version-with-examples-598b0ff1e5fd)

### Группа 10: Сборщик мусора (Garbage Collection) — Важно для Middle
114. **Что такое сборщик мусора? Как определить, что объект — мусор? (Reachability Analysis от GC Roots).**    
115. **Назови виды ссылок в Java: `Strong`, `Soft`, `Weak`, `Phantom`.**    
116. **Что такое `SoftReference`? Где используется (кэши).**    
117. **Что такое `WeakReference`? Где используется (`WeakHashMap`).**    
118. **Что такое `PhantomReference`? (Предварительная очистка, pre-mortem действия).**    
119. **Какие есть поколения памяти? (Young Gen: Eden + S0 + S1, Old Gen, Metaspace).**
120. **Что такое Stop-The-World (STW)?**    
121. **Какие есть GC алгоритмы и сборщики?**    

- **Serial GC** (однопоточный, для мелких приложений).    
- **Parallel/Throughput GC** (многопоточный, по умолчанию до Java 8).
- **CMS (Concurrent Mark Sweep)** — deprecated.    
- **G1 GC** (по умолчанию с Java 9+, делит кучу на регионы). [](https://medium.com/@gouravverma2907/top-20-java-spring-boot-interview-questions-you-must-know-in-2025-63dbac43169d)
- **ZGC** (ultra-low latency, паузы < 1 мс, для больших куч). [](https://medium.com/@gouravverma2907/top-20-java-spring-boot-interview-questions-you-must-know-in-2025-63dbac43169d)
- **Shenandoah**.

122. **Что такое `System.gc()`? Стоит ли вызывать?**    
123. **Что такое OutOfMemoryError? Какие бывают типы? (Heap space, Metaspace, unable to create new native thread).**    

### Группа 11: Java Memory Model (JMM) — Хард, но нужно
124. **Что такое Java Memory Model? Зачем она нужна?**    
125. **Что такое main memory и working memory (кэши процессора)?**    
126. **Что такое переупорядочивание инструкций (reordering)?**    
127. **Правила happens-before (program order, monitor lock, volatile, thread start/join).** [](https://medium.com/@gouravverma2907/top-20-java-spring-boot-interview-questions-you-must-know-in-2025-63dbac43169d)    
128. **Что такое memory barrier (fence)?**    

### Группа 12: Lambda, Stream API, Functional Interfaces — Обязательно
129. **Что такое функциональный интерфейс (`@FunctionalInterface`)?** [](https://90zuneth99.medium.com/56-essential-java-interview-questions-and-answers-2026-expanded-version-with-examples-598b0ff1e5fd)    
130. **Назови встроенные функциональные интерфейсы: `Predicate`, `Function`, `Consumer`, `Supplier`, `UnaryOperator`.**    
131. **Что такое лямбда-выражение? Как оно выглядит?** [](https://90zuneth99.medium.com/56-essential-java-interview-questions-and-answers-2026-expanded-version-with-examples-598b0ff1e5fd)    
132. **Что такое ссылка на метод (`::`)?**    
133. **Что такое Stream API? Чем поток (stream) отличается от коллекции?** [](https://90zuneth99.medium.com/56-essential-java-interview-questions-and-answers-2026-expanded-version-with-examples-598b0ff1e5fd)    
134. **Категории операций: intermediate (промежуточные) vs terminal (терминальные).**    
135. **Примеры intermediate: `filter`, `map`, `flatMap`, `sorted`, `peek`, `limit`, `skip`.**    
136. **Примеры terminal: `forEach`, `collect`, `reduce`, `count`, `anyMatch`, `findFirst`.**
137. **Что такое ленивые вычисления (lazy evaluation) в стримах?**    
138. **Что такое `Optional`? Зачем нужен, чтобы бороться с `NullPointerException`.** [](https://90zuneth99.medium.com/56-essential-java-interview-questions-and-answers-2026-expanded-version-with-examples-598b0ff1e5fd)
139. **Методы `Optional`: `of()`, `ofNullable()`, `orElse()`, `orElseGet()`, `orElseThrow()`, `map()`, `filter()`.**    
140. **Что такое параллельные стримы (`parallelStream()`)? Когда использовать? Проблемы.**

### Группа 13: Модули, Компиляция, ClassLoader (Реже, но для сильного Middle)
141. **Что такое ClassLoader? Какие ClassLoader-ы бывают? (Bootstrap, Extension/Platform, Application).**    
142. **Принцип делегирования (Delegation Model) при загрузке классов. Зачем он нужен?**    
143. **Можно ли переопределить `java.lang.String`? (Нет, за счет делегирования и защиты).**    
144. **Что такое модули (Java 9+ Project Jigsaw)?**    
145. **Что такое `module-info.java`? (Requires, exports, opens).**    
146. **Чем модуль отличается от JAR-файла?**    
147. **Как JVM загружает классы? Этапы: Loading, Linking (Verification, Preparation, Resolution), Initialization.**    

### Группа 14: Java 8, 11, 17, 21 (Новые фичи по версиям)

**Java 8 (должны знать все):**  
148. Lambda и Stream API.  
149. Новый Date/Time API.  
150. `Optional`.  
150. Default и static методы в интерфейсах. [](https://90zuneth99.medium.com/56-essential-java-interview-questions-and-answers-2026-expanded-version-with-examples-598b0ff1e5fd)  
152. `Nashorn` движок (устарел, но было).

**Java 9-11:**  
153. Модули (Project Jigsaw).  
154. `var` (локальная переменная с выводом типа, Java 10).  
154. HTTP Client (Java 11).  
156. `Collection` factory methods: `List.of()`, `Set.of()`, `Map.of()`.  
157. `String` методы: `isBlank()`, `lines()`, `strip()`, `repeat()`.

**Java 17 (LTS) — важно:**  
158. `Sealed Classes` (запечатанные классы/интерфейсы).  
159. `Pattern Matching for`switch`(preview в 17, final в 21). 160.`Records`(компактный способ объявления immutable data-классов). 161.`Text Blocks` (многострочные строки).

**Java 21 (LTS) — современный Middle должен знать:**  
162. Virtual Threads (Project Loom). [ранее обсуждали]  
163. Sequenced Collections (интерфейсы для коллекций с упорядоченным доступом).  
164. Record Patterns.  
165. Pattern Matching for switch (final).  
166. Scoped Values (альтернатива `ThreadLocal`).

### Группа 15: Разное и «Хитрые вопросы» (Проверка кругозора)
167. **Что такое `native` метод?**    
168. **Что такое `transient`? Где применяется?**    
169. **Что такое `strictfp`?**    
170. **Что такое `assert`?**    
171. **Что такое рефлексия (Reflection API)? Где используется (Spring, Hibernate, JUnit).**    
172. **Минусы рефлексии (потеря типобезопасности, производительность).**    
173. **Что такое аннотации (Annotations)? Retention policies (SOURCE, CLASS, RUNTIME).**    
174. **Как создать свою аннотацию?**    
175. **Что такое `Enum`? Чем enum в Java лучше, чем константы? (Type safety, методы, конструктор).**    
176. **Методы `wait()` и `sleep()` — чем отличаются (повтор для закрепления).**
177. **Что такое `NumberFormatException` и `ArithmeticException`?**    
178. **Что такое `StackOverflowError`? Пример.**    
179. **Что такое `OutOfMemoryError`? Пример.**    
180. **Что такое DCL (Double Checked Locking) для Singleton? (Риск без volatile).**
181. **Почему в Java нет множественного наследования классов? (Ромбовидная проблема, сложность JVM).**    
182. **Что такое `instanceof`? Что такое pattern matching для `instanceof` (Java 16+)?**
183. **Что такое `BigDecimal`? Зачем нужен, если есть `double`? (Точность, финансы).**
184. **Что такое `System.arraycopy()`?**
185. **Что такое `Arrays.asList()`? Какие ограничения (фиксированный размер)?**
186. **Что такое `clone()`? Проблема `Cloneable` (marker interface, поверхностное копирование).**
187. **Что такое `StringTokenizer`? (Устарел, используйте `split()`).
188. **Что такое `Timer` и `TimerTask`? Чем плохи по сравнению с `ScheduledExecutorService`?**
189. **Как обменяться данными между двумя потоками без синхронизации? (Никак, volatile/синхронизация обязательны).**
190. **Что такое `Shutdown Hook`?**

### Бонус: Паттерны проектирования на Core Java (Часто спрашивают)
191. **Порождающие:** Singleton, Factory, Builder, Prototype.    
192. **Структурные:** Adapter, Proxy, Decorator, Facade.    
193. **Поведенческие:** Strategy, Observer, Command, Template Method, Iterator.    

---
### Главные советы по Java Core:

1. **JVM и память:** Обязательно учите, как устроена память (Stack, Heap, Metaspace), как летают ссылки. Это разделяет Junior и Middle.
    
2. **Коллекции:** Не заучивайте ответы, а напишите код: вставьте 100500 элементов в `HashMap`, посмотрите, как растет массив. Поймите, зачем нужен `hashCode()`.
    
3. **Многопоточность:** Если не понимаете `happens-before`, выучите 3-4 правила наизусть. Это спасет на собеседовании.
    
4. **Java 8 Streams:** Не просто "фильтруй и маппируй", а понимайте, что стрим нельзя переиспользовать, и знайте разницу между `map()` и `flatMap()`.
    
5. **Новые версии:** Если вакансия на Java 17/21, обязательно пробегитесь по фичам (Records, Sealed Classes, Virtual Threads). Это покажет, что вы развиваетесь.

---
