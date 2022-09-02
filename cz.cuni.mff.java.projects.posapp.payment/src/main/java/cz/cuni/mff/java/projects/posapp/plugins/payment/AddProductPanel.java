package cz.cuni.mff.java.projects.posapp.plugins.payment;

import cz.cuni.mff.java.projects.posapp.plugins.DefaultComponentFactory;
import cz.cuni.mff.java.projects.posapp.plugins.products.ProductGroupsModel;
import cz.cuni.mff.java.projects.posapp.plugins.products.ProductsTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;


public class AddProductPanel extends JPanel {

    private final ProductsTableModel productsModel = ProductsTableModel.getInstance();
    private final ProductGroupsModel groupsModel = ProductGroupsModel.getInstance();

    private final Tab targetTab;

    public AddProductPanel(Tab targetTab) {
        super(new GridBagLayout());
        this.targetTab = targetTab;
        groupsModel.getOrderedGroups().forEach(System.out::println);
        makeContent();
    }


    private void makeContent() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;
        gbc.weighty = 0;

        HashMap<String, ActionListener> buttonDefs = new HashMap<>();
        buttonDefs.put("Cancel", null );
        JPanel headerPanel = DefaultComponentFactory.makeHeader("Add to '" + targetTab.getTabName() + "'",
                new Color(200, 200, 100),buttonDefs);
        add(headerPanel, gbc);

        gbc.weighty = 1;
        add(makeAddPanel(), gbc);

    }

    private JPanel makeAddPanel() {
        JPanel addPanel = new JPanel(new GridBagLayout());

        return addPanel;
    }


}
