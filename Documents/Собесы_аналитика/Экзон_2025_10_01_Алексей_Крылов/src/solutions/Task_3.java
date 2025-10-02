public class Task_3 {

    /*
    Дан массив **`double[ N ]`**, необходимо реализовать на *Java* метод,
    возвращающий частное от деления разности максимального с минимальным элементов
    и среднего арифметического значения элементов массива.
    В случае ошибки требуемый метод должен возвращать <u>только</u> указанный Exception.
    Сигнатура требуемого метода должна иметь вид:
        public static double foo(double[] sourceArray) throws MyException;
     */
    public static double foo(double[] sourceArray) throws MyException {
        if (sourceArray == null || sourceArray.length == 0) {
            throw new MyException("Array is null or empty");
        }

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0;


        for (double value : sourceArray) {

            // Проверка на специальные значения (NaN, Infinity)
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                throw new MyException("Array contains NaN or Infinite values");
            }

            if (value < min) min = value;
            if (value > max) max = value;
            sum += value;
        }

        double average = sum / sourceArray.length;

        // Проверка деления на ноль с учетом точности double
        if (Math.abs(average) < 1e-10) {
            throw new MyException("Arithmetic mean is too close to zero, division by zero");
        }

        return (max - min) / average;
    }



    static class MyException extends RuntimeException {
        public MyException(String message) {
            super(message);
        }
    }



    public static void main(String[] args) {
        try {
            double[] array = {1.0, 2.0, 3.0, 4.0, 5.0};
            double result = foo(array);
            System.out.println("Result: " + result); // (5-1)/3 = 1.333...
        } catch (MyException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
