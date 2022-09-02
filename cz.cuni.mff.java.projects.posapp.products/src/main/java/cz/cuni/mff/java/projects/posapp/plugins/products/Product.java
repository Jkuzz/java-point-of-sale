package cz.cuni.mff.java.projects.posapp.plugins.products;

import java.util.HashMap;

/**
 * Holds data about a product in the database.
 */
public class Product {
    /**
     * Get the product group in which this product is. Null if product is in root group.
     * @return parent group
     */
    public ProductGroup getParentGroup() {
        return parentGroup;
    }

    public void putField(String key, Object field) {
        productDetails.put(key, field);
    }

    /**
     * Get the hashmap defining the product columns.
     * @return the product fields
     */
    public HashMap<String, Object> getProductDetails() {
        return productDetails;
    }

    private final ProductGroup parentGroup;
    private final HashMap<String, Object> productDetails;

    public Product(ProductGroup parentGroup, HashMap<String, Object> productDetails) {
        this.parentGroup = parentGroup;
        this.productDetails = productDetails;
    }
}
