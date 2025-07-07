# Краткий [конспект](https://suchkov.tech/elasticsearch-quick-start/) от [suchkov tech](https://www.youtube.com/@suchkov-tech) // [blog](https://suchkov.tech/blog/):

Elasticsearch – быстрый старт

![|350x200](https://suchkov.tech/wp-content/uploads/2024/08/2100px-Elasticsearch_logo.png)

## Что такое Elasticsearch

![](https://suchkov.tech/wp-content/uploads/2024/08/elastic-what-is-it-1024x617.png)

[Elasticsearch](https://www.elastic.co/elasticsearch/) — это хранилище документов, c возможностью создавать полнотекстовые индексы для последующего поиска, чаще всего используемое в качестве поискового движка. ElasticSearch добавляет к возможностям библиотеки [Apache Lucene](https://lucene.apache.org/), на котором основан, такие функции, как шардирование, репликацию, удобный JSON API и множество других улучшений. Это делает ElasticSearch одним из самых популярных решений для полнотекстового поиска.

## Основные понятия

![](https://suchkov.tech/wp-content/uploads/2024/08/%D0%BE%D1%81%D0%BD%D0%BE%D0%B2%D0%BD%D1%8B%D0%B5-%D0%BF%D0%BE%D0%BD%D1%8F%D1%82%D0%B8%D1%8F-1024x538.png)

### Индекс

Индекс предназначен для группировки данных в логические структуры. У нас может быть индекс по фильмам, или индекс по товарам, или какой-то еще. Если проводить аналогию с реляционными базами данных – то это как таблица в реляционной базе (только более гибкая, так как может хранить документы разной схемы).

### Документы

Это непосредственно записи в индексе, если мы опять сравниваем с реляционными базами данных, то это аналогично строке в таблице, но со своими отличиями.

### Запросы

Запросы в Elastic Search предназначены для поиска и фильтрации данных в индексах. С помощью запросов мы можем находить документы, соответствующие определенным условиям, сортировать результаты по релевантности, выполнять агрегации и многое другое.  
Если сравнивать с реляционной базой данных – то это как обычные sql запросы, только выглядят по-другому.

Если вы хотите разобраться в самом SQL и научиться уверенно работать с реляционными базами данных, то приглашаю на мой **курс по SQL и PostgreSQL**, он доступнен на платформах [**Stepik**](https://stepik.org/a/207878/pay?promo=ddbf70fca27233e4) и [**Udemy**](https://www.udemy.com/course/sql-postgresql/?couponCode=15253750454055F2A858)

## Установка и запуск через Docker

Для запуска Elasticsearch локально проще всего воспользоваться Docker:

```bash
docker run -p 9200:9200 -e "discovery.type=single-node" elasticsearch:7.17.22
```

Проверить что все работает как надо можно перейдя в браузере по адресу `localhost:9200`

В ответ должно появиться следующее

```json
{
  "name" : "8038cbce69bf",
  "cluster_name" : "docker-cluster",
  "cluster_uuid" : "S5nOCHwSQC6dLhUN8iZDZQ",
  "version" : {
    "number" : "7.17.22",
    "build_flavor" : "default",
    "build_type" : "docker",
    "build_hash" : "38e9ca2e81304a821c50862dafab089ca863944b",
    "build_date" : "2024-06-06T07:35:17.876121680Z",
    "build_snapshot" : false,
    "lucene_version" : "8.11.3",
    "minimum_wire_compatibility_version" : "6.8.0",
    "minimum_index_compatibility_version" : "6.0.0-beta1"
  },
  "tagline" : "You Know, for Search"
}
```

## API Elasticsearch

Взаимодействие с Elasticsearch происходит по http протоколу. Ниже представлена коллекция Postman с различными запросами к Elasticsearch: создание индекса, добавление, получение и удаление документов, поиск.

[ElasticSearch Postman collection](https://suchkov.tech/wp-content/uploads/2024/08/ElasticSearch-Postman-collection.zip)[Скачать](https://suchkov.tech/wp-content/uploads/2024/08/ElasticSearch-Postman-collection.zip)

## Материалы

Репозиторий с проектом [github.com/SuchkovDenis/elasticexample](https://github.com/SuchkovDenis/elasticexample)

Видео на youtube [https://youtu.be/vxE1aGTEnbE](https://youtu.be/vxE1aGTEnbE)