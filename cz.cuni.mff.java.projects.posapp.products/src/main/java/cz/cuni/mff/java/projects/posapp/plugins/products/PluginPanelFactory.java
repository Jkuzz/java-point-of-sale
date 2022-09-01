package cz.cuni.mff.java.projects.posapp.plugins.products;

import cz.cuni.mff.java.projects.posapp.core.App;
import cz.cuni.mff.java.projects.posapp.core.Pair;
import cz.cuni.mff.java.projects.posapp.database.DBClient;
import cz.cuni.mff.java.projects.posapp.database.DBTableDef;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;


public class PluginPanelFactory {

    private final Plugin target;
    final DBClient dbClient = new ProductsClient();
    private final ProductGroupsModel productGroupsModel;

    public PluginPanelFactory(Plugin target) {
        this.target = target;
        productGroupsModel = new ProductGroupsModel();
    }

    /**
     * Create a panel for adding a new product group.
     * @return the panel
     */
    JPanel makeNewGroupPanel() {
        return makeDBAddPanel(
                dbClient.getTableDefs().get("product_groups"),
                userInputs -> e -> target.insertNewProductGroup(userInputs)
        );
    }

    /**
     * Create a panel for inserting a new product.
     * Provides text-fields based on the db client table definitions.
     * @return the panel
     */
    JPanel makeNewProductPanel() {
        return makeDBAddPanel(
                dbClient.getTableDefs().get("products"),
                userInputs -> e -> target.insertNewProduct(userInputs)
        );
    }


    /**
     * Create a panel for inserting to database based on the tableDef.
     *
     * Foreign keys are created as a JComboBox filled with names of the foregin objects.
     * Primary key is omitted.
     *
     * Remainder are added as String or Integer input JTextFields.
     *
     * @param tableDef of table to insert into
     * @param makeActionListener function returning an ActionListener to bind to the confirm button.
     *                           Takes an array of the inputs as an argument.
     * @return the panel
     */
    JPanel makeDBAddPanel(DBTableDef tableDef, Function<HashMap<String, ProductInputComponent>, ActionListener> makeActionListener) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(App.getColor("tertiary"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15, 60, 15, 60);
        gbc.ipadx = 50;
        gbc.ipady = 15;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 0;

        final HashMap<String, ProductInputComponent> userInputs = new HashMap<>();
        final HashMap<String, Pair<String, String>>  foreignKeyDefs = tableDef.getForeignKeys();

        foreignKeyDefs.forEach((key, foreign) -> {
            ArrayList<GroupComboBoxItem> foreignKeys = productGroupsModel.getOrderedGroups();

            JComboBox<GroupComboBoxItem> foreignKeyInput = new JComboBox<>();
            foreignKeys.forEach(foreignKeyInput::addItem);

            addInputField(panel, key, foreignKeyInput, gbc);
            userInputs.put(key, new ProductInputComponent(foreignKeyInput));
        });

        tableDef.getTableSchema().forEach((name, type) -> {
            // Don't create input for primary key -> will be auto-incremented or added programmatically otherwise
            // Don't create input for foreign key -> was created by F-K join above
            if(name.equals(tableDef.getPrimaryKey()) || foreignKeyDefs.containsKey(name)) {
                return;
            }
            JTextField inputText = new JTextField();
            if(type.contains("INTEGER")) {
                inputText.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        super.keyTyped(e);
                        if(!Character.isDigit(e.getKeyChar())) {
                            e.consume();
                        }
                    }
                });
            }
            addInputField(panel, name, inputText, gbc);
            userInputs.put(name, new ProductInputComponent(inputText));
        });

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(makeActionListener.apply(userInputs));
        panel.add(confirmButton, gbc);

        return panel;
    }


    private void addInputField(JPanel parent, String label, Component input, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.weightx = 0.1;
        parent.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        parent.add(input, gbc);
    }

    /**
     * Create panel for the display of products.
     * @return the products panel
     */
    JPanel makeProductsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(App.getColor("tertiary"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.weightx = 1;
        gbc.weighty = 1;

        JTable productsTable = new JTable(target.productsModel);
        JScrollPane tableScrollPane = new JScrollPane(productsTable);

        panel.add(tableScrollPane, gbc);
        return panel;
    }
}
