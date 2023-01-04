package dal;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;

public class DBConnection {

    /**
     * Creates a connection to our database.
     * @return database connection.
     */
    public Connection getConnection() {
        SQLServerDataSource ds;
        ds = new SQLServerDataSource();
        ds.setDatabaseName("CSe22B_");
        ds.setUser("CSe22B_");
        ds.setPassword("CSe22B_");
        ds.setServerName("10.176.111.31");
        ds.setPortNumber(1433);
        ds.setTrustServerCertificate(true);
        try {
            return ds.getConnection();
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        }
    }
}
