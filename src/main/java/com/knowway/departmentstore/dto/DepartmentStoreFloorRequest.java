package com.knowway.departmentstore.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DepartmentStoreFloorRequest {
    private String departmentStoreFloor;
    private MultipartFile image;
}
