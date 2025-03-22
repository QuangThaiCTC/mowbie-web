package com.mowbie.mowbie_backend.repository;

import com.mowbie.mowbie_backend.config.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductSpecsRepository {
    public static int addProductSpec(Long productId, String specTitle, String specContent) {
        try (Connection conn = Database.getConnection()){
            String sql = "INSERT INTO tb_product_specs (product_id, specs_title, specs_content) VALUES(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, productId);
            ps.setString(2, specTitle);
            ps.setString(3, specContent);
            ps.executeUpdate();
            return 1;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n\n" + e.getMessage());
            return 0;
        }
    }

    public static int deleteProductSpec(Long specId) {
        try (Connection conn = Database.getConnection()){
            String sql = "DELETE FROM tb_product_specs WHERE product_spec_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, specId);
            ps.executeUpdate();
            return 1;
        } catch (SQLException e) {
            System.out.println("\n\n\n\n\n\n\n" + e.getMessage());
            return 0;
        }
    }
}
