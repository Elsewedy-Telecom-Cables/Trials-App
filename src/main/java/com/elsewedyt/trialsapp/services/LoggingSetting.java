//package com.elsewedyt.calibrationapp.services;
//
//import java.io.*;
//import java.util.Properties;
//
//public class LoggingSetting {
//
//    private static final String CONFIG_DIR = "config";
//    private static final String CONFIG_FILE = CONFIG_DIR + File.separator + "config.properties";
//    private static final String USERNAME_KEY = "last_username";
//
//    // Save last used username to config/config.properties
//    public static void saveLastUsername(String username) {
//        try {
//            File configDir = new File(CONFIG_DIR);
//            if (!configDir.exists()) {
//                configDir.mkdirs(); // Create config folder if not exists
//            }
//
//            Properties props = new Properties();
//            File file = new File(CONFIG_FILE);
//
//            // Load existing properties if file exists
//            if (file.exists()) {
//                try (InputStream in = new FileInputStream(file)) {
//                    props.load(in);
//                }
//            }
//
//            props.setProperty(USERNAME_KEY, username);
//
//            try (OutputStream out = new FileOutputStream(file)) {
//                props.store(out, null);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace(); // Or use your Logging class
//        }
//    }
//
//    // Read last used username from config/config.properties
//    public static String getLastUsername() {
//        File file = new File(CONFIG_FILE);
//        if (!file.exists()) {
//            return "";
//        }
//
//        try (InputStream in = new FileInputStream(file)) {
//            Properties props = new Properties();
//            props.load(in);
//            return props.getProperty(USERNAME_KEY, "");
//        } catch (IOException e) {
//            e.printStackTrace(); // Or use your Logging class
//            return "";
//        }
//    }
//
//}

package com.elsewedyt.trialsapp.services;

import com.elsewedyt.trialsapp.controllers.MainApp;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.util.Duration;

import java.io.File;

public class LoggingSetting {

     //Retrieves the current system user's username (Domain Username).

    public static String getCurrentUsername() {
        try {
            // Get the username from the system property
            String username = System.getProperty("user.name");
            return username != null ? username : "";
        } catch (Exception e) {
            e.printStackTrace(); // Log the error (replace with your logging mechanism if needed)
            return "";
        }
    }
//    private static File currentJarFile;
//    private static long jarLastModifiedTime = -1;
//    public static void startJarUpdateWatcher() {
//        ScheduledService<Void> jarCheckService = new ScheduledService<>() {
//            @Override
//            protected Task<Void> createTask() {
//                return new Task<>() {
//                    @Override
//                    protected Void call() {
//                        if (currentJarFile != null && currentJarFile.exists()) {
//                            long currentModified = currentJarFile.lastModified();
//                            if (currentModified > jarLastModifiedTime) {
//                                jarLastModifiedTime = currentModified;
//
//                                Platform.runLater(() -> {
//                                    Alert alert = new Alert(Alert.AlertType.WARNING);
//                                    alert.setTitle("Application Update");
//                                    alert.setHeaderText("The application has been updated.");
//                                    alert.setContentText("The application must be closed to apply the new version.");
//                                    alert.showAndWait();
////                                    alert.setTitle("تحديث التطبيق");
////                                    alert.setHeaderText("تم تحديث التطبيق.");
////                                    alert.setContentText("يجب إغلاق التطبيق لتطبيق النسخة الجديدة.");
//
//                                });
//                            }
//                        }
//                        return null;
//                    }
//                };
//            }
//        };
//        jarCheckService.setPeriod(Duration.seconds(10));
//        jarCheckService.start();
//    }
//
//    public static void initJarWatcher() {
//        try {
//            String path = MainApp.class
//                    .getProtectionDomain()
//                    .getCodeSource()
//                    .getLocation()
//                    .toURI()
//                    .getPath();
//
//            currentJarFile = new File(path);
//            if (currentJarFile.exists()) {
//                jarLastModifiedTime = currentJarFile.lastModified();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
public static void startJarUpdateWatcher() {
    ScheduledService<Void> jarCheckService = new ScheduledService<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    if (currentJarFile != null && currentJarFile.exists()) {
                        long currentModified = currentJarFile.lastModified();
                        if (currentModified > jarLastModifiedTime) {
                            jarLastModifiedTime = currentModified;

                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Trials Application Update");
                                alert.setHeaderText("The application has been updated.");
                                alert.setContentText("The application must be closed to apply the new version.");

                                alert.showAndWait();
                            });
                        }
                    }
                    return null;
                }
            };
        }
    };
    jarCheckService.setPeriod(Duration.seconds(10));
    jarCheckService.start();
}

    private static File currentJarFile;
    private static long jarLastModifiedTime = -1;
    public static void initJarWatcher() {
        try {
            String path = MainApp.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();

            currentJarFile = new File(path);
            if (currentJarFile.exists()) {
                jarLastModifiedTime = currentJarFile.lastModified();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
