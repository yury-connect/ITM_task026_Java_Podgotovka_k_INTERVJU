# Принципы_взаимодействия_через_SOAP

Давайте разберем принципы SOAP (Simple Object Access Protocol) с примерами на Java.

## Что такое SOAP?

SOAP — это протокол для обмена структурированной информацией в распределенных системах. В отличие от REST, который является архитектурным стилем, SOAP — это строгий стандартный протокол.

### Ключевые характеристики SOAP:

- **XML-основа**: Все сообщения в формате XML
    
- **Стандартизированный**: Строгая спецификация WSDL
    
- **Независимость от транспорта**: Может работать поверх HTTP, SMTP, TCP и др.
    
- **Встроенная безопасность**: WS-Security, цифровые подписи
    
- **Транзакции**: Поддержка ACID-транзакций

---
## Архитектура SOAP сообщения

### Структура SOAP-конверта:
```xml
<?xml version="1.0"?>
<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope">
  <soap:Header>
    <!-- Опциональные заголовки (безопасность, транзакции) -->
    <wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">
      <!-- Данные аутентификации -->
    </wsse:Security>
  </soap:Header>
  <soap:Body>
    <!-- Основные данные запроса -->
    <m:GetUserRequest xmlns:m="http://example.com/webservice">
      <m:UserId>123</m:UserId>
    </m:GetUserRequest>
  </soap:Body>
</soap:Envelope>
```
  

---
## WSDL (Web Services Description Language)

WSDL — это контракт сервиса, который описывает:
- Доступные операции    
- Формат сообщений    
- Протокол передачи    
- Адрес endpoint    

### Пример WSDL фрагмента:
```xml
<definitions name="UserService"
    targetNamespace="http://example.com/wsdl"
    xmlns="http://schemas.xmlsoap.org/wsdl/">
    
    <message name="GetUserRequest">
        <part name="userId" type="xsd:int"/>
    </message>
    
    <portType name="UserServicePort">
        <operation name="getUser">
            <input message="tns:GetUserRequest"/>
            <output message="tns:GetUserResponse"/>
        </operation>
    </portType>
</definitions>
```


---
## Реализация SOAP сервиса на Java

### 1. Интерфейс сервиса (SEI - Service Endpoint Interface)
```java
package com.example.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

@WebService(name = "UserService", targetNamespace = "http://example.com/webservice")
public interface UserService {
    
    @WebMethod
    @WebResult(name = "user")
    User getUser(@WebParam(name = "userId") int userId);
    
    @WebMethod
    @WebResult(name = "success")
    boolean createUser(@WebParam(name = "user") User user);
    
    @WebMethod
    @WebResult(name = "users")
    List<User> getUsersByDepartment(@WebParam(name = "departmentId") int departmentId);
}
```

### 2. Реализация сервиса
```java
package com.example.service.impl;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.List;

@WebService(
    serviceName = "UserService",
    portName = "UserServicePort",
    targetNamespace = "http://example.com/webservice",
    endpointInterface = "com.example.service.UserService"
)
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class UserServiceImpl implements UserService {
    
    private Map<Integer, User> userDatabase = new HashMap<>();
    
    @Override
    public User getUser(int userId) {
        User user = userDatabase.get(userId);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        return user;
    }
    
    @Override
    public boolean createUser(User user) {
        if (user == null || user.getId() <= 0) {
            throw new IllegalArgumentException("Invalid user data");
        }
        userDatabase.put(user.getId(), user);
        return true;
    }
    
    @Override
    public List<User> getUsersByDepartment(int departmentId) {
        return userDatabase.values().stream()
            .filter(user -> user.getDepartmentId() == departmentId)
            .collect(Collectors.toList());
    }
}
```

### 3. Data классы (JAXB-аннотированные)
```java
package com.example.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "user", namespace = "http://example.com/webservice")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "name", "email", "departmentId"})
public class User {
    
    @XmlElement(required = true)
    private int id;
    
    @XmlElement(required = true)
    private String name;
    
    @XmlElement(required = true)
    private String email;
    
    @XmlElement(name = "department_id")
    private int departmentId;
    
    // Конструкторы
    public User() {}
    
    public User(int id, String name, String email, int departmentId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.departmentId = departmentId;
    }
    
    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
}
```

### 4. Пользовательское исключение
```java
package com.example.exception;

import javax.xml.ws.WebFault;

@WebFault(
    name = "UserNotFoundFault",
    targetNamespace = "http://example.com/webservice"
)
public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String message) {
        super(message);
    }
}
```

### 5. Публикация сервиса
```java
package com.example.publisher;

import javax.xml.ws.Endpoint;
import com.example.service.impl.UserServiceImpl;

public class ServicePublisher {
    
    public static void main(String[] args) {
        String address = "http://localhost:8080/userservice";
        Endpoint endpoint = Endpoint.publish(address, new UserServiceImpl());
        
        System.out.println("SOAP Service published at: " + address);
        System.out.println("WSDL available at: " + address + "?wsdl");
        
        // Сервис будет работать до нажатия Enter
        System.out.println("Press Enter to stop the service...");
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        endpoint.stop();
    }
}
```


---
## SOAP клиент на Java

### 1. Генерация классов из WSDL
```bash
# Используем wsimport (входит в JDK)
wsimport -keep -p com.example.client http://localhost:8080/userservice?wsdl
```

### 2. Java клиент
```java
package com.example.client;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

public class UserServiceClient {
    
    public static void main(String[] args) throws Exception {
        URL wsdlUrl = new URL("http://localhost:8080/userservice?wsdl");
        
        QName serviceName = new QName("http://example.com/webservice", "UserService");
        QName portName = new QName("http://example.com/webservice", "UserServicePort");
        
        Service service = Service.create(wsdlUrl, serviceName);
        UserService userService = service.getPort(portName, UserService.class);
        
        try {
            // Создание пользователя
            User newUser = new User(1, "Иван Иванов", "ivan@example.com", 101);
            boolean created = userService.createUser(newUser);
            System.out.println("User created: " + created);
            
            // Получение пользователя
            User user = userService.getUser(1);
            System.out.println("User found: " + user.getName() + ", " + user.getEmail());
            
            // Получение пользователей по отделу
            List<User> departmentUsers = userService.getUsersByDepartment(101);
            System.out.println("Users in department: " + departmentUsers.size());
            
        } catch (Exception e) {
            System.err.println("SOAP Error: " + e.getMessage());
        }
    }
}
```


---
## Обработка SOAP-ошибок
```java
package com.example.client;

import javax.xml.ws.soap.SOAPFaultException;

public class ErrorHandlingExample {
    
    public void demonstrateErrorHandling(UserService userService) {
        try {
            // Попытка получить несуществующего пользователя
            User user = userService.getUser(999);
        } catch (SOAPFaultException e) {
            System.err.println("SOAP Fault occurred:");
            System.err.println("Fault Code: " + e.getFault().getFaultCode());
            System.err.println("Fault String: " + e.getFault().getFaultString());
            // Дополнительная обработка для разных типов ошибок
        }
    }
}
```
  
---
## Spring Boot + SOAP

### Конфигурация Spring WS
```java
@Configuration
@EnableWs
public class WebServiceConfig extends WsConfigurerAdapter {
    
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }
    
    @Bean(name = "users")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema userSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("UserServicePort");
        definition.setLocationUri("/ws");
        definition.setTargetNamespace("http://example.com/webservice");
        definition.setSchema(userSchema);
        return definition;
    }
    
    @Bean
    public XsdSchema userSchema() {
        return new SimpleXsdSchema(new ClassPathResource("user.xsd"));
    }
}
```
  

---

## Сравнение SOAP и REST для Java-разработчика

|Аспект|SOAP|REST|
|---|---|---|
|**Подход**|Протокол|Архитектурный стиль|
|**Формат данных**|Только XML|JSON, XML, текст и др.|
|**Стандарты**|WSDL, WS-*|HTTP-методы, статусы|
|**Безопасность**|WS-Security|HTTPS, OAuth, JWT|
|**Транзакции**|WS-Transactions|Нет встроенной поддержки|
|**Производительность**|Медленнее из-за XML|Быстрее|
|**Сложность**|Выше|Ниже|

### Когда использовать SOAP в Java-проектах:
1. **Корпоративные системы** с требованием ACID-транзакций    
2. **Высокие требования к безопасности** (WS-Security)    
3. **Сложные бизнес-процессы** с компенсирующими транзакциями    
4. **Интеграция с legacy-системами**    
5. **Когда нужна строгая типизация** через WSDL/XSD    

SOAP остается мощным инструментом для enterprise-приложений, где критичны безопасность, надежность и стандартизация.

---
