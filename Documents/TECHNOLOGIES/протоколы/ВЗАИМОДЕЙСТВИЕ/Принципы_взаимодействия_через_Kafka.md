Давайте разберем принципы взаимодействия через Apache Kafka с примерами на Java.

## Основные концепции Kafka

### Архитектура Kafka:

- **Producer** → приложение, отправляющее сообщения
    
- **Consumer** → приложение, читающее сообщения
    
- **Broker** → сервер Kafka
    
- **Topic** → категория/поток сообщений
    
- **Partition** → часть топика для параллельной обработки
    
- **Consumer Group** → группа потребителей, совместно обрабатывающих сообщения
    

---

## Настройка зависимостей Maven
```xml
<dependencies>
    <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-clients</artifactId>
        <version>3.4.0</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>2.0.6</version>
    </dependency>
</dependencies>
```
---

## Producer (Отправитель сообщений)

### 1. Базовый Producer
```java
package com.example.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class BasicProducerExample {
    
    private final Producer<String, String> producer;
    
    public BasicProducerExample(String bootstrapServers) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        
        // Оптимизация производительности
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // Ждать подтверждения от всех реплик
        props.put(ProducerConfig.RETRIES_CONFIG, 3); // Количество повторных попыток
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10); // Задержка перед отправкой батча
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // Размер батча
        
        this.producer = new KafkaProducer<>(props);
    }
    
    // Синхронная отправка
    public void sendMessageSync(String topic, String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        
        try {
            RecordMetadata metadata = producer.send(record).get();
            System.out.printf("Message sent to topic=%s, partition=%d, offset=%d%n",
                    metadata.topic(), metadata.partition(), metadata.offset());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
    
    // Асинхронная отправка с callback
    public void sendMessageAsync(String topic, String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    System.out.printf("Async message sent to topic=%s, partition=%d, offset=%d%n",
                            metadata.topic(), metadata.partition(), metadata.offset());
                } else {
                    System.err.println("Error sending async message: " + exception.getMessage());
                }
            }
        });
    }
    
    public void close() {
        producer.close();
    }
}
```

### 2. Продвинутый Producer с кастомной сериализацией
```java
package com.example.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

// Кастомный сериализатор для JSON
public class JsonSerializer<T> implements Serializer<T> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Конфигурация при необходимости
    }
    
    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) return null;
        
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing JSON", e);
        }
    }
    
    @Override
    public void close() {
        // Очистка ресурсов
    }
}

// Data класс для сообщений
class UserEvent {
    private int userId;
    private String action;
    private long timestamp;
    
    public UserEvent() {}
    
    public UserEvent(int userId, String action, long timestamp) {
        this.userId = userId;
        this.action = action;
        this.timestamp = timestamp;
    }
    
    // Геттеры и сеттеры
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}

// Producer для кастомных объектов
class JsonProducerExample {
    private final Producer<String, UserEvent> producer;
    
    public JsonProducerExample(String bootstrapServers) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        
        this.producer = new KafkaProducer<>(props);
    }
    
    public void sendUserEvent(String topic, UserEvent event) {
        String key = String.valueOf(event.getUserId());
        ProducerRecord<String, UserEvent> record = new ProducerRecord<>(topic, key, event);
        
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.printf("User event sent to partition=%d, offset=%d%n",
                        metadata.partition(), metadata.offset());
            } else {
                System.err.println("Error sending user event: " + exception.getMessage());
            }
        });
    }
    
    public void close() {
        producer.close();
    }
}
```

---

## Consumer (Получатель сообщений)

### 1. Базовый Consumer
```java
package com.example.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class BasicConsumerExample {
    
    private final Consumer<String, String> consumer;
    private volatile boolean running = true;
    
    public BasicConsumerExample(String bootstrapServers, String groupId, String topic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        
        // Важные настройки
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // earliest, latest, none
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); // Ручное подтверждение
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100); // Максимум сообщений за poll
        
        this.consumer = new KafkaConsumer<>(props);
        this.consumer.subscribe(Collections.singletonList(topic));
    }
    
    // Постоянное чтение сообщений
    public void startConsuming() {
        try {
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                
                for (ConsumerRecord<String, String> record : records) {
                    processMessage(record);
                }
                
                // Ручное подтверждение обработки
                if (!records.isEmpty()) {
                    consumer.commitSync();
                    System.out.println("Committed offsets for " + records.count() + " messages");
                }
            }
        } finally {
            consumer.close();
        }
    }
    
    private void processMessage(ConsumerRecord<String, String> record) {
        System.out.printf("Received message: topic=%s, partition=%d, offset=%d, key=%s, value=%s%n",
                record.topic(), record.partition(), record.offset(), record.key(), record.value());
        
        // Бизнес-логика обработки сообщения
        try {
            // Имитация обработки
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void stop() {
        running = false;
    }
}
```

### 2. Продвинутый Consumer с обработкой по партициям
```java
package com.example.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class AdvancedConsumerExample {
    
    private final Consumer<String, String> consumer;
    private volatile boolean running = true;
    
    public AdvancedConsumerExample(String bootstrapServers, String groupId, String topic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        this.consumer = new KafkaConsumer<>(props);
        this.consumer.subscribe(Collections.singletonList(topic), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(org.apache.kafka.common.TopicPartition partitions) {
                System.out.println("Partitions revoked: " + partitions);
                consumer.commitSync(); // Коммит перед ребалансировкой
            }
            
            @Override
            public void onPartitionsAssigned(org.apache.kafka.common.TopicPartition partitions) {
                System.out.println("Partitions assigned: " + partitions);
            }
        });
    }
    
    public void startConsumingWithBatchProcessing() {
        try {
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                
                if (!records.isEmpty()) {
                    // Обработка батча сообщений
                    processBatch(records);
                    
                    // Асинхронный коммит
                    consumer.commitAsync(new OffsetCommitCallback() {
                        @Override
                        public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                            if (exception != null) {
                                System.err.println("Commit failed: " + exception.getMessage());
                            } else {
                                System.out.println("Successfully committed offsets");
                            }
                        }
                    });
                }
            }
        } finally {
            try {
                consumer.commitSync(); // Финальный синхронный коммит
            } finally {
                consumer.close();
            }
        }
    }
    
    private void processBatch(ConsumerRecords<String, String> records) {
        // Группировка по партициям
        for (TopicPartition partition : records.partitions()) {
            var partitionRecords = records.records(partition);
            System.out.printf("Processing %d messages from partition %d%n", 
                    partitionRecords.size(), partition.partition());
            
            for (ConsumerRecord<String, String> record : partitionRecords) {
                processMessage(record);
            }
        }
    }
    
    private void processMessage(ConsumerRecord<String, String> record) {
        // Бизнес-логика
        System.out.printf("Processed: key=%s, value=%s%n", record.key(), record.value());
    }
    
    public void stop() {
        running = false;
    }
}
```

---

## Consumer Groups

### 3. Множественные Consumers в одной группе
```java
package com.example.kafka;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConsumerGroupExample {
    
    private final ExecutorService executor;
    private final String bootstrapServers;
    private final String groupId;
    private final String topic;
    private final int numConsumers;
    
    public ConsumerGroupExample(String bootstrapServers, String groupId, 
                              String topic, int numConsumers) {
        this.bootstrapServers = bootstrapServers;
        this.groupId = groupId;
        this.topic = topic;
        this.numConsumers = numConsumers;
        this.executor = Executors.newFixedThreadPool(numConsumers);
    }
    
    public void start() {
        for (int i = 0; i < numConsumers; i++) {
            final int consumerId = i;
            executor.submit(() -> {
                AdvancedConsumerExample consumer = new AdvancedConsumerExample(
                        bootstrapServers, groupId, topic);
                
                System.out.printf("Consumer %d started%n", consumerId);
                consumer.startConsumingWithBatchProcessing();
            });
        }
    }
    
    public void stop() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
```

---

## Практический пример: Система обработки заказов

### 1. Модели данных
```java
package com.example.kafka.ecommerce;

public class Order {
    private String orderId;
    private int customerId;
    private double amount;
    private String status;
    private long timestamp;
    
    // Конструкторы, геттеры, сеттеры
    public Order() {}
    
    public Order(String orderId, int customerId, double amount, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.status = status;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Геттеры и сеттеры...
}

class OrderEvent {
    private String eventId;
    private String orderId;
    private String eventType; // CREATED, PAID, SHIPPED, etc.
    private Map<String, Object> payload;
    private long timestamp;
    
    // Конструкторы, геттеры, сеттеры...
}
```

### 2. Order Producer
```java
package com.example.kafka.ecommerce;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.UUID;

public class OrderService {
    private final Producer<String, OrderEvent> producer;
    private final String ordersTopic;
    
    public OrderService(String bootstrapServers, String ordersTopic) {
        this.ordersTopic = ordersTopic;
        
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "com.example.kafka.JsonSerializer");
        
        this.producer = new KafkaProducer<>(props);
    }
    
    public void createOrder(int customerId, double amount) {
        String orderId = UUID.randomUUID().toString();
        Order order = new Order(orderId, customerId, amount, "CREATED");
        
        OrderEvent event = new OrderEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setOrderId(orderId);
        event.setEventType("ORDER_CREATED");
        event.setPayload(Map.of("order", order));
        event.setTimestamp(System.currentTimeMillis());
        
        // Ключ = orderId для гарантии порядка обработки одного заказа
        ProducerRecord<String, OrderEvent> record = 
            new ProducerRecord<>(ordersTopic, orderId, event);
        
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.printf("Order event sent for order %s to partition %d%n",
                        orderId, metadata.partition());
            } else {
                System.err.println("Failed to send order event: " + exception.getMessage());
            }
        });
    }
}
```

### 3. Order Processor (Consumer)
```java
package com.example.kafka.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class OrderProcessor {
    private final KafkaConsumer<String, OrderEvent> consumer;
    private volatile boolean running = true;
    
    public OrderProcessor(String bootstrapServers, String groupId, String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", groupId);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "com.example.kafka.JsonDeserializer");
        props.put("value.deserializer.type", "com.example.kafka.ecommerce.OrderEvent");
        props.put("enable.auto.commit", "false");
        
        this.consumer = new KafkaConsumer<>(props);
        this.consumer.subscribe(Collections.serializationList(topic));
    }
    
    public void processOrders() {
        try {
            while (running) {
                ConsumerRecords<String, OrderEvent> records = 
                    consumer.poll(Duration.ofMillis(1000));
                
                records.forEach(record -> {
                    try {
                        handleOrderEvent(record.value());
                        // Коммит после успешной обработки каждого сообщения
                        consumer.commitSync(Collections.singletonMap(
                            record.topicPartition(), 
                            new org.apache.kafka.clients.consumer.OffsetAndMetadata(record.offset() + 1)
                        ));
                    } catch (Exception e) {
                        System.err.println("Failed to process order event: " + e.getMessage());
                        // В реальной системе: отправка в DLQ (Dead Letter Queue)
                    }
                });
            }
        } finally {
            consumer.close();
        }
    }
    
    private void handleOrderEvent(OrderEvent event) {
        switch (event.getEventType()) {
            case "ORDER_CREATED":
                processOrderCreated(event);
                break;
            case "ORDER_PAID":
                processOrderPaid(event);
                break;
            case "ORDER_SHIPPED":
                processOrderShipped(event);
                break;
            default:
                System.out.println("Unknown event type: " + event.getEventType());
        }
    }
    
    private void processOrderCreated(OrderEvent event) {
        System.out.printf("Processing new order: %s%n", event.getOrderId());
        // Бизнес-логика: проверка inventory, валидация платежа и т.д.
    }
    
    private void processOrderPaid(OrderEvent event) {
        System.out.printf("Order paid: %s%n", event.getOrderId());
        // Бизнес-логика: обновление статуса, уведомление службы доставки
    }
    
    private void processOrderShipped(OrderEvent event) {
        System.out.printf("Order shipped: %s%n", event.getOrderId());
        // Бизнес-логика: отправка уведомления клиенту
    }
    
    public void stop() {
        running = false;
    }
}
```

---

## Запуск приложения
```java
package com.example.kafka;

public class KafkaApplication {
    
    public static void main(String[] args) throws InterruptedException {
        String bootstrapServers = "localhost:9092";
        String topic = "user-events";
        String groupId = "user-processor-group";
        
        // Запуск producer
        BasicProducerExample producer = new BasicProducerExample(bootstrapServers);
        
        // Запуск consumer group
        ConsumerGroupExample consumerGroup = new ConsumerGroupExample(
                bootstrapServers, groupId, topic, 3);
        consumerGroup.start();
        
        // Отправка тестовых сообщений
        for (int i = 0; i < 100; i++) {
            producer.sendMessageAsync(topic, "key-" + i, "message-" + i);
            Thread.sleep(100);
        }
        
        // Ожидание и завершение
        Thread.sleep(5000);
        producer.close();
        consumerGroup.stop();
    }
}
```
## Ключевые принципы для Java-разработчика

1. **Производительность Producer**:
    
    - Используйте батчинг и компрессию
        
    - Настраивайте `linger.ms` и `batch.size`
        
2. **Надежность Consumer**:
    
    - Используйте ручной коммит оффсетов
        
    - Обрабатывайте ребалансировку
        
    - Реализуйте retry логику для ошибок
        
3. **Сериализация**:
    
    - Используйте эффективные форматы (Avro, Protobuf)
        
    - Реализуйте кастомные сериализаторы для сложных объектов
        
4. **Масштабирование**:
    
    - Используйте Consumer Groups для горизонтального масштабирования
        
    - Настраивайте количество партиций в топиках
        
5. **Мониторинг**:
    
    - Отслеживайте lag потребителей
        
    - Мониторьте throughput producer/consumer
        

Этот фундамент позволит вам эффективно работать с Kafka в Java-приложениях любой сложности!

---
