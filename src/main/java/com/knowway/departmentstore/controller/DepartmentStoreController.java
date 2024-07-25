package com.knowway.departmentstore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowway.departmentstore.dto.DepartmentStoreFloorMapResponse;
import com.knowway.departmentstore.dto.DepartmentStoreRequest;
import com.knowway.departmentstore.dto.DepartmentStoreResponse;
import com.knowway.departmentstore.service.DepartmentStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/depts")
@RequiredArgsConstructor
public class DepartmentStoreController {
    private final DepartmentStoreService service;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/make", consumes = {"multipart/form-data"})
    public ResponseEntity<Long> makeDepartmentStore(@RequestPart("departmentStoreRequest") String departmentStoreRequest,
                                                    @RequestPart("images") List<MultipartFile> images) throws JsonProcessingException {
        DepartmentStoreRequest request = objectMapper.readValue(departmentStoreRequest, DepartmentStoreRequest.class);

        if (request.getFloorData().size() != images.size()) {
            return ResponseEntity.badRequest().build();
        }

        for (int i = 0; i < request.getFloorData().size(); i++) {
            request.getFloorData().get(i).setImage(images.get(i));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.makeDepartmentStore(request));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<DepartmentStoreResponse>> getAll() {
        return ResponseEntity.ok()
                .body(service.getAll());
    }

    @GetMapping
    public ResponseEntity<Page<DepartmentStoreResponse>> getAllDepartmentStores(@RequestParam(value = "size", defaultValue = "5") Integer size, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        return ResponseEntity.ok()
                .body(service.getAllDepartmentStoreList(size, page));
    }

    @GetMapping(value = "/loc")
    public ResponseEntity<Page<DepartmentStoreResponse>> getDepartmentStoresByLocation(@RequestParam(value = "size", defaultValue = "5") Integer size,
                                                                                       @RequestParam(value = "size", defaultValue = "1") Integer page,
                                                                                       @RequestParam(value = "latitude") String latitude,
                                                                                       @RequestParam(value = "longtitude") String longtitude) {
        return ResponseEntity.ok()
                .body(service.getDepartmentStoreListByLocation(size, page, latitude, longtitude));
    }

    @GetMapping(value = "/floors/{departmentStoreFloorId}")
    public ResponseEntity<DepartmentStoreFloorMapResponse> getDepartmentStoreFloorMap(@PathVariable(value = "departmentStoreFloorId") Long departmentStoreFloorId) {
        return ResponseEntity.ok()
                .body(service.getDepartmentStoreFloorMap(departmentStoreFloorId));
    }

    @GetMapping(value = "/{departmentStoreId}")
    public ResponseEntity<List<Long>> getDepartmentStoreFloorList(@PathVariable(value = "departmentStoreId") Long departmentStoreId) {
        return ResponseEntity.ok()
                .body(service.getDepartmentStoreFloorList(departmentStoreId));
    }

    @GetMapping(value = "/branch")
    public ResponseEntity<DepartmentStoreResponse> getDepartmentStoreByBranch(@RequestParam(value = "departmentStoreBranch") String departmentStoreBranch) {
        return ResponseEntity.ok()
                .body(service.getDepartmentStoreByBranch(departmentStoreBranch));
    }
}
