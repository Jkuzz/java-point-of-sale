package cz.cuni.mff.java.projects.posapp.plugins.inventory;

import cz.cuni.mff.java.projects.posapp.database.Database;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;


/**
 * Table model for the products view table.
 * Requests all products from the database and stores them in memory.
 * Provides accessor methods to the received data.
 * Implemented as a singleton
 */
public class InventoryTableModel extends AbstractTableModel {

    /**
     * Singleton instance retriever.
     * @param db database to retrieve items from.
     * @return the model singleton instance
     */
    public static InventoryTableModel getInstance(Database db) {
        if(instance == null) {
            instance = new InventoryTableModel(db);
        }
        return instance;
    }
    private static InventoryTableModel instance;

    private final Database db;
    private final ArrayList<Object[]> data;
    private final ArrayList<String> columnNames;

    private InventoryTableModel(Database db) {
        this.db = db;
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

    public void changeInventoryStatus(HashMap<Integer, Integer> changedItems) {
        int idIndex = columnNames.indexOf("id");
        int amountIndex = columnNames.indexOf("amount");

        ArrayList<String> dbUpdates = new ArrayList<>();

        changedItems.forEach((id, delta) -> {
            Optional<Object[]> row = data.stream()
                    .filter(r -> r[idIndex] == id)
                    .findFirst();
            if(row.isEmpty()) {
                Object[] newRow = new Object[getColumnCount()];
                newRow[idIndex] = id;
                newRow[amountIndex] = delta;
                data.add(newRow);
                dbUpdates.add("INSERT INTO inventory (id, amount) VALUES (" + id + ", " + delta +
                        ");");
            } else {
                row.get()[amountIndex] = (Integer) row.get()[amountIndex] + delta;
                dbUpdates.add("UPDATE inventory SET amount = " + row.get()[amountIndex] +
                        " WHERE id = " + id + ";");
            }
        });

        db.doTransaction(dbUpdates);
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
