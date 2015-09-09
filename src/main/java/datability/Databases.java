package datability;

import java.sql.Connection;

public class Databases {
    public static Database postgresql(Connection connection) {
        return new Postgresql(connection);
    }
}
