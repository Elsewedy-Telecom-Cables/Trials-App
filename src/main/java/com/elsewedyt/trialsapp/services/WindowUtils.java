package com.elsewedyt.trialsapp.services;

import com.elsewedyt.trialsapp.controllers.AddTrialController;
import com.elsewedyt.trialsapp.controllers.AddUserController;
import com.elsewedyt.trialsapp.controllers.LoginController;
import com.elsewedyt.trialsapp.controllers.TrialsController;
import com.elsewedyt.trialsapp.db.DEF;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.function.Consumer;

import static com.elsewedyt.trialsapp.logging.Logging.ERROR;
import static com.elsewedyt.trialsapp.logging.Logging.logException;

public class WindowUtils {

    public static void OPEN_WINDOW(String fxmlPath, Runnable onCloseAction) {
        try {
            Parent root = FXMLLoader.load(WindowUtils.class.getResource(fxmlPath));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image(WindowUtils.class.getResourceAsStream(iconImagePath)));

            if (onCloseAction != null)
                // Handle window close event
                stage.setOnCloseRequest(event -> onCloseAction.run());

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void OPEN_WINDOW_FULL_SCREEN(String fxmlPath, Runnable onCloseAction) {
        try {
            Parent root = FXMLLoader.load(WindowUtils.class.getResource(fxmlPath));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image(WindowUtils.class.getResourceAsStream(iconImagePath)));

            if (onCloseAction != null) {
                stage.setOnCloseRequest(event -> onCloseAction.run());
            }

            stage.setResizable(true);      // Allow resizing
            stage.setMaximized(true);      // Open maximized by default

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void OPEN_WINDOW_NOT_RESIZABLE(String fxmlPath, Runnable onCloseAction) {
        try {
            Parent root = FXMLLoader.load(WindowUtils.class.getResource(fxmlPath));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image(WindowUtils.class.getResourceAsStream(iconImagePath)));
            if (onCloseAction != null)
                // Handle window close event
                stage.setOnCloseRequest(event -> onCloseAction.run());

            stage.show();
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface StageAware {
        void setStage(Stage stage);
    }

        // Open a non-resizable window and return the Stage
        public static Stage OPEN_WINDOW_NOT_RESIZABLE_2(String fxmlPath, Runnable onCloseAction, Object controllerData) {
            try {
                FXMLLoader loader = new FXMLLoader(WindowUtils.class.getResource(fxmlPath));
                Parent root = loader.load(); // Load the FXML page

                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.getIcons().add(new Image(WindowUtils.class.getResourceAsStream(iconImagePath)));

                // Set the controller's stage if it implements StageAware
                Object controller = loader.getController();
                if (controller instanceof StageAware) {
                    ((StageAware) controller).setStage(stage);
                }

                // Set the provided controller as userData
                stage.setUserData(controllerData);

                if (onCloseAction != null) {
                    stage.setOnCloseRequest(event -> onCloseAction.run());
                }

                stage.show();
                stage.setResizable(false);
                return stage; // Return the created Stage
            } catch (Exception e) {
                e.printStackTrace();
                return null; // Return null in case of an error
            }
        }

    public static void OPEN_WINDOW_NOT_RESIZABLE_3(String fxmlPath, Consumer<Object> controllerHandler) {
        try {
            FXMLLoader loader = new FXMLLoader(WindowUtils.class.getResource(fxmlPath));
            Parent root = loader.load();
            Object controller = loader.getController();

            if (controllerHandler != null) {
                controllerHandler.accept(controller);
            }

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void CLOSE(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public static void CLOSE2(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    public static String iconImagePath = "/images/trials02.png";
    public static String EXCEL_ICON = "/images/excel.png";
    public static int ALERT_WARNING = 1;
    public static int ALERT_ERROR = 2;
    public static int ALERT_CONFIRMATION = 3;
    public static int ALERT_INFORMATION = 4;

    // Tooling Room Screens
    public static final String LOGIN_PAGE = "/screens/Login.fxml";
    public static final String MAIN_PAGE = "/screens/Main.fxml";
    public static final String VIEW_USER_PAGE = "/screens/ViewUsers.fxml";
    public static final String ADD_USER_PAGE = "/screens/AddUser.fxml";
    public static final String PREPARE_DATA = "/screens/PrepareData.fxml";
    public static final String DEPARTMENT_SECTION = "/screens/Department_Section.fxml";
    public static final String TRIALS_PAGE = "/screens/Trials.fxml";
    public static final String ADD_TRIAL_PAGE = "/screens/AddTrial.fxml";
    public static final String DASHBOARD_PAGE = "/screens/Dashboard.fxml";
    public static final String MEW_DASHBOARD_PAGE = "/screens/NewDashboard.fxml";
    public static final String OPEN_FILES_PAGE = "/screens/OpenFiles.fxml";
    public static final String DASHBOARD2_PAGE = "/screens/Dashboard2.fxml";



    // Alert
    public static void ALERT(String header, String message, int type) {
        Alert.AlertType alertType = Alert.AlertType.INFORMATION;
        if (type == 1) {
            alertType = Alert.AlertType.WARNING;
        } else if (type == 2) {
            alertType = Alert.AlertType.ERROR;
        } else if (type == 3) {
            alertType = Alert.AlertType.CONFIRMATION;
        } else if (type == 4) {
            alertType = Alert.AlertType.INFORMATION;
        }
        Alert alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void OPEN_LOGIN_PAGE() {
        try {
            OPEN_WINDOW_NOT_RESIZABLE(
                    LOGIN_PAGE,
                    null
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_LOGIN_PAGE", ex);
        }
    }

    public static void OPEN_MAIN_PAGE() {
        try {
            OPEN_WINDOW_NOT_RESIZABLE(
                    MAIN_PAGE,
                    () -> OPEN_LOGIN_PAGE()
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_MAIN_PAGE", ex);

        }
    }
    public static void OPEN_TRIALS_PAGE_PAGE() {
        try {
            OPEN_WINDOW_FULL_SCREEN(
                    TRIALS_PAGE,
                    () -> OPEN_MAIN_PAGE()
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_TRIALS_PAGE_PAGE", ex);
        }
    }
    public static void OPEN_DASHBOARD_PAGE() {
        try {
            OPEN_WINDOW_FULL_SCREEN(
                    DASHBOARD_PAGE,
                    () -> OPEN_MAIN_PAGE()
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_DASHBOARD_PAGE", ex);
        }
    }
    public static void OPEN_NEW_DASHBOARD_PAGE() {
        try {
            OPEN_WINDOW_FULL_SCREEN(
                    MEW_DASHBOARD_PAGE,
                    () -> OPEN_MAIN_PAGE()
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_DASHBOARD_PAGE", ex);
        }
    }
    public static void OPEN_PREPARE_DATA_PAGE() {
        try {
            OPEN_WINDOW_FULL_SCREEN(
                    PREPARE_DATA,
                    () -> OPEN_MAIN_PAGE()
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_PREPARE_DATA_PAGE", ex);
        }
    }
    public static void OPEN_DEPARTMENT_SECTION_PAGE() {
        try {
            OPEN_WINDOW_NOT_RESIZABLE(
                    DEPARTMENT_SECTION,
                    () -> OPEN_PREPARE_DATA_PAGE()
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_DEPARTMENT_SECTION_PAGE", ex);
        }
    }

    public static void OPEN_VIEW_USERS_PAGE() {
        try {
            OPEN_WINDOW_FULL_SCREEN(
                    VIEW_USER_PAGE,
                    () -> OPEN_MAIN_PAGE()
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_VIEW_USERS_PAGE", ex);
        }
    }

    public static void OPEN_ADD_USER_PAGE() {
        try {
            OPEN_WINDOW_NOT_RESIZABLE(
                    ADD_USER_PAGE,
                    () -> OPEN_VIEW_USERS_PAGE()
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_ADD_USER_PAGE", ex);
        }
    }
    public static void OPEN_ADD_TRIAL_PAGE() {
        try {
            OPEN_WINDOW_NOT_RESIZABLE(
                    ADD_TRIAL_PAGE,
                    () -> OPEN_TRIALS_PAGE_PAGE()
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_ADD_USER_PAGE", ex);
        }
    }

//    public static void OPEN_ADD_TRIAL_PAGE(boolean reopenDashboardOnClose) {
//        try {
//            Runnable onClose = reopenDashboardOnClose ? WindowUtils::OPEN_MAIN_PAGE : null;
//            OPEN_WINDOW_NOT_RESIZABLE_2(ADD_TRIAL_PAGE, onClose);
//        } catch (Exception ex) {
//            logException(ERROR, WindowUtils.class.getName(), "OPEN_CAL_PLAN_PAGE", ex);
//        }
//    }
public static void OPEN_ADD_TRIAL_PAGE(boolean reopenDashboardOnClose, TrialsController trialsController) {
    try {
        Runnable onClose = reopenDashboardOnClose ? WindowUtils::OPEN_MAIN_PAGE : null;
        // Pass the TrialsController as controllerData
        OPEN_WINDOW_NOT_RESIZABLE_2(ADD_TRIAL_PAGE, onClose, trialsController);
    } catch (Exception ex) {
        logException(ERROR, WindowUtils.class.getName(), "OPEN_ADD_TRIAL_PAGE", ex);
    }
}
    public static void OPEN_EDIT_TRIAL_PAGE(int trialId, Runnable onClose) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/screens/AddTrial.fxml"));
            Parent parent = fxmlLoader.load();

            AddTrialController controller = fxmlLoader.getController();
            controller.setTrialData(trialId, true);
            controller.setSaveButton();

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("Update Trial");
            stage.initModality(Modality.NONE);
            stage.initStyle(StageStyle.DECORATED);
            stage.setResizable(false);

            if (onClose != null) {
                stage.setOnHiding(event -> onClose.run());
            }

            stage.show();
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_EDIT_TRIAL_PAGE", ex);
        }
    }



    public static void OPEN_EDIT_USER_PAGE(int userId) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/screens/AddUser.fxml"));
            Parent parent = fxmlLoader.load(); // Load the FXML and get the root
            AddUserController addupdateuser_controller = fxmlLoader.getController();
            addupdateuser_controller.setUserData(userId, true);
            // Change Save Button from
            addupdateuser_controller.setSaveButton();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("Update User");
            stage.setOnCloseRequest(event -> OPEN_VIEW_USERS_PAGE());
            stage.show();
            stage.setResizable(false);
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_ADD_USER_PAGE", ex);
        }
    }


   // UserRole Convert value Int to String and Reverse
   public static int getUserRoleInt(String role) {
       if (DEF.USER_ROLE_SUPER_ADMIN.equals(role)) {
           return 4;
       } else if (DEF.USER_ROLE_DEPARTMENT_MANAGER.equals(role)) {
           return 3;
       } else if (DEF.USER_ROLE_SUPER_VISOR.equals(role)) {
           return 2;
       } else if (DEF.USER_ROLE_USER_STRING.equals(role)) {
           return 1;
       }
       return -1;
   }

    public static String getUserRoleStr(int roleId) {
        if (roleId == 4) {
            return DEF.USER_ROLE_SUPER_ADMIN;
        } else if (roleId == 3) {
            return DEF.USER_ROLE_DEPARTMENT_MANAGER;
        } else if (roleId == 2) {
            return DEF.USER_ROLE_SUPER_VISOR;
        } else if (roleId == 1) {
            return DEF.USER_ROLE_USER_STRING;
        }
        return null;
    }

    // UserActive Convert value Int to String and Reverse
    public static int getUserActiveInt(String active) {
        if (DEF.USER_ACTIVE_STRING.equals(active)) {
            return 1;
        } else if (DEF.USER_NOT_ACTIVE_STRING.equals(active)) {
            return 0;
        }
        return -1;
    }

    public static String getUserActiveStr(int active) {
        if (active == 1) {
            return DEF.USER_ACTIVE_STRING;
        } else if (active == 0) {
            return DEF.USER_NOT_ACTIVE_STRING;
        }
        return null;
    }
    public static String getTestSituationStr(int testSituationInt) {
        if (testSituationInt == 1) {
            return DEF.TEST_SITUATION_ACCEPTED;
        } else if (testSituationInt == 2) {
            return DEF.TEST_SITUATION_REFUSED;
        } else if (testSituationInt == 3) {
            return DEF.TEST_SITUATION_HOLD;
        }
        return null;
    }
    public static int getTestSituationInt(String testSituationStr) {
        if (DEF.TEST_SITUATION_ACCEPTED.equals(testSituationStr)) {
            return 1;
        } else if (DEF.TEST_SITUATION_REFUSED.equals(testSituationStr)) {
            return 2;
        } else if (DEF.TEST_SITUATION_HOLD.equals(testSituationStr)) {
            return 3;
        }
        return -2;
    }



 }

