package cz.cuni.mff.java.projects.posapp.plugins.tables;

import cz.cuni.mff.java.projects.posapp.core.Pair;
import cz.cuni.mff.java.projects.posapp.database.DBClient;
import cz.cuni.mff.java.projects.posapp.database.Database;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Holds the table data and dispatches events upon data change to subscribed observers.
 */
public class TablesModel extends TableEventPublisher {

    /**
     * Colour of static tables that are NOT interactable
     */
    public final Color baseColor = new Color(30, 30, 30);

    /**
     * Colour of tables that are interactable
     */
    public final Color interactColor = new Color(255, 122, 122);

    private final ArrayList<Table> tables = new ArrayList<>();
    private int nextId = 0;


    /**
     * Auto-increments table ID for new tables for DB primary keys.
     * Doesn't use mySQL AUTO_INCREMENT because it only needs to be session-unique
     * @return nex available table id
     */
    private int getNextId() {
        return nextId++;
    }


    /**
     * Create the model holding data about the tables.
     * Will notify subscribed listeners to changes to the table model.
     * @param operations events that will be subscribed to and notified.
     */
    public TablesModel(String... operations) {
        super(operations);
    }


    /**
     * Create a new table and add it to the model.
     * Model notifies observers of change in model.
     * @param newTable Bounds of to add
     * @param interact whether new table is interactive
     */
    public void addTable(Rectangle newTable, boolean interact) {
        Table table = new Table(newTable, interact, interact ? interactColor : baseColor, getNextId());
        addTable(table);
    }


    /**
     * Add an instantiated Table to the model. Use mainly when cloning an existing table.
     * Model notifies observers of change in model.
     * @param newTable table to add to model
     */
    public void addTable(Table newTable) {
        tables.add(newTable);
        notify("tableAdded", newTable);
    }


//    public void addTable(Point centre, double radius, boolean interact) {
//        System.out.println("New table added: ");
//        System.out.println(centre + ": " + radius);
//        Table table = new Table(centre, radius, interact, interact ? interactColor : baseColor);
//        tables.add(table);
//        notify("tableAdded", table);
//    }


    /**
     * Remove a Table from the data model.
     * Model notifies observers of change in model.
     * @param table to be removed
     */
    public void removeTable(Table table) {
        tables.remove(table);
        notify("tableRemoved", table);
    }


    /**
     * Notify observers of new tables save.
     */
    public void saveTables() {
        notify("tablesSaved", tables);
    }


    /**
     * Clone the table using the Prototype pattern
     * @param cloneMe to clone
     * @return the clone
     */
    public Table cloneTable(Table cloneMe) {
        Rectangle newBounds = cloneMe.getBounds();
        newBounds.translate(20, 20);
        return new Table(newBounds, cloneMe.isInteractable(), cloneMe.getBackground(), getNextId());
    }


    /**
     * Load existing saved tables from the database.
     */
    public void loadTables() {
        DBClient dbClient = new TablesDBClient();
        Database database = Database.getInstance(dbClient);
        ResultSet resultSet = database.query("SELECT * FROM tables");

        ArrayList<Pair<Integer, Table>> dbTables = new ArrayList<>();

        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsCount = rsmd.getColumnCount();
            List<String> cols = new ArrayList<>();
            for (int i = 1; i <= columnsCount; i += 1) {
                cols.add(rsmd.getColumnName(i));
            }

            while(resultSet.next()) {
                Object[] row = new Object[columnsCount];
                for(int i = 1; i <= columnsCount; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }

                int x = (int) row[cols.indexOf("x")];
                int y = (int) row[cols.indexOf("y")];
                int z = (int) row[cols.indexOf("z")];
                int width = (int) row[cols.indexOf("width")];
                int height = (int) row[cols.indexOf("height")];
                int id = (int) row[cols.indexOf("id")];
                boolean interact = (boolean) row[cols.indexOf("interact")];

                nextId = Math.max(nextId, id) + 1;  // Move nextId to highest saved id

                Rectangle newTableBounds = new Rectangle(x, y, width, height);
                Table newTable = new Table(newTableBounds, interact, interact ? interactColor : baseColor, id);
                dbTables.add(new Pair<>(z, newTable));  // Add to z position to maintain vertical ordering
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbTables.sort((a, b) -> b.getA() - a.getA());
        tables.addAll(dbTables.stream().map(Pair::getB).toList());
        notify("tablesLoaded", tables);
    }
}
