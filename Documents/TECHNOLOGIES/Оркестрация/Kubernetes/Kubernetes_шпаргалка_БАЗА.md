# ====**Минимум по `Kubernetes` для собеса** (*база разработчика*)====
Запомни **8 ключевых концепций** + **2 фразы для уверенного ответа**.

---
#### **1. Основные сущности** (*Must-Know*)

|**Термин**|**Зачем?**|**Пример использования**|
|---|---|---|
|**Pod**|Базовая единица развертывания (1+ контейнер)|`kubectl get pods -n staging`|
|**Deployment**|Управляет подами (реплики, обновления)|Версионирование, rolling update|
|**Service**|Постоянный IP для доступа к подам (LB/ClusterIP)|Связь между frontend и backend|
|**Ingress**|Маршрутизация HTTP-трафика (внешний доступ)|`company.com/app → service`|
|**ConfigMap**|Хранение конфигов (несекретные данные)|Переменные для Spring Boot `application.yml`|
|**Secret**|Хранение чувствительных данных (пароли, токены)|**Base64-кодирование**, доступ через Volumes|
|**Namespace**|Виртуальное разделение кластера (dev/staging)|`kubectl create namespace test`|
|**kubectl**|CLI для управления кластером|`logs`, `describe pod`, `apply -f`|

---
#### **2. Как это связано с разработкой?**
- **Для деплоя**:
```bash
kubectl apply -f deployment.yaml  # Запуск манифеста  
helm install my-app ./chart      # Через Helm (пакетный менеджер)  
```
    
- **Для отладки**:
```bash
kubectl logs <pod-name>          # Просмотр логов  
kubectl describe pod <pod-name>  # Детали пода (ошибки, env, события)  
```
    
- **Для конфигов**:
```yaml
env:  
  - name: DB_URL  
    valueFrom:  
      configMapKeyRef:  
        name: app-config  
        key: database_url  
```

---
#### **3. Отличия от Docker Swarm**
- **K8s сложнее**, но мощнее:    
    - Автомасштабирование (HPA), Canary-деплой.
    - Больше инструментов (Helm, Operators, Service Mesh).    
- **Swarm проще**:    
    - `docker stack deploy` vs `helm install`.
    - Нет аналогов ConfigMap/Secrets (только через env-файлы).

---
#### **4. Что говорить на собесе?**
**Шаблон ответа:**
> *«Работал с k8s на уровне разработчика:> 
> - Писал манифесты (Deployment/Service/ConfigMap),>     
> - Делал деплой через `kubectl` / Helm,>     
> - Смотрел логи через `kubectl logs` и Lens,>     
> - Настраивал Health Checks для Spring Boot.  
>     Администрирование кластера (сети, ноды) не входило в мои задачи.»*

**Два кейса для уверенности:**
1. _«Под падал с CrashLoopBackOff → нашел через `kubectl describe pod`, что не хватало памяти → увеличил `limits.memory` в Deployment»._    
2. _«Добавлял переменные окружения из ConfigMap для смены БД на staging»._    

---
#### **5. Чего НЕ надо говорить**
- Не путай **Pod** и **Docker-контейнер** («Pod — обертка для контейнера»).    
- Не утверждай, что настраивал кластер (если не делал).    
- Не упоминай продвинутые темы (Istio, CRD), если не готов детально обсуждать.    

---
**Финальный чеклист:**  
✅ Знаешь, что такое **Pod/Deployment/Service**.  
✅ Понимаешь, зачем **ConfigMap** и **Namespace**.  
✅ Умеешь смотреть логи (`kubectl logs`).  
✅ Можешь объяснить разницу между **k8s** и **Docker Swarm**.

Этого хватит, чтобы показать осведомленность! 🚀

---
