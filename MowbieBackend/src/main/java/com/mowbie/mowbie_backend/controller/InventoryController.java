package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.dto.ResponseDTO;
import com.mowbie.mowbie_backend.model.Inventory;
import com.mowbie.mowbie_backend.repository.InventoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventories")
public class InventoryController {

    @GetMapping
    public ResponseEntity<?> getAllInventories() {
        List<Inventory> inventories = InventoryRepository.getAllInventories();
        if (inventories == null) {
            return ResponseEntity.status(404)
                    .body(ResponseDTO.error(404, "INVENTORIES_NOT_FOUND", "Không có dữ liệu!"));
        }
        return ResponseEntity.ok(ResponseDTO.success("Lấy tồn kho thành công", Map.of("inventories", inventories)));
    }

    @PostMapping
    public ResponseEntity<?> createInventory(@RequestParam Long productId, @RequestParam Long stockChange, @RequestParam String changeType) {
        int result = InventoryRepository.addInventory(productId, stockChange, changeType);
        if (result == 1) {
            String message = switch (changeType) {
                case "import" -> "Nhập hàng thành công! Số lượng: " + stockChange;
                case "order" -> "Xuất hàng thành công! Số lượng: " + stockChange;
                case "return" -> "Trả hàng thành công! Số lượng: " + stockChange;
                default -> "Cập nhật tồn kho thành công!";
            };
            return ResponseEntity.ok(ResponseDTO.success(message, null));
        }

        if (result == 2) {
            return ResponseEntity.status(400).body(ResponseDTO.error(400, "OUT_OF_STOCK", "Tồn kho không đủ để xuất hàng!"));
        }
        return ResponseEntity.status(400).body(ResponseDTO.error(400, "ADD_FAILED",
                "Thêm tồn kho không thành công! Kiểm tra thông tin nhập vào."));
    }
}
