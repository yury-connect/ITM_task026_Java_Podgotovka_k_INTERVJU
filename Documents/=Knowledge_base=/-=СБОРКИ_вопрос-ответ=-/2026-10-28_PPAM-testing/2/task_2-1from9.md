## 1/9: Which TWO, inserted independently at line1, will allow the code to compile and produce the output 42?

```java
class H {
    // line1
    protected H(int x) {
        System.out.print(x);
    }
}
class W extends H {
    public static void main(String[] args) {
        W w = new W();
    }
}
```

- protected H() {this(42);}    
- protected H() {super(42);}    
- protected H() {new H(42);}    
- protected H() {H(42);}

---
## 1/9: Какие ДВА варианта, вставленные независимо друг от друга в строку //line1, позволят коду скомпилироваться и вывести 42?

- protected H() {this(42);}    
- protected H() {super(42);}    
- protected H() {new H(42);}    
- protected H() {H(42);}

---
