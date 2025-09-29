package com.elsewedyt.trialsapp.controllers;

import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.models.UserContext;
import com.elsewedyt.trialsapp.services.ShiftManager;
import static com.elsewedyt.trialsapp.services.WindowUtils.*;

import com.elsewedyt.trialsapp.services.WindowUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Label date_lbl;
    @FXML
    private Label dept_name_lbl;

    @FXML
    private Label shift_label;

    @FXML
    private Label welcome_lbl;
    @FXML
    private Button users_btn;
    @FXML
    private ImageView logo_ImageView;
    @FXML
    private ImageView trials_image_view;
    @FXML
    private Button prepare_data_btn;   // Database Button
    @FXML
    private Button trials_btn;
    @FXML
    private Button dashboard_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // Set focus to welcome label
        Platform.runLater(() -> welcome_lbl.requestFocus());

        if(UserContext.getCurrentUser().getDepartmentId() == 1){   // Technical Office  Department
            trials_btn.setText("Adding Trials + Uploading Files");
        }

        // Set shift information
        ShiftManager.setSHIFT(LocalDateTime.now());
        String shiftName = ShiftManager.SHIFT_NAME;
        shift_label.setText("Shift : " + shiftName);

        // Load and set company logo
        Image img = new Image(MainController.class.getResourceAsStream("/images/company_logo.png"));
        logo_ImageView.setImage(img);
        Image img2 = new Image(MainController.class.getResourceAsStream("/images/trials02.png"));
        trials_image_view.setImage(img2);

        // Set current date and time
        java.util.Date date = new java.util.Date();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy  hh:mm a");
        date_lbl.setText(dateFormat2.format(date) + " ");

        // Set welcome message with current user's full name
        String msg = ("Welcome : " + UserContext.getCurrentUser().getFullName());
        welcome_lbl.setText(msg);
        // Set Departmnet Name  with current user's
        String msg2 = (UserContext.getCurrentUser().getDepartmentName() + " Department");
        dept_name_lbl.setText(msg2);

        //set Curser
        users_btn.setCursor(Cursor.HAND);
        prepare_data_btn.setCursor(Cursor.HAND);
        trials_btn.setCursor(Cursor.HAND);
        dashboard_btn.setCursor(Cursor.HAND);

    }


    @FXML
    void openViewUsers(ActionEvent event) {
        // set Permissions
        try {
            // Super Admin and Department Manager
            int role = UserContext.getCurrentUser().getRole();
            if (role == 4 || role == 3) {
                CLOSE(event);
                OPEN_VIEW_USERS_PAGE();
            } else {
                WindowUtils.ALERT("Warning", "You are not authorized to access this page.", WindowUtils.ALERT_WARNING);
                return;
            }
        }catch (Exception ex){
            Logging.logException("ERROR", this.getClass().getName(), "openPrepareData Permission", ex);
        }

    }
//    @FXML
//    void openPrepareData(ActionEvent event) {
//        // set Permissions
//        try {
//            // Super Admin and Department Manager
//            int role = UserContext.getCurrentUser().getRole();
//            if (role == 4 || UserContext.getCurrentUser().getDepartmentId() == 1) {
//                CLOSE(event);
//                OPEN_PREPARE_DATA_PAGE();
//            } else {
//                WindowUtils.ALERT("Warning", "You are not authorized to access this page.", WindowUtils.ALERT_WARNING);
//       return;
//            }
//        }catch (Exception ex){
//            Logging.logException("ERROR", this.getClass().getName(), "openPrepareData Permission", ex);
//        }

        @FXML
        void openPrepareData(ActionEvent event) {
                    CLOSE(event);
                    OPEN_PREPARE_DATA_PAGE();
        }
    @FXML
    void openTrials(ActionEvent event) {
        CLOSE(event);
        OPEN_TRIALS_PAGE_PAGE();
    }
    @FXML
    void openDashboard(ActionEvent event) {
      CLOSE(event);
      //OPEN_DASHBOARD_PAGE();
        OPEN_NEW_DASHBOARD_PAGE();
    }



}
