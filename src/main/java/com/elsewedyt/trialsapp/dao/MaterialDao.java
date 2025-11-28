package com.elsewedyt.trialsapp.dao;

import com.elsewedyt.trialsapp.db.DbConnect;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.model.Material;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class MaterialDao {
    public static String lastErrorMessage = null;
    // Get all Materials
    public  ObservableList<Material> getAllMaterials() {
        ObservableList<Material> list = FXCollections.observableArrayList();
        String query = "SELECT material_id, material_name FROM dbtrials.dbo.materials ORDER BY material_name ASC";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Material(
                        rs.getInt("material_id"),
                        rs.getString("material_name")
                ));
            }

        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialDao.class.getName(), "getAllMaterials", e, "sql", query);

        }

        return list;
    }

    // Insert
    public  boolean insertMaterial(Material m) {
        String query = "INSERT INTO dbtrials.dbo.materials (material_name) VALUES (?)";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, m.getMaterialName());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", MaterialDao.class.getName(), "insertMaterial", e, "sql", query);
        }

        return false;
    }

    // Update
    public  boolean updateMaterial(Material m) {
        String query = "UPDATE dbtrials.dbo.materials SET material_name = ? WHERE material_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, m.getMaterialName());
            ps.setInt(2, m.getMaterialId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", MaterialDao.class.getName(), "updateMaterial", e, "sql", query);
        }

        return false;
    }

    // Delete
    public  boolean deleteMaterial(int matrialId) {
        String query = "DELETE FROM dbtrials.dbo.materials WHERE material_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, matrialId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", MaterialDao.class.getName(), "deleteMaterial", e, "sql", query);
        }

        return false;
    }

    // Get by ID
    public Material getMaterialById(int id) {
        String query = "SELECT material_id, material_name FROM dbtrials.dbo.materials WHERE material_id = ?";
        Material m = null;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    m = new Material(rs.getInt("material_id"), rs.getString("material_name"));
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", MaterialDao.class.getName(), "getMaterialById", e, "sql", query);
        }

        return m;
    }
}
