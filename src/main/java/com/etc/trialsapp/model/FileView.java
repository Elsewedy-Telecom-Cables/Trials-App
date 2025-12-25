package com.etc.trialsapp.model;

import java.time.LocalDateTime ;

public class FileView {
    private int FileId;
    private int trialId;
    private String departmentName;
    private String userFullName;
    private String fileTypeName;
    private String testSituation;
    private String comment;
    private LocalDateTime  fileCreationDate;
    private String filePath;

    public int getFileId() {
        return FileId;
    }

    public void setFileId(int fileId) {
        FileId = fileId;
    }

    public int getTrialId() {
        return trialId;
    }

    public void setTrialId(int trialId) {
        this.trialId = trialId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getFileTypeName() {
        return fileTypeName;
    }

    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    public String getTestSituation() {
        return testSituation;
    }

    public void setTestSituation(String testSituation) {
        this.testSituation = testSituation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime  getFileCreationDate() {
        return fileCreationDate;
    }

    public void setFileCreationDate(LocalDateTime  fileCreationDate) {
        this.fileCreationDate = fileCreationDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public static String convertTestSituationToString2(Integer testSituation) {
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
