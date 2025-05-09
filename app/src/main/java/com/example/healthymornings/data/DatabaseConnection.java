package com.example.healthymornings.data;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection
{
    Connection con;
    String uname, password, ip, port, databaseName;

    public Connection connectionclass()
    {
        ip = "";
        databaseName = "";
        uname = "";
        password = "";
        port = "";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try
        {
            Class.forName("org.postgresql.Driver");
            ConnectionURL = "jdbc:postgresql://" + ip + ":" + port + "/" + databaseName + "?user=" + uname + "&password=" + password;
            connection = DriverManager.getConnection((ConnectionURL));
        }
        catch (Exception ex)
        {
            Log.e("Connection Error", "Error: " + ex.getMessage());
        }

        return connection;
    }
}
