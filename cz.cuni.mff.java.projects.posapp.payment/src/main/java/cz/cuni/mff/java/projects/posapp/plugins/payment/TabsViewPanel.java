package cz.cuni.mff.java.projects.posapp.plugins.payment;

import cz.cuni.mff.java.projects.posapp.core.App;
import cz.cuni.mff.java.projects.posapp.plugins.DefaultComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;


/**
 * Panel that contains the currently opened nameTabs.
 * Also provides the functionality to open new ones or pay for current ones.
 */
public class TabsViewPanel extends JPanel {

    /**
     * Panel that will be placed inside the scroll pane. Place content here.
     */
    private final JPanel tabsScrollPanel = new JPanel(new GridBagLayout());
    /**
     * Mediator to subscribe to and notify of a Tab events
     */
    private final PaymentMediator paymentMediator;


    /**
     * Construct the panel and subscribe to the Mediator.
     * @param paymentMediator to communicate about tab events with
     */
    public TabsViewPanel(PaymentMediator paymentMediator) {
        this.paymentMediator = paymentMediator;
        this.setLayout(new GridBagLayout());
        tabsScrollPanel.setBackground(App.getColor("tertiary"));
        PaymentSuccessHandler paymentSuccessHandler = new PaymentSuccessHandler(this);
        paymentMediator.subscribe("paySuccess", paymentSuccessHandler);
        makeContent();
    }


    /**
     * Create content for the panel.
     */
    private void makeContent() {
        this.setBackground(App.getColor("tertiary"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;
        gbc.weighty = 0;

        HashMap<String, ActionListener> headerButtonDefs = new HashMap<>();
        headerButtonDefs.put("New", e -> addNewTab("New"));
        JPanel headerPanel = DefaultComponentFactory.makeHeader(
                "Payment", new Color(177, 74, 211), headerButtonDefs
        );
        this.add(headerPanel, gbc);

        gbc.weighty = 1;
        gbc.insets = new Insets(20, 20, 20, 20);
        JScrollPane tabsScrollPane = new JScrollPane(tabsScrollPanel);
        tabsScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        this.add(tabsScrollPane, gbc);
    }


    /**
     * Add new blank tab to the tabs panel.
     * @param tabName initial name
     */
    public void addNewTab(String tabName) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;
        gbc.weighty = 0;

        NameTabPanel newTab = new NameTabPanel(tabName, paymentMediator);
        tabsScrollPanel.add(newTab, gbc);
        tabsScrollPanel.revalidate();
        paymentMediator.notify("tabOpened", newTab);
    }


//    /**
//     * Process payment for the tab. If payment is successful, remove the tab.
//     * @param tab to pay
//     */
//    void payTab(Tab tab) {
//        System.out.println("Paying for tab " + tab.getTabName());
//        paymentMediator.notify("payStarted", tab);
//    }


//    /**
//     * Handle adding or removing items to the tab.
//     * @param tab to add/remove to
//     */
//    void addToTab(Tab tab) {
//        System.out.println("Adding to tab " + tab.getTabName());
//        paymentMediator.notify("addStarted", tab);
//    }


    /**
     * Delete the tab from the panel. Assumes unique timeCreated:name identification
     * @param tab to delete
     */
    public void deleteTab(Tab tab) {
        for(Component c: tabsScrollPanel.getComponents()) {
            try {
                NameTabPanel nameTabPanel = (NameTabPanel) c;
                if(nameTabPanel.getTimeCreated() == tab.getTimeCreated()) {
                    nameTabPanel.close();
                    tabsScrollPanel.remove(nameTabPanel);
                    tabsScrollPanel.revalidate();
                    return;
                }
            } catch (ClassCastException ignored) {} // continue;
        }
    }
}
