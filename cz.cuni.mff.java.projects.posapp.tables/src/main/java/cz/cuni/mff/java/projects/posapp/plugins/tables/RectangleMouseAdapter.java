package cz.cuni.mff.java.projects.posapp.plugins.tables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class RectangleMouseAdapter extends MouseAdapter {

    private boolean drawing = false;
    private boolean drawingInteract = false;
    private Point drawStart;

    private final JPanel drawRectangle = new JPanel();
    private final TablesModel tablesModel;


    public RectangleMouseAdapter(JPanel canvasPanel, TablesModel tablesModel) {
        this.tablesModel = tablesModel;
        canvasPanel.add(drawRectangle);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() != MouseEvent.BUTTON1
            && mouseEvent.getButton() != MouseEvent.BUTTON3) return;

        drawing = false;
        tablesModel.addTable(drawRectangle.getBounds(), drawingInteract);
        drawingInteract = false;
        drawRectangle.setVisible(false);
        drawRectangle.setEnabled(false);
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
            drawRectangle.setBounds(drawStart.x, drawStart.y, 1, 1);
            drawRectangle.setVisible(true);
            drawRectangle.setEnabled(true);
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
}
