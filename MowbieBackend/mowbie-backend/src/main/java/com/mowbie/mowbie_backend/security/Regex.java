package com.mowbie.mowbie_backend.security;

import java.util.regex.Pattern;

public class Regex {
    private static final String EMAIL_REGEX = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b";
    private static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
    private static final String PHONE_REGEX = "^(0[3|5|7|8|9])+([0-9]{8})$";

    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return Pattern.matches(PHONE_REGEX, phoneNumber);
    }
}
