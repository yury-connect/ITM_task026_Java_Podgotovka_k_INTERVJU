# Grafana Loki

Grafana Loki — это система агрегации логов, которую часто называют «Prometheus для логов»[](https://grafana.com/oss/loki/?source=post_page-----1ac423d7adcb-----------------------------------------#1)[](https://pkg.go.dev/github.com/grafana/loki/v3@v3.3.1#1). Если Prometheus собирает и индексирует метрики, то Loki делает то же самое, но для лог-сообщений.

Ключевое отличие от привычного стека ELK (Elasticsearch, Logstash, Kibana) в том, что Loki **не индексирует содержимое самих логов**. Он индексирует только метаданные — набор меток (labels), которые ты назначаешь каждому потоку логов (например, `app="my-app"`, `level="ERROR"`)[](https://grafana.com/docs/loki/latest/)[](https://tsecurity.de/de/2636483/it+programmierung/mengkonfigurasi+grafana+loki+dan+elk+stack+untuk+logging+terdistribusi/#1). Это делает Loki значительно легче, дешевле в хранении и проще в эксплуатации[](https://github.com/fprh13/boiler-plate-project/issues/47)[](https://cloud.tencent.com.cn/developer/article/2440955?policyId=1004#1).

### 🧩 Из чего состоит стек Loki

Стандартная связка для логирования выглядит так:

**Spring Boot App → (Promtail / Loki4j) → Loki → Grafana**

Есть два основных способа доставить логи из Spring Boot в Loki:

1. **Promtail (Pull-модель)**: Ты настраиваешь приложение писать логи в файл (или stdout в контейнере). Promtail — это агент, который «хвостит» (tail) этот файл, добавляет к записям метки и отправляет их в Loki[](https://github.com/Hi-lingual/Hilingual-Server/issues/12)[](https://zenodo.org/records/20535747/files/recurso-educativo-logs-v1.0.0.pdf?download=1#18#11). Это классический и самый гибкий путь.
    
2. **Loki4j (Push-модель)**: В приложение добавляется Logback-аппендер `loki-logback-appender`, который шлёт логи напрямую в Loki по HTTP, без промежуточного агента[](https://stackoverflow.com/revisions/d36414db-68c5-4773-b686-b02df4adb978/view-source)[](https://cloud.tencent.com.cn/developer/article/2440955?policyId=1004#1). Это проще в настройке для одного сервиса.    

### 📝 Как выглядит настройка для Spring Boot (через Loki4j)

Если ты хочешь отправлять логи напрямую, схема будет такой:

1. **Добавить зависимость** в `pom.xml`:
```xml
<dependency>
	<groupId>com.github.loki4j</groupId>
	<artifactId>loki-logback-appender</artifactId>
	<version>1.4.1</version>
</dependency>
```
	   
2. **Настроить `logback-spring.xml`**. Здесь важно указать URL Loki и, что критично, определить `labels` (метки), по которым ты потом будешь фильтровать логи в Grafana:
```xml
<appender name="LOKI" class="com.github.loki4j.logback.Loki4jAppender">
	<url>http://localhost:3100/loki/api/v1/push</url>
	<labels>
		<label>app</label>
		<value>my-spring-app</value>
		<label>level</label>
		<value>${level}</value>
	</labels>
</appender>
```
В примерах часто советуют включать в метки `app` (*имя приложения*), `level` (*уровень логирования*) и, что важно для трассировки, `traceId`[](https://github.com/hendisantika/spring-boot-kotlin-monitoring#1)[](https://github.com/chz-scout/chz-scout/issues/38).
    
3. **В Grafana** ты добавляешь Loki как источник данных и используешь язык запросов **LogQL** для поиска. Например, `{app="my-spring-app", level="ERROR"}` покажет все ошибки твоего приложения[](https://github.com/anshulbhardwaj123/Log-Visualization-for-Spring-Boot-Application#1#1)[](https://zenodo.org/records/20535747/files/recurso-educativo-logs-v1.0.0.pdf?download=1#18#11).    

### ⚠️ Главное правило Loki: Осторожно с метками (Labels)

Это самый важный момент. Поскольку Loki индексирует только метки, **нельзя помещать в метки значения с высокой кардинальностью** (*много уникальных значений*). Если ты добавишь в метки `userId`, `orderId` или `requestId`, количество уникал-х «*потоков*» в Loki взорвётся, и система начнёт тормозить и жрать ресурсы[](https://grafana.com/docs/loki/latest/).

Правило простое: в метки идут только низкокардинальные измерения, которые ты используешь **в каждом** запросе (`env`, `app`, `level`, `namespace`). Всё остальное, включая `traceId`, лучше вытаскивать из самого текста лога в момент запроса (парсить JSON в LogQL) или использовать **Structured Metadata** (появилась в Loki 3.0), которая позволяет добавлять поля без создания новых потоков[](https://grafana.com/docs/loki/latest/).

### 💡 Как это связано с Prometheus и Grafana

Самое приятное в этом стеке — **сквозная корреляция**. Если в твоих логах есть `traceId`, а в метриках Micrometer Tracing также проставляет этот ID, ты в Grafana можешь переключаться между графиком (Prometheus) и логами (Loki) по одному клику, видя полную картину по конкретному запросу[](https://github.com/hendisantika/spring-boot-kotlin-monitoring#1)[](https://raw.githubusercontent.com/alexschroth/spring-boot-demo-otel-manual/refs/heads/main/README.md#1).

В итоге, Loki — это дополнение к твоей существующей схеме с Prometheus. Prometheus отвечает на вопрос «что случилось?» (метрики упали), а Loki — «почему случилось?» (что писали в логи в этот момент).

---
