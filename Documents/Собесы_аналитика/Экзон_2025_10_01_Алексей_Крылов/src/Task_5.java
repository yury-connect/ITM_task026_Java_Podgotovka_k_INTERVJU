import java.util.*;
import java.util.stream.Collectors;

public class Task_5 {
    public static List<Tag> getTagsOfAuthorsBooks(List<Author> authors, List<String> searchAuthorIds) {
        // Проверка на null и пустые списки
        if (authors == null || searchAuthorIds == null) {
            return new ArrayList<>();
        }

        // Создаем Set для быстрого поиска идентификаторов
        Set<String> authorIdSet = new HashSet<>(searchAuthorIds);

        // Используем Stream API для обработки данных
        return authors.stream()
                .filter(author -> author != null && authorIdSet.contains(author.id))
                .filter(author -> author.books != null)
                .flatMap(author -> author.books.stream())
                .filter(book -> book != null && book.tags != null)
                .flatMap(book -> book.tags.stream())
                .filter(tag -> tag != null)
                .distinct() // Убираем дубликаты тегов
                .collect(Collectors.toList());
    }

    // *** Из условия : ***
    
}
