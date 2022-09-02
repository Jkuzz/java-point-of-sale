package cz.cuni.mff.java.projects.posapp.plugins.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Implementation of the Mediator pattern using Observers.
 * Emitters emmit events using the Mediator, which then notifies its subscribed Listeners.
 */
public class PaymentMediator {
    Map<String, List<PaymentComponent>> listeners = new HashMap<>();

    public PaymentMediator(String... operations) {
        for (String operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    public void subscribe(String eventType, PaymentComponent listener) {
        List<PaymentComponent> users = listeners.get(eventType);
        users.add(listener);
    }

    public void unsubscribe(String eventType, PaymentComponent listener) {
        List<PaymentComponent> users = listeners.get(eventType);
        users.remove(listener);
    }

    public void notify(String eventType, Tab affectedTab) {
        List<PaymentComponent> users = listeners.get(eventType);
        for (PaymentComponent listener : users) {
            listener.notify(eventType, affectedTab);
        }
    }
}
