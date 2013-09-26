/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.database;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class SQLiteConnectionPool extends AbstractSQLConnectionPool {

    private final File file;

    SQLiteConnectionPool(File file) throws ClassNotFoundException, IllegalArgumentException {
        Class.forName("org.sqlite.JDBC");
        String name = file.getName();
        if (name.contains("/") ||
                name.contains("\\") ||
                name.endsWith(".db")) {
            throw new IllegalArgumentException("The database name can not contain: /, \\, or .db");
        }
        File parentFile = file.getParentFile();
        if (parentFile != null) {
            file.getParentFile().mkdirs();
        }
        this.file = file;
    }

    /** {@inheritDoc} */
    @Override
    protected Connection getBaseConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
    }

    /**
     * Unlike most other database types, SQLite will not utilize a pool of connections.  As such
     * this method always returns the same connection to the database.
     *
     * @return the single connection to the SQLite database.
     * @throws SQLException  if any SQL exceptions occur while attempting to obtain the connection.
     */
    @NotNull
    public Connection getConnection() throws SQLException {
        return getBaseConnection();
    }
}