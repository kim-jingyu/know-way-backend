package com.knowway.departmentstore.service;

import com.knowway.departmentstore.dto.DepartmentStoreFloorMapResponse;
import com.knowway.departmentstore.dto.DepartmentStoreRequest;
import com.knowway.departmentstore.dto.DepartmentStoreResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DepartmentStoreService {
    Long makeDepartmentStore(DepartmentStoreRequest request);
    Page<DepartmentStoreResponse> getAllDepartmentStoreList(Integer size, Integer page);
    List<DepartmentStoreResponse> getDepartmentStoreListByLocation(Double latitude, Double longitude);
    DepartmentStoreFloorMapResponse getDepartmentStoreFloorMap(Long deptId, String floor);
    List<DepartmentStoreFloorMapResponse> getDepartmentStoreFloorList(Long departmentStoreId);
    DepartmentStoreResponse getDepartmentStoreByBranch(String departmentStoreBranch);
    void removeDepartmentStore(Long departmentStoreId);
}
