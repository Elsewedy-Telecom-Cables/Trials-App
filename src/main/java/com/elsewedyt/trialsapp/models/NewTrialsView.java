
package com.elsewedyt.trialsapp.models;
import java.time.LocalDateTime;

public class NewTrialsView {
    private int trialId;
    private String trialPurpose;
    private LocalDateTime trialCreationDate;
    private String trialNotes;
    private String sectionName;
    private String materialName;
    private String supplierName;
    private String supplierCountryName;
    private String tecOfficeFilePath;
    private String planningFilePath;
    private String productionFilePath;
    private String processFilePath;
    private String randDFilePath; // Keep the field name as is
    private String qualityControlFilePath;

    // Getters and setters for other fields (unchanged)
    public int getTrialId() { return trialId; }
    public void setTrialId(int trialId) { this.trialId = trialId; }
    public String getTrialPurpose() { return trialPurpose; }
    public void setTrialPurpose(String trialPurpose) { this.trialPurpose = trialPurpose; }
    public LocalDateTime  getTrialCreationDate() { return trialCreationDate; }
    public void setTrialCreationDate(LocalDateTime  trialCreationDate) { this.trialCreationDate = trialCreationDate; }
    public String getTrialNotes() { return trialNotes; }
    public void setTrialNotes(String trialNotes) { this.trialNotes = trialNotes; }
    public String getSectionName() { return sectionName; }
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getSupplierCountryName() { return supplierCountryName; }
    public void setSupplierCountryName(String supplierCountryName) { this.supplierCountryName = supplierCountryName; }
    public String getTecOfficeFilePath() { return tecOfficeFilePath; }
    public void setTecOfficeFilePath(String tecOfficeFilePath) { this.tecOfficeFilePath = tecOfficeFilePath; }
    public String getPlanningFilePath() { return planningFilePath; }
    public void setPlanningFilePath(String planningFilePath) { this.planningFilePath = planningFilePath; }
    public String getProductionFilePath() { return productionFilePath; }
    public void setProductionFilePath(String productionFilePath) { this.productionFilePath = productionFilePath; }
    public String getProcessFilePath() { return processFilePath; }
    public void setProcessFilePath(String processFilePath) { this.processFilePath = processFilePath; }
    public String getQualityControlFilePath() { return qualityControlFilePath; }
    public void setQualityControlFilePath(String qualityControlFilePath) { this.qualityControlFilePath = qualityControlFilePath; }

    // Modified getter and setter for randDFilePath
    public String getRandDFilePath() {
        return randDFilePath;
    }

    public void setRandDFilePath(String randDFilePath) {
        this.randDFilePath = randDFilePath;
    }
}
