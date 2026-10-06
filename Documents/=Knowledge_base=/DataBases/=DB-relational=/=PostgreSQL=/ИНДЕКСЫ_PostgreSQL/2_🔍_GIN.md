# 🔍 **GIN** — «поиск по элементам» <br>(*второй по частоте*)

**Принцип работы**: Это **инвертированный индекс**. Внутри него строится *B-tree* по «ключам», где каждый ключ — это отдельный элемент из индексируемого значения (*например, элемент массива или слово в тексте*). Для каждого ключа хранится список указателей на строки, где он встречается[](https://www.postgresql.org/docs/16/gin-implementation.html#GIN-FAST-UPDATE).

**Для чего нужен**: Идеален для данных, которые **состоят из множества элементов**: массивы (`int[]`, `text[]`), JSONB, полнотекстовый поиск (`tsvector`)[](https://www.postgresql.org/docs/current/indexes-types.html?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1)[](https://git.postgresql.org/cgit/pgweb-static.git/plain/documentation/pdf/9.5/postgresql-9.5-A4.pdf?id=37e3e136a7856c191ed5bc794062549ef34da233#330#280).

**Сложность**: Поиск быстрый (*зависит от количества уникальных ключей логарифмически*), но **вставка и обновление медленные**, так как одна строка может породить много записей в индексе[](https://www.postgresql.org/docs/16/gin-implementation.html#GIN-FAST-UPDATE).

**Плюсы**:
- Очень быстрый поиск по элементам (*в **3** раза быстрее GiST*)[](https://www.postgresql.org/docs/9/textsearch-indexes.html).    
- Поддерживает полнотекстовый поиск и поиск по массивам.    
- Не теряет точность (не lossy) для стандартных запросов[](https://www.postgresql.org/docs/9/textsearch-indexes.html).    

**Минусы**:
- **Медленное обновление**: каждая вставка порождает много записей в индексе[](https://www.postgresql.org/docs/16/gin-implementation.html#GIN-FAST-UPDATE).    
- Занимает **в 2–3 раза больше места**, чем *GiST*[](https://www.postgresql.org/docs/9/textsearch-indexes.html).    
- Требует много памяти при построении.    

**Когда брать**: Когда данные — это **массивы, JSONB или полнотекстовый поиск**, и таблица **чаще читается, чем обновляется**[](https://www.postgresql.org/docs/10/textsearch-indexes.html).

**Когда не следует**: Для часто обновляемых таблиц (например, логи в реальном времени) или для простых скалярных значений (там B-tree лучше)[](https://www.postgresql.org/message-id/op.uaup73lxcigqcu@apollo13.peufeu.com).

---
