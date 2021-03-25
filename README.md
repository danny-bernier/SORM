# SimpleORM
## This light object relational mapping (ORM) framework will allow for a simplified and SQL-free interaction with a PostGreSQL relational data source.
## https://github.com/danny-bernier/SORM/tree/main

## Goals
* ~~Basic CRUD support for managing entities in database~~
* ~~Hands off use of ORM requiring little knowledge of SQL~~
* ~~Easy to define objects to be stored in database~~
* As light as possible
* ~~Relationships between objects reflected in database~~
* ~~XML for storing/configuring connection settings~~
* 70% minimum coverage with JUnit & JaCoCo

## Potential Stretch Goals
* Basic transaction management (begin, commit, savepoint, rollback)
* Connection pooling
* ~~Multithreading support for executing queries~~
* ~~Define relationships and database structure~~
  * ~~Annotations for marking fields~~
    
## Tech Involved
* Java8
* JUnit
* Maven
* PostGreSQL
* Git SCM
* Mockito
* JaCoCo
* Reflections
* H2
* XML & XSD