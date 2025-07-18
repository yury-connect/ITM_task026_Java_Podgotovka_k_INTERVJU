## Написание Dockerfile (пояснение от *ChatGPT*)

---
**Кратко 🚀**

1. **Dockerfile** – это «рецепт» для сборки образа контейнера.    
2. Состоит из команд (инструкций) – FROM, WORKDIR, COPY, EXPOSE, ENTRYPOINT и т.д.    
3. Docker читает файл сверху вниз, создает слои и кеширует их.    
4. Итог: команда `docker build` превращает Dockerfile в готовый к запуску образ.    

---
## Подробно и с примерами 📚
### 1. Структура Dockerfile
```dockerfile
FROM <базовый-образ>            # 1️⃣ От какого образа отталкиваемся
WORKDIR /app                      # 2️⃣ Устанавливаем рабочую директорию
COPY src/target/app.jar app.jar     # 3️⃣ Копируем файлы внутрь
EXPOSE 8080                           # 4️⃣ Объявляем порт для внешнего доступа
ENTRYPOINT ["java", "-jar", "app.jar"]  # 5️⃣ Команда, которая запустится внутри контейнера
```

> **Лайфхак 🎯**: добавляй в корень проекта файл `.dockerignore`, чтобы не копировать лишние файлы (node_modules, логи и т.д.) – сборка будет быстрее и чище.

---
### 2. Разбор инструкций 🧐

1. **FROM**    
- Определяет базовый образ, например `eclipse-temurin:17-jdk-jammy`.
- Можно делать **многоступенчатую сборку**:
```dockerfile
FROM maven:3.8-jdk-17 AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=builder /build/target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```
	- **Зачем?** Первый этап собирает JAR, второй – минимальный образ без Maven.
       
2. **WORKDIR**    
	- Перемещает «точку сборки» внутрь контейнера.        
	- Если папки нет – создаст её автоматически.
    
3. **COPY / ADD**    
    - `COPY <источник> <назначение>` – копирует файлы/папки.        
    - `ADD` умеет также распаковывать архивы и тянуть по URL (но чаще рекомендуется `COPY` – явнее).
    
4. **RUN**    
    - Выполняет команды при _сборке_ образа, например:        
```dockerfile
 RUN apt-get update && apt-get install -y curl
```
	- Совмещай команды через `&&` – меньше слоёв, меньше веса.
    
5. **EXPOSE**
    - Объявляет порт, по которому приложение слушает.        
    - Не пробрасывает автоматически – для этого при запуске:
```bash
docker run -p 8080:8080 my-image
```
	
6. **ENTRYPOINT и CMD**    
    - `ENTRYPOINT` – основная команда, которую нельзя легко переопределить.        
    - `CMD` – команду можно переопределить при запуске:
```dockerfile
CMD ["--spring.profiles.active=prod"]
```
	- Часто вместе:
```dockerfile
ENTRYPOINT ["java", "-jar", "app.jar"]
CMD ["--spring.profiles.active=prod"]
```
   
---
### 3. Как это работает внутри 🌐

1. **Контекст сборки**    
    - При `docker build .` Docker берет всё из текущей папки (контекст), загружает на демона.        
    - Поэтому важно `.dockerignore` – иначе отправятся большие/лишние файлы.
    
2. **Слои и кеш**    
    - Каждая инструкция создает новый слой файловой системы.        
    - При повторной сборке Docker проверяет кеш: если слои не изменились, он их пропускает → сборка быстрая.        
    - **Совет**: сначала копировать неизменяемые файлы (`pom.xml`, `package.json`), запускать `RUN mvn dependency:go-offline` или `npm install`, а потом копировать исходники.
    
3. **Оптимизация размера**    
    - Мульти-стэйдж сборка        
    - Использование легковесных базовых образов: `alpine`, `distroless`        
    - Удаление ненужных пакетов и кешей: `apt-get clean && rm -rf /var/lib/apt/lists/*`        

---
### 4. Полный пример для Java + Spring Boot 🌿

```dockerfile
# 1) Сборка
FROM maven:3.8-jdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# 2) Минимизированный образ
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/myapp.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
```

---
### Психологический фишинг 👍

- **Ассоциации**: представляй, что каждый слой – как «слой торта», и ты аккуратно наслаиваешь ингредиенты.
    
- **Чанкинг**: разбивай Dockerfile на логические блоки (база, зависимости, код, запуск), чтобы мозг не перегружался.
    

---

❗ **Запуск**
```bash
docker build -t user-service .  
docker run -d -p 8080:8080 user-service
```

---

✨ **Мудрая мысль:**

> «Как и в жизни, в докере важно начинать с прочного фундамента и добавлять лишь то, без чего действительно не обойтись.»