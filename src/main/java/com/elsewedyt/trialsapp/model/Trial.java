package com.elsewedyt.trialsapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Trial {
    // Fields from trials table
    private int trialId;
    private String trialPurpose;
    private Integer sectionId;
    private Integer matrialId;
    private Integer supplierId;
    private Integer supCountryId;
    private Integer userId;
    private LocalDateTime creationDate;
    private String notes;

    // Related objects for foreign key relationships
    private Section section;
    private Material material;
    private Supplier supplier;
    private SupplierCountry supplierCountry;
    private User user;

    // Additional fields for names
    private String sectionName;
    private String matrialName;
    private String supplierName;
    private String supplierCountryName;

    // Default constructor
    public Trial() {
    }

    // Constructor with essential fields
    public Trial(String trialPurpose, Integer sectionId, Integer matrialId, Integer supplierId, Integer supCountryId, Integer userId, LocalDateTime  creationDate, String notes) {
        this.trialPurpose = trialPurpose;
        this.sectionId = sectionId;
        this.matrialId = matrialId;
        this.supplierId = supplierId;
        this.supCountryId = supCountryId;
        this.userId = userId;
        this.creationDate = creationDate;
        this.notes = notes;
    }

    // Full constructor including related objects and names
    public Trial(int trialId, String trialPurpose, Integer sectionId, Integer matrialId, Integer supplierId, Integer supCountryId, Integer userId, LocalDateTime  creationDate, String notes, Section section, Material material, Supplier supplier, SupplierCountry supplierCountry, User user) {
        this.trialId = trialId;
        this.trialPurpose = trialPurpose;
        this.sectionId = sectionId;
        this.matrialId = matrialId;
        this.supplierId = supplierId;
        this.supCountryId = supCountryId;
        this.userId = userId;
        this.creationDate = creationDate;
        this.notes = notes;
        this.section = section;
        this.material = material;
        this.supplier = supplier;
        this.supplierCountry = supplierCountry;
        this.user = user;
        // Set name fields from related objects
        this.sectionName = section != null ? section.getSectionName() : null;
        this.matrialName = material != null ? material.getMatrialName() : null;
        this.supplierName = supplier != null ? supplier.getSupplierName() : null;
        this.supplierCountryName = supplierCountry != null ? supplierCountry.getCountryName() : null;
    }

    // Getters and Setters
    public int getTrialId() {
        return trialId;
    }

    public void setTrialId(int trialId) {
        this.trialId = trialId;
    }

    public String getTrialPurpose() {
        return trialPurpose;
    }

    public void setTrialPurpose(String trialPurpose) {
        this.trialPurpose = trialPurpose;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getMatrialId() {
        return matrialId;
    }

    public void setMatrialId(Integer matrialId) {
        this.matrialId = matrialId;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getSupCountryId() {
        return supCountryId;
    }

    public void setSupCountryId(Integer supCountryId) {
        this.supCountryId = supCountryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime  getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime  creationDate) {
        this.creationDate = creationDate;
    }

    // Get formatted creation date for display (dd-MM-yyyy)
    public String getFormattedCreationDate() {
        if (creationDate == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return creationDate.format(formatter);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
        this.sectionName = section != null ? section.getSectionName() : null;
    }

    public Material getMatrial() {
        return material;
    }

    public void setMatrial(Material material) {
        this.material = material;
        this.matrialName = material != null ? material.getMatrialName() : null;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        this.supplierName = supplier != null ? supplier.getSupplierName() : null;
    }

    public SupplierCountry getSupplierCountry() {
        return supplierCountry;
    }

    public void setSupplierCountry(SupplierCountry supplierCountry) {
        this.supplierCountry = supplierCountry;
        this.supplierCountryName = supplierCountry != null ? supplierCountry.getCountryName() : null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getMatrialName() {
        return matrialName;
    }

    public void setMatrialName(String matrialName) {
        this.matrialName = matrialName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierCountryName() {
        return supplierCountryName;
    }

    public void setSupplierCountryName(String supplierCountryName) {
        this.supplierCountryName = supplierCountryName;
    }

    // toString for display in JavaFX components like ComboBox
    @Override
    public String toString() {
        return trialPurpose != null ? trialPurpose : "Trial " + trialId;
    }
}