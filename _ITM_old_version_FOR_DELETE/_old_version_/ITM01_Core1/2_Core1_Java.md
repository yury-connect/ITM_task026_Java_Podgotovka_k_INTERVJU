[⏪ **PREVIOUS** < _Предыдущая страница_ ⏪](/_ITM_old_version_FOR_DELETE/ITM01_Core1/1_Core1_OOP.md)

---
## Java


<details>
        <summary>13. Какая основная идея языка?</summary>

Основная идея Java – **"Написано однажды – работает везде"**. Это означает, что код, написанный на Java, 
может выполняться **на любой платформе без изменений**, благодаря использованию виртуальной машины _(JVM)_.

```text
***** из методички *****
"«Написано однажды - работает везде».
Идея основывается в написании одного кода, который будет работать на любой платформе."
```
---
</details>



<details>
        <summary>14. За счет чего обеспечивается кроссплатформенность?</summary>

**Кроссплатформенность** Java обеспечивается благодаря `Java Virtual Machine` (_JVM_).

JVM является промежуточным слоем между операционной системой и программой на Java, 
что позволяет выполнять байт-код Java **на любой платформе**, где установлена _JVM_.  
Это устраняет необходимость перекомпиляции кода для разных операционных систем.

```text
***** из методички *****
"Кроссплатформенность была достигнута за счёт создания виртуальной машина Java. 

Java Virtual Machine или JVM - это программа, являющаяся прослойкой между операционной системой 
и Java программой. В среде виртуальной машины выполняются коды Java программ. 
Сама JVM реализована для разных ОС.
Что байт код для JVM может исполняться везде где установлена JVM.
Код не нужно перекомпилировать под каждую из платформ."
```
---
</details>



<details>
        <summary>15. Какие преимущества у java?</summary>

**Преимущества Java**:

> * **Объектно-ориентированное программирование** – объекты управляют данными и их взаимодействием, 
упрощая структуру программы.
> 
> * **Простой синтаксис** – Java легче для изучения, чем C++, что ускоряет процесс обучения.
> 
> * **Стандарт для корпоративных систем** – Java зарекомендовала себя как надежный инструмент 
для разработки корпоративных приложений.
> 
> * **Безопасность** – отсутствие указателей и наличие `Security Manager` для контроля доступа.
> 
> * **Кроссплатформенность** – код компилируется в `байт-код`, который работает на любой платформе 
с установленной _JVM_.
> 
> * **Распределенное программирование** – поддержка _RMI_ и других методов для работы 
    с распределенными вычислениями.
> * **Автоматическое управление памятью** – сборщик мусора управляет памятью 
без вмешательства разработчика.
> 
> * **Многопоточность** – возможность эффективного использования процессора 
через параллельное выполнение потоков.
> 
> * **Стабильность и сообщество** – сильная поддержка и активное сообщество разработчиков.
> 

```text
***** из методички *****
"Объектно-ориентированное программирование   
- структура данных становится объектом, которым можно управлять для создания отношений 
между различными объектами.

Язык высокого уровня с простым синтаксисом и плавной кривой обучения 
- синтаксис Java основан на C ++, поэтому Java похожа на C. Тем не менее, синтаксис Java проще, 
что позволяет новичкам быстрее учиться и эффективнее использовать код для достижения конкретных результатов.

Стандарт для корпоративных вычислительных систем
- корпоративные приложения — главное преимущество Java с 90-х годов, когда организации начали 
искать надежные инструменты программирования не на C.

Безопасность 
- благодарю отсутсвию указателей и Security Manager (политика безопасности, 
в которой можно указать правила доступа, позволяет запускать приложения Java в ""песочнице"").

Независимость от платформы 
- Можно создать Java-приложение на Windows, скомпилировать его в байт-код и запустить его 
на любой другой платформе, поддерживающей виртуальную машину Java (JVM). 
Таким образом, JVM служит уровнем абстракции между кодом и оборудованием.

Язык для распределенного программирования и комфортной удаленной совместной работы
- Специфическая для Java методология распределенных вычислений называется Remote Method Invocation (RMI). 
RMI позволяет использовать все преимущества Java: безопасность, независимость от платформы 
и объектно-ориентированное программирование для распределенных вычислений. 
Кроме того, Java также поддерживает программирование сокетов и методологию распределения CORBA 
для обмена объектами между программами, написанными на разных языках.

Автоматическое управление памятью
Разработчикам Java не нужно вручную писать код для управления памятью благодаря 
автоматическому управлению памятью (AMM).

Многопоточность
Поток — наименьшая единица обработки в программировании. Чтобы максимально эффективно использовать 
время процессора, Java позволяет запускать потоки одновременно, что называется многопоточностью.

Стабильность и сообщество
Сообщество разработчиков Java не имеет себе равных. Около 45% респондентов опроса StackOverflow 2018 
используют Java."
```
---
</details>



<details>
        <summary>16. Какие недостатки у java?</summary>

**Недостатки Java**:

>
> **Платное коммерческое использование** (с 2019 года) – лицензирование требует затрат 
для некоторых видов использования.
>
> **Низкая производительность** – из-за использования JVM и сборщика мусора, 
что может замедлить выполнение приложений.
>
> **Неудобные инструменты для GUI** – создание графических интерфейсов на чистой Java ограничено.
>
> **Многословность кода** – язык требует большего количества кода для выполнения операций 
по сравнению с более компактными языками.
> 

```text
***** из методички *****
"Платное коммерческое использование (с 2019)

Низкая производительность
из-за компиляции и абстракции с помощью виртуальной машины, а также приложение очистки памяти.

Не развитые инструменты по созданию GUI приложений на чистой java.

Многословный код
Java — это более легкая версия неприступного C ++, которая вынуждает программистов прописывать 
свои действия словами из английского языка. Это делает язык более понятным для неспециалистов, 
но менее компактным."
```
---
</details>



<details>
        <summary>17. Что такое JDK? Что в него входит?</summary>

**JDK (Java Development Kit)** – это набор инструментов для разработки приложений на языке Java. 
Включает в себя:

>
> **JRE** (Java Runtime Environment) – среда выполнения Java.
>
> **Компилятор** (javac) – для компиляции исходного кода Java.
>
> **Стандартные библиотеки классов** – набор готовых классов для работы с основными функциями.
>
> **Примеры и документация** – образцы кода и справочные материалы для разработчиков.
>
> **Утилиты** – различные инструменты для разработки и отладки приложений.

```text
***** из методички *****
"JDK (Java Development Kit) - включает JRE и набор инструментов разработчика приложений на языке Java:
- компилятор Java (javac)
- стандартные библиотеки классов java
- примеры
- документацию
- различные утилиты"
```
---
</details>



<details>
        <summary>18. Что такое JRE? Что в него входит?</summary>

**JRE (Java Runtime Environment)** – это минимальная среда для запуска Java-приложений. Включает:

>
> **JVM** (Java Virtual Machine) – виртуальная машина для выполнения байт-кода Java.
>
> **ClassLoader** – компонент для загрузки классов в память.
>
> **Стандартные библиотеки и классы Java** – набор готовых классов для выполнения различных задач.
> 

```text
***** из методички *****
JRE (java Runtime Environment) - минимально-необходимая реализация виртуальной машины для исполнения 
Java-приложений. Состоит из JVM, ClassLoader и стандартного набора библиотек и классов Java
```
---
</details>



<details>
        <summary>19. Что такое JVM?</summary>

**JVM (Java Virtual Machine)** – виртуальная машина, которая выполняет байт-код Java, 
преобразованный JIT-компилятором.

Основные функции:

> * Интерпретация и выполнение байт-кода.
> 
> * Управление памятью _(включая сборку мусора)_.
> 
> * Обеспечение кроссплатформенности Java.

Языки `Scala`, `Kotlin`, `Groovy` так-же юзают **_JVM_**

Одна из популярных _реализаций_ JVM – **HotSpot**.

```text
***** из методички *****
JVM (Java Virtual Machine) - виртуальная машина Java исполняет байт-код Java, предварительно созданный 
из кода JIT компилятором, с помощью встроенного интерпретатора байт-кода.
HotSpot представляет собой реализацию концепции JVM.
```
---
</details>



<details>
        <summary>20. Что такое byte code?</summary>

**Байт-код Java** – это промежуточный набор инструкций, скомпилированный из исходного кода Java, 
который выполняется **JVM**. 

Он делает Java кроссплатформенной, так как может исполняться 
на любой системе с установленной виртуальной машиной.

```text
***** из методички *****
Байт-код Java — набор инструкций, скомпилированный компилятором, исполняемый JVM. 
```
---
</details>



<details>
        <summary>21. Что такое загрузчик классов (classloader)?</summary>

**ClassLoader** _(загрузчик классов)_ – компонент **JVM**, 
загружающий скомпилированный **байт-код** Java-классов в память.

**Основные загрузчики:**

> * **Bootstrap ClassLoader** – загружает базовые классы JDK.
> 
> * **AppClassLoader** – загружает классы приложения из **CLASSPATH**.
> 
> * **Extension ClassLoader** _(до Java 9)_ – загружал классы расширений.
> 

**Этапы работы:**

> 1. **Загрузка** – поиск и импорт байт-кода.
> 2. **Связывание**:
> > * **Проверка** – проверка корректности кода.
> > * **Подготовка** – выделение памяти и инициализация значениями по умолчанию.
> > * **Разрешение** – преобразование символических ссылок в реальные.
> 3. **Инициализация** – выполнение кода для установки окончательных значений переменных.

ClassLoader использует **иерархическую модель** – каждый загрузчик передает загрузку родительскому, 
если не может обработать её сам.

---
Более подробно см. [_вопрос 56:_ "_классы-загрузчики и _динамическая загрузка классов."](4_Core1_OOP_v_Java.md#вопрос-по-архитектуре-jvm-загрузчики)

[![Блок-схема: три встроенных загрузчика классов](/_ITM_old_version_FOR_DELETE/ITM01_Core1/imgs/2025-03-05_10-43-42.png)](https://nuancesprog.ru/p/15245/)    
[**Ссылка** на источник](https://nuancesprog.ru/p/15245/)   //   [**Скрин** всей страницы источника](/_ITM_old_version_FOR_DELETE/ITM01_Core1/files/Архитектура%20виртуальной%20машины%20Java_.html)   
[![Описание: три встроенных загрузчика классов](/_ITM_old_version_FOR_DELETE/ITM01_Core1/imgs/2025-03-05_11-24-03.png)](https://nuancesprog.ru/p/15245/)   
---

```text
***** из методички *****
Используется для передачи в JVM скомпилированного байт-кода, хранится в файлах с расширением .class

При запуске JVM, используются три загрузчика классов:
- Bootstrap ClassLoader - базовый загрузчик
- загружает платформенные классы JDK из архива rt.jar

- AppClassLoader - системный загрузчик
- загружает классы приложения, определенные в CLASSPATH 

- Extension ClassLoader - загрузчик расширений после В Java9 выпилили
- загружает классы расширений, которые по умолчанию находятся в каталоге jre/lib/ext.

ClassLoader выполняет три основных действия в строгом порядке:
•        Загрузка: находит и импортирует двоичные данные для типа.
•        Связывание: выполняет проверку, подготовку и (необязательно) разрешение.
 -        Проверка: обеспечивает правильность импортируемого типа.
 -        Подготовка: выделяет память для переменных класса и инициализация памяти значениями по умолчанию.
 -        Разрешение: преобразует символические ссылки из типа в прямые ссылки.
•        Инициализация: вызывает код Java, который инициализирует переменные класса 
их правильными начальными значениями.

Каждый загрузчик хранит указатель на родительский, чтобы суметь передать загрузку 
если сам будет не в состоянии этого сделать.
```
---
</details>



<details>
        <summary>22. Что такое JIT ?</summary>

**JIT** _(Just-In-Time Compiler)_ – компилятор, который **во время выполнения** программы 
переводит байт-код Java в **машинный код**, ускоряя выполнение.

**Основные функции JIT:**

> * **Повышает производительность** за счёт компиляции часто используемых частей кода.
> 
> * **Оптимизирует код** в реальном времени (инлайн-функции, устранение избыточных вычислений и др.).
> 
> * **Сочетает интерпретацию и компиляцию** для баланса между скоростью и эффективностью.
> 

JIT – ключевой механизм, делающий Java-приложения быстрее без потери кроссплатформенности.

```text
***** из методички *****
JIT (Just-in-time compilation) - компиляция на лету или динамическая компиляция - технология 
увеличения производительности программных систем, использующих байт-код, путем компиляции байт-кода 
в машинный код во время работы программы.

В основном отвечает за оптимизацию производительности приложений во время выполнения.
https://javahelp.online/osnovy/voprosy-otvety-sobesedovanie-java (Q13)
```
---
</details>



<details>
        <summary>23. Что такое сборщик мусора? (Garbage collector)</summary>

## 🔹 Что такое сборщик мусора (Garbage Collector, GC) в Java?

**Сборщик мусора** – это механизм управления памятью, 
который **автоматически** освобождает память от **ненужных объектов**.

### ✅ Как работает GC?
- **Определяет неиспользуемые объекты** (_те, на которые нет ссылок_).
- **Удаляет их из памяти**, освобождая место.

---

## 🔹 Как GC определяет "мусор"?

### 📌 Два подхода:

1️⃣ **Учет ссылок (_Reference Counting_)**
- У каждого объекта есть счетчик ссылок.
- Когда на объект **нет ссылок**, его можно **удалить**.
- ❌ **Минус**: не решает проблему **циклических ссылок** (_утечка памяти_).

2️⃣ **Трассировка (_Tracing GC, используется в HotSpot JVM_)**
- **GC** ищет "**живые**" объекты, начиная от **GC Roots**.
- Всё, до чего **невозможно добраться**, считается **мусором**.

**GC Roots** – это корневые объекты, от которых начинается поиск:   
✔ Объекты в **статических полях**.   
✔ Объекты в **стеках потоков**.   
✔ Объекты из **JNI (_native-кода_)**.   

---

## 🔹 Основные виды сборки мусора

### 🔹 **Minor GC (_малый_)** – чистит только молодое поколение (`Young Gen`).
- ✅ Работает часто, но быстро.
- ✅ Перемещает **живые** объекты в `Survivor` или `Old Gen`.

### 🔹 **Major GC (_старый_)** – чистит **старое поколение** (`Old Gen`).
- ⏳ Работает реже, но дольше.
- ✔ Компактизация памяти (уплотнение объектов).

### 🔹 **Full GC (_полный_)** – чистит **всю кучу** (`Young` + `Old`).
- ⏳ Самая **тяжелая** операция, останавливает приложение (`Stop-the-World`).
- ✔ Вызывается при нехватке памяти.

---

## 🔹 Структура памяти (Heap Generations)

1️⃣ **Young Generation (_Молодое поколение_)** – содержит **новые** объекты.   
- `Eden Space` – все новые объекты создаются здесь.
- `Survivor Spaces (S0, S1)` – выжившие объекты из `Eden`.

2️⃣ **Old Generation (_Старое поколение_)** – долгоживущие объекты.

3️⃣ **Metaspace (_с Java 8_)** – хранит **метаданные классов**.   
(В Java 6-7 использовался **PermGen**, но он удален в `Java 8`).

---

## 🔹 Виды сборщиков мусора в Java

| GC                           | Особенности                                            | Подходит для                           |
|------------------------------|-------------------------------------------------------|----------------------------------------|
| **Serial GC**                 | Однопоточный, Stop-the-World                          | Маленькие приложения                   |
| **Parallel GC**               | Использует несколько потоков                          | Большие приложения без строгих ограничений по задержкам |
| **CMS (Concurrent Mark-Sweep)**| Работает параллельно с программой, снижая задержки    | Приложения с низкими задержками        |
| **G1 GC (Garbage-First)**     | Оптимизирован для больших серверных приложений, заменяет CMS | Высоконагруженные системы             |

### Эволюция GC:
- **Serial** _(Java 1.3)_ → **Parallel** _(Java 1.4)_ → **CMS** _(Java 1.4.1)_ → **G1** _(Java 7)_

---

### ✅ Итоги

- ✔ **GC автоматически управляет памятью**, удаляя неиспользуемые объекты.
- ✔ Основные виды GC: **Minor** (молодые объекты), **Major** (старые), **Full** (вся куча).
- ✔ Современный GC – **G1**, он балансирует производительность и задержки.
- ✔ В **Java 8+** **Metaspace** заменил **PermGen**, улучшив работу с классами. 🚀

---

---

---

## Предыдущий вариант ответа на данный вопрос

**Сборщик мусора** _(Garbage Collector, **GC**)_ – автоматический механизм 
управления памятью в Java, который:

1. **Находит неиспользуемые объекты** (мусор).
2. **Очищает память** от этих объектов.

> 🔹 **Методы обнаружения мусора:**
>
> * **Учет ссылок** _(**Reference Counting**)_ – объект удаляется, 
> если на него нет ссылок (_**не решает** проблему циклических ссылок -> **утечка памяти**_).  
> / Суть подхода состоит в том, что каждый объект имеет некоторый счетчик. 
> Этот счетчик хранит информацию о том, сколько ссылок указывает на объект. 
> Kогда какая-либо ссылка уничтожается, то и значение счетчика уменьшается.
> Если значение счетчика равно нулю - объект можно считать мусором 
> и память, которую он занимает, можно очищать.
> 
> 
> * **Трассировка** _(**Tracing**, используется в HotSpot JVM 6)_ – ищет _**Живые**_ объекты, 
> до которых возможно добраться от корня **GC Roots** (_всё остальное считается **мусором**_).
> > **Типы корневых точек** (_GC Roots_):   
> > * объекты в **статических полях** классов;   
> > * объекты, доступные из **стека потоков**;   
> > * объекты из **JNI** _(java native interface)_ ссылок в `native` методах; 

> 🔹 **Основные типы сборки мусора:**
>
> * ✅**Minor GC** _(малый)_ – чистит **только молодое** поколение (`Eden` → `Survivor` → `Old Gen`).
> > * приложение приостанавливается на начало сборки мусора (_такие остановки называются **stop-the-world**_);
> > * «_**живые**_» объекты из `Eden` перемещаются в область памяти `To`;
> > * «**_живые_**» объекты из `From` перемещаются в `To` или в `old generation`, если они достаточно «**_старые_**»;
> > * `Eden` и `From` очищаются от мусора;
> > * `To` и `From` меняются местами;
> > * приложение возобновляет работу.
> 
> * ✅**Major GC** _(старый)_ – чистит старое поколение (`Old Gen`) с уплотнением памяти.
> > **Редкий** и более **длительный**, затрагивает объекты **старшего** поколения. 
> > 
> > * В принцип работы **major GC** добавляется процедура «**уплотнения**», 
> > позволяющая более **эффективно** использовать память.   
> > * В процедуре живые объекты перемещаются в начало.   
> > Таким образом, мусор остается в конце памяти.   
> 
> * ✅**Full GC** _(полный)_ – очищает всю кучу (Young + Old Gen), **самая затратная** операция.
> > * Зпускает **Minor**, а затем **Major** (_хотя порядок может быть изменен, если старое поколение заполнено,_ 
> > _и в этом случае он освобождается первым, чтобы позволить ему получать объекты от **молодого** поколения_).
>

> 🔹 Разделение памяти на **поколения** (_Heap Generations_):
> 1. **Young Generation** (_Молодое поколение_) — хранит объекты с коротким жизненным циклом.   
> Разделено на **три** области:   
> > * **Eden Space** (_Эдем_) — здесь создаются **новые** объекты.   
> > * **Survivor Spaces** (**_S0, S1_**) — хранят объекты, **пережившие** сборку мусора из **Eden**.   
> 
> 
> 2. **Old Generation** (_Старое поколение_) — содержит **долгоживущие** объекты, 
> прошедшие **несколько** сборок мусора в **Young Generation**.  
> 
> 
> 3. **Permanent Generation** (_PermGen_) / **Metaspace** (_в `Java 8+`_) — область для 
> метаданных классов, статических полей, информации о классах и загрузчиках.

> 🔹 Основные **GC** ([статья](https://javarush.com/quests/lectures/questservlets.level18.lecture05))
> * **Serial GC** – базовый сборщик для **небольших** приложений, не требовательных к задержкам.   
> Редко используется.   
> Может использоваться по умолчанию на **слабых** компьютерах.
> * **Parallel GC** – улучшенная версия **Serial GC** с поддержкой **многопоточности** 
> и **автоматической подстройки параметров** для повышения производительности.
> * **Concurrent Mark Sweep** (**_CMS_**) – снижает задержки 
> за счет **частичной параллельной** работы с основными потоками приложения.   
> Подходит для работы с относительно большими объемами данных.
> * **Garbage-First** (**_G1_**) – создан для замены **CMS**.   
> Эффективен в многопоточных **серверных** приложениях, обрабатывающих большие объемы данных.
 
 `Serial`_Java1.3(2000)_ -> `Parallel`_Java1.4(2002)_ -> `CMS`_Java1.4.1(2002)_ -> `G1`_Java7(2011)_

---

---

---



![HEAP-графическое_представление](/_ITM_old_version_FOR_DELETE/ITM01_Core1/imgs/2025-03-06_20-13-36.png)

![Список GC в Java (кратко)](/_ITM_old_version_FOR_DELETE/ITM01_Core1/imgs/2025-03-04_11-55-41.png)

Сборка мусора выполняется с кратковременной паузой **Stop-The-World** (_STW_), 
временно останавливающей приложение.

[Статья: **_"Garbage Collection и JVM"_** на `habr.com`](https://habr.com/ru/companies/otus/articles/776342/)

```text
***** из методички *****
 
"Сборщик мусора выполняет две задачи:
- поиск мусора;
- очистка мусора.

Для обнаружения мусора есть два подхода:"
- Учет ссылок (Reference counting);
"Учет ссылок - если обьект не имеет ссылок, он считается мусором.
Проблема - не возможность выявить циклические ссылки, когда два обьекта не имеют внешних ссылок, 
но ссылаются друг на друга -> утечка памяти"
- Трассировка (Tracing). (используется в HotSpot)6
"Трассировка - до обьекта можно добраться из Корневых точке (GC root). 
До чего добраться нельзя - мусор.
Всё, что доступно из «живого» объекта, также является «живым»."
Типы корневых точек (GC Roots) java приложения:
- объекты в статических полях классов
- объекты, доступные из стека потоков
- объекты из JNI(java native interface) ссылок в native методах"
"Процессы сборки мусора разделяются несколько видов:
minor GC (малая) - частый и быстрый, работает только с областью памяти ""young generation"";
- приложение приостанавливается на начало сборки мусора (такие остановки называются stop-the-world);
- «живые» объекты из Eden перемещаются в область памяти «To»;
- «живые» объекты из «From» перемещаются в «To» или в «old generation», если они достаточно «старые»;
- Eden и «From» очищаются от мусора;
- «To» и «From» меняются местами;
- приложение возобновляет работу.
major GC (старшая) - редкий и более длительный, затрагивает объекты старшего поколения.
В принцип работы «major GC» добавляется процедура «уплотнения», 
позволяющая более эффективно использовать память. 
В процедуре живые объекты перемещаются в начало. Таким образом, мусор остается в конце памяти.
full GC (полная) -  полный сборщик мусора сначала запускает Minor, 
а затем Major (хотя порядок может быть изменен, 
если старое поколение заполнено, и в этом случае он освобождается первым, 
чтобы позволить ему получать объекты от молодого поколения).
```
---
</details>



<details>
        <summary>24. Виды ссылок в Java</summary>

1. **Strong Reference** _(Сильная ссылка)_   
* **Обычные** ссылки, которые мы создаем в коде.   
* Объекты с такими ссылками **не удаляются** **GC**, пока ссылка на них **существует**.  
```java
    StringBuilder builder = new StringBuilder(); // builder - это strong-ссылка на объект StringBuilder
```

2. **Soft Reference** _(Мягкая ссылка)_   
* **GC** гарантированно удаляет объекты, доступные **только** через **soft**-ссылки ** 
только в случае нехватки памяти** (_перед выбросом `OutOfMemoryError`_).   
* Используются для кэширования.   
~~То же самое работает для **WeakReference**.~~   
```java
    StringBuilder builder = new StringBuilder();
    SoftReference<StringBuilder> softBuilder = new SoftReference(builder);
    softBuilder.get();  // Возвращает strong-ссылку на объект, если он не удален GC (иначе -null)
    softBuilder.clear(); // Явное удаление ссылки
```

3. **Weak Reference** _(Слабая ссылка)_   
* Объекты, доступные **только через ~~_цепочку_~~ weak-ссылки**, удаляются **при первом же запуске GC**.  
* Используется в `WeakHashMap` для хранения ключей.   
```java
    WeakReference<StringBuilder> weakBuilder = new WeakReference<>(new StringBuilder("Hello"));
    weakBuilder.get();  // Может вернуть null, если объект удален GC
```
 
4. **Phantom Reference** _(Фантомная ссылка)_   
* **Не позволяет** получить объект (_метод `get()` **всегда возвращает `null`**_).   
* Используется для отслеживания удаления объекта сборщиком мусора (_например, для управления нативными ресурсами_);   
* В отличие от `WeakReference`, **не удаляется сразу** после потери всех **сильных** ссылок;   
* Можно получить только через `ReferenceQueue`;   
* Полезна для чистки ресурсов (_например, при работе с `DirectByteBuffer`_);   
   💡 **Фантомные ссылки не заменяют финализаторы**, но дают больше **контроля** над удалением объектов. 
```java
    ReferenceQueue<StringBuilder> queue = new ReferenceQueue<>();
    PhantomReference<StringBuilder> phantomBuilder = new PhantomReference<>(new StringBuilder("Hello"), queue);
    phantomBuilder.get();  // Всегда возвращает null
```
 
**ReferenceQueue** (_Очередь ссылок_) - специальная очередь, в которую попадают ссылки 
(`WeakReference`, `SoftReference`, `PhantomReference`) **после** удаления объекта сборщиком мусора (**GC**).   
* Позволяет **отслеживать** момент, когда **GC** признал объект **ненужным**.   
* **Reference-объект** (_не сам удалённый объект_) помещается в очередь **после** очистки памяти.   
* При **создании** ссылки можно передать `ReferenceQueue`, куда она будет добавлена после удаления объекта.   
* **PhantomReference** всегда попадает в `ReferenceQueue`, но только после вызова `finalize()` объекта.   
  💡 В очередь попадают **именно ссылки**, а **не сами объекты**.

```text
***** из методички *****
1) StrongReference — это самые обычные ссылки которые 
    мы создаем каждый день, любая переменная ссылочного типа.
        StringBuilder builder = new StringBuilder();
            - builder это и есть strong-ссылка на объект StringBuilder.
        
2) SoftReference —  GC гарантировано удалит с кучи все объекты, 
    доступные только по soft-ссылке, перед тем как бросит OutOfMemoryError. 
    SoftReference это наш механизм кэширования объектов в памяти, но в критической 
    ситуации, когда закончится доступная память, GC удалит не использующиеся 
    объекты из памяти и тем самым попробует спасти JVM от завершения работы.
        StringBuilder builder = new StringBuilder();
        SoftReference<StringBuilder> softBuilder = new SoftReference(builder);
        softBuilder.get() — вернет strong-ссылку на объект StringBuilder 
            в случае если GC не удалил этот объект из памяти. 
            В другом случае вернется null.
        softBuilder.clear() — удалит ссылку на объект StringBuilder
То же самое работает для WeakReference."

3) WeakReference — если GC видит, что объект доступен только через цепочку 
    weak-ссылок (исчезнули strong-ссылки), то он удалит его из памяти.

4) PhantomReference — если GC видит что объект доступен только через цепочку 
    phantom-ссылок, то он его удалит из памяти. После нескольких запусков GC.
    Особенностей у этого типа ссылок две.
    * Первая это то, что метод get() всегда возвращает null. 
        Именно из-за этого PhantomReference имеет смысл использовать 
        только вместе с ReferenceQueue.
    * Вторая особенность – в отличие от SoftReference и WeakReference, 
        GC добавит phantom-ссылку в ReferenceQueue после того 
        как выполниться метод finalize().

So in brief: Soft references try to keep the reference. Weak references don’t try to keep the reference. 
Phantom references don’t free the reference until cleared.

ReferenceQueue. Он позволяет отслеживать момент, когда GC определит 
что объект более не нужен и его можно удалить. 

Именно сюда попадает Reference объект после того как объект на который он ссылается удален из памяти. 
При создании Reference мы можем передать в конструктор ReferenceQueue, 
в который будут помещаться ссылки после удаления.
```
---
</details>



<details>
        <summary>25.  Stack и Heap</summary>


* 🔹**Stack** _(стек)_ – исп. для хранения _stack frame'ов_, работает по схеме `LIFO`.
> Содержит:
> * параметры метода,
> * указатель на предыдущий фрейм и 
> * локальные переменные.

* 🔹**Heap** _(куча)_ – Эта область для динамического выделения памяти 
для объектов и классов _JRE_ во время выполнения.   
  / Новые объекты всегда создаются в **куче**, а ссылки на них хранятся в **стеке**.   
  / Эти объекты имеют **глобальный** доступ и могут быть получены из **любого** места программы.   
  / Структура **Heap** зависит от выбранного `GC` (_Serial, Parallel, CMS, G1_).   
  / Размер стека **меньше**, но он **быстрее** кучи.

 _**Начальный** размер (Xms) =  **1/64** физической памяти машины (или нек-го разумного минимума).   
**Максимальный** размер (Xmx): **1/4** физической памяти машины (или 1 Гб. До версии 1.5 = 64 Мб)._

**Обе** области хранятся в **RAM**.   
**Ошибки** памяти: `StackOverflowError` (_переполнение **стека**_), `OutOfMemoryError` (_переполнение **кучи**_).  

💡 **Heap** разбит на части (поколения _Generation_):

![иллюстрация Heap при G1 GC](/_ITM_old_version_FOR_DELETE/ITM01_Core1/imgs/2025-02-24_23-16-12.png)
* **Young Generation** — область где размещаются **недавно** созданные объекты.   
Когда она заполняется, происходит быстрая _сборка мусора_ Old (Tenured)   
* **Old Generation** — здесь хранятся долгоживущие объекты.   
Когда объекты из **Young Generation** достигают определенного порога «_возраста_», 
они перемещаются в **Old Generation**   
* **Permanent Generation** — эта область содержит метаинформацию о классах и методах приложения, 
но начиная с `Java 8` данная область памяти была **упразднена** (_классы и методы_). ~~Подробности ниже:~~

> * ✅**PermGen** (_Permanent Generation_) — это специальное место **в куче**, **отделенное** от основной памяти.    
> В **PermGen** виртуальная машина хранит **метаданные загруженных классов**.   
> Также здесь находятся:
> > * всё статическое содержимое приложения, 
> > * переменные примитивных типов и 
> > * ссылки на статические объекты, 
> > * хранит данные о **байткоде** и **JIT информацию**.  
> 
> По умолчанию, максимальный размер этой области памяти: 
> > для **32**-х битной JVM равен **64 Мб**, а   
> > для **64**-х битной версии — **82 Мб**.   
> 
> `-XX:PermSize=[размер]` для установки **минимального** размера **PermGen** области,   
> `-XX:MaxPermSize=[размер]` для установки **максимального** размера   
> 
> Из-за своего ограниченного размера, **PermGen** является причиной возникновения 
> ошибки `java.lang.OutOfMemoryError: PermGen space`.
> 
> 
> 
> * ✅**Metaspace** – новая область памяти, появившаяся в `Java 8` и заменившая устаревшую **PermGen**.    
> Основное их отличие заключается в **способе распределения памяти**.   
> / По умолчанию, **Metaspace** увеличивается **автоматически**.   
> / Есть возможность **управления памятью**: границы можно задать при помощи `MetaspaceSize` и `MaxMetaspaceSize`.   
> / Процесс очистки памяти получил преимущества: `GC` автоматически удаляет ненужные классы, 
> когда пространство под метаданные заканчивается. 

![Модель памяти **Java 8**](/_ITM_old_version_FOR_DELETE/ITM01_Core1/imgs/2025-03-06_10-55-51.png)


```text
***** из методички *****
Память процесса делится на Stack (стек) и Heap (куча) :
- Stack содержит staсk frame'ы, они делятся на три части: 
    * параметры метода,   
    * указатель на предыдущий фрейм    
    * и локальные переменные.

- Структура Heap зависит от выбранного 
    сборщика мусора. Читай про GC!

* MetaSpace - специальное пространство кучи, отделенное от кучи основной памяти. 
    JVM хранит здесь весь статический контент. 
    Это включает в себя все статические методы, 
    примитивные переменные и ссылки на статические объекты. 
    Кроме того, он содержит данные о байт-коде, 
    именах и JIT-информации. 
    До Java 7 String Pool также был частью этой памяти. 

Вкратце, при Serial/ Parallel/ CMS GC будет следующая структура:

А при G1 GC:
С помощью опций Xms и Xmx можно настроить 
начальный и максимально допустимый размер кучи. 
Существуют опции для настройки величины стека.

- Heap - используется всем приложением, 
    Stack - одним потоком исполняемой программы.
- Новый обьект создается в heap, 
    в stack размещается ссылка на него. 
    В стеке размещаются локальные переменные примитивных типов. 
- Обьекты в куче доступны из любого места программы, 
    стековая память не доступна для других потоков.
- Если память стека закончилась 
    JRE вызовет исключение StackOverflowError, 
    если куча заполнена OutOfMemoryError
- Размер памяти стека, меньше памяти кучи. 
    Стековая память быстрее памяти кучи.
- В куче есть ссылки между объектами и их классами. 
    На этом основана рефлексия.

Обе области хранятся в RAM.
```
---
</details>



[>>> **NEXT** > _Следующая страница_ >>>](/_ITM_old_version_FOR_DELETE/ITM01_Core1/3_Core1_ProcedureJava.md)










---

<details>
        <summary>Head</summary>

```text
***** из методички *****
```
</details>
