# 🔷 Hash — «только для равенства» <br>(*пятый по частоте, используется редко*)

**Принцип работы**: Хранит **32-битный хеш-код** от значения колонки. Позволяет найти строку только по точному значению ключа[](https://www.postgresql.org/docs/current/indexes-types.html?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1)[](https://www.postgresql.org/message-id/attachment/115185/0003-index-types.patch).

**Для чего нужен**: **Только для проверки на равенство** (`=`). Не поддерживает диапазоны, сортировку, `LIKE` и т.д.

**Сложность**: Поиск **O(1)** в идеале, но на практике может быть медленнее *B-tree* из-за коллизий.

**Плюсы**:
- Теоретически быстрее *B-tree* для поиска по равенству.    
- Компактнее *B-tree*.    

**Минусы**:
- **Не поддерживает диапазоны и сортировку**.    
- Исторически считался ненадёжным (*в старых версиях не логировался в WAL, из-за чего мог «сломаться» после краха*).    
- На практике **почти не используется**, так как B-tree справляется с равенством не хуже, но при этом умеет гораздо больше[](https://www.postgresql.org/message-id/5416189A-8595-459F-BBFA-481E8AB29162%40decibel.org)[](https://www.postgresql.org/message-id/op.uaup73lxcigqcu@apollo13.peufeu.com).    

**Когда брать**: **Практически никогда**. B-tree настолько же быстр для равенства, но при этом универсален.

**Когда не следует**: Всегда, кроме случаев, когда ты точно знаешь, что тебе нужно именно это, и понимаешь все риски.

---
