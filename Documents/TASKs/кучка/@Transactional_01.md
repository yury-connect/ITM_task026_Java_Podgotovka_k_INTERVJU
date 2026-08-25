Будет-ли сохранено?
```java
@Transactional
public void testSaveWithErrorHandling() {
    try {
        User user = new User("Алексей", 25);
        session.save(user);
        
        throw new RuntimeException("Ошибка!");
    } catch (Exception e) {
        System.out.println("Ошибка перехвачена, но мы ее съели");
    }
}
```



```java
@Transactional
public void testSaveWithErrorHandling() {
    try {
        User user = new User("Алексей", 25);
        session.save(user); // Объект в кэше
        
        throw new RuntimeException("Ошибка!"); // Исключение
    } catch (Exception e) {
        // Проглотили
        System.out.println("Ошибка перехвачена, но мы ее съели");
    }
    // Метод завершается
    // Spring вызовет commit() -> flush() -> INSERT выполнится!
}
```
**В БД появится новый пользователь!** Потому что:
1. Ошибка перехвачена.    
2. Транзакция не откатилась.    
3. `flush()` при коммите выполнил INSERT.

