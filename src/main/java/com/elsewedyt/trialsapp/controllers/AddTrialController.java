package com.elsewedyt.trialsapp.controllers;

import com.elsewedyt.trialsapp.dao.*;
import com.elsewedyt.trialsapp.models.*;
import com.elsewedyt.trialsapp.services.WindowUtils;
import com.elsewedyt.trialsapp.logging.Logging;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddTrialController implements Initializable {

    @FXML
    private Button clear_btn;

    @FXML
    private DatePicker creationDate_datePiker;

    @FXML
    private Label header_lbl;

    @FXML
    private ComboBox<Matrial> matrial_combo;

    @FXML
    private TextArea notes_textArea;

    @FXML
    private Button save_btn;

    @FXML
    private ComboBox<Section> section_combo;

    @FXML
    private ComboBox<Supplier> supplier_combo;

    @FXML
    private ComboBox<SupplierCountry> supplier_country_combo;

    @FXML
    private TextArea trial_purpose_textArea;

    private Stage stage;
    private int CURRENT_TRIAL_ID = 0;
    private boolean update = false;

    // Set the stage for the controller
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Close the current window
    public void closeWindow() {
        if (stage != null) {
            stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set focus to header label on initialization
        Platform.runLater(() -> header_lbl.requestFocus());

        // Set cursor to hand for buttons
        clear_btn.setCursor(Cursor.HAND);
        save_btn.setCursor(Cursor.HAND);

        // Populate ComboBoxes with data
        section_combo.setItems(SectionDAO.getAllSections());
        matrial_combo.setItems(MatrialDAO.getAllMatrials());
        supplier_combo.setItems(SupplierDAO.getAllSuppliers());
        supplier_country_combo.setItems(FXCollections.observableArrayList());

        // Add listener to supplier_combo to update supplier_country_combo when a supplier is selected
        supplier_combo.getSelectionModel().selectedItemProperty().addListener((obs, oldSupplier, newSupplier) -> {
            updateSupplierCountries(newSupplier);
        });
    }

    // Update supplier_country_combo with countries associated with the selected supplier
    private void updateSupplierCountries(Supplier supplier) {
        ObservableList<SupplierCountry> filteredCountries = FXCollections.observableArrayList();
        if (supplier != null) {
            // Get all countries associated with the selected supplier
            filteredCountries = SupplierCountryDAO.getSupplierCountriesBySupplierId(supplier.getSupplierId());
        }
        supplier_country_combo.setItems(filteredCountries);
        supplier_country_combo.getSelectionModel().clearSelection();
        if (!filteredCountries.isEmpty()) {
            supplier_country_combo.getSelectionModel().selectFirst();
        }
    }

    // Clear all input fields
    @FXML
    void clear(ActionEvent event) {
        clearHelp();
    }

    // Helper method to clear all form fields
    private void clearHelp() {
        notes_textArea.clear();
        trial_purpose_textArea.clear();
        creationDate_datePiker.setValue(null);
        section_combo.getSelectionModel().clearSelection();
        section_combo.setValue(null);
        matrial_combo.getSelectionModel().clearSelection();
        matrial_combo.setValue(null);
        supplier_combo.getSelectionModel().clearSelection();
        supplier_combo.setValue(null);
        supplier_country_combo.getSelectionModel().clearSelection();
        supplier_country_combo.setValue(null);
    }

    // Save or update trial based on update flag
    @FXML
    void saveTrial(ActionEvent event) {
        try {
            if (!update) {
                addTrial();
            } else {
                boolean success = updateTrial();
                if (success) {
                    closeWindow();
                }
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "saveTrial", ex);
            WindowUtils.ALERT("Error", "Exception during saving.", WindowUtils.ALERT_ERROR);
        }
    }


    // Add a new trial
    private void addTrial() {
        try {
            // Get and validate required fields
            String notes = notes_textArea.getText();
            String trialPurpose = trial_purpose_textArea.getText();

            LocalDate selectedDate = creationDate_datePiker.getValue();
            LocalDateTime creationDate;

            if (selectedDate != null) {
                if (selectedDate.equals(LocalDate.now())) {
                    creationDate = LocalDateTime.now(); // سجل الوقت الحالي إذا التاريخ هو اليوم
                } else {
                    creationDate = selectedDate.atStartOfDay(); // تاريخ مختلف: الساعة 00:00
                }
            } else {
                creationDate = LocalDateTime.now(); // لم يتم اختيار أي تاريخ
            }



            Section selectedSection = section_combo.getValue();
            Matrial selectedMatrial = matrial_combo.getValue();
            Supplier selectedSupplier = supplier_combo.getValue();
            SupplierCountry selectedSupplierCountry = supplier_country_combo.getValue();

            // Prepare validation error list
            List<String> validationErrors = new ArrayList<>();

            if (selectedSection == null) validationErrors.add("Select a section.");
            if (selectedMatrial == null) validationErrors.add("Select a material.");
            if (selectedSupplier == null) validationErrors.add("Select a supplier.");
            if (selectedSupplierCountry == null) validationErrors.add("Select a supplier country.");
            if (trialPurpose == null || trialPurpose.trim().isEmpty()) validationErrors.add("Trial purpose is required.");

            if (!validationErrors.isEmpty()) {
                WindowUtils.ALERT("Validation Error", String.join("\n", validationErrors), WindowUtils.ALERT_WARNING);
                return;
            }

            // Build Trial object
            Trial trial = new Trial();
            trial.setCreationDate(creationDate);
            trial.setNotes(notes);
            trial.setTrialPurpose(trialPurpose);
            trial.setSectionId(selectedSection.getSectionId());
            trial.setMatrialId(selectedMatrial.getMatrialId());
            trial.setSupplierId(selectedSupplier.getSupplierId());
            trial.setSupCountryId(selectedSupplierCountry.getSupCountryId());
            trial.setUserId(UserContext.getCurrentUser().getUserId());

            boolean success = TrialDAO.insertTrial(trial);
            if (success) {
                WindowUtils.ALERT("Success", "Trial saved successfully.", WindowUtils.ALERT_INFORMATION);
                clearHelp();
                // New Way
                TrialsController trialsController = TrialsController.getInstance();
                if (trialsController != null) {
                    Platform.runLater(() -> {
                        trialsController.refreshTable();
                    //    System.out.println("Table refresh triggered successfully");
                    });
                } else {
                    System.out.println("TrialsController instance is null!");
                }

                closeWindow(); // Close the add trial window

            } else {
                WindowUtils.ALERT("Error", "Failed to save trial.", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "addTrial", ex);
            WindowUtils.ALERT("Error", "Exception during saving.", WindowUtils.ALERT_ERROR);
        }
    }


    // Update an existing trial
    private boolean updateTrial() {
        try {
            String notes = notes_textArea.getText();
            String trialPurpose = trial_purpose_textArea.getText();
            LocalDate selectedDate = creationDate_datePiker.getValue();
            LocalDateTime creationDate;

            if (selectedDate != null) {
                if (selectedDate.equals(LocalDate.now())) {
                    creationDate = LocalDateTime.now(); // تاريخ اليوم → وقت فعلي
                } else {
                    creationDate = selectedDate.atStartOfDay(); // تاريخ مختلف → 12:00 AM
                }
            } else {
                creationDate = LocalDateTime.now(); // لم يتم اختيار تاريخ
            }
            Section selectedSection = section_combo.getValue();
            Matrial selectedMatrial = matrial_combo.getValue();
            Supplier selectedSupplier = supplier_combo.getValue();
            SupplierCountry selectedSupplierCountry = supplier_country_combo.getValue();

            List<String> validationErrors = new ArrayList<>();

            if (selectedSection == null) validationErrors.add("Select a section.");
            if (selectedMatrial == null) validationErrors.add("Select a material.");
            if (selectedSupplier == null) validationErrors.add("Select a supplier.");
            if (selectedSupplierCountry == null) validationErrors.add("Select a supplier country.");
            if (trialPurpose == null || trialPurpose.trim().isEmpty()) validationErrors.add("Trial purpose is required.");

            if (!validationErrors.isEmpty()) {
                WindowUtils.ALERT("Validation Error", String.join("\n", validationErrors), WindowUtils.ALERT_WARNING);
                return false;
            }

            Trial trial = new Trial();
            trial.setTrialId(CURRENT_TRIAL_ID);
            trial.setCreationDate(creationDate);
            trial.setNotes(notes);
            trial.setTrialPurpose(trialPurpose);
            trial.setSectionId(selectedSection.getSectionId());
            trial.setMatrialId(selectedMatrial.getMatrialId());
            trial.setSupplierId(selectedSupplier.getSupplierId());
            trial.setSupCountryId(selectedSupplierCountry.getSupCountryId());
            trial.setUserId(UserContext.getCurrentUser().getUserId());

            boolean success = TrialDAO.updateTrial(trial);
            if (success) {
                WindowUtils.ALERT("Success", "Trial updated successfully.", WindowUtils.ALERT_INFORMATION);
                clearHelp();
            } else {
                WindowUtils.ALERT("Error", "Failed to update trial.", WindowUtils.ALERT_ERROR);
            }
            return success;
        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "updateTrial", ex);
            WindowUtils.ALERT("Error", "Exception during updating.", WindowUtils.ALERT_ERROR);
            return false;
        }
    }

    // Load trial data for editing
    public void setTrialData(int trialId, boolean update) {
        try {
            this.update = update;
            CURRENT_TRIAL_ID = trialId;
            Trial trial = TrialDAO.getTrialById(trialId);

            LocalDateTime creationDateTime = trial.getCreationDate();
            creationDate_datePiker.setValue(creationDateTime != null ? creationDateTime.toLocalDate() : null);

            notes_textArea.setText(trial.getNotes());
            trial_purpose_textArea.setText(trial.getTrialPurpose());
            section_combo.getSelectionModel().select(SectionDAO.getSectionById(trial.getSectionId()));
            matrial_combo.getSelectionModel().select(MatrialDAO.getMatrialById(trial.getMatrialId()));
            Supplier selectedSupplier = SupplierDAO.getSupplierById(trial.getSupplierId());
            supplier_combo.getSelectionModel().select(selectedSupplier);
            // Update supplier countries based on the selected supplier
            updateSupplierCountries(selectedSupplier);
            supplier_country_combo.getSelectionModel().select(SupplierCountryDAO.getSupplierCountryById(trial.getSupCountryId()));
            clear_btn.setVisible(false);
        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "setTrialData", ex);
            WindowUtils.ALERT("Error", "Exception during loading trial data.", WindowUtils.ALERT_ERROR);
        }
    }

    // Set the save button text to "Update" for edit mode
    public void setSaveButton() {
        save_btn.setText("Update");
    }

}