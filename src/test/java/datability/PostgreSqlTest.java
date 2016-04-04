package datability;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class PostgreSqlTest {

    @Test
    public void drop_not_nulls_on_single_table() throws Exception {
        executeSql("drop table if exists mytable", "create table mytable (a int not null)");

        try (Connection connection = openConnection()) {
            Databases.postgresql(connection).dropNotNulls("mytable");
            connection.createStatement().execute("insert into mytable(a) values (null)");
        }
    }

    @Test
    public void drop_not_nulls_on_single_table_with_many_not_nulls() throws Exception {
        executeSql("drop table if exists mytable", "create table mytable (a int not null, b text not null)");

        try (Connection connection = openConnection()) {
            Databases.postgresql(connection).dropNotNulls("mytable");
            connection.createStatement().execute("insert into mytable(a,b) values (null,null)");
        }
    }

    @Test
    public void drop_not_nulls_on_single_table_with_primary_key() throws Exception {
        executeSql("drop table if exists mytable", "create table mytable (a int primary key not null, b int not null)");

        try (Connection connection = openConnection()) {
            Databases.postgresql(connection).dropNotNulls("mytable");
            connection.createStatement().execute("insert into mytable(a,b) values (1, null)");
        }
    }

    @Test
    public void drop_not_nulls_on_single_table_with_composite_primary_key() throws Exception {
        executeSql("drop table if exists mytable", "create table mytable (a int not null, b int not null, c int not null, primary key (a,b))");

        try (Connection connection = openConnection()) {
            Databases.postgresql(connection).dropNotNulls("mytable");
            connection.createStatement().execute("insert into mytable(a,b,c) values (1,1,null)");
        }
    }

    @Test
    public void drop_not_nulls_on_multiple_tables() throws Exception {
        executeSql(
                "drop table if exists mytable", "create table mytable (a int not null, b text not null)",
                "drop table if exists mytable2", "create table mytable2 (c int not null, d text not null)");

        try (Connection connection = openConnection()) {
            Databases.postgresql(connection).dropNotNulls("mytable", "mytable2");
            connection.createStatement().execute("insert into mytable(a,b) values (null, null)");
            connection.createStatement().execute("insert into mytable2(c,d) values (null, null)");
        }
    }

    @Test
    public void drop_primary_keys_on_single_table() throws Exception {
        executeSql("drop table if exists mytable", "create table mytable (a int primary key)");

        try (Connection connection = openConnection()) {
            Databases.postgresql(connection).dropPrimaryKeys("mytable");
            connection.createStatement().execute("insert into mytable(a) values (1)");
            connection.createStatement().execute("insert into mytable(a) values (1)");
        }
    }

    @Test
    public void drop_primary_keys_on_multiple_tables() throws Exception {
        executeSql(
                "drop table if exists mytable", "create table mytable (a int primary key)",
                "drop table if exists mytable2", "create table mytable2 (b int primary key)");

        try (Connection connection = openConnection()) {
            Databases.postgresql(connection).dropPrimaryKeys("mytable", "mytable2");
            connection.createStatement().execute("insert into mytable(a) values (1)");
            connection.createStatement().execute("insert into mytable(a) values (1)");
            connection.createStatement().execute("insert into mytable2(b) values (1)");
            connection.createStatement().execute("insert into mytable2(b) values (1)");
        }
    }

    @Test
    public void drop_composed_primary_key() throws Exception {
        executeSql("drop table if exists mytable",
                "create table mytable (a int, b int, constraint pk primary key (a, b))");

        try (Connection connection = openConnection()) {
            Databases.postgresql(connection).dropPrimaryKeys("mytable");
            connection.createStatement().execute("insert into mytable(a, b) values (1, 1)");
            connection.createStatement().execute("insert into mytable(a, b) values (1, 1)");
        }
    }

    @Test
    public void drop_foreign_keys_on_single_table() throws Exception {
        executeSql(
                "drop table if exists foreigntable", "drop table if exists srctable",
                "create table srctable (id int primary key)",
                "create table foreigntable (fk int, foreign key (fk) REFERENCES srctable(id))"
        );

        try (Connection connection = openConnection()) {
            Databases.postgresql(connection).dropForeignKeys("foreigntable");
            connection.createStatement().execute("insert into foreigntable(fk) values (1)");
        }
    }

    @Test
    public void drop_foreign_keys_on_multiples_tables() throws Exception {
        executeSql(
                "drop table if exists foreigntable", "drop table if exists srctable",
                "create table srctable (id int primary key, col int not null)",
                "create table foreigntable (fk int primary key, foreign key (fk) REFERENCES srctable(id))"
        );

        try (Connection connection = openConnection()) {
            Databases.postgresql(connection).dropAll("foreigntable", "srctable");
            connection.createStatement().execute("insert into srctable(id, col) values (null, null)");
            connection.createStatement().execute("insert into foreigntable(fk) values (null)");
        }
    }

    @Test
    public void drop_all() throws Exception {
        executeSql(
                "drop table if exists foreignforeigntable", "drop table if exists foreigntable", "drop table if exists srctable",
                "create table srctable (id int primary key)",
                "create table foreigntable (fk int primary key, foreign key (fk) REFERENCES srctable(id))",
                "create table foreignforeigntable (fkfk int, foreign key (fkfk) REFERENCES foreigntable(fk))"
        );

        try (Connection connection = openConnection()) {
            Databases.postgresql(connection).dropForeignKeys("foreignforeigntable", "foreigntable");
            connection.createStatement().execute("insert into foreignforeigntable(fkfk) values (1)");
            connection.createStatement().execute("insert into foreigntable(fk) values (1)");
        }
    }

    @Test(expected = DatabaseException.class)
    public void closed_connection() throws Exception {
        Connection connection = mock(Connection.class);
        given(connection.isClosed()).willReturn(true);

        Databases.postgresql(connection);
    }

    @Test
    public void no_op_given_no_table_given() throws Exception {
        Connection connection = mock(Connection.class);

        Databases.postgresql(connection)
                .dropNotNulls()
                .dropPrimaryKeys()
                .dropForeignKeys()
                .dropAll();

        verify(connection).isClosed();
        verifyNoMoreInteractions(connection);
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/datability", "postgres", "");
    }

    private void executeSql(String... sqls) throws SQLException {
        try (Connection conn = openConnection()) {
            for (String sql : sqls) {
                conn.createStatement().execute(sql);
            }
        }
    }
}