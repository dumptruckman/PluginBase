/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.database;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * An abstract implementation of SQLDatabase which implements most of the methods with
 * a SQLConnectionPool passed in via the constructor.
 */
public abstract class SQLDB implements SQLDatabase {

    private final SQLConnectionPool connectionPool;

    /**
     * Creates a SQLDatabase using the given connection pool.
     *
     * @param connectionPool the connection pool to use for this SQLDatabase.
     */
    SQLDB(@NotNull final SQLConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /** {@inheritDoc} */
    @NotNull
    public PreparedStatement getFreshPreparedStatementHotFromTheOven(@NotNull final String query) throws SQLException {
        return getConnection().prepareStatement(query);
    }

    /** {@inheritDoc} */
    @NotNull
    public PreparedStatement getFreshPreparedStatementWithGeneratedKeys(@NotNull final String query) throws SQLException {
        return getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    }

    /** {@inheritDoc} */
    @NotNull
    public Connection getConnection() throws SQLException {
        return getPool().getConnection();
    }

    /** {@inheritDoc} */
    @NotNull
    public SQLConnectionPool getPool() {
        return connectionPool;
    }

    /** {@inheritDoc} */
    @Override
    public abstract boolean checkTable(@NotNull final String table) throws SQLException;

    /** {@inheritDoc} */
    @Override
    public void shutdown() {
        connectionPool.close();
    }
}
