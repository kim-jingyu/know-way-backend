package com.knowway.departmentstore.dto;

import com.knowway.departmentstore.entity.DepartmentStoreFloor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DepartmentStoreFloorMapResponse {
    private Long departmentStoreFloorId;
    private String departmentStoreFloorMapPath;

    public static DepartmentStoreFloorMapResponse from(DepartmentStoreFloor departmentStoreFloor) {
        return DepartmentStoreFloorMapResponse.builder()
                .departmentStoreFloorId(departmentStoreFloor.getDepartmentStoreFloorId())
                .departmentStoreFloorMapPath(departmentStoreFloor.getDepartmentStoreFloorMapPath())
                .build();
    }
}
