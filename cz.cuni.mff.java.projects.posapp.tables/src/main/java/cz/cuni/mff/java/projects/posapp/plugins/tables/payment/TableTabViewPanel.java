package cz.cuni.mff.java.projects.posapp.plugins.tables.payment;

import cz.cuni.mff.java.projects.posapp.core.App;
import cz.cuni.mff.java.projects.posapp.plugins.payment.PaymentMediator;
import cz.cuni.mff.java.projects.posapp.plugins.payment.TabItem;
import cz.cuni.mff.java.projects.posapp.plugins.tables.Table;

import javax.swing.*;
import java.awt.*;


/**
 * Panel providing a view of the TableTab's contents and gives access to Tab actions.
 * Reuses the Payment module's Checkout and Add panels.
 */
public class TableTabViewPanel extends JPanel {
    /**
     * Mediator to notify when events occur.
     */
    private final PaymentMediator paymentMediator;

    /**
     * Table that this tab belongs to.
     */
    private final Table table;

    /**
     * Label displaying the total cost of the tab.
     */
    private JLabel totalCostLabel;

    /**
     * Panel inside the JScrollPane displaying the list of tab items.
     * Update this panel when changing the tab contents after add.
     */
    private JPanel scrollPaneContent;


    /**
     * Construct the view panel and subscribe to the Mediator.
     * @param mediator to notify of Tab events that occur
     * @param table that was selected, whose TableTab should be viewed
     */
    public TableTabViewPanel(PaymentMediator mediator, Table table) {
        super(new GridBagLayout());
        this.paymentMediator = mediator;
        this.table = table;
        setBackground(App.getColor("tertiary"));
        makeContent();

        mediator.subscribe("addEnded", (eventType, tab) -> {
            if("addEnded".equals(eventType)) {
                updateTabListPanel();
                updateTotalCost();
            }
        });

        mediator.subscribe("paySuccess", (eventType, tab) -> {
            if("paySuccess".equals(eventType)) {
                tab.clearTabItems();
                updateTabListPanel();
                updateTotalCost();
            }
        });
    }

    private void makeContent() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.insets = new Insets(30, 30, 10, 30);
        gbc.weighty = 1;
        add(makeTabList(), gbc);

        gbc.insets = new Insets(10, 40, 20, 40);
        gbc.weighty = 0;
        gbc.gridwidth = 1; // Fit the buttons and total on last line
        add(makeTabEditButtonsPanel(), gbc);

        gbc.gridx = 1;
        add(makeTabTotalPanel(), gbc);
    }


    /**
     * Create the list of tabItems to describe the tab.
     * @return displayed list of tabItems
     */
    private JScrollPane makeTabList() {
        scrollPaneContent = new JPanel(new GridBagLayout());
        scrollPaneContent.setBackground(App.getColor("tertiary"));
        JScrollPane listScrollPane = new JScrollPane(scrollPaneContent);

        updateTabListPanel();

        return listScrollPane;
    }


    /**
     * Update the panel containing the list of the tab's items to display the currently present tabItems.
     */
    public void updateTabListPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 30;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;

        scrollPaneContent.removeAll();
        table.getTableTab()
                .getTabItems()
                .forEach(tabItem -> scrollPaneContent.add(makeTabItemDisplay(tabItem), gbc));
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


        totalCostLabel = new JLabel();
        updateTotalCost();
        totalCostLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        totalPanel.add(totalCostLabel, gbc);

        return totalPanel;
    }


    /**
     * Update the total cost label do display the current total cost of the tab
     */
    private void updateTotalCost() {
        Float totalCost = table.getTableTab().getTotalCost();
        totalCostLabel.setText("Total: " + totalCost);
    }


    /**
     * Create a tab containing the tab control buttons: Add to tab, Pay for tab
     * @return panel with buttons
     */
    private JPanel makeTabEditButtonsPanel() {
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

        JButton addButton = new JButton("Add");
        JButton payButton = new JButton("Pay");

        addButton.setBackground(App.getColor("button"));
        addButton.setForeground(App.getColor("button-text"));
        addButton.setFocusPainted(false);
        addButton.addActionListener(a -> paymentMediator.notify("addStarted", table.getTableTab()));

        payButton.setBackground(App.getColor("button"));
        payButton.setForeground(App.getColor("button-text"));
        payButton.setFocusPainted(false);
        payButton.addActionListener(a -> paymentMediator.notify("payStarted", table.getTableTab()));

        panel.add(addButton, gbc);
        panel.add(payButton, gbc);
        return panel;
    }
}
