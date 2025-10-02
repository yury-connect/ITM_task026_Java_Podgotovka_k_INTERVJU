## Задача № 4:

---
**Дана схема БД**

![Схема|500](_Attachments_Task4/shema_BD.png)


У клиента может не быть лицевых счетов. По лицевому счёту может не быть
транзакций.

Необходимо написать SQL-запрос, возвращающий имя клиента, описание его
лицевого счёта и среднюю сумму транзакции по этому счёту.

```mermaid
erDiagram
    CUSTOMER {
        int Id PK "PK"
        string Name
        string Address
    }
    ACCOUNT {
        int Id PK "PK"
        string Acc_Number
        string Description
        int Customer_Id FK "FK1"
    }
    FIN_TRANSACTION {
        int Id PK "PK"
        datetime TransactDate
        decimal Amount
        int Account_Id FK "FK1"
        string Description
    }

    CUSTOMER ||--o{ ACCOUNT : "has"
    ACCOUNT ||--o{ FIN_TRANSACTION : "has"
```

---
Скрин оригинального задания:
![|955x647](_Attachments_Task4/task4_screen.png)

---
