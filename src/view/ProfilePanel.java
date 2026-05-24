package view;

import controller.MealMateController;
import dao.OrderDAO;
import dao.UserDAO;
import model.Order;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProfilePanel extends JPanel {
    private MealMateController controller;
    private User user;
    private JTextField fullNameField, emailField;
    private JPasswordField passwordField;
    private JTable ordersTable;
    private OrderTableModel tableModel;
    private UserDAO userDAO;
    private OrderDAO orderDAO;
    private List<Order> orders;

    public ProfilePanel(MealMateController controller, User user) {
        this.controller = controller;
        this.user = user;
        this.userDAO = new UserDAO();
        this.orderDAO = new OrderDAO();
        setLayout(new BorderLayout());
        setBackground(new Color(240, 243, 247));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(22, 22, 22, 22));

        JButton backBtn = new JButton("← Back to Menu");
        backBtn.setBackground(new Color(42, 101, 255));
        backBtn.setForeground(Color.WHITE);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        backBtn.addActionListener(e -> controller.showDashboard());
        headerPanel.add(backBtn, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(33, 43, 54));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JButton logoutBtn = new JButton("Sign Out");
        logoutBtn.setBackground(new Color(255, 107, 53));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setOpaque(true);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        logoutBtn.addActionListener(e -> controller.logout());
        headerPanel.add(logoutBtn, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Main content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(240, 243, 247));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // User info card
        JPanel infoCard = createInfoCard();
        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBackground(new Color(240, 243, 247));
        topWrapper.add(infoCard, BorderLayout.NORTH);
        contentPanel.add(topWrapper, BorderLayout.NORTH);

        // Order history
        JPanel ordersWrapper = new JPanel(new BorderLayout());
        ordersWrapper.setBackground(new Color(240, 243, 247));
        ordersWrapper.setBorder(new EmptyBorder(20, 0, 0, 0));

        JLabel ordersLabel = new JLabel("Order History");
        ordersLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        ordersLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        ordersWrapper.add(ordersLabel, BorderLayout.NORTH);

        tableModel = new OrderTableModel();
        ordersTable = new JTable(tableModel);
        ordersTable.setRowHeight(45);
        ordersTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        ordersTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));
        ordersTable.setFillsViewportHeight(true);
        ordersTable.setShowGrid(false);
        ordersTable.setIntercellSpacing(new Dimension(0, 0));
        ordersTable.getTableHeader().setBackground(new Color(240, 243, 247));
        ordersTable.getTableHeader().setForeground(new Color(55, 65, 77));
        ordersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        JScrollPane tableScroll = new JScrollPane(ordersTable);
        tableScroll.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 226, 232), 1),
                new EmptyBorder(4, 4, 4, 4)));
        tableScroll.setPreferredSize(new Dimension(380, 260));
        ordersWrapper.add(tableScroll, BorderLayout.CENTER);

        contentPanel.add(ordersWrapper, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        refreshOrders();
    }

    private JPanel createInfoCard() {
        JPanel infoCard = new JPanel(new GridBagLayout());
        infoCard.setBackground(Color.WHITE);
        infoCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 226, 232), 1),
                new EmptyBorder(18, 18, 18, 18)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        infoCard.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        fullNameField = new JTextField(user.getFullName(), 15);
        fullNameField.setEnabled(false);
        fullNameField.setBackground(new Color(248, 250, 254));
        fullNameField.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 232)));
        infoCard.add(fullNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        infoCard.add(new JLabel("Employee ID:"), gbc);
        gbc.gridx = 1;
        JLabel empIdLabel = new JLabel(user.getEmployeeId());
        infoCard.add(empIdLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        infoCard.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(user.getEmail(), 15);
        emailField.setEnabled(false);
        emailField.setBackground(new Color(248, 250, 254));
        emailField.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 232)));
        infoCard.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        infoCard.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setEnabled(false);
        passwordField.setBackground(new Color(248, 250, 254));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 232)));
        infoCard.add(passwordField, gbc);

        JPanel editBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        editBtnPanel.setBackground(Color.WHITE);
        JButton editBtn = new JButton("Edit Profile");
        editBtn.setBackground(new Color(42, 101, 255));
        editBtn.setForeground(Color.WHITE);
        editBtn.setOpaque(true);
        editBtn.setBorderPainted(false);
        editBtn.setFocusPainted(false);
        editBtn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setBackground(new Color(37, 160, 90));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setOpaque(true);
        saveBtn.setBorderPainted(false);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        saveBtn.setVisible(false);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(244, 67, 54));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setOpaque(true);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        cancelBtn.setVisible(false);

        editBtn.addActionListener(e -> {
            fullNameField.setEnabled(true);
            passwordField.setEnabled(true);
            editBtn.setVisible(false);
            saveBtn.setVisible(true);
            cancelBtn.setVisible(true);
        });

        saveBtn.addActionListener(e -> {
            String newName = fullNameField.getText().trim();
            String newPass = new String(passwordField.getPassword());
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty.");
                return;
            }
            if (newPass.isEmpty()) newPass = "default123";
            if (userDAO.updateUser(user.getId(), newName, user.getEmail(), newPass)) {
                user.setFullName(newName);
                JOptionPane.showMessageDialog(this, "Profile updated successfully.");
                fullNameField.setEnabled(false);
                emailField.setEnabled(false);
                passwordField.setEnabled(false);
                editBtn.setVisible(true);
                saveBtn.setVisible(false);
                cancelBtn.setVisible(false);
                passwordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.");
            }
        });

        cancelBtn.addActionListener(e -> {
            fullNameField.setText(user.getFullName());
            passwordField.setText("");
            fullNameField.setEnabled(false);
            passwordField.setEnabled(false);
            editBtn.setVisible(true);
            saveBtn.setVisible(false);
            cancelBtn.setVisible(false);
        });

        JLabel immutableNote = new JLabel("Email and Employee ID cannot be changed.");
        immutableNote.setForeground(new Color(112, 118, 125));
        immutableNote.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        infoCard.add(immutableNote, gbc);
        gbc.gridy = 5;
        editBtnPanel.add(editBtn);
        editBtnPanel.add(saveBtn);
        editBtnPanel.add(cancelBtn);
        infoCard.add(editBtnPanel, gbc);

        return infoCard;
    }

    public void refreshUserInfo() {
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getEmail());
    }

    public void refreshOrders() {
        orders = orderDAO.getOrdersByUser(user.getId());
        tableModel.fireTableDataChanged();
    }

    private class OrderTableModel extends AbstractTableModel {
        private final String[] columns = {"Meal", "Qty", "Pickup Time", "Status", "Total", "Admin Note", "Actions"};

        @Override
        public int getRowCount() {
            return orders == null ? 0 : orders.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Order order = orders.get(rowIndex);
            switch (columnIndex) {
                case 0: return order.getMealName();
                case 1: return order.getQuantity();
                case 2: return order.getPickupTime();
                case 3: return order.getStatus();
                case 4: return String.format("Php %.2f", order.getTotal());
                case 5: return order.getAdminNote() == null ? "" : order.getAdminNote();
                case 6: return order; // return the order object for action rendering
                default: return null;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 6; // only actions column is editable (to trigger editor)
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            // not used
        }
    }

    // Renderer for actions column - shows buttons as panel
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
            setBackground(Color.WHITE);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();
            if (value instanceof Order) {
                Order order = (Order) value;
                if ("Pending".equals(order.getStatus())) {
                    JButton editBtn = new JButton("Edit");
                    editBtn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    editBtn.setBackground(new Color(42, 101, 255));
                    editBtn.setForeground(Color.WHITE);
                    editBtn.setOpaque(true);
                    editBtn.setBorderPainted(false);
                    JButton cancelBtn = new JButton("Cancel");
                    cancelBtn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    cancelBtn.setBackground(new Color(244, 67, 54));
                    cancelBtn.setForeground(Color.WHITE);
                    cancelBtn.setOpaque(true);
                    cancelBtn.setBorderPainted(false);
                    add(editBtn);
                    add(cancelBtn);
                } else {
                    JLabel statusLabel = new JLabel(order.getStatus());
                    statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    statusLabel.setForeground(order.getStatus().equals("Cancelled") ? Color.RED : new Color(76, 175, 80));
                    add(statusLabel);
                }
            }
            return this;
        }
    }

    // Editor for actions column - actually triggers actions
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private Order currentOrder;
        private JButton editButton, cancelButton;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            panel.setBackground(Color.WHITE);
            editButton = new JButton("Edit");
            editButton.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            editButton.setBackground(new Color(42, 101, 255));
            editButton.setForeground(Color.WHITE);
            editButton.setOpaque(true);
            editButton.setBorderPainted(false);
            cancelButton = new JButton("Cancel");
            cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            cancelButton.setBackground(new Color(244, 67, 54));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.setOpaque(true);
            cancelButton.setBorderPainted(false);

            editButton.addActionListener(e -> {
                if (currentOrder != null) {
                    showEditDialog(currentOrder);
                    fireEditingStopped();
                }
            });
            cancelButton.addActionListener(e -> {
                if (currentOrder != null) {
                    if (orderDAO.cancelOrder(currentOrder.getId())) {
                        refreshOrders();
                        JOptionPane.showMessageDialog(panel, "Order cancelled.");
                        fireEditingStopped();
                    } else {
                        JOptionPane.showMessageDialog(panel, "Cancel failed. Order may already be processed.");
                        fireEditingStopped();
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            panel.removeAll();
            if (value instanceof Order) {
                currentOrder = (Order) value;
                if ("Pending".equals(currentOrder.getStatus())) {
                    panel.add(editButton);
                    panel.add(cancelButton);
                } else {
                    JLabel statusLabel = new JLabel(currentOrder.getStatus());
                    statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    statusLabel.setForeground(currentOrder.getStatus().equals("Cancelled") ? Color.RED : new Color(76, 175, 80));
                    panel.add(statusLabel);
                }
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentOrder;
        }
    }

    private void showEditDialog(Order order) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Order", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JSpinner qtySpinner = new JSpinner(new SpinnerNumberModel(order.getQuantity(), 1, 10, 1));
        JComboBox<String> timeCombo = new JComboBox<>(new String[]{"12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM", "2:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM"});
        timeCombo.setSelectedItem(order.getPickupTime());

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        dialog.add(qtySpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Pickup Time:"), gbc);
        gbc.gridx = 1;
        dialog.add(timeCombo, gbc);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBackground(new Color(37, 160, 90));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setOpaque(true);
        updateBtn.setBorderPainted(false);
        updateBtn.addActionListener(e -> {
            int newQty = (Integer) qtySpinner.getValue();
            String newTime = (String) timeCombo.getSelectedItem();
            if (orderDAO.updateOrder(order.getId(), newQty, newTime)) {
                refreshOrders();
                JOptionPane.showMessageDialog(dialog, "Order updated!");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Update failed. Order may no longer be pending.");
                dialog.dispose();
            }
        });

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        dialog.add(updateBtn, gbc);
        dialog.setVisible(true);
    }
}