/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SQLDatabase {

    /**
     * Hot and ready for consumption
     *
     * @param query
     * @return
     * @throws SQLException
     */
    PreparedStatement getFreshPreparedStatementHotFromTheOven(String query) throws SQLException;

    /**
     * Delicious PreparedStatements but with a generate keys attached!
     *
     * @param query
     * @return
     * @throws SQLException
     */
    PreparedStatement getFreshPreparedStatementWithGeneratedKeys(String query) throws SQLException;

    /**
     * Gets a connection from the connection pool
     * @throws SQLException
     */
    Connection getConnection() throws SQLException;

    /**
     * Gets the current connection pool
     * @return
     */
    SQLConnectionPool getPool();

    boolean checkTable(String name) throws SQLException;

    void shutdown();
}
