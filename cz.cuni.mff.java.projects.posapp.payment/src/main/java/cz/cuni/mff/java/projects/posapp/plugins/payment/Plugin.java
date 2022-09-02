package cz.cuni.mff.java.projects.posapp.plugins.payment;

import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import java.awt.*;


/**
 * Plugin providing the payment functionality
 */
public class Plugin implements POSPlugin {

    private final JPanel rootPanel = new JPanel(new GridBagLayout());
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final TabsViewPanel tabsPanel;
    private final PaymentMediator paymentMediator = new PaymentMediator(
            "tabOpened", "tabClosed", "addStarted", "addEnded", "payStarted", "payEnded");


    public Plugin() {
        tabsPanel = new TabsViewPanel(this, paymentMediator);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        PanelSwitchComponent panelSwitchComponent = new PanelSwitchComponent(this);
        paymentMediator.subscribe("addStarted", panelSwitchComponent);
        paymentMediator.subscribe("addEnded", panelSwitchComponent);
        paymentMediator.subscribe("payStarted", panelSwitchComponent);
        paymentMediator.subscribe("payEnded", panelSwitchComponent);
    }

    @Override
    public String getDisplayName() {
        return "Payment";
    }

    @Override
    public JPanel makeMainPanel() {
        rootPanel.add(tabsPanel, gbc);
        return rootPanel;
    }

    /**
     * Show the panel provided. Use for swapping between the payment, tabs and tab-add views.
     * @param panel to show
     */
    public void showPanel(JPanel panel) {
        rootPanel.removeAll();
        rootPanel.add(panel, gbc);
        rootPanel.revalidate();
        rootPanel.repaint();
    }

    /**
     * Show the default tabs panel.
     */
    public void showTabsPanel() {
        showPanel(tabsPanel);
    }
}
