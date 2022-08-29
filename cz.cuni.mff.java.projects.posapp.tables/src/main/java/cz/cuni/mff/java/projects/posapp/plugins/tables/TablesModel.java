package cz.cuni.mff.java.projects.posapp.plugins.tables;

import java.awt.*;
import java.util.ArrayList;

public class TablesModel extends TableEventPublisher{

    public final Color baseColor = new Color(30, 30, 30);
    public final Color interactColor = new Color(255, 122, 122);

    private final ArrayList<Table> tables = new ArrayList<>();


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
        Table table = new Table(newTable, interact, interact ? interactColor : baseColor);
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
     * Load existing saved tables from the database.
     */
    public void loadTables() {
        // TODO: load existing tables from database.
    }
}
