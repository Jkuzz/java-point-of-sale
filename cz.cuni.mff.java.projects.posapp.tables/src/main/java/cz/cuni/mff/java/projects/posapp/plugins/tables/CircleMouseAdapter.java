package cz.cuni.mff.java.projects.posapp.plugins.tables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class CircleMouseAdapter extends MouseAdapter {

    private boolean drawing = false;
    private boolean drawingInteract = false;
    private Point drawStart;

    private final CirclePanel drawCircle = new CirclePanel();
    private final TablesModel tablesModel;


    public CircleMouseAdapter(JPanel canvasPanel, TablesModel tablesModel) {
        this.tablesModel = tablesModel;
        drawCircle.setOpaque(false);
        canvasPanel.setLayout(null);
        canvasPanel.add(drawCircle);
    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() != MouseEvent.BUTTON1
            && mouseEvent.getButton() != MouseEvent.BUTTON3) return;

        drawing = false;
        tablesModel.addTable(drawCircle.getBounds(), drawingInteract);
        drawingInteract = false;
        drawCircle.setVisible(false);
        drawCircle.setEnabled(false);
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
                drawCircle.setBackground(new Color(255, 107, 107));
            } else {
                drawCircle.setBackground(new Color(30, 30, 30));
            }
            drawCircle.setBounds(drawStart.x, drawStart.y, 1, 1);
            drawCircle.setVisible(true);
            drawCircle.setEnabled(true);
        }

        Point currentPoint = mouseEvent.getPoint();
        int distX = Math.abs(currentPoint.x - drawStart.x);
        int distY = Math.abs(currentPoint.y - drawStart.y);
        int radius = (int)Math.sqrt( distX * distX + distY * distY);

        drawCircle.setBounds(
                drawStart.x - radius,
                drawStart.y - radius,
                radius * 2,
                radius * 2
        );
    }

}
