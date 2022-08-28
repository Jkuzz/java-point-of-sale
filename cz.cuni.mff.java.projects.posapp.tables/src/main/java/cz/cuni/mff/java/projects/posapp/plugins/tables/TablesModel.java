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
        System.out.println("New table added: ");
        System.out.println(newTable);
        Table table = new Table(newTable, interact, interact ? interactColor : baseColor);
        tables.add(table);
        notify("tableAdded", table);
    }


    public void addTable(Point centre, double radius, boolean interact) {
        System.out.println("New table added: ");
        System.out.println(centre + ": " + radius);
        // TODO: Notify observers
    }



    public void removeTable() {

    }


    public void saveTables() {

    }
}
