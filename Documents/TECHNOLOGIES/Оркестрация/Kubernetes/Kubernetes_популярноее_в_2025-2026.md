Уровень — не DevOps-инженер, а именно разработчик, который сам деплоит, отлаживает и поддерживает свои микросервисы.

### Самые часто используемые элементы Kubernetes (для Java Middle)

|№|Элемент Kubernetes|Частота использования|Что именно делает Java-разработчик|Примеры команд / файлов|
|---|---|---|---|---|
|1|**Deployment**|★★★★★ (каждый день)|Деплой приложения, rolling update, откат, масштабирование реплик|kubectl apply -f deployment.yaml|
|2|**Pod**|★★★★☆|Смотрит логи, exec в под, описывает под, понимает почему под в CrashLoopBackOff / Error / Pending|kubectl logs, kubectl exec -it, kubectl describe pod|
|3|**Service** (ClusterIP / LoadBalancer)|★★★★☆|Подключает приложение к другим сервисам, настраивает ClusterIP для внутренних вызовов|kubectl get svc, kubectl port-forward|
|4|**ConfigMap**|★★★★☆|Хранит application.yml / properties / feature flags / внешние конфиги|kubectl create configmap, volume mount|
|5|**Secret**|★★★★☆|Хранит пароли, API-ключи, JWT-секреты, сертификаты|kubectl create secret generic, base64|
|6|**HorizontalPodAutoscaler (HPA)**|★★★☆☆|Настраивает автоскейлинг по CPU / памяти / custom metrics (Prometheus)|kubectl autoscale deployment ... --cpu-percent=70|
|7|**Ingress / IngressClass**|★★★☆☆|Настраивает внешний доступ (часто через nginx-ingress или traefik)|kubectl get ingress, аннотации path/rewrite|
|8|**Namespace**|★★★☆☆|Разделяет dev / stage / prod, изолирует ресурсы|kubectl get ns, --namespace=dev|
|9|**Resource Requests & Limits**|★★★☆☆|Обязательно указывает в манифестах (CPU/memory), иначе может быть OOMKilled или throttled|resources: requests: {cpu: "300m", memory: "512Mi"}|
|10|**livenessProbe / readinessProbe**|★★★☆☆|Настраивает health-check'и (HTTP GET /actuator/health чаще всего)|livenessProbe: httpGet: path: /actuator/health|
|11|**CronJob**|★★☆☆☆|Периодические задачи (чистка, отчёты, батчи)|kubectl create cronjob ...|
|12|**StatefulSet**|★★☆☆☆|Если нужен Kafka, Redis, PostgreSQL в кластере (редко для чистых Java-сервисов)|StatefulSet + headless service|
|13|**PersistentVolumeClaim (PVC)**|★★☆☆☆|Если нужен постоянный диск (очень редко для stateless Java)|volumeMounts + persistentVolumeClaim|

---
### Самые популярные инструменты, которыми пользуется Java Middle в 2025–2026

|Инструмент / CLI|Для чего используется|Частота у Java Middle|Альтернативы / комментарий|
|---|---|---|---|
|**kubectl**|Основной инструмент — всё через него|★★★★★|Без него никуда|
|**k9s**|TUI-интерфейс (очень удобный просмотр подов, логов, describe)|★★★★☆|Самый популярный среди разработчиков|
|**Lens** / **OpenLens**|Графический десктоп-клиент (Kubernetes IDE)|★★★☆☆|Удобно для новичков и когда много кластеров|
|**Helm**|Деплой чартов (часто готовые: postgres, keycloak, redis)|★★★☆☆|Сам деплоит редко, но применяет готовые чарты часто|
|**Skaffold** / **Tilt**|Локальная разработка + hot-reload в minikube / kind|★★★☆☆|Skaffold — самый популярный среди Java-разработчиков|
|**kind** / **minikube**|Локальный Kubernetes для отладки|★★★★☆|kind быстрее и легче|
|**kubectx** + **kubens**|Быстрое переключение контекста / namespace|★★★★☆|Экономит очень много времени|
|**stern** / **kubetail**|Многострочные логи нескольких подов одновременно|★★★☆☆|stern сейчас популярнее|
|**kubelog** / **k logs**|Простой просмотр логов|★★★★★|kubectl logs -f deployment/my-app|
|**IntelliJ Kubernetes plugin**|Просмотр ресурсов прямо из IDEA|★★☆☆☆|Удобно, если не хочется выходить из IDE|
|**Prometheus + Grafana**|Метрики приложения (Actuator + Micrometer)|★★★☆☆|Чаще всего смотрят /actuator/prometheus → Grafana|
|**ArgoCD** / **Flux**|GitOps (часто разработчик смотрит, но не настраивает)|★★☆☆☆|Смотрит статус приложения в UI|

---
### Самый типичный ежедневный стек Java Middle (2025–2026)

```text
kubectl + k9s + kind/minikube (локально)
Helm (для внешних зависимостей)
Skaffold / Tilt (для быстрого dev-loop)
ConfigMap + Secret + Deployment + Service + HPA + Probes
Spring Boot Actuator + Micrometer + Prometheus
Grafana (смотреть свои метрики и алерты)
```

Если коротко: **90% времени** — это **Deployment, Pod, Service, ConfigMap/Secret, Probes, kubectl + k9s**.

Остальное — по ситуации (HPA, Ingress, CronJob, StatefulSet — гораздо реже).

---
