package com.mowbie.mowbie_backend.repository;

import com.mowbie.mowbie_backend.config.Database;
import com.mowbie.mowbie_backend.dto.*;
import com.mowbie.mowbie_backend.model.Product;
import com.mowbie.mowbie_backend.security.SecurityInfo;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRepository {

    public static List<ProductDTO> getAllProducts() {
        try (Connection conn = Database.getConnection()){
            String sql = "SELECT p.product_id, p.product_name, p.product_price, (SELECT pi.product_image_path FROM tb_product_images pi WHERE pi.product_id = p.product_id ORDER BY pi.product_image_id LIMIT 1) AS product_image FROM tb_products p";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            List<ProductDTO> products = new ArrayList<>();
            while (rs.next()) {
                products.add(new ProductDTO(
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getBigDecimal("product_price"),
                        rs.getString("product_image")
                ));
            }
            return products;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n\n" + e.getMessage());
        }
        return null;
    }

    public static int createProduct(String productName, String productDescription, BigDecimal productPrice, Long categoryId) {
        try (Connection conn = Database.getConnection()){
            String sql = "INSERT INTO tb_products (product_name, product_description, product_price, category_id) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, productName);
            ps.setString(2, productDescription);
            ps.setBigDecimal(3, productPrice);
            ps.setLong(4, categoryId);
            ps.executeUpdate();
            return 1;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n\n" + e.getMessage());
            return 0;
        }
    }

    public static Product updateProduct(Long productId, String productName, String productDescription, BigDecimal productPrice, Long categoryId) {
        Map<String, String> fields = new HashMap<>();

        if (productName != null && !productName.isEmpty()) fields.put("product_name", productName);
        if (productDescription != null && !productDescription.isEmpty()) fields.put("product_description", productDescription);
        if (productPrice != null && !productPrice.toString().isEmpty()) fields.put("product_price", productPrice.toString());
        if (categoryId != null && !categoryId.toString().isEmpty()) fields.put("category_id", categoryId.toString());
        if (fields.isEmpty()) {
            System.out.println("Không có trường nào cần cập nhật!");
            return null;
        }

        StringBuilder sql = new StringBuilder("UPDATE tb_products SET ");
        int count = 0;
        for (String key : fields.keySet()) {
            sql.append(key).append(" = ?");
            count++;
            if (count < fields.size()) sql.append(", ");
        }
        sql.append(" WHERE product_id = ?");

        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            int index = 1;
            for (String value : fields.values()) {
                ps.setString(index++, value);
                System.out.println(value);
            }
            ps.setLong(index, productId);

            ps.executeUpdate();
            String sqlQuery = "SELECT * FROM tb_products WHERE product_id = ?";
            ps = conn.prepareStatement(sqlQuery);
            ps.setLong(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getBigDecimal("product_price"),
                        rs.getLong("category_id")
                );
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            return null;
        }
    }

    public static ProductDetail getProductDetails(Long productId) {
        try (Connection conn = Database.getConnection()){
            String sql = "SELECT product_id, product_name, product_description, product_price FROM tb_products where product_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new ProductDetail(
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getBigDecimal("product_price"),
                        getProductImages(productId),
                        getProductSpecs(productId),
                        getProductStock(productId)
                );
            }
            return null;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n\n" + e.getMessage());
            return null;
        }
    }

    private static List<ProductImageDTO> getProductImages(Long productId) {
        try (Connection conn = Database.getConnection()){
            List<ProductImageDTO> productImages = new ArrayList<>();
            String sql = "SELECT product_image_id, product_image_path FROM tb_product_images WHERE product_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, productId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                productImages.add(new ProductImageDTO(
                        rs.getLong("product_image_id"),
                        rs.getString("product_image_path")
                ));
            }
            return productImages;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n\n" + e.getMessage());
            return null;
        }
    }

    private static List<ProductSpecDTO> getProductSpecs(Long productId) {
        try (Connection conn = Database.getConnection()){
            List<ProductSpecDTO> productSpecs = new ArrayList<>();
            String sql = "SELECT specs_title, specs_content FROM tb_product_specs WHERE product_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, productId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                productSpecs.add(new ProductSpecDTO(
                        rs.getString("specs_title"),
                        rs.getString("specs_content")
                ));
            }
            return productSpecs;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n\n" + e.getMessage());
            return null;
        }
    }

    private static Integer getProductStock(Long productId) {
        try (Connection conn = Database.getConnection()){
            String sql = "SELECT COALESCE(SUM(i.stock_change), 0) AS stockQuantity FROM tb_inventory i WHERE i.product_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt("stockQuantity");
            }
            return 0;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n\n" + e.getMessage());
            return 0;
        }
    }
}

