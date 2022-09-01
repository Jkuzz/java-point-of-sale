package cz.cuni.mff.java.projects.posapp.database;

import cz.cuni.mff.java.projects.posapp.core.Pair;

import java.util.HashMap;

/**
 * Definition of a table as requied by a plugin. The TableDef will be inserted into the database.
 * After validation, the appropriate table is guaranteed to exist.
 */
public interface DBTableDef {
    /**
     * Define the table schema by listing columns and their data types
     * @return Map of column name: column type
     */
    HashMap<String, String> getTableSchema();

    /**
     * Define the primary key of the table. Key must be defined in getTableSchema()!
     * Does not ensure uniqueness. Either perform manually or make the type AUTO_INCREMENT.
     * @return column to be primary key
     */
    String getPrimaryKey();

    /**
     * Defines foreign keys of the table.
     *
     * Returns hashMap, whose keys define columns of the table that are foreign keys.
     * values of the hashmap are pairs table:column, targeting the foreign unique key.
     * @return hashMap defining foreign keys.
     */
    default HashMap<String, Pair<String, String>> getForeignKeys() {
        return new HashMap<>();
    }
}
