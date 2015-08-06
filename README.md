# Datability

Test what matter the most in your Integration test.

Disable constraints, primary keys, foreigh keys, not nulls, checks... and insert only the data that matters.

## Databases support

* Tested with PostgreSQL 9.4

## Todo

* Use plain Jdbc instead of spring-jdbc to avoid this dependency
* Support additional databases
  * H2
  * Hsql
  * MySQL
  * Oracle
  * SqlServer
