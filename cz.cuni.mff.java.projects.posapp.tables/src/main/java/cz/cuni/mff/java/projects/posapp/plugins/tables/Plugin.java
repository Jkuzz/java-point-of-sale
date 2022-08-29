package cz.cuni.mff.java.projects.posapp.plugins.tables;

import cz.cuni.mff.java.projects.posapp.plugins.DefaultComponentFactory;
import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.util.HashMap;

public class Plugin implements POSPlugin {

    private JPanel activePanel;
    private JPanel tablesPanel;
    private JPanel editPanel;
    private final JLayeredPane editCanvasPanel = new JLayeredPane();
    private MouseAdapter canvasMouseAdapter;
    private KeyListener canvasKeyListener;

    private final TablesModel tablesModel = new TablesModel("tableAdded", "tableRemoved", "tablesSaved");

    public Plugin() {
        editCanvasPanel.setLayout(null);
    }


    @Override
    public String getDisplayName() {
        return "Tables";
    }


    @Override
    public JPanel makeMainPanel() {
        JPanel modulePanel = new JPanel(new GridBagLayout());
        makeContent(modulePanel);

        TablesDatabaseListener databaseListener = new TablesDatabaseListener();
        tablesModel.subscribe("tablesSaved", databaseListener);

        return modulePanel;
    }

    /**
     * Create content for the provided module parent panel.
     * @param modulePanel panel to insert content into
     */
    private void makeContent(JPanel modulePanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;
        gbc.weighty = 0;

        tablesPanel = makeTablesPanel();
        activePanel = tablesPanel;
        editPanel = makeEditPanel();

        HashMap<String, ActionListener> headerButtonDefs = new HashMap<>();
        headerButtonDefs.put("Tables", e -> setActivePanel(tablesPanel));
        headerButtonDefs.put("Edit", e -> setActivePanel(editPanel));

        JPanel headerPanel = DefaultComponentFactory.makeHeader(
                getDisplayName(), new Color(177, 123, 255), headerButtonDefs
        );
        modulePanel.add(headerPanel, gbc);

        gbc.weighty = 1;
        modulePanel.add(tablesPanel, gbc);
        modulePanel.add(editPanel, gbc);
        editPanel.setEnabled(false);
        editPanel.setVisible(false);
    }


    /**
     * Create panel for the display of tables.
     * @return the tables panel
     */
    private JPanel makeTablesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(110, 110, 110));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;
        gbc.weighty = 1;

        JLayeredPane viewCanvasPanel = new JLayeredPane();
        viewCanvasPanel.setLayout(null);
        panel.add(viewCanvasPanel, gbc);

        TableViewListener listener = new TableViewListener(viewCanvasPanel);
        tablesModel.subscribe("tablesSaved", listener);

        return panel;
    }


    /**
     * Create panel for the editing of table setup.
     * Will save the edited state to DB and change tables view panel
     * @return the edit panel
     */
    private JPanel makeEditPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        Color backgroundColour = new Color(110, 110, 110);
        panel.setBackground(backgroundColour);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 0;
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.weighty = 1;
        gbc.weightx = 0;

        panel.add(makePaintSidebarPanel(backgroundColour), gbc);

        gbc.weightx = 1;
        panel.add(editCanvasPanel, gbc);
        editCanvasPanel.setBackground(new Color(175, 175, 175));
        editCanvasPanel.setForeground(new Color(175, 175, 175));

        TableChangeListener tableListener = new TableEditorListener(editCanvasPanel);
        tablesModel.subscribe("tableAdded", tableListener);
        tablesModel.subscribe("tableRemoved", tableListener);

        return panel;
    }


    /**
     * Create the paint tool selection panel of the table editor.
     * Includes buttons for changing of mouse actions using the Strategy pattern.
     * @param backgroundColour of the panel
     * @return the panel
     */
    private JPanel makePaintSidebarPanel(Color backgroundColour) {
        JPanel sidebarPanel = new JPanel(new GridBagLayout());
        sidebarPanel.setBackground(backgroundColour);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 2, 5, 2);
        gbc.ipady = 10;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;

        JButton rectButton = new JButton("Rect");
//        JButton circleButton = new JButton("Circ");
        JButton moveButton = new JButton("Move");
        JButton saveButton = new JButton("Save");

        RectangleInputAdapter rectangleInputStrategy = new RectangleInputAdapter(editCanvasPanel, tablesModel);
        MoveInputAdapter moveInputStrategy = new MoveInputAdapter(editCanvasPanel, tablesModel);

        rectButton.addActionListener(
            e -> {
                changeCanvasMouseAdapter(rectangleInputStrategy);
                changeCanvasKeyListener(rectangleInputStrategy);
            }
        );
        moveButton.addActionListener(
            e -> {
                changeCanvasMouseAdapter(moveInputStrategy);
                changeCanvasKeyListener(moveInputStrategy);
            }
        );
        saveButton.addActionListener(e -> tablesModel.saveTables());

        sidebarPanel.add(rectButton, gbc);
//        sidebarPanel.add(circleButton, gbc);
        sidebarPanel.add(moveButton, gbc);

        rectButton.doClick();  // Initialise editor with the rectangle tool

        gbc.weighty = 1;
        JPanel fillerPanel = new JPanel();
        fillerPanel.setBackground(backgroundColour);
        sidebarPanel.add(fillerPanel, gbc);  // Filler panel to move save to bottom
        gbc.weighty = 0;
        sidebarPanel.add(saveButton, gbc);
        return sidebarPanel;
    }

    /**
     * Set the selected panel as the displayed panel (unless it is already active).
     * @param newActivePanel panel to display
     */
    private void setActivePanel(JPanel newActivePanel) {
        if(newActivePanel == activePanel) {
            return;
        }
        activePanel.setEnabled(false);
        activePanel.setVisible(false);
        newActivePanel.setEnabled(true);
        newActivePanel.setVisible(true);
        activePanel = newActivePanel;
    }


    /**
     * Change the current keyboard listener Strategy for the edit window.
     * @param newListener listener to change to
     */
    private void changeCanvasKeyListener(KeyListener newListener) {
        editCanvasPanel.requestFocusInWindow();
        if(canvasKeyListener == newListener) return;

        if(canvasKeyListener != null) {
            editCanvasPanel.removeKeyListener(canvasKeyListener);
        }
        canvasKeyListener = newListener;
        editCanvasPanel.addKeyListener(newListener);
    }

    /**
     * Change the current mouse listener Strategy for the edit window.
     * @param newAdapter listener to change to
     */
    private void changeCanvasMouseAdapter(MouseAdapter newAdapter) {
        if(canvasMouseAdapter == newAdapter) return;

        if(canvasMouseAdapter != null) {
            editCanvasPanel.removeMouseListener(canvasMouseAdapter);
            editCanvasPanel.removeMouseMotionListener(canvasMouseAdapter);
        }
        canvasMouseAdapter = newAdapter;
        editCanvasPanel.addMouseListener(newAdapter);
        editCanvasPanel.addMouseMotionListener(newAdapter);
    }
}
