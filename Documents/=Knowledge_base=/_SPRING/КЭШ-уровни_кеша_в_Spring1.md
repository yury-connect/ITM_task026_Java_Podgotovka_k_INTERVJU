  Куширование на уровне **Spring**
**Абстракция кеширования Spring (*Spring Cache Abstraction*)**

---
## 1. Что такое Spring Cache Abstraction?

Это **высокоуровневый механизм**, который предоставляет единообразный API для работы с различными кеш-решениями (in-memory, Redis, Ehcache и др.). Он позволяет:
	
- **Кешировать результаты вызовов методов** (например, результаты тяжелых SQL-запросов или вычислений).
    
- **Инвалидировать кеш** при изменении данных.
    
- **Обновлять кеш** актуальными данными.
    
- **Работать с несколькими кеш-провайдерами** одновременно.

**!!! Главная идея**: вы отделяете бизнес-логику от логики кеширования, используя декларативный подход (аннотации или XML). Spring AOP перехватывает вызовы методов и применяет правила кеширования.

---
## 2. Основные компоненты абстракции

Давайте разберем ключевые сущности, из которых состоит *Spring Cache Abstraction.*

### 2.1. Аннотации (*точка входа для разработчика*)

Это то, с чем вы работаете непосредственно в коде.

|Аннотация|Назначение|Пример|
|---|---|---|
|**`@Cacheable`**|Помечает метод, результат которого должен **сохраняться в кеш**. При повторном вызове с теми же аргументами метод **не выполняется** — возвращается значение из кеша.|`@Cacheable("users")`  <br>`public User getUser(Long id) { ... }`|
|**`@CachePut`**|Помечает метод, который **обновляет кеш** (метод всегда выполняется, его результат помещается в кеш). Полезно для методов обновления данных.|`@CachePut(value = "users", key = "#user.id")`  <br>`public User updateUser(User user) { ... }`|
|**`@CacheEvict`**|Помечает метод, который **удаляет** одну или несколько записей из кеша. Используется для очистки устаревших данных.|`@CacheEvict(value = "users", key = "#id")`  <br>`public void deleteUser(Long id) { ... }`|
|**`@Caching`**|Группирует несколько операций кеширования на одном методе (например, обновить один кеш и очистить другой).|`@Caching(<br> evict = @CacheEvict("users"),<br> put = @CachePut(value = "activeUsers", key = "#user.active")<br>)`|
|**`@CacheConfig`**|Задает общие настройки кеширования на уровне класса (например, имя кеша по умолчанию, генератор ключей).|`@CacheConfig(cacheNames = {"users", "admins"})`|

### 2.2. Ключевые интерфейсы

#### 2.2.1. `CacheManager`
Это **центральный интерфейс**, который управляет кеш-хранилищами.
	
- **Ответственность**: предоставлять объекты `Cache` по их именам.
    
- **Основные методы**:
	    
    - `Cache getCache(String name)`: получить кеш по имени.
        
    - `Collection<String> getCacheNames()`: получить список всех доступных кешей.
    
- **Популярные реализации**:

|Реализация|Назначение|
|---|---|
|`ConcurrentMapCacheManager`|Кеш в памяти JVM (по умолчанию). Использует `ConcurrentHashMap`.|
|`CaffeineCacheManager`|Высокопроизводительная библиотека кеширования (замена Guava Cache).|
|`EhCacheCacheManager`|Интеграция с Ehcache (популярная библиотека для кеширования).|
|`RedisCacheManager`|Кеш в Redis (распределенное кеширование).|
|`CompositeCacheManager`|Объединяет несколько `CacheManager` (позволяет использовать цепочку).|

#### 2.2.2. `Cache`
Это **интерфейс**, представляющий конкретное хранилище кеша (по сути — «кеш-корзина»).
	
- **Ответственность**: выполнять операции CRUD над кеш-данными.
    
- **Основные методы**:
	    
    - `ValueWrapper get(Object key)`: получить значение по ключу.
        
    - `<T> T get(Object key, Class<T> type)`: получить значение с приведением типа.
        
    - `void put(Object key, Object value)`: сохранить пару "ключ-значение".
        
    - `void evict(Object key)`: удалить запись по ключу.
        
    - `void clear()`: очистить весь кеш.

#### 2.2.3. `KeyGenerator`
- **Ответственность**: генерировать **ключ** для кеша на основе параметров метода.
    
- **Стандартная стратегия**: если явно не задан через `@Cacheable(key = "...")`, то ключ формируется на основе **всех аргументов метода** (с использованием `SimpleKeyGenerator`).
    
- **Кастомизация**: можно реализовать свой `KeyGenerator` или использовать SpEL-выражения.

#### 2.2.4. `CacheResolver`
- **Ответственность**: определять, какой `Cache` использовать во время выполнения. Это более гибкий механизм, чем статическое имя в аннотации.
    
- **Используется редко**, но полезен, когда имя кеша зависит от контекста выполнения.

### 2.3. Как все связано (пошагово)
```text

```


1. Пользователь вызывает метод с @Cacheable
   ↓
2. Spring AOP перехватывает вызов (через CacheInterceptor)
   ↓
3. Генерируется КЛЮЧ (KeyGenerator или SpEL)
   ↓
4. По имени кеша запрашивается CacheManager.getCache("users")
   ↓
5. Выполняется Cache.get(key)
   ↓
   ЕСЛИ ЕСТЬ ЗНАЧЕНИЕ:
       ↓
       возвращается закешированный результат (метод НЕ выполняется)
   ЕСЛИ НЕТ ЗНАЧЕНИЯ:
       ↓
       выполняется оригинальный метод
       ↓
       результат сохраняется в кеш (Cache.put(key, result))
       ↓
       возвращается результат

---

## 3. Как включить и настроить Spring Cache

### 3.1. Включение кеширования

Добавьте аннотацию `@EnableCaching` к любому классу конфигурации:

java

@Configuration
@EnableCaching
public class AppConfig {
    // ...
}

### 3.2. Настройка CacheManager

Выберите и настройте подходящий `CacheManager`.

#### Пример с ConcurrentMapCacheManager (по умолчанию)

java

@Bean
public CacheManager cacheManager() {
    ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
    cacheManager.setAllowNullValues(false); // не разрешать null в кеше
    return cacheManager;
}

#### Пример с CaffeineCacheManager (высокая производительность)

java

@Bean
public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("users", "products");
    cacheManager.setCaffeine(Caffeine.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .maximumSize(1000)
    );
    return cacheManager;
}

#### Пример с RedisCacheManager (распределенное кеширование)

java

@Bean
public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(10))
        .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    
    return RedisCacheManager.builder(factory)
        .cacheDefaults(config)
        .build();
}

---

## 4. SpEL и генерация ключей

Spring Cache поддерживает **Spring Expression Language (SpEL)** для динамического формирования ключей и условий.

### 4.1. Генерация ключа через SpEL

Пример с использованием аргументов метода:

java

// Простое использование
@Cacheable(value = "users", key = "#id")
public User getUser(Long id) { ... }
// Сложный ключ из нескольких аргументов
@Cacheable(value = "users", key = "#firstName + '_' + #lastName")
public User getUserByName(String firstName, String lastName) { ... }
// Доступ к полям объекта
@Cacheable(value = "users", key = "#user.id")
public User updateUser(User user) { ... }
// Доступ к вложенным свойствам
@Cacheable(value = "orders", key = "#user.id + '_' + #order.status")
public Order getOrder(User user, Order order) { ... }

### 4.2. Условное кеширование

**`condition`** — кешировать только если условие истинно:

java

@Cacheable(value = "users", condition = "#id > 100")
public User getUser(Long id) { ... }

**`unless`** — НЕ кешировать, если условие истинно (проверяется после выполнения метода):

java

@Cacheable(value = "users", unless = "#result == null || #result.isInactive()")
public User getUser(Long id) { ... }

### 4.3. Доступные метаданные в SpEL

|Переменная|Описание|
|---|---|
|`#argumentName`|Аргумент метода по имени.|
|`#a0`, `#p0`|Аргумент метода по индексу (0-based).|
|`#result`|Результат выполнения метода (доступен в `unless`, `@CachePut`, `@CacheEvict`).|
|`#root.method`|Объект `Method`.|
|`#root.target`|Целевой объект, на котором вызван метод.|
|`#root.args`|Массив аргументов метода.|

---

## 5. Важные нюансы и подводные камни

### 5.1. Прокси-механизм (внутренние вызовы)

Spring Cache **НЕ** работает при внутренних вызовах методов (из одного метода класса в другой).

**ПРИМЕР ПРОБЛЕМЫ:**

java

@Service
public class UserService {
    
    @Cacheable("users")
    public User getById(Long id) { ... }
    
    public User getAndProcess(Long id) {
        // Внутренний вызов — кеш НЕ сработает!
        User user = getById(id);
        // ... обработка
        return user;
    }
}

**РЕШЕНИЯ:**

1. **Выделить в отдельный компонент** (рекомендуемый способ):
    
    java
    
    @Service
    public class UserService {
        @Autowired
        private UserCacheService cacheService; // отдельный бин
    }
    
2. **Использовать самоинъекцию**:
    
    java
    
    @Service
    public class UserService {
        @Autowired
        private UserService self;
        
        public User getAndProcess(Long id) {
            // Теперь кеш сработает
            User user = self.getById(id);
            return user;
        }
    }
    
3. **Включить режим AspectJ** (`@EnableCaching(mode = AdviceMode.ASPECTJ)`).
    

### 5.2. Управление null-значениями

По умолчанию, если метод возвращает `null`, Spring **НЕ** кеширует его. Это можно изменить:

java

@Cacheable(value = "users", key = "#id", unless = "#result == null")
public User getUser(Long id) { ... }

### 5.3. Инвалидация нескольких кешей

Используйте `@Caching` для группировки:

java

@Caching(evict = {
    @CacheEvict(value = "users", key = "#id"),
    @CacheEvict(value = "activeUsers", allEntries = true)
})
public void deleteUser(Long id) { ... }

### 5.4. Синхронное кеширование (эффект «собачьей свадьбы»)

Проблема: много потоков одновременно вызывают метод с одним и тем же ключом.  
**Решение**: параметр `sync = true` в `@Cacheable` (поддерживается некоторыми провайдерами):

java

@Cacheable(value = "users", key = "#id", sync = true)
public User getUser(Long id) { ... }

### 5.5. Таймауты и TTL

Настройка времени жизни записей зависит от провайдера:

java

// Caffeine
@Bean
public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager();
    cacheManager.setCaffeine(Caffeine.newBuilder()
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .maximumSize(1000));
    return cacheManager;
}
// Redis
@Bean
public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(5));
    return RedisCacheManager.builder(factory)
        .cacheDefaults(config)
        .build();
}

### 5.6. Кеш и многомодульные приложения

В распределенных системах (микросервисы) используйте распределенные кеши (Redis, Hazelcast), чтобы избежать рассинхронизации.

---

## 6. Практический пример комплексного использования

java

@Service
@CacheConfig(cacheNames = "users") // общее имя кеша
public class UserService {
    
    @Cacheable(key = "#id", unless = "#result == null")
    public User getUser(Long id) {
        // тяжелый запрос в БД
        return userRepository.findById(id).orElse(null);
    }
    
    @CachePut(key = "#user.id")
    public User updateUser(User user) {
        // обновляем данные
        return userRepository.save(user);
    }
    
    @CacheEvict(key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    @Cacheable(value = "activeUsers", key = "#role", condition = "#role != null")
    public List<User> getActiveUsers(String role) {
        // тяжелый запрос с условием
        return userRepository.findByActiveTrueAndRole(role);
    }
    
    @Caching(evict = {
        @CacheEvict(value = "users", key = "#user.id"),
        @CacheEvict(value = "activeUsers", allEntries = true)
    })
    public User deactivateUser(User user) {
        user.setActive(false);
        return userRepository.save(user);
    }
}

---

## 7. Сравнение популярных кеш-провайдеров

|Провайдер|Тип|Скорость|Распределенный|TTL|Персистентность|
|---|---|---|---|---|---|
|**ConcurrentMap**|In-Memory|Высокая|Нет|Да|Нет|
|**Caffeine**|In-Memory|Очень высокая|Нет|Да|Нет|
|**Ehcache**|In-Memory/Disk|Высокая|Да (с Terracotta)|Да|Да|
|**Redis**|Remote|Средняя|Да|Да|Да|
|**Hazelcast**|In-Memory|Высокая|Да|Да|Да (опционально)|

---

## 8. Заключение

Spring Cache Abstraction — это мощный и гибкий инструмент, который:

- **Снижает нагрузку** на базу данных и внешние системы.
    
- **Ускоряет работу** приложения (снижение latency).
    
- **Прост в использовании** (декларативный подход через аннотации).
    
- **Не зависит** от конкретного кеш-провайдера (можно легко заменить один на другой).
    
- **Интегрируется** с другими модулями Spring (Spring Boot Auto-configuration, Spring Data).
    

**Главное правило**: используйте кеширование там, где данные **часто читаются** и **редко изменяются** (справочники, настройки, результаты сложных вычислений). Всегда учитывайте стратегию инвалидации, чтобы избежать использования устаревших данных.