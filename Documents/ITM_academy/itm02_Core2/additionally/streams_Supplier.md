# **Примеры использования `Supplier<T>` в Java**

`Supplier<T>` — это функциональный интерфейс из `java.util.function`, который **не принимает аргументов**, но **возвращает результат типа `T`**.

---
## **1. Генерация случайного числа**
```java
Supplier<Integer> randomNumber = () -> new Random().nextInt(100);
System.out.println(randomNumber.get()); // Например: 42
```

## **2. Создание новых объектов**
```java
Supplier<List<String>> newListSupplier = ArrayList::new;
List<String> list = newListSupplier.get(); // Пустой ArrayList
list.add("Hello");
System.out.println(list); // ["Hello"]
```

## **3. Ленивая инициализация (например, для тяжелых объектов)**
```java
Supplier<BigInteger> factorial = () -> {
    BigInteger result = BigInteger.ONE;
    for (int i = 1; i <= 100; i++) {
        result = result.multiply(BigInteger.valueOf(i));
    }
    return result;
};

// Вычисляется только при вызове .get()
System.out.println(factorial.get()); // 9332621544394415268169923885... (очень большое число)
```

## **4. Получение текущего времени**
```java
Supplier<LocalDateTime> currentTime = LocalDateTime::now;
System.out.println(currentTime.get()); // 2025-07-11T14:30:00.123
```

## **5. Использование в `Optional.orElseGet`**
```java
Optional<String> optional = Optional.empty();

// Вычисляется, только если Optional пуст
String value = optional.orElseGet(() -> "Default Value");
System.out.println(value); // "Default Value"
```

## **6. Паттерн "Фабрика"**
```java
interface Shape {
    void draw();
}

class Circle implements Shape {
    public void draw() { System.out.println("○"); }
}

class Square implements Shape {
    public void draw() { System.out.println("□"); }
}

// Фабрика форм
Map<String, Supplier<Shape>> shapeFactory = Map.of(
    "circle", Circle::new,
    "square", Square::new
);

Shape shape = shapeFactory.get("circle").get();
shape.draw(); // ○
```

## **7. Генерация UUID**
```java
Supplier<String> uuidGenerator = () -> UUID.randomUUID().toString();
System.out.println(uuidGenerator.get()); // Например: "f47ac10b-58cc-4372-a567-0e02b2c3d479"
```

---
### **Ключевые особенности `Supplier<T>`**

- **Нет входных аргументов** → `() -> T`.    
- **Основной метод**: `T get()`.    
- **Ленивые вычисления** — код выполняется только при вызове `get()`.    

### **Где применяется?**

- `Optional.orElseGet(Supplier)` — ленивая альтернатива `orElse()`.    
- `Stream.generate(Supplier)` — бесконечные стримы.    
- Ленивая инициализация ресурсов.    
- Паттерны "Фабрика", "Строитель" (Builder).    

### **Сравнение с другими интерфейсами**

|Интерфейс|Вход|Выход|Пример использования|
|---|---|---|---|
|`Supplier<T>`|`void`|`T`|`() -> "Hello"`|
|`Consumer<T>`|`T`|`void`|`x -> System.out.println(x)`|
|`Function<T, R>`|`T`|`R`|`str -> str.length()`|
|`Predicate<T>`|`T`|`boolean`|`x -> x > 0`|

---
### **Итог**

`Supplier<T>` полезен для:
- **Отложенных вычислений** (ленивая инициализация).    
- **Генерации значений** (случайные числа, UUID).    
- **Фабричного создания объектов**.    
- **Работы с `Optional` и стримами**.    

Пример с `Stream.generate()`:
```java
// Бесконечный стрим случайных чисел
Stream.generate(() -> new Random().nextInt(100))
    .limit(5)
    .forEach(System.out::println); // Например: 42, 17, 88, 3, 91
```

---
