package cz.cuni.mff.java.projects.posapp.plugins.payment;

import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;

public class Plugin implements POSPlugin {
    @Override
    public String getDisplayName() {
        return "Payment";
    }

    @Override
    public JPanel makeMainPanel() {
        JPanel contentPanel = new JPanel();
        makeContent(contentPanel);
        return contentPanel;
    }

    private void makeContent(JPanel panel) {

    }
}
