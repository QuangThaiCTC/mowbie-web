package com.mowbie.mowbie_backend.repository;

import com.mowbie.mowbie_backend.config.Database;
import com.mowbie.mowbie_backend.dto.PaymentDTO;
import com.mowbie.mowbie_backend.model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {

    public static List<PaymentDTO> getAllPayments() {
        List<PaymentDTO> payments = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM tb_payments";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                payments.add(new PaymentDTO(
                        rs.getLong("payment_id"),
                        rs.getLong("order_id"),
                        rs.getBigDecimal("payment_amount"),
                        rs.getString("payment_method"),
                        rs.getString("payment_status"),
                        rs.getTimestamp("payment_time").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public static PaymentDTO getPaymentByOrderId(Long orderId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM tb_payments WHERE order_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new PaymentDTO(
                        rs.getLong("payment_id"),
                        rs.getLong("order_id"),
                        rs.getBigDecimal("payment_amount"),
                        rs.getString("payment_method"),
                        rs.getString("payment_status"),
                        rs.getTimestamp("payment_time").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int createPayment(PaymentDTO payment) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO tb_payments (order_id, payment_amount, payment_method, payment_status) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, payment.getOrderId());
            ps.setBigDecimal(2, payment.getPaymentAmount());
            ps.setString(3, payment.getPaymentMethod());
            ps.setString(4, payment.getPaymentStatus());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int updatePaymentStatus(Long paymentId, String status) {
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE tb_payments SET payment_status = ? WHERE payment_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setLong(2, paymentId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int deletePayment(Long paymentId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM tb_payments WHERE payment_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, paymentId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
