package cz.cuni.mff.java.projects.posapp.plugins.tables;

import javax.swing.*;
import java.awt.*;

public class Table {

    public JPanel getPanel() {
        return panel;
    }

    private final JPanel panel;
    private boolean interactable;

    public boolean isInteractable() {
        return interactable;
    }

    public Table(Rectangle rectangle, boolean interact, Color bgColor) {
        panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setBounds(rectangle);
        interactable = interact;
    }



}
