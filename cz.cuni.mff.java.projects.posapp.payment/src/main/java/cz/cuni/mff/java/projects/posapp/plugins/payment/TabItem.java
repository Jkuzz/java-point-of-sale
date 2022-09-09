package cz.cuni.mff.java.projects.posapp.plugins.payment;

/**
 * Represents an items that can be present in a Tab.
 */
public class TabItem {
    /**
     * @return how many of this item are in the Tab
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @return what is the price of the product
     */
    public float getPrice() {
        return price;
    }

    /**
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * @return id of the product, as is in the database
     */
    public int getProductId() {
        return productId;
    }

    private int amount;
    private final float price;
    private final String name;
    private final int productId;

    /**
     * Create a tabProduct. If adding to a tab, use mergeAmount to combine the item amounts.
     * @param price the price of the product
     * @param name the name of the product
     * @param productId id of the product, as is in the database
     */
    public TabItem(float price, String name, int productId) {
        this.amount = 1;
        this.productId = productId;
        this.price = price;
        this.name = name;
    }

    /**
     * Compare whether the productIds of two TabItems are equal
     * @param obj other tabItem
     * @return true if same id
     */
    public boolean equals(TabItem obj) {
        return obj.getProductId() == productId;
    }


    /**
     * Merge an existing TabItem's amount into this one.
     * @param item to merge
     */
    public void mergeAmount(TabItem item) {
        this.amount += item.getAmount();
    }
}
