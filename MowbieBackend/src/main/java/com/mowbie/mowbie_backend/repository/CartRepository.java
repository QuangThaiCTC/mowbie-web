package com.mowbie.mowbie_backend.repository;

import com.mowbie.mowbie_backend.config.Database;
import com.mowbie.mowbie_backend.dto.CartItemDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartRepository {

    // Lấy danh sách sản phẩm trong giỏ hàng của người dùng
    public static List<CartItemDTO> getCartItemsByUserId(Long userId) {
        List<CartItemDTO> cartItems = new ArrayList<>();

        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT c.cart_id, c.quantity, c.added_at, " +
                    "p.product_id, p.product_name, p.product_image_path, p.product_price " +
                    "FROM tb_carts c " +
                    "JOIN tb_products p ON c.product_id = p.product_id " +
                    "WHERE c.user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CartItemDTO item = new CartItemDTO(
                        rs.getLong("cart_id"),
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_image_path"),
                        rs.getLong("quantity"),
                        rs.getBigDecimal("product_price"),
                        rs.getTimestamp("added_at").toLocalDateTime()
                );
                cartItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartItems;
    }

    // Lấy tổng số sản phẩm trong giỏ hàng của người dùng
    public static Long getCartItemCount(Long userId) {
        try (Connection conn = Database.getConnection()) {
            String sqlQuery = "SELECT SUM(quantity) AS total_items FROM tb_carts WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("total_items");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    // Thêm sản phẩm vào giỏ hàng
    public static int addItemToCart(Long userId, Long productId, Long quantity) {
        try (Connection conn = Database.getConnection()) {
            String sqlQuery = "INSERT INTO tb_carts (user_id, product_id, quantity) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ps.setLong(3, quantity);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    public static int updateCartItem(Long cartId, Long quantity) {
        try (Connection conn = Database.getConnection()) {
            String sqlQuery = "UPDATE tb_carts SET quantity = ? WHERE cart_id = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setLong(1, quantity);
            ps.setLong(2, cartId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public static int deleteCartItem(Long cartId) {
        try (Connection conn = Database.getConnection()) {
            String sqlQuery = "DELETE FROM tb_carts WHERE cart_id = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setLong(1, cartId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}

