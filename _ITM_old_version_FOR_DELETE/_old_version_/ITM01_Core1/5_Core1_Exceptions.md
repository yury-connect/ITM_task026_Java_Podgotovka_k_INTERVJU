[⏪ **PREVIOUS** < _Предыдущая страница_ ⏪](/_ITM_old_version_FOR_DELETE/ITM01_Core1/4_Core1_OOP_v_Java.md)

---
## Исключения



<details>
        <summary>94. Что такое исключения?</summary>

Исключение — это объект, сигнализирующий о возникновении **ошибки** во время **выполнения** программы.

```text
***** из методички *****
Исключение — это ошибка (является объектом), возникающая во время выполнения программы. 
```
---
</details>



<details>
        <summary>95. Опишите иерархию исключений.</summary>

![иерархия](/_ITM_old_version_FOR_DELETE/ITM01_Core1/imgs/2025-02-25_23-20-40.png)
* **Checked** — требуют обработки в коде.
* **Unchecked** — возникают из-за ошибок в логике программы.

Примеры с классами: 
```textmate
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
---
</details>



<details>
        <summary>96. _обрабатываемые и необрабатываемые исключения</summary>

**Расскажите про обрабатываемые и необрабатываемые исключения**

**Обрабатываемые** (`checked`) и **необрабатываемые** (`unchecked`) исключения:

1. **Checked** (_обрабатываемые_) — исключения, которые должны либо обрабатываться в `catch`, 
либо объявляться в `throws` в сигнатуре метода. Они наследуются от `Exception`, 
но **не** от `RuntimeException`. Проверяются компилятором на этапе компиляции.
2. **Unchecked** (_необрабатываемые_) — исключения, которые **не требуют** обязательной обработки. 
Наследуются от `RuntimeException`. Проверяются только во время выполнения.

Главное отличие: `checked` исключения **контролируются** компилятором, `unchecked` — **нет**.

```text
***** из методички *****
1. Checked исключения, это те, которые должны обрабатываться блоком catch 
или описываться в сигнатуре метода. Unchecked могут не обрабатываться и не быть описанными.

2. Unchecked исключения в Java — наследованные от RuntimeException, checked — от Exception.
Checked исключения отличаются от Unchecked исключения в Java, тем что
наличие\обработка Checked исключения проверяются компилятором на этапе компиляции. 

Наличие\обработка Unchecked исключения происходит на этапе выполнения.
```
---
</details>



<details>
        <summary>97. Можно ли обработать необрабатываемые исключения?</summary>

**Да**, можно. Ошибки _JVM_ (_Error_) обычно не обрабатываются, 
но можно использовать `try-catch`, чтобы перехватить некоторые из них 
и предотвратить падение программы, если это возможно. 
Однако в большинстве случаев такие ошибки критичны, и лучше исправлять их причины, 
а не перехватывать.

```text
***** из методички *****
Можно, чтобы в некотрых случаях программа не прекратила работу
```
---
</details>



<details>
        <summary>98. Какой оператор позволяет принудительно выбросить исключение?</summary>

Оператор `throw` позволяет **принудительно** выбросить исключение в Java. 
Используется для генерации как стандартных, так и пользовательских исключений.

```text
***** из методички *****
Throw
```
---
</details>



<details>
        <summary>99. О чем говорит ключевое слово throws?</summary>

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
---
</details>



<details>
        <summary>100. Как создать собственное («пользовательское») исключение?</summary>

```text
***** из методички *****
"Необходимо унаследоваться от базового класса требуемого типа исключений 
(например, от Exception или RuntimeException).
и переопределит методы"
```
---
</details>



<details>
        <summary>101. _Расскажите про Try-catch-finally в java</summary>

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
---
</details>



<details>
        <summary>102. Возможно ли использование блока try-finally (без catch)?</summary>

**Да**, `try` можно использовать с `finally` без `catch`. 

Блок `finally` выполнится **после** `try` в любом случае.

```text
***** из методички *****
try может быть в паре с finally, без catch. 
Работает это точно так же - после выхода из блока try выполняется блок finally
```
---
</details>



<details>
        <summary>103. _несколько исключений в 1-м catch</summary>

**Может ли один блок `catch` отлавливать сразу несколько исключений?**

**Да**, через `|` можно указать **несколько** типов исключений в одном `catch`.

```text
***** из методички *****
Да
```
---
</details>



<details>
        <summary>104. _Когда блок `finally` не будет выполнен?</summary>

**Всегда ли выполняется блок `finally`? Существуют ли ситуации, когда блок `finally` не будет выполнен?**

Блок `finally` выполняется **всегда**, кроме случаев:

1. Вызов `System.exit(0)`, `Runtime.getRuntime().exit(0)`, `Runtime.getRuntime().halt(0)`.
2. Аварийное завершение JVM (_например, из-за ошибки уровня `Error`_).
3. Бесконечный цикл или бесконечное ожидание в `try`, блокирующее выполнение.

```text
***** из методички *****
Да, кроме случаев завершения работы программы или JVM:

1 - Finally может не выполниться в случае если в блоке try вызывает System.exit(0), 
2 - Runtime.getRuntime().exit(0), Runtime.getRuntime().halt(0) 
 и если во время исполнения блока try виртуальная машина выполнила недопустимую операцию и будет закрыта. 
3 - В блоке try{} бесконечный цикл."
```
---
</details>



<details>
        <summary>105. _когда main() выбросит исключение во вне_</summary>

**Может ли метод `main()` выбросить исключение во _вне_ и если _да_, 
то где будет происходить обработка данного исключения?**

**Да**, `main()` может выбросить исключение. Оно передается _JVM_, которая:

1. Завершает главный поток приложения.
2. Вызывает `ThreadGroup.uncaughtException()`, если есть обработчик.

```text
***** из методички *****
Может и оно будет передано в виртуальную машину Java (JVM).
Для случая с методом main произойдет две вещи:

- будет завершен главный поток приложения;
- будет вызван ThreadGroup.uncaughtException.
```
---
</details>



<details>
        <summary>106. В каком порядке следует обрабатывать исключения в catch блоках?</summary>

Обрабатывать исключения нужно **от** более **специфичных** (_наследников_) 
**к** более **общим** (_предкам_), 
иначе компилятор выдаст **ошибку**.

```text
***** из методички *****
От наследника к предку
```
---
</details>



<details>
        <summary>107. Что такое механизм try-with-resources?</summary>

`Try-with-resources` **автоматически** закрывает ресурсы, объявленные в `try`, 
без явного `finally`. 

Работает с объектами, реализующими `AutoCloseable` или `Closeable`.

```text
***** из методички *****
Дает возможность объявлять один или несколько ресурсов в блоке try, 
которые будут закрыты автоматически без использования finally блока.

В качестве ресурса можно использовать любой объект, 
класс которого реализует интерфейс java.lang.AutoCloseable или java.io.Closeable.
```
---
</details>



<details>
        <summary>108. _исключение из блока `catch`, а затем из блока `finally`</summary>

**Что произойдет если исключение будет выброшено из блока `catch` **после** 
чего другое исключение будет выброшено из блока `finally`?**

1. Первоначальное исключение (_из `catch`_) **теряется**.
2. Исключение из `finally` "**перекрывает**" его и становится **основным**. 
(_Только оно будет видно в стеке._)

📌 Вывод: исключение из `finally` всегда **заменяет** исключение из `catch`, 
если **явно** не обработать его   
_(Вручную сохранить его как **подавленное** исключение (`Suppressed Exception`))_.

```text
***** из методички *****
finally - секция может «перебить» 
 throw/return при помощи другого throw/return
```
---
</details>



<details>
        <summary>109. _исключение из блока `catch`, а затем из метода close()</summary>

**Что произойдет если исключение будет выброшено из блока `catch` 
после чего другое исключение будет выброшено из метода `close()` при использовании `try-with-resources`?**

В `try-with-resources` метод `close()` вызывается **автоматически** 
при выходе из блока `try` и может выбросить **исключение**.  
📌 При этом:

1. **Основным** исключением будет исключение из `catch`.
2. Исключение из `close()` **не теряется**, но становится "**подавленным**" (_Suppressed Exception_).
3. Метод `Throwable.getSuppressed()` позволяет **получить** подавленные исключения.

```text
***** из методички *****
В try-with-resources добавленна возможность 
 хранения "подавленных" исключений, 
 и брошенное try-блоком исключение имеет больший приоритет, 
 чем исключения получившиеся во время закрытия.
```
---
</details>

[>>> **NEXT** > _Следующая страница_ >>>](/_ITM_old_version_FOR_DELETE/ITM01_Core1/6_Core1_Serilialization_and_Copy.md)








---

<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>
