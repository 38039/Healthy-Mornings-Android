package com.nforge.healthymornings.data;

// ANDROID
import android.os.StrictMode;
import android.util.Log;
// JDBC
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnectivityJDBC {
    Connection databaseConnection = null;
    String databaseConnectionURL = null, databaseName = "", databaseIpAddress = "", databasePort = "", databaseUsername = "", databasePassword = "", databaseSchema = "";


    // Enkapsulacja danych
    public void provideDatabaseConnectionDetails (
            String providedName,
            String providedIpAddress,
            String providedPort,
            String providedUsername,
            String providedPassword,
            String providedSchema
    ) {
        this.databaseName       = providedName;
        this.databaseIpAddress  = providedIpAddress;
        this.databasePort       = providedPort;
        this.databaseUsername   = providedUsername;
        this.databasePassword   = providedPassword;
        this.databaseSchema     = providedSchema;
    }

    // Utworzenie połączenia z bazą danych przez JDBC
    public Connection establishDatabaseConnection() {
        // Strict Mode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName("org.postgresql.Driver"); //Driver

            // Konkatenacja URL'a bazy danych pod drivera
            databaseConnectionURL =
                    "jdbc:postgresql://"
                            + databaseIpAddress  + ":"
                            + databasePort       + "/"
                            + databaseName
                            + "?currentSchema=" + databaseSchema;

            databaseConnection = DriverManager.getConnection(
                    databaseConnectionURL,
                    databaseUsername,
                    databasePassword
            );

        } catch (Exception ex) { Log.e("DatabaseConnectivityJDBC", "Błąd: " + ex.getMessage()); }

        return databaseConnection;
    }
}
