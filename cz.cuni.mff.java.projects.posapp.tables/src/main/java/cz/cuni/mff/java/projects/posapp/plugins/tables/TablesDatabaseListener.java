package cz.cuni.mff.java.projects.posapp.plugins.tables;

import cz.cuni.mff.java.projects.posapp.database.DBClient;
import cz.cuni.mff.java.projects.posapp.database.Database;
import cz.cuni.mff.java.projects.posapp.database.DevUser;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TablesDatabaseListener implements TableChangeListener {

    private final DBClient dbClient = new TablesDBClient();
    private final Database database = Database.getInstance(dbClient);
    private final JLayeredPane canvasPanel;

    public TablesDatabaseListener(JLayeredPane canvasPanel) {
        this.canvasPanel = canvasPanel;
    }

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
        if("tablesSaved".equals(eventType)) {
            saveTables(tables);
        }
    }


    /**
     * Save the provided tables into the database using the established connection.
     * Replaces currently present tables with the new state of tables.
     * @param tables to save to database
     */
    private void saveTables(ArrayList<Table> tables) {
        ArrayList<String> transactionQueries = new ArrayList<>();
        transactionQueries.add("DELETE FROM tables;");
        StringBuilder queryBuilder = new StringBuilder(
                "INSERT INTO tables(id, x, y, z, width, height, interact) VALUES "
        );

        ArrayList<String> queryTables = new ArrayList<>();
        tables.forEach(table -> {
            Rectangle bounds = table.getBounds();
            int zIndex = canvasPanel.getPosition(table); // 0 = in front
            // (id, x, y, z, width, height, interact)
            queryTables.add("(" +
                    table.id + ", " +
                    bounds.x + ", " +
                    bounds.y + ", " +
                    zIndex + ", " +
                    bounds.width + ", " +
                    bounds.height + ", " +
                    table.isInteractable() + ")"
            );
        });
        queryBuilder.append(String.join(", ", queryTables))
                .append(";");
        System.out.println(queryBuilder);
        transactionQueries.add(queryBuilder.toString());
        database.doTransaction(transactionQueries);
    }
}
