## 4/9: What will be the output of the following program?

```java
StringBuilder builder = new StringBuilder();
builder.append("Class StringBuilder");
builder.delete(10, 16);
builder.insert(10, " ");
System.out.print(builder);
```

- The program will output an empty string    
- Class Stri der    
- Class Str Ider    
- Class StringBuilder    

---
## 4/9: Каким будет вывод следующей программы?

- Программа выведет пустую строку    
- Class Stri der    
- Class Str Ider    
- Class StringBuilder

---
Правильный ответ: **Class Str lder** (третий вариант).

**Пошаговое объяснение:**

1. **Строка 1-2:** Создается объект `StringBuilder`, в который добавляется строка `"Class StringBuilder"`.
	    
    - Длина строки — 19 символов. Индексы начинаются с 0:
        
    - `C(0) l(1) a(2) s(3) s(4) (5) S(6) t(7) r(8) i(9) n(10) g(11) B(12) u(13) i(14) l(15) d(16) e(17) r(18)`
    
2. **Строка 3:** `builder.delete(10, 16);`
	    
    - Метод `delete(start, end)` удаляет символы с индекса `start` (включительно) до индекса `end` (не включая его).
        
    - Удаляются символы с 10 по 15 индексы: `n(10) g(11) B(12) u(13) i(14) l(15)` — это подстрока `"ngBuil"`.
        
    - Оставшаяся строка: `"Class Str` + `der"` = **`"Class Str der"`** (заметьте пробел после `Str`).
        
    - _(Текущее состояние: `C(0) l(1) a(2) s(3) s(4) (5) S(6) t(7) r(8) (9) d(10) e(11) r(12)`)_
    
3. **Строка 4:** `builder.insert(10, " ");`
	    
    - Метод `insert(index, str)` вставляет строку `str` на позицию `index`, сдвигая остальные символы вправо.
        
    - Вставляем пробел на индекс 10 (перед буквой `d`).
        
    - Итоговая строка: **`"Class Str der"`** (теперь между `r` и `d` два пробела: один был изначально на индексе 9, второй вставлен на индекс 10).
        
    - _(Итог: `C(0) l(1) a(2) s(3) s(4) (5) S(6) t(7) r(8) (9) (10) d(11) e(12) r(13)`)_
    
4. **Строка 5:** Выводит получившуюся строку: **`Class Str der`**.

---
