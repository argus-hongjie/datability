# Datability

[![Build Status](https://travis-ci.org/tomsquest/datability.svg?branch=master)](https://travis-ci.org/tomsquest/datability)

Test what matter the most in your Integration test.

Drop primary keys, foreign keys, not nulls to let you test insert only the data that matters.

## Usage

Example with PostgreSQL:

``` java
// Obtain a Connection
Connection connection = DriverManager
    .getConnection("jdbc:postgresql://host:port/database", "user", "pass");

// Create a test table
connection.createStatement()
    .execute("create table mytable (notnullcolumn int not null)");

// Here the magic happens : drop those nasty constraints !
Databases.postgresql(connection)
    .dropNotNulls("mytable", "anothertable")
    .dropPrimaryKeys("mytable", "anothertable")
    .dropForeignKeys("mytable", "anothertable");

// Success: the constraints were removed
connection.createStatement()
    .execute("insert into mytable(notnullcolumn) values (null)");
```

## Databases support

* [x] PostgreSQL 9.4
  * [x] Multiple tables
  * [x] Drop not-nulls
  * [x] Drop primary keys
  * [x] Drop foreign keys
  * [x] Drop all constraints (oneliner)
  
## Hints

### PostgreSQL

It is not possible to drop not null on primary keys without removing the primary keys first.
So first, execute `dropPrimaryKeys()`, then `dropNotNulls()`.

## Todo

* [ ] Upload to Central and update readme with <dependency>
* [ ] Explain Why it matters to testing only the relevant data
* Support additional databases
  * [ ] MySQL
  * [ ] Oracle
  * [ ] SqlServer