package com.knowway.admin.controller;

import com.knowway.admin.dto.AdminRecordResponse;
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
    public ResponseEntity<List<AdminRecordResponse>> getRecordsByFloorId(@RequestParam Long departmentStoreFloorId,@RequestParam Long recordArea, @RequestParam Boolean recordIsSelected) {
        return ResponseEntity.ok(adminService.getRecordsByFloorId(departmentStoreFloorId, recordArea, recordIsSelected));
    }

    @PatchMapping("/records/{recordId}")
    public ResponseEntity<String> toggleRecordIsSelected(@PathVariable Long recordId) {
        adminService.toggleRecordIsSelected(recordId);
        return ResponseEntity.ok().body("녹음 선정 관련 처리 완료");
    }
}
