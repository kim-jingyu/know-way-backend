package com.knowway.departmentstore.dto;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentStoreRequest {
    private String departmentStoreName;
    private String departmentStoreBranch;
    private Double departmentStoreLatitude;
    private Double departmentStoreLongitude;
    private List<DepartmentStoreFloorRequest> floorData;
}
