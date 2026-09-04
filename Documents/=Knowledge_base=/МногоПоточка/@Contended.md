# @Contended

==**`Contended` в Java — это встроенная аннотация, которая указывает виртуальной машине (JVM) добавлять отступы (padding) из пустых байт вокруг поля или класса для предотвращения ложного совместного использования кэш-линий (false sharing)**==**.** [1](https://medium.com/@vishalpriyadarshi/understanding-the-java-contended-annotation-reducing-false-sharing-for-better-concurrency-deeac5dd1df0)

### Как это работает
- **Проблема (False Sharing):** Процессоры читают и синхронизируют память не отдельными байтами, а **целыми кэш-линиями** (*обычно по **64 байта***). Если два независимых потока на разных ядрах изменяют переменные, которые случайно оказались на одной кэш-линии, ядра начинают постоянно инвалидировать кэш друг друга. Это сильно замедляет многопоточные программы. [1](https://www.baeldung.com/java-false-sharing-contended), [2](https://habr.com/ru/companies/otus/articles/1067152/), [3](https://d-it.ru/blog/20260508), [4](https://medium.com/@vishalpriyadarshi/understanding-the-java-contended-annotation-reducing-false-sharing-for-better-concurrency-deeac5dd1df0)

- **Решение:** Аннотация `@Contended` добавляет вокруг аннотированного поля дополнительное пространство (по умолчанию 128 байт), физически разводя данные по разным кэш-линиям процессора. [1](https://medium.com/@vishalpriyadarshi/understanding-the-java-contended-annotation-reducing-false-sharing-for-better-concurrency-deeac5dd1df0), [2](https://www.baeldung.com/java-false-sharing-contended)

### Важные детали
- **Появление:** Аннотация появилась в Java 8 и находится в пакете `jdk.internal.vm.annotation`.

- **Использование:** По умолчанию в обычном пользовательском коде JVM может игнорировать эту аннотацию, если не включен специальный флаг запуска `-XX:-RestrictContended` (изначально она создавалась для нужд внутренней оптимизации самой JDK). [1](https://medium.com/@vishalpriyadarshi/understanding-the-java-contended-annotation-reducing-false-sharing-for-better-concurrency-deeac5dd1df0)

---
## 1. Как включить в продакшене
По умолчанию JVM игнорирует аннотацию для пользовательских классов. Чтобы она заработала, добавьте при запуске приложения флаг:
```bash
-XX:-RestrictContended
```
Используйте код с осторожностью.

## 2. Пример кода (False Sharing)
В этом примере переменные `p1` и `p2` находятся в памяти рядом. Из-за этого потоки будут постоянно блокировать кэш-линии друг друга, критически снижая производительность.
```java
import jdk.internal.vm.annotation.Contended;

public class FalseSharingDemo {

    public static class DataHolder {
        // БЕЗ АННОТАЦИИ: переменные попадут в одну кэш-линию (64 байта)
        public volatile long p1 = 0;
        
        // С АННОТАЦИЕЙ: JVM изолирует поле в отдельную кэш-линию
        @Contended
        public volatile long p2 = 0;
    }

    public static void main(String[] args) throws InterruptedException {
        DataHolder holder = new DataHolder();

        // Поток 1 постоянно меняет p1
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < 100_000_000; i++) holder.p1 = i;
        });

        // Поток 2 постоянно меняет p2
        Thread t2 = new Thread(() -> {
            for (long i = 0; i < 100_000_000; i++) holder.p2 = i;
        });

        t1.start(); t2.start();
        t1.join();  t2.join();
    }
}
```

Используйте код с осторожностью.

**Результат:** Если убрать `@Contended` с поля `p2`, этот код выполнится в несколько раз **медленнее** из-за постоянной синхронизации процессорного кэша между ядрами.

---
