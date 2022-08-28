package cz.cuni.mff.java.projects.posapp.plugins.tables;

import java.util.*;

public class TableEventPublisher {
    Map<String, List<NewTableListener>> listeners = new HashMap<>();

    public TableEventPublisher(String... operations) {
        for (String operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    public void subscribe(String eventType, NewTableListener listener) {
        List<NewTableListener> users = listeners.get(eventType);
        users.add(listener);
    }

    public void unsubscribe(String eventType, NewTableListener listener) {
        List<NewTableListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    public void notify(String eventType, Table affectedTable) {
        List<NewTableListener> users = listeners.get(eventType);
        for (NewTableListener listener : users) {
            listener.notify(eventType, affectedTable);
        }
    }
}
