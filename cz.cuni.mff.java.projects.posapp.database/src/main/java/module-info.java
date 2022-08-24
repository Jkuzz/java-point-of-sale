module cz.cuni.mff.java.projects.posapp.booking {
    requires cz.cuni.mff.java.projects.posapp.core;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.java;

    exports cz.cuni.mff.java.projects.posapp.database;
}