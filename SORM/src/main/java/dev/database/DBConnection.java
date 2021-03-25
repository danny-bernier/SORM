package dev.database;

import dev.config.ConnectionConfiguration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * Represents a database connection acquisition service
 */
public class DBConnection {

    /**
     * Map of database information
     * <p>
     *     url = database endpoint
     *     schema = (may be empty string if none was specified) current schema
     *     user = user login information
     *     password = (may be empty string if none was specified) password for user
     * </p>
     */
    private final Map<String, String> CONNECTION_INFORMATION;


    private DBConnection(){
        this.CONNECTION_INFORMATION = ConnectionConfiguration.getDBConnectionInformation("./src/main/resources/DB_Connection.xml");
    }


    /**
     * Creates and returns a connection to a database using information provided by {@link ConnectionConfiguration}
     * @return Connection for database
     * @throws SQLException Thrown when connection creation fails
     */
    public Connection getConnection() throws SQLException {
        //if no schema was specified
        if(CONNECTION_INFORMATION.get("schema") == null || CONNECTION_INFORMATION.get("schema").equals(""))
            return DriverManager.getConnection(
                    CONNECTION_INFORMATION.get("url"),
                    CONNECTION_INFORMATION.get("user"),
                    CONNECTION_INFORMATION.get("password"));

        //if a schema was specified
        return DriverManager.getConnection(
                CONNECTION_INFORMATION.get("url") + "?currentSchema=" + CONNECTION_INFORMATION.get("schema"), CONNECTION_INFORMATION.get("user"), CONNECTION_INFORMATION.get("password"));
    }


    /**
     * Initialization-on-demand holder idiom implementation
     * @see <a href="https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom</a>
     * Allows for lazy initialization of thread safe singleton
     */
    private static class LazyHolder {
        public static final DBConnection INSTANCE = new DBConnection();
    }


    /**
     * Returns instance of DBConnection
     * @return the current instance of DBConnection
     */
    public static DBConnection getInstance() {
        return LazyHolder.INSTANCE;
    }
}
