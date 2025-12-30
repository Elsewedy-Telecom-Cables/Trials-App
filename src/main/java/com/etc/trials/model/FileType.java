package com.etc.trials.model;

public class FileType {
    private int fileTypeId;
    private String fileTypeName;
    private int departmentId;
    private String departmentName; // Optional - useful for joins/display

    // Constructors
    public FileType() {
    }

    public FileType(int fileTypeId, String fileTypeName, int departmentId) {
        this.fileTypeId = fileTypeId;
        this.fileTypeName = fileTypeName;
        this.departmentId = departmentId;
    }

    public FileType(int fileTypeId, String fileTypeName, int departmentId, String departmentName) {
        this.fileTypeId = fileTypeId;
        this.fileTypeName = fileTypeName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    // Getters and Setters
    public int getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(int fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public String getFileTypeName() {
        return fileTypeName;
    }

    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return fileTypeName;
    }
}

