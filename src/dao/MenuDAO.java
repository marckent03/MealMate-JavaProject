package dao;

import model.MenuItem;
import utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {
    
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> menu = new ArrayList<>();
        String sql = "SELECT * FROM menu WHERE available = TRUE";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                menu.add(new MenuItem(rs.getInt("id"), rs.getString("name"),
                                      rs.getDouble("price"), rs.getString("description")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menu;
    }
    
    public MenuItem getMenuItemById(int id) {
        String sql = "SELECT * FROM menu WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new MenuItem(rs.getInt("id"), rs.getString("name"),
                                    rs.getDouble("price"), rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}