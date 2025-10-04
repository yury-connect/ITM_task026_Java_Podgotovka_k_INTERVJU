# Задача № 4:

---
**Дана схема БД**

![Схема|500](../_Attachments_Task4/shema_BD.png)

У клиента может не быть лицевых счетов. 
По лицевому счёту может не быть транзакций.

Необходимо написать SQL-запрос, возвращающий имя клиента, 
описание его лицевого счёта и среднюю сумму транзакции по этому счёту.

---
# РЕШЕНИЕ:
```sql
SELECT 
    c.Name AS CustomerName,
    a.Description AS AccountDescription,
    AVG(ft.TransactionAmount) AS AverageTransactionAmount
FROM Customer c
INNER JOIN Account a ON c.id = a.Customer_id
LEFT JOIN Fin_Transaction ft ON a.id = ft.Account_Id
GROUP BY c.id, c.Name, a.id, a.Description;
```
# Детальное пояснение:
### 1. **Структура таблиц и связи:**
- **Customer** → **Account** *(один-ко-многим)*: один клиент может иметь несколько счетов
- **Account** → **Fin_Transaction** *(один-ко-многим)*: один счет может иметь несколько транзакций

### 2. **Разбор JOIN'ов:**
#### **INNER JOIN между Customer и Account:**
```sql
INNER JOIN Account a ON c.id = a.Customer_id
```
- Возвращает только клиентов, у которых есть хотя бы один счет
- Исключает клиентов без счетов (по условию "описание его лицевого счёта" - если счета нет, то и описания нет)

#### **LEFT JOIN между Account и Fin_Transaction:**
```sql
LEFT JOIN Fin_Transaction ft ON a.id = ft.Account_Id
```
- Включает все счета, даже если по ним нет транзакций    
- Для счетов без транзакций `AVG(ft.TransactionAmount)` вернет `NULL`

### 3. **Группировка:**
```sql
GROUP BY c.id, c.Name, a.id, a.Description
```
- Группируем по всем неагрегированным полям из `SELECT`    
- `c.id` и `a.id` обеспечивают уникальность записей    
- `c.Name` и `a.Description` - это поля, которые мы хотим видеть в результате

### 4. **Агрегатная функция:**
```sql
AVG(ft.TransactionAmount) AS AverageTransactionAmount
```
- Вычисляет среднее арифметическое всех транзакций по каждому счету    
- Для счетов без транзакций возвращает `NULL`

---
