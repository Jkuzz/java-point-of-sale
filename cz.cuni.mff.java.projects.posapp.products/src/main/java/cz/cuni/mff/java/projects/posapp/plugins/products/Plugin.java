package cz.cuni.mff.java.projects.posapp.plugins.products;

import cz.cuni.mff.java.projects.posapp.plugins.DefaultComponentFactory;
import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;


public class Plugin implements POSPlugin {

    private JPanel productsPanel;
    private JPanel newProductPanel;
    private JPanel newGroupPanel;
    private JPanel activePanel;


    private final PluginPanelFactory panelFactory = new PluginPanelFactory(this);
    final ProductsTableModel productsModel = ProductsTableModel.getInstance();

    /**
     * Requests the database connection singleton object.
     */
    public Plugin() {}

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

        productsPanel = panelFactory.makeProductsPanel();
        activePanel = productsPanel;
        newProductPanel = panelFactory.makeNewProductPanel();
        newGroupPanel = panelFactory.makeNewGroupPanel();

        HashMap<String, ActionListener> headerButtonDefs = new HashMap<>();
        headerButtonDefs.put("Products", e -> setActivePanel(productsPanel));
        headerButtonDefs.put("New product", e -> setActivePanel(newProductPanel));
        headerButtonDefs.put("New group", e -> setActivePanel(newGroupPanel));

        JPanel headerPanel = DefaultComponentFactory.makeHeader(
                getDisplayName(), new Color(132, 213, 213), headerButtonDefs
        );
        modulePanel.add(headerPanel, gbc);

        gbc.weighty = 1;
        modulePanel.add(productsPanel, gbc);
        modulePanel.add(newProductPanel, gbc);
        modulePanel.add(newGroupPanel, gbc);
        newGroupPanel.setEnabled(false);
        newGroupPanel.setVisible(false);
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
     * Request database insertion from Model. Display error in case of failure.
     * @param userInputs field name: input textField
     */
    void insertNewProduct(HashMap<String, JTextField> userInputs) {
        try {
            productsModel.insertNewProduct(userInputs);
        } catch(SQLException e) {
            displayMessage(e.getMessage(), new Color(255, 50, 50));
            return;
        } catch (NumberFormatException e) {
            displayMessage("Invalid integer field input!", new Color(255, 50, 50));
            return;
        }
        displayMessage("Product added successfully", new Color(17, 255, 0));
    }


    /**
     * Request database insertion from Model. Display error in case of failure.
     * @param userInputs field name: input textField
     */
    void insertNewProductGroup(HashMap<String, JTextField> userInputs) {
//        try {
//            productsModel.insertNewProduct(userInputs);
//        } catch(SQLException e) {
//            displayMessage(e.getMessage(), new Color(255, 50, 50));
//            return;
//        } catch (NumberFormatException e) {
//            displayMessage("Invalid integer field input!", new Color(255, 50, 50));
//            return;
//        }
        userInputs.forEach((col, input) -> System.out.println(col + ": " + input));
        displayMessage("Product Group added successfully", new Color(17, 255, 0));
    }


    /**
     * Create a message label and add it to the panel.
     * Set up a worker thread to dispose the label after some time.
     * @param message to be displayed
     * @param textColour colour of text to display
     */
    private void displayMessage(String message, Color textColour) {
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
        errorLabel.setForeground(textColour);

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
