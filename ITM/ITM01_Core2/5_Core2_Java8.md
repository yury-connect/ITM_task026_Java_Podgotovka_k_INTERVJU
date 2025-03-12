
---
## Java 8



<details>
        <summary>72. Какие нововведения появились в java 8?</summary>

## Какие нововведения появились в `java 8`?

**🚀 Фундаментальные изменения:**   
* 1️⃣ **Лямбда-выражения** → упрощают работу с анонимными функциями
* 2️⃣ **Ссылки на методы** (`::`) → удобная передача существующих методов
* 3️⃣ **Функциональные интерфейсы** → `Predicate`, `Consumer`, `Supplier`, `Function`
* 4️⃣ `default`-**методы в интерфейсах** → возможность добавлять реализацию

**🛠 Работа с данными и коллекциями:**
* 5️⃣ **Stream API** → удобная обработка коллекций в функциональном стиле
* 6️⃣ **Новый API для работы с датами** → `java.time` вместо `java.util.Date`

**🔧 Дополнительные улучшения:**
* 7️⃣ **Движок Nashorn** → выполнение JavaScript в Java
* 8️⃣ **База64-кодировщик/декодировщик** → Base64
* 9️⃣ **Новые методы** в `Map` → `putIfAbsent()`, `computeIfAbsent()`, `getOrDefault()`
* 🔟 **Metaspace вместо PermGen** → улучшенное управление памятью

Java 8 — **ключевой релиз**, заложивший основу для современного Java-разработки. 🚀

```text
***** из методички *****
1. Полноценная поддержка лямбда-вражений
2. Ссылки на методы ::
3. Функциональные интерфейсы 
4. default методы в интефейсах 
5. Потоки для работы с коллекциями
6. Новое api для работы с датами 
7. Nashorn движок JavaScript, разрабатываемый полностью на Java компанией Oracle.
8. Кодировщик/декодировщик.
9. Новые методы для Map - PutIfAbsent(), СomputeIfAbsent()\СomputeIfPresent(), Remove(), GetOrDefault(), Merge()
10. Metaspace пришла на замену PermGen
```
---
</details>



<details>
        <summary>73. Какие новые классы для работы с датами появились в java 8?</summary>

## Какие новые классы для работы с датами появились в `java 8`?

🕒 Новые классы для работы с датами (Java 8)

**📅 Дата и время без часового пояса:**
* `LocalDate` → только **дата** (год, месяц, день)
* `LocalTime` → только **время** (часы, минуты, секунды)
* `LocalDateTime` → **дата** + **время**

**🌍 Дата и время с часовым поясом:**
* `ZonedDateTime` → учитывает **часовой пояс**

**⏳ Интервалы времени:**
* `Period` → разница между **датами** (_год, месяц, день_)
* `Duration` → разница между **моментами времени** (_часы, минуты, секунды_)

🔹 Новый API (java.time) заменил устаревший java.util.Date и Calendar, сделав работу с датами удобной и безопасной. 🚀

```text
***** из методички *****
LocalDate , LocalTime, LocalDateTime, ZonedDateTime, Period, Duration
```
---
</details>



<details>
        <summary>74. Расскажите про класс Optional</summary>

## Расскажите про класс `Optional`

`Optional<T>` — **контейнер** для значений, который помогает избежать NullPointerException.   
Хранит **либо значение**, **либо пустое состояние** (_`null`-значение безопасно обрабатывается_).

**🛠 Основные методы:**   
* `of(T value)` → создаёт `Optional` с непустым значением
* `ofNullable(T value)` → создаёт `Optional`, допускающий `null`
* `empty()` → создаёт пустой `Optional`
* `isPresent()` → проверяет, есть ли значение
* `ifPresent(Consumer<T>)` → выполняет действие, если значение есть
* `orElse(T other)` → возвращает значение или замену, если `Optional` пуст
* `orElseGet(Supplier<T>)` → возвращает значение или выполняет логику замены
* `orElseThrow()` → выбрасывает исключение, если `Optional` пуст
* `map(Function<T, R>)` → преобразует значение, если оно есть
* `flatMap(Function<T, Optional<R>>)` → аналог map, но возвращает `Optional`

🔹 Использование `Optional` делает код более **безопасным и читаемым**. 🚀

```text
***** из методички *****
Optional - новый класс в пакете java.util, является контейнером (оберткой) для значений которая также может безопасно содержать null. Благодаря опциональным типам можно забыть про проверки на null и NullPointerException.
```
---
</details>



<details>
        <summary>75. Что такое Nashorn?</summary>

## Что такое `Nashorn`?

🔹 Nashorn (Java 8)
**Nashorn** — это **высокопроизводительный `JavaScript`-движок** в Java 8, заменивший `Rhino`.   
Обеспечивает **ускоренную** работу JS-кода, компилируя его **напрямую в байт-код JVM**.

🛠 Основные особенности:
* ✔ **Быстрее Rhino** (_в 2-10 раз за счёт динамического вызова, добавленного в Java 7_)
* ✔ Позволяет **интегрировать JavaScript** в **Java**
* ✔ Поддерживает выполнение `JS`-кода через `jjs` (_`JS`-интерпретатор_)
* ✔ Позволяет использовать `JS` в `Java`-приложениях **без внешних зависимостей**

🔹 **Важно**: **Удалён в Java 15** и заменён на **GraalVM**. 🚀

```text
***** из методички *****
В Java 8, Nashorn, представлен значительно улучшенный движок javascript для замены существующего Rhino. Nashorn обеспечивает в 2-10 раз лучшую производительность, так как он напрямую компилирует код в памяти и передает байт-код в JVM. Nashorn использует функцию динамического вызова, представленную в Java 7, для повышения производительности.
* Nashorn — немецкое слово (Носорог)
```
---
</details>



<details>
        <summary>76. Что такое jjs?</summary>

## Что такое jjs?

`jjs` — это **интерпретатор JavaScript**, встроенный в **JDK 8**.
Позволяет выполнять JS-код **напрямую из командной строки** с использованием движка **Nashorn**.

**🛠 Основные возможности:**
* ✔ Запуск **JavaScript-кода** в терминале
* ✔ Взаимодействие **JS** с **Java-классами**
* ✔ Выполнение **JS**-файлов (`jjs` `script.js`)

🔹 **Важно**: **Удалён** в **Java 15** вместе с **Nashorn**. 🚀

```text
***** из методички *****
Инструмент командной строки для выполнения JavaScript-кодов на консоли.
```
---
</details>



<details>
        <summary>77. Какой класс появился в Java 8 для код./декод. данных?</summary>

## Какой класс появился в `Java 8` для кодирования/декодирования данных?

В **Java 8** появился класс `Base64` (пакет `java.util`), 
который содержит **вложенные классы** для **кодирования** и **декодирования** данных:

* 🔹 **Base64.Encoder** — **кодирует** данные в Base64
* 🔹 **Base64.Decoder** — **декодирует** данные из Base64

**🛠 Основные методы:**
* ✔ `encode()` / `encodeToString()` — кодирование
* ✔ `decode()` — декодирование

**Варианты кодировки:**
* 🔹 **Basic** (`Base64.getEncoder()`) — стандартная
* 🔹 **URL** (`Base64.getUrlEncoder()`) — без + и /
* 🔹 **MIME** (`Base64.getMimeEncoder()`) — с переносами строк

```text
***** из методички *****
public static class Base64.Encoder /public static class Base64.Decoder
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>



<details>
        <summary>Head</summary>

```text
***** из методички *****
```
---
</details>

