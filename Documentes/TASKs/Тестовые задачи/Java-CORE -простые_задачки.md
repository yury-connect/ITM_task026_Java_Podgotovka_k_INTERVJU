---
tags:
  - Tasks/Core
---
# Java задачи: Анализ и результаты

---
### 1. Сравнение значений

#### Примитивные типы (int)
```java
int s1 = 33;
int s2 = 33;
System.out.println(s1 == s2); // true (сравнение значений)

int s3 = 3333;
int s4 = 3333;
System.out.println(s3 == s4); // true
```

#### Объектные типы (Integer)
```java
Integer s1 = 127;
Integer s2 = 127;
System.out.println(s1 == s2); // true (кеширование)

Integer s3 = 128;
Integer s4 = 128;
System.out.println(s3 == s4); // false (вне кеша)
```

---
### 2. Работа со строками

#### Пул строк
```java
String s1 = "Java";
String s2 = "Java";
System.out.println(s1 == s2); // true
```

#### Новые объекты
```java
String s1 = "Java";
String s2 = new String("Java");
System.out.println(s1 == s2); // false
System.out.println(s1.equals(s2)); // true
```

#### Интернирование
```java
String s1 = "Java";
String s2 = new String("Java").intern();
System.out.println(s1 == s2); // true
```

---
### 3. Инкременты/декременты

#### Базовые операции
```java
int a = 1;
int x = a++;
System.out.println(a); // 2
System.out.println(x); // 1
```

#### Комбинированные операции
```java
int a = 1;
int x = a++ + ++a;
System.out.println(a); // 3
System.out.println(x); // 4
```

#### Множественные изменения
```java
int a = 1;
int x = a++ + ++a + a++;
System.out.println(a); // 4
System.out.println(x); // 6
```

---
### 4. Generics и коллекции

#### Wildcards
```java
List<? extends Number> list1 = new ArrayList<Integer>();
// list1.add(10); // Ошибка компиляции

List<? super Integer> list2 = new ArrayList<Number>();
list2.add(10); // OK
```

---
### 5. Переполнение byte
```java
byte b1 = (byte) -255;
System.out.println(b1); // 1

byte b2 = (byte) -200;
System.out.println(b2); // 56
```

---
### 6. Особые случаи

#### Условные операторы
```java
int num = 5;
if (num == 1) {
    System.out.println("Один");
} else {
    System.out.println("Другое число");
}
```

#### Сравнение Long
```java
Long l1 = 127L;
Long l2 = 127L;
System.out.println(l1 == l2); // true
```

---
## Ключевые особенности

1. **Кеширование объектов**:
   - Integer/Long кешируются в диапазоне -128..127
   - Всегда используйте `.equals()` для сравнения объектов

2. **Строки**:
   - Литералы попадают в пул строк
   - `new String()` создает новый объект
   - `intern()` помещает строку в пул

3. **Инкременты**:
   - `a++` - возвращает значение до увеличения
   - `++a` - сначала увеличивает, потом возвращает

4. **Generics**:
   - `? extends` - только чтение
   - `? super` - можно добавлять элементы

5. **Переполнение byte**:
   - Значения циклически переназначаются
   - -256 → 0, -255 → 1, -200 → 56

6. **Рекомендации**:
   - Для сложных выражений с инкрементами используйте скобки
   - Избегайте множественных изменений переменной в одном выражении
   - Для строк всегда предпочитайте `equals()` вместо `==`