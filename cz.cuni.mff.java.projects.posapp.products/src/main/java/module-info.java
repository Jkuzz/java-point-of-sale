module cz.cuni.mff.java.projects.posapp.products {
    requires java.desktop;
    requires cz.cuni.mff.java.projects.posapp.core;
    requires cz.cuni.mff.java.projects.posapp.database;
    requires java.sql;

    exports cz.cuni.mff.java.projects.posapp.plugins.products;

    provides cz.cuni.mff.java.projects.posapp.plugins.POSPlugin
            with cz.cuni.mff.java.projects.posapp.plugins.products.Plugin;

}