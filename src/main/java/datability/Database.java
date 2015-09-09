package datability;

public interface Database {

    /**
     * Drop not nulls checks
     */
    Database dropNotNulls(String... tables);

    /**
     * Drop primary key constraints.
     *
     * This remove unique constraints which are in fact primary keys.
     */
    Database dropPrimaryKeys(String... tables);
}
