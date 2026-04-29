Для Middle Java-разработчика на собеседовании по Kubernetes (K8s) важно показать не только знание терминов, но и понимание того, как приложение (особенно на Spring Boot) живет внутри кластера.

Ниже приведен список актуальных вопросов, ранжированный от самых частых к более редким.

Вопросы отсортированы по реальной частоте задавания на технических собеседованиях (2025–2026):

### Самые частые (Core концепции и Health Checks)

- Что такое Kubernetes и какие проблемы он решает по сравнению с простым запуском Docker-контейнеров?
- Что такое Pod? Чем Pod отличается от контейнера и почему нельзя запускать контейнер напрямую в Kubernetes?
- Что такое Deployment? Как он работает и зачем нужен?
- В чём разница между Liveness, Readiness и Startup probes? Как их правильно настроить для Spring Boot приложения с использованием Actuator (/actuator/health/liveness, /actuator/health/readiness)?
- Чем отличаются ConfigMap и Secret? Как управлять конфигурацией Spring Boot приложения в Kubernetes (передача свойств через environment variables)?
- Какие типы Service существуют в Kubernetes (ClusterIP, NodePort, LoadBalancer, Headless)? Когда какой использовать и как один микросервис находит другой внутри кластера?
- Requests vs Limits для ресурсов (CPU и Memory). Какие проблемы возникают с JVM, если превысить Memory Limit (OOMKilled)?
- В чём разница между Deployment и StatefulSet? В каких случаях для Java/Spring Boot микросервиса стоит использовать StatefulSet?
- Как организовать Graceful Shutdown в Spring Boot приложении при удалении или обновлении Pod?
- Что такое Ingress? В чём разница между Ingress и Service (LoadBalancer)? Когда нужен Ingress-контроллер?

### Средняя частота (Resources, Scaling, Storage и Networking)

- Что такое Horizontal Pod Autoscaler (HPA)? По каким метрикам обычно масштабируют Spring Boot приложения (CPU, Memory, custom metrics через Micrometer)?
- Как в Kubernetes хранить данные и временные файлы Spring Boot приложения (emptyDir, hostPath, PersistentVolume и PersistentVolumeClaim)?
- Что такое Init Container и Sidecar? Приведите примеры использования в Java/Spring Boot приложениях (ожидание БД, миграции Flyway/Liquibase, логирование).
- Что такое Namespace и для чего они нужны при работе с несколькими микросервисами?
- Как работает networking в Kubernetes? Как поды общаются между собой и с внешним миром? Что делает kube-proxy и CoreDNS?
- Что такое Rolling Update? Как обновить Spring Boot приложение без downtime (стратегии обновления)?
- Как Kubernetes обеспечивает отказоустойчивость и self-healing приложений?

### Реже, но всё равно важны (Troubleshooting и Ops)

- Как диагностировать проблемы с Pod (ImagePullBackOff, CrashLoopBackOff, постоянные перезапуски)? Какие команды kubectl вы используете (describe, logs, events)?
- Что такое ReplicaSet и как он связан с Deployment?
- Node Selector, Affinity и Anti-Affinity. Как предотвратить попадание двух инстансов одного сервиса на одну ноду?
- Что такое Pod Disruption Budget (PDB) и зачем он нужен?
- Что такое Helm и для чего используют Helm Charts при деплое Spring Boot микросервисов?
- Чем отличаются Labels и Annotations?
- Что такое Network Policies и в каких случаях их применяют (например, ограничение доступа к БД)?

### Редкие / Продвинутые

- Что такое Control Plane и Worker Node? Основные компоненты архитектуры Kubernetes.
- Что делает DaemonSet и в каких случаях его используют?
- Что такое Job и CronJob?
- Что такое Kubernetes Operator и когда имеет смысл его использовать для Java-приложений?
- Объясните разницу между kubectl apply и kubectl create.
- Как настроить сбор и отслеживание логов Spring Boot приложения в Kubernetes (EFK/ELK стек)?

---
**Совет для Middle:** На собеседовании обязательно упоминайте **Spring Boot Actuator** и библиотеку **Spring Cloud Kubernetes**, так как это показывает ваш опыт интеграции Java-кода с инфраструктурой.
