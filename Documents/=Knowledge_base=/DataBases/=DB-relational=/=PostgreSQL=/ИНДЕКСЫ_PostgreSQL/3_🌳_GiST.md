# 🌳 **GiST** — «инфраструктура для сложных типов» <br>(*третий по частоте*)

**Принцип работы**: Это не один индекс, а **инфраструктура (*фреймворк*)**, в рамках которой можно реализовать разные стратегии индексации для сложных типов данных. Он позволяет строить сбалансированные деревья, где каждый узел хранит «обобщённую» информацию о своих потомках (например, ограничивающий прямоугольник для геометрии)[](https://www.postgresql.org/docs/current/indexes-types.html?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1).

**Для чего нужен**: Геоданные (PostGIS), полнотекстовый поиск, поиск ближайших соседей (KNN), интервалы (диапазоны дат), а также другие задачи, где нужен многомерный поиск[](https://www.postgresql.org/docs/current/indexes-types.html?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1)[](https://www.postgresql.org/message-id/5416189A-8595-459F-BBFA-481E8AB29162%40decibel.org).

**Сложность**: Зависит от конкретной реализации (операторного класса). Обычно **O(log n)**, но может быть менее эффективен, чем B-tree для простых данных.

**Плюсы**:
- Поддерживает **сложные типы** (геометрия, интервалы, текст).    
- Поддерживает **поиск ближайших соседей** (KNN)[](https://www.postgresql.org/docs/current/indexes-types.html?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1).    
- Быстрее обновляется, чем GIN[](https://www.postgresql.org/docs/9/textsearch-indexes.html).    

**Минусы**:
- **Lossy (потеря точности)**: индекс может вернуть «ложные срабатывания», которые приходится перепроверять по таблице[](https://www.postgresql.org/docs/10/textsearch-indexes.html).    
- Медленнее, чем GIN, для полнотекстового поиска.    
- Требует создания операторного класса для нестандартных типов.    

**Когда брать**: Для **геоданных, интервалов, поиска ближайших соседей**, а также для **динамических данных**, где важна скорость обновления (GiST обновляется быстрее GIN)[](https://www.postgresql.org/docs/9/textsearch-indexes.html).

**Когда не следует**: Для простых скалярных значений (там B-tree быстрее и компактнее)[](https://www.postgresql.org/message-id/op.uaup73lxcigqcu@apollo13.peufeu.com).

---
