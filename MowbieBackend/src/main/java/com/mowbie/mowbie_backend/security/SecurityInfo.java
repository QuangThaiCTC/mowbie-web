package com.mowbie.mowbie_backend.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecurityInfo {
    public static final String SECRET_KEY = "ThisIsASecretKey";

    public static String hashString(String str) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hashedBytes = mac.doFinal(str.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e){
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        String hashedInput = hashString(rawPassword);
        return hashedInput.equals(hashedPassword);
    }
}
