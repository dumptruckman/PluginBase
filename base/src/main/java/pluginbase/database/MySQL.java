/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.database;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Implementation of SQLDatabase for MySQL which connects to the given database on creation.
 */
public class MySQL extends SQLDB {

    /**
     * Connects to the given MySQL database located at the given host address and port using the given login information.
     *
     * @param host the web/ip address where the database is located.
     * @param port the port the database is accepting connections on on the given host.
     * @param database the name of the database to connect to.
     * @param user the username for the database.
     * @param pass the password for the database.
     * @throws ClassNotFoundException if MySQL drivers are not found.
     */
    public MySQL(@NotNull final String host,
                 @NotNull final String port,
                 @NotNull final String database,
                 @NotNull final String user,
                 @NotNull final String pass) throws ClassNotFoundException {
        super(new MySQLConnectionPool("jdbc:mysql://" + host + ":" + port + "/" + database, user, pass));
    }

    /** {@inheritDoc} */
    @Override
    public boolean checkTable(@NotNull final String table) throws SQLException {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM `" + table + "`");
            return result != null;
        } catch (SQLException e) {
            if (e.getMessage().contains("exist")) {
                return false;
            } else {
                throw e;
            }
        }
    }
}