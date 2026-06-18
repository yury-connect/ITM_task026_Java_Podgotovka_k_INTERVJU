Как лучше передавать большой файл по gRPC/REST

### 🎯 Короткий ответ

| Сценарий               | gRPC                                                    | REST                                 |
| ---------------------- | ------------------------------------------------------- | ------------------------------------ |
| **Файлы < 10 MB**      | Обычный unary вызов                                     | Multipart/form-data                  |
| **Файлы 10 MB – 1 GB** | **Client streaming** (чанками)                          | **TUS Protocol** или chunked upload  |
| **Файлы > 1 GB**       | gRPC + **разделение на чанки** + контроль возобновления | Использовать AWS S3 / MinIO (ссылка) |

### 🟢 gRPC — Client Streaming (*чанками*)
```protobuf
service FileService {
    rpc UploadFile(stream FileChunk) returns (UploadResponse);
}

message FileChunk {
    string file_id = 1;
    bytes data = 2;      // 1-4 MB на чанк
    int32 sequence = 3;
}
```

```java

// Клиент отправляет потоком
public void uploadFile(String path) {
    Iterator<FileChunk> chunks = readFileInChunks(path);
    stub.uploadFile(chunks);
}
```

### 🟡 REST — Multipart + Chunked Transfer
```http
POST /upload
Content-Type: multipart/form-data
Transfer-Encoding: chunked
--boundary
Content-Disposition: form-data; name="file"; filename="large.bin"
(данные потоками)
```

### 🔴 TUS Protocol (*для возобновляемой загрузки*)
```http
PATCH /files/{id}           # запись чанка с определённого offset
HEAD  /files/{id}           # узнать прогресс
```

**Лучшая практика для больших файлов (>100 MB):**
```text
Клиент → Получает подписанную ссылку → Загружает напрямую в S3/MinIO
                ↑                              ↓
          Ваш бэкенд                    S3/MinIO
```

---
