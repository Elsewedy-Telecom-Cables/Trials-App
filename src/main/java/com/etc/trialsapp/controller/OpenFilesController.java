
package com.etc.trialsapp.controller;

import com.etc.trialsapp.dao.AppContext;
import com.etc.trialsapp.dao.FileTypeDao;
import com.etc.trialsapp.dao.FileViewDao;
import com.etc.trialsapp.db.DEF;
import com.etc.trialsapp.logging.Logging;
import com.etc.trialsapp.model.FileView;
import com.etc.trialsapp.model.FileType;
import com.etc.trialsapp.service.WindowUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.javafx.FontIcon;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class OpenFilesController implements Initializable {

    @FXML
    private TableView<FileView> table_view;
    @FXML
    private TableColumn<FileView, String> comment_column;
    @FXML
    private TableColumn<FileView, String> department_name_column;
    @FXML
    private TableColumn<FileView, String> download_file_column;
    @FXML
    private TableColumn<FileView, String> file_id_column;
    @FXML
    private TableColumn<FileView, String> file_type_name_column;
    @FXML
    private TableColumn<FileView, String> open_file_column;
    @FXML
    private TableColumn<FileView, String> test_situation_column;
    @FXML
    private TableColumn<FileView, String> trial_id_column;
    @FXML
    private TableColumn<FileView, String> full_name_column;
    @FXML
    private TableColumn<FileView, LocalDateTime> craetion_file_date_column;

    @FXML
    private Label View_Department_files_lbl;

    private ObservableList<FileView> fileList = FXCollections.observableArrayList();
    private ObservableList<FileType> fileTypes = FXCollections.observableArrayList();
    private int trialId;
    private String departmentName;
    private static final String SERVER_UPLOAD_PATH = "\\\\ETCSVR\\TrialsUpload\\";
    private static String selectedDownloadPath = null; // Store download path for the session
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm a");
    
    
    private final FileViewDao fileViewDao = AppContext.getInstance().getFileViewDao();
    private final FileTypeDao fileTypeDao = AppContext.getInstance().getFileTypeDao();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> View_Department_files_lbl.requestFocus());
        setupTable();
        // Reset download path when initializing a new session
        selectedDownloadPath = null;
    }

    // Initialize data from DashboardController
    public void initData(int trialId, String departmentName) {
        this.trialId = trialId;
        this.departmentName = departmentName;
        fileList.clear();
        loadFileTypes();
        loadData();
    }

    // Load files for the selected department
    private void loadData() {
        fileList.clear();
        // Load files for the specific trialId and departmentName for all users
        fileList.addAll(fileViewDao.getFilesByTrialIdAndDepartmentName(trialId, departmentName));
        // Debug: Log the loaded files
       // System.out.println("Loaded files for Trial ID: " + trialId + ", Department: " + departmentName + ", Files: " + fileList);
        table_view.setItems(fileList);
        table_view.refresh();
    }

    // Load file types for the current user's department
    private void loadFileTypes() {
        fileTypes = fileTypeDao.getAllFileTypes();
        // Debug: Log the loaded file types
    //    System.out.println("Loaded file types: " + fileTypes);
    }

    // Setup table columns and cell factories
    private void setupTable() {
        // Verify that all columns are initialized
        if (file_id_column == null || trial_id_column == null ||
                department_name_column == null || file_type_name_column == null || craetion_file_date_column == null ||
                test_situation_column == null || comment_column == null ||
                open_file_column == null || download_file_column == null || full_name_column == null) {
            Logging.logException("ERROR", this.getClass().getName(), "setupTable", new NullPointerException("One or more table columns are null"));
            WindowUtils.ALERT("Error", "Table initialization failed. Please check FXML configuration.", WindowUtils.ALERT_ERROR);
            return;
        }

        file_id_column.setCellValueFactory(new PropertyValueFactory<>("fileId"));
        trial_id_column.setCellValueFactory(new PropertyValueFactory<>("trialId"));
        department_name_column.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        file_type_name_column.setCellValueFactory(new PropertyValueFactory<>("fileTypeName"));
        full_name_column.setCellValueFactory(new PropertyValueFactory<>("userFullName"));
        comment_column.setCellValueFactory(new PropertyValueFactory<>("comment"));
//        craetion_file_date_column.setCellValueFactory(cellData ->
//                new SimpleStringProperty(cellData.getValue().getFileCreationDate() != null
//                        ? cellData.getValue().getFileCreationDate().format(dateFormatter)
//                        : ""));

        craetion_file_date_column.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getFileCreationDate()));
        craetion_file_date_column.setCellFactory(column -> new TableCell<FileView, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });

        craetion_file_date_column.setStyle("-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;");

        // Test Situation Column with colored text
        test_situation_column.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setStyle("");
                } else {
                    FileView file = getTableRow().getItem();
                    String testSit = file.getTestSituation();
                    String color = "";

                    if (testSit != null && testSit.equals(DEF.TEST_SITUATION_ACCEPTED)) {
                        color = "#6ae26a"; // Green
                        testSit = DEF.TEST_SITUATION_ACCEPTED;
                    } else if (testSit != null && testSit.equals(DEF.TEST_SITUATION_REFUSED)) {
                        color = "#ff6b6b"; // Red
                        testSit = DEF.TEST_SITUATION_REFUSED;
                    } else if (testSit != null && testSit.equals(DEF.TEST_SITUATION_HOLD)) {
                        color = "#ffe97d"; // Yellow
                        testSit = DEF.TEST_SITUATION_HOLD;
                    }
                    setText(testSit != null ? testSit : "");
                    if (!color.isEmpty()) {
                        setStyle(String.format("-fx-background-color: %s; -fx-alignment: center; -fx-font-weight: bold;", color));
                    } else {
                        setStyle("-fx-alignment: center; -fx-font-weight: bold;");
                    }
                }
            }
        });

        // Open Button
        open_file_column.setCellFactory(col -> new TableCell<FileView, String>() {
            private final Button btn = new Button();
            private final FontIcon openIcon = new FontIcon("fas-folder-open");

            {
                openIcon.setIconSize(13);
                openIcon.setIconColor(javafx.scene.paint.Color.web("#ecab29"));
                btn.setStyle("-fx-background-color: transparent;");
                btn.setCursor(Cursor.HAND);
                btn.setOnAction(event -> openFile(getTableView().getItems().get(getIndex()).getFilePath()));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()).getFilePath() == null) {
                    setGraphic(null);
                } else {
                    String fullFileName = new File(getTableView().getItems().get(getIndex()).getFilePath()).getName();
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
                    open_file_column.setStyle("-fx-alignment: CENTER; -fx-font-size: 10px; -fx-font-weight: bold;");
                }
            }

        });

        // Download Button
        download_file_column.setCellFactory(col -> new TableCell<FileView, String>() {
            private final Button btn = new Button();
            private final FontIcon downloadIcon = new FontIcon("fas-download");

            {
                downloadIcon.setIconSize(12);
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

        // Apply consistent styling to columns
        String columnStyle = "-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;";
        file_id_column.setStyle(columnStyle);
        trial_id_column.setStyle(columnStyle);
        department_name_column.setStyle(columnStyle);
        file_type_name_column.setStyle(columnStyle);
        full_name_column.setStyle(columnStyle);
        craetion_file_date_column.setStyle(columnStyle);
        test_situation_column.setStyle(columnStyle);
        comment_column.setStyle(columnStyle);
        open_file_column.setStyle(columnStyle);
        download_file_column.setStyle(columnStyle);

        table_view.setFixedCellSize(30);
        table_view.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table_view.setEditable(false);
        table_view.refresh();
    }

    // Open file using default system application with temporary local copy
    private void openFile(String fileName) {
        try {
            if (fileName != null && !fileName.isEmpty()) {
                String fullPath = SERVER_UPLOAD_PATH + fileName;
                File networkFile = new File(fullPath);
                if (networkFile.exists()) {
                    String tempDir = System.getProperty("java.io.tmpdir");
                    File tempFile = new File(tempDir + fileName);
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
    private void downloadFile(FileView fileRecord) {
        try {
            if (fileRecord != null && fileRecord.getFilePath() != null && !fileRecord.getFilePath().isEmpty()) {
                String fileName = fileRecord.getFilePath();
                String fullPath = SERVER_UPLOAD_PATH + fileName;
                File networkFile = new File(fullPath);
                if (networkFile.exists()) {
                    File saveFile;
                    if (selectedDownloadPath == null) {
                        // Open FileChooser for the first download in the session
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setInitialFileName(fileName.replaceFirst("^\\d+_", ""));
                        fileChooser.setTitle("Select Download Location");
                        File selectedDir = fileChooser.showSaveDialog(null);
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
                        saveFile = new File(selectedDownloadPath + File.separator + fileName.replaceFirst("^\\d+_", ""));
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
}