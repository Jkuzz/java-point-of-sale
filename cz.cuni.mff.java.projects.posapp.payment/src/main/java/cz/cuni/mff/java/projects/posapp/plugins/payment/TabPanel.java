package cz.cuni.mff.java.projects.posapp.plugins.payment;

import cz.cuni.mff.java.projects.posapp.core.App;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TabPanel extends JPanel {
    private final LocalDateTime timeCreated;
    private String name;
    private double tabPrice;
    private final JButton nameButton = new JButton();
    private final JTextField nameInputField = new JTextField();

    public TabPanel(String name) {
        super(new GridBagLayout());
        this.name = name;
        timeCreated = LocalDateTime.now();
        tabPrice = 0;

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

        JLabel priceLabel = new JLabel("0");
        priceLabel.setForeground(App.getColor("button-text"));
        add(priceLabel, gbc);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(App.getColor("button"));
        JButton addButton = new JButton("Add");
        JButton payButton = new JButton("Pay");
//        payButton.addActionListener(e -> ((Plugin)getParent()).deleteTab(this));
        buttonsPanel.add(addButton);
        buttonsPanel.add(payButton);
        gbc.weightx = 0;
        add(buttonsPanel);
    }


    private void showNameInput() {
        nameInputField.setEnabled(true);
        nameInputField.setVisible(true);
        nameInputField.setText(name);

        nameButton.setEnabled(false);
        nameButton.setVisible(false);
        revalidate();
        repaint();
    }


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
