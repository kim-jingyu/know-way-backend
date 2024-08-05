package com.knowway.point.controller;

import com.knowway.point.dto.PointRequest;
import com.knowway.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/points")
public class PointController {
    private final PointService pointService;

    @PostMapping
    public ResponseEntity<String> addPoint(@RequestBody PointRequest pointRequest) {
        pointService.addPoint(pointRequest);
        return ResponseEntity.ok().body("포인트 적립이 정상적으로 처리되었습니다.");
    }
}
