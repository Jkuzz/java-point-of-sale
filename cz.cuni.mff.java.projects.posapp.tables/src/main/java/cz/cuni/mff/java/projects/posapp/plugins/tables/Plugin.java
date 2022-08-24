package cz.cuni.mff.java.projects.posapp.plugins.tables;

import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import java.awt.*;

public class Plugin implements POSPlugin {
    @Override
    public String getDisplayName() {
        return "Tables";
    }

    @Override
    public JPanel makeMainPanel() {
        JPanel modulePanel = new JPanel();
        modulePanel.add(new JLabel(getDisplayName()));
        modulePanel.setBackground(new Color(210, 210, 255));
        return modulePanel;
    }
}
