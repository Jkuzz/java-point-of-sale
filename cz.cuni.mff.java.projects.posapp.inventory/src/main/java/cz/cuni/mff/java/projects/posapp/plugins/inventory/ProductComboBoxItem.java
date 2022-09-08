package cz.cuni.mff.java.projects.posapp.plugins.inventory;

/**
 * Objects representing inventory products. Intended to be displayed in a JComboBox to select a product.
 */
public record ProductComboBoxItem(String name, int id) {
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
