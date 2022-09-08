package cz.cuni.mff.java.projects.posapp.core;

import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * GUI application.
 */
public class App {

    /**
     * Set the color theme of the App.
     * @param colorTheme new scheme
     */
    public static void setColorTheme(HashMap<String, Color> colorTheme) {
        App.colorTheme = ColorThemeFactory.validateTheme(colorTheme);
    }

    private static HashMap<String, Color> colorTheme = ColorThemeFactory.makeDarkTheme();

    private static final HashMap<String, POSPlugin> appPlugins = new HashMap<>();
    private static final HashMap<String, JPanel> appPluginPanels = new HashMap<>();
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
        contentPanel.setBackground(getColor("background"));

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
        mainViewPanel.setBackground(getColor("primary"));
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
            appPlugins.put(posPlugin.getClass().getName(), posPlugin);
            appPluginPanels.put(posPlugin.getClass().getName(), posPlugin.makeMainPanel());
        });
    }


    /**
     * If the provided className plugin is loaded in the App, that plugin's message method
     * will be called with the provided message type and payload
     * @param pluginClass name of plugin to notify
     * @param eventType type of event the plugin is being notified of
     * @param payload payload of the message
     */
    public static void messagePlugin(String pluginClass, String eventType, Object payload) {
        if(appPlugins.containsKey(pluginClass)) {
            appPlugins.get(pluginClass).message(eventType, payload);
        }
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
        mainViewPanel.add(appPluginPanels.get(pluginClass), gbc);
        mainViewPanel.revalidate();
        mainViewPanel.repaint();
    }

    /**
     * Get a color from the defined color scheme.
     * @param key of color
     * @return the color or null if not set
     */
    public static Color getColor(String key) {
        return colorTheme.get(key);
    }

    /**
     * GUI App entrypoint
     * @param args program arguments - unused
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(App::createAndShowGUI);
    }
}
