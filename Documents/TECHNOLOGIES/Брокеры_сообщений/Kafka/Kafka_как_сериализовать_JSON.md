---
tags:
  - TECHNOLOGIES/Брокеры/Kafka
---
**Как сериализовать JSON в Kafka** (отправка и приём объектов, а не строк).

---
## 🧠 Алгоритм сериализации JSON в Kafka (Spring Boot)
### 🔄 Что происходит:
1. ✅ **Producer** сериализует Java-объект → JSON    
2. ✅ **Consumer** десериализует JSON → Java-объект    

---
## 🧩 Шаги настройки
### 1️⃣ Добавь зависимости (`pom.xml`)
```xml
<dependency>
  <groupId>org.springframework.kafka</groupId>
  <artifactId>spring-kafka</artifactId>
</dependency>
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
</dependency>
```

---
### 2️⃣ DTO (модель сообщения)
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private Long id;
    private String name;
}
```

---
### 3️⃣ Kafka Config — Producer + Consumer
```java
@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, UserEvent> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, UserEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, UserEvent> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(
            config,
            new StringDeserializer(),
            new JsonDeserializer<>(UserEvent.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
```

---
### 4️⃣ Producer-сервис
```java
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public void send(String topic, UserEvent event) {
        kafkaTemplate.send(topic, event);
    }
}
```

---
### 5️⃣ Kafka Consumer
```java
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "user-events", groupId = "my-json-group")
    public void consume(UserEvent event) {
        System.out.println("Получено: " + event);
    }
}
```

---
---
---
Добавляем **валидацию DTO** и **тесты Kafka-сервиса**

---
## ✅ 1. Валидация DTO
### 📦 Аннотации в `UserEvent.java`
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {

    @NotNull(message = "ID не должен быть null")
    private Long id;

    @NotBlank(message = "Имя не должно быть пустым")
    private String name;
}
```

---
### ⚙️ Валидация при приёме сообщений (Consumer)

> Kafka Listener **не валидирует автоматически**, нужен ручной вызов.
```java
@Component
public class KafkaConsumer {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @KafkaListener(topics = "user-events", groupId = "my-json-group")
    public void consume(UserEvent event) {
        Set<ConstraintViolation<UserEvent>> violations = validator.validate(event);

        if (!violations.isEmpty()) {
            System.err.println("Ошибки валидации:");
            violations.forEach(v -> System.err.println(v.getPropertyPath() + ": " + v.getMessage()));
            return;
        }

        System.out.println("Получено валидное событие: " + event);
    }
}
```

---
## 🧪 2. Unit-тест Kafka Producer
```java
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "user-events" })
class KafkaProducerTest {

    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Test
    void testSendMessage() {
        // given
        UserEvent event = new UserEvent(1L, "Тест");

        // when
        kafkaTemplate.send("user-events", event);

        // then — проверим, что не выброшено исключений (или логика Consumer срабатывает)
        assertDoesNotThrow(() -> kafkaTemplate.flush());
    }
}
```

---
## 🧪 3. Интеграционный тест Kafka Consumer
```java
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "user-events" })
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
})
public class KafkaConsumerTest {

    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @SpyBean
    private KafkaConsumer consumer;

    @Test
    void testConsumeValidMessage() throws Exception {
        // given
        UserEvent event = new UserEvent(10L, "Пользователь");

        // when
        kafkaTemplate.send("user-events", event);
        kafkaTemplate.flush();

        // then
        Thread.sleep(1000); // Подождать приём
        verify(consumer, atLeastOnce()).consume(any(UserEvent.class));
    }
}
```
