package cz.cuni.mff.java.projects.posapp.plugins.products;

import cz.cuni.mff.java.projects.posapp.database.*;
import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;


public class Plugin implements POSPlugin {

    Database db;
    private JPanel productsPanel;
    private JPanel newProductPanel;

    public Plugin() {
        db = Database.getInstance(new DevUser(), new ProductsClient());
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
        newProductPanel = makeNewProductPanel();

        modulePanel.setBackground(new Color(166, 160, 94));
        modulePanel.add(makeHeader(), gbc);

        gbc.weighty = 1;
        modulePanel.add(productsPanel, gbc);
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

        panel.add(new JButton("Products"), gbc);
        panel.add(new JButton("Add new"), gbc);
        return panel;
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

        panel.add(productsTable, gbc);
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
                    System.out.println(rsmd.getColumnName(i));
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
        JPanel panel = new JPanel(new GridBagLayout());;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15, 20, 15, 10);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;

        return panel;
    }
}
