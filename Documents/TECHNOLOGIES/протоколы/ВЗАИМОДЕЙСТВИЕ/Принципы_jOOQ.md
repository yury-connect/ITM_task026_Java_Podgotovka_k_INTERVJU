# Принципы взаимодействия через jOOQ

Давайте разберем принципы взаимодействия с базой данных через jOOQ (Java Object Oriented Querying) с примерами на Java.

## Что такое jOOQ?

jOOQ — это библиотека для типобезопасного построения SQL-запросов в Java. Генерирует Java-классы на основе схемы БД.

---
## Настройка зависимостей Maven
```xml
<properties>
    <jooq.version>3.18.7</jooq.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.jooq</groupId>
        <artifactId>jooq</artifactId>
        <version>${jooq.version}</version>
    </dependency>
    <dependency>
        <groupId>org.jooq</groupId>
        <artifactId>jooq-meta</artifactId>
        <version>${jooq.version}</version>
    </dependency>
    <dependency>
        <groupId>org.jooq</groupId>
        <artifactId>jooq-codegen</artifactId>
        <version>${jooq.version}</version>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <version>2.2.224</version>
    </dependency>
</dependencies>
```

## Генерация кода из схемы БД

### 1. Конфигурация генератора в Maven
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.jooq</groupId>
            <artifactId>jooq-codegen-maven</artifactId>
            <version>${jooq.version}</version>
            <executions>
                <execution>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <jdbc>
                    <driver>org.h2.Driver</driver>
                    <url>jdbc:h2:~/testdb</url>
                    <user>sa</user>
                    <password></password>
                </jdbc>
                <generator>
                    <database>
                        <name>org.jooq.meta.h2.H2Database</name>
                        <includes>.*</includes>
                        <excludes></excludes>
                        <inputSchema>PUBLIC</inputSchema>
                    </database>
                    <generate>
                        <daos>true</daos>
                        <pojos>true</pojos>
                        <javaTimeTypes>true</javaTimeTypes>
                    </generate>
                    <target>
                        <packageName>com.example.db</packageName>
                        <directory>src/main/java</directory>
                    </target>
                </generator>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### 2. SQL схема для примера
```sql
-- Создаем таблицы в H2 базе
CREATE TABLE author (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    birth_date DATE
);

CREATE TABLE book (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    author_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    published_in INTEGER,
    language VARCHAR(2),
    FOREIGN KEY (author_id) REFERENCES author(id)
);

CREATE TABLE book_store (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE book_to_book_store (
    book_store_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    stock INTEGER,
    PRIMARY KEY (book_store_id, book_id),
    FOREIGN KEY (book_store_id) REFERENCES book_store(id),
    FOREIGN KEY (book_id) REFERENCES book(id)
);
```

---
## Базовые операции с jOOQ

### 1. Настройка подключения
```java
package com.example.jooq;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {
    
    public static DSLContext createDslContext() throws Exception {
        Connection connection = DriverManager.getConnection(
            "jdbc:h2:~/testdb", "sa", "");
        return DSL.using(connection, SQLDialect.H2);
    }
    
    // Для Spring Boot
    @Bean
    public DSLContext dslContext(DataSource dataSource) {
        return DSL.using(dataSource, SQLDialect.H2);
    }
}
```

### 2. SELECT операции
```java
package com.example.jooq;

import static com.example.db.Tables.*;
import com.example.db.tables.records.AuthorRecord;
import com.example.db.tables.pojos.Author;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.util.List;

public class SelectExamples {
    
    private final DSLContext dsl;
    
    public SelectExamples(DSLContext dsl) {
        this.dsl = dsl;
    }
    
    // Простой SELECT всех записей
    public void selectAllAuthors() {
        List<AuthorRecord> authors = dsl.selectFrom(AUTHOR)
                .fetch();
        
        authors.forEach(author -> 
            System.out.println(author.getFirstName() + " " + author.getLastName()));
    }
    
    // SELECT с фильтрацией
    public List<AuthorRecord> findAuthorsByLastName(String lastName) {
        return dsl.selectFrom(AUTHOR)
                .where(AUTHOR.LAST_NAME.eq(lastName))
                .fetch();
    }
    
    // SELECT конкретных колонок
    public void selectSpecificColumns() {
        List<String> fullNames = dsl.select(
                    AUTHOR.FIRST_NAME.concat(" ").concat(AUTHOR.LAST_NAME).as("full_name"))
                .from(AUTHOR)
                .fetch()
                .getValues("full_name", String.class);
        
        fullNames.forEach(System.out::println);
    }
    
    // JOIN запрос
    public void findBooksWithAuthors() {
        var result = dsl.select(
                    BOOK.TITLE,
                    AUTHOR.FIRST_NAME,
                    AUTHOR.LAST_NAME)
                .from(BOOK)
                .join(AUTHOR).on(BOOK.AUTHOR_ID.eq(AUTHOR.ID))
                .where(BOOK.PUBLISHED_IN.gt(2000))
                .fetch();
        
        result.forEach(record -> 
            System.out.println(record.get(BOOK.TITLE) + " by " + 
                             record.get(AUTHOR.FIRST_NAME) + " " + 
                             record.get(AUTHOR.LAST_NAME)));
    }
    
    // Агрегатные функции
    public void countBooksPerAuthor() {
        var result = dsl.select(
                    AUTHOR.FIRST_NAME,
                    AUTHOR.LAST_NAME,
                    DSL.count().as("book_count"))
                .from(AUTHOR)
                .join(BOOK).on(AUTHOR.ID.eq(BOOK.AUTHOR_ID))
                .groupBy(AUTHOR.ID, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
                .having(DSL.count().gt(1))
                .fetch();
        
        result.forEach(record -> 
            System.out.println(record.get(AUTHOR.LAST_NAME) + 
                             ": " + record.get("book_count", Integer.class)));
    }
    
    // Сортировка и лимит
    public List<AuthorRecord> findLatestAuthors(int limit) {
        return dsl.selectFrom(AUTHOR)
                .orderBy(AUTHOR.BIRTH_DATE.desc())
                .limit(limit)
                .fetch();
    }
}
```

### 3. INSERT операции
```java
package com.example.jooq;

import static com.example.db.Tables.AUTHOR;
import com.example.db.tables.records.AuthorRecord;
import org.jooq.DSLContext;

import java.time.LocalDate;

public class InsertExamples {
    
    private final DSLContext dsl;
    
    public InsertExamples(DSLContext dsl) {
        this.dsl = dsl;
    }
    
    // INSERT с использованием Record
    public Long insertAuthorWithRecord(String firstName, String lastName, LocalDate birthDate) {
        AuthorRecord author = dsl.newRecord(AUTHOR);
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setBirthDate(birthDate);
        author.store(); // Выполняет INSERT
        
        return author.getId(); // Возвращает сгенерированный ID
    }
    
    // INSERT с использованием DSL
    public void insertAuthorWithDsl(String firstName, String lastName) {
        dsl.insertInto(AUTHOR)
           .set(AUTHOR.FIRST_NAME, firstName)
           .set(AUTHOR.LAST_NAME, lastName)
           .execute();
    }
    
    // INSERT с возвращением сгенерированного ID
    public Long insertAuthorReturningId(String firstName, String lastName) {
        return dsl.insertInto(AUTHOR)
                .set(AUTHOR.FIRST_NAME, firstName)
                .set(AUTHOR.LAST_NAME, lastName)
                .returning(AUTHOR.ID)
                .fetchOne()
                .getId();
    }
    
    // Пакетный INSERT
    public void batchInsertAuthors(List<Author> authors) {
        var batch = dsl.batchInsert(authors.stream()
                .map(author -> {
                    AuthorRecord record = dsl.newRecord(AUTHOR);
                    record.from(author);
                    return record;
                })
                .toList());
        
        batch.execute();
    }
}
```

### 4. UPDATE операции
```java
package com.example.jooq;

import static com.example.db.Tables.AUTHOR;
import org.jooq.DSLContext;

public class UpdateExamples {
    
    private final DSLContext dsl;
    
    public UpdateExamples(DSLContext dsl) {
        this.dsl = dsl;
    }
    
    // Простой UPDATE
    public int updateAuthorLastName(Long authorId, String newLastName) {
        return dsl.update(AUTHOR)
                .set(AUTHOR.LAST_NAME, newLastName)
                .where(AUTHOR.ID.eq(authorId))
                .execute();
    }
    
    // UPDATE с использованием Record
    public void updateAuthorWithRecord(Long authorId, String firstName, String lastName) {
        AuthorRecord author = dsl.fetchOne(AUTHOR, AUTHOR.ID.eq(authorId));
        if (author != null) {
            author.setFirstName(firstName);
            author.setLastName(lastName);
            author.update(); // Выполняет UPDATE
        }
    }
    
    // UPDATE нескольких записей
    public int capitalizeAllLastNames() {
        return dsl.update(AUTHOR)
                .set(AUTHOR.LAST_NAME, 
                     DSL.upper(AUTHOR.LAST_NAME))
                .execute();
    }
}
```

### 5. DELETE операции
```java
package com.example.jooq;

import static com.example.db.Tables.AUTHOR;
import static com.example.db.Tables.BOOK;
import org.jooq.DSLContext;

public class DeleteExamples {
    
    private final DSLContext dsl;
    
    public DeleteExamples(DSLContext dsl) {
        this.dsl = dsl;
    }
    
    // Простой DELETE
    public int deleteAuthor(Long authorId) {
        return dsl.deleteFrom(AUTHOR)
                .where(AUTHOR.ID.eq(authorId))
                .execute();
    }
    
    // DELETE с использованием условий
    public int deleteAuthorsWithoutBooks() {
        return dsl.deleteFrom(AUTHOR)
                .whereNotExists(
                    dsl.selectOne()
                       .from(BOOK)
                       .where(BOOK.AUTHOR_ID.eq(AUTHOR.ID))
                )
                .execute();
    }
    
    // DELETE с использованием Record
    public void deleteAuthorWithRecord(Long authorId) {
        AuthorRecord author = dsl.fetchOne(AUTHOR, AUTHOR.ID.eq(authorId));
        if (author != null) {
            author.delete(); // Выполняет DELETE
        }
    }
}
```

---
## Продвинутые возможности **jOOQ**

### 1. Транзакции
```java
package com.example.jooq;

import org.jooq.DSLContext;
import org.jooq.Transaction;

public class TransactionExamples {
    
    private final DSLContext dsl;
    
    public TransactionExamples(DSLContext dsl) {
        this.dsl = dsl;
    }
    
    // Простая транзакция
    public void createAuthorWithBooks(String firstName, String lastName, List<String> bookTitles) {
        dsl.transaction(configuration -> {
            DSLContext transactionDsl = DSL.using(configuration);
            
            // Вставляем автора
            Long authorId = transactionDsl
                .insertInto(AUTHOR)
                .set(AUTHOR.FIRST_NAME, firstName)
                .set(AUTHOR.LAST_NAME, lastName)
                .returning(AUTHOR.ID)
                .fetchOne()
                .getId();
            
            // Вставляем книги
            for (String title : bookTitles) {
                transactionDsl
                    .insertInto(BOOK)
                    .set(BOOK.AUTHOR_ID, authorId)
                    .set(BOOK.TITLE, title)
                    .set(BOOK.PUBLISHED_IN, 2024)
                    .execute();
            }
        });
    }
    
    // Транзакция с ручным управлением
    public void transactionalOperation() {
        // Начинаем транзакцию
        dsl.transaction(ctx -> {
            try {
                DSLContext transactionalDsl = DSL.using(ctx);
                
                // Операции в транзакции
                transactionalDsl.update(AUTHOR)
                    .set(AUTHOR.LAST_NAME, "Updated")
                    .where(AUTHOR.ID.eq(1L))
                    .execute();
                
                // Если что-то пошло не так
                if (someCondition) {
                    throw new RuntimeException("Rollback this transaction");
                }
                
                // Коммит происходит автоматически при успешном завершении
            } catch (Exception e) {
                // Rollback происходит автоматически при исключении
                throw e;
            }
        });
    }
}
```

### 2. Постраничная выборка *(Pagination*)
```java
package com.example.jooq;

import org.jooq.*;
import java.util.List;

public class PaginationExamples {
    
    private final DSLContext dsl;
    
    public PaginationExamples(DSLContext dsl) {
        this.dsl = dsl;
    }
    
    // Простая пагинация
    public Result<Record> getAuthorsPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        
        return dsl.selectFrom(AUTHOR)
                .orderBy(AUTHOR.LAST_NAME, AUTHOR.FIRST_NAME)
                .limit(pageSize)
                .offset(offset)
                .fetch();
    }
    
    // Пагинация с общим количеством записей
    public PaginationResult<AuthorRecord> getAuthorsWithTotalCount(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        
        // Получаем данные для страницы
        List<AuthorRecord> authors = dsl.selectFrom(AUTHOR)
                .orderBy(AUTHOR.LAST_NAME)
                .limit(pageSize)
                .offset(offset)
                .fetch();
        
        // Получаем общее количество
        int totalCount = dsl.fetchCount(AUTHOR);
        
        return new PaginationResult<>(authors, totalCount, page, pageSize);
    }
    
    public static class PaginationResult<T> {
        private final List<T> data;
        private final int totalCount;
        private final int currentPage;
        private final int pageSize;
        
        public PaginationResult(List<T> data, int totalCount, int currentPage, int pageSize) {
            this.data = data;
            this.totalCount = totalCount;
            this.currentPage = currentPage;
            this.pageSize = pageSize;
        }
        
        // Геттеры
        public List<T> getData() { return data; }
        public int getTotalCount() { return totalCount; }
        public int getCurrentPage() { return currentPage; }
        public int getPageSize() { return pageSize; }
        public int getTotalPages() { 
            return (int) Math.ceil((double) totalCount / pageSize); 
        }
    }
}
```

### 3. Работа с **JSON** и сложными типами данных
```java
package com.example.jooq;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.JSON;

public class JsonExamples {
    
    private final DSLContext dsl;
    
    public JsonExamples(DSLContext dsl) {
        this.dsl = dsl;
    }
    
    // Работа с JSON полями (если БД поддерживает)
    public void jsonOperations() {
        // Предположим, что у AUTHOR есть JSON поле metadata
        String jsonData = "{\"website\": \"example.com\", \"social\": [\"twitter\", \"github\"]}";
        
        // Вставка JSON
        dsl.insertInto(AUTHOR)
           .set(AUTHOR.FIRST_NAME, "John")
           .set(AUTHOR.LAST_NAME, "Doe")
           //.set(AUTHOR.METADATA, DSL.json(jsonData)) // Для H2 с JSON поддержкой
           .execute();
        
        // Выборка с JSON условиями
        /*var result = dsl.selectFrom(AUTHOR)
                .where(DSL.jsonValue(AUTHOR.METADATA, "$.website").eq("example.com"))
                .fetch();*/
    }
}
```

### 4. Кастомные мапперы
```java
package com.example.jooq;

import org.jooq.Record;
import org.jooq.RecordMapper;

// Кастомный маппер для преобразования Record в доменный объект
public class AuthorMapper implements RecordMapper<Record, DomainAuthor> {
    
    @Override
    public DomainAuthor map(Record record) {
        return new DomainAuthor(
            record.get(AUTHOR.ID),
            record.get(AUTHOR.FIRST_NAME),
            record.get(AUTHOR.LAST_NAME),
            record.get(AUTHOR.BIRTH_DATE)
        );
    }
}

// Доменный объект
class DomainAuthor {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final java.time.LocalDate birthDate;
    
    public DomainAuthor(Long id, String firstName, String lastName, java.time.LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }
    
    // Геттеры...
}

// Использование кастомного маппера
public List<DomainAuthor> getDomainAuthors() {
    return dsl.selectFrom(AUTHOR)
            .fetch(new AuthorMapper());
}
```

---
## Интеграция с **Spring Boot**

### 1. Конфигурация Spring Boot
```java
@Configuration
public class JooqConfig {
    
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:~/testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }
    
    @Bean
    public DataSourceConnectionProvider connectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider(dataSource);
    }
    
    @Bean
    public DefaultConfiguration configuration(DataSource dataSource) {
        DefaultConfiguration config = new DefaultConfiguration();
        config.setConnectionProvider(connectionProvider(dataSource));
        config.setSQLDialect(SQLDialect.H2);
        return config;
    }
    
    @Bean
    public DSLContext dslContext(DefaultConfiguration configuration) {
        return DSL.using(configuration);
    }
}
```

### 2. Spring Service с jOOQ
```java
@Service
@Transactional
public class AuthorService {
    
    private final DSLContext dsl;
    
    public AuthorService(DSLContext dsl) {
        this.dsl = dsl;
    }
    
    public AuthorRecord createAuthor(String firstName, String lastName) {
        return dsl.insertInto(AUTHOR)
                .set(AUTHOR.FIRST_NAME, firstName)
                .set(AUTHOR.LAST_NAME, lastName)
                .returning()
                .fetchOne();
    }
    
    public List<AuthorRecord> findAllAuthors() {
        return dsl.selectFrom(AUTHOR)
                .orderBy(AUTHOR.LAST_NAME, AUTHOR.FIRST_NAME)
                .fetch();
    }
    
    public Optional<AuthorRecord> findAuthorById(Long id) {
        return Optional.ofNullable(
            dsl.selectFrom(AUTHOR)
               .where(AUTHOR.ID.eq(id))
               .fetchOne()
        );
    }
    
    public void updateAuthor(Long id, String firstName, String lastName) {
        dsl.update(AUTHOR)
           .set(AUTHOR.FIRST_NAME, firstName)
           .set(AUTHOR.LAST_NAME, lastName)
           .where(AUTHOR.ID.eq(id))
           .execute();
    }
    
    public void deleteAuthor(Long id) {
        dsl.deleteFrom(AUTHOR)
           .where(AUTHOR.ID.eq(id))
           .execute();
    }
}
```

---
## Лучшие практики для Java-разработчика
1. **Всегда используйте сгенерированные классы** для типобезопасности    
2. **Используйте транзакции** для групп связанных операций    
3. **Избегайте N+1 проблемы** с помощью JOIN запросов    
4. **Используйте пагинацию** для больших наборов данных    
5. **Логируйте SQL запросы** для отладки    
6. **Используйте batch операции** для массовых вставок/обновлений    

jOOQ предоставляет мощный и типобезопасный способ работы с SQL в Java, сочетая преимущества чистого SQL с безопасностью типов Java!

---
