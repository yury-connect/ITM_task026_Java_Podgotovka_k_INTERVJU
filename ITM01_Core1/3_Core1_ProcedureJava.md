
---
## Процедурная Java

<details>
        <summary>27. Какие примитивные типы данных есть в Java?</summary>

В Java есть 8 примитивных типов данных, они делятся на:

> 1. **Целочисленные**
> * **byte** (8 бит, от -128 до 127)
> 
> * **short** (16 бит, от -32 768 до 32 767)
> * **int** (32 бита, от -2^31 до 2^31-1)
> * **long** (64 бита, от -2^63 до 2^63-1)

> 2. **Вещественные** (числа с плавающей запятой)
> * **float** (32 бита, 7 знаков после запятой)
> * **double** (64 бита, 15 знаков после запятой)

> 3. **Символьный**
> * **char** (16 бит, хранит один символ в формате UTF-16)

> 4. **Логический**
> * **boolean** (принимает только true или false)

**Важно**:
* Примитивные типы данных не являются объектами и хранятся в стеке.
* String не является примитивным типом, это ссылочный _(объектный)_ тип.

```text
***** из методички *****
"Вещественные, целочисленные, логические и строковые.
byte
short
int
long
float
double
char
boolean"
```
</details>



<details>
        <summary>28. Что такое char?</summary>

`char` в **Java** — это **16-разрядный беззнаковый целочисленный тип данных**, 
который **представляет символ в кодировке UTF-16**.

🔹 **Основные характеристики char**:

Занимает 16 бит (2 байта) в памяти.
Может хранить символы Unicode (буквы, цифры, знаки).
Значения находятся в диапазоне от \u0000 (0) до \uffff (65 535).
Фактически char — это число, интерпретируемое как символ.

🔹 **Пример использования char**:

```java
char letter = 'A';  // Буква A
char number = '7';  // Цифра 7 (но это символ!)
char symbol = '$';  // Символ $
char unicodeChar = '\u2764'; // ❤️ (символ Unicode)
```

🔹 **Важно**:

* `char` — это не строка (`String`), а **одиночный символ**.
* Можно использовать **арифметические операции**, потому что `char` — это число:
```java
char c = 'A';
System.out.println(c + 1);  // Выведет 66 (B в таблице ASCII/Unicode)
```
```text
***** из методички *****
16-разрядное беззнаковое целое, представляющее собой символ UTF-16 (буквы и цифры)
```
</details>



<details>
        <summary>29. Сколько памяти занимает boolean?</summary>

`boolean` в Java не имеет строго фиксированного размера и его объем памяти зависит от реализации JVM:

* **В стандартной реализации (Sun/Oracle HotSpot JVM)**: переменная boolean хранится как int, занимая 4 байта (32 бита).
* **В массивах** `boolean[]`: JVM может оптимизировать хранение, выделяя **1 байт (8 бит) на элемент**, 
а в некоторых случаях — **1 бит на элемент**.

Размер `boolean` определяется внутренними оптимизациями JVM и не всегда очевиден разработчику.

![иллюстрация](/ITM01_Core1/imgs/2025-02-24_23-56-52.png) 

```text
***** из методички *****
"Зависит от реализации JVM
В стандартной реализации Sun JVM и Oracle HotSpot JVM тип boolean занимает 4 байта (32 бита), 
как и тип int. Однако, в определенных версия JVM имеются реализации, 
где в массиве boolean каждое значение занимает по 1-му биту.
"
```
</details>



<details>
        <summary>30. Что такое классы-обертки?</summary>

Классы-обертки (Wrapper classes) — это классы, которые позволяют работать с примитивными типами данных как с объектами.

> **Основные особенности**:
>
> * Хранят внутри себя значение примитивного типа.
> * Объекты классов-оберток **неизменяемы (Immutable)**.
> * Используются для работы с **дженериками**, коллекциями (`List`, `Set`, `Map`), рефлексией и многопоточностью.

> **Список классов-оберток в Java:**
>
> * **Byte** → для byte
> 
> * **Short** → для short
> 
> * **Integer** → для int
> 
> * **Long** → для long
> 
> * **Float** → для float
> 
> * **Double** → для double
> 
> * **Character** → для char
> 
> * **Boolean** → для boolean
> 

Объекты классов-оберток создаются через `valueOf()` или автоматически благодаря **Autoboxing/Unboxing**.

```text
***** из методички *****
"Обертка — это специальный класс, который хранит внутри себя значение примитива
(объекты классов-оберток являются неизменяемыми (Immutable)).
Нужны для реализации дженериков."
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

