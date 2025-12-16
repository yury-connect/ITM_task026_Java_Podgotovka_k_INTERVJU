# **Разделение ответственности в Spring-приложении**

---
## **1. Контроллер (Controller)**
**Задача:** Принимать HTTP-запросы, возвращать HTTP-ответы. Ничего больше.

### **Что делает контроллер:**
✅ Принимает и валидирует входные данные  
✅ Вызывает сервисный слой  
✅ Обрабатывает HTTP-статусы и ошибки  
✅ Преобразует ответы в JSON/XML  
✅ Не содержит бизнес-логики

### Пример правильного контроллера:
```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    
    private final CommentService commentService;
    
    @PostMapping("/{materialId}")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable @Min(1) Long materialId,
            @RequestBody @Valid CommentRequest request) {
        
        // 1. Валидация входных данных (автоматически через @Valid)
        // 2. Вызов сервиса
        CommentDto response = commentService.createComment(materialId, request);
        
        // 3. Формирование HTTP-ответа
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{materialId}")
    public ResponseEntity<Page<CommentDto>> getComments(
            @PathVariable Long materialId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        // Просто делегируем сервису
        Page<CommentDto> comments = commentService.getComments(materialId, page, size);
        return ResponseEntity.ok(comments);
    }
}
```

---
## **2. Сервис (Service)**
**Задача:** Содержит бизнес-логику, координирует работу репозиториев.

### **Что делает сервис:**
✅ Содержит бизнес-правила и логику  
✅ Работает с транзакциями  
✅ Координирует несколько репозиториев  
✅ Преобразует ентити в DTO и обратно  
✅ Не знает о HTTP/вебе

### Пример правильного сервиса:
```java
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final MaterialRepository materialRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    
    public CommentDto createComment(Long materialId, CommentRequest request) {
        // 1. Бизнес-валидация
        Material material = materialRepository.findById(materialId)
            .orElseThrow(() -> new ResourceNotFoundException("Материал не найден"));
        
        User author = userService.getCurrentUser();
        
        // 2. Бизнес-логика
        if (!material.isPublished() && !author.hasRole(RoleConstants.EDITOR)) {
            throw new AccessDeniedException("Нельзя комментировать неопубликованный материал");
        }
        
        // 3. Создание сущности
        Comment comment = new Comment();
        comment.setBody(request.getBody().trim());
        comment.setMaterial(material);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());
        
        // 4. Сохранение
        comment = commentRepository.save(comment);
        
        // 5. Дополнительные бизнес-действия
        if (material.getAuthor().getId().equals(author.getId())) {
            notificationService.sendNewCommentNotification(material.getAuthor(), comment);
        }
        
        // 6. Преобразование в DTO
        return mapToDto(comment);
    }
    
    private CommentDto mapToDto(Comment comment) {
        return CommentDto.builder()
            .id(comment.getId())
            .body(comment.getBody())
            .authorName(comment.getAuthor().getFullName())
            .createdAt(comment.getCreatedAt())
            .build();
    }
}
```

---
---
## **3. Что НЕ должен делать контроллер:**
```java
// ❌ НЕПРАВИЛЬНО - контроллер делает работу сервиса
@PostMapping
public ResponseEntity<?> createComment(@RequestBody CommentRequest request) {
    // ❌ Проверяет существование материала
    if (!materialRepository.existsById(request.getMaterialId())) {
        return ResponseEntity.notFound().build();
    }
    
    // ❌ Создает сущность
    Comment comment = new Comment();
    comment.setText(request.getText());
    
    // ❌ Сохраняет в БД
    commentRepository.save(comment);
    
    // ❌ Отправляет уведомление
    emailService.sendNotification(comment);
    
    return ResponseEntity.ok(comment);
}
```

## **4. Что НЕ должен делать сервис:**
```java
// ❌ НЕПРАВИЛЬНО - сервис работает с HTTP
@Service
public class CommentService {
    
    @Autowired
    private HttpServletRequest request; // ❌ Не должно быть!
    
    public Comment createComment(CommentRequest request) {
        // ❌ Работает с HTTP-сессией
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        
        // ❌ Формирует HTTP-ответ
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); // ❌ HTTP в сервисе
        }
        
        // ...
    }
}
```

---
---
## **5. Краткая шпаргалка:**

|**Контроллер**|**Сервис**|
|---|---|
|✅ HTTP-запросы/ответы|✅ Бизнес-логика|
|✅ Валидация аннотациями|✅ Транзакции|
|✅ Делегирует работу сервису|✅ Работает с репозиториями|
|❌ Не содержит логики|❌ Не знает о HTTP|
|❌ Не работает с БД напрямую|❌ Не возвращает ResponseEntity|

## **6. Простой тест:**

Если вы можете переиспользовать сервис в:
- REST API    
- SOAP WebService    
- Консольном приложении    
- Бэкграунд-джобе    

**и ничего не нужно менять в сервисе** — значит, разделение сделано **правильно**.

Контроллер = **фасад для HTTP**  
Сервис = **ядро бизнес-логики**

---
---
### Как правильно разделять interface / implementation (Best Practice)

#### 🎯 Золотое правило:
- **Интерфейс = API-контракт**    
- **Реализация = поведение**

---
## ✅ Правильная архитектура

### 📌 **Интерфейс** (`EduCommentsController`)

**ТОЛЬКО:**
- Swagger (`@Operation`, `@ApiResponses`, `@Tag`)    
- Spring MVC mapping (`@RequestMapping`, `@PostMapping`)    
- Валидация параметров    
- Сигнатура метода    

❌ **НЕ должно быть:**
- `@Secured`    
- `@Slf4j`    
- бизнес-логики    
- логирования    

### 📌 **Реализация** (`EduCommentsControllerImpl`)

**ТОЛЬКО:**
- `@RestController`    
- `@Secured`    
- логирование    
- вызов сервисов    
- формирование ответа    

❌ **НЕ должно быть:**
- Swagger    
- Mapping’ов    
- Валидаций параметров

---
## Почему так лучше (коротко и по делу)

### 🧠 Психологический якорь
**«Интерфейс — договор. Реализация — исполнитель.»**

### 🔥 Плюсы:
- 🧹 Нет дублирования    
- 📚 Swagger всегда синхронизирован    
- 🔐 Безопасность локализована    
- 🔄 Реализацию можно менять без влияния на API    
- 🧪 Упрощается тестирование (mock интерфейса)    

## 5️⃣ Быстрый чек-лист для запоминания ✅
- Swagger → **interface**    
- Mapping → **interface**    
- Validation → **interface**    
- Security → **impl**    
- Logs → **impl**    
- Service call → **impl**
- 