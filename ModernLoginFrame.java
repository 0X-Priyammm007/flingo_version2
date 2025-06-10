import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ModernLoginFrame extends JFrame {
    JTextField usernameField = new JTextField(16);
    JPasswordField passwordField = new JPasswordField(16);

    public ModernLoginFrame() {
        setTitle("Flingo - Login / Register");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(248, 251, 255));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));

        JLabel title = new JLabel("Flingo");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(new Color(54, 97, 217));
        panel.add(title);

        panel.add(Box.createVerticalStrut(16));

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(userLabel);
        usernameField.setMaximumSize(new Dimension(300, 36));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setBackground(new Color(235,240,250));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,215,245), 1),
            BorderFactory.createEmptyBorder(8,12,8,12)
        ));
        panel.add(usernameField);

        panel.add(Box.createVerticalStrut(10));
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(passLabel);
        passwordField.setMaximumSize(new Dimension(300, 36));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setBackground(new Color(235,240,250));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,215,245), 1),
            BorderFactory.createEmptyBorder(8,12,8,12)
        ));
        panel.add(passwordField);

        panel.add(Box.createVerticalStrut(18));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setOpaque(false);
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");
        styleButton(loginBtn);
        styleButton(registerBtn);
        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);
        panel.add(btnPanel);

        panel.add(Box.createVerticalStrut(10));
        JLabel info = new JLabel(" ");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        info.setForeground(Color.RED);
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(info);

        loginBtn.addActionListener(e -> doLogin(info));
        registerBtn.addActionListener(e -> doRegister(info));

        setContentPane(panel);
        setVisible(true);
    }

    private void styleButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(54, 97, 217));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
    }

    private void doLogin(JLabel info) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            info.setText("Enter username and password.");
            return;
        }
        try (Connection con = DB.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT id FROM users WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");
                info.setForeground(new Color(55,180,80));
                info.setText("Login successful!");
                // Open dashboard
                new ModernDashboardFrame(new User(userId, username));
                dispose();
            } else {
                info.setForeground(Color.RED);
                info.setText("Invalid credentials.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            info.setForeground(Color.RED);
            info.setText("Database error.");
        }
    }

    private void doRegister(JLabel info) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            info.setText("Enter username and password.");
            return;
        }
        try (Connection con = DB.getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT id FROM users WHERE username=?");
            check.setString(1, username);
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                info.setForeground(Color.RED);
                info.setText("Username already exists.");
                return;
            }
            PreparedStatement ps = con.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            info.setForeground(new Color(55,180,80));
            info.setText("Registration successful! You can now log in.");
        } catch (Exception ex) {
            ex.printStackTrace();
            info.setForeground(Color.RED);
            info.setText("Database error.");
        }
    }
}