package cz.cuni.mff.java.projects.posapp.plugins;

import javax.swing.*;
import java.awt.*;

public abstract class SwapPanelPlugin implements POSPlugin {

    public JPanel getActivePanel() {
        return activePanel;
    }

    private JPanel activePanel;

    /**
     * Set the selected panel as the displayed panel (unless it is already active).
     * @param newActivePanel panel to display
     */
    public void setActivePanel(JPanel modulePanel, JPanel newActivePanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;
        gbc.weighty = 1;

        if(activePanel != null) {
            modulePanel.remove(activePanel);
        }
        modulePanel.add(newActivePanel, gbc);
        activePanel = newActivePanel;

        modulePanel.revalidate();
        modulePanel.repaint();
    }
}
