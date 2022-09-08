package cz.cuni.mff.java.projects.posapp.plugins;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract plugin base panel providing panel-swapping functionality.
 * Use with plugins that provide multiple windows/views and need to swap between them
 * usually with header buttons, or otherwise.
 */
public abstract class SwapPanelPlugin implements POSPlugin {

    /**
     * Get the panel, which should be the one currently visible, unless swapped using a method other than
     * by calling setActivePanel(). If another method is used, functionality will behave unexpectedly!
     * @return the currently active panel.
     */
    public JPanel getActivePanel() {
        return activePanel;
    }

    private JPanel activePanel;

    /**
     * Set the selected panel as the displayed panel (unless it is already active).
     * Removes the previously active panel.
     * @param modulePanel plugin's panel to switch the active panel into. Adds to the end.
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
