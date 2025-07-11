
---


---

<details>
        <summary>Java 11: Какие типы файлов можно передавать в JVM? </summary>

**Java Virtual Machine** (JVM) обрабатывает следующие типы файлов:

1. `.class`: Скомпилированные байт-код файлы, которые JVM непосредственно исполняет.

2. `.jar`: Архивы, содержащие `.class` файлы и ресурсы, 
используемые для упаковки и распространения Java-приложений.

3. `.java`: Начиная с `Java 11`, JVM может напрямую выполнять `.java` файлы 
без предварительной компиляции с помощью javac.

Пример:
```cmd
java HelloWorld.java
```
---
</details>



<details>
        <summary>Интерфейсы: сколько раз можно наследоваться от интерфейса</summary>

**Формально** в `Java` можно **унаследоваться** от интерфейсов до **~65 535 раз**.   

Этот лимит обусловлен ограничением формата Java Class File, где максимальное количество интерфейсов, которые может реализовать класс, равно 65 535 (2^16 - 1).

**Однако, на практике...**   
❌ Это **никогда не используется**, так как:

1. JVM и компилятор начнут тормозить (слишком много интерфейсов).
2. Это невозможно поддерживать — такой код будет нечитаемым и неуправляемым.
3. Обычно используется 2-5 интерфейсов, редко больше.

💡 **Вывод**: Теоретически можно унаследоваться от до **65 535 интерфейсов**,   
но **на практике** используется **небольшое** количество. 🚀

```text
```
</details>



<details>
        <summary>Java - компиляторы: бывают</summary>

## Компиляторы Java
1️⃣ `javac` – стандартный компилятор JDK (_с 1996 года_), преобразует исходный код Java в байт-код для JVM.

2️⃣ **JIT-компилятор** (`Just-In-Time`) – встроен в JVM, динамически преобразует байт-код 
в машинный код во время выполнения, улучшая производительность.

3️⃣ **GraalVM** – продвинутая JVM (_с 2019 года_), поддерживает JIT- и AOT-компиляцию, 
позволяет создавать нативные образы Java-приложений (_быстрый запуск, низкое потребление ресурсов_).

### 🔹 Дополнительные компиляторы:

* **ECJ**  (`Eclipse Compiler for Java`) – компилятор Eclipse (с 2001 года), поддерживает инкрементальную компиляцию.   
* **GCJ** (`GNU Compiler for Java`) – компилятор из GCC, позволял транслировать Java в машинный код, но устарел.   
* `Jikes` – компилятор IBM (_1998–2000-е_), отличался высокой скоростью, но больше не развивается.   

🔹 **AOT** (`Ahead-of-Time`) – предварительная компиляция **байт-кода в машинный** код, 
ускоряет запуск и снижает нагрузку на _JVM_. Появилась в Java 9.

### Вывод:   
🔹 `javac` – основной компилятор.   
🔹 `JIT` – ускоряет выполнение в JVM.   
🔹 `GraalVM` – гибрид **JIT** + **AOT**.   
🔹 **AOT** снижает время старта, но требует предварительной компиляции.   
---
</details>



<details>
        <summary>Кодировка чисел в Java. Обратный код</summary>

В Java используются стандартные системы кодирования чисел:

1. **Прямой код** (_Sign-Magnitude_) – старший бит указывает знак (_0 – положительное, 1 – отрицательное_),
остальные биты представляют модуль числа. Не используется в Java.   

2. **Дополнительный код** (_Two's Complement_) – стандарт для представления целых чисел (byte, short, int, long). 
Отрицательные числа хранятся в виде дополнения до двух, что упрощает арифметические операции.   
```java 
int x = -5; // 1111...1011 (32 бита) 
```
3. **Обратный код** (_One’s Complement_) – отрицательные числа представляются инвертированием 
всех битов положительного числа. В Java не используется, но встречается в старых системах.   

4. **Плавающая точка** (_IEEE 754_) – используется для float и double. 
Число делится на знак, мантиссу и порядок (экспоненту).
```java
float f = 3.14f; // 0 10000000 10010001111010111000011 (32 бита)
```

Java использует дополнительный код для представления целых чисел, а для вещественных – стандарт IEEE 754.

### Обратный код _(One’s Complement)_ и прямой код _(Sign-Magnitude)_ в Java не используются.

---
</details>



<details>
        <summary>Ковариантность, контравариантность, инвариантность
</summary>

## Ковариантность, контравариантность, инвариантность

* 🔹 **Ковариантность** (`? extends T`) – позволяет использовать **производные (_подтипы_)** 
вместо базового типа.   
✅ Читаем (get) из коллекции, но не записываем (put).


* 🔹 **Контравариантность** (`? super T`) – позволяет использовать **базовые (_супертипы_)** 
вместо производного типа.   
✅ Записываем (put), но чтение (get) даёт Object.


* 🔹 **Инвариантность** – строгая типизация, разные обобщённые типы не совместимы (`List<String>` ≠ `List<Object>`).

**Вывод**:   
🔹 `? extends` – **только читаем**, не записываем.   
🔹 `? super` – **только записываем**, читаем как `Object`.   
🔹 Без `?` – строгая совместимость типов.   

</details>



<details>
        <summary>ключи в TreeMap или TreeSet</summary>

### Каким требованиям должен удовлетворять класс, чтобы его использовать в качестве ключей в TreeMap или TreeSet?

## Требования к классу для использования в TreeMap / TreeSet
* 🔹 1️⃣ Реализация `Comparable<T>` или передача `Comparator<T>` в конструктор   
✅ **Объекты должны быть сравнимыми**, иначе будет `ClassCastException`.   
✅ `compareTo()` (из `Comparable`) или `compare()` (из `Comparator`) должны быть:
> * **рефлексивными**, 
> * **транзитивными** и 
> * **согласованными** с `equals()`.


* 🔹 2️⃣ `equals()` и `hashCode()` должны быть согласованы с `compareTo()`   
✅ Если `compareTo()` говорит, что объекты равны (`a.compareTo(b) == 0`), `equals()` должен возвращать `true`.   
❌ Иначе возможны несоответствия, например, дубликаты в `TreeSet`.   


* 🔹 3️⃣ Класс должен быть неизменяемым (_желательно_) или корректно изменяемым   
✅ Если объект изменится после помещения в `TreeMap`/`TreeSet`, это может сломать структуру данных.   


**Вывод**:   
✅ **Реализовать** `Comparable<T>` или **передавать** `Comparator<T>`   
✅ Гарантировать **согласованность** `compareTo()` и `equals()`   
✅ **Не изменять** объект после помещения в `TreeMap` / `TreeSet`   

---
</details>



<details>
        <summary>Виртуальные потоки (Virtual Threads) в Java 21</summary>

# Виртуальные потоки (Virtual Threads) в Java 21

🔹 **Лёгковесные потоки**, управляемые JVM, а не ОС.  
🔹 **Сотни тысяч потоков** на одно приложение без лишних затрат.  
🔹 Основаны на **ForkJoinPool** и работают по принципу "мэппинга на реальные потоки (carrier threads)".  
🔹 Отлично подходят для задач с высокой блокировкой (I/O, БД).

## Ключевые особенности:
- ✅ Создание через `Thread.ofVirtual().start()` или `Executors.newVirtualThreadPerTaskExecutor()`.
- ✅ Не требуют сложных пулов потоков – JVM сама управляет планированием.
- ✅ Поддерживают привычные API (`synchronized`, `Lock`, `ThreadLocal`), но не все механизмы работают эффективно.

## Когда использовать?
🔹 Если приложение порождает много краткоживущих потоков, например:
- Обработка HTTP-запросов
- Работа с БД
- Чат-серверы

🔹 Не подходят для вычислительно интенсивных задач – тут лучше обычные **Platform Threads**.

👉 **Вывод**: виртуальные потоки – мощный инструмент для асинхронных задач без сложного управления потоками.

</details>


[⏭️ **NEXT** > _СЛЕДУЮЩИЙ РАЗДЕЛ_ ⏭️](/_ITM_old_version_FOR_DELETE/ITM02_Core2/1_Core2.md)



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>


---
## Блок 2

<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



<details>
        <summary>Head</summary>

```text
```
</details>



