package cz.cuni.mff.java.projects.posapp.plugins.payment;

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
            case "paySuccess" ->handlePaySuccess(tab);
        }
    }

    /**
     * Try to save the transaction to the database. If successful, delete the tab.
     * @param tab to save to database
     */
    private void handlePaySuccess(Tab tab) {
        if(DatabaseConnector.getInstance().savePayment(tab)) {
            viewPanel.deleteTab(tab);
        }
    }
}
