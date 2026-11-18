# ПАТТЕРНЫ РАЗРАБОТКИ ПО (Software Patterns)

```text
ПАТТЕРНЫ РАЗРАБОТКИ ПО (Software Patterns)
│
├── 1. АРХИТЕКТУРНЫЕ ПАТТЕРНЫ (Architectural Patterns) — уровень всей системы
│   ├── Макроархитектура (организация компонентов)
│   │   ├── Монолитная / Слоистая (Layered / N-Tier)
│   │   ├── Микросервисная (Microservices)
│   │   ├── Событийно-ориентированная (Event-Driven Architecture)
│   │   ├── Гексагональная (Hexagonal / Ports & Adapters)
│   │   └── Луковая / Чистая архитектура (Onion / Clean Architecture)
│   │
│   ├── Управление данными (Data Management)
│   │   ├── EVENT SOURCING (Хранение состояния в виде событий)
│   │   ├── CQRS (Разделение моделей чтения и записи)
│   │   ├── Database per Service (Отдельная БД на сервис)
│   │   └── Shared Database (Общая БД)
│   │
│   ├── Распределенные транзакции и интеграция
│   │   ├── Saga (Управление распределенными транзакциями)
│   │   ├── Transactional Outbox (Гарантированная отправка сообщений)
│   │   └── API Gateway / BFF (Единая точка входа)
│   │
│   └── Отказоустойчивость (Cloud & Resilience)
│       ├── Circuit Breaker (Предохранитель)
│       ├── Retry / Timeout (Повторы и таймауты)
│       └── Bulkhead (Изоляция отсеков)
│
├── 2. ПАТТЕРНЫ ПРОЕКТИРОВАНИЯ (Design Patterns) — уровень классов и модулей
│   ├── Классика GoF (Банда четырех)
│   │   ├── Порождающие (Creational): Singleton, Factory Method, Abstract Factory, Builder, Prototype
│   │   ├── Структурные (Structural): Adapter, Decorator, Facade, Proxy, Composite, Bridge, Flyweight
│   │   └── Поведенческие (Behavioral): Strategy, Observer, Command, State, Chain of Responsibility, Iterator
│   │
│   ├── Архитектура корпоративных приложений (PoEAA — Мартин Фаулер)
│   │   ├── Логика домена: Domain Model, Transaction Script, Table Module
│   │   ├── Источники данных: Repository, Data Mapper, Active Record, Unit of Work
│   │   └── Веб-представление: MVC, MVP, MVVM
│   │
│   └── Многопоточность (Concurrency Patterns)
│       ├── Producer-Consumer (Производитель-Потребитель)
│       ├── Thread Pool (Пул потоков)
│       └── Read-Write Lock (Блокировка чтения-записи)
│
└── 3. ИДИОМЫ (Idioms) — уровень конкретного языка программирования
    ├── Контекстные менеджеры (with в Python, using в C#)
    ├── RAII (Управление ресурсами в C++ и Rust)
    └── Сборщики мусора и работа со ссылками (WeakReference и др.)

```
★ 

### Как использовать эту структуру у себя
	
- **Первый уровень (Уровень абстракции).** Делит все шаблоны по масштабу: система целиком (Архитектура), взаимодействие классов (Проектирование) и синтаксис языка (Идиомы).
- **Второй уровень (Область задач).** Внутри каждого уровня паттерны группируются по проблеме, которую они решают (например, хранение данных, создание объектов или отказоустойчивость).
- **Связи между ветками.** Важно учитывать, что некоторые паттерны работают на стыке категорий. Например, **Event Sourcing** тесно связан с веткой _Событийно-ориентированной архитектуры (EDA)_ и методологией _Domain-Driven Design (DDD)_.

---
