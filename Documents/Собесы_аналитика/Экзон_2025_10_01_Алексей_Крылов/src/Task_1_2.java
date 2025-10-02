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
    class Person implements Comparable<Person> {
        String firstName;
        String lastName;
        Integer age;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return Objects.equals(firstName, person.firstName) &&
                    Objects.equals(lastName, person.lastName) &&
                    Objects.equals(age, person.age);
        }

        // result = 31 * result + (element == null ? 0 : element.hashCode());
        @Override
        public int hashCode() {
            return Objects.hash(firstName, lastName, age);
        }

        @Override
        public int compareTo(Person other) {
            // Сравнение по фамилии, затем по имени, затем по возрасту
            int lastNameCompare = Objects.compare(this.lastName, other.lastName,
                    String.CASE_INSENSITIVE_ORDER);
            if (lastNameCompare != 0) return lastNameCompare;

            int firstNameCompare = Objects.compare(this.firstName, other.firstName,
                    String.CASE_INSENSITIVE_ORDER);
            if (firstNameCompare != 0) return firstNameCompare;

            return Objects.compare(this.age, other.age, Integer::compare);
        }
    }
}
