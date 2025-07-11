# **Примеры использования `Consumer<T>` в Java**

`Consumer<T>` — это функциональный интерфейс из `java.util.function`, который принимает один аргумент (`T`) и **не возвращает результат** (`void`).

---

## **1. Простой вывод в консоль**
```java
Consumer<String> printUpperCase = s -> System.out.println(s.toUpperCase());
printUpperCase.accept("hello"); // HELLO
```
## **2. Изменение объекта (например, увеличение зарплаты)**
```java
class Employee {
    String name;
    double salary;
    // конструктор, геттеры, сеттеры...
}

Consumer<Employee> raiseSalary = e -> e.setSalary(e.getSalary() * 1.1);

Employee emp = new Employee("Alice", 1000);
raiseSalary.accept(emp);
System.out.println(emp.getSalary()); // 1100.0
```

## **3. Добавление элементов в коллекцию**
```java
List<String> names = new ArrayList<>();
Consumer<String> addToList = names::add; // Метод-референс

addToList.accept("John");
addToList.accept("Anna");
System.out.println(names); // [John, Anna]
```

## **4. Комбинирование `Consumer` (andThen)**
```java
Consumer<String> print = System.out::println;
Consumer<String> log = s -> System.err.println("LOG: " + s);

// Сначала выводим в stdout, затем в stderr
Consumer<String> printAndLog = print.andThen(log);
printAndLog.accept("Test"); 
// Вывод:
// Test
// LOG: Test
```

## **5. Использование в `forEach` (списки, стримы)**
```java
List<Integer> numbers = List.of(1, 2, 3);

// Вариант 1: лямбда
numbers.forEach(n -> System.out.println(n * 2)); // 2, 4, 6

// Вариант 2: метод-референс
numbers.forEach(System.out::println); // 1, 2, 3
```

## **6. Модификация Map (например, обновление всех значений)**
```java
Map<String, Integer> map = new HashMap<>();
map.put("A", 1);
map.put("B", 2);

Consumer<Map.Entry<String, Integer>> incrementValue = 
    entry -> entry.setValue(entry.getValue() + 10);

map.entrySet().forEach(incrementValue);
System.out.println(map); // {A=11, B=12}
```

## **7. Запись в файл**
```java
Consumer<String> writeToFile = line -> {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
        writer.write(line + "\n");
    } catch (IOException e) {
        e.printStackTrace();
    }
};

writeToFile.accept("New log entry");
```

---
### **Ключевые особенности `Consumer<T>`**

- **Не возвращает значение** (в отличие от `Function<T, R>` или `Predicate<T>`).
    
- **Используется для side-эффектов**: вывод, изменение объекта, запись в файл и т.д.
    
- **Методы**:    
    - `accept(T t)` — выполнить действие.        
    - `andThen(Consumer after)` — цепочка вызовов.        

### **Где применяется?**
- `Iterable.forEach(Consumer)`    
- `Stream.peek(Consumer)`    
- `Optional.ifPresent(Consumer)`    
- Любые операции, где нужно "что-то сделать" с объектом.    

### **Сравнение с другими интерфейсами**

|Интерфейс|Вход|Выход|Пример использования|
|---|---|---|---|
|`Consumer<T>`|`T`|`void`|`list.forEach(x -> ...)`|
|`Function<T, R>`|`T`|`R`|`str -> str.length()`|
|`Predicate<T>`|`T`|`boolean`|`x -> x > 0`|

---
### **Итог**

`Consumer<T>` — это мощный инструмент для **действий над объектами** без возврата результата.  

Используйте его для:
- Модификации объектов.    
- Логирования.    
- Операций ввода-вывода.    
- Цепочки действий через `andThen`.

---
