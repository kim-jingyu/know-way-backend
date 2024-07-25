package com.knowway.departmentstore.dto;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentStoreRequest {
    private String departmentStoreName;
    private String departmentStoreBranch;
    private String departmentStoreLatitude;
    private String departmentStoreLongtitude;
    private List<DepartmentStoreFloorRequest> floorData;
}
