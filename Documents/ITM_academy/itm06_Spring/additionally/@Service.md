`@Service` — это аннотация-стереотип *(как и `@Component`, `@Repository`)*, которая отмечает класс как **сервисный слой приложения** *(слой бизнес-логики)*.

### 1. Основная суть
Технически `@Service` — это та же `@Component`. Spring автоматически обнаруживает такие классы при сканировании пакетов и создает их бины.

Однако семантически она используется для одного конкретного слоя:
- `@Repository` → слой работы с БД (DAO)    
- `@Controller` / `@RestController` → слой контроллеров (API)    
- `@Service` → **слой бизнес-логики**    

### 2. Что обычно пишут в `@Service`?
В сервисном слое инкапсулируется логика приложения. Здесь не должно быть прямых HTTP-запросов (это в контроллере) и прямых SQL-запросов (это в репозитории).
```java
@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Transactional
    public User registerUser(UserDto dto) {
        // Здесь бизнес-логика: проверки, хеширование пароля, вызовы других сервисов
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Email занят");
        }
        User user = new User(dto.getName(), dto.getEmail());
        user.setPassword(encodePassword(dto.getPassword()));
        
        // Сохранение через репозиторий
        return userRepository.save(user);
    }
}
```

### 3. Чем отличается от `@Component`?
Технически — ничем. Но есть важные причины использовать именно `@Service`:

1. **Читаемость кода** — сразу понятно назначение класса.
    
2. **Семантика для разработчиков** — слой бизнес-логики.
    
3. **Таргетинг для АОП** — к сервисам часто применяются транзакции (`@Transactional`), кеширование, проверка безопасности. Spring может применять разные советы (advice) к разным стереотипам.
    
4. **Будущая миграция** — если понадобится написать свой постпроцессор, который обрабатывает только сервисы, проще отфильтровать классы с `@Service`.
    

### 4. Типичная структура (для Middle-разработчика)

java

@RestController // Слой API
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService; // Зависимость на сервис (не на репозиторий!)
}
@Service // Слой бизнес-логики
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService; // Можно внедрять другие сервисы
}
@Repository // Слой данных
public interface UserRepository extends JpaRepository<User, Long> {
}

### Итог

`@Service` — это маркер для **бизнес-логики**. Технически это `@Component`, но использование правильных стереотипов делает архитектуру чистой и понятной.