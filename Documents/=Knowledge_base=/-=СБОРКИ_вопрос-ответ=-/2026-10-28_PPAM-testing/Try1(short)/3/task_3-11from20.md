## 11/20: Which TWO of these statements concerning the collection interfaces are true?

-  Set extends Collection    
-  HashMap extends Hashtable    
-  All methods defined in Collection are also defined in List    
-  HashMap implements SortedMap    

---
## 11/20: Какие ДВА из этих утверждений относительно интерфейсов коллекций верны?

- Set расширяет (extends) Collection    
- HashMap расширяет (extends) Hashtable    
- Все методы, определенные в Collection, также определены в List    
- HashMap реализует (implements) SortedMap

---
Правильные ответы (нужно выбрать два):  
**1. `Set extends Collection`** (первый вариант)  
**2. `All methods defined in Collection are also defined in List`** (третий вариант)

**Объяснение:**

1. **`Set extends Collection` — Истина.** В иерархии коллекций Java интерфейс `Set` является наследником (расширяет) интерфейса `Collection`. Он добавляет поведение, гарантирующее отсутствие дубликатов.
    
2. **`HashMap extends Hashtable` — Ложь.** Класс `HashMap` и класс `Hashtable` — это два разных класса. `HashMap` наследуется от `AbstractMap`, а `Hashtable` наследуется от `Dictionary`. Они не являются родственниками по иерархии наследования (хотя реализуют общий интерфейс `Map`).
    
3. **`All methods defined in Collection are also defined in List` — Истина.** Интерфейс `List` расширяет интерфейс `Collection` (`List extends Collection`). По правилам ООП, наследник наследует все методы родительского интерфейса. Следовательно, все методы, которые есть в `Collection` (например, `add`, `remove`, `size`, `iterator`), также определены и в `List`.
    
4. **`HashMap implements SortedMap` — Ложь.** `HashMap` реализует интерфейс `Map`, но не `SortedMap`. Иерархия интерфейсов для отсортированных карт выглядит так: `Map` -> `SortedMap` -> `NavigableMap`. Класс `HashMap` не реализует `SortedMap` (он не поддерживает сортировку). `SortedMap` реализует другой класс — `TreeMap`.

---
