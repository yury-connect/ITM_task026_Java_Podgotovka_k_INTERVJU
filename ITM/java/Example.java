package ITM;
/* Prompt для ChatGPT

Привет!
Поговорим на тему Java. Преимущественно core.
Я буду писать вопрос и ответ.
Ты Усовершенствуй, оптимизируй. если нужно - упрости или дополни Ответы.
Изложи суть кратко, но емко, четко, отрази все важные аспекты, можешь что-то добавить, чтобы  информация была полной.
Примеров по умолчанию не нужно, только если я попрошу пояснить на примере.

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
