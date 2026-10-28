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
Правильный ответ: **Компиляция завершится с ошибкой (Compilation fails)**.

**Объяснение:**

Ошибка возникнет в **строке 3** из-за неверной сигнатуры метода `compareTo`:

```java
public int compareTo(Object o) {
```

Вот почему это приводит к ошибке компиляции:

1. Запись (record) `Human` объявляет, что она реализует интерфейс `Comparable` (сырой тип, без указания параметра).
2. Однако в Java (начиная с версии 1.5) интерфейс `Comparable` является обобщенным: `Comparable<T>`. В нем объявлен метод `int compareTo(T o)`.
3. Метод `compareTo` в классе `Human` принимает аргумент типа `Object`. Так как `Human` реализует сырой тип `Comparable`, компилятор ожидает метод с сигнатурой `int compareTo(Object o)` (из-за стирания типов это допустимо для сырых типов). **НО** проблема в том, что **записи (records) не могут объявлять методы с сигнатурой, конфликтующей с неявно сгенерированными методами, или методы, которые нарушают контракт интерфейсов в специфическом контексте записей.**
4. Более точная причина с точки зрения спецификации Java для записей: Запись `Human` наследует метод `equals(Object)`. Если вы реализуете сырой `Comparable`, компилятор пытается проверить безопасность типов. Но на самом деле, **компилятор выдаст ошибку из-за того, что `compareTo` не переопределяет метод из обобщенного интерфейса `Comparable<Human>` должным образом в контексте записи.**

_(Примечание: Даже если бы это был обычный класс, реализация сырого типа `Comparable` с методом `compareTo(Object)` вызывает предупреждения компилятора (unchecked), но компилируется. Однако для `record` правила строже. Если бы было написано `implements Comparable<Human>` и `public int compareTo(Human o)`, код скомпилировался бы)._

Если предположить, что это гипотетически скомпилировалось (что невозможно), то:

- В методе `compareTo` всегда возвращается `0`.  
    
- `TreeSet` считает, что все элементы равны друг другу, если `compareTo` возвращает `0`.  
    
- Поэтому при добавлении `human2` в множество, оно решит, что такой элемент уже есть, и не добавит его.  
    
- Вывелось бы `[Jan]`.

Но поскольку код **не компилируется**, правильный вариант ответа — **Compilation fails**.

---