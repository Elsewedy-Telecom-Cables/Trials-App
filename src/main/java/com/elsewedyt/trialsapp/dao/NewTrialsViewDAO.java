//package com.elsewedyt.trialsapp.dao;
//import com.elsewedyt.trialsapp.db.DbConnect;
//import com.elsewedyt.trialsapp.logging.Logging;
//import com.elsewedyt.trialsapp.models.NewTrialsView;
//import com.elsewedyt.trialsapp.models.TrialsView;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//public class NewTrialsViewDAO {
//
//    private static Connection getConnection() {
//        return DbConnect.getConnect();
//    }
//    // new
//    public static ObservableList<NewTrialsView> newGetAllTrialsView() {
//        ObservableList<NewTrialsView> trialsViewList = FXCollections.observableArrayList();
//        String sql = "SELECT t.trial_id, t.trial_purpose, t.creation_date AS trial_creation_date, t.notes AS trial_notes, " +
//                "s.section_name, m.matrial_name, sup.supplier_name, c.country_name AS supplier_country_name, " +
//                "d.department_name, u.full_name AS user_full_name, ft.file_type_name, f.test_situation, " +
//                "f.comment, f.creation_date AS file_creation_date, f.file_path " +
//                "FROM trials t " +
//                "LEFT JOIN sections s ON t.section_id = s.section_id " +
//                "LEFT JOIN matrials m ON t.matrial_id = m.matrial_id " +
//                "LEFT JOIN suppliers sup ON t.supplier_id = sup.supplier_id " +
//                "LEFT JOIN supplier_country sc ON t.sup_country_id = sc.sup_country_id " +
//                "LEFT JOIN dbtrials.dbo.countries c ON sc.country_id = c.country_id " +
//                "LEFT JOIN files f ON t.trial_id = f.trial_id " +
//                "LEFT JOIN users u ON f.user_id = u.user_id " +
//                "LEFT JOIN departments d ON f.department_id = d.department_id " +
//                "LEFT JOIN file_type ft ON f.file_type_id = ft.file_type_id ";
//        //"ORDER BY t.trial_id DESC , d.department_id ASC";
//
//        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                NewTrialsView view = new NewTrialsView();
//                view.setTrialId(rs.getInt("trial_id"));
//                view.setTrialPurpose(rs.getString("trial_purpose"));
//                view.setTrialCreationDate(rs.getDate("trial_creation_date") != null ?
//                        rs.getDate("trial_creation_date").toLocalDate() : null);
//                view.setTrialNotes(rs.getString("trial_notes"));
//                view.setSectionName(rs.getString("section_name"));
//                view.setMaterialName(rs.getString("matrial_name"));
//                view.setSupplierName(rs.getString("supplier_name"));
//                view.setSupplierCountryName(rs.getString("supplier_country_name"));
//                trialsViewList.add(view);
//            }
//        } catch (SQLException e) {
//            Logging.logException("ERROR", TrialsViewDAO.class.getName(), "getAllTrialsView", e);
//        }
//        return trialsViewList;
//    }
//
//    // new
//    public static ObservableList<NewTrialsView> newSearchTrialsView(String trialPurpose, Integer trialId, Integer sectionId, Integer materialId,
//                                                                    Integer supplierId, String supplierCountry, LocalDate fromTrialCreationDate,
//                                                                    LocalDate toTrialCreationDate, Integer departmentId) {
//        ObservableList<NewTrialsView> trialsViewList = FXCollections.observableArrayList();
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
//
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
//
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                NewTrialsView view = new NewTrialsView();
//                view.setTrialId(rs.getInt("trial_id"));
//                view.setTrialPurpose(rs.getString("trial_purpose"));
//                view.setTrialCreationDate(rs.getDate("trial_creation_date") != null ?
//                        rs.getDate("trial_creation_date").toLocalDate() : null);
//                view.setTrialNotes(rs.getString("trial_notes"));
//                view.setSectionName(rs.getString("section_name"));
//                view.setMaterialName(rs.getString("matrial_name"));
//                view.setSupplierName(rs.getString("supplier_name"));
//                view.setSupplierCountryName(rs.getString("supplier_country_name"));
//                trialsViewList.add(view);
//            }
//        } catch (SQLException e) {
//            Logging.logException("ERROR", TrialsViewDAO.class.getName(), "searchTrialsView", e);
//        }
//        return trialsViewList;
//    }
//



package com.elsewedyt.trialsapp.dao;

import com.elsewedyt.trialsapp.db.DbConnect;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.models.NewTrialsView;
import com.elsewedyt.trialsapp.models.TrialsView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class NewTrialsViewDAO {

    private static Connection getConnection() {
        return DbConnect.getConnect();
    }

    // Get all trial files and transform them into NewTrialsView
    public static ObservableList<NewTrialsView> getAllTrialsView() {
        ObservableList<NewTrialsView> trialsViewList = FXCollections.observableArrayList();
        String sql = "SELECT t.trial_id, t.trial_purpose, t.creation_date AS trial_creation_date, t.notes AS trial_notes, " +
                "s.section_name, m.matrial_name, sup.supplier_name, c.country_name AS supplier_country_name, " +
                "d.department_name, f.file_path " +
                "FROM trials t " +
                "LEFT JOIN sections s ON t.section_id = s.section_id " +
                "LEFT JOIN matrials m ON t.matrial_id = m.matrial_id " +
                "LEFT JOIN suppliers sup ON t.supplier_id = sup.supplier_id " +
                "LEFT JOIN supplier_country sc ON t.sup_country_id = sc.sup_country_id " +
                "LEFT JOIN dbtrials.dbo.countries c ON sc.country_id = c.country_id " +
                "LEFT JOIN files f ON t.trial_id = f.trial_id " +
                "LEFT JOIN departments d ON f.department_id = d.department_id";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            // Use a map to aggregate file paths by trial_id
            java.util.Map<Integer, NewTrialsView> trialsMap = new java.util.HashMap<>();
            while (rs.next()) {
                int trialId = rs.getInt("trial_id");
                NewTrialsView view = trialsMap.getOrDefault(trialId, new NewTrialsView());
                view.setTrialId(trialId);
                view.setTrialPurpose(rs.getString("trial_purpose"));
                view.setTrialCreationDate(rs.getDate("trial_creation_date") != null ?
                        rs.getDate("trial_creation_date").toLocalDate() : null);
                view.setTrialNotes(rs.getString("trial_notes"));
                view.setSectionName(rs.getString("section_name"));
                view.setMaterialName(rs.getString("matrial_name"));
                view.setSupplierName(rs.getString("supplier_name"));
                view.setSupplierCountryName(rs.getString("supplier_country_name"));

                // Assign file paths based on department
                String departmentName = rs.getString("department_name");
                String filePath = rs.getString("file_path");
                if (departmentName != null && filePath != null) {
                    switch (departmentName) {
                        case "Tec Office":
                            view.setTecOfficeFilePath(filePath);
                            break;
                        case "Planning":
                            view.setPlanningFilePath(filePath);
                            break;
                        case "Production":
                            view.setProductionFilePath(filePath);
                            break;
                        case "Process":
                            view.setProcessFilePath(filePath);
                            break;
                        case "R & D":
                            view.setRandDFilePath(filePath); // Updated to use setRandDFilePath
                            break;
                        case "Quality Control":
                            view.setQualityControlFilePath(filePath);
                            break;
                    }
                }
                trialsMap.put(trialId, view);
            }
            trialsViewList.addAll(trialsMap.values());
        } catch (SQLException e) {
            Logging.logException("ERROR", NewTrialsViewDAO.class.getName(), "getAllTrialsView", e);
        }
        return trialsViewList;
    }

    // Search trial files with filters and transform them into NewTrialsView
    public static ObservableList<NewTrialsView> newSearchTrialsView(String trialPurpose, Integer trialId, Integer sectionId,
                                                                    Integer materialId, Integer supplierId, String supplierCountry,
                                                                    LocalDate fromTrialCreationDate, LocalDate toTrialCreationDate) {
        ObservableList<NewTrialsView> trialsViewList = FXCollections.observableArrayList();
        StringBuilder sql = new StringBuilder(
                "SELECT t.trial_id, t.trial_purpose, t.creation_date AS trial_creation_date, t.notes AS trial_notes, " +
                        "s.section_name, m.matrial_name, sup.supplier_name, c.country_name AS supplier_country_name, " +
                        "d.department_name, f.file_path " +
                        "FROM trials t " +
                        "LEFT JOIN sections s ON t.section_id = s.section_id " +
                        "LEFT JOIN matrials m ON t.matrial_id = m.matrial_id " +
                        "LEFT JOIN suppliers sup ON t.supplier_id = sup.supplier_id " +
                        "LEFT JOIN supplier_country sc ON t.sup_country_id = sc.sup_country_id " +
                        "LEFT JOIN dbtrials.dbo.countries c ON sc.country_id = c.country_id " +
                        "LEFT JOIN files f ON t.trial_id = f.trial_id " +
                        "LEFT JOIN departments d ON f.department_id = d.department_id " +
                        "WHERE 1=1");

        if (trialPurpose != null && !trialPurpose.trim().isEmpty()) {
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
        if (supplierCountry != null && !supplierCountry.trim().isEmpty()) {
            sql.append(" AND c.country_name LIKE ?");
        }
        if (fromTrialCreationDate != null) {
            sql.append(" AND t.creation_date >= ?");
        }
        if (toTrialCreationDate != null) {
            sql.append(" AND t.creation_date <= ?");
        }

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (trialPurpose != null && !trialPurpose.trim().isEmpty()) {
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
            if (supplierCountry != null && !supplierCountry.trim().isEmpty()) {
                stmt.setString(paramIndex++, "%" + supplierCountry + "%");
            }
            if (fromTrialCreationDate != null) {
                stmt.setDate(paramIndex++, java.sql.Date.valueOf(fromTrialCreationDate));
            }
            if (toTrialCreationDate != null) {
                stmt.setDate(paramIndex++, java.sql.Date.valueOf(toTrialCreationDate));
            }

            ResultSet rs = stmt.executeQuery();
            // Use a map to aggregate file paths by trial_id
            java.util.Map<Integer, NewTrialsView> trialsMap = new java.util.HashMap<>();
            while (rs.next()) {
                 trialId = rs.getInt("trial_id");   // iam remove def  int
                NewTrialsView view = trialsMap.getOrDefault(trialId, new NewTrialsView());
                view.setTrialId(trialId);
                view.setTrialPurpose(rs.getString("trial_purpose"));
                view.setTrialCreationDate(rs.getDate("trial_creation_date") != null ?
                        rs.getDate("trial_creation_date").toLocalDate() : null);
                view.setTrialNotes(rs.getString("trial_notes"));
                view.setSectionName(rs.getString("section_name"));
                view.setMaterialName(rs.getString("matrial_name"));
                view.setSupplierName(rs.getString("supplier_name"));
                view.setSupplierCountryName(rs.getString("supplier_country_name"));

                // Assign file paths based on department
                String departmentName = rs.getString("department_name");
                String filePath = rs.getString("file_path");
                if (departmentName != null && filePath != null) {
                    switch (departmentName) {
                        case "Tec Office":
                            view.setTecOfficeFilePath(filePath);
                            break;
                        case "Planning":
                            view.setPlanningFilePath(filePath);
                            break;
                        case "Production":
                            view.setProductionFilePath(filePath);
                            break;
                        case "Process":
                            view.setProcessFilePath(filePath);
                            break;
                        case "R & D":
                            view.setRandDFilePath(filePath); // Updated to use setRandDFilePath
                            break;
                        case "Quality Control":
                            view.setQualityControlFilePath(filePath);
                            break;
                    }
                }
                trialsMap.put(trialId, view);
            }
            trialsViewList.addAll(trialsMap.values());
        } catch (SQLException e) {
            Logging.logException("ERROR", NewTrialsViewDAO.class.getName(), "newSearchTrialsView", e);
        }
        return trialsViewList;
    }
}