package cz.cuni.mff.java.projects.posapp.plugins.payment;

import cz.cuni.mff.java.projects.posapp.core.Pair;
import cz.cuni.mff.java.projects.posapp.database.DBClient;
import cz.cuni.mff.java.projects.posapp.database.DBTableDef;

import java.util.HashMap;


public class PaymentClient implements DBClient {

    @Override
    public HashMap<String, DBTableDef> getTableDefs() {
        HashMap<String, DBTableDef> tableDefs = new HashMap<>();
        tableDefs.put("transactions", new PaymentClient.ProductTableDef());
        tableDefs.put("transaction_products", new PaymentClient.ProductGroupTableDef());
        return tableDefs;
    }


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
            tableCols.put("price", "FLOAT not NULL");
            tableCols.put("time_opened", "TIMESTAMP not NULL DEFAULT CURRENT_TIMESTAMP");
            tableCols.put("time_closed", "TIMESTAMP not NULL DEFAULT CURRENT_TIMESTAMP");
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
            foreignKeys.put("transaction_id", new Pair<>("transactions", "id"));
            foreignKeys.put("product_id", new Pair<>("products", "id"));
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
            tableCols.put("transaction_id", "INTEGER not NULL");
            tableCols.put("amount", "INTEGER not NULL");
            tableCols.put("product_id", "INTEGER not NULL");
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
            return "(transaction_id, product_id)";
        }
    }
}
