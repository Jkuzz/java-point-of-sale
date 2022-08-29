package cz.cuni.mff.java.projects.posapp.plugins.tables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MoveInputAdapter extends MouseAdapter implements KeyListener {

    private final TablesModel tablesModel;
    private final JLayeredPane canvasPanel;

    private Table lastClickedTable;
    private Table draggedTable;
    private Point dragTableStart;
    private Point dragMouseStart;


    public MoveInputAdapter(JLayeredPane canvasPanel, TablesModel tablesModel) {
        this.canvasPanel = canvasPanel;
        this.tablesModel = tablesModel;

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        Table targetTable;
        try {
            targetTable = (Table) mouseEvent.getComponent().getComponentAt(mouseEvent.getPoint());
        } catch (ClassCastException e) {
            return;
        }

        // Stop dragging
        if(draggedTable != null) {
            selectTable(draggedTable);
            draggedTable = null;
        } else if (targetTable != null) {
            selectTable(targetTable);
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if(draggedTable == null) {
            try {
                draggedTable = (Table) mouseEvent.getComponent().getComponentAt(mouseEvent.getPoint());
            } catch (ClassCastException e) {
                return;
            }
            dragMouseStart = mouseEvent.getPoint();
            dragTableStart = draggedTable.getLocation();
            selectTable(draggedTable);
        }

        Point delta = new Point(
                mouseEvent.getPoint().x - dragMouseStart.x,
                mouseEvent.getPoint().y - dragMouseStart.y
        );
        draggedTable.setLocation(new Point(dragTableStart.x + delta.x, dragTableStart.y + delta.y));
    }


    private void selectTable(Table targetTable) {
        if (lastClickedTable != null && lastClickedTable != targetTable) {
            lastClickedTable.setSelected(false);
        }
        targetTable.setSelected(true);
        lastClickedTable = targetTable;
        if(targetTable.isSelected()) {
            canvasPanel.moveToFront(targetTable);
        }
    }

    private void deleteSelectedTable() {
        if(lastClickedTable != null && lastClickedTable.isSelected()) {
            canvasPanel.remove(lastClickedTable);
            canvasPanel.repaint();
        }
    }

    private void duplicateSelectedTable() {
        if(lastClickedTable != null && lastClickedTable.isSelected()) {
            Table duplicate = (Table) lastClickedTable.clone();
            canvasPanel.add(duplicate);
            canvasPanel.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_BACK_SPACE, KeyEvent.VK_DELETE -> deleteSelectedTable();
            case KeyEvent.VK_D -> duplicateSelectedTable();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }
}