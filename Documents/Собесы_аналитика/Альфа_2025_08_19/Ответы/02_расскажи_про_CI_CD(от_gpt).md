# [**вариант от ChatGPT**](02_расскажи_про_CI_CD(от_gpt).md) // [вариант от DeepSeek](02_расскажи_про_CI_CD(от_deepseek).md)


🚀 Отличный блок вопросов — тут надо показать уверенность и реальный опыт *DevOps*-практик. Отвечаем **структурированно, без лишней воды**, как будто ты реально этим пользовался.

---
## 💡 Идеальный ответ кандидата

**CI/CD процесс у нас выглядел так:**

### 🔹 Ветки Git
- **`develop`** — основная ветка для интеграции новых фич. Все `merge-request` проходили `code review`, `unit-тесты` и `сборку`.    
- **`test`** — ветка, от которой шли релизы на тестовый стенд. В неё мерджили только проверенный код из `develop`.    
- Дополнительно были `stage` (*предпрод*) и `master` (*prod*).    

---
### 🔹 Сборка и образы
- При `merge` в `develop` или `test` автоматически запускался **Jenkins pipeline**:    
    1. Сборка приложения (`Maven`/`Gradle`).        
    2. Прогон `unit` и `integration` тестов.        
    3. Сборка `Docker`-образа и пуш в **Nexus/Artifactory registry**.        
- `Docker` образ собирался именно на стадии `CI` после прохождения тестов.    
  *т.е.  `Docker` образ оказывался в  ветке test и пуш'ился в систему хранения версий в **Nexus/Artifactory registry**.*  

---
### 🔹 Деплой (раскатка)
- Использовали **Kubernetes** как оркестратор.    
- После того как образ попадал в `registry`, `Jenkins` триггерил деплой на соответствующий `namespace` (`test`/`stage`/`prod`).    
- Для деплоя применялись **Helm charts**, в которых описаны деплойменты, сервисы, конфиги.    

---
### 🔹 Jenkins
- Jenkins стоял в отдельном DevOps-namespace.    
- Pipeline был **declarative**, описан в `Jenkinsfile`.    
- Стэйджи Jenkins:    
    1. **Checkout** — забираем код.        
    2. **Build & Test** — Maven build, юнит и интеграционные тесты.        
    3. **Docker build & push** — сборка Docker-образа и загрузка в registry.        
    4. **Deploy** — вызов [Helm](../../../TECHNOLOGIES/Оркестрация/Kubernetes/Helm/Helm_info.md) с параметрами окружения.        

---
### 🔹 Кто инициировал раскатку
- **На test-стенд** раскатка шла **автоматически** при merge в ветку `test`.    
- **На stage/prod** — только по ручному триггеру (инициатор — тимлид или DevOps). Для прод использовался отдельный pipeline с approval step.    

---
### 🔹 Кто писал pipeline
- Основную структуру pipeline писали DevOps-инженеры.    
- Но мы как разработчики тоже вносили правки: добавляли шаги для дополнительных проверок, фиксов, новых метрик.    
- Я лично правил Jenkinsfile: добавлял stage с прогоном интеграционных тестов и расширенное логирование.    

---
### 🔹 Приходилось ли смотреть Jenkinsfile?

👉 Да. Jenkinsfile был declarative, выглядел примерно так (упрощённо):
```groovy
pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'develop', url: 'gitlab-url'
            }
        }
        stage('Build & Test') {
            steps {
                sh './mvnw clean install'
            }
        }
        stage('Docker Build & Push') {
            steps {
                sh 'docker build -t registry/app:${BUILD_NUMBER} .'
                sh 'docker push registry/app:${BUILD_NUMBER}'
            }
        }
        stage('Deploy to Test') {
            when {
                branch 'test'
            }
            steps {
                sh 'helm upgrade --install app charts/app -f values-test.yaml'
            }
        }
    }
}

```

---
🎯 **Ключевой посыл:**
- Ты знаешь **workflow**: Git-flow → Jenkins → Docker → Registry → Kubernetes/Helm.    
- Ты участвовал не только как потребитель, но и **правил pipeline**, понимаешь его стэйджи.    
- Чётко разделяешь автоматическую и ручную раскатку.    

---

Хочешь, я подготовлю ещё **короткий устный вариант** ответа (на 1–2 минуты), чтобы проговаривать его на собесе без деталей кода?

---
# [**вариант от ChatGPT**](02_расскажи_про_CI_CD(от_gpt).md) // [вариант от DeepSeek](02_расскажи_про_CI_CD(от_deepseek).md)
