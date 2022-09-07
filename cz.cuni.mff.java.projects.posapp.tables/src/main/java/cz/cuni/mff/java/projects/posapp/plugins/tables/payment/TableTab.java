package cz.cuni.mff.java.projects.posapp.plugins.tables.payment;

import cz.cuni.mff.java.projects.posapp.plugins.payment.Tab;
import cz.cuni.mff.java.projects.posapp.plugins.payment.TabItem;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class TableTab implements Tab {

    private final ArrayList<TabItem> tabItems = new ArrayList<>();
    private final int tableId;
    private final LocalDateTime timeOpened;

    public TableTab(int tableId) {
        this.tableId = tableId;
        this.timeOpened = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getTimeCreated() {
        return timeOpened;
    }

    @Override
    public String getTabName() {
        return "Table " + tableId;
    }

    @Override
    public ArrayList<TabItem> getTabItems() {
        return tabItems;
    }

    /**
     * Get the sum of the prices of items in the tab.
     * @return the sum
     */
    @Override
    public Float getTotalCost() {
        return tabItems.stream()
                .map(t -> t.getPrice() * t.getAmount())
                .reduce(0f, Float::sum);
    }

    @Override
    public void addTabItem(TabItem item) {
        if(item != null) tabItems.add(item);
    }

    @Override
    public void removeTabItem(TabItem item) {
        tabItems.remove(item);
    }

    @Override
    public void clearTabItems() {
        tabItems.clear();
    }
}
