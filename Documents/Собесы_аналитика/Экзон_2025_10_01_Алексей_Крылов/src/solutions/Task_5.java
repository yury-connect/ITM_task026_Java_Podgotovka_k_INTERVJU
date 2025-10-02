import java.util.*;
import java.util.stream.Collectors;

//import static Task_5.Ex.Author;
//import static Task_5.Ex.Book;
//import static Task_5.Ex.Tag;
import  Task_5.Ex.Tag;
//import static Ex.Tag;

public class Task_5 {
    public static List<Tag> getTagsOfAuthorsBooks(List<Author> authors, List<String> searchAuthorIds) {
        if (authors == null || searchAuthorIds == null) {
            return new ArrayList<>();
        }

        List<Tag> result = new ArrayList<>();

        // Создаем Set для быстрого поиска идентификаторов
        Set<String> searchIds = new HashSet<>(searchAuthorIds);

        for (Author author : authors) {
            // Проверяем, что автор есть в списке искомых ID
            if (author != null && searchIds.contains(author.id)) {
                // Обрабатываем книги автора
                if (author.books != null) {
                    for (Book book : author.books) {
                        // Добавляем теги книги в результат
                        if (book != null && book.tags != null) {
                            result.addAll(book.tags);
                        }
                    }
                }
            }
        }

        return result;
    }



    // *** Из условия : ***
    public enum DocumentType {
        XML, PDF, DOCX
    }

    public static class Document {
        String id;
        DocumentType type;
        String content;
    }

    public static class DocumentService {

//        public void process(Document[] d) {
//            for (Document i : d) {
//                // Общая логика обработки документа
//                switch (i.type) {
//                    case DocumentType.PDF: {
//                        // Специфическая логика для обработки PDF
//                    } break;
//                    case DocumentType.DOCX: {
//                        // Специфическая логика для обработки Word
//                    } break;
//                    case DocumentType.XML: {
//                        // Специфическая логика для обработки XML
//                    } break;
//                }
//            }
//        }
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
