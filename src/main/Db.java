package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Simple DB connection utility. Reads settings from environment variables:
 * DB_URL, DB_USER, DB_PASS. Provides a single shared Connection.
 */
public final class Db {
    private static volatile Connection connection;

    private Db() {}

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        synchronized (Db.class) {
            if (connection == null) {
                String url = System.getenv().getOrDefault("DB_URL",
                        "jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true;loginTimeout=30;");
                String user = System.getenv().getOrDefault("DB_USER", "sa");
                String pass = System.getenv().getOrDefault("DB_PASS", "yourStrong(!)Password");
                try {
                    connection = DriverManager.getConnection(url, user, pass);
                } catch (SQLException se) {
                    System.err.println("Failed to connect to SQL Server.\nURL: " + url + "\nError: " + se.getMessage());
                }
            }
        }
        return connection;
    }
}
