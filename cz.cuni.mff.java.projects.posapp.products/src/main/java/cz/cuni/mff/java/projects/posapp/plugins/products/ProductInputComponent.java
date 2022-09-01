package cz.cuni.mff.java.projects.posapp.plugins.products;

import javax.swing.*;

public class ProductInputComponent {
    public JComboBox<GroupComboBoxItem> getComboInput() {
        return comboInput;
    }

    public JTextField getTextInput() {
        return textInput;
    }

    private final JComboBox<GroupComboBoxItem> comboInput;
    private final JTextField textInput;

    public ProductInputComponent(JComboBox<GroupComboBoxItem> comboInput) {
        this.comboInput = comboInput;
        this.textInput = null;
    }

    public ProductInputComponent(JTextField textInput) {
        this.comboInput = null;
        this.textInput = textInput;
    }
}
