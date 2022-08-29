package cz.cuni.mff.java.projects.posapp.plugins.tables;

import javax.swing.*;

public class TableCanvasListener implements TableChangeListener {

    private final JLayeredPane canvasPanel;

    public TableCanvasListener(JLayeredPane canvasPanel) {
        this.canvasPanel = canvasPanel;
    }

    @Override
    public void notify(String eventType, Table table) {
        switch (eventType) {
            case "tableAdded" -> handleTableAdded(table);
            case "tableRemoved" -> handleTableRemoved(table);
        }
    }

    private void handleTableAdded(Table table) {
        canvasPanel.add(table);
        canvasPanel.moveToFront(table);
    }

    private void handleTableRemoved(Table table) {

    }
}
