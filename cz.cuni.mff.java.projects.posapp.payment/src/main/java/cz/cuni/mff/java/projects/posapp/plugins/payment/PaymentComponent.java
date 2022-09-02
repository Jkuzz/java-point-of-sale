package cz.cuni.mff.java.projects.posapp.plugins.payment;

import java.util.EventListener;

public interface PaymentComponent extends EventListener {
    /**
     * Notify the listener of an event affecting one tab
     * @param eventType event type
     * @param tab affected by event
     */
    void notify(String eventType, Tab tab);
}
