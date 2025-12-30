package com.etc.trials.dao;

import com.etc.trials.db.DbConnect;
import com.etc.trials.logging.Logging;
import com.etc.trials.model.Department;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DepartmentDao {
    public static String lastErrorMessage = null;
    // Get all departments
    public  ObservableList<Department> getAllDepartments() {
        ObservableList<Department> list = FXCollections.observableArrayList();
        String query = "SELECT department_id, department_name FROM dbtrials.dbo.departments ORDER BY department_id ASC";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("department_id");
                String name = rs.getString("department_name");
                list.add(new Department(id, name));
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", DepartmentDao.class.getName(), "getAllDepartments", e, "sql", query);
        }
        return list;
    }

    // Insert department
    public  boolean insertDepartment(Department department) {
        String query = "INSERT INTO dbtrials.dbo.departments (department_name) VALUES (?)";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, department.getDepartmentName());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            lastErrorMessage = e.getMessage(); // حفظ نص الخطأ
            Logging.logExpWithMessage("ERROR", DepartmentDao.class.getName(), "insertDepartment", e, "sql", query);
        }
        return false;
    }

    // Update department
    public  boolean updateDepartment(Department department) {
        String query = "UPDATE dbtrials.dbo.departments SET department_name = ? WHERE department_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, department.getDepartmentName());
            ps.setInt(2, department.getDepartmentId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", DepartmentDao.class.getName(), "updateDepartment", e, "sql", query);
        }
        return false;
    }

    // Check if department can be safely deleted
    public static boolean canDeleteDepartment(int departmentId) {
        String query = """
            SELECT COUNT(*) AS ref_count FROM (
                SELECT department_id FROM dbtrials.dbo.users WHERE department_id = ?
                UNION ALL
                SELECT department_id FROM dbtrials.dbo.file_type WHERE department_id = ?
                UNION ALL
                SELECT department_id FROM dbtrials.dbo.files WHERE department_id = ?
            ) AS refs
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, departmentId);
            ps.setInt(2, departmentId);
            ps.setInt(3, departmentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ref_count") == 0;
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", DepartmentDao.class.getName(), "canDeleteDepartment", e, "sql", query);
        }
        return false;
    }

    // Delete department
    public  boolean deleteDepartment(int departmentId) {
        if (!canDeleteDepartment(departmentId)) {
            System.out.println("Department ID " + departmentId + " is referenced in other tables.");
            return false;
        }

        String query = "DELETE FROM dbtrials.dbo.departments WHERE department_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, departmentId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", DepartmentDao.class.getName(), "deleteDepartment", e, "sql", query);
        }
        return false;
    }

    // Get department by ID
    public  Department getDepartmentById(int departmentId) {
        String query = "SELECT department_id, department_name FROM dbtrials.dbo.departments WHERE department_id = ?";
        Department department = null;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    department = new Department(rs.getInt("department_id"), rs.getString("department_name"));
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", DepartmentDao.class.getName(), "getDepartmentById", e, "sql", query);
        }

        return department;
    }
}


