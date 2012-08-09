/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL extends SQLDB {

    public MySQL(String host,
                 String port,
                 String database,
                 String user,
                 String pass) throws ClassNotFoundException {
        super(new MySQLConnectionPool("jdbc:mysql://" + host + ":" + port + "/" + database, user, pass));
    }

    @Override
    public boolean checkTable(String table) throws SQLException {
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