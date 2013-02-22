/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class MySQLConnectionPool extends AbstractSQLConnectionPool {

    private final String url, user, password;

    MySQLConnectionPool(String url, String user, String password) throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /** {@inheritDoc} */
    @Override
    protected Connection getBaseConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}