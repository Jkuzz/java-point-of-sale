package cz.cuni.mff.java.projects.posapp.database;

import java.util.HashMap;


public interface DBClient {
    HashMap<String, DBTableDef> getTableDefs();
}
