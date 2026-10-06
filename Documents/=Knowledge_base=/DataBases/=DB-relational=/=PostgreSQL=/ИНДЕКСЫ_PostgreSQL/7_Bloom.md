**Bloom — это индекс для «широких» таблиц с множеством фильтров по равенству.**

Он не заменяет B-tree, а решает другую проблему: когда у таблицы 6–10 колонок, и запросы фильтруют их в **произвольных комбинациях**. Вместо шести отдельных B-tree можно создать **один Bloom-индекс**, который покрывает любые подмножества этих колонок [](https://www.prismagraphql.com/blog/postgres-bloom-index-the-overlooked-postgres-feature).

---
## 🔬 Принцип работы

Bloom-индекс хранит **не сами значения**, а **битовую сигнатуру** для каждой строки. При вставке значения колонок хешируются, и в сигнатуре устанавливаются соответствующие биты. При поиске PostgreSQL проверяет: «есть ли в сигнатуре биты для всех указанных в запросе значений?»[](https://www.prismagraphql.com/blog/postgres-bloom-index-the-overlooked-postgres-feature)

Это **lossy (неточный) индекс**: он может вернуть «ложное срабатывание» — строку, которой на самом деле нет. Поэтому найденные кандидаты всегда **перепроверяются по таблице** (heap) [](https://www.postgresql.org/docs/19/bloom.html#BLOOM-EXAMPLES)[](https://postgrespro.ru/docs/postgrespro/17/bloom).

---
## 📊 Ключевые характеристики

**Поддерживает только `=` (равенство).** Не работает для диапазонов (`>`, `<`), `LIKE`, сортировки. Только точное совпадение [](https://www.postgresql.org/docs/19/bloom.html#BLOOM-EXAMPLES)[](https://postgrespro.ru/docs/postgrespro/17/bloom).

**Не поддерживает UNIQUE и NULL.** Нельзя создать уникальный индекс, не индексирует NULL-значения [](https://postgrespro.ru/docs/postgresql/current/bloom).

**Классы операторов только для `int4` и `text`.** Для других типов нужно создавать свой класс операторов [](https://postgrespro.ru/docs/postgresql/current/bloom).

**Параметры:**
- `length` — длина сигнатуры в битах (по умолчанию 80, максимум 4096). Больше бит — меньше ложных срабатываний, но больше индекс [](https://www.postgresql.org/docs/19/bloom.html#BLOOM-EXAMPLES).    
- `col1..col32` — сколько бит выделить под каждую колонку (по умолч-ю 2 бита) [](https://www.postgresql.org/docs/19/bloom.html#BLOOM-EXAMPLES).    

---
## ⚖️ Плюсы и минусы

**Плюсы:**
- **Один индекс вместо многих.** Для 6 колонок — 1 Bloom вместо 6 B-tree или множества композитных [](https://www.prismagraphql.com/blog/postgres-bloom-index-the-overlooked-postgres-feature).    
- **Компактность.** В примере из документации: Bloom = 153 MB против B-tree на все колонки = 386 MB [](https://www.postgresql.org/docs/19/bloom.html#BLOOM-EXAMPLES). В другом примере: 1 Bloom (1.5 MB) vs 6 B-tree (12 MB) [](http://repo.postgrespro.ru/doc//pgproee/12.20.1/en/postgres-A4.pdf#454#366).    
- **Меньше write amplification.** При вставке обновляется один индекс, а не шесть [](https://www.prismagraphql.com/blog/postgres-bloom-index-the-overlooked-postgres-feature).    

**Минусы:**
- **Только равенство.** Никаких диапазонов, сортировок, `LIKE` [](https://www.postgresql.org/docs/19/bloom.html#BLOOM-EXAMPLES).    
- **Lossy.** Требует перепроверки по heap — это дополнительные чтения [](https://www.prismagraphql.com/blog/postgres-bloom-index-the-overlooked-postgres-feature).    
- **Не заменяет B-tree.** Если фильтр всегда по одной-двум колонкам — B-tree быстрее [](https://www.prismagraphql.com/blog/postgres-bloom-index-the-overlooked-postgres-feature).    

---
## 🎯 Когда брать

**Бери, когда:**
- Таблица **широкая** (6+ колонок), и запросы фильтруют **разные подмножества** этих колонок [](https://www.prismagraphql.com/blog/postgres-bloom-index-the-overlooked-postgres-feature).    
- Ты устал создавать **множество B-tree** под каждую комбинацию.    
- Экономия **места и write amplification** важнее микросекунд на чтение.    

**Не бери, когда:**
- Фильтруешь **только по 1–2 известным колонкам** — B-tree быстрее и точнее [](https://www.prismagraphql.com/blog/postgres-bloom-index-the-overlooked-postgres-feature).    
- Нужны **диапазоны, сортировка, LIKE** — Bloom это не умеет [](https://www.postgresql.org/docs/19/bloom.html#BLOOM-EXAMPLES).    
- Нужна **уникальность** (UNIQUE) — Bloom не поддерживает [](https://postgrespro.ru/docs/postgresql/current/bloom).    

---
## 🎤 Для собеседования

> _«Bloom-индекс — это индекс на основе фильтра Блума. Он хранит битовые сигнатуры для каждой строки и позволяет быстро отсеивать неподходящие строки при фильтрации по равенству сразу по нескольким колонкам. Главный сценарий — широкие таблицы, где запросы фильтруют разные комбинации колонок. Один Bloom-индекс заменяет множество B-tree и занимает в разы меньше места. Но он lossy — требует перепроверки по heap, поддерживает только `=`, не поддерживает UNIQUE и NULL. Это нишевый инструмент для конкретной проблемы, а не замена B-tree.»_

---
