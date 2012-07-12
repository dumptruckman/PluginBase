/**
 * MySQL
 * Inherited subclass for making a connection to a MySQL server.
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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Both
 */
//import java.net.MalformedURLException;
//import java.util.logging.Logger;

public class MySQL extends DatabaseHandler {

    private String hostname = "localhost";
    private String portnmbr = "3306";
    private String username = "minecraft";
    private String password = "";
    private String database = "minecraft";

    public MySQL(String hostname,
                 String portnmbr,
                 String database,
                 String username,
                 String password) {
        super("[MySQL] ");
        this.hostname = hostname;
        this.portnmbr = portnmbr;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    /*@Override
     public void writeInfo(String toWrite) {
         if (toWrite != null) {
             this.log.info(this.PREFIX + this.prefix + toWrite);
         }
     }

     @Override
     public void writeError(String toWrite, boolean severe) {
         if (toWrite != null) {
             if (severe) {
                 this.log.severe(this.PREFIX + this.prefix + toWrite);
             } else {
                 this.log.warning(this.PREFIX + this.prefix + toWrite);
             }
         }
     }*/

    @Override
    protected boolean initialize() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); // Check that server's Java has MySQL support.
            return true;
        } catch (ClassNotFoundException e) {
            this.writeError("You need the MySQL library " + e, true);;
            return false;
        }
    }

    @Override
    public Connection open() {
        if (initialize()) {
            String url = "";
            try {
                url = "jdbc:mysql://" + this.hostname + ":" + this.portnmbr + "/" + this.database;
                Logging.finest("Connecting to '" + url + "' with username '" + username + "' and pass '" + password + "'");
                this.connection = DriverManager.getConnection(url, this.username, this.password);
                return this.connection;
            } catch (SQLException e) {
                this.writeError(url, true);
                this.writeError("Could not be resolved because of an SQL Exception: " + e.getMessage() + ".", true);
                return null;
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
            this.writeError("Error in SQL query: " + ex.getMessage(), false);
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkTable(String table) {
        if (!checkConnection()) {
            Logging.severe("Database connection is closed!");
            return false;
        }
        try {
            Statement statement = getConnection().createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM " + table);
            return result != null;
        } catch (SQLException e) {
            if (e.getMessage().contains("exist")) {
                return false;
            } else {
                this.writeError("Error in SQL query: " + e.getMessage(), false);
                e.printStackTrace();
            }
        }

        return (query("SELECT * FROM " + table) == null);
    }

    @Override
    public boolean wipeTable(String table) {
        if (!checkConnection()) {
            Logging.severe("Database connection is closed!");
            return false;
        }
        try {
            if (!this.checkTable(table)) {
                this.writeError("Error wiping table: \"" + table + "\" does not exist.", true);
                return false;
            }
            Statement statement = getConnection().createStatement();
            String query = "DELETE FROM " + table + ";";
            statement.executeUpdate(query);
            return true;
        } catch (SQLException ignore) { }
        return false;
    }
}