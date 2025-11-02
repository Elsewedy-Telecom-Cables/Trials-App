package com.elsewedyt.trialsapp.controllers;

import com.elsewedyt.trialsapp.dao.*;
import com.elsewedyt.trialsapp.models.*;
import com.elsewedyt.trialsapp.services.ConfigLoader;
import com.elsewedyt.trialsapp.services.WindowUtils;
import com.elsewedyt.trialsapp.logging.Logging;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.stage.Stage;
//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddTrialController implements Initializable {
    @FXML private Button clear_btn;
    @FXML private DatePicker creationDate_datePiker;
    @FXML private Label header_lbl;
    @FXML private ComboBox<Matrial> matrial_combo;
    @FXML private TextArea notes_textArea;
    @FXML private Button save_btn;
    @FXML private ComboBox<Section> section_combo;
    @FXML private ComboBox<Supplier> supplier_combo;
    @FXML private ComboBox<SupplierCountry> supplier_country_combo;
    @FXML private TextArea trial_purpose_textArea;

    private Stage stage;
    private int CURRENT_TRIAL_ID = 0;
    private boolean update = false;

    public void closeWindow() {
        if (stage != null) {
            stage.close();
        } else {
            Logging.logException("ERROR", this.getClass().getName(), "closeWindow", new IllegalStateException("Stage is null in closeWindow"));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
   //     System.out.println("initialize called, stage: " + (stage != null ? stage.getTitle() : "null"));
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
                    creationDate = LocalDateTime.now(); // Record current time if date is today
                } else {
                    creationDate = selectedDate.atStartOfDay(); // Different date: set to 00:00
                }
            } else {
                creationDate = LocalDateTime.now(); // No date selected
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
                });
            }else {
              //      System.out.println("TrialsController instance is null!");
                }

//                // Send email notification in a separate thread
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(() -> sendEmailNotification(
                        UserContext.getCurrentUser().getFullName(),
                        selectedSection.getSectionName(),
                        selectedMatrial.getMatrialName(),
                        selectedSupplier.getSupplierName(),
                        selectedSupplierCountry.getCountryName(),
                        trialPurpose,
                        notes
                ));
                executor.shutdown();

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

    public static final String mailName = ConfigLoader.getProperty("TRIAL.MAIL");
    private static final String mailPassword = ConfigLoader.getProperty("TRIAL.MAIL.PASSWORD");

    private void sendEmailNotification(String userFullName, String sectionName, String matrialName,
                                       String supplierName, String supplierCountry, String trialPurpose, String notes) {
        // Email configuration
        String fromEmail = mailName;
        String password = mailPassword;
        List<String> toEmails = new ArrayList<>();

        // Get active users' emails, excluding specific ones
        Set<String> excludedEmails = new HashSet<>();
        //  excludedEmails.add("moh.gabr@elsewedy.com"); // exclude specific emails
        toEmails.addAll(UserDAO.getActiveUsersEmails(excludedEmails));


        // CC recipients as a list
        List<String> ccEmails = new ArrayList<>();

        // SMTP server properties for Elsewedy mail server
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "imail.elsewedy.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
        properties.put("mail.smtp.ssl.trust", "imail.elsewedy.com");
        properties.put("mail.smtp.ssl.ciphersuites", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
        // properties.put("mail.debug", "true"); // Disabled as per request

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));

            // Add TO recipients
            for (String toEmail : toEmails) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            }

            // Add CC recipients
            for (String ccEmail : ccEmails) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccEmail));
            }

            // Set email subject
            message.setSubject("New Trial Added in Trials Application");

            // Handle notes if empty
            String notesText = (notes == null || notes.trim().isEmpty()) ? "No notes provided." : notes;

            // Build HTML table for trial details
            StringBuilder trialTableHtml = new StringBuilder();

            trialTableHtml.append("<table border='1' style='border-collapse: collapse; font-family: Montserrat, sans-serif; font-size: 12px;'>");
            trialTableHtml.append("<tr style='background-color: #f2f2f2;'>");
            trialTableHtml.append("<th colspan='2' style='padding: 8px;'><strong>Trial Details</strong></th>");
            trialTableHtml.append("</tr>");

            trialTableHtml.append("<tr>");
            trialTableHtml.append("<td style='padding: 8px;'><strong>Section</strong></td>");
            trialTableHtml.append("<td style='padding: 8px;'>").append(sectionName).append("</td>");
            trialTableHtml.append("</tr>");

            trialTableHtml.append("<tr>");
            trialTableHtml.append("<td style='padding: 8px;'><strong>Material-Cable Type</strong></td>");
            trialTableHtml.append("<td style='padding: 8px;'>").append(matrialName).append("</td>");
            trialTableHtml.append("</tr>");

            trialTableHtml.append("<tr>");
            trialTableHtml.append("<td style='padding: 8px;'><strong>Supplier</strong></td>");
            trialTableHtml.append("<td style='padding: 8px;'>").append(supplierName).append("</td>");
            trialTableHtml.append("</tr>");

            trialTableHtml.append("<tr>");
            trialTableHtml.append("<td style='padding: 8px;'><strong>Supplier Country<strong></td>");
            trialTableHtml.append("<td style='padding: 8px;'>").append(supplierCountry).append("</td>");
            trialTableHtml.append("</tr>");

            trialTableHtml.append("<tr>");
            trialTableHtml.append("<td style='padding: 8px;'><strong>Trial Purpose</strong></td>");
            trialTableHtml.append("<td style='padding: 8px;'>").append(trialPurpose).append("</td>");
            trialTableHtml.append("</tr>");

            trialTableHtml.append("<tr>");
            trialTableHtml.append("<td style='padding: 8px;'><strong>Notes</strong></td>");
            trialTableHtml.append("<td style='padding: 8px;'>").append(notesText).append("</td>");
            trialTableHtml.append("</tr>");

            trialTableHtml.append("</table>");

            // Load the logo image from resources
            InputStream logoStream = this.getClass().getResourceAsStream("/images/logo_etc.png");
            if (logoStream == null) {
                throw new IllegalStateException("Logo file not found at /images/logo_etc.png");
            }
            // Read the image into a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = logoStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            logoStream.close();
            byte[] logoBytes = baos.toByteArray();

            // Build email body with all required parameters in HTML format
            String emailBody = "<html>" +
                    "<body style='font-family: Montserrat, sans-serif; font-size: 12px;'>" +
                    "<p>Dears,</p>" +
                    "<p>Kindly be informed that Eng. " + userFullName +
                    " – Technical Office Department has initiated a new <strong>TRIAL</strong>.</p>" +
                    "<p>Kindly monitor the execution of the <strong>TRIAL</strong> in the production hall, and then upload the relevant operational and technical tests data so that Technical Office can prepare the final report.</p>" +
                    trialTableHtml.toString() +
                    "<p>--------------------------------------------------------------</p>" +
                    "<br><br>" +
                    "<p>Best Regards,</p>" +
                    "<p><strong>Software Development Section</strong></p>" +
                    "<p><strong>Process & Development Department</strong></p>" +
                    "<p><strong>Elsewedy Telecom Cables</strong></p>" +
                    "<p><strong>System by:</strong> Eng. Mohamed Gabr &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <strong>Supervised by:</strong> Eng. Hassan Farid Ahmed  <strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Operation Director:</strong> Eng. Mohamed Magdy Amer</p>" +
                    "<p><img src='cid:logo' alt='Elsewedy Telecom Cables Logo' style='display: block; margin-top: 10px;'/></p>" +
                    "</body></html>";

            // Create a multipart message for HTML content and embedded image
            MimeMultipart multipart = new MimeMultipart("related");

            // Add HTML content
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(emailBody, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);

            // Add logo image as an embedded resource
            MimeBodyPart imagePart = new MimeBodyPart();
            DataSource dataSource = new ByteArrayDataSource(logoBytes, "image/png");
            imagePart.setDataHandler(new DataHandler(dataSource));
            imagePart.setHeader("Content-ID", "<logo>");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(imagePart);

            // Set the multipart content to the message
            message.setContent(multipart);

            // Send the email
            Transport.send(message);
            //   System.out.println("Email sent successfully to: " + toEmails + ", CC: " + ccEmails);

        } catch (MessagingException | IOException e) {
            Logging.logException("ERROR", this.getClass().getName(), "sendEmailNotification", e);
            WindowUtils.ALERT("Error", "Failed to send email notification: " + e.getMessage(), WindowUtils.ALERT_ERROR);
        }
    }




}