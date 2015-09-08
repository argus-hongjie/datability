package datability;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;

public class PostgreSqlTest {

    @Test
    public void travisPostgresqlConnection() throws Exception {
        Class.forName("org.postgresql.Driver");
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/databilitydb", "postgres", "")) {
            connection.prepareStatement("select 1+1").execute();
        }
    }
}