package cz.cuni.mff.java.projects.posapp.plugins.products;

import cz.cuni.mff.java.projects.posapp.database.Database;
import cz.cuni.mff.java.projects.posapp.database.DevUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Model holding the product groups hierarchy.
 */
public class ProductGroupsModel {

    private final Database db = Database.getInstance(new DevUser(), new ProductsClient());
    private final ArrayList<GroupComboBoxItem> groups = new ArrayList<>();


    /**
     * Creates the model by querying the product groups from the database.
     */
    public ProductGroupsModel() {
        queryGroups();
    }


    /**
     * Get all the groups that were fetched from the database as ComboBoxItems.
     * @return groups
     */
    public ArrayList<GroupComboBoxItem> getGroups() {
        return groups;
    }


    /**
     * Get the groups that were queried in the database. First performs a hierarchical DFS ordering.
     * Use this method to display groups in text or JComboBox. The groups are ordered such that a parent group
     * is followed by every of its children and their children recursively before any other group.
     * Ordering of root parents is same as in the database.
     * @return ordered groups
     */
    public ArrayList<GroupComboBoxItem> getOrderedGroups() {
        ArrayList<GroupComboBoxItem> orderedGroups = new ArrayList<>();

        groups.stream()
                .filter(g -> g.getParentId() == null)
                .forEach(g -> orderGroupChildren(g, orderedGroups, 0));
        return orderedGroups;
    }


    /**
     * Recursively add the parent and its children to the ordering. DFS recursion
     * @param parent order this groups children
     * @param orderedGroups ordered groups will be added here
     * @param level level of recursion to indent, start at 0
     */
    private void orderGroupChildren(GroupComboBoxItem parent, ArrayList<GroupComboBoxItem> orderedGroups, int level) {
        parent.setLevel(level);
        orderedGroups.add(parent);
        if(parent.getId() == null) return;
        groups.stream()
                .filter(g -> parent.getId().equals(g.getParentId()))
                .forEach(g -> orderGroupChildren(g, orderedGroups, level + 1));
    }


    /**
     * Queries for groups in the database and saves to 'groups' arraylist
     */
    private void queryGroups() {
        final String query = "SELECT name, id, parent_id FROM product_groups;";

        groups.add(new GroupComboBoxItem("None", null, null));

        ResultSet resultSet = db.query(query);
        try {
            while(resultSet.next()) {
                String name = resultSet.getString(1);
                int id = resultSet.getInt(2);
                Integer parentId = resultSet.getInt(3);
                if (resultSet.wasNull()) {
                    parentId = null;
                }
                groups.add(new GroupComboBoxItem(name, id, parentId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
