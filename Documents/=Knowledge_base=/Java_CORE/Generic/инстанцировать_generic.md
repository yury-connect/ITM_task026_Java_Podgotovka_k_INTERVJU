# Как инстанцировать экземпляр generic типа?
```java
class Bar { }

class Foo<T> {
    T instantiateWithHack() throws Exception {
        Type genericSuperclass = getClass().getGenericSuperclass();
        ParameterizedType superType = (ParameterizedType) genericSuperclass;
        Class<T> clazz = (Class<T>) superType.getActualTypeArguments()[0];
        return clazz.newInstance();
    }

    T instantiateWithMetaclass(Class<T> clazz) throws Exception {
        // clazz придется передавать вручную,
        // но компилятор позволит передать только то единств. правильное знач-е.
        return clazz.newInstance();
    }
}

// Использование
new Foo<Bar>().instantiateWithHack();
new Foo<Bar>().instantiateWithMetaclass(Bar.class);
```
Внутри класса `class Foo<T>` на generic параметре `T` невозможно выполнить никакой оператор: нельзя взять его `.class`, нельзя применить его в `instanceof`. Также и вызов на нем оператора new приведет к ошибке.  
  
Причина этих ограничений кроется в [стирании типов](https://itsobes.com/ru/java/chto-takoe-type-erasure). Дженерик параметры правильно воспринимать скорее как ограничения типов, чем как конкретные типы. Эти ограничения действуют для более строгих проверок на этапе компиляции. В рантайме же информация о конкретных переданных типах-параметрах стирается. А все эти операторы выполняются именно в рантайме.  
  
Стандартный простой способ действия здесь – кроме значения типа T передавать еще и объект-дескриптор для этого типа, экземпляр класса `Class<T>`. Объект может быть создан из дескриптора [рефлекшеном](https://itsobes.com/ru/java/chto-takoe-reflection-i-kak-ego-ispolzovat).  
  
Но существует один хак, способный справиться со стиранием типов. Тип-параметр все-таки остается в одном месте в рантайме. Метод метакласса _наследника определившего конкретный тип_ `getGenericSuperclass()` возвращает класс, которым параметризован _родитель_.

ссылка на статью: https://itsobes.com/ru/java/kak-instantsirovat-ekzempliar-generic-tipa/

---
---
---
# `getGenericSuperclass()`
Теперь детально про **`getGenericSuperclass()`**

### Главная идея «на пальцах»
Представь, что в Java есть правило: **«Всё, что происходит внутри `<...>`, стирается после компиляции»**. То есть, когда программа уже запущена, Java стирает информацию о том, что это был список именно строк (`List<String>`) или мапа чисел (`Map<Long, ...>`). Для нее это просто «какой-то список» и «какая-то мапа». Это называется _стиранием типов_.

Но разработчикам фреймворков (Spring, Hibernate, Jackson) очень часто нужно знать в рантайме: _«А с каким конкретно типом данных нас попросили поработать?»_

И вот тут на сцену выходит **`getGenericSuperclass()`**. Это специальная лазейка (или «черный ход») в Java, которая позволяет обойти это стирание, но **только в одном конкретном сценарии — при наследовании**.

### Житейская аналогия
Представь себе **чертеж коробки** (это родительский класс):
> `Класс Коробка<Т>` — абстрактная коробка для чего угодно.

Пока мы не создали конкретный подвид коробки, Java не знает, что такое `T`.
Но как только мы берем и создаем **новый чертеж на основе старого** (наследника):
> `Класс КоробкаДляОбуви расширяет Коробка<Обувь>`

Мы прямо в тексте программы («в граните») высекли слово **`<Обувь>`**. И вот эту надпись Java стереть уже не может — она навсегда записана в паспорт класса `КоробкаДляОбуви`.

Метод `getGenericSuperclass()` — это способ подойти к классу `КоробкаДляОбуви` в процессе работы программы, заглянуть в его паспорт (байт-код) и прочитать: **«Ага, твой родитель — это Коробка, и параметризован он типом Обувь!»**.

### Зачем это нужно в реальной жизни?
Без этого метода магия Spring Data JPA была бы невозможна. Когда ты пишешь:
```Java
public interface UserRepository extends JpaRepository<User, Long> { ... }
```

Spring с помощью `getGenericSuperclass()` (и схожих методов для интерфейсов) сканирует твой код и понимает: _«Так, этот репозиторий создан для сущности `User`. Значит, когда вызовут метод `findAll()`, я должен пойти именно в таблицу `users`, достать оттуда строки и превратить их в объекты `User`»_.

То есть это инструмент, позволяющий родительскому классу **автоматически подстраиваться** под то, какого именно наследника от него создали, не заставляя программиста вручную передавать `User.class` в конструкторы.

Представь, что мы пишем базовый обработчик данных (конвертер) из JSON в Java-объекты. Мы хотим, чтобы базовый класс сам понимал, в какой тип данных нужно превращать строку, без ручной передачи `.class` в конструктор.

Вот этот код с подробнейшими комментариями к каждой строчке:
```java
package com.innowise.livecoding.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

// 1. Создаем абстрактного родителя с дженериком <T>
abstract class BaseConverter<T> {
    
    // В этой переменной мы сохраним реальный класс, который придет вместо T
    private final Class<T> typeArgumentClass;

    @SuppressWarnings("unchecked")
    public BaseConverter() {
        // МАГИЯ НАЧИНАЕТСЯ ЗДЕСЬ:
        
        // getClass() возвращает класс ТОГО объекта, который РЕАЛЬНО создается
        //  в памяти (наследника). Если мы создали StringConverter, 
        // то getClass() вернет StringConverter.class
        Class<?> concreteSubclass = getClass();

        // getGenericSuperclass() идет к родителю этого наследника, 
        // но не просто возвращает класс, а возвращает его вместе с "хвостом"
        // в виде дженерика (например: BaseConverter<String>).
        Type genericSuperclass = concreteSubclass.getGenericSuperclass();

        // Нам нужно вытащить то, что внутри скобок <...>. 
        // Для этого проверяем, является ли полученный 
        // тип параметризованным (т.е. содержит ли он дженерики).
        if (genericSuperclass instanceof ParameterizedType) {
            
            // Приводим к интерфейсу ParameterizedType, 
            // у которого есть нужные нам методы
            ParameterizedType parameterizedType 
	            = (ParameterizedType) genericSuperclass;

            // getActualTypeArguments() возвращает МАССИВ всех типов, 
            // переданных в треугольные скобки.
            // Так как у нас BaseConverter<T> имеет только один дженерик, 
            // его значение лежит под индексом [0].
            Type firstTypeArgument 
	            = parameterizedType.getActualTypeArguments()[0];

            // Приводим этот Type к обычному Class<T> 
            // и сохраняем в нашу переменную
            this.typeArgumentClass = (Class<T>) firstTypeArgument;
            
        } else {
            // Если кто-то наследовался без указания дженерика (Raw Type),
            // кидаем исключение
            throw new IllegalArgumentException(
	            "Родительский класс должен быть параметризован!");
        }
    }

    // Обычный метод родителя, который теперь имеет доступ 
    // к информации о типе в рантайме!
    public void printTargetType() {
        System.out.println("Конвертер настроен на работу с типом: " 
	        + typeArgumentClass.getName());
    }
}

// 2. Создаем конкретного наследника. 
// Мы жестко прописываем String вместо T. 
// Это запечатывается в байт-код класса StringConverter!
class StringConverter extends BaseConverter<String> {
    // Конструктор по умолчанию автоматически вызовет super() родителя, 
    // где отработает логика выше
}

// 3. Создаем еще одного наследника, но уже для чисел Integer
class IntegerConverter extends BaseConverter<Integer> {
}

// Точка запуска для проверки
public class Main {
    public static void main(String[] args) {
        // Создаем объект для строк
        BaseConverter<String> stringWorker = new StringConverter();
        stringWorker.printTargetType(); // Выведет: java.lang.String

        // Создаем объект для чисел
        BaseConverter<Integer> intWorker = new IntegerConverter();
        intWorker.printTargetType(); // Выведет: java.lang.Integer
    }
}
```

### На что обратить внимание при разборе:
1. **Строка `getClass()`:** Если бы этот метод возвращал класс, где он _написан_ (то есть `BaseConverter`), то `getGenericSuperclass()` вернул бы просто `Object.class` (ведь у `BaseConverter` родитель — это `Object`). Но так как это динамический полиморфизм, `getClass()` всегда знает имя «конечного» класса-наследника.
    
2. **Безопасность (`instanceof ParameterizedType`):** Это обязательная проверка в продакшн-коде. Если кто-то напишет `class RawConverter extends BaseConverter`, не указав тип в скобках, то `getGenericSuperclass()` вернет обычный `Class`, а не `ParameterizedType`, и без этой проверки программа упадет на касте (будет `ClassCastException`).

---
### Соберем всю суть в три супер-простых предложени:

1. **Проблема:** В Java из-за «стирания типов» запущенная программа напрочь забывает, какие типы данных были указаны внутри треугольных скобок `<...>`.
    
2. **Исключение:** Единственное место, где Java эту информацию **не стирает**, — это объявление класса-наследника (когда мы пишем `class MyConverter extends BaseConverter<String>`). В этот момент тип `String` намертво вшивается в «паспорт» (байт-код) класса-наследника.
    
3. **Решение:** Метод `getGenericSuperclass()` — это просто инструмент, который позволяет заглянуть в этот «паспорт» наследника и прочитать, какой именно тип там запечатан.    

### 🧩 Аналогия для закрепления
Представь **ксерокс** (*это твой фабричный метод или обычный объект с дженериком*). Ты заправил в него синюю краску `<Синий>`. Но как только ксерокс сделал копию, он сам забыл, какая краска в нем была — на выходе просто лист бумаги. Информации в памяти нет.

А теперь представь **памятник** (это класс-наследник). На этапе проектирования скульптор высек на граните: _«Этот памятник посвящен Синей краске»_ (`extends Base<Синий>`). Памятник стоит веками.

Метод `getGenericSuperclass()` — это просто **туристический гид**, который подходит к памятнику, читает надпись на граните и говорит родительскому классу: _«Шеф, мы строили это под Синюю краску, работаем!»_.

Вот и вся магия. Именно на этом «обмане» стирания типов и держатся все умные фреймворки вроде Spring и Hibernate.

---
редставь, что сервер присылает нам данные. Иногда это список пользователей, иногда — список товаров. Мы хотим написать **один базовый класс-парсер**, который сам понимает, в какой класс превращать пришедший JSON-текст, без передачи `.class` в конструктор.

Вот наглядный пример, имитирующий работу популярных библиотек (вроде Jackson или Gson):
```java
package com.innowise.livecoding.reflection;

import java.lang.reflect.ParameterizedType;

// 1. Модели данных, которые приходят от сервера
class UserResponse { String name; }
class ProductResponse { String title; }

// 2. Базовый абстрактный парсер. Он ОДИН для всех типов данных.
abstract class ApiParser<T> {
    
    // Метод, который будет разбирать "якобы JSON"
    public void parse(String jsonText) {
        // Заглядываем в "паспорт" наследника через getGenericSuperclass()
        ParameterizedType superClass = (ParameterizedType) getClass()
	        .getGenericSuperclass();
        
        // Достаем тип класса, который указан в скобках <...>
        Class<?> targetClass = (Class<?>) superClass
	        .getActualTypeArguments()[0];
        
        System.out.println("Парсер прочитал текст: \"" + jsonText + "\"");
        System.out.println("-> Магия рефлексии: 
	        Создаю и возвращаю объект класса: " + targetClass.getSimpleName());
        System.out.println("--------------------------------------------------");
    }
}

// 3. Конкретные парсеры. Внутри них вообще НЕТ кода. Только объявление!
class UserParser extends ApiParser<UserResponse> {
    // В байт-коде этого класса навсегда записано: 
    // мой родитель параметризован UserResponse
}

class ProductParser extends ApiParser<ProductResponse> {
    // В байт-коде этого класса навсегда записано: 
    // мой родитель параметризован ProductResponse
}

// 4. Проверяем в работе
public class Main {
    public static void main(String[] args) {
        // Создаем парсер для пользователей
        ApiParser<UserResponse> userParser = new UserParser();
        userParser.parse("{name: 'Иван'}"); 
        // Выведет: Создаю и возвращаю объект класса: UserResponse

        // Создаем парсер для товаров
        ApiParser<ProductResponse> productParser = new ProductParser();
        productParser.parse("{title: 'Телефон'}"); 
        // Выведет: Создаю и возвращаю объект класса: ProductResponse
    }
}
```
#### В чем здесь наглядность?
Посмотри на классы `UserParser` и `ProductParser`. Они абсолютно **пустые**! В них нет ни строчки логики, нет конструкторов, они ничего не знают про `.class`.

Но благодаря тому, что они просто _унаследовались_ и указали нужный тип в треугольных скобках (`extends ApiParser<UserResponse>`), базовый класс `ApiParser` смог на лету подстроиться под нужную модель данных. Именно так Jackson понимает, как превратить JSON в твой DTO класс.

---
