package datability;

import org.junit.rules.ExternalResource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresqlRule extends ExternalResource {

    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/datability", "postgres", "");
    }

    public void execute(String... sqls) throws SQLException {
        try (Connection conn = openConnection()) {
            for (String sql : sqls) {
                conn.createStatement().execute(sql);
            }
        }
    }
}
