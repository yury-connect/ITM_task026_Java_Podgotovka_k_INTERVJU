## 6/20: What will be the result of the following fragment of code (assume that the connection is valid)?

```java
try(Statement statement = connection.createStatement();
ResultSet resultSet = statement.executeQuery("SELECT * FROM contact")) {
    System.out.println(resultSet.getInt("id") + "\t"
    + resultSet.getString("firstName") + "\t"
    + resultSet.getString("lastName") + "\t"
    + resultSet.getString("email") + "\t"
    + resultSet.getString("phoneNo"));
} catch(SQLException e) {
    System.out.println("SQLException");
}
```

- Compilation fails.    
- This program will print all the rows from contact.    
- This program will print: SQLException.    
- This program will print the first row from contact.    

---
## 6/20: Каким будет результат следующего фрагмента кода (предположим, что соединение действительно)?

- Ошибка компиляции.    
- Эта программа выведет все строки из таблицы contact.    
- Эта программа выведет: SQLException.    
- Эта программа выведет первую строку из таблицы contact.

---
