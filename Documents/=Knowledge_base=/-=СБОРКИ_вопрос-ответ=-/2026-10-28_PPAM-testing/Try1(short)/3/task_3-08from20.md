## 8/20: Given an instance of a Stream, s, and a Collection, c, which TWO are valid ways of creating a parallel stream?

- new ParallelStream(c)    
- s.parallel()    
- c.parallel()    
- c.parallelStream()    
- s.parallelStream()    
- new ParallelStream(s)    

---
## 8/20: Учитывая экземпляр Stream (потока) s и Collection (коллекцию) c, какие ДВА способа являются допустимыми для создания параллельного потока?

---
Правильные ответы (нужно выбрать два):  
**1. `s.parallel()`** (второй вариант)  
**2. `c.parallelStream()`** (четвертый вариант)

**Объяснение:**

В Java Stream API есть два основных способа получить параллельный поток (parallel stream):

1. **Из существующего потока:** Если у вас уже есть объект `Stream` (в данном случае `s`), вы можете вызвать у него метод **`parallel()`**. Этот метод преобразует последовательный поток в параллельный и возвращает его (тип `Stream`).
    - _Пример:_ `Stream<String> parallelStream = s.parallel();`
    
2. **Из коллекции:** Если у вас есть объект, реализующий интерфейс `Collection` (в данном случае `c`), вы можете вызвать у него метод **`parallelStream()`**. Этот метод является методом по умолчанию (default method) в интерфейсе `Collection` и сразу создает параллельный поток из элементов коллекции.
    - _Пример:_ `Stream<String> parallelStream = c.parallelStream();`

Остальные варианты неверны:

- `new ParallelStream(c)` и `new ParallelStream(s)` — класса `ParallelStream` в стандартной библиотеке Java не существует, потоки создаются через фабричные методы. Кроме того, интерфейсы/классы потоков нельзя создать через `new`.
    
- `c.parallel()` — в интерфейсе `Collection` нет метода `parallel()` (он есть только у `Stream`).
    
- `s.parallelStream()` — у интерфейса `Stream` нет метода `parallelStream()`.

---
