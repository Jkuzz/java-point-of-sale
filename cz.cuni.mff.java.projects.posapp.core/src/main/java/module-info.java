module cz.cuni.mff.java.projects.posapp.core {
    requires java.logging;
    requires java.desktop;

    exports cz.cuni.mff.java.projects.posapp.plugins;

    uses cz.cuni.mff.java.projects.posapp.plugins.POSPlugin;
}