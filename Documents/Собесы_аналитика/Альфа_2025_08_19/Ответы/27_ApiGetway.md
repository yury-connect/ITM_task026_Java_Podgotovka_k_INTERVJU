# API Gateway — что это и зачем нужен (кратко и по делу)

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

## Короткая выжимка для собеседования (30–45 сек)

«API Gateway — это фронт для всех клиентов: он маршрутизирует запросы, выполняет аутентификацию/авторизацию (JWT/Keycloak), ставит rate-limit, кэширует idempotent GET-ответы и результаты JWKS/introspection, предоставляет retry/circuit-breaker и собирает логи/метрики. Мы используем gateway как слой политики и безопасности; тяжёлую бизнес-логику оставляем в сервисах.»

---

## Что упомянуть, чтобы произвести впечатление

- Кэшируете **JWKS** + **introspection** с TTL.
    
- Используете **token exchange** / client_credentials для S2S-вызовов.
    
- Gateway + Service Mesh (Envoy/ Istio) для разделения north-south и east-west трафика.
    
- Следите за `Vary`/ETag и умеете делать purge на invalidate.
    

---