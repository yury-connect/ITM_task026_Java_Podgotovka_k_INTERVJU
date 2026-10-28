## 3/9: Given the record Human, what is the result of running the code below?

```java
import java.util.TreeSet;
record Human(String name) implements Comparable {
    public int compareTo(Object o){
        return 0;
    }
    public String toString() {
        return name;
    }
}
public class R1 {
    public static void main(String[] args) {
        Human human1 = new Human("Jan");
        Human human2 = new Human("Jakub");
        TreeSet<Human> set = new TreeSet<>();
        set.add(human1);
        set.add(human2);
        System.out.print(set);
    }
}
```

- The code runs with no output    
- `[Jakub]`    
- Compilation fails    
- An exception is thrown at runtime    
- `[Jan]`    
- `[Jan, Jakub]`    

---
## 3/9: Для данной записи (record) Human, каким будет результат выполнения приведенного ниже кода?

- Код выполнится без вывода- 
- `[Jakub]`
- Ошибка компиляции    
- Во время выполнения будет выброшено исключение
- `[Jan]`
- `[Jan, Jakub]`

---
