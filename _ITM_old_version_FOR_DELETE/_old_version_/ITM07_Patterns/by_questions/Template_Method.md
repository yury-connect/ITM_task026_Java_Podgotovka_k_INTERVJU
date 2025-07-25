# Расскажите про паттерн Шаблонный метод (`Template Method`).

---
## 🎯 `Template Method`: Суть
**Поведенческий паттерн**, который:

* Определяет **скелет алгоритма** в базовом классе.

* Разрешает подклассам **переопределять** отдельные шаги, не меняя структуру алгоритма.

---
## 📌 `Template Method`: Глубокий разбор

### 1. Зачем нужен?

* **Проблема:**  
   Повторяющийся код в алгоритмах с незначительными вариациями:

```java
// Page1: showHeader() → "Content1" → showFooter()  
// Page2: showHeader() → "Content2" → showFooter()  

```

* **Решение:**  
Вынести общие шаги (`showHeader()`, `showFooter()`) в **базовый класс**, 
а уникальные (`showPageContent()`) — в подклассы.

### 2. Компоненты  

| Компонент                                         | 	Роль                                                                                                |
|:--------------------------------------------------|:-----------------------------------------------------------------------------------------------------|
| **Абстрактный класс** (`WebsiteTemplate`)         | Определяет шаблонный метод (`showPage()`) и абстрактные/переопределяемые шаги (`showPageContent()`). |
| **Конкретный класс** (`WelcomePage`, `NewsPage`)  | Реализует специфичные шаги алгоритма.                                                                |

### 3. Плюсы  
   ✔ **Исключает дублирование**: Общий код — в базовом классе.  
   ✔ **Гибкость**: Подклассы меняют только нужные шаги.  
   ✔ **Контроль**: Базовый класс управляет структурой алгоритма.  

### 4. Минусы  
   ❌ **Ограниченность**: Жёсткая привязка к структуре базового класса.  
   ❌ **Сложность**: Может усложнить иерархию наследования.  

### 5. Когда использовать?  
* Есть несколько классов с почти идентичными алгоритмами.

* Нужно **зафиксировать порядок шагов** алгоритма.

* Требуется **расширяемость** отдельных шагов.

### 6. Пример кода

<details>
        <summary>пример 🔽</summary>

```java
public abstract class WebsiteTemplate {  
    // Шаблонный метод (фиксирует структуру)  
    public void showPage() {  
        System.out.println("Header");  // Общий шаг  
        showPageContent();             // Переопределяемый шаг  
        System.out.println("Footer");  // Общий шаг  
    }
    
    public abstract void showPageContent(); // Уникальный для подклассов  
}

public class WelcomePage extends WebsiteTemplate {  
    @Override  
    public void showPageContent() {  
        System.out.println("Welcome"); // Специфичный контент  
    }  
}

// Использование:  
WebsiteTemplate page = new WelcomePage();  
page.showPage(); // Выведет: Header → Welcome → Footer
```
</details>


### 7. Отличие от других паттернов

| Паттерн          | 	Отличие                                                    |
|:-----------------|:------------------------------------------------------------|
| `Strategy`       | 	Инкапсулирует алгоритмы в отдельные классы (_композиция_). |
| `Factory Method` | 	Создание объектов, а не алгоритмов.                        |

> **⚡ Итог:**  
> Используйте `Template Method`, когда:
> * Алгоритм имеет неизменную структуру, но отдельные шаги могут варьироваться.
> * Нужно переиспользовать общую логику в семействе классов.

**Примеры в Java:**
  * `java.io.InputStream` (_`read()` — абстрактный, `read(byte[] b)` — шаблонный_).
  * Фреймворки (_Spring, JUnit_) для стандартизации жизненного цикла (_например, `@BeforeEach`, `@Test`_).



<details>
        <summary>📝 Материал из методички 🔽</summary>

```text
Поведенческий паттерн проектирования,это метод, внутренние блоки которого 
могут переопределяться подклассами для избежания повторного копирования 
(который пошагово определяет алгоритм и позволяет наследникам переопределять 
некоторые шаги алгоритма, не изменяя его структуру в целом).

Паттерн предлагает разбить алгоритм на последовательность шагов, 
описать эти шаги в отдельных методах и вызывать их в одном шаблонном методе друг за другом. 
Для описания шагов используется абстрактный класс. 
Общие шаги можно будет описать прямо в абстрактном класе. 
Это позволит подклассам переопределять некоторые шаги алгоритма, 
оставляя без изменений его структуру и остальные шаги, которые для этого подкласса не так важны.
```
</details>

---

[🔙 _к списку вопросов по теме_ **Patterns** 🔙](/_ITM_old_version_FOR_DELETE/ITM07_Patterns/patterns.md)
