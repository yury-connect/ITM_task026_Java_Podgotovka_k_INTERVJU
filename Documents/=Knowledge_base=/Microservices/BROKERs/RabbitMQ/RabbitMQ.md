## RabbitMQ для собеседования: понятным языком

### Что такое RabbitMQ
**RabbitMQ — это брокер сообщений (message broker)**, т.е. почтальон в распределённой системе. Он помогает разным сервисам общаться друг с другом асинхронно, без необходимости знать о существовании друг друга. RabbitMQ реализует протокол **AMQP 0-9-1 (Advanced Message Queuing Protocol)** [](https://hpe-help-w.docs.opsramp.com/integrations/middleware/rabbitmq/#version-history)[](https://developer.aliyun.com/article/1589633).

---
### Основная модель: <br>отправитель → Exchange → Queue → получатель

**Ключевая идея:** отправитель не отправляет сообщение напрямую в очередь. Вместо этого он отправляет его в **Exchange (обменник)**, а Exchange уже решает, в какую очередь (Queue) направить сообщение [](https://rabbitmq.cn/tutorials/amqp-concepts#exchange-fanout)[](https://www.cloudamqp.com/blog/part4-rabbitmq-for-beginners-exchanges-routing-keys-bindings.html).
```text
Отправитель (Producer) → Exchange → Queue → Получатель (Consumer)
```

---
### Ключевые компоненты

| Компонент                            | Что делает                                                | Важно                                                                                                                                                                                                                                     |
| ------------------------------------ | --------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Producer (*отправитель*)**         | Создаёт и отправляет сообщения                            | Не знает про очереди, отправляет только в Exchange                                                                                                                                                                                        |
| **Exchange (обменник)**              | Получает сообщение и решает, куда его направить           | **Не хранит** сообщения — только маршрутизирует [](https://cloud.tencent.com.cn/developer/article/2572867?policyId=1003)                                                                                                                  |
| **Queue (очередь)**                  | Хранит сообщения до тех пор, пока их не заберёт Consumer  | Буфер сообщений, FIFO [](https://hpe-help-w.docs.opsramp.com/integrations/middleware/rabbitmq/#version-history)[](https://raw.githubusercontent.com/OneUptime/blog/refs/heads/master/posts/2026-02-20-rabbitmq-getting-started/README.md) |
| **Consumer (получатель)**            | Забирает сообщения из очереди и обрабатывает их           | Может быть несколько подписчиков на одну очередь                                                                                                                                                                                          |
| **Binding (связка)**                 | Правило, которое связывает Exchange и Queue               | Задаёт маршрутизацию [](https://developer.aliyun.com/article/1589633)                                                                                                                                                                     |
| **Routing Key (ключ маршрутизации)** | Метка на сообщении, по которой Exchange принимает решение | Часть маршрутизации                                                                                                                                                                                                                       |
| **Virtual Host (vHost)**             | Пространство имён для изоляции                            | Разделяет окружения (dev/prod) в одном кластере [](https://hpe-help-w.docs.opsramp.com/integrations/middleware/rabbitmq/#version-history)                                                                                                 |
| **Channel (канал)**                  | Лёгкое соединение внутри одного TCP-коннекта              | Позволяет использовать одно TCP-соединение для многих операций [](https://developer.aliyun.com/article/1589633)                                                                                                                           |

---
### 4 типа Exchange

Это **самый частый вопрос** на собеседованиях [](https://rabbitmq.cn/tutorials/amqp-concepts#exchange-fanout)[](https://cloud.tencent.com.cn/developer/article/2572867?policyId=1003)[](https://www.cloudamqp.com/blog/part4-rabbitmq-for-beginners-exchanges-routing-keys-bindings.html):

| Тип         | Как работает                                                                      | Когда использовать                             |
| ----------- | --------------------------------------------------------------------------------- | ---------------------------------------------- |
| **Direct**  | Точное совпадение Routing Key и Binding Key                                       | "Отправь логин-событие только в логин-очередь" |
| **Topic**   | Совпадение по маске (`*` — одно слово, `#` — любое количество)                    | "Логи из США" -> `logs.usa.*`                  |
| **Fanout**  | Игнорирует Routing Key. Отправляет копию сообщения во **все** привязанные очереди | Широковещательная рассылка (чат, обновления)   |
| **Headers** | Маршрутизация по заголовкам сообщения (а не Routing Key)                          | Сложные сценарии, когда ключ не подходит       |

**Пример с Direct:**
- Queue A привязана к Exchange с Binding Key `"pdf_create"`.
- Отправитель отправляет сообщение с Routing Key `"pdf_create"`.
- Exchange типа **Direct** видит точное совпадение → отправляет только в Queue A [](https://www.cloudamqp.com/blog/part4-rabbitmq-for-beginners-exchanges-routing-keys-bindings.html).

---
### Persistence (надёжность) — как хранятся данные

**По умолчанию** сообщения хранятся в оперативной памяти. Если сервер упадёт — сообщения потеряются.

Для надёжности используется **Persistence (сохранение на диск)** [](https://support.huaweicloud.com/intl/en-us/usermanual-rabbitmq/rabbitmq_ug_0010.html)[](https://cloud.tencent.com.cn/document/product/1495/122760).

**Три уровня настройки:**
1. **Exchange** — помечается как Durable (переживёт перезапуск) [](https://support.huaweicloud.com/intl/en-us/usermanual-rabbitmq/rabbitmq_ug_0010.html).
2. **Queue** — помечается как Durable [](https://cloud.tencent.com.cn/document/product/1495/122760).
3. **Message** — при отправке ставится флаг `PERSISTENT_TEXT_PLAIN` [](https://support.huaweicloud.com/intl/en-us/usermanual-rabbitmq/rabbitmq_ug_0010.html)[](https://cloud.tencent.com.cn/document/product/1495/122760).

**Важно:** включение persistence **снижает производительность**, т.к. диск медленнее памяти [](https://cloud.tencent.com.cn/document/product/1495/122760)[](https://support.huaweicloud.com/intl/en-us/usermanual-rabbitmq/rabbitmq_ug_0010.html). Но если данные критичны — оправдано.

---
### Как работает доставка и подтверждение
1. **Producer** отправляет сообщение в Exchange.    
2. Exchange маршрутизирует его в очередь.    
3. **Consumer** забирает сообщение из очереди.    
4. Consumer отправляет **Acknowledgement (подтверждение)**, чтобы RabbitMQ удалил сообщение из очереди [](https://rabbitmq.cn/tutorials/amqp-concepts#exchange-fanout).    

**Если Consumer упал и не подтвердил получение** — сообщение останется в очереди и будет отправлено другому Consumer'у.

---

### Для собеседования: 3 основных преимущества

1. **Слабая связанность (Decoupling):** отправитель не знает получателя [](https://github.com/Netcracker/qubership-rabbitmq/blob/fix/doc_fixes/docs/public/architecture.md).
    
2. **Асинхронность:** обработка не блокирует основной поток.
    
3. **Надёжность:** persistence и подтверждения гарантируют доставку.
    

---

### Частые ошибки (о которых стоит упомянуть)

- Если очередь не привязана к Exchange, сообщение будет потеряно [](https://cloud.tencent.com.cn/developer/article/2572867?policyId=1003).
    
- Не использовать persistence без необходимости (падение производительности) [](https://cloud.tencent.com.cn/document/product/1495/122760).
    
- Не закрывать Connection/Channel корректно (утечка ресурсов).