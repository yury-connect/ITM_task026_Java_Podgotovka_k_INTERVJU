Вот наглядные примеры нарушений каждого из 5 принципов **SOLID** на языке Java.

---
### 1. **S** — Single Responsibility Principle <br>(*Принцип единственной ответственности*)

> **Идея:** У класса должна быть только одна причина для изменения.

❌ **Нарушение:** Класс `OrderProcessor` делает всё подряд — считает цену, сохраняет данные в БД и отправляет Email.
```Java
public class OrderProcessor {
    public void processOrder(Order order) {
        // 1. Логика расчета стоимости
        double total = order.getItems().stream()
	        .mapToDouble(Item::getPrice).sum();
        
        // 2. Логика работы с БД
        String sql = "INSERT INTO orders VALUES (" + order.getId() 
	        + ", " + total + ")";
        database.execute(sql);
        
        // 3. Логика отправки уведомлений
        EmailClient client = new EmailClient();
        client.sendEmail(order.getUserEmail(), "Заказ оформлен!");
    }
}
```
- **Почему плохо:** Если изменится формат SQL-запросов, логика скидок или сервис отправки писем — придется менять один и тот же класс.

### 2. **O** — Open/Closed Principle <br>(*Принцип открытости/закрытости*)

> **Идея:** Классы должны быть открыты для расширения, но закрыты для изменения.

❌ **Нарушение:** Расчет скидки через `if-else` или `switch`.
```Java
public class DiscountCalculator {
    public double calculateDiscount(String customerType, double price) {
        if ("REGULAR".equals(customerType)) {
            return price * 0.05;
        } else if ("VIP".equals(customerType)) {
            return price * 0.15;
        } else if ("PREMIUM".equals(customerType)) { 
	        // Добавили новый тип — изменили класс
            return price * 0.25;
        }
        return 0;
    }
}
```
- **Почему плохо:** Чтобы добавить новый тип клиента (например, `PARTNER`), нужно модифицировать сам класс `DiscountCalculator`, что может сломать уже работающий код. (Правильно было сделать интерфейс `DiscountStrategy` с реализациями).

### 3. **L** — Liskov Substitution Principle <br>(*Принцип подстановки Барбары Лисков*)

> **Идея:** Подклассы должны дополнять, а не замещать поведение базовых классов. Объект дочернего класса должен легко заменять объект родительского класса, не ломая программу.

❌ **Нарушение:** Класс `Square` наследуется от `Rectangle`, но ломает логику родителя.
```Java
public class Rectangle {
    protected int width;
    protected int height;
	
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public int getArea() { return width * height; }
}

public class Square extends Rectangle {
    @Override
    public void setWidth(int width) {
        this.width = width;
        this.height = width; // Принудительно меняет и высоту!
    }
	
    @Override
    public void setHeight(int height) {
        this.width = height;
        this.height = height;
    }
}
```
- **Почему плохо:** Если функция принимает `Rectangle` и рассчитывает, что при изменении ширины высота останется прежней — передача `Square` сломает вычисления:
```Java
Rectangle rect = new Square();
rect.setWidth(5);
rect.setHeight(10);
// Ожидаем площадь 50, но получаем 100!
```

### 4. **I** — Interface Segregation Principle <br>(*Принцип разделения интерфейса*)

> **Идея:** Клиенты не должны зависеть от методов, которые они не используют. Лучше много специализированных интерфейсов, чем один универсальный.

❌ **Нарушение:** Огромный интерфейс («жирный» интерфейс), заставляющий реализовывать ненужные методы.
```Java
public interface Worker {
    void work();
    void eat();
    void sleep();
}

public class RobotWorker implements Worker {
    @Override
    public void work() { /* Робот работает */ }

    @Override
    public void eat() {
        // Роботы не едят! Приходится бросать исключение или оставлять пустым
        throw new UnsupportedOperationException("Роботы не едят");
    }

    @Override
    public void sleep() {
        throw new UnsupportedOperationException("Роботы не спят");
    }
}
```
- **Почему плохо:** Класс `RobotWorker` вынужден завязываться на методы `eat()` и `sleep()`. (Правильно разбить на `Workable`, `Eatable`, `Sleepable`).

### 5. **D** — Dependency Inversion Principle <br>(*Принцип инверсии зависимостей*)

> **Идея:** Модули верхнего уровня не должны зависеть от модулей нижнего уровня. Оба должны зависеть от абстракций.

❌ **Нарушение:** Высокоуровневый сервис напрямую создает конкретный жестко завязаный низкоуровневый объект (через `new`).
```Java
public class NotificationService {
    // Прямая жесткая зависимость от конкретной реализации MySQLDatabase
    private MySQLDatabase database = new MySQLDatabase();

    public void sendNotification(String message) {
        database.save(message);
        System.out.println("Уведомление отправлено: " + message);
    }
}
```
- **Почему плохо:** Вы не сможете подменить `MySQLDatabase` на `PostgreSQLDatabase` или написать unit-тест с MOCK-базой данных, не переписав класс `NotificationService`. (Правильно внедрять абстракцию `Database` через конструктор).

---
