/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SQLDB implements SQLDatabase {

    private final SQLConnectionPool connectionPool;
    private final SQLUpdateQueueThread updateQueueThread;

    SQLDB(SQLConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.updateQueueThread = new SQLUpdateQueueThread(connectionPool);
        updateQueueThread.start();
    }

    static void executeUpdate(Connection connection, String sqlQuery) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
    }

    static ResultSet executeQuery(Connection connection, String sqlQuery) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sqlQuery);
    }

    @Override
    public final Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    @Override
    public void executeUpdate(String sqlQuery) {
        updateQueueThread.queueUpdate(sqlQuery);
    }

    @Override
    public ResultSet executeQueryNow(String sqlQuery) throws SQLException {
        return executeQuery(getConnection(), sqlQuery);
    }

    @Override
    public ResultSet executeQueryLast(String sqlQuery) throws SQLException {
        updateQueueThread.waitUntilEmpty();
        return executeQuery(getConnection(), sqlQuery);
    }

    @Override
    public void execute(String sqlQuery) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.execute(sqlQuery);
    }

    @Override
    public abstract boolean checkTable(String table) throws SQLException;

    @Override
    public void shutdown() {
        connectionPool.close();
    }
}
