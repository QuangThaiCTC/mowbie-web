package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.dto.PaymentDTO;
import com.mowbie.mowbie_backend.model.Payment;
import com.mowbie.mowbie_backend.repository.PaymentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @GetMapping
    public ResponseEntity<?> getAllPayments() {
        List<PaymentDTO> payments = PaymentRepository.getAllPayments();
        if (payments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "Không có giao dịch thanh toán nào!"));
        }
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getPayment(@PathVariable Long orderId) {
        PaymentDTO payment = PaymentRepository.getPaymentByOrderId(orderId);
        if (payment != null) {
            return ResponseEntity.ok(payment);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Không tìm thấy thanh toán cho đơn hàng này!"));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPayment(@RequestBody PaymentDTO payment) {
        int result = PaymentRepository.createPayment(payment);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Thêm thanh toán thành công!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Thêm thanh toán không thành công!"));
    }

    @PutMapping("/update/{paymentId}")
    public ResponseEntity<?> updatePayment(@PathVariable Long paymentId, @RequestParam String status) {
        int result = PaymentRepository.updatePaymentStatus(paymentId, status);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Cập nhật trạng thái thanh toán thành công!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Cập nhật trạng thái thanh toán không thành công!"));
    }

    @DeleteMapping("/delete/{paymentId}")
    public ResponseEntity<?> deletePayment(@PathVariable Long paymentId) {
        int result = PaymentRepository.deletePayment(paymentId);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Xóa thanh toán thành công!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Xóa thanh toán không thành công!"));
    }
}

