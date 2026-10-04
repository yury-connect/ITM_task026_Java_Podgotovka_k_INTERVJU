ELK — это стек технологий для централизованного сбора, хранения и анализа логов. Если Prometheus + Grafana отвечают на вопрос «как работает система прямо сейчас» (метрики), то ELK отвечает на вопрос «что именно произошло» (логи).

Аббревиатура расшифровывается так:

- **E**lasticsearch — база данных для хранения и поиска логов.
    
- **L**ogstash — обработчик и трансформер логов.
    
- **K**ibana — веб-интерфейс для поиска и визуализации.    

### 🔍 Как это работает со Spring Boot

Схема выглядит так:

**Spring Boot App → Filebeat/Logstash → Elasticsearch → Kibana**

Разберём по шагам, что происходит с логом.

**1. Spring Boot: структурированные логи**  
Spring Boot по умолчанию пишет логи в консоль в текстовом формате, который человеку читать удобно, а машине — нет. Чтобы ELK мог эффективно искать по логам, их нужно превратить в **JSON**.

Для этого в `logback-spring.xml` подключается энкодер, который форматирует каждую запись в JSON-объект с полями `timestamp`, `level`, `logger`, `message` и так далее. Можно использовать как нативный Spring Boot формат (`logging.structured.format.console=logstash`)[](https://docs.spring.io/spring-boot/3.4/reference/features/logging.html#features.logging.structured), так и библиотеки вроде `logstash-logback-encoder` или `logback-ecs-encoder` (ECS — Elastic Common Schema)[](https://www.sfeir.dev/back/elasticsearch-spring-boot-premiers-pas-partie-2/#/portal/signin#1).

Пример конфигурации Logback (упрощённо):
```xml
<appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>/app/logs/app.json</file>
    <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <fileNamePattern>/app/logs/app.%d{yyyy-MM-dd}.json</fileNamePattern>
    </rollingPolicy>
</appender>
```
Теперь каждая строка в файле — это валидный JSON.

**2. Сборщик: Filebeat или Logstash**  
Дальше логи нужно забрать из файла и отправить в Elasticsearch. Есть два основных пути:
	
- **Filebeat (лёгкий путь, рекомендуется для большинства случаев):** это лёгкий агент, который ставится на ту же машину, что и приложение, следит за появлением новых строк в лог-файле и отправляет их дальше. Он потребляет мало ресурсов и не требует сложной настройки. Filebeat может слать логи **напрямую в Elasticsearch** или **в Logstash**, если нужна дополнительная обработка[](https://github.com/GabSouza98/logs-and-traces-study#1)[](https://m.yisu.com/ask/60983325.html).
    
- **Logstash (тяжёлый путь, для сложных случаев):** это полноценный процессор данных. Он принимает логи от Filebeat (или по TCP напрямую из приложения), может их парсить, обогащать, фильтровать и только потом отправлять в Elasticsearch. Logstash ест заметно больше RAM и CPU, поэтому его обычно ставят на отдельном сервере, а не рядом с приложением[](https://discuss.elastic.co/t/elastic-agent-vs-logstash-with-filebeat/384556/6)[](https://www.elastic.co/guide/en/cloud/current/ec-cloud-ingest-data.html#1).    

В простом сценарии поток выглядит так:  
`Spring Boot (JSON-логи) → Filebeat (читает файл) → Logstash (парсит/фильтрует) → Elasticsearch`

Но если логи уже в JSON и парсить нечего, можно упростить:  
`Spring Boot (JSON-логи) → Filebeat → Elasticsearch (напрямую)`

**3. Elasticsearch: хранение и поиск**  
Elasticsearch принимает логи и сохраняет их в **индексы** (по сути — коллекции документов). Обычно индексы создаются с разбивкой по дате: `logs-2026.10.04`[](https://github.com/GabSouza98/logs-and-traces-study#1)[](https://github.com/backsuend/Cou-commerce/issues/74). Это позволяет удобно управлять хранением: старые индексы можно удалять или архивировать.

Главная ценность Elasticsearch в том, что по всем полям лога можно очень быстро искать. Например: «показать все логи уровня ERROR за последний час, где `service=payment` и `userId=123`».

**4. Kibana: визуализация и анализ**  
Kibana — это «лицо» всего стека. Она подключается к Elasticsearch и даёт удобный веб-интерфейс для:
	
- **Поиска** по логам (полнотекстовый, по полям, по диапазону дат).
    
- **Построения дашбордов** с графиками: количество ошибок по времени, топ ошибок, распределение по сервисам.
    
- **Настройки алертов** (например, «если больше 10 ERROR за мин. — уведомить»)[](https://github.com/ivangfr/springboot-elk-prometheus-grafana#1).

### 💡 Ключевые нюансы для Spring Boot
	
1. **JSON — обязательно.** Без структурированного формата Kibana будет видеть просто «мешком текста», и поиск по конкретным полям будет невозможен.
    
2. **MDC (Mapped Diagnostic Context) — твой друг.** Spring Boot умеет автоматически добавлять в JSON всё, что вы положили в MDC. Например, `traceId`, `userId`, `requestId` — и тогда в Kibana можно будет найти всю цепочку логов одного запроса[](https://www.sfeir.dev/back/elasticsearch-spring-boot-premiers-pas-partie-2/#/portal/signin#1)[](https://docs.spring.io/spring-boot/3.4/reference/features/logging.html#features.logging.structured).
    
3. **Filebeat проще, чем Logstash.** Для 90% задач хватает Filebeat + ingest pipeline внутри Elasticsearch (можно фильтровать логи прямо в ES, не ставя Logstash)[](https://discuss.elastic.co/t/elastic-agent-vs-logstash-with-filebeat/384556/6)[](https://www.elastic.co/guide/en/cloud/current/ec-cloud-ingest-data.html#1).

Если сравнивать с Prometheus/Grafana: метрики отвечают на вопрос «сколько», а логи — на вопрос «почему». Вместе они дают полную картину.

---
