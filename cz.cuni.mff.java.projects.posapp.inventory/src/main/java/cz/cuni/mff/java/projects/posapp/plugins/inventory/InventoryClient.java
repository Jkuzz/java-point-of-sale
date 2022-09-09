package cz.cuni.mff.java.projects.posapp.plugins.inventory;

import cz.cuni.mff.java.projects.posapp.database.DBClient;
import cz.cuni.mff.java.projects.posapp.database.DBTableDef;

import java.util.HashMap;

/**
 * DB Client for the Inventory plugin. Hold table defs required by the plugin.
 */
public class InventoryClient implements DBClient {

    @Override
    public HashMap<String, DBTableDef> getTableDefs() {
        HashMap<String, DBTableDef> tableDefs = new HashMap<>();
        tableDefs.put("inventory", new InventoryClient.InventoryTableDef());
        return tableDefs;
    }


    /**
     * Defines the Inventory table. Holds amounts of products in Inventory.
     */
    public static class InventoryTableDef implements DBTableDef {

        @Override
        public HashMap<String, String> getTableSchema() {
            HashMap<String, String> tableCols = new HashMap<>();
            tableCols.put("id", "INTEGER not NULL AUTO_INCREMENT");
            tableCols.put("amount", "INTEGER not NULL");
            return tableCols;
        }

        @Override
        public String getPrimaryKey() {
            return "id";
        }
    }
}
