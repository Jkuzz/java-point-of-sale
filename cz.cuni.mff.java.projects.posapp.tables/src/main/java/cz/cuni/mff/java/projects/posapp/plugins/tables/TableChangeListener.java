package cz.cuni.mff.java.projects.posapp.plugins.tables;

import java.util.EventListener;

public interface TableChangeListener extends EventListener {
    void notify(String eventType, Table table);
}
