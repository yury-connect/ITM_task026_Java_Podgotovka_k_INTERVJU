### **Avro-схема и JSON Schema: что это и зачем нужно?**

Оба формата используются для **описания структуры данных** (например, JSON-сообщений, которые 1С отправляет в Kafka). Они помогают:  
✅ **Валидировать данные** (проверять, что все поля корректны).  
✅ **Гарантировать совместимость** между разными версиями системы.  
✅ **Уменьшать размер данных** (особенно Avro).

---
## **1. JSON Schema**

Это стандарт для описания структуры JSON-документов. Использует синтаксис JSON.

### **Пример схемы для платежа из 1С**
```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "title": "Платежное поручение 1С",
  "type": "object",
  "properties": {
    "event_id": {
      "type": "string",
      "format": "uuid",
      "description": "UUID события"
    },
    "event_type": {
      "type": "string",
      "enum": ["PAYMENT_CREATED", "PAYMENT_UPDATED", "PAYMENT_DELETED"]
    },
    "doc_id": {
      "type": "string",
      "pattern": "^[A-Z]{2}-\\d{6}$"
    },
    "amount": {
      "type": "number",
      "minimum": 0
    },
    "currency": {
      "type": "string",
      "enum": ["RUB", "USD", "EUR"]
    }
  },
  "required": ["event_id", "doc_id", "amount", "currency"]
}
```

### **Плюсы JSON Schema**
✔ Читаемый формат (человеко-понятный).   
✔ Поддержка во многих языках (JavaScript, Python, Java).   
✔ Гибкость (можно описывать сложные условия валидации).  

### **Минусы**
❌ Большой размер (избыточность).   
❌ Медленнее обработка, чем у Avro.  

---
## **2. Avro-схема**
Бинарный формат, созданный для Apache Kafka. Использует **схему + бинарные данные** (без повторения имен полей).

### **Пример Avro-схемы для платежа**
```json
{
  "type": "record",
  "name": "Payment",
  "namespace": "ru.1c.kafka",
  "fields": [
    {
      "name": "event_id",
      "type": "string",
      "doc": "UUID события"
    },
    {
      "name": "event_type",
      "type": {
        "type": "enum",
        "name": "EventType",
        "symbols": ["PAYMENT_CREATED", "PAYMENT_UPDATED", "PAYMENT_DELETED"]
      }
    },
    {
      "name": "doc_id",
      "type": "string"
    },
    {
      "name": "amount",
      "type": "double"
    },
    {
      "name": "currency",
      "type": {
        "type": "enum",
        "name": "Currency",
        "symbols": ["RUB", "USD", "EUR"]
      }
    }
  ]
}
```

### **Плюсы Avro**  
✔ **Компактный размер** (в Kafka хранится только бинарная версия + ссылка на схему).   
✔ **Быстрая сериализация/десериализация**.   
✔ **Встроенная поддержка в Kafka** (через **Schema Registry**).  

### **Минусы**  
❌ Сложнее отлаживать (бинарный формат).   
❌ Требует **Schema Registry** (доп. инфраструктура).  

---
## **Когда что использовать?**

|Критерий|JSON Schema|Avro|
|---|---|---|
|**Размер данных**|Большой (текст)|Маленький (бинарный)|
|**Скорость**|Медленнее|Быстрее|
|**Поддержка**|Любой HTTP/Kafka|Kafka, Hadoop|
|**Читаемость**|Человеко-понятно|Только через Schema Registry|

### **Рекомендации для 1С и Kafka**
1. **Если Kafka — основной брокер → Avro** (лучшая производительность).    
2. **Если REST/HTTP-интеграция → JSON Schema** (проще для отладки).    

---
## **Как применить в 1С?**

### **1. Для JSON Schema**

- Используйте **строку-шаблон** в 1С и проверяйте через **XDTO** или внешние библиотеки.    
- Пример:
```bsl
// Проверка JSON по схеме (через .NET-библиотеку)
Библиотека = Новый COMОбъект("Newtonsoft.Json.Schema");
Схема = Библиотека.JSchema.Parse(СхемаJSON);
Данные = Библиотека.JObject.Parse(JsonТекст);
Результат = Данные.IsValid(Схема);
```

### **2. Для Avro**
- Используйте **внешнюю компоненту** (например, **Apache Avro .NET**).    
- Или отправляйте данные в **Confluent Schema Registry** через REST.
```bsl
// Пример отправки Avro в Kafka через .NET-адаптер
Адаптер = Новый COMОбъект("Confluent.Kafka.Producer");
Настройки = Новый Соответствие();
Настройки["bootstrap.servers"] = "kafka:9092";
Настройки["schema.registry.url"] = "http://schema-registry:8081";
Производитель = Адаптер.ProducerBuilder(Настройки).Build();
Производитель.Produce("payments_topic", AvroДанные);
```

---
### **Вывод**
- **JSON Schema** — для простых интеграций (REST, HTTP).    
- **Avro** — для высоконагруженных Kafka-систем.    
- В 1С можно использовать оба варианта, но Avro потребует внешних компонент.

