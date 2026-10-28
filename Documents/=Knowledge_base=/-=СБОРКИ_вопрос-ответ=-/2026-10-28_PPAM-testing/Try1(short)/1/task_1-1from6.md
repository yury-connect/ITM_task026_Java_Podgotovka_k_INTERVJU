## 1/6: What will the program compiling and executing result be?

```java
public class StaticQuest5 {
    {
        System.out.print(s);
    }
    static{
        s = "S";
    }
    static String s = "T";
    public StaticQuest5() {
        System.out.print(s);
    }
    public static void main(String[] args) {
        new StaticQuest5();
    }
}
```

- TS    
- TT    
- ST    
- SS    
- Compilation fails    

---
## 1/6: Каким будет результат компиляции и выполнения программы?

- TS    
- TT    
- ST    
- SS    
- Ошибка компиляции (Compilation fails)

---

