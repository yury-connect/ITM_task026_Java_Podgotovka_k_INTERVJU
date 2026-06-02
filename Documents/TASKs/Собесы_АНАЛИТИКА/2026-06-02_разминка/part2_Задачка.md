
```java
import java.util.Collection;
import java.util.List;

public interface Category {
    Long getId();
    String getName();
}

public interface Product {
    Long getId();
    String getName();
    List<Category> getCategories();
}

public interface ProductCategoryService {
    void init(List<Product> products, List<Category> categories);
    boolean isProductInCategory(Long productId, Long categoryId);
    Collection<Product> getProductsByCategory(Long categoryId);
}

public class ProductCategoryServiceImpl implements ProductCategoryService {

    /**
     * Метод вызывается 1 раз,
     * инициализирует сервис всеми товарами и категориями.
     */
    @Override
    public void init(List<Product> products, List<Category> categories) {
        //TODO implement
    }

    /**
     * Проверяет принадлежность товара к категории,
     * может вызываться много раз и часто.
     *
     * @param productId  идентификатор товара
     * @param categoryId идентификатор категории
     * @return {@code true} если товар принадлежит к категории,
     * в остальных случаях {@code code false}
     */
    @Override
    public boolean isProductInCategory(Long productId, Long categoryId) {
        //TODO implement
        return false;
    }

    /**
     * Возвращает список товаров по категории.
     * Товары должны быть отсортированы по имени.
     * Может вызываться много раз и часто.
     *
     * @param categoryId идентификатор категории
     * @return список товаров, принадлежащих к категории
     * или пустой список,
     * если к категории не принадлежит ни один товар
     */
    @Override
    public Collection<Product> getProductsByCategory(Long categoryId) {
        //TODO implement
        return null;
    }
}
```


