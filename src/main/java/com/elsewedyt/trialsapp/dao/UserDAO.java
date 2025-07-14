package com.elsewedyt.trialsapp.dao;
import com.elsewedyt.trialsapp.db.DEF;
import com.elsewedyt.trialsapp.db.DbConnect;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.models.User;
import com.elsewedyt.trialsapp.models.UserContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
public class UserDAO {


        // Get all users
        public static ObservableList<User> getAllUsers() {
            ObservableList<User> list = FXCollections.observableArrayList();
            String query = """
        SELECT u.*, d.department_name
        FROM dbtrials.dbo.users u
        JOIN dbtrials.dbo.departments d ON u.department_id = d.department_id
        ORDER BY u.department_id ASC , u.role DESC
    """;
// ORDER BY u.role DESC , d.department_name ASC
            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    User user = new User(
                            rs.getInt("user_id"),
                            rs.getInt("emp_code"),
                            rs.getString("user_name"),
                            rs.getString("password"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getInt("role"),
                            rs.getInt("active"),
                            rs.getString("creation_date"),
                            rs.getInt("department_id"),
                            rs.getString("department_name")  // newly fetched
                    );
                    list.add(user);
                }

            } catch (Exception e) {
                Logging.logExpWithMessage("ERROR", UserDAO.class.getName(), "getAllUsers", e, "sql", query);
            }
            return list;
        }
    // Get all users by department
    public static ObservableList<User> getUsersByDepartment(int departmentId) {
        ObservableList<User> list = FXCollections.observableArrayList();
        String query = """
        SELECT u.*, d.department_name
        FROM dbtrials.dbo.users u
        JOIN dbtrials.dbo.departments d ON u.department_id = d.department_id
        WHERE u.department_id = ?
        ORDER BY u.department_id ASC , u.role DESC
    """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, departmentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getInt("user_id"),
                            rs.getInt("emp_code"),
                            rs.getString("user_name"),
                            rs.getString("password"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getInt("role"),
                            rs.getInt("active"),
                            rs.getString("creation_date"),
                            rs.getInt("department_id"),
                            rs.getString("department_name")
                    );
                    list.add(user);
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDAO.class.getName(), "getUsersByDepartment", e, "sql", query);
        }

        return list;
    }


    // Insert user
        public static boolean insertUser(User user) {
            String query = "INSERT INTO dbtrials.dbo.users (emp_code, user_name, password, full_name, phone, role, active, creation_date, department_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setInt(1, user.getEmpCode());
                ps.setString(2, user.getUserName());
                ps.setString(3, user.getPassword());
                ps.setString(4, user.getFullName());
                ps.setString(5, user.getPhone());
                ps.setInt(6, user.getRole());
                ps.setInt(7, user.getActive());
                ps.setString(8, user.getCreationDate());
                ps.setInt(9, user.getDepartmentId());

                return ps.executeUpdate() > 0;

            } catch (Exception e) {
                Logging.logExpWithMessage("ERROR", UserDAO.class.getName(), "insertUser", e, "sql", query);
            }
            return false;
        }

        // Update user
        public static boolean updateUser(User user) {
            String query = "UPDATE dbtrials.dbo.users SET emp_code = ?, user_name = ?, password = ?, full_name = ?, phone = ?, " +
                    "role = ?, active = ?, department_id = ? WHERE user_id = ?";

            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setInt(1, user.getEmpCode());
                ps.setString(2, user.getUserName());
                ps.setString(3, user.getPassword());
                ps.setString(4, user.getFullName());
                ps.setString(5, user.getPhone());
                ps.setInt(6, user.getRole());
                ps.setInt(7, user.getActive());
                ps.setInt(8, user.getDepartmentId());
                ps.setInt(9, user.getUserId());

                return ps.executeUpdate() > 0;

            } catch (Exception e) {
                Logging.logExpWithMessage("ERROR", UserDAO.class.getName(), "updateUser", e, "sql", query);
            }
            return false;
        }

        // Delete user
        public static boolean deleteUser(int userId) {
            String query = "DELETE FROM dbtrials.dbo.users WHERE user_id = ?";

            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setInt(1, userId);
                return ps.executeUpdate() > 0;

            } catch (Exception e) {
                Logging.logExpWithMessage("ERROR", UserDAO.class.getName(), "deleteUser", e, "sql", query);
            }
            return false;
        }

        // Get user by username
        public static User getUserByUsername(String username) {
            String query = """
        SELECT u.*, d.department_name
        FROM dbtrials.dbo.users u
        JOIN dbtrials.dbo.departments d ON u.department_id = d.department_id
        WHERE u.user_name = ?
    """;
            User user = null;

            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, username);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        user = new User(
                                rs.getInt("user_id"),
                                rs.getInt("emp_code"),
                                rs.getString("user_name"),
                                rs.getString("password"),
                                rs.getString("full_name"),
                                rs.getString("phone"),
                                rs.getInt("role"),
                                rs.getInt("active"),
                                rs.getString("creation_date"),
                                rs.getInt("department_id"),
                                rs.getString("department_name")
                        );
                    }
                }

            } catch (Exception e) {
                Logging.logExpWithMessage("ERROR", UserDAO.class.getName(), "getUserByUsername", e, "sql", query);
            }

            return user;
        }
    public static User getUserByUserId(int userId) {
        String query = """
        SELECT u.*, d.department_name
        FROM dbtrials.dbo.users u
        JOIN dbtrials.dbo.departments d ON u.department_id = d.department_id
        WHERE u.user_id = ?
    """;

        User user = null;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, userId);  // استخدم setInt وليس setString

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("user_id"),
                            rs.getInt("emp_code"),
                            rs.getString("user_name"),
                            rs.getString("password"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getInt("role"),
                            rs.getInt("active"),
                            rs.getString("creation_date"),
                            rs.getInt("department_id"),
                            rs.getString("department_name")
                    );
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDAO.class.getName(), "getUserByUserId", e, "sql", query);
        }

        return user;
    }


      public static User checkConfirmPassword(String username, String password) {
        String query = """
        SELECT u.*, d.department_name
        FROM dbtrials.dbo.users u
        JOIN dbtrials.dbo.departments d ON u.department_id = d.department_id
        WHERE u.user_name = ? AND u.password = ?
    """;
        User user = null;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("user_id"),
                            rs.getInt("emp_code"),
                            rs.getString("user_name"),
                            rs.getString("password"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getInt("role"),
                            rs.getInt("active"),
                            rs.getString("creation_date"),
                            rs.getInt("department_id"),
                            rs.getString("department_name")
                    );
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", UserDAO.class.getName(), "checkConfirmPassword", e, "sql", query);
        }

        return user;
    }

    public static ObservableList<User> getUsersByDepartmentId(Integer departmentId) {
        ObservableList<User> users = FXCollections.observableArrayList();
        String sql = "SELECT user_id, full_name, department_id FROM users";
        if (departmentId != null) {
            sql += " WHERE department_id = ?";
        }

        try (Connection conn = DbConnect.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (departmentId != null) {
                stmt.setInt(1, departmentId);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setDepartmentId(rs.getInt("department_id"));
                users.add(user);
            }
        } catch (SQLException e) {
            Logging.logException("ERROR", UserDAO.class.getName(), "getUsersByDepartmentId", e);
        }
        return users;
    }
}









