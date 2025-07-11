# **Примеры использования `Function<T, R>` в Java**

`Function<T, R>` — это функциональный интерфейс из `java.util.function`, который принимает один аргумент типа `T` и возвращает результат типа `R`.

---

## **1. Преобразование строки в её длину**
```java
Function<String, Integer> stringLength = s -> s.length();
System.out.println(stringLength.apply("Hello")); // 5
```

## **2. Конвертация числа в строку**
```java
Function<Integer, String> intToString = num -> "Число: " + num;
System.out.println(intToString.apply(42)); // "Число: 42"
```

## **3. Извлечение имени из объекта**
```java
class User {
    String name;
    // конструктор, геттеры...
}

Function<User, String> getName = User::getName; // метод-референс
User user = new User("Alice");
System.out.println(getName.apply(user)); // "Alice"
```

## **4. Композиция функций (`andThen`, `compose`)**
```java
Function<Integer, Integer> multiplyBy2 = x -> x * 2;
Function<Integer, String> toString = Object::toString;

// Сначала умножить на 2, затем преобразовать в строку
Function<Integer, String> process = multiplyBy2.andThen(toString);
System.out.println(process.apply(3)); // "6"

// Сначала преобразовать в строку, затем получить длину
Function<Integer, Integer> anotherProcess = toString.andThen(String::length);
System.out.println(anotherProcess.apply(100)); // 3 (длина строки "100")
```

## **5. Работа с коллекциями (например, преобразование списка)**
```java
List<String> names = List.of("Alice", "Bob", "Charlie");

// Преобразовать имена в их длины
List<Integer> lengths = names.stream()
    .map(String::length) // Function<String, Integer>
    .toList();

System.out.println(lengths); // [5, 3, 7]
```

## **6. Цепочка преобразований**
```java
Function<String, String> addPrefix = s -> "Mr. " + s;
Function<String, String> toUpperCase = String::toUpperCase;

// Комбинация: добавить префикс + сделать заглавными буквами
Function<String, String> processName = addPrefix.andThen(toUpperCase);
System.out.println(processName.apply("John")); // "MR. JOHN"
```

## **7. Использование в `Map.computeIfAbsent`**
```java
Map<String, Integer> nameToLength = new HashMap<>();

// Если ключа нет, вычислить его длину и добавить
nameToLength.computeIfAbsent("Alice", String::length);
System.out.println(nameToLength); // {"Alice": 5}
```

---
### **Ключевые особенности `Function<T, R>`**

- **Принимает `T` → возвращает `R`**.
    
- **Методы**:    
    - `apply(T t)` — применить функцию.        
    - `andThen(Function after)` — выполнить после текущей.        
    - `compose(Function before)` — выполнить перед текущей.
        
- **Используется везде, где нужно преобразование данных**.    

### **Где применяется?**
- `Stream.map(Function)` — преобразование элементов стрима.    
- `Optional.map(Function)` — преобразование значения в `Optional`.    
- `Map.computeIfAbsent` / `computeIfPresent`.    

### **Сравнение с другими интерфейсами**

|Интерфейс|Вход|Выход|Пример использования|
|---|---|---|---|
|`Function<T, R>`|`T`|`R`|`str -> str.length()`|
|`Consumer<T>`|`T`|`void`|`x -> System.out.println(x)`|
|`Predicate<T>`|`T`|`boolean`|`x -> x > 0`|
|`Supplier<T>`|`void`|`T`|`() -> new Random().nextInt()`|

---
### **Итог**

`Function<T, R>` — это **основной инструмент для преобразования данных** в Java.  
Используйте его для:

- Конвертации объектов.    
- Цепочки операций (`andThen`/`compose`).    
- Работы со стримами и `Optional`.

---
