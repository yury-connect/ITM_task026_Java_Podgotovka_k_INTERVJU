---
tags:
  - TECHNOLOGIES/DataBase/S3/MinIO
---
# Пример использования MinIO S3 в Java
Вот подробный пример работы с MinIO (S3-совместимое хранилище) на Java с использованием официального MinIO Java SDK.

## Настройка проекта
Добавьте зависимость в ваш `pom.xml` (Maven):
```xml
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.5.7</version>
</dependency>
```

Или в `build.gradle` (Gradle):

```groovy
implementation 'io.minio:minio:8.5.7'
```
## Полный пример кода с объяснениями
```java
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.DownloadObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.errors.MinioException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinioExample {

    public static void main(String[] args) {
        // 1. Конфигурация подключения к MinIO
        String endpoint = "http://localhost:9000"; // Адрес MinIO сервера
        String accessKey = "minioadmin";         // Access key (по умолчанию minioadmin)
        String secretKey = "minioadmin";         // Secret key (по умолчанию minioadmin)
        String bucketName = "test-bucket";       // Имя бакета
        String objectName = "example.txt";        // Имя объекта в хранилище
        String filePath = "./example.txt";       // Локальный путь к файлу для загрузки
        String downloadPath = "./downloaded.txt"; // Путь для скачивания файла
			
        try {
            // 2. Создание клиента MinIO
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
					
            System.out.println("MinIO клиент успешно создан");
					
            // 3. Проверка существования бакета и создание при необходимости
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
				
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
                System.out.println("Бакет '" + bucketName + "' успешно создан");
            } else {
                System.out.println("Бакет '" + bucketName + "' уже существует");
            }
				
            // 4. Создание тестового файла (в реальном приложении файл уже должен существовать)
            // Здесь просто для демонстрации
            java.nio.file.Files.write(java.nio.file.Path.of(filePath), 
                    "Это тестовое содержимое файла для MinIO".getBytes());
					
            // 5. Загрузка файла в бакет
            minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .filename(filePath)
                    .build());
            System.out.println("Файл '" + filePath + "' успешно загружен в бакет '" + 
                    bucketName + "' под именем '" + objectName + "'");
					
            // 6. Скачивание файла из бакета
            minioClient.downloadObject(DownloadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .filename(downloadPath)
                    .build());
            System.out.println("Файл '" + objectName + "' успешно скачан в '" + 
                    downloadPath + "'");
					
            // 7. Чтение скачанного файла (демонстрация)
            String content = new String(java.nio.file.Files.readAllBytes(
                    java.nio.file.Path.of(downloadPath)));
            System.out.println("Содержимое скачанного файла:\n" + content);
					
            // 8. Удаление объекта из бакета
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            System.out.println("Файл '" + objectName + "' удален из бакета");
					
            // 9. Удаление бакета (бакет должен быть пустым)
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
            System.out.println("Бакет '" + bucketName + "' удален");
					
        } catch (MinioException e) {
            System.err.println("Ошибка MinIO: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            System.err.println("Ошибка аутентификации: " + e.getMessage());
        }
    }
}
```

## Подробное объяснение
1. **Конфигурация подключения**:    
    - `endpoint` - URL вашего MinIO сервера        
    - `accessKey` и `secretKey` - учетные данные для аутентификации        
    - Остальные параметры определяют имена бакета, объекта и пути к файлам
        
2. **Создание клиента MinIO**:    
    - Используем builder-паттерн для создания клиента        
    - Указываем endpoint и учетные данные
        
3. **Работа с бакетами**:    
    - Проверяем существование бакета с помощью `bucketExists()`        
    - Создаем бакет с помощью `makeBucket()`, если он не существует
        
4. **Загрузка файла**:    
    - Создаем тестовый файл (в реальном приложении файл уже должен существовать)      
    - Загружаем файл в бакет с помощью `uploadObject()`
        
5. **Скачивание файла**:    
    - Скачиваем файл из бакета с помощью `downloadObject()`        
    - Читаем содержимое скачанного файла для демонстрации
        
6. **Очистка**:    
    - Удаляем объект из бакета с помощью `removeObject()`        
    - Удаляем сам бакет с помощью `removeBucket()`
        
7. **Обработка ошибок**:    
    - Обрабатываем специфические исключения MinIO        
    - Обрабатываем стандартные исключения Java        

## Дополнительные рекомендации
1. **Для production-кода**:    
    - Не используйте хардкоденные учетные данные        
    - Храните конфигурацию в файлах конфигурации или переменных окружения        
    - Добавьте более детализированную обработку ошибок
        
2. **Альтернативные операции**:
```java
// Получение списка бакетов
List<Bucket> bucketList = minioClient.listBuckets();

// Получение списка объектов в бакете
Iterable<Result<Item>> objects = minioClient.listObjects(
    ListObjectsArgs.builder().bucket(bucketName).build());

// Получение метаданных объекта
StatObjectResponse stat = minioClient.statObject(
    StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
```

3. **Настройка политик доступа**:
```java
// Установка политики доступа к бакету
minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
    .bucket(bucketName)
    .config(policyJson)
    .build());
```

Этот пример демонстрирует основные операции с MinIO в Java. Для реального приложения вам может потребоваться добавить больше проверок и обработку специфических сценариев.


---
[Ссылка](https://github.com/yury-connect/ITM_task028_examples_MinIO) на мой пример реализации проекта с использованием MinIO 
(объектное хранилище, полностью совместимое с Amazon S3 API)

