module cz.cuni.mff.java.projects.posapp.tables {
    requires cz.cuni.mff.java.projects.posapp.core;
    requires java.desktop;

    exports cz.cuni.mff.java.projects.posapp.plugins.tables;

    provides cz.cuni.mff.java.projects.posapp.plugins.POSPlugin
            with cz.cuni.mff.java.projects.posapp.plugins.tables.Plugin;
}