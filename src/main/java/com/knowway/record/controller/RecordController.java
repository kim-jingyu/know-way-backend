package com.knowway.record.controller;

import com.knowway.record.dto.RecordRequest;
import com.knowway.record.dto.RecordResponse;
import com.knowway.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 녹음 관련 컨트롤러
 *
 * @author 박유진
 * @since 2024.07.26
 * @version 1.0
 */
@RequiredArgsConstructor
@RequestMapping(value = "/records")
@RestController
public class RecordController {

    private final RecordService recordService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> recordAdd(
            @RequestPart("file") MultipartFile file,
            @RequestPart("record") RecordRequest recordRequest,
            @AuthenticationPrincipal Long memberId
    ) {
        recordService.addRecord(memberId, recordRequest, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("음성 파일이 정상적으로 업로드 되었습니다.");
    }

    @GetMapping(value = "/{departmentStoreId}/floors")
    public ResponseEntity<List<RecordResponse>> recordList(
            @PathVariable("departmentStoreId") Long departmentStoreId,
            @RequestParam("floor") Long departmentStoreFloorId) {
        return ResponseEntity.ok()
                .body(recordService.findSelectedRecord(departmentStoreId, departmentStoreFloorId));
    }
}
