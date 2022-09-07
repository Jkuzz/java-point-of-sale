package cz.cuni.mff.java.projects.posapp.plugins;

import javax.swing.*;

public interface POSPlugin {
    String getDisplayName();
    JPanel makeMainPanel();

    /**
     * Interface method for receiving communication from other plugins.
     * @param eventType string type of incoming event notification.
     * @param payload object payload accompanying the message.
     */
    void message(String eventType, Object payload);
}
