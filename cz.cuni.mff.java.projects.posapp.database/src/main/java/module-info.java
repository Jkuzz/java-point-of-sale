module cz.cuni.mff.java.projects.posapp.database {
    requires cz.cuni.mff.java.projects.posapp.core;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.java;
    requires com.electronwill.nightconfig.toml;
    requires com.electronwill.nightconfig.core;

    exports cz.cuni.mff.java.projects.posapp.database;
}