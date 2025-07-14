package com.elsewedyt.trialsapp.dao;

import com.elsewedyt.trialsapp.db.DbConnect;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.models.Matrial;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class MatrialDAO {

    // Get all matrials
    public static ObservableList<Matrial> getAllMatrials() {
        ObservableList<Matrial> list = FXCollections.observableArrayList();
        String query = "SELECT matrial_id, matrial_name FROM dbtrials.dbo.matrials ORDER BY matrial_id ASC";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Matrial(
                        rs.getInt("matrial_id"),
                        rs.getString("matrial_name")
                ));
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", MatrialDAO.class.getName(), "getAllMatrials", e, "sql", query);
        }

        return list;
    }

    // Insert
    public static boolean insertMatrial(Matrial m) {
        String query = "INSERT INTO dbtrials.dbo.matrials (matrial_name) VALUES (?)";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, m.getMatrialName());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", MatrialDAO.class.getName(), "insertMatrial", e, "sql", query);
        }

        return false;
    }

    // Update
    public static boolean updateMatrial(Matrial m) {
        String query = "UPDATE dbtrials.dbo.matrials SET matrial_name = ? WHERE matrial_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, m.getMatrialName());
            ps.setInt(2, m.getMatrialId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", MatrialDAO.class.getName(), "updateMatrial", e, "sql", query);
        }

        return false;
    }

    // Delete
    public static boolean deleteMatrial(int matrialId) {
        String query = "DELETE FROM dbtrials.dbo.matrials WHERE matrial_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, matrialId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", MatrialDAO.class.getName(), "deleteMatrial", e, "sql", query);
        }

        return false;
    }

    // Get by ID
    public static Matrial getMatrialById(int id) {
        String query = "SELECT matrial_id, matrial_name FROM dbtrials.dbo.matrials WHERE matrial_id = ?";
        Matrial m = null;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    m = new Matrial(rs.getInt("matrial_id"), rs.getString("matrial_name"));
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", MatrialDAO.class.getName(), "getMatrialById", e, "sql", query);
        }

        return m;
    }
}
