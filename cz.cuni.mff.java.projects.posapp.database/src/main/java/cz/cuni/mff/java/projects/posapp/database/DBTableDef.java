package cz.cuni.mff.java.projects.posapp.database;

import java.util.HashMap;

public interface DBTableDef {
    HashMap<String, String> getTableSchema();
    String getPrimaryKey();
}
