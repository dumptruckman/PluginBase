package pluginbase.database;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Represents a pool of available connections to a SQL database.
 * <p/>
 * Usually there are many connections available at any given time or if any one is in use,
 * a new connection may be established and added to the pool when needed.
 * <p/>
 * It is possible that the pool will always consist of only 1 connection due to database limitations.  This limitation
 * is present with SQLite databases for example.
 */
public interface SQLConnectionPool extends Closeable {

    /**
     * Gets a connection from the pool.
     *
     * @return a connection from the pool.
     * @throws SQLException if any SQL exceptions occur while attempting to obtain a connection.
     */
    @NotNull
    Connection getConnection() throws SQLException;

    /** {@inheritDoc} */
    @Override
    void close();
}
