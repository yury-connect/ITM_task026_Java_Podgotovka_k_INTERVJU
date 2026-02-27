

Что можно ожидать на выводе и как исправить?
```java
public class Demo {
    static int x = 0;
	
    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(() -> { for (int i = 0; i < 100_000; i++) x++; });
        Thread t2 = new Thread(() -> { for (int i = 0; i < 100_001; i++) x++; });
		
        t1.start(); t2.start();
        t1.join(); t2.join();
		
        System.out.println(x);
    }
}
```





