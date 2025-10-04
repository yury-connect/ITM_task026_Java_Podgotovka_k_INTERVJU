package solutions;

import java.util.Collection;
import java.util.Iterator;

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

            // Для не-строк ничего не делаем - это будет максимально быстро
        }
    }
}
