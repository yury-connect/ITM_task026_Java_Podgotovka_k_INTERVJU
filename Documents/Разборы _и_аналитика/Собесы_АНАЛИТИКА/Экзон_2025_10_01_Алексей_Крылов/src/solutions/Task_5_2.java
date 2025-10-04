package solutions;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static solutions.Task_5_1.Ex.Author;
import static solutions.Task_5_1.Ex.Tag;


public class Task_5_2 {
    // Альтернативная реализация с использованием Stream API (Java 8+):
    public static List<Tag> getTagsOfAuthorsBooks(List<Author> authors, List<String> searchAuthorIds) {
        if (authors == null || searchAuthorIds == null) {
            return Collections.emptyList();
        }

        Set<String> searchIds = new HashSet<>(searchAuthorIds);

        return authors.stream()
                .filter(author -> author != null && searchIds.contains(author.id))
                .filter(author -> author.books != null)
                .flatMap(author -> author.books.stream())
                .filter(book -> book != null && book.tags != null)
                .flatMap(book -> book.tags.stream())
                .collect(Collectors.toList());
    }
    /*
    Ожидаемый результат для тестовых данных:
    Для входных данных из main метода с searchAuthorIds = ["Author_1", "Author_3"]:
        [Tag{name="Tag_1"}, Tag{name="Tag_2"}, Tag{name="Tag_3"}, Tag{name="Tag_6"}, Tag{name="Tag_7"}]
      Ключевые преимущества решения:
    * Надежность - обработка всех потенциальных null значений
    * Производительность - использование HashSet для быстрого поиска
    * Читаемость - простой и понятный императивный стиль
    * Соответствие требованиям - точная реализация описанной функциональности
    * Обработка ошибок - graceful degradation при некорректных входных данных
     */



    // *** Из условия : ***
    public class RefEx {
        public enum DocumentType {
            XML, PDF, DOCX
        }

        public static class Document {
            String id;
            Task_5_2.RefEx.DocumentType type;
            String content;
        }

        public static class DocumentService {

            public void process(Task_5_2.RefEx.Document[] d) {
                for (Task_5_2.RefEx.Document i : d) {
                    // Общая логика обработки документа
                    switch (i.type) {
                        case PDF: {
                            // Специфическая логика для обработки PDF
                        } break;
                        case DOCX: {
                            // Специфическая логика для обработки Word
                        } break;
                        case XML: {
                            // Специфическая логика для обработки XML
                        } break;
                    }
                }
            }
        }
    }





    public class Ex {
        // Тег
        public static class Tag {
            String name;

            public Tag(String name) {
                this.name = name;
            }
        }

        // Книга
        public static class Book {
            String name;
            List<Tag> tags;

            public Book(String name, List<Tag> tags) {
                this.name = name; this.tags = tags;
            }
        }

        // Автор
        public static class Author {
            String id;
            String name;
            List<Book> books;

            public Author(String id, String name, List<Book> books) {
                this.id = id; this.name = name; this.books = books;
            }
        }
    }
}
