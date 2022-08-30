module cz.cuni.mff.java.projects.posapp.payment {

    requires cz.cuni.mff.java.projects.posapp.core;
    requires java.desktop;
    requires cz.cuni.mff.java.projects.posapp.database;
    requires java.sql;

    exports cz.cuni.mff.java.projects.posapp.plugins.payment;

    provides cz.cuni.mff.java.projects.posapp.plugins.POSPlugin
    with cz.cuni.mff.java.projects.posapp.plugins.payment.Plugin;
}