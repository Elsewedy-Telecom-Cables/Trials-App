////package com.elsewedyt.trialsapp.models;
////
////import javafx.beans.property.SimpleStringProperty;
////import java.time.LocalDate;
////import java.time.format.DateTimeFormatter;
////
////// Class to represent a node in the TreeTableView (either a Trial or a File)
////public class TrialTreeItem {
////    private final SimpleStringProperty trialId;
////    private final SimpleStringProperty trialPurpose;
////    private final SimpleStringProperty trialCreationDate;
////    private final SimpleStringProperty trialNotes;
////    private final SimpleStringProperty sectionName;
////    private final SimpleStringProperty materialName;
////    private final SimpleStringProperty supplierName;
////    private final SimpleStringProperty supplierCountryName;
////    private final SimpleStringProperty departmentName;
////    private final SimpleStringProperty userFullName;
////    private final SimpleStringProperty fileTypeName;
////    private final SimpleStringProperty testSituation;
////    private final SimpleStringProperty comment;
////    private final SimpleStringProperty fileCreationDate;
////    private final SimpleStringProperty filePath;
////    private final boolean isTrialNode; // To differentiate between Trial (parent) and File (child) nodes
////
////    // Constructor for Trial node (parent)
////    public TrialTreeItem(TrialsView trialsView) {
////        this.trialId = new SimpleStringProperty(String.valueOf(trialsView.getTrialId())); // Convert int to String
////        this.trialPurpose = new SimpleStringProperty(trialsView.getTrialPurpose());
////        this.trialCreationDate = new SimpleStringProperty(
////                trialsView.getTrialCreationDate() != null ? trialsView.getTrialCreationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : "");
////        this.trialNotes = new SimpleStringProperty(trialsView.getTrialNotes());
////        this.sectionName = new SimpleStringProperty(trialsView.getSectionName());
////        this.materialName = new SimpleStringProperty(trialsView.getMaterialName());
////        this.supplierName = new SimpleStringProperty(trialsView.getSupplierName());
////        this.supplierCountryName = new SimpleStringProperty(trialsView.getSupplierCountryName());
////        this.departmentName = new SimpleStringProperty(trialsView.getDepartmentName());
////        this.userFullName = new SimpleStringProperty(trialsView.getUserFullName());
////        this.fileTypeName = new SimpleStringProperty("");
////        this.testSituation = new SimpleStringProperty("");
////        this.comment = new SimpleStringProperty("");
////        this.fileCreationDate = new SimpleStringProperty("");
////        this.filePath = new SimpleStringProperty("");
////        this.isTrialNode = true;
////    }
////
////    // Constructor for File node (child)
////    public TrialTreeItem(TrialsView trialsView, boolean isChild) {
////        this.trialId = new SimpleStringProperty("");
////        this.trialPurpose = new SimpleStringProperty("");
////        this.trialCreationDate = new SimpleStringProperty("");
////        this.trialNotes = new SimpleStringProperty("");
////        this.sectionName = new SimpleStringProperty("");
////        this.materialName = new SimpleStringProperty("");
////        this.supplierName = new SimpleStringProperty("");
////        this.supplierCountryName = new SimpleStringProperty("");
////        this.departmentName = new SimpleStringProperty("");
////        this.userFullName = new SimpleStringProperty("");
////        this.fileTypeName = new SimpleStringProperty(trialsView.getFileTypeName());
////        this.testSituation = new SimpleStringProperty(trialsView.getTestSituation());
////        this.comment = new SimpleStringProperty(trialsView.getComment());
////        this.fileCreationDate = new SimpleStringProperty(
////                trialsView.getFileCreationDate() != null ? trialsView.getFileCreationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : "");
////        this.filePath = new SimpleStringProperty(trialsView.getFilePath());
////        this.isTrialNode = false;
////    }
////
////    // Getters
////    public String getTrialId() { return trialId.get(); }
////    public String getTrialPurpose() { return trialPurpose.get(); }
////    public String getTrialCreationDate() { return trialCreationDate.get(); }
////    public String getTrialNotes() { return trialNotes.get(); }
////    public String getSectionName() { return sectionName.get(); }
////    public String getMaterialName() { return materialName.get(); }
////    public String getSupplierName() { return supplierName.get(); }
////    public String getSupplierCountryName() { return supplierCountryName.get(); }
////    public String getDepartmentName() { return departmentName.get(); }
////    public String getUserFullName() { return userFullName.get(); }
////    public String getFileTypeName() { return fileTypeName.get(); }
////    public String getTestSituation() { return testSituation.get(); }
////    public String getComment() { return comment.get(); }
////    public String getFileCreationDate() { return fileCreationDate.get(); }
////    public String getFilePath() { return filePath.get(); }
////    public boolean isTrialNode() { return isTrialNode; }
////
////    // Property getters
////    public SimpleStringProperty trialIdProperty() { return trialId; }
////    public SimpleStringProperty trialPurposeProperty() { return trialPurpose; }
////    public SimpleStringProperty trialCreationDateProperty() { return trialCreationDate; }
////    public SimpleStringProperty trialNotesProperty() { return trialNotes; }
////    public SimpleStringProperty sectionNameProperty() { return sectionName; }
////    public SimpleStringProperty materialNameProperty() { return materialName; }
////    public SimpleStringProperty supplierNameProperty() { return supplierName; }
////    public SimpleStringProperty supplierCountryNameProperty() { return supplierCountryName; }
////    public SimpleStringProperty departmentNameProperty() { return departmentName; }
////    public SimpleStringProperty userFullNameProperty() { return userFullName; }
////    public SimpleStringProperty fileTypeNameProperty() { return fileTypeName; }
////    public SimpleStringProperty testSituationProperty() { return testSituation; }
////    public SimpleStringProperty commentProperty() { return comment; }
////    public SimpleStringProperty fileCreationDateProperty() { return fileCreationDate; }
////    public SimpleStringProperty filePathProperty() { return filePath; }
////}
//
//
//        package com.elsewedyt.trialsapp.models;
//
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.property.StringProperty;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//public class TrialTreeItem {
//    private final StringProperty trialId;
//    private final StringProperty trialPurpose;
//    private final StringProperty trialCreationDate;
//    private final StringProperty trialNotes;
//    private final StringProperty sectionName;
//    private final StringProperty materialName;
//    private final StringProperty supplierName;
//    private final StringProperty supplierCountryName;
//    private final StringProperty departmentName;
//    private final StringProperty userFullName;
//    private final StringProperty fileTypeName;
//    private final StringProperty testSituation;
//    private final StringProperty comment;
//    private final StringProperty fileCreationDate;
//    private final StringProperty filePath;
//    private final boolean isFileNode;
//
//    // Constructor for trial node (parent node)
//    public TrialTreeItem(TrialsView trial) {
//        // Convert int trialId to String directly (no null check needed for primitive int)
//        this.trialId = new SimpleStringProperty(String.valueOf(trial.getTrialId()));
//        this.trialPurpose = new SimpleStringProperty(trial.getTrialPurpose());
//        // Convert LocalDate to String using DateTimeFormatter
//        this.trialCreationDate = new SimpleStringProperty(
//                trial.getTrialCreationDate() != null
//                        ? trial.getTrialCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//                        : null
//        );
//        this.trialNotes = new SimpleStringProperty(trial.getTrialNotes());
//        this.sectionName = new SimpleStringProperty(trial.getSectionName());
//        this.materialName = new SimpleStringProperty(trial.getMaterialName());
//        this.supplierName = new SimpleStringProperty(trial.getSupplierName());
//        this.supplierCountryName = new SimpleStringProperty(trial.getSupplierCountryName());
//        this.departmentName = new SimpleStringProperty(trial.getDepartmentName());
//        this.userFullName = new SimpleStringProperty(trial.getUserFullName());
//        this.fileTypeName = new SimpleStringProperty(null);
//        this.testSituation = new SimpleStringProperty(null);
//        this.comment = new SimpleStringProperty(null);
//        this.fileCreationDate = new SimpleStringProperty(null);
//        this.filePath = new SimpleStringProperty(null);
//        this.isFileNode = false;
//    }
//
//    // Constructor for file node (child node)
//    public TrialTreeItem(TrialsView trial, boolean isFileNode) {
//        // Convert int trialId to String directly
//        this.trialId = new SimpleStringProperty(String.valueOf(trial.getTrialId()));
//        this.trialPurpose = new SimpleStringProperty(trial.getTrialPurpose());
//        // Convert LocalDate to String using DateTimeFormatter
//        this.trialCreationDate = new SimpleStringProperty(
//                trial.getTrialCreationDate() != null
//                        ? trial.getTrialCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//                        : null
//        );
//        this.trialNotes = new SimpleStringProperty(trial.getTrialNotes());
//        this.sectionName = new SimpleStringProperty(trial.getSectionName());
//        this.materialName = new SimpleStringProperty(trial.getMaterialName());
//        this.supplierName = new SimpleStringProperty(trial.getSupplierName());
//        this.supplierCountryName = new SimpleStringProperty(trial.getSupplierCountryName());
//        this.departmentName = new SimpleStringProperty(trial.getDepartmentName());
//        this.userFullName = new SimpleStringProperty(trial.getUserFullName());
//        this.fileTypeName = new SimpleStringProperty(trial.getFileTypeName());
//        this.testSituation = new SimpleStringProperty(trial.getTestSituation());
//        this.comment = new SimpleStringProperty(trial.getComment());
//        // Convert fileCreationDate (assuming it's also a LocalDate)
//        this.fileCreationDate = new SimpleStringProperty(
//                trial.getFileCreationDate() != null
//                        ? trial.getFileCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//                        : null
//        );
//        this.filePath = new SimpleStringProperty(trial.getFilePath());
//        this.isFileNode = isFileNode;
//    }
//
//    // Getters for StringProperty fields
//    public StringProperty trialIdProperty() {
//        return trialId;
//    }
//
//    public StringProperty trialPurposeProperty() {
//        return trialPurpose;
//    }
//
//    public StringProperty trialCreationDateProperty() {
//        return trialCreationDate;
//    }
//
//    public StringProperty trialNotesProperty() {
//        return trialNotes;
//    }
//
//    public StringProperty sectionNameProperty() {
//        return sectionName;
//    }
//
//    public StringProperty materialNameProperty() {
//        return materialName;
//    }
//
//    public StringProperty supplierNameProperty() {
//        return supplierName;
//    }
//
//    public StringProperty supplierCountryNameProperty() {
//        return supplierCountryName;
//    }
//
//    public StringProperty departmentNameProperty() {
//        return departmentName;
//    }
//
//    public StringProperty userFullNameProperty() {
//        return userFullName;
//    }
//
//    public StringProperty fileTypeNameProperty() {
//        return fileTypeName;
//    }
//
//    public StringProperty testSituationProperty() {
//        return testSituation;
//    }
//
//    public StringProperty commentProperty() {
//        return comment;
//    }
//
//    public StringProperty fileCreationDateProperty() {
//        return fileCreationDate;
//    }
//
//    public StringProperty filePathProperty() {
//        return filePath;
//    }
//
//    // Getter for isFileNode
//    public boolean isFileNode() {
//        return isFileNode;
//    }
//
//    // Getter for isTrialNode
//    public boolean isTrialNode() {
//        return !isFileNode;
//    }
//
//    // Additional getters for direct access (used in double-click handler)
//    public String getTrialId() {
//        return trialId.get();
//    }
//
//    public String getTrialPurpose() {
//        return trialPurpose.get();
//    }
//
//    public String getTrialCreationDate() {
//        return trialCreationDate.get();
//    }
//
//    public String getTrialNotes() {
//        return trialNotes.get();
//    }
//
//    public String getSectionName() {
//        return sectionName.get();
//    }
//
//    public String getMaterialName() {
//        return materialName.get();
//    }
//
//    public String getSupplierName() {
//        return supplierName.get();
//    }
//
//    public String getSupplierCountryName() {
//        return supplierCountryName.get();
//    }
//
//    public String getDepartmentName() {
//        return departmentName.get();
//    }
//
//    public String getUserFullName() {
//        return userFullName.get();
//    }
//
//    public String getFileTypeName() {
//        return fileTypeName.get();
//    }
//
//    public String getTestSituation() {
//        return testSituation.get();
//    }
//
//    public String getComment() {
//        return comment.get();
//    }
//
//    public String getFileCreationDate() {
//        return fileCreationDate.get();
//    }
//
//    public String getFilePath() {
//        return filePath.get();
//    }
//}


package com.elsewedyt.trialsapp.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TrialTreeItem {
    private final StringProperty trialId;
    private final StringProperty trialPurpose;
    private final StringProperty trialCreationDate;
    private final StringProperty trialNotes;
    private final StringProperty sectionName;
    private final StringProperty materialName;
    private final StringProperty supplierName;
    private final StringProperty supplierCountryName;
    private final StringProperty departmentName;
    private final StringProperty userFullName;
    private final StringProperty fileTypeName;
    private final StringProperty testSituation;
    private final StringProperty comment;
    private final StringProperty fileCreationDate;
    private final StringProperty filePath;
    private final boolean isFileNode;
    private String styleClass; // Added to store CSS style class

    // Constructor for trial node (parent node)
    public TrialTreeItem(TrialsView trial) {
        this.trialId = new SimpleStringProperty(String.valueOf(trial.getTrialId()));
        this.trialPurpose = new SimpleStringProperty(trial.getTrialPurpose());
        this.trialCreationDate = new SimpleStringProperty(
                trial.getTrialCreationDate() != null
                        ? trial.getTrialCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        : null
        );
        this.trialNotes = new SimpleStringProperty(trial.getTrialNotes());
        this.sectionName = new SimpleStringProperty(trial.getSectionName());
        this.materialName = new SimpleStringProperty(trial.getMaterialName());
        this.supplierName = new SimpleStringProperty(trial.getSupplierName());
        this.supplierCountryName = new SimpleStringProperty(trial.getSupplierCountryName());
        this.departmentName = new SimpleStringProperty(trial.getDepartmentName());
        this.userFullName = new SimpleStringProperty(trial.getUserFullName());
        this.fileTypeName = new SimpleStringProperty(null);
        this.testSituation = new SimpleStringProperty(null);
        this.comment = new SimpleStringProperty(null);
        this.fileCreationDate = new SimpleStringProperty(null);
        this.filePath = new SimpleStringProperty(null);
        this.isFileNode = false;
        this.styleClass = "trial-node"; // Default style class for parent nodes
    }

    // Constructor for file node (child node)
    public TrialTreeItem(TrialsView trial, boolean isFileNode) {
        this.trialId = new SimpleStringProperty(String.valueOf(trial.getTrialId()));
        this.trialPurpose = new SimpleStringProperty(trial.getTrialPurpose());
        this.trialCreationDate = new SimpleStringProperty(
                trial.getTrialCreationDate() != null
                        ? trial.getTrialCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        : null
        );
        this.trialNotes = new SimpleStringProperty(trial.getTrialNotes());
        this.sectionName = new SimpleStringProperty(trial.getSectionName());
        this.materialName = new SimpleStringProperty(trial.getMaterialName());
        this.supplierName = new SimpleStringProperty(trial.getSupplierName());
        this.supplierCountryName = new SimpleStringProperty(trial.getSupplierCountryName());
        this.departmentName = new SimpleStringProperty(trial.getDepartmentName());
        this.userFullName = new SimpleStringProperty(trial.getUserFullName());
        this.fileTypeName = new SimpleStringProperty(trial.getFileTypeName());
        this.testSituation = new SimpleStringProperty(trial.getTestSituation());
        this.comment = new SimpleStringProperty(trial.getComment());
        this.fileCreationDate = new SimpleStringProperty(
                trial.getFileCreationDate() != null
                        ? trial.getFileCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        : null
        );
        this.filePath = new SimpleStringProperty(trial.getFilePath());
        this.isFileNode = isFileNode;
        this.styleClass = isFileNode ? "file-node" : "trial-node"; // Set style class based on node type
    }

    // Getters for StringProperty fields
    public StringProperty trialIdProperty() {
        return trialId;
    }

    public StringProperty trialPurposeProperty() {
        return trialPurpose;
    }

    public StringProperty trialCreationDateProperty() {
        return trialCreationDate;
    }

    public StringProperty trialNotesProperty() {
        return trialNotes;
    }

    public StringProperty sectionNameProperty() {
        return sectionName;
    }

    public StringProperty materialNameProperty() {
        return materialName;
    }

    public StringProperty supplierNameProperty() {
        return supplierName;
    }

    public StringProperty supplierCountryNameProperty() {
        return supplierCountryName;
    }

    public StringProperty departmentNameProperty() {
        return departmentName;
    }

    public StringProperty userFullNameProperty() {
        return userFullName;
    }

    public StringProperty fileTypeNameProperty() {
        return fileTypeName;
    }

    public StringProperty testSituationProperty() {
        return testSituation;
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public StringProperty fileCreationDateProperty() {
        return fileCreationDate;
    }

    public StringProperty filePathProperty() {
        return filePath;
    }

    // Getter for isFileNode
    public boolean isFileNode() {
        return isFileNode;
    }

    // Getter for isTrialNode
    public boolean isTrialNode() {
        return !isFileNode;
    }

    // Getter and Setter for styleClass
    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    // Additional getters for direct access (used in double-click handler)
    public String getTrialId() {
        return trialId.get();
    }

    public String getTrialPurpose() {
        return trialPurpose.get();
    }

    public String getTrialCreationDate() {
        return trialCreationDate.get();
    }

    public String getTrialNotes() {
        return trialNotes.get();
    }

    public String getSectionName() {
        return sectionName.get();
    }

    public String getMaterialName() {
        return materialName.get();
    }

    public String getSupplierName() {
        return supplierName.get();
    }

    public String getSupplierCountryName() {
        return supplierCountryName.get();
    }

    public String getDepartmentName() {
        return departmentName.get();
    }

    public String getUserFullName() {
        return userFullName.get();
    }

    public String getFileTypeName() {
        return fileTypeName.get();
    }

    public String getTestSituation() {
        return testSituation.get();
    }

    public String getComment() {
        return comment.get();
    }

    public String getFileCreationDate() {
        return fileCreationDate.get();
    }

    public String getFilePath() {
        return filePath.get();
    }
}