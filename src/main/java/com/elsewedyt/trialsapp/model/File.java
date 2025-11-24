package com.elsewedyt.trialsapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class File {
    // Fields from files table
    private int fileId;
    private LocalDateTime creationDate;
    private String filePath;
    private Integer testSituation;
    private Integer trialId;
    private Integer departmentId;
    private Integer userId;
    private String comment;
    private Integer fileTypeId;


    // Related objects for foreign key relationships
    private Trial trial;
    private Department department;
    private User user;
    private FileType fileType;

    // Additional fields for names
    private String trialPurpose;
    private String departmentName;
    private String fileTypeName;

    // Default constructor
    public File() {
    }

    // Constructor with essential fields
    public File(LocalDateTime  creationDate, String filePath, Integer testSituation, Integer trialId, Integer departmentId, Integer userId, String comment, Integer fileTypeId) {
        this.creationDate = creationDate;
        this.filePath = filePath;
        this.testSituation = testSituation;
        this.trialId = trialId;
        this.departmentId = departmentId;
        this.userId = userId;
        this.comment = comment;
        this.fileTypeId = fileTypeId;
    }

    // Full constructor including related objects and names
    public File(int fileId, LocalDateTime  creationDate, String filePath, Integer testSituation, Integer trialId, Integer departmentId, Integer userId, String comment, Integer fileTypeId, Trial trial, Department department, User user, FileType fileType) {
        this.fileId = fileId;
        this.creationDate = creationDate;
        this.filePath = filePath;
        this.testSituation = testSituation;
        this.trialId = trialId;
        this.departmentId = departmentId;
        this.userId = userId;
        this.comment = comment;
        this.fileTypeId = fileTypeId;
        this.trial = trial;
        this.department = department;
        this.user = user;
        this.fileType = fileType;
        // Set name fields from related objects
        this.trialPurpose = trial != null ? trial.getTrialPurpose() : null;
        this.departmentName = department != null ? department.getDepartmentName() : null;
        this.fileTypeName = fileType != null ? fileType.getFileTypeName() : null;
    }


    // Getters and Setters
    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getTestSituation() {
        return testSituation;
    }

    public void setTestSituation(Integer testSituation) {
        this.testSituation = testSituation;
    }

    public Integer getTrialId() {
        return trialId;
    }

    public void setTrialId(Integer trialId) {
        this.trialId = trialId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(Integer fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public Trial getTrial() {
        return trial;
    }

    public void setTrial(Trial trial) {
        this.trial = trial;
        this.trialPurpose = trial != null ? trial.getTrialPurpose() : null;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
        this.departmentName = department != null ? department.getDepartmentName() : null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
        this.fileTypeName = fileType != null ? fileType.getFileTypeName() : null;
    }

    public String getTrialPurpose() {
        return trialPurpose;
    }

    public void setTrialPurpose(String trialPurpose) {
        this.trialPurpose = trialPurpose;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getFileTypeName() {
        return fileTypeName;
    }

    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    // toString for display in JavaFX components like ComboBox
    @Override
    public String toString() {
        return filePath != null ? filePath : "File " + fileId;
    }
}