# Datability

[![Build Status](https://travis-ci.org/tomsquest/datability.svg?branch=master)](https://travis-ci.org/tomsquest/datability)

Test what matter the most in your Integration test.

Drop primary keys, foreign keys, not nulls to let you test insert only the data that matters.

## Usage

In your pom.xml:

``` xml
<dependency>
  <groupId>com.tomsquest</groupId>
  <artifactId>datability</artifactId>
  <version>1.0.0</version>
</dependency>
```

Usage example:

``` java
Databases.postgresql(connection)
    .dropNotNulls("mytable", "anothertable")
    .dropPrimaryKeys("mytable", "anothertable")
    .dropForeignKeys("mytable", "anothertable")
    .dropAll("yetAnotherTable"); // drop all not-nulls, primary and foreign keys
```

Full example (plain Jdbc):

``` java
// Obtain a Connection from DriverManager or DataSource
Connection connection = 
    DriverManager.getConnection("jdbc:postgresql://host:port/db", "user", "pass");

// Let's create a table with a nasty not null
connection.createStatement().execute("create table mytable (notnullcolumn int not null)");

// Here the magic happens : drop those constraints !
Databases.postgresql(connection)
    .dropNotNulls("mytable", "anothertable")
    .dropPrimaryKeys("mytable", "anothertable")
    .dropForeignKeys("mytable", "anothertable")
    .dropAll("yetAnotherTable";

// Success: the constraints were removed
connection.createStatement()
    .execute("insert into mytable(notnullcolumn) values (null)");
```

## Databases support

* [x] PostgreSQL 9.1 to 9.4
  * [x] Drop not-nulls
  * [x] Drop primary keys
  * [x] Drop foreign keys
  * [x] Drop all constraints (oneliner)

## Hints

### PostgreSQL

It is not possible to drop not null on primary keys without removing the primary keys first.
So first, execute `dropPrimaryKeys()`, then `dropNotNulls()`.
  
## Requirements 

Java >= 7

## Todo

* [ ] Explain Why it matters to testing only the relevant data
* Support additional databases
  * [ ] MySQL
  * [ ] Oracle
  * [ ] SqlServer