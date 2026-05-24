package view;

import controller.MealMateController;
import dao.UserDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private MealMateController controller;
    private JTextField fullNameField, empIdField, emailField;
    private JPasswordField passwordField;
    private UserDAO userDAO;
    
    public RegisterPanel(MealMateController controller) {
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
                new EmptyBorder(26, 26, 26, 26)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 107, 53));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(titleLabel, gbc);
        
        JLabel joinLabel = new JLabel("Join MealMate");
        joinLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridy = 1;
        centerPanel.add(joinLabel, gbc);
        
        JLabel subLabel = new JLabel("Enter your details to start pre-ordering your office meals.");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subLabel.setForeground(Color.GRAY);
        gbc.gridy = 2;
        centerPanel.add(subLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        centerPanel.add(new JLabel("Full Name"), gbc);
        gbc.gridy = 4;
        fullNameField = new JTextField(15);
        fullNameField.setPreferredSize(new Dimension(300, 40));
        fullNameField.setBackground(new Color(248, 250, 254));
        fullNameField.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 232)));
        centerPanel.add(fullNameField, gbc);
        
        gbc.gridy = 5;
        centerPanel.add(new JLabel("Employee ID"), gbc);
        gbc.gridy = 6;
        empIdField = new JTextField(15);
        empIdField.setPreferredSize(new Dimension(300, 40));
        empIdField.setBackground(new Color(248, 250, 254));
        empIdField.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 232)));
        centerPanel.add(empIdField, gbc);
        
        gbc.gridy = 7;
        centerPanel.add(new JLabel("Work Email"), gbc);
        gbc.gridy = 8;
        emailField = new JTextField(15);
        emailField.setPreferredSize(new Dimension(300, 40));
        emailField.setBackground(new Color(248, 250, 254));
        emailField.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 232)));
        centerPanel.add(emailField, gbc);
        
        gbc.gridy = 9;
        centerPanel.add(new JLabel("Password"), gbc);
        gbc.gridy = 10;
        passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setBackground(new Color(248, 250, 254));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 232)));
        centerPanel.add(passwordField, gbc);
        
        JLabel termsLabel = new JLabel("<html><body style='text-align:center'>By creating an account, you agree to MealMate's<br>Terms of Service and Privacy Policy.</body></html>");
        termsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        termsLabel.setForeground(Color.GRAY);
        gbc.gridy = 11;
        centerPanel.add(termsLabel, gbc);
        
        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setBackground(new Color(255, 107, 53));
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setOpaque(true);
        signupBtn.setBorderPainted(false);
        signupBtn.setFocusPainted(false);
        signupBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        signupBtn.setPreferredSize(new Dimension(300, 44));
        signupBtn.addActionListener(e -> performRegister());
        gbc.gridy = 12;
        centerPanel.add(signupBtn, gbc);
        
        JLabel backToLogin = new JLabel("Already have an account? Sign In");
        backToLogin.setForeground(new Color(255, 107, 53));
        backToLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backToLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                controller.showLogin();
            }
        });
        gbc.gridy = 13;
        centerPanel.add(backToLogin, gbc);
        
        outerPanel.add(centerPanel, new GridBagConstraints());
        add(outerPanel, BorderLayout.CENTER);
    }
    
    private void performRegister() {
        String fullName = fullNameField.getText().trim();
        String empId = empIdField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (fullName.isEmpty() || empId.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }
        if (userDAO.register(fullName, empId, email, password)) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.");
            controller.showLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Employee ID or Email may already exist.");
        }
    }
    
    public void clearFields() {
        fullNameField.setText("");
        empIdField.setText("");
        emailField.setText("");
        passwordField.setText("");
    }
}