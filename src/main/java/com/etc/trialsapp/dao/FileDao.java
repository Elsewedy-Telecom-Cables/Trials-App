package com.etc.trialsapp.dao;

import com.etc.trialsapp.db.DbConnect;
import com.etc.trialsapp.logging.Logging;
import com.etc.trialsapp.model.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class FileDao {

    // Get all files
    public  ObservableList<File> getAllFiles() {
        ObservableList<File> list = FXCollections.observableArrayList();
        String query = """
            SELECT f.file_id, f.creation_date, f.file_path, f.test_situation, f.trial_id, f.department_id, f.user_id, f.comment, f.file_type_id,
                   t.trial_purpose, d.department_name, ft.file_type_name
            FROM dbtrials.dbo.files f
            LEFT JOIN dbtrials.dbo.trials t ON f.trial_id = t.trial_id
            LEFT JOIN dbtrials.dbo.departments d ON f.department_id = d.department_id
            LEFT JOIN dbtrials.dbo.file_type ft ON f.file_type_id = ft.file_type_id
            ORDER BY f.file_id ASC
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                File file = new File();
                file.setFileId(rs.getInt("file_id"));
                Timestamp timestamp = rs.getTimestamp("creation_date");
                file.setCreationDate(timestamp != null ? timestamp.toLocalDateTime() : null);
                file.setFilePath(rs.getString("file_path"));
                file.setTestSituation(rs.getObject("test_situation") != null ? rs.getInt("test_situation") : null);
                file.setTrialId(rs.getObject("trial_id") != null ? rs.getInt("trial_id") : null);
                file.setDepartmentId(rs.getObject("department_id") != null ? rs.getInt("department_id") : null);
                file.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
                file.setComment(rs.getString("comment"));
                file.setFileTypeId(rs.getObject("file_type_id") != null ? rs.getInt("file_type_id") : null);
                file.setTrialPurpose(rs.getString("trial_purpose"));
                file.setDepartmentName(rs.getString("department_name"));
                file.setFileTypeName(rs.getString("file_type_name"));
                list.add(file);
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "getAllFiles", e, "sql", query);
        }

        return list;
    }

    // Get files by trial ID
    public  ObservableList<File> getFilesByTrialId(int trialId) {
        ObservableList<File> list = FXCollections.observableArrayList();
        String query = """
            SELECT f.file_id, f.creation_date, f.file_path, f.test_situation, f.trial_id, f.department_id, f.user_id, f.comment, f.file_type_id,
                   t.trial_purpose, d.department_name, ft.file_type_name
            FROM dbtrials.dbo.files f
            LEFT JOIN dbtrials.dbo.trials t ON f.trial_id = t.trial_id
            LEFT JOIN dbtrials.dbo.departments d ON f.department_id = d.department_id
            LEFT JOIN dbtrials.dbo.file_type ft ON f.file_type_id = ft.file_type_id
            WHERE f.trial_id = ?
            ORDER BY f.file_id ASC
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, trialId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    File file = new File();
                    file.setFileId(rs.getInt("file_id"));
                    Timestamp timestamp = rs.getTimestamp("creation_date");
                    file.setCreationDate(timestamp != null ? timestamp.toLocalDateTime() : null);
                    file.setFilePath(rs.getString("file_path"));
                    file.setTestSituation(rs.getObject("test_situation") != null ? rs.getInt("test_situation") : null);
                    file.setTrialId(rs.getObject("trial_id") != null ? rs.getInt("trial_id") : null);
                    file.setDepartmentId(rs.getObject("department_id") != null ? rs.getInt("department_id") : null);
                    file.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
                    file.setComment(rs.getString("comment"));
                    file.setFileTypeId(rs.getObject("file_type_id") != null ? rs.getInt("file_type_id") : null);
                    file.setTrialPurpose(rs.getString("trial_purpose"));
                    file.setDepartmentName(rs.getString("department_name"));
                    file.setFileTypeName(rs.getString("file_type_name"));
                    list.add(file);
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "getFilesByTrialId", e, "sql", query);
        }

        return list;
    }
    // Get files by Department ID
    public  ObservableList<File> getFilesByDepartmentId(int departmrntId) {
        ObservableList<File> list = FXCollections.observableArrayList();
        String query = """
            SELECT f.file_id, f.creation_date, f.file_path, f.test_situation, f.trial_id, f.department_id, f.user_id, f.comment, f.file_type_id,
                   t.trial_purpose, d.department_name, ft.file_type_name
            FROM dbtrials.dbo.files f
            LEFT JOIN dbtrials.dbo.trials t ON f.trial_id = t.trial_id
            LEFT JOIN dbtrials.dbo.departments d ON f.department_id = d.department_id
            LEFT JOIN dbtrials.dbo.file_type ft ON f.file_type_id = ft.file_type_id
            WHERE f.department_id = ?
            ORDER BY f.file_id ASC
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, departmrntId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    File file = new File();
                    file.setFileId(rs.getInt("file_id"));
                    Timestamp timestamp = rs.getTimestamp("creation_date");
                    file.setCreationDate(timestamp != null ? timestamp.toLocalDateTime() : null);
                    file.setFilePath(rs.getString("file_path"));
                    file.setTestSituation(rs.getObject("test_situation") != null ? rs.getInt("test_situation") : null);
                    file.setTrialId(rs.getObject("trial_id") != null ? rs.getInt("trial_id") : null);
                    file.setDepartmentId(rs.getObject("department_id") != null ? rs.getInt("department_id") : null);
                    file.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
                    file.setComment(rs.getString("comment"));
                    file.setFileTypeId(rs.getObject("file_type_id") != null ? rs.getInt("file_type_id") : null);
                    file.setTrialPurpose(rs.getString("trial_purpose"));
                    file.setDepartmentName(rs.getString("department_name"));
                    file.setFileTypeName(rs.getString("file_type_name"));
                    list.add(file);
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "getFilesByDepartmentId", e, "sql", query);
        }

        return list;
    }

    // Insert a new file
    public  boolean insertFile(File file) {
        String query = """
            INSERT INTO dbtrials.dbo.files (creation_date, file_path, test_situation, trial_id, department_id, user_id, comment, file_type_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {
            LocalDateTime creationDate = file.getCreationDate() != null ? file.getCreationDate() : LocalDateTime.now();
            ps.setTimestamp(1, Timestamp.valueOf(creationDate));
            ps.setString(2, file.getFilePath());
            ps.setObject(3, file.getTestSituation());
            ps.setObject(4, file.getTrialId());
            ps.setObject(5, file.getDepartmentId());
            ps.setObject(6, file.getUserId());
            ps.setString(7, file.getComment());
            ps.setObject(8, file.getFileTypeId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "insertFile", e, "sql", query);
        }

        return false;
    }

    // Update an existing file
    public  boolean updateFile(File file) {
        String query = """
            UPDATE dbtrials.dbo.files
            SET creation_date = ?, file_path = ?, test_situation = ?, trial_id = ?, department_id = ?, user_id = ?, comment = ?, file_type_id = ?
            WHERE file_id = ?
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            LocalDateTime creationDate = file.getCreationDate() != null ? file.getCreationDate() : LocalDateTime.now();
            ps.setTimestamp(1, Timestamp.valueOf(creationDate));
            ps.setString(2, file.getFilePath());
            ps.setObject(3, file.getTestSituation());
            ps.setObject(4, file.getTrialId());
            ps.setObject(5, file.getDepartmentId());
            ps.setObject(6, file.getUserId());
            ps.setString(7, file.getComment());
            ps.setObject(8, file.getFileTypeId());
            ps.setInt(9, file.getFileId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "updateFile", e, "sql", query);
        }

        return false;
    }

    // Delete a file
    public  boolean deleteFile(int fileId) {
        String query = "DELETE FROM dbtrials.dbo.files WHERE file_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, fileId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "deleteFile", e, "sql", query);
        }

        return false;
    }

    // Get file by ID
    public  File getFileById(int fileId) {
        String query = """
            SELECT f.file_id, f.creation_date, f.file_path, f.test_situation, f.trial_id, f.department_id, f.user_id, f.comment, f.file_type_id,
                   t.trial_purpose, d.department_name, ft.file_type_name
            FROM dbtrials.dbo.files f
            LEFT JOIN dbtrials.dbo.trials t ON f.trial_id = t.trial_id
            LEFT JOIN dbtrials.dbo.departments d ON f.department_id = d.department_id
            LEFT JOIN dbtrials.dbo.file_type ft ON f.file_type_id = ft.file_type_id
            WHERE f.file_id = ?
        """;
        File file = null;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, fileId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    file = new File();
                    file.setFileId(rs.getInt("file_id"));
                    Date sqlDate = rs.getDate("creation_date");
                    Timestamp timestamp = rs.getTimestamp("creation_date");
                    file.setCreationDate(timestamp != null ? timestamp.toLocalDateTime() : null);
                    file.setTestSituation(rs.getObject("test_situation") != null ? rs.getInt("test_situation") : null);
                    file.setTrialId(rs.getObject("trial_id") != null ? rs.getInt("trial_id") : null);
                    file.setDepartmentId(rs.getObject("department_id") != null ? rs.getInt("department_id") : null);
                    file.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
                    file.setComment(rs.getString("comment"));
                    file.setFileTypeId(rs.getObject("file_type_id") != null ? rs.getInt("file_type_id") : null);
                    file.setTrialPurpose(rs.getString("trial_purpose"));
                    file.setDepartmentName(rs.getString("department_name"));
                    file.setFileTypeName(rs.getString("file_type_name"));
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "getFileById", e, "sql", query);
        }

        return file;
    }
    // Get file by file path
    public  File getFileByFilePath(String filePath) {
        String query = """
        SELECT f.file_id, f.creation_date, f.file_path, f.test_situation, f.trial_id, f.department_id, f.user_id, f.comment, f.file_type_id,
               t.trial_purpose, d.department_name, ft.file_type_name
        FROM dbtrials.dbo.files f
        LEFT JOIN dbtrials.dbo.trials t ON f.trial_id = t.trial_id
        LEFT JOIN dbtrials.dbo.departments d ON f.department_id = d.department_id
        LEFT JOIN dbtrials.dbo.file_type ft ON f.file_type_id = ft.file_type_id
        WHERE f.file_path = ?
    """;
        File file = null;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, filePath);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    file = new File();
                    file.setFileId(rs.getInt("file_id"));
                    Timestamp timestamp = rs.getTimestamp("creation_date");
                    file.setCreationDate(timestamp != null ? timestamp.toLocalDateTime() : null);
                    file.setFilePath(rs.getString("file_path"));
                    file.setTestSituation(rs.getObject("test_situation") != null ? rs.getInt("test_situation") : null);
                    file.setTrialId(rs.getObject("trial_id") != null ? rs.getInt("trial_id") : null);
                    file.setDepartmentId(rs.getObject("department_id") != null ? rs.getInt("department_id") : null);
                    file.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
                    file.setComment(rs.getString("comment"));
                    file.setFileTypeId(rs.getObject("file_type_id") != null ? rs.getInt("file_type_id") : null);
                    file.setTrialPurpose(rs.getString("trial_purpose"));
                    file.setDepartmentName(rs.getString("department_name"));
                    file.setFileTypeName(rs.getString("file_type_name"));
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "getFileByFilePath", e, "sql", query);
        }

        return file;
    }

    //Check if any files exist for a specific trial
    public  boolean hasFilesForTrial(int trialId) {
        String query = "SELECT COUNT(*) FROM dbtrials.dbo.files WHERE trial_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, trialId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "hasFilesForTrial", e, "sql", query);
         //   System.out.println("Error in hasFilesForTrial: " + e.getMessage());

        }

        return false;
    }

    // Check if any files exist for a specific trial and department
    public  boolean hasFilesForTrialAndDepartment(int trialId, int departmentId) {
        String query = "SELECT COUNT(*) FROM dbtrials.dbo.files WHERE trial_id = ? AND department_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, trialId);
            ps.setInt(2, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "hasFilesForTrialAndDepartment", e, "sql", query);
       //     System.out.println("Error in hasFilesForTrialAndDepartment: " + e.getMessage());
        }

        return false;
    }

    public  ObservableList<File> getFilesByTrialAndDepartment(int trialId, int deptId) {
        ObservableList<File> list = FXCollections.observableArrayList();
        String query = """
            SELECT f.file_id, f.creation_date, f.file_path, f.test_situation, f.trial_id, f.department_id, f.user_id, f.comment, f.file_type_id,
                   t.trial_purpose, d.department_name, ft.file_type_name
            FROM dbtrials.dbo.files f
            LEFT JOIN dbtrials.dbo.trials t ON f.trial_id = t.trial_id
            LEFT JOIN dbtrials.dbo.departments d ON f.department_id = d.department_id
            LEFT JOIN dbtrials.dbo.file_type ft ON f.file_type_id = ft.file_type_id
            WHERE  t.trial_id = ? AND f.department_id = ?
            ORDER BY f.file_id ASC
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, trialId);
            ps.setInt(2, deptId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    File file = new File();
                    file.setFileId(rs.getInt("file_id"));
                    Timestamp timestamp = rs.getTimestamp("creation_date");
                    file.setCreationDate(timestamp != null ? timestamp.toLocalDateTime() : null);
                    file.setFilePath(rs.getString("file_path"));
                    file.setTestSituation(rs.getObject("test_situation") != null ? rs.getInt("test_situation") : null);
                    file.setTrialId(rs.getObject("trial_id") != null ? rs.getInt("trial_id") : null);
                    file.setDepartmentId(rs.getObject("department_id") != null ? rs.getInt("department_id") : null);
                    file.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
                    file.setComment(rs.getString("comment"));
                    file.setFileTypeId(rs.getObject("file_type_id") != null ? rs.getInt("file_type_id") : null);
                    file.setTrialPurpose(rs.getString("trial_purpose"));
                    file.setDepartmentName(rs.getString("department_name"));
                    file.setFileTypeName(rs.getString("file_type_name"));
                    list.add(file);
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "getFilesByTrialAndDepartment", e, "sql", query);
        }

        return list;
    }
}