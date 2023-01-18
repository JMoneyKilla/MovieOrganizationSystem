package dal;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;

public class DBConnection {

    /**
     * Creates a connection to our database.
     * @return database connection.
     */
    public Connection getConnection() throws SQLServerException {
        SQLServerDataSource ds;
        ds = new SQLServerDataSource();
        ds.setDatabaseName("CSe22B_15_mdb");
        ds.setUser("CSe22B_15");
        ds.setPassword("CSe22B_15");
        ds.setServerName("10.176.111.31");
        ds.setPortNumber(1433);
        ds.setTrustServerCertificate(true);

        return ds.getConnection();
    }
}
