package ITM;
/* Prompt для ChatGPT

Привет!
Поговорим на тему Java. Преимущественно Spring/ Hibernate.
Я буду писать вопрос и ответ.
Ты Тщательнейшим образом проанализируй ответ, Усовершенствуй его,
оптимизируй и если нужно - скорректируй и дополни представленный ответ, а затем оптимизируй и максимально структурируй Ответ, сократи пояснения и примеры.
Изложи суть кратко, сжато, но емко, ничего дишнего, отобрази суть четко и в красивом виде (можешь использовать элементы оформления, эмодзи и т.д.),
отрази все важные аспекты, чтобы информация отражала все важные аспекты.
Примеров приводить по умолчанию не нужно, только если я попрошу пояснить на примере - тогда приводи пример.

Все свои выводимые таблицы, которые ты будешь выводить для меня - оформи Markdown-формате, специально для вставки в GitHub.

Итак поехали:

 */

import java.util.TreeSet;

public class Example extends Object {

    public static void main(String[] args) {

        String src = "Hello";
//        String src = "Hello".intern();
        String update = new String("Hello").intern();
        String concat = new String("Hel" + "lo").intern();

        String dst = update.intern();

        System.out.println("src == dst" + src == dst);
        System.out.println("src == update" + src == update);
        System.out.println("src == concat" + src == concat);
        System.out.println("src == dst" + src == dst);
        TreeSet<String> treeSet = new TreeSet<>();
    }


}
