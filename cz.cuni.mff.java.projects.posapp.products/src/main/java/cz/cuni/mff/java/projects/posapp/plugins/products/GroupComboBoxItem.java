package cz.cuni.mff.java.projects.posapp.plugins.products;


/**
 * Item that holds fields of a product group and is used to display the group in a JComboBox
 */
public final class GroupComboBoxItem {
    private final String name;
    private final Integer id;
    private final Integer parentId;

    private int level = 0;

    /**
     * Set indentation level of the product group.
     * @param level how many parent groups until root group
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Create the GroupComboBoxItem. Used to hold information about a ProductGroup,
     * as well as be displayed in a ComboBox for easy selection
     * @param name of the group
     * @param id of the group
     * @param parentId if the group has a parent group, null if it's a root group
     */
    public GroupComboBoxItem(String name, Integer id, Integer parentId) {
        this.name = name;
        this.id = id;
        this.parentId = parentId;
    }

    /**
     * @return unique key of the group
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return foreign key id of parent group
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * Get the un-indented name of the product group
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Indent the name of the group by its level (how many parents before root)
     * @return correctly indented name
     */
    @Override
    public String toString() {
        return "    ".repeat(level) + name;
    }
}
