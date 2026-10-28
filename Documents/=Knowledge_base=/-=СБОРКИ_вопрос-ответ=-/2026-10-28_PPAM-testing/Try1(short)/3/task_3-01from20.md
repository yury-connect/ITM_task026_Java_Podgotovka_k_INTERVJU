## 1/20: What is the result of the following statements?

```java
Predicate<? super String> predicate = s -> s.startsWith("J");
Stream<String> stream1 = Stream.generate(() -> "Java");
Stream<String> stream2 = Stream.generate(() -> "Java19");
boolean b1 = stream1.anyMatch(predicate);
boolean b2 = stream2.allMatch(predicate);
System.out.println(b1 + " " + b2);
```

- An exception is thrown at runtime    
- false false    
- Compilation fails    
- true false    
- true true    
- The code hangs    
- false true    

---
## 1/20: Каков результат следующих инструкций?

- Во время выполнения будет выброшено исключение    
- false false    
- Ошибка компиляции    
- true false    
- true true    
- Код зависнет    
- false true

---
