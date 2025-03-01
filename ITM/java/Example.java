package ITM;
/* Prompt для ChatGPT

Привет!
Поговорим на тему Java. Преимущественно core.
Я буду писать вопрос и ответ.
Ты Усовершенствуй, оптимизируй и если нужно - упрости или дополни Ответы.
Изложи суть кратко, но емко, четко и в красивом виде, отрази все важные аспекты, чтобы информация отражала все важные аспекты.
Примеров по умолчанию не нужно, только если я попрошу пояснить на примере - тогда приводи пример.
Итак поехали:

 */

public class Example {

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
    }


}
