package cz.cuni.mff.java.projects.posapp.plugins.tables;

import cz.cuni.mff.java.projects.posapp.plugins.DefaultComponentFactory;
import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class Plugin implements POSPlugin {

//    Database db;
//    private final DBClient dbClient = new ProductsClient();


    /**
     * Requests the database connection singleton object.
     */
    public Plugin() {
//        db = Database.getInstance(new DevUser(), dbClient);
    }

    @Override
    public String getDisplayName() {
        return "Tables";
    }

    @Override
    public JPanel makeMainPanel() {
        JPanel modulePanel = new JPanel(new GridBagLayout());;
        makeContent(modulePanel);
        return modulePanel;
    }

    /**
     * Create content for the provided module parent panel.
     * @param modulePanel panel to insert content into
     */
    private void makeContent(JPanel modulePanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;
        gbc.weighty = 0;

        JPanel tablesPanel = makeTablesPanel();
//        activePanel = productsPanel;
//        newProductPanel = makeNewProductPanel();

        HashMap<String, ActionListener> headerButtonDefs = new HashMap<>();
        headerButtonDefs.put("Tables", null);
        headerButtonDefs.put("Edit", null);

        JPanel headerPanel = DefaultComponentFactory.makeHeader(
                getDisplayName(), new Color(177, 123, 255), headerButtonDefs
        );
        modulePanel.add(headerPanel, gbc);

        gbc.weighty = 1;
        modulePanel.add(tablesPanel, gbc);
//        modulePanel.add(newProductPanel, gbc);
//        newProductPanel.setEnabled(false);
//        newProductPanel.setVisible(false);
    }


    /**
     * Create panel for the display of products.
     * @return the products panel
     */
    private JPanel makeTablesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(175, 175, 175));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.weightx = 1;
        gbc.weighty = 1;

//        JTable productsTable = new JTable(new ProductsTableModel(db));
//        JScrollPane tableScrollPane = new JScrollPane(productsTable);

//        panel.add(tableScrollPane, gbc);
        return panel;
    }
}
