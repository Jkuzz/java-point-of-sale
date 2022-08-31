package cz.cuni.mff.java.projects.posapp.plugins.products;

import cz.cuni.mff.java.projects.posapp.database.DBClient;
import cz.cuni.mff.java.projects.posapp.database.Database;
import cz.cuni.mff.java.projects.posapp.database.DevUser;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


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

    private final ArrayList<Object[]> data;
    private final ArrayList<String> columnNames;

    private final DBClient dbClient = new ProductsClient();
    private final Database db = Database.getInstance(new DevUser(), dbClient);


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
            "INSERT INTO `products` (`price`, `name`, `id`) VALUES (?, ?, NULL);"
        );
    }


    /**
     * Process input text fields into prepared query and send to database.
     * Add the new product to the table model.
     * @param userInputs field name: input textField
     */
    public void insertNewProduct(HashMap<String, JTextField> userInputs) throws SQLException {
        ResultSet rs = db.query("""
                SELECT AUTO_INCREMENT
                FROM information_schema.tables
                WHERE table_name = 'products'""");

        rs.next();
        int nextId = rs.getInt(1);

        PreparedStatement preparedStatement = prepareInsertStatement();
        int priceInput = Integer.parseInt(userInputs.get("price").getText());
        preparedStatement.setInt(1, priceInput);
        preparedStatement.setString(2, userInputs.get("name").getText());
        preparedStatement.execute();

        data.add(new Object[]{priceInput, userInputs.get("name").getText(), nextId});
        fireTableDataChanged();
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