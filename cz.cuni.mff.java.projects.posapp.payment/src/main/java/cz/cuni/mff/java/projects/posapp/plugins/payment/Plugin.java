package cz.cuni.mff.java.projects.posapp.plugins.payment;

import cz.cuni.mff.java.projects.posapp.core.App;
import cz.cuni.mff.java.projects.posapp.plugins.DefaultComponentFactory;
import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;


public class Plugin implements POSPlugin {
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
        GridBagConstraints gbc = new GridBagConstraints();
        modulePanel.setBackground(App.getColor("tertiary"));
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;
        gbc.weighty = 0;

        HashMap<String, ActionListener> headerButtonDefs = new HashMap<>();
        headerButtonDefs.put("New", null);
        JPanel headerPanel = DefaultComponentFactory.makeHeader(
                getDisplayName(), new Color(177, 74, 211), headerButtonDefs
        );
        modulePanel.add(headerPanel, gbc);

        gbc.weighty = 1;
        JPanel paymentPanel = makePaymentPanel();
        modulePanel.add(paymentPanel, gbc);
    }

    private JPanel makePaymentPanel() {
        JPanel paymentPanel = new JPanel(new GridBagLayout());

        return paymentPanel;
    }
}
