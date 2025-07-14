package com.elsewedyt.trialsapp.dao;

import com.elsewedyt.trialsapp.db.DbConnect;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.models.Section;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SectionDAO {

    // Get all sections
    public static ObservableList<Section> getAllSections() {
        ObservableList<Section> list = FXCollections.observableArrayList();
        String query = "SELECT section_id, section_name FROM dbtrials.dbo.sections ORDER BY section_id ASC";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Section(rs.getInt("section_id"), rs.getString("section_name")));
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SectionDAO.class.getName(), "getAllSections", e, "sql", query);
        }

        return list;
    }

    // Insert section
    public static boolean insertSection(Section section) {
        String query = "INSERT INTO dbtrials.dbo.sections (section_name) VALUES (?)";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, section.getSectionName());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SectionDAO.class.getName(), "insertSection", e, "sql", query);
        }

        return false;
    }

    // Update section
    public static boolean updateSection(Section section) {
        String query = "UPDATE dbtrials.dbo.sections SET section_name = ? WHERE section_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, section.getSectionName());
            ps.setInt(2, section.getSectionId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SectionDAO.class.getName(), "updateSection", e, "sql", query);
        }

        return false;
    }

    // Check if section is referenced
    public static boolean canDeleteSection(int sectionId) {
        String query = """
            SELECT COUNT(*) AS ref_count FROM dbtrials.dbo.trials WHERE section_id = ?
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ref_count") == 0;
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SectionDAO.class.getName(), "canDeleteSection", e, "sql", query);
        }

        return false;
    }

    // Delete section
    public static boolean deleteSection(int sectionId) {
        if (!canDeleteSection(sectionId)) {
            System.out.println("Section ID " + sectionId + " is referenced in 'trials' table.");
            return false;
        }

        String query = "DELETE FROM dbtrials.dbo.sections WHERE section_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, sectionId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SectionDAO.class.getName(), "deleteSection", e, "sql", query);
        }

        return false;
    }

    // Get section by ID
    public static Section getSectionById(int sectionId) {
        String query = "SELECT section_id, section_name FROM dbtrials.dbo.sections WHERE section_id = ?";
        Section section = null;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    section = new Section(rs.getInt("section_id"), rs.getString("section_name"));
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SectionDAO.class.getName(), "getSectionById", e, "sql", query);
        }

        return section;
    }
}
