package cz.cuni.mff.java.projects.posapp.plugins.payment;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface Tab {
    public LocalDateTime getTimeCreated();
    public String getTabName();
    public ArrayList<TabItem> getTabItems();
    public void addTabItem(TabItem item);
    public void removeTabItem(TabItem item);
}
