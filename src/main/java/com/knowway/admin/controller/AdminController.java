package com.knowway.admin.controller;

import com.knowway.admin.dto.AdminRecordDto;
import com.knowway.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/records")
    public ResponseEntity<List<AdminRecordDto>> getRecordsByFloorId(@RequestParam Integer departmentStoreFloorId) {
        return ResponseEntity.ok(adminService.getRecordsByFloorId(departmentStoreFloorId));
    }

    @PatchMapping("/records/{recordId}")
    public ResponseEntity<String> toggleRecordIsSelected(@PathVariable Long recordId) {
        boolean result = adminService.toggleRecordIsSelected(recordId);
        if (result) {
            return ResponseEntity.ok("success to select record");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to select record");
        }
    }
}
