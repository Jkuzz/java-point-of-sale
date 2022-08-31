package cz.cuni.mff.java.projects.posapp.core;

import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


/**
 * Sidebar for the main application. Generates and holds plugin buttons for each discovered plugin.
 */
public class SidebarPanel extends JPanel {

    /**
     * Create the sidebar for the application. Holds plugin-switching buttons.
     * @param buttonPlugins list of discovered plugins to make buttons for
     */
    public SidebarPanel(ArrayList<POSPlugin> buttonPlugins) {
        this.setLayout(new GridBagLayout());
        this.setBackground(App.getColor("primary"));
        this.add(makeModuleButtons(buttonPlugins));
    }


    /**
     * Create buttons for the discovered button-requiring plugins.
     * Plugins use the plugins class name to request panel change in the root App class.
     * @param buttonPlugins list of all plugins to display in sidebar
     * @return JPanel containing the buttons
     */
    private JPanel makeModuleButtons(ArrayList<POSPlugin> buttonPlugins) {
        JPanel moduleButtonsPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        moduleButtonsPanel.setOpaque(false);
        moduleButtonsPanel.setMinimumSize(new Dimension(500,500));

        for(POSPlugin plugin : buttonPlugins) {
            JButton moduleButton = makeModuleButton(plugin.getDisplayName());

            moduleButton.addActionListener(actionEvent -> App.switchActivePlugin(plugin.getClass().getName()));
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

        btn.setBackground(App.getColor("button"));
        btn.setForeground(App.getColor("button-text"));
        btn.setFont(new Font("Sans", Font.PLAIN, 18));
        btn.setPreferredSize(new Dimension(300, 50));
        return btn;
    }
}
