package cz.cuni.mff.java.projects.posapp.plugins.inventory;

import cz.cuni.mff.java.projects.posapp.core.App;
import cz.cuni.mff.java.projects.posapp.database.DBClient;
import cz.cuni.mff.java.projects.posapp.database.Database;
import cz.cuni.mff.java.projects.posapp.database.DevUser;
import cz.cuni.mff.java.projects.posapp.plugins.DefaultComponentFactory;
import cz.cuni.mff.java.projects.posapp.plugins.SwapPanelPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Plugin provides the inventory management and display functionality.
 */
public class Inventory extends SwapPanelPlugin {

    Database db;
    private final JPanel modulePanel = new JPanel(new GridBagLayout());

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
            InventoryTableModel.getInstance(db).changeInventoryStatus((ArrayList<HashMap<String, Object>>) payload);
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

        HashMap<String, ActionListener> headerButtonDefs = new HashMap<>();
        headerButtonDefs.put("Inventory", a -> setActivePanel(modulePanel, makeInventoryPanel()));
        headerButtonDefs.put("Add new", a -> setActivePanel(modulePanel, makeInventoryFillPanel()));

        JPanel headerPanel = DefaultComponentFactory.makeHeader(
                getDisplayName(), new Color(255, 160, 100), headerButtonDefs
        );
        modulePanel.add(headerPanel, gbc);

        setActivePanel(modulePanel, makeInventoryPanel());
    }


    /**
     * Create panel for the display of products inventory.
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


    /**
     * Create panel for adding products to the inventory.
     * @return the products panel
     */
    private JPanel makeInventoryFillPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(App.getColor("tertiary"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 50;
        gbc.ipady = 30;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridx = 0;

        gbc.insets = new Insets(20, 80, 20, 40);
        gbc.weightx = 0.2;
        JLabel label1 = new JLabel("Select product");
        label1.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(label1, gbc);

        gbc.insets = new Insets(20, 40, 20, 80);
        gbc.weightx = 1;
        gbc.gridx = 1;
        JComboBox<ProductComboBoxItem> inputComboBox = new JComboBox<>();
        getProducts().forEach(inputComboBox::addItem);
        panel.add(inputComboBox, gbc);

        gbc.insets = new Insets(20, 80, 20, 40);
        gbc.weightx = 0.2;
        gbc.gridx = 0;
        JLabel label2 = new JLabel("Add");
        label2.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(label2, gbc);

        gbc.insets = new Insets(20, 40, 20, 80);
        gbc.weightx = 1;
        gbc.gridx = 1;
        JTextField amountInput = new JTextField();
        amountInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if(!Character.isDigit(e.getKeyChar()) && !(amountInput.getText().length() == 0 && e.getKeyChar() == '-')) {
                    e.consume();
                }
            }
        });
        panel.add(amountInput, gbc);

        JButton confirmButton = new JButton("Add to Inventory");
        confirmButton.setFocusPainted(false);
        confirmButton.addActionListener(a -> addToInventory(inputComboBox, amountInput));
        panel.add(confirmButton, gbc);

        return panel;
    }


    /**
     * Add the inputted product to the database and Inventory table model
     * @param inputComboBox comboBox containing the products
     * @param amountInput amount textFields containing only valid numeric inputs
     */
    private void addToInventory(JComboBox<ProductComboBoxItem> inputComboBox, JTextField amountInput) {
        if(amountInput.getText().length() == 0) return;

        int productId = inputComboBox.getItemAt(inputComboBox.getSelectedIndex()).getId();
        int delta = Integer.parseInt(amountInput.getText());

        System.out.println("Adding product stock: " + productId + ": " + delta);
        // TODO: Add to Database
        if(true) { //TODO: if database insert successful
            // TODO: Add to Inventory table model
            inputComboBox.setSelectedItem(0);
            amountInput.setText("");
        }
    }


    /**
     * Fetch the product names and ids from the database.
     * @return list of products as ProductComboBoxItems
     */
    private ArrayList<ProductComboBoxItem> getProducts() {
        ArrayList<ProductComboBoxItem> products = new ArrayList<>();
        ResultSet rs = db.query("SELECT id, name FROM products");
        try {
            while(rs.next()) {
                products.add(new ProductComboBoxItem(rs.getString(2), rs.getInt(1)));
            }
        } catch (SQLException ignored) {
        }
        return products;
    }
}
