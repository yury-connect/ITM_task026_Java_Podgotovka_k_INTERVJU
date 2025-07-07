
полный список из **8 этапов CI/CD**, ранжированных по популярности инструментов для каждого этапа:

---

сводная таблица **8 этапов CI/CD** с инструментами, ранжированными по популярности:



таблица CI/CD этапов для **Java-backend разработки** с инструментами, ранжированными по популярности в экосистеме Java:

| Этап            | Инструменты<br/>(от наиболее к менее популярным)                           | Обоснование для Java-backend                                                                  |
| :-------------- | :------------------------------------------------------------------------- | :-------------------------------------------------------------------------------------------- |
| **1. PLAN**     | **JIRA**, **Confluence**, <br/>Trello                                      | Стандарт для Agile-планирования в enterprise Java-проектах.                                   |
| **2. CODE**     | **IntelliJ IDEA**, GitLab/GitHub, <br/>**Maven/Gradle**, Eclipse, Jenkins  | IntelliJ — лидер для Java-разработки; Maven/Gradle — стандарт сборки.                         |
| **3. BUILD**    | **Maven/Gradle**, Docker, **Jenkins**, Bazel, BuildKite                    | Maven/Gradle — основа сборки Java; Jenkins — наиболее распространённый CI-сервер для Java.    |
| **4. TEST**     | **JUnit**, **Mockito**, **TestNG**, Selenium, **JaCoCo**, **REST Assured** | JUnit + Mockito — стандарт unit-тестов; JaCoCo — покрытие кода; REST Assured — тесты API.     |
| **5. DEPLOY**   | **Kubernetes**, **Docker**, **Helm**, Terraform, AWS Lambda                | Kubernetes + Docker — стандарт для контейнеризации Java-приложений; Helm — управление charts. |
| **6. OPERATE**  | **Kubernetes**, **OpenShift**, **Ansible**, Docker Swarm, AWS ECS          | Оркестрация Java-сервисов через K8s/OpenShift; Ansible для конфигурации.                      |
| **7. MONITOR**  | **Prometheus+Grafana**, **ELK Stack**, **Micrometer**, DataDog, Jaeger     | Micrometer интегрирует метрики Java с Prometheus; ELK — анализ логов Spring Boot.             |
| **8. FEEDBACK** | **JIRA**, <br/>**Slack**, **Sentry**, PagerDuty, Opsgenie                  | JIRA для трекинга Java-багов; Sentry для отслеживания исключений в JVM.                       |

---
детализация по каждому инструменту с акцентом на их применение в Java-экосистеме:

| Инструмент         | Описание                                                                                                                                  |
| ------------------ | ----------------------------------------------------------------------------------------------------------------------------------------- |
| **PLAN**           |                                                                                                                                           |
| JIRA               | Система управления задачами от Atlassian. Используется для трекинга Java-багов, спринтов и Agile-планирования (Scrum/Kanban).             |
| Confluence         | Инструмент для документации. Хранит спецификации API (Swagger/OpenAPI), архитектурные решения и требования к Java-сервисам.               |
| Trello             | Упрощённые Kanban-доски для небольших команд. Подходит для бэклога Java-микросервисов.                                                    |
| **CODE**           |                                                                                                                                           |
| IntelliJ IDEA      | IDE №1 для Java. Поддерживает Spring Boot, рефакторинг, дебаггинг, интеграцию с Maven/Gradle.                                             |
| GitLab/GitHub      | Хостинг кода + CI/CD. GitLab CI/CD/GitHub Actions для автоматизации сборки Java-проектов.                                                 |
| Maven/Gradle       | **Maven**: XML-конфигурация, стандарт для enterprise. **Gradle**: Kotlin/DSL, гибкость, ускоренная сборка (кэширование).                  |
| **BUILD**          |                                                                                                                                           |
| Jenkins            | CI-сервер с 500+ плагинами для Java. Автоматизирует сборку (Maven/Gradle), артефакты в Nexus/Artifactory.                                 |
| Docker             | Контейнеризация Java-приложений. Создание образов на базе OpenJDK, уменьшение размера через multi-stage build.                            |
| Bazel              | Сборка от Google. Ускоряет инкрементальные сборки больших Java-монопоек (кеширование зависимостей).                                       |
| **TEST**           |                                                                                                                                           |
| JUnit              | Фреймворк для unit-тестов. Интеграция с Spring Boot Test, параметризованные тесты, поддержка JUnit Jupiter (JUnit 5).                     |
| Mockito            | Мокирование зависимостей в тестах. Позволяет изолировать тестируемый Java-код (например, сервисный слой без БД).                          |
| JaCoCo             | Анализ покрытия кода. Генерация отчётов для SonarQube, контроль порогов coverage в сборках Maven/Gradle.                                  |
| **DEPLOY**         |                                                                                                                                           |
| Kubernetes         | Оркестратор контейнеров. Управление Spring Boot-приложениями в кластере (Deployments, Services, ConfigMaps для application.yml).          |
| Helm               | Менеджер чартов для Kubernetes. Шаблонизация деплоя Java-приложений (настройка переменных для разных сред: dev/stage/prod).               |
| Terraform          | Infrastructure as Code (IaC). Автоматизированное создаение AWS/GCP ресурсов (EKS, VPC, RDS) для Java-сервисов.                            |
| **OPERATE**        |                                                                                                                                           |
| OpenShift          | Enterprise-платформа на базе Kubernetes (Red Hat). Упрощает управление Java-микросервисами, встроенный мониторинг, CI/CD.                 |
| Ansible            | Автоматизация конфигурации серверов. Развёртывание JVM, настройка Tomcat/WildFly для legacy Java-приложений.                              |
| **MONITOR**        |                                                                                                                                           |
| Prometheus+Grafana | **Prometheus**: сбор метрик JVM (через Micrometer), мониторинг CPU/GC. **Grafana**: дашборды для Spring Boot Actuator endpoints.          |
| ELK Stack          | **Elasticsearch**: хранение логов Logback/Log4j. **Kibana**: визуализация. **Logstash**: парсинг Java-логфайлов (стектрейсы, исключения). |
| Micrometer         | Библиотека для экспорта метрик из Java-приложений в Prometheus. Интеграция с Spring Boot Actuator.                                        |
| **FEEDBACK**       |                                                                                                                                           |
| Sentry             | Отслеживание исключений в JVM в реальном времени. Интеграция с Spring, Slack-оповещения при ошибках в Java-коде.                          |
| PagerDuty          | Управление инцидентами. Автоматические алерты при падении Java-сервисов (интеграция с Prometheus/Grafana).                                |

---
### Ключевые связки для Java:

1. **Сборка**:  
    `Maven/Gradle + Jenkins + Docker` → Сборка JAR-артефакта, упаковка в Docker-образ, публикация в registry.
    
2. **Тестирование**:  
    `JUnit + Mockito + JaCoCo` → Покрытие unit-тестами >80%, проверка интеграций с моками, контроль качества.
    
3. **Деплой**:  
    `Helm + Kubernetes + Terraform` → Развёртывание Java-приложения с настройками через values.yaml, инфраструктура от Terraform.
    
4. **Мониторинг**:  
    `Micrometer + Prometheus + Grafana` → Метрики GC-пауз, времени ответа API, алертинг при высокой загрузке CPU JVM.
    

> Источники: [JetBrains Java Developer Survey 2024](https://www.jetbrains.com/lp/devecosystem-2024/java/), [Spring One 2025 Report](https://spring.io/blog/2025/04/15/springone-report).