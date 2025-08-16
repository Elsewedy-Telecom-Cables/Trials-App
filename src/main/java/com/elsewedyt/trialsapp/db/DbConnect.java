package com.elsewedyt.trialsapp.db;

import com.elsewedyt.trialsapp.services.ConfigLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DbConnect {
    private static Connection connection = null;
    private static Connection externalConnection = null;

    // Server Connection
   // private static final String HOST = "10.1.212.147";  // Server IP

 //  private static final String HOST = "localhost";  // local
  // private static final String HOST = "SWD100924";  // local moh.gabr   computer name
  //  private static final String HOST = "10.1.208.74";  // local moh.gabr   computer name
    private static final String HOST = "ETCSVR";  // Server NAME
    public static final String USER = ConfigLoader.getProperty("DB.USER");
    private static final String PASSWORD = ConfigLoader.getProperty("DB.PASSWORD");
    public static final String DB_NAME_CONECCTION = ConfigLoader.getProperty("DB.NAME");
    private static final int PORT = 1433;




   // private static final String USER = "sa";   // Server and local User
  //  private static final String PASSWORD = "Pro@12345"; // Server and local Pass


public static Connection getConnect() {
    try {
        String url = String.format(
                "jdbc:sqlserver://%s:%d;databaseName=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=true",
                HOST, PORT, DB_NAME_CONECCTION, USER, PASSWORD
        );
        return DriverManager.getConnection(url);
    } catch (SQLException e) {
        System.err.println("getConnect Error: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}


    private static String getDriverClass(String dbType) {
        switch (dbType.toLowerCase()) {
            case "mysql":
                return "com.mysql.cj.jdbc.Driver";
            case "sqlserver":
                return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            case "oracle":
                return "oracle.jdbc.driver.OracleDriver";
            default:
                throw new IllegalArgumentException("Unsupported DB: " + dbType);
        }
    }

}


