# **Работа с Redis из Spring Boot**
**Простое руководство по кешированию и работе с ключами.**

---
## **1. Подключение `Redis` к `Spring Boot`**

### **① Добавьте зависимости**

В `pom.xml` (*Maven*) или `build.gradle` (*Gradle*):
#### **Maven**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
#### **Gradle**
```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
```

---
### **② Настройте подключение**

В `application.yml` (или `application.properties`):
```yaml
spring:
  redis:
    host: localhost  # или IP вашего Redis-сервера
    port: 6379       # стандартный порт Redis
    password: ваш-пароль (если есть)
    database: 0      # номер БД (от 0 до 15)
```

---
## **2. Примеры использования**

### **① `RedisTemplate` — для ручного управления**

Создайте конфигурационный класс:
```java
@Configuration
public class RedisConfig {
	
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
```

**Как использовать** (*в сервисе или контроллере*):
```java
@Service
public class RedisService {
	
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
    // Запись значения
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
	
    // Чтение значения
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }
	
    // Удаление ключа
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
}
```

---
### **② Spring Cache — автоматическое кеширование**

#### **Включите кеширование**
В `application.yml`:
```yaml
spring:
  cache:
    type: redis
```

#### **Добавьте аннотации к методам**
```java
@Service
public class UserService {
	
    @Cacheable(value = "users", key = "#id")  // Кеширует результат
    public User getUserById(Long id) {
        // Логика загрузки пользователя из БД
        return userRepository.findById(id).orElseThrow();
    }
	
    @CacheEvict(value = "users", key = "#id")  // Удаляет кеш при обновлении
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
```

---
## **3. Полезные команды Redis**

|Команда|Описание|
|---|---|
|`KEYS *`|Показать все ключи|
|`GET ключ`|Получить значение|
|`SET ключ значение`|Записать значение|
|`DEL ключ`|Удалить ключ|
|`TTL ключ`|Проверить время жизни (TTL)|
|`EXPIRE ключ 60`|Установить TTL = 60 секунд|

---
## **4. Примеры JSON-кеширования**

Если нужно хранить объекты в JSON:
```java
@Bean
public RedisTemplate<String, User> userRedisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, User> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
    return template;
}
```

**Использование**:
```java
@Autowired
private RedisTemplate<String, User> userRedisTemplate;

public void cacheUser(User user) {
    userRedisTemplate.opsForValue().set("user:" + user.getId(), user);
}
```

---
## **5. Итог**
- **Redis + Spring Boot** = быстрый доступ к данным.    
- **`RedisTemplate`** — для ручного управления ключами.    
- **`@Cacheable`** — для автоматического кеширования методов.    

> **Совет**: Используйте **TTL (*время жизни ключей*)**, чтобы `Redis` не переполнялся.
```java
redisTemplate.opsForValue().set("tempData", data, Duration.ofMinutes(10));  // Автоудаление через 10 мин
```
