package cz.cuni.mff.java.projects.posapp.core;

import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import java.awt.*;
import java.util.ServiceLoader;


public class SidebarPanel extends JPanel {

    public SidebarPanel(MainViewPanel mainViewPanel) {
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(50, 50, 50));
        this.add(makeModuleButtons(mainViewPanel));
    }

    private JPanel makeModuleButtons(MainViewPanel mainViewPanel) {
        JPanel moduleButtonsPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        moduleButtonsPanel.setOpaque(false);
        moduleButtonsPanel.setMinimumSize(new Dimension(500,500));

        ServiceLoader<POSPlugin> pluginServiceLoader =  ServiceLoader.load(POSPlugin.class);
        for(POSPlugin plugin : pluginServiceLoader) {
            JButton moduleButton = makeModuleButton(plugin.getDisplayName());
            moduleButton.addActionListener(actionEvent -> {
                mainViewPanel.removeAll();
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1;
                gbc.weighty = 1;
                mainViewPanel.add(plugin.makeMainPanel(), gbc);
                mainViewPanel.revalidate();
            });
            moduleButtonsPanel.add(moduleButton);
        }

        return moduleButtonsPanel;
    }


    private JButton makeModuleButton(String name) {
        JButton btn = new JButton(name);

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);

        btn.setBackground(new Color(90, 90, 90));
        btn.setForeground(new Color(240, 240, 240));
        btn.setFont(new Font("Sans", Font.PLAIN, 18));

        btn.setPreferredSize(new Dimension(300, 50));

        return btn;
    }
}
