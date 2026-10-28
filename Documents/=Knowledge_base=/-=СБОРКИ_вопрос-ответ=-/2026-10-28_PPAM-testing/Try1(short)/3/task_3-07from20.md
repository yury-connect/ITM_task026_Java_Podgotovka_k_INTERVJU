## 7/20: Which TWO of these methods are defined in the Map interface?

- toArray()    
- remove(Object o)    
- addAll(Collection c)    
- contains(Object o)    
- values()    

---
## 7/20: Какие ДВА из этих методов определены в интерфейсе Map?

- toArray()    
- remove(Object o)    
- addAll(Collection c)    
- contains(Object o)    
- values()

---
Правильные ответы (нужно выбрать два):  
**1. remove(Object o)** (второй вариант)  
**2. values()** (пятый вариант)

**Объяснение:**

Интерфейс `Map` в Java имеет свои собственные методы и **не наследуется** от интерфейса `Collection` (в отличие от `List` или `Set`). Поэтому методы, характерные для коллекций, здесь не работают напрямую.

Разберем варианты:

1. **`toArray()`** — **Нет.** Этот метод определен в интерфейсе `Collection`. У `Map` нет такого метода (преобразовать в массив можно только отдельно ключи через `keySet().toArray()` или значения через `values().toArray()`).
    
2. **`remove(Object o)`** — **Да.** В интерфейсе `Map` есть метод `remove(Object key)`, который удаляет пару ключ-значение по указанному ключу. (Обратите внимание, в скобках стоит тип `Object`, так как дженерики стираются, но по смыслу это ключ).
    
3. **`addAll(Collection c)`** — **Нет.** Этот метод есть в `Collection`. В `Map` для добавления используется `put()` или `putAll()`.
    
4. **`contains(Object o)`** — **Нет.** В `Map` нет метода `contains()`. Вместо него есть два специфичных метода: `containsKey(Object key)` и `containsValue(Object value)`.
    
5. **`values()`** — **Да.** Этот метод возвращает `Collection`, содержащую все значения (values), хранящиеся в данной карте.

---
