package com.knowway.departmentstore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowway.departmentstore.dto.DepartmentStoreFloorMapResponse;
import com.knowway.departmentstore.dto.DepartmentStoreRequest;
import com.knowway.departmentstore.dto.DepartmentStoreResponse;
import com.knowway.departmentstore.service.DepartmentStoreServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 백화점 컨트롤러
 *
 * @author 김진규
 * @since 2024.07.25
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/depts")
@RequiredArgsConstructor
public class DepartmentStoreController {
    private final DepartmentStoreServiceImpl service;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Long> makeDepartmentStore(@RequestPart("departmentStoreRequest") String departmentStoreRequest,
                                                    @RequestPart("images") List<MultipartFile> images,
                                                    @AuthenticationPrincipal Long memberId) throws JsonProcessingException {
        DepartmentStoreRequest request = objectMapper.readValue(departmentStoreRequest, DepartmentStoreRequest.class);

        if (request.getFloorData().size() != images.size()) {
            return ResponseEntity.badRequest().build();
        }

        for (int i = 0; i < request.getFloorData().size(); i++) {
            request.getFloorData().get(i).setImage(images.get(i));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.makeDepartmentStore(request, memberId));
    }

    @GetMapping
    public ResponseEntity<Page<DepartmentStoreResponse>> getAllDepartmentStores(@RequestParam(value = "size", defaultValue = "5") Integer size, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        return ResponseEntity.ok()
                .body(service.getAllDepartmentStoreList(size, page));
    }

    @GetMapping(value = "/loc")
    public ResponseEntity<List<DepartmentStoreResponse>> getDepartmentStoresByLocation(@RequestParam(value = "latitude") Double latitude,
                                                                                       @RequestParam(value = "longitude") Double longitude) {
        return ResponseEntity.ok()
                .body(service.getDepartmentStoreListByLocation(latitude, longitude));
    }

    @GetMapping(value = "/{deptId}/floors")
    public ResponseEntity<DepartmentStoreFloorMapResponse> getDepartmentStoreFloorMap(
            @PathVariable(value = "deptId") Long deptId,
            @RequestParam(value = "floor") String floor) {
        return ResponseEntity.ok()
                .body(service.getDepartmentStoreFloorMap(deptId, floor));
    }

    @GetMapping(value = "/{deptId}")
    public ResponseEntity<List<DepartmentStoreFloorMapResponse>> getDepartmentStoreFloorList(@PathVariable(value = "deptId") Long departmentStoreId) {
        return ResponseEntity.ok()
                .body(service.getDepartmentStoreFloorList(departmentStoreId));
    }

    @GetMapping(value = "/branch")
    public ResponseEntity<List<DepartmentStoreResponse>> getDepartmentStoreByBranch(@RequestParam(value = "departmentStoreBranch") String departmentStoreBranch) {
        return ResponseEntity.ok()
                .body(service.getDepartmentStoreByBranch(departmentStoreBranch));
    }

    @DeleteMapping(value = "/{deptId}")
    public ResponseEntity<Void> removeDepartmentStore(@PathVariable(value = "deptId") Long departmentStoreId,
                                                      @AuthenticationPrincipal Long memberId) {
        service.removeDepartmentStore(departmentStoreId, memberId);
        return ResponseEntity.ok().build();
    }
}
