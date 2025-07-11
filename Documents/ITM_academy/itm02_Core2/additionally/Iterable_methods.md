### **Методы интерфейса `Iterable<T>` в Java**

Интерфейс `Iterable<T>` появился в Java 5 и является основой для **for-each** цикла. Он содержит следующие методы:

---

### **1. `Iterator<T> iterator()`**

**Основной метод**, возвращающий итератор для перебора элементов.
```java
List<String> list = List.of("A", "B", "C");
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    System.out.println(it.next());
}
```

**Вывод:**
```text
A
B
C
```

---
### **2. `void forEach(Consumer<? super T> action)`** _(с Java 8)_

Принимает `Consumer` и выполняет действие для каждого элемента.
```java
List<String> list = List.of("X", "Y", "Z");
list.forEach(s -> System.out.println(s.toLowerCase()));
```

**Вывод:**
```text
x
y
z
```

---
### **3. `Spliterator<T> spliterator()`** _(с Java 8)_

Возвращает **Spliterator** (специальный итератор для параллельной обработки).
```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5);
Spliterator<Integer> split = numbers.spliterator();
split.forEachRemaining(n -> System.out.println(n * 2));
```

**Вывод:**
```text
2
4
6
8
10
```

---
### **Итого: методы `Iterable<T>`**

|Метод|Описание|С версии Java|
|---|---|---|
|`Iterator<T> iterator()`|Возвращает итератор для последовательного перебора|5|
|`void forEach(Consumer<? super T> action)`|Выполняет действие для каждого элемента|8|
|`Spliterator<T> spliterator()`|Возвращает итератор для параллельной обработки|8|

---
### **Как `for-each` связан с `Iterable`?**

Когда вы пишете:
```java
for (String item : list) { ... }
```

**Компилятор разворачивает это в:**
```java
Iterator<String> it = list.iterator();  // Вызывает iterator()
while (it.hasNext()) {
    String item = it.next();
    ...
}
```

**Условие:** объект должен быть массивом или реализовывать `Iterable`.

---
### **Пример кастомного `Iterable`**
```java
class MyCollection<T> implements Iterable<T> {
    private List<T> items = new ArrayList<>();

    public void add(T item) {
        items.add(item);
    }

    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }
}

// Использование:
MyCollection<String> coll = new MyCollection<>();
coll.add("Hello");
coll.add("World");

for (String s : coll) {  // Работает благодаря iterator()
    System.out.println(s);
}
```

**Вывод:**
```text
Hello
World
```

---
### **Вывод**
- **`Iterable`** — это интерфейс с тремя методами:    
    - `iterator()` (обязательный),        
    - `forEach()` и `spliterator()` (добавлены в Java 8).
        
- **`for-each`** использует `iterator()` "под капотом".
    
- Любой класс, реализующий `Iterable`, можно перебирать в `for-each`.
	

---
