import java.util.Objects;

public class Task_1_1 {

    /*
    Для следующего Java - класса
        class Person {
            String firstName;
            String lastName;
            Integer age;
        }
    Необходимо реализовать стандартный метод,
    осуществляющий сравнение 2-х объектов Person.
     */
    class Person {
        String firstName;
        String lastName;
        Integer age;

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return Objects.equals(firstName, person.firstName) &&
                    Objects.equals(lastName, person.lastName) &&
                    Objects.equals(age, person.age); // Objects.equals() - для безопасного сравнения (обрабатывает null)
        }

        // result = 31 * result + (element == null ? 0 : element.hashCode());
        @Override
        public int hashCode() {
            return Objects.hash(firstName, lastName, age); // Objects.hash() для упрощения расчета хеш-кода
        }
    }
}
