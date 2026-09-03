## `HashMap` vs `HashTable` — простыми словами

---
## 🔑 Главное отличие

| Характеристика          | HashMap                           | HashTable                              |
| ----------------------- | --------------------------------- | -------------------------------------- |
| **Синхронизация**       | ❌ Нет                             | ✅ Да (*все методы синхронизированы*)   |
| **Null ключи/значения** | ✅ Да                              | ❌ Нет (*бросает NullPointerException*) |
| **Производительность**  | Высокая                           | Низкая (*из-за синхронизации*)         |
| **Наследие**            | Java 1.2 (*Collection Framework*) | Java 1.0 (*устаревший класс*)          |
| **Итерация**            | Iterator (*fail-fast*)            | Iterator + Enumeration (*fail-safe*)   |

---
## 📜 Историческая справка

**HashTable** появился в **Java 1.0** — ещё до появления `Collection Framework`.

**HashMap** появился в **Java 1.2** как часть нового `Collection Framework` и сразу был спроектирован без синхронизации (*для производительности*).

---
## 🔍 Детали

#### 1. Синхронизация
```java
// HashTable — все методы синхронизированы
public synchronized V put(K key, V value) { ... }
public synchronized V get(Object key) { ... }

// HashMap — без синхронизации
public V put(K key, V value) { ... }
public V get(Object key) { ... }
```

#### 2. `Null` ключи / значения
```java
// HashMap — разрешает null
HashMap<String, String> map = new HashMap<>();
map.put(null, "value");   // ✅ OK
map.put("key", null);     // ✅ OK

// HashTable — НЕ разрешает null
Hashtable<String, String> table = new Hashtable<>();
table.put(null, "value"); // ❌ NullPointerException
table.put("key", null);   // ❌ NullPointerException
```

#### 3. Итераторы
```java
// HashMap — fail-fast (бросает ConcurrentModificationException при изменении)
Iterator<String> it = map.keySet().iterator();
map.put("new", "value");  // ❌ ConcurrentModificationException

// HashTable — можно использовать Enumeration (не бросает)
Enumeration<String> en = table.keys();
table.put("new", "value"); // ✅ OK (но не рекомендуется)
```

---
### 📊 Сравнительная таблица

| Критерий               | HashMap              | HashTable           |
| ---------------------- | -------------------- | ------------------- |
| **Потокобезопасность** | ❌ Нет                | ✅ Да                |
| **`Null` ключи**       | ✅ Да                 | ❌ Нет (NPE)         |
| **`Null` значения**    | ✅ Да                 | ❌ Нет (NPE)         |
| **Синхронизация**      | ❌ Нет (ручная)       | ✅ Да (встроенная)   |
| **Производительность** | ⚡ Высокая            | 🐢 Низкая           |
| **Версия Java**        | 1.2+                 | 1.0+ (устаревший)   |
| **Fail-fast итератор** | ✅ Да                 | ❌ Нет (Enumeration) |
| **Рекомендуется**      | ✅ Да (в однопоточке) | ❌ Нет (устарел)     |

---
### 💡 Что использовать сегодня?

|Сценарий|Решение|
|---|---|
|**Однопоточный код**|`HashMap`|
|**Многопоточный код (высокая нагрузка)**|`ConcurrentHashMap`|
|**Многопоточный код (низкая нагрузка)**|`Collections.synchronizedMap(new HashMap<>())`|
|**HashTable**|❌ **Не использовать** (устарел)|

---
### 📝 Итог
```text
HashTable — устаревший класс из Java 1.0:
✅ Синхронизирован
❌ Медленный
❌ Не разрешает null
❌ Enumeration вместо Iterator

HashMap — современный класс из Java 1.2:
✅ Быстрый
✅ Разрешает null
✅ Iterator (fail-fast)
❌ Не синхронизирован

```
Если нужна многопоточность → ConcurrentHashMap, а не HashTable!

---
