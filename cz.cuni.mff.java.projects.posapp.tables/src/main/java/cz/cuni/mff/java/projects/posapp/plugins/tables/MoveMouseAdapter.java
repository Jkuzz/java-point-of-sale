package cz.cuni.mff.java.projects.posapp.plugins.tables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MoveMouseAdapter extends MouseAdapter {

    private final TablesModel tablesModel;


    public MoveMouseAdapter(JPanel canvasPanel, TablesModel tablesModel) {
        this.tablesModel = tablesModel;

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }
}
