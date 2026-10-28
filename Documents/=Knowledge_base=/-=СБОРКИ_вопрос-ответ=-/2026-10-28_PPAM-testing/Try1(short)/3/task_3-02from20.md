## 2/20: The result of the following compilation is:

```java
class A{}
class B{}
public class Main{
    void f(A a){
        System.out.println("A");
    }
    static void f(B b){
        System.out.println("B");
    }
    public static void main(String[] args){
        f(new B());
    }
}
```

- A    
- B    
- Compilation fails    
- An exception is thrown at runtime    

---
## 2/20: Результат следующей компиляции:

- A    
- B    
- Ошибка компиляции    
- Во время выполнения будет выброшено исключение

---
