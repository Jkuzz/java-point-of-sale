package cz.cuni.mff.java.projects.posapp.plugins.payment;


/**
 * Mediator component to listen to add and payment events to switch the panel to the appropriate one.
 */
public class PanelSwitchComponent implements PaymentComponent {

    private final Plugin plugin;

    /**
     * Set the target plugin to change panels in.
     * @param plugin to affect
     */
    public PanelSwitchComponent(Plugin plugin) {
        this.plugin = plugin;
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
            case "addStarted" -> handleAddStarted(tab);
            case "addEnded" -> handleAddEnded();
            case "payStarted" -> handlePayStarted(tab);
            case "payCancel", "paySuccess" -> handlePayEnded();
        }
    }

    private void handleAddStarted(Tab tab) {
        AddProductPanel addProductPanel = new AddProductPanel(tab, plugin.getPaymentMediator());
        plugin.showPanel(addProductPanel);
    }

    private void handleAddEnded() {
        plugin.showTabsPanel();
    }

    private void handlePayStarted(Tab tab) {
        CheckoutPanel checkoutPanel = new CheckoutPanel(tab, plugin.getPaymentMediator());
        plugin.showPanel(checkoutPanel);
    }

    private void handlePayEnded() {
        plugin.showTabsPanel();
    }
}
