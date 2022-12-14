package cz.cuni.mff.java.projects.posapp.plugins.payment;

import cz.cuni.mff.java.projects.posapp.core.App;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


/**
 * Panel representing a Tab that is associated with a name. Intended to be displayed as a row in a view.
 */
public class NameTabPanel extends JPanel implements Tab {
    /**
     * @return The time when the tab was created.
     */
    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    /**
     * @return name of the tab
     */
    public String getTabName() {
        return name;
    }

    /**
     * Get all the tabItems currently present in this tab's list
     * @return the tab's items
     */
    public ArrayList<TabItem> getTabItems() {
        return tabItems;
    }

    /**
     * Get the sum of the prices of items in the tab.
     * @return the sum
     */
    @Override
    public Float getTotalCost() {
        return tabItems.stream()
                .map(t -> t.getPrice() * t.getAmount())
                .reduce(0f, Float::sum);
    }

    /**
     * Add a TabItem to the tab. Does not handle external side effects such as updating tab price totals.
     * @param item to add
     */
    public void addTabItem(TabItem item) {
        tabItems.add(item);
    }

    public void removeTabItem(TabItem item) {
        tabItems.remove(item);
    }

    @Override
    public void clearTabItems() {
        tabItems.clear();
    }

    /**
     * When this tab was created
     */
    private final LocalDateTime timeCreated;

    /**
     * Name of the tab. Either Name or Table or whatever. This will be shown to user in some places.
     */
    private String name;

    /**
     * List of TabItems that are in the Tab. These must be paid for by the customer to delete the tab.
     */
    private final ArrayList<TabItem> tabItems = new ArrayList<>();

    /**
     * Label showing the total price of the tab. Update when changing tab contents with new text.
     */
    private final JLabel priceLabel = new JLabel("0");

    /**
     * Button showing the name of the tab. Swap with nameInputField on click.
     */
    private final JButton nameButton = new JButton();

    /**
     * Text field for inputting a new name. Replace with the altered button on confirm.
     */
    private final JTextField nameInputField = new JTextField();

    /**
     * Listens to the mediator for a new tab event and handles itm adding a NameTab to the panel.
     */
    private final NameTabAddListener nameTabAddListener = new NameTabAddListener(this);

    /**
     * Mediator of the Payment tab. Components will handle events,
     * therefore only notifying the mediator to start an event is sufficient.
     */
    private final PaymentMediator paymentMediator;


    /**
     * Create the nameTab view panel. Intended to be displayed as a row in a list of tabs.
     * @param name of the tab
     * @param paymentMediator Mediator object to announce tab actions and button presses to.
     */
    public NameTabPanel(String name, PaymentMediator paymentMediator) {
        super(new GridBagLayout());
        this.name = name;
        this.paymentMediator = paymentMediator;
        timeCreated = LocalDateTime.now();
        paymentMediator.subscribe("addEnded", nameTabAddListener);

        setBackground(App.getColor("button"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.gridy = 0;
        gbc.weighty = 0;
        gbc.weightx = 0;
        gbc.ipadx = 150;
        gbc.insets = new Insets(0, 20, 0, 0);

        LocalDateTime now = LocalDateTime.now();
        JLabel timeLabel = new JLabel(now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        timeLabel.setForeground(App.getColor("button-text"));
        add(timeLabel, gbc);

        gbc.ipadx = 0;
        gbc.weightx = 1;
        nameButton.setText(name);
        nameButton.setFocusPainted(false);
        nameButton.setBorderPainted(false);
        nameButton.setBackground(App.getColor("button"));
        nameButton.setForeground(App.getColor("button-text"));
        nameButton.addActionListener(a -> showNameInput());
        add(nameButton, gbc);

        nameInputField.setVisible(false);
        nameInputField.setEnabled(false);
        nameInputField.addActionListener(a -> hideNameInput());
        add(nameInputField, gbc);

        gbc.ipadx = 100;
        gbc.weightx = 0;
        priceLabel.setForeground(App.getColor("button-text"));
        add(priceLabel, gbc);

        gbc.weightx = 0;
        add(makePayButtonsPanel(paymentMediator));
    }


    /**
     * Create the panel containing the payment buttons of the tab.
     * @param paymentMediator Mediator to notify
     * @return the panel
     */
    private JPanel makePayButtonsPanel(PaymentMediator paymentMediator) {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(App.getColor("button"));
        JButton addButton = new JButton("Add");
        JButton payButton = new JButton("Pay");
        payButton.addActionListener(e -> paymentMediator.notify("payStarted", this));
        addButton.addActionListener(e -> paymentMediator.notify("addStarted", this));
        buttonsPanel.add(addButton);
        buttonsPanel.add(payButton);
        return buttonsPanel;
    }


    /**
     * Update the price label of the nameTab panel to display the sum of the tab's tabItems.
     */
    void updatePrice() {
        priceLabel.setText(String.valueOf(getTotalCost()));
    }


    /**
     * Close the tab and dispose of any connections.
     */
    public void close() {
        paymentMediator.unsubscribe("addEnded", nameTabAddListener);
    }


    /**
     * Toggle the tab name button to the name input
     */
    private void showNameInput() {
        nameInputField.setEnabled(true);
        nameInputField.setVisible(true);
        nameInputField.setText(name);
        nameInputField.requestFocusInWindow();

        nameButton.setEnabled(false);
        nameButton.setVisible(false);
        revalidate();
        repaint();
    }


    /**
     * Toggle the tab name input to the name defualt button
     */
    private void hideNameInput() {
        nameInputField.setEnabled(false);
        nameInputField.setVisible(false);

        name = nameInputField.getText();
        nameButton.setText(name);
        nameButton.setEnabled(true);
        nameButton.setVisible(true);
        revalidate();
        repaint();
    }
}
