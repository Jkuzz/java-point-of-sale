package cz.cuni.mff.java.projects.posapp.plugins.tables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * MouseAdapter that handles drawing rectangles onto the tables edit canvas.
 */
public class RectangleInputAdapter extends MouseAdapter implements KeyListener {

    private boolean drawing = false;
    private boolean drawingInteract = false;
    private Point drawStart;

    private final JPanel drawRectangle = new JPanel();
    private final TablesModel tablesModel;
    private final JLayeredPane canvasPanel;


    /**
     * Create the adapter for the specific canvas. Informs the tablesModel to notify
     * of drawing and completed input events
     * @param canvasPanel to draw onto
     * @param tablesModel to notify of canvas changes
     */
    public RectangleInputAdapter(JLayeredPane canvasPanel, TablesModel tablesModel) {
        this.tablesModel = tablesModel;
        this.canvasPanel = canvasPanel;
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() != MouseEvent.BUTTON1
            && mouseEvent.getButton() != MouseEvent.BUTTON3) return;

        canvasPanel.remove(drawRectangle);
        drawing = false;
        tablesModel.addTable(drawRectangle.getBounds(), drawingInteract);
        drawingInteract = false;
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if(!SwingUtilities.isLeftMouseButton(mouseEvent)
            && !SwingUtilities.isRightMouseButton(mouseEvent)) return;
        if(!drawing) {
            drawing = true;
            drawStart = mouseEvent.getPoint();
            if(SwingUtilities.isRightMouseButton(mouseEvent)) {
                drawingInteract = true;
                drawRectangle.setBackground(tablesModel.interactColor);
            } else {
                drawRectangle.setBackground(tablesModel.baseColor);
            }
            canvasPanel.add(drawRectangle);
            canvasPanel.moveToFront(drawRectangle);
            drawRectangle.setBounds(drawStart.x, drawStart.y, 1, 1);
        }

        Point currentPoint = mouseEvent.getPoint();
        int rectX = Math.min(currentPoint.x, drawStart.x);
        int rectY = Math.min(currentPoint.y, drawStart.y);

        drawRectangle.setBounds(
                rectX, rectY,
                Math.abs(currentPoint.x - drawStart.x),
                Math.abs(currentPoint.y - drawStart.y)
        );
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
