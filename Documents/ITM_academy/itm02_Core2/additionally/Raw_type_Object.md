## Разница между `Collection<?>` и `Collection<Object>`:

1. **`Collection<?>`** (коллекция с **неограниченным wildcard**) — может содержать элементы **любого типа**, но **нельзя добавлять** новые элементы (кроме `null`).
    
2. **`Collection<Object>`** — может содержать **только `Object`**, но **можно добавлять** любые объекты (так как все классы наследуются от `Object`).
    
### Пример:
```java
Collection<?> wildcardCollection = new ArrayList<String>(); // OK
wildcardCollection.add("text"); // Ошибка компиляции!

Collection<Object> objectCollection = new ArrayList<>();
objectCollection.add("text"); // OK
```


**Вывод:**
- `<?>` — для **чтения** элементов неизвестного типа.    
- `<Object>` — для работы с **любыми объектами**, но с возможностью **добавления**.	

---
