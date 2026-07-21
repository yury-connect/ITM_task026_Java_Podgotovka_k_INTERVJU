Вот краткая и структурированная информация о `ctid`.
	
- **Размер**: **6 байт** (48 бит) для хранения в структуре `ItemPointerData` [](https://www.postgresql.org/message-id/DBAP191MB128920D3A4B5DFB03ED61D1BB0889%40DBAP191MB1289.EURP191.PROD.OUTLOOK.COM)[](http://mail.spinics.net/lists/pgsql/msg215764.html)[](https://postgrespro.ru/list/thread-id/2657526#CACJufxG9bvBRC9zT_ph-MC9jZEOawyqR=ESs5wCG59j2Jxu2Rg@mail.gmail.com).
    
- **Диапазон**: Внутреннее представление — пара чисел `(номер_страницы, индекс_кортежа)`. Максимальный размер файла таблицы накладывает практический лимит на количество страниц, который, как правило, не достигается до исчерпания дискового пространства. Нумерация строк на странице начинается с **1** [](https://v2.postgres.ai/docs/postgres-howtos/advanced-topics/misc/tuple-sparsity)[](https://raw.githubusercontent.com/postgres-ai/postgres-howtos/main/0004_tuple_sparsenes.md). Явного ограничения в 4 миллиарда для `ctid` **нет**, в отличие от идентификаторов транзакций `xid` [](https://anoncvs.postgresql.org/cvsweb.cgi/pgsql/doc/Attic/FAQ.diff?r1=1.400;r2=1.403)[](https://lists.osgeo.org/pipermail/postgis-users/2005-September/009425.html).
    
- **При переполнении**: Практически недостижимо из-за ограничений файловой системы (*максимальный размер таблицы*), но теоретически может привести к ошибкам ввода-вывода или невозможности выделить новую страницу.

### ⚠️ Самое важное
	
- **Это не постоянный ID**: `ctid` — это **физический адрес** строки на диске. 
  Он меняется при `UPDATE` или `VACUUM FULL` и поэтому **не должен использоваться в качестве первичного ключа** или для долгосрочной идентификации строк [](https://postgrespro.ru/docs/postgrespro/current/ddl-system-columns?lang=en)[](https://lists.osgeo.org/pipermail/postgis-users/2005-September/009425.html).
    
- **Быстрый доступ**: `ctid` позволяет выполнить **Tid Scan** — самый быстрый способ прямого доступа к строке (например, `SELECT * FROM table WHERE ctid = '(10,5)'`). Для PostgreSQL версии 14+ также доступен сканир-е диапазона `ctid` [](https://postgrespro.ru/list/thread-id/1858245)[](https://git.cse.iitb.ac.in/abuhujairkhan/postgres-fd-implementation/-/commits/388b959315205b0b65efb074ec84e1d7fad62402)[](https://git.cse.iitb.ac.in/abuhujairkhan/postgres-fd-implementation/-/blob/27e1f14563cf982f1f4d71e21ef247866662a052/src/backend/optimizer/plan/setrefs.c).
    
- **Особенности**: На `ctid` **нельзя создать индекс** [](https://v2.postgres.ai/docs/postgres-howtos/advanced-topics/misc/tuple-sparsity)[](https://raw.githubusercontent.com/postgres-ai/postgres-howtos/main/0004_tuple_sparsenes.md).
