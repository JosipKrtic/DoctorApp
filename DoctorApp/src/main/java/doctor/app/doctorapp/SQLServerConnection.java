package doctor.app.doctorapp;

import java.sql.*;

public class SQLServerConnection {
    private static final String DB_URL = "jdbc:sqlserver://desktop-rcubead\\instanca1;databaseName=DoctorApp;sslProtocol=TLS;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASSWORD = "DugaResa2014";

    private static Connection connection;

    private SQLServerConnection() {}

    // Method to establish a database connection
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    // Method to close the database connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
