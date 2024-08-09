package com.knowway.departmentstore.dto;

import com.knowway.departmentstore.entity.DepartmentStoreFloor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentStoreFloorResponse {
    private Long departmentStoreFloorId;
    private String departmentStoreFloorMapPath;
    private String departmentStoreFloor;

    public static DepartmentStoreFloorResponse from(DepartmentStoreFloor departmentStoreFloor) {
        return DepartmentStoreFloorResponse.builder()
                .departmentStoreFloorId(departmentStoreFloor.getDepartmentStoreFloorId())
                .departmentStoreFloorMapPath(departmentStoreFloor.getDepartmentStoreFloorMapPath())
                .departmentStoreFloor(departmentStoreFloor.getDepartmentStoreFloor())
                .build();
    }
}
