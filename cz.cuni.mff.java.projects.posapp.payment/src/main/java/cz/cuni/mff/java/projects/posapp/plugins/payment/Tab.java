package cz.cuni.mff.java.projects.posapp.plugins.payment;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A Tab contains items that are to be paid for by the customer.
 */
public interface Tab {
    /**
     * Get the creation time of the Tab. Probably at the time of instantiation.
     * @return the time when this Tab was opened.
     */
    LocalDateTime getTimeCreated();

    /**
     * @return get the name of the Tab
     */
    String getTabName();

    /**
     * @return list of items currently present in the Tab.
     */
    ArrayList<TabItem> getTabItems();

    /**
     * Calculate the total sum of the tab's items.
     * @return total cost of the tab
     */
    Float getTotalCost();

    /**
     * Add a TabItem to the Tab
     * @param item to add to the tab
     */
    void addTabItem(TabItem item);

    /**
     * Remove a TabItem from the Tab
     * @param item to remove
     */
    void removeTabItem(TabItem item);

    /**
     * Clear TabItems from the Tab's list
     */
    void clearTabItems();
}
