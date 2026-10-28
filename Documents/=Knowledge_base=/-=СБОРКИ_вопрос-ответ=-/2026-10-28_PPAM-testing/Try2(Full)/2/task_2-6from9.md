## 6/9: Which of these methods returns the description of an exception?

- getDescription()    
- getStackTrace()    
- getException()    
- getMessage()    

---
## 6/9: Какой из этих методов возвращает описание исключения?

- getDescription()    
- getStackTrace()    
- getException()    
- getMessage()

---
Правильный ответ: **getMessage()** (четвертый вариант).

**Объяснение:**

В Java класс `Throwable` (от которого наследуются все исключения и ошибки) содержит несколько методов для получения информации об ошибке:

1. **`getMessage()`** — возвращает строку с описанием исключения (текст сообщения, который был передан в конструктор исключения). Именно этот метод возвращает "описание" (description).
    
2. **`getStackTrace()`** — возвращает массив элементов `StackTraceElement`, представляющий собой стек вызовов в момент возникновения исключения.
    
3. **`toString()`** — возвращает краткое описание исключения, обычно включающее имя класса и сообщение (например, `java.lang.NullPointerException: Some message`).
    
4. **`printStackTrace()`** — выводит информацию об исключении (включая стек вызовов) в стандартный поток ошибок.    

Остальные варианты (`getDescription()`, `getException()`) не существуют в стандартном API исключений Java.

---
