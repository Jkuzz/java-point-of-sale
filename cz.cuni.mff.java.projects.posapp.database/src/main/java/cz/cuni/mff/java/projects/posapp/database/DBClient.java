package cz.cuni.mff.java.projects.posapp.database;

import java.util.HashMap;


/**
 * Plugins define their Database table requirements using this Client class
 */
public interface DBClient {
    /**
     * The database will get the client's table defs and use them to ensure the plugin's
     * required tables are present in the database before allowing for queries to the DB.
     * @return the plugin's required table defs
     */
    HashMap<String, DBTableDef> getTableDefs();
}
