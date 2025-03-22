package com.mowbie.mowbie_backend.repository;

import com.mowbie.mowbie_backend.config.Database;
import com.mowbie.mowbie_backend.model.Inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryRepository {
    public static List<Inventory> getAllInventories() {
        try (Connection conn = Database.getConnection()){
            String sql = "SELECT * FROM tb_inventory";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Inventory> inventories = new ArrayList<Inventory>();
            while (rs.next()) {
                inventories.add(new Inventory(
                        rs.getLong("inventory_id"),
                        rs.getLong("product_id"),
                        rs.getLong("stock_change"),
                        rs.getString("change_type"),
                        rs.getTimestamp("changed_at").toLocalDateTime()
                ));
            }
            return inventories;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n\n" + e.getMessage());
            return null;
        }
    }

    public static int addInventory(Long productId, Long stockChange, String changeType) {
        try (Connection conn = Database.getConnection()) {
            // Lấy tổng số lượng tồn kho hiện tại
            String checkStockSql = "SELECT COALESCE(SUM(stock_change), 0) FROM tb_inventory WHERE product_id = ?";
            PreparedStatement checkStockPs = conn.prepareStatement(checkStockSql);
            checkStockPs.setLong(1, productId);
            ResultSet rs = checkStockPs.executeQuery();

            long currentStock = 0;
            if (rs.next()) {
                currentStock = rs.getLong(1);
            }

            // Nếu là xuất hàng (order) và tồn kho không đủ, trả về 2
            if ("order".equalsIgnoreCase(changeType) && currentStock + stockChange < 0) {
                return 2;
            }

            // Thực hiện INSERT vào bảng tb_inventory
            String sql = "INSERT INTO tb_inventory (product_id, stock_change, change_type) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, productId);
            ps.setLong(2, stockChange);
            ps.setString(3, changeType);
            ps.executeUpdate();

            return 1; // Thành công
        } catch (SQLException e) {
            System.out.println("\n\n\n\n\n\n\n" + e.getMessage());
            return 0; // Lỗi SQL
        }
    }

}
