package cz.cuni.mff.java.projects.posapp.core;

import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;


import javax.swing.*;
import java.awt.*;

/**
 * GUI application.
 */
public class App {

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Point of Sale");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(createContent());

        frame.setMinimumSize(new Dimension(1200, 750));
        frame.pack();
        frame.setVisible(true);
    }

    private static JPanel createContent() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        contentPanel.setBackground(new Color(60, 60, 60));

        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15, 20, 15, 10);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;  // Don't stretch sidebar with app scaling
        gbc.weighty = 1;

        MainViewPanel mainViewPanel = new MainViewPanel();
        SidebarPanel areaPanel = new SidebarPanel(mainViewPanel);

        contentPanel.add(areaPanel, gbc);

        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(15, 10, 15, 20);
        gbc.weightx = 1;
        contentPanel.add(mainViewPanel, gbc);

        return contentPanel;
    }

    /**
     * GUI App entrypoint
     * @param args program arguments - unused
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(App::createAndShowGUI);
    }
}
