package com.elsewedyt.trialsapp.model;

public class User {
    private int userId;
    private int empCode;
    private String userName;
    private String password;
    private String fullName;
    private String phone;
    private int role;
    private int active;
    private String creationDate;
    private int departmentId;
    private String departmentName;



    public User() {}

    public User(int userId, int empCode, String userName, String password, String fullName,
                String phone, int role, int active, String creationDate, int departmentId, String departmentName) {
        this.userId = userId;
        this.empCode = empCode;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
        this.active = active;
        this.creationDate = creationDate;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getEmpCode() { return empCode; }
    public void setEmpCode(int empCode) { this.empCode = empCode; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }

    public int getActive() { return active; }
    public void setActive(int active) { this.active = active; }

    public String getCreationDate() { return creationDate; }
    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    @Override
    public String toString() {
        return fullName;
    }
}
