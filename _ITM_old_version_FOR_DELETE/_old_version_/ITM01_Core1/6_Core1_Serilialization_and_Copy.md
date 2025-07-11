[⏪ **PREVIOUS** < _Предыдущая страница_ ⏪](/_ITM_old_version_FOR_DELETE/ITM01_Core1/5_Core1_Exceptions.md)

---
## Сериализация и копирование


<details>
        <summary>109. Что такое сериализация и как она реализована в Java?</summary>

**Сериализация** — это процесс преобразования объекта 
в последовательность байт для сохранения или передачи.

В Java реализуется через интерфейс `Serializable`, 
который **не содержит методов**, но помечает класс 
как поддерживающий сериализацию.

```text
***** из методички *****
"Сериализация это процесс сохранения состояния 
объекта в последовательность байт;  

Реализована через интерфейс - маркер Serializable. "
```
---
</details>



<details>
        <summary>110. Для чего нужна сериализация?</summary>

Сериализация нужна для **сохранения** и **передачи** состояния объекта, 
например, при работе с _файлами_, _базами данных_, _сетью_ или _кэшированием_.

```text
***** из методички *****
Для компактного сохранения состояния объекта 
и считывание этого состояния.
```
---
</details>



<details>
        <summary>111. _процесс сериализации/десериализации</summary>

**Опишите процесс сериализации/десериализации с использованием `Serializable`.**

**Сериализация**:

1. Класс должен реализовать `Serializable`.
2. Использовать `ObjectOutputStream` для записи объекта в `OutputStream`.
3. Вызвать `writeObject(object)`.
4. Завершить `flush()` и `close()`.

**Десериализация**:

1. Использовать `ObjectInputStream` для чтения из `InputStream`.
2. Вызвать `readObject()`.
3. Привести результат к нужному типу.

**Пример**: код сначала записывает объект `Person` в файл `person.ser`, 
а потом считывает его обратно, восстанавливая объект в памяти.
```java
import java.io.*;

// Класс, который будет сериализоваться
class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}

public class SerializationExample {
    public static void main(String[] args) {
        Person person = new Person("Alice", 30);

        // Сериализация
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("person.ser"))) {
            oos.writeObject(person);
            System.out.println("Объект сериализован");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Десериализация
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("person.ser"))) {
            Person deserializedPerson = (Person) ois.readObject();
            System.out.println("Объект десериализован: " + deserializedPerson);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

```text
***** из методички *****
1) Класс объекта должен реализовывать интерфейс Serializable
2) Создать поток ObjectOutputStream (oos), 
который записывает объект в переданный OutputStream.
3) Записать в поток: oos.writeObject(Object);
4) Сделать oos.flush() и oos.close()"
```
---
</details>



<details>
        <summary>112. _изменить поведение сериализации_</summary>

**Как изменить стандартное поведение `сериализации`/`десериализации`?**

Изменить стандартное поведение можно, реализовав интерфейс `Externalizable` 
и переопределив методы `writeExternal()` и `readExternal()`. 
Это дает **полный контроль** над процессом 
`сериализации` и `десериализации`.

```text
***** из методички *****
"Использовать интерфейс Externalizable. 

Переопределить методы
 writeExternal(ObjectOutput out) throws IOException
 readExternal(ObjectInput in) throws IOException, ClassNotFoundException"
```
---
</details>



<details>
        <summary>113. _сериализациия полей/ final</summary>

**Какие поля не будут сериализованы при сериализации? Будет ли сериализовано `final` поле?**

Не сериализуются:

1. `transient` – после десериализации будет `null` или значение по умолчанию.
2. `static` – не сохраняются, так как принадлежат **классу**, а не объекту.

`final`-поля сериализуются стандартным способом, но при `Externalizable` 
их изменить нельзя, так как они должны быть инициализированы в конструкторе.

```text
***** из методички *****
1) Добавить к полю модификатор transient. 
В таком случае после восстановления его значение будет null.

2) Сделать поле static. Значения статических полей автоматически не сохраняются. 

3) Поля с модификатором final сериализуются как и обычные. 
За одним исключением – их невозможно десериализовать при использовании Externalizable, 
поскольку final-поля должны быть инициализированы в конструкторе, 
а после этого в readExternal изменить значение этого поля будет невозможно. 
Соответственно, если необходимо сериализовать объект с final-полем 
неоходимо использовать только стандартную сериализацию.
```
---
</details>



<details>
        <summary>114. Как создать собственный протокол сериализации?</summary>

Создать собственный протокол можно, переопределив `writeExternal()` 
и `readExternal()` в `Externalizable`. 

В этом случае все операции `чтения` и `записи` выполняются **вручную**,
без автоматической сериализации.

```text
***** из методички *****
Для создания собственного протокола 
нужно просто переопределить writeExternal() и readExternal(). 

В отличие от двух других вариантов сериализации, 
здесь ничего не делается автоматически. 
Протокол полностью в ваших руках.
```
---
</details>



<details>
        <summary>115. Какая роль поля serialVersionUID в сериализации?</summary>

`serialVersionUID` — это `private` `static` `final` `long` поле, 
определяющее **версию** сериализованного класса. 

Оно предотвращает ошибки десериализации, гарантируя совместимость 
объектов между разными версиями класса. Если поле не указано, 
JVM вычисляет его автоматически, что может привести 
к несовместимости при изменениях в классе.

Пример использования `serialVersionUID`:

```java
import java.io.*;

class Person implements Serializable {
    private static final long serialVersionUID = 1L; // Фиксированная версия класса

    private String name;

    public Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "'}";
    }
}

public class SerialVersionUIDExample {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Сериализация
        Person person = new Person("Alice");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("person.ser"))) {
            oos.writeObject(person);
        }

        // Десериализация
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("person.ser"))) {
            Person deserializedPerson = (Person) ois.readObject();
            System.out.println(deserializedPerson);
        }
    }
}
```

Если изменить класс `Person` (например, добавить новое поле) и не задать `serialVersionUID`, 
JVM сгенерирует новое значение автоматически, и десериализация старого объекта 
может вызвать `InvalidClassException`. 
Если `serialVersionUID` задан **вручную**, старые объекты **останутся совместимыми**.

```text
***** из методички *****
Поле private static final long serialVersionUID 
содержит уникальный идентификатор версии сериализованного класса. 
Оно вычисляется по содержимому класса - полям, их порядку объявления, 
методам, их порядку объявления. 
Соответственно, при любом изменении в классе это поле поменяет свое значение.
Если мы не объявляем его явно, Java делает это за нас."
```
---
</details>



<details>
        <summary>116. Когда стоит изменять значение поля serialVersionUID?</summary>

Значение `serialVersionUID` следует менять только при внесении изменений, 
нарушающих совместимость с ранее сериализованными объектами. 

Это необходимо, если класс изменился настолько, 
что его старые версии нельзя корректно десериализовать. 

В остальных случаях сохранение `serialVersionUID` 
позволяет избежать ошибок совместимости.

```text
***** из методички *****
Вы должны изменить serialVersionUID только тогда, 
когда вы сознательно хотите нарушить совместимость 
со всеми существующими сериализациями , 
например, когда изменения в вашем классе сделают его 
настолько семантически отличным, что у вас не будет выбора 
- в этом случае вы действительно должны 
несколько раз подумать о том, что вы на самом деле делаете.
```
---
</details>



<details>
        <summary>117. В чем проблема сериализации Singleton?</summary>

**Проблема**: при десериализации `Singleton` создается **новый** экземпляр, 
нарушая его **единственность**.

**Решение**: реализовать метод

```java
protected Object readResolve() throws ObjectStreamException {
    return INSTANCE;
}
```
**Назначение**: предотвращает создание нового объекта, 
заменяя его **существующим** экземпляром `Singleton`.

```text
***** из методички *****
- Проблема  -
в том что после десериализации мы получим другой объект. 

Таким образом, сериализация дает возможность 
создать Singleton еще раз, что не совсем нужно. 

- Решение  -
В классе определяется метод с сигнатурой 
"Object readResolve() throws ObjectStreamException"

- Назначение  -
этого метода - возвращать замещающий объект 
вместо объекта, на котором он вызван."
```
---
</details>




<details>
        <summary>118. Расскажите про клонирование объектов.</summary>

Существует **три** способа `клонирования` объекта:

1. **Реализация `Cloneable` (_поверхностное клонирование_)**
> * Используется метод `clone()`, унаследованный от `Object`.
> * Класс должен реализовать `Cloneable`, иначе `clone()` выбросит `CloneNotSupportedException`.
> * **Примитивные** поля `копируются`, а **ссылочные** `остаются одинаковыми` 
    (_не создаются новые объекты_).

2. **Конструктор копирования**
> * В классе создается конструктор, принимающий объект этого же класса.
> * Позволяет контролировать процесс клонирования, 
> включая создание новых объектов для ссылочных полей.

3. **Сериализация (_глубокое клонирование_)**
> * Объект **сериализуется** в поток байтов, затем **десериализуется** в новый объект.
> * Все объекты внутри клонируемого также будут **новыми** экземплярами.
> * Требует реализации `Serializable`.

   **Каждый** метод имеет свои плюсы и минусы: 
*   `Cloneable` прост, но требует **ручного** клонирования сложных объектов; 
*   **конструктор** удобен, но требует **явного** кода; 
*   **сериализация** универсальна, но **медленнее** других методов.
 
```text
***** из методички *****
В Java, есть 3 способа клонирования объекта:

1. С использованием интерфейса Cloneable;
Первый способ подразумевает, что вы будете использовать 
механизм так называемого «поверхностного клонирования» 
и сами позаботитесь о клонировании полей-объектов. 
Метод clone() в родительском классе Object является protected, 
поэтому требуется переопределение его с объявлением как public. 
Он возвращает экземпляр объекта с копированными полями-примитивами 
и ссылками. 
И получается что у оригинала и его клона 
поля-ссылки указывают на одни и те же объекты. 

2. С использованием конструктора клонирования объекта;
В классе описывается конструктор, который принимает объект 
этого же класса и инициализирует значениями 
его полей поля нового объекта.

3. С использованием сериализации.
Он заключается в сохранении объекта в поток байтов 
с последующей эксгумацией его от туда."
```
---
</details>



<details>
        <summary>119. _отличие поверхностного и глубокого клонирования?</summary>

**В чем отличие между `поверхностным` и `глубоким` клонированием?**

1. **Поверхностное клонирование (_Shallow Copy_)**

* Копирует **только** сам объект и его примитивные поля.
* Ссылочные поля (_объекты_) копируются **по ссылке**, 
т.е. оригинал и клон **ссылаются на одни и те же** вложенные объекты.
* Используется метод `clone()` по умолчанию.

2. **Глубокое клонирование (_Deep Copy_)**

* Создаются **новые** копии **всех** вложенных объектов.
* Оригинальный и клонированный объекты **полностью независимы**.
* Реализуется **вручную** (_например, через конструктор копирования_) 
или с помощью `сериализации`.

**Главное отличие**
* При **поверхностном** клонировании вложенные объекты остаются **общими**, а 
* при **глубоком** клонировании создаются их **новые** копии.

```text
***** из методички *****
Поверхностное копирование копирует настолько малую часть информации, насколько это возможно. 
По умолчанию, клонирование в Java является поверхностным, 
т.е. Object class не знает о структуре класса, которого он копирует. 

Глубокое копирование дублирует все. 
Глубокое копирование — это две коллекции, 
в одну из которых дублируются все элементы оригинальной коллекции.
```
---
</details>



<details>
        <summary>120. Какой способ клонирования предпочтительней?</summary>

**Лучший вариант — конструктор копирования**, так как:
* ✔️ Избегает проблем с наследованием _(`clone()` не учитывает новые поля в подклассах)_.
* ✔️ Позволяет **явно** указать, какие поля копировать.
* ✔️ Работает даже с `final` полями.
* ✔️ Гибкость: можно реализовать **поверхностное** или **глубокое** клонирование.

`clone()` сложен в использовании, а сериализация медленнее и требует `Serializable`.

```text
***** из методички *****
Наиболее безопасным и следовательно предпочтительным способом клонирования 
является использование специализированного конструктора копирования:
* Отсутствие ошибок наследования (не нужно беспокоиться, что у наследников появятся новые поля, 
которые не будут склонированы через метод clone());
* Поля для клонирования указываются явно;
* Возможность клонировать даже final поля.
```
---
</details>



<details>
        <summary>121. Почему метод clone() объявлен в классе Object_?</summary>

**Почему метод `clone()` объявлен в классе `Object`, а не в интерфейсе `Cloneable`?**

Метод `clone()` находится в `Object`, а не в `Cloneable`, потому что:

1. **Использует нативный код** для копирования полей, включая ссылки.
2. **Объявлен как** `protected`, чтобы запретить клонирование объектов без явного разрешения.
3. `Cloneable` — **маркерный интерфейс**, не содержащий методов. 
Он лишь указывает, что объект **можно клонировать**.

Таким образом, Java требует, чтобы класс **явно переопределил** `clone()` 
и реализовал `Cloneable`, иначе метод бросит `CloneNotSupportedException`.

```text
***** из методички *****
Метод clone() объявлен в классе Object с сигнатурой native, 
чтобы обеспечить доступ к стандартному механизму "поверхностного копирования" объектов 
(копируются значения всех полей, включая ссылки на сторонние объекты); 

он объявлен, как protected, чтобы нельзя было вызвать 
этот метод у не переопределивших его объектов. 
```
---
</details>



<details>
        <summary>122. Как создать глубокую копию объекта? (2 способа)</summary>

Как создать глубокую копию объекта? (2 способа)
1. **Сериализация**
> * Объект записывается в байтовый поток (`ObjectOutputStream`) 
> и затем восстанавливается (`ObjectInputStream`).
> * Все вложенные объекты также **сериализуются**, создавая их **новые копии**.
> * Требует реализации `Serializable`.
> * **Минус**: 
> > * медленнее, 
> > * требует обработки исключений.

2. **Конструктор копирования**
> * Создается конструктор, принимающий объект того же класса 
> и **явно** копирующий его поля.
> * Позволяет **точно контролировать** процесс клонирования.
> * Работает даже с `final` полями.
> * **Минус**: 
> > * требует ручного прописывания копирования всех полей.

Другие варианты:

* Переопределение `clone()` + ручное копирование вложенных объектов.
* Библиотеки (`DeepCloneable`, `Apache Commons Lang`), если нужен готовый инструмент.

```text
***** из методички *****
1 Сериализация – это еще один способ глубокого копирования. 
 Мы просто сериализуем нужный объект и десериализуем его. 
    Очевидно, объект должен поддерживать интерфейс Serializable. 
    Мы сохраняет объект в массив байт и потом прочитать из него.
2 При помощи библиотеки DeepCloneable
    Глубокое клонирование с этой библиотекой сводится с двум строкам кода:
    Cloner cloner = new Cloner();
    DeepCloneable clone = cloner.deepClone(this);

- Переопределение метода clone() и реализация интерфейса Cloneable();
- Механизм сериализации - сохранение и последующее восстановление 
объекта в/из потока байтов.
- Конструктор копирования - в классе описывается конструктор, 
который принимает объект этого же класса и инициализирует 
поля создаваемого объекта значениями полей переданного;
```
---
</details>



---



<details>
        <summary>sealed классы</summary>

Введены в **Java 15** (_в предварительном виде_) и окончательно закреплены в **Java 17**. 

Они позволяют **явно контролировать**, какие классы могут наследоваться от данного класса, 
улучшая инкапсуляцию и безопасность кода.

🔹 Что такое **sealed классы**?
Обычно в Java любой класс можно унаследовать, если он не является final. 
Однако sealed (запечатанный) класс позволяет ограничить круг подклассов. 
Это полезно, когда вы хотите разрешить наследование только определённым классам.

🔹 Как объявить **sealed класс**?
При объявлении sealed класса нужно использовать ключевое слово permits, 
чтобы указать допустимые подклассы.

```java
public sealed class Animal permits Dog, Cat, Bird { 
    // код класса
}
```
Здесь `Animal` — запечатанный класс, и **только** `Dog`, `Cat` и `Bird` могут от него наследоваться.

🔹 Возможные подклассы
Классы, которые наследуют sealed класс, должны явно указать, как они себя ведут в плане наследования. 
Они могут быть:

1. `final` – запрещает дальнейшее наследование.
2. `sealed` – продолжает ограниченное наследование.
3. `non-sealed` – снимает ограничения, позволяя наследование без ограничений.

Пример:
```java
public final class Dog extends Animal { }  // нельзя унаследовать дальше

public sealed class Cat extends Animal permits PersianCat { }  // продолжает sealed-ограничения
public non-sealed class Bird extends Animal { }  // открывает наследование
```

🔹 Когда использовать sealed классы?
* ✅ Когда нужно контролировать иерархию наследования.
* ✅ Когда нужно улучшить безопасность кода.
* ✅ Когда разрабатываете API или библиотеку и хотите избежать нежелательных подклассов.

🔹 Отличия от других модификаторов:
![Отличия от других модификаторов](/_ITM_old_version_FOR_DELETE/ITM01_Core1/imgs/2025-02-25_15-57-31.png)

🔹 Пример с использованием instanceof
Так как sealed классы ограничивают подклассы, они отлично работают 
с instanceof и **pattern matching**:
```java
static void processAnimal(Animal animal) {
    switch (animal) {
        case Dog d -> System.out.println("This is a Dog");
        case Cat c -> System.out.println("This is a Cat");
        case Bird b -> System.out.println("This is a Bird");
    }
}
```

Здесь `switch` проверяет все возможные подклассы `Animal`, и компилятор знает, 
что **других вариантов быть не может**.

🔹 Итог:
`sealed` классы — это мощный инструмент для создания безопасных 
и контролируемых иерархий в Java. 
Они позволяют избежать нежелательного наследования и улучшают поддержку switch-выражений.

---
</details>










---

<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>
