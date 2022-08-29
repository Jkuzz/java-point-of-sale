package cz.cuni.mff.java.projects.posapp.plugins.tables;

import java.util.*;

public class TableEventPublisher {
    Map<String, List<TableChangeListener>> listeners = new HashMap<>();

    public TableEventPublisher(String... operations) {
        for (String operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    public void subscribe(String eventType, TableChangeListener listener) {
        List<TableChangeListener> users = listeners.get(eventType);
        users.add(listener);
    }

    public void unsubscribe(String eventType, TableChangeListener listener) {
        List<TableChangeListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    public void notify(String eventType, Table affectedTable) {
        List<TableChangeListener> users = listeners.get(eventType);
        for (TableChangeListener listener : users) {
            listener.notify(eventType, affectedTable);
        }
    }
}
