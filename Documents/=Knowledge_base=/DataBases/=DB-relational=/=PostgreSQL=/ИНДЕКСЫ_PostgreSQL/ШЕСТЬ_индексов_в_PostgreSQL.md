В PostgreSQL **6 встроенных типов индексов**, которые поддерживаются ядром системы, плюс один расширяемый.

---
- **B-tree** — для 95% задач (*равенство, диапазоны, сортировка*).    
- **GIN** — для массивов, JSONB, полнотекстового поиска (*много чтения, мало записи*).    
- **GiST** — для геометрии, интервалов, KNN (*динамические данные*).    
- **BRIN** — для огромных упорядоченных таблиц (*логи, события*).    
- **Hash** — почти никогда (*B-tree лучше*).    
- **SP-GiST** — для специфических небалансированных структур (*геоданные, префиксы*).

---
## 🏗️ Встроенные типы (доступны «из коробки»)

Согласно официальной документации, к ним относятся[](https://www.postgresql.org/docs/current/indexes-types.html?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1)[](https://postgresql.kr/docs/current/indexes-types.html):

- **B-tree**: тип по умолчанию. Подходит для большинства запросов: сравнение на равенство, диапазонные условия (`<`, `>`, `BETWEEN`), а также сортировка (`ORDER BY`). Именно только B-tree может обеспечить уникальность (UNIQUE) и сортированный вывод[](http://repo.postgrespro.ru/doc//pgsql/10.22/en/postgres-A4-fop.pdf#366#65)[](https://git.postgresql.org/cgit/pgweb-static.git/plain/documentation/pdf/9.5/postgresql-9.5-A4.pdf?id=37e3e136a7856c191ed5bc794062549ef34da233#330#63).
    
- **Hash**: хранит 32-битный хеш-код. Используется **только для проверки на равенство** (`=`). На практике применяется редко из-за исторических проблем с надежностью и отсутствия поддержки диапазонных запросов[](https://www.postgresql.org/docs/current/indexes-types.html?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1)[](https://docs.aws.amazon.com/zh_cn/dms/latest/oracle-to-aurora-postgresql-migration-playbook/chap-oracle-aurora-pg.tables.indexes.md#1).
    
- **GiST (Generalized Search Tree)**: это не один индекс, а **инфраструктура** для создания разных стратегий индексации. Хорош для геометрических данных, полнотекстового поиска и поиска «ближайших соседей»[](https://www.postgresql.org/docs/current/indexes-types.html?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1)[](https://docs.aws.amazon.com/zh_cn/dms/latest/oracle-to-aurora-postgresql-migration-playbook/chap-oracle-aurora-pg.tables.indexes.md#1).
    
- **SP-GiST (Space-Partitioned GiST)**: похож на GiST, но реализует **небалансированные** структуры данных (например, квадродеревья, k-d деревья). Также используется для геоданных и сложных поисков[](https://www.postgresql.org/docs/current/indexes-types.html?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1)[](https://download.microsoft.com/download/8/9/b/89bf1624-449c-43dc-8ee8-23f4259d7e1d/Ora2Azure_PG_Migration_Guide.pdf#38#36).
    
- **GIN (Generalized Inverted Index)**: **инвертированный индекс**. Идеален для данных, состоящих из множества элементов: массивов, JSONB, полнотекстового поиска. Работает быстро на чтение, но медленнее на обновление[](https://www.postgresql.org/docs/current/indexes-types.html?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1)[](https://docs.aws.amazon.com/zh_cn/dms/latest/oracle-to-aurora-postgresql-migration-playbook/chap-oracle-aurora-pg.tables.indexes.md#1)[](https://www.postgresql.org/message-id/op.uaup73lxcigqcu@apollo13.peufeu.com).
    
- **BRIN (Block Range INdex)**: хранит сводку (минимум/максимум) для **диапазонов физических блоков** таблицы. Очень компактный и быстрый для больших таблиц, данные в которых физически упорядочены (например, по дате)[](https://www.postgresql.org/docs/current/indexes-types.html?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1)[](https://docs.aws.amazon.com/zh_cn/dms/latest/oracle-to-aurora-postgresql-migration-playbook/chap-oracle-aurora-pg.tables.indexes.md#1).    

### 🧩 Дополнительные (расширяемые)

- **Bloom**: тип индекса, доступный через расширение `bloom`. Используется для проверки на равенство по нескольким колонкам одновременно[](https://www.postgresql.org/docs/current/indexes-types.html?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1).    

Итог: **6 встроенных** (B-tree, Hash, GiST, SP-GiST, GIN, BRIN) и **1 расширяемый** (Bloom).

---
