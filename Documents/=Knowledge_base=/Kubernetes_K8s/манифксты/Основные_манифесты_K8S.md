Манифесты Kubernetes — это YAML-файлы, которые описывают **желаемое состояние** вашего приложения в кластере. Вы не говорите Kubernetes «сделай это сейчас», вы говорите «я хочу, чтобы всегда было так» — а он уже сам приводит систему к этому состоянию [](https://learn.microsoft.com/pt-br/training/modules/deploy-apps-azure-kubernetes-service/2-create-deployment-manifests?ns-enrollment-type=learningpath&ns-enrollment-id=learn.wwl.deploy-monitor-apps-azure-kubernetes-service#1)[](https://learn.microsoft.com/it-it/training/modules/deploy-apps-azure-kubernetes-service/2-create-deployment-manifests#1).

## Deployment — описание приложения
**Deployment** отвечает за запуск и поддержание нужного количества копий (*Pod*) вашего приложения. Если Pod упадёт, Deployment создаст новый. Если нужно обновить версию — сделает это постепенно [](https://learn.microsoft.com/pt-br/training/modules/deploy-apps-azure-kubernetes-service/2-create-deployment-manifests?ns-enrollment-type=learningpath&ns-enrollment-id=learn.wwl.deploy-monitor-apps-azure-kubernetes-service#1).

**Основные поля:**
	
- **replicas** — сколько копий приложения запускать. Для отказоустойчивости обычно 2–3 [](https://learn.microsoft.com/pt-br/training/modules/deploy-apps-azure-kubernetes-service/2-create-deployment-manifests?ns-enrollment-type=learningpath&ns-enrollment-id=learn.wwl.deploy-monitor-apps-azure-kubernetes-service#1)[](https://learn.microsoft.com/it-it/training/modules/deploy-apps-azure-kubernetes-service/2-create-deployment-manifests#1).
    
- **selector** — по каким меткам *Deployment* находит «свои» Pod.
    
- **template** — шаблон для создания *Pod*: какой образ использовать, какие порты открыть, какие ресурсы выделить.
    
- **image** — путь к Docker-образу в реестре (например, `myregistry.azurecr.io/inference-api:v1.0`) [](https://learn.microsoft.com/pt-br/training/modules/deploy-apps-azure-kubernetes-service/2-create-deployment-manifests?ns-enrollment-type=learningpath&ns-enrollment-id=learn.wwl.deploy-monitor-apps-azure-kubernetes-service#1).    

Пример из практики:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ai-inference-api
spec:
  replicas: 2
  selector:
    matchLabels:
      app: inference-api
  template:
    metadata:
      labels:
        app: inference-api
    spec:
      containers:
      - name: api
        image: myregistry.azurecr.io/inference-api:v1.0
        ports:
        - containerPort: 8080
```

## Service — доступ к приложению
Pod'ы в Kubernetes недолговечны: их IP-адреса меняются при пересоздании. **Service** даёт стабильную точку входа и распределяет трафик между Pod'ами [](https://v1-32.docs.kubernetes.io/docs/tutorials/kubernetes-basics/expose/expose-intro/).

**Типы Service:**
- **ClusterIP** (по умолчанию) — доступен только внутри кластера [](https://v1-32.docs.kubernetes.io/docs/tutorials/kubernetes-basics/expose/expose-intro/)[](https://docs.cloud.google.com/kubernetes-engine/docs/concepts/service?hl=pt-Br).    
- **NodePort** — откр-ет порт на каждй ноде, доступен извне по `NodeIP:NodePort` [](https://docs.cloud.google.com/kubernetes-engine/docs/concepts/service?hl=pt-Br).    
- **LoadBalancer** — создаёт облачный балансировщик с внешним IP [](https://v1-32.docs.kubernetes.io/docs/tutorials/kubernetes-basics/expose/expose-intro/).    
- **ExternalName** — просто DNS-алиас на внешний ресурс [](https://docs.cloud.google.com/kubernetes-engine/docs/concepts/service?hl=pt-Br).    

**Ключевые поля:**
- **selector** — по каким меткам Service находит Pod'ы.    
- **port** — порт, на котором Service слушает.    
- **targetPort** — порт, на котором слушает контейнер внутри Pod [](https://docs.cloud.google.com/kubernetes-engine/docs/concepts/service?hl=pt-Br).    

Пример:
```yaml
apiVersion: v1
kind: Service
metadata:
  name: my-service
spec:
  selector:
    app: inference-api
  type: ClusterIP
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
```

## Переменные окружения (`env`)
Переменные передаются в контейнер при запуске. Можно задать напрямую или взять из ConfigMap/Secret [](https://kubernetes.io/docs/tasks/inject-data-application/define-environment-variable-container/)[](https://notes.kodekloud.com/docs/Certified-Kubernetes-Application-Developer-CKAD/Configuration/Environment-Variables/page#1).

**Прямое указание:**
```yaml
env:
- name: MODEL_NAME
  value: "gpt-4"
- name: API_KEY
  valueFrom:
    secretKeyRef:
      name: api-secrets
      key: api-key
```
**Важно:** если вы используете `envFrom` с ConfigMap или Secret, все ключи из них становятся переменными окружения. Переменные, заданные через `env`, имеют приоритет над теми, что уже есть в образе [](https://kubernetes.io/docs/tasks/inject-data-application/define-environment-variable-container/).

## Лимиты по ресурсам (CPU и Memory)
**requests** — сколько ресурсов Pod гарантированно получает. По этому значению планировщик решает, на какую ноду его поставить [](https://kubernetes.io/zh-cn/docs/concepts/configuration/manage-resources-containers/)[](https://learn.microsoft.com/fi-fi/azure/aks/developer-best-practices-resource-management#1).

**limits** — максимум, который Pod может потребить. Если превысит лимит по памяти — будет убит (OOMKilled). По CPU — просто ограничат в использовании [](https://learn.microsoft.com/fi-fi/azure/aks/developer-best-practices-resource-management#1).

**Единицы измерения:**
- CPU: `1000m` = 1 ядро, `100m` = 0.1 ядра [](https://learn.microsoft.com/fi-fi/azure/aks/developer-best-practices-resource-management#1).    
- Memory: `128Mi`, `1Gi` и т.д.    

Пример:
```yaml
resources:
  requests:
    memory: "256Mi"
    cpu: "100m"
  limits:
    memory: "512Mi"
    cpu: "500m"
```

**Почему это важно:** без requests планировщик не знает, сколько ресурсов нужно приложению, и может поставить Pod на перегруженную ноду. Без limits один Pod может «съесть» всю память ноды и уронить соседей [](https://learn.microsoft.com/fi-fi/azure/aks/developer-best-practices-resource-management#1).

---
