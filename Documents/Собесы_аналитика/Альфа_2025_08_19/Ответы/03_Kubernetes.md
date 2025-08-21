Дам «уровень **миддла** с **DevOps-насмотренностью**»: чётко, по шагам, без воды.

---
# `Kubernetes` — моя вовлечённость
**Уровень:** уверенный разработчик, умею деплоить через [Helm](../../../TECHNOLOGIES/Оркестрация/Kubernetes/Helm/Helm_info.md), читать манифесты (Deployment/Service/Ingress/ConfigMap/Secret), настраивать probes/ресурсы/HPA, делать rollback/rollout status, смотреть логи/ивенты. Работаю через Jenkins → Helm → k8s.

---
## 1) Чем деплоилось в `Kubernetes`?

- 🧰 **[Helm](../../../TECHNOLOGIES/Оркестрация/Kubernetes/Helm/Helm_info.md)**: собственный **Helm-chart** сервиса (*база от DevOps, я допиливал values, probes, ресурсы*).
    
- 🧩 В чарте: `Deployment`, `Service (ClusterIP)`, `Ingress (nginx)`, `ConfigMap`, `Secret`, `HPA`, `ServiceAccount` + **Helm-hooks** для Liquibase job (pre-upgrade).
    
- 📦 Registry: Harbor/Artifactory (*образ из CI*).    

---
## 2) Механизм: есть `Docker`-образ →<br>→ как он попадает в `k8s`

1. Образ запушен в `registry` с тегом `app:gitSha` (или `buildNumber`).
    
2. **Jenkins** вызывает `helm upgrade --install` с `values-test.yaml`, пробрасывая `image.tag`.
    
3. `Deployment` получает новый `ReplicaSet`, стратегия **RollingUpdate** (`maxSurge=1`, `maxUnavailable=0`).
    
4. `kubelet` тянет образ (`imagePullSecret`), поды стартуют.
    
5. Срабатывают **liveness/readiness** probes → только **Ready** поды попадают за **Service**.
    
6. **Service** даёт стабильный endpoint; **Ingress** публикует URL на тестовом домене.
    
7. Helm-hook запускает **Liquibase Job** (если есть миграции), ждём успеха → завершаем релиз.    

---
## 3) После merge в `test`: что я делаю, чтобы релиз оказался на тестовом стенде

- 🔁 **Ничего вручную не прошу**: раскатка **автоматическая**. Merge в `test` триггерит Jenkins-pipeline **CD** на `test` namespace.
    
- ✅ Я **мониторю** пайплайн, проверяю `helm release` и readiness, смотрю **smoke-тесты** и оповещение в Slack/Teams.
    
- 🧪 Далее сам **дергаю API** на тестовом URL (Swagger, Postman), проверяю графики/логи.    

---
## 4) Полная цепочка «написал код → стал доступен на стенде» (глубоко)

1. **Commit/PR → review → merge в `test`**.
    
2. **Jenkins CI**:    
    - `mvn clean verify` (юнит/интеграционные тесты).        
    - Сборка Docker-образа `app:gitSha`.        
    - (Опционально) **Trivy**/scan образа.        
    - Push в **registry**.
    
3. **Jenkins CD** (тот же pipeline либо downstream job):    
    - `helm upgrade --install app charts/app -f values-test.yaml --set image.tag=gitSha`        
    - **Helm pre-hook**: `Job` с **Liquibase** (миграции схемы).        
    - Обновление **Deployment** → создаётся новый **ReplicaSet**.        
    - **RollingUpdate**: старые поды постепенно заменяются новыми.        
    - **Probes**: readiness/liveness (например, `/actuator/health`).        
    - **Service** уже привязан к селекторам; как только поды `Ready`, трафик идёт на новую версию.        
    - **Ingress** даёт внешний доступ: `https://test.company.local/my-service`.
    
4. **Smoke-тесты** (Postman/Newman/REST-assured) запускаются из Jenkins.
    
5. **Наблюдаемость**:    
    - Логи в **ELK** (Kibana).        
    - Метрики в **Prometheus/Grafana** (RPS, latency, 5xx, JVM).        
    - Алерты/нотификации в Slack/Teams.
    
6. **Конфиги/секреты**:    
    - `ConfigMap` для non-secret (feature flags, URLs).        
    - `Secret` для паролей/ключей (Kafka, Postgres, SMTP), монтируются как env.
    
7. **Доступ разработчика**:    
    - На `dev` — kubectl RW; на `test/stage` — чаще RO. Операции деплоя делает **Jenkins SA** через RBAC.
    
8. **Rollback**:    
    - `helm rollback <release> <revision>` либо задаём прошлый `image.tag`, после чего тот же rolling update назад.
    
9. **Безопасность**:    
    - `SecurityContext`, `readOnlyRootFilesystem`, ограничение capabilities, `NetworkPolicy`.
    
10. **Ресурсы и авто-масштабирование**:    

- `resources.requests/limits`, **HPA** по CPU/RAM/кастомным метрикам.    

---
## 5) Кто инициирует поставку на тест и как именно

- 🧷 **Триггер — merge в `test`** (инициатор поставки — разработчик, который смержил PR).
    
- 🧑‍💼 **Ручных согласований** для `test` нет; для `stage/prod` — manual approval (тимлид/DevOps).
    
- 🔐 Jenkins использует **ServiceAccount** с ограниченными правами на `test` namespace.    

---
## 6) Кто писал pipeline/чарты и моя роль

- 🛠️ **Helm-chart, Jenkinsfile** — базово DevOps.
    
- ✍️ Я редактировал **values** (ресурсы, probes, env), добавлял **Helm-hook** под Liquibase, настраивал **readiness** (чтобы не пускать трафик, пока не прогрелся пул, не поднялась Kafka/Redis), правил **Jenkinsfile** (smoke-тесты, нотификации, fail-fast).    

---
## 7) Как API «становится доступным» и как я его проверяю

- 🌐 **Ingress** публикует маршрут на тестовом домене, например `https://test.company.local/notify`.
    
- 📜 **Swagger/OpenAPI** доступен по `…/swagger-ui` (либо через API-gateway).
    
- 👀 Проверки:
    
    - `GET /actuator/health` (readiness).
        
    - Набор **smoke-эндпоинтов** (Postman коллекция) — 200/4xx/5xx поведение.
        
    - Логи и трейсинг (корреляция по `X-Request-Id`).
        
    - Kafka round-trip (если событийная часть задействована).
        

---
### Резюме для интервьюера
- Использую **Helm-базированный CD** через **Jenkins**; понимаю весь путь: image → Deployment/RS → Probes → Service → Ingress.
    
- Умею **диагностировать** (events/logs/describe), **рулить** миграциями (Helm-hooks/Liquibase), **делать rollback**, настраивать ресурсы/HPA и **observability**.

---
---
---
## 👌 Вот «живой» вариант ответа (*60–90 сек*), которым ты можешь отбиться на собесе:

---

**Ответ кандидата:**

«После того как я смержил изменения в `test`, автоматически запускался Jenkins-пайплайн.  
Он собирал проект через Maven, прогонял тесты, собирал Docker-образ, пушил его в registry и запускал Helm-деплой в Kubernetes.

Для деплоя у нас был свой Helm-chart: в нём описаны Deployment, Service, Ingress, ConfigMap и Secret.  
Helm upgrade создавал новый ReplicaSet, шёл rolling-update, readiness-probes проверяли готовность подов. Когда они переходили в Ready, трафик начинал идти через Service и Ingress на новый билд.

Доступ на тестовый стенд появлялся по тестовому домену, я сам мог зайти в Swagger или Postman и проверить API. Логи и метрики смотрели через ELK и Prometheus/Grafana.

Таким образом, как только я делал merge в `test`, через CI/CD изменения автоматически попадали на тестовый стенд без ручных действий.»

---
---
---
**Чек-лист ключевых терминов по Kubernetes**, которые обязательно нужно упомянуть на собесе (и уметь в 1 фразе объяснить). Это создаёт впечатление уверенного «миддла» 🚀

---
# ✅ Чек-лист Kubernetes для собеса

### 🔹 Базовое
- **Pod** — минимальная единица развертывания, контейнер(ы) с общим IP/volume.    
- **Deployment** — управляет ReplicaSet и rolling update приложения.    
- **ReplicaSet** — набор подов, следит за количеством реплик.    
- **Service (ClusterIP, NodePort, LoadBalancer)** — балансировка и доступ к подам.    
- **Ingress** — маршрутизация HTTP(S)-трафика, тестовый домен → сервис.    

---
### 🔹 Конфиги и данные
- **ConfigMap** — хранение конфигураций (не секретных).    
- **Secret** — хранение паролей, токенов, ключей.    
- **Volumes / PersistentVolumeClaim** — подключение диска к подам.    

---
### 🔹 Надёжность и стабильность
- **Liveness probe** — проверка «жив ли контейнер», перезапуск если нет.    
- **Readiness probe** — готов ли принимать трафик.    
- **Startup probe** — для долгого старта (например, JVM).    
- **RollingUpdate** — поэтапное обновление без простоя.    
- **Rollback** — откат на предыдущую версию (helm rollback).    

---
### 🔹 Масштабирование
- **Horizontal Pod Autoscaler (HPA)** — авто-масштабирование по CPU/RAM/метрикам.
- **Resources requests/limits** — гарантированный и максимальный ресурс для пода.    

---
### 🔹 CI/CD и управление
- **Helm chart** — пакет манифестов (Deployment, Service, Ingress, ConfigMap, Secret, HPA).    
- **Helm values.yaml** — параметры окружения (тест, прод).    
- **Helm hooks** — pre/post действия (например, запуск Liquibase).
- **kubectl describe / logs / get events** — базовые команды диагностики.    

---
### 🔹 Observability
- **Prometheus/Grafana** — метрики (RPS, latency, 5xx, CPU, memory).    
- **ELK (Elasticsearch + Logstash + Kibana)** — централизованные логи.    
- **Tracing (Jaeger/Zipkin)** — распределённые трассировки.    

---

⚡ Если ты в ответах упоминаешь эти термины — ты звучишь как уверенный разработчик, понимающий DevOps-практику.

---
---
---
**мини-Q/A по Kubernetes-терминам** — быстрое повторение перед собесом.

---
# 📝 Mini Q/A Kubernetes

### 🔹 Pod / Deployment / ReplicaSet

**Q:** Что такое Pod?  
**A:** Минимальная единица в Kubernetes — один или несколько контейнеров, которые разделяют сеть и volume.

**Q:** Чем Deployment отличается от ReplicaSet?  
**A:** ReplicaSet следит за количеством подов, а Deployment управляет ReplicaSet и реализует rolling-update и rollback.

---
### 🔹 Service / Ingress

**Q:** Для чего нужен Service?  
**A:** Абстракция для доступа к подам. Balancer внутри кластера. Типы: ClusterIP (внутри кластера), NodePort (через порт ноды), LoadBalancer (через внешний LB).

**Q:** Зачем Ingress?  
**A:** Для маршрутизации HTTP(S)-запросов по доменному имени и пути. Даёт доступ к сервису извне.

---
### 🔹 ConfigMap / Secret

**Q:** Разница между ConfigMap и Secret?  
**A:** ConfigMap хранит обычные конфиги, Secret — чувствительные данные (пароли, ключи, токены), которые шифруются в etcd.

---
### 🔹 Probes

**Q:** Разница между liveness, readiness и startup probe?  
**A:**
- **Liveness** — жив ли контейнер, иначе рестарт.    
- **Readiness** — готов ли обрабатывать запросы (иначе под не включают в Service).    
- **Startup** — проверка долгого старта (например, JVM).    

---
### 🔹 Масштабирование

**Q:** Что делает HPA?  
**A:** Автоматически увеличивает/уменьшает количество подов в Deployment по метрикам (CPU/RAM или кастомные).

**Q:** Для чего нужны requests/limits?  
**A:** Requests — гарантированный ресурс пода. Limits — максимальный. Это помогает планировать нагрузку и избежать OOMKilled.

---
### 🔹 CI/CD

**Q:** Что такое Helm chart?  
**A:** Набор шаблонов манифестов Kubernetes (Deployment, Service, Ingress и др.) + values.yaml для параметризации.

**Q:** Helm values.yaml — зачем нужен?  
**A:** Хранит настройки для разных окружений (test, stage, prod). Позволяет одним chart’ом деплоить в разные среды.

**Q:** Helm hooks для чего?  
**A:** Для выполнения действий до или после релиза (например, миграция базы через Liquibase job).

---
### 🔹 Observability

**Q:** Как смотрите логи в Kubernetes?  
**A:** Через `kubectl logs`, а централизованно — в ELK (Kibana).

**Q:** Как собираете метрики?  
**A:** Prometheus собирает, Grafana визуализирует (RPS, latency, ошибки, ресурсы).

**Q:** Для чего нужны трассировки (Jaeger/Zipkin)?  
**A:** Чтобы видеть полный путь запроса через микросервисы и выявлять узкие места.

---
⚡ Если ты на собесе **на лету и чётко** произносишь такие ответы — интервьюер понимает: «Ок, человек реально работал с Kubernetes, а не только слышал».

---
---
---
**Супер-краткая карточка по `Kubernetes`** — 2 минуты на повторение перед собесом:

# 🚀 Kubernetes Cheat-Card (*Ultra Short*)

- **Pod** → минимальная единица (контейнеры + сеть + volume).    
- **ReplicaSet** → следит за количеством подов.    
- **Deployment** → управляет ReplicaSet, rolling update + rollback.    
- **Service** → доступ к подам (ClusterIP / NodePort / LoadBalancer).    
- **Ingress** → роутинг HTTP(S) по домену/пути.    
- **ConfigMap** → обычные конфиги.    
- **Secret** → пароли, токены (шифруются в etcd).    
- **Liveness probe** → жив ли контейнер (рестарт).    
- **Readiness probe** → готов к трафику (иначе не в Service).    
- **Startup probe** → для долгого старта (JVM, БД).    
- **HPA** → авто-масштабирование по CPU/RAM/метрикам.    
- **Requests / Limits** → гарантированные и максимальные ресурсы пода.    
- **Helm chart** → пакет манифестов (Deployment, Service, Ingress и т.д.).    
- **values.yaml** → настройки для окружений (test/stage/prod).    
- **Helm hooks** → pre/post действия (например, Liquibase миграции).    
- **kubectl logs / describe / get events** → диагностика.    
- **ELK** → централизованные логи.    
- **Prometheus / Grafana** → метрики и дашборды.    
- **Jaeger / Zipkin** → распределённые трассировки.

---
