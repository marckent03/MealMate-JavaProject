package controller;

import view.*;
import model.User;
import view.AdminDashboardPanel;
import view.DashboardPanel;
import view.LoginPanel;
import view.ProfilePanel;
import view.RegisterPanel;

import javax.swing.*;
import java.awt.*;

public class MealMateController extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private User currentUser;
    
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private DashboardPanel dashboardPanel;
    private ProfilePanel profilePanel;
    private AdminDashboardPanel adminDashboardPanel;
    
    public MealMateController() {
        initUI();
        setTitle("MealMate - Workplace Meal Ordering");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 760);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 243, 247));
        setVisible(true);
    }
    
    private void initUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(248, 248, 252));
        
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        
        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }
    
    public void showLogin() {
        cardLayout.show(mainPanel, "login");
        loginPanel.clearFields();
    }
    
    public void showRegister() {
        cardLayout.show(mainPanel, "register");
        registerPanel.clearFields();
    }
    
    public void loginSuccess(User user) {
        this.currentUser = user;
        if (user.isAdmin()) {
            adminDashboardPanel = new AdminDashboardPanel(this);
            adminDashboardPanel.configureTable();
            mainPanel.add(adminDashboardPanel, "admin");
            cardLayout.show(mainPanel, "admin");
        } else {
            dashboardPanel = new DashboardPanel(this, currentUser);
            profilePanel = new ProfilePanel(this, currentUser);
            mainPanel.add(dashboardPanel, "dashboard");
            mainPanel.add(profilePanel, "profile");
            cardLayout.show(mainPanel, "dashboard");
        }
    }

    public void showAdminDashboard() {
        if (adminDashboardPanel != null) {
            adminDashboardPanel.refreshOrders();
            cardLayout.show(mainPanel, "admin");
        }
    }
    
    public void showDashboard() {
        if (dashboardPanel != null) {
            dashboardPanel.refreshMenu();
            cardLayout.show(mainPanel, "dashboard");
        }
    }
    
    public void showProfile() {
        if (profilePanel != null) {
            profilePanel.refreshUserInfo();
            profilePanel.refreshOrders();
            cardLayout.show(mainPanel, "profile");
        }
    }
    
    public void logout() {
        currentUser = null;
        if (dashboardPanel != null) {
            mainPanel.remove(dashboardPanel);
            mainPanel.remove(profilePanel);
            dashboardPanel = null;
            profilePanel = null;
        }
        showLogin();
    }
    
    public User getCurrentUser() { return currentUser; }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MealMateController();
        });
    }
}