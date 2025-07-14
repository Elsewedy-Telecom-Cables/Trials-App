package com.elsewedyt.trialsapp.services;

import com.elsewedyt.trialsapp.dao.UserDAO;
import com.elsewedyt.trialsapp.models.User;
import javafx.application.Platform;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

import static com.elsewedyt.trialsapp.services.SettingService.APP_BUNDLE;
import static com.elsewedyt.trialsapp.services.WindowUtils.ALERT;
import static com.elsewedyt.trialsapp.services.WindowUtils.ALERT_WARNING;

public class UserService {

    private static UserDAO userDao;

    public UserService() {

        this.userDao = new UserDAO();
    }


    public static boolean confirmPassword(String currentUsername) {
        userDao = new UserDAO();
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Confirm Password");
        dialog.setHeaderText("Please enter password to confirm");

        ButtonType confirmButtonType = new ButtonType("ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Password:"), 0, 0);
        grid.add(passwordField, 1, 0);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

        // Focus the password field by default
        Platform.runLater(passwordField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return passwordField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        return result.map(pass -> {
            User user = userDao.checkConfirmPassword(currentUsername, pass);
            int userId = -1;
            if(user!= null) {
                userId = user.getUserId();
            }
            return userId != -1;
        }).orElse(false);
    }


}
