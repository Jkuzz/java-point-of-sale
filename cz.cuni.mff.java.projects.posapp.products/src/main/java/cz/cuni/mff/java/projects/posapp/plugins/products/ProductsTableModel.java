package cz.cuni.mff.java.projects.posapp.plugins.products;

import cz.cuni.mff.java.projects.posapp.database.Database;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


/**
 * Table model for the products view table, implemented as Singleton.
 * Requests all products from the database and stores them in memory.
 * Provides accessor methods to the received data.
 */
public class ProductsTableModel extends AbstractTableModel {

    /**
     * get the Singleton product model instance.
     * @return the Singleton instance
     */
    public static ProductsTableModel getInstance() {
        if(instance == null) {
            instance = new ProductsTableModel();
        }
        return instance;
    }

    /**
     * Singleton Model instance
     */
    private static ProductsTableModel instance;

    /**
     * Content rows of the table model. Must be same width as columnNames
     */
    private final ArrayList<Object[]> data;

    /**
     * Names of columns of the table model
     */
    private final ArrayList<String> columnNames;

    /**
     * Database instance - connection to DB via Database module
     */
    private final Database db = Database.getInstance(new ProductsClient());


    /**
     * Private constructor for the Singleton object.
     * Loads existing objects from the Database into memory.
     */
    private ProductsTableModel() {
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


    /**
     * Prepare an SQL statement to insert a new product into the database.
     * @return the prepared statement
     * @throws SQLException if prepareStatement() failed
     */
    PreparedStatement prepareInsertStatement() throws SQLException {
        return db.prepareStatement(
            "INSERT INTO `products` (`price`, `name`, `id`, `group_id`) VALUES (?, ?, NULL, ?);"
        );
    }


    /**
     * Get all products that are present in the group as a Hashmap of fields in the model.
     * @param groupId id of the parent group.
     * @return list of products in that group, as a hashmap indexed by columns.
     */
    public ArrayList<HashMap<String, Object>> getGroupProducts(Integer groupId) {
        ArrayList<HashMap<String, Object>> groupProducts = new ArrayList<>();

        int groupIdIndex = columnNames.indexOf("group_id");

        for(Object[] row: data) {
            if(!Objects.equals(row[groupIdIndex], groupId)) continue;

            HashMap<String, Object> fields = new HashMap<>();
            for(int i = 0; i < row.length; i += 1) {
                fields.put(columnNames.get(i), row[i]);
            }
            groupProducts.add(fields);
        }
        return groupProducts;
    }


    /**
     * Process input text fields into prepared query and send to database.
     * Add the new product to the table model.
     * @param userInputs field name: input textField
     * @throws SQLException when database query/connection fails
     */
    public void insertNewProduct(HashMap<String, ProductInputComponent> userInputs) throws SQLException {
        ResultSet rs = db.query("""
                SELECT AUTO_INCREMENT
                FROM information_schema.tables
                WHERE table_name = 'products'""");

        rs.next();
        int nextId = rs.getInt(1);

        PreparedStatement preparedStatement = prepareInsertStatement();
        int priceInput = Integer.parseInt(userInputs.get("price").getTextInput().getText());
        preparedStatement.setInt(1, priceInput);

        String nameInput = userInputs.get("name").getTextInput().getText();
        preparedStatement.setString(2, nameInput);

        JComboBox<GroupComboBoxItem> groupIdInput = userInputs.get("group_id").getComboInput();
        Integer groupId = groupIdInput.getItemAt(groupIdInput.getSelectedIndex()).getId();
        if(groupId == null) {
            preparedStatement.setNull(3, Types.INTEGER);
        } else {
            preparedStatement.setInt(3, groupId);
        }
        preparedStatement.execute();

        HashMap<String, Object> newData = new HashMap<>();
        newData.put("price", priceInput);
        newData.put("name", nameInput);
        newData.put("group_id", groupId);
        newData.put("id", nextId);
        addToData(newData);
        fireTableDataChanged();
    }


    /**
     * Add new data row to the table model. Adds to the correct columns.
     * @param fields to add. Must contain the same columns as model.
     */
    private void addToData(HashMap<String, Object> fields) {
        Object[] newRow = new Object[getColumnCount()];
        fields.forEach((key, val) -> {
            int keyIndex = columnNames.indexOf(key);
            newRow[keyIndex] = val;
        });
        data.add(newRow);
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