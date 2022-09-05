package cz.cuni.mff.java.projects.posapp.plugins.payment;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface Tab {
    LocalDateTime getTimeCreated();
    String getTabName();
    ArrayList<TabItem> getTabItems();
    Float getTotalCost();
    void addTabItem(TabItem item);
    void removeTabItem(TabItem item);
}
