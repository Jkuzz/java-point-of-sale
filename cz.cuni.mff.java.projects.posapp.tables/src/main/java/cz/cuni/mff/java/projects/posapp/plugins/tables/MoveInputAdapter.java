package cz.cuni.mff.java.projects.posapp.plugins.tables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * MouseAdapter that handles selecting, moving, deleting and duplicating objects in the canvas.
 */
public class MoveInputAdapter extends MouseAdapter implements KeyListener {

    private final TablesModel tablesModel;
    private final JLayeredPane canvasPanel;

    private Table lastClickedTable;
    private Table draggedTable;
    private Point dragTableStart;
    private Point dragMouseStart;


    /**
     * Create the adapter for the specific canvas. Informs the tablesModel to notify
     * of drawing and completed input events
     * @param canvasPanel to draw onto
     * @param tablesModel to notify of canvas changes
     */
    public MoveInputAdapter(JLayeredPane canvasPanel, TablesModel tablesModel) {
        this.canvasPanel = canvasPanel;
        this.tablesModel = tablesModel;

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    /**
     * Select clicked panel or finish dragging.
     * If a panel is dragged outside the canvas, delete it.
     * @param mouseEvent MouseEvent
     */
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
            if(!draggedTable.getBounds().intersects(canvasPanel.getBounds())) {
                deleteSelectedTable();
            }
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
            tablesModel.removeTable(lastClickedTable);
            lastClickedTable = null;
        }
    }

    private void duplicateSelectedTable() {
        if(lastClickedTable != null && lastClickedTable.isSelected()) {
            Table duplicate =tablesModel.cloneTable(lastClickedTable);
            tablesModel.addTable(duplicate);
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
