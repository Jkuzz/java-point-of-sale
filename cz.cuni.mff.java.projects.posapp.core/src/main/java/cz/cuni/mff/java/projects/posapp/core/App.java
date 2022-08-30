package cz.cuni.mff.java.projects.posapp.core;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * GUI application.
 */
public class App {

    private static final HashMap<String, JPanel> appPlugins = new HashMap<>();
    private static JPanel mainViewPanel;


    /**
     * Root app GUI creation method.
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Point of Sale");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PluginLoader pluginLoader = new PluginLoader();
        makePluginPanels(pluginLoader);
        frame.getContentPane().add(createContent(pluginLoader));

        frame.setMinimumSize(new Dimension(1200, 750));
        frame.pack();
        frame.setVisible(true);
    }


    /**
     * Create content for the application window.
     * @param pluginLoader that loaded plugins for the sidebar to display.
     * @return the content panel
     */
    private static JPanel createContent(PluginLoader pluginLoader) {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        contentPanel.setBackground(new Color(60, 60, 60));

        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15, 20, 15, 10);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;  // Don't stretch sidebar with app scaling
        gbc.weighty = 1;
        SidebarPanel areaPanel = new SidebarPanel(pluginLoader.getPlugins());
        contentPanel.add(areaPanel, gbc);

        mainViewPanel = new JPanel(new GridBagLayout());
        mainViewPanel.setBackground(new Color(50, 50, 50));
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(15, 10, 15, 20);
        gbc.weightx = 1;
        contentPanel.add(mainViewPanel, gbc);

        return contentPanel;
    }


    /**
     * Instantiate all loaded plugins' main panels and save them in a hashMap.
     * JPanels are later accessible using their plugins class name.
     * @param pluginLoader that loaded the plugins
     */
    private static void makePluginPanels(PluginLoader pluginLoader) {
        pluginLoader.getPlugins().forEach(posPlugin -> {
            appPlugins.put(posPlugin.getClass().getName(), posPlugin.makeMainPanel());
            System.out.println(posPlugin.getClass().getName());
        });
    }


    /**
     * Switch the main view window to display the main panel corresponding to the provided plugin.
     * @param pluginClass whose panel to show
     */
    public static void switchActivePlugin(String pluginClass) {
        mainViewPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        mainViewPanel.add(appPlugins.get(pluginClass), gbc);
        mainViewPanel.revalidate();
        mainViewPanel.repaint();
    }

    /**
     * GUI App entrypoint
     * @param args program arguments - unused
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(App::createAndShowGUI);
    }
}
