package gui;

import connectDB.DatabaseConnection;
import dao.EmployeeDAO;
import entity.Employee;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class EmployeePanel extends JPanel {
    private JPanel mainPanel, formPanel, tablePanel, buttonPanel;
    private JButton btnSearch, btnReset, btnAdd, btnDeleteOne, btnDelete, btnUpdate, btnPrint;
    private JTextField txtEmployeeId, txtFullName, txtDob, txtPhone, txtPosition, txtSalary;
    private JComboBox<String> cmbGender;
    private JTable employeeTable;
    private JLabel lblTitle;
    private EmployeeDAO employeeDAO;
    private DecimalFormat currencyFormat = new DecimalFormat("#,###");

    public EmployeePanel() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            employeeDAO = new EmployeeDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        setLayout(new BorderLayout());
        
        createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        loadEmployeeData();
    }

    private void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(26, 82, 118));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.SOUTH);
    }

    private void createFormPanel() {
        formPanel = new JPanel();
        formPanel.setLayout(new BorderLayout());
        formPanel.setBackground(Color.WHITE);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(26, 82, 118)),
                "Thông tin nhân viên");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        titledBorder.setTitleColor(new Color(26, 82, 118));
        titledBorder.setTitlePosition(TitledBorder.TOP);
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                titledBorder,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JPanel fieldsPanel = new JPanel(new GridLayout(3, 3, 15, 15));
        fieldsPanel.setBackground(Color.WHITE);

        JPanel idPanel = createFieldPanel("Mã nhân viên:");
        txtEmployeeId = new JTextField();
        idPanel.add(txtEmployeeId, BorderLayout.CENTER);

        JPanel phonePanel = createFieldPanel("Số điện thoại:");
        txtPhone = new JTextField();
        phonePanel.add(txtPhone, BorderLayout.CENTER);

        JPanel genderPanel = createFieldPanel("Giới tính:");
        cmbGender = new JComboBox<>(new String[]{"", "Nam", "Nữ"});
        genderPanel.add(cmbGender, BorderLayout.CENTER);

        JPanel namePanel = createFieldPanel("Họ tên:");
        txtFullName = new JTextField();
        namePanel.add(txtFullName, BorderLayout.CENTER);

        JPanel positionPanel = createFieldPanel("Chức vụ:");
        txtPosition = new JTextField();
        positionPanel.add(txtPosition, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);
        btnSearch = createActionButton("Tìm", new Color(70, 130, 180));
        searchPanel.add(btnSearch);

        JPanel dobPanel = createFieldPanel("Ngày sinh:");
        txtDob = new JTextField();
        dobPanel.add(txtDob, BorderLayout.CENTER);

        JPanel salaryPanel = createFieldPanel("Mức lương:");
        txtSalary = new JTextField();
        salaryPanel.add(txtSalary, BorderLayout.CENTER);

        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        resetPanel.setBackground(Color.WHITE);
        btnReset = createActionButton("Làm mới", new Color(100, 149, 237));
        resetPanel.add(btnReset);

        fieldsPanel.add(idPanel);
        fieldsPanel.add(phonePanel);
        fieldsPanel.add(genderPanel);
        fieldsPanel.add(namePanel);
        fieldsPanel.add(positionPanel);
        fieldsPanel.add(searchPanel);
        fieldsPanel.add(dobPanel);
        fieldsPanel.add(salaryPanel);
        fieldsPanel.add(resetPanel);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);
    }

    private JPanel createFieldPanel(String labelText) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setPreferredSize(new Dimension(100, 25));

        panel.add(label, BorderLayout.WEST);
        return panel;
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void createTablePanel() {
        tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(26, 82, 118)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        String[] columnNames = {"Mã nhân viên", "Họ tên", "Ngày sinh", "Số điện thoại", "Giới tính", "Chức vụ", "Mức lương"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(model);
        employeeTable.setRowHeight(30);
        employeeTable.setSelectionBackground(new Color(173, 216, 230));
        employeeTable.setGridColor(new Color(200, 200, 200));
        employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        employeeTable.getTableHeader().setBackground(new Color(26, 82, 118));
        employeeTable.getTableHeader().setForeground(Color.WHITE);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 12));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < employeeTable.getColumnCount(); i++) {
            employeeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setPreferredSize(new Dimension(1000, 600));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        btnAdd = createActionButton("Thêm", new Color(46, 139, 87));
        btnDeleteOne = createActionButton("Xóa 1 NV", new Color(178, 34, 34));
        btnDelete = createActionButton("Xóa", new Color(178, 34, 34));
        btnUpdate = createActionButton("Cập nhật", new Color(70, 130, 180));
        btnPrint = createActionButton("In", new Color(139, 69, 19));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDeleteOne);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnPrint);

        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addEmployee());
        btnDeleteOne.addActionListener(e -> deleteSelectedEmployee());
        btnDelete.addActionListener(e -> deleteAllEmployees());
        btnUpdate.addActionListener(e -> updateEmployee());
        btnPrint.addActionListener(e -> printEmployeeList());
        btnSearch.addActionListener(e -> searchEmployee());
        btnReset.addActionListener(e -> resetForm());

        employeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow >= 0) {
                    displayEmployeeData(selectedRow);
                }
            }
        });
    }

    private void loadEmployeeData() {
        try {
            DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
            model.setRowCount(0);
            for (Employee emp : employeeDAO.getAllEmployees()) {
                model.addRow(new Object[]{
                        emp.getEmployeeId(),
                        emp.getFullName(),
                        emp.getDob(),
                        emp.getPhone(),
                        emp.getGender(),
                        emp.getPosition(),
                        currencyFormat.format(emp.getSalary())
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addEmployee() {
        try {
            Employee employee = new Employee();
            employee.setEmployeeId(txtEmployeeId.getText().trim());
            employee.setFullName(txtFullName.getText().trim());
            employee.setDob(txtDob.getText().trim());
            employee.setPhone(txtPhone.getText().trim());
            employee.setGender(cmbGender.getSelectedItem().toString());
            employee.setPosition(txtPosition.getText().trim());
            String salaryText = txtSalary.getText().trim().replace(",", "");
            employee.setSalary(salaryText.isEmpty() ? 0 : Double.parseDouble(salaryText));

            if (employee.getEmployeeId().isEmpty() || employee.getFullName().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã và tên nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            employeeDAO.addEmployee(employee);
            loadEmployeeData();
            resetForm();
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Mức lương không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    String employeeId = employeeTable.getValueAt(selectedRow, 0).toString();
                    employeeDAO.deleteEmployee(employeeId);
                    loadEmployeeData();
                    resetForm();
                    JOptionPane.showMessageDialog(this, "Đã xóa nhân viên!");
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi xóa nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAllEmployees() {
        int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tất cả nhân viên?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                employeeDAO.deleteAllEmployees();
                loadEmployeeData();
                resetForm();
                JOptionPane.showMessageDialog(this, "Đã xóa tất cả nhân viên!");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa tất cả nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Employee employee = new Employee();
                employee.setEmployeeId(txtEmployeeId.getText().trim());
                employee.setFullName(txtFullName.getText().trim());
                employee.setDob(txtDob.getText().trim());
                employee.setPhone(txtPhone.getText().trim());
                employee.setGender(cmbGender.getSelectedItem().toString());
                employee.setPosition(txtPosition.getText().trim());
                String salaryText = txtSalary.getText().trim().replace(",", "");
                employee.setSalary(salaryText.isEmpty() ? 0 : Double.parseDouble(salaryText));

                if (employee.getEmployeeId().isEmpty() || employee.getFullName().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã và tên nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                employeeDAO.updateEmployee(employee);
                loadEmployeeData();
                resetForm();
                JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Mức lương không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần cập nhật!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void printEmployeeList() {
        try {
            String currentDir = System.getProperty("user.dir");
            String filePath = currentDir + File.separator + "employees.csv";
            employeeDAO.saveToFile("employees.csv");
            System.out.println("Saving employees to: " + filePath);
            JOptionPane.showMessageDialog(this, "Danh sách nhân viên đã được lưu vào: " + filePath, "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu danh sách nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchEmployee() {
        String searchId = txtEmployeeId.getText().trim();
        if (!searchId.isEmpty()) {
            try {
                Employee employee = employeeDAO.searchEmployee(searchId);
                if (employee != null) {
                    DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
                    model.setRowCount(0);
                    model.addRow(new Object[]{
                            employee.getEmployeeId(),
                            employee.getFullName(),
                            employee.getDob(),
                            employee.getPhone(),
                            employee.getGender(),
                            employee.getPosition(),
                            currencyFormat.format(employee.getSalary())
                    });
                    displayEmployeeData(0);
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên với mã " + searchId, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            loadEmployeeData();
        }
    }

    private void resetForm() {
        txtEmployeeId.setText("");
        txtFullName.setText("");
        txtDob.setText("");
        txtPhone.setText("");
        cmbGender.setSelectedIndex(0);
        txtPosition.setText("");
        txtSalary.setText("");
        employeeTable.clearSelection();
    }

    private void displayEmployeeData(int row) {
        txtEmployeeId.setText(employeeTable.getValueAt(row, 0).toString());
        txtFullName.setText(employeeTable.getValueAt(row, 1).toString());
        txtDob.setText(employeeTable.getValueAt(row, 2).toString());
        txtPhone.setText(employeeTable.getValueAt(row, 3).toString());
        cmbGender.setSelectedItem(employeeTable.getValueAt(row, 4).toString());
        txtPosition.setText(employeeTable.getValueAt(row, 5).toString());
        txtSalary.setText(employeeTable.getValueAt(row, 6).toString().replace(",", ""));
    }
}