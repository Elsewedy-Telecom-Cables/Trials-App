package com.etc.trials.controller;

import com.etc.trials.dao.AppContext;
import com.etc.trials.dao.DepartmentDao;
import com.etc.trials.dao.SectionDao;
import com.etc.trials.logging.Logging;
import com.etc.trials.model.Department;
import com.etc.trials.model.Section;
import com.etc.trials.model.UserContext;
import com.etc.trials.service.UserService;
import com.etc.trials.service.WindowUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;

public class DepartmentSectionController implements Initializable {

    @FXML private Button add_department_btn;
    @FXML private Button add_section_btn;
    @FXML private Button clear_department_btn;
    @FXML
    private Button clear_section_btn;
    @FXML private TableColumn<Department, String> department_delete_colm;

    @FXML private TableColumn<Department, String> department_id_colm;

    @FXML private TableColumn<Department, String> department_name_colm;

    @FXML private TextField department_name_textF;

    @FXML private TableView<Department> department_table_view;
    @FXML private TextField filter_department_textF;
    @FXML private TextField filter_section_textF;
    @FXML private TableColumn<Section, String> section_delete_colm;

    @FXML private TableColumn<Section, String> section_id_colm;

    @FXML private TableColumn<Section, String> section_name_colm;

    @FXML private TextField section_name_textF;


    @FXML private TableView<Section> section_table_view;
    @FXML private Button update_department_btn;

    @FXML private TextField update_department_name_textF;
    @FXML private Button update_section_btn;

    @FXML private TextField update_section_name_textF;
    ObservableList<Department> departmentList;
    ObservableList<Section> sectionList;
    private final DepartmentDao departmentDAO = AppContext.getInstance().getDepartmentDao();
    private final SectionDao sectionDAO = AppContext.getInstance().getSectionDao();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //  // Set cursor to hand for all buttons
        add_section_btn.setCursor(Cursor.HAND);
        clear_department_btn.setCursor(Cursor.HAND);
        clear_section_btn.setCursor(Cursor.HAND);
        update_department_btn.setCursor(Cursor.HAND);
        update_section_btn.setCursor(Cursor.HAND);
        add_department_btn.setCursor(Cursor.HAND);
        // Load Data For All Tables
        loadDepartmentsData();
        loadSectionsData();
        // Call Tables Listener
        setupDepartmentTableListener();
        setupSectionTableListener();

        // Initialize ObservableLists
        departmentList = departmentDAO.getAllDepartments();
        sectionList = sectionDAO.getAllSections();
        // Set items to TableViews
        department_table_view.setItems(departmentList);
        section_table_view.setItems(sectionList);


    }

    // Load Departments Data
    private void loadDepartmentsData() {
        department_name_colm.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        department_id_colm.setCellValueFactory(new PropertyValueFactory<>("departmentId"));
        department_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        department_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        Callback<TableColumn<Department, String>, TableCell<Department, String>> cellFactory = param -> {
            final TableCell<Department, String> cell = new TableCell<Department, String>() {
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
                        Tooltip.install(deleteIcon, new Tooltip("Delete Department"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this department?");
                            alert.setContentText("Delete department confirmation");
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
                                            Department department = department_table_view.getSelectionModel().getSelectedItem();
                                            departmentDAO.deleteDepartment(department.getDepartmentId());
                                            departmentList = departmentDAO.getAllDepartments();
                                            department_table_view.setItems(departmentList);
                                            WindowUtils.ALERT("Success", "Department deleted successfully", WindowUtils.ALERT_INFORMATION);
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "deleteDepartment", ex);
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
        department_delete_colm.setCellFactory(cellFactory);
        department_table_view.setItems(departmentList);
    }
    // Load Sections Data
    private void loadSectionsData() {
        section_name_colm.setCellValueFactory(new PropertyValueFactory<>("sectionName"));
        section_id_colm.setCellValueFactory(new PropertyValueFactory<>("sectionId"));
        section_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        section_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        Callback<TableColumn<Section, String>, TableCell<Section, String>> cellFactory = param -> {
            final TableCell<Section, String> cell = new TableCell<Section, String>() {
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
                        Tooltip.install(deleteIcon, new Tooltip("Delete Section"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this section?");
                            alert.setContentText("Delete section confirmation");
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
                                            Section section = section_table_view.getSelectionModel().getSelectedItem();
                                            sectionDAO.deleteSection(section.getSectionId());
                                            sectionList = sectionDAO.getAllSections();
                                            section_table_view.setItems(sectionList);
                                            WindowUtils.ALERT("Success", "Section deleted successfully", WindowUtils.ALERT_INFORMATION);
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "deleteSection", ex);
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
        section_delete_colm.setCellFactory(cellFactory);
        section_table_view.setItems(sectionList);
    }
    // Filter Departments
    @FXML
    void filter_department(KeyEvent event) {
        FilteredList<Department> filteredData = new FilteredList<>(departmentList, p -> true);
        filter_department_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(department -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (department.getDepartmentName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String id = department.getDepartmentId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<Department> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(department_table_view.comparatorProperty());
        department_table_view.setItems(sortedData);
    }
    // Filter Sections
    @FXML
    void filter_section(KeyEvent event) {
        FilteredList<Section> filteredData = new FilteredList<>(sectionList, p -> true);
        filter_section_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(section -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (section.getSectionName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String id = section.getSectionId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<Section> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(section_table_view.comparatorProperty());
        section_table_view.setItems(sortedData);
    }
    // Add Section
    @FXML
    void add_section(ActionEvent event) {
        String sectionName = section_name_textF.getText().trim();
        if (sectionName.isEmpty()) {
            WindowUtils.ALERT("ERR", "section_name_empty", WindowUtils.ALERT_ERROR);
            return;
        }

        Section section = new Section();
        section.setSectionName(sectionName);

        boolean success = sectionDAO.insertSection(section);

        if (success) {
            WindowUtils.ALERT("Success", "Section added successfully", WindowUtils.ALERT_INFORMATION);
            section_name_textF.clear();
            update_section_name_textF.clear();
            filter_section_textF.clear();
            sectionList = sectionDAO.getAllSections();
            section_table_view.setItems(sectionList);
        } else {
            String err = SectionDao.lastErrorMessage;
            if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
                WindowUtils.ALERT("Duplicate", "Section name already exists", WindowUtils.ALERT_ERROR);
            } else {
                WindowUtils.ALERT("database_error", "section_name_add_failed", WindowUtils.ALERT_ERROR);
            }
        }

    }
    @FXML
    void add_department(ActionEvent event) {
        String departmentName = department_name_textF.getText().trim();
        if (departmentName.isEmpty()) {
            WindowUtils.ALERT("ERR", "department_name_empty", WindowUtils.ALERT_ERROR);
            return;
        }

        Department department = new Department();
        department.setDepartmentName(departmentName);

        boolean success = departmentDAO.insertDepartment(department);
        if (success) {
            WindowUtils.ALERT("Success", "Department added successfully", WindowUtils.ALERT_INFORMATION);
            department_name_textF.clear();
            update_department_name_textF.clear();
            filter_department_textF.clear();
            departmentList = departmentDAO.getAllDepartments();
            department_table_view.setItems(departmentList);
        } else {
            String err = DepartmentDao.lastErrorMessage;
            if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
                WindowUtils.ALERT("Duplicate", "Department name already exists", WindowUtils.ALERT_ERROR);
            } else {
                WindowUtils.ALERT("database_error", "department_add_failed", WindowUtils.ALERT_ERROR);
            }
        }
    }


    // Clear Department
    @FXML
    void clear_department(ActionEvent event) {
        filter_department_textF.clear();
        update_department_name_textF.clear();
        department_name_textF.clear();
    }
    // Clear Section
    @FXML
    void clear_section(ActionEvent event) {
        filter_section_textF.clear();
        update_section_name_textF.clear();
        section_name_textF.clear();
    }
    // Update Department
    @FXML
    void update_department(ActionEvent event) {
        try {
            Department selectedDepartment = department_table_view.getSelectionModel().getSelectedItem();
            if (selectedDepartment == null) {
                WindowUtils.ALERT("ERR", "No Department selected", WindowUtils.ALERT_ERROR);
                return;
            }

            String departmentName = update_department_name_textF.getText().trim();
            if (departmentName.isEmpty()) {
                WindowUtils.ALERT("ERR", "department_name_empty", WindowUtils.ALERT_ERROR);
                return;
            }

            selectedDepartment.setDepartmentName(departmentName);
            boolean success = departmentDAO.updateDepartment(selectedDepartment);
            if (success) {
                WindowUtils.ALERT("Success", "Department updated successfully", WindowUtils.ALERT_INFORMATION);
                update_department_name_textF.clear();
                department_name_textF.clear();
                filter_department_textF.clear();

                departmentList = departmentDAO.getAllDepartments();
                department_table_view.setItems(departmentList);
            } else {
                WindowUtils.ALERT("ERR", "department_updated_failed", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "updateDepartment", ex);
        }
    }

    // Update Section
    @FXML
    void update_section(ActionEvent event) {
        try {
            Section selectedSection = section_table_view.getSelectionModel().getSelectedItem();
            if (selectedSection == null) {
                WindowUtils.ALERT("ERR", "No Section selected", WindowUtils.ALERT_ERROR);
                return;
            }

            String sectionName = update_section_name_textF.getText().trim();
            if (sectionName.isEmpty()) {
                WindowUtils.ALERT("ERR", "section_name_empty", WindowUtils.ALERT_ERROR);
                return;
            }

            selectedSection.setSectionName(sectionName);
            boolean success = sectionDAO.updateSection(selectedSection);
            if (success) {
                WindowUtils.ALERT("Success", "Section updated successfully", WindowUtils.ALERT_INFORMATION);
                update_section_name_textF.clear();
                section_name_textF.clear();
                filter_section_textF.clear();

                sectionList = sectionDAO.getAllSections();
                section_table_view.setItems(sectionList);
            } else {
                WindowUtils.ALERT("ERR", "section_updated_failed", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "updateSection", ex);
        }
    }
    // Setup Department Table Listener
    private void setupDepartmentTableListener() {
        department_table_view.setOnMouseClicked(event -> {
            Department selectedDepartment = department_table_view.getSelectionModel().getSelectedItem();
            if (selectedDepartment != null) {
                update_department_name_textF.setText(selectedDepartment.getDepartmentName());
            }
        });
    }

    // Setup Section Table Listener
    private void setupSectionTableListener() {
        section_table_view.setOnMouseClicked(event -> {
            Section selectedSection = section_table_view.getSelectionModel().getSelectedItem();
            if (selectedSection != null) {
                update_section_name_textF.setText(selectedSection.getSectionName());
            }
        });
    }

}
