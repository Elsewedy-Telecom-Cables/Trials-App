package com.elsewedyt.trialsapp.db;

import com.elsewedyt.trialsapp.services.ConfigLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DbConnect {


    private static final String HOST = "10.1.212.147";  // Server IP
   // private static final String HOST = "localhost";  // Server IP
    private static final int PORT = 1433;
    public static final String USER = ConfigLoader.getProperty("DB.USER");
    private static final String PASSWORD = ConfigLoader.getProperty("DB.PASSWORD");
    public static final String DB_NAME_CONECCTION = ConfigLoader.getProperty("DB.NAME");

    public static Connection getConnect() {
        try {
            String url = String.format(
                    "jdbc:sqlserver://%s:%d;" +
                            "databaseName=%s;" +
                            "encrypt=false;" +  // أو true لو السيرفر عنده شهادة SSL كويسة
                            "trustServerCertificate=true;" +
                            "loginTimeout=30;" +     // After 30 sec throw Exception
                            "socketTimeout=60000;",  // After 60 sec Exception
                    HOST, PORT, DB_NAME_CONECCTION
            );

            // لاحظ: user/password ما يتحطوش جوة الـ URL لو حاططهم في Properties
            Properties props = new Properties();
            props.setProperty("user", USER);
            props.setProperty("password", PASSWORD);

            return DriverManager.getConnection(url, props);

        } catch (SQLException e) {
            System.err.println("getConnect Error: " + e.getMessage());
            return null;
        }
    }

}


