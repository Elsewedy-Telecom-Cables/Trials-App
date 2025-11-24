//
//package com.elsewedyt.trialsapp.model;
//import java.time.LocalDateTime;
//
//public class NewTrialsView {
//    private int trialId;
//    private String trialPurpose;
//    private LocalDateTime trialCreationDate;
//    private String trialNotes;
//    private String sectionName;
//    private String materialName;
//    private String supplierName;
//    private String supplierCountryName;
//    private String tecOfficeFilePath;
//    private String planningFilePath;
//    private String productionFilePath;
//    private String processFilePath;
//    private String randDFilePath; // Keep the field name as is
//    private String qualityControlFilePath;
//
//    // Getters and setters for other fields (unchanged)
//    public int getTrialId() { return trialId; }
//    public void setTrialId(int trialId) { this.trialId = trialId; }
//    public String getTrialPurpose() { return trialPurpose; }
//    public void setTrialPurpose(String trialPurpose) { this.trialPurpose = trialPurpose; }
//    public LocalDateTime  getTrialCreationDate() { return trialCreationDate; }
//    public void setTrialCreationDate(LocalDateTime  trialCreationDate) { this.trialCreationDate = trialCreationDate; }
//    public String getTrialNotes() { return trialNotes; }
//    public void setTrialNotes(String trialNotes) { this.trialNotes = trialNotes; }
//    public String getSectionName() { return sectionName; }
//    public void setSectionName(String sectionName) { this.sectionName = sectionName; }
//    public String getMaterialName() { return materialName; }
//    public void setMaterialName(String materialName) { this.materialName = materialName; }
//    public String getSupplierName() { return supplierName; }
//    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
//    public String getSupplierCountryName() { return supplierCountryName; }
//    public void setSupplierCountryName(String supplierCountryName) { this.supplierCountryName = supplierCountryName; }
//    public String getTecOfficeFilePath() { return tecOfficeFilePath; }
//    public void setTecOfficeFilePath(String tecOfficeFilePath) { this.tecOfficeFilePath = tecOfficeFilePath; }
//    public String getPlanningFilePath() { return planningFilePath; }
//    public void setPlanningFilePath(String planningFilePath) { this.planningFilePath = planningFilePath; }
//    public String getProductionFilePath() { return productionFilePath; }
//    public void setProductionFilePath(String productionFilePath) { this.productionFilePath = productionFilePath; }
//    public String getProcessFilePath() { return processFilePath; }
//    public void setProcessFilePath(String processFilePath) { this.processFilePath = processFilePath; }
//    public String getQualityControlFilePath() { return qualityControlFilePath; }
//    public void setQualityControlFilePath(String qualityControlFilePath) { this.qualityControlFilePath = qualityControlFilePath; }
//
//    // Modified getter and setter for randDFilePath
//    public String getRandDFilePath() {
//        return randDFilePath;
//    }
//
//    public void setRandDFilePath(String randDFilePath) {
//        this.randDFilePath = randDFilePath;
//    }
//}

//package com.elsewedyt.trialsapp.model;
//import javafx.beans.property.*;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class NewTrialsView {
//    private final IntegerProperty trialId = new SimpleIntegerProperty();
//    private final StringProperty trialPurpose = new SimpleStringProperty();
//    private final ObjectProperty<LocalDateTime> trialCreationDate = new SimpleObjectProperty<>();
//    private final StringProperty trialNotes = new SimpleStringProperty();
//    private final StringProperty sectionName = new SimpleStringProperty();
//    private final StringProperty materialName = new SimpleStringProperty();
//    private final StringProperty supplierName = new SimpleStringProperty();
//    private final StringProperty supplierCountryName = new SimpleStringProperty();
//    private final ObjectProperty<List<String>> tecOfficeFilePaths = new SimpleObjectProperty<>(new ArrayList<>());
//    // Assume other departments already use lists (based on your description)
//    private final ObjectProperty<List<String>> planningFilePaths = new SimpleObjectProperty<>(new ArrayList<>());
//    private final ObjectProperty<List<String>> productionFilePaths = new SimpleObjectProperty<>(new ArrayList<>());
//    private final ObjectProperty<List<String>> processFilePaths = new SimpleObjectProperty<>(new ArrayList<>());
//    private final ObjectProperty<List<String>> randDFilePaths = new SimpleObjectProperty<>(new ArrayList<>());
//    private final ObjectProperty<List<String>> qualityControlFilePaths = new SimpleObjectProperty<>(new ArrayList<>());
//
//    // Getters and setters for trialId, trialPurpose, etc.
//    public int getTrialId() { return trialId.get(); }
//    public void setTrialId(int trialId) { this.trialId.set(trialId); }
//    public String getTrialPurpose() { return trialPurpose.get(); }
//    public void setTrialPurpose(String trialPurpose) { this.trialPurpose.set(trialPurpose); }
//    public LocalDateTime getTrialCreationDate() { return trialCreationDate.get(); }
//    public void setTrialCreationDate(LocalDateTime trialCreationDate) { this.trialCreationDate.set(trialCreationDate); }
//    public String getTrialNotes() { return trialNotes.get(); }
//    public void setTrialNotes(String trialNotes) { this.trialNotes.set(trialNotes); }
//    public String getSectionName() { return sectionName.get(); }
//    public void setSectionName(String sectionName) { this.sectionName.set(sectionName); }
//    public String getMaterialName() { return materialName.get(); }
//    public void setMaterialName(String materialName) { this.materialName.set(materialName); }
//    public String getSupplierName() { return supplierName.get(); }
//    public void setSupplierName(String supplierName) { this.supplierName.set(supplierName); }
//    public String getSupplierCountryName() { return supplierCountryName.get(); }
//    public void setSupplierCountryName(String supplierCountryName) { this.supplierCountryName.set(supplierCountryName); }
//
//    // Getter and setter for tecOfficeFilePaths
//    public List<String> getTecOfficeFilePaths() { return tecOfficeFilePaths.get(); }
//    public void setTecOfficeFilePaths(List<String> filePaths) { this.tecOfficeFilePaths.set(filePaths); }
//    public void addTecOfficeFilePath(String filePath) { this.tecOfficeFilePaths.get().add(filePath); }
//
//    // Getters and setters for other departments (assuming they use lists)
//    public List<String> getPlanningFilePaths() { return planningFilePaths.get(); }
//    public void setPlanningFilePaths(List<String> filePaths) { this.planningFilePaths.set(filePaths); }
//    public void addPlanningFilePath(String filePath) { this.planningFilePaths.get().add(filePath); }
//    public List<String> getProductionFilePaths() { return productionFilePaths.get(); }
//    public void setProductionFilePaths(List<String> filePaths) { this.productionFilePaths.set(filePaths); }
//    public void addProductionFilePath(String filePath) { this.productionFilePaths.get().add(filePath); }
//    public List<String> getProcessFilePaths() { return processFilePaths.get(); }
//    public void setProcessFilePaths(List<String> filePaths) { this.processFilePaths.set(filePaths); }
//    public void addProcessFilePath(String filePath) { this.processFilePaths.get().add(filePath); }
//    public List<String> getRandDFilePaths() { return randDFilePaths.get(); }
//    public void setRandDFilePaths(List<String> filePaths) { this.randDFilePaths.set(filePaths); }
//    public void addRandDFilePath(String filePath) { this.randDFilePaths.get().add(filePath); }
//    public List<String> getQualityControlFilePaths() { return qualityControlFilePaths.get(); }
//    public void setQualityControlFilePaths(List<String> filePaths) { this.qualityControlFilePaths.set(filePaths); }
//    public void addQualityControlFilePath(String filePath) { this.qualityControlFilePaths.get().add(filePath); }
//}
package com.elsewedyt.trialsapp.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NewTrialsView {
    private final IntegerProperty trialId = new SimpleIntegerProperty();
    private final StringProperty trialPurpose = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> trialCreationDate = new SimpleObjectProperty<>();
    private final StringProperty trialNotes = new SimpleStringProperty();
    private final StringProperty sectionName = new SimpleStringProperty();
    private final StringProperty materialName = new SimpleStringProperty();
    private final StringProperty supplierName = new SimpleStringProperty();
    private final StringProperty supplierCountryName = new SimpleStringProperty();
    private final ObjectProperty<List<String>> tecOfficeFilePaths = new SimpleObjectProperty<>(new ArrayList<>());
    private final ObjectProperty<List<String>> planningFilePaths = new SimpleObjectProperty<>(new ArrayList<>());
    private final ObjectProperty<List<String>> productionFilePaths = new SimpleObjectProperty<>(new ArrayList<>());
    private final ObjectProperty<List<String>> processFilePaths = new SimpleObjectProperty<>(new ArrayList<>());
    private final ObjectProperty<List<String>> randDFilePaths = new SimpleObjectProperty<>(new ArrayList<>());
    private final ObjectProperty<List<String>> qualityControlFilePaths = new SimpleObjectProperty<>(new ArrayList<>());

    // Getters and setters for trialId, trialPurpose, etc.
    public int getTrialId() { return trialId.get(); }
    public void setTrialId(int trialId) { this.trialId.set(trialId); }
    public String getTrialPurpose() { return trialPurpose.get(); }
    public void setTrialPurpose(String trialPurpose) { this.trialPurpose.set(trialPurpose); }
    public LocalDateTime getTrialCreationDate() { return trialCreationDate.get(); }
    public void setTrialCreationDate(LocalDateTime trialCreationDate) { this.trialCreationDate.set(trialCreationDate); }
    public String getTrialNotes() { return trialNotes.get(); }
    public void setTrialNotes(String trialNotes) { this.trialNotes.set(trialNotes); }
    public String getSectionName() { return sectionName.get(); }
    public void setSectionName(String sectionName) { this.sectionName.set(sectionName); }
    public String getMaterialName() { return materialName.get(); }
    public void setMaterialName(String materialName) { this.materialName.set(materialName); }
    public String getSupplierName() { return supplierName.get(); }
    public void setSupplierName(String supplierName) { this.supplierName.set(supplierName); }
    public String getSupplierCountryName() { return supplierCountryName.get(); }
    public void setSupplierCountryName(String supplierCountryName) { this.supplierCountryName.set(supplierCountryName); }

    // Getters and setters for file paths
    public List<String> getTecOfficeFilePaths() { return tecOfficeFilePaths.get(); }
    public void setTecOfficeFilePaths(List<String> filePaths) { this.tecOfficeFilePaths.set(filePaths); }
    public void addTecOfficeFilePath(String filePath) { this.tecOfficeFilePaths.get().add(filePath); }

    public List<String> getPlanningFilePaths() { return planningFilePaths.get(); }
    public void setPlanningFilePaths(List<String> filePaths) { this.planningFilePaths.set(filePaths); }
    public void addPlanningFilePath(String filePath) { this.planningFilePaths.get().add(filePath); }

    public List<String> getProductionFilePaths() { return productionFilePaths.get(); }
    public void setProductionFilePaths(List<String> filePaths) { this.productionFilePaths.set(filePaths); }
    public void addProductionFilePath(String filePath) { this.productionFilePaths.get().add(filePath); }

    public List<String> getProcessFilePaths() { return processFilePaths.get(); }
    public void setProcessFilePaths(List<String> filePaths) { this.processFilePaths.set(filePaths); }
    public void addProcessFilePath(String filePath) { this.processFilePaths.get().add(filePath); }

    public List<String> getRandDFilePaths() { return randDFilePaths.get(); }
    public void setRandDFilePaths(List<String> filePaths) { this.randDFilePaths.set(filePaths); }
    public void addRandDFilePath(String filePath) { this.randDFilePaths.get().add(filePath); }

    public List<String> getQualityControlFilePaths() { return qualityControlFilePaths.get(); }
    public void setQualityControlFilePaths(List<String> filePaths) { this.qualityControlFilePaths.set(filePaths); }
    public void addQualityControlFilePath(String filePath) { this.qualityControlFilePaths.get().add(filePath); }
}
