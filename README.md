# Datability

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.tomsquest/datability/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.tomsquest/datability)
[![Build Status](https://travis-ci.org/tomsquest/datability.svg?branch=master)](https://travis-ci.org/tomsquest/datability)
[![codecov.io](https://codecov.io/github/tomsquest/datability/coverage.svg?branch=master)](https://codecov.io/github/tomsquest/datability?branch=master)

Test what matter the most in your Integration test.

Drop primary keys, foreign keys, not nulls to let you test insert only the data that matters.

## Why

**TL;DR**

* Database Constraints are useful in **production**
* Some integration tests benefit of having constraints disabled
* Some database engines make this even better: PostgreSQL can rollback `Alter table` and MySql can completely ignore 
them with `set foreign_key_checks=0`

Several years ago, I contracted for a Mobile carrier. The team mission was to remotely configure mobile phone by sending 
binary SMS for NFC payments. The data model was something like this : a `Phone` has 1-* `SimCard`, a `SimCard` has a `Carrier`, a `Carrier` has 1-* 
`CarrierConfig`, a `SimCard` has 1-* `TransportProtocols`, a `TransportProtocols` has 1-* `ProtocolVersion`... and this continues 
over ~30 tables, all with many foreign keys and not nulls.

Inserting a line in one of the child tables was very tedious due to the foreign keys requiring to insert all values until 
the top of the chain. And all I wanted was to test a complex SQL query on one of these tables. 

So the idea was born : just get rid of these constraints. They are not relevant for my test case.
Eg. Why do I have to insert a `Carrier` when I just require a `SimCard` ? Let me insert a null in `carrier_fk` !.

## Usage

To use Datability in your test, add it to your pom.xml:

``` xml
<dependency>
  <groupId>com.tomsquest</groupId>
  <artifactId>datability</artifactId>
  <version>1.1.0</version>
</dependency>
```

Then in java: 

``` java
Databases.postgresql(connection)
    .dropNotNulls("mytable", "anothertable")
    .dropPrimaryKeys("mytable", "anothertable")
    .dropForeignKeys("mytable", "anothertable")
    .dropAll("yetAnotherTable"); // drop all not-nulls, primary and foreign keys
```

Full example using plain Jdbc:

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

* PostgreSQL can rollback DDL statements. Ie. if Datability is called in a Transaction, rollbacking this transaction will
let the database unmodified.

* It is not possible to drop not null on primary keys without removing the primary keys first.
So first, execute `dropPrimaryKeys()`, then `dropNotNulls()`.

### MySQL

Use `set foreign_key_checks=0` and enjoy testing. 
  
## Requirements 

Java >= 7

## Todo

* Support additional databases
  * [ ] Oracle
  * [ ] SqlServer
