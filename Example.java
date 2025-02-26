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
