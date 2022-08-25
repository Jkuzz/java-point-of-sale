package cz.cuni.mff.java.projects.posapp.database;


public interface DBClient {
    Iterable<DBTableDef> getTableDefs();
}
