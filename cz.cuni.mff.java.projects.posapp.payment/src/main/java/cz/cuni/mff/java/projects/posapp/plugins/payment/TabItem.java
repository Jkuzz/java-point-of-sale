package cz.cuni.mff.java.projects.posapp.plugins.payment;

public class TabItem {
    public int getAmount() {
        return amount;
    }

    public float getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getProductId() {
        return productId;
    }

    private int amount;
    private final float price;
    private final String name;
    private final int productId;

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
