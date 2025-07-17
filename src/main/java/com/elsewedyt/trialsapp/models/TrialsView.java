
package com.elsewedyt.trialsapp.models;

import java.time.LocalDateTime ;

public class TrialsView {
    private int trialId;
    private String trialPurpose;
    private LocalDateTime  trialCreationDate;
    private String trialNotes;
    private String sectionName;
    private String materialName;
    private String supplierName;
    private String supplierCountryName;
    private String departmentName;
    private String userFullName;
    private String fileTypeName;
    private String testSituation;
    private String comment;
    private LocalDateTime  fileCreationDate;
    private String filePath;

    // Getters and setters
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
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }
    public String getFileTypeName() { return fileTypeName; }
    public void setFileTypeName(String fileTypeName) { this.fileTypeName = fileTypeName; }
    public String getTestSituation() { return testSituation; }
    public void setTestSituation(String testSituation) { this.testSituation = testSituation; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime  getFileCreationDate() { return fileCreationDate; }
    public void setFileCreationDate(LocalDateTime  fileCreationDate) { this.fileCreationDate = fileCreationDate; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public static String convertTestSituationToString(Integer testSituation) {
        if (testSituation == null) {
            return "-";
        }
        switch (testSituation) {
            case 1:
                return "Accepted";
            case 2:
                return "Refused";
            case 3:
                return "Hold";
            default:
                return "-";
        }
    }
}