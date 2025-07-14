//
//package com.elsewedyt.trialsapp.dao;
//
//import com.elsewedyt.trialsapp.db.DbConnect;
//import com.elsewedyt.trialsapp.logging.Logging;
//import com.elsewedyt.trialsapp.models.SupplierCountry;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import java.sql.*;
//
//public class SupplierCountryDAO {
//
//    // Get all supplier countries
//    public static ObservableList<SupplierCountry> getAllSupplierCountries() {
//        ObservableList<SupplierCountry> list = FXCollections.observableArrayList();
//
//        String query = """
//        SELECT sc.sup_country_id, sc.country_name, sc.supplier_id, s.supplier_name
//        FROM dbtrials.dbo.supplier_country sc
//        LEFT JOIN dbtrials.dbo.suppliers s ON sc.supplier_id = s.supplier_id
//        ORDER BY s.supplier_id ASC, sc.country_name ASC
//        """;
//
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(query);
//             ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                SupplierCountry country = new SupplierCountry();
//                country.setSupCountryId(rs.getInt("sup_country_id"));
//                country.setCountryName(rs.getString("country_name"));
//                country.setSupplierId(rs.getInt("supplier_id"));
//                country.setSupplierName(rs.getString("supplier_name"));
//                list.add(country);
//            }
//
//        } catch (Exception e) {
//            Logging.logExpWithMessage("ERROR", SupplierCountryDAO.class.getName(), "getAllSupplierCountries", e, "sql", query);
//        }
//
//        return list;
//    }
//
//    // Insert a new supplier country
//    public static boolean insertSupplierCountry(SupplierCountry sc) {
//        String query = "INSERT INTO dbtrials.dbo.supplier_country (country_name, supplier_id) VALUES (?, ?)";
//
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(query)) {
//
//            ps.setString(1, sc.getCountryName());
//            if (sc.getSupplierId() != null) {
//                ps.setInt(2, sc.getSupplierId());
//            } else {
//                ps.setNull(2, Types.INTEGER);
//            }
//
//            return ps.executeUpdate() > 0;
//
//        } catch (Exception e) {
//            Logging.logExpWithMessage("ERROR", SupplierCountryDAO.class.getName(), "insertSupplierCountry", e, "sql", query);
//        }
//        return false;
//    }
//
//    // Update an existing supplier country
//    public static boolean updateSupplierCountry(SupplierCountry sc) {
//        String query = "UPDATE dbtrials.dbo.supplier_country SET country_name = ?, supplier_id = ? WHERE sup_country_id = ?";
//
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(query)) {
//
//            ps.setString(1, sc.getCountryName());
//            if (sc.getSupplierId() != null) {
//                ps.setInt(2, sc.getSupplierId());
//            } else {
//                ps.setNull(2, Types.INTEGER);
//            }
//            ps.setInt(3, sc.getSupCountryId());
//
//            return ps.executeUpdate() > 0;
//
//        } catch (Exception e) {
//            Logging.logExpWithMessage("ERROR", SupplierCountryDAO.class.getName(), "updateSupplierCountry", e, "sql", query);
//        }
//        return false;
//    }
//
//    // Delete a supplier country by ID
//    public static boolean deleteSupplierCountry(int supCountryId) {
//        String query = "DELETE FROM dbtrials.dbo.supplier_country WHERE sup_country_id = ?";
//
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(query)) {
//
//            ps.setInt(1, supCountryId);
//            return ps.executeUpdate() > 0;
//
//        } catch (Exception e) {
//            Logging.logExpWithMessage("ERROR", SupplierCountryDAO.class.getName(), "deleteSupplierCountry", e, "sql", query);
//        }
//        return false;
//    }
//
//    // Get a supplier country by its ID
//    public static SupplierCountry getSupplierCountryById(int id) {
//        String query = "SELECT sup_country_id, country_name, supplier_id FROM dbtrials.dbo.supplier_country WHERE sup_country_id = ?";
//        SupplierCountry sc = null;
//
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(query)) {
//
//            ps.setInt(1, id);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    sc = new SupplierCountry(
//                            rs.getInt("sup_country_id"),
//                            rs.getString("country_name"),
//                            rs.getInt("supplier_id") == 0 ? null : rs.getInt("supplier_id")
//                    );
//                }
//            }
//
//        } catch (Exception e) {
//            Logging.logExpWithMessage("ERROR", SupplierCountryDAO.class.getName(), "getSupplierCountryById", e, "sql", query);
//        }
//
//        return sc;
//    }
//
//    // Get all supplier countries associated with a specific supplier ID
//    public static ObservableList<SupplierCountry> getSupplierCountriesBySupplierId(int supplierId) {
//        ObservableList<SupplierCountry> list = FXCollections.observableArrayList();
//        String query = """
//        SELECT sup_country_id, country_name, supplier_id
//        FROM dbtrials.dbo.supplier_country
//        WHERE supplier_id = ?
//        ORDER BY country_name ASC
//        """;
//
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(query)) {
//
//            ps.setInt(1, supplierId);
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    SupplierCountry sc = new SupplierCountry(
//                            rs.getInt("sup_country_id"),
//                            rs.getString("country_name"),
//                            rs.getInt("supplier_id") == 0 ? null : rs.getInt("supplier_id")
//                    );
//                    list.add(sc);
//                }
//            }
//
//        } catch (Exception e) {
//            Logging.logExpWithMessage("ERROR", SupplierCountryDAO.class.getName(), "getSupplierCountriesBySupplierId", e, "sql", query);
//        }
//
//        return list;
//    }
//}

package com.elsewedyt.trialsapp.dao;

import com.elsewedyt.trialsapp.db.DbConnect;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.models.SupplierCountry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class SupplierCountryDAO {

    // Get all supplier countries (with country name via JOIN)
    public static ObservableList<SupplierCountry> getAllSupplierCountries() {
        ObservableList<SupplierCountry> list = FXCollections.observableArrayList();

        String query = """
            SELECT sc.sup_country_id, sc.country_id, c.country_name, 
                   sc.supplier_id, s.supplier_name
            FROM dbtrials.dbo.supplier_country sc
            LEFT JOIN dbtrials.dbo.countries c ON sc.country_id = c.country_id
            LEFT JOIN dbtrials.dbo.suppliers s ON sc.supplier_id = s.supplier_id
            ORDER BY s.supplier_id ASC, c.country_name ASC
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
            Logging.logExpWithMessage("ERROR", SupplierCountryDAO.class.getName(), "getAllSupplierCountries", e, "sql", query);
        }

        return list;
    }

    // Insert a new supplier country (by country_id)
    public static boolean insertSupplierCountry(SupplierCountry sc) {
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
            Logging.logExpWithMessage("ERROR", SupplierCountryDAO.class.getName(), "insertSupplierCountry", e, "sql", query);
        }
        return false;
    }

    // Update supplier country
    public static boolean updateSupplierCountry(SupplierCountry sc) {
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
            Logging.logExpWithMessage("ERROR", SupplierCountryDAO.class.getName(), "updateSupplierCountry", e, "sql", query);
        }
        return false;
    }

    // Delete supplier country by ID
    public static boolean deleteSupplierCountry(int supCountryId) {
        String query = "DELETE FROM dbtrials.dbo.supplier_country WHERE sup_country_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, supCountryId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierCountryDAO.class.getName(), "deleteSupplierCountry", e, "sql", query);
        }
        return false;
    }

    // Get supplier country by ID (includes country name)
    public static SupplierCountry getSupplierCountryById(int id) {
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
            Logging.logExpWithMessage("ERROR", SupplierCountryDAO.class.getName(), "getSupplierCountryById", e, "sql", query);
        }

        return null;
    }

    // Get all supplier countries by supplier ID
    public static ObservableList<SupplierCountry> getSupplierCountriesBySupplierId(int supplierId) {
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
            Logging.logExpWithMessage("ERROR", SupplierCountryDAO.class.getName(), "getSupplierCountriesBySupplierId", e, "sql", query);
        }

        return list;
    }
}

