package datability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcOperations;

import javax.sql.DataSource;
import java.util.List;

public class Datability {

    private static final Logger LOG = LoggerFactory.getLogger(Datability.class);

    private JdbcOperations jdbc;

    public Datability(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    public Datability disableNotNull(String table) {
        findNotNullColumns(table).stream()
                .forEach(column -> {
                            LOG.debug("Disabling not-null on column '{}' of table '{}'", column, table);
                            jdbc.execute("ALTER TABLE " + table + " ALTER " + column + " DROP NOT NULL");
                        }
                );
        return this;
    }

    public Datability disableForeignKeys(String table) {
        findForeignKeys(table).stream()
                .forEach(fk -> {
                    LOG.debug("Disabling foreign key '{}' of table '{}'", fk, table);
                    jdbc.execute("alter table " + table + " drop CONSTRAINT " + fk);
                });
        return this;
    }

    private List<String> findNotNullColumns(String table) {
        List<String> notNulls = jdbc.queryForList("SELECT DISTINCT column_name FROM INFORMATION_SCHEMA.COLUMNS" +
                " WHERE column_name IS NOT NULL" +
                " AND columns.table_name = '" + table + "'" +
                " AND columns.is_nullable='NO'", String.class);

        notNulls.removeAll(findPrimaryKeys(table));
        return notNulls;
    }

    private List<String> findPrimaryKeys(String table) {
        return jdbc.queryForList("SELECT a.attname" +
                " FROM pg_index i JOIN pg_attribute a ON a.attrelid = i.indrelid AND a.attnum = ANY (i.indkey)" +
                " WHERE i.indrelid = '" + table + "' :: REGCLASS AND i.indisprimary", String.class);

    }

    private List<String> findForeignKeys(String table) {
        return jdbc.queryForList("SELECT constraint_name FROM information_schema.table_constraints" +
                " WHERE constraint_type = 'FOREIGN KEY' AND table_name='" + table + "'", String.class);
    }

}