# Optional — зачем нужен и как применять (сначала просто, потом детально) ✅

---
## Коротко — суть (одним предложением)
`Optional<T>` — контейнер, который явно обозначает «значение может быть или не быть» и помогает избежать `null`-чеков и `NullPointerException`, заменяя `null`-return безопасным API.

---
## Когда применять (правило)

- Использовать **как возвращаемый тип** из методов, когда результат может отсутствовать.
    
- **Не рекомендуется**: поля в POJO/Entity, параметры публичных API (лучше перегрузки/объекты с дефолтами).
    
- Отлично в комбинации со стримами и функциональными методами (`map`, `flatMap`, `filter`).    

---
# Все основные методы `Optional` (с пояснениями и версиями JVM)

### Статические фабрики
- `static <T> Optional<T> empty()` — пустой Optional.    
- `static <T> Optional<T> of(T value)` — создаёт Optional с value (value ≠ null; иначе NPE).    
- `static <T> Optional<T> ofNullable(T value)` — создаёт Optional.empty() если value == null.    

### Проверка и извлечение
- `boolean isPresent()` — true если есть значение.    
- `boolean isEmpty()` — true если пуст (добавлено в Java 11).    
- `T get()` — возвращает значение или бросает `NoSuchElementException` если пусто (небезопасно без проверки).    

### Действия при наличии/отсутствии
- `void ifPresent(Consumer<? super T> action)` — выполнить действие при наличии.   
- `void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction)` — (Java 9) действие или альтернативное действие при пустом.    

### Преобразования и фильтрация
- `Optional<T> filter(Predicate<? super T> predicate)` — оставить value если predicate true, иначе empty.    
	
- `<U> Optional<U> map(Function<? super T, ? extends U> mapper)` — преобразовать value → U и обернуть в Optional.    
	
- `<U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper)` — как map, но mapper уже возвращает Optional (разворачивание).    

### Заменители / альтернативы
- `T orElse(T other)` — вернуть value или other (**other вычисляется всегда** — см. примечание).
    
- `T orElseGet(Supplier<? extends T> supplier)` — лениво: supplier вызывается только при пустом Optional.
    
- `<X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X` — бросить кастомное исключение при пустом (Java 8).
    
- `T orElseThrow()` — без-аргумента (added later, бросает `NoSuchElementException`).
    
- `Optional<T> or(Supplier<? extends Optional<? extends T>> supplier)` — (Java 9) вернуть этот Optional, или альтернативный Optional от supplier.    

### Работа со стримами и утилиты
- `Stream<T> stream()` — (Java 9) превращает Optional в Stream (0 или 1 элемент), удобно в pipeline.
    
- `boolean equals(Object)` / `int hashCode()` / `String toString()` — стандартные.    

---
# Психологические мемы / важные отличия (чтобы не ошибиться)

- `orElse` **лениво? Нет** — выражение в `orElse` **вычисляется всегда**; если вычисление дорогостоящее, используйте `orElseGet`.
    
- `map` vs `flatMap`: `map` возвращает `Optional<У>`, `flatMap` используется когда функция уже даёт `Optional<У>` (чтобы не получить `Optional<Optional<У>>`).
    
- `get()` — почти всегда плохая идея без предварительной проверки `isPresent()` или `orElseThrow` — избегать в продакшене.

---
# Примеры (чётко и практично)

### 1) Базовое использование
`Optional<User> findUser(String id) { ... }  findUser(id)   .ifPresent(user -> sendWelcomeEmail(user));`

### 2) Преобразование
`Optional<User> userOpt = findUser(id); Optional<String> emailOpt = userOpt.map(User::getEmail); String email = emailOpt.orElse("no-reply@example.com");`

### 3) Ленивая альтернатива
`String name = findUser(id)     .map(User::getName)     .orElseGet(() -> expensiveDefaultName()); // expensiveDefaultName вызовется только при пустом`

### 4) flatMap для вложенных/nullable полей

`// user.getAddress() может быть null, address.getStreet() тоже Optional<String> street = findUser(id)     .flatMap(u -> Optional.ofNullable(u.getAddress()))     .map(Address::getStreet);`

### 5) Комбинировать два Optional

`Optional<A> a = ... Optional<B> b = ... Optional<C> c = a.flatMap(av -> b.map(bv -> combine(av, bv)));`

### 6) Optional в Stream

`List<String> names = users.stream()     .map(this::findUser)         // Stream<Optional<User>>     .flatMap(Optional::stream)   // Stream<User>     .map(User::getName)     .collect(Collectors.toList());`

### 7) Альтернатива с exception

`User user = findUser(id).orElseThrow(() -> new NotFoundException("user " + id));`

---

# Best practices / anti-patterns (что сказать на собеседовании)

- ✅ **Return Optional** из методов, когда значение может отсутствовать.
    
- ❌ **Не использовать Optional как поле в entity/DTO** (увеличивает сложность сериализации, ORM не ожидает).
    
- ❌ **Не использовать Optional в параметрах публичных API** — это ухудшает читаемость и совместимость.
    
- ✅ Использовать `orElseGet` для ленивой логики; избегать `get()` без проверки.
    
- ✅ В stream-пайплайнах использовать `flatMap(Optional::stream)` для чистоты.
    
- ❗ Не превращайте Optional в `null` и обратно бессмысленно — цель — избавиться от `null`-семантики.
    

---

# Производительность и под капотом (коротко)

- `Optional` — тонкая оболочка: внутри хранит единственное поле `T value`. Небольшой объект, GC-friendly.
    
- Частая критика: обёртка + лямбды (map/flatMap) могут создавать дополнительных аллокаций, но в большинстве приложений это не проблема. Для hot-path с миллионами вызовов перфом-тест — решает.
    
- Важно: `orElse` вычисляет аргумент всегда → неожиданный overhead; `orElseGet` — ленивый.
    

---

# Полезная «шпаргалка» (мнемоника)

- `of` — не null; `ofNullable` — может быть null.
    
- `isPresent`/`isEmpty` — проверка.
    
- `map` — трансформировать; `flatMap` — трансформировать, если результат — Optional.
    
- `filter` — отбросить по условию.
    
- `orElse` (eager) vs `orElseGet` (lazy) vs `orElseThrow` (исключение).
    
- `ifPresent` / `ifPresentOrElse` — побочные эффекты.
    
- `stream()` — вписать Optional в Stream pipeline.
    

---