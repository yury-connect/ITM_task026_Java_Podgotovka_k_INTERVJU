## 8/20: What method of the HttpSession interface allows a servlet to determine the last time the session was accessed before the current request?

- getLastTime()    
- getLastAccessedTime()    
- getTime()    
- getPreviousAccessTime()    

---
## 8/20: Какой метод интерфейса HttpSession позволяет сервлету определить время последнего обращения к сессии перед текущим запросом?

- getLastTime()    
- getLastAccessedTime()    
- getTime()    
- getPreviousAccessTime()

---
Правильный ответ: **getLastAccessedTime()** (второй вариант).

**Объяснение:**

В Java Servlet API интерфейс `HttpSession` содержит метод **`getLastAccessedTime()`**.

Этот метод возвращает время (в миллисекундах с 1 января 1970 года), когда клиент последний раз отправлял запрос, связанный с этой сессией, **до текущего запроса**. Это используется, например, для вычисления времени бездействия сессии (idle time) и принятия решения о ее завершении (timeout).

Остальные варианты не существуют в стандартном API:

- `getLastTime()` — нет такого метода.    
- `getTime()` — нет такого метода в `HttpSession`.    
- `getPreviousAccessTime()` — нет такого метода.

---
