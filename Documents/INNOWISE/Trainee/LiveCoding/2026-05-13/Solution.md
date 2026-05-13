Идеальное решение данной задачи
от ментора / Владислава

---
```java
class ProductCategoryServiceImpl implements ProductCategoryService {  
    /* Метод вызывается 1 раз,  
     * инициализирует сервис всеми товарами и категориями.  
     */  
  
    private final Map<Long, Set<Long>> productIdByCategoryId 
	    = new HashMap();  
    private final Map<Long, List<Product>> productListByCategoryId 
	    = new HashMap();  
  
    @Override  
    public void init(List<Product> products, List<Category> categories) {  
        //TODO implement  
  
        for (Product pr : products) {  
            for (Category category : pr.getCategories()) {  
                if (productIdByCategoryId.get(category.getId()) == null) {  
                    productIdByCategoryId.put(category.getId(), new HashSet());  
                }  
                productIdByCategoryId.get(category.getId()).add(pr.getId());  
                if (productListByCategoryId.get(category.getId()) == null) {
	                productListByCategoryId.put(
		                category.getId(), new ArrayList());  
                }  
                productListByCategoryId.get(category.getId()).add(pr);  
            }  
        }  
  
        for (Map.Entry<Long, 
	        List<Product>> entry : productListByCategoryId.entrySet()) {  
            entry.getValue().sort(Comparator.comparing(Product::getName));  
        }  
    }  
  
    /**  
     * Проверяет принадлежность товара к категории,  
     * может вызываться много раз и часто.  
     *  
     * @param productId идентификатор товара  
     * @param categoryId идентификатор категории  
     * @return @code true} если товар принадлежит к категории,  
     * в остальных случаях {@code false}  
     */  
    @Override  
    public boolean isProductInCategory(Long productId, Long categoryId) { 
	    //TODO implement  
        // need map categoryId -> Set<ProductId> (Map<Long, Set<Long>)  
        Set<Long> productsInCategory = productIdByCategoryId.get(categoryId);  
        if (productsInCategory != null) {  
            return productsInCategory.contains(productId);  
        } else {  
            return false;  
        }  
    }  
  
    /**  
     * Возвращает список товаров по категории.  
     * Товары должны быть отсортированы по имени.  
     * Может вызываться много раз и часто.  
     *  
     * @param categoryId идентификатор категории  
     * @return список товаров, принадлежащих к категории * или пустой список,  
     * если к категории не принадлежит ни один товар  
     */  
    @Override  
    public Collection<Product> getProductsByCategory(Long categoryId) {  
        //TODO implement  
        // need map category -> List<Product> (sorted)  
        List<Product> products = productListByCategoryId.get(categoryId);  
        if (products != null) {  
            return products;  
        } else {  
            return Collections.emptyList();  
        }  
    }  
}
```

---
