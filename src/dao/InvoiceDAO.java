package dao;

import entity.Invoice;
import entity.InvoiceDetail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    private Connection connection;
    private InvoiceDetailDAO invoiceDetailDAO;

    public InvoiceDAO(Connection connection) {
        this.connection = connection;
        this.invoiceDetailDAO = new InvoiceDetailDAO(connection);
    }

    public void addInvoice(Invoice invoice) throws SQLException {
        String sql = "INSERT INTO invoices (invoice_id, invoice_date, total_amount) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, invoice.getInvoiceId());
            stmt.setString(2, invoice.getInvoiceDate());
            stmt.setDouble(3, invoice.getTotalAmount());
            stmt.executeUpdate();
        }
    }

    public List<Invoice> getAllInvoices() throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceId(rs.getString("invoice_id"));
                invoice.setInvoiceDate(rs.getString("invoice_date"));
                invoice.setTotalAmount(rs.getDouble("total_amount"));
                invoices.add(invoice);
            }
        }
        return invoices;
    }

    public Invoice getInvoiceById(String invoiceId) throws SQLException {
        String sql = "SELECT * FROM invoices WHERE invoice_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, invoiceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(rs.getString("invoice_id"));
                    invoice.setInvoiceDate(rs.getString("invoice_date"));
                    invoice.setTotalAmount(rs.getDouble("total_amount"));
                    return invoice;
                }
            }
        }
        return null;
    }

    public List<Invoice> getInvoicesByDate(String date) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE invoice_date = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(rs.getString("invoice_id"));
                    invoice.setInvoiceDate(rs.getString("invoice_date"));
                    invoice.setTotalAmount(rs.getDouble("total_amount"));
                    invoices.add(invoice);
                }
            }
        }
        return invoices;
    }

    public List<Invoice> getInvoicesByMonth(int month) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE SUBSTRING(invoice_date, 4, 2) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.format("%02d", month));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(rs.getString("invoice_id"));
                    invoice.setInvoiceDate(rs.getString("invoice_date"));
                    invoice.setTotalAmount(rs.getDouble("total_amount"));
                    invoices.add(invoice);
                }
            }
        }
        return invoices;
    }

    public List<Invoice> getInvoicesByYear(int year) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE SUBSTRING(invoice_date, 7, 4) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(year));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(rs.getString("invoice_id"));
                    invoice.setInvoiceDate(rs.getString("invoice_date"));
                    invoice.setTotalAmount(rs.getDouble("total_amount"));
                    invoices.add(invoice);
                }
            }
        }
        return invoices;
    }

    public List<Invoice> getInvoicesByMonthYear(int month, int year) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE SUBSTRING(invoice_date, 4, 2) = ? AND SUBSTRING(invoice_date, 7, 4) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.format("%02d", month));
            stmt.setString(2, String.valueOf(year));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(rs.getString("invoice_id"));
                    invoice.setInvoiceDate(rs.getString("invoice_date"));
                    invoice.setTotalAmount(rs.getDouble("total_amount"));
                    invoices.add(invoice);
                }
            }
        }
        return invoices;
    }

    public void saveToFile(String fileName, List<Invoice> invoices) throws IOException {
        String currentDir = System.getProperty("user.dir");
        String filePath = currentDir + File.separator + fileName;
        File file = new File(filePath);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            // Write header for invoices
            bufferedWriter.write("InvoiceId,InvoiceDate,TotalAmount");
            bufferedWriter.newLine();

            // Write each invoice and its details
            for (Invoice invoice : invoices) {
                String invoiceRow = String.format("%s,%s,%.2f",
                        invoice.getInvoiceId(),
                        invoice.getInvoiceDate(),
                        invoice.getTotalAmount());
                bufferedWriter.write(invoiceRow);
                bufferedWriter.newLine();

                // Write details header
                bufferedWriter.write("Details: ProductName,Quantity,UnitPrice,TotalPrice");
                bufferedWriter.newLine();

                // Get and write invoice details
                List<InvoiceDetail> details = invoiceDetailDAO.getDetailsByInvoiceId(invoice.getInvoiceId());
                for (InvoiceDetail detail : details) {
                    String detailRow = String.format("%s,%d,%.2f,%.2f",
                            detail.getProductName(),
                            detail.getQuantity(),
                            detail.getUnitPrice(),
                            detail.getTotalPrice());
                    bufferedWriter.write(detailRow);
                    bufferedWriter.newLine();
                }

                // Add empty line to separate invoices
                bufferedWriter.newLine();
            }
            System.out.println("Saved invoices to: " + filePath);
        } catch (SQLException e) {
            throw new IOException("Error retrieving invoice details: " + e.getMessage());
        } catch (IOException e) {
            throw new IOException("Error writing to file: " + e.getMessage());
        }
    }
}