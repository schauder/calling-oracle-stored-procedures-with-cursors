# Calling Oracle Stored Procedures with Cursors

Calling stored procedures is a major PITA, especially when they return cursors. This repositories demonstrates how one can do it.

## Setup

Since Oracle in it's infinite wisdom doesn't make drivers available on Maven Central, let alone an in memory version of their database that one could use for demonstration stuff like this you have to do some preparation, before being able to execute this project.

1. Get an Oracle Database

    If you are interested in this topic, you probably have one available. If you don't I recommend using Docker. Either way, you'll need the jdbc URL under which you can reach the database, including username and password. The user must have privileges to create and modify stored procedures and to execute them.

2. Get the Oracle JDBC driver

    And put it in a lib folder under the project root. Check the `pom.xml` to make sure it matches the name of the jar. _Note: I do know that it is not recommended to use `system` scope, but it does it's job for this demo. Feel free to change the setup if it bothers you._

3. Copy the `src/main/resources/application.properties.template` to `src/main/resources/application.properties` and put in the username, password and the jdbc url of the database you are using.

## Executing

Run `de.schauderhaft.storedprocedure.StoredprocedureApplication`. It will create some out put on the console if everything works fine. If not you'll see stacktraces.

## Enjoy

... the feeling of having achieved something unneccessarily complicated.
