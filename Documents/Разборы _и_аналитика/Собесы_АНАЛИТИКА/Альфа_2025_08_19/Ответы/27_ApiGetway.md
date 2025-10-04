# **API Gateway** — что это и зачем нужен *(кратко и по делу)*

**Определение:** шлюз, стоящий на «границе» между клиентами и бэкенд-сервисами, который принимает все входящие запросы и выполняет центральные функции: маршрутизация, безопасность, трансформация и наблюдаемость.

---
## Что решает (*конкретно*)

- 🔀 **Маршрутизация** — направляет запросы к нужному микросервису по URL, версии, заголовкам, cookie, A/B-флагам.    
- 🔐 **Аутентификация/авторизация** — валидация JWT, OAuth2 introspection, mTLS, проверка scope/roles.    
- 🛡️ **Защита** — rate-limiting, throttling, IP-whitelisting, WAF-правила.    
- ♻️ **Надёжность** — retries, circuit breaker, fallback-responses.    
- ⚡ **Кэширование** — кэширование GET-ответов, результатов авторизации (token introspection / public keys), снижающее нагрузку на бекенд.    
- 🔧 **Трансформация** — изменение заголовков, тело запроса/ответа, агрегация нескольких сервисов в один ответ (BFF).    
- 📊 **Observability** — централизованные логи, метрики, трассировки (добавляет correlation id).    
- 📦 **Политики версий и маршрутов** — A/B, canary, blue/green rollout.    

---
## Кеш: что и как кэшировать (*практически*)

- ✅ **Кэшировать**: idempotent GET-ответы, статические ответы, результаты expensive lookups; **JWKS/public keys** и (с осторожностью) результаты token introspection с TTL.
    
- ⚠️ **Не кэшировать**: персонализированные ответы без Vary, ответы с приватными заголовками, чувствительные данные без контроля.
    
- 🕒 **Инвалидация**: TTL, ETag/If-None-Match, Cache-Control, explicit purge API.
    
- 🧠 **Хитрость для собеса:** скажи про `Vary` header и про кэширование по ключу (URL + query + headers) — это показывает детальность понимания.    

---
## Авторизация/аутентификация — как обычно делают

1. Client → Gateway: проверка JWT (подпись через JWKS) или call to Keycloak (introspection) при необходимости.
    
2. Gateway валидирует `iss`, `aud`, `exp`, `scope` и **может**:    
    - прокинуть `Authorization` дальше, или        
    - заменить на service-to-service token (token exchange / client_credentials).
    
3. Для критичных вызовов — mTLS между Gateway и internal сервисами.
    
4. Кешировать JWKS и результаты introspection с небольшим TTL.    

---
## Где и как размещать

- Edge Gateway (наружный) — входящий трафик.    
- Internal Gateway / Sidecar — внутренняя маршрутизация, BFF-слои.    
- Pattern: API Gateway + Service Mesh (для east-west traffic) — gateway отвечает за north-south, mesh — за внутрикластерную сетку.    

---
## Реализации (*коротко, для упоминания*)

- Open source: **Kong**, **Traefik**, **NGINX**, **Envoy** / **Istio** (as gateway), **Spring Cloud Gateway**, **Zuul**.    
- Cloud: **AWS API Gateway**, **Azure API Management**, **GCP API Gateway**.    

---
## Операционные и архитектурные замечания

- Gateway — single point of entry: нужен HA, autoscale и кеш-кластер.    
- Логирование/трейсинг + correlation id обязательны.    
- Стараться не делать в Gateway тяжёлую бизнес-логику — только маршрутизация/политики/агрегация.    
- Для high-throughput: использовать асинхронные модели и локальный LRU/Redis кеш.    

---
## Короткая выжимка для собеседования (*30–45 сек*)

«API Gateway — это фронт для всех клиентов: он маршрутизирует запросы, выполняет аутентификацию/авторизацию (JWT/Keycloak), ставит rate-limit, кэширует idempotent GET-ответы и результаты JWKS/introspection, предоставляет retry/circuit-breaker и собирает логи/метрики. Мы используем gateway как слой политики и безопасности; тяжёлую бизнес-логику оставляем в сервисах.»

---
## Что упомянуть, чтобы произвести впечатление

- Кэшируете **JWKS** + **introspection** с TTL.    
- Используете **token exchange** / client_credentials для S2S-вызовов.    
- Gateway + Service Mesh (Envoy/ Istio) для разделения north-south и east-west трафика.
- Следите за `Vary`/ETag и умеете делать purge на invalidate.    

------
---
---
# Короткая фраза для собеса (*1–2 предложения*)

«API-Gateway — это фронт для клиентов: маршрутизует запросы, выполняет аутентификацию/авторизацию (JWT/Keycloak), ставит rate-limit и кэширует idempotent GET/результаты introspection/JWKS; тяжёлую бизнес-логику оставляем в микросервисах.» ✅

# Примеры конфигураций — кратко и по делу

## 1) Spring Cloud Gateway (application.yml + Security)

**`application.yml`**
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: notify
          uri: lb://NOTIFY-SERVICE
          predicates:
            - Path=/api/notify/**
          filters:
            - RemoveRequestHeader=Cookie
            - AddRequestHeader=X-Request-Id, #{T(java.util.UUID).randomUUID().toString()}
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: https://auth.company/realms/myrealm/protocol/openid-connect/certs
```

**Класс security** (*упрощённо*):
```java
@EnableWebSecurity
public class GatewaySecurityConfig {
  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http
      .authorizeExchange(ex -> ex
        .pathMatchers("/public/**").permitAll()
        .anyExchange().authenticated()
      )
      .oauth2ResourceServer(o -> o.jwt()); // валидирует JWT по JWKS
    return http.build();
  }
}
```

**Пояснения**:
- Gateway валидирует JWT (подпись, exp, aud/iss).    
- Можно **инвалидировать/кэшировать JWKS** на стороне gateway (внутренний кеш, TTL).    
- Gateway может заменять/обменивать токен (token exchange) перед внутри-сервисным вызовом.    

---
## 2) **NGINX** (*edge*) — маршрутизация + кэш GET + forward auth

**`nginx.conf`** (*фрагмент*):
```nginx
proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=api_cache:10m max_size=1g inactive=10m use_temp_path=off;

server {
  listen 443 ssl;
  server_name api.company.local;

  location /api/ {
    # optional: external auth (token introspection) endpoint
    auth_request /_introspect;

    # cache idempotent GET responses
    proxy_cache api_cache;
    proxy_cache_key "$scheme$proxy_host$request_uri";
    proxy_cache_valid 200 10s;   # short TTL, safe for test/prod tuning
    add_header X-Cache-Status $upstream_cache_status;

    proxy_pass http://backend_pool;
    proxy_set_header Authorization $http_authorization; # forward token or replace
    proxy_set_header X-Request-Id $request_id;
  }

  location = /_introspect {
    internal;
    proxy_pass http://auth-introspect/internal; # introspection implementation
    proxy_pass_request_body off;
    proxy_set_header Content-Length "";
    proxy_set_header X-Original-URI $request_uri;
  }
}
```

**Пояснения**:
- `auth_request` делает синхронную проверку токена (можно кешировать результат introspection).    
- `proxy_cache` — для GET/статических ответов; TTL короткий, инвалидация через purge.    

---
## Практические нюансы / что упомянуть

- ✅ **Кэшировать:** idempotent GET, JWKS, результаты introspection (с TTL).    
- ⚠️ **Не кэшировать:** персонализированные ответы без Vary/условий.
- 🔁 **Token exchange / client_credentials:** Gateway может выполнить обмен токенов (token exchange) или запросить service-to-service token (client_credentials) и вызвать внутренний сервис от своего имени.    
- 🔒 **mTLS** между Gateway и внутренними сервисами — для повышения доверия.    
- 🧾 **Observability:** Gateway добавляет correlation id, собирает логи/метрики/traces.
---
