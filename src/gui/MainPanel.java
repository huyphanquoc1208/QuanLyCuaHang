package gui;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private JPanel mainPanel;
    private JLabel lblTitle;

    public MainPanel() {
        setLayout(new BorderLayout());
        
        createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }

    private void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblTitle = new JLabel("CHÀO MỪNG ĐẾN VỚI HỆ THỐNG QUẢN LÝ CỬA HÀNG", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(26, 82, 118));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        mainPanel.add(lblTitle, BorderLayout.NORTH);
        
        // Thêm nội dung trang chính ở đây nếu cần
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        contentPanel.setBackground(new Color(240, 240, 240));
        
        // Thêm các panel thông tin tổng quan
        contentPanel.add(createInfoPanel("Tổng số nhân viên", "10", new Color(70, 130, 180)));
        contentPanel.add(createInfoPanel("Tổng số sản phẩm", "20", new Color(46, 139, 87)));
        contentPanel.add(createInfoPanel("Tổng số hóa đơn", "96", new Color(178, 34, 34)));
        contentPanel.add(createInfoPanel("Doanh thu", "28,500,000 VNĐ", new Color(139, 69, 19)));
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
    private JPanel createInfoPanel(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(color, 2));
        
        JLabel lblTitle = new JLabel(title, JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(color);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JLabel lblValue = new JLabel(value, JLabel.CENTER);
        lblValue.setFont(new Font("Arial", Font.BOLD, 24));
        lblValue.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);
        
        return panel;
    }
}