package cz.cuni.mff.java.projects.posapp.plugins.payment;

import cz.cuni.mff.java.projects.posapp.core.App;
import cz.cuni.mff.java.projects.posapp.plugins.DefaultComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;


public class AddProductPanel extends JPanel {

    /**
     * Panel containing the ProductGroupAddPanel of the currently selected ProductGroup
     */
    private final JPanel addPanel = new JPanel(new GridBagLayout());

    /**
     * Panel containing information about the currently selected product.
     */
    private final JPanel itemDisplayPanel = new JPanel(new GridBagLayout());

    /**
     * Tab to which this addPanel is adding
     */
    private final Tab targetTab;
    private final PaymentMediator mediator;

    public AddProductPanel(Tab targetTab, PaymentMediator mediator) {
        super(new GridBagLayout());
        this.targetTab = targetTab;
        this.mediator = mediator;
        makeContent();
    }

    private void makeContent() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;
        gbc.weighty = 0;

        HashMap<String, ActionListener> buttonDefs = new HashMap<>();
        buttonDefs.put("Done", a -> mediator.notify("addEnded", targetTab));
        JPanel headerPanel = DefaultComponentFactory.makeHeader("Add to '" + targetTab.getTabName() + "'",
                new Color(200, 200, 100),buttonDefs);
        add(headerPanel, gbc);

        add(makeSelectedItemDisplay(), gbc);

        gbc.weighty = 1;
        add(makeAddPanel(), gbc);
    }


    /**
     * Initialise the currently selected item panel
     * @return the panel
     */
    private JPanel makeSelectedItemDisplay() {
        itemDisplayPanel.setBackground(App.getColor("button"));
        showSelectedItem("Select a product", null, null);
        return itemDisplayPanel;
    }


    /**
     * Show information about the product in the product info panel.
     * @param name of product
     * @param amount how many of said product are currently in the tab
     * @param price of the product
     */
    private void showSelectedItem(String name, Integer amount, Float price) {
        itemDisplayPanel.removeAll();

        if(amount == null) amount = 0;
        if(price == null) price = 0f;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 30, 5, 30);
        gbc.ipady = 20;
        gbc.gridy = 0;
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;
        gbc.weighty = 0;

        itemDisplayPanel.add(new JLabel(name), gbc);
        itemDisplayPanel.add(new JLabel(amount + "x"), gbc);
        itemDisplayPanel.add(new JLabel(String.valueOf(price)), gbc);
        itemDisplayPanel.add(new JLabel("= " + price * amount), gbc);


        for(Component c: itemDisplayPanel.getComponents()) {
            c.setForeground(App.getColor("button-text"));
        }
        itemDisplayPanel.revalidate();
        itemDisplayPanel.repaint();
    }


    /**
     * Display information about the selected item in the item selection panel.
     * @param itemToShowId product_id of the selected item to show
     */
    void selectItem(Integer itemToShowId) {
        if(itemToShowId == null) {
            showSelectedItem("Select a product", null, null);
            return;
        }
        Optional<TabItem> tabItemOptional = targetTab.getTabItems()
                .stream().filter(t -> Objects.equals(t.getProductId(), itemToShowId))
                .findFirst();

        if(tabItemOptional.isEmpty()) {
            showSelectedItem("None in tab", null, null);
        } else {
            TabItem item = tabItemOptional.get();
            showSelectedItem(item.getName(), item.getAmount(), item.getPrice());
        }

    }

    /**
     * Initialise the selection window by displaying the root product groups.
     * @return the panel
     */
    private JPanel makeAddPanel() {
        addPanel.setBackground(App.getColor("tertiary"));
        showGroupAddPanel(new ProductGroupAddPanel(this, null, null));
        return addPanel;
    }


    /**
     * Display the provided productGroup selection panel in the add window.
     * @param panelToShow group panel that will be shown
     */
    void showGroupAddPanel(ProductGroupAddPanel panelToShow) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(50, 50, 50, 50);
        gbc.weightx = 1;
        gbc.weighty = 1;

        addPanel.removeAll();
        addPanel.add(panelToShow, gbc);
        addPanel.revalidate();
        addPanel.repaint();
    }

    /**
     * Add the provided product fields as a tabItem to the current target tab
     * @param productFields fields of product to add
     */
    void addToTab(HashMap<String, Object> productFields) {
        TabItem tabItem = new TabItem(
                (Integer) productFields.get("price"),
                (String) productFields.get("name"),
                (Integer) productFields.get("id")
        );
        boolean added = false;
        for(TabItem item: targetTab.getTabItems()) {
            if(item.equals(tabItem)) {
                item.mergeAmount(tabItem);
                added = true;
                break;
            }
        }
        if(!added) targetTab.addTabItem(tabItem);
    }
}
