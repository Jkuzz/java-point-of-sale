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

        ResultSet resultSet = db.query("SELECT products.name, inventory.id, inventory.amount" +
                " FROM inventory INNER JOIN products ON inventory.id = products.id");
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
     * Change the status of a product's inventory.
     * Will add or remove from the product's inventory both in the app table and the database.
     * If the product id is not present in the database or table, it will be added.
     * @param changedItems list of object fields that were changed. Must contain fields "id", "amount", "name"
     */
    public void changeInventoryStatus(ArrayList<HashMap<String, Object>> changedItems) {
        int idIndex = columnNames.indexOf("id");
        int amountIndex = columnNames.indexOf("amount");
        int nameIndex = columnNames.indexOf("name");

        ArrayList<String> dbUpdates = new ArrayList<>();

        changedItems.forEach(item -> {
            Integer id = (Integer) item.get("id");
            Integer delta = (Integer) item.get("amount");
            String name = (String) item.get("name");

            if(id == null || delta == null || name == null) {
                return;
            }

            Optional<Object[]> row = data.stream()
                    .filter(r -> r[idIndex] == id)
                    .findFirst();
            if(row.isEmpty()) {
                Object[] newRow = new Object[getColumnCount()];
                newRow[idIndex] = id;
                newRow[amountIndex] = delta;
                newRow[nameIndex] = name;
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
