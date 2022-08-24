package cz.cuni.mff.java.projects.posapp.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        DBClient client = new DevClient();

        try (Database db = new Database(new DevUser(), client)){
            client.getTableDefs().forEach(def -> {
                ResultSet rs = db.query("select * from " + def.getTableName());
                try {
                    while(rs.next()) {
                        System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
