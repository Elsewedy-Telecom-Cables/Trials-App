package com.etc.trialsapp.controller;

import com.etc.trialsapp.dao.*;
import com.etc.trialsapp.logging.Logging;
import com.etc.trialsapp.model.*;
import com.etc.trialsapp.service.UserService;
import com.etc.trialsapp.service.WindowUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
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


public class TrialsViewController implements Initializable {
    @FXML private Button add_trial_btn;
    @FXML private TableColumn<Trial, LocalDateTime> creation_date_column;
    @FXML private Label date_lbl;
    @FXML private TableColumn<Trial, String> edit_column;
    @FXML private TableColumn<Trial, String> files_column;
    @FXML private TextField filter_creation_textF;
    @FXML private TextField filter_trial_id_textF;
    @FXML private TextField filter_trial_purpose_textF;
    @FXML private TableColumn<Trial, String> id_column;
    @FXML private ImageView logo_ImageView;
    @FXML private ComboBox<Supplier> supplier_Comb;
    @FXML private ComboBox<SupplierCountry> supplier_country_Comb;
    @FXML private ComboBox<Material> material_Comb;
    @FXML private TableColumn<Trial, String> material_column;
    @FXML private TableColumn<Trial, String> no_column;
    @FXML private TableColumn<Trial, String> notes_column;
    @FXML private ComboBox<Section> section_Comb;
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
    private static TrialsViewController instance;

    public static TrialsViewController getInstance() {
        return instance;
    }

    private final SupplierDao supplierDao = AppContext.getInstance().getSupplierDao();
    private final SupplierCountryDao supplierCountryDao = AppContext.getInstance().getSupplierCountryDao();
    private final MaterialDao materialDao = AppContext.getInstance().getMaterialDao();
    private final SectionDao sectionDao = AppContext.getInstance().getSectionDao();
    private final TrialDao trialDao = AppContext.getInstance().getTrialDao();
    private final FileTypeDao fileTypeDao = AppContext.getInstance().getFileTypeDao();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        update_btn.setOnAction(event -> refreshTable());
        // Set focus to welcome label
        Platform.runLater(() -> welcome_lbl.requestFocus());
        // Set current date and time
        java.util.Date date = new java.util.Date();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy  hh:mm a");
        date_lbl.setText(dateFormat2.format(date) + " ");
        // Set welcome message with current user's full name
        String msg = ("Welcome : " + UserContext.getCurrentUser().getFullName());
        welcome_lbl.setText(msg);

        String msg2 = (UserContext.getCurrentUser().getDepartmentName() + " Department");
        dept_name_lbl.setText(msg2);
        // Load and set company logo
        Image img = new Image(Objects.requireNonNull(MainController.class.getResourceAsStream("/images/etc_logo.png")));
        logo_ImageView.setImage(img);


        // Set TextFiled Count Non Editable
        trials_count_textF.setEditable(false);
        cu_trials_count_textF.setEditable(false);
        fo_trials_count_textF.setEditable(false);
        try {
            // Super Admin and Technical office Department Only
            int adminRole = UserContext.getCurrentUser().getRole();
            int deptId = UserContext.getCurrentUser().getDepartmentId();
            if (adminRole == 4 ||  deptId == 1) {
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

        supplier_country_Comb.setItems(FXCollections.observableArrayList());

        // Add listener to supplier_combo to update supplier_country_combo
        supplier_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldSupplier, newSupplier) -> {
            updateSupplierCountries(newSupplier);
        });

        // Load initial data
        loadData();
        updateTrialsCount();
        addFilterListeners();
    }

    private void initializeComboBoxes() {
        // Load data into ComboBoxes
        supplier_Comb.setItems(supplierDao.getAllSuppliers());
        supplier_country_Comb.setItems(supplierCountryDao.getAllSupplierCountries());
        material_Comb.setItems(materialDao.getAllMaterials());
        section_Comb.setItems(sectionDao.getAllSections());

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
        material_Comb.setCellFactory(param -> new ListCell<Material>() {
            @Override
            protected void updateItem(Material item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getMaterialName());
            }
        });
        material_Comb.setButtonCell(new ListCell<Material>() {
            @Override
            protected void updateItem(Material item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getMaterialName());
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


    private void updateSupplierCountries(Supplier supplier) {
        ObservableList<SupplierCountry> filteredCountries = FXCollections.observableArrayList();
        if (supplier != null) {
            // Get all countries associated with the selected supplier
            filteredCountries = supplierCountryDao.getSupplierCountriesBySupplierId(supplier.getSupplierId());
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
        material_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
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
        Integer matrialId = material_Comb.getSelectionModel().getSelectedItem() != null ?
                material_Comb.getSelectionModel().getSelectedItem().getMaterialId() : null;
        Integer supplierId = supplier_Comb.getSelectionModel().getSelectedItem() != null ?
                supplier_Comb.getSelectionModel().getSelectedItem().getSupplierId() : null;
        Integer supplierCountryId = supplier_country_Comb.getSelectionModel().getSelectedItem() != null ?
                supplier_country_Comb.getSelectionModel().getSelectedItem().getSupCountryId() : null;

        // Retrieve all trials (preferably from a cached unfiltered copy)
        List<Trial> allTrials = trialDao.getAllTrials();
        final Integer finalTrialId = trialId; // Create a final copy of trialId for lambda
        List<Trial> filtered = allTrials.stream()
                .filter(trial -> trial.getTrialPurpose().toLowerCase().contains(trialPurpose))
                .filter(trial -> finalTrialId == null || trial.getTrialId() == finalTrialId) // Fixed filter
                .filter(trial -> sectionId == null || trial.getSectionId() == sectionId)
                .filter(trial -> matrialId == null || trial.getMaterialId() == matrialId)
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
        int totalTrials = trialDao.getTrialsCount();
        int foTrials = trialDao.getTrialsCountBySection(1);// Fiber Trials Counts
        int cuTrials = trialDao.getTrialsCountBySection(2);  // Copper Trials Counts
        trials_count_textF.setText(totalTrials + "");
        cu_trials_count_textF.setText(cuTrials + "");
        fo_trials_count_textF.setText(foTrials + "");
    }

    private void loadData() {
        Platform.runLater(() -> {
            // Load all trials
            trialsList = trialDao.getAllTrials();
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
                        setStyle("-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;");
                    }
                }
            });

            id_column.setCellValueFactory(new PropertyValueFactory<>("trialId"));
            section_column.setCellValueFactory(new PropertyValueFactory<>("sectionName"));
            trial_purpose_column.setCellValueFactory(new PropertyValueFactory<>("trialPurpose"));
            supplier_column.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
            material_column.setCellValueFactory(new PropertyValueFactory<>("materialName"));
            supplier_country_column.setCellValueFactory(new PropertyValueFactory<>("supplierCountryName"));
            notes_column.setCellValueFactory(new PropertyValueFactory<>("notes"));

            creation_date_column.setCellValueFactory(cellData ->
                    new SimpleObjectProperty<>(cellData.getValue().getCreationDate()));
            creation_date_column.setCellFactory(column -> new TableCell<Trial, LocalDateTime>() {
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


            files_column.setCellFactory(param -> new TableCell<>() {
                private final FontIcon folderIcon = new FontIcon("fas-folder-plus");
              //  private final FontIcon plusIcon = new FontIcon("fas-plus");
                private final HBox container = new HBox(folderIcon);

                {

                    folderIcon.setIconSize(21);
                    folderIcon.setFill(Color.web("#ecab29"));
                    //plusIcon.setIconSize(9);
                  //  plusIcon.setFill(Color.web("#3b3b3b"));


                   // container.setSpacing(2);
                    container.setAlignment(Pos.CENTER);
                    container.setCursor(Cursor.HAND);

                    Tooltip tooltip = new Tooltip("Add/View File");
                    Tooltip.install(container, tooltip);

                    EventHandler<MouseEvent> clickHandler = event -> {
                        Trial selectedTrial = getTableView().getItems().get(getIndex());
                        if (selectedTrial != null) {
                            int Trialid = selectedTrial.getTrialId();
                            String trialPurpose = selectedTrial.getTrialPurpose();
                            String departmentName = UserContext.getCurrentUser().getDepartmentName();
                            int departmentId = UserContext.getCurrentUser().getDepartmentId();
                            FileType fileType = fileTypeDao.getFileTypeByDepartmentId(departmentId);

                            WindowUtils.OPEN_WINDOW_WITH_CONTROLLER_AND_STAGE("/screens/File.fxml", controller -> {
                                controller.initData(Trialid, trialPurpose, departmentName);
                            });
                        }
                    };

                    container.setOnMouseClicked(clickHandler);
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                        setGraphic(null);
                        setStyle("");
                    } else {
                        setGraphic(container);


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
                    editIcon.setIconSize(15);
                    editIcon.setFill(Color.GREEN);
                    deleteIcon.setCursor(Cursor.HAND);
                    deleteIcon.setIconSize(12);
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
                                trialsList.set(getIndex(), trialDao.getTrialById(trial.getTrialId()));
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
                                            boolean deleted = trialDao.deleteTrial(trial.getTrialId());
                                            if (deleted) {
                                                loadData(); // Refresh table
                                                WindowUtils.ALERT("Success", "Trial deleted successfully", WindowUtils.ALERT_INFORMATION);
                                            } else {
                                                WindowUtils.ALERT("Error", "Failed to delete trial", WindowUtils.ALERT_ERROR);
                                            }
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", TrialsViewController.class.getName(), "deleteTrial", ex);
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
            String columnStyle = "-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;";
            id_column.setStyle(columnStyle);
            section_column.setStyle(columnStyle);
            material_column.setStyle(columnStyle);
            files_column.setStyle(columnStyle);
            creation_date_column.setStyle(columnStyle);
            trial_purpose_column.setStyle(columnStyle);
            no_column.setStyle(columnStyle);
            supplier_column.setStyle("-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;");
            supplier_country_column.setStyle("-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;");
            notes_column.setStyle("-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;");

            // Set fixed cell size and row factory
            trials_table_view.setFixedCellSize(30);
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
        material_Comb.getSelectionModel().clearSelection();
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
        WindowUtils.OPEN_ADD_TRIAL_PAGE(false, this); // Pass this TrialsViewController instance
    }

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