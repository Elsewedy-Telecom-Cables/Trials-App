
package com.elsewedyt.trialsapp.controllers;

import com.elsewedyt.trialsapp.dao.*;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.models.*;
import com.elsewedyt.trialsapp.services.UserService;
import com.elsewedyt.trialsapp.services.WindowUtils;
import static com.elsewedyt.trialsapp.services.WindowUtils.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;

public class PrepareDataController implements Initializable {

    @FXML private Button add_file_type_btn;
    @FXML private Button add_matrial_btn;
    @FXML private Button add_supplier_btn;
    @FXML private Button add_supplier_country_btn;

    @FXML private Button clear_file_type_btn;

    @FXML private Button clear_matrial_btn;

    @FXML private Button clear_supplier_btn;

    @FXML private Button clear_supplier_cou_btn;
    @FXML private Label date_lbl;
    @FXML private TableColumn<FileType, String> file_type_delete_colm;
    @FXML
    private TableColumn<FileType, String> file_type_id_colm;

    @FXML private TableColumn<FileType, String> file_type_name_colm;

    @FXML private TableColumn<FileType, String> dept_name_in_file_type_colm;

    @FXML private TextField file_type_name_textF;

    @FXML private TableView<FileType> file_type_table_view;

    @FXML private TextField filter_file_type_textF;

    @FXML private TextField filter_matrial_textF;

    @FXML private TextField filter_supplier_cou_textF;

    @FXML private TextField filter_supplier_textF;

    @FXML private ImageView logo_ImageView;

    @FXML private TableColumn<Matrial, String> matrial_delete_colm;

    @FXML private TableColumn<Matrial, String> matrial_id_colm;

    @FXML private TableColumn<Matrial, String> matrial_name_colm;

    @FXML private TextField matrial_name_textF;

    @FXML private TableView<Matrial> matrial_table_view;

    @FXML private Label shift_label;

    @FXML private ComboBox<Supplier> supplier_comb;
    @FXML private ComboBox<Country> country_comb;
    @FXML private TableColumn<SupplierCountry, String> supplier_cou_delete_colm;

    @FXML private TableColumn<SupplierCountry, String> supplier_cou_id_colm;

    @FXML private TableColumn<SupplierCountry, String> supplier_cou_name_colm;

    @FXML private TableColumn<SupplierCountry, String> supplier_name_in_sup_coun_colm;

    @FXML private TableView<SupplierCountry> supplier_country_table_view;
    @FXML private TableColumn<Supplier, String> supplier_delete_colm;

    @FXML private TableColumn<Supplier, String> supplier_id_colm;

    @FXML private TableColumn<Supplier, String> supplier_name_colm;

    @FXML private TextField supplier_name_textF;

    @FXML private TableView<Supplier> supplier_table_view;

    @FXML private Button update_file_type_btn;

    @FXML private TextField update_file_type_name_textF;

    @FXML private Button update_matrial_btn;

    @FXML private TextField update_matrial_name_textF;

    @FXML private Button update_supplier_btn;

    @FXML private Button update_supplier_cou_btn;

    @FXML private TextField update_supplier_name_textF;

    @FXML private Label welcome_lbl;
    @FXML
    private Button add_country_btn;

    ObservableList<FileType> fileTypeList;
    ObservableList<Matrial> materialList;
    ObservableList<SupplierCountry> supplierCountryList;
    ObservableList<Supplier> supplierList;
    ObservableList<Country> countryList;
    @FXML
    private Button deptartments_sections_btn;

    @FXML
    private Button clear_country_btn;

    @FXML
    private TableColumn<Country,String> country_delete_colm;

    @FXML
    private TableColumn<Country,String> country_id_colm;

    @FXML
    private TableColumn<Country,String> country_name_colm;

    @FXML
    private TextField country_name_textF;

    @FXML
    private TableView<Country> country_table_view;

    @FXML
    private TextField filter_country_textF;

    @FXML
    private Button update_country_btn;

    @FXML
    private TextField update_country_name_textF;
    @FXML private Label department_lbl;
    @FXML private ComboBox<Department> department_comb;
    ObservableList<Department> departmentList;
    //ObservableList<Country> countryList;

    // DAO instances

    private final FileTypeDAO fileTypeDAO = new FileTypeDAO();
    private final MatrialDAO matrialDAO = new MatrialDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private final SupplierCountryDAO supplierCountryDAO = new SupplierCountryDAO();
    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final CountryDAO countryDAO = new CountryDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set focus to welcome label
        Platform.runLater(() -> welcome_lbl.requestFocus());
        // Load and set company logo
        Image img = new Image(MainController.class.getResourceAsStream("/images/company_logo.png"));
        logo_ImageView.setImage(img);
        // Set current date and time
        java.util.Date date = new java.util.Date();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy  hh:mm a");
        date_lbl.setText(dateFormat2.format(date) + " ");
        // Set welcome message with current user's full name
        String msg = ("Welcome : " + UserContext.getCurrentUser().getFullName());
        welcome_lbl.setText(msg);
        // Set Department Name with current user's
        String msg2 = (UserContext.getCurrentUser().getDepartmentName() + " Department");
        department_lbl.setText(msg2);
        //
        // Existing initialization code (e.g., table setup, load data, etc.)
        try {
            if (UserContext.getCurrentUser() == null) {
                Logging.logMessage(Logging.WARN, getClass().getName(), "initialize Permission", "Current user is null");
                return;
            }

            Platform.runLater(() -> {
                if (UserContext.getCurrentUser().getRole() != 4) {
                    int deptId = UserContext.getCurrentUser().getDepartmentId();
                    // Auto-select department for userDeptartment_ComBox
                    for (Department dept : department_comb.getItems()) {
                        if (dept.getDepartmentId() == deptId) {
                            department_comb.getSelectionModel().select(dept);
                            department_comb.setMouseTransparent(true); // Make read-only
                            department_comb.setFocusTraversable(false);
                            break;
                        }
                    }
                    // Auto-select department for department_comb
                    for (Department dept : department_comb.getItems()) {
                        if (dept.getDepartmentId() == deptId) {
                            department_comb.getSelectionModel().select(dept);
                            department_comb.setMouseTransparent(true); // Make read-only
                            department_comb.setFocusTraversable(false);
                            break;
                        }
                    }
                } else {
                    // Super Admin: allow interaction
                    department_comb.setMouseTransparent(false);
                    department_comb.setFocusTraversable(true);
                    department_comb.setMouseTransparent(false);
                    department_comb.setFocusTraversable(true);
                }
            });

            // Other initialization code (e.g., setupDepartmentColumn, load data, etc.)
        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "initialize", ex);
        }



        try {
            // Get current user and their role/department
            if (UserContext.getCurrentUser() == null) {
                Logging.logMessage(Logging.WARN, getClass().getName(), "initialize Permission", "Current user is null");

                return; // Exit if user is null to avoid NullPointerException
            }

            int role = UserContext.getCurrentUser().getRole();
            int departmentId = UserContext.getCurrentUser().getDepartmentId();

            // Always hide deptartments_sections_btn for Technical Office Department (DepartmentId == 1)
            deptartments_sections_btn.setVisible(departmentId != 1);

            // Super Admin (role == 4) permissions
            if (role == 4) {
                deptartments_sections_btn.setVisible(true); // Override for Super Admin
                add_country_btn.setVisible(true);
                add_matrial_btn.setVisible(true);
                add_supplier_btn.setVisible(true);
                add_supplier_country_btn.setVisible(true);
                update_country_btn.setVisible(true);
                update_matrial_btn.setVisible(true);
                update_supplier_btn.setVisible(true);
                update_supplier_cou_btn.setVisible(true);
                country_delete_colm.setVisible(true);
                matrial_delete_colm.setVisible(true);
                supplier_delete_colm.setVisible(true);
                supplier_cou_delete_colm.setVisible(true);
            }
            // Technical Office Department (DepartmentId == 1) or Super Admin additional permissions
            else if (departmentId == 1) {
                add_country_btn.setVisible(true);
                add_matrial_btn.setVisible(true);
                add_supplier_btn.setVisible(true);
                add_supplier_country_btn.setVisible(true);
                update_country_btn.setVisible(true);
                update_matrial_btn.setVisible(true);
                update_supplier_btn.setVisible(true);
                update_supplier_cou_btn.setVisible(true);
                country_delete_colm.setVisible(true);
                matrial_delete_colm.setVisible(true);
                supplier_delete_colm.setVisible(true);
                supplier_cou_delete_colm.setVisible(true);
            }
            // Default: Hide all buttons for other roles/departments
            else {
                add_country_btn.setVisible(false);
                add_matrial_btn.setVisible(false);
                add_supplier_btn.setVisible(false);
                add_supplier_country_btn.setVisible(false);
                update_country_btn.setVisible(false);
                update_matrial_btn.setVisible(false);
                update_supplier_btn.setVisible(false);
                update_supplier_cou_btn.setVisible(false);
                country_delete_colm.setVisible(false);
                matrial_delete_colm.setVisible(false);
                supplier_delete_colm.setVisible(false);
                supplier_cou_delete_colm.setVisible(false);
                deptartments_sections_btn.setVisible(false);
            }

        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "initialize Permission", ex);
        }

        // Set cursor to hand for all buttons

        add_file_type_btn.setCursor(Cursor.HAND);
        add_matrial_btn.setCursor(Cursor.HAND);
        add_supplier_btn.setCursor(Cursor.HAND);
        add_supplier_country_btn.setCursor(Cursor.HAND);
        clear_file_type_btn.setCursor(Cursor.HAND);
        clear_matrial_btn.setCursor(Cursor.HAND);
        clear_supplier_btn.setCursor(Cursor.HAND);
        clear_supplier_cou_btn.setCursor(Cursor.HAND);
        update_file_type_btn.setCursor(Cursor.HAND);
        update_matrial_btn.setCursor(Cursor.HAND);
        update_supplier_btn.setCursor(Cursor.HAND);
        update_supplier_cou_btn.setCursor(Cursor.HAND);
        add_country_btn.setCursor(Cursor.HAND);
        deptartments_sections_btn.setCursor(Cursor.HAND);

        // Load Data For All Tables
        loadFileTypesData();
        loadMatrialsData();
        loadSupplierCountriesData();
        loadSuppliersData();

        // Initialize ObservableLists
      //  fileTypeList = fileTypeDAO.getAllFileTypes();
        fileTypeList = fileTypeDAO.getAllFileTypes();
        materialList = matrialDAO.getAllMatrials();
        supplierCountryList = supplierCountryDAO.getAllSupplierCountries();
        supplierList = supplierDAO.getAllSuppliers();
        countryList = countryDAO.getAllCountries();
        departmentList = departmentDAO.getAllDepartments();

        // Set items to TableViews

        file_type_table_view.setItems(fileTypeList);
        matrial_table_view.setItems(materialList);
        supplier_country_table_view.setItems(supplierCountryList);
        supplier_table_view.setItems(supplierList);

        // Set ComboBox
        supplier_comb.setItems(SupplierDAO.getAllSuppliers());
        country_comb.setItems(CountryDAO.getAllCountries());
        department_comb.setItems(DepartmentDAO.getAllDepartments());

        // Call Tables Listener
        setupFileTypeTableListener();
        setupMatrialTableListener();
        setupSupplierCountryTableListener();
        setupSupplierTableListener();

        // new
        countryList = CountryDAO.getAllCountries();
        loadCountriesData();
        setupCountryTableListener();
        country_table_view.setItems(countryList);
        add_country_btn.setCursor(Cursor.HAND);
        update_country_btn.setCursor(Cursor.HAND);
        clear_country_btn.setCursor(Cursor.HAND);

    }

    // Load File Types Data
    private void loadFileTypesData() {
        file_type_name_colm.setCellValueFactory(new PropertyValueFactory<>("fileTypeName"));
        dept_name_in_file_type_colm.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        file_type_id_colm.setCellValueFactory(new PropertyValueFactory<>("fileTypeId"));
        file_type_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        dept_name_in_file_type_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        file_type_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        Callback<TableColumn<FileType, String>, TableCell<FileType, String>> cellFactory = param -> {
            final TableCell<FileType, String> cell = new TableCell<FileType, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final FontIcon deleteIcon = new FontIcon("fas-trash");
                        deleteIcon.setCursor(Cursor.HAND);
                        deleteIcon.setIconSize(13);
                        deleteIcon.setFill(javafx.scene.paint.Color.RED);
                        Tooltip.install(deleteIcon, new Tooltip("Delete File Type"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this file type?");
                            alert.setContentText("Delete file type confirmation");
                            alert.getButtonTypes().addAll(ButtonType.CANCEL);

                            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
                            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                            cancelButton.setText("Cancel");
                            okButton.setText("OK");
                            Platform.runLater(() -> cancelButton.requestFocus());
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
                                        try {
                                            FileType fileType = file_type_table_view.getSelectionModel().getSelectedItem();
                                            fileTypeDAO.deleteFileType(fileType.getFileTypeId());
                                            helpclearFileTyple();
                                            fileTypeList = fileTypeDAO.getAllFileTypes();
                                            file_type_table_view.setItems(fileTypeList);
                                            WindowUtils.ALERT("Success", "File Type deleted successfully", WindowUtils.ALERT_INFORMATION);
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "deleteFileType", ex);
                                        }
                                    } else {
                                        WindowUtils.ALERT("ERR", "Password not correct", WindowUtils.ALERT_WARNING);
                                    }
                                }
                            });
                        });

                        HBox manageBtn = new HBox(deleteIcon);
                        manageBtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        file_type_delete_colm.setCellFactory(cellFactory);
        file_type_table_view.setItems(fileTypeList);
    }

    // Load Matrials Data
    private void loadMatrialsData() {
        matrial_name_colm.setCellValueFactory(new PropertyValueFactory<>("matrialName"));
        matrial_id_colm.setCellValueFactory(new PropertyValueFactory<>("matrialId"));
        matrial_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        matrial_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        Callback<TableColumn<Matrial, String>, TableCell<Matrial, String>> cellFactory = param -> {
            final TableCell<Matrial, String> cell = new TableCell<Matrial, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final FontIcon deleteIcon = new FontIcon("fas-trash");
                        deleteIcon.setCursor(Cursor.HAND);
                        deleteIcon.setIconSize(13);
                        deleteIcon.setFill(javafx.scene.paint.Color.RED);
                        Tooltip.install(deleteIcon, new Tooltip("Delete Matrial"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this matrial?");
                            alert.setContentText("Delete matrial confirmation");
                            alert.getButtonTypes().addAll(ButtonType.CANCEL);

                            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
                            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                            cancelButton.setText("Cancel");
                            okButton.setText("OK");
                            Platform.runLater(() -> cancelButton.requestFocus());
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
                                        try {
                                            Matrial matrial = matrial_table_view.getSelectionModel().getSelectedItem();
                                            matrialDAO.deleteMatrial(matrial.getMatrialId());
                                            materialList = matrialDAO.getAllMatrials();
                                            matrial_table_view.setItems(materialList);
                                            WindowUtils.ALERT("Success", "Matrial deleted successfully", WindowUtils.ALERT_INFORMATION);
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "deleteMatrial", ex);
                                        }
                                    } else {
                                        WindowUtils.ALERT("ERR", "Password not correct", WindowUtils.ALERT_WARNING);
                                    }
                                }
                            });
                        });

                        HBox manageBtn = new HBox(deleteIcon);
                        manageBtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        matrial_delete_colm.setCellFactory(cellFactory);
        matrial_table_view.setItems(materialList);
    }

    // Load Supplier Countries Data
    private void loadSupplierCountriesData() {
        supplier_cou_name_colm.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        supplier_name_in_sup_coun_colm.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        supplier_cou_id_colm.setCellValueFactory(new PropertyValueFactory<>("supCountryId"));
        supplier_cou_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        supplier_name_in_sup_coun_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        supplier_cou_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        Callback<TableColumn<SupplierCountry, String>, TableCell<SupplierCountry, String>> cellFactory = param -> {
            final TableCell<SupplierCountry, String> cell = new TableCell<SupplierCountry, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final FontIcon deleteIcon = new FontIcon("fas-trash");
                        deleteIcon.setCursor(Cursor.HAND);
                        deleteIcon.setIconSize(13);
                        deleteIcon.setFill(javafx.scene.paint.Color.RED);
                        Tooltip.install(deleteIcon, new Tooltip("Delete Supplier Country"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this supplier country?");
                            alert.setContentText("Delete supplier country confirmation");
                            alert.getButtonTypes().addAll(ButtonType.CANCEL);

                            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
                            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                            cancelButton.setText("Cancel");
                            okButton.setText("OK");
                            Platform.runLater(() -> cancelButton.requestFocus());
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
                                        try {
                                            SupplierCountry supplierCountry = supplier_country_table_view.getSelectionModel().getSelectedItem();
                                            supplierCountryDAO.deleteSupplierCountry(supplierCountry.getSupCountryId());
                                            helpSupplierCountry();
                                            supplierCountryList = supplierCountryDAO.getAllSupplierCountries();
                                            supplier_country_table_view.setItems(supplierCountryList);
                                            WindowUtils.ALERT("Success", "Supplier Country deleted successfully", WindowUtils.ALERT_INFORMATION);
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "deleteSupplierCountry", ex);
                                        }
                                    } else {
                                        WindowUtils.ALERT("ERR", "Password not correct", WindowUtils.ALERT_WARNING);
                                    }
                                }
                            });
                        });

                        HBox manageBtn = new HBox(deleteIcon);
                        manageBtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        supplier_cou_delete_colm.setCellFactory(cellFactory);
        supplier_country_table_view.setItems(supplierCountryList);
    }

    // Load Suppliers Data
    private void loadSuppliersData() {
        supplier_name_colm.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        supplier_id_colm.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        supplier_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        supplier_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        Callback<TableColumn<Supplier, String>, TableCell<Supplier, String>> cellFactory = param -> {
            final TableCell<Supplier, String> cell = new TableCell<Supplier, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final FontIcon deleteIcon = new FontIcon("fas-trash");
                        deleteIcon.setCursor(Cursor.HAND);
                        deleteIcon.setIconSize(13);
                        deleteIcon.setFill(javafx.scene.paint.Color.RED);
                        Tooltip.install(deleteIcon, new Tooltip("Delete Supplier"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this supplier?");
                            alert.setContentText("Delete supplier confirmation");
                            alert.getButtonTypes().addAll(ButtonType.CANCEL);

                            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
                            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                            cancelButton.setText("Cancel");
                            okButton.setText("OK");
                            Platform.runLater(() -> cancelButton.requestFocus());
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
                                        try {
                                            Supplier supplier = supplier_table_view.getSelectionModel().getSelectedItem();
                                            supplierDAO.deleteSupplier(supplier.getSupplierId());
                                            supplierList = supplierDAO.getAllSuppliers();
                                            supplier_table_view.setItems(supplierList);
                                            WindowUtils.ALERT("Success", "Supplier deleted successfully", WindowUtils.ALERT_INFORMATION);
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "deleteSupplier", ex);
                                        }
                                    } else {
                                        WindowUtils.ALERT("ERR", "Password not correct", WindowUtils.ALERT_WARNING);
                                    }
                                }
                            });
                        });

                        HBox manageBtn = new HBox(deleteIcon);
                        manageBtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        supplier_delete_colm.setCellFactory(cellFactory);
        supplier_table_view.setItems(supplierList);
    }

    // Filter File Types
    @FXML
    void filter_file_type(KeyEvent event) {
        FilteredList<FileType> filteredData = new FilteredList<>(fileTypeList, p -> true);
        filter_file_type_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(fileType -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (fileType.getFileTypeName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String id = fileType.getFileTypeId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<FileType> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(file_type_table_view.comparatorProperty());
        file_type_table_view.setItems(sortedData);
    }

    // Filter Matrials
    @FXML
    void filter_matrial(KeyEvent event) {
        FilteredList<Matrial> filteredData = new FilteredList<>(materialList, p -> true);
        filter_matrial_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(matrial -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (matrial.getMatrialName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String id = matrial.getMatrialId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<Matrial> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(matrial_table_view.comparatorProperty());
        matrial_table_view.setItems(sortedData);
    }

    // Filter Supplier Countries
    @FXML
    void filter_supplier_country(KeyEvent event) {
        FilteredList<SupplierCountry> filteredData = new FilteredList<>(supplierCountryList, p -> true);
        filter_supplier_cou_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(supplierCountry -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (supplierCountry.getCountryName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String id = supplierCountry.getSupCountryId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<SupplierCountry> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(supplier_country_table_view.comparatorProperty());
        supplier_country_table_view.setItems(sortedData);
    }

    // Filter Suppliers
    @FXML
    void filter_supplier(KeyEvent event) {
        FilteredList<Supplier> filteredData = new FilteredList<>(supplierList, p -> true);
        filter_supplier_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(supplier -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (supplier.getSupplierName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String id = supplier.getSupplierId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<Supplier> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(supplier_table_view.comparatorProperty());
        supplier_table_view.setItems(sortedData);
    }

    // Add File Type
//    @FXML
//    void add_file_type(ActionEvent event) {
//        String fileTypeName = file_type_name_textF.getText().trim();
//        Department selectedDepartment = department_comb.getSelectionModel().getSelectedItem();
//        if (fileTypeName.isEmpty()) {
//            WindowUtils.ALERT("ERR", "file_type_name_empty", WindowUtils.ALERT_ERROR);
//            return;
//        }
//        if (selectedDepartment == null) {
//            WindowUtils.ALERT("ERR", "No Department selected", WindowUtils.ALERT_ERROR);
//            return;
//        }
//
//        FileType fileType = new FileType();
//        fileType.setFileTypeName(fileTypeName);
//        fileType.setDepartmentId(selectedDepartment.getDepartmentId());
//
//        boolean success = fileTypeDAO.insertFileType(fileType);
//
//        if (success) {
//            WindowUtils.ALERT("Success", "File Type added successfully", WindowUtils.ALERT_INFORMATION);
//            file_type_name_textF.clear();
//            update_file_type_name_textF.clear();
//            filter_file_type_textF.clear();
//            department_comb.getSelectionModel().clearSelection();
//            fileTypeList = fileTypeDAO.getAllFileTypes();
//            file_type_table_view.setItems(fileTypeList);
//        } else {
//            String err = MatrialDAO.lastErrorMessage;
//            if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
//                WindowUtils.ALERT("Duplicate", "File Type already exists", WindowUtils.ALERT_ERROR);
//            } else {
//                WindowUtils.ALERT("database_error", "file_type_add_failed", WindowUtils.ALERT_ERROR);
//            }
//        }
//    }
    @FXML
    void add_file_type(ActionEvent event) {
        try {
            // Get current user
            if (UserContext.getCurrentUser() == null) {
                WindowUtils.ALERT("Error", "No user logged in", WindowUtils.ALERT_ERROR);
                Logging.logMessage(Logging.WARN, getClass().getName(), "add_file_type", "Current user is null");

                return;
            }

            String fileTypeName = file_type_name_textF.getText().trim();
            Department selectedDepartment = null;

            // Determine department based on user role
            if (UserContext.getCurrentUser().getRole() == 4) {
                // Super Admin: can choose any department
                selectedDepartment = department_comb.getSelectionModel().getSelectedItem();
                department_comb.setMouseTransparent(false); // Allow interaction
                department_comb.setFocusTraversable(true);
            } else {
                // Non-Super Admin: auto-select their own department
                int deptId = UserContext.getCurrentUser().getDepartmentId();
                for (Department dept : department_comb.getItems()) {
                    if (dept.getDepartmentId() == deptId) {
                        selectedDepartment = dept;
                        department_comb.getSelectionModel().select(dept);
                        department_comb.setMouseTransparent(true); // Make read-only
                        department_comb.setFocusTraversable(false);
                        break;
                    }
                }
            }

            // Validate inputs
            if (fileTypeName.isEmpty()) {
                WindowUtils.ALERT("ERR", "file_type_name_empty", WindowUtils.ALERT_ERROR);
                return;
            }
            if (selectedDepartment == null) {
                WindowUtils.ALERT("ERR", "No Department selected or department not found", WindowUtils.ALERT_ERROR);
                return;
            }

            FileType fileType = new FileType();
            fileType.setFileTypeName(fileTypeName);
            fileType.setDepartmentId(selectedDepartment.getDepartmentId());

            boolean success = fileTypeDAO.insertFileType(fileType);

            if (success) {
                WindowUtils.ALERT("Success", "File Type added successfully", WindowUtils.ALERT_INFORMATION);
                file_type_name_textF.clear();
                update_file_type_name_textF.clear();
                filter_file_type_textF.clear();
                department_comb.getSelectionModel().clearSelection();
                fileTypeList = fileTypeDAO.getAllFileTypes();
                file_type_table_view.setItems(fileTypeList);
            } else {
                String err = MatrialDAO.lastErrorMessage;
                if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
                    WindowUtils.ALERT("Duplicate", "File Type already exists", WindowUtils.ALERT_ERROR);
                } else {
                    WindowUtils.ALERT("database_error", "file_type_add_failed", WindowUtils.ALERT_ERROR);
                }
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "add_file_type", ex);
            WindowUtils.ALERT("Exception", "An unexpected error occurred", WindowUtils.ALERT_ERROR);
        }
    }

    // Add Matrial
    @FXML
    void add_matrial(ActionEvent event) {
        String matrialName = matrial_name_textF.getText().trim();
        if (matrialName.isEmpty()) {
            WindowUtils.ALERT("ERR", "matrial_name_empty", WindowUtils.ALERT_ERROR);
            return;
        }

        Matrial matrial = new Matrial();
        matrial.setMatrialName(matrialName);

            boolean success = matrialDAO.insertMatrial(matrial);

            if (success) {
                WindowUtils.ALERT("Success", "Matrial added successfully", WindowUtils.ALERT_INFORMATION);
                matrial_name_textF.clear();
                update_matrial_name_textF.clear();
                filter_matrial_textF.clear();
                materialList = matrialDAO.getAllMatrials();
                matrial_table_view.setItems(materialList);
            }else {
                String err = MatrialDAO.lastErrorMessage;
                if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
                    WindowUtils.ALERT("Duplicate", "Matrial name already exists", WindowUtils.ALERT_ERROR);
                } else {
                    WindowUtils.ALERT("database_error", "matrial_name_add_failed", WindowUtils.ALERT_ERROR);
                }
            }

        }

    @FXML
    void add_supplier_country(ActionEvent event) {
        Supplier selectedSupplier = supplier_comb.getSelectionModel().getSelectedItem();
        Country selectedCountry = country_comb.getSelectionModel().getSelectedItem();

        if (selectedSupplier == null) {
            WindowUtils.ALERT("ERR", "No Supplier selected", WindowUtils.ALERT_ERROR);
            return;
        }
        if (selectedCountry == null) {
            WindowUtils.ALERT("ERR", "No Country selected", WindowUtils.ALERT_ERROR);
            return;
        }

        // Check if the supplier-country combination already exists
        boolean exists = supplierCountryList.stream()
                .anyMatch(sc -> sc.getSupplierId() == selectedSupplier.getSupplierId()
                        && sc.getCountryId() == selectedCountry.getCountryId());
        if (exists) {
            WindowUtils.ALERT("ERR", "This Supplier and Country combination already exists", WindowUtils.ALERT_ERROR);
            return;
        }

        SupplierCountry supplierCountry = new SupplierCountry();
        supplierCountry.setSupplierId(selectedSupplier.getSupplierId());
        supplierCountry.setCountryId(selectedCountry.getCountryId());

        boolean success = supplierCountryDAO.insertSupplierCountry(supplierCountry);

        if (success) {
            WindowUtils.ALERT("Success", "Supplier Country added successfully", WindowUtils.ALERT_INFORMATION);
            filter_supplier_cou_textF.clear();
            supplier_comb.getSelectionModel().clearSelection();
            country_comb.getSelectionModel().clearSelection();
            supplierCountryList = supplierCountryDAO.getAllSupplierCountries();
            supplier_country_table_view.setItems(supplierCountryList);
        } else {
            String err = SupplierCountryDAO.lastErrorMessage;
            if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
                WindowUtils.ALERT("Duplicate", "Supplier Country already exists", WindowUtils.ALERT_ERROR);
            } else {
                WindowUtils.ALERT("database_error", "supplier_country_add_failed", WindowUtils.ALERT_ERROR);
            }
        }
    }

    // Add Supplier
    @FXML
    void add_supplier(ActionEvent event) {
        String supplierName = supplier_name_textF.getText().trim();
        if (supplierName.isEmpty()) {
            WindowUtils.ALERT("ERR", "supplier_name_empty", WindowUtils.ALERT_ERROR);
            return;
        }

        Supplier supplier = new Supplier();
        supplier.setSupplierName(supplierName);

        boolean success = supplierDAO.insertSupplier(supplier);
        if (success) {
            WindowUtils.ALERT("Success", "Supplier added successfully", WindowUtils.ALERT_INFORMATION);
            supplier_name_textF.clear();
            update_supplier_name_textF.clear();
            filter_supplier_textF.clear();
            supplierList = supplierDAO.getAllSuppliers();
            supplier_table_view.setItems(supplierList);
            supplier_comb.setItems(SupplierDAO.getAllSuppliers());
        } else {
            String err = SupplierDAO.lastErrorMessage;
            if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
                WindowUtils.ALERT("Duplicate", "Supplier name already exists", WindowUtils.ALERT_ERROR);
            } else {
                WindowUtils.ALERT("database_error", "supplier_add_failed", WindowUtils.ALERT_ERROR);
            }
        }
    }


    void helpclearFileTyple(){
        filter_file_type_textF.clear();
        update_file_type_name_textF.clear();
        file_type_name_textF.clear();
        department_comb.getSelectionModel().clearSelection();
        department_comb.setValue(null);
    }

    // Clear File Type
    @FXML
    void clear_file_type(ActionEvent event) {
        helpclearFileTyple();
    }

    // Clear Matrial
    @FXML
    void clear_matrial(ActionEvent event) {
        filter_matrial_textF.clear();
        update_matrial_name_textF.clear();
        matrial_name_textF.clear();
    }

// Clear Supplier Country
    void helpSupplierCountry(){
        filter_supplier_cou_textF.clear();
        // Clear combo box completely
        supplier_comb.getSelectionModel().clearSelection();
        supplier_comb.setValue(null);
        country_comb.getSelectionModel().clearSelection();
        country_comb.setValue(null);
    }
    @FXML
    void clear_supplier_country(ActionEvent event) {
        helpSupplierCountry();
    }

    // Clear Supplier
    @FXML
    void clear_supplier(ActionEvent event) {
        filter_supplier_textF.clear();
        update_supplier_name_textF.clear();
        supplier_name_textF.clear();
    }



//    // Update File Type
//    @FXML
//    void update_file_type(ActionEvent event) {
//        try {
//            FileType selectedFileType = file_type_table_view.getSelectionModel().getSelectedItem();
//            if (selectedFileType == null) {
//                WindowUtils.ALERT("ERR", "No File Type selected", WindowUtils.ALERT_ERROR);
//                return;
//            }
//
//            String fileTypeName = update_file_type_name_textF.getText().trim();
//            Department selectedDepartment = department_comb.getSelectionModel().getSelectedItem();
//            if (fileTypeName.isEmpty()) {
//                WindowUtils.ALERT("ERR", "file_type_name_empty", WindowUtils.ALERT_ERROR);
//                return;
//            }
//            if (selectedDepartment == null) {
//                WindowUtils.ALERT("ERR", "No Department selected", WindowUtils.ALERT_ERROR);
//                return;
//            }
//
//            selectedFileType.setFileTypeName(fileTypeName);
//            selectedFileType.setDepartmentId(selectedDepartment.getDepartmentId());
//            boolean success = fileTypeDAO.updateFileType(selectedFileType);
//            if (success) {
//                WindowUtils.ALERT("Success", "File Type updated successfully", WindowUtils.ALERT_INFORMATION);
//                update_file_type_name_textF.clear();
//                file_type_name_textF.clear();
//                filter_file_type_textF.clear();
//                department_comb.getSelectionModel().clearSelection();
//                fileTypeList = fileTypeDAO.getAllFileTypes();
//                file_type_table_view.setItems(fileTypeList);
//            } else {
//                WindowUtils.ALERT("ERR", "file_type_updated_failed", WindowUtils.ALERT_ERROR);
//            }
//        } catch (Exception ex) {
//            Logging.logException("ERROR", getClass().getName(), "updateFileType", ex);
//        }
//    }
   @FXML
   void update_file_type(ActionEvent event) {
    try {
        // Get selected FileType
        FileType selectedFileType = file_type_table_view.getSelectionModel().getSelectedItem();
        if (selectedFileType == null) {
            WindowUtils.ALERT("ERR", "No File Type selected", WindowUtils.ALERT_ERROR);
            return;
        }

        String fileTypeName = update_file_type_name_textF.getText().trim();
        Department selectedDepartment = null;

        // Determine department based on user role
        if (UserContext.getCurrentUser().getRole() == 4) {
            // Super Admin: can choose any department
            selectedDepartment = department_comb.getSelectionModel().getSelectedItem();
            department_comb.setMouseTransparent(false); // Allow interaction
            department_comb.setFocusTraversable(true);
        } else {
            // Non-Super Admin: auto-select their own department
            int deptId = UserContext.getCurrentUser().getDepartmentId();
            for (Department dept : department_comb.getItems()) {
                if (dept.getDepartmentId() == deptId) {
                    selectedDepartment = dept;
                    department_comb.getSelectionModel().select(dept);
                    department_comb.setMouseTransparent(true); // Make read-only
                    department_comb.setFocusTraversable(false);
                    break;
                }
            }
        }

        // Validate inputs
        if (fileTypeName.isEmpty()) {
            WindowUtils.ALERT("ERR", "file_type_name_empty", WindowUtils.ALERT_ERROR);
            return;
        }
        if (selectedDepartment == null) {
            WindowUtils.ALERT("ERR", "No Department selected or department not found", WindowUtils.ALERT_ERROR);
            return;
        }

        // Update FileType object
        selectedFileType.setFileTypeName(fileTypeName);
        selectedFileType.setDepartmentId(selectedDepartment.getDepartmentId());
        boolean success = fileTypeDAO.updateFileType(selectedFileType);

        if (success) {
            WindowUtils.ALERT("Success", "File Type updated successfully", WindowUtils.ALERT_INFORMATION);
            update_file_type_name_textF.clear();
            file_type_name_textF.clear();
            filter_file_type_textF.clear();
            department_comb.getSelectionModel().clearSelection();
            fileTypeList = fileTypeDAO.getAllFileTypes();
            file_type_table_view.setItems(fileTypeList);
        } else {
            String err = fileTypeDAO.lastErrorMessage; // Use fileTypeDAO instead of MatrialDAO
            if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
                WindowUtils.ALERT("Duplicate", "File Type already exists", WindowUtils.ALERT_ERROR);
            } else {
                WindowUtils.ALERT("ERR", "file_type_updated_failed", WindowUtils.ALERT_ERROR);
            }
        }
    } catch (Exception ex) {
        Logging.logException("ERROR", this.getClass().getName(), "update_file_type", ex);
        WindowUtils.ALERT("Exception", "An unexpected error occurred", WindowUtils.ALERT_ERROR);
    }
}

    // Update Matrial
    @FXML
    void update_matrial(ActionEvent event) {
        try {
            Matrial selectedMatrial = matrial_table_view.getSelectionModel().getSelectedItem();
            if (selectedMatrial == null) {
                WindowUtils.ALERT("ERR", "No Matrial selected", WindowUtils.ALERT_ERROR);
                return;
            }

            String matrialName = update_matrial_name_textF.getText().trim();
            if (matrialName.isEmpty()) {
                WindowUtils.ALERT("ERR", "matrial_name_empty", WindowUtils.ALERT_ERROR);
                return;
            }

            selectedMatrial.setMatrialName(matrialName);
            boolean success = matrialDAO.updateMatrial(selectedMatrial);
            if (success) {
                WindowUtils.ALERT("Success", "Matrial updated successfully", WindowUtils.ALERT_INFORMATION);
                update_matrial_name_textF.clear();
                matrial_name_textF.clear();
                filter_matrial_textF.clear();
                materialList = matrialDAO.getAllMatrials();
                matrial_table_view.setItems(materialList);
            } else {
                WindowUtils.ALERT("ERR", "matrial_updated_failed", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "updateMatrial", ex);
        }
    }

    // Update Supplier Country
    @FXML
    void update_supplier_country(ActionEvent event) {
        try {
            SupplierCountry selectedSupplierCountry = supplier_country_table_view.getSelectionModel().getSelectedItem();
            if (selectedSupplierCountry == null) {
                WindowUtils.ALERT("ERR", "No Supplier Country selected", WindowUtils.ALERT_ERROR);
                return;
            }


            Supplier selectedSupplier = supplier_comb.getSelectionModel().getSelectedItem();
            Country selectedCountry = country_comb.getSelectionModel().getSelectedItem();

            if (selectedSupplier == null) {
                WindowUtils.ALERT("ERR", "No Supplier selected", WindowUtils.ALERT_ERROR);
                return;
            }
            if (selectedCountry == null) {
                WindowUtils.ALERT("ERR", "No Coubtry selected", WindowUtils.ALERT_ERROR);
                return;
            }
            // Check if the supplier-country combination already exists
            if (supplierCountryDAO.existsSupplierCountry(selectedSupplier.getSupplierId(), selectedCountry.getCountryId()) &&
                    !(selectedSupplierCountry.getSupplierId() == selectedSupplier.getSupplierId() &&
                            selectedSupplierCountry.getCountryId() == selectedCountry.getCountryId())) {
                WindowUtils.ALERT("ERR", "This Supplier and Country combination already exists", WindowUtils.ALERT_ERROR);
                return;
            }

            selectedSupplierCountry.setSupplierId(selectedSupplier.getSupplierId());
            selectedSupplierCountry.setCountryId(selectedCountry.getCountryId());
            boolean success = supplierCountryDAO.updateSupplierCountry(selectedSupplierCountry);
            if (success) {
                WindowUtils.ALERT("Success", "Supplier Country updated successfully", WindowUtils.ALERT_INFORMATION);
                filter_supplier_cou_textF.clear();
                supplier_comb.getSelectionModel().clearSelection();
                country_comb.getSelectionModel().clearSelection();
                supplierCountryList = supplierCountryDAO.getAllSupplierCountries();
                supplier_country_table_view.setItems(supplierCountryList);
            } else {
                WindowUtils.ALERT("ERR", "supplier_country_updated_failed", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "updateSupplierCountry", ex);
        }
    }

    // Update Supplier
    @FXML
    void update_supplier(ActionEvent event) {
        try {
            Supplier selectedSupplier = supplier_table_view.getSelectionModel().getSelectedItem();
            if (selectedSupplier == null) {
                WindowUtils.ALERT("ERR", "No Supplier selected", WindowUtils.ALERT_ERROR);
                return;
            }

            String supplierName = update_supplier_name_textF.getText().trim();
            if (supplierName.isEmpty()) {
                WindowUtils.ALERT("ERR", "supplier_name_empty", WindowUtils.ALERT_ERROR);
                return;
            }

            selectedSupplier.setSupplierName(supplierName);
            boolean success = supplierDAO.updateSupplier(selectedSupplier);
            if (success) {
                WindowUtils.ALERT("Success", "Supplier updated successfully", WindowUtils.ALERT_INFORMATION);
                update_supplier_name_textF.clear();
                supplier_name_textF.clear();
                filter_supplier_textF.clear();
                supplierList = supplierDAO.getAllSuppliers();
                supplier_table_view.setItems(supplierList);
            } else {
                WindowUtils.ALERT("ERR", "supplier_updated_failed", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "updateSupplier", ex);
        }
    }



//    // Setup File Type Table Listener
//    private void setupFileTypeTableListener() {
//        file_type_table_view.setOnMouseClicked(event -> {
//            FileType selectedFileType = file_type_table_view.getSelectionModel().getSelectedItem();
//            if (selectedFileType != null) {
//                update_file_type_name_textF.setText(selectedFileType.getFileTypeName());
//                department_comb.getSelectionModel().select(
//                        departmentList.stream()
//                                .filter(d -> d.getDepartmentId() == selectedFileType.getDepartmentId())
//                                .findFirst()
//                                .orElse(null)
//                );
//            }
//        });
//    }
private void setupFileTypeTableListener() {
    try {
        // Ensure current user is available
        if (UserContext.getCurrentUser() == null) {
            Logging.logMessage(Logging.WARN, getClass().getName(), "setupFileTypeTableListener", "Current user is null");

            return;
        }

        // Set row factory to control row selection
        file_type_table_view.setRowFactory(tv -> {
            TableRow<FileType> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    FileType selectedFileType = row.getItem();
                    int userRole = UserContext.getCurrentUser().getRole();
                    int userDeptId = UserContext.getCurrentUser().getDepartmentId();

                    // Allow selection only for Super Admin or matching DepartmentId
                    if (userRole == 4 || selectedFileType.getDepartmentId() == userDeptId) {
                        file_type_table_view.getSelectionModel().select(selectedFileType);
                        update_file_type_name_textF.setText(selectedFileType.getFileTypeName());
                        department_comb.getSelectionModel().select(
                                departmentList.stream()
                                        .filter(d -> d.getDepartmentId() == selectedFileType.getDepartmentId())
                                        .findFirst()
                                        .orElse(null)
                        );
                    } else {
                        // Prevent selection for non-matching departments
                        event.consume(); // Ignore the click event
                    }
                }
            });

            // Disable rows for non-matching departments (optional visual feedback)
            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null && UserContext.getCurrentUser().getRole() != 4) {
                    int userDeptId = UserContext.getCurrentUser().getDepartmentId();
                    if (newItem.getDepartmentId() != userDeptId) {
                        row.setDisable(true); // Visually disable non-matching rows
                        row.setStyle("-fx-opacity: 0.5;"); // Optional: reduce opacity for clarity
                    } else {
                        row.setDisable(false);
                        row.setStyle(""); // Reset style for matching rows
                    }
                } else {
                    row.setDisable(false);
                    row.setStyle(""); // Reset style for Super Admin
                }
            });

            return row;
        });
    } catch (Exception ex) {
        Logging.logException("ERROR", this.getClass().getName(), "setupFileTypeTableListener", ex);
    }
}


    // Setup Matrial Table Listener
    private void setupMatrialTableListener() {
        matrial_table_view.setOnMouseClicked(event -> {
            Matrial selectedMatrial = matrial_table_view.getSelectionModel().getSelectedItem();
            if (selectedMatrial != null) {
                update_matrial_name_textF.setText(selectedMatrial.getMatrialName());
            }
        });
    }


    // Setup Supplier Country Table Listener
    private void setupSupplierCountryTableListener() {
        supplier_country_table_view.setOnMouseClicked(event -> {
            SupplierCountry selectedSupplierCountry = supplier_country_table_view.getSelectionModel().getSelectedItem();
            if (selectedSupplierCountry != null) {
                supplier_comb.getSelectionModel().select(
                        supplierList.stream()
                                .filter(s -> s.getSupplierId() == selectedSupplierCountry.getSupplierId())
                                .findFirst()
                                .orElse(null)
                );
            }
            if (selectedSupplierCountry != null) {
                country_comb.getSelectionModel().select(
                        countryList.stream()
                                .filter(s -> s.getCountryId() == selectedSupplierCountry.getCountryId())
                                .findFirst()
                                .orElse(null)
                );
            }
        });
    }

    // Setup Supplier Table Listener
    private void setupSupplierTableListener() {
        supplier_table_view.setOnMouseClicked(event -> {
            Supplier selectedSupplier = supplier_table_view.getSelectionModel().getSelectedItem();
            if (selectedSupplier != null) {
                update_supplier_name_textF.setText(selectedSupplier.getSupplierName());
            }
        });
    }
    @FXML
    void openDepartment_Section(ActionEvent event) {
        CLOSE(event);
        OPEN_DEPARTMENT_SECTION_PAGE();
    }

    // new

    // Load Countries Data
    private void loadCountriesData() {
        country_name_colm.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        country_id_colm.setCellValueFactory(new PropertyValueFactory<>("countryId"));
        country_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        country_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        Callback<TableColumn<Country, String>, TableCell<Country, String>> cellFactory = param -> {
            final TableCell<Country, String> cell = new TableCell<Country, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final FontIcon deleteIcon = new FontIcon("fas-trash");
                        deleteIcon.setCursor(Cursor.HAND);
                        deleteIcon.setIconSize(13);
                        deleteIcon.setFill(javafx.scene.paint.Color.RED);
                        Tooltip.install(deleteIcon, new Tooltip("Delete Country"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this country?");
                            alert.setContentText("Delete country confirmation");
                            alert.getButtonTypes().addAll(ButtonType.CANCEL);

                            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
                            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                            cancelButton.setText("Cancel");
                            okButton.setText("OK");
                            Platform.runLater(() -> cancelButton.requestFocus());
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
                                        try {
                                            Country country = country_table_view.getSelectionModel().getSelectedItem();
                                            CountryDAO.deleteCountry(country.getCountryId());
                                            country_name_textF.clear();
                                            filter_country_textF.clear();
                                            update_country_name_textF.clear();
                                            countryList = CountryDAO.getAllCountries();
                                            country_table_view.setItems(countryList);
                                            WindowUtils.ALERT("Success", "Country deleted successfully", WindowUtils.ALERT_INFORMATION);
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "deleteCountry", ex);
                                        }
                                    } else {
                                        WindowUtils.ALERT("ERR", "Password not correct", WindowUtils.ALERT_WARNING);
                                    }
                                }
                            });
                        });

                        HBox manageBtn = new HBox(deleteIcon);
                        manageBtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        country_delete_colm.setCellFactory(cellFactory);
        country_table_view.setItems(countryList);

    }

    // Add Country
    @FXML
    void add_country(ActionEvent event) {
        String countryName = country_name_textF.getText().trim();
        if (countryName.isEmpty()) {
            WindowUtils.ALERT("ERR", "country_name_empty", WindowUtils.ALERT_ERROR);
            return;
        }

        Country country = new Country();
        country.setCountryName(countryName);

        boolean success = CountryDAO.insertCountry(country);

        if (success) {
            WindowUtils.ALERT("Success", "Country added successfully", WindowUtils.ALERT_INFORMATION);
            country_name_textF.clear();
            update_country_name_textF.clear();
            filter_country_textF.clear();
            countryList = CountryDAO.getAllCountries();
            country_table_view.setItems(countryList);
            country_comb.setItems(CountryDAO.getAllCountries());

        } else {
            WindowUtils.ALERT("database_error", "country_add_failed", WindowUtils.ALERT_ERROR);
        }
    }
    // Update Country
    @FXML
    void update_country(ActionEvent event) {
        try {
            Country selectedCountry = country_table_view.getSelectionModel().getSelectedItem();
            if (selectedCountry == null) {
                WindowUtils.ALERT("ERR", "No Country selected", WindowUtils.ALERT_ERROR);
                return;
            }

            String countryName = update_country_name_textF.getText().trim();
            if (countryName.isEmpty()) {
                WindowUtils.ALERT("ERR", "country_name_empty", WindowUtils.ALERT_ERROR);
                return;
            }

            selectedCountry.setCountryName(countryName);
            boolean success = CountryDAO.updateCountry(selectedCountry);
            if (success) {
                WindowUtils.ALERT("Success", "Country updated successfully", WindowUtils.ALERT_INFORMATION);
                update_country_name_textF.clear();
                country_name_textF.clear();
                filter_country_textF.clear();
                countryList = CountryDAO.getAllCountries();
                country_table_view.setItems(countryList);
            } else {
                WindowUtils.ALERT("ERR", "country_updated_failed", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "updateCountry", ex);
        }
    }

    // Clear Country
    @FXML
    void clear_country(ActionEvent event) {
        filter_country_textF.clear();
        update_country_name_textF.clear();
        country_name_textF.clear();
    }

    // Filter Countries
    @FXML
    void filter_country(KeyEvent event) {
        FilteredList<Country> filteredData = new FilteredList<>(countryList, p -> true);
        filter_country_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(country -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (country.getCountryName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String id = country.getCountryId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<Country> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(country_table_view.comparatorProperty());
        country_table_view.setItems(sortedData);
    }

    // Setup Country Table Listener
    private void setupCountryTableListener() {
        country_table_view.setOnMouseClicked(event -> {
            Country selectedCountry = country_table_view.getSelectionModel().getSelectedItem();
            if (selectedCountry != null) {
                update_country_name_textF.setText(selectedCountry.getCountryName());
            }
        });
    }

}
