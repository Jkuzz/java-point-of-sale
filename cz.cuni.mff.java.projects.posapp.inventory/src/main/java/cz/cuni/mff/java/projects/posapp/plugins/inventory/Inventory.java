package cz.cuni.mff.java.projects.posapp.plugins.inventory;

import cz.cuni.mff.java.projects.posapp.core.App;
import cz.cuni.mff.java.projects.posapp.database.DBClient;
import cz.cuni.mff.java.projects.posapp.database.Database;
import cz.cuni.mff.java.projects.posapp.database.DevUser;
import cz.cuni.mff.java.projects.posapp.plugins.DefaultComponentFactory;
import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class Inventory implements POSPlugin{

    Database db;

    /**
     * Requests the database connection singleton object.
     */
    public Inventory() {
        DBClient dbClient = new InventoryClient();
        db = Database.getInstance(new DevUser(), dbClient);
    }

    @Override
    public String getDisplayName() {
        return "Inventory";
    }

    @Override
    public JPanel makeMainPanel() {
        JPanel modulePanel = new JPanel(new GridBagLayout());
        makeContent(modulePanel);
        return modulePanel;
    }

    /**
     * Interface method for receiving communication from other plugins.
     *
     * @param eventType string type of incoming event notification.
     * @param payload   object payload accompanying the message.
     */
    @Override
    public void message(String eventType, Object payload) {
        if("paySuccess".equals(eventType)) {
            InventoryTableModel.getInstance(db).changeInventoryStatus((HashMap<Integer, Integer>) payload);
        }
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

        JPanel inventoryPanel = makeInventoryPanel();

        HashMap<String, ActionListener> headerButtonDefs = new HashMap<>();
        headerButtonDefs.put("Inventory", null);
        headerButtonDefs.put("Add new", null);

        JPanel headerPanel = DefaultComponentFactory.makeHeader(
                getDisplayName(), new Color(255, 160, 100), headerButtonDefs
        );
        modulePanel.add(headerPanel, gbc);

        gbc.weighty = 1;
        modulePanel.add(inventoryPanel, gbc);
    }


    /**
     * Create panel for the display of products.
     * @return the products panel
     */
    private JPanel makeInventoryPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(App.getColor("tertiary"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.weightx = 1;
        gbc.weighty = 1;

        JTable productsTable = new JTable(InventoryTableModel.getInstance(db));
        JScrollPane tableScrollPane = new JScrollPane(productsTable);

        panel.add(tableScrollPane, gbc);
        return panel;
    }
}
