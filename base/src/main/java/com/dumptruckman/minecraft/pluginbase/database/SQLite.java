/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.database;

import java.io.File;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLite extends SQLDB {

    public SQLite(File file) throws ClassNotFoundException, IllegalArgumentException {
        super(new SQLiteConnectionPool(file));
    }

    @Override
    public boolean checkTable(String table) throws SQLException {
        DatabaseMetaData dbm = getConnection().getMetaData();
        ResultSet tables = dbm.getTables(null, null, table, null);
        try {
            return tables.next();
        } finally {
            tables.close();
        }
    }
}