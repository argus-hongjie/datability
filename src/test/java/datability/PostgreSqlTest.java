package datability;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;

public class PostgreSqlTest {

    @ClassRule
    public static PostgresqlRule postgreSql = new PostgresqlRule();

    @Test
    public void table_with_not_null() throws Exception {
        postgreSql.execute("drop table if exists mytable", "create table mytable (a int not null)");

        try (Connection connection = postgreSql.openConnection()) {
            Databases.postgresql(connection).disableNotNulls("mytable");
            connection.createStatement().execute("insert into mytable(a) values (null)");
        }
    }

    @Test
    public void table_with_many_not_nulls() throws Exception {
        postgreSql.execute("drop table if exists mytable", "create table mytable (a int not null, b text not null)");

        try (Connection connection = postgreSql.openConnection()) {
            Databases.postgresql(connection).disableNotNulls("mytable");
            connection.createStatement().execute("insert into mytable(a,b) values (null,null)");
        }
    }

    @Test
    public void two_tables_with_not_nulls() throws Exception {
        postgreSql.execute(
                "drop table if exists tablea", "create table tablea (a int not null, b text not null)",
                "drop table if exists tableb", "create table tableb (c int not null, d text not null)");

        try (Connection connection = postgreSql.openConnection()) {
            Databases.postgresql(connection).disableNotNulls("tablea", "tableb");
            connection.createStatement().execute("insert into tablea(a,b) values (null, null)");
            connection.createStatement().execute("insert into tableb(c,d) values (null, null)");
        }
    }

}