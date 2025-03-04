package com.mowbie.mowbie_backend.repository;

import com.mowbie.mowbie_backend.config.Database;
import com.mowbie.mowbie_backend.dto.CategoryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    public static List<CategoryDTO> getAllCategories() {
        List<CategoryDTO> categories = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sqlQuery = "SELECT * FROM tb_categories";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                categories.add(new CategoryDTO(
                        rs.getLong("category_id"),
                        rs.getString("category_name"),
                        rs.getString("category_description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public static CategoryDTO getCategoryById(Long categoryId) {
        try (Connection conn = Database.getConnection()) {
            String sqlQuery = "SELECT * FROM tb_categories WHERE category_id = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setLong(1, categoryId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new CategoryDTO(
                        rs.getLong("category_id"),
                        rs.getString("category_name"),
                        rs.getString("category_description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int createCategory(String categoryName, String description) {
        try (Connection conn = Database.getConnection()) {
            String sqlQuery = "INSERT INTO tb_categories (category_name, category_description) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setString(1, categoryName);
            ps.setString(2, description);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int updateCategory(Long categoryId, String categoryName, String description) {
        try (Connection conn = Database.getConnection()) {
            String sqlQuery = "UPDATE tb_categories SET category_name = ?, category_description = ? WHERE category_id = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setString(1, categoryName);
            ps.setString(2, description);
            ps.setLong(3, categoryId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int deleteCategory(Long categoryId) {
        try (Connection conn = Database.getConnection()) {
            String sqlQuery = "DELETE FROM tb_categories WHERE category_id = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setLong(1, categoryId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}

