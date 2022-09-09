package cz.cuni.mff.java.projects.posapp.plugins.products;

import javax.swing.*;

/**
 * Input component used for inputting product data.
 * Holds either a JComboBox or a JTextField
 */
public class ProductInputComponent {
    /**
     * @return the JComboBox or null
     */
    public JComboBox<GroupComboBoxItem> getComboInput() {
        return comboInput;
    }

    /**
     * @return the JTextField or null
     */
    public JTextField getTextInput() {
        return textInput;
    }

    private final JComboBox<GroupComboBoxItem> comboInput;
    private final JTextField textInput;

    /**
     * Create the input component with a JComboBox input mode
     * @param comboInput the JComboBox
     */
    public ProductInputComponent(JComboBox<GroupComboBoxItem> comboInput) {
        this.comboInput = comboInput;
        this.textInput = null;
    }

    /**
     * Create the input component with a JTextField input mode
     * @param textInput the JTextField
     */
    public ProductInputComponent(JTextField textInput) {
        this.comboInput = null;
        this.textInput = textInput;
    }
}
