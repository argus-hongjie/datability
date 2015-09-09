# Datability

[![Build Status](https://travis-ci.org/tomsquest/datability.svg?branch=master)](https://travis-ci.org/tomsquest/datability)

Test what matter the most in your Integration test.

Disable primary keys, foreigh keys, not nulls, checks... and insert only the data that matters.

## Databases support

* [ ] PostgreSQL 9.4
  * [x] Multiple tables
  * [x] Drop not-nulls
  * [ ] Drop primary keys
  * [ ] Drop foreign keys
  * [ ] Drop checks

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
