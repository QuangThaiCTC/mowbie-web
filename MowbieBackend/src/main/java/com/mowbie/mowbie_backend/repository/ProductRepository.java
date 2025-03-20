package com.mowbie.mowbie_backend.repository;

import com.mowbie.mowbie_backend.config.Database;
import com.mowbie.mowbie_backend.dto.InventoryDTO;
import com.mowbie.mowbie_backend.dto.ProductDTO;
import com.mowbie.mowbie_backend.dto.ProductDetail;
import com.mowbie.mowbie_backend.dto.ProductSpecDTO;
import com.mowbie.mowbie_backend.model.Product;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public static int addProduct(String productName, String productDescription, BigDecimal productPrice, Long categoryId) {
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

    private static List<String> getProductImages(Long productId) {
        try (Connection conn = Database.getConnection()){
            List<String> productImages = new ArrayList<>();
            String sql = "SELECT product_image_path FROM tb_product_images WHERE product_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, productId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                productImages.add(rs.getString("product_image_path"));
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

