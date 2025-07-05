### Простая инструкция по подключению `MinIO` к `Java`-проекту:

### 1. Установите MinIO (если ещё не установлен)
```bash
docker run -p 9000:9000 -p 9001:9001 minio/minio server /data --console-address ":9001"
```

**Данные для доступа** (появятся в консоли при запуске):
- Endpoint: `http://localhost:9000`    
- Access Key: `ваш_ключ` (например `admin`)    
- Secret Key: `ваш_секрет` (например `1234`)    

---
### 2. Добавьте зависимость в проект

**Для Maven** (`pom.xml`):
```xml
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.5.7</version> <!-- Актуальная версия на 2025 -->
</dependency>
```

**Для Gradle** (`build.gradle`):
```groovy
implementation 'io.minio:minio:8.5.7'
```

---
### 3. Базовый пример работы (Java 17+)
```java
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;

public class MinioExample {
    public static void main(String[] args) throws Exception {
        // 1. Создаём клиент
        MinioClient minio = MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("minioadmin", "minioadmin")
                .build();

        // 2. Проверяем/создаём бакет
        String bucketName = "my-java-bucket";
        boolean exists = minio.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists) {
            minio.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            System.out.println("Бакет создан!");
        }

        // 3. Загружаем файл
        minio.uploadObject(
            UploadObjectArgs.builder()
                .bucket(bucketName)
                .object("photo.jpg") // имя в MinIO
                .filename("C:/Users/User/Desktop/real-photo.jpg") // локальный путь
                .build()
        );
        
        System.out.println("Файл успешно загружен!");
    }
}
```

---
### 4. Проверка

1. Запустите код    
2. Откройте веб-интерфейс MinIO:  
    `http://localhost:9001` → логин/пароль (указаны при запуске Docker)    
3. Увидите файл `photo.jpg` в бакете `my-java-bucket

---
### Основные операции
```java
// Скачать файл
minio.downloadObject(
    DownloadObjectArgs.builder()
        .bucket(bucketName)
        .object("photo.jpg")
        .filename("C:/Downloads/new-photo.jpg")
        .build()
);

// Удалить файл
minio.removeObject(
    RemoveObjectArgs.builder()
        .bucket(bucketName)
        .object("photo.jpg")
        .build()
);
```

---
### Важные нюансы

1. **HTTPS**: В продакшене используйте `.endpoint("https://...")` и настройте SSL    
2. **Исключения**: Все операции бросают `Exception` — обрабатывайте их!    
3. **Права доступа**: Настройте политики бакета через веб-консоль    
4. **Альтернативные клиенты**:    
    - AWS SDK v2 (для S3-совместимых хранилищ)        
    - Spring Content (если используете Spring Boot)        

---

**Готово!** Теперь ваш Java-проект умеет работать с MinIO. Для Spring Boot-проекта могу показать интеграцию через свойства — просто скажите! 😊

