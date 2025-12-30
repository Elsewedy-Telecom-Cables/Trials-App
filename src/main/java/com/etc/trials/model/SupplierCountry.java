package com.etc.trials.model;

public class SupplierCountry {
    private int supCountryId;
    private Integer countryId;
    private String countryName;
    private Integer supplierId;
    private String supplierName;

    // Constructors
    public SupplierCountry() {
    }

    public SupplierCountry(int supCountryId, Integer countryId, String countryName, Integer supplierId, String supplierName) {
        this.supCountryId = supCountryId;
        this.countryId = countryId;
        this.countryName = countryName;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
    }

    public SupplierCountry(Integer countryId, Integer supplierId) {
        this.countryId = countryId;
        this.supplierId = supplierId;
    }

    public SupplierCountry(int supCountryId, Integer countryId, Integer supplierId) {
        this.supCountryId = supCountryId;
        this.countryId = countryId;
        this.supplierId = supplierId;
    }

    // Getters and Setters
    public int getSupCountryId() {
        return supCountryId;
    }

    public void setSupCountryId(int supCountryId) {
        this.supCountryId = supCountryId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public String toString() {
        return countryName;
    }
}
