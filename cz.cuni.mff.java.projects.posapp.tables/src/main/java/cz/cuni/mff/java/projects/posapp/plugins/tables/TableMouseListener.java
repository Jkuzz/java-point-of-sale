package cz.cuni.mff.java.projects.posapp.plugins.tables;

import cz.cuni.mff.java.projects.posapp.plugins.tables.payment.TableTabViewPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TableMouseListener implements MouseListener {

    private final Table targetTable;
    private final Plugin plugin;

    public TableMouseListener(Table targetTable, Plugin plugin) {
        this.targetTable = targetTable;
        this.plugin = plugin;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        TableTabViewPanel tableTabViewPanel = new TableTabViewPanel(plugin.getPaymentMediator(), targetTable);
        plugin.setActivePanel(tableTabViewPanel);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
