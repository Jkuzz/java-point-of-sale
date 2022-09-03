package cz.cuni.mff.java.projects.posapp.plugins.payment;

public class PanelSwitchComponent implements PaymentComponent {

    private final Plugin plugin;

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
            case "addEnded" -> handleAddEnded(tab);
            case "payStarted" -> handlePayStarted(tab);
            case "payEnded" -> handlePayEnded(tab);
        }
    }

    private void handleAddStarted(Tab tab) {
        AddProductPanel addProductPanel = new AddProductPanel(tab, plugin.getPaymentMediator());
        plugin.showPanel(addProductPanel);
    }

    private void handleAddEnded(Tab tab) {
        plugin.showTabsPanel();
    }

    private void handlePayStarted(Tab tab) {
        System.out.println("Starting payment for " + tab.getTabName());
        CheckoutPanel addProductPanel = new CheckoutPanel(tab);
        plugin.showPanel(addProductPanel);
    }

    private void handlePayEnded(Tab tab) {
        System.out.println("Ending payment for " + tab.getTabName());
        plugin.showTabsPanel();
    }
}
