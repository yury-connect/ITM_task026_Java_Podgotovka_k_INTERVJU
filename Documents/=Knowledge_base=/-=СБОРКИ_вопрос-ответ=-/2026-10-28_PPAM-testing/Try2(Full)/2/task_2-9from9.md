## 9/9: What is the result of running the code below?

```java
List<Integer> list = List.of(1, 3, 5, 7, 9);
LinkedList<Integer> list2 = new LinkedList<>(list);
list2.add(0, 0);
var index = Collections.indexOfSubList(list2, list);
System.out.println(index);
```

- -1    
- Compilation fails.    
- 2    
- 1    
- 0    
- An exception is thrown at runtime.    

---
## 9/9: Каков результат выполнения приведенного ниже кода?

- -1    
- Ошибка компиляции.    
- 2    
- 1    
- 0    
- Во время выполнения будет выброшено исключение.

---
Правильный ответ: **1** (четвертый вариант).

**Пошаговое объяснение:**

1. **Строка 1:** `List<Integer> list = List.of(1, 3, 5, 7, 9);`  
    Создается неизменяемый (immutable) список `list` из пяти чисел: `[1, 3, 5, 7, 9]`.
    
2. **Строка 2:** `LinkedList<Integer> list2 = new LinkedList<>(list);`  
    Создается новый изменяемый список `list2` (тип `LinkedList`), в который копируются все элементы из `list`.  
    Текущее состояние `list2`: `[1, 3, 5, 7, 9]`.
    
3. **Строка 3:** `list2.add(0, 0);`  
    В список `list2` по индексу `0` вставляется число `0`. Остальные элементы сдвигаются вправо.  
    Новое состояние `list2`: **`[0, 1, 3, 5, 7, 9]`**.
    
4. **Строка 4:** `var index = Collections.indexOfSubList(list2, list);`  
    Метод `Collections.indexOfSubList(source, target)` ищет первое вхождение подсписка `target` в списке `source` и возвращает индекс, с которого начинается это вхождение.	    
    - `source` (`list2`): `[0, 1, 3, 5, 7, 9]`        
    - `target` (`list`): `[1, 3, 5, 7, 9]`        
    
    Программа ищет, где в `list2` находится последовательность `[1, 3, 5, 7, 9]`. Эта последовательность начинается с индекса **`1`** (сразу после нуля).
    
5. **Строка 5:** `System.out.println(index);` выведет **`1`**.

---
