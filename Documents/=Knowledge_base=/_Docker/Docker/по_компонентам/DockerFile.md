**Минимальный Dockerfile** — файл с инструкциями для создания минимального образа, содержащего только самое необходимое для работы приложения.
## Пример минимального **Dockerfile**:
```dockerfile
# Используем минимальный базовый образ
FROM alpine:latest

# Копируем готовый бинарник в образ
COPY my-app /usr/local/bin/

# Команда запуска по умолчанию
CMD ["my-app"]
```
### Что обязательно в минимальном наборе:
1. **`FROM`** - указание базового образа    
    - `alpine` *(∼5 МБ)* - самый популярный минимальный вариант        
    - `scratch` - полностью пустой образ *(0 МБ)*    
2. **`COPY`** - копирование файлов в образ    
    - Только необходимые файлы приложения        
    - Скомпилированный бинарник *(не исходный код!)*    
3. **`CMD`** или **`ENTRYPOINT`** - команда запуска

> **Ключевой принцип:**   
> `минимальный образ` = 
> 	`минимальный базовый образ` + 
> 	`только необходимые файлы` + 
> 	`одна команда запуска`.

---
## Типичный **Dockerfile** для **Spring Boot** приложения:
```dockerfile
# 1. Базовый образ с JDK (обычно используют официальные образы OpenJDK)
FROM eclipse-temurin:17-jre-alpine

# 2. Устанавливаем рабочую директорию
WORKDIR /app

# 3. Копируем JAR-файл приложения
COPY target/*.jar app.jar

# 4. Создаем непривилегированного пользователя для безопасности
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# 5. Открываем порт (опционально, для документации)
EXPOSE 8080

# 6. Команда запуска приложения
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

### Оптимизированная версия с **многостадийной** сборкой:
```dockerfile
# Стадия сборки
FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Финальная стадия
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

RUN addgroup -S spring && adduser -S spring -G spring
USER spring

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Ключевые особенности:
1. **Базовый образ** - `eclipse-temurin:17-jre-alpine` (минимальный образ с JRE)    
2. **Безопасность** - запуск от непривилегированного пользователя    
3. **JAR-файл** - копируется уже собранный артефакт    
4. **Порт** 8080 - стандартный порт Spring Boot    
5. **Команда запуска** - `java -jar` для исполнения JAR-файла    

**Размер итогового образа:** ∼150-200 МБ (вместо ∼500 МБ с JDK)

---
# **Dockerfile**: основные команды и рекомендации

### 1. **FROM** - базовый образ
```dockerfile
FROM eclipse-temurin:17-jre-jammy
```
**Рекомендации:**
- Используйте официальные образы    
- Выбирайте конкретные версии (не *latest*)    
- Используйте `alpine` или `slim` версии для уменьшения размера    

### 2. **WORKDIR** - рабочая директория
```dockerfile
WORKDIR /app
```
**Рекомендации:**
- Всегда явно указывайте `WORKDIR`    
- Используйте абсолютные пути    
- Одна директория для приложения    
### 3. **COPY** vs **ADD** - копирование файлов
```dockerfile
COPY target/app.jar app.jar
COPY --chown=user:group config/ ./config/
```
**Рекомендации:**
- Используйте `COPY` вместо `ADD` (*без магии*)    
- Копируйте только <u>необходимые</u> файлы    
- Используйте **`.dockerignore`** для исключения файлов

### 4. **RUN** - выполнение команд
```dockerfile
RUN apt-get update && apt-get install -y curl \
    && rm -rf /var/lib/apt/lists/*
```
**Рекомендации:**
- Объединяйте команды в один `RUN`    
- Удаляйте кэш пакетных менеджеров    
- Используйте `\` для читаемости    

### 5. **USER** - смена пользователя
```dockerfile
RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser
```
**Рекомендации:**
- Не запускайте от `root`    
- Создавайте <u>непривилегированного пользователя</u>    
- Меняйте пользователя перед запуском приложения    

### 6. **EXPOSE** - документирование портов
```dockerfile
EXPOSE 8080
EXPOSE 8080/tcp
```
**Рекомендации:**
- Указывайте протокол (`tcp`/`udp`)    
- Это <u>документация</u>, а <u>не открытие портов</u>    

### 7. **ENV** - переменные окружения
```dockerfile
ENV APP_HOME=/app \
    JAVA_OPTS="-Xmx512m"
```
**Рекомендации:**
- Используйте <u>для конфигурации</u>    
- Группируйте связанные переменные    
- Не храните секреты в <u>ENV</u>    

### 8. **ENTRYPOINT** vs **CMD** - запуск приложения
```dockerfile
ENTRYPOINT ["java", "-jar"]
CMD ["app.jar"]
```
**Рекомендации:**
- `ENTRYPOINT` для основной команды    
- `CMD` для аргументов по умолчанию    
- Используйте `exec` форму (["java", "-jar"])    

### 9. **HEALTHCHECK** - проверка здоровья
```dockerfile
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/health || exit 1
```
**Рекомендации:**
- Добавляйте для *production* образов
- Настраивайте разумные интервалы    

### 10. **LABEL** - метаданные
```dockerfile
LABEL maintainer="team@company.com"
LABEL version="1.0"
```
**Рекомендации:**
- Добавляйте метаданные для управления    
- Используйте стандартные labels    

### Пример оптимизированного `Dockerfile`:
```dockerfile
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --chown=appuser:appgroup target/*.jar app.jar

USER appuser

EXPOSE 8080/tcp

HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Ключевые принципы:**
- Минимальный размер образа    
- Безопасность (non-root user)    
- Повторяемость сборок    
- Читаемость и поддерживаемость

---
[пример](https://youtu.be/RlQn_ZWuNa0?si=sG1bJaENiAK5tGtW&t=574) написания `DockerFile`

