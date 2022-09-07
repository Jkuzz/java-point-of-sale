package cz.cuni.mff.java.projects.posapp.plugins.tables;

import javax.swing.*;
import java.util.ArrayList;


public record TableViewListener(JLayeredPane viewCanvasPanel, Plugin plugin) implements TableChangeListener {

    @Override
    public void notify(String eventType, Table table) {
    }

    @Override
    public void notify(String eventType, ArrayList<Table> tables) {
        switch (eventType) {
            case "tablesSaved", "tablesLoaded" -> handleTablesSaved(tables);
        }
    }

    /**
     * Display the newly saved tables state in the panel.
     * Removes all previous container children first.
     * Also works with loading tables.
     * @param tables to add to view
     */
    private void handleTablesSaved(ArrayList<Table> tables) {
        viewCanvasPanel.removeAll();
        tables.forEach(table -> {
            Table clonedTable = (Table) table.clone();
            if(table.isInteractable()) {
                clonedTable.addMouseListener(new TableMouseListener(clonedTable, plugin));
            }
            viewCanvasPanel.add(clonedTable);
            viewCanvasPanel.moveToFront(clonedTable);
        });
    }
}
