## 5/20: Which TWO of the following statements are true about finalization?

- The method finalize() is defined in the class java.lang.Thread    
- The method finalize() will be called only once for an object.    
- The Garbage Collector calls the finalize() method before destroying the object.    
- The finalize() method should not thrown an exception.    

---
## 5/20: Какие ДВА из следующих утверждений верны относительно финализации (finalization)?

- Метод finalize() определен в классе java.lang.Thread    
- Метод finalize() будет вызван для объекта только один раз.    
- Сборщик мусора (Garbage Collector) вызывает метод finalize() перед уничтожением объекта.    
- Метод finalize() не должен выбрасывать исключение.

---
#### Правильные ответы (нужно выбрать два):  

**1. The method finalize() will be called only once for an object.** (второй вариант)  
**2. The Garbage Collector calls the finalize() method before destroying the object.** (третий вариант)

#### **Объяснение:**

Финализация в Java — это процесс, при котором сборщик мусора (Garbage Collector) вызывает метод `finalize()` перед тем, как окончательно удалить объект из памяти.

1. **`The method finalize() will be called only once for an object.` — ВЕРНО.** Спецификация Java гарантирует, что метод `finalize()` для конкретного объекта будет вызван сборщиком мусора не более одного раза. Даже если внутри метода `finalize()` объект "воскресят" (сохранят на него ссылку), при следующем удалении метод `finalize()` для него вызываться уже не будет.
    
2. **`The Garbage Collector calls the finalize() method before destroying the object.` — ВЕРНО.** Это и есть основное назначение метода `finalize()` — дать объекту последний шанс освободить ресурсы (например, закрыть файлы или сетевые соединения) до того, как память, занимаемая объектом, будет очищена.    

#### Разбор остальных вариантов:

- `The method finalize() is defined in the class java.lang.Thread` — **НЕВЕРНО.** Метод `finalize()` определен в корневом классе `java.lang.Object`, а не в классе `Thread`. Все классы наследуют его от `Object`.
    
- `The finalize() method should not thrown an exception.` — **НЕВЕРНО.** Метод `finalize()` может выбрасывать исключения (он объявлен как `throws Throwable`). Однако, если он выбросит исключение, это исключение будет проигнорировано сборщиком мусора, и процесс удаления объекта продолжится. Тем не менее, синтаксически выбрасывать исключения из него можно, но крайне не рекомендуется. Утверждение "should not" (не должен) — это совет по проектированию, а не строгое правило языка, которое тестируется в таких вопросах. Кроме того, в тестах по Java чаще всего правильными считаются утверждения про однократный вызов и про вызов перед уничтожением.

---
