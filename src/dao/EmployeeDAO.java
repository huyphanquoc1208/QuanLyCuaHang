package dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Employee;

public class EmployeeDAO {
    private Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    // Thêm nhân viên mới
    public void addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (employee_id, full_name, dob, phone, gender, position, salary) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employee.getEmployeeId());
            stmt.setString(2, employee.getFullName());
            stmt.setString(3, employee.getDob());
            stmt.setString(4, employee.getPhone());
            stmt.setString(5, employee.getGender());
            stmt.setString(6, employee.getPosition());
            stmt.setDouble(7, employee.getSalary());
            stmt.executeUpdate();
        }
    }

    // Lấy tất cả nhân viên
    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(rs.getString("employee_id"));
                employee.setFullName(rs.getString("full_name"));
                employee.setDob(rs.getString("dob"));
                employee.setPhone(rs.getString("phone"));
                employee.setGender(rs.getString("gender"));
                employee.setPosition(rs.getString("position"));
                employee.setSalary(rs.getDouble("salary"));
                employees.add(employee);
            }
        }
        return employees;
    }

    // Lấy nhân viên theo ID
    public Employee getEmployeeById(String employeeId) throws SQLException {
        String sql = "SELECT * FROM employees WHERE employee_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Employee employee = new Employee();
                    employee.setEmployeeId(rs.getString("employee_id"));
                    employee.setFullName(rs.getString("full_name"));
                    employee.setDob(rs.getString("dob"));
                    employee.setPhone(rs.getString("phone"));
                    employee.setGender(rs.getString("gender"));
                    employee.setPosition(rs.getString("position"));
                    employee.setSalary(rs.getDouble("salary"));
                    return employee;
                }
            }
        }
        return null;
    }

    // Cập nhật thông tin nhân viên
    public void updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE employees SET full_name = ?, dob = ?, phone = ?, gender = ?, position = ?, salary = ? WHERE employee_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employee.getFullName());
            stmt.setString(2, employee.getDob());
            stmt.setString(3, employee.getPhone());
            stmt.setString(4, employee.getGender());
            stmt.setString(5, employee.getPosition());
            stmt.setDouble(6, employee.getSalary());
            stmt.setString(7, employee.getEmployeeId());
            stmt.executeUpdate();
        }
    }

    // Xóa nhân viên
    public void deleteEmployee(String employeeId) throws SQLException {
        String sql = "DELETE FROM employees WHERE employee_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employeeId);
            stmt.executeUpdate();
        }
    }

    // Xóa tất cả nhân viên
    public void deleteAllEmployees() throws SQLException {
        String sql = "DELETE FROM employees";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }

    // Tìm kiếm nhân viên theo ID
    public Employee searchEmployee(String employeeId) throws SQLException {
        return getEmployeeById(employeeId);
    }

    // Lưu danh sách nhân viên vào file CSV
    public void saveToFile(String fileName) throws IOException {
        String currentDir = System.getProperty("user.dir");
        String filePath = currentDir + File.separator + fileName;
        File file = new File(filePath);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write("EmployeeId,FullName,DOB,Phone,Gender,Position,Salary");
            bufferedWriter.newLine();

            List<Employee> employees = getAllEmployees();
            for (Employee emp : employees) {
                String row = emp.getEmployeeId() + "," +
                             emp.getFullName() + "," +
                             emp.getDob() + "," +
                             emp.getPhone() + "," +
                             emp.getGender() + "," +
                             emp.getPosition() + "," +
                             emp.getSalary();
                bufferedWriter.write(row);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Error writing to file: " + e.getMessage());
        } catch (SQLException e) {
            throw new IOException("Error retrieving employee data: " + e.getMessage());
        }
    }
}