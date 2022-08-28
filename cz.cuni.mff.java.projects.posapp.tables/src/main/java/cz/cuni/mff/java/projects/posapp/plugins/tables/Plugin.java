package cz.cuni.mff.java.projects.posapp.plugins.tables;

import cz.cuni.mff.java.projects.posapp.plugins.DefaultComponentFactory;
import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.HashMap;

public class Plugin implements POSPlugin {


    private JPanel activePanel;
    private JPanel tablesPanel;
    private JPanel editPanel;
    private JPanel canvasPanel = new JPanel();
    private MouseAdapter canvasMouseAdapter;

    private TablesModel tablesModel = new TablesModel("tableAdded");

    public Plugin() {
        canvasPanel.setLayout(null);
    }


    @Override
    public String getDisplayName() {
        return "Tables";
    }

    @Override
    public JPanel makeMainPanel() {
        JPanel modulePanel = new JPanel(new GridBagLayout());;
        makeContent(modulePanel);

        TablesDatabaseConnector databaseConnector = new TablesDatabaseConnector();
        tablesModel.subscribe("tableAdded", databaseConnector);

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
        panel.setBackground(new Color(175, 175, 175));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 0, 5, 5);
        gbc.weightx = 1;
        gbc.weighty = 1;

        return panel;
    }

    /**
     * Create panel for the editing of table setup.
     * Will save the edited state to DB and change tables view panel
     * @return the edit panel
     */
    private JPanel makeEditPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(175, 175, 175));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 0;
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.weighty = 1;
        gbc.weightx = 0;

        MouseAdapter mouseAdapter = new RectangleMouseAdapter(canvasPanel, tablesModel);
        changeCanvasMouseAdapter(mouseAdapter);

        panel.add(makePaintSidebarPanel(), gbc);
        gbc.weightx = 1;
        panel.add(canvasPanel, gbc);

        return panel;
    }


    private JPanel makePaintSidebarPanel() {
        JPanel sidebarPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 2, 5, 2);
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;

        JButton rectButton = new JButton("Rect");
        JButton circleButton = new JButton("Circ");
        JButton moveButton = new JButton("Move");

        rectButton.addActionListener(
                e -> changeCanvasMouseAdapter(new RectangleMouseAdapter(canvasPanel, tablesModel))
        );
        circleButton.addActionListener(
                e -> changeCanvasMouseAdapter(new CircleMouseAdapter(canvasPanel, tablesModel))
        );

        sidebarPanel.add(rectButton, gbc);
        sidebarPanel.add(circleButton, gbc);
        sidebarPanel.add(moveButton, gbc);
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


    private void changeCanvasMouseAdapter(MouseAdapter newAdapter) {
        if(canvasMouseAdapter == newAdapter) return;

        if(canvasMouseAdapter != null) {
            canvasPanel.removeMouseListener(canvasMouseAdapter);
            canvasPanel.removeMouseMotionListener(canvasMouseAdapter);
        }
        canvasMouseAdapter = newAdapter;
        canvasPanel.addMouseListener(newAdapter);
        canvasPanel.addMouseMotionListener(newAdapter);
    }
}
