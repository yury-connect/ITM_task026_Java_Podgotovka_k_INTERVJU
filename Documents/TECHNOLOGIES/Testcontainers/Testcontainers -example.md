
---

## 🧪 Что такое **Testcontainers**?

**Testcontainers** — это библиотека для **интеграционного тестирования**, которая позволяет **запускать реальные версии внешних сервисов в Docker-контейнерах прямо во время тестов**.
📦 Под "внешними сервисами" понимаются:
- базы данных (PostgreSQL, MySQL, MongoDB),    
- брокеры сообщений (Kafka, RabbitMQ),    
- браузеры (Selenium),    
- даже целые микросервисы.    

---
## 🧠 Зачем он нужен?
Обычные **юнит-тесты** проверяют код "в вакууме", без подключения к реальным базам или другим системам.
**Интеграционные тесты** проверяют, как ваш код работает в связке с реальными сервисами (например, с базой PostgreSQL), и именно тут помогает **Testcontainers**:
### 🔄 Что делает Testcontainers:
- Загружает нужный Docker-контейнер;    
- Запускает его в тестовом окружении;    
- Даёт вам доступ к базе или Kafka как к настоящей, но временной и безопасной;    
- После тестов — всё автоматически удаляется.    

---
## 🛠️ Пример: PostgreSQL + Spring Boot
### 1. **Добавляем зависимость (Maven)**
```xml
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>postgresql</artifactId>
  <version>1.19.1</version>
  <scope>test</scope>
</dependency>

```
### 2. **Пример теста**
```java
@Testcontainers
@SpringBootTest
public class ContractServiceTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ContractRepository contractRepository;

    @Test
    void testCreateContract() {
        Contract c = new Contract("123", LocalDate.now(), LocalDate.now().plusMonths(6));
        contractRepository.save(c);
        assertEquals("123", contractRepository.findById(c.getId()).get().getNumber());
    }
}
```
### Что здесь происходит:
- `@Container` — говорит, что надо поднять контейнер с PostgreSQL.    
- `DynamicPropertySource` — Spring подставляет реальные параметры подключения из этого контейнера.    
- Всё работает как будто с настоящей БД — но безопасно и временно.    

---
## 📈 Как еще используют Testcontainers?
- С Kafka: поднимается Kafka и тестируются продюсеры/консьюмеры.    
- С Redis: можно проверять кеширование.    
- С Elasticsearch: тестировать поиск.    
- С S3 (MinIO): проверять загрузку файлов.    

---
## ⚠️ На что обратить внимание
- **Нужен Docker** (локально или в CI).    
- Лучше не использовать в юнит-тестах — это инструмент для **интеграционного** тестирования.    
- На CI/CD (например, в GitLab) нужно разрешить запуск Docker внутри runner'а.    

---
## 🧠 В связке с вашим резюме
Ты использовал Testcontainers для:
- запуска временных PostgreSQL-контейнеров в тестах;    
- проверки работы бизнес-логики с реальной базой данных;    
- тестирования микросервисов и взаимодействия через Kafka;    
- написания надёжных тестов, которые **работают как в продакшене**, но **не ломают прод**.

---

