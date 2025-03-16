package com.mowbie.mowbie_backend.repository;

import com.mowbie.mowbie_backend.config.Database;
import com.mowbie.mowbie_backend.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    public static List<Category> getAllCategory() {
        try (Connection conn = Database.getConnection()){
            String sql = "select * from tb_categories";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(new Category(
                        rs.getLong("category_id"),
                        rs.getString("category_name")
                ));
            }
            return categories;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n" + e.getMessage());
            return null;
        }
    }

    public static int addCategory(Category category) {
        try (Connection conn = Database.getConnection()){
            String sql = "INSERT INTO tb_categories (category_name) VALUES (?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, category.getCategoryName());
            ps.executeUpdate();
            return 1;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n" + e.getMessage());
            return 0;
        }
    }

    public static int updateCategory(Category category) {
        try (Connection conn = Database.getConnection()){
            String sql = "UPDATE tb_categories SET category_name = ? WHERE category_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, category.getCategoryName());
            ps.setLong(2, category.getCategoryId());
            ps.executeUpdate();
            return 1;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n" + e.getMessage());
            return 0;
        }
    }

    public static int deleteCategory(Category category) {
        try (Connection conn = Database.getConnection()){
            String sql = "DELETE FROM tb_categories WHERE category_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, category.getCategoryId());
            ps.executeUpdate();
            return 1;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n" + e.getMessage());
            return 0;
        }
    }
}

