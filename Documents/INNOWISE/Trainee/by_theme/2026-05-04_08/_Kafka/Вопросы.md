Перечень вопросов на позицию Middle Java Developer. 
Список ранжирован по приоритетности: от фундаментальных основ 
и Spring-реализации до низкоуровневых оптимизаций.

## 1. Фундаментальные основы и архитектура (*Must-have*)

1. **Архитектура Kafka и терминология:** из чего состоит экосистема (Producer, Consumer, Broker, Topic, Partition, Offset, Consumer Group, Replica)? В чем ключевое отличие от RabbitMQ (Pull vs Push модель, хранение данных vs удаление после прочтения)?
    
2. **Устройство Topic и Partition:** зачем нужно разбиение на партиции, как они обеспечивают масштабируемость и параллелизм? Что происходит при увеличении или (теоретическом) уменьшении количества партиций?
    
3. **Группы потребителей (Consumer Groups) и Rebalancing:** как партиции распределяются внутри группы? Что триггерит ребалансировку, чем она опасна («Stop-the-world») и какие есть стратегии ребаланса (Range, Round Robin, Cooperative Sticky)?
    
4. **Гарантии доставки и семантика (Delivery Semantics):** подробное различие между _At-most-once_, _At-least-once_ и _Exactly-once_. Как настроить каждую из них на стороне продюсера и консьюмера?
    
5. **Гарантия порядка сообщений:** как Kafka обеспечивает порядок? Можно ли гарантировать порядок в рамках всего топика (и какой ценой) или только в рамках партиции? Роль ключа сообщения в этом процессе.
    
6. **Acks (Acknowledgements) и надежность:** разница между `acks=0`, `1` и `all`. Взаимосвязь `acks=all` с параметром `min.insync.replicas`.    

---
## 2. Работа со Spring Kafka (*Практический слой*)

7. **Конфигурация в Spring Boot:** как настроить Kafka через `application.yml` vs Java Config? Роль `KafkaTemplate`, `ConsumerFactory` и `ProducerFactory`.
    
8. **Аннотация @KafkaListener:** как она работает «под капотом»? Как слушать несколько топиков, передавать метаданные (Headers, Partition, Offset) и динамически управлять ID группы?
    
9. **Обработка ошибок в Spring:** как настроить `DefaultErrorHandler`? Реализация стратегий FixedBackOff и ExponentialBackOff.
    
10. **Retry и DLT (Dead Letter Topics):** как реализовать цикл повторных попыток (Retry Topics) и пересылку «отравленных сообщений» в DLT средствами Spring Kafka? Чем это лучше/хуже обработки внутри кода?
    
11. **Сериализация и десериализация:** использование `JsonSerializer/Deserializer`. Проблема «Poison Pill» (когда сообщение не может быть десериализовано) и как ее решить с помощью `ErrorHandlingDeserializer`.

---
## 3. Продвинутая разработка и отказоустойчивость

12. **Управление Offset (Commit strategies):** разница между автоматическим коммитом и ручным (`AckMode` в Spring: RECORD, BATCH, MANUAL, MANUAL_IMMEDIATE). Риски потери данных или дублирования.
    
13. **Idempotent Producer и Транзакции:** как работает `enable.idempotence`? Как реализовать транзакционную модель «read-process-write» в Spring с помощью `@Transactional` или `kafkaTemplate.executeInTransaction`.
    
14. **Репликация и ISR (In-Sync Replicas):** кто такой Leader и Follower? Что такое ISR список, как выбирается новый лидер и что такое `unclean.leader.election.enable` (Consistency vs Availability)?
    
15. **Consumer Lag:** что это за метрика, как ее отслеживать и какие шаги предпринять при ее росте (увеличение партиций, оптимизация кода консьюмера)?
    
16. **Producer performance:** влияние параметров `batch.size`, `linger.ms`, `buffer.memory` и компрессии (`compression.type`) на пропускную способность и задержки.

---
### 4. Глубокое понимание и инфраструктура (*Deep Dive*)

17. **Хранение данных и Log Compaction:** как Kafka пишет на диск (Sequential Write, Page Cache)? Зачем нужен Log Compaction и как он работает для хранения последних состояний по ключу?
    
18. **Политики очистки и сегменты:** как работают `retention.ms`, `retention.bytes` и зачем лог разбивается на сегменты?
    
19. **ZooKeeper vs KRaft:** почему сообщество уходит от ZooKeeper? Преимущества протокола KRaft для масштабируемости и управления метаданными.
    
20. **Низкоуровневые оптимизации:** принцип **Zero-copy** (как данные передаются в сеть мимо User Space) и работа системного вызова `sendfile`.
    
21. **Экосистема (Streams, Connect, Registry):** когда использовать Kafka Streams вместо обычного приложения? Зачем нужен Schema Registry (Avro/Protobuf) и как он обеспечивает совместимость схем?

---
