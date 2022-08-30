package cz.cuni.mff.java.projects.posapp.plugins.tables;

import javax.swing.*;
import java.util.ArrayList;

public record TableEditorListener(JLayeredPane canvasPanel) implements TableChangeListener {

    @Override
    public void notify(String eventType, Table table) {
        switch (eventType) {
            case "tableAdded" -> handleTableAdded(table);
            case "tableRemoved" -> handleTableRemoved(table);
        }
    }

    @Override
    public void notify(String eventType, ArrayList<Table> tables) {
        if("tablesLoaded".equals(eventType)) {
            handleTablesLoaded(tables);
        }
    }

    /**
     * Handle adding new table to the editor canvas panel
     * @param table to add
     */
    private void handleTableAdded(Table table) {
        canvasPanel.add(table);
        canvasPanel.moveToFront(table);
    }


    /**
     * Handle removing table from the editor canvas panel
     * @param table to remove
     */
    private void handleTableRemoved(Table table) {
        canvasPanel.remove(table);
        canvasPanel.repaint();
    }

    /**
     * Handle loading existing tables by adding them all to the editor panel.
     * @param tables to add
     */
    private void handleTablesLoaded(ArrayList<Table> tables) {
        tables.forEach(this::handleTableAdded);
    }
}
