package com.mowbie.mowbie_backend.repository;

import com.mowbie.mowbie_backend.config.Database;
import com.mowbie.mowbie_backend.dto.UserDTO;
import com.mowbie.mowbie_backend.security.Regex;
import com.mowbie.mowbie_backend.security.SecurityInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {
    public static int register(String username, String email, String phoneNumber, String password) {
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
            System.out.println("\n\n\n\n\n\n\n" + e.getMessage());
            return 2; // Lỗi hệ thống
        }
    }

    public static UserDTO login(String email, String password) {
        try (Connection conn = Database.getConnection()){
            String checkUserSql = "SELECT * FROM tb_users WHERE email = ?";
            PreparedStatement checkUserPs = conn.prepareStatement(checkUserSql);
            checkUserPs.setString(1, email);
            ResultSet rs = checkUserPs.executeQuery();
            if (rs.next() && SecurityInfo.verifyPassword(password, rs.getString("password"))){
                return new UserDTO(
                        rs.getLong("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("avatar_path"),
                        rs.getBoolean("is_active"),
                        rs.getString("user_role")
                );
            }
            return null;
        } catch (SQLException e) {
            System.out.println("\n\n\n\n\n\n\n" + e.getMessage());
            return null;
        }
    }

    public static UserDTO getUserByEmailOrId(String email, Long userId) {
        try (Connection conn = Database.getConnection()){
            String sqlQuery = "SELECT * FROM tb_users WHERE email = ? or user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            // Nếu email null, set NULL cho PreparedStatement
            if (email != null) {
                ps.setString(1, email);
            } else {
                ps.setNull(1, Types.VARCHAR);
            }

            // Nếu userId null, set NULL cho PreparedStatement
            if (userId != null) {
                ps.setLong(2, userId);
            } else {
                ps.setNull(2, Types.BIGINT);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new UserDTO(
                        rs.getLong("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("avatar_path"),
                        rs.getBoolean("is_active"),
                        rs.getString("user_role")
                );
            }
            return null;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n\n" + e.getMessage());
            return null;
        }
    }

    public static UserDTO updateProfile(Long userId, String username, String phoneNumber, String newPassword, String avatarPath) {
        Map<String, String> fields = new HashMap<>();

        if (username != null && !username.isEmpty()) fields.put("username", username);
        if (phoneNumber != null && !phoneNumber.isEmpty()) fields.put("phone_number", phoneNumber);
        if (newPassword != null && !newPassword.isEmpty()) fields.put("password", SecurityInfo.hashString(newPassword));
        if (avatarPath != null && !avatarPath.isEmpty()) fields.put("avatar_path", avatarPath);
        if (fields.isEmpty()) {
            System.out.println("Không có trường nào cần cập nhật!");
            return null;
        }

        StringBuilder sql = new StringBuilder("UPDATE tb_users SET ");
        int count = 0;
        for (String key : fields.keySet()) {
            sql.append(key).append(" = ?");
            count++;
            if (count < fields.size()) sql.append(", ");
        }
        sql.append(" WHERE user_id = ?");
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            int index = 1;
            for (String value : fields.values()) {
                ps.setString(index++, value);
                System.out.println(value);
            }
            ps.setLong(index, userId);

            ps.executeUpdate();
            String sqlQuery = "SELECT * FROM tb_users WHERE user_id = ?";
            ps = conn.prepareStatement(sqlQuery);
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new UserDTO(
                        rs.getLong("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("avatar_path"),
                        rs.getBoolean("is_active"),
                        rs.getString("user_role")
                );
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật user: " + e.getMessage());
            return null;
        }
    }


    public static int updateUserStatusById(Long userId, Boolean active) {
        try (Connection conn = Database.getConnection()){
            String sql = "UPDATE tb_users SET is_active = ? WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1, active);
            ps.setLong(2, userId);
            ps.executeUpdate();
            return active ? 1 : 0;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n" + e.getMessage());
            return 2;
        }
    }

    public static List<UserDTO> getAllUsers() {
        List<UserDTO> users = new ArrayList<>();
        try (Connection conn = Database.getConnection()){
            String sql = "SELECT * FROM tb_users";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                users.add(new UserDTO(
                        rs.getLong("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("avatar_path"),
                        rs.getBoolean("is_active"),
                        rs.getString("user_role")
                ));
            }
            return users;
        } catch (SQLException e){
            System.out.println("\n\n\n\n\n\n" + e.getMessage());
            return users;
        }
    }
}
