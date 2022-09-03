package cz.cuni.mff.java.projects.posapp.plugins.payment;

import cz.cuni.mff.java.projects.posapp.plugins.products.GroupComboBoxItem;
import cz.cuni.mff.java.projects.posapp.plugins.products.ProductGroupsModel;
import cz.cuni.mff.java.projects.posapp.plugins.products.ProductsTableModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

public class ProductGroupAddPanel extends JPanel {

    private static final int GRID_COLUMNS = 8;
    private final AddProductPanel root;
    private final HashMap<Integer, ProductGroupAddPanel> childPanelsCache = new HashMap<>();
    private JButton selectedProduct;

    private final static Border buttonBorder = BorderFactory.createLineBorder(
            new Color(85, 99, 255), 2);

    /**
     * Make the panel that displays all the groups subgroups, as well as all its products.
     * The subgroups onclick changes the window to display their own group panel.
     * Products can be selected and added to the tab by the root.
     * @param root owner of this panel. Provides the panel switching and adding logic.
     * @param parentGroupPanel parent group of this group, null if root
     * @param panelGroupId id of the current group, null if root
     */
    public ProductGroupAddPanel(AddProductPanel root, ProductGroupAddPanel parentGroupPanel, Integer panelGroupId) {
        super(new GridBagLayout());
        this.root = root;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 50;
        gbc.ipady = 50;
        gbc.anchor = GridBagConstraints.PAGE_START;


        if(panelGroupId != null) {
            addButton(makeGroupSwitchButton("Back", parentGroupPanel), gbc);
        }

        ProductGroupsModel groupsModel = ProductGroupsModel.getInstance();
        Stream<GroupComboBoxItem> childGroups = groupsModel.getGroupChildren(panelGroupId);
        childGroups.forEach(g -> {
            JButton button = makeGroupSwitchButton(g.getName(), getCachedPanel(g.getId()));
            addButton(button, gbc);
        });

        ProductsTableModel productsModel = ProductsTableModel.getInstance();
        ArrayList<HashMap<String, Object>> groupProducts = productsModel.getGroupProducts(panelGroupId);
        groupProducts.forEach(g -> {
            JButton button = makeProductButton(g);
            addButton(button, gbc);
        });
    }


    /**
     * Add a button to the button panel. Properly increments GBC to maintain a grid.
     * (GridLayout was not used due to inappropriate stretching and scaling issues)
     * @param button to add
     * @param gbc grid's GBC
     */
    private void addButton(JButton button, GridBagConstraints gbc) {
        if(gbc.gridx > GRID_COLUMNS) {
            gbc.gridx = 0;
            gbc.gridy += 1;
        } else {
            gbc.gridx += 1;
        }
        add(button, gbc);
    }


    /**
     * Get cached panel for the child group. If not present in cache, create it.
     * @param groupId of child group to display
     * @return panel of child group
     */
    private ProductGroupAddPanel getCachedPanel(int groupId) {
        if(!childPanelsCache.containsKey(groupId))  {
            ProductGroupAddPanel addPanel = new ProductGroupAddPanel(root, this, groupId);
            childPanelsCache.put(groupId, addPanel);
        }            
        return childPanelsCache.get(groupId);        
    }

    /**
     * Make a button for switching to another group's panel. Notifies the parent panel of the switch.
     * The parent does not hold the hierarchy, only makes the switch. The group panels hierarchy is
     * controlled directly by the panels.
     * @param text to display on the button
     * @param panelToShow in the parent panel
     * @return the button
     */
    private JButton makeGroupSwitchButton(String text, ProductGroupAddPanel panelToShow) {
        JButton button = new JButton(text);
        button.addActionListener(a -> {
            root.showGroupAddPanel(panelToShow);
        });
        button.setBackground(new Color(178, 255, 227));
        button.setBorder(buttonBorder);
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Make a select button for a product.
     * @param productFields fields of the product as hashmap
     * @return the button
     */
    private JButton makeProductButton(HashMap<String, Object> productFields) {
        JButton button = new JButton(productFields.get("name").toString());
        button.setBackground(new Color(245, 178, 255));
        button.setFocusPainted(false);

        button.addActionListener(a -> selectProduct(button, productFields));
        button.setBorder(buttonBorder);
        return button;
    }

    private void selectProduct(JButton productButton, HashMap<String, Object> productFields) {
        if(productButton.equals(selectedProduct)) {
            root.addToTab(productFields);
        } else {
            if(selectedProduct != null) selectedProduct.setBorder(buttonBorder);

            // TODO: Display current tab amount
            productButton.setBorder(new LineBorder(new Color(255, 0, 89), 3));
            selectedProduct = productButton;
        }
    }
}
