**LongAdder** — это класс в Java из пакета `java.util.concurrent.atomic`, который предназначен для эффективного суммирования чисел (`long`) в условиях высокой многопоточности. [1](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/LongAdder.html), [2](https://t.me/s/javalib?before=5262)

### Как он работает
- **Разделение ячеек:** Вместо одной переменной (как в `AtomicLong`), за которую борются все потоки, `LongAdder` использует массив независимых ячеек (`variables`).
	
- **Снижение конкуренции:** При высокой нагрузке потоки записывают данные в разные ячейки, что уменьшает задержки (contention).
	
- **Итоговая сумма:** Метод `sum()` собирает значения из всех ячеек и возвращает общий результат. [1](https://www.baeldung.com/java-longadder-and-longaccumulator)

### Основные методы
- `increment()` — увеличивает значение на 1.
- `add(long x)` — добавляет указанное число.
- `sum()` — возвращает текущую сумму всех ячеек.
- `reset()` — сбрасывает счетчик в ноль. [1](https://metanit.com/java/tutorial/8.17.php), [2](https://learn.microsoft.com/ru-ru/dotnet/api/java.util.concurrent.atomic.longadder.sum?view=net-android-35.0&viewFallbackFrom=xamarin-android-sdk-12)

### Когда использовать
- **Высокая нагрузка:** Когда много потоков одновременно обновляют счетчик (например, логгирование, статистика, подсчет запросов).
	  
- **Не важен порядок:** Если вам не нужно получать точное атомарное значение после каждой отдельной операции, а важна общая производительность.
	  
- **Замена AtomicLong:** `LongAdder` работает быстрее `AtomicLong` при частой конкуренции потоков. [1](https://tproger.ru/translations/java8-concurrency-tutorial-3), [2](https://metanit.com/java/tutorial/8.17.php), [3](https://t.me/s/javalib?before=5262), [4](https://habr.com/ru/companies/otus/articles/353414/)

---
