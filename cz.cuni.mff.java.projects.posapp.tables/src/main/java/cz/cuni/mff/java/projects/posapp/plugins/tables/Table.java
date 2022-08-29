package cz.cuni.mff.java.projects.posapp.plugins.tables;

import javax.swing.*;
import java.awt.*;

public class Table extends JPanel implements Prototype{

    private boolean interactable;

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

    public Table(Rectangle rectangle, boolean interact, Color bgColor) {
        super();
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


    @Override
    public Prototype clone() {
        Rectangle newBounds = getBounds();
        newBounds.translate(20, 20);
        return new Table(newBounds, interactable, getBackground());
    }
}
