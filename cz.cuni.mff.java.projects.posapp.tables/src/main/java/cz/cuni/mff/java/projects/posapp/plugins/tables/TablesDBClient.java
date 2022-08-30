package cz.cuni.mff.java.projects.posapp.plugins.tables;

import cz.cuni.mff.java.projects.posapp.database.DBClient;
import cz.cuni.mff.java.projects.posapp.database.DBTableDef;

import java.util.HashMap;

public class TablesDBClient implements DBClient {
    @Override
    public HashMap<String, DBTableDef> getTableDefs() {
        HashMap<String, DBTableDef> tables = new HashMap<>();
        tables.put("tables", new TablesTableDef());
        return tables;
    }

    public static class TablesTableDef implements DBTableDef {
        @Override
        public HashMap<String, String> getTableSchema() {
            HashMap<String, String> tableCols = new HashMap<>();
            tableCols.put("id", "INTEGER not NULL");
            tableCols.put("x", "INTEGER not NULL");
            tableCols.put("y", "INTEGER not NULL");
            tableCols.put("z", "INTEGER not NULL");
            tableCols.put("width", "INTEGER not NULL");
            tableCols.put("height", "INTEGER not NULL");
            tableCols.put("interact", "BOOL not NULL");
            return tableCols;
        }

        @Override
        public String getPrimaryKey() {
            return "id";
        }
    }
}
