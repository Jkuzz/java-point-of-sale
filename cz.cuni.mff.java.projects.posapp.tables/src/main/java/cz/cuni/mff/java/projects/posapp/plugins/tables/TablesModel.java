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


    public ArrayList<Table> getTables() {
        return null;
    }


    public void addTable(Rectangle newTable, boolean interact) {
        Table table = new Table(newTable, interact, interact ? interactColor : baseColor);
        addTable(table);
    }

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


    public void removeTable(Table table) {
        tables.remove(table);
        notify("tableRemoved", table);
    }


    public void saveTables() {
        tables.forEach(table -> {
            Rectangle bounds = table.getBounds();
            System.out.println(bounds);
            // TODO: Save tables to database
        });
    }
}
