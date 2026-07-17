В контексте CAP-теоремы эти популярные БД и системы классифицируются так:

- **PostgreSQL / MySQL (Классические RDBMS)**: Тяготеют к **CP** (*Consistency + Partition Tolerance*). В случае сетевого раздела (*partition*) они, как правило, жертвуют доступностью (*Availability*) в пользу строгой согласованности (*Consistency*) и ACID-гарантий [](https://raw.githubusercontent.com/karanpratapsingh/portfolio/498eac1d906d83c1260609c2a72fef4116189954/data/courses/system-design/cap-theorem.mdx)[](https://www.opensourceforu.com/2023/09/cap-theorem-use-it-to-choose-an-open-source-database/)[](https://tikv.website.cncfstack.com/deep-dive/consensus-algorithm/consistency-availability-partitioning/).
    
- **MongoDB**: Относится к **CP** (*Consistency + Partition Tolerance*) [](https://raw.githubusercontent.com/karanpratapsingh/portfolio/498eac1d906d83c1260609c2a72fef4116189954/data/courses/system-design/cap-theorem.mdx)[](https://www.opensourceforu.com/2023/09/cap-theorem-use-it-to-choose-an-open-source-database/). В случае раздела, чтобы гарантировать консистентность, некоторые узлы могут стать недоступными (*отказ от Availability*).
    
- **Redis**: При использовании в распределённом режиме (кластер, sentinel) классифицируется как **CP** (*Consistency + Partition Tolerance*) [](https://www.opensourceforu.com/2023/09/cap-theorem-use-it-to-choose-an-open-source-database/)[](https://cloud.tencent.cn/developer/ask/sof/108549713?from=16139)[](https://cloud.tencent.com.cn/developer/article/2109707?from=15425).  
  При сетевом разделе Redis на стороне меньшинства будет недоступен, чтобы избежать конфликтов записи.
    
- **MinIO**: Выбирает модель **CP** (*Consistency + Partition Tolerance*) [](https://www.min.io/learn/cap-theorem#w-node-e0f3f3e8-7fc8-bed8-0f5e-2c38a3355af4-262a83eb)[](https://wenku.csdn.net/column/5uszvmztpt).  
  Принцип работы построен на кворумах: запись считается успешной только после подтверждения от строго определённого числа узлов, что гарантирует сильную согласованность даже ценой доступности во время проблем с сетью [](https://wenku.csdn.net/column/5uszvmztpt).
    
- **Cassandra (для сравнения)**: Часто приводит как пример **AP** (*Availability + Partition Tolerance*), жертвуя сильной согласованностью в пользу высокой доступности, обеспечивая в итоге лишь согласованность "в конце концов" (*eventual consistency*) [](https://raw.githubusercontent.com/karanpratapsingh/portfolio/498eac1d906d83c1260609c2a72fef4116189954/data/courses/system-design/cap-theorem.mdx)[](https://www.opensourceforu.com/2023/09/cap-theorem-use-it-to-choose-an-open-source-database/).

---
