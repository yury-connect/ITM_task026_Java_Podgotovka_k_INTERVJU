---
tags:
  - TECHNOLOGIES/Docker
  - TECHNOLOGIES/DockerCompose
---
### **Docker Compose** — это инструмент для определения и запуска многоконтейнерных приложений с помощью `YAML`-файла.
### Зачем нужно:
1. **Управление несколькими сервисами** одновременно    
2. **Простая настройка** сетей, томов, переменных окружения    
3. **Быстрый запуск** сложных приложений одной командой    
4. **Локальная разработка** и тестирование

---
## Минимальный `docker-compose.yml` <br>для `Spring Boot` + `PostgreSQL`:
```yaml
version: '3.8'

services:
  # Сервис приложения
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/mydb
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=password
    depends_on:
      - db
    networks:
      - app-network

  # Сервис базы данных
  db:
    image: postgres:13-alpine
    environment:
      - POSTGRES_DB=mydb
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - app-network

# Определение томов для хранения данных
volumes:
  postgres_data:

# Определение сети для изоляции сервисов
networks:
  app-network:
    driver: bridge
```

### Детальный комментарий:

**1. Версия формата:**
```yaml
version: '3.8'  # Последняя стабильная версия схемы
```

**2. Сервис приложения:**
```yaml
app:
  build: .  # Собирать образ из Dockerfile в текущей директории
  ports:
    - "8080:8080"  # Проброс портов: хост:контейнер
  environment:  # Переменные окружения
    - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/mydb
    # Обратите внимание: 'db' - имя сервиса, Compose автоматически резолвит DNS
  depends_on:
    - db  # Запускать после сервиса db
  networks:
    - app-network  # Подключение к сети
```

**3. Сервис базы данных:**
```yaml
db:
  image: postgres:13-alpine  # Готовый образ из реестра
  environment:
    - POSTGRES_DB=mydb  # Создать БД при первом запуске
  volumes:
    - postgres_data:/var/lib/postgresql/data  # Сохранять данные между перезапусками
```

**4. Тома (volumes):**
```yaml
volumes:
  postgres_data:  # Именованный том для persistent storage
    # Данные не удаляются при удалении контейнера
```

**5. Сети (networks):**
```yaml
networks:
  app-network:
    driver: bridge  # Изолированная сеть только для этих сервисов
    # Сервисы видят друг друга по имени (app, db)
```

## **Основные команды**:
```bash
# Запуск всех сервисов
docker-compose up

# Запуск в фоновом режиме
docker-compose up -d

# Остановка сервисов
docker-compose down

# Просмотр логов
docker-compose logs

# Пересборка и запуск
docker-compose up --build
```

**Преимущества:** один файл описывает всю инфраструктуру приложения, легко `version control` и совместная работа.

Хорошее видео [ссылка](https://youtu.be/sXjkAEqFZEI?si=dMFQDGz6dsXqFR6Q)

---
