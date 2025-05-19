package com.nforge.healthymornings.data;

// ANDROID
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

// JDBC
import java.sql.Connection;
import java.sql.DriverManager;


public class DatabaseConnectivityJDBC {
    Connection databaseConnection = null;

    // Domyślne dane naszego prywatnego serwera z bazą danych
    String databaseConnectionURL = null,
            databaseName = "healthy_mornings",
            databaseIpAddress = "136.244.87.135",
            databasePort = "5432",
            databaseUsername = "application_jdbc_connection",
            databasePassword = "123",
            databaseSchema = "application";


    // Programista może nadpisać dane połączenia poniższą metodą
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
        // Strict Mode (Wymagane do poprawnego działania JDBC pod Androidem)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {

            // Używamy PostgreSQL jako naszej bazy danych
            Class.forName("org.postgresql.Driver");


            // Konkatenacja URL'a (widziałem wersję z danymi logowania, ale tu nie działa prawdopodobnie przez hash)
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

        } catch (Exception connectionException) { Log.e("DatabaseConnectivityJDBC", "establishDatabaseConnection(): " + connectionException.getMessage()); }
        return databaseConnection;
    }
}
