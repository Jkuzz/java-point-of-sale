package cz.cuni.mff.java.projects.posapp.core;

import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


/**
 * Sidebar for the main application. Generates and holds plugin buttons for each discovered plugin.
 */
public class SidebarPanel extends JPanel {

    public SidebarPanel(MainViewPanel mainViewPanel, ArrayList<POSPlugin> buttonPlugins) {
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(50, 50, 50));
        this.add(makeModuleButtons(mainViewPanel, buttonPlugins));
    }


    /**
     * Create buttons for the discovered button-requiring plugins.
     * @param mainViewPanel Main application panel. The plugins will create their panels here.
     * @param buttonPlugins list of all plugins to display in sidebar
     * @return JPanel containing the buttons
     */
    private JPanel makeModuleButtons(MainViewPanel mainViewPanel, ArrayList<POSPlugin> buttonPlugins) {
        JPanel moduleButtonsPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        moduleButtonsPanel.setOpaque(false);
        moduleButtonsPanel.setMinimumSize(new Dimension(500,500));

        for(POSPlugin plugin : buttonPlugins) {
            JButton moduleButton = makeModuleButton(plugin.getDisplayName());
            moduleButton.addActionListener(actionEvent -> {
                mainViewPanel.removeAll();
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1;
                gbc.weighty = 1;
                mainViewPanel.add(plugin.makeMainPanel(), gbc);
                mainViewPanel.revalidate();
            });
            moduleButtonsPanel.add(moduleButton);
        }
        return moduleButtonsPanel;
    }


    /**
     * Create a button for the sidebar.
     * @param text text to show on the button -- the name of the plugin
     * @return The created button
     */
    private JButton makeModuleButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);

        btn.setBackground(new Color(90, 90, 90));
        btn.setForeground(new Color(240, 240, 240));
        btn.setFont(new Font("Sans", Font.PLAIN, 18));
        btn.setPreferredSize(new Dimension(300, 50));
        return btn;
    }
}
