# **Примеры использования `BinaryOperator<T>` в Java**

`BinaryOperator<T>` — это частный случай `BiFunction<T, T, T>`, где **оба входных аргумента и возвращаемое значение имеют один тип `T`**.  
По сути, это операция, которая **принимает два значения типа `T` и возвращает одно значение типа `T`**.

---
## **1. Сложение чисел**
```java
BinaryOperator<Integer> sum = (a, b) -> a + b;
System.out.println(sum.apply(3, 5)); // 8
```

## **2. Поиск максимума/минимума**
```java
BinaryOperator<Integer> max = Integer::max; // метод-референс
BinaryOperator<Integer> min = Integer::min;

System.out.println(max.apply(10, 20)); // 20
System.out.println(min.apply(10, 20)); // 10
```

## **3. Конкатенация строк**
```java
BinaryOperator<String> concat = (s1, s2) -> s1 + " " + s2;
System.out.println(concat.apply("Hello", "World")); // "Hello World"
```

## **4. Умножение элементов списка попарно**
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
BinaryOperator<Integer> multiply = (a, b) -> a * b;

int product = numbers.stream()
    .reduce(1, multiply); // 1 * 1 * 2 * 3 * 4

System.out.println(product); // 24
```

## **5. Объединение мап с заменой значений**
```java
BinaryOperator<String> resolveConflict = (oldVal, newVal) -> newVal + " (updated)";

Map<String, String> map1 = Map.of("A", "Apple", "B", "Banana");
Map<String, String> map2 = Map.of("A", "Apricot", "C", "Cherry");

Map<String, String> merged = new HashMap<>(map1);
map2.forEach((k, v) -> merged.merge(k, v, resolveConflict));

System.out.println(merged); 
// {A=Apricot (updated), B=Banana, C=Cherry}
```

## **6. Своя логика для `reduce` в стримах**
```java
BinaryOperator<String> longestString = (s1, s2) -> 
    s1.length() > s2.length() ? s1 : s2;

List<String> words = Arrays.asList("Java", "Python", "C++", "Go");
String longest = words.stream()
    .reduce(longestString)
    .orElse("");

System.out.println(longest); // "Python"
```

## **7. Композиция операторов (`andThen`)**
```java
BinaryOperator<Integer> sum = (a, b) -> a + b;
UnaryOperator<Integer> square = x -> x * x;

// Сначала сложить, потом возвести в квадрат
BinaryOperator<Integer> sumAndSquare = sum.andThen(square);
System.out.println(sumAndSquare.apply(2, 3)); // 25 ( (2+3)² )
```

---
### **Ключевые особенности `BinaryOperator<T>**

- Наследует `BiFunction<T, T, T>`, поэтому поддерживает `apply(T, T)`, `andThen(Function)`.   
- Часто используется для **агрегации данных** (`reduce`, `merge`).    
- Оптимизирован для операций, где **входы и выходы одного типа**.    

### **Где применяется?**
- Математические операции (`+`, `-`, `max`, `min`).    
- Работа с коллекциями (`Stream.reduce`, `Map.merge`).    
- Алгоритмы (поиск минимума/максимума, конкатенация).    

### **Сравнение с другими интерфейсами**

|Интерфейс|Вход|Выход|Пример использования|
|---|---|---|---|
|`BinaryOperator<T>`|`T, T`|`T`|`(a, b) -> a + b`|
|`UnaryOperator<T>`|`T`|`T`|`x -> x + 1`|
|`BiFunction<T, U, R>`|`T, U`|`R`|`(str, num) -> str + num`|

---
### **Итог**

`BinaryOperator<T>` полезен для:
- **Попарных операций** (сложение, сравнение).    
- **Агрегации данных** в стримах (`reduce`).    
- **Слияния коллекций** (`Map.merge`).    

**Пример с `Stream.reduce`:**

java

List<Integer> nums = List.of(1, 2, 3, 4);
int sum = nums.stream().reduce(0, Integer::sum); // 10