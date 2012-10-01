/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.database;

import com.dumptruckman.minecraft.pluginbase.util.Logging;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

final class SQLUpdateQueueThread extends Thread {

    private final SQLConnectionPool connectionPool;
    private final BlockingQueue<String> queryQueue = new LinkedBlockingQueue<String>();
    private final BlockingQueue<String> waitingQueue = new LinkedBlockingQueue<String>();

    private volatile boolean waiting = false;

    SQLUpdateQueueThread(SQLConnectionPool connectionPool) {
        super("SQLUpdateQueueThread");
        this.connectionPool = connectionPool;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String query = queryQueue.take();
                try {
                    SQLDB.executeUpdate(getConnection(), query);
                } catch (SQLException e) {
                    Logging.severe("SQL Exception occured while processing the update queue: " + e.getMessage());
                }
            } catch (InterruptedException ignore) { }
        }
    }

    public void queueUpdate(String query) {
        if (waiting) {
            waitingQueue.add(query);
        } else {
            queryQueue.add(query);
        }
    }

    public void waitUntilEmpty() {
        waiting = true;
        while (!queryQueue.isEmpty()) { }
        waiting = false;
        queryQueue.addAll(waitingQueue);
        waitingQueue.clear();
    }

    private Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }
}
