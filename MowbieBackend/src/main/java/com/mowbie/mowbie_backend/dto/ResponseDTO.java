package com.mowbie.mowbie_backend.dto;

import java.time.Instant;
import java.util.Map;

public class ResponseDTO<T> {
    private int status;
    private String message;
    private String error; // Chứa mã lỗi (vd: NOT_FOUND, BAD_REQUEST)
    private T data;
    private Instant timestamp; // Thời gian trả về response

    // Constructor private để bắt buộc dùng builder
    private ResponseDTO(int status, String message, String error, T data) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.data = data;
        this.timestamp = Instant.now(); // Tự động lấy timestamp hiện tại
    }

    // Trả về response thành công với message tùy chỉnh
    public static <T> ResponseDTO<T> success(String message, T data) {
        return new ResponseDTO<>(200, message, null, data);
    }

    // Trả về response lỗi có mã lỗi HTTP và mô tả lỗi
    public static ResponseDTO<Void> error(int status, String error, String message) {
        return new ResponseDTO<>(status, message, error, null);
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public String getError() { return error; }
    public T getData() { return data; }
    public Instant getTimestamp() { return timestamp; }
}
