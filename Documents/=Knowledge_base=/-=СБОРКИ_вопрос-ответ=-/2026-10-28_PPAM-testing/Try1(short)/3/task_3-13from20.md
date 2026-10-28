## 13/20: What is the result of compiling and running the code below?

```java
class Count<T> {
    private T counter;
    public Count(T counter) {
        this.counter = counter;
    }
    public int toInt() {
        return counter.intValue(); //line1
    }
    public static void main(String[] args) {
        System.out.print(new Count(3.01)); //line2
        System.out.print(new Count(7.9)); //line3
    }
}
```

- 37    
- 38    
- Compilation fails at line1    
- Compilation fails at line2    
- Compilation fails at line3    

---
## 13/20: Каков результат компиляции и выполнения приведенного ниже кода?

- 37    
- 38    
- Ошибка компиляции в строке 1 (line1)    
- Ошибка компиляции в строке 2 (line2)    
- Ошибка компиляции в строке 3 (line3)

---
