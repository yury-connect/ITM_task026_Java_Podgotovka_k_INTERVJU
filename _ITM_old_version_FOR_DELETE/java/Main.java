import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

record User(String name, int age, List<String> cards) {}

public class Main {
    public static void main(String[] args) {

        List<User> users = new ArrayList<>();

        users.add(new User("John Doe", 30, List.of("Visa", "MasterCard", "Visa Gold", "Visa Infinite")));
        users.add(new User("Jane Smith", 25, List.of("Visa Gold", "American Express", "UnionPay", "JCB")));
        users.add(new User("Alice Johnson", 40, List.of("MasterCard", "Discover", "Diners Club", "Maestro")));
        users.add(new User("Bob Brown", 35, List.of("Visa", "MasterCard", "Visa Platinum", "World Elite Mastercard")));
        users.add(new User("Charlie Davis", 28, List.of("Visa Electron", "UnionPay", "Mir", "Carte Blanche")));


//        users.forEach(System.out::println);
        users.stream()
                .flatMap(u -> u.cards().stream())
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()))
                .entrySet()
                .stream()
//                .sorted((c1, c2) -> c1.getValue().compareTo(c2.getValue()))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .forEach(System.out::println);


    }
}