# Группа 8: Безопасность (Spring Security) 
— Для Middle часто базово

Spring Security — тема, где Middle должен уверенно ориентироваться в базовых концепциях. Отвечаю четко и по делу.

---

### 86. Что такое Spring Security? Основные компоненты (SecurityContext, Authentication, Principal)

**Ответ:** Spring Security — это **фреймворк для обеспечения безопасности** приложений на Spring, который предоставляет аутентификацию (кто ты), авторизацию (что тебе можно) и защиту от распространенных атак (CSRF, session fixation и др.).

**Основные компоненты:**

|Компонент|Описание|Где хранится|
|---|---|---|
|**`SecurityContext`**|Контейнер, который хранит информацию о безопасности текущего потока (кто аутентифицирован).|В `SecurityContextHolder` (по умолчанию в `ThreadLocal`)|
|**`Authentication`**|Интерфейс, представляющий **токен аутентификации** — содержит principal, credentials, authorities.|Внутри `SecurityContext`|
|**`Principal`**|Интерфейс, представляющий **пользователя** (обычно `UserDetails`). Доступен через `Authentication.getPrincipal()`.|Внутри `Authentication`|

**Как это связано в коде:**
```java
// Получить текущего пользователя
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
String username = auth.getName();                    // имя пользователя
UserDetails user = (UserDetails) auth.getPrincipal(); // детали пользователя
Collection<? extends GrantedAuthority> authorities = auth.getAuthorities(); // роли/права
// В контроллере — упрощенно через @AuthenticationPrincipal
@GetMapping("/profile")
public UserDto profile(@AuthenticationPrincipal UserDetails user) {
    return UserDto.from(user);
}
```

**Жизненный цикл:**

1. Пользователь отправляет запрос с credentials (логин/пароль, JWT).
    
2. Фильтры Spring Security создают `Authentication` (обычно `UsernamePasswordAuthenticationToken`).
    
3. `AuthenticationManager` проверяет credentials.
    
4. Успешная аутентификация → `Authentication` кладется в `SecurityContext`.
    
5. `SecurityContext` сохраняется в `SecurityContextHolder` (и опционально в сессии для stateful-приложений).
    
6. При последующих запросах `SecurityContext` восстанавливается из сессии (или из JWT).
    

**Важный нюанс:** `SecurityContextHolder` по умолчанию использует `ThreadLocal`, поэтому в асинхронной обработке (например, `@Async`) контекст **не передается** автоматически. Для передачи нужно настроить `SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)` или использовать `DelegatingSecurityContextRunnable`.

---

### 87. Что такое `SecurityFilterChain`? Как работают фильтры?

**Ответ:** `SecurityFilterChain` — это интерфейс, который определяет, **какие фильтры безопасности** должны быть применены к определенным URL-паттернам.

**Как работают фильтры Spring Security:**

1. В контейнере сервлетов (Tomcat) есть цепочка фильтров.
    
2. Spring Security встраивает в эту цепочку свой **`DelegatingFilterProxy`**, который делегирует вызовы внутреннему `FilterChainProxy`.
    
3. `FilterChainProxy` содержит список `SecurityFilterChain`.
    
4. Для каждого запроса находится подходящий `SecurityFilterChain` (по `matches(request)`).
    
5. Выполняются все фильтры из этой цепочки **в определенном порядке**.
    

**Стандартные фильтры (в порядке выполнения):**

|Фильтр|Назначение|
|---|---|
|`CsrfFilter`|Защита от CSRF-атак|
|`LogoutFilter`|Обработка выхода из системы (/logout)|
|`UsernamePasswordAuthenticationFilter`|Аутентификация по логину/паролю (/login)|
|`BasicAuthenticationFilter`|Basic Auth|
|`RequestCacheAwareFilter`|Кэширование запросов после аутентификации|
|`SecurityContextHolderAwareRequestFilter`|Обертка request с методами безопасности|
|`AnonymousAuthenticationFilter`|Создание anonymous-пользователя, если нет аутентификации|
|`SessionManagementFilter`|Управление сессиями|
|`ExceptionTranslationFilter`|Обработка исключений безопасности (403, 401)|
|`FilterSecurityInterceptor`|**Финальная авторизация** — проверяет права доступа перед вызовом контроллера|

**Пример конфигурации `SecurityFilterChain`:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")  // этот chain только для /api/**
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults());
        return http.build();
    }
    
    @Bean
    public SecurityFilterChain anotherChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/admin/**")
            .authorizeHttpRequests(auth -> auth
                .anyRequest().hasRole("ADMIN")
            )
            .formLogin(withDefaults());
        return http.build();
    }
}
```

**Порядок цепочек:** важен порядок объявления `@Bean` методов. Более специфичные паттерны должны идти раньше общих.

---

### 88. Что такое `@PreAuthorize`, `@PostAuthorize`, `@Secured`?

**Ответ:** Это аннотации для **method-level security** — проверки прав доступа **на уровне методов** (сервисов, контроллеров).

**Сравнение аннотаций:**

|Аннотация|Выражение|Проверка|Включение|
|---|---|---|---|
|**`@Secured`**|Только роли: `{"ROLE_ADMIN", "ROLE_USER"}`|До вызова метода|`@EnableGlobalMethodSecurity(securedEnabled = true)`|
|**`@PreAuthorize`**|SpEL-выражения: `hasRole('ADMIN') and #id == principal.id`|**До** вызова метода|`@EnableGlobalMethodSecurity(prePostEnabled = true)`|
|**`@PostAuthorize`**|SpEL-выражение, доступ к `returnObject`|**После** вызова метода|`@EnableGlobalMethodSecurity(prePostEnabled = true)`|

**Примеры:**
```java
@RestController
public class UserController {
    
    // Только ADMIN
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/user/{id}")
    public void delete(@PathVariable Long id) { ... }
    
    // ADMIN или владелец ресурса (id из параметра)
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) { ... }
    
    // Проверка после вызова: только ADMIN или владелец возвращенного объекта
    @PostAuthorize("hasRole('ADMIN') or returnObject.id == authentication.principal.id")
    @GetMapping("/user/me")
    public User getCurrentUser() { ... }
    
    // С проверкой разрешения (permission)
    @PreAuthorize("hasPermission(#documentId, 'Document', 'READ')")
    @GetMapping("/document/{id}")
    public Document getDocument(@PathVariable Long documentId) { ... }
}
```

**Важные SpEL-переменные в `@PreAuthorize`:**

|Переменная|Описание|
|---|---|
|`authentication`|Объект `Authentication`|
|`principal`|`principal` из `Authentication` (обычно `UserDetails`)|
|`#paramName`|Значение параметра метода по имени|
|`#args`|Массив всех параметров|
|`hasRole('ROLE_NAME')`|Проверка роли (автоматически добавляет `ROLE_` префикс)|
|`hasAuthority('AUTHORITY')`|Проверка authority (без автоматического префикса)|
|`permitAll` / `denyAll`|Все разрешено / запрещено|

**Включение method security:**
```java
@Configuration
@EnableGlobalMethodSecurity(
    securedEnabled = true,   // для @Secured
    prePostEnabled = true     // для @PreAuthorize/@PostAuthorize
)
public class MethodSecurityConfig { }
```

**Рекомендация:** Используйте `@PreAuthorize` — он самый гибкий. `@Secured` считается устаревшим по возможностям.

---

### 89. Что такое `UserDetailsService`?

**Ответ:** `UserDetailsService` — это **сервис для загрузки пользователя** по его идентификатору (обычно имени пользователя или email). Это основной интерфейс, который необходимо реализовать для интеграции Spring Security с вашим хранилищем пользователей (БД, LDAP, внешний API).

**Интерфейс:**
```java
public interface UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
```

**Пример реализации:**
```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPasswordHash())
            .authorities(user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .toArray(GrantedAuthority[]::new))
            .accountExpired(!user.isActive())
            .accountLocked(user.isLocked())
            .credentialsExpired(false)
            .disabled(!user.isEnabled())
            .build();
    }
}
```

**Как используется Spring Security:**

1. При аутентификации `AuthenticationManager` (через `DaoAuthenticationProvider`) вызывает `UserDetailsService.loadUserByUsername()`.
    
2. Получает `UserDetails` (содержит пароль, роли, статус аккаунта).
    
3. Сравнивает предоставленный пароль с `UserDetails.getPassword()` (через `PasswordEncoder`).
    
4. Если совпадает — создает `Authentication` объект.
    

**`UserDetails` — что это:** Интерфейс, представляющий аутентифицированного пользователя:
```java
public interface UserDetails {
    String getUsername();
    String getPassword();
    Collection<? extends GrantedAuthority> getAuthorities();
    boolean isAccountNonExpired();
    boolean isAccountNonLocked();
    boolean isCredentialsNonExpired();
    boolean isEnabled();
}
```

**Встроенные реализации:**

- `InMemoryUserDetailsManager` — для тестов (пользователи в памяти).
    
- `JdbcUserDetailsManager` — для загрузки из БД по стандартной схеме.
    
- `LdapUserDetailsManager` — для LDAP.
    

**Важный нюанс:** `UserDetailsService` используется только для **аутентификации**. Для авторизации (проверки прав) Spring Security использует `GrantedAuthority`, которые возвращаются из `UserDetails.getAuthorities()`.

---

### 90. Как работает Basic Auth и JWT в Spring Security?

**Ответ:** Basic Auth и JWT — это два разных механизма аутентификации.

**Basic Auth (простой, но небезопасный без HTTPS):**

1. Клиент отправляет заголовок: `Authorization: Basic base64(username:password)`
    
2. `BasicAuthenticationFilter` перехватывает запрос.
    
3. Декодирует credentials, создает `UsernamePasswordAuthenticationToken`.
    
4. `AuthenticationManager` (через `DaoAuthenticationProvider`) вызывает `UserDetailsService` и проверяет пароль через `PasswordEncoder`.
    
5. При успехе — `Authentication` кладется в `SecurityContext`.
    

**Настройка Basic Auth:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());  // включаем Basic Auth
        return http.build();
    }
}
```

**JWT (JSON Web Token) — stateless аутентификация:**

**Как работает JWT в Spring Security:**

1. **Логин:** Клиент отправляет логин/пароль на `/login`.
    
2. **Генерация JWT:** Сервер проверяет credentials, создает JWT (содержит userId, роли, время жизни), подписывает его секретным ключом.
    
3. **Ответ:** Сервер возвращает JWT клиенту.
    
4. **Последующие запросы:** Клиент отправляет JWT в заголовке: `Authorization: Bearer <jwt-token>`.
    
5. **Валидация JWT:** Кастомный фильтр (или `JwtAuthenticationFilter`) перехватывает запрос, валидирует JWT (подпись, срок годности), создает `Authentication` и кладет в `SecurityContext`.
    

**Пример JWT фильтра:**
```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String token = extractToken(request);
        
        if (token != null && tokenProvider.validateToken(token)) {
            Authentication auth = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        
        chain.doFilter(request, response);
    }
    
    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
```

**Настройка Spring Security для JWT:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // JWT не требует CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
```

**Сравнение Basic Auth vs JWT:**

|Характеристика|Basic Auth|JWT|
|---|---|---|
|State|Stateful (обычно с сессией)|Stateless|
|Хранение на сервере|Сессия в памяти или БД|Ничего не хранится|
|Масштабирование|Требует session affinity или распределенных сессий|Легко (любой сервер валидирует JWT)|
|Истечение сессии|Управляется на сервере|Закодировано в токене|
|Отзыв токена|Легко (удалить сессию)|Сложно (нужен blacklist)|
|Безопасность|Пароль при каждом запросе (только с HTTPS)|Токен может быть скомпрометирован|

**Рекомендация:** Для REST API используйте **JWT** — он stateless, хорошо масштабируется. Basic Auth используйте только для простых интеграций или с HTTPS + API ключами.

---

### 91. Что такое `PasswordEncoder`? (`BCryptPasswordEncoder`)

**Ответ:** `PasswordEncoder` — интерфейс Spring Security для **одностороннего хеширования паролей**. Он отвечает за:

- Хеширование пароля при регистрации.
    
- Проверку пароля при аутентификации (сравнение предоставленного пароля с сохраненным хешем).
    

**Интерфейс:**
```java
public interface PasswordEncoder {
    String encode(CharSequence rawPassword);
    boolean matches(CharSequence rawPassword, String encodedPassword);
}
```

**Почему нельзя хранить пароли в открытом виде:**

- При утечке БД злоумышленник получает все пароли.
    
- Пользователи часто используют одинаковые пароли на разных сайтах.
    

**Основные реализации `PasswordEncoder`:**

|Реализация|Алгоритм|Соль|Рекомендация|
|---|---|---|---|
|`NoOpPasswordEncoder`|Нет (plain text)|Нет|**Только для тестов!**|
|`BCryptPasswordEncoder`|BCrypt|Встроена в хеш|**Рекомендуемый**|
|`Pbkdf2PasswordEncoder`|PBKDF2|Да|Хорош для legacy систем|
|`SCryptPasswordEncoder`|SCrypt|Да|Более устойчив к GPU-атакам|
|`Argon2PasswordEncoder`|Argon2|Да|Самый безопасный, но ресурсоемкий|

**BCryptPasswordEncoder — стандарт де-факто:**
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // strength = 10 по умолчанию
}
// Регистрация
String encodedPassword = passwordEncoder.encode("userPassword");
// Результат: $2a$10$N9qo8uLOickgx2ZMRZoMy.MrCQkZqHwX5lMq7P8YkZqHwX5lMq7P8Y
// Аутентификация
boolean matches = passwordEncoder.matches("rawPassword", storedHash);
```

**Структура BCrypt хеша:** `$2a$10$N9qo8uLOickgx2ZMRZoMy.MrCQkZqHwX5lMq7P8YkZqHwX5lMq7P8Y`

- `2a` — версия алгоритма    
- `10` — cost factor (2^10 итераций = 1024)    
- `N9qo8uLOickgx2ZMRZoMy.` — соль (22 символа)    
- Остальное — хеш    

**Как использовать с `UserDetailsService`:**
```java
@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public void register(UserRegistrationDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
    }
}
```

**Миграция с legacy хешей (например, MD5):**
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new DelegatingPasswordEncoder("bcrypt", Map.of(
        "bcrypt", new BCryptPasswordEncoder(),
        "md5", new MessageDigestPasswordEncoder("MD5"),
        "sha256", new MessageDigestPasswordEncoder("SHA-256")
    ));
}
// Хеши в БД должны иметь префикс: {bcrypt}..., {md5}...
```

**Важный нюанс:** Никогда не используйте `MessageDigestPasswordEncoder` с MD5 или SHA для новых проектов — они не имеют соли и устойчивы к радужным таблицам.

---

### 92. Что такое CSRF? Когда его надо отключать (REST API)?

**Ответ:** **CSRF (Cross-Site Request Forgery)** — атака, при которой злоумышленник заставляет браузер пользователя отправить поддельный запрос к сайту, где пользователь уже аутентифицирован.

**Как работает CSRF-атака:**

1. Пользователь залогинен на `bank.com` (в браузере есть session cookie).
    
2. Пользователь переходит на `evil.com`.
    
3. `evil.com` отправляет на `bank.com/transfer?to=attacker&amount=1000` (например, через невидимую форму или изображение).
    
4. Браузер автоматически отправляет session cookie с запросом.
    
5. `bank.com` думает, что запрос от пользователя, и выполняет перевод.
    

**Защита CSRF:** сервер генерирует уникальный токен, который клиент должен отправлять в каждом state-changing запросе (POST, PUT, DELETE). Без токена — запрос отклоняется.

**В Spring Security CSRF защита включена по умолчанию:**
```java
// По умолчанию CSRF включен
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(Customizer.withDefaults())  // включен по умолчанию
        .authorizeHttpRequests(...);
    return http.build();
}
```

**Когда нужно **отключать** CSRF (и почему):**

|Сценарий|Отключать?|Причина|
|---|---|---|
|**REST API (stateless)**|✅ **ДА**|CSRF защита основана на сессиях и токенах в cookies. Для stateless JWT аутентификации CSRF не нужен|
|**Mobile приложение (iOS/Android)**|✅ ДА|У мобильных приложений нет "браузера" и автоматической отправки cookies|
|**Backend-for-frontend с SPA**|❌ НЕТ (или использовать токены)|SPA работает в браузере, нужна защита|
|**Админка (Web UI)**|❌ НЕТ|Web-интерфейс должен быть защищен от CSRF|
|**Public API (без cookies)**|✅ ДА|Если аутентификация через API-ключ в заголовке, CSRF не актуален|

**Пример отключения CSRF для REST API с JWT:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // JWT stateless, CSRF не нужен
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
```

**Альтернатива для SPA (не отключать CSRF полностью):**
```java
http.csrf(csrf -> csrf
    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
);
```

Токен передается в cookie с флагом `HttpOnly=false`, чтобы SPA могла прочитать его и отправить в заголовке `X-XSRF-TOKEN`.

**Важное правило:** Никогда не отключайте CSRF для Web-приложений с HTML-формами и session-based аутентификацией — это критическая уязвимость. Для REST API с JWT — отключайте безопасно.

---
