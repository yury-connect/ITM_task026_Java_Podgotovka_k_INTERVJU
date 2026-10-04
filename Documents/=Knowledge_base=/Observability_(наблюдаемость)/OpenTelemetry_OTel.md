# OpenTelemetry
	
**OpenTelemetry** (*часто сокращают как **OTel***) — это не готовый инструмент для просмотра графиков, а **стандарт и набор библиотек для сбора данных о работе приложения**.
	
Простыми словами: это «переводчик», который приводит данные (*метрики, логи, трейсы*) из твоего приложения к единому виду, чтобы потом их можно было отправить в _любой_ бэкенд (`Prometheus`, `Grafana`, `Datadog`, `Jaeger`) без переписывания кода.

### 🧩 Главная идея: «Инструментируй один раз — отправляй куда угодно»
	
Раньше было так: чтобы отправить метрики в Prometheus, ты использовал библиотеку Prometheus. Чтобы добавить трейсы, подключал Jaeger. Сменить инструмент = переписать код.
	
OpenTelemetry решает эту проблему. Ты один раз пишешь код (или используешь авто-инструментацию), который генерирует телеметрию в стандартном формате **OTLP**. А потом просто меняешь настройку экспортера, чтобы данные улетели в Prometheus, или в Datadog, или в SigNoz [](https://opentelemetry.io/docs/what-is-opentelemetry/?source=post_page-----1c4a05a9e343-----------------------------------------)[](https://engineering.homeoffice.gov.uk/patterns/observability-by-opentelemetry/).

### 📦 Что входит в OpenTelemetry
	
Это не одна библиотека, а целая экосистема [](https://opentelemetry.io/docs/what-is-opentelemetry/?source=post_page-----1c4a05a9e343-----------------------------------------)[](https://opentelemetry.io/ja/docs/what-is-opentelemetry/index.md):
	
1. **API и SDK**: Наборы для разных языков (Java, Go, Python, .NET), которые ты используешь в коде.
    
2. **Стандартный протокол (OTLP)**: Язык, на котором приложение «говорит» с внешним миром.
    
3. **Авто-инструментация (Java Agent)**: Самый магический компонент. Ты просто добавляешь флаг `-javaagent` при запуске Spring Boot, и OpenTelemetry сам начинает собирать метрики HTTP-запросов, JDBC, пулов соединений и т.д., не меняя твой код [](https://hk.opentelemetry.xyz/ja/docs/languages/java/getting-started/)[](https://springframework.org.cn/blog/2025/11/18/opentelemetry-with-spring-boot/#beware-the-context).
    
4. **OpenTelemetry Collector**: Отдельный сервис-«прокси». Он принимает данные от приложений, может их фильтровать, обогащать и отправлять дальше в несколько бэкендов одновременно [](https://coralogix.com/guides/opentelemetry-vs-prometheus/#section-6)[](https://uptrace.dev/comparisons/opentelemetry-vs-prometheus).    

### 🔗 Как это связано с твоей схемой (Spring Boot → Prometheus → Grafana)
	
OpenTelemetry **не заменяет** Prometheus или Grafana. Он встает **перед** ними, как универсальный шлюз.
	
Вот как теперь может выглядеть поток данных:
	
**Spring Boot (с OTel Agent) → OpenTelemetry Collector → Prometheus (или другой бэкенд) → Grafana**

**Что это дает на практике:**
	
- **Единый сбор**: У тебя есть один агент (OTel), который собирает и метрики, и логи, и трейсы. Тебе не нужно три разных инструмента [](https://pkg.go.dev/github.com/abd-ulbasit/forgepoint/pkg/observability#1).
    
- **Свобода миграции**: Если завтра ты решишь уйти с Prometheus на VictoriaMetrics или Datadog, тебе не нужно трогать код Spring Boot. Ты просто перенастраиваешь Collector, чтобы он отправлял данные в новое место [](https://middleware.io/blog/opentelemetry-vs-prometheus/).
    
- **Корреляция**: OpenTelemetry автоматически проставляет `trace_id` и в логи, и в метрики. Это позволяет в Grafana одним кликом перейти от графика с ошибками к конкретному логу этого запроса [](https://docs.cloud.google.com/trace/docs/setup/java-ot?hl=en).    

### ⚠️ Важный нюанс для Spring Boot
	
Spring Boot из коробки использует **Micrometer** (*как мы обсуждали ранее*).  
**Micrometer** — это тоже абстракция, но «*старого*» образца. Сейчас идет активный переход: *Spring Boot 3+* учится отправлять данные через **OTLP** (*протокол OpenTelemetry*) вместо нативных форматов *Prometheus* [](https://springframework.org.cn/blog/2025/11/18/opentelemetry-with-spring-boot/#beware-the-context).

Это значит, что ты можешь использовать привычный *Micrometer API* для добавления своих метрик, но «под капотом» данные будут уходить в формате *OpenTelemetry*, что даст тебе все преимущества стандарта *OTel*.

---
