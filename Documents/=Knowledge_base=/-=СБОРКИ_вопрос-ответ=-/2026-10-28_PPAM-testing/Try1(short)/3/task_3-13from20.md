## 13/20: What is the result of compiling and running the code below?

```java
class Count<T> {
    private T counter;
    public Count(T counter) {
        this.counter = counter;
    }
    public int toInt() {
        return counter.intValue(); //line1
    }
    public static void main(String[] args) {
        System.out.print(new Count(3.01)); //line2
        System.out.print(new Count(7.9)); //line3
    }
}
```

- 37    
- 38    
- Compilation fails at line1    
- Compilation fails at line2    
- Compilation fails at line3    

---
## 13/20: Каков результат компиляции и выполнения приведенного ниже кода?

- 37    
- 38    
- Ошибка компиляции в строке 1 (line1)    
- Ошибка компиляции в строке 2 (line2)    
- Ошибка компиляции в строке 3 (line3)

---
Правильный ответ: **Compilation fails at line1** (Компиляция завершится с ошибкой в строке 1, отмеченной как `//line1`).

**Объяснение:**

Ошибка возникает в **строке 7**: `return counter.intValue();`

Вот почему это происходит:

1. Класс `Count` является обобщенным (generic): `class Count<T>`. Это означает, что тип переменной `counter` будет известен только во время выполнения (при создании экземпляра).
2. Переменная `counter` имеет тип `T`. По умолчанию, если не указана верхняя граница (например, `T extends Number`), компилятор воспринимает `T` просто как `Object`.
3. У класса `Object` нет метода `intValue()`. Этот метод принадлежит классу `Number` (и его наследникам: `Integer`, `Double` и т.д.).
4. Поскольку компилятор не знает, что в качестве `T` будет передан именно числовой тип, он запрещает вызов метода `intValue()`. Возникает ошибка: `cannot find symbol method intValue()`.

**Как исправить?**  
Нужно ограничить тип `T` классом `Number`, чтобы компилятор знал, что у объекта будут числовые методы:

```java
class Count<T extends Number> {
```

_(Примечание: строки 10 и 11, отмеченные как `//line2` и `//line3`, синтаксически верны, так как происходит autoboxing примитивов `3.01` и `7.9` в объекты `Double`. Если бы класс компилировался, методы вывели бы `3` и `7` (из-за усечения при `intValue()`). Но из-за ошибки в строке 7 код не скомпилируется)_.

---
