# **Почему после `collect()` можно вызвать `forEach()` без ошибки?**

В Java Stream API, методы делятся на **промежуточные** (intermediate) и **терминальные** (terminal).

- **Промежуточные операции** (например, `map`, `filter`, `sorted`) возвращают новый `Stream` и **не выполняются** до вызова терминальной операции.
    
- **Терминальные операции** (например, `collect`, `forEach`, `reduce`) **закрывают** поток и возвращают результат (или `void`).
    

---
### **1. `collect()` — это терминальная операция**

После вызова `collect()` поток **завершается**, и дальнейшие операции с ним невозможны.

#### **Пример с ошибкой: повторное использование Stream**
```java
Stream<String> stream = Stream.of("A", "B", "C");
List<String> list = stream.collect(Collectors.toList()); // Stream закрыт

stream.forEach(System.out::println); // Ошибка: IllegalStateException
// Stream уже использован!
```

---
### **2. Почему `forEach()` работает после `collect()`?**

Потому что `forEach()` вызывается **не на Stream, а на коллекции**, которую вернул `collect()`:

#### **Правильный пример:**
```java
List<String> letters = Stream.of("A", "B", "C")
    .collect(Collectors.toList()); // Терминальная операция

letters.forEach(System.out::println); // Работает, т.к. это метод List, а не Stream
```

#### **Что происходит:**
1. `collect()` собирает элементы в коллекцию (например, `List`).    
2. `forEach()` вызывается у этой коллекции (не у `Stream`).    

---
### **3. Разница между `Stream.forEach` и `Collection.forEach`**

|Метод|Тип|Когда использовать|
|---|---|---|
|`Stream.forEach()`|Терминальный|Для побочных эффектов в Stream (не для модификации данных).|
|`List.forEach()`|Метод коллекции|Для перебора элементов после сохранения в коллекцию.|
#### **Пример обоих вариантов:**
```java
// 1. Stream.forEach (не сохраняет элементы)
Stream.of("X", "Y", "Z")
    .forEach(System.out::println); // OK, но поток закрыт

// 2. List.forEach (работает с коллекцией)
List<String> saved = Stream.of("X", "Y", "Z")
    .collect(Collectors.toList());

saved.forEach(System.out::println); // OK, коллекция сохранена
```

---
### **4. Почему нет ошибки?**

- `collect()` возвращает **новый объект** (`List`, `Set` и т.д.), и `forEach()` применяется уже к нему.    
- Поток **уже закрыт**, но коллекция — самостоятельный объект.    

#### **Аналогия:**
```java
String text = "Hello";
List<String> words = Arrays.stream(text.split(" "))
    .collect(Collectors.toList()); // Stream закрыт

words.forEach(System.out::println); // Работает с List, а не Stream
```

---
### **Итог**

- После `collect()` **Stream больше нельзя использовать**.    
- `forEach()` работает, потому что вызывается **у коллекции**, а не у потока.    
- **Так делать можно:**
```java
collection.stream()  // Создать Stream
    .collect(...)    // Закрыть Stream, получить коллекцию
    .forEach(...);   // Работать с коллекцией
```
    
- **Так нельзя:**
```java
stream.collect(...)  // Stream закрыт
    .filter(...)     // Ошибка: поток уже использован
```
  

**Главное правило:**

> После терминальной операции (`collect`, `forEach`, `reduce`) поток завершается. Дальнейшие операции возможны только с результатом (например, с коллекцией).

---
