# **Примеры использования `UnaryOperator<T>` в Java**

`UnaryOperator<T>` — это частный случай `Function<T, T>`, где **входной и выходной тип совпадают**.  
По сути, это функция, которая **принимает `T` и возвращает `T`** после преобразования.

---
## **1. Увеличение числа на 1**
```java
UnaryOperator<Integer> increment = x -> x + 1;
System.out.println(increment.apply(5)); // 6
```

## **2. Преобразование строки в верхний регистр**
```java
UnaryOperator<String> toUpperCase = String::toUpperCase;
System.out.println(toUpperCase.apply("hello")); // "HELLO"
```

## **3. Удвоение значения в списке**
```java
List<Integer> numbers = Arrays.asList(1, 2, 3);
numbers.replaceAll(x -> x * 2); // UnaryOperator внутри replaceAll
System.out.println(numbers); // [2, 4, 6]
```

## **4. Композиция операторов (`andThen`, `compose`)**
```java
UnaryOperator<Integer> square = x -> x * x;
UnaryOperator<Integer> increment = x -> x + 1;

// Сначала возвести в квадрат, потом добавить 1
UnaryOperator<Integer> squareAndIncrement = square.andThen(increment);
System.out.println(squareAndIncrement.apply(3)); // 10 (3² + 1)

// Сначала добавить 1, потом возвести в квадрат
UnaryOperator<Integer> incrementAndSquare = square.compose(increment);
System.out.println(incrementAndSquare.apply(3)); // 16 ((3 + 1)²)
```

## **5. Работа с объектами (например, обновление имени пользователя)**
```java
class User {
    String name;
    // конструктор, геттеры, сеттеры...
}

UnaryOperator<User> renameToBob = user -> {
    user.setName("Bob");
    return user;
};

User alice = new User("Alice");
renameToBob.apply(alice);
System.out.println(alice.getName()); // "Bob"
```

## **6. Использование в `Stream.map`**
```java
List<String> words = Arrays.asList("a", "bb", "ccc");

// Удваиваем каждую строку: "a" → "aa", "bb" → "bbbb" и т.д.
UnaryOperator<String> duplicate = s -> s + s;
List<String> result = words.stream()
    .map(duplicate)
    .toList();

System.out.println(result); // ["aa", "bbbb", "cccccc"]
```

## **7. Кэширование (мемоизация) результата**
```java
import java.util.HashMap;
import java.util.Map;

public class Memoizer<T> {
    private final Map<T, T> cache = new HashMap<>();
    private final UnaryOperator<T> operator;
	
    public Memoizer(UnaryOperator<T> operator) {
        this.operator = operator;
    }
	
    public T apply(T input) {
        return cache.computeIfAbsent(input, operator);
    }
}

// Пример:
UnaryOperator<Integer> expensiveCalculation = x -> {
    System.out.println("Вычисляю для " + x);
    return x * x;
};

Memoizer<Integer> memoizer = new Memoizer<>(expensiveCalculation);
System.out.println(memoizer.apply(3)); // Вычисляю для 3 → 9
System.out.println(memoizer.apply(3)); // Берётся из кэша → 9 (без вычислений)
```

---
### **Ключевые особенности `UnaryOperator<T>`**
- Наследует `Function<T, T>`, поэтому поддерживает `apply()`, `andThen()`, `compose()`.    
- Оптимизирован для случаев, **когда вход и выход одного типа**.    
- Часто используется в `List.replaceAll()` и `Stream.map()`.    

### **Где применяется?**
- Математические операции (`x → x + 1`).    
- Модификация объектов (изменение полей).    
- Работа с коллекциями (`replaceAll`).    
- Кэширование вычислений.    
### **Сравнение с другими интерфейсами**

|Интерфейс|Вход|Выход|Пример использования|
|---|---|---|---|
|`UnaryOperator<T>`|`T`|`T`|`x -> x + 1`|
|`Function<T, R>`|`T`|`R`|`str -> str.length()`|
|`BinaryOperator<T>`|`T, T`|`T`|`(a, b) -> a + b`|

---
### **Итог**

`UnaryOperator<T>` — это удобная специализация `Function` для случаев, когда:
- Нужно **изменить объект того же типа** (например, `String → String`).    
- Требуется **цепочка однотипных преобразований** (`andThen`/`compose`).    
- Работаете с API, где ожидается оператор (например, `List.replaceAll`).    

**Осторожно:** Не путать с `BinaryOperator<T>` (который принимает **два аргумента**).

---
