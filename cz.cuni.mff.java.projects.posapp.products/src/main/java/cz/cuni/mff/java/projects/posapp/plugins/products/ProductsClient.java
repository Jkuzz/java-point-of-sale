package cz.cuni.mff.java.projects.posapp.plugins.products;

import cz.cuni.mff.java.projects.posapp.database.DBClient;
import cz.cuni.mff.java.projects.posapp.database.DBTableDef;
import cz.cuni.mff.java.projects.posapp.database.DevClient;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductsClient implements DBClient {

    @Override
    public Iterable<DBTableDef> getTableDefs() {
        ArrayList<DBTableDef> tableDefs = new ArrayList<>();
        tableDefs.add(new ProductsClient.ProductTableDef());
        return tableDefs;
    }


    public static class ProductTableDef implements DBTableDef {

        @Override
        public HashMap<String, String> getTableSchema() {
            HashMap<String, String> tableCols = new HashMap<>();
            tableCols.put("id", "INTEGER not NULL AUTO_INCREMENT");
            tableCols.put("name", "VARCHAR(255) not NULL");
            tableCols.put("price", "INTEGER not NULL");
            return tableCols;
        }

        @Override
        public String getTableName() {
            return "products";
        }

        @Override
        public String getPrimaryKey() {
            return "id";
        }
    }
}
