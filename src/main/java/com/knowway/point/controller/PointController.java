package com.knowway.point.controller;

import com.knowway.point.dto.PointRequest;
import com.knowway.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 포인트 관련 컨트롤러
 *
 * @author 이주현
 * @since 2024.08.04
 * @version 1.0
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class PointController {
    private final PointService pointService;

    @PostMapping("/points")
    public ResponseEntity<String> addPoint(@RequestBody PointRequest pointRequest) {
        pointService.addPoint(pointRequest);
        return ResponseEntity.ok().body("포인트 적립이 정상적으로 처리되었습니다.");
    }
}
