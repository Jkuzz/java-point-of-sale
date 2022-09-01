package cz.cuni.mff.java.projects.posapp.plugins.payment;

import cz.cuni.mff.java.projects.posapp.core.App;
import cz.cuni.mff.java.projects.posapp.plugins.DefaultComponentFactory;
import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;


public class Plugin implements POSPlugin {

    private final JPanel tabsPanel = new JPanel(new GridBagLayout());

    public Plugin() {
        tabsPanel.setBackground(App.getColor("tertiary"));
    }

    @Override
    public String getDisplayName() {
        return "Payment";
    }

    @Override
    public JPanel makeMainPanel() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        makeContent(contentPanel);
        return contentPanel;
    }

    /**
     * Create content for the provided module parent panel.
     * @param modulePanel panel to insert content into
     */
    private void makeContent(JPanel modulePanel) {
        modulePanel.setBackground(App.getColor("tertiary"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;
        gbc.weighty = 0;

        HashMap<String, ActionListener> headerButtonDefs = new HashMap<>();
        headerButtonDefs.put("New", e -> addNewTab("New"));
        JPanel headerPanel = DefaultComponentFactory.makeHeader(
                getDisplayName(), new Color(177, 74, 211), headerButtonDefs
        );
        modulePanel.add(headerPanel, gbc);

        gbc.weighty = 1;
        gbc.insets = new Insets(20, 20, 20, 20);
        JScrollPane tabsScrollPane = new JScrollPane(tabsPanel);
        tabsScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        modulePanel.add(tabsScrollPane, gbc);
    }


    public JPanel makeTabAdd() {
        return null;
    }


    public void addNewTab(String tabName) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;
        gbc.weighty = 0;

        tabsPanel.add(new NameTabPanel(tabName), gbc);
        tabsPanel.revalidate();
    }


    public void deleteTab(NameTabPanel tab) {
        tabsPanel.remove(tab);
        tabsPanel.revalidate();
    }
}
