### **[[Documentes/JAVA-обучение/Java_СПРАВОЧНИК/ЛикБез/SOAP.md.md|SOAP]]-интеграции: что это и как работает?**

**[[Documentes/JAVA-обучение/Java_СПРАВОЧНИК/ЛикБез/SOAP.md.md|SOAP]]** (Simple Object Access Protocol) — это протокол для обмена структурированными сообщениями в распределённых системах. Чаще всего используется для интеграции между корпоративными системами (1С, SAP, банковскими сервисами, гос.платформами).

---
## 🔹 **Основные особенности SOAP**

1. **XML-формат**    
    - Все запросы и ответы передаются в виде XML-документов.        
    - Пример тела SOAP-запроса:
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
    <soap:Body>
        <getUserRequest xmlns="http://example.com/api">
            <id>123</id>
        </getUserRequest>
    </soap:Body>
</soap:Envelope>
```
                
2. **WSDL-контракт**    
    - Сервис описывается в WSDL-файле (Web Services Description Language), который содержит:        
        - Доступные методы (operations).            
        - Формат запросов/ответов (XSD-схемы).            
        - URL сервиса (endpoint).
            
3. **Транспортные протоколы**    
    - Работает поверх **HTTP/HTTPS**, SMTP, JMS.        
    - В отличие от REST, SOAP не привязан к HTTP — можно использовать и другие протоколы.
        
4. **Встроенная безопасность**    
    - Поддержка WS-Security (шифрование, цифровые подписи).        
    - Часто используется в банковских и государственных системах.        

---
## 🔹 **Где применяется SOAP?**
1. **Корпоративные системы**    
    - Интеграция с SAP, 1С, Oracle E-Business Suite.
        
2. **Финансовые сервисы**    
    - Платежные шлюзы (например, WebMoney, банковские API).
        
3. **Государственные платформы**    
    - Налоговые службы, электронные госуслуги.
        
4. **Устаревшие системы**    
    - Где важна стандартизация и безопасность, а не простота.
        

---
## 🔹 **Плюсы SOAP**

✅ **Стандартизация** — строгие контракты (WSDL).  
✅ **Безопасность** — встроенные механизмы (WS-Security).  
✅ **Надёжность** — поддержка транзакций и ACID в некоторых реализациях.  
✅ **Независимость от транспорта** — можно использовать не только HTTP.

---
## 🔹 **Минусы SOAP**

❌ **Сложность** — объёмные XML-документы, ручное описание схем.  
❌ **Медленнее REST** — из-за XML и более тяжёлых запросов.  
❌ **Меньшая гибкость** — сложно вносить изменения без обновления WSDL.

---
## 🔹 **Пример интеграции**
Допустим, нужно получить данные о пользователе через SOAP-сервис.

### 1. **Изучаем WSDL**
Файл `user-service.wsdl` описывает метод `getUserById`.

### 2. **Формируем SOAP-запрос**
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
    <soap:Body>
        <getUserById xmlns="http://example.com/ws">
            <id>123</id>
        </getUserById>
    </soap:Body>
</soap:Envelope>
```

### 3. **Отправляем запрос (на примере Java + JAX-WS)**
```java
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

public class SoapClient {
    public static void main(String[] args) throws Exception {
        URL wsdlUrl = new URL("http://example.com/user-service?wsdl");
        QName serviceName = new QName("http://example.com/ws", "UserService");
        
        Service service = Service.create(wsdlUrl, serviceName);
        UserService port = service.getPort(UserService.class);
        
        User user = port.getUserById(123);
        System.out.println(user.getName());
    }
}
```

---
## 🔹 **SOAP vs REST**

|Характеристика|SOAP|REST|
|---|---|---|
|**Формат данных**|XML|JSON/XML/другие|
|**Протокол**|HTTP, SMTP, JMS|Только HTTP/HTTPS|
|**Контракты**|WSDL (строгий)|OpenAPI (необязательный)|
|**Безопасность**|WS-Security|OAuth2, JWT|
|**Гибкость**|Низкая|Высокая|
|**Скорость**|Медленнее|Быстрее|

---
## 🔹 **Вывод**
SOAP — это «тяжёлый», но стандартизированный протокол для интеграции между корпоративными системами. В 2020-х его чаще заменяют на REST/gRPC, но в банковской и государственной сферах он всё ещё актуален.

**Когда выбирать SOAP?**
- Если система требует строгих контрактов (WSDL).
    
- Если нужна встроенная безопасность (WS-Security).
    
- Если работаете с устаревшими ERP (SAP, 1С).
    

Для современных микросервисов чаще используют **REST API** или **gRPC**.

