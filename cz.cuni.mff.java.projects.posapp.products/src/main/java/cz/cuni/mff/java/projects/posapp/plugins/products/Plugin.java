package cz.cuni.mff.java.projects.posapp.plugins.products;

import cz.cuni.mff.java.projects.posapp.database.*;
import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import java.awt.*;


public class Plugin implements POSPlugin {

    Database db;

    public Plugin() {
        DBClient client = new ProductsClient();
        db = Database.getInstance(new DevUser(), client);
    }

    @Override
    public String getDisplayName() {
        return "Products";
    }

    @Override
    public JPanel makeMainPanel() {
        JPanel modulePanel = new JPanel();
        modulePanel.add(new JLabel(getDisplayName()));
        modulePanel.setBackground(new Color(166, 160, 94));
        return modulePanel;
    }
}
