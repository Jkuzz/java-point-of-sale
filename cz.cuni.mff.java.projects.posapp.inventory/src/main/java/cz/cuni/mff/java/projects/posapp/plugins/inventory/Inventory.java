package cz.cuni.mff.java.projects.posapp.plugins.inventory;

import cz.cuni.mff.java.projects.posapp.database.DBClient;
import cz.cuni.mff.java.projects.posapp.database.Database;
import cz.cuni.mff.java.projects.posapp.database.DevUser;
import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class Inventory implements POSPlugin{

    Database db;
    private final DBClient dbClient = new InventoryClient();


    /**
     * Requests the database connection singleton object.
     */
    public Inventory() {
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
//        activePanel = productsPanel;
//        newProductPanel = makeNewProductPanel();

        modulePanel.add(makeHeader(), gbc);

        gbc.weighty = 1;
        modulePanel.add(inventoryPanel, gbc);
//        modulePanel.add(newProductPanel, gbc);
//        newProductPanel.setEnabled(false);
//        newProductPanel.setVisible(false);
    }

    /**
     * Create the plugin header.
     * Includes the title and mode-switch buttons.
     * @return the header panel
     */
    private JPanel makeHeader() {
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(new Color(255, 160, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 10);
        gbc.ipadx = 20;
        gbc.ipady = 0;
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel headerLabel = new JLabel(getDisplayName());
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        header.add(headerLabel, gbc);

        JPanel headerButtonsPanel = makeHeaderButtonsPanel();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.LINE_END;
        header.add(headerButtonsPanel, gbc);
        return header;
    }

    /**
     * Create buttons for the header. The buttons enable switching of the currently displayed tab.
     * @return panel with buttons
     */
    private JPanel makeHeaderButtonsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 0;
        gbc.ipady = 0;
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;

        JButton tableButton = new JButton("Products");
        JButton newProductButton = new JButton("Add new");

//        tableButton.addActionListener(e -> setActivePanel(productsPanel));
//        newProductButton.addActionListener(e -> setActivePanel(newProductPanel));

        panel.add(tableButton, gbc);
        panel.add(newProductButton, gbc);
        return panel;
    }

    /**
     * Create panel for the display of products.
     * @return the products panel
     */
    private JPanel makeInventoryPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(175, 175, 175));

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
     * Table model for the products view table.
     * Requests all products from the database and stores them in memory.
     * Provides accessor methods to the received data.
     */
    static class ProductsTableModel extends AbstractTableModel {

        private final ArrayList<Object[]> data;
        private final ArrayList<String> columnNames;

        public ProductsTableModel(Database db) {
            this.columnNames = new ArrayList<>();
            this.data = new ArrayList<>();
            ResultSet resultSet = db.query("SELECT * FROM inventory");
            try {
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnsCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnsCount; i += 1) {
                    this.columnNames.add(rsmd.getColumnName(i));
                }
                while(resultSet.next()) {
                    Object[] row = new Object[columnsCount];
                    for(int i = 1; i <= columnsCount; i++) {
                        row[i - 1] = resultSet.getObject(i);
                    }
                    data.add(row);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.size();
        }

        @Override
        public String getColumnName(int col) {
            return columnNames.get(col);
        }

        @Override
        public Object getValueAt(int row, int col) {
            return data.get(row)[col];
        }
    }
}
