package com.mowbie.mowbie_backend.repository;

import com.mowbie.mowbie_backend.config.Database;
import com.mowbie.mowbie_backend.model.User;
import com.mowbie.mowbie_backend.security.SecurityInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT user_id, username, email, phone_number, password, user_role FROM tb_users";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                users.add(new User(
                        rs.getLong("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("password"),
                        rs.getString("user_role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static Optional<User> getUserByIdOrEmail(Long userId, String email) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT user_id, username, email, phone_number, password, user_role FROM tb_users WHERE user_id = ? OR email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, userId);
            ps.setObject(2, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(new User(
                        rs.getLong("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("password"),
                        rs.getString("user_role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static int createUser(String username, String email, String phoneNumber, String password) {
        try (Connection conn = Database.getConnection()) {
            // Kiểm tra email tồn tại
            String checkUserSql = "SELECT 1 FROM tb_users WHERE email = ?";
            PreparedStatement checkUserPs = conn.prepareStatement(checkUserSql);
            checkUserPs.setString(1, email);
            ResultSet rs = checkUserPs.executeQuery();

            if (rs.next()) {
                rs.close();
                return 0; // Email đã tồn tại
            }
            rs.close();

            // Hash password trước khi lưu
            String hashPassword = SecurityInfo.hashString(password);

            // Tạo tài khoản mới
            String insertUserSql = "INSERT INTO tb_users (username, email, phone_number, password) VALUES (?, ?, ?, ?)";
            PreparedStatement insertUserPs = conn.prepareStatement(insertUserSql);
            insertUserPs.setString(1, username);
            insertUserPs.setString(2, email);
            insertUserPs.setString(3, phoneNumber);
            insertUserPs.setString(4, hashPassword);
            insertUserPs.executeUpdate();

            return 1; // Tạo tài khoản thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return 2; // Lỗi hệ thống
        }
    }

    public static int updateUser(Long userId, String username, String phoneNumber, String password) {
        try (Connection conn = Database.getConnection()) {
            // Tạo StringBuilder để xây dựng câu lệnh UPDATE động
            StringBuilder sqlBuilder = new StringBuilder("UPDATE tb_users SET ");
            List<Object> params = new ArrayList<>();

            if (username != null && !username.isEmpty()) {
                sqlBuilder.append("username = ?, ");
                params.add(username);
            }
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                sqlBuilder.append("phone_number = ?, ");
                params.add(phoneNumber);
            }
            if (password != null && !password.isEmpty()) {
                sqlBuilder.append("password = ?, ");
                params.add(SecurityInfo.hashString(password)); // Hash mật khẩu trước khi cập nhật
            }

            // Kiểm tra xem có dữ liệu để cập nhật không
            if (params.isEmpty()) {
                return 0; // Không có gì để cập nhật
            }

            // Xóa dấu ", " cuối cùng và thêm điều kiện WHERE
            sqlBuilder.setLength(sqlBuilder.length() - 2);
            sqlBuilder.append(" WHERE user_id = ?");
            params.add(userId);

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

    public static int deleteUser(Long userId) {
        try (Connection conn = Database.getConnection()) {
            String sqlQuery = "DELETE FROM tb_users WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setLong(1, userId);
            int affectedRows = ps.executeUpdate();

            return affectedRows > 0 ? 1 : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
