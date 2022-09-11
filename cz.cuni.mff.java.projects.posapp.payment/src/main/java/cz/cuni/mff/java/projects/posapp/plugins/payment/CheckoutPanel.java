package cz.cuni.mff.java.projects.posapp.plugins.payment;

import cz.cuni.mff.java.projects.posapp.core.App;
import cz.cuni.mff.java.projects.posapp.plugins.DefaultComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * The panel where the tab total is summarised and payment is processed.
 * Does not handle post-payment effects like saving transaction and clearing the tab.
 * For that, use listeners listening to the paymentMediator! "paySuccess" event.
 */
public class CheckoutPanel extends JPanel {

    /**
     * The Tab that is being paid with this checkout panel instance.
     */
    private final Tab tabToPay;

    /**
     * Mediator to notify of a successful or canceled payment
     */
    private final PaymentMediator paymentMediator;

    /**
     * Create the Checkout panel for the tab. Provides options for payment without implementation.
     * @param tabToPay Tab to be paid
     * @param mediator Mediator to report payment events to
     */
    public CheckoutPanel(Tab tabToPay, PaymentMediator mediator) {
        super(new GridBagLayout());
        this.tabToPay = tabToPay;
        this.paymentMediator = mediator;

        setBackground(App.getColor("tertiary"));
        makeContent();
    }

    private void makeContent() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 0;

        HashMap<String, ActionListener> buttonDefs = new HashMap<>();
        buttonDefs.put("Cancel", a -> paymentMediator.notify("payCancel", tabToPay));
        JPanel headerPanel = DefaultComponentFactory.makeHeader("Add to '" + tabToPay.getTabName() + "'",
                new Color(200, 200, 100),buttonDefs);
        add(headerPanel, gbc);

        gbc.insets = new Insets(30, 30, 10, 30);
        gbc.weighty = 1;
        add(makeTabList(), gbc);

        gbc.insets = new Insets(10, 40, 20, 40);
        gbc.weighty = 0;
        gbc.gridwidth = 1; // Fit the buttons and total on last line
        add(makePaymentButtonsPanel(), gbc);

        gbc.gridx = 1;
        add(makeTabTotalPanel(), gbc);
    }


    /**
     * Create the list of tabItems to describe the tab.
     * @return displayed list of tabItems
     */
    private JScrollPane makeTabList() {
        JPanel scrollPaneContent = new JPanel(new GridBagLayout());
        JScrollPane listScrollPane = new JScrollPane(scrollPaneContent);
        scrollPaneContent.setBackground(App.getColor("tertiary"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 30;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;

        tabToPay.getTabItems().forEach(tabItem -> scrollPaneContent.add(makeTabItemDisplay(tabItem), gbc));

        return listScrollPane;
    }


    /**
     * Create the display row for the individual tabItem to be displayed in their display tab.
     * @param tabItem to display
     * @return the tabItem's display panel
     */
    private JPanel makeTabItemDisplay(TabItem tabItem) {
        JPanel tabItemPanel = new JPanel(new GridBagLayout());
        tabItemPanel.setBackground(App.getColor("button"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 20, 3, 20);
        gbc.ipadx = 50;
        gbc.gridy = 0;
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 0;

        JLabel nameLabel = new JLabel(tabItem.getName());
        nameLabel.setForeground(App.getColor("button-text"));

        JLabel amountLabel = new JLabel(tabItem.getAmount() + "x");
        amountLabel.setForeground(App.getColor("button-text"));

        JLabel priceLabel = new JLabel(tabItem.getPrice() + "");
        priceLabel.setForeground(App.getColor("button-text"));

        JLabel totalLabel = new JLabel("=   " + tabItem.getAmount() * tabItem.getPrice());
        totalLabel.setForeground(App.getColor("button-text"));

        tabItemPanel.add(nameLabel, gbc);
        tabItemPanel.add(amountLabel, gbc);
        tabItemPanel.add(priceLabel, gbc);
        tabItemPanel.add(totalLabel, gbc);

        return tabItemPanel;
    }


    /**
     * Make a summary of the tab's total price
     * @return panel displaying the total.
     */
    private JPanel makeTabTotalPanel() {
        JPanel totalPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        Float totalCost = tabToPay.getTotalCost();

        JLabel label = new JLabel("Total: " + totalCost);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        totalPanel.add(label, gbc);

        return totalPanel;
    }


    /**
     * Create a tab containing the payment button options.
     * @return panel with buttons
     */
    private JPanel makePaymentButtonsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(App.getColor("tertiary"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.ipadx = 45;
        gbc.ipady = 20;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 20, 0, 20);
        gbc.anchor = GridBagConstraints.EAST;

        JButton cashButton = new JButton("Cash");
        JButton cardButton = new JButton("Card");

        cashButton.setBackground(App.getColor("button"));
        cashButton.setForeground(App.getColor("button-text"));
        cashButton.setFocusPainted(false);
        cashButton.addActionListener(a -> paymentMediator.notify("paySuccess", tabToPay));

        cardButton.setBackground(App.getColor("button"));
        cardButton.setForeground(App.getColor("button-text"));
        cardButton.setFocusPainted(false);
        cardButton.addActionListener(a -> paymentMediator.notify("paySuccess", tabToPay));

        panel.add(cashButton, gbc);
        panel.add(cardButton, gbc);
        return panel;
    }
}
