### **🐳 Docker Compose для Spring Boot: полное руководство**
_(Оптимизировано для Spring Boot-разработчиков)_

---
## **🛠️ 1. Что такое Docker Compose?**

**Docker Compose** — инструмент для определения и запуска **многоконтейнерных приложений** через YAML-файл.

🔹 **Зачем Spring Boot-разработчику?**
- Запуск всего стека (приложение + БД + кеш + и т.д.) одной командой    
- Воспроизводимость окружения (dev/test/prod)    
- Упрощение CI/CD    

---
## **📦 2. Базовый `docker-compose.yml` для Spring Boot**
```yaml
version: '3.8'

services:
  app:  # Ваше Spring Boot-приложение
    image: my-spring-app:latest
    build: .  # Собирает образ из Dockerfile в текущей папке
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - DB_URL=jdbc:postgresql://db:5432/mydb
    depends_on:
      - db
      - redis

  db:  # PostgreSQL
    image: postgres:13
    environment:
      POSTGRES_PASSWORD: mypassword
      POSTGRES_DB: mydb
    volumes:
      - pg_data:/var/lib/postgresql/data

  redis:  # Кеш
    image: redis:6
    ports:
      - "6379:6379"

volumes:
  pg_data:  # Постоянное хранилище для БД
```

---
## **🔧 3. Оптимизация для Spring Boot**

### **3.1. Подготовка `Dockerfile`**
```dockerfile
# Базовый образ с JDK (лучше использовать JRE для production)
FROM eclipse-temurin:17-jdk-jammy

# Оптимизация для Spring Boot
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Копируем JAR (после сборки через Maven/Gradle)
WORKDIR /app
COPY target/my-spring-app.jar app.jar

# Точка входа
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
```

### **3.2. Полезные настройки в `compose.yml`**
```yaml
services:
  app:
    # Оптимизация ресурсов
    deploy:
      resources:
        limits:
          cpus: '0.5'
          memory: 512M
    # Healthcheck для Spring Actuator
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
```

---
## **🚀 4. Запуск и управление**

### **Основные команды**
```bash
# Сборка и запуск (в фоне)
docker-compose up -d --build

# Остановка с удалением контейнеров
docker-compose down

# Просмотр логов приложения
docker-compose logs -f app

# Масштабирование (если поддерживается)
docker-compose up -d --scale app=2
```

### **Spring-специфичные сценарии**

bash

# Запуск с разными профилями
SPRING_PROFILES_ACTIVE=prod docker-compose up

# Пересборка только приложения
docker-compose build app

---

## **🔍 5. Отладка и мониторинг**

### **5.1. Подключение к контейнеру**

bash

# Для проверки конфигурации
docker-compose exec app bash

# Для мониторинга (если есть Actuator)
curl http://localhost:8080/actuator/metrics

### **5.2. Интеграция с IDE**

1. **IntelliJ IDEA**:
    
    - Поддержка Docker Compose из коробки
        
    - Запуск дебаггера через "Remote JVM Debug"
        
2. **VS Code**:
    
    - Расширение "Docker" + "Remote Containers"
        

---

## **⚡ 6. Продвинутые сценарии**

### **6.1. Мульти-профили Spring**

yaml

services:
  app:
    environment:
      - SPRING_PROFILES_ACTIVE=dev,docker

### **6.2. Подключение к локальной БД при разработке**

yaml

services:
  app:
    # Для доступа к хостовой БД (не рекомендуется для prod)
    extra_hosts:
      - "host.docker.internal:host-gateway"
    environment:
      DB_URL: jdbc:postgresql://host.docker.internal:5432/mydb

### **6.3. Использование Config Server**

yaml

services:
  config-server:
    image: my-config-server:latest
    ports:
      - "8888:8888"

  app:
    environment:
      SPRING_CONFIG_URI: http://config-server:8888

---

## **📌 7. Best Practices для Spring Boot**

1. **Оптимизация образов**:
    
    - Используйте `.dockerignore`
        
    - Многостадийную сборку для уменьшения размера
        
2. **Безопасность**:
    
    - Не храните секреты в compose-файле (используйте `env_file`)
        
    - Обновляйте базовые образы
        
3. **Производительность**:
    
    - Ограничивайте ресурсы контейнеров
        
    - Настраивайте `JAVA_OPTS` под контейнеризацию
        

---

## **🛠️ 8. Пример полной конфигурации**

**`.dockerignore`**

text

target/
*.jar
.idea
.git

**`docker-compose.prod.yml`**

yaml

services:
  app:
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    deploy:
      resources:
        limits:
          memory: 1G

Запуск:

bash

docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d

---

## **💡 Итог: зачем Spring Boot-разработчику Docker Compose?**

✅ **Локальная разработка** с полным стеком (БД, кеш и т.д.)  
✅ **Идентичное окружение** на всех этапах (dev/test/staging)  
✅ **Быстрое прототипирование** микросервисов

**👉 Next Step**: Для production-развертывания рассмотрите **Kubernetes** + **Helm**.