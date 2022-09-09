//package cz.cuni.mff.java.projects.posapp.plugins.tables;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class CirclePanel extends JPanel {
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        int radius = g.getClipBounds().width;
//
//        Graphics2D graphics = (Graphics2D) g;
//        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        graphics.setColor(getBackground());
//        graphics.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius); //paint background
//        graphics.setColor(getForeground());
//
////        g.fillOval(0, 0, g.getClipBounds().width, g.getClipBounds().height);
//        this.setOpaque(false);
//        super.paintComponent(g);
//    }
//}
