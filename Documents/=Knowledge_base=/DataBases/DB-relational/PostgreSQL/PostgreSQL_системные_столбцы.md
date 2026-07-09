[***GitHub**link*](https://github.com/yury-connect/ITM_task026_Java_Podgotovka_k_INTERVJU/blob/main/Documents/%3DKnowledge_base%3D/DataBases/DB-relational/PostgreSQL/PostgreSQL_%D1%81%D0%B8%D1%81%D1%82%D0%B5%D0%BC%D0%BD%D1%8B%D0%B5_%D1%81%D1%82%D0%BE%D0%BB%D0%B1%D1%86%D1%8B.md)
В PostgreSQL каждая строка (tuple) таблицы содержит набор **системных столбцов** (system columns), которые не видны при обычном запросе (`SELECT *`), но могут быть явно запрошены [](https://postgresql.ac.cn/docs/current/ddl-system-columns.html)[](https://www.commandprompt.com/ppbook/x8747.html). Эти столбцы содержат служебную информацию, критически важную для понимания работы MVCC и внутреннего устройства базы данных [](https://www.highgo.ca/2020/05/20/phoney-table-columns-in-postgresql/).

Ниже представлен перечень всех системных атрибутов, с описанием их типа значения и примерами.

### 📋 Системные столбцы PostgreSQL

|Атрибут|Тип значения|Краткое описание|Пример значения|Примечание|
|---|---|---|---|---|
|**`ctid`**|`tid` (tuple ID)|**Физическое местоположение** версии строки в таблице. Это пара чисел: номер блока (страницы) и индекс кортежа внутри этого блока [](https://postgresql.ac.cn/docs/current/ddl-system-columns.html)[](https://www.highgo.ca/2020/05/20/phoney-table-columns-in-postgresql/).|`(0,1)`|**Важно:** Это не постоянный идентификатор строки. Значение меняется при `UPDATE` или `VACUUM FULL` [](https://postgresql.ac.cn/docs/current/ddl-system-columns.html)[](https://www.highgo.ca/2020/05/20/phoney-table-columns-in-postgresql/). Может использоваться для удаления дубликатов, когда нет первичного ключа [](https://www.highgo.ca/2020/05/20/phoney-table-columns-in-postgresql/).|
|**`xmin`**|`xid` (transaction ID)|**Идентификатор транзакции**, которая **вставила** эту версию строки (`INSERT`) [](https://postgresql.ac.cn/docs/current/ddl-system-columns.html)[](https://www.highgo.ca/2020/05/20/phoney-table-columns-in-postgresql/).|`12345`|Основной атрибут для определения видимости строки. Позволяет узнать время вставки, если включено `track_commit_timestamp` [](https://www.highgo.ca/2020/05/20/phoney-table-columns-in-postgresql/).|
|**`xmax`**|`xid` (transaction ID)|**Идентификатор транзакции**, которая **удалила** (или заблокировала) эту версию строки. Для активных, не удаленных строк значение равно `0` [](https://postgresql.ac.cn/docs/current/ddl-system-columns.html)[](https://www.highgo.ca/2020/05/20/phoney-table-columns-in-postgresql/).|`12346` или `0`|Если значение не равно нулю, это означает, что строка была удалена или заблокирована другой транзакцией [](https://www.highgo.ca/2020/05/20/phoney-table-columns-in-postgresql/).|
|**`cmin`**|`cid` (command ID)|**Идентификатор команды** внутри вставляющей транзакции. Используется для того, чтобы транзакция могла видеть свои собственные изменения в правильном порядке [](https://postgresql.ac.cn/docs/current/ddl-system-columns.html).|`0`|На практике вместе с `cmax` указывают на одно и то же поле, которое может отражать комбинированный идентификатор обеих операций [](https://www.postgresql.org/docs/19/ddl-system-columns.html).|
|**`cmax`**|`cid` (command ID)|**Идентификатор команды** внутри удаляющей транзакции [](https://postgresql.ac.cn/docs/current/ddl-system-columns.html).|`0`|См. описание `cmin`. В современных версиях PostgreSQL эти столбцы функционально объединены [](https://www.postgresql.org/docs/19/ddl-system-columns.html).|
|**`tableoid`**|`oid` (object ID)|**OID таблицы**, в которой физически хранится строка. Это особенно полезно при запросах к **секционированным** или **наследуемым** таблицам [](https://postgresql.ac.cn/docs/current/ddl-system-columns.html)[](https://www.highgo.ca/2020/05/20/phoney-table-columns-in-postgresql/).|`16384`|Может быть соединен с `pg_class.oid`, чтобы получить имя таблицы (`tableoid::regclass`) [](https://postgresql.ac.cn/docs/current/ddl-system-columns.html)[](https://www.highgo.ca/2020/05/20/phoney-table-columns-in-postgresql/).|

### ⚙️ Дополнительная техническая информация

- **Внутреннее представление:** В заголовке кортежа (`HeapTupleHeaderData`) хранятся не только эти системные атрибуты, но и битовые маски (`t_infomask`), которые кешируют статус транзакций (`XMIN_COMMITTED`, `XMAX_INVALID` и т.д.) для ускорения проверок видимости [](https://raw.githubusercontent.com/postgres/postgres/master/src/include/access/htup_details.h)[](https://cgi.cse.unsw.edu.au/~cs9315/21T1/lectures/pg-tuples/slides.html).
    
- **Команда `SELECT`:** Чтобы увидеть значения системных столбцов, их нужно явно указать в запросе:
```sql
SELECT ctid, xmin, xmax, tableoid, * FROM your_table;
```

Эти системные столбцы — фундамент, на котором построена механика MVCC, но они предназначены для внутреннего использования и отладки. Для надежной идентификации логических строк в приложении всегда следует использовать **первичный ключ** (Primary Key), а не `ctid` или `xmin` [](https://postgresql.ac.cn/docs/current/ddl-system-columns.html)[](https://www.postgresql.org/docs/19/ddl-system-columns.html).


