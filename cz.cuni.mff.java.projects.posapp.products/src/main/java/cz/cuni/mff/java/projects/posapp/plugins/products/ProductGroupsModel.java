package cz.cuni.mff.java.projects.posapp.plugins.products;

import cz.cuni.mff.java.projects.posapp.database.Database;
import cz.cuni.mff.java.projects.posapp.database.DevUser;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * Model holding the product groups hierarchy. Implemented as Singleton.
 */
public class ProductGroupsModel {

    /**
     * get the Singleton product model instance.
     * @return the Singleton instance
     */
    public static ProductGroupsModel getInstance() {
        if(instance == null) {
            instance = new ProductGroupsModel();
        }
        return instance;
    }

    /**
     * Singleton Model instance
     */
    private static ProductGroupsModel instance;

    private final Database db = Database.getInstance(new DevUser(), new ProductsClient());
    private final ArrayList<GroupComboBoxItem> groups = new ArrayList<>();


    /**
     * Creates the model by querying the product groups from the database.
     */
    private ProductGroupsModel() {
        queryGroups();
    }


    public Stream<GroupComboBoxItem> getGroupChildren(Integer parentId) {
        return groups.stream().filter(g -> g.getId() != null && Objects.equals(g.getParentId(), parentId));
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
        getGroupChildren(parent.getId()).forEach(g -> orderGroupChildren(g, orderedGroups, level + 1));
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


    public void insertNewGroup(HashMap<String, ProductInputComponent> userInputs) throws SQLException {
        ResultSet rs = db.query("""
                SELECT AUTO_INCREMENT
                FROM information_schema.tables
                WHERE table_name = 'product_groups'""");

        rs.next();
        int nextId = rs.getInt(1);

        JComboBox<GroupComboBoxItem> groupInput = userInputs.get("parent_id").getComboInput();
        GroupComboBoxItem selectedGroup = groupInput.getItemAt(groupInput.getSelectedIndex());

        String query = "INSERT INTO `product_groups` (`parent_id`, `name`, `id`) VALUES (?, ?, NULL);";
        String name = userInputs.get("name").getTextInput().getText();
        PreparedStatement stmt = db.prepareStatement(query);
        if(selectedGroup.getId() == null) {
            stmt.setNull(1, Types.INTEGER);
        } else {
            stmt.setInt(1, selectedGroup.getId());
        }

        groups.add(new GroupComboBoxItem(name, nextId, selectedGroup.getId()));

        stmt.setString(2, name);
        stmt.execute();
    }
}
