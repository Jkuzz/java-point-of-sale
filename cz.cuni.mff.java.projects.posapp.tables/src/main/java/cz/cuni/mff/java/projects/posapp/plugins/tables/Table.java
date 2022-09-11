package cz.cuni.mff.java.projects.posapp.plugins.tables;

import cz.cuni.mff.java.projects.posapp.plugins.tables.payment.TableTab;

import javax.swing.*;
import java.awt.*;

/**
 * Table rectangle to be displayed in the tables view and editor.
 */
public class Table extends JPanel implements Prototype {

    /**
     * Whether this table can be interacted with. Set and handle externally.
     */
    private final boolean interactable;
    /**
     * ID of the table, same as in the database.
     */
    public final int id;
    /**
     * Tab of this table. Add items here, display this when opening the table.
     */
    private final TableTab tableTab;

    /**
     * Find if the table is currently selected in the editor.
     * Handle externally so that there is only one selected.
     * @return if the table is selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Select the table and handle its appearance change.
     * Handle externally so that there is only one selected.
     * @param selected new selected status.
     */
    public void setSelected(boolean selected) {
        if(selected != this.selected) {
            this.toggleSelected();
        }
    }

    /**
     * Whether this table is currently selected.
     */
    private boolean selected;

    /**
     * Find whether the table is interactable or not. If yes, should be provided an ActionListener externally
     * @return if is interactable
     */
    public boolean isInteractable() {
        return interactable;
    }


    /**
     * Constructor method.
     * @param rectangle position of the table panel
     * @param interact if the table should be interactable
     * @param bgColor colour of the rectangle panel
     * @param id of the table, as will be in the database
     */
    public Table(Rectangle rectangle, boolean interact, Color bgColor, int id) {
        super();
        this.id = id;
        setBackground(bgColor);
        setBounds(rectangle);
        interactable = interact;
        tableTab = new TableTab(id);
    }

    /**
     * Toggle the selection of the table. Handles highlighting.
     */
    public void toggleSelected() {
        if(selected) {
            setBorder(null);
            selected = false;
        } else {
            setBorder(BorderFactory.createLineBorder(new Color(0, 255, 228), 5));
            selected = true;
        }
    }


    /**
     * Clone the table panel using the Protoype pattern. Use this to make a copy of
     * the table object for multiple windows. DO NOT USE FOR EDITOR, will cause an issue with
     * non-unique ids. For that, use TablesModel.cloneTable to maintain unique key uniqueness.
     * @return the clone
     */
    @Override
    public Prototype clone() {
        Rectangle newBounds = getBounds();
        newBounds.translate(20, 20);
        return new Table(newBounds, interactable, getBackground(), id);
    }

    /**
     * Get the Tab containing items on the table's tab.
     * @return the Tab of the table
     */
    public TableTab getTableTab() {
        return tableTab;
    }
}
