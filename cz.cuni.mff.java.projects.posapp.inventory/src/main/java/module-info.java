module cz.cuni.mff.java.projects.posapp.inventory {
    requires cz.cuni.mff.java.projects.posapp.core;
    requires cz.cuni.mff.java.projects.posapp.database;
    requires java.desktop;
    requires java.sql;

    exports cz.cuni.mff.java.projects.posapp.plugins.inventory;

    provides cz.cuni.mff.java.projects.posapp.plugins.POSPlugin
            with cz.cuni.mff.java.projects.posapp.plugins.inventory.Inventory;
}