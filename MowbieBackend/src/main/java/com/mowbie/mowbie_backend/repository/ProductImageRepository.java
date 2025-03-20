package com.mowbie.mowbie_backend.repository;

import com.mowbie.mowbie_backend.config.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductImageRepository {
    public static int addProductImage(Long productId, String productImagePath) {
        try (Connection conn = Database.getConnection()){
            String sql = "INSERT INTO tb_product_images (product_id, product_image_path) VALUES(?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, productId);
            ps.setString(2, productImagePath);
            ps.executeUpdate();
            return 1;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n\n" + e.getMessage());
            return 0;
        }
    }

    public static int deleteProductImage(Long imageId) {
        try (Connection conn = Database.getConnection()){
            String sql = "DELETE FROM tb_product_images WHERE product_image_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, imageId);
            ps.executeUpdate();
            return 1;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n\n" + e.getMessage());
            return 0;
        }
    }
}
