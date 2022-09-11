package cz.cuni.mff.java.projects.posapp.plugins.products;

import cz.cuni.mff.java.projects.posapp.plugins.DefaultComponentFactory;
import cz.cuni.mff.java.projects.posapp.plugins.SwapPanelPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.HashMap;


/**
 * Plugin providing management of the store's offered products.
 */
public class Plugin extends SwapPanelPlugin {

    private JPanel productsPanel;
    private final JPanel modulePanel = new JPanel(new GridBagLayout());


    final ProductsTableModel productsModel = ProductsTableModel.getInstance();
    final ProductGroupsModel groupsModel = ProductGroupsModel.getInstance();
    private final PluginPanelFactory panelFactory;

    /**
     * Requests the database connection singleton object.
     */
    public Plugin() {
        panelFactory = new PluginPanelFactory(this);
    }

    @Override
    public String getDisplayName() {
        return "Products";
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

    }


    /**
     * Create content for the provided module parent panel.
     * @param modulePanel panel to insert content into
     */
    private void makeContent(JPanel modulePanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;
        gbc.weighty = 0;

        productsPanel = panelFactory.makeProductsPanel();

        HashMap<String, ActionListener> headerButtonDefs = new HashMap<>();
        headerButtonDefs.put("Products", e -> setActivePanel(modulePanel, productsPanel));
        headerButtonDefs.put("New product", e -> setActivePanel(modulePanel, panelFactory.makeNewProductPanel()));
        headerButtonDefs.put("New group", e -> setActivePanel(modulePanel, panelFactory.makeNewGroupPanel()));

        JPanel headerPanel = DefaultComponentFactory.makeHeader(
                getDisplayName(), new Color(132, 213, 213), headerButtonDefs
        );
        modulePanel.add(headerPanel, gbc);

        setActivePanel(modulePanel, productsPanel);
    }


    /**
     * Request database insertion from Model. Display error in case of failure.
     * @param userInputs field name: input textField
     */
    void insertNewProduct(HashMap<String, ProductInputComponent> userInputs) {
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
    void insertNewProductGroup(HashMap<String, ProductInputComponent> userInputs) {
        try {
            groupsModel.insertNewGroup(userInputs);
        } catch (SQLException e) {
            displayMessage(e.getMessage(), new Color(255, 50, 50));
            return;
        } catch (InvalidParameterException e) {
            displayMessage("Enter a valid group!", new Color(255, 50, 50));
            return;
        }
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
        JPanel activePanel = getActivePanel();
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
