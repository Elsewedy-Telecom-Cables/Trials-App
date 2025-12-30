
package com.etc.trials.dao;

import com.etc.trials.db.DbConnect;
import com.etc.trials.logging.Logging;
import com.etc.trials.model.SupplierCountry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class SupplierCountryDao {
    public static String lastErrorMessage = null;
  //   Get all supplier countries (with country name via JOIN)

    public  ObservableList<SupplierCountry> getAllSupplierCountries() {
        ObservableList<SupplierCountry> list = FXCollections.observableArrayList();

        String query = """
            SELECT sc.sup_country_id, sc.country_id, c.country_name,
                   sc.supplier_id, s.supplier_name
            FROM dbtrials.dbo.supplier_country sc
            LEFT JOIN dbtrials.dbo.countries c ON sc.country_id = c.country_id
            LEFT JOIN dbtrials.dbo.suppliers s ON sc.supplier_id = s.supplier_id
            ORDER BY s.supplier_name ASC, c.country_name ASC
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SupplierCountry sc = new SupplierCountry();
                sc.setSupCountryId(rs.getInt("sup_country_id"));
                sc.setCountryId(rs.getInt("country_id"));
                sc.setCountryName(rs.getString("country_name"));
                sc.setSupplierId(rs.getInt("supplier_id"));
                sc.setSupplierName(rs.getString("supplier_name"));
                list.add(sc);
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierCountryDao.class.getName(), "getAllSupplierCountries", e, "sql", query);
        }

        return list;
    }

    // Insert a new supplier country (by country_id)
    public  boolean insertSupplierCountry(SupplierCountry sc) {
        String query = "INSERT INTO dbtrials.dbo.supplier_country (country_id, supplier_id) VALUES (?, ?)";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            if (sc.getCountryId() != null) {
                ps.setInt(1, sc.getCountryId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            if (sc.getSupplierId() != null) {
                ps.setInt(2, sc.getSupplierId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", SupplierCountryDao.class.getName(), "insertSupplierCountry", e, "sql", query);
        }
        return false;
    }

    // Update supplier country
    public  boolean updateSupplierCountry(SupplierCountry sc) {
        String query = "UPDATE dbtrials.dbo.supplier_country SET country_id = ?, supplier_id = ? WHERE sup_country_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            if (sc.getCountryId() != null) {
                ps.setInt(1, sc.getCountryId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            if (sc.getSupplierId() != null) {
                ps.setInt(2, sc.getSupplierId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setInt(3, sc.getSupCountryId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", SupplierCountryDao.class.getName(), "updateSupplierCountry", e, "sql", query);
        }
        return false;
    }

    // Delete supplier country by ID
    public  boolean deleteSupplierCountry(int supCountryId) {
        String query = "DELETE FROM dbtrials.dbo.supplier_country WHERE sup_country_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, supCountryId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierCountryDao.class.getName(), "deleteSupplierCountry", e, "sql", query);
        }
        return false;
    }

    // Get supplier country by ID (includes country name)
    public  SupplierCountry getSupplierCountryById(int id) {
        String query = """
            SELECT sc.sup_country_id, sc.country_id, c.country_name, sc.supplier_id
            FROM dbtrials.dbo.supplier_country sc
            LEFT JOIN dbtrials.dbo.countries c ON sc.country_id = c.country_id
            WHERE sc.sup_country_id = ?
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SupplierCountry sc = new SupplierCountry();
                    sc.setSupCountryId(rs.getInt("sup_country_id"));
                    sc.setCountryId(rs.getInt("country_id"));
                    sc.setCountryName(rs.getString("country_name"));
                    sc.setSupplierId(rs.getInt("supplier_id"));
                    return sc;
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierCountryDao.class.getName(), "getSupplierCountryById", e, "sql", query);
        }

        return null;
    }

    // Get all supplier countries by supplier ID
    public  ObservableList<SupplierCountry> getSupplierCountriesBySupplierId(int supplierId) {
        ObservableList<SupplierCountry> list = FXCollections.observableArrayList();
        String query = """
            SELECT sc.sup_country_id, sc.country_id, c.country_name, sc.supplier_id
            FROM dbtrials.dbo.supplier_country sc
            LEFT JOIN dbtrials.dbo.countries c ON sc.country_id = c.country_id
            WHERE sc.supplier_id = ?
            ORDER BY c.country_name ASC
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, supplierId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SupplierCountry sc = new SupplierCountry();
                    sc.setSupCountryId(rs.getInt("sup_country_id"));
                    sc.setCountryId(rs.getInt("country_id"));
                    sc.setCountryName(rs.getString("country_name"));
                    sc.setSupplierId(rs.getInt("supplier_id"));
                    list.add(sc);
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierCountryDao.class.getName(), "getSupplierCountriesBySupplierId", e, "sql", query);
        }

        return list;
    }

    public boolean existsSupplierCountry(int supplierId, int countryId) {
        String query = "SELECT COUNT(*) FROM dbtrials.dbo.supplier_country WHERE supplier_id = ? AND country_id = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, supplierId);
            ps.setInt(2, countryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            Logging.logException("ERROR", getClass().getName(), "existsSupplierCountry", e);
        }
        return false;
    }
}

