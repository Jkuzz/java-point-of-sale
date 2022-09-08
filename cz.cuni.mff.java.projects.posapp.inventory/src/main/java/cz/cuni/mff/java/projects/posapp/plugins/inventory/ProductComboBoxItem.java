package cz.cuni.mff.java.projects.posapp.plugins.inventory;

public class ProductComboBoxItem {
    public int getId() {
        return id;
    }

    private final String name;
    private final int id;

    public ProductComboBoxItem(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
