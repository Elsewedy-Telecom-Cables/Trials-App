
package com.elsewedyt.trialsapp.dao;
import com.elsewedyt.trialsapp.db.DbConnect;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.model.NewTrialsView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;

public class NewTrialsViewDao {


        // Get all trial files and transform them into NewTrialsView
        public  ObservableList<NewTrialsView> getAllTrialsView() {
            ObservableList<NewTrialsView> trialsViewList = FXCollections.observableArrayList();
            String sql = """
                    SELECT t.trial_id, t.trial_purpose, t.creation_date AS trial_creation_date, t.notes AS trial_notes,
                    s.section_name, m.material_name, sup.supplier_name, c.country_name AS supplier_country_name,
                    d.department_name, f.file_path
                    FROM dbtrials.dbo.trials t
                    LEFT JOIN dbtrials.dbo.sections s ON t.section_id = s.section_id
                    LEFT JOIN dbtrials.dbo.materials m ON t.material_id = m.material_id
                    LEFT JOIN dbtrials.dbo.suppliers sup ON t.supplier_id = sup.supplier_id
                    LEFT JOIN dbtrials.dbo.supplier_country sc ON t.sup_country_id = sc.sup_country_id
                    LEFT JOIN dbtrials.dbo.countries c ON sc.country_id = c.country_id
                    LEFT JOIN dbtrials.dbo.files f ON t.trial_id = f.trial_id
                    LEFT JOIN dbtrials.dbo.departments d ON f.department_id = d.department_id
                    ORDER BY t.trial_id DESC
                    """;

            try (Connection conn = DbConnect.getConnect();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                // Use a map to aggregate file paths by trial_id
                HashMap<Integer, NewTrialsView> trialsMap = new HashMap<>();
                while (rs.next()) {
                    int trialId = rs.getInt("trial_id");
                    NewTrialsView view = trialsMap.getOrDefault(trialId, new NewTrialsView());
                    view.setTrialId(trialId);
                    view.setTrialPurpose(rs.getString("trial_purpose"));
                    Timestamp ts = rs.getTimestamp("trial_creation_date");
                    view.setTrialCreationDate(ts != null ? ts.toLocalDateTime() : null);
                    view.setTrialNotes(rs.getString("trial_notes"));
                    view.setSectionName(rs.getString("section_name"));
                    view.setMaterialName(rs.getString("material_name"));
                    view.setSupplierName(rs.getString("supplier_name"));
                    view.setSupplierCountryName(rs.getString("supplier_country_name"));

                    // Assign file paths based on department
                    String departmentName = rs.getString("department_name");
                    String filePath = rs.getString("file_path");
                    // Debug: Log System.out and file path
                 //   System.out.println("Department Name: " + departmentName + ", File Path: " + filePath + ", Trial ID: " + trialId);
                    if (departmentName != null && filePath != null) {
                        switch (departmentName) {
                            case "Technical Office":
                                view.addTecOfficeFilePath(filePath);
                                // Debug: Confirm Tec Office file path addition
                        //        System.out.println("Added Tec Office FilePath: " + filePath + " for Trial ID: " + trialId);
                                break;
                            case "Planning":
                                view.addPlanningFilePath(filePath);
                                break;
                            case "Production":
                                view.addProductionFilePath(filePath);
                                break;
                            case "Process":
                                view.addProcessFilePath(filePath);
                                break;
                            case "R & D":
                                view.addRandDFilePath(filePath);
                                break;
                            case "Quality Control":
                                view.addQualityControlFilePath(filePath);
                                break;
                            default:
                                // Debug: Log unrecognized department name
                           //     System.out.println("Unrecognized Department Name: " + departmentName + " for Trial ID: " + trialId);
                                break;
                        }
                    }
                    trialsMap.put(trialId, view);
                }
                trialsViewList.addAll(trialsMap.values());
            } catch (SQLException e) {
                Logging.logExpWithMessage("ERROR", NewTrialsViewDao.class.getName(), "getAllTrialsView", e, "sql", sql);

            }
            return trialsViewList;
        }

    public  ObservableList<NewTrialsView> newSearchTrialsView(String trialPurpose, Integer trialId, Integer sectionId,
                                                                    Integer materialId, Integer supplierId, String supplierCountry,
                                                                    LocalDate fromTrialCreationDate, LocalDate toTrialCreationDate) {
        ObservableList<NewTrialsView> trialsViewList = FXCollections.observableArrayList();
//        StringBuilder sql = new StringBuilder(
//                "SELECT t.trial_id, t.trial_purpose, t.creation_date AS trial_creation_date, t.notes AS trial_notes, " +
//                        "s.section_name, m.material_name, sup.supplier_name, c.country_name AS supplier_country_name, " +
//                        "d.department_name, f.file_path " +
//                        "FROM dbtrials.dbo.trials t " +
//                        "LEFT JOIN dbtrials.dbo.sections s ON t.section_id = s.section_id " +
//                        "LEFT JOIN dbtrials.dbo.Materials m ON t.material_id = m.material_id " +
//                        "LEFT JOIN dbtrials.dbo.suppliers sup ON t.supplier_id = sup.supplier_id " +
//                        "LEFT JOIN dbtrials.dbo.supplier_country sc ON t.sup_country_id = sc.sup_country_id " +
//                        "LEFT JOIN dbtrials.dbo.countries c ON sc.country_id = c.country_id " +
//                        "LEFT JOIN dbtrials.dbo.files f ON t.trial_id = f.trial_id " +
//                        "LEFT JOIN dbtrials.dbo.departments d ON f.department_id = d.department_id " +
//                        "WHERE 1=1");

        StringBuilder sql = new StringBuilder("""
    SELECT t.trial_id, t.trial_purpose, t.creation_date AS trial_creation_date, t.notes AS trial_notes,
           s.section_name, m.material_name, sup.supplier_name, c.country_name AS supplier_country_name,
           d.department_name, f.file_path
    FROM dbtrials.dbo.trials t
    LEFT JOIN dbtrials.dbo.sections s ON t.section_id = s.section_id
    LEFT JOIN dbtrials.dbo.Materials m ON t.material_id = m.material_id
    LEFT JOIN dbtrials.dbo.suppliers sup ON t.supplier_id = sup.supplier_id
    LEFT JOIN dbtrials.dbo.supplier_country sc ON t.sup_country_id = sc.sup_country_id
    LEFT JOIN dbtrials.dbo.countries c ON sc.country_id = c.country_id
    LEFT JOIN dbtrials.dbo.files f ON t.trial_id = f.trial_id
    LEFT JOIN dbtrials.dbo.departments d ON f.department_id = d.department_id
    WHERE 1=1
    """);

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
            sql.append(" AND t.material_id = ?");
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
        sql.append(" ORDER BY t.trial_id, f.creation_date DESC");

        try (Connection conn = DbConnect.getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
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
            HashMap<Integer, NewTrialsView> trialsMap = new HashMap<>();
            while (rs.next()) {
                int trialIdResult = rs.getInt("trial_id");
                NewTrialsView view = trialsMap.getOrDefault(trialIdResult, new NewTrialsView());
                view.setTrialId(trialIdResult);
                view.setTrialPurpose(rs.getString("trial_purpose"));
                Timestamp ts = rs.getTimestamp("trial_creation_date");
                view.setTrialCreationDate(ts != null ? ts.toLocalDateTime() : null);
                view.setTrialNotes(rs.getString("trial_notes"));
                view.setSectionName(rs.getString("section_name"));
                view.setMaterialName(rs.getString("material_name"));
                view.setSupplierName(rs.getString("supplier_name"));
                view.setSupplierCountryName(rs.getString("supplier_country_name"));

                // Assign file paths based on department
                String departmentName = rs.getString("department_name");
                String filePath = rs.getString("file_path");
                // Debug: Log department name and file path
          //      System.out.println("Department Name: " + departmentName + ", File Path: " + filePath + ", Trial ID: " + trialIdResult);
                if (departmentName != null && filePath != null) {
                    switch (departmentName) {
                        case "Technical Office":
                            view.addTecOfficeFilePath(filePath);
                            // Debug: Confirm Tec Office file path addition
                       //     System.out.println("Added Tec Office FilePath: " + filePath + " for Trial ID: " + trialIdResult);
                            break;
                        case "Planning":
                            view.addPlanningFilePath(filePath);
                            break;
                        case "Production":
                            view.addProductionFilePath(filePath);
                            break;
                        case "Process":
                            view.addProcessFilePath(filePath);
                            break;
                        case "R & D":
                            view.addRandDFilePath(filePath);
                            break;
                        case "Quality Control":
                            view.addQualityControlFilePath(filePath);
                            break;
                        default:
                            // Debug: Log unrecognized department name
                     //       System.out.println("Unrecognized Department Name: " + departmentName + " for Trial ID: " + trialIdResult);
                            break;
                    }
                }
                trialsMap.put(trialIdResult, view);
            }
            trialsViewList.addAll(trialsMap.values());
        } catch (SQLException e) {
            Logging.logException("ERROR", NewTrialsViewDao.class.getName(), "newSearchTrialsView", e);
        }
        return trialsViewList;
    }
    }



