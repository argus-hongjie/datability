# Datability

[![Build Status](https://travis-ci.org/tomsquest/datability.svg?branch=master)](https://travis-ci.org/tomsquest/datability)

Test what matter the most in your Integration test.

Drop primary keys, foreigh keys, not nulls, checks... and insert only the data that matters.

## Usage

Example with PostgreSQL:

``` java
Connection connection = DriverManager
    .getConnection("jdbc:postgresql://host:port/database", "user", "pass");

// Create a test table
connection.createStatement()
    .execute("create table mytable (notnullcolumn int not null)");

// Here the magic happens : drop those nasty constraints !
Databases
    .postgresql(connection)
    .dropNotNulls("mytable", "anothertable");

// Test that the constraint where removed
connection.createStatement()
    .execute("insert into mytable(notnullcolumn) values (null)"); // Success !
```

## Databases support

* [ ] PostgreSQL 9.x
  * [x] Multiple tables
  * [x] Drop not-nulls
  * [x] Drop primary keys
  * [ ] Drop foreign keys
  * [ ] Drop checks
  * [ ] Drop all constraints (oneliner)
  
## Hints

### PostgreSQL

It is not possible to drop not null on primary keys.
So the best way to forget about not-null primary keys is to first `dropPrimaryKeys()` then `dropNotNulls()`.

## Todo

* [ ] Explain Why it matters to testing only the relevant data
* [ ] Upload to Central and update readme with <dependency>
* Support additional databases
  * [ ] MySQL
  * [ ] Oracle
  * [ ] SqlServer
* Connection to the database
  * [ ] Using a jdbc url
  * [ ] Using a DataSource
* [ ] Datability could close the opened connection
