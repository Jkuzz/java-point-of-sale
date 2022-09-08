package cz.cuni.mff.java.projects.posapp.plugins;

import javax.swing.*;

/**
 * Application plugin interface. App plugins are instantiated and asked for their panels by the App.
 */
public interface POSPlugin {
    /**
     * Get the name of the plugin. Will be displayed in the sidebar.
     * @return the plugin's name
     */
    String getDisplayName();

    /**
     * Make the plugins interface panel. This panel will be displayed in the main application viwe
     * when the plugin is selected.
     * @return the main panel.
     */
    JPanel makeMainPanel();

    /**
     * Interface method for receiving communication from other plugins.
     * @param eventType string type of incoming event notification.
     * @param payload object payload accompanying the message.
     */
    void message(String eventType, Object payload);
}
