# **Что выведет и почему?**
```java
import java.util.*;

class User {
    private final String id;
    User(String id) { this.id = id; }
}

public class Demo {
    public static void main(String[] args) {
        Map<User, String> m = new HashMap<>();
        m.put(new User("42"), "ok");
        System.out.println(m.get(new User("42")));
    }
}
```

---
### **Ответ:**
Выведет `null`.
### **Почему?**
1. **Ключи в `HashMap` сравниваются по `equals()` и `hashCode()`.**    
    - В классе `User` **не переопределены** методы `equals()` и `hashCode()`.        
    - Значит, используется реализация по умолчанию из `Object`:        
        - `equals()` сравнивает **ссылки** (объекты равны, только если это один и тот же объект в памяти).            
        - `hashCode()` возвращает случайный *(для разных объектов)* идентификатор, обычно основанный на адресе памяти.
    
2. **Что происходит в коде:**    
    - `m.put(new User("42"), "ok")` — создаётся **объект №1** (User@123), кладётся в мапу.        
    - `m.get(new User("42"))` — создаётся **объект №2** (User@456), совершенно новый, с другим адресом.        
    - Для поиска ключа HashMap вычислит `hashCode()` объекта №2, затем по нему найдёт корзину, а внутри попытается найти объект, равный объекту №2 по `equals()`.        
    - Но `equals()` вернёт `false`, т.к. объекты разные (даже если поля `id` одинаковые).
    
3. **Итог:** совпадение не найдено → возвращается `null`.    

### **Как исправить, чтобы работало ожидаемо:**
Нужно переопределить `equals()` и `hashCode()` в классе `User`:
```java
class User {
    private final String id;
    
    User(String id) { this.id = id; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
```

Теперь два `User` с одинаковым `id` будут считаться равными, и `get()` вернёт `"ok"`.

---
[Перейти к списку заданий](ЗАДАНИЯ.md)
