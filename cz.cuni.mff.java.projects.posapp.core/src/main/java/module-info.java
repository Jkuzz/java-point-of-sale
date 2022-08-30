module cz.cuni.mff.java.projects.posapp.core {
    requires java.logging;
    requires java.desktop;

    exports cz.cuni.mff.java.projects.posapp.plugins;
    exports cz.cuni.mff.java.projects.posapp.core;

    uses cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;
}