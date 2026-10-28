## 1/9: What will be the result of following code fragment?

```java
StringBuilder sb1 = new StringBuilder("abc");
StringBuilder sb2 = new StringBuilder("abc");
System.out.println(sb1.equals(sb2) + " ");
System.out.println(sb1 == sb2);
```

- true true    
- false false    
- true false    
- false true    

---
## 1/9: Каким будет результат следующего фрагмента кода?

- true true    
- false false    
- true false    
- false true

---
Правильный ответ: **false false** (второй вариант).

**Объяснение:**

1. **`sb1.equals(sb2)` (строка 3):**  
    Класс `StringBuilder` **не переопределяет** метод `equals()` из класса `Object`. В классе `Object` метод `equals()` сравнивает ссылки на объекты (работает так же, как оператор `==`). Поскольку `sb1` и `sb2` — это два разных объекта в памяти (созданные через `new`), результат сравнения — **`false`**.
2. **`sb1 == sb2` (строка 4):**  
    Оператор `==` сравнивает ссылки на объекты. Так как было создано два разных экземпляра (разные адреса в памяти), они не равны друг другу. Результат — **`false`**.

_(Примечание: Если бы вместо `StringBuilder` использовался класс `String`, то метод `equals()` был бы переопределен и сравнивал бы содержимое строк, дав результат `true` в первом случае)._

---
