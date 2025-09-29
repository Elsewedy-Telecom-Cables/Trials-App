package com.elsewedyt.trialsapp.controllers;
import com.elsewedyt.trialsapp.db.DbConnect;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.services.LoggingSetting;
import com.elsewedyt.trialsapp.services.WindowUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        try {

            Connection connection = DbConnect.getConnect();
            if (connection == null) {
                WindowUtils.ALERT("ERR", "Connection Error", WindowUtils.ALERT_ERROR);

            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/screens/Login.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("Login Form");
                stage.setScene(scene);
                stage.show();
                stage.setResizable(false);
                LoggingSetting.startJarUpdateWatcher();
                LoggingSetting.initJarWatcher();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logException("ERROR", this.getClass().getName(), "start", e);
           // Logging.logException("ERROR", this.getClass().getName(), "start", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}



