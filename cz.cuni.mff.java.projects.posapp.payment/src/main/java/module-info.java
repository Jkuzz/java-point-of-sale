module cz.cuni.mff.java.projects.posapp.payment {
    exports cz.cuni.mff.java.projects.posapp.plugins.payment;

    requires java.desktop;
    requires java.sql;
    requires cz.cuni.mff.java.projects.posapp.core;
    requires cz.cuni.mff.java.projects.posapp.database;
    requires cz.cuni.mff.java.projects.posapp.products;
    requires static cz.cuni.mff.java.projects.posapp.inventory;

    provides cz.cuni.mff.java.projects.posapp.plugins.POSPlugin
    with cz.cuni.mff.java.projects.posapp.plugins.payment.Plugin;
}