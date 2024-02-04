package com.example.project;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {

    // Credentials and connection details
    private static final String ip = "bsqwofv2ctgjiksuem7o-mysql.services.clever-cloud.com";
    private static final String database = "bsqwofv2ctgjiksuem7o";
    private static final String uname = "uhsnrjgwdiqnzjur";
    private static final String pass = "Ir3Qdnt4Kys0d5Cg0SJ8";
    private static final String port = "3306";
    private static Connection conn = null;

    public Connection connectionclass() {
        String connectionUrl = "jdbc:mysql://" + ip + ":" + port + "/" + database;
        if (conn != null) {
            return conn;
        }
        else {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                conn = DriverManager.getConnection(connectionUrl, uname, pass);
            } catch (SQLException | ClassNotFoundException ex) {
                Log.e("error", ex.getMessage());
                ex.printStackTrace();
            }
        }

        return conn;
    }
}
