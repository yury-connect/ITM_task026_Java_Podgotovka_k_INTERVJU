## 5/20: Running the code below creates the object B b = new B(). What values will the fields of this object get during deserialization?

```java
class A {
    public int a = 0;
    public A() {
        a = 1;
    }
}

class B extends A implements Serializable {
    public int b = 0;
    public B() {
        a = 2;
        b = 3;
    }
}
```

- b.a=0, b.b=0    
- b.a=0, b.b=3    
- Deserialization will not be possible because class A does not implement the Serializable interface.    
- b.a=1, b.b=3    
- b.a=1, b.b=0    
- b.a=2, b.b=3    

---
## 5/20: Выполнение приведенного ниже кода создает объект B b = new B(). Какие значения получат поля этого объекта во время десериализации?

- b.a=0, b.b=0    
- b.a=0, b.b=3    
- Десериализация будет невозможна, так как класс A не реализует интерфейс Serializable.    
- b.a=1, b.b=3    
- b.a=1, b.b=0    
- b.a=2, b.b=3

---
