package datability;

import org.junit.ClassRule;
import org.junit.Test;

import java.sql.Connection;

public class PostgreSqlTest {

    @ClassRule
    public static PostgresqlRule postgreSql = new PostgresqlRule();

    @Test
    public void drop_not_nulls_on_single_table() throws Exception {
        postgreSql.execute("drop table if exists mytable", "create table mytable (a int not null)");

        try (Connection connection = postgreSql.openConnection()) {
            Databases.postgresql(connection).dropNotNulls("mytable");
            connection.createStatement().execute("insert into mytable(a) values (null)");
        }
    }

    @Test
    public void drop_not_nulls_on_single_table_with_many_not_nulls() throws Exception {
        postgreSql.execute("drop table if exists mytable", "create table mytable (a int not null, b text not null)");

        try (Connection connection = postgreSql.openConnection()) {
            Databases.postgresql(connection).dropNotNulls("mytable");
            connection.createStatement().execute("insert into mytable(a,b) values (null,null)");
        }
    }

    @Test
    public void drop_not_nulls_on_single_table_with_primary_key() throws Exception {
        postgreSql.execute("drop table if exists mytable", "create table mytable (a int primary key not null, b int not null)");

        try (Connection connection = postgreSql.openConnection()) {
            Databases.postgresql(connection).dropNotNulls("mytable");
            connection.createStatement().execute("insert into mytable(a,b) values (1, null)");
        }
    }

    @Test
    public void drop_not_nulls_on_single_table_with_composite_primary_key() throws Exception {
        postgreSql.execute("drop table if exists mytable", "create table mytable (a int not null, b int not null, c int not null, primary key (a,b))");

        try (Connection connection = postgreSql.openConnection()) {
            Databases.postgresql(connection).dropNotNulls("mytable");
            connection.createStatement().execute("insert into mytable(a,b,c) values (1,1,null)");
        }
    }

    @Test
    public void drop_not_nulls_on_multiple_tables() throws Exception {
        postgreSql.execute(
                "drop table if exists mytable", "create table mytable (a int not null, b text not null)",
                "drop table if exists mytable2", "create table mytable2 (c int not null, d text not null)");

        try (Connection connection = postgreSql.openConnection()) {
            Databases.postgresql(connection).dropNotNulls("mytable", "mytable2");
            connection.createStatement().execute("insert into mytable(a,b) values (null, null)");
            connection.createStatement().execute("insert into mytable2(c,d) values (null, null)");
        }
    }

    @Test
    public void drop_primary_keys_on_single_table() throws Exception {
        postgreSql.execute("drop table if exists mytable", "create table mytable (a int primary key)");

        try (Connection connection = postgreSql.openConnection()) {
            Databases.postgresql(connection).dropPrimaryKeys("mytable");
            connection.createStatement().execute("insert into mytable(a) values (1)");
            connection.createStatement().execute("insert into mytable(a) values (1)");
        }
    }

    @Test
    public void drop_primary_keys_on_multiple_tables() throws Exception {
        postgreSql.execute(
                "drop table if exists mytable", "create table mytable (a int primary key)",
                "drop table if exists mytable2", "create table mytable2 (b int primary key)");

        try (Connection connection = postgreSql.openConnection()) {
            Databases.postgresql(connection).dropPrimaryKeys("mytable", "mytable2");
            connection.createStatement().execute("insert into mytable(a) values (1)");
            connection.createStatement().execute("insert into mytable(a) values (1)");
            connection.createStatement().execute("insert into mytable2(b) values (1)");
            connection.createStatement().execute("insert into mytable2(b) values (1)");
        }
    }
}