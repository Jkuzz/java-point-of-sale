package cz.cuni.mff.java.projects.posapp.plugins.payment;

import cz.cuni.mff.java.projects.posapp.core.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;


/**
 * Handles the database and display events that occur when the tab payment succeeds.
 */
public class PaymentSuccessHandler implements PaymentComponent {

    private final TabsViewPanel viewPanel;

    public PaymentSuccessHandler(TabsViewPanel viewPanel) {
        this.viewPanel = viewPanel;
    }

    /**
     * Notify the listener of an event affecting one tab
     *
     * @param eventType event type
     * @param tab       affected by event
     */
    @Override
    public void notify(String eventType, Tab tab) {
        switch(eventType) {
            case "paySuccess" -> handlePaySuccess(tab);
        }
    }

    /**
     * Try to save the transaction to the database. If successful, delete the tab.
     * @param tab to save to database
     */
    private void handlePaySuccess(Tab tab) {
        if(DatabaseConnector.getInstance().savePayment(tab)) {
            viewPanel.deleteTab(tab);

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
        ArrayList<HashMap<String, Object>> productsChanged = new ArrayList<>();
        // For each product, message indicating id and how many were sold.
        tab.getTabItems().forEach(t -> {
            HashMap<String, Object> product = new HashMap<>();
            product.put("id", t.getProductId());
            product.put("amount", t.getAmount());
            product.put("name", t.getName());
            productsChanged.add(product);
        });
        App.messagePlugin(
                "cz.cuni.mff.java.projects.posapp.plugins.inventory.Inventory",
                "paySuccess",
                productsChanged
        );
    }
}
