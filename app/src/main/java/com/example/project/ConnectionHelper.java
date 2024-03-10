package com.example.project;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Helper class for establishing a connection to the MySQL database.
 */
public class ConnectionHelper {

    // Database credentials and connection details
    private static final String ip = "bsqwofv2ctgjiksuem7o-mysql.services.clever-cloud.com";
    private static final String database = "bsqwofv2ctgjiksuem7o";
    private static final String uname = "uhsnrjgwdiqnzjur";
    private static final String pass = "Ir3Qdnt4Kys0d5Cg0SJ8";
    private static final String port = "3306";
    private static Connection conn = null;

    /**
     * Establishes a connection to the MySQL database.
     *
     * @return The established database connection.
     */
    public static Connection connectionclass() {
        // Constructing the connection URL
        String connectionUrl = "jdbc:mysql://" + ip + ":" + port + "/" + database;

        // Allowing network operations on the main thread temporarily (not recommended)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Checking if a connection already exists
        if (conn != null) {
            try {
                // If the connection is not closed, return the existing connection
                if (!conn.isClosed()) {
                    return conn;
                }
            } catch (SQLException e) {
                Log.e("error", e.getMessage());
            }
        } else {
            try {
                // Loading the MySQL JDBC driver class
                Class.forName("com.mysql.jdbc.Driver");

                // Establishing a new connection using DriverManager
                conn = DriverManager.getConnection(connectionUrl, uname, pass);
            } catch (Exception ex) {
                // Handling exceptions that occur during the connection process
                Log.e("error", ex.getMessage());
                ex.printStackTrace();
            }
        }
        // Logging the connection details
        Log.i("connection", conn.toString());
        return conn;
    }
}
