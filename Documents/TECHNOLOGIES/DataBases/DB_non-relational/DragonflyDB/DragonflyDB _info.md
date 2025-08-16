**DragonflyDB** — это супербыстрая `in-memory` БД, совместимая с `Redis`, но гораздо производительнее. Вот ключевые моменты:

### 🔥 **Основные фишки `DragonflyDB`**
1. **Бешеная скорость** – до 4 млн операций в секунду на одном ядре (*Redis выдает ~200-500 тыс*).    
2. **Полная совместимость с Redis** – работают те же команды (`SET`, `GET`, `HASH`, `PUB/SUB`).    
3. **Меньше памяти** – сжимает данные, экономит до 80% RAM по сравнению с Redis.    
4. **Масштабируется без шардирования** – Redis требует ручного деления данных, Dragonfly делает это автоматически.    

---
### 🚀 **Примеры на Python, Go, Node.js**

#### **1. Python (redis-py)**
```python
import redis

# Подключаемся как к обычному Redis
r = redis.Redis(host='localhost', port=6379)

# Записываем и читаем данные
r.set('player:100', '{"name": "Alex", "score": 5000}')
print(r.get('player:100'))  # b'{"name": "Alex", "score": 5000}'
```

#### **2. Go (go-redis)**
```go
package main

import (
	"context"
	"fmt"
	"github.com/redis/go-redis/v9"
)

func main() {
	client := redis.NewClient(&redis.Options{Addr: "localhost:6379"})
	ctx := context.Background()

	// Запись и чтение
	err := client.Set(ctx, "server:status", "online", 0).Err()
	if err != nil {
		panic(err)
	}

	val, err := client.Get(ctx, "server:status").Result()
	fmt.Println(val)  // "online"
}
```

#### **3. Node.js (ioredis)**
```javascript
const Redis = require('ioredis');
const redis = new Redis();

// Работа с Dragonfly как с Redis
await redis.set('config:theme', 'dark');
const theme = await redis.get('config:theme');
console.log(theme);  // "dark"
```

---
### 🔄 **Сравнение с Redis**

|Фича|DragonflyDB|Redis|
|---|---|---|
|**Скорость**|4M ops/sec (1 ядро)|~200K ops/sec (1 ядро)|
|**Память**|Сжатие, экономит RAM|Стандартный расход|
|**Масштабирование**|Автоматическое|Только шардирование|
|**Доп. возможности**|SQL-запросы, Tiered Storage|Модули (Lua)|

---
### 💡 **Когда выбирать DragonflyDB?**
- Нужна **максимальная скорость** без сложной настройки кластера.    
- Хочется **экономии памяти** (например, в облаке).    
- Требуется **простота масштабирования**.    

**Как запустить?**
```bash
docker run -p 6379:6379 dragonflydb/dragonfly
```
**Документация:** [dragonflydb.io](https://www.dragonflydb.io/)

---
---
---
Вот примеры работы с **DragonflyDB** на **Java** с использованием популярных клиентов:

### 🔥 **1. Подключение через `Jedis`** (*аналог redis-py*)
```java
import redis.clients.jedis.Jedis;

public class DragonflyExample {
    public static void main(String[] args) {
        // Подключение к Dragonfly (порт 6379, как у Redis)
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            // SET/GET
            jedis.set("user:1", "{\"name\":\"Alice\",\"score\":100}");
            String userData = jedis.get("user:1");
            System.out.println(userData); // {"name":"Alice","score":100}
			
            // HASH
            jedis.hset("player:2", "name", "Bob");
            jedis.hset("player:2", "level", "50");
            System.out.println(jedis.hgetAll("player:2")); // {name=Bob, level=50}
        }
    }
}
```

---
### ⚡ **2. `Spring Boot` + `Lettuce`** (*реактивный клиент*)
Добавьте в `pom.xml`:
```xml
<dependency>
    <groupId>io.lettuce</groupId>
    <artifactId>lettuce-core</artifactId>
    <version>6.2.4.RELEASE</version>
</dependency>
```

Пример:
```java
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class DragonflyLettuceExample {
    public static void main(String[] args) {
        RedisClient client = RedisClient.create("redis://localhost");
        try (StatefulRedisConnection<String, String> connection = client.connect()) {
            RedisCommands<String, String> commands = connection.sync();
            
            // Работа с JSON
            commands.set("config:app", "{\"theme\":\"dark\",\"lang\":\"en\"}");
            String config = commands.get("config:app");
            System.out.println(config); // {"theme":"dark","lang":"en"}

            // Pub/Sub (аналогично Redis)
            commands.publish("updates", "New data arrived!");
        }
    }
}
```

---
### 🚀 **3. Spring Data Redis** (*интеграция с `Spring Boot`*)
Конфигурация в `application.yml`:
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
```

Пример репозитория:
```java
@Repository
public class PlayerRepository {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void savePlayer(String id, Player player) {
        redisTemplate.opsForValue().set("player:" + id, player);
    }

    public Player getPlayer(String id) {
        return (Player) redisTemplate.opsForValue().get("player:" + id);
    }
}

// DTO
public class Player {
    private String name;
    private int score;
    // геттеры/сеттеры
}
```

---
### 🔄 **Сравнение клиентов для Java**

|Клиент|Когда использовать|Особенности|
|---|---|---|
|**Jedis**|Простые синхронные запросы|Блокирующий, потоконебезопасный|
|**Lettuce**|Реактивные/асинхронные задачи|Поддержка Netty, высокая производительность|
|**Spring Data Redis**|Интеграция с Spring Boot|Аннотации (`@RedisHash`), шаблоны|

---
### 💡 **Важные нюансы для `DragonflyDB` в `Java`**

1. **Сериализация JSON**:  
    Используйте `Jackson` или `Gson` для сложных объектов:
```java
ObjectMapper mapper = new ObjectMapper();
String json = mapper.writeValueAsString(player);
jedis.set("player:1", json);
```
    
2. **Транзакции**:  
    Работают так же, как в Redis:
```java
jedis.watch("balance");
Transaction t = jedis.multi();
t.decrBy("balance", 100);
t.exec();
```
    
3. **Поддержка команд**:  
    Dragonfly поддерживает все основные команды Redis (`SET`, `GET`, `HASH`, `ZSET`), но проверяйте [документацию](https://www.dragonflydb.io/docs/compatibility) для экзотических случаев.    

---
### 🛠 **Как запустить тестовый сервер?**
```bash
docker run -p 6379:6379 dragonflydb/dragonfly
```

**Официальная документация**:  
👉 [https://www.dragonflydb.io/docs/java](https://www.dragonflydb.io/docs/java)

---
