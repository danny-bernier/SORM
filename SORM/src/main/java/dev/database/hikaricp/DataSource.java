package dev.database.hikaricp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * HikariCP data source used for connection pooling
 */
public class DataSource {
    private static HikariConfig hikariConfig = new HikariConfig();
    private static HikariDataSource dataSource;
    private static int CONNECTION_SIZE = 10;

    /**
     * Constructor to create a new connection pool
     * @param jdbcUrl The url of the database to be connected to
     * @param jdbcUserName The username used to authorize with the database
     * @param jdbcPassword The password used to authorize with the database
     */
    public DataSource(String jdbcUrl, String jdbcUserName, String jdbcPassword){
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(jdbcUserName);
        hikariConfig.setPassword(jdbcPassword);
        hikariConfig.setMaximumPoolSize(CONNECTION_SIZE);
        dataSource = new HikariDataSource(hikariConfig);
    }

    /**
     * Get the current connection
     * @return Returns an active connection
     * @throws SQLException Thrown when an issue is encountered with connection
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
