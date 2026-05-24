package view;

import controller.MealMateController;
import dao.UserDAO;
import model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPanel extends JPanel {
    private MealMateController controller;
    private JTextField identifierField;
    private JPasswordField passwordField;
    private UserDAO userDAO;
    
    public LoginPanel(MealMateController controller) {
        this.controller = controller;
        this.userDAO = new UserDAO();
        setLayout(new BorderLayout());
        setBackground(new Color(240, 243, 247));
        
        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(new Color(240, 243, 247));
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 229, 235), 1),
                new EmptyBorder(28, 28, 28, 28)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("MealMate");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 107, 53));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(titleLabel, gbc);
        
        JLabel welcomeLabel = new JLabel("Welcome back!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        welcomeLabel.setForeground(Color.DARK_GRAY);
        gbc.gridy = 1;
        centerPanel.add(welcomeLabel, gbc);
        
        JLabel subLabel = new JLabel("Sign in to pre-order your meals and skip the rush.");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setForeground(Color.GRAY);
        gbc.gridy = 2;
        centerPanel.add(subLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        centerPanel.add(new JLabel("Employee ID or Email"), gbc);
        gbc.gridy = 4;
        identifierField = new JTextField(15);
        identifierField.setPreferredSize(new Dimension(300, 40));
        identifierField.setBackground(new Color(248, 250, 254));
        identifierField.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 232)));
        centerPanel.add(identifierField, gbc);
        
        gbc.gridy = 5;
        centerPanel.add(new JLabel("Password"), gbc);
        gbc.gridy = 6;
        passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setBackground(new Color(248, 250, 254));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 232)));
        centerPanel.add(passwordField, gbc);
        
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(255, 107, 53));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginBtn.setPreferredSize(new Dimension(300, 44));
        loginBtn.addActionListener(e -> performLogin());
        gbc.gridy = 7;
        centerPanel.add(loginBtn, gbc);
        
        JLabel createAccountLabel = new JLabel("Don't have an account? Create Account");
        createAccountLabel.setForeground(new Color(255, 107, 53));
        createAccountLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        createAccountLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                controller.showRegister();
            }
        });
        gbc.gridy = 8;
        centerPanel.add(createAccountLabel, gbc);
        
        outerPanel.add(centerPanel, new GridBagConstraints());
        add(outerPanel, BorderLayout.CENTER);
    }
    
    private void performLogin() {
        String identifier = identifierField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (identifier.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields.");
            return;
        }
        User user = userDAO.login(identifier, password);
        if (user != null) {
            controller.loginSuccess(user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
        }
    }
    
    public void clearFields() {
        identifierField.setText("");
        passwordField.setText("");
    }
}