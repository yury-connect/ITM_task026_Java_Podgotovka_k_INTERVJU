## 4/6: Which one of the following definitions of the Rectangle constructor can be placed without compiler errors instead of the comment line0?

```java
class Rectangle {
    private int length, width;
    public Rectangle(int length, int width) {
        this.length = length;
        this.width = width;
    }
    //line0
}
```

Варианты:

```java
public Rectangle() {
    super();
    this(0, 0);
}
```

```java
public Rectangle() {
    this(0, 0);
    super();
}
```

```java
public Rectangle() {
    this();
}
```

```java
public Rectangle() {  
	this(x, 0);  
}
```

```java
public Rectangle() {  
	this(0, 0);  
}
```

---
## 4/6: Какое из следующих определений конструктора Rectangle можно разместить без ошибок компиляции вместо строки с комментарием line0?

---

