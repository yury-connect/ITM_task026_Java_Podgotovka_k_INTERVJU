## 2/6: The result of the following compilation and execution is:

```java
interface I1 {
    default void m(){
        System.out.print("1");
    }
}
interface I2 {
    default void m(){
        System.out.print("2");
    }
}
class A implements I1, I2 { //line1
}
class Main{
    public static void main(String[] args) {
        I1 i1 = new A();
        I2 i2 = new A(); //line2
        i1.m(); //line3
        i2.m(); //line3
    }
}
```

- 12    
- An exception is thrown at runtime    
- Compilation fails at line1    
- Compilation fails at line2    
- Compilation fails at line3    

---
## 2/6: Результат следующей компиляции и выполнения:

- 12    
- Во время выполнения будет выброшено исключение    
- Ошибка компиляции в строке 1 (line1)    
- Ошибка компиляции в строке 2 (line2)    
- Ошибка компиляции в строке 3 (line3)

---
