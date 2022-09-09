package cz.cuni.mff.java.projects.posapp.plugins.tables;

import java.util.*;

/**
 * Publishes events to subscribed Observers. Processes events regarding events affecting one or many Tables
 */
public class TableEventPublisher {
    Map<String, List<TableChangeListener>> listeners = new HashMap<>();

    /**
     * Initialise the Publisher with Listener lists for each event
     * @param operations events that can be subscribed to and emitted
     */
    public TableEventPublisher(String... operations) {
        for (String operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    /**
     * Subscribe a component to listen to the particular event.
     * Make sure the event exists in the Listener.
     * @param eventType to listen to
     * @param listener that will be notified of that event
     */
    public void subscribe(String eventType, TableChangeListener listener) {
        List<TableChangeListener> users = listeners.get(eventType);
        users.add(listener);
    }

    /**
     * Unsubscribe a component from a particular event.
     * Make sure the event exists in the Listener.
     * @param eventType to unsubscribe from
     * @param listener to be unsubscribed
     */
    public void unsubscribe(String eventType, TableChangeListener listener) {
        List<TableChangeListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    /**
     * Notify all subscribed Components of an event affecting a Table
     * @param eventType type of event that occurred
     * @param affectedTable table that was affected by the event
     */
    public void notify(String eventType, Table affectedTable) {
        List<TableChangeListener> users = listeners.get(eventType);
        for (TableChangeListener listener : users) {
            listener.notify(eventType, affectedTable);
        }
    }

    /**
     * Notify all subscribed Components of an event affecting multiple Tables
     * @param eventType type of event that occurred
     * @param affectedTables tables that were affected by the event
     */
    public void notify(String eventType, ArrayList<Table> affectedTables) {
        List<TableChangeListener> users = listeners.get(eventType);
        for (TableChangeListener listener : users) {
            listener.notify(eventType, affectedTables);
        }
    }
}
