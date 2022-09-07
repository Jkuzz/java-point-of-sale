package cz.cuni.mff.java.projects.posapp.plugins.tables.payment;

import cz.cuni.mff.java.projects.posapp.plugins.payment.*;
import cz.cuni.mff.java.projects.posapp.plugins.tables.Plugin;

import javax.swing.*;

public class PaymentTabSwitchListener implements PaymentComponent {

    private final Plugin plugin;
    private final PaymentMediator paymentMediator;
    private JPanel lastActiveTableTab;

    public PaymentTabSwitchListener(Plugin plugin, PaymentMediator paymentMediator) {
        this.plugin = plugin;
        this.paymentMediator = paymentMediator;
    }

    /**
     * Notify the listener of an event affecting one tab
     *
     * @param eventType event type
     * @param tab       affected by event
     */
    @Override
    public void notify(String eventType, Tab tab) {
        switch (eventType) {
            case "payStarted" -> handlePayStarted(tab);
            case "paySuccess" -> handlePaySuccess();
            case "addStarted" -> handleAddStarted(tab);
            case "addEnded" -> handleAddEnded();
        }
    }

    private void handlePayStarted(Tab tab) {
        CheckoutPanel checkoutPanel = new CheckoutPanel(tab, paymentMediator);
        plugin.setActivePanel(checkoutPanel);
    }

    private void handlePaySuccess() {
        plugin.resetActivePanel();
    }

    private void handleAddStarted(Tab tab) {
        lastActiveTableTab = plugin.getActivePanel();
        AddProductPanel addProductPanel = new AddProductPanel(tab, paymentMediator);
        plugin.setActivePanel(addProductPanel);
    }

    private void handleAddEnded() {
        if(lastActiveTableTab != null) {
            plugin.setActivePanel(lastActiveTableTab);
        }
    }
}
