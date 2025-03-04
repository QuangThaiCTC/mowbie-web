package com.mowbie.mowbie_backend.repository;

import com.mowbie.mowbie_backend.config.Database;
import com.mowbie.mowbie_backend.dto.OrderDTO;
import com.mowbie.mowbie_backend.dto.OrderItemDTO;
import com.mowbie.mowbie_backend.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    public static List<OrderDTO> getAllOrders() {
        List<OrderDTO> orders = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM tb_orders";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Long orderId = rs.getLong("order_id");
                orders.add(new OrderDTO(
                        orderId,
                        rs.getLong("user_id"),
                        rs.getBigDecimal("total_price"),
                        rs.getString("order_status"),
                        rs.getTimestamp("order_time").toLocalDateTime(),
                        getOrderItemsByOrderId(orderId, conn)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static OrderDTO getOrderById(Long orderId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM tb_orders WHERE order_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new OrderDTO(
                        rs.getLong("order_id"),
                        rs.getLong("user_id"),
                        rs.getBigDecimal("total_price"),
                        rs.getString("order_status"),
                        rs.getTimestamp("order_time").toLocalDateTime(),
                        getOrderItemsByOrderId(orderId, conn)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<OrderItemDTO> getOrderItemsByOrderId(Long orderId, Connection conn) {
        List<OrderItemDTO> orderItems = new ArrayList<>();
        try {
            String sql = "SELECT oi.product_id, p.product_name, oi.order_item_quantity, oi.order_item_price " +
                    "FROM tb_order_items oi " +
                    "JOIN tb_products p ON oi.product_id = p.product_id " +
                    "WHERE oi.order_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                orderItems.add(new OrderItemDTO(
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getLong("order_item_quantity"),
                        rs.getBigDecimal("order_item_price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }
}
