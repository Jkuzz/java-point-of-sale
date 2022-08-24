package cz.cuni.mff.java.projects.posapp.plugins;

import javax.swing.*;

public interface POSPlugin {
    String getDisplayName();
    JPanel makeMainPanel();
}
