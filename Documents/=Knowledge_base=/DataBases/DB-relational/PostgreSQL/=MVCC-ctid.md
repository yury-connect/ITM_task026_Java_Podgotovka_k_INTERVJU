поле `ctid` — это не 4 байта. Это специальный тип данных `tid` (tuple identifier), который физически представляет собой пару чисел: номер блока (страницы) и индекс кортежа внутри этого блока [](https://dba.stackexchange.com/revisions/d20f2bfc-0d05-45c1-a666-a4213c7b781d/view-source)[](https://stackoverflow.com/feeds/question/73047900).

Его размер в памяти составляет **6 байт** (48 бит) [](https://www.postgresql.org/message-id/DBAP191MB128920D3A4B5DFB03ED61D1BB0889%40DBAP191MB1289.EURP191.PROD.OUTLOOK.COM)[](http://mail.spinics.net/lists/pgsql/msg215764.html). Это позволяет адресовать значительно больший объем данных, чем 32-битное число.

### 🎯 Диапазон и переполнение

- **Диапазон номеров блоков:** Блоки (страницы) нумеруются от **0 до 4 294 967 294** (`0xFFFFFFFE`), что почти полностью покрывает 32-битный диапазон без знака (2^32 - 2) [](https://dba.stackexchange.com/revisions/d20f2bfc-0d05-45c1-a666-a4213c7b781d/view-source)[](https://topanswers.xyz/databases?q=178). Это сделано для поддержки максимального размера таблицы в 32 ТБ [](https://dba.stackexchange.com/revisions/d20f2bfc-0d05-45c1-a666-a4213c7b781d/view-source)[](https://topanswers.xyz/databases?q=178).
    
- **Переполнение:** Практически **невозможно**. Чтобы исчерпать этот диапазон, ваша таблица должна была бы превысить максимальный физический размер, допустимый в PostgreSQL. Поэтому проблема переполнения `ctid` на практике не стоит.