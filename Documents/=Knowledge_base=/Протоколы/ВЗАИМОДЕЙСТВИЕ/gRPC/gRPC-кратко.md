Мой разбор/ конспект по статье [**ссылка**](gRPC_&_REST)
Понравившееся видео: [Что такое gRPC и Protobuf?](https://youtu.be/_EqVG-El5z0?si=iAzDjfU37_uY-JI8)

**gRPC (gRPC Remote Procedure Call)** — высокопроизводительный RPC-фреймворк от Google, работающий поверх HTTP/2.

### 📊 Сравнение с REST

| Характеристика         | gRPC                                 | REST (HTTP/1.1)                 |
| ---------------------- | ------------------------------------ | ------------------------------- |
| **Транспорт**          | HTTP/2 (*мультиплексирование*)       | HTTP/1.1 или HTTP/2             |
| **Формат данных**      | Protobuf (*бинарный*)                | JSON/XML (текстовый)            |
| **Типизация**          | Строгая (`.proto` *файлы*)           | Слабая (*OpenAPI опционально*)  |
| **Кодогенерация**      | ✅ Да (*клиент+сервер*)               | 🟡 Частично (*OpenAPI→код*)     |
| **Стриминг**           | ✅ (*client, server, bi-directional*) | ❌ (*только Server-Sent Events*) |
| **Производительность** | 🔥 Очень высокая                     | 🐢 Медленнее                    |
| **Человекочитаемость** | ❌ Нет (*бинарный*)                   | ✅ Да (JSON)                     |

### 🔄 Типы стримов в `gRPC`
```text
1. Unary             → клиент ── запрос ──→ сервер
                        клиент ←─ ответ ─── сервер
                        
2. Server Streaming  → клиент ── запрос ──→ сервер
                        клиент ←─ поток ─── сервер
                        
3. Client Streaming  → клиент ── поток ──→ сервер
                        клиент ←─ ответ ─── сервер
                        
4. Bidirectional    → клиент ⇄⇄⇄ поток ⇄⇄⇄ сервер
```

### 📝 Пример `.proto` файла
```protobuf
syntax = "proto3";

service UserService {
    rpc GetUser (GetUserRequest) returns (User);           // Unary
    rpc GetUsers (Empty) returns (stream User);            // Server streaming
    rpc UploadAvatar (stream Chunk) returns (UploadStatus); // Client streaming
}

message GetUserRequest {
    int64 id = 1;
}

message User {
    int64 id = 1;
    string name = 2;
    string email = 3;
}
```

По умолчанию максимальный размер сообщения в **gRPC** ограничен **4 MB**. Если передавать файл unary-запросом, приложение упадет с ошибкой `ResourceExhaustedException`. При передаче через *Client Streaming* чанками gRPC активно грузит эти чанки в оперативную память для сериализации/десериализации *Protobuf*, что создает **overhead на CPU и RAM**. 
REST с `Transfer-Encoding: chunked` в этом сценарии работает «потоково» на уровне байт-стрима ввода-вывода (I/O) и часто оказывается менее требовательным к ресурсам бэкенда.

---
