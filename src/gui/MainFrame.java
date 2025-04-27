package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
    private JPanel navPanel, contentPanel;
    private JButton btnEmployee, btnProduct, btnPayment, btnInvoice, btnStatistics, btnLogout;
    private CardLayout cardLayout;

    public MainFrame() {
        setTitle("Hệ Thống Quản Lý");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        createNavigationPanel();
        createContentPanel();

        add(navPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void createNavigationPanel() {
        navPanel = new JPanel();
        navPanel.setLayout(new BorderLayout());
        navPanel.setBackground(new Color(26, 82, 118));
        navPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        leftButtons.setBackground(new Color(26, 82, 118));

        btnEmployee = createNavButton("Nhân viên");
        btnProduct = createNavButton("Sản phẩm");
        btnPayment = createNavButton("Thanh toán");
        btnInvoice = createNavButton("Hóa đơn");
        btnStatistics = createNavButton("Thống kê");

        leftButtons.add(btnEmployee);
        leftButtons.add(btnProduct);
        leftButtons.add(btnPayment);
        leftButtons.add(btnInvoice);
        leftButtons.add(btnStatistics);

        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        rightButtons.setBackground(new Color(26, 82, 118));

        btnLogout = createNavButton("Đăng xuất");
        rightButtons.add(btnLogout);

        navPanel.add(leftButtons, BorderLayout.WEST);
        navPanel.add(rightButtons, BorderLayout.EAST);

        btnEmployee.addActionListener(e -> cardLayout.show(contentPanel, "Employee"));
        btnProduct.addActionListener(e -> cardLayout.show(contentPanel, "Product"));
        btnPayment.addActionListener(e -> cardLayout.show(contentPanel, "Payment"));
        btnInvoice.addActionListener(e -> cardLayout.show(contentPanel, "Invoice"));
        btnStatistics.addActionListener(e -> cardLayout.show(contentPanel, "Statistics"));
        btnLogout.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 30));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    private void createContentPanel() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new EmployeePanel(), "Employee");
        contentPanel.add(new ProductPanel(), "Product");
        contentPanel.add(new PaymentPanel(), "Payment");
        contentPanel.add(new InvoicePanel(), "Invoice");
        contentPanel.add(new StatisticsPanel(), "Statistics");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}