# 🌲 **SP-GiST** — «для небалансированных деревьев» <br>(*шестой по частоте*)

**Принцип работы**: **Space-Partitioned Generalized Search Tree**. Это инфраструктура, похожая на GiST, но для **небалансированных** структур данных: квадродеревья (quadtrees), k-d деревья, radix-деревья (tries)[](https://www.postgresql.org/docs/current/indexes-types.html?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1?utm_source=balun_courses&utm_medium=organic&utm_campaign=junior_golang?utm_source=telegram&utm_medium=cpc&utm_campaign=Golang_google&utm_term=vnutrennee-ustrojstvo-allokatora&erid=LjN8KS8u1)[](https://www.postgis.net/stuff/postgis-3.4.0beta1-it.pdf#128#10).

**Для чего нужен**: Геоданные, поиск по префиксам, маршрутизация IP, телефонные номера, подстроки[](https://www.postgis.net/stuff/postgis-3.4.0beta1-it.pdf#128#10).

**Сложность**: Зависит от реализации. Обычно **O(log n)**, но может быть неоптимальным для некоторых типов запросов.

**Плюсы**:
- Поддерживает **небалансированные деревья**, которые GiST не может.    
- Хорош для точечных запросов и поиска по префиксам.    

**Минусы**:
- **Lossy** (*как GiST*).    
- Менее универсален, чем GiST.    
- Требует создания операторного класса.    

**Когда брать**: Для **геоданных, поиска по префиксам, IP-маршрутизации**, когда GiST не подходит[](https://www.postgis.net/stuff/postgis-3.4.0beta1-it.pdf#128#10).

**Когда не следует**: Для большинства стандартных задач (*B-tree, GIN или GiST справятся лучше*).

---
