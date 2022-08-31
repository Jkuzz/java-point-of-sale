package cz.cuni.mff.java.projects.posapp.plugins;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class DefaultComponentFactory {


    /**
     * Default implementation of the plugin headers.
     * Contains a title and a set of buttons, which are created using the provided HashMap.
     * @param title of the plugin to be displayed
     * @param color of the header panel
     * @param buttonDefinitions button text: button action listener
     * @return the created header panel
     */
    public static JPanel makeHeader(String title, Color color , HashMap<String, ActionListener> buttonDefinitions) {
        JPanel header = new JPanel(new GridBagLayout());;
        header.setBackground(color);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 15, 5, 10);
        gbc.ipadx = 20;
        gbc.ipady = 0;
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        header.add(headerLabel, gbc);

        if(buttonDefinitions != null) {
            JPanel headerButtonsPanel = makeHeaderButtonsPanel(buttonDefinitions);
            gbc.fill = GridBagConstraints.VERTICAL;
            gbc.anchor = GridBagConstraints.LINE_END;
            header.add(headerButtonsPanel, gbc);
        }
        return header;
    }

    /**
     * Create a panel, into which create buttons according to the provided
     * button definition hashMap.
     * @param buttonDefinitions button text: button action listener
     * @return panel containing the buttons
     */
    private static JPanel makeHeaderButtonsPanel(HashMap<String, ActionListener> buttonDefinitions) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 0;
        gbc.ipady = 0;
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;

        buttonDefinitions.forEach((name, actionListener) -> {
            JButton button = new JButton(name);
            if(actionListener != null) {
                button.addActionListener(actionListener);
            }
            panel.add(button, gbc);
        });
        return panel;
    }
}
