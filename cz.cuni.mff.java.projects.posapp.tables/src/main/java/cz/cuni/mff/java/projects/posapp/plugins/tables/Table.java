package cz.cuni.mff.java.projects.posapp.plugins.tables;

import javax.swing.*;
import java.awt.*;

public class Table extends JPanel implements Prototype {

    private final boolean interactable;
    public final int id;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if(selected != this.selected) {
            this.toggleSelected();
        }
    }

    private boolean selected;

    public boolean isInteractable() {
        return interactable;
    }

    public Table(Rectangle rectangle, boolean interact, Color bgColor, int id) {
        super();
        this.id = id;
        setBackground(bgColor);
        setBounds(rectangle);
        interactable = interact;
    }

//    public Table(Point centre, double radius, boolean interact, Color bgColor) {
//        panel = new CirclePanel();
//        panel.setBounds(circleToRectangle(centre, radius));
//        panel.setOpaque(false);
//        panel.setBackground(bgColor);
//        interactable = interact;
//    }


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


    public static Rectangle circleToRectangle(Point centre, double radius) {
        return new Rectangle(
                (int) (centre.x - radius),
                (int) (centre.y - radius),
                (int) (radius * 2),
                (int) (radius * 2)
        );
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
}
