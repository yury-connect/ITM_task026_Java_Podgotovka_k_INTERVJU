# Расскажите про паттерн Декоратор (`Decorator`).

---
## 🎯 `Decorator`: Суть
**Структурный паттерн**, который:
* **Динамически** добавляет объектам новые обязанности.
* Альтернатива **наследованию** для расширения функциональности.
* Сохраняет **интерфейс** исходного объекта, но **усиливает** его поведение.

---
## 📌 `Decorator`: Глубокий разбор
### 1. Зачем нужен?
* **Проблема:**  
   Наследование негибко:

```java
class FileReaderWithBufferAndCompression extends FileReader { ... } // ❌ Комбинаторный взрыв  
```

* **Решение:**

```java
FileStream reader = new FileBufferedReader(new FileCompressor(new FileStreamReader()));  
reader.read(); // Чтение + буферизация + сжатие
```

### 2. Компоненты

| Компонент                                       | 	Роль                                                     |
|:------------------------------------------------|:----------------------------------------------------------|
| **Компонент** (`FileStream`)                    | 	Интерфейс базового объекта (`read()`).                   |
| **Конкретный компонент** (`FileStreamReader`)   | 	Исходный объект с основной функциональностью.            |
| **Декоратор** (`FileDecorator`)                 | 	Абстрактный класс-обёртка, хранящий ссылку на компонент. |
| **Конкретный декоратор** (`FileBufferedReader`) | 	Добавляет новое поведение (_например, буферизацию_).     |

### 3. Плюсы  
   ✔ **Гибкость**: Можно комбинировать декораторы на лету.  
   ✔ **Открытость/закрытость**: Новые функции без изменения кода.  
   ✔ **Чистота кода**: Избегает сложных иерархий наследования.  

### 4. Минусы  
   ❌ **Сложность отладки**: Цепочка вызовов через несколько обёрток.  
   ❌ **Неочевидность**: Поведение объекта зависит от порядка декораторов.  

### 5. Когда использовать?  
* Нужно **добавлять/удалять** функции **динамически**.  

* Нельзя расширить класс через **наследование** (_например, `final`-классы в Java_).

* Требуется **множество независимых** расширений.

### 6. Пример кода

<details>
        <summary>пример 🔽</summary>

```java
   // Базовый интерфейс  
interface FileStream {
    void read();
}

// Конкретный компонент  
class FileStreamReader implements FileStream {
    @Override
    public void read() {
        System.out.println("read file");
    }
}

// Абстрактный декоратор  
abstract class FileDecorator implements FileStream {
    protected FileStream fileDecorator;
    public FileDecorator(FileStream fileDecorator) {
        this.fileDecorator = fileDecorator;
    }
}

// Конкретный декоратор  
class FileBufferedReader extends FileDecorator {
    public FileBufferedReader(FileStream fileDecorator) {
        super(fileDecorator);
    }
    @Override
    public void read() {
        fileDecorator.read(); // Базовая функциональность  
        System.out.println("buffered read"); // Дополнение  
    }
}

// Использование:  
FileStream reader = new FileBufferedReader(new FileStreamReader());  
reader.read(); // Выведет: "read file" → "buffered read"
```
</details>


### 7. Отличие от _других_ паттернов

| Паттерн     | 	Отличие                                                      |
|:------------|:--------------------------------------------------------------|
| `Adapter`   | 	Меняет интерфейс объекта, а не его поведение.                |
| `Composite` | 	Управляет группой объектов, а не расширяет функциональность. |

> **⚡ Итог:**  
> Используйте `Decorator` для:  
> * **Расширения объектов** без наследования.  
> * **Динамического** добавления функций (_логирование, кеширование и т.д._).  

**Примеры в Java:**
* `java.io` (`BufferedReader`, `ZipInputStream`).
* GUI-библиотеки (_добавление скролла, границ к компонентам_).



<details>
        <summary>📝 Материал из методички 🔽</summary>

```text
Структурный паттерн проектирования, который позволяет добавлять объектам 
новую функциональность, оборачивая их в полезные «обёртки».
(Надстройка когда уже есть готовый функционал(класс) и мы хотим вызывать 
этот же функционал но с добавлением своей реализации)

Целевой объект помещается в другой объект-обёртку, который запускает базовое поведение 
обёрнутого объекта, а затем добавляет к результату что-то своё.

Оба объекта имеют общий интерфейс, поэтому для пользователя нет никакой разницы, 
с каким объектом работать — чистым или обёрнутым. 
Вы можете использовать несколько разных обёрток одновременно — результат будет иметь 
объединённое поведение всех обёрток сразу.

Адаптер не менят состояния объекта, а декоратор может менять.

+: Большая гибкость, чем у наследования.
- : Труднее конфигурировать многократно обёрнутые объекты.
```
</details>

---

[🔙 _к списку вопросов по теме_ **Patterns** 🔙](/_ITM_old_version_FOR_DELETE/ITM07_Patterns/patterns.md)
