## 5/6: Are there any issues in the code?

```java
class M implements T1, T2 {
    public void m() {
    }
}

interface T1 {
    int VALUE = 1;
    void m();
}

interface T2 {
    int VALUE = 2;
    void m();
}
```

- The code will compile fine if VALUE is removed from one of the interfaces.    
- The code will compile fine if m() is removed from one of the interfaces.    
- Class M cannot implement both interfaces as it leads to ambiguity.    
- The code has no problems.    

---
## 5/6: Есть ли в коде какие-либо проблемы?

- Код скомпилируется без ошибок, если удалить VALUE из одного из интерфейсов.    
- Код скомпилируется без ошибок, если удалить m() из одного из интерфейсов.    
- Класс M не может реализовывать оба интерфейса, так как это приводит к неоднозначности.    
- В коде нет проблем.

---
