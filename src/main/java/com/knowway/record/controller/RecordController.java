package com.knowway.record.controller;

import com.knowway.record.dto.RecordRequest;
import com.knowway.record.service.RecordServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping(value = "/records")
@RestController
public class RecordController {

    private final RecordServiceImpl recordServiceImpl;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> recordAdd(
            @RequestPart("file") MultipartFile file,
            @RequestPart("record") RecordRequest recordRequest) {
        recordServiceImpl.addRecord(recordRequest, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("음성 파일이 정상적으로 업로드 되었습니다.");
    }
}
