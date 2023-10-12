package com.example.project;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    Connection con;
    String uname, pass,ip,port,database;

    public Connection connectionclass()
    {
        ip = "10.0.2.2";
        database="project";
        uname="donut";
        pass="artem1409";
        port = "1433";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection=null;
        String connectionUrl=null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databaseName=" + database + ";user=" + uname + ";password=" + pass;
            connection = DriverManager.getConnection(connectionUrl);
        }
        catch (Exception ex){
            Log.e("error", ex.getMessage());
        }
        return connection;
    }
}
