Делаем **глубокое, но структурированное** объяснение про **JWT** (обычно пишут «JWT», не GWT) — от структуры до практических деталей, в том числе безопасность и эксплуатация.

---
# 1) Что такое JWT (*в одном предложении*)

**JSON Web Token (JWT)** — это компактный, URL-безопасный формат для передачи утверждений (*claims*) между сторонами в виде подписанного (*и/или зашифрованного*) JSON-объекта. [JSON Web Tokens - jwt.io](https://jwt.io/introduction?utm_source=chatgpt.com)[datatracker.ietf.org](https://datatracker.ietf.org/doc/html/rfc7519?utm_source=chatgpt.com)

---
# 2) Общая структура (*три части*)

**`JWT`** состоит из **трёх** частей, разделённых точками:
**`<base64url(header)>.<base64url(payload)>.<base64url(signature)>`**

- **Header (*заголовок*)** — JSON, указывает алгоритм подписи (`alg`) и тип (`typ: "JWT"`). Пример: `{"alg":"RS256","typ":"JWT","kid":"abc123"}`. [datatracker.ietf.org](https://datatracker.ietf.org/doc/html/rfc7519?utm_source=chatgpt.com)[JSON Web Tokens - jwt.io](https://jwt.io/?utm_source=chatgpt.com)
    
- **Payload (*тело / claims*)** — набор утверждений (*claims*): зарегистрированные (`iss`, `sub`, `aud`, `exp`, `iat`, `nbf`, `jti`), публичные и приватные (custom). Пример: `{"sub":"123","scope":"notify","exp":1710000000}`. [datatracker.ietf.org](https://datatracker.ietf.org/doc/html/rfc7519?utm_source=chatgpt.com)
    
- **Signature (*подпись*)** — криптографическая подпись над `base64url(header) + "." + base64url(payload)`, созданная с использованием алгоритма из header (HMAC SHA, RSA, ECDSA). Подпись обеспечивает целостность и аутентичность. [stytch.com](https://stytch.com/blog/rfc-7519-jwt-part-1/?utm_source=chatgpt.com)[IBM](https://www.ibm.com/docs/en/cics-ts/6.x?topic=cics-json-web-token-jwt&utm_source=chatgpt.com

---
# 3) Примеры алгоритмов подписи

- **HS256** — симметричный HMAC-SHA256 (один секрет для подписи и проверки).
    
- **RS256 / ES256** — асимметричные (RSA / ECDSA): приватный ключ подписывает, публичный ключ проверяет. Ассиметрия предпочтительна для распределённых систем. [JSON Web Tokens - jwt.io](https://jwt.io/?utm_source=chatgpt.com)[Auth0](https://auth0.com/docs/secure/tokens/json-web-tokens?utm_source=chatgpt.com)    

---
# 4) Claims — что и зачем

- **Registered claims (*стандартные*):**    
    - `iss` — issuer (*кто выпустил токен*).        
    - `sub` — subject (*кому выдано, например userId или serviceId*).        
    - `aud` — audience (*для кого токен предназначен — ресурс/сервис*).        
    - `exp` — expiration time (*время жизни*).        
    - `nbf` — not before (*не действителен до*).        
    - `iat` — issued at (*время выпуска*).        
    - `jti` — уникальный id токена (можно использовать для ревокации/отслеживания). [datatracker.ietf.org](https://datatracker.ietf.org/doc/html/rfc7519?utm_source=chatgpt.com)
        
- **Custom claims** — роли, scope, tenantId и т.д. Осторожно: не храните в payload чувствительные данные (payload не зашифрован по умолчанию). [owasp.org](https://owasp.org/www-project-web-security-testing-guide/latest/4-Web_Application_Security_Testing/06-Session_Management_Testing/10-Testing_JSON_Web_Tokens?utm_source=chatgpt.com)    

---
# 5) Как проверять JWT на стороне Resource Server (*пошагово*)

1. Проверить формат (3 части) и декодировать header/payload (base64url).
    
2. Проверить `alg` — принимать только ожидаемые алгоритмы (не доверять заголовку без проверки). [owasp.org](https://owasp.org/www-project-web-security-testing-guide/latest/4-Web_Application_Security_Testing/06-Session_Management_Testing/10-Testing_JSON_Web_Tokens?utm_source=chatgpt.com)
    
3. Проверить подпись: для RS/ES — получить публичный ключ (см. JWKS) и проверить подпись; для HS — использовать общий секрет. [Auth0](https://auth0.com/docs/secure/tokens/json-web-tokens/json-web-key-sets?utm_source=chatgpt.com)[stytch.com](https://stytch.com/blog/understanding-jwks/?utm_source=chatgpt.com)
    
4. Проверить время жизни: `exp` > now, `nbf` <= now. [datatracker.ietf.org](https://datatracker.ietf.org/doc/html/rfc7519?utm_source=chatgpt.com)
    
5. Проверить `iss` и `aud` (чтобы токен выдан вашим авторизационным сервером и предназначен для этого ресурса).
    
6. При необходимости проверить `jti` в списке отозванных токенов (или использовать introspection). [datatracker.ietf.org](https://datatracker.ietf.org/doc/html/rfc7662?utm_source=chatgpt.com)    

---

# 6) JWKS, `kid` и ротация ключей

- Authorization Server (например Keycloak) публикует **JWKS** (JSON Web Key Set) — набор публичных ключей по URL (`/.well-known/jwks.json`). Resource server использует JWKS для проверки подписи (по `kid` в header выбирает нужный ключ). [Auth0](https://auth0.com/docs/secure/tokens/json-web-tokens/json-web-key-sets?utm_source=chatgpt.com)[stytch.com](https://stytch.com/blog/understanding-jwks/?utm_source=chatgpt.com)
    
- **Ротация ключей**: авторизационный сервер добавляет новый ключ в JWKS, помечает старый как deprecated и удаляет его после истечения времени действия токенов. Клиентам советуют периодически подтягивать JWKS и кешировать с TTL. [authgear.com](https://www.authgear.com/post/what-is-jwks?utm_source=chatgpt.com)[SuperTokens](https://supertokens.com/blog/understanding-jwks?utm_source=chatgpt.com)
    

---

# 7) Разница между подписанным (JWS) и зашифрованным (JWE)

- **JWS** — JSON Web Signature (подписанный): payload читаем, но гарантируется целостность/подлинность.
    
- **JWE** — JSON Web Encryption (зашифрованный): payload шифруется, содержимое скрыто. Часто используют JWS+JWE (nested) если нужно и подпись, и конфиденциальность. [datatracker.ietf.org](https://datatracker.ietf.org/doc/html/rfc7519?utm_source=chatgpt.com)
    

---

# 8) Жизненный цикл токенов: access & refresh

- **Access token (JWT)** — короткий срок жизни (минуты/часы). Используется для доступа к ресурсам. [curity.io](https://curity.io/resources/learn/jwt-best-practices/?utm_source=chatgpt.com)
    
- **Refresh token** — даёт возможность получить новый access token без повторного логина; обычно хранится более защищённо и реже передаётся. Для межсервисного `client_credentials` refresh часто не нужен: сервис просто запрашивает новый токен.
    
- **Почему короткий TTL**: JWT сложно отозвать — лучше давать короткий срок и использовать refresh / introspection для контроля. [curity.io](https://curity.io/resources/learn/jwt-best-practices/?utm_source=chatgpt.com)[datatracker.ietf.org](https://datatracker.ietf.org/doc/html/rfc7662?utm_source=chatgpt.com)
    

---

# 9) Ревокация и introspection

- JWT по сути **самодостаточен** — при проверке по подписи нельзя узнать, отозван ли токен. Для поддержки немедленной ревокации ресурсы применяют:
    
    - короткий `exp` + refresh токены;
        
    - **token introspection** (RFC 7662) — resource server обращается к авторизационному серверу, чтобы узнать активность токена; это даёт централизацию ревокации. [datatracker.ietf.org](https://datatracker.ietf.org/doc/html/rfc7662?utm_source=chatgpt.com)[OAuth](https://oauth.net/2/token-introspection/?utm_source=chatgpt.com)
        

---

# 10) Практические угрозы и рекомендации (безопасность)

- **Не доверять полю `alg` в header** (атаки с `alg: none` и т.п.). Всегда ограничивайте допустимые алгоритмы. [owasp.org+1](https://owasp.org/www-project-web-security-testing-guide/latest/4-Web_Application_Security_Testing/06-Session_Management_Testing/10-Testing_JSON_Web_Tokens?utm_source=chatgpt.com)
    
- **Не хранить чувствительные данные в payload**, payload читаем без проверки подписи. [owasp.org](https://owasp.org/www-project-web-security-testing-guide/latest/4-Web_Application_Security_Testing/06-Session_Management_Testing/10-Testing_JSON_Web_Tokens?utm_source=chatgpt.com)
    
- **XSS/CSRF**: если храните JWT в `localStorage` — риск XSS; в куках — риск CSRF. Лучший вариант для веб-клиентов: _HttpOnly, Secure, SameSite_ куки + CSRF-защита, или хранение в памяти и короткий TTL. [owasp.org](https://owasp.org/www-chapter-vancouver/assets/presentations/2020-01_Attacking_and_Securing_JWT.pdf?utm_source=chatgpt.com)
    
- **Короткие TTL** для access token, проверка `aud`/`iss`, использование `jti` для обнаружения повторного использования и blacklist при необходимости. [curity.io](https://curity.io/resources/learn/jwt-best-practices/?utm_source=chatgpt.com)[owasp.org](https://owasp.org/www-chapter-vancouver/assets/presentations/2020-01_Attacking_and_Securing_JWT.pdf?utm_source=chatgpt.com)
    
- **Использовать проверенные библиотеки** и обновлять их (не писать парсеры/проверки вручную). OWASP рекомендует проверенные реализации. [OWASP Cheat Sheet Series](https://cheatsheetseries.owasp.org/cheatsheets/JSON_Web_Token_for_Java_Cheat_Sheet.html?utm_source=chatgpt.com)[owasp.org](https://owasp.org/www-project-web-security-testing-guide/latest/4-Web_Application_Security_Testing/06-Session_Management_Testing/10-Testing_JSON_Web_Tokens?utm_source=chatgpt.com)
    

---

# 11) JWT в контексте межсистемной авторизации (пример)

- **Service A** (client) выполняет `POST /token` на Keycloak с `grant_type=client_credentials`, передаёт `client_id`/`client_secret`. Получает `access_token` (JWT).
    
- Service A добавляет: `Authorization: Bearer <JWT>` → Service B.
    
- Service B валидирует подпись через JWKS, проверяет `iss`, `aud`, `exp`, роли/скоупы — и разрешает/отказывает. (Именно этот процесс мы применяли в проекте.) [JSON Web Tokens - jwt.io](https://jwt.io/introduction?utm_source=chatgpt.com)[stytch.com](https://stytch.com/blog/understanding-jwks/?utm_source=chatgpt.com)
    

---

# 12) Пример реального JWT (псевдо)

Header:

`{"alg":"RS256","typ":"JWT","kid":"key1"}`

Payload:

`{"iss":"https://auth.company","sub":"service-a","aud":"service-b","exp":1710000000,"iat":1709996400,"scope":"notify","jti":"uuid-1"}`

Signature: — подпись (base64url) по приватному ключу RS256.

---

# 13) Что говорить на собесе — короткая выжимка (2–3 предложения)

JWT — это подписанный JSON-токен (header.payload.signature), где подпись гарантирует целостность; claims (`iss`, `aud`, `exp`, `sub`) используются для валидации. Для проверки в распределённой системе берём публичный ключ из JWKS (по `kid`), проверяем подпись, `exp`, `iss` и `aud`. Ради безопасности даём короткий TTL, используем refresh/introspection и ключевую ротацию через JWKS. [datatracker.ietf.org](https://datatracker.ietf.org/doc/html/rfc7519?utm_source=chatgpt.com)[Auth0](https://auth0.com/docs/secure/tokens/json-web-tokens/json-web-key-sets?utm_source=chatgpt.com)[curity.io](https://curity.io/resources/learn/jwt-best-practices/?utm_source=chatgpt.com)

---