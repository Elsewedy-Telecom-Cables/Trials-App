
package com.elsewedyt.trialsapp.dao;

import com.elsewedyt.trialsapp.db.DbConnect;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.models.TrialsView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
public class TrialsViewDAO {

    private static Connection getConnection() {
        return DbConnect.getConnect();
    }
    public static ObservableList<TrialsView> getAllTrialsView() {
        ObservableList<TrialsView> trialsViewList = FXCollections.observableArrayList();
        String sql = "SELECT t.trial_id, t.trial_purpose, t.creation_date AS trial_creation_date, t.notes AS trial_notes, " +
                "s.section_name, m.matrial_name, sup.supplier_name, c.country_name AS supplier_country_name, " +
                "d.department_name, u.full_name AS user_full_name, ft.file_type_name, f.test_situation, " +
                "f.comment, f.creation_date AS file_creation_date, f.file_path " +
                "FROM trials t " +
                "LEFT JOIN sections s ON t.section_id = s.section_id " +
                "LEFT JOIN matrials m ON t.matrial_id = m.matrial_id " +
                "LEFT JOIN suppliers sup ON t.supplier_id = sup.supplier_id " +
                "LEFT JOIN supplier_country sc ON t.sup_country_id = sc.sup_country_id " +
               "LEFT JOIN dbtrials.dbo.countries c ON sc.country_id = c.country_id " +
                "LEFT JOIN files f ON t.trial_id = f.trial_id " +
                "LEFT JOIN users u ON f.user_id = u.user_id " +
                "LEFT JOIN departments d ON f.department_id = d.department_id " +
                "LEFT JOIN file_type ft ON f.file_type_id = ft.file_type_id ";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TrialsView view = new TrialsView();
                view.setTrialId(rs.getInt("trial_id"));
                view.setTrialPurpose(rs.getString("trial_purpose"));
                view.setTrialCreationDate(rs.getDate("trial_creation_date") != null ?
                        rs.getDate("trial_creation_date").toLocalDate() : null);
                view.setTrialNotes(rs.getString("trial_notes"));
                view.setSectionName(rs.getString("section_name"));
                view.setMaterialName(rs.getString("matrial_name"));
                view.setSupplierName(rs.getString("supplier_name"));
                view.setSupplierCountryName(rs.getString("supplier_country_name"));
                view.setDepartmentName(rs.getString("department_name"));
                view.setUserFullName(rs.getString("user_full_name") != null ? rs.getString("user_full_name") : "-");
                view.setFileTypeName(rs.getString("file_type_name"));
                view.setTestSituation(TrialsView.convertTestSituationToString(rs.getObject("test_situation", Integer.class)));
                view.setComment(rs.getString("comment"));
                view.setFileCreationDate(rs.getDate("file_creation_date") != null ?
                        rs.getDate("file_creation_date").toLocalDate() : null);
                view.setFilePath(rs.getString("file_path"));
                trialsViewList.add(view);
            }
        } catch (SQLException e) {
            Logging.logException("ERROR", TrialsViewDAO.class.getName(), "getAllTrialsView", e);
        }
        return trialsViewList;
    }

//    public static ObservableList<TrialsView> searchTrialsView(String trialPurpose, Integer trialId, Integer sectionId, Integer materialId,
//                                                              Integer supplierId, String supplierCountry, LocalDate fromTrialCreationDate,
//                                                              LocalDate toTrialCreationDate, Integer departmentId, String comment,
//                                                              String trialNotes, LocalDate fromFileCreationDate, LocalDate toFileCreationDate,
//                                                              Integer userId) {
//        ObservableList<TrialsView> trialsViewList = FXCollections.observableArrayList();
//        StringBuilder sql = new StringBuilder(
//                "SELECT t.trial_id, t.trial_purpose, t.creation_date AS trial_creation_date, t.notes AS trial_notes, " +
//                        "s.section_name, m.matrial_name, sup.supplier_name, c.country_name AS supplier_country_name, " +
//                        "d.department_name, u.full_name AS user_full_name, ft.file_type_name, f.test_situation, " +
//                        "f.comment, f.creation_date AS file_creation_date, f.file_path " +
//                        "FROM trials t " +
//                        "LEFT JOIN sections s ON t.section_id = s.section_id " +
//                        "LEFT JOIN matrials m ON t.matrial_id = m.matrial_id " +
//                        "LEFT JOIN suppliers sup ON t.supplier_id = sup.supplier_id " +
//                        "LEFT JOIN supplier_country sc ON t.sup_country_id = sc.sup_country_id " +
//                        "LEFT JOIN dbtrials.dbo.countries c ON sc.country_id = c.country_id " +
//                        "LEFT JOIN files f ON t.trial_id = f.trial_id " +
//                        "LEFT JOIN users u ON f.user_id = u.user_id " +
//                        "LEFT JOIN departments d ON f.department_id = d.department_id " +
//                        "LEFT JOIN file_type ft ON f.file_type_id = ft.file_type_id " +
//                        "WHERE 1=1");
//
//        if (trialPurpose != null && !trialPurpose.isEmpty()) {
//            sql.append(" AND t.trial_purpose LIKE ?");
//        }
//        if (trialId != null) {
//            sql.append(" AND t.trial_id = ?");
//        }
//        if (sectionId != null) {
//            sql.append(" AND t.section_id = ?");
//        }
//        if (materialId != null) {
//            sql.append(" AND t.matrial_id = ?");
//        }
//        if (supplierId != null) {
//            sql.append(" AND t.supplier_id = ?");
//        }
//        if (supplierCountry != null && !supplierCountry.isEmpty()) {
//            sql.append(" AND c.country_name LIKE ?");
//        }
//        if (fromTrialCreationDate != null) {
//            sql.append(" AND t.creation_date >= ?");
//        }
//        if (toTrialCreationDate != null) {
//            sql.append(" AND t.creation_date <= ?");
//        }
//        if (departmentId != null) {
//            sql.append(" AND f.department_id = ?");
//        }
//        if (comment != null && !comment.isEmpty()) {
//            sql.append(" AND f.comment LIKE ?");
//        }
//        if (trialNotes != null && !trialNotes.isEmpty()) {
//            sql.append(" AND t.notes LIKE ?");
//        }
//        if (fromFileCreationDate != null) {
//            sql.append(" AND f.creation_date >= ?");
//        }
//        if (toFileCreationDate != null) {
//            sql.append(" AND f.creation_date <= ?");
//        }
//        if (userId != null) {
//            sql.append(" AND f.user_id = ?");
//        }
//
//        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
//            int paramIndex = 1;
//            if (trialPurpose != null && !trialPurpose.isEmpty()) {
//                stmt.setString(paramIndex++, "%" + trialPurpose + "%");
//            }
//            if (trialId != null) {
//                stmt.setInt(paramIndex++, trialId);
//            }
//            if (sectionId != null) {
//                stmt.setInt(paramIndex++, sectionId);
//            }
//            if (materialId != null) {
//                stmt.setInt(paramIndex++, materialId);
//            }
//            if (supplierId != null) {
//                stmt.setInt(paramIndex++, supplierId);
//            }
//            if (supplierCountry != null && !supplierCountry.isEmpty()) {
//                stmt.setString(paramIndex++, "%" + supplierCountry + "%");
//            }
//            if (fromTrialCreationDate != null) {
//                stmt.setDate(paramIndex++, java.sql.Date.valueOf(fromTrialCreationDate));
//            }
//            if (toTrialCreationDate != null) {
//                stmt.setDate(paramIndex++, java.sql.Date.valueOf(toTrialCreationDate));
//            }
//            if (departmentId != null) {
//                stmt.setInt(paramIndex++, departmentId);
//            }
//            if (comment != null && !comment.isEmpty()) {
//                stmt.setString(paramIndex++, "%" + comment + "%");
//            }
//            if (trialNotes != null && !trialNotes.isEmpty()) {
//                stmt.setString(paramIndex++, "%" + trialNotes + "%");
//            }
//            if (fromFileCreationDate != null) {
//                stmt.setDate(paramIndex++, java.sql.Date.valueOf(fromFileCreationDate));
//            }
//            if (toFileCreationDate != null) {
//                stmt.setDate(paramIndex++, java.sql.Date.valueOf(toFileCreationDate));
//            }
//            if (userId != null) {
//                stmt.setInt(paramIndex++, userId);
//            }
//
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                TrialsView view = new TrialsView();
//                view.setTrialId(rs.getInt("trial_id"));
//                view.setTrialPurpose(rs.getString("trial_purpose"));
//                view.setTrialCreationDate(rs.getDate("trial_creation_date") != null ?
//                        rs.getDate("trial_creation_date").toLocalDate() : null);
//                view.setTrialNotes(rs.getString("trial_notes"));
//                view.setSectionName(rs.getString("section_name"));
//                view.setMaterialName(rs.getString("matrial_name"));
//                view.setSupplierName(rs.getString("supplier_name"));
//                view.setSupplierCountryName(rs.getString("supplier_country_name"));
//                view.setDepartmentName(rs.getString("department_name"));
//                view.setUserFullName(rs.getString("user_full_name") != null ? rs.getString("user_full_name") : "-");
//                view.setFileTypeName(rs.getString("file_type_name"));
//                view.setTestSituation(TrialsView.convertTestSituationToString(rs.getObject("test_situation", Integer.class)));
//                view.setComment(rs.getString("comment"));
//                view.setFileCreationDate(rs.getDate("file_creation_date") != null ?
//                        rs.getDate("file_creation_date").toLocalDate() : null);
//                view.setFilePath(rs.getString("file_path"));
//                trialsViewList.add(view);
//            }
//        } catch (SQLException e) {
//            Logging.logException("ERROR", TrialsViewDAO.class.getName(), "searchTrialsView", e);
//        }
//        return trialsViewList;
//    }

    public static ObservableList<TrialsView> searchTrialsView(String trialPurpose, Integer trialId, Integer sectionId, Integer materialId,
                                                              Integer supplierId, String supplierCountry, LocalDate fromTrialCreationDate,
                                                              LocalDate toTrialCreationDate, Integer departmentId,Integer userId) {
        ObservableList<TrialsView> trialsViewList = FXCollections.observableArrayList();
        StringBuilder sql = new StringBuilder(
                "SELECT t.trial_id, t.trial_purpose, t.creation_date AS trial_creation_date, t.notes AS trial_notes, " +
                        "s.section_name, m.matrial_name, sup.supplier_name, c.country_name AS supplier_country_name, " +
                        "d.department_name, u.full_name AS user_full_name, ft.file_type_name, f.test_situation, " +
                        "f.comment, f.creation_date AS file_creation_date, f.file_path " +
                        "FROM trials t " +
                        "LEFT JOIN sections s ON t.section_id = s.section_id " +
                        "LEFT JOIN matrials m ON t.matrial_id = m.matrial_id " +
                        "LEFT JOIN suppliers sup ON t.supplier_id = sup.supplier_id " +
                        "LEFT JOIN supplier_country sc ON t.sup_country_id = sc.sup_country_id " +
                        "LEFT JOIN dbtrials.dbo.countries c ON sc.country_id = c.country_id " +
                        "LEFT JOIN files f ON t.trial_id = f.trial_id " +
                        "LEFT JOIN users u ON f.user_id = u.user_id " +
                        "LEFT JOIN departments d ON f.department_id = d.department_id " +
                        "LEFT JOIN file_type ft ON f.file_type_id = ft.file_type_id " +
                        "WHERE 1=1");

        if (trialPurpose != null && !trialPurpose.isEmpty()) {
            sql.append(" AND t.trial_purpose LIKE ?");
        }
        if (trialId != null) {
            sql.append(" AND t.trial_id = ?");
        }
        if (sectionId != null) {
            sql.append(" AND t.section_id = ?");
        }
        if (materialId != null) {
            sql.append(" AND t.matrial_id = ?");
        }
        if (supplierId != null) {
            sql.append(" AND t.supplier_id = ?");
        }
        if (supplierCountry != null && !supplierCountry.isEmpty()) {
            sql.append(" AND c.country_name LIKE ?");
        }
        if (fromTrialCreationDate != null) {
            sql.append(" AND t.creation_date >= ?");
        }
        if (toTrialCreationDate != null) {
            sql.append(" AND t.creation_date <= ?");
        }
        if (departmentId != null) {
            sql.append(" AND f.department_id = ?");
        }
//        if (comment != null && !comment.isEmpty()) {
//            sql.append(" AND f.comment LIKE ?");
//        }
//        if (trialNotes != null && !trialNotes.isEmpty()) {
//            sql.append(" AND t.notes LIKE ?");
//        }
//        if (fromFileCreationDate != null) {
//            sql.append(" AND f.creation_date >= ?");
//        }
//        if (toFileCreationDate != null) {
//            sql.append(" AND f.creation_date <= ?");
//        }
        if (userId != null) {
            sql.append(" AND f.user_id = ?");
        }

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (trialPurpose != null && !trialPurpose.isEmpty()) {
                stmt.setString(paramIndex++, "%" + trialPurpose + "%");
            }
            if (trialId != null) {
                stmt.setInt(paramIndex++, trialId);
            }
            if (sectionId != null) {
                stmt.setInt(paramIndex++, sectionId);
            }
            if (materialId != null) {
                stmt.setInt(paramIndex++, materialId);
            }
            if (supplierId != null) {
                stmt.setInt(paramIndex++, supplierId);
            }
            if (supplierCountry != null && !supplierCountry.isEmpty()) {
                stmt.setString(paramIndex++, "%" + supplierCountry + "%");
            }
            if (fromTrialCreationDate != null) {
                stmt.setDate(paramIndex++, java.sql.Date.valueOf(fromTrialCreationDate));
            }
            if (toTrialCreationDate != null) {
                stmt.setDate(paramIndex++, java.sql.Date.valueOf(toTrialCreationDate));
            }
            if (departmentId != null) {
                stmt.setInt(paramIndex++, departmentId);
            }
//            if (comment != null && !comment.isEmpty()) {
//                stmt.setString(paramIndex++, "%" + comment + "%");
//            }
//            if (trialNotes != null && !trialNotes.isEmpty()) {
//                stmt.setString(paramIndex++, "%" + trialNotes + "%");
//            }
//            if (fromFileCreationDate != null) {
//                stmt.setDate(paramIndex++, java.sql.Date.valueOf(fromFileCreationDate));
//            }
//            if (toFileCreationDate != null) {
//                stmt.setDate(paramIndex++, java.sql.Date.valueOf(toFileCreationDate));
//            }
            if (userId != null) {
                stmt.setInt(paramIndex++, userId);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TrialsView view = new TrialsView();
                view.setTrialId(rs.getInt("trial_id"));
                view.setTrialPurpose(rs.getString("trial_purpose"));
                view.setTrialCreationDate(rs.getDate("trial_creation_date") != null ?
                        rs.getDate("trial_creation_date").toLocalDate() : null);
                view.setTrialNotes(rs.getString("trial_notes"));
                view.setSectionName(rs.getString("section_name"));
                view.setMaterialName(rs.getString("matrial_name"));
                view.setSupplierName(rs.getString("supplier_name"));
                view.setSupplierCountryName(rs.getString("supplier_country_name"));
                view.setDepartmentName(rs.getString("department_name"));
                view.setUserFullName(rs.getString("user_full_name") != null ? rs.getString("user_full_name") : "-");
                view.setFileTypeName(rs.getString("file_type_name"));
                view.setTestSituation(TrialsView.convertTestSituationToString(rs.getObject("test_situation", Integer.class)));
                view.setComment(rs.getString("comment"));
                view.setFileCreationDate(rs.getDate("file_creation_date") != null ?
                        rs.getDate("file_creation_date").toLocalDate() : null);
                view.setFilePath(rs.getString("file_path"));
                trialsViewList.add(view);
            }
        } catch (SQLException e) {
            Logging.logException("ERROR", TrialsViewDAO.class.getName(), "searchTrialsView", e);
        }
        return trialsViewList;
    }

}
