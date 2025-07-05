# Расскажите про паттерн Заместитель (`Proxy`).

---
## 🎯 `Proxy`: Суть

**Структурный паттерн**, который:


* Создает **объект-заместитель**, контролирующий доступ к реальному объекту.


* Перехватывает вызовы, добавляя **дополнительную логику** (_кеширование, проверки, ленивую инициализацию_).


* Сохраняет **интерфейс** исходного объекта, оставаясь прозрачным для клиента.

> Предоставляет **объект - заместитель**, который контролирует доступ к реальному/ базовому объекту.
> _(оптимизируя исп. ресурсов или обеспечивая безопасность)_ 

---
## 📌 `Proxy`: Глубокий разбор

### 1. Зачем нужен?
* **Проблема:**  
   Прямой доступ к объекту неудобен или небезопасен:

```java
Database realDB = new RealDatabase();  
realDB.query("SELECT * FROM users"); // ❌ Нет контроля доступа/кеширования  
```

* **Решение:**

```java
Database proxyDB = new DatabaseProxy();  
proxyDB.query("SELECT * FROM users"); // ✅ Контроль + кеширование
```

### 2. Компоненты

| Компонент                            | 	Роль                                                                    |
|:-------------------------------------|:-------------------------------------------------------------------------|
| **Субъект** (`Database`)             | 	Интерфейс, общий для реального объекта и заместителя.                   |
| **Реальный объект** (`RealDatabase`) | 	Класс, выполняющий основную работу.                                     |
| **Заместитель** (`DatabaseProxy`)    | 	Контролирует доступ к реальному объекту, добавляя промежуточную логику. |

### 3. Типы прокси
1. **Виртуальный** (_ленивая инициализация_):

<details>
        <summary>пример кода 🔽</summary>

```java
public class ImageProxy implements Image {
   private RealImage realImage;
   @Override
   public void display() {
      if (realImage == null) {
         realImage = new RealImage(); // Создаём только при необходимости  
      }
      realImage.display();
   }
}  
```
</details>



2. **Защитный** (_контроль доступа_):

<details>
        <summary>пример кода 🔽</summary>

```java
public class SecureDatabaseProxy implements Database {
   private RealDatabase realDB;
   private String userRole;

   public SecureDatabaseProxy(String userRole) {
      this.userRole = userRole;
   }

   @Override
   public void query(String sql) {
      if (userRole.equals("ADMIN")) {
         realDB.query(sql); // Только для админов  
      }
   }
}
```
</details>



3. **Кеширующий:**

<details>
        <summary>пример кода 🔽</summary>

```java
public class CachedYouTubeProxy implements YouTubeService {
   private YouTubeService realService;
   private Map<String, Video> cache = new HashMap<>();

   @Override
   public Video getVideo(String id) {
      if (!cache.containsKey(id)) {
         cache.put(id, realService.getVideo(id)); // Сохраняем в кеш  
      }
      return cache.get(id);
   }
}
```
</details>



### 4. Плюсы  
   ✔ **Контроль доступа**: Защита, логирование, кеширование.  
   ✔ **Ленивая инициализация**: Создание ресурсоёмких объектов по требованию.  
   ✔ **Прозрачность**: Клиент не знает о существовании прокси.  

### 5. Минусы  
   ❌ **Задержки**: Дополнительные проверки/операции увеличивают время отклика.  
   ❌ **Сложность**: Введение дополнительных классов.  

### 6. Когда использовать?
* Нужен **удалённый доступ** (_RPC, REST-клиенты_).  
* Требуется **кеширование** или **логирование** вызовов.
* Необходимо **контролировать** доступ к объекту (права, безопасность).

### 7. Пример

<details>
        <summary>пример 🔽</summary>

```java
   interface Database {  
   void query(String sql);  
   }

class RealDatabase implements Database {  
@Override  
public void query(String sql) {  
System.out.println("Executing: " + sql);  
}  
}

class DatabaseProxy implements Database {  
private RealDatabase realDB;

    @Override  
    public void query(String sql) {  
        if (realDB == null) {  
            realDB = new RealDatabase(); // Ленивая инициализация  
        }  
        System.out.println("Log query: " + sql); // Логирование  
        realDB.query(sql);  
    }  
}

// Использование:  
Database db = new DatabaseProxy();  
db.query("SELECT * FROM users");
```

</details>

### 8. Отличие от _других_ паттернов

| Паттерн     | 	Отличие                                               |
|:------------|:-------------------------------------------------------|
| `Decorator` | 	Добавляет функциональность, а не контролирует доступ. |
| `Facade`    | 	Упрощает сложную систему, а не перехватывает вызовы.  |

> **⚡ Итог:**  
> Используйте `Proxy` для:  
> * **Оптимизации** (_кеш, ленивая загрузка_).  
> * **Безопасности** (_проверка прав_).  
> * **Мониторинга** (_логирование, статистика_). 

**Примеры в Java:**
* `java.lang.reflect.Proxy` (динамические прокси).
* Hibernate (_ленивая загрузка сущностей_).
* Spring AOP (_аспекты для логирования/транзакций_).



<details>
        <summary>📝 Материал из методички 🔽</summary>

```text
Структурный паттерн проектирования, который позволяет подставлять вместо реальных объектов 
специальные объекты-заменители, которые перехватывают вызовы к оригинальному объекту, 
позволяя сделать что-то до или после передачи вызова оригиналу.

Заместитель предлагает создать новый класс-дублёр, имеющий тот же интерфейс, 
что и оригинальный служебный объект. 
При получении запроса от клиента объект-заместитель сам бы создавал экземпляр служебного объекта, 
выполняя промежуточную логику, которая выполнялась бы до (или после) вызовов этих же методов 
в настоящем объекте.

+: Позволяет контролировать сервисный объект незаметно для клиента.
- : Увеличивает время отклика от сервиса.
```
</details>

---

[🔙 _к списку вопросов по теме_ **Patterns** 🔙](/_ITM_old_version_FOR_DELETE/ITM07_Patterns/patterns.md)
