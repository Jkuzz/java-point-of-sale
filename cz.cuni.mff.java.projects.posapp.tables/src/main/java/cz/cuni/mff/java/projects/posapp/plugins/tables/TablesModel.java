package cz.cuni.mff.java.projects.posapp.plugins.tables;

import java.awt.*;
import java.util.ArrayList;

public class TablesModel {    





    public ArrayList<Rectangle> getTables() {
        return null;
    }


    public void addTable(Rectangle newTable, boolean interact) {
        System.out.println("New table added: ");
        System.out.println(newTable);
        // TODO: Notify observers
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
