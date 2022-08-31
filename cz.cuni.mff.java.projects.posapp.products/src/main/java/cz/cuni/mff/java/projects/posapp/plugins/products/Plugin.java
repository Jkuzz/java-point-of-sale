package cz.cuni.mff.java.projects.posapp.plugins.products;

import cz.cuni.mff.java.projects.posapp.core.App;
import cz.cuni.mff.java.projects.posapp.database.*;
import cz.cuni.mff.java.projects.posapp.plugins.DefaultComponentFactory;
import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class Plugin implements POSPlugin {

    Database db;
    private JPanel productsPanel;
    private JPanel newProductPanel;
    private JPanel activePanel;

    private final DBClient dbClient = new ProductsClient();


    /**
     * Requests the database connection singleton object.
     */
    public Plugin() {
        db = Database.getInstance(new DevUser(), dbClient);
    }

    @Override
    public String getDisplayName() {
        return "Products";
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

        productsPanel = makeProductsPanel();
        activePanel = productsPanel;
        newProductPanel = makeNewProductPanel();

        HashMap<String, ActionListener> headerButtonDefs = new HashMap<>();
        headerButtonDefs.put("Products", e -> setActivePanel(productsPanel));
        headerButtonDefs.put("Add new", e -> setActivePanel(newProductPanel));

        JPanel headerPanel = DefaultComponentFactory.makeHeader(
                getDisplayName(), new Color(132, 213, 213), headerButtonDefs
        );
        modulePanel.add(headerPanel, gbc);

        gbc.weighty = 1;
        modulePanel.add(productsPanel, gbc);
        modulePanel.add(newProductPanel, gbc);
        newProductPanel.setEnabled(false);
        newProductPanel.setVisible(false);
    }


    /**
     * Set the selected panel as the displayed panel (unless it is already active).
     * @param newActivePanel panel to display
     */
    private void setActivePanel(JPanel newActivePanel) {
        if(newActivePanel == activePanel) {
            return;
        }
        activePanel.setEnabled(false);
        activePanel.setVisible(false);
        newActivePanel.setEnabled(true);
        newActivePanel.setVisible(true);
        activePanel = newActivePanel;
    }


    /**
     * Create panel for the display of products.
     * @return the products panel
     */
    private JPanel makeProductsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(App.getColor("tertiary"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.weightx = 1;
        gbc.weighty = 1;

        JTable productsTable = new JTable(new ProductsTableModel(db));
        JScrollPane tableScrollPane = new JScrollPane(productsTable);

        panel.add(tableScrollPane, gbc);
        return panel;
    }


    /**
     * Create a panel for inserting a new product.
     * Provides text-fields based on the db client table definitions.
     * @return the panel
     */
    private JPanel makeNewProductPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(App.getColor("tertiary"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15, 60, 15, 60);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 0;

        final HashMap<String, DBTableDef> tableDefs = dbClient.getTableDefs();
        final HashMap<String, JTextField> userInputs = new HashMap<>();
        final DBTableDef productsDef = tableDefs.get("products");


        productsDef.getTableSchema().forEach((name, type) -> {
            if(name.equals(productsDef.getPrimaryKey())) {
                return;
            }
            gbc.gridx = 0;
            gbc.weightx = 0.1;
            panel.add(new JLabel(name), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1;
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
            userInputs.put(name, inputText);
            panel.add(inputText, gbc);
        });

        JButton confirmButton = new JButton("Confirm");
        panel.add(confirmButton, gbc);

        confirmButton.addActionListener(e -> insertNewProduct(userInputs));

        return panel;
    }


    /**
     * Prepare an SQL statement to insert a new product into the database.
     * @return the prepared statement
     * @throws SQLException if prepareStatement() failed
     */
    private PreparedStatement prepareInsertStatement() throws SQLException {
        return db.prepareStatement(
                "INSERT INTO `products` (`price`, `name`, `id`) VALUES (?, ?, NULL);"
        );
    }


    /**
     * Process input text fields into prepared query and send to database.
     * @param userInputs field name: input textField
     */
    private void insertNewProduct(HashMap<String, JTextField> userInputs) {
        try(PreparedStatement preparedStatement = prepareInsertStatement()){
            int priceInput = Integer.parseInt(userInputs.get("price").getText());
            preparedStatement.setInt(1, priceInput);
            preparedStatement.setString(2, userInputs.get("name").getText());
            preparedStatement.execute();
        } catch(SQLException e) {
            displayDBError(e.getMessage());
        } catch (NumberFormatException e) {
            displayDBError("Invalid integer field input!");
        }
    }


    /**
     * Create an error label and add it to the panel.
     * Set up a worker thread to dispose the label after some time.
     * @param message to be displayed
     */
    private void displayDBError(String message) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 60, 15, 60);
        gbc.gridy = GridBagConstraints.PAGE_END;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 0;
        gbc.gridwidth = 2;

        JLabel errorLabel = new JLabel(message);
        activePanel.add(errorLabel, gbc);
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        errorLabel.setForeground(new Color(255, 50, 50));

        // The worker will disable the label later
        SwingWorker<Object, Object> hideErrorWorker = new SwingWorker<>(){
            @Override
            protected Integer doInBackground() throws Exception {
                Thread.sleep(10000);
                return null;
            }
            @Override
            protected void done() {
                super.done();
                errorLabel.setEnabled(false);
                errorLabel.setVisible(false);
                activePanel.remove(errorLabel);
            }
        };
        hideErrorWorker.execute();
    }
}
