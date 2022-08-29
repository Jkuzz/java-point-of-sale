package cz.cuni.mff.java.projects.posapp.plugins.tables;

import javax.swing.*;
import java.util.ArrayList;

public record TableViewListener(JLayeredPane viewCanvasPanel) implements TableChangeListener {

    @Override
    public void notify(String eventType, Table table) {
    }

    @Override
    public void notify(String eventType, ArrayList<Table> tables) {
        if ("tablesSaved".equals(eventType)) {
            handleTablesSaved(tables);
        }
    }

    private void handleTablesSaved(ArrayList<Table> tables) {
        viewCanvasPanel.removeAll();
        tables.forEach(table -> {
            System.out.println(table);
            Table clonedTable = (Table) table.clone();
            viewCanvasPanel.add(clonedTable);
            viewCanvasPanel.moveToFront(clonedTable);
        });
    }
}
