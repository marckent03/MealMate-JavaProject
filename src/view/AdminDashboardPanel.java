package view;

import controller.MealMateController;
import dao.OrderDAO;
import model.Order;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AdminDashboardPanel extends JPanel {
    private MealMateController controller;
    private OrderDAO orderDAO;
    private JTable ordersTable;
    private OrderTableModel tableModel;
    private List<Order> orders;

    public AdminDashboardPanel(MealMateController controller) {
        this.controller = controller;
        this.orderDAO = new OrderDAO();
        setLayout(new BorderLayout());
        setBackground(new Color(240, 243, 247));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel titleLabel = new JLabel("Admin Dashboard - Order Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 43, 54));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(255, 107, 53));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setOpaque(true);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        logoutBtn.addActionListener(e -> controller.logout());
        headerPanel.add(logoutBtn, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        tableModel = new OrderTableModel();
        ordersTable = new JTable(tableModel);
        ordersTable.setRowHeight(50);
        ordersTable.setFillsViewportHeight(true);
        ordersTable.setShowGrid(false);
        ordersTable.setIntercellSpacing(new Dimension(0, 0));
        ordersTable.getTableHeader().setBackground(new Color(240, 243, 247));
        ordersTable.getTableHeader().setForeground(new Color(55, 65, 77));
        ordersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        ordersTable.getColumnModel().getColumn(6).setPreferredWidth(150); // note column
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 226, 232), 1),
                new EmptyBorder(8, 8, 8, 8)));
        add(scrollPane, BorderLayout.CENTER);

        // Refresh button
        JButton refreshBtn = new JButton("Refresh Orders");
        refreshBtn.setBackground(new Color(42, 101, 255));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setOpaque(true);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        refreshBtn.addActionListener(e -> refreshOrders());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(248, 248, 252));
        bottomPanel.add(refreshBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshOrders();
    }

    public void refreshOrders() {
        orders = orderDAO.getAllOrders();
        tableModel.fireTableDataChanged();
    }

    private class OrderTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "User", "Meal", "Qty", "Pickup", "Status", "Admin Note", "Actions"};

        @Override
        public int getRowCount() { return orders == null ? 0 : orders.size(); }

        @Override
        public int getColumnCount() { return columns.length; }

        @Override
        public String getColumnName(int column) { return columns[column]; }

        @Override
        public Object getValueAt(int row, int col) {
            Order order = orders.get(row);
            switch (col) {
                case 0: return order.getId();
                case 1: return order.getUserName();
                case 2: return order.getMealName();
                case 3: return order.getQuantity();
                case 4: return order.getPickupTime();
                case 5: return order.getStatus();
                case 6: return order.getAdminNote();
                default: return null;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 7; // Actions column editable to open dialog
        }

        @Override
        public void setValueAt(Object value, int row, int column) {
            // not used, we will handle via dialog
        }
    }

    // Renderer and Editor for actions column
    class ButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setBackground(Color.WHITE);
        }
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();
            JButton manageBtn = new JButton("Manage");
            manageBtn.setBackground(new Color(255, 107, 53));
            manageBtn.setForeground(Color.WHITE);
            manageBtn.setOpaque(true);
            manageBtn.setBorderPainted(false);
            add(manageBtn);
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private Order currentOrder;
        private JButton manageBtn;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setBackground(Color.WHITE);
            manageBtn = new JButton("Manage");
            manageBtn.setBackground(new Color(255, 107, 53));
            manageBtn.setForeground(Color.WHITE);
            manageBtn.setOpaque(true);
            manageBtn.setBorderPainted(false);
            manageBtn.addActionListener(e -> showOrderManagementDialog(currentOrder));
            panel.add(manageBtn);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentOrder = orders.get(row);
            return panel;
        }

        @Override
        public Object getCellEditorValue() { return currentOrder; }
    }

    private void showOrderManagementDialog(Order order) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Manage Order #" + order.getId(), true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Order ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        dialog.add(new JLabel(String.valueOf(order.getId())), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("User:"), gbc);
        gbc.gridx = 1;
        dialog.add(new JLabel(order.getUserName()), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Meal:"), gbc);
        gbc.gridx = 1;
        dialog.add(new JLabel(order.getMealName() + " x" + order.getQuantity()), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Current Status:"), gbc);
        gbc.gridx = 1;
        JLabel statusLabel = new JLabel(order.getStatus());
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dialog.add(statusLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("Update Status:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Pending", "Accepted", "Ready", "Completed", "Cancelled"});
        statusCombo.setSelectedItem(order.getStatus());
        dialog.add(statusCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        dialog.add(new JLabel("Admin Note:"), gbc);
        gbc.gridx = 1;
        JTextArea noteArea = new JTextArea(3, 20);
        noteArea.setText(order.getAdminNote() == null ? "" : order.getAdminNote());
        JScrollPane noteScroll = new JScrollPane(noteArea);
        dialog.add(noteScroll, gbc);

        JButton updateBtn = new JButton("Update Order");
        updateBtn.setBackground(new Color(37, 160, 90));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setOpaque(true);
        updateBtn.setBorderPainted(false);
        updateBtn.setFocusPainted(false);
        if ("Cancelled".equals(order.getStatus())) {
            statusCombo.setEnabled(false);
            noteArea.setEnabled(false);
            updateBtn.setEnabled(false);
            JLabel cancelledHint = new JLabel("Cancelled orders cannot be modified.");
            cancelledHint.setForeground(Color.RED);
            cancelledHint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
            dialog.add(cancelledHint, gbc);
            gbc.gridy = 7;
        } else {
            updateBtn.addActionListener(e -> {
                String newStatus = (String) statusCombo.getSelectedItem();
                String note = noteArea.getText().trim();
                if (orderDAO.updateOrderStatus(order.getId(), newStatus, note)) {
                    JOptionPane.showMessageDialog(dialog, "Order updated!");
                    refreshOrders();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Update failed. Cancelled orders cannot be changed.");
                }
            });
            gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
            dialog.add(updateBtn, gbc);
        }

        dialog.setVisible(true);
    }

    // Set custom renderer/editor after table is created
    public void configureTable() {
        ordersTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        ordersTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox()));
    }
}