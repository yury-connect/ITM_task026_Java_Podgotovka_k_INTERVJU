## 7/9: The result of the compilation and execution is:

```java
class A{
    A(){System.out.print("A1");}
    A(int i){ System.out.print("A" + 2);}
}
class B extends A{
    public static void main(String args[]){
        new B(3);
        System.out.println("B");
    }
}
```

- B    
- A2B    
- A3B    
- Compilation fails    
- An exception is thrown at runtime    

---
## 7/9: Результат компиляции и выполнения:

- B    
- A2B    
- A3B    
- Ошибка компиляции    
- Во время выполнения будет выброшено исключение

---
