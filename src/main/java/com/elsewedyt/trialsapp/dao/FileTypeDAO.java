package com.elsewedyt.trialsapp.dao;

import com.elsewedyt.trialsapp.db.DbConnect;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.models.FileType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class FileTypeDAO {
    public static String lastErrorMessage = null;

    // Insert
    public static boolean insertFileType(FileType fileType) {
        String query = "INSERT INTO dbtrials.dbo.file_type (file_type_name, department_id) VALUES (?, ?)";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, fileType.getFileTypeName());
            ps.setInt(2, fileType.getDepartmentId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", FileTypeDAO.class.getName(), "insertFileType", e, "sql", query);
        }

        return false;
    }

    // Update
    public static boolean updateFileType(FileType fileType) {
        String query = "UPDATE dbtrials.dbo.file_type SET file_type_name = ?, department_id = ? WHERE file_type_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, fileType.getFileTypeName());
            ps.setInt(2, fileType.getDepartmentId());
            ps.setInt(3, fileType.getFileTypeId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", FileTypeDAO.class.getName(), "updateFileType", e, "sql", query);
        }

        return false;
    }

    // Delete
    public static boolean deleteFileType(int fileTypeId) {
        String query = "DELETE FROM dbtrials.dbo.file_type WHERE file_type_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, fileTypeId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileTypeDAO.class.getName(), "deleteFileType", e, "sql", query);
        }

        return false;
    }

    // Get by ID
    public static FileType getFileTypeById(int id) {
        String query = """
                SELECT f.file_type_id, f.file_type_name, f.department_id, d.department_name
                FROM dbtrials.dbo.file_type f
                LEFT JOIN dbtrials.dbo.departments d ON f.department_id = d.department_id
                WHERE f.file_type_id = ?
                """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new FileType(
                            rs.getInt("file_type_id"),
                            rs.getString("file_type_name"),
                            rs.getInt("department_id"),
                            rs.getString("department_name")
                    );
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileTypeDAO.class.getName(), "getFileTypeById", e, "sql", query);
        }

        return null;
    }

    // Get all file types (with department name)
    public static ObservableList<FileType> getAllFileTypes() {
        ObservableList<FileType> list = FXCollections.observableArrayList();
        String query = """
                SELECT f.file_type_id, f.file_type_name, f.department_id, d.department_name
                FROM dbtrials.dbo.file_type f
                LEFT JOIN dbtrials.dbo.departments d ON f.department_id = d.department_id
                 ORDER BY f.department_id ASC , f.file_type_id ASC
                """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                FileType f = new FileType(
                        rs.getInt("file_type_id"),
                        rs.getString("file_type_name"),
                        rs.getInt("department_id"),
                        rs.getString("department_name")
                );
                list.add(f);
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileTypeDAO.class.getName(), "getAllFileTypes", e, "sql", query);
        }

        return list;
    }
    public static FileType getFileTypeByDepartmentId(int departmentId) {
        String query = """
                SELECT f.file_type_id, f.file_type_name, f.department_id, d.department_name
                FROM dbtrials.dbo.file_type f
                LEFT JOIN dbtrials.dbo.departments d ON f.department_id = d.department_id
                WHERE f.department_id = ?
                """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new FileType(
                            rs.getInt("file_type_id"),
                            rs.getString("file_type_name"),
                            rs.getInt("department_id"),
                            rs.getString("department_name")
                    );
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", FileTypeDAO.class.getName(), "getFileTypeByDepartmentId", e, "sql", query);
        }

        return null;
    }
}
