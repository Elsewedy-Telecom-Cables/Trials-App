//
//package com.elsewedyt.trialsapp.controllers;
//
//import com.elsewedyt.trialsapp.dao.*;
//import com.elsewedyt.trialsapp.db.DEF;
//import com.elsewedyt.trialsapp.logging.Logging;
//import com.elsewedyt.trialsapp.models.*;
//import com.elsewedyt.trialsapp.services.ShiftManager;
//import com.elsewedyt.trialsapp.services.WindowUtils;
//import javafx.application.Platform;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.geometry.NodeOrientation;
//import javafx.scene.Cursor;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Text;
//import javafx.scene.text.TextAlignment;
//import javafx.scene.text.TextFlow;
//import javafx.stage.FileChooser;
//import org.kordamp.ikonli.javafx.FontIcon;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//import java.nio.file.Files;
//import java.nio.file.StandardCopyOption;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ResourceBundle;
//
//public class DashboardController implements Initializable {
//    @FXML private Button clearSearch_btn;
//    @FXML private TableColumn<TrialsView, String> comment_column;
//    @FXML private TextField cu_trials_count_textF;
//    @FXML private Label date_lbl;
//    @FXML private ComboBox<Department> department_Comb;
//    @FXML private TableColumn<TrialsView, String> department_name_column;
//    @FXML private VBox department_lbl;
//    @FXML private Label dept_name_lbl;
//    @FXML private TableColumn<TrialsView, String> download_file_column;
//    @FXML private TableColumn<TrialsView, String> file_creation_date_column;
//    @FXML private TableColumn<TrialsView, String> file_type_column;
//    @FXML private TextField filter_trial_purpose_textF;
//    @FXML private TextField filter_trial_id_textF;
//   // @FXML private TextField filter_file_comment_textF;
//   //  @FXML private TextField filter_trial_notes_textF;
//   // @FXML private DatePicker from_file_creation_date_DP;
//   // @FXML private DatePicker to_file_creation_date_DP;
//    @FXML private DatePicker from_trial_creation_date_DP;
//
//    @FXML private DatePicker to_trial_creation_date_DP;
//    @FXML private TextField fo_trials_count_textF;
//    @FXML private TableColumn<TrialsView, String> trial_id_column;
//    @FXML private ImageView logo_ImageView;
//    @FXML private ComboBox<Matrial> matrial_Comb;
//    @FXML private TableColumn<TrialsView, String> matrial_name_column;
//    @FXML private TableColumn<TrialsView, String> no_column;
//    @FXML private TableColumn<TrialsView, String> trial_notes_column;
//    @FXML private TableColumn<TrialsView, String> open_file_column;
//    @FXML private Button searchWithFilter_btn;
//    @FXML private Label search_lable;
//    @FXML private ComboBox<Section> section_Comb;
//    @FXML private TableColumn<TrialsView, String> section_column;
//    @FXML private Label shift_label;
//    @FXML private ComboBox<Supplier> supplier_Comb;
//    @FXML private TableColumn<TrialsView, String> supplier_name_column;
//    @FXML private ComboBox<SupplierCountry> supplier_country_Comb;
//    @FXML private TableColumn<TrialsView, String> supplier_country_name_column;
//    @FXML private TableView<TrialsView> table_view;
//    @FXML private TableColumn<TrialsView, String> test_situation_column;
//    @FXML private TableColumn<TrialsView, String> trial_creation_date_column;
//    @FXML private TableColumn<TrialsView, String> trial_purpose_column;
//    @FXML private TextField trials_count_textF;
//    @FXML private Button update_btn;
//    @FXML private TableColumn<TrialsView, String> user_fullname_column;
//    @FXML private ComboBox<User> user_fullname_Comb;
//    @FXML private Label welcome_lbl;
//
//    private ObservableList<TrialsView> trialsViewList;
//    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//    private static String downloadPath = null;
//    private static final String SERVER_UPLOAD_PATH = "\\\\ETCSVR\\TrialsUpload\\";
//    private static final String LOCAL_UPLOAD_PATH = System.getProperty("user.home") + "\\TrialsUpload\\";
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Platform.runLater(() -> welcome_lbl.requestFocus());
//        ShiftManager.setSHIFT(LocalDateTime.now());
//        shift_label.setText("Shift: " + ShiftManager.SHIFT_NAME);
//        date_lbl.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(new java.util.Date()));
//        welcome_lbl.setText("Welcome: " + UserContext.getCurrentUser().getFullName());
//        dept_name_lbl.setText(UserContext.getCurrentUser().getDepartmentName() + " Department");
//        logo_ImageView.setImage(new Image(DashboardController.class.getResourceAsStream("/images/company_logo.png")));
//        clearSearch_btn.setCursor(Cursor.HAND);
//        searchWithFilter_btn.setCursor(Cursor.HAND);
//        update_btn.setCursor(Cursor.HAND);
//        initializeComboBoxes();
//        addFilterListeners();
//        loadData();
//        updateTrialsCount();
//        table_view.getStylesheets().add(getClass().getResource("/screens/style.css").toExternalForm());
//
//        supplier_country_Comb.setItems(FXCollections.observableArrayList());
//
//        // Add listener to supplier_combo to update supplier_country_combo when a supplier is selected
//        supplier_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldSupplier, newSupplier) -> {
//            updateSupplierCountries(newSupplier);
//        });
//
//    }
//    // Update supplier_country_combo with countries associated with the selected supplier
//    private void updateSupplierCountries(Supplier supplier) {
//        ObservableList<SupplierCountry> filteredCountries = FXCollections.observableArrayList();
//        if (supplier != null) {
//            // Get all countries associated with the selected supplier
//            filteredCountries = SupplierCountryDAO.getSupplierCountriesBySupplierId(supplier.getSupplierId());
//        }
//        supplier_country_Comb.setItems(filteredCountries);
//        supplier_country_Comb.getSelectionModel().clearSelection();
//
//    }
//
//    private void initializeComboBoxes() {
//        department_Comb.setItems(DepartmentDAO.getAllDepartments());
//        department_Comb.setPromptText("Select Department");
//        supplier_Comb.setItems(SupplierDAO.getAllSuppliers());
//        supplier_Comb.setPromptText("Select Supplier");
//        matrial_Comb.setItems(MatrialDAO.getAllMatrials());
//        matrial_Comb.setPromptText("Select Material");
//        section_Comb.setItems(SectionDAO.getAllSections());
//        section_Comb.setPromptText("Select Section");
//        supplier_country_Comb.setItems(SupplierCountryDAO.getAllSupplierCountries());
//        supplier_country_Comb.setPromptText("Select Supplier Country");
//        user_fullname_Comb.setItems(UserDAO.getUsersByDepartmentId(null));
//        user_fullname_Comb.setPromptText("Select User");
//
//        department_Comb.setCellFactory(param -> new ListCell<Department>() {
//            @Override
//            protected void updateItem(Department item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : item.getDepartmentName());
//            }
//        });
//        department_Comb.setButtonCell(new ListCell<Department>() {
//            @Override
//            protected void updateItem(Department item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : item.getDepartmentName());
//            }
//        });
//
//        supplier_Comb.setCellFactory(param -> new ListCell<Supplier>() {
//            @Override
//            protected void updateItem(Supplier item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : item.getSupplierName());
//            }
//        });
//        supplier_Comb.setButtonCell(new ListCell<Supplier>() {
//            @Override
//            protected void updateItem(Supplier item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : item.getSupplierName());
//            }
//        });
//
//        matrial_Comb.setCellFactory(param -> new ListCell<Matrial>() {
//            @Override
//            protected void updateItem(Matrial item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : item.getMatrialName());
//            }
//        });
//        matrial_Comb.setButtonCell(new ListCell<Matrial>() {
//            @Override
//            protected void updateItem(Matrial item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : item.getMatrialName());
//            }
//        });
//
//        section_Comb.setCellFactory(param -> new ListCell<Section>() {
//            @Override
//            protected void updateItem(Section item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : item.getSectionName());
//            }
//        });
//        section_Comb.setButtonCell(new ListCell<Section>() {
//            @Override
//            protected void updateItem(Section item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : item.getSectionName());
//            }
//        });
//
//        supplier_country_Comb.setCellFactory(param -> new ListCell<SupplierCountry>() {
//            @Override
//            protected void updateItem(SupplierCountry item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : item.getCountryName());
//            }
//        });
//        supplier_country_Comb.setButtonCell(new ListCell<SupplierCountry>() {
//            @Override
//            protected void updateItem(SupplierCountry item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : item.getCountryName());
//            }
//        });
//
//        user_fullname_Comb.setCellFactory(param -> new ListCell<User>() {
//            @Override
//            protected void updateItem(User item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : item.getFullName());
//            }
//        });
//        user_fullname_Comb.setButtonCell(new ListCell<User>() {
//            @Override
//            protected void updateItem(User item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : item.getFullName());
//            }
//        });
//
//        department_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
//            user_fullname_Comb.getSelectionModel().clearSelection();
//            user_fullname_Comb.setItems(UserDAO.getUsersByDepartmentId(newValue != null ? newValue.getDepartmentId() : null));
//            filterTrials();
//        });
//    }
//
//    private void loadData() {
//        trialsViewList = TrialsViewDAO.getAllTrialsView();
//        table_view.setItems(trialsViewList);
//
//        no_column.setCellFactory(column -> new TableCell<TrialsView, String>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || getTableRow() == null || getTableRow().getItem() == null ? null : String.valueOf(getIndex() + 1));
//                setStyle("-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;");
//            }
//        });
//
//        trial_id_column.setCellValueFactory(new PropertyValueFactory<>("trialId"));
//        trial_purpose_column.setCellValueFactory(new PropertyValueFactory<>("trialPurpose"));
//        trial_creation_date_column.setCellValueFactory(cellData ->
//                new SimpleStringProperty(cellData.getValue().getTrialCreationDate() != null ?
//                        cellData.getValue().getTrialCreationDate().format(dateFormatter) : ""));
//        trial_notes_column.setCellValueFactory(new PropertyValueFactory<>("trialNotes"));
//        section_column.setCellValueFactory(new PropertyValueFactory<>("sectionName"));
//        matrial_name_column.setCellValueFactory(new PropertyValueFactory<>("materialName"));
//        supplier_name_column.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
//        supplier_country_name_column.setCellValueFactory(new PropertyValueFactory<>("supplierCountryName"));
//        department_name_column.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
//        user_fullname_column.setCellValueFactory(new PropertyValueFactory<>("userFullName"));
//        file_type_column.setCellValueFactory(new PropertyValueFactory<>("fileTypeName"));
//        comment_column.setCellValueFactory(new PropertyValueFactory<>("comment"));
//        file_creation_date_column.setCellValueFactory(cellData ->
//                new SimpleStringProperty(cellData.getValue().getFileCreationDate() != null ?
//                        cellData.getValue().getFileCreationDate().format(dateFormatter) : ""));
//
//      //  test_situation_column.setCellValueFactory(new PropertyValueFactory<>("testSituation"));
//
//        test_situation_column.setCellFactory(column -> new TableCell<>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
//                    setText(null);
//                    setStyle("");
//                } else {
//                    TrialsView tr = getTableRow().getItem();
//                    String testSit = tr.getTestSituation() ;
//                    String color = "";
//
//                    if (testSit.equals(DEF.TEST_SITUATION_ACCEPTED)) {
//                        color = "#6ae26a"; // Green
//                        testSit = DEF.TEST_SITUATION_ACCEPTED ;
//
//                    } else if (testSit.equals(DEF.TEST_SITUATION_REFUSED)) {
//                        color = "#ff6b6b"; // Red
//                        testSit = DEF.TEST_SITUATION_REFUSED ;
//                    } else if(testSit.equals(DEF.TEST_SITUATION_HOLD))  {
//                        color = "#ffe97d"; // Yellow
//                        testSit = DEF.TEST_SITUATION_HOLD ;
//                    }
//                    setText(testSit);
//                    // Apply style only if color is not empty, otherwise use alignment and font-weight only
//                    if (!color.isEmpty()) {
//                        setStyle(String.format("-fx-background-color: %s; -fx-alignment: center; -fx-font-weight: bold;", color));
//                    } else {
//                        setStyle("-fx-alignment: center; -fx-font-weight: bold;");
//                    }
//                   // setStyle(String.format("-fx-background-color: %s; -fx-alignment: center; -fx-font-weight: bold;", color));
//                }
//            }
//        });
//
//
//        open_file_column.setCellFactory(col -> new TableCell<TrialsView, String>() {
//            private final Button btn = new Button();
//            private final FontIcon openIcon = new FontIcon("fas-folder-open");
//
//            {
//                openIcon.setIconSize(16);
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
//                    String fullFileName = new File(getTableView().getItems().get(getIndex()).getFilePath()).getName();
//                    String fileName = fullFileName.replaceFirst("^\\d+_", "");
//                    btn.setText(fileName);
//                    btn.setGraphic(openIcon);
//                    setGraphic(btn);
//                }
//            }
//        });
//
//        download_file_column.setCellFactory(col -> new TableCell<TrialsView, String>() {
//            private final Button btn = new Button();
//            private final FontIcon downloadIcon = new FontIcon("fas-download");
//
//            {
//                downloadIcon.setIconSize(16);
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
//        String columnStyle = "-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;";
//
//        trial_id_column.setStyle(columnStyle);
//        trial_purpose_column.setStyle(columnStyle);
//        trial_creation_date_column.setStyle(columnStyle);
//        trial_notes_column.setStyle(columnStyle);
//        section_column.setStyle(columnStyle);
//        matrial_name_column.setStyle(columnStyle);
//        supplier_name_column.setStyle(columnStyle);
//        supplier_country_name_column.setStyle(columnStyle);
//        department_name_column.setStyle(columnStyle);
//        user_fullname_column.setStyle(columnStyle);
//        file_type_column.setStyle(columnStyle);
//        test_situation_column.setStyle(columnStyle);
//        comment_column.setStyle(columnStyle);
//        file_creation_date_column.setStyle(columnStyle);
//        open_file_column.setStyle(columnStyle);
//        download_file_column.setStyle(columnStyle);
//
//        table_view.setFixedCellSize(32);
//        table_view.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//        table_view.setEditable(false);
//        table_view.refresh();
//        // Add double-click listener to TableView to show row details in an Alert
//        table_view.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 2 && table_view.getSelectionModel().getSelectedItem() != null) {
//                TrialsView selectedRow = table_view.getSelectionModel().getSelectedItem();
//                StringBuilder details = new StringBuilder();
//                details.append("Trial ID : ").append(selectedRow.getTrialId()).append("\n");
//                details.append("Trial Purpose : ").append(selectedRow.getTrialPurpose() != null ? selectedRow.getTrialPurpose() : "-").append("\n");
//                details.append("Trial Creation Date : ").append(selectedRow.getTrialCreationDate() != null ?
//                        selectedRow.getTrialCreationDate().format(dateFormatter) : "-").append("\n");
//                details.append("Trial Notes : ").append(selectedRow.getTrialNotes() != null ? selectedRow.getTrialNotes() : "-").append("\n");
//                details.append("Section : ").append(selectedRow.getSectionName() != null ? selectedRow.getSectionName() : "-").append("\n");
//                details.append("Material : ").append(selectedRow.getMaterialName() != null ? selectedRow.getMaterialName() : "-").append("\n");
//                details.append("Supplier : ").append(selectedRow.getSupplierName() != null ? selectedRow.getSupplierName() : "-").append("\n");
//                details.append("Supplier Country : ").append(selectedRow.getSupplierCountryName() != null ?
//                        selectedRow.getSupplierCountryName() : "-").append("\n");
//                details.append("Department : ").append(selectedRow.getDepartmentName() != null ? selectedRow.getDepartmentName() : "-").append("\n");
//                details.append("User : ").append(selectedRow.getUserFullName() != null ? selectedRow.getUserFullName() : "-").append("\n");
//                details.append("File Type : ").append(selectedRow.getFileTypeName() != null ? selectedRow.getFileTypeName() : "-").append("\n");
//                details.append("Test Situation : ").append(selectedRow.getTestSituation() != null ? selectedRow.getTestSituation() : "-").append("\n");
//                details.append("Comment : ").append(selectedRow.getComment() != null ? selectedRow.getComment() : "-").append("\n");
//                details.append("File Creation Date : ").append(selectedRow.getFileCreationDate() != null ?
//                        selectedRow.getFileCreationDate().format(dateFormatter) : "-").append("\n");
//                details.append("File Path : ").append(selectedRow.getFilePath() != null ? selectedRow.getFilePath() : "-");
//
//                showInfo(details.toString());
//
//            }
//        });
//    }
//
//
//    private void showInfo(String message) {
//        Platform.runLater(() -> {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Trial Details");
//            alert.setHeaderText("Trial Details");
//
//            TextFlow textFlow = new TextFlow();
//            String[] lines = message.split("\n");
//            for (int i = 0; i < lines.length; i++) {
//                String line = lines[i];
//                String[] parts = line.split(": ", 2); // Split on ": " to separate column name and value
//                if (parts.length == 2) {
//                    Text columnName = new Text(parts[0] + ": ");
//                    columnName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #00008B;"); // Dark blue, size 13, bold
//                    Text value = new Text(parts[1] + "\n");
//                    value.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #000000;"); // Black, size 12, bold
//                    textFlow.getChildren().addAll(columnName, value);
//                } else {
//                    Text text = new Text(line + "\n");
//                    text.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #000000;"); // Black, size 13, bold
//                    textFlow.getChildren().add(text);
//                }
//                // Add separator except after the last line
//                if (i < lines.length - 1) {
//                    Text separator = new Text("-------------------\n");
//                    separator.setStyle("-fx-font-size: 10px; -fx-font-weight: normal; -fx-fill: #000000;"); // Black, size 13, normal
//                    textFlow.getChildren().add(separator);
//                }
//            }
//
//            textFlow.setTextAlignment(TextAlignment.LEFT);
//            textFlow.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
//
//            ScrollPane scrollPane = new ScrollPane(textFlow);
//            scrollPane.setFitToWidth(true);
//            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
//            alert.getDialogPane().setContent(scrollPane);
//           // alert.getDialogPane().setMinWidth(600); // Adjust width for better readability
//            alert.showAndWait();
//        });
//    }
//
//    private void updateTrialsCount() {
//        int totalTrials = TrialDAO.getTrialsCount();
//        int cuTrials = TrialDAO.getTrialsCountBySection(1);
//        int foTrials = TrialDAO.getTrialsCountBySection(2);
//        trials_count_textF.setText(String.valueOf(totalTrials));
//        cu_trials_count_textF.setText(String.valueOf(cuTrials));
//        fo_trials_count_textF.setText(String.valueOf(foTrials));
//    }
//
//    private void addFilterListeners() {
//        filter_trial_purpose_textF.textProperty().addListener((obs, oldValue, newValue) -> filterTrials());
//        filter_trial_id_textF.textProperty().addListener((obs, oldValue, newValue) -> filterTrials());
//      //  filter_trial_notes_textF.textProperty().addListener((obs, oldValue, newValue) -> filterTrials());
//     //   filter_file_comment_textF.textProperty().addListener((obs, oldValue, newValue) -> filterTrials());
//        from_trial_creation_date_DP.valueProperty().addListener((obs, oldValue, newValue) -> filterTrials());
//        to_trial_creation_date_DP.valueProperty().addListener((obs, oldValue, newValue) -> filterTrials());
//       // from_file_creation_date_DP.valueProperty().addListener((obs, oldValue, newValue) -> filterTrials());
//   //     to_file_creation_date_DP.valueProperty().addListener((obs, oldValue, newValue) -> filterTrials());
//        section_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
//        matrial_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
//        supplier_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
//        supplier_country_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
//        department_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
//        user_fullname_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
//    }
//
//    private void filterTrials() {
//        String trialPurpose = filter_trial_purpose_textF.getText();
//        Integer trialId = null;
//        try {
//            String trialIdText = filter_trial_id_textF.getText();
//            if (trialIdText != null && !trialIdText.trim().isEmpty()) {
//                trialId = Integer.parseInt(trialIdText.trim());
//            }
//        } catch (NumberFormatException e) {
//            WindowUtils.ALERT("Error", "Please enter a valid Trial ID (numeric value)", WindowUtils.ALERT_ERROR);
//            filter_trial_id_textF.clear();
//            return;
//        }
//       // String trialNotes = filter_trial_notes_textF.getText();
//      //  String comment = filter_file_comment_textF.getText();
//        LocalDate fromTrialCreationDate = from_trial_creation_date_DP.getValue();
//        LocalDate toTrialCreationDate = to_trial_creation_date_DP.getValue();
//      //  LocalDate fromFileCreationDate = from_file_creation_date_DP.getValue();
//      //  LocalDate toFileCreationDate = to_file_creation_date_DP.getValue();
//
//        if (fromTrialCreationDate != null && toTrialCreationDate != null && fromTrialCreationDate.isAfter(toTrialCreationDate)) {
//            WindowUtils.ALERT("Error", "Trial creation 'From' date cannot be after 'To' date", WindowUtils.ALERT_ERROR);
//            from_trial_creation_date_DP.setValue(null);
//            return;
//        }
////        if (fromFileCreationDate != null && toFileCreationDate != null && fromFileCreationDate.isAfter(toFileCreationDate)) {
////            WindowUtils.ALERT("Error", "File creation 'From' date cannot be after 'To' date", WindowUtils.ALERT_ERROR);
////            from_file_creation_date_DP.setValue(null);
////            return;
////        }
//
//        Integer sectionId = section_Comb.getSelectionModel().getSelectedItem() != null ?
//                section_Comb.getSelectionModel().getSelectedItem().getSectionId() : null;
//        Integer materialId = matrial_Comb.getSelectionModel().getSelectedItem() != null ?
//                matrial_Comb.getSelectionModel().getSelectedItem().getMatrialId() : null;
//        Integer supplierId = supplier_Comb.getSelectionModel().getSelectedItem() != null ?
//                supplier_Comb.getSelectionModel().getSelectedItem().getSupplierId() : null;
//        String supplierCountry = supplier_country_Comb.getSelectionModel().getSelectedItem() != null ?
//                supplier_country_Comb.getSelectionModel().getSelectedItem().getCountryName() : null;
//        Integer departmentId = department_Comb.getSelectionModel().getSelectedItem() != null ?
//                department_Comb.getSelectionModel().getSelectedItem().getDepartmentId() : null;
//        Integer userId = user_fullname_Comb.getSelectionModel().getSelectedItem() != null ?
//                user_fullname_Comb.getSelectionModel().getSelectedItem().getUserId() : null;
//
//        trialsViewList = TrialsViewDAO.searchTrialsView(trialPurpose, trialId, sectionId, materialId, supplierId, supplierCountry,
//                fromTrialCreationDate, toTrialCreationDate, departmentId,userId);
//        table_view.setItems(trialsViewList);
//        updateTrialsCount();
//    }
//
//    private void openFile(String fileName) {
//        try {
//            if (fileName != null && !fileName.isEmpty()) {
//                String basePath = isLocalTesting() ? LOCAL_UPLOAD_PATH : SERVER_UPLOAD_PATH;
//                String fullPath = basePath + fileName;
//                File networkFile = new File(fullPath);
//                if (networkFile.exists()) {
//                    String tempDir = System.getProperty("java.io.tmpdir");
//                    File tempFile = new File(tempDir + fileName);
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
//    private void downloadFile(TrialsView trialsView) {
//        try {
//            if (trialsView != null && trialsView.getFilePath() != null && !trialsView.getFilePath().isEmpty()) {
//                String fileName = trialsView.getFilePath();
//                String basePath = isLocalTesting() ? LOCAL_UPLOAD_PATH : SERVER_UPLOAD_PATH;
//                String fullPath = basePath + fileName;
//                File networkFile = new File(fullPath);
//                if (networkFile.exists()) {
//                    if (downloadPath == null) {
//                        FileChooser fileChooser = new FileChooser();
//                        fileChooser.setInitialFileName(fileName.replaceFirst("^\\d+_", ""));
//                        fileChooser.setTitle("Save File");
//                        File saveFile = fileChooser.showSaveDialog(null);
//                        if (saveFile != null) {
//                            downloadPath = saveFile.getParent();
//                            Files.copy(networkFile.toPath(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                            WindowUtils.ALERT("Success", "File downloaded to: " + saveFile.getAbsolutePath(), WindowUtils.ALERT_INFORMATION);
//                        } else {
//                            WindowUtils.ALERT("Error", "Download cancelled", WindowUtils.ALERT_ERROR);
//                            return;
//                        }
//                    } else {
//                        File saveFile = new File(downloadPath + File.separator + fileName.replaceFirst("^\\d+_", ""));
//                        Files.copy(networkFile.toPath(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                        WindowUtils.ALERT("Success", "File downloaded to: " + saveFile.getAbsolutePath(), WindowUtils.ALERT_INFORMATION);
//                    }
//                } else {
//                    WindowUtils.ALERT("Error", "Source file doesn't exist at: " + fullPath, WindowUtils.ALERT_ERROR);
//                }
//            } else {
//                WindowUtils.ALERT("Error", "File name is empty", WindowUtils.ALERT_ERROR);
//            }
//        } catch (IOException e) {
//            Logging.logExpWithMessage("ERROR", getClass().getName(), "downloadFile", e, "file", trialsView != null ? trialsView.getFilePath() : "null");
//            WindowUtils.ALERT("Error", "Failed to download file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
//        }
//    }
//
//    private boolean isLocalTesting() {
//        return !new File(SERVER_UPLOAD_PATH).exists();
//    }
//
//    @FXML
//    void clearSearch(ActionEvent event) {
//        filter_trial_purpose_textF.clear();
//        filter_trial_id_textF.clear();
//        //filter_trial_notes_textF.clear();
//       // filter_file_comment_textF.clear();
//        from_trial_creation_date_DP.setValue(null);
//        to_trial_creation_date_DP.setValue(null);
//        //from_file_creation_date_DP.setValue(null);
//       // to_file_creation_date_DP.setValue(null);
//        section_Comb.getSelectionModel().clearSelection();
//        matrial_Comb.getSelectionModel().clearSelection();
//        supplier_Comb.getSelectionModel().clearSelection();
//        supplier_country_Comb.getSelectionModel().clearSelection();
//        department_Comb.getSelectionModel().clearSelection();
//        user_fullname_Comb.getSelectionModel().clearSelection();
//        loadData();
//    }
//
//
//    @FXML
//    void filterTrialPurpose(KeyEvent event) {
//        filterTrials();
//    }
//
//    @FXML
//    void searchWithFilter(ActionEvent event) {
//        filterTrials();
//    }
//
//    @FXML
//    void update(ActionEvent event) {
//        loadData();
//        updateTrialsCount();
//        table_view.refresh();
//    }
//}
//

package com.elsewedyt.trialsapp.controllers;

import com.elsewedyt.trialsapp.dao.*;
import com.elsewedyt.trialsapp.db.DEF;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.models.*;
import com.elsewedyt.trialsapp.services.ShiftManager;
import com.elsewedyt.trialsapp.services.WindowUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML private Button clearSearch_btn;
    @FXML private TableColumn<TrialsView, String> comment_column;
    @FXML private TextField cu_trials_count_textF;
    @FXML private Label date_lbl;
    @FXML private ComboBox<Department> department_Comb;
    @FXML private TableColumn<TrialsView, String> department_name_column;
    @FXML private VBox department_lbl;
    @FXML private Label dept_name_lbl;
    @FXML private TableColumn<TrialsView, String> download_file_column;
    @FXML private TableColumn<TrialsView, String> file_creation_date_column;
    @FXML private TableColumn<TrialsView, String> file_type_column;
    @FXML private TextField filter_trial_purpose_textF;
    @FXML private TextField filter_trial_id_textF;
    // @FXML private TextField filter_file_comment_textF;
    // @FXML private TextField filter_trial_notes_textF;
    // @FXML private DatePicker from_file_creation_date_DP;
    // @FXML private DatePicker to_file_creation_date_DP;
    @FXML private DatePicker from_trial_creation_date_DP;
    @FXML private DatePicker to_trial_creation_date_DP;
    @FXML private TextField fo_trials_count_textF;
    @FXML private TableColumn<TrialsView, String> trial_id_column;
    @FXML private ImageView logo_ImageView;
    @FXML private ComboBox<Matrial> matrial_Comb;
    @FXML private TableColumn<TrialsView, String> matrial_name_column;
    @FXML private TableColumn<TrialsView, String> no_column;
    @FXML private TableColumn<TrialsView, String> trial_notes_column;
    @FXML private TableColumn<TrialsView, String> open_file_column;
    @FXML private Button searchWithFilter_btn;
    @FXML private Label search_lable;
    @FXML private ComboBox<Section> section_Comb;
    @FXML private TableColumn<TrialsView, String> section_column;
    @FXML private Label shift_label;
    @FXML private ComboBox<Supplier> supplier_Comb;
    @FXML private TableColumn<TrialsView, String> supplier_name_column;
    @FXML private ComboBox<SupplierCountry> supplier_country_Comb;
    @FXML private TableColumn<TrialsView, String> supplier_country_name_column;
    @FXML private TableView<TrialsView> table_view;
    @FXML private TableColumn<TrialsView, String> test_situation_column;
    @FXML private TableColumn<TrialsView, String> trial_creation_date_column;
    @FXML private TableColumn<TrialsView, String> trial_purpose_column;
    @FXML private TextField trials_count_textF;
    @FXML private Button update_btn;
    @FXML private TableColumn<TrialsView, String> user_fullname_column;
    @FXML private ComboBox<User> user_fullname_Comb;
    @FXML private Label welcome_lbl;

    private ObservableList<TrialsView> trialsViewList;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static String downloadPath = null;
    private static final String SERVER_UPLOAD_PATH = "\\\\ETCSVR\\TrialsUpload\\";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> welcome_lbl.requestFocus());
        ShiftManager.setSHIFT(LocalDateTime.now());
        shift_label.setText("Shift : " + ShiftManager.SHIFT_NAME);
        date_lbl.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(new java.util.Date()));
        welcome_lbl.setText("Welcome: " + UserContext.getCurrentUser().getFullName());
        dept_name_lbl.setText(UserContext.getCurrentUser().getDepartmentName() + " Department");
        logo_ImageView.setImage(new Image(DashboardController.class.getResourceAsStream("/images/company_logo.png")));
        // Set Curser Hans
        clearSearch_btn.setCursor(Cursor.HAND);
        searchWithFilter_btn.setCursor(Cursor.HAND);
        update_btn.setCursor(Cursor.HAND);
        // Set TextFiled Count Non Editable
        trials_count_textF.setEditable(false);
        cu_trials_count_textF.setEditable(false);
        fo_trials_count_textF.setEditable(false);
        initializeComboBoxes();
        addFilterListeners();
        loadData();
        updateTrialsCount();
        table_view.getStylesheets().add(getClass().getResource("/screens/style.css").toExternalForm());
        supplier_country_Comb.setItems(FXCollections.observableArrayList());
        // Add listener to supplier_combo to update supplier_country_combo when a supplier is selected
        supplier_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldSupplier, newSupplier) -> {
            updateSupplierCountries(newSupplier);
        });
        // Reset download path for new session
        downloadPath = null;
    }

    // Update supplier_country_combo with countries associated with the selected supplier
    private void updateSupplierCountries(Supplier supplier) {
        ObservableList<SupplierCountry> filteredCountries = FXCollections.observableArrayList();
        if (supplier != null) {
            // Get all countries associated with the selected supplier
            filteredCountries = SupplierCountryDAO.getSupplierCountriesBySupplierId(supplier.getSupplierId());
        }
        supplier_country_Comb.setItems(filteredCountries);
        supplier_country_Comb.getSelectionModel().clearSelection();
    }

    private void initializeComboBoxes() {
        department_Comb.setItems(DepartmentDAO.getAllDepartments());
        department_Comb.setPromptText("Select Department");
        supplier_Comb.setItems(SupplierDAO.getAllSuppliers());
        supplier_Comb.setPromptText("Select Supplier");
        matrial_Comb.setItems(MatrialDAO.getAllMatrials());
        matrial_Comb.setPromptText("Select Material");
        section_Comb.setItems(SectionDAO.getAllSections());
        section_Comb.setPromptText("Select Section");
        supplier_country_Comb.setItems(SupplierCountryDAO.getAllSupplierCountries());
        supplier_country_Comb.setPromptText("Select Supplier Country");
        user_fullname_Comb.setItems(UserDAO.getUsersByDepartmentId(null));
        user_fullname_Comb.setPromptText("Select User");

        department_Comb.setCellFactory(param -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDepartmentName());
            }
        });
        department_Comb.setButtonCell(new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDepartmentName());
            }
        });

        supplier_Comb.setCellFactory(param -> new ListCell<Supplier>() {
            @Override
            protected void updateItem(Supplier item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getSupplierName());
            }
        });
        supplier_Comb.setButtonCell(new ListCell<Supplier>() {
            @Override
            protected void updateItem(Supplier item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getSupplierName());
            }
        });

        matrial_Comb.setCellFactory(param -> new ListCell<Matrial>() {
            @Override
            protected void updateItem(Matrial item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getMatrialName());
            }
        });
        matrial_Comb.setButtonCell(new ListCell<Matrial>() {
            @Override
            protected void updateItem(Matrial item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getMatrialName());
            }
        });
                section_Comb.setCellFactory(param -> new ListCell<Section>() {
            @Override
            protected void updateItem(Section item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getSectionName());
            }
        });

        section_Comb.setButtonCell(new ListCell<Section>() {
            @Override
            protected void updateItem(Section item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getSectionName());
            }
        });

        supplier_country_Comb.setCellFactory(param -> new ListCell<SupplierCountry>() {
            @Override
            protected void updateItem(SupplierCountry item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getCountryName());
            }
        });
        supplier_country_Comb.setButtonCell(new ListCell<SupplierCountry>() {
            @Override
            protected void updateItem(SupplierCountry item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getCountryName());
            }
        });

        user_fullname_Comb.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getFullName());
            }
        });
        user_fullname_Comb.setButtonCell(new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getFullName());
            }
        });

        department_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            user_fullname_Comb.getSelectionModel().clearSelection();
            user_fullname_Comb.setItems(UserDAO.getUsersByDepartmentId(newValue != null ? newValue.getDepartmentId() : null));
            filterTrials();
        });
    }

    private void loadData() {
        trialsViewList = TrialsViewDAO.getAllTrialsView();
        table_view.setItems(trialsViewList);

        no_column.setCellFactory(column -> new TableCell<TrialsView, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || getTableRow() == null || getTableRow().getItem() == null ? null : String.valueOf(getIndex() + 1));
                setStyle("-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;");
            }
        });

        trial_id_column.setCellValueFactory(new PropertyValueFactory<>("trialId"));
        trial_purpose_column.setCellValueFactory(new PropertyValueFactory<>("trialPurpose"));
        trial_creation_date_column.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTrialCreationDate() != null ?
                        cellData.getValue().getTrialCreationDate().format(dateFormatter) : ""));
        trial_notes_column.setCellValueFactory(new PropertyValueFactory<>("trialNotes"));
        section_column.setCellValueFactory(new PropertyValueFactory<>("sectionName"));
        matrial_name_column.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        supplier_name_column.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        supplier_country_name_column.setCellValueFactory(new PropertyValueFactory<>("supplierCountryName"));
        department_name_column.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        user_fullname_column.setCellValueFactory(new PropertyValueFactory<>("userFullName"));
        file_type_column.setCellValueFactory(new PropertyValueFactory<>("fileTypeName"));
        comment_column.setCellValueFactory(new PropertyValueFactory<>("comment"));
        file_creation_date_column.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFileCreationDate() != null ?
                        cellData.getValue().getFileCreationDate().format(dateFormatter) : ""));

        test_situation_column.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setStyle("");
                } else {
                    TrialsView tr = getTableRow().getItem();
                    String testSit = tr.getTestSituation();
                    String color = "";

                    if (testSit.equals(DEF.TEST_SITUATION_ACCEPTED)) {
                        color = "#6ae26a"; // Green
                        testSit = DEF.TEST_SITUATION_ACCEPTED;
                    } else if (testSit.equals(DEF.TEST_SITUATION_REFUSED)) {
                        color = "#ff6b6b"; // Red
                        testSit = DEF.TEST_SITUATION_REFUSED;
                    } else if (testSit.equals(DEF.TEST_SITUATION_HOLD)) {
                        color = "#ffe97d"; // Yellow
                        testSit = DEF.TEST_SITUATION_HOLD;
                    }
                    setText(testSit);
                    // Apply style only if color is not empty, otherwise use alignment and font-weight only
                    if (!color.isEmpty()) {
                        setStyle(String.format("-fx-background-color: %s; -fx-alignment: center; -fx-font-weight: bold;", color));
                    } else {
                        setStyle("-fx-alignment: center; -fx-font-weight: bold;");
                    }
                }
            }
        });

        open_file_column.setCellFactory(col -> new TableCell<TrialsView, String>() {
            private final Button btn = new Button();
            private final FontIcon openIcon = new FontIcon("fas-folder-open");

            {
                openIcon.setIconSize(14);
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
                    String fileName = fullFileName.replaceFirst("^\\d+_", "");
                    btn.setText(fileName);
                    btn.setGraphic(openIcon);
                    setGraphic(btn);
                }
            }
        });

        download_file_column.setCellFactory(col -> new TableCell<TrialsView, String>() {
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

        String columnStyle = "-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;";

        trial_id_column.setStyle(columnStyle);
        trial_purpose_column.setStyle(columnStyle);
        trial_creation_date_column.setStyle(columnStyle);
        trial_notes_column.setStyle(columnStyle);
        section_column.setStyle(columnStyle);
        matrial_name_column.setStyle(columnStyle);
        supplier_name_column.setStyle(columnStyle);
        supplier_country_name_column.setStyle(columnStyle);
        department_name_column.setStyle(columnStyle);
        user_fullname_column.setStyle(columnStyle);
        file_type_column.setStyle(columnStyle);
        test_situation_column.setStyle(columnStyle);
        comment_column.setStyle(columnStyle);
        file_creation_date_column.setStyle(columnStyle);
        open_file_column.setStyle(columnStyle);
        download_file_column.setStyle(columnStyle);

        table_view.setFixedCellSize(32);
        table_view.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table_view.setEditable(false);
        table_view.refresh();
        // Add double-click listener to TableView to show row details in an Alert
        table_view.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && table_view.getSelectionModel().getSelectedItem() != null) {
                TrialsView selectedRow = table_view.getSelectionModel().getSelectedItem();
                StringBuilder details = new StringBuilder();
                details.append("Trial ID : ").append(selectedRow.getTrialId()).append("\n");
                details.append("Trial Purpose : ").append(selectedRow.getTrialPurpose() != null ? selectedRow.getTrialPurpose() : "-").append("\n");
                details.append("Trial Creation Date : ").append(selectedRow.getTrialCreationDate() != null ?
                        selectedRow.getTrialCreationDate().format(dateFormatter) : "-").append("\n");
                details.append("Trial Notes : ").append(selectedRow.getTrialNotes() != null ? selectedRow.getTrialNotes() : "-").append("\n");
                details.append("Section : ").append(selectedRow.getSectionName() != null ? selectedRow.getSectionName() : "-").append("\n");
                details.append("Material : ").append(selectedRow.getMaterialName() != null ? selectedRow.getMaterialName() : "-").append("\n");
                details.append("Supplier : ").append(selectedRow.getSupplierName() != null ? selectedRow.getSupplierName() : "-").append("\n");
                details.append("Supplier Country : ").append(selectedRow.getSupplierCountryName() != null ?
                        selectedRow.getSupplierCountryName() : "-").append("\n");
                details.append("Department : ").append(selectedRow.getDepartmentName() != null ? selectedRow.getDepartmentName() : "-").append("\n");
                details.append("User : ").append(selectedRow.getUserFullName() != null ? selectedRow.getUserFullName() : "-").append("\n");
                details.append("File Type : ").append(selectedRow.getFileTypeName() != null ? selectedRow.getFileTypeName() : "-").append("\n");
                details.append("Test Situation : ").append(selectedRow.getTestSituation() != null ? selectedRow.getTestSituation() : "-").append("\n");
                details.append("Comment : ").append(selectedRow.getComment() != null ? selectedRow.getComment() : "-").append("\n");
                details.append("File Creation Date : ").append(selectedRow.getFileCreationDate() != null ?
                        selectedRow.getFileCreationDate().format(dateFormatter) : "-").append("\n");
                details.append("File Path : ").append(selectedRow.getFilePath() != null ? selectedRow.getFilePath() : "-");

                showInfo(details.toString());
            }
        });
    }

    private void showInfo(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Trial Details");
            alert.setHeaderText("Trial Details");

            TextFlow textFlow = new TextFlow();
            String[] lines = message.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                String[] parts = line.split(": ", 2); // Split on ": " to separate column name and value
                if (parts.length == 2) {
                    Text columnName = new Text(parts[0] + ": ");
                    columnName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #00008B;"); // Dark blue, size 13, bold
                    Text value = new Text(parts[1] + "\n");
                    value.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #000000;"); // Black, size 12, bold
                    textFlow.getChildren().addAll(columnName, value);
                } else {
                    Text text = new Text(line + "\n");
                    text.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #000000;"); // Black, size 13, bold
                    textFlow.getChildren().add(text);
                }
                // Add separator except after the last line
                if (i < lines.length - 1) {
                    Text separator = new Text("-------------------\n");
                    separator.setStyle("-fx-font-size: 10px; -fx-font-weight: normal; -fx-fill: #000000;"); // Black, size 13, normal
                    textFlow.getChildren().add(separator);
                }
            }

            textFlow.setTextAlignment(TextAlignment.LEFT);
            textFlow.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

            ScrollPane scrollPane = new ScrollPane(textFlow);
            scrollPane.setFitToWidth(true);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            alert.getDialogPane().setContent(scrollPane);
            alert.showAndWait();
        });
    }

    private void updateTrialsCount() {
        int totalTrials = TrialDAO.getTrialsCount();
        int cuTrials = TrialDAO.getTrialsCountBySection(1);
        int foTrials = TrialDAO.getTrialsCountBySection(2);
        trials_count_textF.setText(String.valueOf(totalTrials));
        cu_trials_count_textF.setText(String.valueOf(cuTrials));
        fo_trials_count_textF.setText(String.valueOf(foTrials));
    }

    private void addFilterListeners() {
        filter_trial_purpose_textF.textProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        filter_trial_id_textF.textProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        // filter_trial_notes_textF.textProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        // filter_file_comment_textF.textProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        from_trial_creation_date_DP.valueProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        to_trial_creation_date_DP.valueProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        // from_file_creation_date_DP.valueProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        // to_file_creation_date_DP.valueProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        section_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        matrial_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        supplier_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        supplier_country_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        department_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        user_fullname_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
    }

    private void filterTrials() {
        String trialPurpose = filter_trial_purpose_textF.getText();
        Integer trialId = null;
        try {
            String trialIdText = filter_trial_id_textF.getText();
            if (trialIdText != null && !trialIdText.trim().isEmpty()) {
                trialId = Integer.parseInt(trialIdText.trim());
            }
        } catch (NumberFormatException e) {
            WindowUtils.ALERT("Error", "Please enter a valid Trial ID (numeric value)", WindowUtils.ALERT_ERROR);
            filter_trial_id_textF.clear();
            return;
        }
        // String trialNotes = filter_trial_notes_textF.getText();
        // String comment = filter_file_comment_textF.getText();
        LocalDate fromTrialCreationDate = from_trial_creation_date_DP.getValue();
        LocalDate toTrialCreationDate = to_trial_creation_date_DP.getValue();
        // LocalDate fromFileCreationDate = from_file_creation_date_DP.getValue();
        // LocalDate toFileCreationDate = to_file_creation_date_DP.getValue();

        if (fromTrialCreationDate != null && toTrialCreationDate != null && fromTrialCreationDate.isAfter(toTrialCreationDate)) {
            WindowUtils.ALERT("Error", "Trial creation 'From' date cannot be after 'To' date", WindowUtils.ALERT_ERROR);
            from_trial_creation_date_DP.setValue(null);
            return;
        }
        // if (fromFileCreationDate != null && toFileCreationDate != null && fromFileCreationDate.isAfter(toFileCreationDate)) {
        //     WindowUtils.ALERT("Error", "File creation 'From' date cannot be after 'To' date", WindowUtils.ALERT_ERROR);
        //     from_file_creation_date_DP.setValue(null);
        //     return;
        // }

        Integer sectionId = section_Comb.getSelectionModel().getSelectedItem() != null ?
                section_Comb.getSelectionModel().getSelectedItem().getSectionId() : null;
        Integer materialId = matrial_Comb.getSelectionModel().getSelectedItem() != null ?
                matrial_Comb.getSelectionModel().getSelectedItem().getMatrialId() : null;
        Integer supplierId = supplier_Comb.getSelectionModel().getSelectedItem() != null ?
                supplier_Comb.getSelectionModel().getSelectedItem().getSupplierId() : null;
        String supplierCountry = supplier_country_Comb.getSelectionModel().getSelectedItem() != null ?
                supplier_country_Comb.getSelectionModel().getSelectedItem().getCountryName() : null;
        Integer departmentId = department_Comb.getSelectionModel().getSelectedItem() != null ?
                department_Comb.getSelectionModel().getSelectedItem().getDepartmentId() : null;
        Integer userId = user_fullname_Comb.getSelectionModel().getSelectedItem() != null ?
                user_fullname_Comb.getSelectionModel().getSelectedItem().getUserId() : null;

        trialsViewList = TrialsViewDAO.searchTrialsView(trialPurpose, trialId, sectionId, materialId, supplierId, supplierCountry,
                fromTrialCreationDate, toTrialCreationDate, departmentId, userId);
        table_view.setItems(trialsViewList);
        updateTrialsCount();
    }

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

    private void downloadFile(TrialsView trialsView) {
        try {
            if (trialsView != null && trialsView.getFilePath() != null && !trialsView.getFilePath().isEmpty()) {
                String fileName = trialsView.getFilePath();
                String fullPath = SERVER_UPLOAD_PATH + fileName;
                File networkFile = new File(fullPath);
                if (networkFile.exists()) {
                    File saveFile;
                    if (downloadPath == null) {
                        // Open FileChooser for the first download in the session
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setInitialFileName(fileName.replaceFirst("^\\d+_", ""));
                        fileChooser.setTitle("Select Download Location");
                        File selectedDir = fileChooser.showSaveDialog(null);
                        if (selectedDir != null) {
                            // Save the directory path for the session
                            downloadPath = selectedDir.getParent();
                            saveFile = selectedDir;
                        } else {
                            WindowUtils.ALERT("Error", "Download cancelled by user", WindowUtils.ALERT_ERROR);
                            return;
                        }
                    } else {
                        // Use the previously selected directory for subsequent downloads
                        saveFile = new File(downloadPath + File.separator + fileName.replaceFirst("^\\d+_", ""));
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
            Logging.logExpWithMessage("ERROR", getClass().getName(), "downloadFile", e, "file", trialsView != null ? trialsView.getFilePath() : "null");
            WindowUtils.ALERT("Error", "Failed to download file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
        }
    }

    @FXML
    void clearSearch(ActionEvent event) {
        filter_trial_purpose_textF.clear();
        filter_trial_id_textF.clear();
        // filter_trial_notes_textF.clear();
        // filter_file_comment_textF.clear();
        from_trial_creation_date_DP.setValue(null);
        to_trial_creation_date_DP.setValue(null);
        // from_file_creation_date_DP.setValue(null);
        // to_file_creation_date_DP.setValue(null);
        section_Comb.getSelectionModel().clearSelection();
        matrial_Comb.getSelectionModel().clearSelection();
        supplier_Comb.getSelectionModel().clearSelection();
        supplier_country_Comb.getSelectionModel().clearSelection();
        department_Comb.getSelectionModel().clearSelection();
        user_fullname_Comb.getSelectionModel().clearSelection();
        loadData();
    }

    @FXML
    void filterTrialPurpose(KeyEvent event) {
        filterTrials();
    }

    @FXML
    void searchWithFilter(ActionEvent event) {
        filterTrials();
    }

    @FXML
    void update(ActionEvent event) {
        loadData();
        updateTrialsCount();
        table_view.refresh();
    }
}


