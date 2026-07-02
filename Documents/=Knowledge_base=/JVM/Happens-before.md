# **`Happens-before`** — что это, пояснить всё

 `happens-before` — это **гарантия порядка операций** в *Java Memory Model* (JMM). 
 Если операция **A** *happens-before* операцией **B**, то все изменения **A** видны в **B** (*и B видит A*).
 
## **Ключевые правила `happens-before`:**
 1. **Программный порядок** — внутри одного потока каждая инструкция happens-before следующей.
     
 2. **Монитор (synchronized)** — разблокировка монитора *happens-before* последующей блокировкой этого же монитора.
     
 3. **Volatile** — запись в `volatile` переменную *happens-before* последующим чтением этой же переменной.
     
 4. **Thread start** — `Thread.start()` *happens-before* первым действием в новом потоке.
     
 5. **Thread join** — все действия в потоке *happens-before* `Thread.join()` возвращает управление.
     
 6. **Interrupt** — вызов `interrupt()` *happens-before* когда поток обнаруживает прерывание.
     
 7. **Transitivity** — если **A** *happens-before* **B**, а **B** *happens-before* **C**, то **A** *happens-before* **C**.
 
 **Пример:**
 ```java
 int x = 0;
volatile boolean ready = false;

// Поток 1
x = 42;      // 1
ready = true; // 2 (volatile write)

// Поток 2
if (ready) {  // 3 (volatile read)
    System.out.println(x); // увидит 42
}
 ```
 
 `Volatile` гарантирует, что всё, что было до записи, видно при чтении.
 
 **Зачем нужно:** Без *happens-before* JVM могла бы **переупорядочивать** инструкции, и поток 2 мог увидеть `ready=true`, но `x=0`.

---
