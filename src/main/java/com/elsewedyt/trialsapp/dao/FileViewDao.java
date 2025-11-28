
package com.elsewedyt.trialsapp.dao;
import com.elsewedyt.trialsapp.db.DbConnect;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.model.FileView;
import com.elsewedyt.trialsapp.model.TrialsView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class FileViewDao {

    // Get all files for a specific trial ID
    public  ObservableList<FileView> getFilesByTrialId(int trialId) {
        ObservableList<FileView> list = FXCollections.observableArrayList();
        String query = """
            SELECT f.file_id, f.creation_date, f.file_path, f.test_situation, f.trial_id, f.department_id, f.user_id, f.comment, f.file_type_id,
                   d.department_name, u.full_name, ft.file_type_name
            FROM dbtrials.dbo.files f
            LEFT JOIN dbtrials.dbo.departments d ON f.department_id = d.department_id
            LEFT JOIN dbtrials.dbo.users u ON f.user_id = u.user_id
            LEFT JOIN dbtrials.dbo.file_type ft ON f.file_type_id = ft.file_type_id
            WHERE f.trial_id = ?
            ORDER BY f.file_id ASC
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, trialId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FileView file = new FileView();
                    file.setFileId(rs.getInt("file_id"));
                    Timestamp timestamp = rs.getTimestamp("creation_date");
                    file.setFileCreationDate(timestamp != null ? timestamp.toLocalDateTime() : null);
                    file.setFilePath(rs.getString("file_path"));
                    file.setTestSituation(TrialsView.convertTestSituationToString(rs.getObject("test_situation", Integer.class)));
                    file.setTrialId(rs.getInt("trial_id"));
                    file.setDepartmentName(rs.getString("department_name"));
                    file.setUserFullName(rs.getString("full_name") != null ? rs.getString("full_name") : "-");
                    file.setFileTypeName(rs.getString("file_type_name"));
                    file.setComment(rs.getString("comment"));
                    list.add(file);
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileViewDao.class.getName(), "getFilesByTrialId", e, "sql", query);
        }

        return list;
    }

    // Get files for a specific trial ID and department name
    public  ObservableList<FileView> getFilesByTrialIdAndDepartmentName(int trialId, String departmentName) {
        ObservableList<FileView> list = FXCollections.observableArrayList();
        String query = """
            SELECT f.file_id, f.creation_date, f.file_path, f.test_situation,
                   f.trial_id, f.department_id, f.user_id, f.comment, f.file_type_id,
                   d.department_name,
                   u.full_name,
                   ft.file_type_name
            FROM dbtrials.dbo.files f
            LEFT JOIN dbtrials.dbo.departments d ON f.department_id = d.department_id
            LEFT JOIN dbtrials.dbo.users u ON f.user_id = u.user_id
            LEFT JOIN dbtrials.dbo.file_type ft ON f.file_type_id = ft.file_type_id
            WHERE f.trial_id = ? AND d.department_name = ?
            ORDER BY f.file_id ASC
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, trialId);
            ps.setString(2, departmentName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FileView file = new FileView();
                    file.setFileId(rs.getInt("file_id"));
                    Timestamp timestamp = rs.getTimestamp("creation_date");
                    file.setFileCreationDate(timestamp != null ? timestamp.toLocalDateTime() : null);
                    file.setFilePath(rs.getString("file_path"));
                    file.setTestSituation(TrialsView.convertTestSituationToString(rs.getObject("test_situation", Integer.class)));
                    file.setTrialId(rs.getInt("trial_id"));
                    file.setDepartmentName(rs.getString("department_name"));
                    file.setUserFullName(rs.getString("full_name") != null ? rs.getString("full_name") : "-");
                    file.setFileTypeName(rs.getString("file_type_name"));
                    file.setComment(rs.getString("comment"));
                    list.add(file);
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileViewDao.class.getName(), "getFilesByTrialIdAndDepartmentName", e, "sql", query);
        }

        return list;
    }
}