package cz.cuni.mff.java.projects.posapp.plugins.payment;

public class NameTabAddListener implements PaymentComponent {

    private final NameTabPanel panel;

    public NameTabAddListener(NameTabPanel panel) {
        this.panel = panel;
    }

    /**
     * Notify the listener of an event affecting one tab
     *
     * @param eventType event type
     * @param tab       affected by event
     */
    @Override
    public void notify(String eventType, Tab tab) {
        if("addEnded".equals(eventType) && tab == panel) {
            panel.updatePrice();
        }
    }
}
