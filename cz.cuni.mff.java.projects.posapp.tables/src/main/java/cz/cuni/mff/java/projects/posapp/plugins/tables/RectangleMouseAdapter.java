package cz.cuni.mff.java.projects.posapp.plugins.tables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RectangleMouseAdapter extends MouseAdapter {

    private boolean drawing = false;
    private Point drawStart;

    private final JPanel drawRectangle = new JPanel();
    private final JPanel canvasPanel;


    public RectangleMouseAdapter(JPanel canvasPanel) {
        this.canvasPanel = canvasPanel;
        canvasPanel.setLayout(null);
        canvasPanel.add(drawRectangle);
    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
//        if(mouseEvent.getButton() != MouseEvent.BUTTON1) return;

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() != MouseEvent.BUTTON1) return;
        drawing = false;
        drawRectangle.setVisible(false);
        drawRectangle.setEnabled(false);
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() != MouseEvent.BUTTON1) return;
        if(!drawing) {
            drawing = true;
            drawStart = mouseEvent.getPoint();

            drawRectangle.setBackground(new Color(30, 30, 30));
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
