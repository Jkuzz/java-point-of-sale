package cz.cuni.mff.java.projects.posapp.plugins.payment;

import cz.cuni.mff.java.projects.posapp.plugins.products.ProductsTableModel;

import javax.swing.*;
import java.awt.*;


public class AddProductPanel extends JPanel {

    private final ProductsTableModel productsModel = ProductsTableModel.getInstance();


    public AddProductPanel() {
        super(new GridBagLayout());
    }
}
