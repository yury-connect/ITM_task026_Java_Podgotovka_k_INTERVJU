# WAL (**Write-Ahead Logging**, журнал предзаписи)

WAL (**Write-Ahead Logging**, журнал предзаписи) — это ==метод в базах данных и файловых системах, при котором любые изменения сначала записываются в специальный файл на диске, и только потом меняются основные файлы с данными==. Это гарантирует сохранность информации при сбоях питания или падении программы. [1](https://postgrespro.ru/docs/postgresql/14/wal-intro?lang=ru-en), [2](https://datafinder.ru/products/kak-ustroen-postgresql/wal-v-postgresql-obzor-mehanizma-zhurnala-predzapisi), [3](https://postgrespro.ru/docs/postgrespro/current/wal-intro)

Как это работает

- База данных получает запрос на изменение или добавление данных.

- Система пишет короткую инструкцию об этом шаге в конец файла журнала (WAL).

- После успешной записи в журнал система подтверждает клиенту, что операция выполнена.

- Сами файлы таблиц на диске обновляются позже, уже в фоновом режиме. [1](https://ru.wikipedia.org/wiki/%D0%A3%D0%BF%D1%80%D0%B5%D0%B6%D0%B4%D0%B0%D1%8E%D1%89%D0%B0%D1%8F_%D0%B6%D1%83%D1%80%D0%BD%D0%B0%D0%BB%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D1%8F)

Зачем нужен WAL

- **Надежность данных**: если сервер внезапно выключится, при перезапуске система заглянет в журнал и восстановит все незаписанные вовремя куски. [1](https://datafinder.ru/products/kak-ustroen-postgresql/wal-v-postgresql-obzor-mehanizma-zhurnala-predzapisi)

- **Высокая скорость**: последовательная запись в конец одного файла журнала идет намного быстрее, чем хаотичное изменение разных участков большой базы на диске. [1](https://habr.com/ru/companies/postgrespro/articles/459250/)

- **Репликация**: журнал помогает передавать точную историю изменений на другие запасные серверы. [1](https://www.reddit.com/r/AskComputerScience/comments/1dfazpm/what_is_the_difference_between_a_write_ahead_log/?tl=ru)

---
### Далее: <br> [Как работает WAL в **PostgreSQL**](Documents/=Knowledge_base=/DataBases/DB-relational/PostgreSQL/WAL_в_PostgreSQL)
