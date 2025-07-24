//
//package com.elsewedyt.trialsapp.controllers;
//import com.elsewedyt.trialsapp.dao.FileDAO;
//import com.elsewedyt.trialsapp.dao.FileTypeDAO;
//import com.elsewedyt.trialsapp.logging.Logging;
//import com.elsewedyt.trialsapp.models.File;
//import com.elsewedyt.trialsapp.models.FileType;
//import com.elsewedyt.trialsapp.models.UserContext;
//import com.elsewedyt.trialsapp.services.UserService;
//import com.elsewedyt.trialsapp.services.WindowUtils;
//import javafx.application.Platform;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.Cursor;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.control.cell.TextFieldTableCell;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.HBox;
//import javafx.stage.FileChooser;
//import org.kordamp.ikonli.javafx.FontIcon;
//import java.io.IOException;
//import java.net.URL;
//import java.nio.file.Files;
//import java.nio.file.StandardCopyOption;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ResourceBundle;
//
//public class AddFileController implements Initializable {
//
//    @FXML
//    private Button addFile_btn;
//    @FXML
//    private TableColumn<File, String> comment_column;
//    @FXML
//    private TableColumn<File, String> delete_file_column;
//    @FXML
//    private TableColumn<File, String> department_name_column;
//    @FXML
//    private TableColumn<File, String> download_file_column;
//    @FXML
//    private TableColumn<File, String> file_id_column;
//    @FXML
//    private TableColumn<File, String> file_type_name_column;
//    @FXML
//    private TableColumn<File, String> open_file_column;
//    @FXML
//    private TableView<File> table_view;
//    @FXML
//    private TableColumn<File, String> test_situation_column;
//    @FXML
//    private Label title_lbl;
//    @FXML
//    private TableColumn<File, String> trial_id_column;
//    @FXML
//    private TableColumn<File, String> trial_purpose_column;
//    @FXML
//    private TableColumn<File, String> upload_file_column;
//    @FXML
//    private TableColumn<File, String> upload_file_date_column;
//
//    private ObservableList<File> fileList = FXCollections.observableArrayList();
//    private ObservableList<FileType> fileTypes = FXCollections.observableArrayList();
//    private int trialId;
//    private String trialPurpose;
//    private String departmentName;
//    private static final String SERVER_UPLOAD_PATH = "\\\\ETCSVR\\TrialsUpload\\";
//    private static final String LOCAL_UPLOAD_PATH = System.getProperty("user.home") + "\\TrialsUpload\\";
//
//    // Initialize the controller
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Platform.runLater(() -> title_lbl.requestFocus());
//        setupTable();
//        addFile_btn.setCursor(Cursor.HAND);
//        // Ensure all users can see action buttons
//        addFile_btn.setVisible(true);
//        upload_file_column.setVisible(true);
//        download_file_column.setVisible(true);
//        delete_file_column.setVisible(true);
//        // Ensure table is editable
//        table_view.setEditable(true);
//        comment_column.setEditable(true);
//    }
//
//    // Initialize data from TrialsController
//    public void initData(int trialId, String trialPurpose, String departmentName) {
//        this.trialId = trialId;
//        this.trialPurpose = trialPurpose;
//        this.departmentName = departmentName;
//        fileList.clear();
//        loadFileTypes();
//        loadData();
//    }
//
//    // Load files based on user role
//    private void loadData() {
//        fileList.clear();
//        if (UserContext.getCurrentUser().getRole() == 4) {
//            // Super Admin: Load all files for the specific trialId
//            fileList.addAll(FileDAO.getFilesByTrialId(trialId));
//        } else {
//            // Regular user: Load only files for their department and trial
//            int deptId = UserContext.getCurrentUser().getDepartmentId();
//           fileList.addAll(FileDAO.getFilesByTrialId(trialId).filtered(f -> f.getDepartmentId() == deptId));
//       //    int deptId = UserContext.getCurrentUser().getDepartmentId();
//        //    fileList.addAll(FileDAO.getFilesByDepartmentId(deptId).filtered(f -> f.getTrialId() == trialId));
//
//        }
//        table_view.setItems(fileList);
//        table_view.refresh();
//    }
//
//    // Load file types for the current user's department
//    private void loadFileTypes() {
//        int deptId = UserContext.getCurrentUser().getDepartmentId();
//        fileTypes = FileTypeDAO.getAllFileTypes().filtered(ft -> ft.getDepartmentId() == deptId);
//    }
//
//    // Setup table columns and cell factories
//    private void setupTable() {
//        // Verify that all columns are initialized
//        if (file_id_column == null || trial_id_column == null || trial_purpose_column == null ||
//                department_name_column == null || file_type_name_column == null || upload_file_date_column == null ||
//                test_situation_column == null || comment_column == null || upload_file_column == null ||
//                open_file_column == null || download_file_column == null || delete_file_column == null) {
//            Logging.logException("ERROR", this.getClass().getName(), "setupTable", new NullPointerException("One or more table columns are null"));
//            WindowUtils.ALERT("Error", "Table initialization failed. Please check FXML configuration.", WindowUtils.ALERT_ERROR);
//            return;
//        }
//
//        file_id_column.setCellValueFactory(new PropertyValueFactory<>("fileId"));
//        trial_id_column.setCellValueFactory(new PropertyValueFactory<>("trialId"));
//        trial_purpose_column.setCellValueFactory(new PropertyValueFactory<>("trialPurpose"));
//        department_name_column.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
//        file_type_name_column.setCellValueFactory(new PropertyValueFactory<>("fileTypeName"));
//        upload_file_date_column.setCellValueFactory(cellData ->
//                new SimpleStringProperty(cellData.getValue().getCreationDate() != null
//                        ? cellData.getValue().getCreationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
//                        : ""));
//        upload_file_date_column.setStyle("-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;");
//
//        // Test Situation Column with CheckBoxes
//        test_situation_column.setCellFactory(col -> new TableCell<>() {
//            private final CheckBox accepted = new CheckBox("Accepted");
//            private final CheckBox refused = new CheckBox("Refused");
//            private final CheckBox hold = new CheckBox("Hold");
//            private final HBox hBox = new HBox(3, accepted, refused, hold);
//
//            {
//                // Style CheckBoxes as squares with colored backgrounds only when selected
//                accepted.setStyle("-fx-font-size: 10px;-fx-font-weight: bold; -fx-padding: 1px;" +
//                        "-fx-background-color: transparent;" +
//                        "-fx-border-color: white; -fx-border-width: 0; -fx-border-radius: 0;" +
//                        "-fx-shape: \"M0,0 H10 V10 H0 Z\";");
//                refused.setStyle("-fx-font-size: 10px;-fx-font-weight: bold; -fx-padding: 1px;" +
//                        "-fx-background-color: transparent;" +
//                        "-fx-border-color: white; -fx-border-width: 0; -fx-border-radius: 0;" +
//                        "-fx-shape: \"M0,0 H10 V10 H0 Z\";");
//                hold.setStyle("-fx-font-size: 10px;-fx-font-weight: bold; -fx-padding: 1px;" +
//                        "-fx-background-color: transparent;" +
//                        "-fx-border-color: white; -fx-border-width: 0; -fx-border-radius: 0;" +
//                        "-fx-shape: \"M0,0 H10 V10 H0 Z\";");
//
//                // Apply colors only when selected using CSS pseudo-class
//                accepted.getStyleClass().add("accepted-checkbox");
//                refused.getStyleClass().add("refused-checkbox");
//                hold.getStyleClass().add("hold-checkbox");
//
//                hBox.setStyle("-fx-alignment: CENTER; -fx-padding: 1; -fx-spacing: 3;");
//
//                // Handle CheckBox selection with toggle behavior
//                accepted.setOnAction(e -> handleTestSituation(getIndex(), accepted.isSelected() ? 1 : null));
//                refused.setOnAction(e -> handleTestSituation(getIndex(), refused.isSelected() ? 2 : null));
//                hold.setOnAction(e -> handleTestSituation(getIndex(), hold.isSelected() ? 3 : null));
//            }
//
//            private void handleTestSituation(int index, Integer situation) {
//                File file = getTableView().getItems().get(index);
//                if (file.getFileId() != 0) { // Ensure file is saved in DB
//                    file.setTestSituation(situation);
//                    FileDAO.updateFile(file);
//                    updateCheckBoxes(situation);
//                    getTableView().refresh();
//                }
//            }
//
//            private void updateCheckBoxes(Integer situation) {
//                accepted.setSelected(situation != null && situation == 1);
//                refused.setSelected(situation != null && situation == 2);
//                hold.setSelected(situation != null && situation == 3);
//            }
//
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || getTableView().getItems().get(getIndex()).getFileId() == 0) {
//                    setGraphic(null);
//                } else {
//                    File file = getTableView().getItems().get(getIndex());
//                    updateCheckBoxes(file.getTestSituation());
//                    setGraphic(hBox);
//                }
//            }
//        });
//
//        // Comment Column with Double-Click Editable TextField
//        comment_column.setCellValueFactory(new PropertyValueFactory<>("comment"));
//        comment_column.setCellFactory(TextFieldTableCell.forTableColumn());
//        comment_column.setOnEditCommit(event -> {
//            File file = event.getRowValue();
//            if (file.getFileId() != 0) { // Ensure file is saved in DB
//                file.setComment(event.getNewValue());
//                FileDAO.updateFile(file);
//                table_view.getItems().set(table_view.getItems().indexOf(file), file); // Update ObservableList
//                table_view.refresh();
//            }
//        });
//        table_view.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 2 && table_view.getSelectionModel().getSelectedItem() != null) {
//                File selectedFile = table_view.getSelectionModel().getSelectedItem();
//                if (selectedFile.getFileId() != 0) { // Ensure file is saved in DB
//                    table_view.edit(table_view.getSelectionModel().getSelectedIndex(), comment_column);
//                }
//            }
//        });
//
//
//        // Upload Button
//        upload_file_column.setCellFactory(col -> new TableCell<>() {
//            private final Button btn = new Button();
//            private final FontIcon uploadIcon = new FontIcon("fas-upload");
//
//            {
//                uploadIcon.setIconSize(14);
//                uploadIcon.setIconColor(javafx.scene.paint.Color.GREEN);
//                btn.setGraphic(uploadIcon);
//                btn.setStyle("-fx-background-color: transparent;");
//                btn.setCursor(Cursor.HAND);
//                btn.setOnAction(event -> uploadFile(getTableView().getItems().get(getIndex())));
//            }
//
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                setGraphic(empty ? null : btn);
//            }
//        });
//
//        // Open Button
//        open_file_column.setCellFactory(col -> new TableCell<>() {
//            private final Button btn = new Button();
//            private final FontIcon openIcon = new FontIcon("fas-folder-open");
//
//            {
//                openIcon.setIconSize(15);
//                openIcon.setIconColor(javafx.scene.paint.Color.web("#ecab29"));
//                btn.setStyle("-fx-background-color: transparent;");
//                btn.setCursor(Cursor.HAND);
//                btn.setOnAction(event -> openFile(getTableView().getItems().get(getIndex()).getFilePath()));
//            }
//
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || getTableView().getItems().get(getIndex()).getFilePath() == null) {
//                    setGraphic(null);
//                } else {
//                    String fullFileName = new java.io.File(getTableView().getItems().get(getIndex()).getFilePath()).getName();
//                    String fileName = fullFileName.replaceFirst("^\\d+_", "");
//                    btn.setText(fileName);
//                    btn.setGraphic(openIcon);
//                    setGraphic(btn);
//                }
//            }
//        });
//
//        // Download Button
//        download_file_column.setCellFactory(col -> new TableCell<>() {
//            private final Button btn = new Button();
//            private final FontIcon downloadIcon = new FontIcon("fas-download");
//
//            {
//                downloadIcon.setIconSize(14);
//                downloadIcon.setIconColor(javafx.scene.paint.Color.web("#1E90FF"));
//                btn.setGraphic(downloadIcon);
//                btn.setStyle("-fx-background-color: transparent;");
//                btn.setCursor(Cursor.HAND);
//                btn.setOnAction(event -> downloadFile(getTableView().getItems().get(getIndex())));
//            }
//
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                setGraphic(empty || getTableView().getItems().get(getIndex()).getFilePath() == null ? null : btn);
//            }
//        });
//
//        // Delete Button
//        delete_file_column.setCellFactory(col -> new TableCell<>() {
//            private final Button btn = new Button();
//            private final FontIcon deleteIcon = new FontIcon("fas-trash");
//
//            {
//                deleteIcon.setIconSize(14);
//                deleteIcon.setIconColor(javafx.scene.paint.Color.RED);
//                btn.setGraphic(deleteIcon);
//                btn.setStyle("-fx-background-color: transparent;");
//                btn.setCursor(Cursor.HAND);
//                btn.setOnAction(event -> deleteFile(getTableView().getItems().get(getIndex())));
//            }
//
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                setGraphic(empty || getTableView().getItems().get(getIndex()).getFileId() == 0 ? null : btn);
//            }
//        });
//
//        // Apply consistent styling to columns
//        String columnStyle = "-fx-alignment: CENTER; -fx-font-size: 12px; -fx-font-weight: bold;";
//        file_id_column.setStyle(columnStyle);
//        trial_id_column.setStyle(columnStyle);
//        trial_purpose_column.setStyle(columnStyle);
//        department_name_column.setStyle(columnStyle);
//        file_type_name_column.setStyle(columnStyle);
//        upload_file_date_column.setStyle("-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;");
//        test_situation_column.setStyle(columnStyle);
//        comment_column.setStyle("-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;");
//        upload_file_column.setStyle(columnStyle);
//        open_file_column.setStyle(columnStyle);
//        download_file_column.setStyle(columnStyle);
//        delete_file_column.setStyle(columnStyle);
//    }
//
//    // Add new file rows (up to 2 if multiple file types exist)
//    @FXML
//    void addFile(ActionEvent event) {
//        if (fileTypes.size() > 1) {
//            // Add two rows if there are 2 or more file types
//            for (FileType ft : fileTypes.subList(0, fileTypes.size())) {
//                File newFile = new File();
//                newFile.setTrialId(trialId);
//                newFile.setTrialPurpose(trialPurpose);
//                newFile.setDepartmentId(UserContext.getCurrentUser().getDepartmentId());
//                newFile.setDepartmentName(departmentName);
//                newFile.setFileTypeId(ft.getFileTypeId());
//                newFile.setFileTypeName(ft.getFileTypeName());
//                newFile.setUserId(UserContext.getCurrentUser().getUserId());
//                fileList.add(newFile);
//            }
//        } else {
//            // Add one row if there's only one or no file type
//            File newFile = new File();
//            newFile.setTrialId(trialId);
//            newFile.setTrialPurpose(trialPurpose);
//            newFile.setDepartmentId(UserContext.getCurrentUser().getDepartmentId());
//            newFile.setDepartmentName(departmentName);
//            newFile.setFileTypeId(fileTypes.isEmpty() ? null : fileTypes.get(0).getFileTypeId());
//            newFile.setFileTypeName(fileTypes.isEmpty() ? null : fileTypes.get(0).getFileTypeName());
//            newFile.setUserId(UserContext.getCurrentUser().getUserId());
//            fileList.add(newFile);
//        }
//        table_view.setItems(fileList);
//        table_view.refresh();
//    }
//
//
//// Upload file to local or server path
//    private void uploadFile(File fileRecord) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Select File to Upload");
//        java.io.File selectedFile = fileChooser.showOpenDialog(null);
//        if (selectedFile != null) {
//            try {
//                String basePath = isLocalTesting() ? LOCAL_UPLOAD_PATH : SERVER_UPLOAD_PATH;
//                String fileName = selectedFile.getName();
//                String uniqueName = System.currentTimeMillis() + "_" + fileName;
//                java.io.File dest = new java.io.File(basePath + uniqueName);
//
//                dest.getParentFile().mkdirs();
//                Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
//
//                if (dest.exists()) {
//                    fileRecord.setFilePath(uniqueName);
//                    fileRecord.setCreationDate(LocalDate.now());
//                    int index = fileList.indexOf(fileRecord);
//                    if (fileRecord.getFileId() == 0) {
//                        // Insert file and retrieve fileId using filePath
//                        if (FileDAO.insertFile(fileRecord)) {
//                            File fetchedFile = FileDAO.getFileByFilePath(uniqueName);
//                            if (fetchedFile != null && fetchedFile.getFileId() != 0) {
//                                fileRecord.setFileId(fetchedFile.getFileId()); // Update fileId
//                            }
//                        }
//                    } else {
//                        FileDAO.updateFile(fileRecord);
//                    }
//                    // Update fileList with the modified fileRecord
//                    if (index != -1) {
//                        fileList.set(index, fileRecord); // Update the existing file in the list
//                    }
//                    table_view.setItems(fileList);
//                    table_view.refresh();
//                    WindowUtils.ALERT("Success", "File uploaded successfully", WindowUtils.ALERT_INFORMATION);
//                } else {
//                    WindowUtils.ALERT("Error", "Failed to upload file", WindowUtils.ALERT_ERROR);
//                }
//            } catch (IOException e) {
//                Logging.logExpWithMessage("ERROR", getClass().getName(), "uploadFile", e, "io", selectedFile.getAbsolutePath());
//                WindowUtils.ALERT("Error", "Failed to upload file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
//            }
//        }
//    }
//
//    // Open file using default system application
//    private void openFile(String fileName) {
//        try {
//            if (fileName != null && !fileName.isEmpty()) {
//                String basePath = isLocalTesting() ? LOCAL_UPLOAD_PATH : SERVER_UPLOAD_PATH;
//                String fullPath = basePath + fileName;
//                java.io.File networkFile = new java.io.File(fullPath);
//                if (networkFile.exists()) {
//                    String tempDir = System.getProperty("java.io.tmpdir");
//                    java.io.File tempFile = new java.io.File(tempDir + fileName);
//                    Files.copy(networkFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                    java.awt.Desktop.getDesktop().open(tempFile);
//                    tempFile.deleteOnExit();
//                } else {
//                    WindowUtils.ALERT("Error", "File not found at: " + fullPath, WindowUtils.ALERT_ERROR);
//                }
//            } else {
//                WindowUtils.ALERT("Error", "File name is empty", WindowUtils.ALERT_ERROR);
//            }
//        } catch (IOException e) {
//            Logging.logExpWithMessage("ERROR", getClass().getName(), "openFile", e, "file", fileName);
//            WindowUtils.ALERT("Error", "Failed to open file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
//        }
//    }
//
//    // Download file to user-selected location
//    private void downloadFile(File fileRecord) {
//        try {
//            if (fileRecord != null && fileRecord.getFilePath() != null && !fileRecord.getFilePath().isEmpty()) {
//                String fileName = fileRecord.getFilePath();
//                String basePath = isLocalTesting() ? LOCAL_UPLOAD_PATH : SERVER_UPLOAD_PATH;
//                String fullPath = basePath + fileName;
//
//                java.io.File networkFile = new java.io.File(fullPath);
//                if (networkFile.exists()) {
//                    FileChooser fileChooser = new FileChooser();
//                    fileChooser.setInitialFileName(fileName.replaceFirst("^\\d+_", ""));
//                    fileChooser.setTitle("Save File");
//                    java.io.File saveFile = fileChooser.showSaveDialog(null);
//
//                    if (saveFile != null) {
//                        Files.copy(networkFile.toPath(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                        WindowUtils.ALERT("Success", "File downloaded successfully to: " + saveFile.getAbsolutePath(), WindowUtils.ALERT_INFORMATION);
//                    } else {
//                        WindowUtils.ALERT("Error", "Download cancelled by user", WindowUtils.ALERT_ERROR);
//                    }
//                } else {
//                    WindowUtils.ALERT("Error", "Source file doesn't exist at: " + fullPath, WindowUtils.ALERT_ERROR);
//                }
//            } else {
//                WindowUtils.ALERT("Error", "File name is empty", WindowUtils.ALERT_ERROR);
//            }
//        } catch (IOException e) {
//            Logging.logExpWithMessage("ERROR", getClass().getName(), "downloadFile", e, "file", fileRecord != null ? fileRecord.getFilePath() : "null");
//            WindowUtils.ALERT("Error", "Failed to download file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
//        }
//    }
//
//    // Delete file with confirmation and password verification
//    private void deleteFile(File file) {
//        if (file.getFileId() != 0) {
//            // Show confirmation dialog
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Delete Confirmation");
//            alert.setHeaderText("Are you sure you want to delete this file?");
//            String fileName = new java.io.File(file.getFilePath()).getName().replaceFirst("^\\d+_", "");
//            alert.setContentText("File: " + fileName);
//
//            ButtonType okButton = ButtonType.OK;
//            ButtonType cancelButton = ButtonType.CANCEL;
//            alert.getButtonTypes().setAll(okButton, cancelButton);
//
//            Button okBtn = (Button) alert.getDialogPane().lookupButton(okButton);
//            Button cancelBtn = (Button) alert.getDialogPane().lookupButton(cancelButton);
//            okBtn.setText("OK");
//            cancelBtn.setText("Cancel");
//            Platform.runLater(() -> cancelBtn.requestFocus());
//
//            alert.showAndWait().ifPresent(response -> {
//                if (response == okButton) {
//                    // Verify password using UserService
//                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
//                        try {
//                            boolean deleted = FileDAO.deleteFile(file.getFileId());
//                            if (deleted) {
//                                fileList.remove(file);
//                                loadData();
//                                WindowUtils.ALERT("Success", "File deleted successfully", WindowUtils.ALERT_INFORMATION);
//                            } else {
//                                WindowUtils.ALERT("Error", "Failed to delete file", WindowUtils.ALERT_ERROR);
//                            }
//                        } catch (Exception ex) {
//                            Logging.logExpWithMessage("ERROR", getClass().getName(), "deleteFile", ex, "file", file.getFilePath());
//                            WindowUtils.ALERT("Error", "Failed to delete file", WindowUtils.ALERT_ERROR);
//                        }
//                    } else {
//                        WindowUtils.ALERT("Error", "Password not correct", WindowUtils.ALERT_WARNING);
//                    }
//                }
//            });
//        }
//    }
//
//    // Check if running in local testing mode
//    private boolean isLocalTesting() {
//        return !new java.io.File(SERVER_UPLOAD_PATH).exists();
//    }
//}



package com.elsewedyt.trialsapp.controllers;

import com.elsewedyt.trialsapp.dao.FileDAO;
import com.elsewedyt.trialsapp.dao.FileTypeDAO;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.models.File;
import com.elsewedyt.trialsapp.models.FileType;
import com.elsewedyt.trialsapp.models.UserContext;
import com.elsewedyt.trialsapp.services.UserService;
import com.elsewedyt.trialsapp.services.WindowUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.javafx.FontIcon;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddFileController implements Initializable {

    @FXML
    private Button addFile_btn;
    @FXML
    private TableColumn<File, String> comment_column;
    @FXML
    private TableColumn<File, String> delete_file_column;
    @FXML
    private TableColumn<File, String> department_name_column;
    @FXML
    private TableColumn<File, String> download_file_column;
    @FXML
    private TableColumn<File, String> file_id_column;
    @FXML
    private TableColumn<File, String> file_type_name_column;
    @FXML
    private TableColumn<File, String> open_file_column;
    @FXML
    private TableView<File> table_view;
    @FXML
    private TableColumn<File, String> test_situation_column;
    @FXML
    private Label title_lbl;
    @FXML
    private TableColumn<File, String> trial_id_column;
    @FXML
    private TableColumn<File, String> trial_purpose_column;
    @FXML
    private TableColumn<File, String> upload_file_column;
    @FXML
    private TableColumn<File, String> upload_file_date_column;

    private ObservableList<File> fileList = FXCollections.observableArrayList();
    private ObservableList<FileType> fileTypes = FXCollections.observableArrayList();
    private int trialId;
    private String trialPurpose;
    private String departmentName;
    private static final String SERVER_UPLOAD_PATH = "\\\\ETCSVR\\TrialsUpload\\";
    private static String selectedDownloadPath = null; // Store download path for the session
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm a");

    // Initialize the controller
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> title_lbl.requestFocus());
        setupTable();
        addFile_btn.setCursor(Cursor.HAND);
        // Ensure all users can see action buttons
        addFile_btn.setVisible(true);
        upload_file_column.setVisible(true);
        download_file_column.setVisible(true);
        delete_file_column.setVisible(true);
        // Ensure table is editable
        table_view.setEditable(true);
        comment_column.setEditable(true);
        // Reset download path when initializing a new session
        selectedDownloadPath = null;
    }

    // Initialize data from TrialsController
    public void initData(int trialId, String trialPurpose, String departmentName) {
        this.trialId = trialId;
        this.trialPurpose = trialPurpose;
        this.departmentName = departmentName;
        fileList.clear();
        loadFileTypes();
        loadData();

    }

    // Load files based on user role
    private void loadData() {
        fileList.clear();
        if (UserContext.getCurrentUser().getRole() == 4) {
            // Super Admin: Load all files for the specific trialId
            fileList.addAll(FileDAO.getFilesByTrialId(trialId));
        } else {
            // Regular user: Load only files for their department and trial
            int deptId = UserContext.getCurrentUser().getDepartmentId();
            fileList.addAll(FileDAO.getFilesByTrialId(trialId).filtered(f -> f.getDepartmentId() == deptId));
        }
        table_view.setItems(fileList);
        table_view.refresh();
    }

    // Load file types for the current user's department
    private void loadFileTypes() {
        int deptId = UserContext.getCurrentUser().getDepartmentId();
        fileTypes = FileTypeDAO.getAllFileTypes().filtered(ft -> ft.getDepartmentId() == deptId);
    }

    // Setup table columns and cell factories
    private void setupTable() {
        // Verify that all columns are initialized
        if (file_id_column == null || trial_id_column == null || trial_purpose_column == null ||
                department_name_column == null || file_type_name_column == null || upload_file_date_column == null ||
                test_situation_column == null || comment_column == null || upload_file_column == null ||
                open_file_column == null || download_file_column == null || delete_file_column == null) {
            Logging.logException("ERROR", this.getClass().getName(), "setupTable", new NullPointerException("One or more table columns are null"));
            WindowUtils.ALERT("Error", "Table initialization failed. Please check FXML configuration.", WindowUtils.ALERT_ERROR);
            return;
        }

        file_id_column.setCellValueFactory(new PropertyValueFactory<>("fileId"));
        trial_id_column.setCellValueFactory(new PropertyValueFactory<>("trialId"));
        trial_purpose_column.setCellValueFactory(new PropertyValueFactory<>("trialPurpose"));
        department_name_column.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        file_type_name_column.setCellValueFactory(new PropertyValueFactory<>("fileTypeName"));
        upload_file_date_column.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getCreationDate() != null
                                ? cellData.getValue().getCreationDate().format(dateFormatter)
                                : ""
                )
        );
        upload_file_date_column.setStyle("-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;");

        // Test Situation Column with CheckBoxes
        test_situation_column.setCellFactory(col -> new TableCell<>() {
            private final CheckBox accepted = new CheckBox("Accepted");
            private final CheckBox refused = new CheckBox("Refused");
            private final CheckBox hold = new CheckBox("Hold");
            private final HBox hBox = new HBox(2, accepted, refused, hold);

            {
                // Style CheckBoxes as squares with colored backgrounds only when selected
                accepted.setStyle("-fx-font-size: 10px;-fx-font-weight: bold; -fx-padding: 1px;" +
                        "-fx-background-color: transparent;" +
                        "-fx-border-color: white; -fx-border-width: 0; -fx-border-radius: 0;" +
                        "-fx-shape: \"M0,0 H10 V10 H0 Z\";");
                refused.setStyle("-fx-font-size: 10px;-fx-font-weight: bold; -fx-padding: 1px;" +
                        "-fx-background-color: transparent;" +
                        "-fx-border-color: white; -fx-border-width: 0; -fx-border-radius: 0;" +
                        "-fx-shape: \"M0,0 H10 V10 H0 Z\";");
                hold.setStyle("-fx-font-size: 10px;-fx-font-weight: bold; -fx-padding: 1px;" +
                        "-fx-background-color: transparent;" +
                        "-fx-border-color: white; -fx-border-width: 0; -fx-border-radius: 0;" +
                        "-fx-shape: \"M0,0 H10 V10 H0 Z\";");

                // Apply colors only when selected using CSS pseudo-class
                accepted.getStyleClass().add("accepted-checkbox");
                refused.getStyleClass().add("refused-checkbox");
                hold.getStyleClass().add("hold-checkbox");

                hBox.setStyle("-fx-alignment: CENTER; -fx-padding: 1; -fx-spacing: 3;");

                // Handle CheckBox selection with toggle behavior
                accepted.setOnAction(e -> handleTestSituation(getIndex(), accepted.isSelected() ? 1 : null));
                refused.setOnAction(e -> handleTestSituation(getIndex(), refused.isSelected() ? 2 : null));
                hold.setOnAction(e -> handleTestSituation(getIndex(), hold.isSelected() ? 3 : null));
            }

            private void handleTestSituation(int index, Integer situation) {
                File file = getTableView().getItems().get(index);
                if (file.getFileId() != 0) { // Ensure file is saved in DB
                    file.setTestSituation(situation);
                    FileDAO.updateFile(file);
                    updateCheckBoxes(situation);
                    getTableView().refresh();
                }
            }

            private void updateCheckBoxes(Integer situation) {
                accepted.setSelected(situation != null && situation == 1);
                refused.setSelected(situation != null && situation == 2);
                hold.setSelected(situation != null && situation == 3);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()).getFileId() == 0) {
                    setGraphic(null);
                } else {
                    File file = getTableView().getItems().get(getIndex());
                    updateCheckBoxes(file.getTestSituation());
                    setGraphic(hBox);
                }
            }
        });

        // Comment Column with Double-Click Editable TextField
        comment_column.setCellValueFactory(new PropertyValueFactory<>("comment"));
        comment_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comment_column.setOnEditCommit(event -> {
            File file = event.getRowValue();
            if (file.getFileId() != 0) { // Ensure file is saved in DB
                file.setComment(event.getNewValue());
                FileDAO.updateFile(file);
                table_view.getItems().set(table_view.getItems().indexOf(file), file); // Update ObservableList
                table_view.refresh();
            }
        });
        table_view.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && table_view.getSelectionModel().getSelectedItem() != null) {
                File selectedFile = table_view.getSelectionModel().getSelectedItem();
                if (selectedFile.getFileId() != 0) { // Ensure file is saved in DB
                    table_view.edit(table_view.getSelectionModel().getSelectedIndex(), comment_column);
                }
            }
        });

        // Upload Button
        upload_file_column.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button();
            private final FontIcon uploadIcon = new FontIcon("fas-upload");

            {
                uploadIcon.setIconSize(14);
                uploadIcon.setIconColor(javafx.scene.paint.Color.GREEN);
                btn.setGraphic(uploadIcon);
                btn.setStyle("-fx-background-color: transparent;");
                btn.setCursor(Cursor.HAND);
                btn.setOnAction(event -> uploadFile(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Open Button
        open_file_column.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button();
            private final FontIcon openIcon = new FontIcon("fas-folder-open");

            {
                openIcon.setIconSize(15);
                openIcon.setIconColor(javafx.scene.paint.Color.web("#ecab29"));
                btn.setStyle("-fx-background-color: transparent;");
                btn.setCursor(Cursor.HAND);
                btn.setOnAction(event -> openFile(getTableView().getItems().get(getIndex()).getFilePath()));
            }

//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || getTableView().getItems().get(getIndex()).getFilePath() == null) {
//                    setGraphic(null);
//                } else {
//                    String fullFileName = new java.io.File(getTableView().getItems().get(getIndex()).getFilePath()).getName();
//                    String fileName = fullFileName.replaceFirst("^\\d+_", "");
//                    btn.setText(fileName);
//                    btn.setGraphic(openIcon);
//                    setGraphic(btn);
//                }
//            }
//        });
@Override
protected void updateItem(String item, boolean empty) {
    super.updateItem(item, empty);
    if (empty || getTableView().getItems().get(getIndex()).getFilePath() == null) {
        setGraphic(null);
    } else {
        String fullFileName = new java.io.File(getTableView().getItems().get(getIndex()).getFilePath()).getName();

        String nameWithoutExtension = fullFileName.contains(".") ?
                fullFileName.substring(0, fullFileName.lastIndexOf(".")) : fullFileName;

        String originalName = nameWithoutExtension.contains("_") ?
                nameWithoutExtension.substring(0, nameWithoutExtension.indexOf("_")) : nameWithoutExtension;

        String extension = fullFileName.contains(".") ?
                fullFileName.substring(fullFileName.lastIndexOf(".")) : "";

        String fileName = originalName + extension;

        btn.setText(fileName);
        btn.setGraphic(openIcon);
        setGraphic(btn);
        Tooltip tooltip = new Tooltip(fileName);
        tooltip.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-background-color: #f4f4f4; -fx-text-fill: #333;");
        Tooltip.install(btn, tooltip);
    }
   }
        });

                    // Download Button
        download_file_column.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button();
            private final FontIcon downloadIcon = new FontIcon("fas-download");

            {
                downloadIcon.setIconSize(14);
                downloadIcon.setIconColor(javafx.scene.paint.Color.web("#1E90FF"));
                btn.setGraphic(downloadIcon);
                btn.setStyle("-fx-background-color: transparent;");
                btn.setCursor(Cursor.HAND);
                btn.setOnAction(event -> downloadFile(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || getTableView().getItems().get(getIndex()).getFilePath() == null ? null : btn);
            }
        });

        // Delete Button
        delete_file_column.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button();
            private final FontIcon deleteIcon = new FontIcon("fas-trash");

            {
                deleteIcon.setIconSize(14);
                deleteIcon.setIconColor(javafx.scene.paint.Color.RED);
                btn.setGraphic(deleteIcon);
                btn.setStyle("-fx-background-color: transparent;");
                btn.setCursor(Cursor.HAND);
                btn.setOnAction(event -> deleteFile(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || getTableView().getItems().get(getIndex()).getFileId() == 0 ? null : btn);
            }
        });

        // Apply consistent styling to columns
        String columnStyle1 = "-fx-alignment: CENTER; -fx-font-size: 12px; -fx-font-weight: bold;";
        String columnStyle2 = "-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;";
        String columnStyle3 = "-fx-alignment: CENTER; -fx-font-size: 10px; -fx-font-weight: bold;";
        file_id_column.setStyle(columnStyle1);
        trial_id_column.setStyle(columnStyle1);
        trial_purpose_column.setStyle(columnStyle1);
        department_name_column.setStyle(columnStyle1);
        file_type_name_column.setStyle(columnStyle1);
        upload_file_date_column.setStyle(columnStyle2);
        test_situation_column.setStyle(columnStyle1);
        comment_column.setStyle(columnStyle2);
        upload_file_column.setStyle(columnStyle1);
        open_file_column.setStyle(columnStyle3);
        download_file_column.setStyle(columnStyle1);
        delete_file_column.setStyle(columnStyle1);
        table_view.setFixedCellSize(36);
    }


    // Add new file rows (up to 2 if multiple file types exist)
    @FXML
    void addFile(ActionEvent event) {
        if (fileTypes.size() > 1) {
            // Add two rows if there are 2 or more file types
            for (FileType ft : fileTypes.subList(0, fileTypes.size())) {
                File newFile = new File();
                newFile.setTrialId(trialId);
                newFile.setTrialPurpose(trialPurpose);
                newFile.setDepartmentId(UserContext.getCurrentUser().getDepartmentId());
                newFile.setDepartmentName(departmentName);
                newFile.setFileTypeId(ft.getFileTypeId());
                newFile.setFileTypeName(ft.getFileTypeName());
                newFile.setUserId(UserContext.getCurrentUser().getUserId());
                fileList.add(newFile);
            }
        } else {
            // Add one row if there's only one or no file type
            File newFile = new File();
            newFile.setTrialId(trialId);
            newFile.setTrialPurpose(trialPurpose);
            newFile.setDepartmentId(UserContext.getCurrentUser().getDepartmentId());
            newFile.setDepartmentName(departmentName);
            newFile.setFileTypeId(fileTypes.isEmpty() ? null : fileTypes.get(0).getFileTypeId());
            newFile.setFileTypeName(fileTypes.isEmpty() ? null : fileTypes.get(0).getFileTypeName());
            newFile.setUserId(UserContext.getCurrentUser().getUserId());
            fileList.add(newFile);
        }
        table_view.setItems(fileList);
        table_view.refresh();
    }

    // Upload file from local device to server path
    private void uploadFile(File fileRecord) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Upload");
        java.io.File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                String fileName = selectedFile.getName();
               // String uniqueName = System.currentTimeMillis() + "_" + fileName;
                int deptId = UserContext.getCurrentUser().getDepartmentId();
                int userId = UserContext.getCurrentUser().getUserId();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
                String timestamp = LocalDateTime.now().format(formatter);

                String namePart = fileName.contains(".") ?
                        fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
                String extension = fileName.contains(".") ?
                        fileName.substring(fileName.lastIndexOf(".")) : "";

                String uniqueName = namePart + "_" + deptId + "-" + userId + "_" + timestamp + extension;

                java.io.File dest = new java.io.File(SERVER_UPLOAD_PATH + uniqueName);

                dest.getParentFile().mkdirs();
                Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

                if (dest.exists()) {
                    fileRecord.setFilePath(uniqueName);
                    fileRecord.setCreationDate(LocalDateTime.now());
                    int index = fileList.indexOf(fileRecord);
                    if (fileRecord.getFileId() == 0) {
                        // Insert file and retrieve fileId using filePath
                        if (FileDAO.insertFile(fileRecord)) {
                            File fetchedFile = FileDAO.getFileByFilePath(uniqueName);
                            if (fetchedFile != null && fetchedFile.getFileId() != 0) {
                                fileRecord.setFileId(fetchedFile.getFileId()); // Update fileId
                            }
                        }
                    } else {
                        FileDAO.updateFile(fileRecord);
                    }
                    // Update fileList with the modified fileRecord
                    if (index != -1) {
                        fileList.set(index, fileRecord); // Update the existing file in the list
                    }
                    table_view.setItems(fileList);
                    table_view.refresh();
                    WindowUtils.ALERT("Success", "File uploaded successfully", WindowUtils.ALERT_INFORMATION);
                } else {
                    WindowUtils.ALERT("Error", "Failed to upload file", WindowUtils.ALERT_ERROR);
                }
            } catch (IOException e) {
                Logging.logExpWithMessage("ERROR", getClass().getName(), "uploadFile", e, "io", selectedFile.getAbsolutePath());
                WindowUtils.ALERT("Error", "Failed to upload file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
            }
        }
    }

    // Open file using default system application with temporary local copy
    private void openFile(String fileName) {
        try {
            if (fileName != null && !fileName.isEmpty()) {
                String fullPath = SERVER_UPLOAD_PATH + fileName;
                java.io.File networkFile = new java.io.File(fullPath);
                if (networkFile.exists()) {
                    String tempDir = System.getProperty("java.io.tmpdir");
                    java.io.File tempFile = new java.io.File(tempDir + fileName);
                    Files.copy(networkFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    if (tempFile.exists()) {
                        java.awt.Desktop.getDesktop().open(tempFile);
                        tempFile.deleteOnExit();
                    } else {
                        WindowUtils.ALERT("Error", "Failed to copy file to: " + tempFile.getAbsolutePath(), WindowUtils.ALERT_ERROR);
                    }
                } else {
                    WindowUtils.ALERT("Error", "File not found at: " + fullPath, WindowUtils.ALERT_ERROR);
                }
            } else {
                WindowUtils.ALERT("Error", "File name is empty", WindowUtils.ALERT_ERROR);
            }
        } catch (IOException e) {
            Logging.logExpWithMessage("ERROR", getClass().getName(), "openFile", e, "file", fileName);
            WindowUtils.ALERT("Error", "Failed to open file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
        }
    }

    // Download file from server path to a user-selected location (once per session)
    private void downloadFile(File fileRecord) {
        try {
            if (fileRecord != null && fileRecord.getFilePath() != null && !fileRecord.getFilePath().isEmpty()) {
                String fileName = fileRecord.getFilePath();
                String fullPath = SERVER_UPLOAD_PATH + fileName;
                java.io.File networkFile = new java.io.File(fullPath);
                if (networkFile.exists()) {
                    java.io.File saveFile;
                    if (selectedDownloadPath == null) {
                        // Open FileChooser for the first download in the session
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setInitialFileName(fileName.replaceFirst("^\\d+_", ""));
                        fileChooser.setTitle("Select Download Location");
                        java.io.File selectedDir = fileChooser.showSaveDialog(null);
                        if (selectedDir != null) {
                            // Save the directory path for the session
                            selectedDownloadPath = selectedDir.getParent();
                            saveFile = selectedDir;
                        } else {
                            WindowUtils.ALERT("Error", "Download cancelled by user", WindowUtils.ALERT_ERROR);
                            return;
                        }
                    } else {
                        // Use the previously selected directory for subsequent downloads
                        saveFile = new java.io.File(selectedDownloadPath + java.io.File.separator + fileName.replaceFirst("^\\d+_", ""));
                    }

                    // Copy the file to the selected or previously chosen location
                    Files.copy(networkFile.toPath(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    if (saveFile.exists()) {
                        WindowUtils.ALERT("Success", "File downloaded successfully to: " + saveFile.getAbsolutePath(), WindowUtils.ALERT_INFORMATION);
                    } else {
                        WindowUtils.ALERT("Error", "Failed to download file", WindowUtils.ALERT_ERROR);
                    }
                } else {
                    WindowUtils.ALERT("Error", "Source file doesn't exist at: " + fullPath, WindowUtils.ALERT_ERROR);
                }
            } else {
                WindowUtils.ALERT("Error", "File name is empty", WindowUtils.ALERT_ERROR);
            }
        } catch (IOException e) {
            Logging.logExpWithMessage("ERROR", getClass().getName(), "downloadFile", e, "file", fileRecord != null ? fileRecord.getFilePath() : "null");
            WindowUtils.ALERT("Error", "Failed to download file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
        }
    }

    // Delete file with confirmation and password verification
    private void deleteFile(File file) {
        if (file.getFileId() != 0) {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Are you sure you want to delete this file?");
            String fileName = new java.io.File(file.getFilePath()).getName().replaceFirst("^\\d+_", "");
            alert.setContentText("File: " + fileName);

            ButtonType okButton = ButtonType.OK;
            ButtonType cancelButton = ButtonType.CANCEL;
            alert.getButtonTypes().setAll(okButton, cancelButton);

            Button okBtn = (Button) alert.getDialogPane().lookupButton(okButton);
            Button cancelBtn = (Button) alert.getDialogPane().lookupButton(cancelButton);
            okBtn.setText("OK");
            cancelBtn.setText("Cancel");
            Platform.runLater(() -> cancelBtn.requestFocus());

            alert.showAndWait().ifPresent(response -> {
                if (response == okButton) {
                    // Verify password using UserService
                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
                        try {
                            boolean deleted = FileDAO.deleteFile(file.getFileId());
                            if (deleted) {
                                fileList.remove(file);
                                loadData();
                                WindowUtils.ALERT("Success", "File deleted successfully", WindowUtils.ALERT_INFORMATION);
                            } else {
                                WindowUtils.ALERT("Error", "Failed to delete file", WindowUtils.ALERT_ERROR);
                            }
                        } catch (Exception ex) {
                            Logging.logExpWithMessage("ERROR", getClass().getName(), "deleteFile", ex, "file", file.getFilePath());
                            WindowUtils.ALERT("Error", "Failed to delete file", WindowUtils.ALERT_ERROR);
                        }
                    } else {
                        WindowUtils.ALERT("Error", "Password not correct", WindowUtils.ALERT_WARNING);
                    }
                }
            });
        }
    }
}