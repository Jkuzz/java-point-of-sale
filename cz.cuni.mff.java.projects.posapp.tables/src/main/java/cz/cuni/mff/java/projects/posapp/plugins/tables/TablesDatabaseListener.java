package cz.cuni.mff.java.projects.posapp.plugins.tables;

import java.util.ArrayList;

public class TablesDatabaseListener implements TableChangeListener {

    @Override
    public void notify(String eventType, Table table) {
    }

    /**
     * Notify the listener of an event affecting multiple tables
     *
     * @param eventType event type
     * @param tables    affected by event
     */
    @Override
    public void notify(String eventType, ArrayList<Table> tables) {
        // TODO: Save new table to database
        System.out.println("Saving tables to database:");
        tables.forEach(System.out::println);
    }
}
