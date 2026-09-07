

```Java
"Привет! Пришли мне аналогичную (отличающуюся, с другими ошибками, но в том-же стиле задачу) на лайвкод по рефакторингу, без заранее готовой пометки ответов, только код"
@Service
public class PaymentService {

    NotificationService notificationService = new NotificationService();

    @Autowired
    RatesService ratesService;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Transactional
    public void calculate(double amount, Currency currency) {
        double rate = ratesService.doRequest()
                                  .getRates(currency.getCode())
                                  .getValue();

        double amountNat = rate * amount;

        if (amountNat > 1000) {
            Payment payment = new Payment(amountNat, amountNat * 0.5);
            paymentRepository.save(payment);
        }

        if (amountNat < 1000) {
            Payment payment = new Payment(amountNat, amountNat * 0.25);
            paymentRepository.save(payment);
        }

        if (amountNat > 5000) {
            Payment payment = new Payment(amountNat, amountNat * 0.75);
            paymentRepository.save(payment);
        }

        try {
            List<History> history = historyRepository.findAll();

            int historySizeNew = (int) history.stream()
                                              .filter(History::isNew)
                                              .count();

            notificationService.notify(payment, historySizeNew);
        } catch (Throwable t) {
            // some code
        }
    }
}
```

В чем суть задачи? Вам за 10 минут (пока тренируетесь засекайте, это важно) нужно отметить все проблемы связанные с кодом, логические, синтаксические, архитектурные, плохие моменты в код-стайле. Плюс общую логику задания, уточняйте у интервьюера что делает этот сервис, какие данные по контракту возвращаются. Не забывайте про мапперы вместо преобразований в коде, ДУМАЙТЕ ПРО СОЛИД, ОСОБЕННО ПРО D И S, В НИХ ВСЕГДА КОСЯКИ. Пример этот разберем на дейлике в четверг

---


