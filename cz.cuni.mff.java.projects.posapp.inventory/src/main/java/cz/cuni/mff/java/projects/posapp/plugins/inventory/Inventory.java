package cz.cuni.mff.java.projects.posapp.plugins.inventory;

import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import java.awt.*;

public class Inventory implements POSPlugin{
    @Override
    public String getDisplayName() {
        return "Inventory";
    }

    @Override
    public JPanel makeMainPanel() {
        JPanel modulePanel = new JPanel();
        modulePanel.add(new JLabel(getDisplayName()));
        modulePanel.setBackground(new Color(210, 129, 33));
        return modulePanel;
    }
}
