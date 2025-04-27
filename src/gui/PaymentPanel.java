package gui;

import connectDB.DatabaseConnection;
import dao.InvoiceDAO;
import dao.InvoiceDetailDAO;
import dao.ProductDAO;
import entity.Invoice;
import entity.InvoiceDetail;
import entity.Product;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentPanel extends JPanel {
    private JPanel mainPanel, leftPanel, rightPanel;
    private JPanel searchPanel, productTablePanel, receiptPanel, receiptTablePanel, totalPanel, actionPanel;
    private JButton btnSearch, btnReset, btnAdd, btnExport, btnDeleteOne, btnDelete;
    private JTextField txtProductId, txtProductName, txtInvoiceId, txtDate, txtTotal;
    private JComboBox<String> cmbProductType;
    private JTable productTable, receiptTable;
    private JLabel lblTitle;
    private double totalAmount = 0;
    private DecimalFormat currencyFormat = new DecimalFormat("#,###");
    private ProductDAO productDAO;
    private InvoiceDAO invoiceDAO;
    private InvoiceDetailDAO invoiceDetailDAO;

    public PaymentPanel() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            productDAO = new ProductDAO(connection);
            invoiceDAO = new InvoiceDAO(connection);
            invoiceDetailDAO = new InvoiceDetailDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        setLayout(new BorderLayout());
        
        createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        loadProductData();
        initializeInvoice();
    }

    private void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblTitle = new JLabel("THANH TOÁN", JLabel.CENTER);
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

        createProductTablePanel();
        leftPanel.add(productTablePanel, BorderLayout.CENTER);
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

        JPanel idPanel = createFieldPanel("Mã sản phẩm:");
        txtProductId = new JTextField();
        txtProductId.setToolTipText("Nhập mã sản phẩm");
        idPanel.add(txtProductId, BorderLayout.CENTER);

        JPanel searchBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchBtnPanel.setBackground(Color.WHITE);
        btnSearch = createActionButton("Tìm", new Color(70, 130, 180));
        searchBtnPanel.add(btnSearch);

        JPanel namePanel = createFieldPanel("Tên sản phẩm:");
        txtProductName = new JTextField();
        txtProductName.setToolTipText("Nhập tên sản phẩm");
        namePanel.add(txtProductName, BorderLayout.CENTER);

        JPanel resetBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        resetBtnPanel.setBackground(Color.WHITE);
        btnReset = createActionButton("Làm mới", new Color(100, 149, 237));
        resetBtnPanel.add(btnReset);

        JPanel typePanel = createFieldPanel("Loại sản phẩm:");
        cmbProductType = new JComboBox<>(new String[]{"", "Đồ ăn", "Đồ uống", "Đồ gia dụng"});
        cmbProductType.setBackground(Color.WHITE);
        typePanel.add(cmbProductType, BorderLayout.CENTER);

        JPanel addBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addBtnPanel.setBackground(Color.WHITE);
        btnAdd = createActionButton("Thêm", new Color(46, 139, 87));
        addBtnPanel.add(btnAdd);

        fieldsPanel.add(idPanel);
        fieldsPanel.add(searchBtnPanel);
        fieldsPanel.add(namePanel);
        fieldsPanel.add(resetBtnPanel);
        fieldsPanel.add(typePanel);
        fieldsPanel.add(addBtnPanel);

        searchPanel.add(fieldsPanel, BorderLayout.CENTER);
    }

    private void createProductTablePanel() {
        productTablePanel = new JPanel(new BorderLayout());
        productTablePanel.setBackground(Color.WHITE);
        productTablePanel.setBorder(BorderFactory.createLineBorder(new Color(26, 82, 118)));

        String[] columnNames = {"Mã sản Phẩm", "Tên sản phẩm", "Số lượng", "Loại sản phẩm", "Mức giá"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(model);
        productTable.setRowHeight(30);
        productTable.setSelectionBackground(new Color(173, 216, 230));
        productTable.setGridColor(new Color(200, 200, 200));
        productTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        productTable.getTableHeader().setBackground(new Color(26, 82, 118));
        productTable.getTableHeader().setForeground(Color.WHITE);
        productTable.setFont(new Font("Arial", Font.PLAIN, 12));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < productTable.getColumnCount(); i++) {
            productTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setPreferredSize(new Dimension(450, 400));
        productTablePanel.add(scrollPane, BorderLayout.CENTER);
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
                "Hóa đơn");
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
        txtInvoiceId = new JTextField();
        txtInvoiceId.setEditable(false);
        invoicePanel.add(txtInvoiceId, BorderLayout.CENTER);

        JPanel datePanel = new JPanel(new BorderLayout(5, 0));
        datePanel.setBackground(Color.WHITE);
        datePanel.add(new JLabel("Ngày:"), BorderLayout.WEST);
        txtDate = new JTextField(); // KHỞI TẠO txtDate
        txtDate.setEditable(false);
        datePanel.add(txtDate, BorderLayout.CENTER);

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

        createActionPanel();
        totalPanel.add(actionPanel, BorderLayout.CENTER);
    }

    private void createActionPanel() {
        actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionPanel.setBackground(Color.WHITE);

        btnExport = createActionButton("Xuất hóa đơn", new Color(139, 69, 19));
        btnDeleteOne = createActionButton("Xóa 1 SP", new Color(178, 34, 34));
        btnDelete = createActionButton("Xóa", new Color(178, 34, 34));

        actionPanel.add(btnExport);
        actionPanel.add(btnDeleteOne);
        actionPanel.add(btnDelete);

        btnSearch.addActionListener(e -> searchProduct());
        btnReset.addActionListener(e -> resetSearchForm());
        btnAdd.addActionListener(e -> addProductToInvoice());
        btnExport.addActionListener(e -> exportInvoice());
        btnDeleteOne.addActionListener(e -> deleteSelectedProductFromInvoice());
        btnDelete.addActionListener(e -> deleteAllProductsFromInvoice());
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
        button.setPreferredSize(new Dimension(120, 30));
        return button;
    }

    private void loadProductData() {
        try {
            DefaultTableModel model = (DefaultTableModel) productTable.getModel();
            model.setRowCount(0);
            for (Product prod : productDAO.getAllProducts()) {
                model.addRow(new Object[]{
                        prod.getProductId(),
                        prod.getProductName(),
                        prod.getQuantity(),
                        prod.getProductType(),
                        currencyFormat.format(prod.getPrice())
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeInvoice() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtInvoiceId.setText("HD" + System.currentTimeMillis());
        txtDate.setText(sdf.format(new Date()));
        txtTotal.setText("0");
        totalAmount = 0;
    }

    private void searchProduct() {
        try {
            String id = txtProductId.getText().trim();
            String name = txtProductName.getText().trim();
            String type = cmbProductType.getSelectedItem().toString();
            if (id.isEmpty() && name.isEmpty() && type.isEmpty()) {
                loadProductData();
                return;
            }

            DefaultTableModel model = (DefaultTableModel) productTable.getModel();
            model.setRowCount(0);
            for (Product prod : productDAO.searchProducts(id, name, type)) {
                model.addRow(new Object[]{
                        prod.getProductId(),
                        prod.getProductName(),
                        prod.getQuantity(),
                        prod.getProductType(),
                        currencyFormat.format(prod.getPrice())
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetSearchForm() {
        txtProductId.setText("");
        txtProductName.setText("");
        cmbProductType.setSelectedIndex(0);
        loadProductData();
    }

    private void addProductToInvoice() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                String productId = productTable.getValueAt(selectedRow, 0).toString();
                Product product = productDAO.getProductById(productId);
                if (product == null || product.getQuantity() <= 0) {
                    JOptionPane.showMessageDialog(this, "Sản phẩm không tồn tại hoặc đã hết hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String quantityStr = JOptionPane.showInputDialog(this, "Nhập số lượng:", "1");
                if (quantityStr == null) return;

                int quantity;
                try {
                    quantity = Integer.parseInt(quantityStr);
                    if (quantity <= 0 || quantity > product.getQuantity()) {
                        JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ hoặc vượt quá tồn kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DefaultTableModel model = (DefaultTableModel) receiptTable.getModel();
                double totalPrice = quantity * product.getPrice();
                totalAmount += totalPrice;

                model.addRow(new Object[]{
                        product.getProductName(),
                        quantity,
                        currencyFormat.format(product.getPrice()),
                        currencyFormat.format(totalPrice)
                });

                txtTotal.setText(currencyFormat.format(totalAmount));

                product.setQuantity(product.getQuantity() - quantity);
                productDAO.updateProduct(product);
                loadProductData();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm sản phẩm vào hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để thêm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedProductFromInvoice() {
        int selectedRow = receiptTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                String productName = receiptTable.getValueAt(selectedRow, 0).toString();
                int quantity = Integer.parseInt(receiptTable.getValueAt(selectedRow, 1).toString());
                double unitPrice = Double.parseDouble(receiptTable.getValueAt(selectedRow, 2).toString().replace(",", ""));
                double totalPrice = Double.parseDouble(receiptTable.getValueAt(selectedRow, 3).toString().replace(",", ""));

                Product product = productDAO.getProductById(productName);
                if (product != null) {
                    product.setQuantity(product.getQuantity() + quantity);
                    productDAO.updateProduct(product);
                }

                totalAmount -= totalPrice;
                txtTotal.setText(currencyFormat.format(totalAmount));

                DefaultTableModel model = (DefaultTableModel) receiptTable.getModel();
                model.removeRow(selectedRow);

                loadProductData();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa sản phẩm khỏi hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAllProductsFromInvoice() {
        DefaultTableModel model = (DefaultTableModel) receiptTable.getModel();
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Hóa đơn hiện tại trống!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tất cả sản phẩm trong hóa đơn?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                for (int i = 0; i < model.getRowCount(); i++) {
                    String productName = model.getValueAt(i, 0).toString();
                    int quantity = Integer.parseInt(model.getValueAt(i, 1).toString());
                    Product product = productDAO.getProductById(productName);
                    if (product != null) {
                        product.setQuantity(product.getQuantity() + quantity);
                        productDAO.updateProduct(product);
                    }
                }

                model.setRowCount(0);
                totalAmount = 0;
                txtTotal.setText("0");
                loadProductData();
                JOptionPane.showMessageDialog(this, "Đã xóa tất cả sản phẩm trong hóa đơn!");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa tất cả sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportInvoice() {
        DefaultTableModel model = (DefaultTableModel) receiptTable.getModel();
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Hóa đơn hiện tại trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Invoice invoice = new Invoice();
            invoice.setInvoiceId(txtInvoiceId.getText());
            invoice.setInvoiceDate(txtDate.getText());
            invoice.setTotalAmount(totalAmount);

            invoiceDAO.addInvoice(invoice);

            for (int i = 0; i < model.getRowCount(); i++) {
                InvoiceDetail detail = new InvoiceDetail();
                detail.setInvoiceId(invoice.getInvoiceId());
                detail.setProductName(model.getValueAt(i, 0).toString());
                detail.setQuantity(Integer.parseInt(model.getValueAt(i, 1).toString()));
                detail.setUnitPrice(Double.parseDouble(model.getValueAt(i, 2).toString().replace(",", "")));
                detail.setTotalPrice(Double.parseDouble(model.getValueAt(i, 3).toString().replace(",", "")));
                invoiceDetailDAO.addInvoiceDetail(detail);
            }

            JOptionPane.showMessageDialog(this, "Xuất hóa đơn thành công: " + invoice.getInvoiceId());
            model.setRowCount(0);
            totalAmount = 0;
            txtTotal.setText("0");
            initializeInvoice();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}