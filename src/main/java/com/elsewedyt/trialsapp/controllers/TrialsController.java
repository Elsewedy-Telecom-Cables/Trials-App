package com.elsewedyt.trialsapp.controllers;

import com.elsewedyt.trialsapp.dao.FileTypeDAO;
import com.elsewedyt.trialsapp.dao.MatrialDAO;
import com.elsewedyt.trialsapp.dao.SupplierDAO;
import com.elsewedyt.trialsapp.dao.TrialDAO;
import com.elsewedyt.trialsapp.dao.SectionDAO;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.models.*;
import com.elsewedyt.trialsapp.services.ShiftManager;
import com.elsewedyt.trialsapp.services.UserService;
import com.elsewedyt.trialsapp.services.WindowUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class TrialsController implements Initializable {
    @FXML
    private Button add_trial_btn;

    @FXML
    private Button clearSearch_btn;

    @FXML
    private TableColumn<Trial, String> creation_date_column;

    @FXML
    private Label date_lbl;

    @FXML
    private VBox department_lbl;

    @FXML
    private TableColumn<Trial, String> edit_column;

    @FXML
    private TableColumn<Trial, String> files_column;



    @FXML
    private TextField filter_creation_textF;

    @FXML
    private TextField filter_suplier_country_textF;

    @FXML
    private TextField filter_trial_purpose_textF;

    @FXML
    private TableColumn<Trial, String> id_column;

    @FXML
    private ImageView logo_ImageView;

    @FXML
    private ComboBox<Supplier> supplier_Comb;

    @FXML
    private ComboBox<Matrial> matrial_Comb;

    @FXML
    private TableColumn<Trial, String> matrial_column;

    @FXML
    private TableColumn<Trial, String> no_column;

    @FXML
    private TableColumn<Trial, String> notes_column;

    @FXML
    private Button searchWithFilter_btn;

    @FXML
    private Label search_lable;

    @FXML
    private ComboBox<Section> section_Comb;

    @FXML
    private Label shift_label;

    @FXML
    private TableColumn<Trial, String> supplier_column;

    @FXML
    private TableColumn<Trial, String> supplier_country_column;

    @FXML
    private TableColumn<Trial, String> trial_purpose_column;
    @FXML
    private TableColumn<Trial, String> section_column;



    @FXML
    private TextField trials_count_textF;
    @FXML
    private TextField fo_trials_count_textF;
    @FXML
    private TextField cu_trials_count_textF;


    @FXML
    private TableView<Trial> trials_table_view;

    @FXML
    private Button update_btn;

    @FXML
    private Label welcome_lbl;
    @FXML
    private Label dept_name_lbl;

    private ObservableList<Trial> trialsList;
    private Trial trialObj = null;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        // Initialize ComboBoxes
        initializeComboBoxes();

        // Load initial data
        loadData();
        updateTrialsCount();

        // Update trials count
       // updateTrialsCount();

        // Add listeners for real-time filtering
        addFilterListeners();
    }

    private void initializeComboBoxes() {
        // Load data into ComboBoxes
        supplier_Comb.setItems(SupplierDAO.getAllSuppliers());
        supplier_Comb.setPromptText("Select Supplier");
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
                LocalDate date = cellData.getValue().getCreationDate();
                StringProperty formatted = new SimpleStringProperty();
                if (date != null) {
                    formatted.set(date.format(dateFormatter));
                } else {
                    formatted.set("");
                }
                return formatted;
            });

            // Configure files column with file icon
            files_column.setCellFactory(param -> new TableCell<>() {
                private final FontIcon fileIcon = new FontIcon("fa-file");
                private final HBox container = new HBox(fileIcon);

                {
                    fileIcon.setIconSize(14);
                    fileIcon.setFill(Color.web("#ecab29"));
                    fileIcon.setCursor(Cursor.HAND);
                    Tooltip.install(fileIcon, new Tooltip("Add/View File"));

                    fileIcon.setOnMouseClicked(event -> {
                        Trial selectedTrial = getTableView().getItems().get(getIndex());
                        if (selectedTrial != null) {
                            int Trialid = selectedTrial.getTrialId();
                            String trialPurpose = selectedTrial.getTrialPurpose();
                            String departmentName = UserContext.getCurrentUser().getDepartmentName();
                            int departmentId = UserContext.getCurrentUser().getDepartmentId();
                            FileType fileType = FileTypeDAO.getFileTypeByDepartmentId(departmentId);
                           // String fileTypeName = fileType != null ? fileType.getFileTypeName() : null;

                            WindowUtils.OPEN_WINDOW_NOT_RESIZABLE_3("/screens/AddFile.fxml", controller -> {
                                ((AddFileController) controller).initData(Trialid, trialPurpose, departmentName);
                            });
                        }
                    });

                    container.setSpacing(0.7);
                    container.setStyle("-fx-alignment: center;");
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                        setGraphic(null);
                    } else {
                        setGraphic(container);
                    }
                    setText(null);
                }
            });

            // Configure edit column with edit and delete icons
            edit_column.setCellFactory(param -> new TableCell<Trial, String>() {
                private final FontIcon editIcon = new FontIcon("fa-pencil-square");
                private final FontIcon deleteIcon = new FontIcon("fa-trash");
                private final HBox manageBtn = new HBox(editIcon, deleteIcon);

                {
                    editIcon.setCursor(Cursor.HAND);
                    editIcon.setIconSize(14);
                    editIcon.setFill(Color.GREEN);
                    deleteIcon.setCursor(Cursor.HAND);
                    deleteIcon.setIconSize(14);
                    deleteIcon.setFill(Color.RED);
                    manageBtn.setSpacing(0.7);
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

    // trials_count_textF.setText(String.valueOf(trialsList.size()));
    private void updateTrialsCount() {
        int totalTrials = TrialDAO.getTrialsCount();
        int cuTrials = TrialDAO.getTrialsCountBySection(1);  // Cupper Trials Counts
        int foTrials = TrialDAO.getTrialsCountBySection(2);// Fiber Trials Counts
        trials_count_textF.setText(totalTrials + "");
        cu_trials_count_textF.setText(cuTrials + "");
        fo_trials_count_textF.setText(foTrials + "");
    }

    private void addFilterListeners() {
        // Real-time filtering for text fields
        filter_trial_purpose_textF.textProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        filter_suplier_country_textF.textProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        filter_creation_textF.textProperty().addListener((obs, oldValue, newValue) -> filterTrials());

        // Real-time filtering for ComboBoxes
        section_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        matrial_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        supplier_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
    }

    private void filterTrials() {
        String trialPurpose = filter_trial_purpose_textF.getText();
        String supplierCountry = filter_suplier_country_textF.getText();
        LocalDate creationDate = null;

        // Parse creation date
        try {
            if (!filter_creation_textF.getText().isEmpty()) {
                creationDate = LocalDate.parse(filter_creation_textF.getText(), dateFormatter);
            }
        } catch (DateTimeParseException e) {
            // Invalid date format, ignore
            creationDate = null;
        }

        Integer sectionId = section_Comb.getSelectionModel().getSelectedItem() != null ?
                section_Comb.getSelectionModel().getSelectedItem().getSectionId() : null;
        Integer matrialId = matrial_Comb.getSelectionModel().getSelectedItem() != null ?
                matrial_Comb.getSelectionModel().getSelectedItem().getMatrialId() : null;
        Integer supplierId = supplier_Comb.getSelectionModel().getSelectedItem() != null ?
                supplier_Comb.getSelectionModel().getSelectedItem().getSupplierId() : null;

        trialsList = TrialDAO.searchTrials(trialPurpose, sectionId, matrialId, supplierId, supplierCountry, creationDate);
        trials_table_view.setItems(trialsList);
        updateTrialsCount();
    }


    void clearHelp(){
        // Clear text fields
        filter_trial_purpose_textF.clear();
        filter_suplier_country_textF.clear();
        filter_creation_textF.clear();

        // Clear ComboBox selections
        section_Comb.getSelectionModel().clearSelection();
        matrial_Comb.getSelectionModel().clearSelection();
        supplier_Comb.getSelectionModel().clearSelection();

        // Reload all trials
        loadData();
    }
    @FXML
    void clearSearch(ActionEvent event) {
     clearHelp();
    }

    @FXML
    void filterCreationDate(KeyEvent event) {
        filterTrials();
    }

    @FXML
    void filterSuplierCountry(KeyEvent event) {
        filterTrials();
    }

    @FXML
    void filterTrialPurpose(KeyEvent event) {
        filterTrials();
    }

//    @FXML
//    void openAddTrial(ActionEvent event) {
//        WindowUtils.OPEN_ADD_TRIAL_PAGE(false);
//    }
    @FXML
    void openAddTrial(ActionEvent event) {
        WindowUtils.OPEN_ADD_TRIAL_PAGE(false, this); // Pass this TrialsController instance
    }

    @FXML
    void searchWithFilter(ActionEvent event) {
        filterTrials();
    }





        // Other initialization code

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
            System.out.println("Table items after loadData: " + trials_table_view.getItems());
            trials_table_view.refresh();
            updateTrialsCount();
        });
    }

}