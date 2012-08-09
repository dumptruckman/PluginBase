/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

abstract class SQLDB implements SQLDatabase {

    private final SQLConnectionPool connectionPool;

    protected enum Statements {
        SELECT, INSERT, UPDATE, DELETE, DO, REPLACE, LOAD, HANDLER, CALL, // Data manipulation statements
        CREATE, ALTER, DROP, TRUNCATE, RENAME  // Data definition statements
    }

    SQLDB(SQLConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    protected final Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    public ResultSet query(String query) throws SQLException {
        Statement statement = getConnection().createStatement();
        switch (this.getStatement(query)) {
            case SELECT:
                return statement.executeQuery(query);
            default:
                statement.executeUpdate(query);
                return null;
        }
    }

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

    public abstract boolean checkTable(String table) throws SQLException;

    @Override
    public void shutdown() {
        connectionPool.close();
    }
}
