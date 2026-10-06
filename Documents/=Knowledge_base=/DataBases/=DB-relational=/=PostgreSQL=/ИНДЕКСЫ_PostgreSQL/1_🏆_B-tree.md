# 🏆 B-tree — «рабочая лошадка» (*используется в 95% случаев*)

**Принцип работы**: Сбалансированное дерево, где данные отсортированы по ключу. Это классический индекс, кот-й по умолчанию создаётся командой `CREATE INDEX`[](https://www.postgresql.org/docs/current/indexes-types.html?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1).

**Для чего нужен**: Поддерживает **все основные операции**: равенство (`=`), диапазонные сравнения (`<`, `>`, `BETWEEN`, `IN`), сортировку (`ORDER BY`) и поиск по префиксу строки (`LIKE 'foo%'`). Именно этот тип индекса может обеспечивать уникальность (UNIQUE) и сортированный вывод данных[](https://www.postgresql.org/docs/current/indexes-types.html?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1).

**Сложность**: Поиск, вставка и удаление — **O(log n)**.

**Плюсы**:
- Универсальность: подходит для большинства задач.    
- Поддерживает сортировку и уникальность.    
- Хорошо оптимизирован в PostgreSQL.    

**Минусы**:
- Не подходит для сложных структур данных (*массивы, JSONB, геометрия*).    
- Требует больше места, чем *BRIN*.    

**Когда брать**: **Всегда**, если у тебя нет специфической задачи (*полнотекстовый поиск, геоданные, массивы*). Если сомневаешься — бери *B-tree*[](https://www.postgresql.org/message-id/op.uaup73lxcigqcu@apollo13.peufeu.com).

**Когда не следует**: Когда данные — это массивы, JSONB, геометрия или полнотекстовый поиск. Для этого есть *GIN* и *GiST*.

---
