/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.database;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of SQLDatabase for SQLite which uses a specified SQLite database file.
 */
public class SQLite extends SQLDB {

    /**
     * Creates a SQLite database connection from the given file.
     *
     * @param file the file representing a SQLite database.
     * @throws ClassNotFoundException if SQLite drivers are not found.
     * @throws IllegalArgumentException if the file is invalid.
     */
    public SQLite(@NotNull final File file) throws ClassNotFoundException, IllegalArgumentException {
        super(new SQLiteConnectionPool(file));
    }

    /** {@inheritDoc} */
    @Override
    public boolean checkTable(@NotNull final String table) throws SQLException {
        final DatabaseMetaData dbm = getConnection().getMetaData();
        final ResultSet tables = dbm.getTables(null, null, table, null);
        try {
            return tables.next();
        } finally {
            tables.close();
        }
    }
}