
package com.elsewedyt.trialsapp.controller;

import com.elsewedyt.trialsapp.dao.*;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.model.*;
import com.elsewedyt.trialsapp.service.ConfigLoader;
import com.elsewedyt.trialsapp.service.UserService;
import com.elsewedyt.trialsapp.service.WindowUtils;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AddFileController implements Initializable {

    @FXML private Button addFile_btn;
    @FXML private TableColumn<File, String> comment_column;
    @FXML private TableColumn<File, String> delete_file_column;
    @FXML private TableColumn<File, String> department_name_column;
    @FXML private TableColumn<File, String> download_file_column;
    @FXML private TableColumn<File, String> file_id_column;
    @FXML private TableColumn<File, String> file_type_name_column;
    @FXML private TableColumn<File, String> open_file_column;
    @FXML private TableView<File> table_view;
    @FXML private TableColumn<File, String> test_situation_column;
    @FXML private Label title_lbl;
    @FXML private Button close_btn;
    @FXML private TableColumn<File, String> trial_id_column;
    @FXML private TableColumn<File, String> trial_purpose_column;
    @FXML private TableColumn<File, LocalDateTime> upload_file_date_column;
    @FXML private TableColumn<File, String> upload_file_column;
    private ObservableList<File> fileList = FXCollections.observableArrayList();
    private ObservableList<FileType> fileTypes = FXCollections.observableArrayList();
    private List<Integer> uploadedFileTypeIds = new ArrayList<>();
    private int trialId;
    private String trialPurpose;
    private String departmentName;
    public static final String mailName = ConfigLoader.getProperty("TRIAL.MAIL");
   private static final String mailPassword = ConfigLoader.getProperty("TRIAL.MAIL.PASSWORD");

   private static final String SERVER_UPLOAD_PATH = "\\\\ETCSVR\\TrialsUpload\\";

    //  private static final String SERVER_UPLOAD_PATH = "\\\\G:\\ETC Projects\\Tooling\\PdsUpload";
    private static String selectedDownloadPath = null;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm a");
    private final DateTimeFormatter dateFormatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
     //   System.out.println("setStage called with stage: " + (stage != null ? stage.toString() : "null"));
    }

    private final UserDao userDao = AppContext.getInstance().getUserDao();
    private final FileDao fileDao = AppContext.getInstance().getFileDao();
    private final FileTypeDao fileTypeDao = AppContext.getInstance().getFileTypeDao();
    private final MaterialDao materialDao = AppContext.getInstance().getMaterialDao();
    private final TrialDao trialDao = AppContext.getInstance().getTrialDao();

    public void closeWindow() {
        if (stage != null) {
            stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      //  System.out.println("initialize called, stage: " + (stage != null ? stage.toString() : "null"));
        Platform.runLater(() -> title_lbl.requestFocus());
        setupTable();
        addFile_btn.setCursor(Cursor.HAND);
        addFile_btn.setVisible(true);
        upload_file_column.setVisible(true);
        download_file_column.setVisible(true);
        delete_file_column.setVisible(true);
        table_view.setEditable(true);
        comment_column.setEditable(true);
        selectedDownloadPath = null;
    }

    public void initData(int trialId, String trialPurpose, String departmentName) {
      //  System.out.println("initData called with trialId: " + trialId + ", trialPurpose: " + trialPurpose + ", departmentName: " + departmentName + ", stage: " + (stage != null ? stage.toString() : "null"));
        this.trialId = trialId;
        this.trialPurpose = trialPurpose;
        this.departmentName = departmentName;
        fileList.clear();
        uploadedFileTypeIds.clear();
        loadFileTypes();
        loadData();

        if (stage != null) {
            stage.setOnCloseRequest(event -> {
                event.consume();
         //       System.out.println("Close request via X ignored. Use Close button instead.");
                WindowUtils.ALERT("Info", "Please use the Red Close button to exit ⬇️.", WindowUtils.ALERT_INFORMATION);
            });
        } else {
            Logging.logException("ERROR", this.getClass().getName(), "initData", new IllegalStateException("Stage is null in initData"));
        }
    }

    private void loadData() {
        fileList.clear();
        if (UserContext.getCurrentUser().getRole() == 4) {
            fileList.addAll(fileDao.getFilesByTrialId(trialId));
        } else {
            int deptId = UserContext.getCurrentUser().getDepartmentId();
            fileList.addAll(fileDao.getFilesByTrialId(trialId).filtered(f -> f.getDepartmentId() == deptId));
        }
        table_view.setItems(fileList);
        table_view.refresh();
     //   System.out.println("Loaded files for trialId: " + trialId + ", count: " + fileList.size());
        for (File file : fileList) {
      //      System.out.println("File: file_id=" + file.getFileId() + ", trial_purpose=" + file.getTrialPurpose() + ", file_type_id=" + file.getFileTypeId());
        }
    }

    private void loadFileTypes() {
        int deptId = UserContext.getCurrentUser().getDepartmentId();
        fileTypes.setAll(fileTypeDao.getAllFileTypes().filtered(ft -> ft.getDepartmentId() == deptId));
     //   System.out.println("Loaded file types: " + fileTypes.size());
        for (FileType ft : fileTypes) {
     //       System.out.println("FileType: id=" + ft.getFileTypeId() + ", name=" + ft.getFileTypeName());
        }
    }

    private void setupTable() {
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
                new SimpleObjectProperty<>(cellData.getValue().getCreationDate()));
        upload_file_date_column.setCellFactory(column -> new TableCell<File, LocalDateTime>() {
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

        upload_file_date_column.setStyle("-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;");

        test_situation_column.setCellFactory(col -> new TableCell<>() {
            private final CheckBox accepted = new CheckBox("Accepted");
            private final CheckBox refused = new CheckBox("Refused");
            private final CheckBox hold = new CheckBox("Hold");
            private final HBox hBox = new HBox(2, accepted, refused, hold);

            {
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

                accepted.getStyleClass().add("accepted-checkbox");
                refused.getStyleClass().add("refused-checkbox");
                hold.getStyleClass().add("hold-checkbox");

                hBox.setStyle("-fx-alignment: CENTER; -fx-padding: 1; -fx-spacing: 3;");

                accepted.setOnAction(e -> handleTestSituation(getIndex(), accepted.isSelected() ? 1 : null));
                refused.setOnAction(e -> handleTestSituation(getIndex(), refused.isSelected() ? 2 : null));
                hold.setOnAction(e -> handleTestSituation(getIndex(), hold.isSelected() ? 3 : null));
            }

            private void handleTestSituation(int index, Integer situation) {
                File file = getTableView().getItems().get(index);
                if (file.getFileId() != 0) {
                    file.setTestSituation(situation);
                    fileDao.updateFile(file);
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

        comment_column.setCellValueFactory(new PropertyValueFactory<>("comment"));
        comment_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comment_column.setOnEditCommit(event -> {
            File file = event.getRowValue();
            if (file.getFileId() != 0) {
                file.setComment(event.getNewValue());
                fileDao.updateFile(file);
                table_view.getItems().set(table_view.getItems().indexOf(file), file);
                table_view.refresh();
            }
        });
        table_view.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && table_view.getSelectionModel().getSelectedItem() != null) {
                File selectedFile = table_view.getSelectionModel().getSelectedItem();
                if (selectedFile.getFileId() != 0) {
                    table_view.edit(table_view.getSelectionModel().getSelectedIndex(), comment_column);
                }
            }
        });

        upload_file_column.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button();
            private final FontIcon uploadIcon = new FontIcon("fas-upload");

            {
                uploadIcon.setIconSize(14);
                uploadIcon.setIconColor(javafx.scene.paint.Color.GREEN);
                btn.setGraphic(uploadIcon);
                btn.setStyle("-fx-background-color: transparent;");
                btn.setCursor(Cursor.HAND);
                btn.setOnAction(event -> uploadFile(getIndex()));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

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

    @FXML
    void addFile(ActionEvent event) {
        if (fileTypes.isEmpty()) {
            WindowUtils.ALERT("Error", "No file types available for this department.", WindowUtils.ALERT_ERROR);
            return;
        }
        for (FileType ft : fileTypes) {
            File newFile = new File();
            newFile.setTrialId(trialId);
            newFile.setTrialPurpose(trialPurpose);
            newFile.setDepartmentId(UserContext.getCurrentUser().getDepartmentId());
            newFile.setDepartmentName(departmentName);
            newFile.setFileTypeId(ft.getFileTypeId());
            newFile.setFileTypeName(ft.getFileTypeName());
            newFile.setUserId(UserContext.getCurrentUser().getUserId());
            fileList.add(newFile);
       //     System.out.println("Added file row: trialId=" + trialId + ", trialPurpose=" + trialPurpose + ", fileTypeId=" + ft.getFileTypeId() + ", fileTypeName=" + ft.getFileTypeName());
        }
        table_view.setItems(fileList);
        table_view.refresh();
    }

    public void uploadFile(int rowIndex) {
        if (stage == null) {
            Logging.logException("ERROR", this.getClass().getName(), "uploadFile", new IllegalStateException("Stage is null in uploadFile"));
       //     System.out.println("Cannot open FileChooser: stage is null");
            WindowUtils.ALERT("Error", "Cannot open file chooser: Stage is not initialized.", WindowUtils.ALERT_ERROR);
            return;
        }

        if (rowIndex < 0 || rowIndex >= fileList.size()) {
            WindowUtils.ALERT("Error", "Invalid row selected.", WindowUtils.ALERT_ERROR);
            return;
        }

        File selectedRow = fileList.get(rowIndex);
        if (selectedRow.getFileTypeId() == null) {
            WindowUtils.ALERT("Error", "No file type assigned to this row.", WindowUtils.ALERT_ERROR);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Upload");
        java.io.File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                File file = new File();
                file.setTrialId(trialId);
                file.setTrialPurpose(trialPurpose);
                file.setDepartmentName(departmentName);
                file.setDepartmentId(UserContext.getCurrentUser().getDepartmentId());
                file.setUserId(UserContext.getCurrentUser().getUserId());
                file.setCreationDate(LocalDateTime.now());
                file.setFileTypeId(selectedRow.getFileTypeId());
                file.setFileTypeName(selectedRow.getFileTypeName());

                String fileName = selectedFile.getName();
                int deptId = UserContext.getCurrentUser().getDepartmentId();
                int userId = UserContext.getCurrentUser().getUserId();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
                String timestamp = LocalDateTime.now().format(formatter);
                String namePart = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
                String extension = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : "";
                String uniqueName = namePart + "_" + deptId + "-" + userId + "_" + timestamp + extension;

                file.setFilePath(uniqueName);
                uploadedFileTypeIds.add(file.getFileTypeId());
          //      System.out.println("FileType set: ID=" + file.getFileTypeId() + ", Name=" + file.getFileTypeName());
                file.setTestSituation(null);
                file.setComment("");

//                System.out.println("File details before insert: trialId=" + file.getTrialId() +
//                        ", trialPurpose=" + file.getTrialPurpose() +
//                        ", filePath=" + file.getFilePath() +
//                        ", fileTypeId=" + file.getFileTypeId() +
//                        ", fileTypeName=" + file.getFileTypeName() +
//                        ", testSituation=" + file.getTestSituation() +
//                        ", comment=" + file.getComment());

                java.io.File dest = new java.io.File(SERVER_UPLOAD_PATH + uniqueName);
                dest.getParentFile().mkdirs();
                Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

                if (dest.exists()) {
                    if (fileDao.insertFile(file)) {
                        File fetchedFile = fileDao.getFileByFilePath(uniqueName);
                        if (fetchedFile != null && fetchedFile.getFileId() != 0) {
                            file.setFileId(fetchedFile.getFileId());
                        }
                        fileList.set(rowIndex, file); // Update the row with the uploaded file
                    //    System.out.println("File uploaded, filePath: " + uniqueName);
                        table_view.refresh();
                        WindowUtils.ALERT("Success", "File uploaded successfully", WindowUtils.ALERT_INFORMATION);
                    } else {
                   //     System.out.println("Failed to insert file into database");
                        WindowUtils.ALERT("Error", "Failed to insert file into database", WindowUtils.ALERT_ERROR);
                    }
                } else {
                //    System.out.println("Failed to copy file to server: " + uniqueName);
                    WindowUtils.ALERT("Error", "Failed to upload file to server", WindowUtils.ALERT_ERROR);
                }
            } catch (IOException e) {
                Logging.logException("ERROR", this.getClass().getName(), "uploadFile", e);
           //     System.out.println("IOException in uploadFile: " + e.getMessage());
                WindowUtils.ALERT("Error", "Failed to upload file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
            }
        }
    }

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
            Logging.logException("ERROR", this.getClass().getName(), "openFile", e);
            WindowUtils.ALERT("Error", "Failed to open file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
        }
    }

    private void downloadFile(File fileRecord) {
        try {
            if (fileRecord != null && fileRecord.getFilePath() != null && !fileRecord.getFilePath().isEmpty()) {
                String fileName = fileRecord.getFilePath();
                String fullPath = SERVER_UPLOAD_PATH + fileName;
                java.io.File networkFile = new java.io.File(fullPath);
                if (networkFile.exists()) {
                    java.io.File saveFile;
                    if (selectedDownloadPath == null) {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setInitialFileName(fileName.replaceFirst("^\\d+_", ""));
                        fileChooser.setTitle("Select Download Location");
                        java.io.File selectedDir = fileChooser.showSaveDialog(stage);
                        if (selectedDir != null) {
                            selectedDownloadPath = selectedDir.getParent();
                            saveFile = selectedDir;
                        } else {
                            WindowUtils.ALERT("Error", "Download cancelled by user", WindowUtils.ALERT_ERROR);
                            return;
                        }
                    } else {
                        saveFile = new java.io.File(selectedDownloadPath + java.io.File.separator + fileName.replaceFirst("^\\d+_", ""));
                    }

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
            Logging.logException("ERROR", this.getClass().getName(), "downloadFile", e);
            WindowUtils.ALERT("Error", "Failed to download file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
        }
    }

    private void deleteFile(File file) {
        if (file.getFileId() != 0) {
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
                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
                        try {
                            boolean deleted = fileDao.deleteFile(file.getFileId());
                            if (deleted) {
                                fileList.remove(file);
                                // in case user remove any file before save
                                uploadedFileTypeIds.remove(Integer.valueOf(file.getFileTypeId()));
                                loadData();
                                WindowUtils.ALERT("Success", "File deleted successfully", WindowUtils.ALERT_INFORMATION);
                            } else {
                                WindowUtils.ALERT("Error", "Failed to delete file", WindowUtils.ALERT_ERROR);
                            }
                        } catch (Exception ex) {
                            Logging.logException("ERROR", this.getClass().getName(), "deleteFile", ex);
                            WindowUtils.ALERT("Error", "Failed to delete file", WindowUtils.ALERT_ERROR);
                        }
                    } else {
                        WindowUtils.ALERT("Error", "Password not correct", WindowUtils.ALERT_WARNING);
                    }
                }
            });
        }
    }


    private void sendFileUploadNotification(int trialId, List<Integer> fileTypeIds) {
        try {
            Trial trial = trialDao.getTrialById(trialId);
            String departmentName = UserContext.getCurrentUser().getDepartmentName();
            String userFullName = UserContext.getCurrentUser().getFullName();
            Material material = materialDao.getMaterialById(trial.getMatrialId());

            // Build file type names
            StringBuilder fileTypeNames = new StringBuilder();
            for (int fileTypeId : fileTypeIds) {
                FileType fileType = fileTypeDao.getFileTypeById(fileTypeId);
                if (fileType != null) {
                    if (fileTypeNames.length() > 0) fileTypeNames.append(", ");
                    fileTypeNames.append(fileType.getFileTypeName());
                }
            }
            String fileTypeNamesStr = fileTypeNames.length() > 0 ? fileTypeNames.toString() : "Unknown File Type";

            // Email settings
            String fromEmail = mailName;
            String password = mailPassword;
            List<String> toEmails = new ArrayList<>();
            List<String> ccEmails = new ArrayList<>();
            Set<String> excludedEmails = new HashSet<>();
            // Test
           //  toEmails.add("moh.gabr@elsewedy.com");
           //   ccEmails.add("h.farid@elsewedy.com");
            //  excludedEmails.add("moh.gabr@elsewedy.com"); // exclude specific emails

               toEmails.addAll(userDao.getActiveUsersEmails(excludedEmails));

            // CC recipients as a list

            Properties properties = new Properties();
            properties.put("mail.transport.protocol", "smtp");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "imail.elsewedy.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
            properties.put("mail.smtp.ssl.trust", "imail.elsewedy.com");
            properties.put("mail.smtp.ssl.ciphersuites", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");


            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            for (String toEmail : toEmails) message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            for (String ccEmail : ccEmails) message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccEmail));

            message.setSubject("Last Situation of Trial (" + trial.getTrialPurpose() + " - " + material.getMaterialName() + ")");

            // Departments
            List<Map<String, Object>> departments = Arrays.asList(
                    Map.of("name", "Technical Office", "id", 1),
                    Map.of("name", "Process", "id", 4),
                    Map.of("name", "Production", "id", 3),
                    Map.of("name", "Quality Control", "id", 6)
            );

            // Status + Files Table (Merged)
            StringBuilder tableHtml = new StringBuilder();
            tableHtml.append("<table border='1' style='border-collapse: collapse; font-family: Montserrat, sans-serif; font-size: 12px; width:100%; table-layout: fixed;'>");
            tableHtml.append("<colgroup>");
            tableHtml.append("<col style='width:5%;'>");   // N   3
            tableHtml.append("<col style='width:25%;'>");  // Department  15
            tableHtml.append("<col style='width:18%;'>");  // Uploaded (Yes/No)  10
            tableHtml.append("<col style='width:52%;'>");  // Files   72
            tableHtml.append("</colgroup>");
            tableHtml.append("<tr style='background-color: #f2f2f2;'>");
            tableHtml.append("<th style='padding:6px 4px;'>N</th>");
            tableHtml.append("<th style='padding:6px 4px;'>Department</th>");
            tableHtml.append("<th style='padding:6px 4px; white-space: nowrap;'>Uploaded</th>");
            tableHtml.append("<th style='padding: 4px;'>Uploaded Files</th>");
            tableHtml.append("</tr>");

            final String SERVER_UPLOAD_PATH = "\\\\ETCSVR\\TrialsUpload\\";
            int index = 1;
            for (Map<String, Object> dept : departments) {
                String deptName = (String) dept.get("name");
                int deptId = (int) dept.get("id");

                boolean hasUploaded = fileDao.hasFilesForTrialAndDepartment(trialId, deptId);
                String status = hasUploaded ? "Yes" : "Not Yet";
                String color = hasUploaded ? "green" : "red";

                List<File> deptFiles = fileDao.getFilesByTrialAndDepartment(trialId, deptId);

                tableHtml.append("<tr>");
                tableHtml.append("<td style='padding: 4px; text-align:center;'>").append(index++).append("</td>");
                tableHtml.append("<td style='padding: 4px;'>").append(deptName).append("</td>");
                tableHtml.append("<td style='padding: 4px; color: ").append(color).append("; font-weight:bold;'>")
                        .append(status).append("</td>");

                tableHtml.append("<td style='padding: 4px;'>");
                if (deptFiles != null && !deptFiles.isEmpty()) {
                    for (int i = 0; i < deptFiles.size(); i++) {
                        File f = deptFiles.get(i);
                        String fileFullName = new java.io.File(f.getFilePath()).getName();
                        String nameWithoutExt = fileFullName.contains(".") ? fileFullName.substring(0, fileFullName.lastIndexOf(".")) : fileFullName;
                        String originalName = nameWithoutExt.contains("_") ? nameWithoutExt.substring(0, nameWithoutExt.indexOf("_")) : nameWithoutExt;
                        String extension = fileFullName.contains(".") ? fileFullName.substring(fileFullName.lastIndexOf(".")) : "";
                        String displayName = originalName + extension;
                        String fileLink = "file:///" + (SERVER_UPLOAD_PATH + fileFullName).replace("\\", "/");

                        tableHtml.append("<a href='").append(fileLink).append("' ")
                                .append("title='Click to open (Available only on ETC network)' ")
                                .append("style='color: #0078D7; text-decoration: underline; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; display:inline-block; max-width:95%;'>")
                                .append(displayName).append("</a>");
                        if (i < deptFiles.size() - 1) tableHtml.append("<br>");
                    }
                } else {
                    tableHtml.append("<span style='color:red; font-weight:bold;'>No Files</span>");
                }
                tableHtml.append("</td>");
                tableHtml.append("</tr>");
            }
            tableHtml.append("</table>");

            // Logo
            InputStream logoStream = this.getClass().getResourceAsStream("/images/etc_logo_mail.png");
            if (logoStream == null) throw new IllegalStateException("Logo file not found at /images/etc_logo_mail.png");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = logoStream.read(buffer)) != -1) baos.write(buffer, 0, bytesRead);
            logoStream.close();
            byte[] logoBytes = baos.toByteArray();

            String creationDateStr = trial.getCreationDate() != null ? trial.getCreationDate().format(dateFormatter2) : "Unknown";

            // Email body
            String emailBody = "<html><body style='font-family: Montserrat, sans-serif; font-size: 12px;'>"
                    + "<p>Dears,</p>"
                    + "<p>Kindly be informed that Eng. <strong>" + userFullName + "</strong>"
                    + " - " + departmentName + " Department uploaded  <strong>" + fileTypeNamesStr + "</strong>"
                    + " document,<br> for trial( " + trial.getTrialPurpose() + " - " + material.getMaterialName() + ") and The last situation of this trial is:</p>"
                    + " Trial Creation Date: <strong>" + creationDateStr + "</strong><br><br>"
                    + tableHtml
                    + "<p>--------------------------------------------------------------</p>"
                    + "<p>Best Regards,</p>"
                    + "<p><strong>Software Development Section</strong></p>"
                    + "<p><strong>Process & Development Department</strong></p>"
                    + "<p><strong>Elsewedy Telecom Cables</strong></p>"
                    + "<p><strong>System by:</strong> Eng. Mohamed Gabr &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                    + "<strong>Supervised by:</strong> Eng. Hassan Farid Ahmed &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                    + "<strong>Operation Director:</strong> Eng. Mohamed Magdy Amer</p>"
                    + "<p><img src='cid:logo' alt='Elsewedy Telecom Cables Logo' style='display: block; margin-top: 10px;'/></p>"
                    + "</body></html>";

            MimeMultipart multipart = new MimeMultipart("related");
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(emailBody, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);
            MimeBodyPart imagePart = new MimeBodyPart();
            DataSource dataSource = new ByteArrayDataSource(logoBytes, "image/png");
            imagePart.setDataHandler(new DataHandler(dataSource));
            imagePart.setHeader("Content-ID", "<logo>");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(imagePart);
            message.setContent(multipart);
            Transport.send(message);

        } catch (Exception e) {
            Logging.logException("ERROR", this.getClass().getName(), "sendFileUploadNotification", e);
            Platform.runLater(() -> WindowUtils.ALERT("Error", "Failed to send file upload notification: " + e.getMessage(), WindowUtils.ALERT_ERROR));
        }
    }


    @FXML
    void closePage(ActionEvent event) {
        boolean hasFiles = fileDao.hasFilesForTrial(trialId);
      //  System.out.println("closePage: hasFilesForTrial=" + hasFiles);
        if (hasFiles && !uploadedFileTypeIds.isEmpty()) {
            sendFileUploadNotification(trialId, uploadedFileTypeIds);
        }
        if (stage != null) {
            stage.close();
        } else {
            Logging.logException("ERROR", this.getClass().getName(), "closePage", new IllegalStateException("Stage is null in closePage"));
        }
    }


    //    private void sendFileUploadNotification(int trialId, List<Integer> fileTypeIds) {
//        try {
//            Trial trial = trialDao.getTrialById(trialId);
//            String departmentName = UserContext.getCurrentUser().getDepartmentName();
//            String fullName = UserContext.getCurrentUser().getFullName();
//            Material matrial = materialDao.getMatrialById(trial.getMatrialId());
//
//            StringBuilder fileTypeNames = new StringBuilder();
//            for (int fileTypeId : fileTypeIds) {
//                FileType fileType = fileTypeDao.getFileTypeById(fileTypeId);
//                if (fileType != null) {
//                    if (fileTypeNames.length() > 0) {
//                        fileTypeNames.append(", ");
//                    }
//                    fileTypeNames.append(fileType.getFileTypeName());
//                }
//            }
//            String fileTypeNamesStr = fileTypeNames.length() > 0 ? fileTypeNames.toString() : "Unknown File Type";
//
//            String fromEmail = mailName;
//            String password = mailPassword;
//            List<String> toEmails = new ArrayList<>();
//            // Get active users' emails, excluding specific ones
//            Set<String> excludedEmails = new HashSet<>();
//            //  excludedEmails.add("moh.gabr@elsewedy.com"); // exclude specific emails
//         //   toEmails.addAll(userDao.getActiveUsersEmails(excludedEmails));
//            // CC recipients as a list
//            List<String> ccEmails = new ArrayList<>();
//          //  ccEmails.add("m.magdy@elsewedy.com");
//            toEmails.add("moh.gabr@elsewedy.com");
//           // ccEmails.add("h.farid@elsewedy.com");
//
//            Properties properties = new Properties();
//            properties.put("mail.transport.protocol", "smtp");
//            properties.put("mail.smtp.auth", "true");
//            properties.put("mail.smtp.starttls.enable", "true");
//            properties.put("mail.smtp.host", "imail.elsewedy.com");
//            properties.put("mail.smtp.port", "587");
//            properties.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
//            properties.put("mail.smtp.ssl.trust", "imail.elsewedy.com");
//            properties.put("mail.smtp.ssl.ciphersuites", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
//
//            Session session = Session.getInstance(properties, new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(fromEmail, password);
//                }
//            });
//
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(fromEmail));
//
//            for (String toEmail : toEmails) {
//                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
//            }
//            for (String ccEmail : ccEmails) {
//                message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccEmail));
//            }
//
//            message.setSubject("Last Situation of Trial (" + trial.getTrialPurpose() + " - " + matrial.getMatrialName() + ")");
//
//            List<Map<String, Object>> departments = Arrays.asList(
//                    new HashMap<String, Object>() {{ put("name", "Technical Office"); put("id", 1); }},
//                    new HashMap<String, Object>() {{ put("name", "Process"); put("id", 4); }},
//                    new HashMap<String, Object>() {{ put("name", "Production"); put("id", 3); }},
//                    new HashMap<String, Object>() {{ put("name", "Quality Control"); put("id", 6); }}
//            );
//
//            StringBuilder statusTableHtml = new StringBuilder();
//            statusTableHtml.append("<table border='1' style='border-collapse: collapse; font-family: Montserrat, sans-serif; font-size: 12px;'>");
//            statusTableHtml.append("<tr style='background-color: #f2f2f2;'>");
//            statusTableHtml.append("<th style='padding: 8px;'>N</th>");
//            statusTableHtml.append("<th style='padding: 8px;'>Department</th>");
//            statusTableHtml.append("<th style='padding: 8px;'>Uploaded</th>");
//            statusTableHtml.append("</tr>");
//
//            int index = 1;
//            for (Map<String, Object> dept : departments) {
//                String deptName = (String) dept.get("name");
//                int deptId = (int) dept.get("id");
//                boolean hasUploaded = fileDao.hasFilesForTrialAndDepartment(trialId, deptId);
//                String status = hasUploaded ? "Yes" : "Not Yet";
//                String color = hasUploaded ? "green" : "red";
//                statusTableHtml.append("<tr>");
//                statusTableHtml.append("<td style='padding: 8px;'>").append(index++).append("</td>");
//                statusTableHtml.append("<td style='padding: 8px;'>").append(deptName).append("</td>");
//                statusTableHtml.append("<td style='padding: 8px; color: ").append(color).append("; font-weight: bold;'>").append(status).append("</td>");
//                statusTableHtml.append("</tr>");
//            }
//            statusTableHtml.append("</table>");
//
//
//            // Second Table (Files Table)
//
//            StringBuilder filesTableHtml = new StringBuilder();
//            filesTableHtml.append("<br><br>");
//            filesTableHtml.append("<table border='1' style='border-collapse: collapse; font-family: Montserrat, sans-serif; font-size: 12px;'>");
//            filesTableHtml.append("<tr style='background-color: #f2f2f2;'>");
//            filesTableHtml.append("<th style='padding: 8px;'>N</th>");
//            filesTableHtml.append("<th style='padding: 8px;'>Department</th>");
//            filesTableHtml.append("<th style='padding: 8px;'>File Name</th>");
//            filesTableHtml.append("</tr>");
//
//            int fileIndex = 1;
//            for (Map<String, Object> dept : departments) {
//                String deptName = (String) dept.get("name");
//                int deptId = (int) dept.get("id");
//
//                // Get all files for this department from DB
//                List<File> deptFiles = fileDao.getFilesByTrialAndDepartment(trialId, deptId);
//
//                filesTableHtml.append("<tr>");
//                filesTableHtml.append("<td style='padding: 8px;'>").append(fileIndex++).append("</td>");
//                filesTableHtml.append("<td style='padding: 8px;'>").append(deptName).append("</td>");
//                filesTableHtml.append("<td style='padding: 8px;'>");
//
//                if (deptFiles != null && !deptFiles.isEmpty()) {
//                    for (int i = 0; i < deptFiles.size(); i++) {
//                        File f = deptFiles.get(i);
//                        fullName = new java.io.File(f.getFilePath()).getName();
//                        String nameWithoutExtension = fullName.contains(".") ?
//                                fullName.substring(0, fullName.lastIndexOf(".")) : fullName;
//
//                        String originalName = nameWithoutExtension.contains("_") ?
//                                nameWithoutExtension.substring(0, nameWithoutExtension.indexOf("_")) : nameWithoutExtension;
//
//                        String extension = fullName.contains(".") ?
//                                fullName.substring(fullName.lastIndexOf(".")) : "";
//
//                        String displayName = originalName + extension;
//
//                        filesTableHtml.append(displayName);
//                        if (i < deptFiles.size() - 1) {
//                            filesTableHtml.append("<br>"); // separate multiple files in same cell
//                        }
//                    }
//                } else {
//                    filesTableHtml.append("<span style='color:red; font-weight:bold;'>No Files</span>");
//                }
//
//                filesTableHtml.append("</td>");
//                filesTableHtml.append("</tr>");
//            }
//            filesTableHtml.append("</table>");
//
//// Add note for users outside the network
//            filesTableHtml.append("<p style='color:red; font-size:11px; margin-top:5px;'>");
//            filesTableHtml.append("⚠ Note: Files are only accessible from inside the company network (ETC LAN). ");
//            filesTableHtml.append("If you are outside, please connect via VPN.");
//            filesTableHtml.append("</p>");
//
//
//            InputStream logoStream = this.getClass().getResourceAsStream("/images/etc_logo.png");
//            if (logoStream == null) {
//                throw new IllegalStateException("Logo file not found at /images/etc_logo.png");
//            }
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = logoStream.read(buffer)) != -1) {
//                baos.write(buffer, 0, bytesRead);
//            }
//            logoStream.close();
//            byte[] logoBytes = baos.toByteArray();
//
//            String creationDateStr = trial.getCreationDate() != null
//                    ? trial.getCreationDate().format(dateFormatter2)
//                    : "Unknown";
//
//            String emailBody = "<html>" +
//                    "<body style='font-family: Montserrat, sans-serif; font-size: 12px;'>" +
//                    "<p>Dears,</p>" +
//                    "<p>Kindly be informed that Eng: " + fullName +
//                    " - " + departmentName + " Department uploaded  " + fileTypeNamesStr +
//                    " document," +
//                    "<br></br> " +
//                    " for trial( " + trial.getTrialPurpose() + " - " + trial.getMatrialName() +
//                    ") and The last situation of this trial is:</p>" +
//                    " Trial Creation Date: " + creationDateStr +
//                    "<br></br> "+
//                    statusTableHtml +
//                    "<br></br> "+
//                    "<p><strong>Uploaded Files:</strong></p>" +
//                    filesTableHtml +
//                    "<p>--------------------------------------------------------------</p>" +
//                    "<br><br>" +
//                    "<p>Best Regards,</p>" +
//                    "<p><strong>Software Development Section</strong></p>" +
//                    "<p><strong>Process & Development Department</strong></p>" +
//                    "<p><strong>Elsewedy Telecom Cables</strong></p>" +
//                    "<p><strong>System by:</strong> Eng. Mohamed Gabr &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " +
//                    "<strong>Supervised by:</strong> Eng. Hassan Farid Ahmed &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
//                    "<strong>Operation Director:</strong> Eng. Mohamed Magdy Amer</p>" +
//                    "<p><img src='cid:logo' alt='Elsewedy Telecom Cables Logo' style='display: block; margin-top: 10px;'/></p>" +
//                    "</body></html>";
//
//            MimeMultipart multipart = new MimeMultipart("related");
//            MimeBodyPart htmlPart = new MimeBodyPart();
//            htmlPart.setContent(emailBody, "text/html; charset=utf-8");
//            multipart.addBodyPart(htmlPart);
//            MimeBodyPart imagePart = new MimeBodyPart();
//            DataSource dataSource = new ByteArrayDataSource(logoBytes, "image/png");
//            imagePart.setDataHandler(new DataHandler(dataSource));
//            imagePart.setHeader("Content-ID", "<logo>");
//            imagePart.setDisposition(MimeBodyPart.INLINE);
//            multipart.addBodyPart(imagePart);
//            message.setContent(multipart);
//            Transport.send(message);
//         //   System.out.println("Email sent successfully for trialId: " + trialId);
//        } catch (Exception e) {
//            Logging.logException("ERROR", this.getClass().getName(), "sendFileUploadNotification", e);
//       //    System.out.println("Failed to send email: " + e.getMessage());
//            Platform.runLater(() -> WindowUtils.ALERT("Error", "Failed to send file upload notification: " + e.getMessage(), WindowUtils.ALERT_ERROR));
//        }
//    }





}