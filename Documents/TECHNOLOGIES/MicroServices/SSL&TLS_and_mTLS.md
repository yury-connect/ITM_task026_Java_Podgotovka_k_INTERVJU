### **`SSL`/`TLS` и `mTLS`** — просто о сложном

#### 1. **SSL/TLS** (*обычное HTTPS-шифрование)*
**Что это?**
- Это протоколы, которые шифруют данные между клиентом (*например, браузером*) и сервером (*например, сайтом*).    
- **SSL** (устарел) → **TLS** (современная версия).

**Как работает?**
1. **Клиент** (браузер) подключается к **серверу** (сайту).    
2. **Сервер** отправляет свой **SSL-сертификат**, удостоверяет, что это настоящий сайт.    
3. **Браузер** проверяет сертификат 
	   (*например, выпущен ли он доверенным центром, как `Let's Encrypt`*).    
4. Если всё ок — устанавливается **зашифрованное соединение** (`HTTPS`).    

**Где используется?**
- Веб-сайты (`https://`), API, почта, мессенджеры.    

---
#### 2. **mTLS** (*`Mutual TLS` — двусторонняя проверка*)
**Что это?**
- Расширение TLS, где **не только сервер**, но и **клиент** должен подтвердить свою подлинность сертификатом.    

**Как работает?**
1. **Клиент** подключается к серверу.    
2. **Сервер** отправляет свой сертификат (*как в обычном `TLS`*).    
3. **Клиент** тоже отправляет **свой сертификат** (*доказывает, что он "свой"*).    
4. Сервер проверяет клиентский сертификат (*например, по списку доверенных*).    
5. Если оба сертификата валидны — устанавливается соединение.    

**Где используется?**
- **Критичные системы**: банки, госуслуги, IoT (умные устройства).    
- **Микросервисы**: когда сервисы внутри системы доверяют только "своим".    
- **VPN/корпоративные сети**: чтобы пускать только сотрудников с сертификатами.    

---
### **🔑 Ключевые отличия**

| **Параметр**     | **TLS**                   | **mTLS**                             |
| ---------------- | ------------------------- | ------------------------------------ |
| **Проверка**     | Только сервер             | Сервер + клиент                      |
| **Сертификаты**  | Нужен только серверу      | Нужны обоим сторонам                 |
| **Безопасность** | Защита от подмены сервера | Защита и от подмены клиента          |
| **Примеры**      | Сайты, публичные API      | Банки, военные системы, микросервисы |

---
### **🔧 Как настроить mTLS?** (мини-гайд)
1. **Серверу** нужен:    
    - SSL-сертификат (например, от Let's Encrypt).        
    - Настроенный веб-сервер (Nginx/Apache) с поддержкой `verify_client`.
    
2. **Клиенту** нужен:    
    - Свой сертификат (выпущенный вашим **внутренним УЦ**).        
    - Например, для API — добавляем сертификат в заголовки запроса.
    
3. **Проверка**:    
    - Сервер отклоняет запросы без валидного клиентского сертификата.

---
### **💡 Почему mTLS — это круто?**
- **Нет паролей**: нельзя взломать аккаунт через утечку.    
- **Защита от DDoS**: атакуют только те, у кого есть сертификат.    
- **Доверенные устройства**: например, только корпоративные ноутбуки могут подключиться к VPN.    

---
### **🚨 Ошибки при использовании**
- **Потеря сертификатов** = отказ в доступе.    
- **Слабая криптография** (например, SHA-1) = уязвимость.    
- **Нет механизма отзыва** (CRL/OCSP) — если сертификат украли, его нельзя заблокировать.

---
### **📌 Пример из вашего проекта**
Если у вас **микросервисы**, mTLS можно использовать так:
1. **Сервис А** хочет запросить данные у **Сервиса Б**.    
2. **Сервис Б** проверяет сертификат **Сервиса А**.    
3. Если сертификат есть в доверенных — отвечает.    
4. Если нет — возвращает `403 Forbidden`.    

Так вы защищаетесь от **несанкционированных вызовов API** (например, если хакер пробрался в сеть).

---
### **Как объяснить на собеседовании?**

> _"TLS — это когда браузер проверяет сервер (как на HTTPS-сайтах). А mTLS — когда и сервер проверяет клиента (например, API требует сертификат). Мы использовали **mTLS для внутренних микросервисов**, чтобы только доверенные сервисы могли общаться между собой."_

Это покажет, что вы понимаете **кибербезопасность** и **современные практики**. 🔒


