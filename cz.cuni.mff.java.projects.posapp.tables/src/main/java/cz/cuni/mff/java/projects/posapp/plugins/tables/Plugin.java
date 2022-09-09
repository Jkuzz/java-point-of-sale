package cz.cuni.mff.java.projects.posapp.plugins.tables;

import cz.cuni.mff.java.projects.posapp.core.App;
import cz.cuni.mff.java.projects.posapp.plugins.DefaultComponentFactory;
import cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;
import cz.cuni.mff.java.projects.posapp.plugins.payment.PaymentMediator;
import cz.cuni.mff.java.projects.posapp.plugins.tables.payment.PaymentTabSwitchListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.util.HashMap;

/**
 * Plugin providing a table layout, editor and table payment functionality
 */
public class Plugin implements POSPlugin {

    /**
     * Get the panel that is currently active in the plugin view.
     * Use mainly to check to avoid switching to one that is already active.
     * @return the currently active panel
     */
    public JPanel getActivePanel() {
        return activePanel;
    }

    /**
     * The currently active panel that is being displayed in the modulePanel
     */
    private JPanel activePanel;

    /**
     * Panel containing the tables view
     */
    private JPanel tablesPanel;

    /**
     * Panel providing tables editing functionality
     */
    private JPanel editPanel;

    /**
     * JPanel displaying the header and the module content.
     * Swap the active view by adding and removing the JPanel here below the header.
     */
    private JPanel modulePanel;


    private final JLayeredPane editCanvasPanel = new JLayeredPane();
    private MouseAdapter canvasMouseAdapter;
    private KeyListener canvasKeyListener;

    private final PaymentMediator paymentMediator = new PaymentMediator(
            "addStarted", "addEnded", "payStarted", "payCancel", "paySuccess");

    private final TablesModel tablesModel = new TablesModel(
            "tableAdded", "tableRemoved", "tablesSaved", "tablesLoaded");

    /**
     * Construct the panel.
     * Importantly, subscribe Listeners to listen to Mediator events.
     */
    public Plugin() {
        editCanvasPanel.setLayout(null);
        PaymentTabSwitchListener paymentTabSwitchListener = new PaymentTabSwitchListener(this, paymentMediator);
        paymentMediator.subscribe("addStarted", paymentTabSwitchListener);
        paymentMediator.subscribe("addEnded", paymentTabSwitchListener);
        paymentMediator.subscribe("payStarted", paymentTabSwitchListener);
        paymentMediator.subscribe("payCancel", paymentTabSwitchListener);
        paymentMediator.subscribe("paySuccess", paymentTabSwitchListener);

        paymentMediator.subscribe("paySuccess", new PaymentSuccessHandler());
    }


    @Override
    public String getDisplayName() {
        return "Tables";
    }


    @Override
    public JPanel makeMainPanel() {
        modulePanel = new JPanel(new GridBagLayout());
        makeContent();
        tablesModel.loadTables();
        return modulePanel;
    }

    /**
     * Interface method for receiving communication from other plugins.
     *
     * @param eventType string type of incoming event notification.
     * @param payload   object payload accompanying the message.
     */
    @Override
    public void message(String eventType, Object payload) {

    }

    /**
     * Create content for the module parent panel.
     */
    private void makeContent() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;
        gbc.weighty = 0;

        tablesPanel = makeTablesPanel();
        editPanel = makeEditPanel();
        activePanel = tablesPanel;

        HashMap<String, ActionListener> headerButtonDefs = new HashMap<>();
        headerButtonDefs.put("Tables", e -> setActivePanel(tablesPanel));
        headerButtonDefs.put("Edit", e -> setActivePanel(editPanel));

        JPanel headerPanel = DefaultComponentFactory.makeHeader(
                getDisplayName(), new Color(177, 123, 255), headerButtonDefs
        );
        modulePanel.add(headerPanel, gbc);

        gbc.weighty = 1;
        modulePanel.add(tablesPanel, gbc);
    }


    /**
     * Create panel for the display of tables.
     * @return the tables panel
     */
    private JPanel makeTablesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(App.getColor("tertiary"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;
        gbc.weighty = 1;

        JLayeredPane viewCanvasPanel = new JLayeredPane();
        viewCanvasPanel.setLayout(null);
        panel.add(viewCanvasPanel, gbc);

        TableViewListener listener = new TableViewListener(viewCanvasPanel, this);
        tablesModel.subscribe("tablesSaved", listener);
        tablesModel.subscribe("tablesLoaded", listener);
        return panel;
    }


    /**
     * Create panel for the editing of table setup.
     * Will save the edited state to DB and change tables view panel
     * @return the edit panel
     */
    private JPanel makeEditPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        Color backgroundColour = App.getColor("tertiary");
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
        editCanvasPanel.setBackground(App.getColor("tertiary"));
        editCanvasPanel.setForeground(App.getColor("tertiary"));
        editCanvasPanel.setOpaque(true);

        TableChangeListener tableListener = new TableEditorListener(editCanvasPanel);
        tablesModel.subscribe("tableAdded", tableListener);
        tablesModel.subscribe("tableRemoved", tableListener);
        tablesModel.subscribe("tablesLoaded", tableListener);
        TablesDatabaseListener databaseListener = new TablesDatabaseListener(editCanvasPanel);
        tablesModel.subscribe("tablesSaved", databaseListener);

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
    public void setActivePanel(JPanel newActivePanel) {
        if(newActivePanel == activePanel) {
            return;
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;
        gbc.weighty = 1;

        modulePanel.remove(activePanel);
        modulePanel.add(newActivePanel, gbc);
        modulePanel.revalidate();
        modulePanel.repaint();
        activePanel = newActivePanel;
    }

    /**
     * Reset the active panel to show the default tables view;
     */
    public void resetActivePanel() {
        setActivePanel(tablesPanel);
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

    /**
     * @return the Mediator of the plugin, mediating control flow events.
     */
    public PaymentMediator getPaymentMediator() {
        return paymentMediator;
    }
}
