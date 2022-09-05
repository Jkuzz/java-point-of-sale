package cz.cuni.mff.java.projects.posapp.plugins.payment;

import cz.cuni.mff.java.projects.posapp.core.App;
import cz.cuni.mff.java.projects.posapp.plugins.DefaultComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;


public class AddProductPanel extends JPanel {

    private final JPanel addPanel = new JPanel(new GridBagLayout());

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

        gbc.weighty = 1;
        add(makeAddPanel(), gbc);
    }

    private JPanel makeAddPanel() {
        addPanel.setBackground(App.getColor("tertiary"));
        showGroupAddPanel(new ProductGroupAddPanel(this, null, null));
        return addPanel;
    }

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
