-- Удалите старую таблицу если существует
DROP TABLE IF EXISTS books;

-- Создание таблицы (оптимизированная версия для H2)
-- Для H2 (полностью совместимая версия)
CREATE TABLE IF NOT EXISTS books (
                                     id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
    );

-- Добавляем ограничения отдельными командами
ALTER TABLE books ADD CONSTRAINT chk_price CHECK (price >= 1);
ALTER TABLE books ADD CONSTRAINT chk_status CHECK (status IN ('AVAILABLE', 'RESERVED', 'SOLD_OUT'));

-- Вставка 10 книг с корректными UUID
INSERT INTO books (id, title, price, status) VALUES
                                                 ('550e8400-e29b-41d4-a716-446655440000', 'Clean Code', 35.99, 'AVAILABLE'),
                                                 ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'Design Patterns', 42.50, 'AVAILABLE'),
                                                 ('3fa85f64-5717-4562-b3fc-2c963f66afa6', 'The Pragmatic Programmer', 29.99, 'RESERVED'),
                                                 ('d3d94468-2d9a-471f-96d9-6dfa7dca5f3a', 'Effective Java', 39.95, 'AVAILABLE'),
                                                 ('a3f8e7b4-7d7e-4f5a-b8d3-3e9d4c2f1e0b', 'Head First Design Patterns', 45.00, 'AVAILABLE'),
                                                 ('6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'Refactoring', 37.25, 'SOLD_OUT'),
                                                 ('9a7b8140-9dad-11d1-80b4-00c04fd430c8', 'Domain-Driven Design', 31.99, 'AVAILABLE'),
                                                 ('2b8f2c8e-9dae-11d1-80b4-00c04fd430c8', 'Introduction to Algorithms', 89.99, 'AVAILABLE'),
                                                 ('4c8f2c8e-9dae-11d1-80b4-00c04fd430c8', 'Code Complete', 49.99, 'RESERVED'),
                                                 ('5d9f2c8e-9dae-11d1-80b4-00c04fd430c8', 'The Mythical Man-Month', 27.50, 'AVAILABLE');


