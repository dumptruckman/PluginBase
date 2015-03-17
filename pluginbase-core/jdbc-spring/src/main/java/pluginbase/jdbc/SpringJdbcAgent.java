package pluginbase.jdbc;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.File;

public class SpringJdbcAgent implements JdbcAgent {

    /**
     * Initializes a SpringJdbcAgent, starting a connection to a database based on the given settings.
     *
     * @param settings The settings which indicate how and what database to connect to.
     * @param databaseFolder A folder which will contain any embedded database.  This may or may not be used depending on the given settings.
     * @param classLoader This is the ClassLoader that will be used to load the database Driver class.
     * @return a new SpringJdbcAgent.
     * @throws ClassNotFoundException if the driver specified in settings is not found.
     */
    public static SpringJdbcAgent createAgent(@NotNull DatabaseSettings settings, @NotNull File databaseFolder, @NotNull ClassLoader classLoader) throws ClassNotFoundException {
        String dbType = settings.getDatabaseType();
        String url = settings.getDatabaseInfo().getUrl();
        if (dbType.equalsIgnoreCase("H2")) {
            dbType = "org.h2.Driver";
            if (!url.startsWith("jdbc")) {
                url = "jdbc:h2:" + new File(databaseFolder, url).getPath();
            }
        } else if (dbType.equalsIgnoreCase("MySQL")) {
            dbType = "com.mysql.jdbc.Driver";
        } else if (dbType.equalsIgnoreCase("SQLite")) {
            dbType = "org.sqlite.JDBC";
            if (!url.startsWith("jdbc")) {
                url = "jdbc:sqlite:" + new File(databaseFolder, url).getPath();
            }
        }
        ClassLoader previousClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dbType);
        dataSource.setUrl(url);
        dataSource.setUsername(settings.getDatabaseInfo().getUser());
        dataSource.setPassword(settings.getDatabaseInfo().getPass());
        Thread.currentThread().setContextClassLoader(previousClassLoader);
        return new SpringJdbcAgent(settings, dataSource);
    }

    @NotNull
    private final DatabaseSettings settings;
    @NotNull
    private final DriverManagerDataSource dataSource;

    private SpringJdbcAgent(@NotNull DatabaseSettings settings, @NotNull DriverManagerDataSource dataSource) {
        this.settings = settings;
        this.dataSource = dataSource;
    }

    @NotNull
    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @NotNull
    @Override
    public DatabaseSettings getSettings() {
        return settings;
    }

    /**
     * A convenience method for creating JdbcTemplate which allows for very intuitive database access.
     */
    @NotNull
    public JdbcTemplate createJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }
}
