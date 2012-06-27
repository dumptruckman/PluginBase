/**
 * SQLite
 * Inherited subclass for reading and writing to and from an SQLite file.
 *
 * Date Created: 2011-08-26 19:08
 * @author PatPeter
 */
package com.dumptruckman.minecraft.pluginbase.database;

/*
 * SQLite
 */

import com.dumptruckman.minecraft.pluginbase.util.Logging;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Both
 */
//import java.net.MalformedURLException;

public class SQLite extends DatabaseHandler {

    public String name;
    private File sqlFile;

    public SQLite(String name, File folder) {
        super("[SQLite] ");
        this.name = name;
        if (this.name.contains("/") ||
                this.name.contains("\\") ||
                this.name.endsWith(".db")) {
            this.writeError("The database name can not contain: /, \\, or .db", true);
        }
        if (!folder.exists()) {
            folder.mkdir();
        }

        sqlFile = new File(folder.getAbsolutePath() + File.separator + name + ".db");
    }

    /*@Override
     public void writeInfo(String toWrite) {
         if (toWrite != null) {
             this.log.info(this.PREFIX + this.prefix + toWrite);
         }
     }

     @Override
     public void writeError(String toWrite, boolean severe) {
         if (severe) {
             if (toWrite != null) {
                 this.log.severe(this.PREFIX + this.prefix + toWrite);
             }
         } else {
             if (toWrite != null) {
                 this.log.warning(this.PREFIX + this.prefix + toWrite);
             }
         }
     }*/

    protected boolean initialize() {
        try {
            Class.forName("org.sqlite.JDBC");
            return true;
        } catch (ClassNotFoundException e) {
            this.writeError("You need the SQLite library " + e, true);
            return false;
        }
    }

    @Override
    public Connection open() {
        if (initialize()) {
            try {
                this.connection = DriverManager.getConnection("jdbc:sqlite:" +
                        sqlFile.getAbsolutePath());
                return this.connection;
            } catch (SQLException e) {
                this.writeError("SQLite exception on initialize " + e, true);
            }
        }
        return this.connection;
    }

    @Override
    public ResultSet query(String query) {
        if (!checkConnection()) {
            Logging.severe("Database connection is closed!");
            return null;
        }
        try {
            Statement statement = getConnection().createStatement();

            switch (this.getStatement(query)) {
                case SELECT:
                    return statement.executeQuery(query);
                default:
                    statement.executeUpdate(query);
                    return null;
            }
        } catch (SQLException ex) {
            if (ex.getMessage().toLowerCase().contains("locking") || ex.getMessage().toLowerCase().contains("locked")) {
                return retryResult(query);
            } else {
                this.writeError("Error at SQL Query: " + ex.getMessage(), false);
            }
        }
        return null;
    }

    @Override
    public boolean checkTable(String table) {
        try {
            DatabaseMetaData dbm = getConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, table, null);
            return tables.next();
        } catch (SQLException e) {
            this.writeError("Failed to hasPerm if table \"" + table + "\" exists: " + e.getMessage(), true);
            return false;
        }
    }

    @Override
    public boolean wipeTable(String table) {
        if (!checkConnection()) {
            Logging.severe("Database connection is closed!");
            return false;
        }
        try {
            if (!this.checkTable(table)) {
                this.writeError("Error at Wipe Table: table, " + table + ", does not exist", true);
                return false;
            }
            Statement statement = getConnection().createStatement();
            String query = "DELETE FROM " + table + ";";
            statement.executeQuery(query);
            return true;
        } catch (SQLException ex) {
            if (!(ex.getMessage().toLowerCase().contains("locking") ||
                    ex.getMessage().toLowerCase().contains("locked")) &&
                    !ex.toString().contains("not return ResultSet"))
                this.writeError("Error at SQL Wipe Table Query: " + ex, false);
            return false;
        }
    }

    /*
      * <b>retry</b><br>
      * <br>
      * Retries.
      * <br>
      * <br>
      * @param query The SQL query.
      */
    public void retry(String query) {
        if (!checkConnection()) {
            Logging.severe("Database connection is closed!");
            return;
        }
        boolean passed = false;
        Statement statement;
        while (!passed) {
            try {
                statement = getConnection().createStatement();
                statement.executeQuery(query);
                passed = true;
            } catch (SQLException ex) {
                if (ex.getMessage().toLowerCase().contains("locking") || ex.getMessage().toLowerCase().contains("locked")) {
                    passed = false;
                } else {
                    this.writeError("Error at SQL Query: " + ex.getMessage(), false);
                }
            }
        }
    }

    /*
      * Retries a result.
      *
      * @param query The SQL query to retry.
      * @return The SQL query result.
      */
    public ResultSet retryResult(String query) {
        if (!checkConnection()) {
            Logging.severe("Database connection is closed!");
            return null;
        }
        Statement statement = null;
        while (true) {
            try {
                //Connection connection = getConnection();
                this.connection = this.getConnection();
                statement = this.connection.createStatement();
                return statement.executeQuery(query);
            } catch (SQLException ex) {
                if (ex.getMessage().toLowerCase().contains("locking") || ex.getMessage().toLowerCase().contains("locked")) {
                } else {
                    this.writeError("Error at SQL Query: " + ex.getMessage(), false);
                    return null;
                }
            }
        }
    }
}