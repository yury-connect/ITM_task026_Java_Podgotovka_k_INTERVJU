## Написание Dockerfile (пояснение от *DeepSeek*)

---
# Подробное объяснение **Dockerfile** на примере.

#### Краткое описание:
**Dockerfile** — это инструкция для сборки Docker-образа. Он описывает:
1. Базовый образ (исходную ОС и окружение)    
2. Действия по настройке окружения    
3. Копирование файлов приложения    
4. Параметры запуска контейнера    

#### Пример для рассмотрения:
```dockerfile
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY user-service-src/target/user-service-src-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Ваш **Dockerfile** делает следующее:
1. Берет образ с Java 17    
2. Создает рабочую директорию `/app`    
3. Копирует JAR-файл приложения    
4. Открывает порт 8080    
5. Задает команду запуска приложения    

---
# Детальный разбор каждой инструкции:

**1. `FROM eclipse-temurin:17-jdk-jammy`**
```dockerfile
# Базовый образ с предустановленной Java 17
FROM eclipse-temurin:17-jdk-jammy
```
- **Что делает:** Задает основу для образа    
- **Детали:**    
    - `eclipse-temurin` — официальный образ OpenJDK от Eclipse Adoptium        
    - `17-jdk-jammy` — тег, указывающий версию:        
        - `17`: Версия Java **17**           
        - `jdk`: Тип пакета (**JDK** вместо JRE)            
        - `jammy`: Базовая ОС **Ubuntu 22.04 LTS**
    
- **Альтернативы:**    
    - `eclipse-temurin:17-jre-jammy` — для production (меньший размер)        
    - `openjdk:17-slim-bullseye` — Debian-based образ        

---
**2. `WORKDIR /app`**
```dockerfile
# Устанавливает рабочую директорию внутри контейнера
WORKDIR /app
```
- **Что делает:** Создает директорию и делает ее текущей    
- **Детали:**    
    - Все последующие команды выполняются относительно `/app`        
    - Если директория не существует — создается автоматически        
    - Эквивалент последовательности команд:        
```dockerfile
RUN mkdir -p /app
cd /app
```

---
**3. `COPY user-service-src/target/user-service-src-0.0.1-SNAPSHOT.jar app.jar`**
```dockerfile
# Копирует JAR-файл из хоста в образ
COPY user-service-src/target/user-service-src-0.0.1-SNAPSHOT.jar app.jar
```

- **Что делает:** Копирует файл из локальной системы в образ    
- **Синтаксис:** `COPY <src> <dest>`    
- **Детали:**    
    - `src`: Путь относительно директории Dockerfile (на хосте)        
    - `dest`: Путь внутри образа (относительно WORKDIR)        
    - В примере: переименовывает файл в `app.jar`
    
- **Важно:**    
    - Используйте `.dockerignore` чтобы исключить ненужные файлы        
    - Для Maven/Gradle проектов копируйте только готовый артефакт

---
**4. `EXPOSE 8080`**
```dockerfile
# Декларирует порт, который слушает приложение
EXPOSE 8080
```
- **Что делает:** Указывает на какой порт контейнер принимает подключения    
- **Детали:**    
    - Не открывает порт автоматически — это метаданные для `docker run`        
    - Фактическое открытие порта происходит при запуске:
	```bash
	docker run -p 8080:8080 ...
	```
    - Рекомендуется указывать протокол: `EXPOSE 8080/tcp`

---
**5. `ENTRYPOINT ["java", "-jar", "app.jar"]`**
```dockerfile
# Команда запуска при старте контейнера
ENTRYPOINT ["java", "-jar", "app.jar"]
```

- **Что делает:** Определяет исполняемую команду при запуске    
- **Форматы:**    
    - **Exec форма (рекомендуется):** `["исполняемый_файл", "арг1", "арг2"]`        
    - Shell форма: `ENTRYPOINT java -jar app.jar`
    
- **Особенности:**    
    - Параметры можно переопределить при запуске:  
        `docker run <image> --spring.profiles.active=prod`        
    - Часто используется в комбинации с `CMD`        

---
# Полный улучшенный пример с комментариями:
```dockerfile
# Базовый образ с Java 17 (оптимизированный для production)
FROM eclipse-temurin:17-jre-jammy

# Установка рабочей директории
WORKDIR /app

# Копирование JAR-файла (используем имя без версии)
COPY target/*.jar app.jar

# Добавляем пользователя для безопасности (не root)
RUN useradd -m appuser && chown -R appuser:appuser /app
USER appuser

# Открываем порт приложения
EXPOSE 8080/tcp

# Параметры Java для production
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

# Команда запуска (исполняемая форма)
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar ${0} ${@}"]
```

---
# Ключевые принципы написания **Dockerfile**:

1. **Минимизация слоев:**    
    - Объединяйте команды `RUN`:
```dockerfile
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*
```
    
2. **Безопасность:**    
    - Не используйте root:
```dockerfile
RUN useradd -m appuser
USER appuser
```
	
3. **Кэширование:**    
    - Меняющиеся операции размещайте в конце файла        
    - Стабильные зависимости копируйте отдельно:        
```dockerfile
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ src/
```
    
4. **Переменные окружения:**
```dockerfile
ENV SPRING_PROFILES_ACTIVE=prod
```
    
5. **Многоступенчатая сборка (для уменьшения размера):**
```dockerfile
# Сборка
FROM maven:3.8-jdk-11 AS build
COPY . .
RUN mvn package

# Финальный образ
FROM eclipse-temurin:17-jre-jammy
COPY --from=build /target/*.jar app.jar
```
  
---
# Процесс работы **Docker**:

1. **Сборка образа:**
   ```bash
docker build -t my-service:1.0 .
   ```
    - Читает Dockerfile        
    - Выполняет инструкции последовательно        
    - Создает слои для каждой инструкции        
    - Формирует финальный образ
    
2. **Запуск контейнера:**
   ```bash
docker run -d -p 8080:8080 --name my-container my-service:1.0
   ```
    - Создает контейнер из образа        
    - Выполняет команду из `ENTRYPOINT`        
    - Пробрасывает порт 8080 хоста на порт 8080 контейнера
    
3. **Жизненный цикл:**    
    - При старте: `ENTRYPOINT` → `CMD`        
    - При остановке: SIGTERM → SIGKILL (через 10 сек)        

---
# Рекомендации для **Spring Boot**:

1. **Используйте `Spring Boot layerization`:**
```dockerfile
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-Djarmode=layertools", "-jar", "app.jar", "extract"]
```
    
2. **Добавьте `health-check`:**
```dockerfile
HEALTHCHECK --interval=30s CMD curl -f http://localhost:8080/actuator/health || exit 1
```
    
3. **Оптимизируйте `JVM` для контейнеров:**
```dockerfile
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"
```
    
4. **Используйте `.dockerignore`:**
```text
.git
.mvn
target
*.iml
Dockerfile
```

Такой **Dockerfile** обеспечивает:
- Минимальный размер образа    
- Безопасную работу без root-прав    
- Оптимальные настройки JVM    
- Легкое масштабирование в Kubernetes    
- Понятную структуру для сопровождения


