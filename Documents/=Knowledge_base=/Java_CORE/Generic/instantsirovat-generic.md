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
        // но компилятор позволит передать только то единственно правильное значение.
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

---
ссылка на статью: https://itsobes.com/ru/java/kak-instantsirovat-ekzempliar-generic-tipa/
