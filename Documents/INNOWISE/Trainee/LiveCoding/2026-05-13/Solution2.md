Такое себе решение...
Скажем так - не особо рабоче...

```java
package com.innowise.livecoding;  
  
import java.util.*;  
import java.util.function.Function;  
import java.util.stream.Collectors;  
  
public class ProductCategoryServiceImpl implements ProductCategoryService {  
  
    private Map<Long, Product> products;  
    private Map<Long, Category> categories;  
  
    /* Метод вызывается 1 раз, инициализирует сервис всеми товарами и категориями.  
     */  
    @Override  
    public void init(List<Product> productsList, List<Category> categoriesList) {  
        products = productsList.stream().collect(Collectors.toMap(Product::getId, Function.identity()));  
        categories = categoriesList.stream().collect(Collectors.toMap(Category::getId, Function.identity()));  
    }  
  
    /**  
     * Проверяет принадлежность товара к категории, может вызываться много раз и часто.  
     *  
     * @param productId идентификатор товара  
     * @param categoryId идентификатор категории  
     * @return @code true} если товар принадлежит к категории, в остальных случаях {@code false}  
     *  
     * много раз и часто - значит будем работать по hash - функции  
     * Думаю - тут сделать на основе HashMap.  
     */  
    @Override  
    public boolean isProductInCategory(Long productId, Long categoryId) {  
        Product product = products.get(productId);  
        for (Category current: product.getCategories()) {  
            if (current.getId().equals(categoryId)) {  
                return true;  
            }  
        }  
        return false;  
    }  
  
    /**  
     * Возвращает список товаров по категории.  
     * Товары должны быть отсортированы по имени.  
     * Может вызываться много раз и часто.  
     *  
     * @param categoryId идентификатор категории  
     * @return список товаров, принадлежащих к категории * или пустой список, если к категории не принадлежит ни один товар  
     */  
    @Override  
    public Collection<Product> getProductsByCategory(Long categoryId) {  
        Set<Product> result = new HashSet<>();  
        for (Product product: products.values()) {  
            for (Category category: product.getCategories()) {  
                if (category.getId().equals(categoryId)) {  
                    result.add(product);  
                }  
            }  
        }  
        return result.stream().sorted(Comparator.comparing(Product::getName)).collect(Collectors.toList());  
    }  
}  
  
interface Category {  
    Long getId();  
    String getName();  
}  
  
interface Product {  
    Long getId();  
    String getName();  
    List<Category> getCategories();  
}  
  
interface ProductCategoryService {  
    void init(List<Product> products, List<Category> categories);  
    boolean isProductInCategory(Long productId, Long categoryId);  
    Collection<Product> getProductsByCategory(Long categoryId);  
}
```

---
