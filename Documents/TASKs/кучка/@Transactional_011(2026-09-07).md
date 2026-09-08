

```Java
 
Вы работаете над backend-сервисом банка, который управляет кредитными картами клиентов.

У каждого клиента может быть несколько кредитных карт.
Каждая карта имеет список операций (transactions).

Сервис аналитики хочет получить агрегированную статистику:
сумму потраченных денег по каждой категории операций среди всех активных карт клиентов.

Категории операций могут быть, например:
FOOD
TAXI
TRAVEL
SHOPPING

Важно учитывать только активные карты.

@Data
public class Client {

    private Long id;
    private String name;
    private List<CreditCard> cards;

}

@Data
public class CreditCard {

    private String cardNumber;
    private CardStatus status;
    private List<Transaction> transactions;

}

public enum CardStatus {

    ACTIVE,
    BLOCKED,
    EXPIRED

}

public enum TransactionCategory {

    FOOD,
    TAXI,
    TRAVEL,
    SHOPPING

}

public Map<TransactionCategory, BigDecimal> calculateTotalSpentByCategory(List<Client> clients) {

    return clients.stream()
            // TODO
}
```