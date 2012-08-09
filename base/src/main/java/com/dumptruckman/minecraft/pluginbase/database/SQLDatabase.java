/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLDatabase {

    ResultSet query(String string) throws SQLException;

    boolean checkTable(String name) throws SQLException;

    void shutdown();
}
