package cz.cuni.mff.java.projects.posapp.plugins.tables;

import java.util.EventListener;

public interface NewTableListener extends EventListener {
    public void notify(String eventType, Table table);
}
