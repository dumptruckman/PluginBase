/**
 * Database Handler
 * Abstract superclass for all subclass database files.
 *
 * Date Created: 2011-08-26 19:08
 * @author PatPeter
 */
package com.dumptruckman.minecraft.pluginbase.database;

/*
 * MySQL
 */

import com.dumptruckman.minecraft.pluginbase.util.Logging;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 *  SQLLite
 */
//import java.io.File;
//import java.sql.DatabaseMetaData;
/*
 *  Both
 */
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;

public abstract class DatabaseHandler {

    protected final String prefix;
    protected Connection connection;

    protected enum Statements {
        SELECT, INSERT, UPDATE, DELETE, DO, REPLACE, LOAD, HANDLER, CALL, // Data manipulation statements
        CREATE, ALTER, DROP, TRUNCATE, RENAME  // Data definition statements
    }

    /*
      *  MySQL, SQLLite
      */

    public DatabaseHandler(String prefix) {
        this.prefix = prefix;
        this.connection = null;
    }

    /**
     * <b>writeInfo</b><br>
     * <br>
     * &nbsp;&nbsp;Writes information to the console.
     * <br>
     * <br>
     *
     * @param toWrite - the <a href="http://download.oracle.com/javase/6/docs/api/java/lang/String.html">String</a>
     *                of content to write to the console.
     */
    protected void writeInfo(String toWrite) {
        if (toWrite != null) {
            Logging.info(this.prefix + toWrite);
        }
    }

    /**
     * <b>writeError</b><br>
     * <br>
     * &nbsp;&nbsp;Writes either errors or warnings to the console.
     * <br>
     * <br>
     *
     * @param toWrite - the <a href="http://download.oracle.com/javase/6/docs/api/java/lang/String.html">String</a>
     *                written to the console.
     * @param severe  - whether console output should appear as an error or warning.
     */
    protected void writeError(String toWrite, boolean severe) {
        if (toWrite != null && !toWrite.contains("Duplicate")) {
            if (severe) {
                Logging.severe(this.prefix + toWrite);
            } else {
                Logging.warning(this.prefix + toWrite);
            }
        }
    }

    /**
     * <b>initialize</b><br>
     * <br>
     * &nbsp;&nbsp;Used to hasPerm whether the class for the SQL engine is installed.
     * <br>
     * <br>
     */
    abstract boolean initialize();

    /**
     * <b>open</b><br>
     * <br>
     * &nbsp;&nbsp;Opens a connection with the database.
     * <br>
     * <br>
     *
     * @return the success of the method.
     */
    abstract Connection open();

    /**
     * <b>close</b><br>
     * <br>
     * &nbsp;&nbsp;Closes a connection with the database.
     * <br>
     * <br>
     */
    public void close() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                this.writeError("Failed to close database connection: " + e.getMessage(), true);
            }
        }
    }

    /**
     * <b>getConnection</b><br>
     * <br>
     * &nbsp;&nbsp;Gets the connection variable
     * <br>
     * <br>
     *
     * @return the <a href="http://download.oracle.com/javase/6/docs/api/java/sql/Connection.html">Connection</a> variable.
     */
    public Connection getConnection() {
        return this.connection == null ? open() : this.connection;
    }

    /**
     * <b>checkConnection</b><br>
     * <br>
     * Checks the connection between Java and the database engine.
     * <br>
     * <br>
     *
     * @return the status of the connection, true for up, false for down.
     */
    public boolean checkConnection() {
        return this.connection != null || open() != null;
    }

    /**
     * <b>query</b><br>
     * &nbsp;&nbsp;Sends a query to the SQL database.
     * <br>
     * <br>
     *
     * @param query - the SQL query to send to the database.
     * @return the table of results from the query.
     */
    abstract ResultSet query(String query);

    /**
     * <b>getStatement</b><br>
     * <p/>
     * <br>
     * <br>
     */
    protected Statements getStatement(String query) {
        String trimmedQuery = query.trim();
        if (trimmedQuery.substring(0, 6).equals("SELECT"))
            return Statements.SELECT;
        else if (trimmedQuery.substring(0, 6).equals("INSERT"))
            return Statements.INSERT;
        else if (trimmedQuery.substring(0, 6).equals("UPDATE"))
            return Statements.UPDATE;
        else if (trimmedQuery.substring(0, 6).equals("DELETE"))
            return Statements.DELETE;
        else if (trimmedQuery.substring(0, 6).equals("CREATE"))
            return Statements.CREATE;
        else if (trimmedQuery.substring(0, 5).equals("ALTER"))
            return Statements.ALTER;
        else if (trimmedQuery.substring(0, 4).equals("DROP"))
            return Statements.DROP;
        else if (trimmedQuery.substring(0, 8).equals("TRUNCATE"))
            return Statements.TRUNCATE;
        else if (trimmedQuery.substring(0, 6).equals("RENAME"))
            return Statements.RENAME;
        else if (trimmedQuery.substring(0, 2).equals("DO"))
            return Statements.DO;
        else if (trimmedQuery.substring(0, 7).equals("REPLACE"))
            return Statements.REPLACE;
        else if (trimmedQuery.substring(0, 4).equals("LOAD"))
            return Statements.LOAD;
        else if (trimmedQuery.substring(0, 7).equals("HANDLER"))
            return Statements.HANDLER;
        else if (trimmedQuery.substring(0, 4).equals("CALL"))
            return Statements.CALL;
        else
            return Statements.SELECT;
    }

    /**
     * <b>createTable</b><br>
     * <br>
     * &nbsp;&nbsp;Creates a table in the database based on a specified query.
     * <br>
     * <br>
     *
     * @param query - the SQL query for creating a table.
     * @return the success of the method.
     */
    public boolean createTable(String query) {
        if (!checkConnection()) {
            Logging.severe("Database connection is closed!");
            return false;
        }
        try {
            if (query == null || query.equals("")) {
                this.writeError("SQL query empty: createTable(" + query + ")", true);
                return false;
            }

            Statement statement = getConnection().createStatement();
            statement.execute(query);
            return true;
        } catch (SQLException e) {
            this.writeError(e.getMessage(), true);
            return false;
        }
    }

    /**
     * <b>checkTable</b><br>
     * <br>
     * &nbsp;&nbsp;Checks a table in a database based on the table's name.
     * <br>
     * <br>
     *
     * @param table - name of the table to hasPerm.
     * @return success of the method.
     */
    abstract boolean checkTable(String table);

    /**
     * <b>wipeTable</b><br>
     * <br>
     * &nbsp;&nbsp;Wipes a table given its name.
     * <br>
     * <br>
     *
     * @param table - name of the table to wipe.
     * @return success of the method.
     */
    abstract boolean wipeTable(String table);

    /*
      *  SQLLite
      */

    /*
      * <b>retry</b><br>
      * <br>
      * Retries.
      * <br>
      * <br>
      * @param query The SQL query.
      */
    //abstract void retry(String query);

    /*
      * Retries a result.
      *
      * @param query The SQL query to retry.
      * @return The SQL query result.
      */
    //abstract ResultSet retryResult(String query);
}