package com.mowbie.mowbie_backend.repository;

import com.mowbie.mowbie_backend.config.Database;
import com.mowbie.mowbie_backend.dto.ProductDTO;
import com.mowbie.mowbie_backend.model.Product;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    public static List<ProductDTO> getAllProducts() {
        List<ProductDTO> products = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sqlQuery = "SELECT * FROM tb_products";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(new ProductDTO(
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getString("product_image_path"),
                        rs.getBigDecimal("product_price"),
                        rs.getLong("category_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static ProductDTO getProductById(Long productId) {
        try (Connection conn = Database.getConnection()) {
            String sqlQuery = "SELECT * FROM tb_products WHERE product_id = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setLong(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new ProductDTO(
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getString("product_image_path"),
                        rs.getBigDecimal("product_price"),
                        rs.getLong("category_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ProductDTO> getProductsByCategory(Long categoryId) {
        List<ProductDTO> products = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sqlQuery = "SELECT * FROM tb_products WHERE category_id = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setLong(1, categoryId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(new ProductDTO(
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getString("product_image_path"),
                        rs.getBigDecimal("product_price"),
                        rs.getLong("category_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static int createProduct(String productName, String description, String imagePath, BigDecimal price, Long categoryId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO tb_products (product_name, product_description, product_image_path, product_price, category_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, productName);
            ps.setString(2, description);
            ps.setString(3, imagePath);
            ps.setBigDecimal(4, price);
            ps.setLong(5, categoryId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int updateProduct(Long productId, String productName, String description, String imagePath, BigDecimal price, Long categoryId) {
        try (Connection conn = Database.getConnection()) {
            // Tạo StringBuilder để xây dựng câu lệnh UPDATE động
            StringBuilder sqlBuilder = new StringBuilder("UPDATE tb_products SET ");
            List<Object> params = new ArrayList<>();

            if (productName != null && !productName.isEmpty()) {
                sqlBuilder.append("product_name = ?, ");
                params.add(productName);
            }
            if (description != null && !description.isEmpty()) {
                sqlBuilder.append("product_description = ?, ");
                params.add(description);
            }
            if (imagePath != null && !imagePath.isEmpty()) {
                sqlBuilder.append("product_image_path = ?, ");
                params.add(imagePath);
            }
            if (price != null) {
                sqlBuilder.append("product_price = ?, ");
                params.add(price);
            }
            if (categoryId != null) {
                sqlBuilder.append("category_id = ?, ");
                params.add(categoryId);
            }

            // Kiểm tra xem có dữ liệu để cập nhật không
            if (params.isEmpty()) {
                return 0; // Không có gì để cập nhật
            }

            // Xóa dấu ", " cuối cùng và thêm điều kiện WHERE
            sqlBuilder.setLength(sqlBuilder.length() - 2);
            sqlBuilder.append(" WHERE product_id = ?");
            params.add(productId);

            // Chuẩn bị câu lệnh SQL
            PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString());

            // Gán giá trị vào câu lệnh SQL
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int deleteProduct(Long productId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM tb_products WHERE product_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, productId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getImagePathById(Long productId) {
        try (Connection conn = Database.getConnection()) {
            String sqlQuery = "SELECT product_image_path FROM tb_products WHERE product_id = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setLong(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("product_image_path");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

