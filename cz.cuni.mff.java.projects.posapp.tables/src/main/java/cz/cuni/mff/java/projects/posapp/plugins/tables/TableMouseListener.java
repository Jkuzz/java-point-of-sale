package cz.cuni.mff.java.projects.posapp.plugins.tables;

import cz.cuni.mff.java.projects.posapp.plugins.tables.payment.TableTabViewPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * MouseListener for an interactable Table Panel. Opens that table's TableTab on click.
 */
public class TableMouseListener implements MouseListener {

    private final Table targetTable;
    private final Plugin plugin;

    /**
     * Create the listener. It will notify the plugin's mediator
     * and switch the plugin's active panel to that of the clicked table's tab view.
     * @param targetTable that the listener is associated with
     * @param plugin to which the table belongs to
     */
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
