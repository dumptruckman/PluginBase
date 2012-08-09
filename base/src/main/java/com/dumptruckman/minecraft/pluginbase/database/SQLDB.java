/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Queue;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

abstract class SQLDB implements SQLDatabase {

    private final SQLConnectionPool connectionPool;

    private final Queue<String> queryQueue = new LinkedBlockingQueue<String>();

    SQLDB(SQLConnectionPool connectionPool) {
        this.connectionPool = connectionPool;

    }

    private final class QueueProcessor extends TimerTask {
        private final long timeLimit;
        private QueueProcessor(int timeLimit) {
            this.timeLimit = timeLimit;
        }
        @Override
        public void run() {
            long endTime = System.currentTimeMillis() + timeLimit;
            while (!queryQueue.isEmpty() && (timeLimit == 0 || System.currentTimeMillis() < endTime)) {

            }
        }
    }

    protected final Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    public void executeUpdate(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.executeUpdate(sql);
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();
        return statement.executeQuery(sql);
    }

    public ResultSet executeQueryLast(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();
        return statement.executeQuery(sql);
    }

    public void execute(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.execute(sql);
    }

    public abstract boolean checkTable(String table) throws SQLException;

    @Override
    public void shutdown() {
        connectionPool.close();
    }
}
