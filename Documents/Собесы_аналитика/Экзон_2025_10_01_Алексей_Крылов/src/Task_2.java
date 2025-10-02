import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class Task_2 {

    /*
    Необходимо реализовать на Java метод, принимающий на вход коллекцию,
    в которой присутствуют строки, и удаляющий из переданной коллекции
    все строки, начинающиеся на «aaa».

    Сигнатура необходимого метода должна иметь вид:
    public static void filterTripleA(Collection strings);
     */

    public static void filterTripleA(Collection strings) {
        if (strings == null || strings.isEmpty()) {
            return;
        }

        Iterator iterator = strings.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();

            // Быстрая проверка: если это строка И начинается с "aaa"
            if (obj instanceof String && ((String) obj).startsWith("aaa")) {
                iterator.remove();
            }
            // Для не-строк ничего не делаем - максимально быстро
        }



        Map<String, Integer> countMap = new HashMap<>();
        for (String s : strings) {
            String lower = s.toLowerCase();
            countMap.put(lower, countMap.getOrDefault(lower, 0) + 1);
        }

        return strings.stream()
                .filter(s -> countMap.get(s.toLowerCase()) > 1)
                .collect(Collectors.toSet()); // или toList(), если нужны ду
    }
}
