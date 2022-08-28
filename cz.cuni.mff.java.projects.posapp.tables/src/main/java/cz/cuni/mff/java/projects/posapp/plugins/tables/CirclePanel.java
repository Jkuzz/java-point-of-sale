package cz.cuni.mff.java.projects.posapp.plugins.tables;

import javax.swing.*;
import java.awt.*;

public class CirclePanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        g.fillOval(0, 0, g.getClipBounds().width, g.getClipBounds().height);
    }
}
