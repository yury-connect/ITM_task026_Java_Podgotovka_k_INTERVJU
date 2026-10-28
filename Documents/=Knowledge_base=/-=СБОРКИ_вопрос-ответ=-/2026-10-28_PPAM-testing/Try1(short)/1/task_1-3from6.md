## 3/6: The result of the following compilation and execution is:

```java
public abstract class Point1D {
    protected final int x;
    public Point1D(int x) {
        this.x = x;
    }
}
interface Colorable {}
class Point2D extends Point1D implements Colorable {
    public Point2D() {
        super(7);
    }
    public Point1D get() {
        return this;
    }
    public static void main(String[] args) {
        Colorable obj = (Colorable) (Point1D) new Point2D().get();
        System.out.print(((Point2D) obj).x);
    }
}
```

- An ClassCastException is thrown at runtime    
- Compilation fails for a different reason    
- 7    
- Compilation fails due an invalid cast    

---
## 3/6: Результат следующей компиляции и выполнения:

- Во время выполнения будет выброшено исключение ClassCastException    
- Ошибка компиляции по другой причине    
- 7    
- Ошибка компиляции из-за недопустимого приведения типов (invalid cast)

---
