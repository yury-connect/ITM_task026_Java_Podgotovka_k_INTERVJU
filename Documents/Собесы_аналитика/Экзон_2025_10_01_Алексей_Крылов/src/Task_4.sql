SELECT
    c.Name AS CustomerName,
    a.Description AS AccountDescription,
    AVG(ft.TransactionAmount) AS AverageTransactionAmount
FROM Customer c
         INNER JOIN Account a ON c.id = a.Customer_id
         LEFT JOIN Fin_Transaction ft ON a.id = ft.Account_Id
GROUP BY c.id, c.Name, a.id, a.Description;

