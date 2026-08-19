---
tags:
  - TECHNOLOGIES/Брокеры/RabbitMQ
---
# 🐇 **Что такое RabbitMQ?**

**RabbitMQ** — это **брокер сообщений** (message broker), то есть система, которая **передаёт сообщения между различными частями приложения**, по принципу **"отправитель → посредник → получатель"**.

Он помогает **разделить логику** и **повысить надёжность** коммуникации между микросервисами, приложениями, модулями и т.д.

---
# ⚙️ **Принцип и механизм работы (вкратце)**

Вот как всё работает:
### 📤 1. **Producer (отправитель)**

Отправляет сообщение в **Exchange** (обменник).
### 🔀 2. **Exchange (обменник)**

Решает, куда направить сообщение, используя **Routing Key** и **binding rules**.

### 📬 3. **Queue (очередь)**
Сообщения приходят в очередь, где **ждут потребителя**.

### 📥 4. **Consumer (получатель)**
Получает сообщение из очереди и обрабатывает его.

---
> Всё это работает **асинхронно** и надёжно, даже если часть системы временно недоступна.

---
# 💻 **Как подключить RabbitMQ к Java-проекту**

## 🔧 Шаг 1: Добавь зависимость
Если используешь Maven:
```xml
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>5.20.0</version>
</dependency>
```

---
## 🔌 Шаг 2: Подключение к серверу и отправка сообщений (Producer)
```java
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // или IP сервера RabbitMQ
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Привет от Зайки 🐰";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Отправлено: '" + message + "'");
        }
    }
}
```

---

## 📥 Шаг 3: Получение сообщений (Consumer)
```java
import com.rabbitmq.client.*;

public class Receiver {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Ждём сообщения...");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Получено: '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }
}
```

---
# 🧠 **Технические детали (всё, что нужно знать)**
### 🔧 Протокол

Работает по **AMQP 0-9-1 (Advanced Message Queuing Protocol)**

---

### 🌀 Типы **Exchange'ов**

|Тип|Описание|
|---|---|
|`direct`|Маршрутизирует по точному совпадению `routing key`|
|`fanout`|Рассылает во все очереди|
|`topic`|Поддержка шаблонов, типа `order.*`|
|`headers`|Маршрутизация по заголовкам|

---
### 🧱 Свойства очередей:

- **durable** — сохраняется после перезапуска брокера    
- **exclusive** — доступна только текущему соединению    
- **auto-delete** — удаляется при закрытии    

---
### 🚨 Поддержка ack/nack:
- Сообщения подтверждаются вручную (`channel.basicAck`) или автоматически.    
- Это важно для **гарантии доставки!**    

---
### 🛡️ Надёжность:
- Поддерживает **персистентность сообщений** (`deliveryMode=2`)    
- Возможен **кластеринг и зеркалирование очередей** (HA)    
- Можно ограничить размер очередей, TTL сообщений и retry-логику    

---
### 🌐 Подключение к удалённому серверу:
```java
factory.setHost("your-server-ip");
factory.setUsername("user");
factory.setPassword("pass");
factory.setPort(5672); // стандартный порт
```

---
# ⚠️ Ошибки и обработка сообщений

## 1. **Ручное подтверждение (`ack`)**

По умолчанию `basicConsume(..., autoAck = true)` — сообщение считается доставленным **сразу**, даже если получатель упал.  
🧨 Опасно, если у тебя критичные данные.

💡 Решение:
```java
channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
```

Если не смог обработать — можно:
```java
channel.basicNack(deliveryTag, false, true); // повторная доставка
```

---
## 2. **Dead-Letter Queue (DLQ) — очередь мёртвых сообщений ☠️**

Если сообщение **не удалось обработать**, его можно **отправить в другую очередь**, чтобы:
- не потерять    
- потом анализировать    
- повторно обрабатывать    

📦 **Как создать DLQ?**
```java
Map<String, Object> args = new HashMap<>();
args.put("x-dead-letter-exchange", "dlx.exchange"); // обменник, куда пойдёт "трупик"
args.put("x-dead-letter-routing-key", "dlx.key");

channel.queueDeclare("main.queue", true, false, false, args);
channel.exchangeDeclare("dlx.exchange", "direct");
channel.queueDeclare("dlx.queue", true, false, false, null);
channel.queueBind("dlx.queue", "dlx.exchange", "dlx.key");
```
Теперь, если ты отправляешь **basicNack(...) с requeue = false**, сообщение уйдёт в `dlx.queue`.

---
# 🔁 **Retry-механика (повторная попытка)**

RabbitMQ **не делает retry сам по себе**, но ты можешь настроить его так:
### ✅ Способ 1: Через TTL + DLQ + delay
1. **Создай delay-очередь** с TTL:    
```java
Map<String, Object> retryArgs = new HashMap<>();
retryArgs.put("x-message-ttl", 5000); // 5 секунд задержки
retryArgs.put("x-dead-letter-exchange", "main.exchange"); // вернётся назад

channel.queueDeclare("retry.queue", true, false, false, retryArgs);
```

2. Если потребитель **не справился** — отправляй в эту очередь (она "подержит" сообщение и перекинет обратно).    

---
# 🔀 **Exchange’ы (обменники)** — углублённо

### 1. **direct**
— классика. Один в один по ключу.
### 2. **fanout**
— рассылает всем очередям. Идеально для **рассылок и уведомлений**.
### 3. **topic**
— маршрутизация по шаблону:
```java
channel.exchangeDeclare("topic_logs", "topic");
channel.queueBind("queue1", "topic_logs", "user.*");
channel.queueBind("queue2", "topic_logs", "user.#");
```

| Маршрут              | Попадёт куда       |
| -------------------- | ------------------ |
| `user.login`         | `user.*`, `user.#` |
| `user.login.success` | `user.#`           |
| `user`               | `user.*`, `user.#` |
### 4. **headers**

Маршрутизация по **заголовкам**, а не по ключу. Используется редко, но мощно для сложной логики.

---
# 💣 Pro-фишки
### ✨ Prefetch
Лимит сообщений, которые можно доставить потребителю, пока он не подтвердил предыдущее.
```java
channel.basicQos(1); // по одному сообщению за раз
```
### ✨ Priority queues
Можно задать приоритеты сообщениям — чем выше, тем раньше будет обработано.
```java
Map<String, Object> args = new HashMap<>();
args.put("x-max-priority", 10);
channel.queueDeclare("priority.queue", true, false, false, args);
```

---
## 🔐 Безопасность и HA
- Поддерживает **авторизацию**, SSL    
- **Зеркалирование очередей** (high availability)    
- **Clustering** и **Federation** между серверами    

---
# 🔧 Monitoring и админка
👉 Включи **RabbitMQ Management Plugin**:
```bash
rabbitmq-plugins enable rabbitmq_management
```
Затем зайди в браузере:  
[http://localhost:15672](http://localhost:15672)  
(логин: `guest` / пароль: `guest`)


