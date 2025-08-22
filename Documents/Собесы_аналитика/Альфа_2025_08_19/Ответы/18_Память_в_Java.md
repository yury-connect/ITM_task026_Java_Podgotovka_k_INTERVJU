# **Кратко** — главное (*1–2 предложения*)

Heap и Stack настраиваются JVM-флагами: `-Xms/-Xmx` для кучи, `-Xss` для стека потока; значения по умолчанию JVM выбирает эргономикой платформы, а в контейнерах есть специальные флаги (`-XX:MaxRAMPercentage` и т.п.). [Oracle Documentation](https://docs.oracle.com/javase/9/gctuning/ergonomics.htm?utm_source=chatgpt.com)[Datadog](https://www.datadoghq.com/blog/java-on-containers/?utm_source=chatgpt.com)

---
# 1) **Heap** — какие флаги и что задают

- `-Xms<size>` — **initial heap** (размер при старте).    
- `-Xmx<size>` — **max heap** (лимит кучи).    
- `-XX:MaxRAMPercentage=<percent>` / `-XX:InitialRAMPercentage` — в контейнерах удобнее задавать как процент от доступной памяти. [Datadog](https://www.datadoghq.com/blog/java-on-containers/?utm_source=chatgpt.com)[Stack Overflow](https://stackoverflow.com/questions/29923531/how-to-set-java-heap-size-xms-xmx-inside-docker-container?utm_source=chatgpt.com)    
- **Совет**: в production часто ставят `-Xms = -Xmx` (фиксированный heap) — чтобы избежать расходов на динамическое увеличение/сжатие и «resize pauses». [Discuss the Elastic Stack](https://discuss.elastic.co/t/initial-heap-size-199229440-not-equal-to-maximum-heap-size-3187671040-this-can-cause-resize-pauses-and-prevents-mlockall-from-locking-the-entire-heap/142282?utm_source=chatgpt.com)    

**От чего зависят значения по умолчанию**  
JVM использует _ergonomics_ — смотрит на платформу/физическую память и выбирает старт/макс-heap автоматически, если флаги не заданы. Поведение зависит от JVM-версии и среды (bare metal vs container). [Oracle Documentation](https://docs.oracle.com/javase/9/gctuning/ergonomics.htm?utm_source=chatgpt.com)

---
# 2) **Stack** (*стек потока*) — флаги и дефолты

- `-Xss<size>` — задаёт размер стека для каждого Java-потока.    
- По умолчанию на **64-bit** HotSpot обычно **~1MB** (на 32-bit и на разных ОС — другие значения; см. доки). Если много потоков — уменьшайте `-Xss`, но осторожно (риск `StackOverflowError`). [Oracle Documentation](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html?utm_source=chatgpt.com)    

**Пример расчёта числа потоков (ориентир):**  
`maxThreads ≈ available_native_memory / Xss`.  
Если Xss=1MB и у вас 4 GB свободной памяти → теоретически ~4000 потоков (но учтите остальные потребители памяти).

---
# 3) **Metaspace** / **CodeCache** / **Direct memory**

- `-XX:MaxMetaspaceSize=<size>` — ограничивает Metaspace (метаданные классов). По умолчанию Metaspace растёт динамически из native memory.
    
- `-XX:MaxDirectMemorySize=<size>` — лимит для `DirectByteBuffer`/off-heap. Если не задан — равен `-Xmx` в старых JVM, но лучше выставлять явно при активном off-heap.
    
- `-XX:ReservedCodeCacheSize` / `-XX:InitialCodeCacheSize` — Code Cache для JIT. Большие приложения (много JIT) могут требовать увеличения. [openjdk.org](https://openjdk.org/groups/hotspot/docs/RuntimeOverview.html?utm_source=chatgpt.com)  

---
# 4) Специально про контейнеры <br>(`Kubernetes`/`Docker`)

- Раньше JVM не учитывала cgroup, сейчас — JDK11+ учитывают; но удобнее контролировать через `-XX:MaxRAMPercentage`/`-XX:InitialRAMPercentage`. Практика: выделять heap примерно 60–80% от container memory, остальное оставить под Metaspace, direct memory, native и ОС. [Datadog](https://www.datadoghq.com/blog/java-on-containers/?utm_source=chatgpt.com)[Jfokus](https://www.jfokus.se/jfokus22-preso/The-Diabolical-Developers-Guide-to-JVM-Ergonomics-in-Containers.pdf?utm_source=chatgpt.com)

**Пример:** Pod limit = 4GB → `-Xmx=3g` или `-XX:MaxRAMPercentage=75.0` (и `-XX:InitialRAMPercentage=50.0` по необходимости).

---
# 5) Практические рекомендации <br>(*шаблон для продакшна*)

1. _Измерить_ текущие потребления (JFR, jstat, jcmd) — тюнинг по фактам.
    
2. Для production: `-Xms = -Xmx = <подходящий размер>` (стабильность). [Discuss the Elastic Stack](https://discuss.elastic.co/t/initial-heap-size-199229440-not-equal-to-maximum-heap-size-3187671040-this-can-cause-resize-pauses-and-prevents-mlockall-from-locking-the-entire-heap/142282?utm_source=chatgpt.com)
    
3. Оставить 20–40% памяти на off-heap/OS/CodeCache/Metaspace при расчёте Xmx.
    
4. Ядро GC: для общих целей используйте **G1** (или ZGC для низколатентных систем) и задавайте целевую паузу `-XX:MaxGCPauseMillis=<ms>`.
    
5. Для большого количества потоков уменьшите `-Xss`, но проверяйте стек на рекурсию.
    
6. Укажите `-XX:MaxDirectMemorySize` если используете Netty/Direct buffers. [openjdk.org](https://openjdk.org/groups/hotspot/docs/RuntimeOverview.html?utm_source=chatgpt.com)    

---
# 6) Примеры флагов (*шаблоны*)

**Производственный сервер (4 GB контейнер):**

`# фиксированная куча 3GB, G1, целевая пауза 200ms, метаспейс лимит 256M -Xms3g -Xmx3g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:MaxMetaspaceSize=256m -XX:MaxDirectMemorySize=512m`

**Контейнер-ориентированный вариант (динамически):**

`-XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0 -XX:+UseG1GC`

(тогда JVM сама выставит `-Xmx` как процент от доступной памяти). [Datadog](https://www.datadoghq.com/blog/java-on-containers/?utm_source=chatgpt.com)[Stack Overflow](https://stackoverflow.com/questions/29923531/how-to-set-java-heap-size-xms-xmx-inside-docker-container?utm_source=chatgpt.com)

---
# 7) Как понять, что вы выбрали неправильно (симптомы)

- `OutOfMemoryError: Java heap space` → Xmx слишком мал / утечка.
    
- Частые длинные GC-паузы → Xmx мал или GC неправильно настроен.
    
- `OutOfMemoryError: Metaspace` → classloader-leak или Metaspace лимит.
    
- `Unable to create new native thread` → стека или системных лимитов не хватает (много потоков).    

---
# 8) Быстрые команды для диагностики / проверки флагов

- Показать фактические флаги JVM:    
	
`java -XX:+PrintFlagsFinal -version | egrep 'MaxHeapSize|MaxRAM|ThreadStackSize|MaxMetaspaceSize'`
	
- Узнать текущие значения heap/GC (в рантайме): `jcmd <pid> VM.flags`, `jstat -gc <pid> 1s`, `jcmd <pid> GC.heap_info`.
    
- Heap dump: `jmap -dump:live,format=b,file=heap.hprof <pid>` (анализ в MAT).    

---
# 9) **Короткий ответ для собеседования** <br>(*2–3 строки*)

> «Heap настраивается `-Xms/-Xmx` (JVM выбирает умные дефолты через ergonomics), стек на поток — `-Xss` (по умолчанию ≈1MB на 64-бит), Metaspace и direct memory — отдельные нативные области; в контейнерах удобнее управлять через `-XX:MaxRAMPercentage`. В проде обычно фиксируем `-Xms=Xmx` и оставляем буфер под off-heap/Metaspace.» [Oracle Documentation+1](https://docs.oracle.com/javase/9/gctuning/ergonomics.htm?utm_source=chatgpt.com)[Datadog](https://www.datadoghq.com/blog/java-on-containers/?utm_source=chatgpt.com)

---
