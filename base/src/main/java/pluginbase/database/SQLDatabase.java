/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.database;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represents a connection to a SQL database wherein queries can be initiated through the
 * use of prepared statements.
 */
public interface SQLDatabase {

    /**
     * Hot and ready for consumption
     *
     * @param query
     * @return
     * @throws SQLException
     */
    @NotNull
    PreparedStatement getFreshPreparedStatementHotFromTheOven(@NotNull final String query) throws SQLException;

    /**
     * Delicious PreparedStatements but with a generate keys attached!
     *
     * @param query
     * @return
     * @throws SQLException
     */
    @NotNull
    PreparedStatement getFreshPreparedStatementWithGeneratedKeys(@NotNull final String query) throws SQLException;

    /**
     * Gets a connection from the connection pool.
     *
     * @throws SQLException
     */
    @NotNull
    Connection getConnection() throws SQLException;

    /**
     * Gets the current connection pool.
     *
     * @return the current connection pool.
     */
    @NotNull
    SQLConnectionPool getPool();

    /**
     * Verifies that a table with the given name exists.
     *
     * @param name the name of the table to check for.
     * @return true if the table by the given name exists.
     * @throws SQLException
     */
    boolean checkTable(@NotNull final String name) throws SQLException;

    /**
     * Shuts down all connections within the connection pool.
     */
    void shutdown();
}
