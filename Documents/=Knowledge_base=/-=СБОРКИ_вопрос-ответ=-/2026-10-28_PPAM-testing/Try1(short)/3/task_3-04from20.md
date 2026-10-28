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

```
java

public void write(String str) throws IOException {
    OutputStreamWriter comp = new OutputStreamWriter(
        new OutputStream(new File("file")), "UTF8");
    comp.write(str);
    comp.close();
}

Вариант 3:
```java

```
java

public void write(String str) throws IOException {
    FileWriter comp = new FileWriter(new File("file"));
    comp.write(str);
    comp.close();
}

Вариант 4:
```java

```
java

public void write(String str) throws IOException {
    FileWriter comp = new FileWriter(new File("file"));
    comp.setEncoding("UTF8");
    comp.write(str);
    comp.close();
}

Вариант 5:
```java

```
java

public void write(String str) throws IOException {
    FilterWriter comp = new FilterWriter(
        new FileWriter("file"), "UTF8");
    comp.write(str);
    comp.close();
}

---

**Перевод задания на русский:**

4/20: Какая реализация метода запишет заданную строку в файл с именем "file", используя кодировку UTF8?

Вариант 1:

java

public void write(String str) throws IOException {
    OutputStreamWriter comp = new OutputStreamWriter(
        new FileOutputStream("file"), "UTF8");
    comp.write(str);
    comp.close();
}

Вариант 2:

java

public void write(String str) throws IOException {
    OutputStreamWriter comp = new OutputStreamWriter(
        new OutputStream(new File("file")), "UTF8");
    comp.write(str);
    comp.close();
}

Вариант 3:

java

public void write(String str) throws IOException {
    FileWriter comp = new FileWriter(new File("file"));
    comp.write(str);
    comp.close();
}

Вариант 4:

java

public void write(String str) throws IOException {
    FileWriter comp = new FileWriter(new File("file"));
    comp.setEncoding("UTF8");
    comp.write(str);
    comp.close();
}

Вариант 5:

java

public void write(String str) throws IOException {
    FilterWriter comp = new FilterWriter(
        new FileWriter("file"), "UTF8");
    comp.write(str);
    comp.close();
}