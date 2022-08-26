package cz.cuni.mff.java.projects.posapp.database;

import java.util.ArrayList;
import java.util.HashMap;


public class DevClient implements DBClient{

    @Override
    public HashMap<String, DBTableDef> getTableDefs() {
        HashMap<String, DBTableDef> tableDefs = new HashMap<>();
        tableDefs.put("devTable", new DevTableDef());
        return tableDefs;
    }

    public static class DevTableDef implements DBTableDef {

        @Override
        public HashMap<String, String> getTableSchema() {
            HashMap<String, String> tableCols = new HashMap<>();
            tableCols.put("id", "INTEGER not NULL AUTO_INCREMENT");
            tableCols.put("name", "VARCHAR(255) not NULL");
            tableCols.put("value", "INTEGER not NULL");
            return tableCols;
        }


        @Override
        public String getPrimaryKey() {
            return "id";
        }
    }
}
