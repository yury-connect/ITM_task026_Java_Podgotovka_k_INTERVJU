import java.util.Objects;

// Вариация предыдущей задачи
public class Task_1_2 {

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
            return Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(age, person.age);
        }

        @Override
        public int hashCode() {
            return Objects.hash(firstName, lastName, age);
        }
    }

// Решение



}
