## 4/20: Which method implementation will write the given string to a file named "file", using UTF8 encoding?

Вариант 1:
```java
public void write(String str) throws IOException {
    OutputStreamWriter comp = new OutputStreamWriter(
        new FileOutputStream("file"), "UTF8");
    comp.write(str);
    comp.close();
}
```

Вариант 2:
```java
public void write(String str) throws IOException {
    OutputStreamWriter comp = new OutputStreamWriter(
        new OutputStream(new File("file")), "UTF8");
    comp.write(str);
    comp.close();
}
```

Вариант 3:
```java
public void write(String str) throws IOException {
    FileWriter comp = new FileWriter(new File("file"));
    comp.write(str);
    comp.close();
}
```

Вариант 4:
```java
public void write(String str) throws IOException {
    FileWriter comp = new FileWriter(new File("file"));
    comp.setEncoding("UTF8");
    comp.write(str);
    comp.close();
}
```

Вариант 5:
```java
public void write(String str) throws IOException {
    FilterWriter comp = new FilterWriter(
        new FileWriter("file"), "UTF8");
    comp.write(str);
    comp.close();
}
```

---
## 4/20: Какая реализация метода запишет заданную строку в файл с именем "file", используя кодировку UTF8?

---
Правильный ответ: **первый вариант** (самый верхний блок кода).

**Объяснение:**

Чтобы записать строку в файл с определенной кодировкой (в данном случае UTF-8), нужно использовать цепочку классов `OutputStreamWriter`, передав ему в конструктор экземпляр `FileOutputStream` и название кодировки.

Разберем варианты:

1. **Верный вариант:**
```java
OutputStreamWriter comp = new OutputStreamWriter(
    new FileOutputStream("file"), "UTF8");
```
    
- `FileOutputStream("file")` открывает поток для записи байтов в указанный файл.
        
- `OutputStreamWriter` оборачивает этот поток и преобразует символы в байты, используя указанную кодировку `"UTF8"`. Это стандартный и правильный способ.
        
2. **Неверный вариант:** `new OutputStreamWriter(new OutputStream(new File("file")), "UTF8");`    
    - Ошибка компиляции. Класс `OutputStream` является абстрактным, его нельзя создать через `new`. Кроме того, `File` не является потоком (это просто представление пути к файлу).
    
3. **Неверный вариант:** `FileWriter comp = new FileWriter(new File("file"));`    
    - Этот код запишет файл, но **не в UTF-8**, а в кодировке по умолчанию вашей операционной системы (например, Windows-1251 или Cp1252). Кодировку здесь указать нельзя.
    
4. **Неверный вариант:** `FileWriter comp = ...; comp.setEncoding("UTF8");`    
    - Ошибка компиляции. В классе `FileWriter` (как и в его родителе `OutputStreamWriter`) **нет метода** `setEncoding()`. Кодировка задается только один раз при создании объекта.
    
5. **Неверный вариант:** `FilterWriter comp = new FilterWriter(new FileWriter("file"), "UTF8");`    
    - Ошибка компиляции. `FilterWriter` — это абстрактный класс, у него нет конструктора, принимающего кодировку. Для указания кодировки нужно использовать `OutputStreamWriter`.

---
