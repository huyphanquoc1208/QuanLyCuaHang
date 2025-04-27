package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.InvoiceDetail;

public class InvoiceDetailDAO {
    private Connection connection;

    public InvoiceDetailDAO(Connection connection) {
        this.connection = connection;
    }

    // Thêm chi tiết hóa đơn mới
    public void addInvoiceDetail(InvoiceDetail detail) throws SQLException {
        String sql = "INSERT INTO invoice_details (invoice_id, product_name, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, detail.getInvoiceId());
            stmt.setString(2, detail.getProductName());
            stmt.setInt(3, detail.getQuantity());
            stmt.setDouble(4, detail.getUnitPrice());
            stmt.setDouble(5, detail.getTotalPrice());
            stmt.executeUpdate();
        }
    }

    // Lấy chi tiết hóa đơn theo invoice_id
    public List<InvoiceDetail> getDetailsByInvoiceId(String invoiceId) throws SQLException {
        List<InvoiceDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM invoice_details WHERE invoice_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, invoiceId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    InvoiceDetail detail = new InvoiceDetail();
                    detail.setInvoiceId(rs.getString("invoice_id"));
                    detail.setProductName(rs.getString("product_name"));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setUnitPrice(rs.getDouble("unit_price"));
                    detail.setTotalPrice(rs.getDouble("total_price"));
                    details.add(detail);
                }
            }
        }
        return details;
    }

    // Xóa chi tiết hóa đơn theo invoice_id
    public void deleteDetailsByInvoiceId(String invoiceId) throws SQLException {
        String sql = "DELETE FROM invoice_details WHERE invoice_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, invoiceId);
            stmt.executeUpdate();
        }
    }
}