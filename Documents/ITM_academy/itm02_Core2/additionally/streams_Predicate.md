# **Примеры использования `Predicate<T>` в Java**

`Predicate<T>` — это функциональный интерфейс из `java.util.function`, который проверяет условие и возвращает `boolean`.

#### **1. Простая проверка числа (чётное/нечётное)**
```java
Predicate<Integer> isEven = num -> num % 2 == 0;
System.out.println(isEven.test(4)); // true
System.out.println(isEven.test(5)); // false
```

#### **2. Фильтрация списка строк (длина > 3)**
```java
List<String> words = Arrays.asList("Java", "Kotlin", "C++", "Go");
Predicate<String> isLongerThan3 = s -> s.length() > 3;

List<String> filtered = words.stream()
    .filter(isLongerThan3)
    .toList(); // ["Java", "Kotlin"]
```

#### **3. Комбинирование предикатов (`and`, `or`, `negate`)**
```java
Predicate<Integer> isPositive = num -> num > 0;
Predicate<Integer> isEven = num -> num % 2 == 0;

// "Положительное И чётное"
Predicate<Integer> isPositiveAndEven = isPositive.and(isEven);
System.out.println(isPositiveAndEven.test(6)); // true

// "Отрицательное ИЛИ нечётное"
Predicate<Integer> isNegativeOrOdd = isPositive.negate().or(isEven.negate());
System.out.println(isNegativeOrOdd.test(-3)); // true
```

#### **4. Проверка объекта (например, возраст > 18)**
```java
class Person {
    String name;
    int age;
    // конструктор, геттеры...
}

Predicate<Person> isAdult = p -> p.getAge() >= 18;

Person alice = new Person("Alice", 20);
System.out.println(isAdult.test(alice)); // true
```

#### **5. Удаление элементов из коллекции (`Collection.removeIf`)**
```java
List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
numbers.removeIf(num -> num % 2 == 0); // Удалить чётные
System.out.println(numbers); // [1, 3, 5]
```

#### **6. Проверка на `null` (комбинация с `Objects.nonNull`)**
```java
Predicate<String> isNotNull = Objects::nonNull;
Predicate<String> isEmpty = String::isEmpty;

// "Не null И не пустая строка"
Predicate<String> isValid = isNotNull.and(isEmpty.negate());

System.out.println(isValid.test("Hello")); // true
System.out.println(isValid.test(""));     // false
```

### **Ключевые моменты:**

- `Predicate<T>` часто используется в `Stream.filter()`, `Collection.removeIf()`.
    
- Можно комбинировать через `and()`, `or()`, `negate()`.
    
- Удобен для проверок условий без создания отдельного метода.
    

**Лучшие практики:**

- Для сложных условий выносите `Predicate` в отдельные переменные.
    
- Используйте **метод-референсы** (`String::isEmpty`, `Objects::nonNull`), где это упрощает код.

---
