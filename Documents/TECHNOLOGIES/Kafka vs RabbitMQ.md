---
tags:
  - Брокеры/Kafka
  - Брокеры/RabbitMQ
---

# 1. Основные концепции брокеров сообщений

Модель работы Брокеры работают по принципу "**продюсер → брокер → консьюмер**". 
- **Продюсер**: Отправляет сообщения в брокер. 
- **Брокер**: Хранит сообщения и управляет их доставкой. 
- **Консьюмер**: Получает и обрабатывает сообщения.

Типы брокеров 
- **Очереди сообщений** *(например, **RabbitMQ**)*: Сообщения доставляются в порядке очереди, часто с удалением после обработки. 
- **Журнал событий** *(например, **Kafka**)*: Сообщения сохраняются как лог, доступный для многократного чтения.

# 2. Основные **различия**

- **Kafka**:    
    - Распределенный **лог событий** (сообщения хранятся долго).        
    - Использует **партиции** для масштабирования.        
    - Подходит для **стриминга данных** и аналитики.
    
- **RabbitMQ**:    
    - Классический **брокер сообщений** (очереди).        
    - Гибкие модели: очереди, топики, RPC.

| **Критерий**            | **Apache Kafka**                                                                                                  | **RabbitMQ**                                                                    |
| ----------------------- | ----------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------- |
| **Тип системы**         | Распределённый журнал событий *(event streaming platform)*                                                        | Классический брокер сообщений *(message broker)*                                |
| **Модель работы**       | Логи событий, чтение потоков, партиционирование                                                                   | Очереди, маршрутизация через exchange                                           |
| **Производительность**  | Высокая: до миллионов сообщений/сек *(зависит от конфигурации кластера)*                                          | Средняя: десятки тысяч сообщений/сек *(зависит от настроек и нагрузки)*         |
| **Хранение**            | Долгосрочное *(дни, недели, настраивается через retention policy)*                                                | Краткосрочное *(сообщения удаляются после обработки, но есть persistent mode)*  |
| **Гарантии доставки**   | At-least-once *(по умолчанию)*, Exactly-once *(с версии 0.11 при использовании idempotent producer и транзакций)* | At-least-once, Exactly-once *(с использованием confirmations и транзакций)*     |
| **Сложность настройки** | Высокая *(требуется ZooKeeper/KRaft, настройка партиций и реплик)*                                                | Средняя *(простая установка, поддержка Docker, но сложность растёт в кластере)* |
| **Поддержка языков**    | Клиенты для Java, Python, Go и других через `kafka-clients`                                                       | Клиенты для Java, Python, .NET и других через `rabbitmq-client`                 |

---

# 3. Когда использовать **Kafka** ?

### 3.1. Основные характеристики 
- **Тип**: Распределённая платформа для потоковой обработки событий. 
- **Модель**: Лог событий, разбитый на партиции, с долгосрочным хранением. 
- **Производительность**: Высокая, до миллионов сообщений в секунду. 
- **Применение**: Аналитика в реальном времени, сбор логов, интеграция с Big Data (Flink, Spark).
### 3.2. Ключевые понятия 
- **Топик**: Логическая категория для сообщений *(например, "orders")*. 
- **Партиции**: Разделение топика для параллельной обработки. 
- **Реплики**: Копии партиций для отказоустойчивости. 
- **Retention period**: Время хранения сообщений *(настраивается, например, 7 дней)*.

 **Сценарии**
	 1. **Потоковая обработка данных** (аналитика в реальном времени)    
	 2. **Хранение истории событий** (event sourcing)    
	 3. **Сбор и анализ логов**    
	 4. **Интеграция Big Data-систем** (Flink, Spark)

**Пример Kafka**:
 ```java
 import org.apache.kafka.clients.producer.*;
 import org.apache.kafka.clients.consumer.*;
 import org.apache.kafka.common.serialization.StringSerializer;
 import org.apache.kafka.common.serialization.StringDeserializer;

 import java.util.Properties;
 import java.util.Collections;

 public class KafkaExample {
     private static final String BOOTSTRAP_SERVERS = "localhost:9092";
     private static final String TOPIC = "example-topic";

     public static void main(String[] args) {
         // Producer
         Properties producerProps = new Properties();
         producerProps.put("bootstrap.servers", BOOTSTRAP_SERVERS);
         producerProps.put("key.serializer", StringSerializer.class.getName());
         producerProps.put("value.serializer", StringSerializer.class.getName());

         try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps)) {
             ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, "key", "Hello, Kafka!");
             producer.send(record, (metadata, exception) -> {
                 if (exception == null) {
                     System.out.println("Сообщение отправлено: " + metadata);
                 } else {
                     exception.printStackTrace();
                 }
             });
         }

         // Consumer
         Properties consumerProps = new Properties();
         consumerProps.put("bootstrap.servers", BOOTSTRAP_SERVERS);
         consumerProps.put("group.id", "example-group");
         consumerProps.put("key.deserializer", StringDeserializer.class.getName());
         consumerProps.put("value.deserializer", StringDeserializer.class.getName());
         consumerProps.put("auto.offset.reset", "earliest");

         try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
             consumer.subscribe(Collections.singletonList(TOPIC));
             while (true) {
                 ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                 for (ConsumerRecord<String, String> record : records) {
                     System.out.printf("Получено: key=%s, value=%s, partition=%d, offset=%d%n",
                             record.key(), record.value(), record.partition(), record.offset());
                 }
             }
         }
     }
 }
 ```

**Зависимости для Maven:**
 ```xml
 <dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>3.6.0</version>
 </dependency>
 ```

**Плюсы**:
	- ✅ Масштабируемость *(партиции + реплики)*.    
	- ✅ Долгосрочное хранение сообщений
	- ✅ Интеграция с потоковыми платформами *(Kafka Streams, ksqlDB)*. 

**Минусы**:
	- ❌ Сложность настройки кластера *(ZooKeeper или KRaft)*.
	- ❌ Избыточность для простых задач *(например, отправка email)*.   

---
# 4. Когда использовать **RabbitMQ?**

### 4.1. Основные характеристики
- **Тип**: Классический брокер сообщений.
- **Модель**: Очереди с маршрутизацией через exchange.
- **Производительность**: До десятков тысяч сообщений в секунду.
- **Применение**: Фоновые задачи, RPC-запросы, балансировка нагрузки.
### 4.2. Ключевые понятия
- **Очередь**: Хранилище сообщений, из которого консьюмеры забирают данные.
- **Exchange**: Маршрутизатор, распределяющий сообщения по очередям (типы: direct, topic, fanout).
- **Bindings**: Правила маршрутизации между exchange и очередями.

**Сценарии**
	1. **Фоновые задачи**: Отправка email, обработка заказов, генерация отчетов.
	2. **RPC-запросы**: Асинхронное взаимодействие между микросервисами.
	3. **Балансировка нагрузки**: Распределение задач между воркерами.    

Пример **RabbitMQ**
 ```java
 import com.rabbitmq.client.*;

 public class RabbitMQExample {
     private static final String QUEUE_NAME = "example-queue";
     private static final String HOST = "localhost";

     public static void main(String[] args) throws Exception {
         ConnectionFactory factory = new ConnectionFactory();
         factory.setHost(HOST);

         // Producer
         try (Connection connection = factory.newConnection();
              Channel channel = connection.createChannel()) {
             channel.queueDeclare(QUEUE_NAME, false, false, false, null);
             String message = "Hello, RabbitMQ!";
             channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
             System.out.println("Отправлено: " + message);
         }

         // Consumer
         try (Connection connection = factory.newConnection();
              Channel channel = connection.createChannel()) {
             channel.queueDeclare(QUEUE_NAME, false, false, false, null);
             DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                 String message = new String(delivery.getBody(), "UTF-8");
                 System.out.println("Получено: " + message);
             };
             channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
         }
     }
 }
 ``` 

**Зависимости для Maven:**
 ```xml
 <dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>5.20.0</version>
 </dependency>
 ```

**Плюсы**:
	- ✅ Простота настройки (Docker-образы, минимальная конфигурация).
	- ✅ Гибкость маршрутизации (direct, topic, fanout exchange).
	- ✅ Поддержка множества языков программирования.

**Минусы**:
	- ❌Ограниченное долгосрочное хранение сообщений.
	- ❌Меньшая производительность при высоких нагрузках по сравнению с **Kafka**.

---
## 5. Что выбрать?

- **Kafka**: лучше для высоконагруженных систем с долгосрочным хранением данных.
  // Подходит для задач, где важны стриминг, аналитика или долгосрочное хранение событий.
    - **Примеры**: Uber (аналитика поездок), Netflix (обработка пользовательских действий), LinkedIn (логи и метрики).
    
- **RabbitMQ**: проще в использовании и подходит для асинхронных задач и микросервисов. 
  // Идеально для асинхронных задач, фоновой обработки и микросервисной архитектуры.
    - **Примеры**: Reddit (уведомления), Shopify (обработка заказов).

### **Гибридный вариант**
- **RabbitMQ** для синхронных задач (например, оплата).    
- **Kafka** для асинхронной аналитики (например, сбор статистики).    

---
## **5. Альтернативы**

- **NATS**: Для высоконагруженных систем (но без хранения).    
- **AWS SQS/SNS**: Managed-решения в облаке.    

> **Итог**:> 
> - **Kafka** — это «журнал событий» для Big Data.>     
> - **RabbitMQ** — «почтальон» для задач между сервисами.
>