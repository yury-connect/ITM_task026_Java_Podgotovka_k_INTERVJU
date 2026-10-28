## 15/20: Given the following method declaration signature, which of the following is a valid call of this method?

```java
public void action(int var, String name, String... descriptions)
```

- action(1, "Jan", "Kastus", null)    
- action(1)    
- action(1, "Jan")    
- action(1, "Jan", 2, "Kastus")    
- action(1, "Jan", '2')    
- action(1, "Jan", 2)    

---
## 15/20: Учитывая следующую сигнатуру объявления метода, какой из вариантов является допустимым вызовом этого метода?

- action(1, "Jan", "Kastus", null)
- action(1)
- action(1, "Jan")
- action(1, "Jan", 2, "Kastus")
- action(1, "Jan", '2')
- action(1, "Jan", 2)

---
Правильные ответы (подходят два варианта):  
**1. `action(1, "Jan", "Kastus", null)`** (первый вариант)  
**2. `action(1, "Jan")`** (третий вариант)

**Объяснение:**

Сигнатура метода: `public void action(int var, String name, String... descriptions)`
	
- Первые два параметра обязательны: целое число (`int`) и строка (`String`).
    
- Третий параметр — это **varargs** (переменное число аргументов) типа `String`. Это означает, что вы можете передать ноль, один или несколько аргументов типа `String`. Под капотом это превращается в массив `String[]`.

Разберем варианты:

1. **`action(1, "Jan", "Kastus", null)` — ВЕРНО.**
	    
    - `1` — подходит под `int`.
        
    - `"Jan"` — подходит под `String name`.
        
    - `"Kastus"` и `null` — это два элемента для varargs `String...`. `null` совместим со `String`.
    
2. **`action(1)` — НЕВЕРНО.** Отсутствует обязательный второй параметр `String name`.
    
3. **`action(1, "Jan")` — ВЕРНО.** Переданы два обязательных аргумента. Для varargs `String... descriptions` не передано ни одного аргумента. Это допустимо (внутри метода массив `descriptions` будет пустым).
    
4. **`action(1, "Jan", 2, "Kastus")` — НЕВЕРНО.** В varargs передается число `2`, а ожидается только `String`.
    
5. **`action(1, "Jan", '2')` — НЕВЕРНО.** Передан символ (`char`), а ожидается `String`.
    
6. **`action(1, "Jan", 2)` — НЕВЕРНО.** Передано число `2`, а ожидается `String`.

---
