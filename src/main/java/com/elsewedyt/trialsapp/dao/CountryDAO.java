package com.elsewedyt.trialsapp.dao;

import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.models.Country;
import com.elsewedyt.trialsapp.db.DbConnect;
import com.elsewedyt.trialsapp.models.SupplierCountry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO {
    public static String lastErrorMessage = null;
    // INSERT
    public static boolean insertCountry(Country country) {
        String sql = "INSERT INTO dbtrials.dbo.countries (country_name) VALUES (?)";
        try (Connection conn = DbConnect.getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, country.getCountryName());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", CountryDAO.class.getName(), "insertCountry", e, "sql", sql);
            return false;
        }
    }

    // UPDATE
    public static boolean updateCountry(Country country) {
        String sql = "UPDATE dbtrials.dbo.countries SET country_name = ? WHERE country_id = ?";
        try (Connection conn = DbConnect.getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, country.getCountryName());
            stmt.setInt(2, country.getCountryId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            Logging.logExpWithMessage("ERROR", CountryDAO.class.getName(), "updateCountry", e, "sql", sql);

            return false;
        }
    }

    // DELETE
    public static boolean deleteCountry(int countryId) {
        String sql = "DELETE FROM dbtrials.dbo.countries WHERE country_id = ?";
        try (Connection conn = DbConnect.getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, countryId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            Logging.logExpWithMessage("ERROR", CountryDAO.class.getName(), "deleteCountry", e, "sql", sql);
            return false;
        }
    }

    // GET ALL
    public static ObservableList<Country> getAllCountries() {
        ObservableList<Country> countries = FXCollections.observableArrayList();
        String sql = "SELECT country_id, country_name FROM dbtrials.dbo.countries ORDER BY country_name";
        try (Connection conn = DbConnect.getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Country country = new Country();
                country.setCountryId(rs.getInt("country_id"));
                country.setCountryName(rs.getString("country_name"));
                countries.add(country);
            }

        } catch (SQLException e) {
            Logging.logExpWithMessage("ERROR", CountryDAO.class.getName(), "getAllCountries", e, "sql", sql);

        }
        return countries;
    }

    // GET BY ID
    public static Country getCountryById(int countryId) {
        String sql = "SELECT country_id, country_name FROM dbtrials.dbo.countries WHERE country_id = ?";
        try (Connection conn = DbConnect.getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, countryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Country(
                            rs.getInt("country_id"),
                            rs.getString("country_name")
                    );
                }
            }

        } catch (SQLException e) {
            Logging.logExpWithMessage("ERROR", CountryDAO.class.getName(), "getCountryById", e, "sql", sql);

        }
        return null;
    }
}

