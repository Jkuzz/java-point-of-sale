package cz.cuni.mff.java.projects.posapp.plugins.tables;

import java.util.ArrayList;
import java.util.EventListener;

/**
 * Listener listening for events affecting one or multiple Tables.
 */
public interface TableChangeListener extends EventListener {
    /**
     * Notify the listener of an event affecting one tables
     * @param eventType event type
     * @param table affected by event
     */
    void notify(String eventType, Table table);

    /**
     * Notify the listener of an event affecting multiple tables
     * @param eventType event type
     * @param tables affected by event
     */
    void notify(String eventType, ArrayList<Table> tables);
}
