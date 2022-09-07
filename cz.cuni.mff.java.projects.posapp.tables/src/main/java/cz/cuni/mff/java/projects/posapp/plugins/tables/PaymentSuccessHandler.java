package cz.cuni.mff.java.projects.posapp.plugins.tables;

import cz.cuni.mff.java.projects.posapp.core.App;
import cz.cuni.mff.java.projects.posapp.plugins.payment.DatabaseConnector;
import cz.cuni.mff.java.projects.posapp.plugins.payment.PaymentComponent;
import cz.cuni.mff.java.projects.posapp.plugins.payment.Tab;

import java.util.HashMap;
import java.util.Optional;


/**
 * Handles the database and display events that occur when the tab payment succeeds.
 */
public class PaymentSuccessHandler implements PaymentComponent {

    /**
     * Notify the listener of an event affecting one tab
     *
     * @param eventType event type
     * @param tab       affected by event
     */
    @Override
    public void notify(String eventType, Tab tab) {
        if ("paySuccess".equals(eventType)) {
            handlePaySuccess(tab);
        }
    }

    /**
     * Try to save the transaction to the database. If successful, delete the tab.
     * @param tab to save to database
     */
    private void handlePaySuccess(Tab tab) {
        if(DatabaseConnector.getInstance().savePayment(tab)) {
            Optional<Module> module = ModuleLayer.boot().findModule("cz.cuni.mff.java.projects.posapp.inventory");
            if(module.isPresent()) {
                notifyInventoryPlugin(tab);
            }
        }
    }

    /**
     * Uses the App.messagePlugin() channel of communication to notify the Inventory plugin of a sale.
     * @param tab to save
     */
    private void notifyInventoryPlugin(Tab tab) {
        HashMap<Integer, Integer> productsChanged = new HashMap<>();
        // For each product, message indicating id and how many were sold.
        tab.getTabItems().forEach(t -> productsChanged.put(t.getProductId(), -1 * t.getAmount()));
        App.messagePlugin(
                "cz.cuni.mff.java.projects.posapp.plugins.inventory.Inventory",
                "paySuccess",
                productsChanged
        );
    }
}
