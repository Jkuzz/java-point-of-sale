package cz.cuni.mff.java.projects.posapp.plugins.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Implementation of the Mediator pattern using Observers.
 * Emitters emit events using the Mediator, which then notifies its subscribed Listeners.
 */
public class PaymentMediator {
    Map<String, List<PaymentComponent>> listeners = new HashMap<>();

    /**
     * Initialise the mediator with Listener lists for each event
     * @param operations events that can be subscribed to and emitted
     */
    public PaymentMediator(String... operations) {
        for (String operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    /**
     * Subscribe a component to listen to the particular event.
     * Make sure the event exists in the Mediator.
     * @param eventType to listen to
     * @param listener that will be notified of that event
     */
    public void subscribe(String eventType, PaymentComponent listener) {
        List<PaymentComponent> users = listeners.get(eventType);
        users.add(listener);
    }

    /**
     * Unsubscribe a component from a particular event.
     * Make sure the event exists in the Mediator.
     * @param eventType to unsubscribe from
     * @param listener to be unsubscribed
     */
    public void unsubscribe(String eventType, PaymentComponent listener) {
        List<PaymentComponent> users = listeners.get(eventType);
        users.remove(listener);
    }

    /**
     * Notify all subscribed Components of an event affecting a Tab
     * @param eventType type of event that occurred
     * @param affectedTab tab that was affected by the event
     */
    public void notify(String eventType, Tab affectedTab) {
        System.out.println("PaymentMediator notifying: " + eventType);
        List<PaymentComponent> users = listeners.get(eventType);
        for (PaymentComponent listener : users) {
            listener.notify(eventType, affectedTab);
        }
    }
}
