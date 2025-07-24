package com.elsewedyt.trialsapp.controllers;

import com.elsewedyt.trialsapp.dao.*;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.models.*;
import com.elsewedyt.trialsapp.services.ShiftManager;
import com.elsewedyt.trialsapp.services.UserService;
import com.elsewedyt.trialsapp.services.WindowUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class TrialsController implements Initializable {
    @FXML private Button add_trial_btn;
    @FXML private Button clearSearch_btn;
    @FXML private TableColumn<Trial, String> creation_date_column;
    @FXML private Label date_lbl;
    @FXML private VBox department_lbl;
    @FXML private TableColumn<Trial, String> edit_column;
    @FXML private TableColumn<Trial, String> files_column;
    @FXML private TextField filter_creation_textF;
    @FXML private TextField filter_trial_id_textF;
    @FXML private TextField filter_trial_purpose_textF;
    @FXML private TableColumn<Trial, String> id_column;
    @FXML private ImageView logo_ImageView;
    @FXML private ComboBox<Supplier> supplier_Comb;
    @FXML private ComboBox<SupplierCountry> supplier_country_Comb;
    @FXML private ComboBox<Matrial> matrial_Comb;
    @FXML private TableColumn<Trial, String> matrial_column;
    @FXML private TableColumn<Trial, String> no_column;
    @FXML private TableColumn<Trial, String> notes_column;
    @FXML private Button searchWithFilter_btn;
    @FXML private ComboBox<Section> section_Comb;
    @FXML private Label shift_label;
    @FXML private TableColumn<Trial, String> supplier_column;
    @FXML private TableColumn<Trial, String> supplier_country_column;
    @FXML private TableColumn<Trial, String> trial_purpose_column;
    @FXML private TableColumn<Trial, String> section_column;
    @FXML private TextField trials_count_textF;
    @FXML private TextField fo_trials_count_textF;
    @FXML private TextField cu_trials_count_textF;
    @FXML private TableView<Trial> trials_table_view;
    @FXML private Button update_btn;
    @FXML private Label welcome_lbl;
    @FXML private Label dept_name_lbl;

    private ObservableList<Trial> trialsList;
    private Trial trialObj = null;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm a");
    private static TrialsController instance;

    public static TrialsController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        update_btn.setOnAction(event -> refreshTable());
        // Set focus to welcome label
        Platform.runLater(() -> welcome_lbl.requestFocus());
        // Set shift information
        ShiftManager.setSHIFT(LocalDateTime.now());
        String shiftName = ShiftManager.SHIFT_NAME;
        shift_label.setText("Shift : " + shiftName);
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
        // Load and set company logo
        Image img = new Image(MainController.class.getResourceAsStream("/images/company_logo.png"));
        logo_ImageView.setImage(img);

        // Set cursor for buttons
        add_trial_btn.setCursor(Cursor.HAND);
        clearSearch_btn.setCursor(Cursor.HAND);
        update_btn.setCursor(Cursor.HAND);
        searchWithFilter_btn.setCursor(Cursor.HAND);
                // set Permissions
        // Set TextFiled Count Non Editable
        trials_count_textF.setEditable(false);
        cu_trials_count_textF.setEditable(false);
        fo_trials_count_textF.setEditable(false);
        try {
            // Super Admin and Tecnical office Department Only
            int role = UserContext.getCurrentUser().getRole();
            if (role == 4 ||  UserContext.getCurrentUser().getDepartmentId() == 1) {
                add_trial_btn.setVisible(true);
                edit_column.setVisible(true);
            } else {
                add_trial_btn.setVisible(false);
                edit_column.setVisible(false);
            }

        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "initialize Permission", ex);
        }

        // Initialize ComboBoxes
        initializeComboBoxes();
        // Add listener to supplier_combo to update supplier_country_combo when a supplier is selected
        supplier_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldSupplier, newSupplier) -> {
            updateSupplierCountries(newSupplier);
        });

        // Load initial data
        loadData();
        updateTrialsCount();

        // Add listeners for real-time filtering
        addFilterListeners();
    }

    private void initializeComboBoxes() {
        // Load data into ComboBoxes
        supplier_Comb.setItems(SupplierDAO.getAllSuppliers());
        supplier_Comb.setPromptText("Select Supplier");
        supplier_country_Comb.setItems(SupplierCountryDAO.getAllSupplierCountries());
        supplier_country_Comb.setPromptText("Select Supplier Country");
        matrial_Comb.setItems(MatrialDAO.getAllMatrials());
        matrial_Comb.setPromptText("Select Material");
        section_Comb.setItems(SectionDAO.getAllSections());
        section_Comb.setPromptText("Select Section");

        // Set cell factory for ComboBox display
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

    private void addFilterListeners() {
        // Real-time filtering for text fields
        filter_trial_id_textF.textProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        filter_trial_purpose_textF.textProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        filter_creation_textF.textProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        // Real-time filtering for ComboBoxes
        section_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        matrial_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        supplier_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        supplier_country_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
    }

    private void filterTrials() {
        String trialPurpose = filter_trial_purpose_textF.getText().toLowerCase();
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

        // Use as a string
        String creationDatePart = filter_creation_textF.getText().toLowerCase();

        Integer sectionId = section_Comb.getSelectionModel().getSelectedItem() != null ?
                section_Comb.getSelectionModel().getSelectedItem().getSectionId() : null;
        Integer matrialId = matrial_Comb.getSelectionModel().getSelectedItem() != null ?
                matrial_Comb.getSelectionModel().getSelectedItem().getMatrialId() : null;
        Integer supplierId = supplier_Comb.getSelectionModel().getSelectedItem() != null ?
                supplier_Comb.getSelectionModel().getSelectedItem().getSupplierId() : null;
        Integer supplierCountryId = supplier_country_Comb.getSelectionModel().getSelectedItem() != null ?
                supplier_country_Comb.getSelectionModel().getSelectedItem().getSupCountryId() : null;

        // Retrieve all trials (preferably from a cached unfiltered copy)
        List<Trial> allTrials = TrialDAO.getAllTrials();
        final Integer finalTrialId = trialId; // Create a final copy of trialId for lambda
        List<Trial> filtered = allTrials.stream()
                .filter(trial -> trial.getTrialPurpose().toLowerCase().contains(trialPurpose))
                .filter(trial -> finalTrialId == null || trial.getTrialId() == finalTrialId) // Fixed filter
                .filter(trial -> sectionId == null || trial.getSectionId() == sectionId)
                .filter(trial -> matrialId == null || trial.getMatrialId() == matrialId)
                .filter(trial -> supplierId == null || trial.getSupplierId() == supplierId)
                .filter(trial -> supplierCountryId == null || trial.getSupCountryId() == supplierCountryId)
                .filter(trial -> {
                    if (creationDatePart.isEmpty()) return true;
                    String formattedDate = trial.getCreationDate().format(dateFormatter).toLowerCase();
                    return formattedDate.contains(creationDatePart);
                })
                .collect(Collectors.toList());

        trialsList.setAll(filtered);
        updateTrialsCount();
    }
    @FXML
    void filterTrial_id_country(KeyEvent event) {
        filterTrials();
    }
    @FXML
    void filterTrialPurpose(KeyEvent event) {
        filterTrials();
    }

    @FXML
    void filterCreationDate(KeyEvent event) {
        filterTrials();
    }

    @FXML
    void searchWithFilter(ActionEvent event) {
        filterTrials();
    }

    private void updateTrialsCount() {
        int totalTrials = TrialDAO.getTrialsCount();
        int foTrials = TrialDAO.getTrialsCountBySection(1);// Fiber Trials Counts
        int cuTrials = TrialDAO.getTrialsCountBySection(2);  // Cupper Trials Counts
        trials_count_textF.setText(totalTrials + "");
        cu_trials_count_textF.setText(cuTrials + "");
        fo_trials_count_textF.setText(foTrials + "");
    }

    private void loadData() {
        Platform.runLater(() -> {
            // Load all trials
            trialsList = TrialDAO.getAllTrials();
            trials_table_view.setItems(trialsList);

            // Set cell value factories for columns
            no_column.setCellFactory(column -> new TableCell<Trial, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                        setText(null);
                    } else {
                        // Display the row number based on the visible index (starting from 1)
                        setText(String.valueOf(getIndex() + 1));
                        setStyle("-fx-alignment: CENTER; -fx-font-size: 12px; -fx-font-weight: bold;");
                    }
                }
            });

            id_column.setCellValueFactory(new PropertyValueFactory<>("trialId"));
            section_column.setCellValueFactory(new PropertyValueFactory<>("sectionName"));
            trial_purpose_column.setCellValueFactory(new PropertyValueFactory<>("trialPurpose"));
            supplier_column.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
            matrial_column.setCellValueFactory(new PropertyValueFactory<>("matrialName"));
            supplier_country_column.setCellValueFactory(new PropertyValueFactory<>("supplierCountryName"));
            notes_column.setCellValueFactory(new PropertyValueFactory<>("notes"));

            // Format creation date column
            creation_date_column.setCellValueFactory(cellData -> {
                LocalDateTime dateTime = cellData.getValue().getCreationDate();
                StringProperty formatted = new SimpleStringProperty();
                if (dateTime != null) {
                    formatted.set(dateTime.format(dateFormatter));
                } else {
                    formatted.set("");
                }
                return formatted;
            });

            // Configure files column with file icon
//            files_column.setCellFactory(param -> new TableCell<>() {
//              //  private final FontIcon fileIcon = new FontIcon("fa-file");
//                private final FontIcon fileIcon = new FontIcon("fa-folder-plus");
//               // HBox iconBox = new HBox(new FontIcon("fa-folder"), new FontIcon("fa-plus"));
//                private final HBox container = new HBox(fileIcon);
//
//                {
//                    fileIcon.setIconSize(14);
//                    fileIcon.setFill(Color.web("#ecab29"));
//                    fileIcon.setCursor(Cursor.HAND);
//                    Tooltip.install(fileIcon, new Tooltip("Add/View File"));
//
//                    fileIcon.setOnMouseClicked(event -> {
//                        Trial selectedTrial = getTableView().getItems().get(getIndex());
//                        if (selectedTrial != null) {
//                            int Trialid = selectedTrial.getTrialId();
//                            String trialPurpose = selectedTrial.getTrialPurpose();
//                            String departmentName = UserContext.getCurrentUser().getDepartmentName();
//                            int departmentId = UserContext.getCurrentUser().getDepartmentId();
//                            FileType fileType = FileTypeDAO.getFileTypeByDepartmentId(departmentId);
//                           // String fileTypeName = fileType != null ? fileType.getFileTypeName() : null;
//
//                            WindowUtils.OPEN_WINDOW_NOT_RESIZABLE_3("/screens/AddFile.fxml", controller -> {
//                                ((AddFileController) controller).initData(Trialid, trialPurpose, departmentName);
//                            });
//                        }
//                    });
//
//                    container.setSpacing(0.7);
//                    container.setStyle("-fx-alignment: center;");
//                }
//
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
//                        setGraphic(null);
//                    } else {
//                        setGraphic(container);
//                    }
//                    setText(null);
//                }
//            });
            files_column.setCellFactory(param -> new TableCell<>() {
                private final FontIcon folderIcon = new FontIcon("fa-folder");
                private final FontIcon plusIcon = new FontIcon("fa-plus");
                private final HBox container = new HBox(folderIcon, plusIcon);

                {
                    // إعداد الشكل
                    folderIcon.setIconSize(20);
                    folderIcon.setFill(Color.web("#ecab29"));
                    plusIcon.setIconSize(10); // أصغر قليلاً ليبدو داخل المجلد
                    plusIcon.setFill(Color.web("#3b3b3b")); // رمادي غامق
                    // plusIcon.setFill(Color.web("#2c7be5")); // أزرق أنيق
                    //  plusIcon.setFill(Color.web("#ffffff")); //  ابيض
                    // plusIcon.setFill(Color.web("#000000")); //  اسمر

                    container.setSpacing(2); // تجعل الأيقونتين متداخلتين قليلاً لتبدو كرمز واحد
                    container.setAlignment(Pos.CENTER); // توسيط الأيقونات داخل الخلية
                    container.setCursor(Cursor.HAND);

                    // نفس التولتيب
                    Tooltip tooltip = new Tooltip("Add/View File");
                    Tooltip.install(container, tooltip);

                    // نفس الحدث عند الضغط على أي من الأيقونات
                    EventHandler<MouseEvent> clickHandler = event -> {
                        Trial selectedTrial = getTableView().getItems().get(getIndex());
                        if (selectedTrial != null) {
                            int Trialid = selectedTrial.getTrialId();
                            String trialPurpose = selectedTrial.getTrialPurpose();
                            String departmentName = UserContext.getCurrentUser().getDepartmentName();
                            int departmentId = UserContext.getCurrentUser().getDepartmentId();
                            FileType fileType = FileTypeDAO.getFileTypeByDepartmentId(departmentId);

                            WindowUtils.OPEN_WINDOW_NOT_RESIZABLE_3("/screens/AddFile.fxml", controller -> {
                                ((AddFileController) controller).initData(Trialid, trialPurpose, departmentName);
                            });
                        }
                    };

                    // تطبيق الحدث على  HBox
                    container.setOnMouseClicked(clickHandler);
                }

                //                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
//                        setGraphic(null);
//                    } else {
//                        setGraphic(container);
//                    }
//                    setText(null);
//                }
//            });
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                        setGraphic(null);
                        setStyle("");
                    } else {
                        setGraphic(container);
                        // setStyle("-fx-background-color: #f9f9f9;"); // أو استخدم class من CSS
                        // setStyle("-fx-background-color: #e5e5e5;"); // أو استخدم class من CSS
                        //   setStyle("-fx-background-color: #f9f9f9;"); // أو استخدم class من CSS


                    }
                    setText(null);
                }
            });



            // Configure edit column with edit and delete icons
            edit_column.setCellFactory(param -> new TableCell<Trial, String>() {
                private final FontIcon editIcon = new FontIcon("fa-pencil-square");
                private final FontIcon deleteIcon = new FontIcon("fas-trash");
                private final HBox manageBtn = new HBox(editIcon, deleteIcon);

                {
                    editIcon.setCursor(Cursor.HAND);
                    editIcon.setIconSize(16);
                    editIcon.setFill(Color.GREEN);
                    deleteIcon.setCursor(Cursor.HAND);
                    deleteIcon.setIconSize(13);
                    deleteIcon.setFill(Color.RED);
                    manageBtn.setSpacing(2);
                    manageBtn.setAlignment(Pos.CENTER);
                    HBox.setMargin(editIcon, new javafx.geometry.Insets(1.7, 5, 1.7, 5));
                    HBox.setMargin(deleteIcon, new javafx.geometry.Insets(1.7, 5, 1.7, 5));

                    Tooltip.install(editIcon, new Tooltip("Update trial"));
                    Tooltip.install(deleteIcon, new Tooltip("Delete trial"));

                    editIcon.setOnMouseClicked(event -> {
                        Trial trial = getTableView().getItems().get(getIndex());
                        if (trial != null) {
                            trialObj = trial;
                            WindowUtils.OPEN_EDIT_TRIAL_PAGE(trial.getTrialId(), () -> {
                                trialsList.set(getIndex(), TrialDAO.getTrialById(trial.getTrialId()));
                                trials_table_view.refresh();
                                updateTrialsCount();
                            });
                        }
                    });

                    deleteIcon.setOnMouseClicked(event -> {
                        Trial trial = getTableView().getItems().get(getIndex());
                        if (trial != null) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Delete Confirmation");
                            alert.setHeaderText("Are you sure you want to delete this trial?");
                            alert.setContentText("Trial: " + trial.getTrialPurpose() + " | " + trial.getSectionName());

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
                                            boolean deleted = TrialDAO.deleteTrial(trial.getTrialId());
                                            if (deleted) {
                                                loadData(); // Refresh table
                                                WindowUtils.ALERT("Success", "Trial deleted successfully", WindowUtils.ALERT_INFORMATION);
                                            } else {
                                                WindowUtils.ALERT("Error", "Failed to delete trial", WindowUtils.ALERT_ERROR);
                                            }
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", TrialsController.class.getName(), "deleteTrial", ex);
                                            WindowUtils.ALERT("Error", "Failed to delete trial", WindowUtils.ALERT_ERROR);
                                        }
                                    } else {
                                        WindowUtils.ALERT("Error", "Password not correct", WindowUtils.ALERT_WARNING);
                                    }
                                }
                            });
                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                        setGraphic(null);
                    } else {
                        setGraphic(manageBtn);
                    }
                    setText(null);
                }
            });

            // Apply consistent styling to columns
            String columnStyle = "-fx-alignment: CENTER; -fx-font-size: 12px; -fx-font-weight: bold;";
            id_column.setStyle(columnStyle);
            section_column.setStyle(columnStyle);
            matrial_column.setStyle(columnStyle);
            files_column.setStyle(columnStyle);
            creation_date_column.setStyle(columnStyle);
            trial_purpose_column.setStyle(columnStyle);
            no_column.setStyle(columnStyle);
            supplier_column.setStyle("-fx-alignment: CENTER; -fx-font-size: 12px; -fx-font-weight: bold;");
            supplier_country_column.setStyle("-fx-alignment: CENTER; -fx-font-size: 12px; -fx-font-weight: bold;");
            notes_column.setStyle("-fx-alignment: CENTER; -fx-font-size: 12px; -fx-font-weight: bold;");

            // Set fixed cell size and row factory
            trials_table_view.setFixedCellSize(32);
            trials_table_view.setRowFactory(tv -> {
                TableRow<Trial> row = new TableRow<>() {
                    @Override
                    protected void updateItem(Trial trial, boolean empty) {
                        super.updateItem(trial, empty);
                    }
                };

                row.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                    if (isSelected && !row.isEmpty()) {
                        row.getStyleClass().add("selected-row");
                    } else {
                        row.getStyleClass().remove("selected-row");
                    }
                });

                return row;
            });

            trials_table_view.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            trials_table_view.refresh();
        });
    }

    void clearHelp(){
        // Clear text fields
        filter_trial_purpose_textF.clear();
        filter_trial_id_textF.clear();
        filter_creation_textF.clear();
        // Clear ComboBox selections
        section_Comb.getSelectionModel().clearSelection();
        matrial_Comb.getSelectionModel().clearSelection();
        supplier_Comb.getSelectionModel().clearSelection();
        supplier_country_Comb.getSelectionModel().clearSelection();

        // Reload all trials
        loadData();
    }
    @FXML
    void clearSearch(ActionEvent event) {
     clearHelp();
    }

    @FXML
    void openAddTrial(ActionEvent event) {
        WindowUtils.OPEN_ADD_TRIAL_PAGE(false, this); // Pass this TrialsController instance
    }

    // Update the table view and related data
    @FXML
    void update() {
        Platform.runLater(() -> {
            clearHelp();
            trials_table_view.refresh();
            updateTrialsCount();
            loadData();
        });
    }

    // Refresh the table view and related data
    public void refreshTable() {
        Platform.runLater(() -> {
            clearHelp();
            loadData();
            trials_table_view.refresh();
            updateTrialsCount();
        });
    }



}