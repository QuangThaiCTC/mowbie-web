package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.model.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        return null;
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable("orderId") String orderId) {
        return null;
    }
    @GetMapping("/{userId}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable("userId") String userId) {
        return null;
    }
    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@RequestBody Order order) {
        return null;
    }
    @PutMapping("/update/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable("orderId") String orderId, @RequestBody Order order) {
        return null;
    }
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable("orderId") String orderId) {
        return null;
    }
}
