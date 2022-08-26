package cz.cuni.mff.java.projects.posapp.plugins.products;

import cz.cuni.mff.java.projects.posapp.database.*;
import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
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

        modulePanel.setBackground(new Color(166, 160, 94));
        modulePanel.add(makeHeader(), gbc);

        gbc.weighty = 1;
        modulePanel.add(productsPanel, gbc);
        modulePanel.add(newProductPanel, gbc);
        newProductPanel.setEnabled(false);
        newProductPanel.setVisible(false);
    }


    private JPanel makeHeader() {
        JPanel header = new JPanel(new GridBagLayout());;
        header.setBackground(new Color(132, 213, 213));

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

        tableButton.addActionListener(e -> setActivePanel(productsPanel));
        newProductButton.addActionListener(e -> setActivePanel(newProductPanel));

        panel.add(tableButton, gbc);
        panel.add(newProductButton, gbc);
        return panel;
    }


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


    private JPanel makeProductsPanel() {
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


    static class ProductsTableModel extends AbstractTableModel {

        private final ArrayList<Object[]> data;
        private final ArrayList<String> columnNames;

        public ProductsTableModel(Database db) {
            this.columnNames = new ArrayList<>();
            this.data = new ArrayList<>();
            ResultSet resultSet = db.query("SELECT * FROM products");
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

    private JPanel makeNewProductPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(175, 175, 175));

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


    private PreparedStatement prepareInsertStatement() throws SQLException {
        return db.prepareStatement(
                "INSERT INTO `products` (`price`, `name`, `id`) VALUES (?, ?, NULL);"
        );
    }


    private void insertNewProduct(HashMap<String, JTextField> userInputs) {
        try(PreparedStatement preparedStatement = prepareInsertStatement()){
            int priceInput = Integer.parseInt(userInputs.get("price").getText());
            preparedStatement.setInt(1, priceInput);
            preparedStatement.setString(2, userInputs.get("name").getText());
            preparedStatement.execute();
        } catch(SQLException e) {
            displayDBError(e.getMessage());
        } catch (NumberFormatException e) {
            displayDBError("Invalid integer field input");
        }
    }

    private void displayDBError(String message) {
        // TODO: Report database connection error
        System.out.println("ERROR: " + message);
    }
}
