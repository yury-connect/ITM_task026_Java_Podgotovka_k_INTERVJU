**Spliterator** в Java — это ==специальный интерфейс (появился в Java 8), предназначенный для обхода и разделения элементов на части==. Главная особенность `Spliterator` (от слов _split_ и _iterator_) — способность делить набор данных на две части, что позволяет эффективно обрабатывать коллекции в параллельных потоках (например, в Stream API). [1](https://habr.com/ru/articles/256905/), [2](https://sobes.tech/en/bank/java/57b1247c-70f1-4071-b988-3fac733526a5), [3](https://xtool.ru/java/set.spliterator%28%29/)

### Главные методы **Spliterator**
	
- **`tryAdvance()`** — аналог метода `next()` у обычного итератора. Выполняет действие над текущим элементом и переходит к следующему, возвращая `true`, если элемент был, и `false`, если элементы кончились.
	
- **`trySplit()`** — разделяет часть своих элементов и возвращает новый `Spliterator` для этой части, чтобы их можно было обрабатывать параллельно.
	
- **`forEachRemaining()`** — последовательно выполняет заданное действие для всех оставшихся элементов.
	
- **`characteristics()`** — возвращает набор свойств источника данных (например, упорядоченный, отсортированный, уникальный, размер и т. д.). [1](https://learn.microsoft.com/ru-ru/dotnet/api/java.util.spliterators.spliterator?view=net-android-35.0), [2](https://sky.pro/wiki/java/raznitsa-mezhdu-iterator-i-iterable-v-java-prakticheskiy-gid/), [3](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)

---
### Отличие от обычного **Iterator**
	
- **Обход:** `Iterator` обходит элементы только по одному и строго последовательно. `Spliterator` умеет делать это и по одному (`tryAdvance`), и сразу группами (`forEachRemaining`).
	
- **Параллельность:** `Iterator` ничего не знает про разделение данных и не подходит для многопоточности. `Spliterator` создан для параллельного выполнения задач, так как умеет «отщипывать» половину коллекции с помощью `trySplit()`. [1](https://habr.com/ru/articles/256905/), [2](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)

---
