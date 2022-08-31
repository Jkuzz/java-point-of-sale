package cz.cuni.mff.java.projects.posapp.plugins.products;

import cz.cuni.mff.java.projects.posapp.database.Database;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Table model for the products view table.
 * Requests all products from the database and stores them in memory.
 * Provides accessor methods to the received data.
 */
public class ProductsTableModel extends AbstractTableModel {

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