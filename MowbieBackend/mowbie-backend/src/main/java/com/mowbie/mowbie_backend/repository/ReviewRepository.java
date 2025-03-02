package com.mowbie.mowbie_backend.repository;

import com.mowbie.mowbie_backend.config.Database;
import com.mowbie.mowbie_backend.dto.ReviewDTO;
import com.mowbie.mowbie_backend.model.Review;

import java.awt.image.DataBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewRepository {

    public static List<ReviewDTO> getReviewsByProductId(Long productId) {
        List<ReviewDTO> reviews = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM tb_reviews WHERE product_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, productId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                reviews.add(new ReviewDTO(
                        rs.getLong("review_id"),
                        rs.getLong("user_id"),
                        rs.getLong("product_id"),
                        rs.getInt("review_rating"),
                        rs.getString("review_comment")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public static int addReview(ReviewDTO review) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO tb_reviews (user_id, product_id, review_rating, review_comment) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, review.getUserId());
            ps.setLong(2, review.getProductId());
            ps.setInt(3, review.getReviewRating());
            ps.setString(4, review.getReviewComment());

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int updateReview(Long reviewId, int rating, String comment) {
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE tb_reviews SET review_rating = ?, review_comment = ? WHERE review_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, rating);
            ps.setString(2, comment);
            ps.setLong(3, reviewId);

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int deleteReview(Long reviewId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM tb_reviews WHERE review_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, reviewId);

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
