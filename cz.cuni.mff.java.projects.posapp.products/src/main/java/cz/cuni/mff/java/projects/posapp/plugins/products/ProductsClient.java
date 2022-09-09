package cz.cuni.mff.java.projects.posapp.plugins.products;

import cz.cuni.mff.java.projects.posapp.core.Pair;
import cz.cuni.mff.java.projects.posapp.database.DBClient;
import cz.cuni.mff.java.projects.posapp.database.DBTableDef;

import java.util.HashMap;


/**
 * DB Client for the Products plugin. Hold table defs required by the plugin.
 */
public class ProductsClient implements DBClient {

    @Override
    public HashMap<String, DBTableDef> getTableDefs() {
        HashMap<String, DBTableDef> tableDefs = new HashMap<>();
        tableDefs.put("products", new ProductsClient.ProductTableDef());
        tableDefs.put("product_groups", new ProductsClient.ProductGroupTableDef());
        return tableDefs;
    }


    /**
     * Defines the Products table. Holds what products are available and which group they belong to.
     */
    public static class ProductTableDef implements DBTableDef {

        /**
         * Define the table schema by listing columns and their data types
         *
         * @return Map of column name: column type
         */
        @Override
        public HashMap<String, String> getTableSchema() {
            HashMap<String, String> tableCols = new HashMap<>();
            tableCols.put("id", "INTEGER not NULL AUTO_INCREMENT");
            tableCols.put("name", "VARCHAR(255) not NULL");
            tableCols.put("price", "INTEGER not NULL");
            tableCols.put("group_id", "INTEGER NULL");
            return tableCols;
        }

        /**
         * Define the primary key of the table. Key must be defined in getTableSchema()!
         * Does not ensure uniqueness. Either perform manually or make the type AUTO_INCREMENT.
         *
         * @return column to be primary key
         */
        @Override
        public String getPrimaryKey() {
            return "id";
        }

        /**
         * Defines foreign keys of the table.
         * <p>
         * Returns hashMap, whose keys define columns of the table that are foreign keys.
         * values of the hashmap are pairs table:column, targeting the foreign unique key.
         *
         * @return hashMap defining foreign keys.
         */
        @Override
        public HashMap<String, Pair<String, String>> getForeignKeys() {
            HashMap<String, Pair<String, String>> foreignKeys = new HashMap<>();
            foreignKeys.put("group_id", new Pair<>("product_groups", "id"));
            return foreignKeys;
        }
    }


    /**
     * Defines the Product Groups table. Holds product groups, a hierarchical structure.
     * Products can be assigned to a product group vie foreign key.
     */
    public static class ProductGroupTableDef implements DBTableDef {
        /**
         * Defines foreign keys of the table.
         * <p>
         * Returns hashMap, whose keys define columns of the table that are foreign keys.
         * values of the hashmap are pairs table:column, targeting the foreign unique key.
         *
         * @return hashMap defining foreign keys.
         */
        @Override
        public HashMap<String, Pair<String, String>> getForeignKeys() {
            HashMap<String, Pair<String, String>> foreignKeys = new HashMap<>();
            foreignKeys.put("parent_id", new Pair<>("product_groups", "id"));
            return foreignKeys;
        }

        /**
         * Define the table schema by listing columns and their data types
         *
         * @return Map of column name: column type
         */
        @Override
        public HashMap<String, String> getTableSchema() {
            HashMap<String, String> tableCols = new HashMap<>();
            tableCols.put("id", "INTEGER not NULL AUTO_INCREMENT");
            tableCols.put("name", "VARCHAR(255) not NULL");
            tableCols.put("parent_id", "INTEGER NULL");
            return tableCols;
        }

        /**
         * Define the primary key of the table. Key must be defined in getTableSchema()!
         * Does not ensure uniqueness. Either perform manually or make the type AUTO_INCREMENT.
         *
         * @return column to be primary key
         */
        @Override
        public String getPrimaryKey() {
            return "id";
        }
    }
}
