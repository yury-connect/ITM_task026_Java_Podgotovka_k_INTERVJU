кто из них хранит данные только в памяти, а кто на диске ?

- **Kafka** — это **дисковый** брокер. **Все** сообщения по умолчанию сразу записываются на диск и хранятся там столько, сколько вы настроите, даже после того, как их прочитали[](https://cloud.tencent.cn/developer/article/2427061)[](https://stackshare.io/stackups/gearman-vs-kafka-vs-rabbitmq)[](https://unogeeks.com/kafka-and-rabbit-mq/#content). Это её главная фишка — надёжное долгосрочное хранение и возможность "перемотать" историю событий[](https://cloud.tencent.cn/developer/article/2427061)[](https://t1-cloud.ru/blog/brokery-soobshchenij-kafka-i-rabbitmq-v-realnoj-zhizni).
    
- **RabbitMQ** — это **в основном "оперативно-памятный"** брокер. По умолчанию сообщения живут в оперативной памяти и удаляются сразу после обработки[](https://cloud.tencent.cn/developer/article/2427061)[](https://stackshare.io/stackups/gearman-vs-kafka-vs-rabbitmq)[](https://qna.habr.com/q/1046954). Однако он **может** сохранять данные на диск, но:
	    
    - Это нужно включать вручную (делать очередь `durable`, а сообщения помечать как `persistent`)[](https://t1-cloud.ru/blog/brokery-soobshchenij-kafka-i-rabbitmq-v-realnoj-zhizni).
        
    - Это снижает производительность и не является основным сценарием использования[](https://cloud.ru/docs/en-ru/usermanual/rabbitmq/rabbitmq_ug_0009)[](https://t1-cloud.ru/blog/brokery-soobshchenij-kafka-i-rabbitmq-v-realnoj-zhizni).
        
    - Если оперативная память заканчивается, он _может_ начать сбрасывать данные на диск для освобождения места (это называется "paging"), но это скорее аварийный механизм, а не норма работы[](https://cloud.ru/docs/en-ru/usermanual/rabbitmq/rabbitmq_ug_0009).

