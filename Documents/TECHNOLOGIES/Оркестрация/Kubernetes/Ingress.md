**Ingress** (в контексте `Kubernetes` и облачных технологий) — это **API-объект Kubernetes**, который управляет внешним доступом к сервисам внутри кластера, обычно через `HTTP`/`HTTPS`. Он действует как "*умный маршрутизатор*" (*аналог `Nginx` или `Cloud Load Balancer`*), но работает на уровне `Kubernetes`, предоставляя:
- **Единую точку входа** для множества сервисов.    
- **Гибкую маршрутизацию** на основе доменов, путей или заголовков.    
- **`SSL`/`TLS`-терминацию**.    
- **Балансировку нагрузки**.    

---
## 🔹 **Зачем нужен Ingress?**
Без **`Ingress`** для доступа к каждому сервису в `Kubernetes` пришлось бы:
- Создавать отдельный **Service типа LoadBalancer** (*дорого, если сервисов много*).    
- Использовать **NodePort** (*небезопасно и неудобно для production*).    

`Ingress` решает эти проблемы, позволяя:  
✅ **Один внешний IP/домен** → множество сервисов.  
✅ **Человекочитаемые URL** (например, `api.example.com` или `app.example.com/blog`).  
✅ **Централизованное управление SSL**.

---
## 🔹 **Как работает Ingress?**
1. **Пользователь** делает запрос на `https://app.example.com`    
2. **Ingress-контроллер** (например, Nginx, Traefik) принимает запрос.    
3. **Ingress-ресурс** (YAML-манифест) указывает, куда направить трафик (например, в сервис `frontend-service:80`).    
4. **Трафик** попадает в нужный Pod внутри кластера.    

---
## 🔹 **Основные компоненты**

### 1. **Ingress-ресурс (Ingress Resource)**

YAML-манифест, который описывает правила маршрутизации. Пример:
```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: my-app-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /  # Аннотации для контроллера
spec:
  tls:
    - hosts:
        - app.example.com
      secretName: my-tls-secret  # Сертификат для HTTPS
  rules:
    - host: app.example.com
      http:
        paths:
          - path: /api
            pathType: Prefix
            backend:
              service:
                name: api-service
                port:
                  number: 80
          - path: /
            pathType: Prefix
            backend:
              service:
                name: frontend-service
                port:
                  number: 80
```

### 2. **Ingress-контроллер (Ingress Controller)**
Программа, которая обрабатывает Ingress-правила. Популярные варианты:
- **[Nginx Ingress Controller](https://github.com/kubernetes/ingress-nginx)** (самый распространенный).    
- **[Traefik](https://traefik.io/)** (подходит для микросервисов).    
- **[AWS ALB Ingress Controller](https://docs.aws.amazon.com/eks/latest/userguide/alb-ingress.html)** (для интеграции с AWS).    

### 3. **Service (ClusterIP или NodePort)**
Сервисы, к которым Ingress направляет трафик.

---
## 🔹 **Как развернуть Ingress?**
### 1. Установите Ingress-контроллер (например, Nginx):
```bash
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/cloud/deploy.yaml
```

Проверьте, что контроллер запущен:
```bash
kubectl get pods -n ingress-nginx
```

### 2. Создайте Ingress-ресурс:
```bash
kubectl apply -f my-ingress.yaml
```

### 3. Проверьте доступность:
```bash
kubectl get ingress
```

Если используется `Minikube`, включите `Ingress`:
```bash
minikube addons enable ingress
```

---
## 🔹 **Примеры использования**

### 1. **Маршрутизация по доменам**
```yaml
rules:
  - host: api.example.com
    http:
      paths:
        - backend:
            service: api-service
            port: 80
  - host: app.example.com
    http:
      paths:
        - backend:
            service: frontend-service
            port: 80
```

### 2. **Маршрутизация по путям**
```yaml
rules:
  - host: example.com
    http:
      paths:
        - path: /shop
          backend:
            service: shop-service
            port: 80
        - path: /blog
          backend:
            service: blog-service
            port: 80
```

### 3. **HTTPS с Let's Encrypt**

Используйте **[cert-manager](https://cert-manager.io/)** для автоматического получения сертификатов:
```yaml
apiVersion: cert-manager.io/v1
kind: ClusterIssuer
metadata:
  name: letsencrypt-prod
spec:
  acme:
    server: https://acme-v02.api.letsencrypt.org/directory
    email: your@email.com
    privateKeySecretRef:
      name: letsencrypt-prod
    solvers:
      - http01:
          ingress:
            class: nginx
```

---
## 🔹 **Ограничения Ingress**
- **Не все контроллеры поддерживают сложные правила** (например, gRPC, WebSockets).    
- **Для высоконагруженных систем** может потребоваться настройка Rate Limiting или WAF (например, через аннотации Nginx).    
- **В облачных провайдерах** (AWS, GCP) Ingress может создавать внешние балансировщики (ALB, GCLB), что увеличивает стоимость.    

---
## 🔹 **Альтернативы Ingress**
- **Service Mesh (Istio, Linkerd)**: Для сложной маршрутизации и безопасности.    
- **API Gateway (Kong, Apigee)**: Если нужна дополнительная логика (аутентификация, трансформация запросов).    

---
## **Вывод**

Ingress в Kubernetes — это:  
🚀 **Удобный способ управления входящим трафиком**.  
🔌 **Гибкость маршрутизации** (по доменам, путям, заголовкам).  
🔒 **Простое подключение HTTPS**.

Если у вас больше одного сервиса в кластере — `Ingress` практически обязателен!