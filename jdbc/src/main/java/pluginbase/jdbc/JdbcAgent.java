package pluginbase.jdbc;

import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;

/**
 * Contains everything necessary for connecting to a database.
 */
public interface JdbcAgent {

    /**
     * Retrieves the DataSource provided by this JDBC agent.
     *
     * @return the DataSource provided by this JDBC agent.
     */
    @NotNull
    DataSource getDataSource();

    /**
     * Retrieves the configuration object that contains settings related to this JDBC agent's database connection.
     *
     * @return the configuration object that contains settings related to this JDBC agent's database connection.
     */
    @NotNull
    DatabaseSettings getSettings();
}
