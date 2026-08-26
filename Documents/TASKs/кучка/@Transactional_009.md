**Кейс 9: Призрачное сохранение (`@TransactionalEventListener`)**

Вы хотите записать аудит в БД строго _после_ того, как основная транзакция успешно закоммитилась.
```Java
@Component
public class AuditListener {
    @Autowired 
    private AuditLogRepository auditRepository;
	
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserCreated(UserCreatedEvent event) {
        // Пытаемся записать лог
        auditRepository.save(new AuditLog("User created: " + event.getUserId()));
    }
}
```

**Вопрос:** Сохранится ли запись `AuditLog` в базу данных?

**Что произойдет:** Запись **НЕ сохранится**, при этом приложение **НЕ выбросит никаких ошибок** (все отработает "тихо").

**Почему:** Фаза `AFTER_COMMIT` гарантирует, что слушатель вызывается, когда _исходная_ транзакция уже успешно завершена и закоммичена.

Когда `auditRepository.save()` вызывается внутри этого метода, Spring видит текущий контекст транзакции, который находится в состоянии `COMMITTED` (закрыт). Hibernate привязан к этой закрытой сессии, он принимает объект в памяти, но `flush()` уже никогда не произойдет, так как коммит физического соединения с БД уже произошел шагом ранее. Запрос `INSERT` просто улетает в никуда. 

_Лечение:_ Аннотировать метод слушателя через `@Transactional(propagation = Propagation.REQUIRES_NEW)`, чтобы явным образом открыть новую, независимую транзакцию базы данных.

---
