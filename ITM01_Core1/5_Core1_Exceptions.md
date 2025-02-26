
---
## Исключения



<details>
        <summary>94. Что такое исключения?</summary>

Исключение — это объект, сигнализирующий о возникновении **ошибки** во время **выполнения** программы.

```text
***** из методички *****
Исключение — это ошибка (является объектом), возникающая во время выполнения программы. 
```
</details>



<details>
        <summary>95. Опишите иерархию исключений.</summary>

![иерархия](/ITM01_Core1/imgs/2025-02-25_23-20-40.png)
* **Checked** — требуют обработки в коде.
* **Unchecked** — возникают из-за ошибок в логике программы.

Примеры с классами: 
```java
Throwable (checked) — базовый класс всех исключений и ошибок.
├── Error (unchecked) — критические ошибки JVM.
│ ├── OutOfMemoryError
│ ├── StackOverflowError
│ └── InternalError
└── Exception (checked) — ошибки, зависящие от программы.
├── RuntimeException (unchecked) — ошибки в логике программы.
│ ├── NullPointerException
│ ├── IndexOutOfBoundsException
│ ├── ArithmeticException
│ └── ClassCastException
└── Checked Exceptions (checked) — требуют обработки.
├── IOException
├── SQLException
└── ReflectiveOperationException
```

```text
***** из методички *****
"1. класс Throwable (checked)

2. от Throwable  -> Error (ошибки JVM) и Exception (checked общие)

3. от Exception 
    - > RuntimeException (unchecked)
    - > IOException, SQLException, ReflectiveOperationException (checked)

4.RuntimeException (unchecked):
  ClassCastExceptiuon
  IndexOutOfBoundException
  AritthmeticException
  NullPointerException


checked - зависит от программиста, unchecked - от программиста не зависит"
```
</details>



<details>
        <summary>96. Можно ли обработать необрабатываемые исключения?</summary>

**Да**, можно. Ошибки _JVM_ (_Error_) обычно не обрабатываются, 
но можно использовать `try-catch`, чтобы перехватить некоторые из них 
и предотвратить падение программы, если это возможно. 
Однако в большинстве случаев такие ошибки критичны, и лучше исправлять их причины, 
а не перехватывать.

```text
***** из методички *****
Можно, чтобы в некотрых случаях программа не прекратила работу
```
</details>



<details>
        <summary>97. Какой оператор позволяет принудительно выбросить исключение?</summary>

Оператор `throw` позволяет **принудительно** выбросить исключение в Java. 
Используется для генерации как стандартных, так и пользовательских исключений.

```text
***** из методички *****
Throw
```
</details>



<details>
        <summary>98. О чем говорит ключевое слово throws?</summary>

`throws` указывает, какие исключения метод может выбросить. 
Перекладывает ответственность за их обработку на вызывающий код.

Для создания собственного исключения нужно унаследоваться от `Exception` 
(если требуется **проверяемое** исключение) или `RuntimeException` 
(если **непроверяемое**) и, при необходимости, переопределить конструкторы и методы.

```text
***** из методички *****
"Метод потенциально может выбросить исключение с указанным типом. 
Передаёт обработку исключения вышестоящему методу."
```
</details>



<details>
        <summary>99. Как создать собственное («пользовательское») исключение?</summary>

```text
***** из методички *****
"Необходимо унаследоваться от базового класса требуемого типа исключений 
(например, от Exception или RuntimeException).
и переопределит методы"
```
</details>



<details>
        <summary>100. _Расскажите про Try-catch-finally в java</summary>

**Расскажите про механизм обработки исключений в java (`Try-catch-finally`)**

Механизм обработки исключений в Java:

* `try` – блок, в котором может возникнуть исключение.
* `catch` – перехватывает и обрабатывает указанное исключение. 
Может быть несколько блоков catch для разных типов исключений.
* `finally` – выполняется всегда, независимо от того, было исключение или нет. 
Используется для освобождения ресурсов.

```text
***** из методички *****
"Try - блок в котором может появиться исключение;
Catch - блок в котором мы указываем исключение и логику его обработки;
Finally - блок который обязательно отработает"
```
</details>




<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>




<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>




<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>



<details>
        <summary>Head</summary>
</details>

