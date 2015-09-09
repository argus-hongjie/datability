package datability;

public interface Database {

    /**
     * Drop not nulls checks.
     * <p>
     * For PostgreSQL: Primary key are normally not nulls by default (this is the case of Posgresql). 
     * So {@link #dropNotNulls} won't let you insert null in primary keys. You will have to use 
     * {@link #dropPrimaryKeys} to not have to insert values in primary keys.
     */
    Database dropNotNulls(String... tables);

    /**
     * Drop primary key constraints.
     * <p/>
     * This remove unique constraints which are in fact primary keys.
     */
    Database dropPrimaryKeys(String... tables);
}
