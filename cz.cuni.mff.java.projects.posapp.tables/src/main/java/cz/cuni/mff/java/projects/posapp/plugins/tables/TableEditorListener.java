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
    }

    private void handleTableAdded(Table table) {
        canvasPanel.add(table);
        canvasPanel.moveToFront(table);
    }

    private void handleTableRemoved(Table table) {
        canvasPanel.remove(table);
        canvasPanel.repaint();
    }
}
