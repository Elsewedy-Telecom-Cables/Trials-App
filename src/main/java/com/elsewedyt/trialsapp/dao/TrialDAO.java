package com.elsewedyt.trialsapp.dao;

import com.elsewedyt.trialsapp.db.DbConnect;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.models.Trial;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;

public class TrialDAO {
    public static ObservableList<Trial> getAllTrials() {
        ObservableList<Trial> list = FXCollections.observableArrayList();
        String query = """
            SELECT t.trial_id, t.trial_purpose, t.section_id, t.matrial_id, t.supplier_id, t.sup_country_id, t.user_id, t.creation_date, t.notes,
                   s.section_name, m.matrial_name, sp.supplier_name, c.country_name
            FROM dbtrials.dbo.trials t
            LEFT JOIN dbtrials.dbo.sections s ON t.section_id = s.section_id
            LEFT JOIN dbtrials.dbo.matrials m ON t.matrial_id = m.matrial_id
            LEFT JOIN dbtrials.dbo.suppliers sp ON t.supplier_id = sp.supplier_id
            LEFT JOIN dbtrials.dbo.supplier_country sc ON t.sup_country_id = sc.sup_country_id
            LEFT JOIN dbtrials.dbo.countries c ON sc.country_id = c.country_id
            ORDER BY t.trial_id DESC
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Trial trial = new Trial();
                trial.setTrialId(rs.getInt("trial_id"));
                trial.setTrialPurpose(rs.getString("trial_purpose"));
                trial.setSectionId(rs.getObject("section_id") != null ? rs.getInt("section_id") : null);
                trial.setMatrialId(rs.getObject("matrial_id") != null ? rs.getInt("matrial_id") : null);
                trial.setSupplierId(rs.getObject("supplier_id") != null ? rs.getInt("supplier_id") : null);
                trial.setSupCountryId(rs.getObject("sup_country_id") != null ? rs.getInt("sup_country_id") : null);
                trial.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
                Date sqlDate = rs.getDate("creation_date");
                trial.setCreationDate(sqlDate != null ? sqlDate.toLocalDate() : null);
                trial.setNotes(rs.getString("notes"));
                trial.setSectionName(rs.getString("section_name"));
                trial.setMatrialName(rs.getString("matrial_name"));
                trial.setSupplierName(rs.getString("supplier_name"));
                trial.setSupplierCountryName(rs.getString("country_name"));
                list.add(trial);
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", TrialDAO.class.getName(), "getAllTrials", e, "sql", query);
        }

        return list;
    }

    // Search trials based on provided filters
    // LEFT JOIN dbtrials.dbo.supplier_country sc ON t.sup_country_id = sc.sup_country_id
    public static ObservableList<Trial> searchTrials(String trialPurpose, Integer sectionId, Integer matrialId, Integer supplierId, String supplierCountryName, LocalDate creationDate) {
        ObservableList<Trial> list = FXCollections.observableArrayList();
        StringBuilder query = new StringBuilder("""
            SELECT t.trial_id, t.trial_purpose, t.section_id, t.matrial_id, t.supplier_id, t.sup_country_id, t.user_id, t.creation_date, t.notes,
                   s.section_name, m.matrial_name, sp.supplier_name, c.country_name
            FROM dbtrials.dbo.trials t
            LEFT JOIN dbtrials.dbo.sections s ON t.section_id = s.section_id
            LEFT JOIN dbtrials.dbo.matrials m ON t.matrial_id = m.matrial_id
            LEFT JOIN dbtrials.dbo.suppliers sp ON t.supplier_id = sp.supplier_id
            LEFT JOIN dbtrials.dbo.supplier_country sc ON t.sup_country_id = sc.sup_country_id
            LEFT JOIN dbtrials.dbo.countries c ON sc.country_id = c.country_id
            WHERE 1=1
        """);

        // Dynamic query conditions
        if (trialPurpose != null && !trialPurpose.trim().isEmpty()) {
            query.append(" AND t.trial_purpose LIKE ?");
        }
        if (sectionId != null) {
            query.append(" AND t.section_id = ?");
        }
        if (matrialId != null) {
            query.append(" AND t.matrial_id = ?");
        }
        if (supplierId != null) {
            query.append(" AND t.supplier_id = ?");
        }
        if (supplierCountryName != null && !supplierCountryName.trim().isEmpty()) {
            query.append(" AND c.country_name LIKE ?");
        }
        if (creationDate != null) {
            query.append(" AND t.creation_date = ?");
        }
        query.append(" ORDER BY t.trial_id ASC");

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query.toString())) {

            int paramIndex = 1;
            if (trialPurpose != null && !trialPurpose.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + trialPurpose.trim() + "%");
            }
            if (sectionId != null) {
                ps.setInt(paramIndex++, sectionId);
            }
            if (matrialId != null) {
                ps.setInt(paramIndex++, matrialId);
            }
            if (supplierId != null) {
                ps.setInt(paramIndex++, supplierId);
            }
            if (supplierCountryName != null && !supplierCountryName.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + supplierCountryName.trim() + "%");
            }
            if (creationDate != null) {
                ps.setDate(paramIndex, Date.valueOf(creationDate));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Trial trial = new Trial();
                    trial.setTrialId(rs.getInt("trial_id"));
                    trial.setTrialPurpose(rs.getString("trial_purpose"));
                    trial.setSectionId(rs.getObject("section_id") != null ? rs.getInt("section_id") : null);
                    trial.setMatrialId(rs.getObject("matrial_id") != null ? rs.getInt("matrial_id") : null);
                    trial.setSupplierId(rs.getObject("supplier_id") != null ? rs.getInt("supplier_id") : null);
                    trial.setSupCountryId(rs.getObject("sup_country_id") != null ? rs.getInt("sup_country_id") : null);
                    trial.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
                    Date sqlDate = rs.getDate("creation_date");
                    trial.setCreationDate(sqlDate != null ? sqlDate.toLocalDate() : null);
                    trial.setNotes(rs.getString("notes"));
                    trial.setSectionName(rs.getString("section_name"));
                    trial.setMatrialName(rs.getString("matrial_name"));
                    trial.setSupplierName(rs.getString("supplier_name"));
                    trial.setSupplierCountryName(rs.getString("country_name"));
                    list.add(trial);
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", TrialDAO.class.getName(), "searchTrials", e, "sql", query.toString());
        }

        return list;
    }

    // Insert a new trial
    public static boolean insertTrial(Trial trial) {
        String query = """
            INSERT INTO dbtrials.dbo.trials (trial_purpose, section_id, matrial_id, supplier_id, sup_country_id, user_id, creation_date, notes)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, trial.getTrialPurpose());
            ps.setObject(2, trial.getSectionId());
            ps.setObject(3, trial.getMatrialId());
            ps.setObject(4, trial.getSupplierId());
            ps.setObject(5, trial.getSupCountryId());
            ps.setObject(6, trial.getUserId());
            LocalDate creationDate = trial.getCreationDate() != null ? trial.getCreationDate() : LocalDate.now();
            ps.setDate(7, Date.valueOf(creationDate));
            ps.setString(8, trial.getNotes());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", TrialDAO.class.getName(), "insertTrial", e, "sql", query);
        }

        return false;
    }

    // Update an existing trial
    public static boolean updateTrial(Trial trial) {
        String query = """
            UPDATE dbtrials.dbo.trials
            SET trial_purpose = ?, section_id = ?, matrial_id = ?, supplier_id = ?, sup_country_id = ?, user_id = ?, creation_date = ?, notes = ?
            WHERE trial_id = ?
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, trial.getTrialPurpose());
            ps.setObject(2, trial.getSectionId());
            ps.setObject(3, trial.getMatrialId());
            ps.setObject(4, trial.getSupplierId());
            ps.setObject(5, trial.getSupCountryId());
            ps.setObject(6, trial.getUserId());
            LocalDate creationDate = trial.getCreationDate() != null ? trial.getCreationDate() : LocalDate.now();
            ps.setDate(7, Date.valueOf(creationDate));
            ps.setString(8, trial.getNotes());
            ps.setInt(9, trial.getTrialId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", TrialDAO.class.getName(), "updateTrial", e, "sql", query);
        }

        return false;
    }

    // Check if trial is referenced in files table
    public static boolean canDeleteTrial(int trialId) {
        String query = """
            SELECT COUNT(*) AS ref_count FROM dbtrials.dbo.files WHERE trial_id = ?
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, trialId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ref_count") == 0;
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", TrialDAO.class.getName(), "canDeleteTrial", e, "sql", query);
        }

        return false;
    }

    // Delete a trial
    public static boolean deleteTrial(int trialId) {
        if (!canDeleteTrial(trialId)) {
            System.out.println("Trial ID " + trialId + " is referenced in 'files' table.");
            return false;
        }

        String query = "DELETE FROM dbtrials.dbo.trials WHERE trial_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, trialId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", TrialDAO.class.getName(), "deleteTrial", e, "sql", query);
        }

        return false;
    }

    // Get trial by ID with related names
    // LEFT JOIN dbtrials.dbo.supplier_country sc ON t.sup_country_id = sc.sup_country_id
    public static Trial getTrialById(int trialId) {
        String query = """
            SELECT t.trial_id, t.trial_purpose, t.section_id, t.matrial_id, t.supplier_id, t.sup_country_id, t.user_id, t.creation_date, t.notes,
                   s.section_name, m.matrial_name, sp.supplier_name, c.country_name
            FROM dbtrials.dbo.trials t
            LEFT JOIN dbtrials.dbo.sections s ON t.section_id = s.section_id
            LEFT JOIN dbtrials.dbo.matrials m ON t.matrial_id = m.matrial_id
            LEFT JOIN dbtrials.dbo.suppliers sp ON t.supplier_id = sp.supplier_id
            LEFT JOIN dbtrials.dbo.supplier_country sc ON t.sup_country_id = sc.sup_country_id
            LEFT JOIN dbtrials.dbo.countries c ON sc.country_id = c.country_id
            WHERE t.trial_id = ?
        """;
        Trial trial = null;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, trialId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    trial = new Trial();
                    trial.setTrialId(rs.getInt("trial_id"));
                    trial.setTrialPurpose(rs.getString("trial_purpose"));
                    trial.setSectionId(rs.getObject("section_id") != null ? rs.getInt("section_id") : null);
                    trial.setMatrialId(rs.getObject("matrial_id") != null ? rs.getInt("matrial_id") : null);
                    trial.setSupplierId(rs.getObject("supplier_id") != null ? rs.getInt("supplier_id") : null);
                    trial.setSupCountryId(rs.getObject("sup_country_id") != null ? rs.getInt("sup_country_id") : null);
                    trial.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
                    Date sqlDate = rs.getDate("creation_date");
                    trial.setCreationDate(sqlDate != null ? sqlDate.toLocalDate() : null);
                    trial.setNotes(rs.getString("notes"));
                    trial.setSectionName(rs.getString("section_name"));
                    trial.setMatrialName(rs.getString("matrial_name"));
                    trial.setSupplierName(rs.getString("supplier_name"));
                    trial.setSupplierCountryName(rs.getString("country_name"));
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", TrialDAO.class.getName(), "getTrialById", e, "sql", query);
        }

        return trial;
    }

    // Get the total number of trials
    public static int getTrialsCount() {
        String query = "SELECT COUNT(*) AS trial_count FROM dbtrials.dbo.trials";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("trial_count");
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", TrialDAO.class.getName(), "getTrialsCount", e, "sql", query);
        }
        return 0;
    }

    // Get the number of trials by section ID
    public static int getTrialsCountBySection(Integer sectionId) {
        String query = "SELECT COUNT(*) AS trial_count FROM dbtrials.dbo.trials WHERE section_id = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setObject(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("trial_count");
                }
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", TrialDAO.class.getName(), "getTrialsCountBySection", e, "sql", query);
        }
        return 0;
    }

}