module cz.cuni.mff.java.projects.posapp.tables {
    requires cz.cuni.mff.java.projects.posapp.core;
    requires java.desktop;
    requires cz.cuni.mff.java.projects.posapp.database;
    requires java.sql;
    requires cz.cuni.mff.java.projects.posapp.payment;

    exports cz.cuni.mff.java.projects.posapp.plugins.tables;
    exports cz.cuni.mff.java.projects.posapp.plugins.tables.payment;

    provides cz.cuni.mff.java.projects.posapp.plugins.POSPlugin
            with cz.cuni.mff.java.projects.posapp.plugins.tables.Plugin;
}