**`TransactionTemplate`** — это инструмент Spring для программного управления транзакциями. Он дает больше контроля, чем декларативные аннотации (`@Transactional`), но проще, чем низкоуровневая работа с `PlatformTransactionManager`.

### Как это работает:
1. **Создание:** Ты получаешь бин `TransactionTemplate` из контекста Spring.    
2. **Выполнение:** Вызываешь его метод `execute()`, передавая внутрь свою логику.    
3. **Автоматизация:** Шаблон сам открывает, коммитит или откатывает транзакцию (при возникновении исключения).    

### Пример кода:
```java
@Service
public class UserService {
    private final TransactionTemplate transactionTemplate;
    private final UserRepository userRepository;
    public UserService(TransactionTemplate transactionTemplate, 
                      UserRepository userRepository) {
        this.transactionTemplate = transactionTemplate;
        this.userRepository = userRepository;
    }
    public void complexOperation() {
        transactionTemplate.execute(status -> {
            // Код внутри транзакции
            userRepository.save(new User("John"));
            
            // Если случится RuntimeException — будет rollback
            // Если хочешь принудительно откатить:
            // status.setRollbackOnly();
            
            return null; // или верни результат
        });
    }
}
```

### Зачем нужен?
- **Тонкий контроль:** Нужно управлять транзакцией внутри метода (например, в циклах или сложных условиях).    
- **Несколько транзакций:** Один метод может содержать несколько блоков `execute` с разными настройками.    
- **Вызов из этого же класса:** В отличие от `@Transactional`, работает без прокси (проблемы с `this`).

---
