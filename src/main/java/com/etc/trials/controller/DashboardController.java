
package com.etc.trials.controller;

import com.etc.trials.dao.*;
import com.etc.trials.model.*;
import com.etc.trials.service.WindowUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DashboardController implements Initializable {

    @FXML private Button clearSearch_btn;
    @FXML private Label date_lbl;
    @FXML private TextField filter_trial_purpose_textF;
    @FXML private TextField filter_trial_id_textF;
    @FXML private DatePicker from_trial_creation_date_DP;
    @FXML private DatePicker to_trial_creation_date_DP;

    @FXML private TableColumn<NewTrialsView, String> trial_id_column;
    @FXML private ImageView logo_ImageView;
    @FXML private ComboBox<Material> material_Comb;
    @FXML private TableColumn<NewTrialsView, String> material_name_column;
    @FXML private TableColumn<NewTrialsView, String> no_column;
    @FXML private TableColumn<NewTrialsView, String> trial_notes_column;
    @FXML private Button searchWithFilter_btn;
    @FXML private ComboBox<Section> section_Comb;
    @FXML private TableColumn<NewTrialsView, String> section_column;
    @FXML private ComboBox<Supplier> supplier_Comb;
    @FXML private TableColumn<NewTrialsView, String> supplier_name_column;
    @FXML private ComboBox<SupplierCountry> supplier_country_Comb;
    @FXML private TableColumn<NewTrialsView, String> supplier_country_name_column;
    @FXML private TableView<NewTrialsView> table_view;
    @FXML private TableColumn<NewTrialsView, LocalDateTime> trial_creation_date_column;
    @FXML private TableColumn<NewTrialsView, String> trial_purpose_column;

    @FXML private Button update_btn;
    @FXML private Label welcome_lbl;
    @FXML private Label dept_name_lbl;

    // Department columns
//    @FXML private TableColumn<NewTrialsView, String> tecOfficeFilePath_column;
//    @FXML private TableColumn<NewTrialsView, String> planningFilePath_column;
//    @FXML private TableColumn<NewTrialsView, String> productionFilePath_column;
//    @FXML private TableColumn<NewTrialsView, String> processFilePath_column;
//    @FXML private TableColumn<NewTrialsView, String> rAndDFilePath_column;
//    @FXML private TableColumn<NewTrialsView, String> qualityControlFilePath_column;
    // FXML fields for department columns
    @FXML
    private TableColumn<NewTrialsView, List<String>> tecOfficeFilePath_column;
    @FXML
    private TableColumn<NewTrialsView, List<String>> planningFilePath_column;
    @FXML
    private TableColumn<NewTrialsView, List<String>> productionFilePath_column;
    @FXML
    private TableColumn<NewTrialsView, List<String>> processFilePath_column;
    @FXML
    private TableColumn<NewTrialsView, List<String>> rAndDFilePath_column;
    @FXML
    private TableColumn<NewTrialsView, List<String>> qualityControlFilePath_column;

    private ObservableList<NewTrialsView> trialsViewList;

   private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm a");

    private static String downloadPath = null;

    private final SectionDao sectionDao = AppContext.getInstance().getSectionDao();
    private final SupplierDao supplierDao = AppContext.getInstance().getSupplierDao();
    private final MaterialDao materialDao = AppContext.getInstance().getMaterialDao();
    private final NewTrialsViewDao newTrialsViewDao = AppContext.getInstance().getNewTrialsViewDao();
    private final SupplierCountryDao supplierCountryDao = AppContext.getInstance().getSupplierCountryDao();
    private final TrialDao trialDao = AppContext.getInstance().getTrialDao();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table_view.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // Allow single row selection
        // Initialize UI components
        Platform.runLater(() -> welcome_lbl.requestFocus());
        date_lbl.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(new java.util.Date()));
        welcome_lbl.setText("Welcome: " + UserContext.getCurrentUser().getFullName());
        dept_name_lbl.setText(UserContext.getCurrentUser().getDepartmentName() + " Department");
        logo_ImageView.setImage(new Image(Objects.requireNonNull(DashboardController.class.getResourceAsStream("/images/etc_logo.png"))));

        // Set cursor for buttons
        clearSearch_btn.setCursor(Cursor.HAND);
        searchWithFilter_btn.setCursor(Cursor.HAND);
        update_btn.setCursor(Cursor.HAND);


        // Initialize combo boxes and listeners
        initializeComboBoxes();
        addFilterListeners();
        loadData();
        //updateTrialsCount();
        table_view.getStylesheets().add(getClass().getResource("/css/style3.css").toExternalForm());
        supplier_country_Comb.setItems(FXCollections.observableArrayList());

        // Add listener to supplier_combo to update supplier_country_combo
        supplier_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldSupplier, newSupplier) -> {
            updateSupplierCountries(newSupplier);
        });

        // Reset download path for new session
        downloadPath = null;
    }

    private void updateSupplierCountries(Supplier supplier) {
        ObservableList<SupplierCountry> filteredCountries = FXCollections.observableArrayList();
        if (supplier != null) {
            filteredCountries = supplierCountryDao.getSupplierCountriesBySupplierId(supplier.getSupplierId());
        }
        supplier_country_Comb.setItems(filteredCountries);
        supplier_country_Comb.getSelectionModel().clearSelection();
    }

    private void initializeComboBoxes() {
        // Initialize combo boxes for filtering
        supplier_Comb.setItems(supplierDao.getAllSuppliers());
      //  supplier_Comb.setPromptText("Select Supplier");
        material_Comb.setItems(materialDao.getAllMaterials());
     //   material_Comb.setPromptText("Select Material");
        section_Comb.setItems(sectionDao.getAllSections());
     //   section_Comb.setPromptText("Select Section");
        supplier_country_Comb.setItems(supplierCountryDao.getAllSupplierCountries());
      //  supplier_country_Comb.setPromptText("Select Supplier Country");

        // Set cell factories for combo boxes
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
    }

    private void loadData() {
        // Debug: Log the contents of trialsViewList for Tec Office
//        for (NewTrialsView trial : trialsViewList) {
//          //  if (trial.getTecOfficeFilePath() != null) {
//             //   System.out.println("Loaded Tec Office FilePath: " + trial.getTecOfficeFilePath() + " for Trial ID: " + trial.getTrialId());
//        //    }
//        }
        trialsViewList = newTrialsViewDao.getAllTrialsView();
        table_view.setItems(trialsViewList);
        // Set up table columns
        no_column.setCellFactory(column -> new TableCell<NewTrialsView, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || getTableRow() == null || getTableRow().getItem() == null ? null : String.valueOf(getIndex() + 1));
                setStyle("-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;");
            }
        });

        trial_id_column.setCellValueFactory(new PropertyValueFactory<>("trialId"));
        trial_purpose_column.setCellValueFactory(new PropertyValueFactory<>("trialPurpose"));
//        trial_creation_date_column.setCellValueFactory(cellData ->
//                new SimpleStringProperty(cellData.getValue().getTrialCreationDate() != null ?
//                        cellData.getValue().getTrialCreationDate().format(dateFormatter) : ""));

        trial_creation_date_column.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getTrialCreationDate()));
        trial_creation_date_column.setCellFactory(column -> new TableCell<NewTrialsView, LocalDateTime>() {
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

        trial_notes_column.setCellValueFactory(new PropertyValueFactory<>("trialNotes"));
        section_column.setCellValueFactory(new PropertyValueFactory<>("sectionName"));
        material_name_column.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        supplier_name_column.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        supplier_country_name_column.setCellValueFactory(new PropertyValueFactory<>("supplierCountryName"));

        // Set up department columns with icons
        setupDepartmentColumn(tecOfficeFilePath_column, "Tec Office", "tecOfficeFilePath");
        setupDepartmentColumn(planningFilePath_column, "Planning", "planningFilePath");
        setupDepartmentColumn(productionFilePath_column, "Production", "productionFilePath");
        setupDepartmentColumn(processFilePath_column, "Process", "processFilePath");
        setupDepartmentColumn(rAndDFilePath_column, "R & D", "rAndDFilePath");
        setupDepartmentColumn(qualityControlFilePath_column, "Quality Control", "qualityControlFilePath");

        // Group department columns under "Departments" header
        TableColumn<NewTrialsView, Void> departmentsHeader = new TableColumn<>("Departments");
        departmentsHeader.getColumns().addAll(

                tecOfficeFilePath_column,
                qualityControlFilePath_column,
                productionFilePath_column,
                processFilePath_column,
                planningFilePath_column,
                rAndDFilePath_column
        );

        // Add all columns to the table
        table_view.getColumns().clear();
        table_view.getColumns().addAll(
                no_column,
                trial_id_column,
                trial_purpose_column,
                trial_creation_date_column,
                trial_notes_column,
                section_column,
                material_name_column,
                supplier_name_column,
                supplier_country_name_column,
                departmentsHeader
        );

        String columnStyle = "-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;";
        String columnStyle2 = "-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;";
        String columnStyle3 = "-fx-alignment: CENTER; -fx-font-size: 10px; -fx-font-weight: bold;";
        trial_id_column.setStyle(columnStyle);
        trial_purpose_column.setStyle(columnStyle2);
        trial_creation_date_column.setStyle(columnStyle2);
        trial_notes_column.setStyle(columnStyle2);
        section_column.setStyle(columnStyle);
        material_name_column.setStyle(columnStyle);
        supplier_name_column.setStyle(columnStyle);
        supplier_country_name_column.setStyle(columnStyle);

        table_view.setFixedCellSize(30);
        table_view.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table_view.setEditable(false);
        table_view.refresh();

        // Add double-click listener to show row details
        table_view.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && table_view.getSelectionModel().getSelectedItem() != null) {
                NewTrialsView selectedRow = table_view.getSelectionModel().getSelectedItem();
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

                showInfo(details.toString());
            }
        });
    }




    private void setupDepartmentColumn(TableColumn<NewTrialsView, List<String>> column, String title, String property) {
        column.setText(title);
        // Adjust property name to match NewTrialsView getter (randDFilePaths for getRandDFilePaths)
        String adjustedProperty = property.equals("rAndDFilePath") ? "randDFilePaths" : property + "s"; // Add 's' for list properties
        // Add CSS class to identify department columns
        column.getStyleClass().add("department-column");
        // Add special CSS class for the last department column
        if (property.equals("qualityControlFilePath")) {
            column.getStyleClass().add("last-department-column");
        }

        // Set cell value factory to retrieve list of file paths
        column.setCellValueFactory(cellData -> {
            switch (property) {
                case "tecOfficeFilePath":
                    return new SimpleObjectProperty<>(cellData.getValue().getTecOfficeFilePaths());
                case "planningFilePath":
                    return new SimpleObjectProperty<>(cellData.getValue().getPlanningFilePaths());
                case "productionFilePath":
                    return new SimpleObjectProperty<>(cellData.getValue().getProductionFilePaths());
                case "processFilePath":
                    return new SimpleObjectProperty<>(cellData.getValue().getProcessFilePaths());
                case "rAndDFilePath":
                    return new SimpleObjectProperty<>(cellData.getValue().getRandDFilePaths());
                case "qualityControlFilePath":
                    return new SimpleObjectProperty<>(cellData.getValue().getQualityControlFilePaths());
                default:
                    return null;
            }
        });

        column.setCellFactory(col -> new TableCell<NewTrialsView, List<String>>() {
            private final FontIcon icon = new FontIcon("fas-folder-open"); // Use FontAwesome5 icon

            {
                icon.setIconSize(18);
                icon.setIconColor(javafx.scene.paint.Color.web("#ecab29"));
                icon.setCursor(Cursor.HAND);
                icon.setOnMouseClicked(e -> {
                    NewTrialsView item = getTableView().getItems().get(getIndex());
                    List<String> filePaths;
                    String departmentName = getDepartmentNameFromProperty(property);
                    switch (property) {
                        case "tecOfficeFilePath":
                            filePaths = item.getTecOfficeFilePaths();
                            break;
                        case "planningFilePath":
                            filePaths = item.getPlanningFilePaths();
                            break;
                        case "productionFilePath":
                            filePaths = item.getProductionFilePaths();
                            break;
                        case "processFilePath":
                            filePaths = item.getProcessFilePaths();
                            break;
                        case "rAndDFilePath":
                            filePaths = item.getRandDFilePaths();
                            break;
                        case "qualityControlFilePath":
                            filePaths = item.getQualityControlFilePaths();
                            break;
                        default:
                            filePaths = null;
                            break;
                    }
                    if (filePaths != null && !filePaths.isEmpty()) {
                        // Open file window with trialId and departmentName
                        WindowUtils.OPEN_WINDOW_WITH_CONTROLLER(WindowUtils.OPEN_FILES_PAGE, controller -> {
                            if (controller instanceof OpenFilesController) {
                                // No need to pass filePaths since OpenFilesController uses FileViewDao
                                ((OpenFilesController) controller).initData(item.getTrialId(), departmentName);
                            }
                        });
                    }
                });
            }

            @Override
            protected void updateItem(List<String> paths, boolean empty) {
                super.updateItem(paths, empty);
                // Debug: Log the file paths for all department columns when rendering
               // System.out.println("Rendering " + property + " FilePaths: " + paths + " for row: " + getIndex() + ", Trial ID: " +
               //         (getTableRow() != null && getTableRow().getItem() != null ? getTableRow().getItem().getTrialId() : "N/A"));
                if (empty || paths == null || paths.isEmpty()) {
                    setGraphic(null);
                } else {
                    setGraphic(icon);
                }
                setStyle("-fx-alignment: CENTER;");
            }
        });
    }
    // Helper method to get department name from property
    private String getDepartmentNameFromProperty(String property) {
        switch (property) {
            case "tecOfficeFilePath":
                return "Technical Office";
            case "planningFilePath":
                return "Planning";
            case "productionFilePath":
                return "Production";
            case "processFilePath":
                return "Process";
            case "rAndDFilePath":
                return "R & D";
            case "qualityControlFilePath":
                return "Quality Control";
            default:
                return "";
        }
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
                String[] parts = line.split(": ", 2);
                if (parts.length == 2) {
                    Text columnName = new Text(parts[0] + ": ");
                    columnName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #00008B;");
                    Text value = new Text(parts[1] + "\n");
                    value.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #000000;");
                    textFlow.getChildren().addAll(columnName, value);
                } else {
                    Text text = new Text(line + "\n");
                    text.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #000000;");
                    textFlow.getChildren().add(text);
                }
                if (i < lines.length - 1) {
                    Text separator = new Text("-------------------\n");
                    separator.setStyle("-fx-font-size: 10px; -fx-font-weight: normal; -fx-fill: #000000;");
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

    private void addFilterListeners() {
        filter_trial_purpose_textF.textProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        filter_trial_id_textF.textProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        from_trial_creation_date_DP.valueProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        to_trial_creation_date_DP.valueProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        section_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        material_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        supplier_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
        supplier_country_Comb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterTrials());
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
        LocalDate fromTrialCreationDate = from_trial_creation_date_DP.getValue();
        LocalDate toTrialCreationDate = to_trial_creation_date_DP.getValue();
        if (fromTrialCreationDate != null && toTrialCreationDate != null && fromTrialCreationDate.isAfter(toTrialCreationDate)) {
            WindowUtils.ALERT("Error", "Trial creation 'From' date cannot be after 'To' date", WindowUtils.ALERT_ERROR);
            from_trial_creation_date_DP.setValue(null);
            return;
        }

        Integer sectionId = section_Comb.getSelectionModel().getSelectedItem() != null ?
                section_Comb.getSelectionModel().getSelectedItem().getSectionId() : null;
        Integer materialId = material_Comb.getSelectionModel().getSelectedItem() != null ?
                material_Comb.getSelectionModel().getSelectedItem().getMaterialId() : null;
        Integer supplierId = supplier_Comb.getSelectionModel().getSelectedItem() != null ?
                supplier_Comb.getSelectionModel().getSelectedItem().getSupplierId() : null;
        String supplierCountry = supplier_country_Comb.getSelectionModel().getSelectedItem() != null ?
                supplier_country_Comb.getSelectionModel().getSelectedItem().getCountryName() : null;

        trialsViewList = newTrialsViewDao.newSearchTrialsView(
                trialPurpose, trialId, sectionId, materialId, supplierId, supplierCountry,
                fromTrialCreationDate, toTrialCreationDate);
        table_view.setItems(trialsViewList);
       // updateTrialsCount();
    }

    @FXML
    void clearSearch(ActionEvent event) {
        filter_trial_purpose_textF.clear();
        filter_trial_id_textF.clear();
        from_trial_creation_date_DP.setValue(null);
        to_trial_creation_date_DP.setValue(null);
        section_Comb.getSelectionModel().clearSelection();
        material_Comb.getSelectionModel().clearSelection();
        supplier_Comb.getSelectionModel().clearSelection();
        supplier_country_Comb.getSelectionModel().clearSelection();
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
     //   updateTrialsCount();
        table_view.refresh();
    }

}