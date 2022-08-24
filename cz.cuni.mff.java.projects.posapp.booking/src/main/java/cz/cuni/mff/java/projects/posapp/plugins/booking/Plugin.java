package cz.cuni.mff.java.projects.posapp.plugins.booking;

import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import java.awt.*;

public class Plugin implements POSPlugin {
    @Override
    public String getDisplayName() {
        return "Booking";
    }

    @Override
    public JPanel makeMainPanel() {
        JPanel modulePanel = new JPanel();
        modulePanel.add(new JLabel(getDisplayName()));
        modulePanel.setBackground(new Color(40, 158, 255));
        return modulePanel;
    }
}
