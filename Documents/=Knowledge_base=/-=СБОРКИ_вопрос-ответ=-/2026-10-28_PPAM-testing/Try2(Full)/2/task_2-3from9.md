## 3/9: What will be the result of attempting to run the following program?

```java
String[][] arr = {
    { "1", null },
    { "5", "8" },
    { "2", null, "1" },
    { "4", null }
};
System.out.print(arr.length + arr[1][2].length);
```

- An NullPointerException is thrown at runtime.    
- An ArrayIndexOutOfBoundsException is thrown at runtime.    
- Number 4 will be written to standard output.    
- Number 6 will be written to standard output.    
- Number 7 will be written to standard output.    

---
## 3/9: Каким будет результат попытки выполнить следующую программу?

- Во время выполнения будет выброшено исключение NullPointerException.    
- Во время выполнения будет выброшено исключение ArrayIndexOutOfBoundsException.    
- В стандартный поток вывода будет записано число 4.    
- В стандартный поток вывода будет записано число 6.    
- В стандартный поток вывода будет записано число 7.

---
Правильный ответ: **An ArrayIndexOutOfBoundsException is thrown at runtime** (второй вариант).

**Объяснение:**

Давайте проанализируем структуру массива `arr` и выражение в строке 6: `arr.length + arr[1][2].length`.

1. **`arr.length`:** Это внешний массив (трехмерный). Смотрим на его инициализацию (строки 1-5). Он содержит **3 элемента** (три блока, разделенные запятыми):
    - `arr[0]` = `{ {}, null }`  
        
    - `arr[1]` = `{ {"5", "8"}, {"2", null, "1"} }`  
        
    - `arr[2]` = `{ {}, {"4", null} }`  
        
    - Значит, `arr.length` равно **3**.
2. **`arr[1]`:** Это второй элемент внешнего массива. Он представляет собой двумерный массив:
    - `{ {"5", "8"}, {"2", null, "1"} }`. В нем **2 элемента**.  
        
    - `arr[1][0]` = `{"5", "8"}`  
        
    - `arr[1][1]` = `{"2", null, "1"}`
3. **`arr[1][2]`:** Мы пытаемся получить доступ к **третьему** элементу массива `arr[1]`.
    - Однако в `arr[1]` всего два элемента (с индексами `0` и `1`). Элемента с индексом `2` не существует.  
        
    - Попытка обратиться к `arr[1][2]` вызовет исключение **`ArrayIndexOutOfBoundsException`**.

Поскольку исключение возникает до того, как программа сможет вычислить сумму и вывести результат, выполнение программы прервется с ошибкой.

---
