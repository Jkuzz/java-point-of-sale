package cz.cuni.mff.java.projects.posapp.plugins.products;

import java.util.ArrayList;


public class ProductGroup {
    private final ArrayList<ProductGroup> childGroups;
    private final ArrayList<Product> childProducts;

    public ArrayList<ProductGroup> getChildGroups() {
        return childGroups;
    }

    public ArrayList<Product> getChildProducts() {
        return childProducts;
    }

    public void addChildProduct(Product child) {
        childProducts.add(child);
    }

    public int getId() {
        return id;
    }

    private final int id;

    public ProductGroup(ArrayList<ProductGroup> childGroups, ArrayList<Product> childProducts, int id) {
        this.childGroups = childGroups;
        this.childProducts = childProducts;
        this.id = id;
    }
}
