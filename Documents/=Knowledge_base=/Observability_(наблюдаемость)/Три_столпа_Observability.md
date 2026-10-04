1. **Метрики** — самые дешевые в хранении и самые быстрые в запросе. Отвечают на вопрос: **«Что-то сломалось? Где именно?»**
    
2. **Логи** — дороже, но дают контекст. Отвечают: **«Почему сломалось? Что именно произошло?»**
    
3. **Трейсы** — самые дорогие (нужно хранить и сэмплировать), но показывают путь запроса. Отвечают: **«Где в цепочке микросервисов затык?»**

---
**Observability (наблюдаемость)** — это ==**свойство IT-системы, которое позволяет оценивать её внутреннее состояние и понимать причины сбоев на основе внешних данных**==. Если говорить простыми словами, observability дает ответ не просто на вопрос _«Что сломалось?»_, а на вопрос _«Почему это сломалось?»_, помогая распутывать сложные аномалии в современных распределенных приложениях и микросервисах. [1](https://habr.com/ru/companies/slurm/articles/713196/), [2](https://javarush.com/quests/lectures/ru.javarush.java.spring.lecture.level26.lecture01), [3](https://slurm.io/blog/tpost/0lg5cxbua1-observability-kak-filosofiya)

Чем *Observability* отличается от *мониторинга*

Эти понятия часто путают, но между ними есть принципиальная разница: [1](https://habr.com/ru/companies/monq/articles/878278/), [2](https://habr.com/ru/companies/slurm/articles/713196/)

- **Мониторинг (Monitoring)** — это сбор заранее определенных метрик для выявления известных проблем. Он сигнализирует о факте аварии: _«База данных перегружена»_ или _«Сервер упал»_. [1](https://habr.com/ru/companies/selectel/articles/885890/), [2](https://aws.amazon.com/ru/compare/the-difference-between-monitoring-and-observability/), [3](https://habr.com/ru/companies/slurm/articles/713196/), [4](https://slurm.io/blog/tpost/0lg5cxbua1-observability-kak-filosofiya)
- **Наблюдаемость (Observability)** — это более глубокий подход. Она позволяет расследовать новые, непредсказуемые сценарии поведения системы (так называемые «неизвестные неизвестные»), агрегируя и связывая между собой разнородные данные со всех слоев инфраструктуры. [1](https://riverbed.bakotech.com/ru/monitoring-visibility-observability-is-there-any-difference-in-concepts), [2](https://habr.com/ru/companies/slurm/articles/713196/), [3](https://aws.amazon.com/ru/compare/the-difference-between-monitoring-and-observability/)

|Критерий|Мониторинг|Observability|
|---|---|---|
|**Основной вопрос**|Что и когда сломалось?|Почему это произошло?|
|**Характер работы**|Реактивный (реагирует на триггеры)|Проактивный (помогает исследовать систему)|
|**Тип проблем**|Известные паттерны отказов|Уникальные аномалии в микросервисах|

Три столпа *Observability* («M.E.L.T.»)

Полноценная наблюдаемость строится на сборе и сквозной корреляции трех основных типов данных: [1](https://habr.com/ru/companies/slurm/articles/713196/), [2](https://habr.com/ru/companies/selectel/articles/885890/)

1. **Метрики (Metrics):** Числовые показатели, измеряемые за интервалы времени (загрузка CPU, количество запросов в секунду (RPS), уровень ошибок 5xx). Показывают, _что_ происходит с ресурсами прямо сейчас. [1](https://slurm.io/blog/tpost/0lg5cxbua1-observability-kak-filosofiya), [2](https://habr.com/ru/companies/selectel/articles/885890/)
2. **Логи (Logs):** Текстовые записи конкретных событий в приложении с временной меткой. Помогают детализировать, _где_ именно возникла ошибка в коде. [1](https://habr.com/ru/companies/selectel/articles/885890/), [2](https://slurm.io/blog/tpost/0lg5cxbua1-observability-kak-filosofiya)
3. **Трассировки (Traces):** Хронологические цепочки прохождения одного пользовательского запроса через десятки разных микросервисов, очередей и баз данных. Трассировка склеивает весь путь с помощью уникальных ID (`Trace ID`), позволяя увидеть, на каком именно этапе запрос «застрял» или сломался. [1](https://habr.com/ru/companies/rwb/articles/995330/), [2](https://slurm.io/blog/tpost/0lg5cxbua1-observability-kak-filosofiya)

Популярные инструменты

Для реализации observability инженеры используют специализированные стеки программного обеспечения: [1](https://habr.com/ru/companies/selectel/articles/885890/)

- **Сбор и оркестрация данных:** Стандартом де-факто стал проект [OpenTelemetry](https://opentelemetry.io/).
- **Работа с метриками:** [Prometheus](https://prometheus.io/) и панели визуализации [Grafana](https://grafana.com/).
- **Хранение логов:** [Grafana Loki](https://grafana.com/oss/loki/), [Elasticsearch](https://www.elastic.co/) или [OpenSearch](https://opensearch.org/).
- **Распределенная трассировка:** [Jaeger](https://www.jaegertracing.io/) и [Grafana Tempo](https://grafana.com/oss/tempo/). [1](https://habr.com/ru/companies/selectel/articles/885890/), [2](https://slurm.io/blog/tpost/0lg5cxbua1-observability-kak-filosofiya)

Вы изучаете концепцию **observability** для внедрения в проект или готовитесь к собеседованию? Я могу детальнее рассказать про **сквозную трассировку** или посоветовать **конкретный стек инструментов** под ваш язык программирования.

---
