package com.elsewedyt.trialsapp.controller;

import com.elsewedyt.trialsapp.dao.AppContext;
import com.elsewedyt.trialsapp.dao.DepartmentDao;
import com.elsewedyt.trialsapp.dao.UserDao;
import com.elsewedyt.trialsapp.db.DEF;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.model.Department;
import com.elsewedyt.trialsapp.model.User;
import com.elsewedyt.trialsapp.model.UserContext;
import com.elsewedyt.trialsapp.service.WindowUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static com.elsewedyt.trialsapp.service.WindowUtils.*;

public class AddUserController implements Initializable {
    @FXML
    private Label page_title;

    @FXML
    private TextField emp_id_txtF;
    @FXML
    private TextField user_name_txtF;
    @FXML
    private PasswordField password_passF;
    @FXML
    private TextField full_name_txtF;
    @FXML
    private TextField phone_txtF;
    @FXML
    private ComboBox<String> userRole_ComBox;
    @FXML
    private ComboBox<String> userActive_ComBox;
    @FXML
    private ComboBox<Department> userDeptartment_ComBox;

    @FXML
    private Button saveUser_btn;
    int updatedUserId = 0;
    ObservableList listComboUserRole;
    ObservableList listComboUserActive;
    int indexUsers = -1;
    boolean update = false;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private final UserDao userDao = AppContext.getInstance().getUserDao();
    private final DepartmentDao departmentDao = AppContext.getInstance().getDepartmentDao();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> page_title.requestFocus());
        userDeptartment_ComBox.setItems(departmentDao.getAllDepartments());
        listComboUserRole = FXCollections.observableArrayList(DEF.USER_ROLE_SUPER_ADMIN, DEF.USER_ROLE_DEPARTMENT_MANAGER, DEF.USER_ROLE_SUPER_VISOR, DEF.USER_ROLE_USER_STRING);
        listComboUserActive = FXCollections.observableArrayList(DEF.USER_ACTIVE_STRING, DEF.USER_NOT_ACTIVE_STRING);
        userRole_ComBox.setItems(listComboUserRole);
        userActive_ComBox.setItems(listComboUserActive);
        // setCueser
        saveUser_btn.setCursor(Cursor.HAND);
        Platform.runLater(() -> {
            if (UserContext.getCurrentUser().getRole() != 4) {
                int deptId = UserContext.getCurrentUser().getDepartmentId();
                for (Department dept : userDeptartment_ComBox.getItems()) {
                    if (dept.getDepartmentId() == deptId) {
                        userDeptartment_ComBox.getSelectionModel().select(dept);
                        // userDeptartment_ComBox.setDisable(true); // منع التعديل
                        // جعل الكومبو "قراءة فقط" بدون أن يتغير شكله
                        userDeptartment_ComBox.setMouseTransparent(true);
                        userDeptartment_ComBox.setFocusTraversable(false);
                        break;
                    }
                }
            }
        });
    }

    public void setUserData(int userId, boolean update) {
        try {
            this.update = update;
            this.updatedUserId = userId;
            User us = userDao.getUserByUserId(userId);

            emp_id_txtF.setText(us.getEmpCode() + "");
            //emp_id_txtF.setEditable(false);

            user_name_txtF.setText(us.getUserName());
            user_name_txtF.setEditable(false);

            password_passF.setText(us.getPassword());
            full_name_txtF.setText(us.getFullName());
            phone_txtF.setText(us.getPhone());

            // Set department
            Department dept = departmentDao.getDepartmentById(us.getDepartmentId());
            userDeptartment_ComBox.getSelectionModel().select(dept);

            if (UserContext.getCurrentUser().getRole() == 4) {
                // Super Admin: allow changing department
                userDeptartment_ComBox.setDisable(false);
            } else {
                // Others: prevent changing department
                // userDeptartment_ComBox.setDisable(true);
                // جعل الكومبو "قراءة فقط" بدون أن يتغير شكله
                userDeptartment_ComBox.setMouseTransparent(true);
                userDeptartment_ComBox.setFocusTraversable(false);
            }

            // Set role
            int roleId = us.getRole();
            String roleStr = WindowUtils.getUserRoleStr(roleId);
            int userRoleIndex = listComboUserRole.indexOf(roleStr);
            userRole_ComBox.getSelectionModel().select(userRoleIndex);

            if (UserContext.getCurrentUser().getRole() < roleId) {
                // إذا كان المستخدم يعدل مستخدم أعلى منه صلاحية → اجعله للعرض فقط
                userRole_ComBox.setDisable(true);
            }

            // Set active
            int activeId = us.getActive();
            String activeStr = WindowUtils.getUserActiveStr(activeId);
            int userActiveIndex = listComboUserActive.indexOf(activeStr);
            userActive_ComBox.getSelectionModel().select(userActiveIndex);

        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "setUserData", ex);
        }
    }

    public void setSaveButton() {
        saveUser_btn.setText("Update");
    }

    void clearUserPage() {
        emp_id_txtF.clear();
        user_name_txtF.clear();
        password_passF.clear();
        full_name_txtF.clear();
        phone_txtF.clear();
        userRole_ComBox.getSelectionModel().clearSelection();
        userActive_ComBox.getSelectionModel().clearSelection();
        userDeptartment_ComBox.getSelectionModel().clearSelection();
    }

    boolean addUserHelp() {
        Department selectDepartment = null;

        try {
            Date currentDate = new Date();
            String creationDate = dateFormat.format(currentDate);

            List<String> validationErrors = new ArrayList<>();

            // تحديد القسم بناءً على صلاحيات المستخدم
            try {
                if (UserContext.getCurrentUser().getRole() == 4) {
                    // Super Admin: can choose department
                    selectDepartment = userDeptartment_ComBox.getValue();
                    if (selectDepartment == null) {
                        validationErrors.add("Select a Department.");
                    }
                } else {
                    // Department Manager: auto-select their own department // AI : ok i need when open auto select dept their own department with not editable
                    int deptId = UserContext.getCurrentUser().getDepartmentId();
                    for (Department dept : userDeptartment_ComBox.getItems()) {
                        if (dept.getDepartmentId() == deptId) {
                            selectDepartment = dept;
                            userDeptartment_ComBox.getSelectionModel().select(dept);
                            userDeptartment_ComBox.setDisable(true); // Prevent Edit
                            break;
                        }
                    }

                    if (selectDepartment == null) {
                        validationErrors.add("Department not found in combo box.");
                    }
                }
            } catch (Exception e) {
                Logging.logException("ERROR", this.getClass().getName(), "SelectDepartment By Role", e);
            }

            // Validate emp_code
            int emp_code = -1;
            try {
                emp_code = Integer.parseInt(emp_id_txtF.getText().trim());
                if (emp_code <= 0) {
                    validationErrors.add("Employee Code must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                validationErrors.add("Employee Code must be a valid number.");
            }

            // Validate user_name
            String user_name = user_name_txtF.getText();
            if (user_name == null || user_name.trim().isEmpty()) {
                validationErrors.add("User Name is required.");
            } else {
                // Check uniqueness
                User existingUser = userDao.getUserByUsername(user_name);
                if (existingUser != null) {
                    validationErrors.add("User Name already exists. Please choose another.");
                }
            }


            // Validate password
            String password = password_passF.getText();
            if (password == null || password.trim().isEmpty()) {
                validationErrors.add("Password is required.");
            }

            // Validate role selection
            String roleStr = userRole_ComBox.getSelectionModel().getSelectedItem();
            int roleInt = WindowUtils.getUserRoleInt(roleStr);
            int currentUserRole = UserContext.getCurrentUser().getRole();

            if (roleStr == null || roleStr.trim().isEmpty() || roleInt == -1) {
                validationErrors.add("User Role is required.");
            } else if (currentUserRole != 4 && roleInt > currentUserRole) {
                // Only Super Admin (4) can assign roles higher than their own
                validationErrors.add("You cannot assign a role higher than your own.");
            }


            // Validate active
            String activeStr = userActive_ComBox.getSelectionModel().getSelectedItem();
            int activeInt = WindowUtils.getUserActiveInt(activeStr);
            if (activeStr == null || activeStr.trim().isEmpty() || activeInt == -1) {
                validationErrors.add("Active Status is required.");
            }

            // Stop if any validation failed
            if (!validationErrors.isEmpty()) {
                WindowUtils.ALERT("Validation Error", String.join("\n", validationErrors), WindowUtils.ALERT_WARNING);
                return false;
            }

            // Optional fields
            String fullname = full_name_txtF.getText();
            String phone = phone_txtF.getText();

            // Build user object
            User us = new User();
            us.setDepartmentId(selectDepartment.getDepartmentId());
            us.setEmpCode(emp_code);
            us.setUserName(user_name);
            us.setPassword(password);
            us.setFullName(fullname);
            us.setPhone(phone);
            us.setRole(roleInt);
            us.setActive(activeInt);
            us.setCreationDate(creationDate);

            if (!userDao.insertUser(us)) {
                WindowUtils.ALERT("Error", "Failed to insert user into database.", WindowUtils.ALERT_ERROR);
                return false;
            }
            return true; // Added Sucessfuly
        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "addUserHelp", ex);
            WindowUtils.ALERT("Exception", "An unexpected error occurred.", WindowUtils.ALERT_ERROR);
            return false;
        }
    }


    public boolean UpdateUserHelp() {
        User us = null;
        try {
            int emp_code = Integer.parseInt(emp_id_txtF.getText());
            String password = password_passF.getText();
            String username = user_name_txtF.getText();
            String fullname = full_name_txtF.getText();
            String phone = phone_txtF.getText();
            Department selectedDept = userDeptartment_ComBox.getValue();

            String role = userRole_ComBox.getSelectionModel().getSelectedItem();
            int roleInt = WindowUtils.getUserRoleInt(role);
            int currentUserRole = UserContext.getCurrentUser().getRole();

            String active = userActive_ComBox.getSelectionModel().getSelectedItem();
            int activeInt = WindowUtils.getUserActiveInt(active);

            // Validation
            List<String> validationErrors = new ArrayList<>();
            // User Name Check
            User existingUser = userDao.getUserByUsername(username);
            if (existingUser != null && existingUser.getUserId() != this.updatedUserId) {
                validationErrors.add("User Name already exists. Please choose another.");
            }

            // Check role hierarchy
            if (currentUserRole != 4 && roleInt > currentUserRole) {
                validationErrors.add("You cannot assign a role higher than your own.");
            }

            // Check if department was changed by unauthorized user
            if (currentUserRole != 4 && selectedDept.getDepartmentId() != UserContext.getCurrentUser().getDepartmentId()) {
                validationErrors.add("You cannot change the department.");
            }

            if (!validationErrors.isEmpty()) {
                WindowUtils.ALERT("Validation Error", String.join("\n", validationErrors), WindowUtils.ALERT_WARNING);
                return false;
            }

            // Build user
            us = new User();
            us.setUserId(this.updatedUserId);
            us.setEmpCode(emp_code);
            us.setUserName(username);
            us.setPassword(password);
            us.setFullName(fullname);
            us.setPhone(phone);
            us.setRole(roleInt);
            us.setActive(activeInt);
            us.setDepartmentId(selectedDept.getDepartmentId());
            us.setCreationDate(userDao.getUserByUserId(this.updatedUserId).getCreationDate());


            // Execute update
            return userDao.updateUser(us);

        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "UpdateUserHelp", ex);
        }

        return false;
    }


    @FXML
    void saveUserButton(ActionEvent event) {
        try {
            if (!update) {
                boolean added = addUserHelp();  // تحقق من نجاح الإضافة
                if (added) {
                    clearUserPage();
                    WindowUtils.ALERT("Success", "User added successfully", WindowUtils.ALERT_INFORMATION);
                    CLOSE(event);
                    OPEN_VIEW_USERS_PAGE();
                }
            } else {
                boolean updated = UpdateUserHelp();
                if (updated) {
                    WindowUtils.ALERT("Success", "User updated successfully", WindowUtils.ALERT_INFORMATION);
                } else {
                    WindowUtils.ALERT("Warning", "User update failed or no changes made", WindowUtils.ALERT_WARNING);
                }
                clearUserPage();
                CLOSE(event);
                OPEN_VIEW_USERS_PAGE();
            }

        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "saveUserButton", ex);
        }
    }


}
