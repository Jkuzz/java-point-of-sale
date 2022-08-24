package cz.cuni.mff.java.projects.posapp.database;

import java.util.ArrayList;

public interface DBClient {
    Iterable<DBTableDef> getTableDefs();
}
