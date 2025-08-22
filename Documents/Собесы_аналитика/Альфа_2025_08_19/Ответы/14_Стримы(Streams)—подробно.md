# Стримы (*Streams*) — подробно, но понятным языком ✅

Стрим — это последовательность элементов с набором операций (pipeline). Операции делятся на **промежуточные** (intermediate) — ленивые, возвращают Stream и **составляют план**, и **терминальные** (terminal) — запускают выполнение и возвращают результат (или побочный эффект). Стримы могут быть последовательными или параллельными.

---
# Ключевые концепты (коротко)

- **Ленивость**: промежуточные операции не выполняются до терминальной.
    
- **Статeless vs stateful**: некоторые промежуточные операции требуют состояния (например, `sorted`, `distinct`) — они блокируют/буферизуют поток.
    
- **Short-circuit**: некоторые операции могут завершить работу раньше (например, `limit`, `anyMatch`, `findAny`).
    
- **Параллельность**: подходящие операции + корректные коллекторы → `parallel()` для распараллеливания.
    
- **Не делайте side-effects** в `map`/`filter` — используйте `collect` или `forEach` для побочных эффектов; `peek` — только для отладки.    

---
# 👇 Таблица: промежуточные операции (основные)

> Формат: `метод(параметры)` — принимает `функциональный интерфейс` — краткое описание.

- `map(Function<? super T,? extends R> mapper)`  
    → `Function<T,R>` — преобразует каждый элемент в другой.
```java
list.stream().map(User::getName)
```
    
- `flatMap(Function<? super T,? extends Stream<? extends R>> mapper)`  
    → `Function<T,Stream<R>>` — разворачивает вложенные стримы (полезно для коллекций внутри объектов).
```java
people.stream().flatMap(p -> p.getPhones().stream())
```
    
- `filter(Predicate<? super T> predicate)`  
    → `Predicate<T>` — отбрасывает элементы, не прошедшие условие.
```java
stream.filter(x -> x > 0)
```
    
- `peek(Consumer<? super T> action)`  
    → `Consumer<T>` — побочный эффект/отладка; не гарантируется вызов для каждого элемента в параллельном/оптимизированном pipeline.
```java
stream.peek(x -> log.debug(x));
```
    
- `distinct()` — нет параметров — оставляет уникальные по `equals()` (stateful, использует Set).
    
- `sorted()` / `sorted(Comparator<? super T> cmp)`  
    → `Comparator<T>` — stateful; сортировка (может быть дорогой).
    
- `limit(long maxSize)` — нет параметров — short-circuiting; берёт первые N элементов (в упорядоченном потоке — первые по порядку).
    
- `skip(long n)` — пропускает первые n (stateful-ish).
    
- Преобразования в примитивные стримы:    
    - `mapToInt(ToIntFunction<? super T> mapper)` → `ToIntFunction<T>` → `IntStream`
        
    - `mapToLong(ToLongFunction<? super T>`), `mapToDouble(ToDoubleFunction<? super T>)`
        
    - `boxed()` — наоборот: примитивный Stream → `Stream<T>`.
    
- `asLongStream()`, `mapToObj(IntFunction<R>)` и т.п. — для переходов между примитивными и объектными стримами.
    
- `unordered()` — снимает гарантию порядка (может помочь при параллелизме).

---
# 👇 Таблица: терминальные операции (основные)

- **Коллекторы / агрегаты**
    
    - `collect(Collector<? super T,A,R> collector)`  
        → принимает `Collector` (см. раздел Collector ниже). Используется для накопления в коллекции/мэпе и т.д.
```java
List<User> users = stream.collect(Collectors.toList());
```
        
    - `collect(Supplier<R> supplier, BiConsumer<R,? super T> accumulator, BiConsumer<R,R> combiner)`  
        → `Supplier`, `BiConsumer`, `BiConsumer` — низкоуровневая mutable-reduction.
        
- **Редукция**    
    - `reduce(BinaryOperator<T> op)` — `BinaryOperator<T>`; возвращает `Optional<T>`.
        
    - `reduce(T identity, BinaryOperator<T> op)` — возвращает T (identity + accumulator).
        
    - `reduce(U identity, BiFunction<U,? super T,U> accumulator, BinaryOperator<U> combiner)` — более общий вариант (итерация + комбинирование).
        
- **Поиск / матчи / короткозамыкания**    
    - `anyMatch(Predicate<? super T> predicate)` — `Predicate<T>` — возвращает true при первом совпадении (short-circuit).
        
    - `allMatch(Predicate<? super T> predicate)` — возвращает false при первом несоответствии.
        
    - `noneMatch(Predicate<? super T> predicate)` — true если никакой элемент не удовлетворяет.
        
    - `findFirst()` — `Optional<T>` — первый элемент (в параллельном потоке может быть дорого).
        
    - `findAny()` — `Optional<T>` — любой элемент (короткозамыкающий в параллельном режиме).
        
- **Количественные и простые**    
    - `count()` — `long` — число элементов.
        
    - `min(Comparator<? super T>)` / `max(Comparator<? super T>)` — `Optional<T>`.
        
- **Вывод / побочные эффекты**    
    - `forEach(Consumer<? super T> action)` — `Consumer<T>` — выполняет действия (в параллельном потоке порядок не гарантируется).
        
    - `forEachOrdered(Consumer<? super T> action)` — гарантирует порядок, но может быть дороже в parallel.
        
- **Конвертация**    
    - `toArray()` / `toArray(IntFunction<A[]>)` — возвращает массив.        

---
# Collectors — подробно (важно для production)

`Collector<T,A,R>` — интерфейс с5 компонентами: `Supplier<A> supplier`, `BiConsumer<A,T> accumulator`, `BinaryOperator<A> combiner`, `Function<A,R> finisher`, `Set<Characteristics> characteristics`.

**Характеристики:** `CONCURRENT`, `UNORDERED`, `IDENTITY_FINISH`.

**Частые готовые Collectors:**
- `Collectors.toList()` → `List<T>`
    
- `Collectors.toSet()` → `Set<T>`
    
- `Collectors.toMap(keyMapper, valueMapper)` — есть перегрузки с mergeFunction и supplier.
    
- `Collectors.groupingBy(classifier)` — возвращает `Map<K,List<T>>`; можно использовать downstream collector (например, `groupingBy(classifier, counting())`).
    
- `Collectors.partitioningBy(predicate)` — `Map<Boolean, List<T>>` (специальный случай groupingBy).
    
- `Collectors.joining(delim, prefix, suffix)` — для строк.
    
- `Collectors.counting()`, `Collectors.summingInt(ToIntFunction)`, `averagingInt/toLong/toDouble`
    
- `Collectors.reducing(...)` — необязательная reduce-форма.
    
- `Collectors.mapping(mapper, downstreamCollector)` — комбинирование маппинга и дальнейшей агрегации.    

**Пример группировки:**
```java
Map<Department, Long> countByDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept, Collectors.counting()));
```

**Mutable reduction vs Immutable**
- `collect(...)` с `Collector` — рекомендуется для mutable accumulation (List/Map).    
- `reduce` — для immutable accumulation (более тяжёлый путь для коллекций).    

---
# Примитивные стримы (IntStream/LongStream/DoubleStream)

- Есть специализированные операции: `sum()`, `average()`, `summaryStatistics()` → возвращают `IntSummaryStatistics`.
    
- Примитивные версии принимают специальные функциональные интерфейсы:    
    - `ToIntFunction<T>` для `mapToInt`        
    - `IntPredicate`, `IntUnaryOperator`, `IntConsumer`, `IntFunction<R>` и т.д.
    
- Преимущества: меньше авто-боксинга → лучше perf.    

---
# Функциональные интерфейсы — краткий словарь (чаще встречающиеся)

- `Function<T,R>` — используется в `map`.
    
- `Predicate<T>` — `filter`, `anyMatch`.
    
- `Consumer<T>` — `forEach`, `peek`.
    
- `Supplier<T>` — `collect` (supplier), `Stream.generate`.
    
- `UnaryOperator<T>` — `map` когда тип Т→T.
    
- `BinaryOperator<T>` — `reduce` (аккумулятор).
    
- Примитивные версии: `IntFunction`, `ToIntFunction<T>`, `IntPredicate`, `IntUnaryOperator`, `IntBinaryOperator`, `IntConsumer`.    

---
# Поведение при параллелизме и порядок (important)

- **Stateless intermediate ops** (map/filter) легко параллелить.
    
- **Stateful ops** (sorted, distinct, limit) требуют синхронизации/буферизации → ухудшают параллелизм.
    
- **Collectors**: использовать `Collectors.toConcurrentMap()`/`groupingByConcurrent()` или собирать в `ConcurrentMap` если параллелизм нужен.
    
- **Avoid shared mutable state**: не модифицируйте внешние коллекции из лямбд — либо используйте concurrent-коллекторы, либо `collect`.
    
- **ordered vs unordered**: `forEachOrdered` и `findFirst` зависят от порядка; `findAny` быстрее в unordered/parallel.
    

---
# Common pitfalls и best practices

- `orElse` vs `orElseGet` — не связано со стримами, но принцип ленивости важен (аналогично `orElseGet`).
    
- **Не делайте heavy side-effects в map/filter** — используйте `peek` только для логов и `collect` для накопления.
    
- В параллельных стримах используйте **параллельные** коллекторы или `toConcurrentMap` и проверяйте, что reducer/combiner ассоциативны.
    
- Для больших данных — используйте примитивные стримы или `LongStream.range...` вместо коллекций.
    
- Для I/O-bound задач лучше использовать асинхронные/reactive подходы; parallel stream распараллеливает CPU-потоки (ForkJoinPool.commonPool()).
    

---
# Примеры (коротко, полезные шаблоны)

1. Список имён старше 18, уникальные, отсортированные:
    

`List<String> names = people.stream()     .filter(p -> p.getAge() >= 18)                    // Predicate<Person>     .map(Person::getName)                             // Function<Person,String>     .distinct()     .sorted()     .collect(Collectors.toList());`

2. Группировка и суммирование:
    

`Map<Dept, Integer> sumSalary = employees.stream()     .collect(Collectors.groupingBy(Employee::getDept, Collectors.summingInt(Employee::getSalary)));`

3. Параллельный подсчёт sum:
    

`int total = data.parallelStream()     .mapToInt(My::getValue)                           // ToIntFunction<My>     .sum();`

4. Наиболее частое слово (Map + reduce):
    

`Map<String, Long> freq = words.stream()     .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())); Optional<String> top = freq.entrySet().stream()     .max(Map.Entry.comparingByValue())     .map(Map.Entry::getKey);`

---
# Короткая шпаргалка по методам (самые употребляемые)

- **Intermediate**: `map`, `flatMap`, `filter`, `peek`, `distinct`, `sorted`, `limit`, `skip`, `mapToInt/Long/Double`, `boxed`
    
- **Terminal**: `collect`, `reduce`, `forEach`, `forEachOrdered`, `count`, `anyMatch/allMatch/noneMatch`, `findFirst/findAny`, `min/max`, `toArray`
    

---