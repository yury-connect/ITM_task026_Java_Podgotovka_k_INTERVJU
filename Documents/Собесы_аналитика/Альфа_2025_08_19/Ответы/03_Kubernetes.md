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
