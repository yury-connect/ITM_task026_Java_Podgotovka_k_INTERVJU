---
tags:
  - TECHNOLOGIES/Брокеры/Kafka
---
**Kafka** — сначала 🔍 _принцип работы_, 
потом 🧑‍💻 _подключение в проект (Java + Spring Boot)_.

---
## 🔄 **Kafka: Алгоритм и принцип работы**

### 📦 Kafka — это распределённая **платформа потоковой передачи данных** (streaming platform), где ключевая концепция — _паблишер–топик–консьюмер_.

---
## 🧠 Основные термины и роли

| Компонент          | Описание                                                |
| ------------------ | ------------------------------------------------------- |
| **Producer**       | Публикует (отправляет) сообщения в Kafka                |
| **Topic**          | Канал/очередь для сообщений. Разделён на **partitions** |
| **Broker**         | Сервер Kafka, хранящий данные и обрабатывающий запросы  |
| **Consumer**       | Подписчик, читает сообщения из топиков                  |
| **Consumer Group** | Группа консьюмеров, обрабатывающих топик параллельно    |
| **Zookeeper**      | (устарело, но используется) Координатор кластеров       |

---
## ⚙️ Как работает Kafka (пошаговый алгоритм)
1. **Producer** отправляет сообщение в определённый **topic**.
2. **Kafka broker** сохраняет сообщение в **partition** (с определённым offset).    
3. **Consumer** (или группа) подписывается на **topic** и читает сообщения.    
4. Kafka **не удаляет** сообщения сразу — они хранятся **X** времени (настраивается).    
5. **Offset** — уник-й № сообщения в разделе. Consumer сам решает, с какого offset читать.    

---
## 💡 Важные особенности Kafka
- **Высокая пропускная способность** — подходит для логов, аналитики, событий.    
- **Гарантии доставки**:  
    🔁 At most once (без повторов)  
    ✅ At least once (минимум один раз)  
    🔒 Exactly once (ровно один раз — требует настройки)    
- **Хранение сообщений** — по времени или объему (не FIFO-очередь!).    

---
## 🧑‍💻 Подключение Kafka к Spring Boot
### 1️⃣ Добавить зависимости в `pom.xml`
```xml
<dependency>
  <groupId>org.springframework.kafka</groupId>
  <artifactId>spring-kafka</artifactId>
</dependency>
```

---
### 2️⃣ Настроить `application.yml`
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: my-consumer-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
```

---
### 3️⃣ Producer-сервис
```java
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
```

---
### 4️⃣ Consumer
```java
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "my-topic", groupId = "my-consumer-group")
    public void listen(String message) {
        System.out.println("Получено сообщение: " + message);
    }
}
```

---
### 5️⃣ Создание топика (опционально)
```java
@Bean
public NewTopic topic() {
    return TopicBuilder.name("my-topic")
        .partitions(3)
        .replicas(1)
        .build();
}
```

---
