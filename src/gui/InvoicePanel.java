package gui;

import connectDB.DatabaseConnection;
import dao.InvoiceDAO;
import dao.InvoiceDetailDAO;
import entity.Invoice;
import entity.InvoiceDetail;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class InvoicePanel extends JPanel {
    private JPanel mainPanel, leftPanel, rightPanel;
    private JPanel searchPanel, invoiceTablePanel, receiptPanel, receiptTablePanel, totalPanel;
    private JButton btnSearch, btnReset, btnPrint;
    private JTextField txtInvoiceId, txtDate, txtReceiptInvoiceId, txtReceiptDate, txtTotal;
    private JTable invoiceTable, receiptTable;
    private JLabel lblTitle;
    private DecimalFormat currencyFormat = new DecimalFormat("#,###");
    private InvoiceDAO invoiceDAO;
    private InvoiceDetailDAO invoiceDetailDAO;

    public InvoicePanel() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            invoiceDAO = new InvoiceDAO(connection);
            invoiceDetailDAO = new InvoiceDetailDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        setLayout(new BorderLayout());
        
        createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        loadInvoiceData();
    }

    private void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblTitle = new JLabel("CHI TIẾT HÓA ĐƠN", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(26, 82, 118));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(800);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);

        createLeftPanel();
        splitPane.setLeftComponent(leftPanel);

        createRightPanel();
        splitPane.setRightComponent(rightPanel);

        mainPanel.add(splitPane, BorderLayout.CENTER);
    }

    private void createLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout(0, 10));
        leftPanel.setBackground(new Color(240, 240, 240));

        createSearchPanel();
        leftPanel.add(searchPanel, BorderLayout.NORTH);

        createInvoiceTablePanel();
        leftPanel.add(invoiceTablePanel, BorderLayout.CENTER);
    }

    private void createSearchPanel() {
        searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(26, 82, 118)),
                "Tìm kiếm");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        titledBorder.setTitleColor(new Color(26, 82, 118));
        titledBorder.setTitlePosition(TitledBorder.TOP);
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                titledBorder,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        fieldsPanel.setBackground(Color.WHITE);

        JPanel idPanel = createFieldPanel("Mã Hóa đơn:");
        txtInvoiceId = new JTextField();
        txtInvoiceId.setToolTipText("Nhập mã hóa đơn");
        idPanel.add(txtInvoiceId, BorderLayout.CENTER);

        JPanel searchBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchBtnPanel.setBackground(Color.WHITE);
        btnSearch = createActionButton("Tìm", new Color(70, 130, 180));
        searchBtnPanel.add(btnSearch);

        JPanel datePanel = createFieldPanel("Ngày:");
        txtDate = new JTextField();
        txtDate.setToolTipText("Nhập ngày (dd/MM/yyyy)");
        datePanel.add(txtDate, BorderLayout.CENTER);

        JPanel resetBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        resetBtnPanel.setBackground(Color.WHITE);
        btnReset = createActionButton("Làm mới", new Color(100, 149, 237));
        resetBtnPanel.add(btnReset);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Color.WHITE);

        JPanel printBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        printBtnPanel.setBackground(Color.WHITE);
        btnPrint = createActionButton("In hóa đơn", new Color(139, 69, 19));
        printBtnPanel.add(btnPrint);

        fieldsPanel.add(idPanel);
        fieldsPanel.add(searchBtnPanel);
        fieldsPanel.add(datePanel);
        fieldsPanel.add(resetBtnPanel);
        fieldsPanel.add(emptyPanel);
        fieldsPanel.add(printBtnPanel);

        searchPanel.add(fieldsPanel, BorderLayout.CENTER);
    }

    private void createInvoiceTablePanel() {
        invoiceTablePanel = new JPanel(new BorderLayout());
        invoiceTablePanel.setBackground(Color.WHITE);
        invoiceTablePanel.setBorder(BorderFactory.createLineBorder(new Color(26, 82, 118)));

        String[] columnNames = {"Mã hóa đơn", "Ngày", "Tổng tiền"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        invoiceTable = new JTable(model);
        invoiceTable.setRowHeight(30);
        invoiceTable.setSelectionBackground(new Color(173, 216, 230));
        invoiceTable.setGridColor(new Color(200, 200, 200));
        invoiceTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        invoiceTable.getTableHeader().setBackground(new Color(26, 82, 118));
        invoiceTable.getTableHeader().setForeground(Color.WHITE);
        invoiceTable.setFont(new Font("Arial", Font.PLAIN, 12));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < invoiceTable.getColumnCount(); i++) {
            invoiceTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        invoiceTablePanel.add(scrollPane, BorderLayout.CENTER);

        invoiceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = invoiceTable.getSelectedRow();
                if (selectedRow >= 0) {
                    displayInvoiceDetails(selectedRow);
                }
            }
        });
    }

    private void createRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(0, 10));
        rightPanel.setBackground(new Color(240, 240, 240));

        createReceiptPanel();
        rightPanel.add(receiptPanel, BorderLayout.CENTER);
    }

    private void createReceiptPanel() {
        receiptPanel = new JPanel();
        receiptPanel.setLayout(new BorderLayout(0, 10));
        receiptPanel.setBackground(Color.WHITE);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(26, 82, 118)),
                "Chi tiết hóa đơn");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        titledBorder.setTitleColor(new Color(26, 82, 118));
        titledBorder.setTitlePosition(TitledBorder.TOP);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        receiptPanel.setBorder(BorderFactory.createCompoundBorder(
                titledBorder,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JPanel headerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        headerPanel.setBackground(Color.WHITE);

        JPanel invoicePanel = new JPanel(new BorderLayout(5, 0));
        invoicePanel.setBackground(Color.WHITE);
        invoicePanel.add(new JLabel("Mã hóa đơn:"), BorderLayout.WEST);
        txtReceiptInvoiceId = new JTextField();
        txtReceiptInvoiceId.setEditable(false);
        invoicePanel.add(txtReceiptInvoiceId, BorderLayout.CENTER);

        JPanel datePanel = new JPanel(new BorderLayout(5, 0));
        datePanel.setBackground(Color.WHITE);
        datePanel.add(new JLabel("Ngày:"), BorderLayout.WEST);
        txtReceiptDate = new JTextField();
        txtReceiptDate.setEditable(false);
        datePanel.add(txtReceiptDate, BorderLayout.CENTER);

        headerPanel.add(invoicePanel);
        headerPanel.add(datePanel);

        receiptPanel.add(headerPanel, BorderLayout.NORTH);

        createReceiptTablePanel();
        receiptPanel.add(receiptTablePanel, BorderLayout.CENTER);

        createTotalPanel();
        receiptPanel.add(totalPanel, BorderLayout.SOUTH);
    }

    private void createReceiptTablePanel() {
        receiptTablePanel = new JPanel(new BorderLayout());
        receiptTablePanel.setBackground(Color.WHITE);

        String[] columnNames = {"Tên SP", "SL", "Mức Giá", "Thành Tiền"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        receiptTable = new JTable(model);
        receiptTable.setRowHeight(30);
        receiptTable.setSelectionBackground(new Color(173, 216, 230));
        receiptTable.setGridColor(new Color(200, 200, 200));
        receiptTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        receiptTable.getTableHeader().setBackground(new Color(26, 82, 118));
        receiptTable.getTableHeader().setForeground(Color.WHITE);
        receiptTable.setFont(new Font("Arial", Font.PLAIN, 12));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < receiptTable.getColumnCount(); i++) {
            receiptTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(receiptTable);
        scrollPane.setPreferredSize(new Dimension(450, 300));
        receiptTablePanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void createTotalPanel() {
        totalPanel = new JPanel(new BorderLayout(0, 10));
        totalPanel.setBackground(Color.WHITE);

        JPanel amountPanel = new JPanel(new BorderLayout(10, 0));
        amountPanel.setBackground(Color.WHITE);
        amountPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel lblTotal = new JLabel("Tổng tiền:");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 30));
        lblTotal.setHorizontalAlignment(JLabel.RIGHT);

        txtTotal = new JTextField("0");
        txtTotal.setFont(new Font("Arial", Font.BOLD, 50));
        txtTotal.setHorizontalAlignment(JTextField.RIGHT);
        txtTotal.setEditable(false);
        txtTotal.setBackground(Color.WHITE);
        txtTotal.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        amountPanel.add(lblTotal, BorderLayout.WEST);
        amountPanel.add(txtTotal, BorderLayout.CENTER);

        totalPanel.add(amountPanel, BorderLayout.NORTH);

        btnSearch.addActionListener(e -> searchInvoice());
        btnReset.addActionListener(e -> resetSearchForm());
        btnPrint.addActionListener(e -> printInvoice());
    }

    private JPanel createFieldPanel(String labelText) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setPreferredSize(new Dimension(120, 25));

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
        button.setPreferredSize(new Dimension(100, 30));
        return button;
    }

    private void loadInvoiceData() {
        try {
            DefaultTableModel model = (DefaultTableModel) invoiceTable.getModel();
            model.setRowCount(0);
            for (Invoice invoice : invoiceDAO.getAllInvoices()) {
                model.addRow(new Object[]{
                        invoice.getInvoiceId(),
                        invoice.getInvoiceDate(),
                        currencyFormat.format(invoice.getTotalAmount())
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayInvoiceDetails(int row) {
        try {
            String invoiceId = invoiceTable.getValueAt(row, 0).toString();
            String invoiceDate = invoiceTable.getValueAt(row, 1).toString();
            String totalAmount = invoiceTable.getValueAt(row, 2).toString();

            txtReceiptInvoiceId.setText(invoiceId);
            txtReceiptDate.setText(invoiceDate);
            txtTotal.setText(totalAmount);

            DefaultTableModel model = (DefaultTableModel) receiptTable.getModel();
            model.setRowCount(0);
            for (InvoiceDetail detail : invoiceDetailDAO.getDetailsByInvoiceId(invoiceId)) {
                model.addRow(new Object[]{
                        detail.getProductName(),
                        detail.getQuantity(),
                        currencyFormat.format(detail.getUnitPrice()),
                        currencyFormat.format(detail.getTotalPrice())
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchInvoice() {
        try {
            String invoiceId = txtInvoiceId.getText().trim();
            String date = txtDate.getText().trim();

            DefaultTableModel model = (DefaultTableModel) invoiceTable.getModel();
            model.setRowCount(0);

            if (!invoiceId.isEmpty()) {
                Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
                if (invoice != null) {
                    model.addRow(new Object[]{
                            invoice.getInvoiceId(),
                            invoice.getInvoiceDate(),
                            currencyFormat.format(invoice.getTotalAmount())
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn với mã " + invoiceId, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (!date.isEmpty()) {
                for (Invoice invoice : invoiceDAO.getInvoicesByDate(date)) {
                    model.addRow(new Object[]{
                            invoice.getInvoiceId(),
                            invoice.getInvoiceDate(),
                            currencyFormat.format(invoice.getTotalAmount())
                    });
                }
                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn cho ngày " + date, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                loadInvoiceData();
            }

            ((DefaultTableModel) receiptTable.getModel()).setRowCount(0);
            txtReceiptInvoiceId.setText("");
            txtReceiptDate.setText("");
            txtTotal.setText("0");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetSearchForm() {
        txtInvoiceId.setText("");
        txtDate.setText("");
        loadInvoiceData();
        ((DefaultTableModel) receiptTable.getModel()).setRowCount(0);
        txtReceiptInvoiceId.setText("");
        txtReceiptDate.setText("");
        txtTotal.setText("0");
    }

    private void printInvoice() {
        int selectedRow = invoiceTable.getSelectedRow();
        if (selectedRow >= 0) {
            String invoiceId = invoiceTable.getValueAt(selectedRow, 0).toString();
            JOptionPane.showMessageDialog(this, "Đang in hóa đơn: " + invoiceId);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần in!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}