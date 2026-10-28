## 4/9: What is the purpose of the Collections class?

- To provide a way to store and manipulate groups of objects.    
- To handle user input.    
- To create new objects.    
- To define the structure of objects.    

---
## 4/9: Каково назначение класса Collections?

- Предоставить способ хранения и управления группами объектов.    
- Обрабатывать пользовательский ввод.    
- Создавать новые объекты.    
- Определять структуру объектов.

---
Правильный ответ: **To provide a way to store and manipulate groups of objects** (первый вариант).

**Объяснение:**

В Java есть два похожих по названию понятия, которые часто путают:
1. **`Collection` (интерфейс)** — это корневой интерфейс для большинства структур данных (List, Set, Queue), который как раз и предназначен для хранения и манипулирования группами объектов.    
2. **`Collections` (класс, с буквой "s" на конце)** — это **утилитный класс** (utility class), состоящий только из статических методов.    

Хотя вопрос спрашивает именно про класс `Collections`, предложенный вариант ответа ("предоставить способ хранения и манипулирования группами объектов") является общим описанием **фреймворка коллекций (Collections Framework)** в целом. Среди предложенных вариантов это единственный логичный ответ, описывающий назначение коллекций.

На практике класс `java.util.Collections` используется для выполнения различных операций над коллекциями:

- **Сортировка:** `Collections.sort(list)`    
- **Поиск:** `Collections.binarySearch(list, key)`    
- **Создание неизменяемых коллекций:** `Collections.unmodifiableList(list)`    
- **Создание пустых коллекций:** `Collections.emptyList()`    
- **Реверсирование:** `Collections.reverse(list)`    

Остальные варианты не подходят:
- _To handle user input_ — для этого используется пакет `java.util.Scanner` или потоки ввода-вывода.    
- _To create new objects_ — для этого используется оператор `new` и конструкторы.    
- _To define the structure of objects_ — для этого используются классы и интерфейсы.

---
