/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SQLDB implements SQLDatabase {

    private final SQLConnectionPool connectionPool;

    SQLDB(SQLConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public PreparedStatement getFreshPreparedStatementHotFromTheOven(String query) throws SQLException {
        final PreparedStatement preparedStatement = getConnection().prepareStatement(query);
        return preparedStatement;
    }

    public PreparedStatement getFreshPreparedStatementWithGeneratedKeys(String query) throws SQLException {
        final PreparedStatement ps = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        return ps;
    }

    public Connection getConnection() throws SQLException {
        return getPool().getConnection();
    }

    public SQLConnectionPool getPool() {
        return connectionPool;
    }

    @Override
    public abstract boolean checkTable(String table) throws SQLException;

    @Override
    public void shutdown() {
        connectionPool.close();
    }
}
